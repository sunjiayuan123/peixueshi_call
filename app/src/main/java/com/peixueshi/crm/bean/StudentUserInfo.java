package com.peixueshi.crm.bean;

import java.io.Serializable;

public class StudentUserInfo implements Serializable {
//    {"date":{"count":10,"page":0,"total":9,"collation":0,"rules":1,
//            "list":[{"cstu_id":11742,"cstu_weight":0,"cstu_phone":"15649053728","cstu_at":1587009531,"cstu_amount":200,
//            "cstu_name":"","cstu_senior":1,"cstu_uid":21,"cstu_uname":"聂梦笑","cstu_uat":0,"cstu_order_id":2214,
//            "cstu_c_number":0,"cstu_c_at":0,"cstu_note":"教务老师","stuinfo_id":13083,"stuinfo_phone":"15649053728",
//            "stuinfo_name":"15649053728","stuinfo_at":1587009514,"stuinfo_last_at":1588565699,"stuinfo_coun":14,
//            "stu_email":"","stu_address":"","stu_start":0}
//                  ,{"cstu_id":1665,"cstu_weight":0,"cstu_phone":"18333966659","cstu_at":1586757549,"cstu_amount":700,"cstu_name":"","cstu_senior":1,"cstu_uid":21,"cstu_uname":"聂梦笑","cstu_uat":0,"cstu_order_id":1665,"cstu_c_number":0,"cstu_c_at":0,"cstu_note":"","stuinfo_id":2637,"stuinfo_phone":"18333966659","stuinfo_name":"测试","stuinfo_at":1586757538,"stuinfo_last_at":1588746801,"stuinfo_coun":691,"stu_email":"","stu_address":"","stu_start":0},{"cstu_id":1662,"cstu_weight":0,"cstu_phone":"19803091863","cstu_at":1586749860,"cstu_amount":700,"cstu_name":"","cstu_senior":1,"cstu_uid":21,"cstu_uname":"聂梦笑","cstu_uat":0,"cstu_order_id":1656,"cstu_c_number":0,"cstu_c_at":0,"cstu_note":"","stuinfo_id":2634,"stuinfo_phone":"19803091863","stuinfo_name":"","stuinfo_at":1586749845,"stuinfo_last_at":1588644599,"stuinfo_coun":24,"stu_email":"","stu_address":"","stu_start":0},{"cstu_id":1659,"cstu_weight":0,"cstu_phone":"18737332693","cstu_at":1586748456,"cstu_amount":100,"cstu_name":"","cstu_senior":1,"cstu_uid":21,"cstu_uname":"聂梦笑","cstu_uat":0,"cstu_order_id":1647,"cstu_c_number":0,"cstu_c_at":0,"cstu_note":"","stuinfo_id":2631,"stuinfo_phone":"18737332693","stuinfo_name":"","stuinfo_at":1586748422,"stuinfo_last_at":1588723798,"stuinfo_coun":26,"stu_email":"","stu_address":"","stu_start":0},{"cstu_id":1656,"cstu_weight":0,"cstu_phone":"18236993950","cstu_at":1586748450,"cstu_amount":100,"cstu_name":"","cstu_senior":1,"cstu_uid":21,"cstu_uname":"聂梦笑","cstu_uat":0,"cstu_order_id":1650,"cstu_c_number":0,"cstu_c_at":0,"cstu_note":"","stuinfo_id":2628,"stuinfo_phone":"18236993950","stuinfo_name":"","stuinfo_at":1586748420,"stuinfo_last_at":1588728776,"stuinfo_coun":19,"stu_email":"","stu_address":"","stu_start":0},{"cstu_id":1653,"cstu_weight":0,"cstu_phone":"18838957034","cstu_at":1586748443,"cstu_amount":100,"cstu_name":"","cstu_senior":1,"cstu_uid":21,"cstu_uname":"聂梦笑","cstu_uat":0,"cstu_order_id":1653,"cstu_c_number":0,"cstu_c_at":0,"cstu_note":"","stuinfo_id":2625,"stuinfo_phone":"18838957034","stuinfo_name":"","stuinfo_at":1586748414,"stuinfo_last_at":1588731861,"stuinfo_coun":10,"stu_email":"","stu_address":"","stu_start":0},{"cstu_id":1650,"cstu_weight":0,"cstu_phone":"18838079878","cstu_at":1586747353,"cstu_amount":200,"cstu_name":"","cstu_senior":1,"cstu_uid":21,"cstu_uname":"聂梦笑","cstu_uat":0,"cstu_order_id":1635,"cstu_c_number":0,"cstu_c_at":0,"cstu_note":"教务测试","stuinfo_id":2622,"stuinfo_phone":"18838079878","stuinfo_name":"","stuinfo_at":1586747331,"stuinfo_last_at":1588730151,"stuinfo_coun":34,"stu_email":"","stu_address":"","stu_start":0},{"cstu_id":1647,"cstu_weight":0,"cstu_phone":"18703825326","cstu_at":1586746792,"cstu_amount":700,"cstu_name":"","cstu_senior":1,"cstu_uid":21,"cstu_uname":"聂梦笑","cstu_uat":0,"cstu_order_id":1632,"cstu_c_number":0,"cstu_c_at":0,"cstu_note":"测试使用","stuinfo_id":345,"stuinfo_phone":"18703825326","stuinfo_name":"づ有刚","stuinfo_at":1586426088,"stuinfo_last_at":1588038227,"stuinfo_coun":9,"stu_email":"","stu_address":"","stu_start":0},{"cstu_id":3,"cstu_weight":0,"cstu_phone":"13216123186","cstu_at":1585394595,"cstu_amount":1280000,"cstu_name":"","cstu_senior":1,"cstu_uid":21,"cstu_uname":"测班主任","cstu_uat":0,"cstu_order_id":18,"cstu_c_number":0,"cstu_c_at":0,"cstu_note":"测试","stuinfo_id":24,"stuinfo_phone":"13216123186","stuinfo_name":"榴莲味的方便面","stuinfo_at":1585394563,"stuinfo_last_at":1588579638,"stuinfo_coun":26,"stu_email":"","stu_address":"","stu_start":0}]},"err":0}

