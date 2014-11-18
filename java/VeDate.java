import java.text.*;
import java.util.Calendar;

/**
* 突然忘记了时间格式怎么转换,特此做个记录
*  
* Java时间格式转换大全
*/
public class VeDate {
/**
   * 获取现在时间
   * 
   * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
   */
public static Date getNowDate() {
   Date currentTime = new Date();
   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   String dateString = formatter.format(currentTime);
   ParsePosition pos = new ParsePosition(8);
   Date currentTime_2 = formatter.parse(dateString, pos);
   return currentTime_2;
}
/**
   * 获取现在时间
   * 
   * @return返回短时间格式 yyyy-MM-dd
   */
DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");         
DateFormat format 2= new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");         
Date date = null;    
String str = null;                  
            
// String转Date    
str = "2007-1-18";          
try {    
           date = format1.parse(str);   
           data = format2.parse(str); 
} catch (ParseException e) {    
           e.printStackTrace();    
}   
/**
   * 获取现在时间
   * 
   * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
   */
public static String getStringDate() {
   Date currentTime = new Date();
   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   String dateString = formatter.format(currentTime);
   return dateString;
}
/**
   * 获取现在时间
   * 
   * @return 返回短时间字符串格式yyyy-MM-dd
   */
public static String getStringDateShort() {
   Date currentTime = new Date();
   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
   String dateString = formatter.format(currentTime);
   return dateString;
}
/**
   * 获取时间 小时:分;秒 HH:mm:ss
   * 
   * @return
   */
public static String getTimeShort() {
   SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
   Date currentTime = new Date();
   String dateString = formatter.format(currentTime);
   return dateString;
}
/**
   * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
   * 
   * @param strDate
   * @return
   */
public static Date strToDateLong(String strDate) {
   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   ParsePosition pos = new ParsePosition(0);
   Date strtodate = formatter.parse(strDate, pos);
   return strtodate;
}
/**
   * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
   * 
   * @param dateDate
   * @return
   */
public static String dateToStrLong(java.util.Date dateDate) {
   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   String dateString = formatter.format(dateDate);
   return dateString;
}
/**
   * 将短时间格式时间转换为字符串 yyyy-MM-dd
   * 
   * @param dateDate
   * @param k
   * @return
   */
public static String dateToStr(java.util.Date dateDate) {
   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
   String dateString = formatter.format(dateDate);
   return dateString;
}
/**
   * 将短时间格式字符串转换为时间 yyyy-MM-dd 
   * 
   * @param strDate
   * @return
   */
public static Date strToDate(String strDate) {
   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
   ParsePosition pos = new ParsePosition(0);
   Date strtodate = formatter.parse(strDate, pos);
   return strtodate;
}
/**
   * 得到现在时间
   * 
   * @return
   */
public static Date getNow() {
   Date currentTime = new Date();
   return currentTime;
}
/**
   * 提取一个月中的最后一天
   * 
   * @param day
   * @return
   */
public static Date getLastDate(long day) {
   Date date = new Date();
   long date_3_hm = date.getTime() - 3600000 * 34 * day;
   Date date_3_hm_date = new Date(date_3_hm);
   return date_3_hm_date;
}
/**
   * 得到现在时间
   * 
   * @return 字符串 yyyyMMdd HHmmss
   */
public static String getStringToday() {
   Date currentTime = new Date();
   SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
   String dateString = formatter.format(currentTime);
   return dateString;
}
/**
   * 得到现在小时
   */
public static String getHour() {
   Date currentTime = new Date();
   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   String dateString = formatter.format(currentTime);
   String hour;
   hour = dateString.substring(11, 13);
   return hour;
}
/**
   * 得到现在分钟
   * 
   * @return
   */
public static String getTime() {
   Date currentTime = new Date();
   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   String dateString = formatter.format(currentTime);
   String min;
   min = dateString.substring(14, 16);
   return min;
}
/**
   * 根据用户传入的时间表示格式，返回当前时间的格式 如果是yyyyMMdd，注意字母y不能大写。
   * 
   * @param sformat
   *             yyyyMMddhhmmss
   * @return
   */
public static String getUserDate(String sformat) {
   Date currentTime = new Date();
   SimpleDateFormat formatter = new SimpleDateFormat(sformat);
   String dateString = formatter.format(currentTime);
   return dateString;
}

//--------------------------------------------------------------------------------------------------------------------------------
做成方法
import java.util.*;
import java.text.*;
import java.util.Calendar;

public class VeDate {
 /**
  * 获取现在时间
  * 
  * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
  */
 public static Date getNowDate() {
  Date currentTime = new Date();
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  String dateString = formatter.format(currentTime);
  ParsePosition pos = new ParsePosition(8);
  Date currentTime_2 = formatter.parse(dateString, pos);
  return currentTime_2;
 }

