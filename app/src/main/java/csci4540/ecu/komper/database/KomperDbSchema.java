package csci4540.ecu.komper.database;

/**
 * Created by anil on 10/24/17.
 */

public class KomperDbSchema {
    public static final class GroceryListTable{
        public static final String NAME = "GroceryList";
        public static final class Cols{
            public static final String UUID = "grocerylist_uuid";
            public static final String LABEL = "grocerylist_label";
            public static final String DATE = "grocerylist_created_date";
            public static final String TOTALPRICE = "grocerylist_total_price";
        }

    }
    public static final class ItemTable{
        public static final String NAME = "Item";
        public static final class Cols{
            public static final String UUID = "item_uuid";
            public static final String LABEL = "item_name";
            public static final String BRAND = "item_brand";
            public static final String Quantity = "item_quantity";
            public static final String EXPIRYDATE = "item_expiry_date";
        }
    }
}
