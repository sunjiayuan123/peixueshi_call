package com.peixueshi.crm.bean;

public class ShowZiType {
//    {"pid":1001001,"pname":"执业药师","count":10004,"p_at":1591588314,"often":300}
    public String pid;
    public String pname;
    public int count;
    public long p_at;
    public int ofter;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getP_at() {
        return p_at;
    }

    public void setP_at(long p_at) {
        this.p_at = p_at;
    }

    public int getOfter() {
        return ofter;
    }

    public void setOfter(int ofter) {
        this.ofter = ofter;
    }
}
