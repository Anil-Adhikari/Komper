package csci4540.ecu.komper.activities.grocerylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import csci4540.ecu.komper.R;
import csci4540.ecu.komper.activities.KomperBase;
import csci4540.ecu.komper.datamodel.GroceryList;
import csci4540.ecu.komper.datamodel.Item;
import csci4540.ecu.komper.utilities.WalmartRestClient;
import csci4540.ecu.komper.utilities.WalmartRestClientHelper;
import cz.msebera.android.httpclient.Header;

public class ListGroceryListFragment extends Fragment {

    private RecyclerView mGLRecyclerView;
    private GroceryListAdapter mGLAdapter;

    DateFormat dateformat = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
    NumberFormat numberFormat  = new DecimalFormat("##.##");

    private static final String ARG_GOOGLE_CLIENT = "google_client";
    private static final String TAG = "ListGroceryListFragment";

    private GoogleApiClient mGoogleClient;


    /*public static ListGroceryListFragment newInstance(){
        return new ListGroceryListFragment();
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_create_grocery_list, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add_grocery_list:
                GroceryList groceryList = new GroceryList();
                KomperBase.getKomperBase(getActivity()).addGroceryList(groceryList);
                Intent intent = csci4540.ecu.komper.activities.grocerylist.AddGroceryListActivity.newIntent(getActivity(), groceryList.getID());
                startActivity(intent);
                return true;
            /*case R.id.signout:
                signOut();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        getActivity().finish();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_grocerylist, container, false);

        //mGoogleClient = (GoogleApiClient) getArguments().getSerializable(ARG_GOOGLE_CLIENT);

        mGLRecyclerView = (RecyclerView) view.findViewById(R.id.grocery_recycler_view);
        mGLRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGLRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));
        mGLRecyclerView.setHasFixedSize(true);

        KomperBase base = KomperBase.getKomperBase(getActivity());

        List<GroceryList> list = base.getGroceryLists();
        upDateGroceryListUI(list);

        return view;
    }

    private void upDateGroceryListUI(List<GroceryList> groceryLists) {

        if (mGLAdapter == null) {
            mGLAdapter = new GroceryListAdapter(groceryLists);
            mGLRecyclerView.setAdapter(mGLAdapter);
        } else {
            mGLAdapter.setGroceryLists(groceryLists);
            mGLAdapter.notifyDataSetChanged();
        }
        /*mGLAdapter = new GroceryListAdapter(groceryLists);
        mGLRecyclerView.setAdapter(mGLAdapter);*/

    }

    @Override
    public void onResume() {
        super.onResume();

        List<GroceryList> list = KomperBase.getKomperBase(getActivity()).getGroceryLists();
        upDateGroceryListUI(list);
    }

    public static ListGroceryListFragment newInstance() {
        ListGroceryListFragment fragment = new ListGroceryListFragment();
        /*Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_GOOGLE_CLIENT, serializableExtra);
        fragment.setArguments(bundle);*/
        return fragment;
    }

    private class GroceryListViewHolder extends RecyclerView.ViewHolder{

        private GroceryList mGroceryList;
        private TextView mGLLabel;
        private TextView mGLDate;
        private TextView mGLPrice;
        private TextView mTotalItems;
        private CardView mCardView;

        public GroceryListViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_list_grocerylist, parent, false));

            mCardView = (CardView) itemView.findViewById(R.id.cardview_list_grocerylist);
            mGLLabel = (TextView) itemView.findViewById(R.id.gcl_label);
            mGLDate = (TextView) itemView.findViewById((R.id.gcl_date));
            mGLPrice = (TextView) itemView.findViewById(R.id.gcl_price);
            //mListView = (ListView) itemView.findViewById(R.id.grocerylist_list_view);
            mTotalItems = (TextView) itemView.findViewById(R.id.gcl_totalnumberofItems);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = LIstItemListActivity.newIntent(getActivity(), mGroceryList.getID());
                    startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(getActivity(), itemView);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_grocerylist, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch(item.getItemId()){
                                case R.id.edit_grocerylist:
                                    //Toast.makeText(getActivity(), "Edit GroceryList", Toast.LENGTH_SHORT).show();
                                    Intent intent = AddGroceryListActivity.newIntent(getActivity(), mGroceryList.getID());
                                    startActivity(intent);
                                    return  true;
                                case R.id.delete_grocerylist:
                                    KomperBase.getKomperBase(getActivity()).deleteGroceryList(mGroceryList.getID());
                                    KomperBase.getKomperBase(getActivity()).deleteItemFromGroceryList(mGroceryList.getID());
                                    List<GroceryList> list = KomperBase.getKomperBase(getActivity()).getGroceryLists();
                                    upDateGroceryListUI(list);
                                    return true;
                                case R.id.searchinstore_grocerylist:
                                    List<Item> items = KomperBase.getKomperBase(getActivity()).getAllItems(mGroceryList.getID());
                                    for (Item groceryItem : items) {
                                        WalmartRestClient.query(
                                                getActivity(),                          // Context
                                                groceryItem.getItemName(),              // Item name to search
                                                null, new JsonHttpResponseHandler() {   // Handler for search response
                                                    @Override
                                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                        try {
                                                            JSONArray itemsList = (JSONArray) response.get("items");
                                                            JSONObject item = (JSONObject) itemsList.get(0);
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
                                    }
                                    return true;
                                default:
                                    return true;
                            }
                        }
                    });
                    popupMenu.show();
                    popupMenu.setGravity(Gravity.CENTER);
                    return true;
                }
            });
        }

        public void bind(GroceryList list, int numberOfItems){
            mGroceryList = list;

            mGLLabel.setText(getString(R.string.grocery_label, mGroceryList.getLabel()));

            mGLLabel.setText(getString(R.string.grocery_label, mGroceryList.getLabel()));
            mGLDate.setText(getString(R.string.date_created, dateformat.format(mGroceryList.getDate())));
            mGLPrice.setText(getString(R.string.total_price, String.format("%.2f",mGroceryList.getTotalPrice())));
            // TODO: remove gone when total price is available
            mGLPrice.setVisibility(View.GONE);
            mTotalItems.setText(getString(R.string.item_number, String.valueOf(numberOfItems)));
        }
    }
    private class GroceryListAdapter extends RecyclerView.Adapter<GroceryListViewHolder>{

        private List<GroceryList> groceryLists;

        public GroceryListAdapter(List<GroceryList> list){groceryLists = list;}

        @Override
        public GroceryListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new GroceryListViewHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(GroceryListViewHolder holder, int position) {
            GroceryList groceryList = groceryLists.get(position);
            int numberOfItems = KomperBase.getKomperBase(getActivity()).getNumberOfItems(groceryList.getID());
            holder.bind(groceryList, numberOfItems);

        }

        @Override
        public int getItemCount() {
            return groceryLists.size();
        }

        public void setGroceryLists(List<GroceryList> groceryLists) {
            this.groceryLists = groceryLists;
        }
    }



}
