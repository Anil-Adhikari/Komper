package csci4540.ecu.komper.activities.stores;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

import csci4540.ecu.komper.activities.FragmentContainerActivity;

/**
 * Created by anil on 11/24/17.
 */

public class StoreActivity extends FragmentContainerActivity {

    private static final String EXTRA_GROCERYLISTID = "csci4540.ecu.komper.activities.storeactivity.grocerylistid";

    @Override
    protected Fragment createFragment() {
        return StoreFragment.newInstance((UUID)getIntent().getSerializableExtra(EXTRA_GROCERYLISTID));
    }

    public static Intent newIntent(Context context, UUID grocerylistID){
        Intent intent = new Intent(context, StoreActivity.class);
        intent.putExtra(EXTRA_GROCERYLISTID, grocerylistID);
        return intent;
    }

}
