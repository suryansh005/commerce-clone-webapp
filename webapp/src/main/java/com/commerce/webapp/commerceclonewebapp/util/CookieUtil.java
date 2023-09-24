package com.commerce.webapp.commerceclonewebapp.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static com.commerce.webapp.commerceclonewebapp.util.Constants.*;

public class CookieUtil {
    public static Cookie getCookieByName(HttpServletRequest request , String name){
        Cookie []  cookies =  request.getCookies();
        if(cookies!=null){
            for(Cookie c :  cookies){
                if(c.getName().equals(name))
                    return c;
            }
        }
        return null;
    }
    public static Cookie generateJwtCookie(String jwtToken){
        Cookie c = new Cookie(ACCESS_TOKEN ,jwtToken );
        c.setPath(ROOT_PATH);
        c.setSecure(true);
        c.setHttpOnly(true);
        c.setMaxAge((int)TimeUnit.DAYS.toSeconds(2));
        return c;
    }
    public static void deleteCookie(HttpServletResponse resp, HttpServletRequest req,String name){
        Cookie [] cookies = req.getCookies();
        for(Cookie c : cookies){
            if (c.getName().equals(name)){
                c.setMaxAge(0);
                c.setPath(ROOT_PATH);
                c.setValue(BLANK);
                resp.addCookie(c);
                return;
            }
        }
    }
    public static Cookie generateRefreshCookie(String refreshToken) {

        Cookie c = new Cookie(REFRESH_TOKEN,refreshToken);
        c.setPath(REFRESH_TOKEN_END_POINT);
        c.setSecure(true);
        c.setHttpOnly(true);
        c.setMaxAge(5000);
        return c;
    }
}
