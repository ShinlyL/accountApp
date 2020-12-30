package com.example.administrator.accountbook;

import java.io.Serializable;

/**
 * Created by Administrator on 2020-10-31 , 0031.
 */

public class Acbook implements Serializable{
    private int _id;
    private String detail;
    private String flag;
    private String date;
    private String money;
    private String number;

    public Acbook() {
    }

    public Acbook(int _id, String detail, String flag, String date, String money, String number) {
        this._id = _id;
        this.detail = detail;
        this.flag = flag;
        this.date = date;
        this.money = money;
        this.number = number;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Acbook{" +
                "_id=" + _id +
                ", detail='" + detail + '\'' +
                ", flag='" + flag + '\'' +
                ", date='" + date + '\'' +
                ", money='" + money + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
