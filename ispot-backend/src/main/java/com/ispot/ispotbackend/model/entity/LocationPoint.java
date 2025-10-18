package com.ispot.ispotbackend.model.entity;

public class LocationPoint {
    private String id;
    private String userId;
    private double lat;
    private double lng;
    private long timestamp;

    public LocationPoint() {}

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }

}
