package csci4540.ecu.komper.activities.grocerylist;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import csci4540.ecu.komper.R;
import csci4540.ecu.komper.activities.KomperBase;

/**
 * Created by anil on 11/27/17.
 */

public class ImageViewFragment extends DialogFragment {

    private ImageView receiptImage;
    private static final String ARG_GROCERYLISTID = "grocerylist";
    private UUID mGroceryListID;

    public static ImageViewFragment newInstance(UUID grocerylistid){
        Bundle args = new Bundle();
        args.putSerializable(ARG_GROCERYLISTID, grocerylistid);

        ImageViewFragment fragment = new ImageViewFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_imageview, null);

        mGroceryListID = (UUID)getArguments().getSerializable(ARG_GROCERYLISTID);

        receiptImage = (ImageView) view.findViewById(R.id.zoomed_imageview);

        File image = KomperBase.getKomperBase(getActivity()).getLatestModifiedFile(mGroceryListID);

        Glide.with(receiptImage.getContext()).load(image).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(receiptImage);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Receipt Image")
                .create();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                getResources().getDisplayMetrics().widthPixels,
                getResources().getDisplayMetrics().heightPixels
        );
    }
}
