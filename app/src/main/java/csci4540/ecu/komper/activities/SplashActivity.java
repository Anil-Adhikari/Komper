package csci4540.ecu.komper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import csci4540.ecu.komper.activities.grocerylist.ListGroceryListActivity;

/**
 * Created by Amanda on 10/31/2017.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Intent intent= new Intent(this,ListGroceryListActivity.class);
        startActivity(intent);
        finish();
    }
}
