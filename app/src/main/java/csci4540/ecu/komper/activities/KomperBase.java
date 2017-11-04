package csci4540.ecu.komper.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import csci4540.ecu.komper.database.KomperCursorWrapper;
import csci4540.ecu.komper.database.KomperDbSchema;
import csci4540.ecu.komper.database.KomperDbSchema.GroceryListTable;
import csci4540.ecu.komper.database.KomperDbSchema.ItemTable;
import csci4540.ecu.komper.database.KomperSQLiteHelper;
import csci4540.ecu.komper.datamodel.GroceryList;
import csci4540.ecu.komper.datamodel.Item;

/**
 * Created by anil on 10/25/17.
 */

public class KomperBase {

    private static KomperBase sKomperBase;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private KomperBase(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new KomperSQLiteHelper(mContext).getWritableDatabase();
    }

    public static KomperBase getKomperBase(Context context){
        if(sKomperBase == null){
            sKomperBase = new KomperBase(context);
        }
        return sKomperBase;
    }

    /**
     * Methods those manipulates grocerylist data
     */

    public boolean addGroceryList(GroceryList list){

        ContentValues values = getGroceryListContentValues(list);
        long success = mDatabase.insert(GroceryListTable.NAME, null, values);

        return success > -1;
    }

    private ContentValues getGroceryListContentValues(GroceryList list){
        ContentValues values = new ContentValues();
        values.put(GroceryListTable.Cols.UUID, list.getID().toString());
        values.put(GroceryListTable.Cols.LABEL, list.getLabel());
        values.put(GroceryListTable.Cols.DATE, list.getDate().getTime());
        values.put(GroceryListTable.Cols.TOTALPRICE, list.getTotalPrice());

        return values;
    }

    public List<GroceryList> getGorceryLists() {
        String tableName = GroceryListTable.NAME;
        List<GroceryList> lists = new ArrayList<>();

        KomperCursorWrapper cursor = queryDb(null, null, tableName);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                lists.add(cursor.getGroceryList());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        /*for(int i = 0;i < 80; i++){
            GroceryList list = new GroceryList(UUID.randomUUID());
            lists.add(list);
        }*/

        return lists;
    }

    public void updateGroceryList(GroceryList grocerylist) {
        String uuidString = grocerylist.getID().toString();
        ContentValues values = getGroceryListContentValues(grocerylist);
        mDatabase.update(GroceryListTable.NAME, values,
                GroceryListTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public GroceryList getGroceryList(UUID groceryListId) {
        String tableName = GroceryListTable.NAME;
        String uuidString = groceryListId.toString();
        String whereClause = GroceryListTable.Cols.UUID + " = ?";
        String[] whereArgs = new String[]{uuidString};

        KomperCursorWrapper cursor = queryDb(whereClause, whereArgs, tableName);
        try{
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getGroceryList();
        }finally {
            cursor.close();
        }
    }

    public int getNumberOfItems(UUID groceryListID){
        String tableName = ItemTable.NAME;
        String countQuery = "SELECT * FROM " + tableName + " WHERE " + ItemTable.Cols.GROCERYLISTID + " = ?";
        Cursor cursor = mDatabase.rawQuery(countQuery, new String[]{groceryListID.toString()});
        int count  = cursor.getCount();
        cursor.close();
        return count;
    }

    public void deleteGroceryList(UUID groceryListID){
        mDatabase.delete(GroceryListTable.NAME, GroceryListTable.Cols.UUID + " = ?", new String[]{groceryListID.toString()});
    }

    public void deleteItemFromGroceryList(UUID groceryListID){
        mDatabase.delete(ItemTable.NAME, ItemTable.Cols.GROCERYLISTID + " = ?", new String[]{groceryListID.toString()});
    }

    /**
     * Methods those manipulates item data
     */

    public void addItem(Item item, UUID grocerylistId){
        ContentValues values = getItemContentValues(item, grocerylistId);
        mDatabase.insert(ItemTable.NAME, null, values);
    }

    public void updateItem(Item item, UUID grocerylistId){
        ContentValues values = getItemContentValues(item, grocerylistId);
        String whereclause = ItemTable.Cols.UUID + " = ?";
        String[] whereArgs = new String[]{item.getItemID().toString()};

        mDatabase.update(ItemTable.NAME,values, whereclause, whereArgs);
    }

    private ContentValues getItemContentValues(Item item, UUID grocerylistId) {
        ContentValues values = new ContentValues();

        values.put(ItemTable.Cols.UUID, item.getItemID().toString());
        values.put(ItemTable.Cols.ITEMNAME, item.getItemName());
        values.put(ItemTable.Cols.BRAND, item.getItemBrandName());
        values.put(ItemTable.Cols.QUANTITY, item.getItemQuantity());
        values.put(ItemTable.Cols.ENTEREDDATE, item.getItemEnteredDate().getTime());
        values.put(ItemTable.Cols.EXPIRYDATE, item.getItemExpiryDate().getTime());
        values.put(ItemTable.Cols.PRICE, item.getItemPrice());
        values.put(ItemTable.Cols.GROCERYLISTID, grocerylistId.toString());

        return values;
    }

    public List<Item> getAllItems(UUID groceryListID){
        List<Item> itemList = new ArrayList<>();
        String whereClause = ItemTable.Cols.GROCERYLISTID + " = ?";
        String[] whereArgs = new String[]{groceryListID.toString()};
        String tableName = ItemTable.NAME;

        KomperCursorWrapper cursor = queryDb(whereClause, whereArgs, tableName);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                itemList.add(cursor.getItem());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        return itemList;
    }

    public Item getItem(UUID itemID){
        String whereClause = ItemTable.Cols.UUID + " = ?";
        String[] whereArgs = new String[]{itemID.toString()};
        String tableName = ItemTable.NAME;

        KomperCursorWrapper cursor = queryDb(whereClause, whereArgs, tableName);
        try{
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getItem();
        }finally{
            cursor.close();
        }
    }

    public void deleteItem(UUID itemID){
        mDatabase.delete(ItemTable.NAME, ItemTable.Cols.UUID + " = ?", new String[]{itemID.toString()});
    }


    private KomperCursorWrapper queryDb(String whereClause, String[] whereArgs, String tableName){
        Cursor cursor = mDatabase.query(tableName,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new KomperCursorWrapper(cursor);
    }

}
