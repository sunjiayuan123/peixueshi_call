package com.peixueshi.crm.bean;

import java.util.List;

public class OrderUserInfo {

   public int ord_id;
   public int ord_uid;
   public String ord_uname;
   public String ord_phone;
   public String ord_bill_no;
   public int ord_rid;
   public int ord_tid;
   public int ord_bill_type;
   public int ord_sell_price;
   public int ord_state;

    public List<OrderDetail> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDetail> orders) {
        this.orders = orders;
    }

    public List<OrderDetail> orders;

    public int getOrd_real_price() {
        return ord_real_price;
    }

    public void setOrd_real_price(int ord_real_price) {
        this.ord_real_price = ord_real_price;
    }

    public int ord_real_price;
   public String ord_real_bill;
   public String ord_pay_type;

    public String getOrd_at() {
        return ord_at;
    }

    public void setOrd_at(String ord_at) {
        this.ord_at = ord_at;
    }

    public String ord_at;
   public int ord_pay_at;
   public int ord_admin_id;
   public String ord_admin_name;
   public String ord_note;

    public int getOrd_id() {
        return ord_id;
    }

    public void setOrd_id(int ord_id) {
        this.ord_id = ord_id;
    }

    public int getOrd_uid() {
        return ord_uid;
    }

    public void setOrd_uid(int ord_uid) {
        this.ord_uid = ord_uid;
    }

    public String getOrd_uname() {
        return ord_uname;
    }

    public void setOrd_uname(String ord_uname) {
        this.ord_uname = ord_uname;
    }

    public String getOrd_phone() {
        return ord_phone;
    }

    public void setOrd_phone(String ord_phone) {
        this.ord_phone = ord_phone;
    }

    public String getOrd_bill_no() {
        return ord_bill_no;
    }

    public void setOrd_bill_no(String ord_bill_no) {
        this.ord_bill_no = ord_bill_no;
    }

    public int getOrd_rid() {
        return ord_rid;
    }

    public void setOrd_rid(int ord_rid) {
        this.ord_rid = ord_rid;
    }

    public int getOrd_tid() {
        return ord_tid;
    }

    public void setOrd_tid(int ord_tid) {
        this.ord_tid = ord_tid;
    }

    public int getOrd_bill_type() {
        return ord_bill_type;
    }

    public void setOrd_bill_type(int ord_bill_type) {
        this.ord_bill_type = ord_bill_type;
    }

    public int getOrd_sell_price() {
        return ord_sell_price;
    }

    public void setOrd_sell_price(int ord_sell_price) {
        this.ord_sell_price = ord_sell_price;
    }

    public String getOrd_real_bill() {
        return ord_real_bill;
    }

    public void setOrd_real_bill(String ord_real_bill) {
        this.ord_real_bill = ord_real_bill;
    }

    public String getOrd_pay_type() {
        return ord_pay_type;
    }

    public void setOrd_pay_type(String ord_pay_type) {
        this.ord_pay_type = ord_pay_type;
    }



    public int getOrd_pay_at() {
        return ord_pay_at;
    }

    public void setOrd_pay_at(int ord_pay_at) {
        this.ord_pay_at = ord_pay_at;
    }

    public int getOrd_admin_id() {
        return ord_admin_id;
    }

    public void setOrd_admin_id(int ord_admin_id) {
        this.ord_admin_id = ord_admin_id;
    }

    public String getOrd_admin_name() {
        return ord_admin_name;
    }

    public void setOrd_admin_name(String ord_admin_name) {
        this.ord_admin_name = ord_admin_name;
    }

    public String getOrd_note() {
        return ord_note;
    }

    public void setOrd_note(String ord_note) {
        this.ord_note = ord_note;
    }

    public int getOrd_state() {
        return ord_state;
    }

    public void setOrd_state(int ord_state) {
        this.ord_state = ord_state;
    }
}
