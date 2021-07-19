package com.peixueshi.crm.bean;

public class UserInfo {

    /**
     * err : 0
     * info : {"emp_id":9,"emp_role_id":1,"emp_team_id":3,"emp_proj_id":1004001,"emp_name":"测招生","emp_phone":"15110181819","emp_tq_acc":0,"emp_tq_pw":"0","emp_start":0,"emp_at":0,"qm_acc":"8001@bjzybx","qm_pw":"Ky!D1x6ne7o8001","mod_pass_at":1603503650}
     * is_new : false
     * need_mod_pass : false
     * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVSWQiOjksIkdJZCI6MywiUElkIjoxMDA0MDAxLCJSSWQiOjEsIk5hbWUiOiLmtYvmi5vnlJ8iLCJQaG9uZSI6IjE1MTEwMTgxODE5IiwiZXhwIjoxNjAzNTkzODUyLCJpc3MiOiJjcm0iLCJuYmYiOjE2MDM1MDc0NTJ9.7EtF9MsIiy0V4mVOouApxXYEIWF73qGOSBtpbMF3GbQ
     */

    private int err;
    private InfoBean info;
    private boolean is_new;
    private boolean need_mod_pass;
    private String token;

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public boolean isIs_new() {
        return is_new;
    }

    public void setIs_new(boolean is_new) {
        this.is_new = is_new;
    }

    public boolean isNeed_mod_pass() {
        return need_mod_pass;
    }

    public void setNeed_mod_pass(boolean need_mod_pass) {
        this.need_mod_pass = need_mod_pass;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class InfoBean {

        /**
         * emp_id : 9
         * emp_role_id : 1
         * emp_team_id : 3
         * emp_proj_id : 1004001
         * emp_name : 测招生
         * emp_phone : 15110181819
         * emp_tq_acc : 0
         * emp_tq_pw : 0
         * emp_start : 0
         * emp_at : 0
         * qm_acc : 8001@bjzybx
         * qm_pw : Ky!D1x6ne7o8001
         * mod_pass_at : 1603503650
         */

        public int emp_id;
        public int emp_role_id;
        public int emp_team_id;
        public int emp_proj_id;
        public String emp_name;
        public String emp_phone;
        public int emp_tq_acc;
        public String emp_tq_pw;
        public int emp_start;
        public int emp_at;
        public String qm_acc;
        public String qm_pw;
        public int mod_pass_at;
        public String getAcc_token() {
            return acc_token;
        }

        public void setAcc_token(String acc_token) {
            this.acc_token = acc_token;
        }

        public String acc_token;

        public int getEmp_id() {
            return emp_id;
        }

        public void setEmp_id(int emp_id) {
            this.emp_id = emp_id;
        }

        public int getEmp_role_id() {
            return emp_role_id;
        }

        public void setEmp_role_id(int emp_role_id) {
            this.emp_role_id = emp_role_id;
        }

        public int getEmp_team_id() {
            return emp_team_id;
        }

        public void setEmp_team_id(int emp_team_id) {
            this.emp_team_id = emp_team_id;
        }

        public int getEmp_proj_id() {
            return emp_proj_id;
        }

        public void setEmp_proj_id(int emp_proj_id) {
            this.emp_proj_id = emp_proj_id;
        }

        public String getEmp_name() {
            return emp_name;
        }

        public void setEmp_name(String emp_name) {
            this.emp_name = emp_name;
        }

        public String getEmp_phone() {
            return emp_phone;
        }

        public void setEmp_phone(String emp_phone) {
            this.emp_phone = emp_phone;
        }

        public int getEmp_tq_acc() {
            return emp_tq_acc;
        }

        public void setEmp_tq_acc(int emp_tq_acc) {
            this.emp_tq_acc = emp_tq_acc;
        }

        public String getEmp_tq_pw() {
            return emp_tq_pw;
        }

        public void setEmp_tq_pw(String emp_tq_pw) {
            this.emp_tq_pw = emp_tq_pw;
        }

        public int getEmp_start() {
            return emp_start;
        }

        public void setEmp_start(int emp_start) {
            this.emp_start = emp_start;
        }

        public int getEmp_at() {
            return emp_at;
        }

        public void setEmp_at(int emp_at) {
            this.emp_at = emp_at;
        }

        public String getQm_acc() {
            return qm_acc;
        }

        public void setQm_acc(String qm_acc) {
            this.qm_acc = qm_acc;
        }

        public String getQm_pw() {
            return qm_pw;
        }

        public void setQm_pw(String qm_pw) {
            this.qm_pw = qm_pw;
        }

        public int getMod_pass_at() {
            return mod_pass_at;
        }

        public void setMod_pass_at(int mod_pass_at) {
            this.mod_pass_at = mod_pass_at;
        }
    }
}
