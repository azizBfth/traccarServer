package org.traccar.reports.model;
import java.util.Date;

public class ChartReport   {

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

    private double Fuel;

    public double getFuel() {
        return Fuel;
    }

    public void setFuel(double d) {
        this.Fuel = (long) d;
    }

    private double temperature; 

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
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
    private double BlueCoinTemp1 ; 

    public double getBluecointemp1() {
        return BlueCoinTemp1;
    }

    public void setBluecointemp1(double BlueCoinTemp1) {
        this.BlueCoinTemp1 = BlueCoinTemp1;
    }



    private double BlueCoinTemp2 ; 

    public double getBluecointemp2() {
        return BlueCoinTemp2;
    }

    public void setBluecointemp2(double BlueCoinTemp2) {
        this.BlueCoinTemp2 = BlueCoinTemp2;
    }

}
