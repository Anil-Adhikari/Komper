package csci4540.ecu.komper.activities.grocerylist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

import csci4540.ecu.komper.activities.FragmentContainerActivity;

/**
 * Created by anil on 10/28/17.
 */

public class AddGroceryListActivity extends FragmentContainerActivity {

    private static final String EXTRA_GROCERY_LIST_ID = "csci4540.ecu.komper.activities.creategrocerylist.extraId";
    @Override
    protected Fragment createFragment() {
        return csci4540.ecu.komper.activities.grocerylist.AddGroceryListFragment.newInstance((UUID)getIntent().getSerializableExtra(EXTRA_GROCERY_LIST_ID));
    }

    public static Intent newIntent(Context context, UUID uuid){
        Intent intent = new Intent(context, AddGroceryListActivity.class);
        intent.putExtra(EXTRA_GROCERY_LIST_ID, uuid);
        return intent;
    }
}
