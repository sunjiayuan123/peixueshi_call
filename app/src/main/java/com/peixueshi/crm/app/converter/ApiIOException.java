package com.peixueshi.crm.app.converter;

import java.io.IOException;


public class ApiIOException extends IOException {

    public int code;

    public ApiIOException(String msg) {
        super(msg);
    }

}
