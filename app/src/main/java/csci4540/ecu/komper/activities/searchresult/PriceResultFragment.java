package csci4540.ecu.komper.activities.searchresult;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import csci4540.ecu.komper.R;
import csci4540.ecu.komper.activities.KomperBase;
import csci4540.ecu.komper.datamodel.Store;

/**
 * Created by anil on 11/25/17.
 */

public class PriceResultFragment extends Fragment{

    private static final String ARG_GROCERYLISTID = "csci4540.ecu.komper.activities.stores.grocerylistid";

    private UUID mGroceryListID;
    private List<Store> selectedStores;
    private UUID mPriceID;


    private RecyclerView mPriceRecyclerView;
    private PriceViewAdapter mPriceViewAdapter;

    DateFormat dateformat = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);

    public static PriceResultFragment newInstance(UUID grocerylistid) {
        PriceResultFragment fragment = new PriceResultFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_GROCERYLISTID, grocerylistid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroceryListID = (UUID) getArguments().getSerializable(ARG_GROCERYLISTID);
        selectedStores = KomperBase.getKomperBase(getActivity()).getSelectedStore();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_price, container,false);

        mPriceRecyclerView = (RecyclerView) view.findViewById(R.id.price_recycler_view);
        mPriceRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
            mPriceViewAdapter = new PriceViewAdapter(selectedStores);
            mPriceRecyclerView.setAdapter(mPriceViewAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private class PriceViewHolder extends RecyclerView.ViewHolder{

        private Store mStore;
        private TextView mPriceGLLabel;
        private TextView mPriceGLDate;
        private TextView mPriceGLPrice;
        private TextView mPriceStore;
        private TextView mPriceTotalItems;

        public PriceViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_list_result_price, parent, false));

            mPriceGLLabel = (TextView)itemView.findViewById(R.id.price_gcl_label);
            mPriceGLDate = (TextView)itemView.findViewById(R.id.price_gcl_date);
            mPriceTotalItems = (TextView)itemView.findViewById(R.id.price_gcl_totalnumberofItems);
            mPriceGLPrice = (TextView)itemView.findViewById(R.id.price_gcl_totalprice);
            mPriceStore = (TextView)itemView.findViewById(R.id.price_storename);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = ItemPriceActivity.newIntent(getActivity(), mGroceryListID, mStore.getStoreId());
                    startActivity(intent);
                }
            });

        }

        public void bind(Store store){
            mStore = store;
            mPriceGLLabel.setText(getString(R.string.grocery_label,KomperBase.getKomperBase(getActivity()).getGroceryList(mGroceryListID).getLabel()));
            mPriceGLDate.setText(getString(R.string.date_created, dateformat.format(KomperBase.getKomperBase(getActivity()).getGroceryList(mGroceryListID).getDate())));
            mPriceTotalItems.setText(getString(R.string.item_number, String.valueOf(KomperBase.getKomperBase(getActivity()).getNumberOfItems(mGroceryListID))));
            mPriceGLPrice.setText(getString(R.string.total_price, String.valueOf(KomperBase.getKomperBase(getActivity()).getTotalPrice(mGroceryListID,mStore.getStoreId()))));
            mPriceStore.setText(getString(R.string.store_name, KomperBase.getKomperBase(getActivity()).getStore(mStore.getStoreId()).getStoreName()));
        }
    }

    private class PriceViewAdapter extends RecyclerView.Adapter<PriceViewHolder>{

        List<Store> mStores = new ArrayList<>();

        public PriceViewAdapter(List<Store> stores){mStores = stores;}

        @Override
        public PriceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new PriceViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(PriceViewHolder holder, int position) {
            Store store = mStores.get(position);
            holder.bind(store);
        }

        @Override
        public int getItemCount() {
            return mStores.size();
        }
    }
}
