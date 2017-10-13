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

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import csci4540.ecu.komper.R;

public class CreateGroceryListFragment extends Fragment {

    private static final int REQUEST_DATE = 0;
    private static final String DIALOG_DATE = "Dialog Date";

    private GroceryList mGroceryList;

    private EditText mItemName;
    private EditText mBrandName;
    private EditText mQuantity;
    private EditText mExpiryDate;

    private Button mDone;

    DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);

    public static CreateGroceryListFragment newInstance(){
        return new CreateGroceryListFragment();
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
                dialog.setTargetFragment(CreateGroceryListFragment.this, REQUEST_DATE);
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

                SQLiteHelperClass sqlHelper = new SQLiteHelperClass(getActivity());
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
            mExpiryDate.setText(format.format(date));
        }
    }

}
