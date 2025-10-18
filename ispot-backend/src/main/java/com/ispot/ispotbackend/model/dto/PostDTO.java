package com.ispot.ispotbackend.model.dto;

public class PostDTO {
    private String userId;
    private double lat;
    private double lng;

    // getter & setter
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }
}
