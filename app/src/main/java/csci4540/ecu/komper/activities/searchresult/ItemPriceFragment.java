package csci4540.ecu.komper.activities.searchresult;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import csci4540.ecu.komper.R;
import csci4540.ecu.komper.activities.KomperBase;
import csci4540.ecu.komper.datamodel.GroceryList;
import csci4540.ecu.komper.datamodel.Item;
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
    private List<Item> mCheckedItems;

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
        mCheckedItems = new ArrayList<>();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list_pricelist_item, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.checkout:
                if(mCheckedItems.size() == 0){
                    Toast.makeText(getActivity(), "Please select items to checkout", Toast.LENGTH_SHORT).show();
                }else{
                    GroceryList mylist = KomperBase.getKomperBase(getActivity()).getGroceryList(mGroceryListID);
                    mylist.setChecked("yes");
                    mylist.setTotalPrice(KomperBase.getKomperBase(getActivity()).getTotalCheckedPrice(mGroceryListID, mStore.getStoreId()));
                    KomperBase.getKomperBase(getActivity()).updateGroceryList(mylist);

                    Intent intent = CheckoutActivity.newIntent(getActivity(), mGroceryListID, mStore.getStoreId());
                    startActivity(intent);
                }
                return true;
            case R.id.openmap:
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f",Double.parseDouble(mStore.getLatitude()) , Double.parseDouble(mStore.getLongitude()));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        private CheckBox mCheckbox;

        private Item mItem;
        private String price;

        public ItemPriceViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_list_pricelist_item, parent, false));

            mItemName = (TextView) itemView.findViewById(R.id.price_item_name);
            mBrandName = (TextView) itemView.findViewById(R.id.price_item_brand_name);
            mExpiryDate = (TextView) itemView.findViewById(R.id.price_item_expiry_date);
            mQuantity = (TextView) itemView.findViewById(R.id.price_item_quantity);
            mPrice = (TextView) itemView.findViewById(R.id.price_item_price);
            mCheckbox = (CheckBox) itemView.findViewById(R.id.price_checkout);
            mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(mCheckbox.isChecked()){
                        mItem.setChecked("yes");
                        mCheckedItems.add(mItem);
                        mItem.setItemPrice(Double.parseDouble(price));
                    }else{
                        mItem.setChecked("no");
                        mCheckedItems.remove(mItem);
                        mItem.setItemPrice(0.0);
                    }
                    KomperBase.getKomperBase(getActivity()).updateItem(mItem, mGroceryListID);
                }
            });

        }

        public void bind(Item item){
            mItem = item;

            price = KomperBase.getKomperBase(getActivity()).getPrice(mGroceryListID, mStore.getStoreId(), item.getItemID()).getPrice();
            double realprice = Double.parseDouble(price);
            mItemName.setText(getString(R.string.item_name, item.getItemName()));
            mBrandName.setText(getString(R.string.brand_name,item.getItemBrandName()));
            mExpiryDate.setText(getString(R.string.expiry_date, dateformat.format(item.getItemExpiryDate())));
            mQuantity.setText(getString(R.string.quantity, numberFormat.format(item.getItemQuantity())));
            mPrice.setText(getString(R.string.total_price,String.valueOf(realprice * item.getItemQuantity())));
            mCheckbox.setChecked(mItem.getChecked().equals("yes"));

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
