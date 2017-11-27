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
            public static final String CHECKED = "grocerylist_checked";
        }

    }
    public static final class ItemTable{
        public static final String NAME = "Item";
        public static final class Cols{
            public static final String UUID = "item_uuid";
            public static final String ITEMNAME = "item_name";
            public static final String BRAND = "item_brand";
            public static final String QUANTITY = "item_quantity";
            public static final String EXPIRYDATE = "item_expiry_date";
            public static final String ENTEREDDATE = "item_entered_date";
            public static final String PRICE = "item_price";
            public static final String GROCERYLISTID = "grocerylist_id";
            public static final String CHECKED = "item_checked";
        }
    }
    public static final class StoreTable{
        public static final String NAME = "Store";
        public static final class Cols{
            public static final String UUID = "store_uuid";
            public static final String STORENAME = "store_name";
            public static final String ADDRESS = "store_address";
            public static final String LONGITUDE = "store_longitude";
            public static final String LATITUDE = "store_latitude";
            public static final String SELECTED = "store_selected";

        }
    }
    public static final class PriceTable{
        public static final String NAME = "Price";
        public static final class Cols{
            public static final String UUID = "price_uuid";
            public static final String GROCERYLISTID = "price_grocerylistid";
            public static final String STOREID = "price_storeid";
            public static final String ITEMID = "price_itemid";
            public static final String PRICE = "price";
        }
    }
}
