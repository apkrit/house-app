/*
 *   @author Anthony Kritikos
 *   House Decoration Project
 *   05/01/2022
 */
package com.EZPZ.housedecorators;

public class UsereventsModel {
    private String bundle;
    private String date;
    private String time;
    private String total;
    private String type;

    // Needed for Firebase Firestore
    private UsereventsModel(){}

    private UsereventsModel(String bundle, String date, String time, String total, String type){
        this.bundle = bundle;
        this.date = date;
        this.time = time;
        this.total = total;
        this.type = type;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
