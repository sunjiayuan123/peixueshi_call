package com.mf.library.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String getDateStr(Date date, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static int differentDaysByString(String date1, String date2)
    {
        int days = 0;
        try {
//            days = (int) ((parseDate(date2).getTime() - parseDate(date1).getTime()) / (1000*3600*24));
            days = (int) ((parseDate(date2).getTime() - parseDate(date1).getTime()) / (1000*60));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    public static Date parseDate(String date) throws ParseException {
        if(date.isEmpty()){
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
    }

}
