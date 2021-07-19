package com.peixueshi.crm.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class SubmiteBean {
    /**
     * phone : 18710226073
     * billtype : 1
     * totalPrice : 4000
     * notes : 网课
     * orderType : 1
     * customerId : 12310000000001
     * infos : [{"projectId":1001001,"projectName":"执业药师","classId":261,"className":"中药师名师尊享班","price":1000},{"projectId":1001001,"projectName":"执业药师","classId":264,"className":"西药药师名师尊享班","price":1000}]
     */

    @SerializedName("phone")
    private String phoneX;
    private int billtype;
    private String totalPrice;
    private String notes;
    private int orderType;
    private String customerId;
    private List<Map<String,Object>> infos;

    public String getPhoneX() {
        return phoneX;
    }

    public void setPhoneX(String phoneX) {
        this.phoneX = phoneX;
    }

    public int getBilltype() {
        return billtype;
    }

    public void setBilltype(int billtype) {
        this.billtype = billtype;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<Map<String,Object>> getInfos() {
        return infos;
    }

    public void setInfos(List<Map<String,Object>> infos) {
        this.infos = infos;
    }

    public static class InfosBean {
        /**
         * projectId : 1001001
         * projectName : 执业药师
         * classId : 261
         * className : 中药师名师尊享班
         * price : 1000
         */

        private int projectId;
        private String projectName;
        private int classId;
        private String className;
        private int price;

        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public int getClassId() {
            return classId;
        }

        public void setClassId(int classId) {
            this.classId = classId;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }

}
