package csci4540.ecu.komper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import csci4540.ecu.komper.database.KomperDbSchema.GroceryListTable;
import csci4540.ecu.komper.database.KomperDbSchema.ItemTable;

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

        sqLiteDatabase.execSQL(CREATE_GROCERY_LISTS_TABLE);
        sqLiteDatabase.execSQL(CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        /*sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GroceryListTable.NAME);

        onCreate(sqLiteDatabase);*/

    }
}
