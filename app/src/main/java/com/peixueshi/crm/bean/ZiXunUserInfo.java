package com.peixueshi.crm.bean;

import java.io.Serializable;

public class ZiXunUserInfo implements Serializable {

   public String wf_id;
    public int wf_uid;
   public long wf_p_id;
   public String wf_p_name;
   public String wf_phone;
   public String wf_u_note;
   public int wf_c_count;
   public int net_city;
   public int wf_c_at;
   public int wf_weight;

    public int getWf_state() {
        return wf_state;
    }

    public void setWf_state(int wf_state) {
        this.wf_state = wf_state;
    }

    public int wf_state;
   public String wf_get_at;

    public boolean isShouZi;

   public String wp_phone;
   public String wp_pname;
   public String wp_call_at;
    public String wp_id;
    public String wp_u_note;
    public String wp_call_count;
    public String wp_at;
    public String wp_state;
    public String wp_weight;


    public boolean isShouZi() {
        return isShouZi;
    }

    public int getWf_weight() {
        return wf_weight;
    }

    public void setWf_weight(int wf_weight) {
        this.wf_weight = wf_weight;
    }

    public void setShouZi(boolean shouZi) {
        isShouZi = shouZi;
    }

    public String getWf_get_at() {
        return wf_get_at;
    }

    public void setWf_get_at(String wf_get_at) {
        this.wf_get_at = wf_get_at;
    }

    public String getWp_u_note() {
        return wp_u_note;
    }

    public void setWp_u_note(String wp_u_note) {
        this.wp_u_note = wp_u_note;
    }

    public String getWp_call_count() {
        return wp_call_count;
    }

    public void setWp_call_count(String wp_call_count) {
        this.wp_call_count = wp_call_count;
    }

    public String getWp_at() {
        return wp_at;
    }

    public void setWp_at(String wp_at) {
        this.wp_at = wp_at;
    }

    public String getWp_state() {
        return wp_state;
    }

    public void setWp_state(String wp_state) {
        this.wp_state = wp_state;
    }

    public String getWp_weight() {
        return wp_weight;
    }

    public void setWp_weight(String wp_weight) {
        this.wp_weight = wp_weight;
    }

    public String getWp_id() {
        return wp_id;
    }

    public void setWp_id(String wp_id) {
        this.wp_id = wp_id;
    }

    public String getWp_phone() {
        return wp_phone;
    }

    public void setWp_phone(String wp_phone) {
        this.wp_phone = wp_phone;
    }

    public String getWp_pname() {
        return wp_pname;
    }

    public void setWp_pname(String wp_pname) {
        this.wp_pname = wp_pname;
    }

    public String getWp_call_at() {
        return wp_call_at;
    }

    public void setWp_call_at(String wp_call_at) {
        this.wp_call_at = wp_call_at;
    }

