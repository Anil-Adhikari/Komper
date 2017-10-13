package csci4540.ecu.komper.activities;

import android.support.v4.app.Fragment;

/**
 * Created by anil on 10/13/17.
 */

public class ViewGroceryListActivity extends FragmentContainerActivity {
    @Override
    protected Fragment createFragment() {
        return new ViewGroceryListFragment();
    }
}
