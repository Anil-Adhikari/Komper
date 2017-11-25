package csci4540.ecu.komper.datamodel;

import java.util.UUID;

/**
 * Created by anil on 11/24/17.
 */

public class Price {

    private UUID priceId;
    private UUID grocerylistId;
    private UUID itemId;
    private UUID storeId;
    private String price;

    public Price(){
        this(UUID.randomUUID());
    }
    public Price(UUID priceId){
        this.priceId = priceId;
    }

    public UUID getPriceId() {
        return priceId;
    }

    public UUID getGrocerylistId() {
        return grocerylistId;
    }

    public void setGrocerylistId(UUID grocerylistId) {
        this.grocerylistId = grocerylistId;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
