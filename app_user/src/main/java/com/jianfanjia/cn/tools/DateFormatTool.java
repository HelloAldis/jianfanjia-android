package com.jianfanjia.cn.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Description:com.jianfanjia.cn.tools
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 12:51
 */
public class DateFormatTool {
    private static final long ONE_S = 1000;
    private static final long ONE_MIN = ONE_S * 60;
    private static final long ONE_HOUR = ONE_MIN * 60;
    private static final long ONE_DAY = ONE_HOUR * 24;
    private static final long ONE_YEAR = ONE_DAY * 365;

    /**
     * @param times
     * @param pattern
     * @return
     * @decription 转化时间为字符串
     */
    public static String covertLongToString(long times, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(times);
        return dateFormat.format(calendar.getTime());
    }

    public static long covertStringToLong(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long dateStr = format.parse(date).getTime();
            return dateStr;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0L;
    }

    public static String toLocalTimeString(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(time);
    }

    public static String longToString(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(time);
    }

    public static String getHumReadDateString(long time) {
        long now = System.currentTimeMillis();
        long diff = now - time;
        if (diff < 5 * ONE_MIN) {
            return "刚刚";
        } else if (diff >= 5 * ONE_MIN && diff < ONE_HOUR) {
            return String.format("%d分钟前", diff / ONE_MIN);
        } else if (diff >= ONE_HOUR && diff < ONE_DAY) {
            return String.format("%d小时前", diff / ONE_HOUR);
        } else if (diff >= ONE_DAY && diff < 2 * ONE_DAY) {
            String str = DateFormatTool.covertLongToString(time, "HH:mm");
            return String.format("昨天%s", str);
        } else if (diff >= 2 * ONE_DAY && diff < 3 * ONE_DAY) {
            String str = DateFormatTool.covertLongToString(time, "HH:mm");
            return String.format("前天%s", str);
        } else if (diff >= 3 * ONE_DAY && diff < ONE_YEAR) {
            return DateFormatTool.covertLongToString(time, "MM-dd");
        } else if (diff >= ONE_YEAR) {
            return DateFormatTool.longToString(time);
        } else {
            return DateFormatTool.longToString(time);
        }
    }
}
