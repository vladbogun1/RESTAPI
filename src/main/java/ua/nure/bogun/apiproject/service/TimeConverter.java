package main.java.ua.nure.bogun.apiproject.service;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeConverter {
    public static String timestampToString(Timestamp time){

        return (time!=null)?time.toString():null;
    }
    public static Timestamp  stringToTimestamp(String time){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date();
        try {
            date = formatter.parse(time);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Timestamp(date.getTime());
    }
    public static Timestamp currentTimestamp(){
        return new Timestamp(new Date().getTime());
    }
}
