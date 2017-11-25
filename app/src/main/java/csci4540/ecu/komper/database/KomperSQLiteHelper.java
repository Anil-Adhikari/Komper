package csci4540.ecu.komper.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import csci4540.ecu.komper.activities.KomperBase;
import csci4540.ecu.komper.database.KomperDbSchema.GroceryListTable;
import csci4540.ecu.komper.database.KomperDbSchema.ItemTable;
import csci4540.ecu.komper.database.KomperDbSchema.PriceTable;
import csci4540.ecu.komper.database.KomperDbSchema.StoreTable;
import csci4540.ecu.komper.datamodel.Store;

/**
 * Created by anil on 10/24/17.
 */

public class KomperSQLiteHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "Komper.db";

    public KomperSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_GROCERY_LISTS_TABLE = "CREATE TABLE " + GroceryListTable.NAME +" ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GroceryListTable.Cols.UUID + ", "+
                GroceryListTable.Cols.LABEL + ", "+
                GroceryListTable.Cols.DATE + ", "+
                GroceryListTable.Cols.TOTALPRICE + ")" ;

        String CREATE_ITEMS_TABLE = "CREATE TABLE " + ItemTable.NAME +" ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ItemTable.Cols.UUID + ", "+
                ItemTable.Cols.ITEMNAME + ", "+
                ItemTable.Cols.BRAND + ", "+
                ItemTable.Cols.QUANTITY + ", " +
                ItemTable.Cols.ENTEREDDATE + ", " +
                ItemTable.Cols.EXPIRYDATE + ", " +
                ItemTable.Cols.PRICE + ", " +
                ItemTable.Cols.GROCERYLISTID
                //"FOREIGN KEY (" + ItemTable.Cols.GROCERYLISTID + ") REFERENCES " + GroceryListTable.NAME + "(grocerylist_uuid) "
                + ")" ;

        String CREATE_STORES_TABLE = "CREATE TABLE " + StoreTable.NAME +" ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StoreTable.Cols.UUID + ", "+
                StoreTable.Cols.STORENAME + ", "+
                StoreTable.Cols.ADDRESS + ", "+
                StoreTable.Cols.LONGITUDE + ", " +
                StoreTable.Cols.LATITUDE + ", " +
                StoreTable.Cols.SELECTED
                + ")" ;

        String CREATE_PRICE_TABLE = "CREATE TABLE " + PriceTable.NAME +" ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PriceTable.Cols.UUID + ", "+
                PriceTable.Cols.GROCERYLISTID + ", "+
                PriceTable.Cols.STOREID + ", "+
                PriceTable.Cols.ITEMID + ", " +
                PriceTable.Cols.PRICE
                + ")" ;

        sqLiteDatabase.execSQL(CREATE_GROCERY_LISTS_TABLE);
        sqLiteDatabase.execSQL(CREATE_ITEMS_TABLE);
        sqLiteDatabase.execSQL(CREATE_STORES_TABLE);
        addStores(sqLiteDatabase);
        sqLiteDatabase.execSQL(CREATE_PRICE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StoreTable.NAME);

        //onCreate(sqLiteDatabase);

    }

    private void addStores(SQLiteDatabase sqLiteDatabase) {
        Store walmart = new Store();
        walmart.setStoreName("Walmart Supercenter");
        walmart.setStoreaddress("210 Greenville Blvd Sw");
        walmart.setLongitude("-77.38752");
        walmart.setLatitude("35.574422");
        walmart.setSelected("No");
        ContentValues values = getStoreContentValues(walmart);
        sqLiteDatabase.insert(StoreTable.NAME, null, values);

        Store komperStore = new Store();
        komperStore.setStoreName("Komper Store");
        komperStore.setStoreaddress("Somewhere in Cloud");
        komperStore.setLongitude("0");
        komperStore.setLatitude("0");
        komperStore.setSelected("No");
        ContentValues values1 = getStoreContentValues(komperStore);
        sqLiteDatabase.insert(StoreTable.NAME, null, values1);
    }

    private ContentValues getStoreContentValues(Store store){
        ContentValues values = new ContentValues();
        values.put(StoreTable.Cols.UUID, store.getStoreId().toString());
        values.put(StoreTable.Cols.STORENAME, store.getStoreName());
        values.put(StoreTable.Cols.ADDRESS, store.getStoreaddress());
        values.put(StoreTable.Cols.LONGITUDE, store.getLongitude());
        values.put(StoreTable.Cols.LATITUDE, store.getLatitude());
        values.put(StoreTable.Cols.SELECTED, store.getSelected());

        return values;
    }
}
