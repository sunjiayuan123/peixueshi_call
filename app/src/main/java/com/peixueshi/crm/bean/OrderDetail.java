package com.peixueshi.crm.bean;

public class OrderDetail {

    /* "sch_id": 2,
             "sch_sid": 1000,
             "sch_sname": "河南师范",
             "sch_ssion_id": 2000,
             "sch_ssion_name": "计算机",
             "sch_sc_number": 1,
             "sch_test_type": 1,
             "sch_uname": "张三",
             "sch_number_id": "410821199509221356",
             "sch_domicile": "河南省焦作市",
             "sch_order_id": 14
*/
    public int sch_id;
    public int sch_sid;
    public String sch_sname;
    public int sch_ssion_id;
    public String sch_ssion_name;
    public int sch_sc_number;
    public int sch_test_type;
    public String sch_uname;
    public String sch_number_id;
    public String sch_domicile;
    public int sch_order_id;

    public String getStudy_type() {
        return study_type;
    }

    public void setStudy_type(String study_type) {
        this.study_type = study_type;
    }

    public String study_type;

    public OrderDetail(String zhuanye, String name, String location,String stydy) {
        this.sch_ssion_name = zhuanye;
        this.sch_uname = name;
        this.sch_domicile = location;
        this.study_type = stydy;
    }

    public OrderDetail() {
    }

    public int getSch_id() {
        return sch_id;
    }

    public void setSch_id(int sch_id) {
        this.sch_id = sch_id;
    }

    public int getSch_sid() {
        return sch_sid;
    }

    public void setSch_sid(int sch_sid) {
        this.sch_sid = sch_sid;
    }

    public String getSch_sname() {
        return sch_sname;
    }

    public void setSch_sname(String sch_sname) {
        this.sch_sname = sch_sname;
    }

    public int getSch_ssion_id() {
        return sch_ssion_id;
    }

    public void setSch_ssion_id(int sch_ssion_id) {
        this.sch_ssion_id = sch_ssion_id;
    }

    public String getSch_ssion_name() {
        return sch_ssion_name;
    }

    public void setSch_ssion_name(String sch_ssion_name) {
        this.sch_ssion_name = sch_ssion_name;
    }

    public int getSch_sc_number() {
        return sch_sc_number;
    }

    public void setSch_sc_number(int sch_sc_number) {
        this.sch_sc_number = sch_sc_number;
    }

    public int getSch_test_type() {
        return sch_test_type;
    }

    public void setSch_test_type(int sch_test_type) {
        this.sch_test_type = sch_test_type;
    }

    public String getSch_uname() {
        return sch_uname;
    }

    public void setSch_uname(String sch_uname) {
        this.sch_uname = sch_uname;
    }

    public String getSch_number_id() {
        return sch_number_id;
    }

    public void setSch_number_id(String sch_number_id) {
        this.sch_number_id = sch_number_id;
    }

    public String getSch_domicile() {
        return sch_domicile;
    }

    public void setSch_domicile(String sch_domicile) {
        this.sch_domicile = sch_domicile;
    }

    public int getSch_order_id() {
        return sch_order_id;
    }

    public void setSch_order_id(int sch_order_id) {
        this.sch_order_id = sch_order_id;
    }
}
