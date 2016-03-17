package com.jianfanjia.cn.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    /**
     * 获取相对时间(将给点的时间变换成相对于系统当前时间的差值)，格式为“XX分钟前”
     *
     * @return
     */
    public static String getRelativeTime(long date) {
        String time = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
            Date dt1 = sdf.parse(sdf.format(date));

            Calendar cl = Calendar.getInstance();
            int year2 = cl.get(Calendar.YEAR);
            int month2 = cl.get(Calendar.MONTH);
            int day2 = cl.get(Calendar.DAY_OF_MONTH);
            int hour2 = cl.get(Calendar.HOUR_OF_DAY);
            int minute2 = cl.get(Calendar.MINUTE);
            // int second2=cl.get(Calendar.SECOND);

            cl.setTime(dt1);
            int year1 = cl.get(Calendar.YEAR);
            int month1 = cl.get(Calendar.MONTH);
            int day1 = cl.get(Calendar.DAY_OF_MONTH);
            int hour1 = cl.get(Calendar.HOUR_OF_DAY);
            int minute1 = cl.get(Calendar.MINUTE);
            // int second1=cl.get(Calendar.SECOND);

            if (year1 == year2) {
                if (month1 == month2) {
                    if (day1 == day2) {
                        if (hour1 == hour2) {
                            if (minute2 > minute1) {
                                time = (minute2 - minute1) + "分钟前";
                            } else {
                                time = "刚才";
                            }
                        } else if (hour2 - hour1 > 3) {
                            time = formatTime(hour1, minute1);
                        } else if (hour2 - hour1 == 1) {
                            if (minute2 - minute1 > 0) {
                                time = "1小时前";
                            } else {
                                time = (60 + minute2 - minute1) + "分钟前";
                            }
                        } else {
                            time = (hour2 - hour1) + "小时前";
                        }
                    } else if (day2 - day1 == 1) { // 昨天
                        if (hour1 > 12) {
                            time = (month1 + 1) + "月" + day1 + "日  下午";
                        } else {
                            time = (month1 + 1) + "月" + day1 + "日  上午";
                        }
                    } else {
                        time = (month1 + 1) + "月" + day1 + "日";
                    }
                } else {
                    time = (month1 + 1) + "月" + day1 + "日";
                }
            } else {
                time = year1 + "年" + month1 + "月" + day1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    private static String formatTime(int hour, int minute) {
        String time = "";
        if (hour < 10) {
            time += "0" + hour + ":";
        } else {
            time += hour + ":";
        }

        if (minute < 10) {
            time += "0" + minute;
        } else {
            time += minute;
        }
        return time;
    }

    public static String getHumReadDateString(long time) {
        long now = System.currentTimeMillis();
        long diff = now - time;

        if (diff < 5*60) {
            return "刚刚";
        } else if (diff >= 5*ONE_MIN && diff < ONE_HOUR) {
            return String.format("%ld分钟前", diff / ONE_MIN);
        } else if (diff >= ONE_HOUR && diff < ONE_DAY) {
            return String.format("%ld小时前", diff / ONE_HOUR);
        } else if (diff >= ONE_DAY && diff < 2*ONE_DAY) {
            String str = DateFormatTool.covertLongToString(time, "HH:mm");
            return String.format("昨天%@", str);
        } else if (diff >= 2*ONE_DAY && diff < 3*ONE_DAY) {
            String str = DateFormatTool.covertLongToString(time, "HH:mm");
            return String.format("前天%@", str);
        } else if (diff >= 3*ONE_DAY && diff < ONE_YEAR) {
            return DateFormatTool.covertLongToString(time, "MM-dd");
        } else if (diff >= ONE_YEAR) {
            return DateFormatTool.longToString(time);
        } else {
            return DateFormatTool.longToString(time);
        }
    }
}
