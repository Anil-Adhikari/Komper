package csci4540.ecu.komper.datamodel;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by anil on 10/7/17.
 */

public class GroceryList {

    private UUID uuid;
    private String label;
    private Date date;
    private double totalPrice;
    private String checked;



    public GroceryList(){
        this(UUID.randomUUID());
    }

    public GroceryList(UUID ID) {
        uuid = ID;
        label = " ";
        date = new Date();
        totalPrice = 0.0;
        checked = "no";
    }

    public UUID getID() {
        return uuid;
    }

    public String getLabel() {
        return label;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }
}
