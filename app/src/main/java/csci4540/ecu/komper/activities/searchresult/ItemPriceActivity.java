package csci4540.ecu.komper.activities.searchresult;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

import csci4540.ecu.komper.activities.FragmentContainerActivity;

/**
 * Created by anil on 11/25/17.
 */

public class ItemPriceActivity extends FragmentContainerActivity {

    private static final String EXTRA_GROCERYLISTID = "csci4540.ecu.komper.activities.searchresult.grocerylistid";
    private static final String EXTRA_STOREID = "csci4540.ecu.komper.activities.searchresult.storeid";

    @Override
    protected Fragment createFragment() {
        return ItemPriceFragment.newInstance((UUID)getIntent().getSerializableExtra(EXTRA_GROCERYLISTID),
                (UUID) getIntent().getSerializableExtra(EXTRA_STOREID));
    }

    public static Intent newIntent(Context context, UUID grocerylistid, UUID storeId){
        Intent intent = new Intent(context, ItemPriceActivity.class);
        intent.putExtra(EXTRA_GROCERYLISTID, grocerylistid);
        intent.putExtra(EXTRA_STOREID, storeId);
        return intent;
    }

}