 /**
  * 获取现在时间
  * 
  * @return返回短时间格式 yyyy-MM-dd
  */
 public static Date getNowDateShort() {
  Date currentTime = new Date();
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
  String dateString = formatter.format(currentTime);
  ParsePosition pos = new ParsePosition(8);
  Date currentTime_2 = formatter.parse(dateString, pos);
  return currentTime_2;
 }

 /**
  * 获取现在时间
  * 
  * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
  */
 public static String getStringDate() {
  Date currentTime = new Date();
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  String dateString = formatter.format(currentTime);
  return dateString;
 }

 /**
  * 获取现在时间
  * 
  * @return 返回短时间字符串格式yyyy-MM-dd
  */
 public static String getStringDateShort() {
  Date currentTime = new Date();
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
  String dateString = formatter.format(currentTime);
  return dateString;
 }

 /**
  * 获取时间 小时:分;秒 HH:mm:ss
  * 
  * @return
  */
 public static String getTimeShort() {
  SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
  Date currentTime = new Date();
  String dateString = formatter.format(currentTime);
  return dateString;
 }

 /**
  * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
  * 
  * @param strDate
  * @return
  */
 public static Date strToDateLong(String strDate) {
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  ParsePosition pos = new ParsePosition(0);
  Date strtodate = formatter.parse(strDate, pos);
  return strtodate;
 }

 /**
  * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
  * 
  * @param dateDate
  * @return
  */
 public static String dateToStrLong(java.util.Date dateDate) {
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  String dateString = formatter.format(dateDate);
  return dateString;
 }

 /**
  * 将短时间格式时间转换为字符串 yyyy-MM-dd
  * 
  * @param dateDate
  * @param k
  * @return
  */
 public static String dateToStr(java.util.Date dateDate) {
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
  String dateString = formatter.format(dateDate);
  return dateString;
 }

 /**
  * 将短时间格式字符串转换为时间 yyyy-MM-dd 
  * 
  * @param strDate
  * @return
  */
 public static Date strToDate(String strDate) {
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
  ParsePosition pos = new ParsePosition(0);
  Date strtodate = formatter.parse(strDate, pos);
  return strtodate;
 }

 /**
  * 得到现在时间
  * 
  * @return
  */
 public static Date getNow() {
  Date currentTime = new Date();
  return currentTime;
 }

 /**
  * 提取一个月中的最后一天
  * 
  * @param day
  * @return
  */
 public static Date getLastDate(long day) {
  Date date = new Date();
  long date_3_hm = date.getTime() - 3600000 * 34 * day;
  Date date_3_hm_date = new Date(date_3_hm);
  return date_3_hm_date;
 }

 /**
  * 得到现在时间
  * 
  * @return 字符串 yyyyMMdd HHmmss
  */
 public static String getStringToday() {
  Date currentTime = new Date();
  SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
  String dateString = formatter.format(currentTime);
  return dateString;
 }

 /**
  * 得到现在小时
  */
 public static String getHour() {
  Date currentTime = new Date();
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  String dateString = formatter.format(currentTime);
  String hour;
  hour = dateString.substring(11, 13);
  return hour;
 }

 /**
  * 得到现在分钟
  * 
  * @return
  */
 public static String getTime() {
  Date currentTime = new Date();
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  String dateString = formatter.format(currentTime);
  String min;
  min = dateString.substring(14, 16);
  return min;
 }

 /**
  * 根据用户传入的时间表示格式，返回当前时间的格式 如果是yyyyMMdd，注意字母y不能大写。
  * 
  * @param sformat
  *            yyyyMMddhhmmss
  * @return
  */
 public static String getUserDate(String sformat) {
  Date currentTime = new Date();
  SimpleDateFormat formatter = new SimpleDateFormat(sformat);
  String dateString = formatter.format(currentTime);
  return dateString;
 }

 /**
  * 二个小时时间间的差值,必须保证二个时间都是"HH:MM"的格式，返回字符型的分钟
  */
 public static String getTwoHour(String st1, String st2) {
  String[] kk = null;
  String[] jj = null;
  kk = st1.split(":");
  jj = st2.split(":");
  if (Integer.parseInt(kk[0]) < Integer.parseInt(jj[0]))
   return "0";
  else {
   double y = Double.parseDouble(kk[0]) + Double.parseDouble(kk[1]) / 60;
   double u = Double.parseDouble(jj[0]) + Double.parseDouble(jj[1]) / 60;
   if ((y - u) > 0)
    return y - u + "";
   else
    return "0";
  }
 }

 /** * 得到二个日期间的间隔天数 */
 public static String getTwoDay(String sj1, String sj2) {
  SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
  long day = 0;
  try {
   java.util.Date date = myFormatter.parse(sj1);
   java.util.Date mydate = myFormatter.parse(sj2);
   day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
  } catch (Exception e) {
   return ""; } return day + ""; } 
