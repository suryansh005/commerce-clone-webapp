package com.commerce.webapp.commerceclonewebapp.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static Cookie getCookieByName(HttpServletRequest request , String name){
        Cookie []  cookies =  request.getCookies();
        for(Cookie c :  cookies){
            if(c.getName().equals(name))
                return c;
        }
        return null;
    }
    public static Cookie generateJwtCookie(String jwtToken){
        Cookie c = new Cookie("accessToken" ,jwtToken );
        c.setPath("/");
        c.setSecure(true);
        c.setHttpOnly(true);
        c.setMaxAge(5000);
        return c;
    }
    public static void deleteCookie(HttpServletResponse resp, HttpServletRequest req,String name){
        Cookie [] cookies = req.getCookies();
        for(Cookie c : cookies){
            if (c.getName().equals(name)){
                c.setMaxAge(0);
                c.setPath("/");
                c.setValue("");
                resp.addCookie(c);
                return;
            }
        }
    }
    public static Cookie generateRefreshCookie(String refreshToken) {

        Cookie c = new Cookie("refreshToken",refreshToken);
//        c.setPath("/refresh-token");
        c.setPath("/webapp/user/refresh-token");
        c.setSecure(true);
        c.setHttpOnly(true);
        c.setMaxAge(5000);
        return c;
    }
}
