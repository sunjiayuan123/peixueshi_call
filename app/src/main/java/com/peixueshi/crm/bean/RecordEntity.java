package com.peixueshi.crm.bean;
/**
 * Created by zbl on 2020/4/16.
 */

public class RecordEntity {
    public String name;
    public String number;
    public int type;
    public long lDate;
    public long duration;
    public int _new;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getlDate() {
        return lDate;
    }

    public void setlDate(long lDate) {
        this.lDate = lDate;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int get_new() {
        return _new;
    }

    public void set_new(int _new) {
        this._new = _new;
    }
}
