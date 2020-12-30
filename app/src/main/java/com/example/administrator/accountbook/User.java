package com.example.administrator.accountbook;

import java.io.Serializable;

/**
 * Created by Administrator on 2020-10-31 , 0031.
 */

public class User implements Serializable {
    private int _id;
    private String account;
    private String password;
    private String nickname;
    private String photo;

    public User() {
        super();
    }

    public User(int _id, String account, String password, String nickname, String photo) {
        this._id = _id;
        this.account = account;
        this.password = password;
        this.nickname = nickname;
        this.photo = photo;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id=" + _id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
