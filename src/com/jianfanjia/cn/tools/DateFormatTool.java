package com.jianfanjia.cn.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateFormatTool {
	
	/**
	 * @decription ת��ʱ��Ϊ�ַ���
	 * @param times
	 * @param pattern
	 * @return
	 */
	public static String covertLongToString(long times,String pattern){
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(times);
		return dateFormat.format(calendar.getTime());
	}
	

}
