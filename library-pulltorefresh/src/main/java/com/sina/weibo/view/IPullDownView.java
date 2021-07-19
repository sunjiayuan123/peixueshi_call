package com.sina.weibo.view;


import java.util.Date;

public interface IPullDownView {
    void updateWithoutOffset();

    boolean isEnable();

    void update();

    void endUpdate(Date var1);

    void setUpdateHandle(IUpdateHandle var1);

    public interface IUpdateHandle {
        void onUpdate();
    }
}
