package com.peixueshi.crm.app.inter;

import org.json.JSONObject;

/**
 * Created by admin on 2018/1/24.
 */

public abstract class OkhttpCallback<T> {

    public void onBefore(){

    }

//    public abstract void onFailure(Request request, IOException e);
    public abstract void onFailure(String message);
    public abstract void onGetResult(T object);
    public abstract T parseNetworkResponse(JSONObject response) throws Exception;

}
