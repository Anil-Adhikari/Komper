package csci4540.ecu.komper.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import csci4540.ecu.komper.database.KomperCursorWrapper;
import csci4540.ecu.komper.database.KomperDbSchema;
import csci4540.ecu.komper.database.KomperDbSchema.GroceryListTable;
import csci4540.ecu.komper.database.KomperSQLiteHelper;
import csci4540.ecu.komper.datamodel.GroceryList;

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

    public boolean addGroceryList(GroceryList list){

        ContentValues values = getContentValues(list);
        long success = mDatabase.insert(GroceryListTable.NAME, null, values);

        return success > -1;
    }

    private ContentValues getContentValues(GroceryList list){
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
        ContentValues values = getContentValues(grocerylist);
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
