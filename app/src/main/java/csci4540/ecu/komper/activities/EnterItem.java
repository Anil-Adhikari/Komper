package csci4540.ecu.komper.activities;

/**
 * Created by anil on 10/28/17.
 */

public class EnterItem {

    /*private static final int REQUEST_DATE = 0;
    private static final String DIALOG_DATE = "Dialog Date";

    private GroceryList mGroceryList;

    private EditText mLabelName;
    private Button mDone;

    //DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);

    public static ListGroceryListFragment newInstance(){
        return new ListGroceryListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGroceryList = new GroceryList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_grocery_list, container, false);

        mLabelName = (EditText) view.findViewById(R.id.cgl_label_grocery_list);
        *//*mBrandName = (EditText) view.findViewById(R.id.cgl_brand_name);
        mQuantity = (EditText) view.findViewById(R.id.cgl_quantity_name);

        mExpiryDate = (EditText) view.findViewById(R.id.cgl_expiry_date);
        mExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(new Date());
                dialog.setTargetFragment(ListGroceryListFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);

            }
        });*//*

        mDone = (Button) view.findViewById(R.id.cgl_button_done);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGroceryList.setLabel(mLabelName.getText().toString());

                boolean result = KomperBase.getKomperBase(getActivity()).addGroceryList(mGroceryList);
                if(result){
                    Toast.makeText(getActivity(), "Grocery List created successfully :) ", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Grocery List could not be created :( ", Toast.LENGTH_SHORT).show();
                }

                //updateFields();
            }
        });
        getActivity().finish();
        return view;
    }

    *//*private void updateFields() {

        mItemName.setText(null);
        mBrandName.setText(null);
        mQuantity.setText(null);
        mExpiryDate.setText(null);
    }*//*

    *//*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode != REQUEST_DATE){
            return;
        }
        if(resultCode == Activity.RESULT_OK && data != null){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mExpiryDate.setText(format.format(date));
        }
    }*/
}
