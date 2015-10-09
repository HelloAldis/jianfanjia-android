package com.jianfanjia.cn.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateFormatTool {

	/**
	 * @decription 转化时间为字符串
	 * @param times
	 * @param pattern
	 * @return
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
}
