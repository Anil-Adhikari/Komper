package csci4540.ecu.komper.activities.stores;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

import csci4540.ecu.komper.R;
import csci4540.ecu.komper.activities.KomperBase;
import csci4540.ecu.komper.database.KomperDbSchema;
import csci4540.ecu.komper.datamodel.Item;
import csci4540.ecu.komper.datamodel.Price;
import csci4540.ecu.komper.datamodel.Store;
import csci4540.ecu.komper.utilities.WalmartRestClient;
import cz.msebera.android.httpclient.Header;

/**
 * Created by anil on 11/24/17.
 */

public class StoreFragment extends Fragment {

    private static final String ARG_GROCERYLISTID = "csci4540.ecu.komper.activities.storefragment.grocerylistid";
    private static final String WALMART = "Walmart Supercenter";
    private static final String KOMPER = "Komper Store";
    private static final String TAG = "ListGroceryListFragment";

    private RecyclerView mStoreRecyclerView;
    private StoreAdapter mStoreAdapter;
    private Button searchButton;

    private UUID mGroceryListID;
    private Price mPrice;

    private ProgressDialog progress;

    public static StoreFragment newInstance(UUID grocerylistID) {
        StoreFragment fragment = new StoreFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_GROCERYLISTID, grocerylistID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroceryListID = (UUID)getArguments().getSerializable(ARG_GROCERYLISTID);
        progress = new ProgressDialog(getActivity());
        progress.setTitle("Fetching Item Information");
        progress.setMessage("Fetching item information.....");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        mStoreRecyclerView = (RecyclerView) view.findViewById(R.id.store_recycler_view);
        mStoreRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        searchButton = (Button) view.findViewById(R.id.search_instore_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = false;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.show();
                    }
                });
                List<Store> selectedStores = KomperBase.getKomperBase(getActivity()).getSelectedStore();
                if(selectedStores.size() != 0){
                    for(Store store : selectedStores){
                        result = searchInStore(store);
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Please select stores", Toast.LENGTH_SHORT).show();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                        }
                    });
                }
                if(result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                        }
                    });
                    showResult();
                }
            }
        });

        return view;

    }

    private boolean searchInStore(final Store store) {
        if(store.getStoreName().equals(WALMART)) {
            List<Item> items = KomperBase.getKomperBase(getActivity()).getAllItems(mGroceryListID);
            if (items.size() > 0) {
                for (final Item groceryItem : items) {
                    WalmartRestClient.query(
                            getActivity(),                          // Context
                            groceryItem.getItemName(),              // Item name to search
                            null, new JsonHttpResponseHandler() {   // Handler for search response
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    try {
                                        JSONArray itemsList = (JSONArray) response.get("items");
                                        JSONObject item = (JSONObject) itemsList.get(0);
                                        mPrice = createPrice(String.valueOf(item.getDouble("salePrice"))
                                                , groceryItem.getItemID(), store.getStoreId());
                                        if (KomperBase.getKomperBase(getActivity()).getPrice(mGroceryListID, store.getStoreId(), groceryItem.getItemID()) == null) {
                                            KomperBase.getKomperBase(getActivity()).addPrice(mPrice);
                                        } else {
                                            Price oldprice = KomperBase.getKomperBase(getActivity()).getPrice(mGroceryListID, store.getStoreId(), groceryItem.getItemID());
                                            KomperBase.getKomperBase(getActivity()).updatePrice(mPrice, oldprice.getPriceId());
                                        }

                                        Toast.makeText(getActivity(),
                                                item.getString("name") + " " + String.valueOf(item.getDouble("salePrice")),
                                                Toast.LENGTH_SHORT).show();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                    // NOTE: Seems necessary to satisfy loopj.
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                    throwable.printStackTrace();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    Log.d(TAG, responseString);
                                }
                            });
                    mPrice = null;
                }
                return true;
            } else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                    }
                });
                Toast.makeText(getActivity(), "Grocery list does not have any item", Toast.LENGTH_SHORT).show();
            }
        }else if (store.getStoreName().equals(KOMPER)) {
            Toast.makeText(getActivity(), "Store: " + KOMPER, Toast.LENGTH_SHORT).show();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress.dismiss();
                }
            });
            return true;
        }
        return false;
    }

    private void showResult() {
        Intent intent = PriceResultActivity.newIntent(getActivity(), mGroceryListID);
        startActivity(intent);
    }

    private void updateUI() {
        List<Store> stores = KomperBase.getKomperBase(getActivity()).getStores();
        mStoreAdapter = new StoreAdapter(stores);
        mStoreRecyclerView.setAdapter(mStoreAdapter);
    }

    private Price createPrice(String itemprice, UUID itemid, UUID storeId){
        Price price = new Price();
        price.setGrocerylistId(mGroceryListID);
        price.setStoreId(storeId);
        price.setItemId(itemid);
        price.setPrice(itemprice);
        return price;
    }


    private class StoreViewHolder extends RecyclerView.ViewHolder{
        private TextView mStoreName;
        private TextView mStoreAddress;
        private CheckBox mCheckbox;

        private Store mStore;

        public StoreViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_list_store, parent, false));
            mStoreName = (TextView) itemView.findViewById(R.id.store_name);
            mStoreAddress = (TextView) itemView.findViewById(R.id.store_address);
            mCheckbox = (CheckBox) itemView.findViewById(R.id.checkbox_store);
            mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(mCheckbox.isChecked()){
                        mStore.setSelected("yes");
                    }else{
                        mStore.setSelected("no");
                    }
                    KomperBase.getKomperBase(getActivity()).updateStore(mStore);
                }
            });
        }

        public void bind(Store store) {
            mStore = store;
            mStoreName.setText(mStore.getStoreName());
            mStoreAddress.setText(mStore.getStoreaddress());
            mCheckbox.setChecked(mStore.getSelected().equals("yes"));
        }
    }

    private class StoreAdapter extends RecyclerView.Adapter<StoreViewHolder>{

        private final List<Store> mStores;

        public StoreAdapter(List<Store> stores){
            mStores = stores;
        }

        @Override
        public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new StoreViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(StoreViewHolder holder, int position) {
            Store store = mStores.get(position);
            holder.bind(store);
        }

        @Override
        public int getItemCount() {
            return mStores.size();
        }
    }


}