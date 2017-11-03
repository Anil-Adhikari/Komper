package csci4540.ecu.komper.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Exchanger;

import csci4540.ecu.komper.database.KomperDbSchema.GroceryListTable;
import csci4540.ecu.komper.database.KomperDbSchema.ItemTable;
import csci4540.ecu.komper.datamodel.GroceryList;
import csci4540.ecu.komper.datamodel.Item;

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

        GroceryList groceryList = new GroceryList(UUID.fromString(uuidString));
        groceryList.setLabel(label);
        groceryList.setDate(new Date(date));
        groceryList.setTotalPrice(price);

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

        Item item = new Item(UUID.fromString(uuidString));
        item.setItemName(itemname);
        item.setItemBrandName(brandname);
        item.setItemQuantity(quantity);
        item.setItemPrice(price);
        item.setItemEnteredDate(new Date(enteredDte));
        item.setItemExpiryDate(new Date(expiryDate));

        return item;
    }
}
