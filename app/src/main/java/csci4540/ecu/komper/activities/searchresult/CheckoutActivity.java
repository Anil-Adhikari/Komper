package csci4540.ecu.komper.activities.searchresult;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

import csci4540.ecu.komper.activities.FragmentContainerActivity;

/**
 * Created by anil on 11/26/17.
 */

public class CheckoutActivity extends FragmentContainerActivity {

    private static final String EXTRA_GROCERYLISTID = "csci4540.ecu.komper.activities.searchresult.groceryListId";
    private static final String EXTRA_STOREID = "csci4540.ecu.komper.activities.searchresult.storeId";


    @Override
    protected Fragment createFragment() {
        return CheckoutFragment.newInstance((UUID)getIntent().getSerializableExtra(EXTRA_GROCERYLISTID),
                (UUID)getIntent().getSerializableExtra(EXTRA_STOREID));
    }

    public static Intent newIntent(Context context, UUID groceryListId, UUID storeId){
        Intent intent = new Intent(context, CheckoutActivity.class);
        intent.putExtra(EXTRA_GROCERYLISTID, groceryListId);
        intent.putExtra(EXTRA_STOREID, storeId);
        return intent;
    }
}
