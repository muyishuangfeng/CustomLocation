package com.silence.customlocation.model;

/**
 * Created by Think on 2018/9/15.
 */

public class GpsModel {

    private String cityName;
    private double Longitude;

    private double Latitude;
    private String address;
    private String province;
    private String country;
    private String cityCode;




    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Override
    public String toString() {
        return "GpsModel{" +
                "cityName='" + cityName + '\'' +
                ", Longitude=" + Longitude +
                ", Latitude=" + Latitude +
                ", address='" + address + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", cityCode='" + cityCode + '\'' +
                '}';
    }
}
