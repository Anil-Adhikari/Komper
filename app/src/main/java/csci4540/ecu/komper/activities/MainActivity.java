package csci4540.ecu.komper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import csci4540.ecu.komper.R;

/**
 * Created by anil on 10/12/17.
 */

public class MainActivity extends AppCompatActivity {

    private Button mCreateGroceryList;
    private Button mViewGroceryList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mCreateGroceryList = (Button) findViewById(R.id.button_create_grocerylist);
        mCreateGroceryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mViewGroceryList = (Button) findViewById(R.id.button_view_grocerylist);
        mViewGroceryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CreateGroceryListActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }
}
