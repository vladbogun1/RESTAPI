package main.java.ua.nure.bogun.apiproject.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RequestResponseService {

    public static  final String DOCUMENTATION =
            "INVALID DATA, USE APROPRIATE COMMANDS: " +
                    "";


    public boolean notEmpty(String... args){
        for (String arg: args){
            if(arg==null||arg.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    public String setError() {
        return new Gson().toJson(DOCUMENTATION);
    }


    //Boolean.toString(b);
    public void write(String jsonString, HttpServletRequest request, HttpServletResponse response){
        PrintWriter out = null;

        try {
            out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(jsonString);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
