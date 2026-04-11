package com.revworkforce.model;

import java.sql.Date;

public class Holiday {

    private int holidayId;
    private Date holidayDate;
    private String holidayName;
    private String description;

    public int getHolidayId() {
        return holidayId;
    }
    public void setHolidayId(int holidayId) {
        this.holidayId = holidayId;
    }
    public Date getHolidayDate() {
        return holidayDate;
    }
    public void setHolidayDate(Date holidayDate) {
        this.holidayDate = holidayDate;
    }
    public String getHolidayName() {
        return holidayName;
    }
    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}