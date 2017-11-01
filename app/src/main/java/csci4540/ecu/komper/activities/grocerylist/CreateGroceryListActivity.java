package csci4540.ecu.komper.activities.grocerylist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import csci4540.ecu.komper.activities.FragmentContainerActivity;

public class CreateGroceryListActivity extends FragmentContainerActivity {

    @Override
    protected Fragment createFragment() {
        return new CreateGroceryListFragment();
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, CreateGroceryListActivity.class);
        return intent;
    }
}
