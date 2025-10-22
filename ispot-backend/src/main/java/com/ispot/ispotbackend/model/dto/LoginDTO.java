package com.ispot.ispotbackend.model.dto;

import com.ispot.ispotbackend.model.entity.User;
import lombok.Data;

@Data
public class LoginDTO {
    private Boolean success;
    private String message;
    private User user;
    private String token;   // 暂无 token 可留空

    public static LoginDTO fail(String msg) {
        LoginDTO resp = new LoginDTO();
        resp.setSuccess(false);
        resp.setMessage(msg);
        return resp;
    }

    public static LoginDTO ok(User user) {
        LoginDTO resp = new LoginDTO();
        resp.setSuccess(true);
        resp.setUser(user);
        return resp;
    }
}