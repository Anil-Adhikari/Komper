package csci4540.ecu.komper.activities.grocerylist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

import csci4540.ecu.komper.activities.FragmentContainerActivity;

/**
 * Created by anil on 11/1/17.
 */

public class AddItemActivity extends FragmentContainerActivity {

    private static final String EXTRA_ITEMID = "csci4540.ecu.komper.activities.grocerylist.addItem.itemid";
    private static final String EXTRA_GROCERYLISTID = "csci4540.ecu.komper.activities.grocerylist.addItem.grocerylistid";

    @Override
    protected Fragment createFragment() {
        return AddItemFragment.newInstance((UUID)getIntent().getSerializableExtra(EXTRA_ITEMID)
                                            , (UUID)getIntent().getSerializableExtra(EXTRA_GROCERYLISTID));
    }

    public static Intent newIntent(Context context, UUID itemid, UUID grocerylistId){
        Intent intent = new Intent(context, AddItemActivity.class);
        intent.putExtra(EXTRA_ITEMID, itemid);
        intent.putExtra(EXTRA_GROCERYLISTID, grocerylistId);

        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
