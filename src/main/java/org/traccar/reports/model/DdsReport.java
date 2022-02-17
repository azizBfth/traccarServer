package org.traccar.reports.model;
import java.util.Date;

public class DdsReport   {

    private long deviceId;

public long getdeviceId()
{
    return deviceId;
}
public void setdeviceId(long deviceId){
    this.deviceId=deviceId;
}
    private long positionId;

    public long getPositionId() {
        return positionId;
    }

    public void setPositionId(long positionId) {
        this.positionId = positionId;
    }

    private double latitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    private double longitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private double masse;

    public double getMasse() {
        return masse;
    }

    public void setMasse(double d) {
        this.masse = (long) d;
    }

    
    

    private Date serverTime;
    public Date getserverTime() {
        return serverTime;
    }

    public void setserverTime(Date serverTime) {
        this.serverTime = serverTime;
    }


    private double speed; // value in knots

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }


}
