package csci4540.ecu.komper.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by anil on 10/13/17.
 */

public class SQLiteHelperClass extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "GroceryListDB";
    private static final String TABLE_ITEMS = "ItemsTable";

    private String KEY_ID = "id";
    private String KEY_ITEM_NAME = "item_name";
    private String KEY_BRAND_NAME = "brand_name";
    private String KEY_QUANTITY = "quantity";
    private String KEY_EXPIRY_DATE = "expiry_date";
    private String KEY_CREATED_DATE = "created_date";

    private String[] COLUMNS = {KEY_ID, KEY_ITEM_NAME, KEY_BRAND_NAME, KEY_QUANTITY, KEY_EXPIRY_DATE, KEY_CREATED_DATE};

    public SQLiteHelperClass(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_GROCERY_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS +" ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "item_name TEXT, " +
                "brand_name TEXT, " +
                "quantity INTEGER, " +
                "expiry_date TEXT, " +
                "created_date TEXT " + ")" ;

        sqLiteDatabase.execSQL(CREATE_GROCERY_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);

        onCreate(sqLiteDatabase);
    }

    boolean addItems(GroceryList list){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ITEM_NAME, list.getItemName());
        values.put(KEY_BRAND_NAME, list.getBrandName());
        values.put(KEY_QUANTITY, list.getQuantity());
        values.put(KEY_EXPIRY_DATE, list.getExpiryDate());
        values.put(KEY_CREATED_DATE, list.getListCreatedDate());

        long success = db.insert(TABLE_ITEMS, null, values);

        db.close();

        return success > -1;
    }


}
