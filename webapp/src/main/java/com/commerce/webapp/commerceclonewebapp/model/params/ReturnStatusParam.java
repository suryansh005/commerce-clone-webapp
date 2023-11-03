package com.commerce.webapp.commerceclonewebapp.model.params;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import lombok.Builder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder
public class ReturnStatusParam implements Serializable {
    private boolean success = true;
    private int statusCode =  200;
    private String message = "success";
    List< ?  extends Object> data = new ArrayList<>();

    public String toJson(){
        return new JSONSerializer().exclude("*.class").deepSerialize(this);
    }

    public boolean isSuccess() {
        return success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public List<? extends Object> getData() {
        return data;
    }
}
