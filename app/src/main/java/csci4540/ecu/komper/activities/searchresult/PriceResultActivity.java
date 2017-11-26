package csci4540.ecu.komper.activities.searchresult;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

import csci4540.ecu.komper.activities.FragmentContainerActivity;

/**
 * Created by anil on 11/25/17.
 */

public class PriceResultActivity extends FragmentContainerActivity {

    private static final String EXTRA_GROCERYLISTID = "csci4540.ecu.komper.activities.stores.grocerylistid";


    @Override
    protected Fragment createFragment() {
        return PriceResultFragment.newInstance((UUID)getIntent().getSerializableExtra(EXTRA_GROCERYLISTID));
    }

    public static Intent newIntent(Context context, UUID grocerylistid){
        Intent intent = new Intent(context, PriceResultActivity.class);
        intent.putExtra(EXTRA_GROCERYLISTID, grocerylistid);
        return intent;
    }
}
