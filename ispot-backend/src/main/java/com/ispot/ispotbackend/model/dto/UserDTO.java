package com.ispot.ispotbackend.model.dto;

public class UserDTO {
    private String username;
    private String password;
    private String nickname;
<<<<<<< HEAD
    private Double lat;
    private Double lng;
=======
    private Double lat = null;  // 默认为 null
    private Double lng = null;  // 默认为 null

>>>>>>> 7bc3476 (后端代码优化)

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLng() { return lng; }
    public void setLng(Double lng) { this.lng = lng; }
}
