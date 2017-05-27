package com.xiyi.weixin.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
/**
 * 有小数点时间转换
 * @author Administrator
 *
 */

public class DateUtils {
	/**
	 *  十三位数
	 * @param shijian
	 * @return
	 */
	public static String returnDate(String shijian){
		String haomiao = shijian.substring(10, shijian.length());
		String time = shijian.substring(0, 10)+"000";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		time = format.format(new Date(Long.valueOf(time)));
		time = time+haomiao;
		return time;
	}
	/**
	 * 十位数
	 * @param time
	 * @return
	 */
	public static String dateFromat(String time){
		time = time+"000";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		time = format.format(new Date(Long.valueOf(time)));
		return time;
	}
	
	 /**
     * 返回年月日
     * 
     * @return yyyyMMdd
     */
    public static String getTodayChar8()
    {
        return DateFormatUtils.format(new Date(), "yyyyMMdd");
    }
    /**
     * 取时间戳返回年月日
     * @return String
     */
    public static String getDate(){
		String time = String.valueOf(new Date().getTime()).substring(0, 10);
		return time;
	}
   
    /**
     * 将字符串转为时间戳  
     * @param user_time
     * @return
     */
    public static String getTime(String user_time) {  
    String re_time = null;  
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
    Date d;  
    try {   
    d = sdf.parse(user_time);  
    long l = d.getTime();  
    String str = String.valueOf(l);  
    re_time = str.substring(0, 10);         
    } catch (Exception e) { 
    e.printStackTrace();  
    }  
    return re_time;  
    }  
 
}
