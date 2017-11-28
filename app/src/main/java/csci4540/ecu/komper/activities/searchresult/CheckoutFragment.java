package csci4540.ecu.komper.activities.searchresult;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import csci4540.ecu.komper.R;
import csci4540.ecu.komper.activities.KomperBase;
import csci4540.ecu.komper.activities.grocerylist.ImageViewFragment;
import csci4540.ecu.komper.activities.grocerylist.LIstItemListActivity;
import csci4540.ecu.komper.activities.grocerylist.ListGroceryListActivity;
import csci4540.ecu.komper.datamodel.Item;

/**
 * Created by anil on 11/26/17.
 */

public class CheckoutFragment extends Fragment {

    private static final String ARG_GROCERYLISTID = "csci4540.ecu.komper.activities.searchresult.groceryListId";
    private static final String ARG_STOREID = "csci4540.ecu.komper.activities.searchresult.storeId";

    private static final String DIALOG_IMAGE = "Receipt Image";


    DateFormat dateformat = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
    NumberFormat numberFormat  = new DecimalFormat("##.##");

    private RecyclerView mCheckoutItemRecyclerView;
    private CheckoutItemAdapter mAdapter;
    private Button uploadReceipt;
    private TextView totalPriceTextview;
    private ImageView receiptImage;
    private Button finalize;

    private UUID mGroceryListID;
    private UUID mStoreID;

    private File mPhotoFile;

    private boolean mUploadedPhoto = false;

    private static final int REQUEST_PHOTO = 1;

    public static CheckoutFragment newInstance(UUID groceryListId, UUID storeId) {
        CheckoutFragment fragment = new CheckoutFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_GROCERYLISTID, groceryListId);
        bundle.putSerializable(ARG_STOREID, storeId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroceryListID = (UUID)getArguments().getSerializable(ARG_GROCERYLISTID);
        mStoreID = (UUID)getArguments().getSerializable(ARG_STOREID);
        mPhotoFile = KomperBase.getKomperBase(getActivity()).getNewReceiptPhotoFile(mGroceryListID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_checkoutitem, container, false);

        mCheckoutItemRecyclerView = (RecyclerView) view.findViewById(R.id.price_itemlist_checkout_recyclerview);
        mCheckoutItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        totalPriceTextview = (TextView) view.findViewById(R.id.price_item_totalprice_checkout);
        uploadReceipt = (Button) view.findViewById(R.id.upload_receipt);
        receiptImage = (ImageView) view.findViewById(R.id.image_receipt);
        finalize = (Button) view.findViewById(R.id.button_finalize);

        receiptImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DialogFragment imageDialog = ImageViewFragment.newInstance(mGroceryListID);
                imageDialog.show(manager, DIALOG_IMAGE);
            }
        });

        Double totalprice = KomperBase.getKomperBase(getActivity()).getTotalCheckedPrice(mGroceryListID, mStoreID);
        totalPriceTextview.setText(getString(R.string.total_price,String.valueOf(totalprice)));

        List<Item> items = KomperBase.getKomperBase(getActivity()).getCheckedoutItems(mGroceryListID);
        mAdapter = new CheckoutItemAdapter(items);
        mCheckoutItemRecyclerView.setAdapter(mAdapter);

        // to upload receipt
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        uploadReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoFile = KomperBase.getKomperBase(getActivity()).getNewReceiptPhotoFile(mGroceryListID);
                Uri uri = FileProvider.getUriForFile(getActivity(),
                            "csci4540.ecu.komper.fileprovider",
                            mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity()
                            .getPackageManager().queryIntentActivities(captureImage,
                                    PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                        getActivity().grantUriPermission(activity.activityInfo.packageName,
                                uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
                }
        });

        updateReceiptView();

        finalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (!mUploadedPhoto) {
                    Toast.makeText(getActivity(), R.string.upload_receipt_prompt,
                            Toast.LENGTH_SHORT).show();
                } else {*/
                Toast.makeText(getActivity(), "Congratulations you have finalized your shopping",
                        Toast.LENGTH_SHORT).show();
                finalize.setEnabled(false);
                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), ListGroceryListActivity.class);
                    startActivity(intent);
                //}
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PHOTO) {
            //mUploadedPhoto = true;

            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "csci4540.ecu.komper.fileprovider",
                    mPhotoFile);

            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updateReceiptView();
        }
    }

    private void updateReceiptView() {
        File latestImage = KomperBase.getKomperBase(getActivity()).getLatestModifiedFile(mGroceryListID);
        Glide.with(receiptImage.getContext()).load(latestImage).fitCenter().into(receiptImage);
    }

    private class CheckoutItemViewHolder extends RecyclerView.ViewHolder{

        private TextView mItemName;
        private TextView mBrandName;
        private TextView mExpiryDate;
        private TextView mQuantity;
        private TextView mPrice;
        private CheckBox mCheckbox;

        private Item mItem;

        public CheckoutItemViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_list_checkoutitemlist, parent, false));

            mItemName = (TextView) itemView.findViewById(R.id.checkout_item_name);
            mBrandName = (TextView) itemView.findViewById(R.id.checkout_item_brand_name);
            mExpiryDate = (TextView) itemView.findViewById(R.id.checkout_item_expiry_date);
            mQuantity = (TextView) itemView.findViewById(R.id.checkout_item_quantity);
            mPrice = (TextView) itemView.findViewById(R.id.checkout_item_price);
            mCheckbox = (CheckBox) itemView.findViewById(R.id.price_checkout_checkedout);

        }

        public void bind(Item item){
            mItemName.setText(getString(R.string.item_name, item.getItemName()));
            mBrandName.setText(getString(R.string.brand_name,item.getItemBrandName()));
            mExpiryDate.setText(getString(R.string.expiry_date, dateformat.format(item.getItemExpiryDate())));
            mQuantity.setText(getString(R.string.quantity, numberFormat.format(item.getItemQuantity())));
            mPrice.setText(getString(R.string.total_price, String.valueOf(item.getItemPrice() * item.getItemQuantity())));
            mCheckbox.setChecked(item.getChecked().equals("yes"));
            mCheckbox.setEnabled(false);

        }
    }

    private class CheckoutItemAdapter extends RecyclerView.Adapter<CheckoutItemViewHolder>{

        private final List<Item> mItems;

        public CheckoutItemAdapter(List<Item> items){
            mItems = items;
        }

        @Override
        public CheckoutItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new CheckoutItemViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(CheckoutItemViewHolder holder, int position) {
            Item item = mItems.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }
}
