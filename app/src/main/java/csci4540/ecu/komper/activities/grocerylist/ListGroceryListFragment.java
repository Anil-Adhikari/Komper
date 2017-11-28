package csci4540.ecu.komper.activities.grocerylist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import csci4540.ecu.komper.R;
import csci4540.ecu.komper.activities.KomperBase;
import csci4540.ecu.komper.activities.searchresult.StoreActivity;
import csci4540.ecu.komper.datamodel.GroceryList;

public class ListGroceryListFragment extends Fragment {

    private RecyclerView mGLRecyclerView;
    private GroceryListAdapter mGLAdapter;

    DateFormat dateformat = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
    NumberFormat numberFormat  = new DecimalFormat("##.##");

    private static final String ARG_GOOGLE_CLIENT = "google_client";
    private static final String TAG = "ListGroceryListFragment";

    private static final String DIALOG_IMAGE = "Receipt Image";

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

        View view = inflater.inflate(R.layout.fragment_view_grocerylist, container, false);

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
        private ImageView mReceiptImage;

        public GroceryListViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_list_grocerylist, parent, false));

            mGLLabel = (TextView) itemView.findViewById(R.id.gcl_label);
            mGLDate = (TextView) itemView.findViewById((R.id.gcl_date));
            mGLPrice = (TextView) itemView.findViewById(R.id.gcl_price);
            mTotalItems = (TextView) itemView.findViewById(R.id.gcl_totalnumberofItems);
            mReceiptImage = (ImageView) itemView.findViewById(R.id.grocery_list_receipt_image);

            mReceiptImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager manager = getFragmentManager();
                    DialogFragment imageDialog = ImageViewFragment.newInstance(mGroceryList.getID());
                    imageDialog.show(manager, DIALOG_IMAGE);
                }
            });

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
                                        Intent intent2 = StoreActivity.newIntent(getActivity(), mGroceryList.getID());
                                        startActivity(intent2);

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
            if(mGroceryList.getChecked().equals("no")) {
                mGLPrice.setTextColor(Color.parseColor("#B82837"));
                mReceiptImage.setVisibility(View.GONE);
            }else{
                mGLPrice.setTextColor(Color.parseColor("#4FAF49"));
                File image = KomperBase.getKomperBase(getActivity()).getLatestModifiedFile(mGroceryList.getID());
                if(image.exists()){
                    Glide.with(mReceiptImage.getContext()).load(image).fitCenter().into(mReceiptImage);
                }else{
                    mReceiptImage.setVisibility(View.GONE);
                }
            }
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