    public int cstu_id;
    public int cstu_weight;
    public String cstu_phone;
    public String cstu_at;
    public String cstu_amount;
    public String cstu_name;
    public String cstu_senior;
    public String cstu_uid;

    public String cstu_uname;
    public int cstu_uat;
    public int cstu_order_id;
    public int cstu_c_number;
    public int cstu_c_at;
    public String cstu_note;

    public int stuinfo_id;
    public String stuinfo_phone;
    public String stuinfo_name;
    public String stuinfo_at;
    public String stuinfo_last_at;
    public int stuinfo_coun;
    public String stu_email;
    public String stu_address;
    public String stu_start;

    public int getCstu_id() {
        return cstu_id;
    }

    public void setCstu_id(int cstu_id) {
        this.cstu_id = cstu_id;
    }

    public int getCstu_weight() {
        return cstu_weight;
    }

    public void setCstu_weight(int cstu_weight) {
        this.cstu_weight = cstu_weight;
    }

    public String getCstu_phone() {
        return cstu_phone;
    }

    public void setCstu_phone(String cstu_phone) {
        this.cstu_phone = cstu_phone;
    }

    public String getCstu_at() {
        return cstu_at;
    }

    public void setCstu_at(String cstu_at) {
        this.cstu_at = cstu_at;
    }

    public String getCstu_amount() {
        return cstu_amount;
    }

    public void setCstu_amount(String cstu_amount) {
        this.cstu_amount = cstu_amount;
    }

    public String getCstu_name() {
        return cstu_name;
    }

    public void setCstu_name(String cstu_name) {
        this.cstu_name = cstu_name;
    }

    public String getCstu_senior() {
        return cstu_senior;
    }

    public void setCstu_senior(String cstu_senior) {
        this.cstu_senior = cstu_senior;
    }

    public String getCstu_uid() {
        return cstu_uid;
    }

    public void setCstu_uid(String cstu_uid) {
        this.cstu_uid = cstu_uid;
    }

    public String getCstu_uname() {
        return cstu_uname;
    }

    public void setCstu_uname(String cstu_uname) {
        this.cstu_uname = cstu_uname;
    }

    public int getCstu_uat() {
        return cstu_uat;
    }

    public void setCstu_uat(int cstu_uat) {
        this.cstu_uat = cstu_uat;
    }

    public int getCstu_order_id() {
        return cstu_order_id;
    }

    public void setCstu_order_id(int cstu_order_id) {
        this.cstu_order_id = cstu_order_id;
    }

    public int getCstu_c_number() {
        return cstu_c_number;
    }

    public void setCstu_c_number(int cstu_c_number) {
        this.cstu_c_number = cstu_c_number;
    }

    public int getCstu_c_at() {
        return cstu_c_at;
    }

    public void setCstu_c_at(int cstu_c_at) {
        this.cstu_c_at = cstu_c_at;
    }

    public String getCstu_note() {
        return cstu_note;
    }

    public void setCstu_note(String cstu_note) {
        this.cstu_note = cstu_note;
    }

    public int getStuinfo_id() {
        return stuinfo_id;
    }

    public void setStuinfo_id(int stuinfo_id) {
        this.stuinfo_id = stuinfo_id;
    }

    public String getStuinfo_phone() {
        return stuinfo_phone;
    }

    public void setStuinfo_phone(String stuinfo_phone) {
        this.stuinfo_phone = stuinfo_phone;
    }

    public String getStuinfo_name() {
        return stuinfo_name;
    }

    public void setStuinfo_name(String stuinfo_name) {
        this.stuinfo_name = stuinfo_name;
    }

    public String getStuinfo_at() {
        return stuinfo_at;
    }

    public void setStuinfo_at(String stuinfo_at) {
        this.stuinfo_at = stuinfo_at;
    }

    public String getStuinfo_last_at() {
        return stuinfo_last_at;
    }

    public void setStuinfo_last_at(String stuinfo_last_at) {
        this.stuinfo_last_at = stuinfo_last_at;
    }

    public int getStuinfo_coun() {
        return stuinfo_coun;
    }

    public void setStuinfo_coun(int stuinfo_coun) {
        this.stuinfo_coun = stuinfo_coun;
    }

    public String getStu_email() {
        return stu_email;
    }

    public void setStu_email(String stu_email) {
        this.stu_email = stu_email;
    }

    public String getStu_address() {
        return stu_address;
    }

    public void setStu_address(String stu_address) {
        this.stu_address = stu_address;
    }

    public String getStu_start() {
        return stu_start;
    }

    public void setStu_start(String stu_start) {
        this.stu_start = stu_start;
    }
}
