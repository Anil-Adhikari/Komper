package csci4540.ecu.komper.activities.creategrocerylist;

import android.content.Intent;
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
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import csci4540.ecu.komper.R;
import csci4540.ecu.komper.activities.KomperBase;
import csci4540.ecu.komper.datamodel.GroceryList;

public class CreateGroceryListFragment extends Fragment {

    private RecyclerView mGLRecyclerView;
    private GroceryListAdapter mGLAdapter;

    DateFormat dateformat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);

    /*public static CreateGroceryListFragment newInstance(){
        return new CreateGroceryListFragment();
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
                Intent intent = AddGroceryListActivity.newIntent(getActivity(), groceryList.getID());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_grocery_list, container, false);

        mGLRecyclerView = (RecyclerView) view.findViewById(R.id.grocery_recycler_view);
        mGLRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        KomperBase base = KomperBase.getKomperBase(getActivity());

        List<GroceryList> list = base.getGorceryLists();
        upDateGroceryListUI(list);

        return view;
    }

    private void upDateGroceryListUI(List<GroceryList> groceryLists) {

        if (mGLAdapter == null) {
            mGLAdapter = new GroceryListAdapter(groceryLists);
            mGLRecyclerView.setAdapter(mGLAdapter);
        } else {
            mGLAdapter.setCrimes(groceryLists);
            mGLAdapter.notifyDataSetChanged();
        }
        mGLAdapter = new GroceryListAdapter(groceryLists);
        mGLRecyclerView.setAdapter(mGLAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        List<GroceryList> list = KomperBase.getKomperBase(getActivity()).getGorceryLists();
        upDateGroceryListUI(list);
    }

    private class GroceryListViewHolder extends RecyclerView.ViewHolder{

        private GroceryList mGroceryList;
        private TextView mGLLabel;
        private TextView mGLDate;
        private TextView mGLPrice;

        public GroceryListViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_list_grocery_list, parent, false));

            mGLLabel = (TextView) itemView.findViewById(R.id.gcl_label);
            mGLDate = (TextView) itemView.findViewById((R.id.gcl_date));
            mGLPrice = (TextView) itemView.findViewById(R.id.gcl_price);
        }

        public void bind(GroceryList list){
            mGroceryList = list;
            mGLLabel.setText(mGroceryList.getLabel());
            mGLDate.setText(dateformat.format(mGroceryList.getDate()));
            mGLPrice.setText(String.format("%.2f",mGroceryList.getTotalPrice()));
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
            holder.bind(groceryList);

        }

        @Override
        public int getItemCount() {
            return groceryLists.size();
        }

        public void setCrimes(List<GroceryList> groceryLists) {
            this.groceryLists = groceryLists;
        }
    }

}
