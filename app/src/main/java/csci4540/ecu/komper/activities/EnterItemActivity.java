package csci4540.ecu.komper.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class EnterItemActivity extends FragmentContainerActivity {

    @Override
    protected Fragment createFragment() {
        return EnterItemFragment.newInstance();
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, EnterItemActivity.class);
        return intent;
    }
}
