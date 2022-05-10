/*
 *   @author Anthony Kritikos
 *   House Decoration Project
 *   05/01/2022
 */
package com.EZPZ.housedecorators;

public class DecorationModel {
    private String imageURL;
    private String decorationName;
    private String decorationType;
    private String itemDescription;
    private String itemID;
    private int quantity;
    private long price;
    private long price_micro;
    private long subtotal_micro;

    private int dollars;
    private int cents;


    private DecorationModel(){}

    private DecorationModel(String imageURL, String decorationName, String decorationType,
                            String itemDescription, String itemID, long price, long price_micro,
                            int dollars, int cents){
        this.imageURL = imageURL;
        this.decorationName = decorationName;
        this.decorationType = decorationType;
        this.itemDescription = itemDescription;
        this.itemID = itemID;
        this.quantity = 0;
        this.price = price;
        this.price_micro = price_micro;
        this.dollars = dollars;
        this.cents = cents;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDecorationName() {
        return decorationName;
    }

    public void setDecorationName(String decorationName) {
        this.decorationName = decorationName;
    }

    public String getDecorationType() {
        return decorationType;
    }

    public void setDecorationType(String decorationType) {
        this.decorationType = decorationType;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getPrice_micro() {
        return price_micro;
    }

    public void setPrice_micro(long price_micro) {
        this.price_micro = price_micro;
    }

    public long getSubtotal_micro() {
        return subtotal_micro;
    }

    public void setSubtotal_micro(long subtotal_micro) {
        this.subtotal_micro = subtotal_micro;
    }

    public int getDollars() {
        return dollars;
    }

    public void setDollars(int dollars) {
        this.dollars = dollars;
    }

    public int getCents() {
        return cents;
    }

    public void setCents(int cents) {
        this.cents = cents;
    }

}
