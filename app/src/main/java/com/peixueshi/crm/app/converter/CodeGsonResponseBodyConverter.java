package com.peixueshi.crm.app.converter;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;


public class CodeGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private boolean isBaseResponse;

    CodeGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, boolean isBaseResponse) {
        this.gson = gson;
        this.adapter = adapter;
        this.isBaseResponse = isBaseResponse;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String results = value.string();
        try {
            JSONObject object = new JSONObject(results);
            if (!"1".equals(object.optString("code"))) {
                throw new ApiIOException(object.optString("message"));
            }

//            if (!isBaseResponse) {
//                results = object.optString("message");
//            }

            Reader reader = new StringReader(results);
            JsonReader jsonReader = gson.newJsonReader(reader);
//            jsonReader.setLenient(true);
            T result = adapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }

            return result;
        } catch (JSONException e) {
            throw new ApiIOException(e.getMessage());
        } finally {
            value.close();
        }
    }
}