package csci4540.ecu.komper.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import csci4540.ecu.komper.database.KomperCursorWrapper;
import csci4540.ecu.komper.database.KomperDbSchema;
import csci4540.ecu.komper.database.KomperDbSchema.GroceryListTable;
import csci4540.ecu.komper.database.KomperDbSchema.ItemTable;
import csci4540.ecu.komper.database.KomperDbSchema.PriceTable;
import csci4540.ecu.komper.database.KomperDbSchema.StoreTable;
import csci4540.ecu.komper.database.KomperSQLiteHelper;
import csci4540.ecu.komper.datamodel.GroceryList;
import csci4540.ecu.komper.datamodel.Item;
import csci4540.ecu.komper.datamodel.Price;
import csci4540.ecu.komper.datamodel.Store;

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
        values.put(GroceryListTable.Cols.CHECKED, list.getChecked());

        return values;
    }

    public List<GroceryList> getGroceryLists() {
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
        values.put(ItemTable.Cols.CHECKED, item.getChecked());

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

    public List<Item> getCheckedoutItems(UUID groceryListID) {
        List<Item> itemList = new ArrayList<>();
        String whereClause = ItemTable.Cols.GROCERYLISTID + " = ?" + " AND " + ItemTable.Cols.CHECKED + " = ?";
        String[] whereArgs = new String[]{groceryListID.toString(), "yes"};
        String tableName = ItemTable.NAME;

        KomperCursorWrapper cursor = queryDb(whereClause, whereArgs, tableName);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                itemList.add(cursor.getItem());
                cursor.moveToNext();
            }
        } finally {
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

    /***
     * Methods for manipulating store data
     */
    public void addStore(Store store){
        ContentValues values = getStoreContentValues(store);
        mDatabase.insert(StoreTable.NAME, null, values);
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

    public List<Store> getStores(){
        List<Store> storeList = new ArrayList<>();
        String tableName = StoreTable.NAME;

        KomperCursorWrapper cursor = queryDb(null, null, tableName);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                storeList.add(cursor.getStore());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        return storeList;
    }

    public List<Store> getSelectedStore(){
        List<Store> storeList = new ArrayList<>();
        String tableName = StoreTable.NAME;
        String whereClause = StoreTable.Cols.SELECTED + " = ?";
        String[] whereArgs = new String[]{"yes"};

        KomperCursorWrapper cursor = queryDb(whereClause, whereArgs, tableName);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                storeList.add(cursor.getStore());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        return storeList;
    }

    public void updateStore(Store store){
        ContentValues values = getStoreContentValues(store);
        mDatabase.update(StoreTable.NAME, values, StoreTable.Cols.UUID + " = ?", new String[]{store.getStoreId().toString()});
    }

    public Store getStore(UUID storeId) {
        String whereClause = StoreTable.Cols.UUID + " = ?";
        String[] whereArgs = new String[]{storeId.toString()};
        String tableName = StoreTable.NAME;

        KomperCursorWrapper cursor = queryDb(whereClause, whereArgs, tableName);
        try{
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getStore();
        }finally{
            cursor.close();
        }
    }

    /***
     *Methods to manipulate price from the searched store
     */
    public void addPrice(Price price){
        ContentValues values = getPriceContentValues(price);
        mDatabase.insert(PriceTable.NAME, null, values);

    }

    public void updatePrice(Price price, UUID oldpriceid){
        ContentValues values = getPriceContentValues(price);
        mDatabase.update(PriceTable.NAME, values, PriceTable.Cols.UUID + " = ?", new String[]{oldpriceid.toString()});

    }

    public void deletePrice(Price price){
        mDatabase.delete(PriceTable.NAME, PriceTable.Cols.UUID + " = ?", new String[]{price.getPriceId().toString()});
    }

    public List<Price> getPrices(UUID grocerylistID, UUID storeID){
        List<Price> priceList = new ArrayList<>();
        String tableName = PriceTable.NAME;
        String whereclause = PriceTable.Cols.GROCERYLISTID + " = ?" + " AND " +
                             PriceTable.Cols.STOREID + " = ? ";
        String [] whereargs = new String[]{grocerylistID.toString(), storeID.toString()};

        KomperCursorWrapper cursor = queryDb(whereclause, whereargs, tableName);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                priceList.add(cursor.getPrice());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        return priceList;
    }

    public Price getPrice(UUID grocerylistID, UUID storeID, UUID itemID){
        String tableName = PriceTable.NAME;
        String whereclause = PriceTable.Cols.GROCERYLISTID + " = ?" + " AND " +
                PriceTable.Cols.ITEMID + " = ?" + " AND " +
                PriceTable.Cols.STOREID + " = ?";
        String [] whereargs = new String[]{grocerylistID.toString(), itemID.toString(), storeID.toString()};

        KomperCursorWrapper cursor = queryDb(whereclause, whereargs, tableName);

        try{
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getPrice();
        }finally {
            cursor.close();
        }
    }

    private ContentValues getPriceContentValues(Price price) {
        ContentValues values = new ContentValues();
        values.put(PriceTable.Cols.UUID, price.getPriceId().toString());
        values.put(PriceTable.Cols.GROCERYLISTID, price.getGrocerylistId().toString());
        values.put(PriceTable.Cols.STOREID, price.getStoreId().toString());
        values.put(PriceTable.Cols.ITEMID, price.getItemId().toString());
        values.put(PriceTable.Cols.PRICE, price.getPrice());
        return values;
    }

    public double getTotalPrice(UUID grocerylistid, UUID storeid){
        List<Price> prices = getPrices(grocerylistid, storeid);
        double totalprice = 0.0;
        for(Price price : prices){
            double quantity = getItem(price.getItemId()).getItemQuantity();
            totalprice += Double.parseDouble(price.getPrice()) * quantity;
        }
        return totalprice;
    }

    public double getTotalCheckedPrice(UUID grocerylistid, UUID storeid){
        List<Price> prices = getPrices(grocerylistid, storeid);
        double totalprice = 0.0;
        for(Price price : prices){
            Item item = getItem(price.getItemId());
            if(item.getChecked().equals("yes")) {
                double quantity = item.getItemQuantity();
                totalprice += Double.parseDouble(price.getPrice()) * quantity;
            }
        }
        return totalprice;
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

    public File getNewReceiptPhotoFile(UUID grocerylistID){
        File fileDir = mContext.getFilesDir();
        return new File(fileDir, "IMG_"+grocerylistID.toString()+new Date()+".jpg");

    }

    public File getLatestModifiedFile(UUID grocerylistID){
        File fileDir = mContext.getFilesDir();
        //File parentDir = fileDir.getParentFile();
        File[] files = fileDir.listFiles();
        long lastmod = Long.MIN_VALUE;
        File choice = new File(fileDir, "temp.jpg");
        for(File file: files){
            if(file.getName().contains(grocerylistID.toString())){
                if(file.lastModified() > lastmod) {
                    choice = file;
                    lastmod = choice.lastModified();
                }
            }
        }
        return choice;
    }
}
