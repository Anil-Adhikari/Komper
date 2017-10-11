package csci4540.ecu.komper.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import csci4540.ecu.komper.R;

public class EnterItemFragment extends Fragment {

    private static final int REQUEST_DATE = 0;
    private static final String DIALOG_DATE = "Dialog Date";

    private GroceryList mGroceryList;

    private EditText mItemName;
    private EditText mBrandName;
    private EditText mQuantity;
    private EditText mExpiryDate;

    private Button mDone;

    //DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);

    public static EnterItemFragment newInstance(){
        return new EnterItemFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGroceryList = new GroceryList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_enter_item, container, false);

        mItemName = (EditText) view.findViewById(R.id.cgl_item_name);
        mBrandName = (EditText) view.findViewById(R.id.cgl_brand_name);
        mQuantity = (EditText) view.findViewById(R.id.cgl_quantity_name);
        mExpiryDate = (EditText) view.findViewById(R.id.cgl_expiry_date);
        mExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(new Date());
                dialog.setTargetFragment(EnterItemFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);

            }
        });

        mDone = (Button) view.findViewById(R.id.cgl_button_done);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGroceryList.setItemName(mItemName.getText().toString());
                mGroceryList.setBrandName(mBrandName.getText().toString());
                mGroceryList.setQuantity(Integer.parseInt(mQuantity.getText().toString()));
                mGroceryList.setExpiryDate(mExpiryDate.getText().toString());

                SQLiteHelperForCreateGroceryList sqlHelper = new SQLiteHelperForCreateGroceryList(getActivity());
                boolean result = sqlHelper.addItems(mGroceryList);
                if(result){
                    Toast.makeText(getActivity(), "Items added successfully :) ", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Items could not be added :( ", Toast.LENGTH_SHORT).show();
                }

                updateFields();
            }
        });

        return view;
    }

    private void updateFields() {

        mItemName.setText(null);
        mBrandName.setText(null);
        mQuantity.setText(null);
        mExpiryDate.setText(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode != REQUEST_DATE){
            return;
        }
        if(resultCode == Activity.RESULT_OK && data != null){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mExpiryDate.setText(date.toString());
        }
    }

    private static class SQLiteHelperForCreateGroceryList extends SQLiteOpenHelper{

        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "GroceryListDB";
        private static final String TABLE_ITEMS = "ItemsTable";

        private String KEY_ID = "id";
        private String KEY_ITEM_NAME = "item_name";
        private String KEY_BRAND_NAME = "brand_name";
        private String KEY_QUANTITY = "quantity";
        private String KEY_EXPIRY_DATE = "expiry_date";
        private String KEY_CREATED_DATE = "created_date";

        private String[] COLUMNS = {KEY_ID, KEY_ITEM_NAME, KEY_BRAND_NAME, KEY_QUANTITY, KEY_EXPIRY_DATE, KEY_CREATED_DATE};

        public SQLiteHelperForCreateGroceryList(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String CREATE_GROCERY_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS +" ( " +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "item_name TEXT, " +
                    "brand_name TEXT, " +
                    "quantity INTEGER, " +
                    "expiry_date TEXT, " +
                    "created_date TEXT " + ")" ;

            sqLiteDatabase.execSQL(CREATE_GROCERY_ITEMS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);

            onCreate(sqLiteDatabase);
        }

        private boolean addItems(GroceryList list){

            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(KEY_ITEM_NAME, list.getItemName());
            values.put(KEY_BRAND_NAME, list.getBrandName());
            values.put(KEY_QUANTITY, list.getQuantity());
            values.put(KEY_EXPIRY_DATE, list.getExpiryDate().toString());
            values.put(KEY_CREATED_DATE, list.getListCreatedDate().toString());

            long success = db.insert(TABLE_ITEMS, null, values);

            db.close();

            return success > -1;
        }
    }
}
