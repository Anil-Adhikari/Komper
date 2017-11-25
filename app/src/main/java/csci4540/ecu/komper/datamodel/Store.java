package csci4540.ecu.komper.datamodel;

import java.util.UUID;

/**
 * Created by anil on 11/24/17.
 */

public class Store {
    private UUID storeId;
    private String storeName;
    private String storeaddress;
    private String longitude;
    private String latitude;
    private String selected;

    public Store(UUID storeId) {
        this.storeId = storeId;
        this.storeName = "";
        this.storeaddress = "";
        this.longitude = "";
        this.latitude = "";
        this.selected = "";
    }

    public Store(){
        this(UUID.randomUUID());
    }

    public UUID getStoreId() {
        return storeId;
    }

    public String getStoreName(){
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreaddress() {
        return storeaddress;
    }

    public void setStoreaddress(String storeaddress) {
        this.storeaddress = storeaddress;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
