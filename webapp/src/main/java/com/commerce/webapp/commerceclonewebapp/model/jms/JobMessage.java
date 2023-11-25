package com.commerce.webapp.commerceclonewebapp.model.jms;

import flexjson.JSONSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobMessage implements Serializable {

    private static final long serialVersionUID = -295422703255886286L;

    private String email;
    private JobType jobType;

    public String toJson(){
        return new JSONSerializer().exclude("*.class").deepSerialize(this);
    }

}
