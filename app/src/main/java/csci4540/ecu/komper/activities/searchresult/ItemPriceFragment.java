package csci4540.ecu.komper.activities.searchresult;

import android.content.ClipData;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import csci4540.ecu.komper.R;
import csci4540.ecu.komper.activities.KomperBase;
import csci4540.ecu.komper.database.KomperDbSchema;
import csci4540.ecu.komper.database.KomperSQLiteHelper;
import csci4540.ecu.komper.datamodel.Item;
import csci4540.ecu.komper.datamodel.Price;
import csci4540.ecu.komper.datamodel.Store;

/**
 * Created by anil on 11/25/17.
 */

public class ItemPriceFragment extends Fragment {

    private static final String ARG_GROCERYLISTID = "csci4540.ecu.komper.activities.searchresult.grocerylistid";
    private static final String ARG_STOREID = "csci4540.ecu.komper.activities.searchresult.storeid";

    private RecyclerView mItemPriceRecyclerview;
    private ItemPriceAdapter mAdapter;

    private TextView mStoreName;

    private Store mStore;
    private UUID mGroceryListID;

    DateFormat dateformat = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
    NumberFormat numberFormat  = new DecimalFormat("##.##");

    public static ItemPriceFragment newInstance(UUID grocerylistid, UUID storeid) {
        ItemPriceFragment fragment = new ItemPriceFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_GROCERYLISTID, grocerylistid);
        bundle.putSerializable(ARG_STOREID, storeid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID storeID = (UUID)getArguments().getSerializable(ARG_STOREID);
        mGroceryListID = (UUID)getArguments().getSerializable(ARG_GROCERYLISTID);
        mStore = KomperBase.getKomperBase(getActivity()).getStore(storeID);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list_pricelist_item, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pricelist_item, container, false);

        mItemPriceRecyclerview = (RecyclerView) view.findViewById(R.id.price_itemlist_recyclerview);
        mItemPriceRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        mStoreName = (TextView) view.findViewById(R.id.price_item_storename);
        mStoreName.setText(mStore.getStoreName());

        UpdateUI();

        return view;
    }

    private void UpdateUI() {
        List<Item> items = KomperBase.getKomperBase(getActivity()).getAllItems(mGroceryListID);
        mAdapter = new ItemPriceAdapter(items);
        mItemPriceRecyclerview.setAdapter(mAdapter);
    }

    private class ItemPriceViewHolder extends RecyclerView.ViewHolder{

        private TextView mItemName;
        private TextView mBrandName;
        private TextView mExpiryDate;
        private TextView mQuantity;
        private TextView mPrice;

        public ItemPriceViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_list_pricelist_item, parent, false));

            mItemName = (TextView) itemView.findViewById(R.id.price_item_name);
            mBrandName = (TextView) itemView.findViewById(R.id.price_item_brand_name);
            mExpiryDate = (TextView) itemView.findViewById(R.id.price_item_expiry_date);
            mQuantity = (TextView) itemView.findViewById(R.id.price_item_quantity);
            mPrice = (TextView) itemView.findViewById(R.id.price_item_price);

        }

        public void bind(Item item){

            String price = KomperBase.getKomperBase(getActivity()).getPrice(mGroceryListID, mStore.getStoreId(), item.getItemID()).getPrice();

            mItemName.setText(getString(R.string.item_name, item.getItemName()));
            mBrandName.setText(getString(R.string.brand_name,item.getItemBrandName()));
            mExpiryDate.setText(getString(R.string.expiry_date, dateformat.format(item.getItemExpiryDate())));
            mQuantity.setText(getString(R.string.quantity, numberFormat.format(item.getItemQuantity())));
            mPrice.setText(getString(R.string.price,price));

        }
    }

    private class ItemPriceAdapter extends RecyclerView.Adapter<ItemPriceViewHolder>{

        private final List<Item> mItems;

        public ItemPriceAdapter(List<Item> items){
            mItems = items;
        }

        @Override
        public ItemPriceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new ItemPriceViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(ItemPriceViewHolder holder, int position) {
            Item item = mItems.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }
}
