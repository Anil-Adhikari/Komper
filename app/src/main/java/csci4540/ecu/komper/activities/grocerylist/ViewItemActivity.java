package csci4540.ecu.komper.activities.grocerylist;

import android.support.v4.app.Fragment;

import csci4540.ecu.komper.activities.FragmentContainerActivity;

/**
 * Created by anil on 11/1/17.
 */

public class ViewItemActivity extends FragmentContainerActivity {
    @Override
    protected Fragment createFragment() {
        return ViewItemFragment.newInstance();
    }
}