    //    {"date":{"count":10,"page":1,"total":35,"collation":0,"rules":0,
//            "list":[
//          {"wp_id":69777,"wp_using_id":26829,"wp_phone":18152776482,"wp_u_note":"2","wp_uid":54,"wp_call_count":1,"wp_call_at":1586242544,"wp_pid":1001001,"wp_pname":"职业药师","wp_at":1586242537,"wp_city":0,"wp_state":0,"wp_weight":0},
//        {"wp_id":69780,"wp_using_id":26870,"wp_phone":13433272417,"wp_u_note":"","wp_uid":54,"wp_call_count":0,"wp_call_at":0,"wp_pid":1001001,"wp_pname":"职业药师","wp_at":1586242537,"wp_city":0,"wp_state":0,"wp_weight":0},
//        {"wp_id":69783,"wp_using_id":26844,"wp_phone":13654062437,"wp_u_note":"","wp_uid":54,"wp_call_count":0,"wp_call_at":0,"wp_pid":1001001,"wp_pname":"职业药师","wp_at":1586242537,"wp_city":0,"wp_state":0,"wp_weight":0},
//        {"wp_id":69786,"wp_using_id":26830,"wp_phone":18997627192,"wp_u_note":"","wp_uid":54,"wp_call_count":0,"wp_call_at":0,"wp_pid":1001001,"wp_pname":"职业药师","wp_at":1586242537,"wp_city":0,"wp_state":0,"wp_weight":0},
//        {"wp_id":69789,"wp_using_id":26862,"wp_phone":15810314782,"wp_u_note":"","wp_uid":54,"wp_call_count":0,"wp_call_at":0,"wp_pid":1001001,"wp_pname":"职业药师","wp_at":1586242537,"wp_city":0,"wp_state":0,"wp_weight":0},
//        {"wp_id":69792,"wp_using_id":26827,"wp_phone":18129325962,"wp_u_note":"","wp_uid":54,"wp_call_count":0,"wp_call_at":0,"wp_pid":1001001,"wp_pname":"职业药师","wp_at":1586242537,"wp_city":0,"wp_state":0,"wp_weight":0},
//        {"wp_id":69795,"wp_using_id":26858,"wp_phone":13798782337,"wp_u_note":"","wp_uid":54,"wp_call_count":0,"wp_call_at":0,"wp_pid":1001001,"wp_pname":"职业药师","wp_at":1586242537,"wp_city":0,"wp_state":0,"wp_weight":0},
//        {"wp_id":69798,"wp_using_id":26831,"wp_phone":18878497032,"wp_u_note":"","wp_uid":54,"wp_call_count":0,"wp_call_at":0,"wp_pid":1001001,"wp_pname":"职业药师","wp_at":1586242537,"wp_city":0,"wp_state":0,"wp_weight":0},
//        {"wp_id":69801,"wp_using_id":26391,"wp_phone":13480358802,"wp_u_note":"","wp_uid":54,"wp_call_count":0,"wp_call_at":0,"wp_pid":1001001,"wp_pname":"职业药师","wp_at":1586242537,"wp_city":0,"wp_state":0,"wp_weight":0},
//        {"wp_id":69804,"wp_using_id":26818,"wp_phone":18192823987,"wp_u_note":"","wp_uid":54,"wp_call_count":0,"wp_call_at":0,"wp_pid":1001001,"wp_pname":"职业药师","wp_at":1586242537,"wp_city":0,"wp_state":0,"wp_weight":0}
//        ]},"err":0}

    public String getWf_id() {
        return wf_id;
    }

    public void setWf_id(String wf_id) {
        this.wf_id = wf_id;
    }


    public int getWf_uid() {
        return wf_uid;
    }

    public void setWf_uid(int wf_uid) {
        this.wf_uid = wf_uid;
    }

    public long getWf_p_id() {
        return wf_p_id;
    }

    public void setWf_p_id(long wf_p_id) {
        this.wf_p_id = wf_p_id;
    }

    public String getWf_p_name() {
        return wf_p_name;
    }

    public void setWf_p_name(String wf_p_name) {
        this.wf_p_name = wf_p_name;
    }

    public String getWf_phone() {
        return wf_phone;
    }

    public void setWf_phone(String wf_phone) {
        this.wf_phone = wf_phone;
    }

    public String getWf_u_note() {
        return wf_u_note;
    }

    public void setWf_u_note(String wf_u_note) {
        this.wf_u_note = wf_u_note;
    }

    public int getWf_c_count() {
        return wf_c_count;
    }

    public void setWf_c_count(int wf_c_count) {
        this.wf_c_count = wf_c_count;
    }

    public int getNet_city() {
        return net_city;
    }

    public void setNet_city(int net_city) {
        this.net_city = net_city;
    }

    public int getWf_c_at() {
        return wf_c_at;
    }

    public void setWf_c_at(int wf_c_at) {
        this.wf_c_at = wf_c_at;
    }

}
