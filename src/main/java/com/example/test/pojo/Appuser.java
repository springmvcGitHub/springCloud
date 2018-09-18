package com.example.test.pojo;

import javax.persistence.*;
import java.io.Serializable;


public class Appuser implements Serializable {

    private long id;

    private long inviteCode;

    private String nickName;

    private String userKey;

    private String userName;

    private String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(long inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
