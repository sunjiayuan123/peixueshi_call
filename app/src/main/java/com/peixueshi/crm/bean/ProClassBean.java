package com.peixueshi.crm.bean;

import com.google.gson.annotations.SerializedName;

public class ProClassBean {

    /**
     * class : {"id":1871,"pId":1001001,"name":"2020年zxx测试生成pdf","price":1,"at":1603164213,"start":1603123200,"agreement":2,"end":1633535999,"validTeam":60,"OnYear":2020}
     * pro : {"Id":1001001,"Name":"执业药师"}
     */

    @SerializedName("class")
    public ClassBean classX;
    public ProBean pro;

    public ClassBean getClassX() {
        return classX;
    }

    public void setClassX(ClassBean classX) {
        this.classX = classX;
    }

    public ProBean getPro() {
        return pro;
    }

    public void setPro(ProBean pro) {
        this.pro = pro;
    }

    public static class ClassBean {
        /**
         * id : 1871
         * pId : 1001001
         * name : 2020年zxx测试生成pdf
         * price : 1
         * at : 1603164213
         * start : 1603123200
         * agreement : 2
         * end : 1633535999
         * validTeam : 60
         * OnYear : 2020
         */

        public int id;
        public int pId;
        public String name;
        public int price;
        public int at;
        public int start;
        public int agreement;
        public int end;
        public int validTeam;
        public int OnYear;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPId() {
            return pId;
        }

        public void setPId(int pId) {
            this.pId = pId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getAt() {
            return at;
        }

        public void setAt(int at) {
            this.at = at;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getAgreement() {
            return agreement;
        }

        public void setAgreement(int agreement) {
            this.agreement = agreement;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public int getValidTeam() {
            return validTeam;
        }

        public void setValidTeam(int validTeam) {
            this.validTeam = validTeam;
        }

        public int getOnYear() {
            return OnYear;
        }

        public void setOnYear(int OnYear) {
            this.OnYear = OnYear;
        }
    }

    public static class ProBean {
        /**
         * Id : 1001001
         * Name : 执业药师
         */

        public int Id;
        public String Name;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }
    }
}
