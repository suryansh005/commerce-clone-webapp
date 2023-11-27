package com.commerce.webapp.commerceclonewebapp.security.filters;

import com.commerce.webapp.commerceclonewebapp.model.entity.Customer;
import com.commerce.webapp.commerceclonewebapp.model.entity.RefreshToken;
import com.commerce.webapp.commerceclonewebapp.model.params.ReturnStatusParam;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.RefreshTokService;
import com.commerce.webapp.commerceclonewebapp.util.CookieUtil;
import com.commerce.webapp.commerceclonewebapp.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


import static com.commerce.webapp.commerceclonewebapp.util.Constants.BLANK;
import static com.commerce.webapp.commerceclonewebapp.util.Constants.REFRESH_TOKEN;

@Service
public class RefreshTokFilter extends OncePerRequestFilter {
    @Autowired
    private RefreshTokService refreshTokService;

    @Autowired
    private ObjectMapper mapper;

    private static List<String> skipFilterUrls = Arrays.asList("/user/refresh-token");


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !skipFilterUrls.stream().anyMatch( url -> new AntPathRequestMatcher(url).matches(request));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        try {
            Cookie rfshTokCookie = CookieUtil.getCookieByName(request,REFRESH_TOKEN);
            String token = rfshTokCookie == null ? BLANK : rfshTokCookie.getValue();
            //fecth token
            RefreshToken tokenDb = refreshTokService.findByToken(token).orElseThrow(
                    ()->new RuntimeException(String.format("Token %s not in DB",token))
            );
            // verify token
            refreshTokService.verifyToken(tokenDb);
            //fetch user
            Customer user = tokenDb.getCustomer();

            //authtenticate -> as /refresh-token end point is protected and we are using stateless mechanism so need to create auth object
            UsernamePasswordAuthenticationToken authObj = new UsernamePasswordAuthenticationToken(
                    user ,null ,user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authObj);
            authObj.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            filterChain.doFilter(request,response);
        }catch (Exception e){
            logger.error("Error occured During Refresh token verification " ,e );

            ReturnStatusParam ret = JsonUtil.tokenErrResponse(e.getMessage());

            mapper.writeValue(response.getWriter(), ret);
        }

    }
}
