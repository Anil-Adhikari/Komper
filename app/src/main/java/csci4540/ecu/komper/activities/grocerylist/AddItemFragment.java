package csci4540.ecu.komper.activities.grocerylist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import csci4540.ecu.komper.R;
import csci4540.ecu.komper.activities.KomperBase;
import csci4540.ecu.komper.datamodel.Item;
import csci4540.ecu.komper.utilities.WalmartRestClient;

/**
 * Created by anil on 11/1/17.
 */

public class AddItemFragment extends Fragment {

    private EditText mItemName;
    private EditText mBrandName;
    private EditText mQuantity;
    private EditText mExpiryDate;
    private Button mDone;

    private static final String DIALOG_DATE = "Dialog Date";
    private static final int REQUEST_DATE = 0;

    private static final String ARG_ITEMID = "csci4540.ecu.komper.activities.grocerylist.additem.itemid";
    private static final String ARG_GROCERYLISTID = "csci4540.ecu.komper.activities.grocerylist.additem.grocerylistid";

    private Item mItem;

    private UUID grocerylistId;

    DateFormat dateformat = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
    NumberFormat numberFormat  = new DecimalFormat("##.##");


    public static AddItemFragment newInstance(UUID itemID, UUID grocerylistId){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_ITEMID, itemID);
        bundle.putSerializable(ARG_GROCERYLISTID, grocerylistId);

        AddItemFragment fragment = new AddItemFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID itemID = (UUID) getArguments().getSerializable(ARG_ITEMID);
        grocerylistId = (UUID) getArguments().getSerializable(ARG_GROCERYLISTID);

        mItem = KomperBase.getKomperBase(getActivity()).getItem(itemID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter_item, container, false);

        mItemName = (EditText) view.findViewById(R.id.enteritem_edittext_name);
        mItemName.setText(mItem.getItemName());
        mItemName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mItem.setItemName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBrandName = (EditText) view.findViewById(R.id.enteritem_edittext_brandname);
        mBrandName.setText(mItem.getItemBrandName());
        mBrandName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mItem.setItemBrandName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mQuantity = (EditText) view.findViewById(R.id.enteritem_edittext_quantity);
        mQuantity.setText(numberFormat.format(mItem.getItemQuantity()));

        mExpiryDate = (EditText) view.findViewById(R.id.enteritem_edittext_expirydate);
        mExpiryDate.setText(dateformat.format(mItem.getItemExpiryDate()));
        mExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(new Date());
                dialog.setTargetFragment(AddItemFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });
        mExpiryDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    mItem.setItemExpiryDate(dateformat.parse(charSequence.toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDone = (Button) view.findViewById(R.id.enteritem_button_done);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPause();
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mItem.setItemQuantity(Double.parseDouble(mQuantity.getText().toString()));
        KomperBase.getKomperBase(getActivity()).updateItem(mItem, grocerylistId);
        WalmartRestClient.query(mItem.getItemName(), null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode != REQUEST_DATE){
            return;
        }
        if(resultCode == Activity.RESULT_OK && data != null){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mExpiryDate.setText(dateformat.format(date));
        }
    }
}
