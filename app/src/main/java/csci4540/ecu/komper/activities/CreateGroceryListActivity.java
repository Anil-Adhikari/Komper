package csci4540.ecu.komper.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class CreateGroceryListActivity extends FragmentContainerActivity {

    @Override
    protected Fragment createFragment() {
        return CreateGroceryListFragment.newInstance();
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, CreateGroceryListActivity.class);
        return intent;
    }
}
