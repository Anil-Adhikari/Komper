package csci4540.ecu.komper.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

import csci4540.ecu.komper.database.KomperDbSchema.GroceryListTable;
import csci4540.ecu.komper.datamodel.GroceryList;

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
}
