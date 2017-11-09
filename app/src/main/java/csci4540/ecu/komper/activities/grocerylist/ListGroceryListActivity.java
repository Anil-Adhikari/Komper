package csci4540.ecu.komper.activities.grocerylist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.Serializable;

import csci4540.ecu.komper.activities.FragmentContainerActivity;

public class ListGroceryListActivity extends FragmentContainerActivity {

    private static final String EXTRA_GOOGLE_CLIENT = "google_client";

    @Override
    protected Fragment createFragment() {
        return ListGroceryListFragment.newInstance();
    }

    public static Intent newIntent(Context context, GoogleApiClient googleApiClient){
        Intent intent = new Intent(context, ListGroceryListActivity.class);
        //intent.putExtra(EXTRA_GOOGLE_CLIENT, (Serializable) googleApiClient);
        return intent;
    }

    @Override
    public void onBackPressed() {

    }
}
