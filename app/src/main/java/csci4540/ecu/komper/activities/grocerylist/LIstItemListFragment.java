package csci4540.ecu.komper.activities.grocerylist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by anil on 11/1/17.
 */

public class LIstItemListFragment extends Fragment {

    private static final String ARG_GROCERYLISTID = "csci4540.ecu.komper.activities.grocerylist.grocerylistId";
    private static final int REQUEST_CODE = 0;

    private RecyclerView itemListRecyclerView;
    private ItemAdapter itemAdapter;

    private UUID groceryListID;

    DateFormat dateformat = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
    NumberFormat numberFormat  = new DecimalFormat("##.##");

    public static LIstItemListFragment newInstance(UUID groceryListID){

        LIstItemListFragment fragment = new LIstItemListFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_GROCERYLISTID, groceryListID);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groceryListID = (UUID)getArguments().getSerializable(ARG_GROCERYLISTID);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_itemslist, container, false);
        itemListRecyclerView = (RecyclerView) view.findViewById(R.id.grocery_itemlist_recyclerview);
        itemListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateListUI();

        return view;
    }

    private void updateListUI() {
        List<Item> list = KomperBase.getKomperBase(getActivity()).getAllItems(groceryListID);

        if(itemAdapter == null){
            itemAdapter = new ItemAdapter(list);
            itemListRecyclerView.setAdapter(itemAdapter);
        }
        else{
            itemAdapter.setItems(list);
            itemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list_itemslist, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_item:
                Item myitem = new Item();
                KomperBase.getKomperBase(getActivity()).addItem(myitem, groceryListID);
                Intent intent = AddItemActivity.newIntent(getActivity(), myitem.getItemID(), groceryListID);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateListUI();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView mItemName;
        private TextView mBrandName;
        private TextView mExpiryDate;
        private TextView mQuantity;
        private TextView mPrice;
        private CardView mItemCardView;

        private Item mItem;

        public ItemViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_list_itemslist, parent, false));

            mItemCardView = (CardView) itemView.findViewById(R.id.cardview_list_itemlist);
            mItemName = (TextView) itemView.findViewById(R.id.item_name);
            mBrandName = (TextView) itemView.findViewById(R.id.item_brand_name);
            mExpiryDate = (TextView) itemView.findViewById(R.id.item_expiry_date);
            mQuantity = (TextView) itemView.findViewById(R.id.item_quantity);
            mPrice = (TextView) itemView.findViewById(R.id.item_price);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Item item){
            mItem = item;

            mItemName.setText(Html.fromHtml(getString(R.string.item_name, item.getItemName())));
            mBrandName.setText(getString(R.string.brand_name,item.getItemBrandName()));
            mExpiryDate.setText(getString(R.string.expiry_date, dateformat.format(item.getItemExpiryDate())));
            mQuantity.setText(getString(R.string.quantity, numberFormat.format(item.getItemQuantity())));
            mPrice.setText(getString(R.string.price, String.format("%.2f",item.getItemPrice())));
            // TODO : remove comment when we get price form store
            mPrice.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View view) {
            Intent intent = AddItemActivity.newIntent(getActivity(), mItem.getItemID(), groceryListID);
            startActivity(intent);
        }


        @Override
        public boolean onLongClick(View view) {
            PopupMenu popupMenu = new PopupMenu(getActivity(), itemView);
            popupMenu.getMenuInflater().inflate(R.menu.popup_itemlist, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch(item.getItemId()){
                        case R.id.delete_item:
                            KomperBase.getKomperBase(getActivity()).deleteItem(mItem.getItemID());
                            updateListUI();
                            return true;
                        case R.id.searchinstore_item:
                            Toast.makeText(getActivity(), "Search in Store", Toast.LENGTH_SHORT).show();
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
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder>{

        private List<Item> mItemList = new ArrayList<>();

        public ItemAdapter(List<Item> itemList){
            mItemList = itemList;
        }
        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new ItemViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            Item item = mItemList.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }

        public void setItems(List<Item> list){
            mItemList = list;
        }
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_CANCELED && data != null){
                groceryListID = (UUID)data.getSerializableExtra(LIstItemListActivity.EXTRA_GROCERYLISTID);
                List<Item> itemList = KomperBase.getKomperBase(getActivity()).getAllItems(groceryListID);
                updateListUI(itemList);
            }
        }
    }*/
}
