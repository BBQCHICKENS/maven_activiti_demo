package com.lbj.model;



import java.io.Serializable;
import java.util.Date;

public class LeaveApply implements Serializable{
    private  String reason;
    private  Integer days;
    private Date startTime;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
