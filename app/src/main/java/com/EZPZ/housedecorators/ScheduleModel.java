/*
 *   @author Anthony Kritikos
 *   House Decoration Project
 *   05/01/2022
 */
package com.EZPZ.housedecorators;

public class ScheduleModel {
    private String date;
    private String month;
    private String day;
    private String dayofweek;
    private String year;
    private String time;

    // Needed for Firebase Firestore
    private ScheduleModel(){}

    private ScheduleModel(String date, String month, String day, String year, String hour){
        this.date = date;
        this.month = month;
        this.day = day;
        this.year = year;
        this.time = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDayofweek() {
        return dayofweek;
    }

    public void setDayofweek(String dayofweek) {
        this.dayofweek = dayofweek;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
