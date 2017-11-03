package csci4540.ecu.komper.activities.grocerylist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.UUID;

import csci4540.ecu.komper.R;
import csci4540.ecu.komper.activities.KomperBase;
import csci4540.ecu.komper.datamodel.GroceryList;

/**
 * Created by anil on 10/28/17.
 */

public class AddGroceryListFragment extends Fragment {

    private TextView mFragmentTitle;
    private TextView mLabel;
    private EditText mEnterLabel;
    private Button mAddButton;

    private GroceryList mGroceryList;

    private static final String ARG_GROCERYLIST_ID = "csci4540.ecu.komper.activities.creategrocerylist.agrId";

    public static AddGroceryListFragment newInstance(UUID groceryListId){
        AddGroceryListFragment addGroceryListFragment = new AddGroceryListFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_GROCERYLIST_ID, groceryListId);

        addGroceryListFragment.setArguments(args);

        return addGroceryListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID groceryListId = (UUID) getArguments().getSerializable(ARG_GROCERYLIST_ID);
        mGroceryList = KomperBase.getKomperBase(getActivity()).getGroceryList(groceryListId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_new_grocerylist, container, false);

        mFragmentTitle = (TextView) view.findViewById(R.id.cgl_top_label);
        mLabel = (TextView) view.findViewById(R.id.cgl_label_for_enter_grocerylist);

        mEnterLabel = (EditText) view.findViewById(R.id.cgl_enter_label);
        mEnterLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEnterLabel.setSelection(0);
            }
        });
        mEnterLabel.setText(mGroceryList.getLabel());
        mEnterLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mGroceryList.setLabel(mEnterLabel.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mAddButton = (Button) view.findViewById(R.id.cgl_button_done);
        mAddButton.setOnClickListener(new View.OnClickListener() {
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
        KomperBase.getKomperBase(getActivity()).updateGroceryList(mGroceryList);
    }
}
