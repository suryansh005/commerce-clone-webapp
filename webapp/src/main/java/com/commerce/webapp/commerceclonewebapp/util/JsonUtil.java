package com.commerce.webapp.commerceclonewebapp.util;

import com.commerce.webapp.commerceclonewebapp.model.params.ReturnStatusParam;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class JsonUtil {
    public static ResponseEntity<String> genericLoginSuccess(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        ReturnStatusParam returnStatusParam = ReturnStatusParam.builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .build();
        String returnStatusParamStr = returnStatusParam.toJson();

        return new ResponseEntity<String>(returnStatusParamStr,headers,HttpStatus.OK);
    }

    public static ResponseEntity<String> genericLoginRedirect(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        ReturnStatusParam returnStatusParam = ReturnStatusParam.builder()
                .success(true)
                .statusCode(HttpStatus.MOVED_TEMPORARILY.value())
                .build();
        String returnStatusParamStr = returnStatusParam.toJson();

        return new ResponseEntity<String>(returnStatusParamStr,headers,HttpStatus.MOVED_TEMPORARILY);
    }
    public static ResponseEntity<String> genericLoginError(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        ReturnStatusParam returnStatusParam = ReturnStatusParam.builder()
                .success(false)
                .message("Authenticate again")
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        String returnStatusParamStr = returnStatusParam.toJson();

        return new ResponseEntity<String>(returnStatusParamStr,headers,HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<String> genericUnauthorized() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        ReturnStatusParam returnStatusParam = ReturnStatusParam.builder()
                .success(false)
                .message("Authenticate again")
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        String returnStatusParamStr = returnStatusParam.toJson();

        return new ResponseEntity<String>(returnStatusParamStr,headers,HttpStatus.UNAUTHORIZED);

    }

    public static ResponseEntity<String> genericSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        ReturnStatusParam returnStatusParam = ReturnStatusParam.builder()
                .success(false)
                .message("Successful")
                .statusCode(HttpStatus.OK.value())
                .build();
        String returnStatusParamStr = returnStatusParam.toJson();

        return new ResponseEntity<String>(returnStatusParamStr,headers,HttpStatus.OK);

    }
}
