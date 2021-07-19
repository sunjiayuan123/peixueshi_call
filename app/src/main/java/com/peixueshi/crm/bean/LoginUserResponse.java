package com.peixueshi.crm.bean;

public class LoginUserResponse {
    public int err;
    public UserInfo info;
    public String token;

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public UserInfo getInfo() {
        return info;
    }

    public void setInfo(UserInfo info) {
        this.info = info;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
