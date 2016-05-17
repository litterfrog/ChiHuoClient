package com.fxp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fuxinpeng on 2016/5/17.
 */
public class TimeUtil {
    private static Calendar calendar = Calendar.getInstance();
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    /**
     * 得到当前时间
     * @return
     */
    public static String getCurrentTime(){
        return simpleDateFormat.format(new Date().getTime());
    }

}
