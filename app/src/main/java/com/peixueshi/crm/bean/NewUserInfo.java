package com.peixueshi.crm.bean;

public class NewUserInfo {


    /**
     * code : 0
     * data : {"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJDSWQiOjEsIlVJZCI6NDIzLCJQSWQiOjAsIkhJZCI6MTAwMzQyLCJXb3JrSWQiOjEwMDUxOCwiU2hvd0lkIjpbMjAwMDEwMDAxMDAwMzAwMDNdLCJSb2xlSWQiOjAsIk5hbWUiOiLmn7Tov5siLCJQaG9uZSI6IjE1NjI1NzY5ODQ5In0.aNEyUD_-mAOh6Eo7mqhBgiMTT_KZHyk6GBU_YjdszKg","staff":{"id":423,"cId":1,"workId":100518,"name":"柴进","phone":"15625769849","showId":0,"roleId":0,"wId":0,"hId":100342,"status":0,"message":"登录成功"}}
     * msg : 🆗
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJDSWQiOjEsIlVJZCI6NDIzLCJQSWQiOjAsIkhJZCI6MTAwMzQyLCJXb3JrSWQiOjEwMDUxOCwiU2hvd0lkIjpbMjAwMDEwMDAxMDAwMzAwMDNdLCJSb2xlSWQiOjAsIk5hbWUiOiLmn7Tov5siLCJQaG9uZSI6IjE1NjI1NzY5ODQ5In0.aNEyUD_-mAOh6Eo7mqhBgiMTT_KZHyk6GBU_YjdszKg
         * staff : {"id":423,"cId":1,"workId":100518,"name":"柴进","phone":"15625769849","showId":0,"roleId":0,"wId":0,"hId":100342,"status":0,"message":"登录成功"}
         */

        private String token;
        private StaffBean staff;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public StaffBean getStaff() {
            return staff;
        }

        public void setStaff(StaffBean staff) {
            this.staff = staff;
        }

        public static class StaffBean {
            /**
             * id : 423
             * cId : 1
             * workId : 100518
             * name : 柴进
             * phone : 15625769849
             * showId : 0
             * roleId : 0
             * wId : 0
             * hId : 100342
             * status : 0
             * message : 登录成功
             */

            public int id;
            public int cId;
            public int workId;
            public String name;
            public String phone;
            public int showId;
            public int roleId;
            public int wId;
            public int hId;
            public int status;
            public String message;

            public String acc_token;
            public String getAcc_token() {
                return acc_token;
            }

            public void setAcc_token(String acc_token) {
                this.acc_token = acc_token;
            }
            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getCId() {
                return cId;
            }

            public void setCId(int cId) {
                this.cId = cId;
            }

            public int getWorkId() {
                return workId;
            }

            public void setWorkId(int workId) {
                this.workId = workId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public int getShowId() {
                return showId;
            }

            public void setShowId(int showId) {
                this.showId = showId;
            }

            public int getRoleId() {
                return roleId;
            }

            public void setRoleId(int roleId) {
                this.roleId = roleId;
            }

            public int getWId() {
                return wId;
            }

            public void setWId(int wId) {
                this.wId = wId;
            }

            public int getHId() {
                return hId;
            }

            public void setHId(int hId) {
                this.hId = hId;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }
        }
    }
}
