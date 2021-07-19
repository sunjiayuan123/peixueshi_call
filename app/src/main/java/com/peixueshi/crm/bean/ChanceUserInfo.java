package com.peixueshi.crm.bean;

import java.io.Serializable;

public class ChanceUserInfo implements Serializable {
    /**
     * workPool : {"id":10710000004393,"cId":1,"bId":4393,"pId":107,"ppid":1002002,"pName":"注册会计师","uId":423,"uName":"","phone":19556746635,"showId":20001000100030003,"showName":"营销中心第一事业部第一军团四部招生三组","levels":0,"info":"报考科目:注册会计师--您的学历:本科--所学专业:金融类--工作年限:3-5年","callAt":0,"callCount":0,"logic":1,"firstAt":0,"at":1605853994,"noteAt":0,"testAddre":"","submit":0}
     * workProTg : {"Id":10710000004393,"bId":4393,"channelId":36,"wayId":30,"sId":3,"info":"报考科目:注册会计师--您的学历:本科--所学专业:金融类--工作年限:3-5年","sUrl":"https://www.chengzijianzhan.com/tetris/page/6888604938714054670/"}
     */

    public WorkPoolBean workPool;
    public WorkProTgBean workProTg;
    public boolean isShouZi;

    public boolean isShouZi() {
        return isShouZi;
    }

    public void setShouZi(boolean shouZi) {
        isShouZi = shouZi;
    }

    public WorkPoolBean getWorkPool() {
        return workPool;
    }

    public void setWorkPool(WorkPoolBean workPool) {
        this.workPool = workPool;
    }

    public WorkProTgBean getWorkProTg() {
        return workProTg;
    }

    public void setWorkProTg(WorkProTgBean workProTg) {
        this.workProTg = workProTg;
    }

    public static class WorkPoolBean {
        /**
         * id : 10710000004393
         * cId : 1
         * bId : 4393
         * pId : 107
         * ppid : 1002002
         * pName : 注册会计师
         * uId : 423
         * uName :
         * phone : 19556746635
         * showId : 20001000100030003
         * showName : 营销中心第一事业部第一军团四部招生三组
         * levels : 0
         * info : 报考科目:注册会计师--您的学历:本科--所学专业:金融类--工作年限:3-5年
         * callAt : 0
         * callCount : 0
         * logic : 1
         * firstAt : 0
         * at : 1605853994
         * noteAt : 0
         * testAddre :
         * submit : 0
         */

        public String id;
        public int cId;
        public int bId;
        public int pId;
        public int ppid;
        public String pName;
        public String uId;
        public String uName;
        public String phone;
        public long showId;
        public String showName;
        public int levels;
        public String info;
        public int callAt;
        public int callCount;
        public int logic;
        public int firstAt;
        public int at;
        public int noteAt;
        public String testAddre;
        public int submit;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getCId() {
            return cId;
        }

        public void setCId(int cId) {
            this.cId = cId;
        }

        public int getBId() {
            return bId;
        }

        public void setBId(int bId) {
            this.bId = bId;
        }

        public int getPId() {
            return pId;
        }

        public void setPId(int pId) {
            this.pId = pId;
        }

        public int getPpid() {
            return ppid;
        }

        public void setPpid(int ppid) {
            this.ppid = ppid;
        }

        public String getPName() {
            return pName;
        }

        public void setPName(String pName) {
            this.pName = pName;
        }

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
        }

        public String getUName() {
            return uName;
        }

        public void setUName(String uName) {
            this.uName = uName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
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

        public int getLevels() {
            return levels;
        }

        public void setLevels(int levels) {
            this.levels = levels;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public int getCallAt() {
            return callAt;
        }

        public void setCallAt(int callAt) {
            this.callAt = callAt;
        }

        public int getCallCount() {
            return callCount;
        }

        public void setCallCount(int callCount) {
            this.callCount = callCount;
        }

        public int getLogic() {
            return logic;
        }

        public void setLogic(int logic) {
            this.logic = logic;
        }

        public int getFirstAt() {
            return firstAt;
        }

        public void setFirstAt(int firstAt) {
            this.firstAt = firstAt;
        }

        public int getAt() {
            return at;
        }

        public void setAt(int at) {
            this.at = at;
        }

        public int getNoteAt() {
            return noteAt;
        }

        public void setNoteAt(int noteAt) {
            this.noteAt = noteAt;
        }

        public String getTestAddre() {
            return testAddre;
        }

        public void setTestAddre(String testAddre) {
            this.testAddre = testAddre;
        }

        public int getSubmit() {
            return submit;
        }

        public void setSubmit(int submit) {
            this.submit = submit;
        }
    }

    public static class WorkProTgBean {
        /**
         * Id : 10710000004393
         * bId : 4393
         * channelId : 36
         * wayId : 30
         * sId : 3
         * info : 报考科目:注册会计师--您的学历:本科--所学专业:金融类--工作年限:3-5年
         * sUrl : https://www.chengzijianzhan.com/tetris/page/6888604938714054670/
         */

        public long Id;
        public int bId;
        public int channelId;
        public int wayId;
        public int sId;
        public String info;
        public String sUrl;

        public long getId() {
            return Id;
        }

        public void setId(long Id) {
            this.Id = Id;
        }

        public int getBId() {
            return bId;
        }

        public void setBId(int bId) {
            this.bId = bId;
        }

        public int getChannelId() {
            return channelId;
        }

        public void setChannelId(int channelId) {
            this.channelId = channelId;
        }

        public int getWayId() {
            return wayId;
        }

        public void setWayId(int wayId) {
            this.wayId = wayId;
        }

        public int getSId() {
            return sId;
        }

        public void setSId(int sId) {
            this.sId = sId;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getSUrl() {
            return sUrl;
        }

        public void setSUrl(String sUrl) {
            this.sUrl = sUrl;
        }
    }
}
