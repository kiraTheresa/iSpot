package com.ispot.ispotbackend.model.entity;

public class User {
    private String id;
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private double lat;
    private double lng;
    private long lastActiveTime;

    public User() {}

    // getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }

    public long getLastActiveTime() { return lastActiveTime; }
    public void setLastActiveTime(long lastActiveTime) { this.lastActiveTime = lastActiveTime; }
}
