package csci4540.ecu.komper.activities.grocerylist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

import csci4540.ecu.komper.activities.FragmentContainerActivity;

/**
 * Created by anil on 11/1/17.
 */

public class LIstItemListActivity extends FragmentContainerActivity {

    public static final String EXTRA_GROCERYLISTID = "csci4540.ecu.komper.activities.grocerylist.grocerylistID";

    @Override
    protected Fragment createFragment() {
        return LIstItemListFragment.newInstance((UUID)getIntent().getSerializableExtra(EXTRA_GROCERYLISTID));
    }

    public static Intent newIntent(Context context, UUID groceryListID){
        Intent intent = new Intent(context, LIstItemListActivity.class);
        intent.putExtra(EXTRA_GROCERYLISTID, groceryListID);

        return intent;
    }
}
