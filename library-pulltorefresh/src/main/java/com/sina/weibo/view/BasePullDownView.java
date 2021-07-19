package com.sina.weibo.view;


/**
 *
 * @auth zhaobaolei 2018-1-22
 */
public class BasePullDownView{
    private PullDownView.UpdateHandle updateHandle = new PullDownView.UpdateHandle() {
        @Override
        public void onUpdate() {
            refresh();
        }
    };

    public PullDownView.UpdateHandle getUpdateHandle() {
        return updateHandle;
    }

    public void refresh(){
    }


}
