package csci4540.ecu.komper.activities;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by anil on 10/7/17.
 */

public class GroceryList {

    private int ID;
    private String itemName;
    private String brandName;
    private int quantity;
    private String expiryDate;
    private String listCreatedDate;

    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);

    GroceryList(){
        Date date = new Date();
        listCreatedDate = dateFormat.format(date);
    }

    public GroceryList(int ID, String itemName, String brandName, int quantity, String expiryDate, String listCreatedDate) {
        this.ID = ID;
        this.itemName = itemName;
        this.brandName = brandName;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.listCreatedDate = listCreatedDate;
    }

    public int getID() {
        return ID;
    }

    String getItemName() {
        return itemName;
    }

    void setItemName(String itemName) {
        this.itemName = itemName;
    }

    String getBrandName() {
        return brandName;
    }

    void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    int getQuantity() {
        return quantity;
    }

    void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    String getExpiryDate() {
        return expiryDate;
    }

    void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    String getListCreatedDate() {
        return listCreatedDate;
    }

}
