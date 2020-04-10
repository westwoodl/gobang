package com.xrc.gb.common.util;

import com.xrc.gb.common.enums.DateFormatEnum;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @author xu rongchao
 * @date 2020/3/27 17:00
 */
public class DateFormatUtils {

    public static HashMap<DateFormatEnum, ThreadLocal<DateFormat>> formatMap;

    static {
        formatMap = new HashMap<>();
        for( DateFormatEnum formatEnum : DateFormatEnum.values()) {
            formatMap.put(formatEnum, new ThreadLocal<DateFormat>() {
                @Override
                protected DateFormat initialValue() {
                    return new SimpleDateFormat(formatEnum.getFormatStr());
                }
            });
        }
    }

    private DateFormatUtils() {}

    public static String parseDate(Date date, DateFormatEnum formatEnum) {
        return formatMap.get(formatEnum).get().format(date);
    }


    public static Date parseString(String dateStr, DateFormatEnum formatEnum) {
        try {
            return formatMap.get(formatEnum).get().parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
