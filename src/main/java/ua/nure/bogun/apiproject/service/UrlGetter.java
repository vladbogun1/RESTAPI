package main.java.ua.nure.bogun.apiproject.service;

import javax.servlet.http.HttpServletRequest;

public class UrlGetter {
    public static String get(HttpServletRequest request){
        StringBuffer requestURL = request.getRequestURL();
        if (request.getQueryString() != null) {
            requestURL.append("?").append(request.getQueryString());
        }
        return requestURL.toString();
    }
}
