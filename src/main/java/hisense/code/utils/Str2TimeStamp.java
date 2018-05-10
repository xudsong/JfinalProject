package hisense.code.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dragon on 2017/4/10.
 */
public class Str2TimeStamp {
//	根据需求 前台展示的时间都是截止到分钟 所以预设的时间格式也是到分钟
//	后来又加了三个。。一定注意Java原生时间戳13位 毫秒 数据库里10位 秒
	private static Date date;
	
	public long getTimeStamp(String timeStr){
		try {
			if(timeStr.split(":").length==3) {
				SimpleDateFormat simpleDateFormat5 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				date = simpleDateFormat5 .parse(timeStr);
			}
			else if(timeStr.split(":").length==2) {
				SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm");
				date = simpleDateFormat .parse(timeStr);//对传入的字符串进行解析 得到date
			}
			else if(timeStr.split("-").length==3&&!timeStr.contains(":")) {
				SimpleDateFormat simpleDateFormat2 =new SimpleDateFormat("yyyy-MM-dd");
				date = simpleDateFormat2 .parse(timeStr);
			}
			else if(timeStr.split("-").length==2&&!timeStr.contains(":")) {
				SimpleDateFormat simpleDateFormat3 =new SimpleDateFormat("yyyy-MM");
				date = simpleDateFormat3 .parse(timeStr);
			}
			else if(!timeStr.contains("-")) {
				SimpleDateFormat simpleDateFormat4 =new SimpleDateFormat("yyyy");
				date = simpleDateFormat4 .parse(timeStr+"-01-01");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	     long timeStamp = date.getTime();//再获得date的时间戳 此时是13位 所以要存到我们的数据库在外面还要/1000
		return timeStamp;
	}
	public long getCircleStamp(String timeStr){
		//传进来的可能是年 月 或者日 或者小时 或者分钟 得到到下一周期的时间戳 比如传入2016年 得到2017年
		try {
			 if (timeStr.split(":").length == 2) {
				SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm");
				date = simpleDateFormat .parse(timeStr);
		     }
			if(timeStr.split("-").length==3&&!timeStr.contains(":")){
				String[] times = timeStr.split("-");
				if(times[2].length()>2){
					SimpleDateFormat simpleDateFormat7 =new SimpleDateFormat("yyyy-MM-dd HH");
					date = simpleDateFormat7 .parse(timeStr);
				}else{
					SimpleDateFormat simpleDateFormat2 =new SimpleDateFormat("yyyy-MM-dd");
					date = simpleDateFormat2 .parse(timeStr);	
				}
			}
			if(timeStr.split("-").length==2&&!timeStr.contains(":")){
				SimpleDateFormat simpleDateFormat3 =new SimpleDateFormat("yyyy-MM");
				String[] timeStrs = timeStr.split("-");
				if(timeStrs[1].equals("12")){
					date = simpleDateFormat3 .parse((Integer.parseInt(timeStrs[0])+1)+"-01");
				}else{
				date = simpleDateFormat3 .parse(timeStrs[0]+"-"+(Integer.parseInt(timeStrs[1])+1));
				}
			}
			if(!timeStr.contains("-")) {
				SimpleDateFormat simpleDateFormat2 =new SimpleDateFormat("yyyy-MM-dd");
				date = simpleDateFormat2 .parse((Integer.parseInt(timeStr)+1)+"-01-01");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     long timeStamp = date.getTime();//再获得date的时间戳 此时是13位 所以要存到我们的数据库在外面还要/1000
	     if(timeStr.split("-").length==3&&!timeStr.contains(":")){
				String[] times = timeStr.split("-");
				if(times[2].length()>2){
					timeStamp+=3600000;
				}else{
			    	 timeStamp+=86400000;
				}
	     }
	     if (timeStr.split(":").length == 2) {
	    	 timeStamp += 60000;
	     }
		return timeStamp;
	}
	public String timeStamp2Second(long timeStamp){
		SimpleDateFormat simpleDateFormat5 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String res;
        long lt = new Long(timeStamp);
        Date date = new Date(lt);
        res = simpleDateFormat5.format(date);
        return res;
	}
	public String timeStamp2Hour(long timeStamp){
		SimpleDateFormat simpleDateFormat6 =new SimpleDateFormat("yyyy-MM-dd HH时");
		String res;
        long lt = new Long(timeStamp);
        Date date = new Date(lt);
        res = simpleDateFormat6.format(date);
        return res;
	}
	public String timeStamp2Day(long timeStamp){
		SimpleDateFormat simpleDateFormat2 =new SimpleDateFormat("yyyy-MM-dd");
		String res;
        long lt = new Long(timeStamp);
        Date date = new Date(lt);
        res = simpleDateFormat2.format(date);
        return res;
	}
	public String timeStamp2Month(long timeStamp){
		SimpleDateFormat simpleDateFormat3 =new SimpleDateFormat("yyyy-MM");
		String res;
        long lt = new Long(timeStamp);
        Date date = new Date(lt);
        res = simpleDateFormat3.format(date);
        return res;
	}
	public String timeStamp2Year(long timeStamp){
		SimpleDateFormat simpleDateFormat4 =new SimpleDateFormat("yyyy");
		String res;
        long lt = new Long(timeStamp);
        Date date = new Date(lt);
        res = simpleDateFormat4.format(date);
        return res;
	}
	public String timeStamp2Minute(long timeStamp) {
		SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String res;
        long lt = new Long(timeStamp);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
	}
	
	public Object[] getThisWeekStartAndEnd(long timeStamp){
		Object[] l = new Object[4];
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(timeStamp));
		int w = cal.get(7);
		String start = "";
		if(w>2){
			start = timeStamp2Day(timeStamp-((w-2)*86400000L));
		}else if(w==2){
			start = timeStamp2Day(timeStamp);
		}else if(w<2){
			start = timeStamp2Day(timeStamp-(6*86400000L));
		}
		l[2] = start;
		Long startL = getTimeStamp(start);
		l[0] = startL;
		l[1] = startL+(7*86400000L)-1;
		l[3] = timeStamp2Day(startL+(7*86400000L)-1);
		return l;
	} 
}
