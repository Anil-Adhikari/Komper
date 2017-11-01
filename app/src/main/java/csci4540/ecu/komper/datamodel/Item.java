package csci4540.ecu.komper.datamodel;

import java.util.Date;
import java.util.UUID;

/**
 * Created by anil on 11/1/17.
 */

public class Item {

    private UUID itemID;
    private String itemName;
    private Date itemEnteredDate;
    private Date itemExpiryDate;
    private String itemBrandName;
    private double itemQuantity;

    public Item(String itemName, Date itemEnteredDate, Date itemExpiryDate, String itemBrandName, double itemQuantity) {
        this.itemID = UUID.randomUUID();
        this.itemName = itemName;
        this.itemEnteredDate = itemEnteredDate;
        this.itemExpiryDate = itemExpiryDate;
        this.itemBrandName = itemBrandName;
        this.itemQuantity = itemQuantity;
    }

    public Item(){
        this.itemID = UUID.randomUUID();
        this.itemName = "";
        this.itemEnteredDate = new Date();
        this.itemExpiryDate = null;
        this.itemBrandName = "";
        this.itemQuantity = 0.0;
    }

    public UUID getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Date getItemEnteredDate() {
        return itemEnteredDate;
    }

    public void setItemEnteredDate(Date itemEnteredDate) {
        this.itemEnteredDate = itemEnteredDate;
    }

    public Date getItemExpiryDate() {
        return itemExpiryDate;
    }

    public void setItemExpiryDate(Date itemExpiryDate) {
        this.itemExpiryDate = itemExpiryDate;
    }

    public String getItemBrandName() {
        return itemBrandName;
    }

    public void setItemBrandName(String itemBrandName) {
        this.itemBrandName = itemBrandName;
    }

    public double getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(double itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
}
