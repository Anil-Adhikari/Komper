package csci4540.ecu.komper.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Exchanger;

import csci4540.ecu.komper.database.KomperDbSchema.GroceryListTable;
import csci4540.ecu.komper.database.KomperDbSchema.ItemTable;
import csci4540.ecu.komper.database.KomperDbSchema.PriceTable;
import csci4540.ecu.komper.database.KomperDbSchema.StoreTable;
import csci4540.ecu.komper.datamodel.GroceryList;
import csci4540.ecu.komper.datamodel.Item;
import csci4540.ecu.komper.datamodel.Price;
import csci4540.ecu.komper.datamodel.Store;

/**
 * Created by anil on 10/24/17.
 */

public class KomperCursorWrapper extends CursorWrapper {

    public KomperCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public GroceryList getGroceryList(){
        String uuidString = getString(getColumnIndex(GroceryListTable.Cols.UUID));
        String label = getString(getColumnIndex(GroceryListTable.Cols.LABEL));
        double price = getDouble(getColumnIndex(GroceryListTable.Cols.TOTALPRICE));
        long date = getLong(getColumnIndex(GroceryListTable.Cols.DATE));
        String checked = getString(getColumnIndex(GroceryListTable.Cols.CHECKED));

        GroceryList groceryList = new GroceryList(UUID.fromString(uuidString));
        groceryList.setLabel(label);
        groceryList.setDate(new Date(date));
        groceryList.setTotalPrice(price);
        groceryList.setChecked(checked);

        return groceryList;
    }

    public Item getItem(){
        String uuidString = getString(getColumnIndex(ItemTable.Cols.UUID));
        String itemname = getString(getColumnIndex(ItemTable.Cols.ITEMNAME));
        String brandname = getString(getColumnIndex(ItemTable.Cols.BRAND));
        double quantity = getDouble(getColumnIndex(ItemTable.Cols.QUANTITY));
        double price = getDouble(getColumnIndex(ItemTable.Cols.PRICE));
        long enteredDte = getLong(getColumnIndex(ItemTable.Cols.ENTEREDDATE));
        long expiryDate = getLong(getColumnIndex(ItemTable.Cols.EXPIRYDATE));
        String checked = getString(getColumnIndex(ItemTable.Cols.CHECKED));

        Item item = new Item(UUID.fromString(uuidString));
        item.setItemName(itemname);
        item.setItemBrandName(brandname);
        item.setItemQuantity(quantity);
        item.setItemPrice(price);
        item.setItemEnteredDate(new Date(enteredDte));
        item.setItemExpiryDate(new Date(expiryDate));
        item.setChecked(checked);

        return item;
    }

    public Store getStore() {
        String uuidString = getString(getColumnIndex(StoreTable.Cols.UUID));
        String storeName = getString(getColumnIndex(StoreTable.Cols.STORENAME));
        String storeAddress = getString(getColumnIndex(StoreTable.Cols.ADDRESS));
        String longitude = getString(getColumnIndex(StoreTable.Cols.LONGITUDE));
        String latitude = getString(getColumnIndex(StoreTable.Cols.LATITUDE));
        String selected = getString(getColumnIndex(StoreTable.Cols.SELECTED));

        Store store = new Store(UUID.fromString(uuidString));
        store.setStoreName(storeName);
        store.setStoreaddress(storeAddress);
        store.setLongitude(longitude);
        store.setLatitude(latitude);
        store.setSelected(selected);
        return store;
    }

    public Price getPrice() {
        String uuidString = getString(getColumnIndex(PriceTable.Cols.UUID));
        String grocerylistid = getString(getColumnIndex(PriceTable.Cols.GROCERYLISTID));
        String storeid = getString(getColumnIndex(PriceTable.Cols.STOREID));
        String itemid = getString(getColumnIndex(PriceTable.Cols.ITEMID));
        String price = getString(getColumnIndex(PriceTable.Cols.PRICE));

        Price myprice = new Price(UUID.fromString(uuidString));
        myprice.setGrocerylistId(UUID.fromString(grocerylistid));
        myprice.setStoreId(UUID.fromString(storeid));
        myprice.setItemId(UUID.fromString(itemid));
        myprice.setPrice(price);
        return myprice;
    }
}
