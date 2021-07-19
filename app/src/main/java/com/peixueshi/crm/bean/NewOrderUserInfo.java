package com.peixueshi.crm.bean;

import java.util.List;

public class NewOrderUserInfo {
    /**
     * orderId : 390
     * commpId : 1
     * customerId : 10810000005020
     * phone : 19600002020
     * billNo : G0100120201117027600
     * billType : 1
     * payType : 0
     * orderType : 1
     * userId : 423
     * userName : 柴进
     * teachId : 0
     * teachName :
     * showId : 20001000100030003
     * showName :
     * totalPrice : 1
     * createdAt : 1605583250
     * payAt : 0
     * notes : app
     * status : 1
     * cancelAt : 0
     * reasonAt : 0
     * reason :
     * details : [{"infoId":405,"orderId":390,"projectId":1002003,"projectName":"中级会计师","classId":312,"className":"2020年中级会计【名师尊享班】","schoolId":0,"schoolName":"","majorId":0,"majorName":"","mStatus":0,"sStatus":0,"sStaName":"","studentName":"","idCard":"","address":"","certName":"","oAddress":"","price":1,"oStatus":1,"oChange":1,"oClose":1,"updateAt":1605583250,"closeAt":0}]
     */

    public int orderId;
    public int commpId;
    public String customerId;
    public String phone;
    public String billNo;
    public int billType;
    public int payType;
    public int orderType;
    public int userId;
    public String userName;
    public int teachId;
    public String teachName;
    public long showId;
    public String showName;
    public int totalPrice;
    public String createdAt;
    public String payAt;
    public String notes;
    public int status;
    public int cancelAt;
    public int reasonAt;
    public String reason;
    public List<DetailsBean> details;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCommpId() {
        return commpId;
    }

    public void setCommpId(int commpId) {
        this.commpId = commpId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public int getBillType() {
        return billType;
    }

    public void setBillType(int billType) {
        this.billType = billType;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTeachId() {
        return teachId;
    }

    public void setTeachId(int teachId) {
        this.teachId = teachId;
    }

    public String getTeachName() {
        return teachName;
    }

    public void setTeachName(String teachName) {
        this.teachName = teachName;
    }

    public long getShowId() {
        return showId;
    }

    public void setShowId(long showId) {
        this.showId = showId;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPayAt() {
        return payAt;
    }

    public void setPayAt(String payAt) {
        this.payAt = payAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCancelAt() {
        return cancelAt;
    }

    public void setCancelAt(int cancelAt) {
        this.cancelAt = cancelAt;
    }

    public int getReasonAt() {
        return reasonAt;
    }

    public void setReasonAt(int reasonAt) {
        this.reasonAt = reasonAt;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<DetailsBean> getDetails() {
        return details;
    }

    public void setDetails(List<DetailsBean> details) {
        this.details = details;
    }

}
