package hisense.code.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SqlByCondis {

	public static String queryCondition(Map<String, Object> condis) {
		// 参数Map
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		String sql = "";
		if (condis.size() == 0) {
			return sql;// 重置条件时也可将空map传入
		}
		Set<Map.Entry<String, Object>> condi = condis.entrySet();
		for (Map.Entry<String, Object> me : condi) {// 排空操作 将有值的参数提取出来
			if (me.getValue() != null && me.getValue().toString().length() > 0
					&& !me.getValue().toString().equals("null"))
				map.put(me.getKey(), me.getValue());
		}
		Set<String> ks = map.keySet();// 对有效参数进行遍历
		for (String str : ks) {
			// 值
			Object value = map.get(str);
			String newStr = "";
			String[] paras = str.split("%");
			if (paras.length < 2) {
				continue;
			}
			newStr = paras[0];
			str = paras[1];
			if (str.endsWith("w")) {// 时间段+周期 检索方式 w代表周几
				dateMap.put("weekIndex", (String) value);
				dateMap.put("newStr", newStr);
				continue;
			}
			if (str.endsWith("d-")) {
				dateMap.put("startTime", (String) value);
				continue;
			}
			if (str.endsWith("d+")) {
				dateMap.put("endTime", (String) value);
				continue;
			}
			// 这是我设定的判断规则 唯一需要的就是在前台传给后台的时候给不同参数加不同的后缀 能提高很大效率
			// 然后拼装成适用于mysql的条件语句
			if (str.endsWith("s~")) {
				sql += " and UPPER(" + newStr + ") like '%" + value.toString().trim().toUpperCase() + "%' ";
			} else if (str.endsWith("s=")) {// 字符 指定
				sql += " and " + newStr + "='" + value + "' ";
			} else if (str.endsWith("i>")) {// 数值 大于
				sql += " and " + newStr + ">" + value + " ";
			} else if (str.endsWith("i>=")) {// 数值 大于等于
				sql += " and " + newStr + ">=" + value + " ";
			} else if (str.endsWith("i=")) {// 等于
				sql += " and " + newStr + "=" + value + " ";
			} else if (str.endsWith("i<")) {// 小于
				sql += " and " + newStr + "<" + value + " ";
			} else if (str.endsWith("i<=")) {// 小于等于
				sql += " and " + newStr + "<=" + value + " ";
			} else if (str.endsWith("i~")) { // 包含于
				sql += " and " + newStr + " in(" + value + ") ";
			} else if (str.endsWith("t>")) { // 时间大于

			} else if (str.endsWith("t<")) { // 时间小于

			} else if (str.endsWith("t>=")) { // 时间大于
				sql += " and " + newStr + ">=" + " to_date('" + value + "','yyyy-mm-dd')";
			} else if (str.endsWith("t<=")) { // 时间小于
				sql += " and " + newStr + "<=" + " to_date('" + value + "','yyyy-mm-dd')";
			} else if (str.endsWith("t-")) {
				// 判断在一天内
				sql += " and " + newStr + ">=" + " to_date('" + value + "','yyyy-mm-dd hh24:mi:ss') and " + newStr
						+ "<= " + " to_date('" + value + " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
			} else if(str.endsWith("m+")){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				Date month;
				try {
					month = sdf.parse(value.toString());
					Calendar ca = Calendar.getInstance();
					ca.setTime(month);
					ca.set(Calendar.DAY_OF_MONTH, 1);
					ca.add(Calendar.MONTH, 1);
					ca.add(Calendar.DAY_OF_MONTH, -1);
					Date lastDay = ca.getTime();
					sdf = new SimpleDateFormat("yyyy-MM-dd");
					sql += " and " + newStr + "<=" + " to_date('" + sdf.format(lastDay) + "','yyyy-mm-dd hh24:mi:ss') ";
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(str.endsWith("m-")){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
					Date month;
					try {
						month = sdf.parse(value.toString());
						Calendar ca = Calendar.getInstance();
						ca.setTime(month);
						ca.set(Calendar.DAY_OF_MONTH, 1);
						Date firstDay = ca.getTime();
						sdf = new SimpleDateFormat("yyyy-MM-dd");
						sql += " and " + newStr + ">=" + " to_date('" + sdf.format(firstDay) + " 00:00:00','yyyy-mm-dd hh24:mi:ss')";
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}else if(str.endsWith("dtt")){
				sql += " and " + newStr + ">=" + " to_date('" + value + " 00:00:00','yyyy-mm-dd hh24:mi:ss')";
			}else if(str.endsWith("dt+")){
				sql += " and " + newStr + "<=" + " to_date('" + value + " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
			}
		}
		if (dateMap.size() != 0) {
			sql += getSqlByDate(dateMap);
		}
		return sql;
	}

	// 当含有单引号时做转义操作
	private static String validateNoSingleColumn(String value) {
		Pattern p = Pattern.compile("[^\']");
		Matcher m = p.matcher(value);
		boolean asd = m.find();
		int groups = m.groupCount();
		StringBuilder sb = new StringBuilder(value);
		for (int i = 1; i <= groups; i++) {
			int start = m.start(i);
			sb.insert(start, "\\");
		}
		value = sb.toString();
		return value;
	}

	/**
	 * 根据时间参数(带时分秒)返回拼接的sql语句
	 * 
	 * @param dateMap
	 * @return
	 */
	public static String getSqlByDate(Map<String, String> dateMap) {
		String sql = "";
		String newStr = dateMap.get("newStr");
		String startTime = dateMap.get("startTime");
		String endTime = dateMap.get("endTime");
		int weekIndex = Integer.parseInt(dateMap.get("weekIndex"));
		if (weekIndex == 0) { // 全部
			sql += " and " + newStr + ">=" + " to_date('" + startTime + " 00:00:00','yyyy-mm-dd hh24:mi:ss') and "
					+ newStr + "<= " + " to_date('" + endTime + " 23:59:59 ','yyyy-mm-dd hh24:mi:ss')";
		} else {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			int dayIndex = getAllDays(startTime, endTime);// 时间段内总天数
			Calendar c = Calendar.getInstance();
			Date beginTime = null;
			try {
				beginTime = format.parse(startTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			c.setTime(beginTime);
			sql += " and (";
			boolean index = true;
			for (int i = 1; i <= dayIndex; i++) {
				int weekday = c.get(Calendar.DAY_OF_WEEK);// 周几
				if (weekday == weekIndex) {// 是选择的周期
					index = false;
					sql += "(" + newStr + " >= to_date('" + format.format(beginTime) + " 00:00:00','yyyy-mm-dd hh24:mi:ss') and "
							+ newStr + " <= to_date('" +  format.format(beginTime) + " 23:59:59','yyyy-mm-dd hh24:mi:ss')) or";
				}
				c.add(Calendar.DAY_OF_MONTH, 1);// 明天
				beginTime = c.getTime();
			}
			if(index){
				sql += "(" + newStr + " >= to_date('9333-11-11 00:00:00','yyyy-mm-dd hh24:mi:ss') and "
						+ newStr + " <= to_date('9333-11-11 23:59:59','yyyy-mm-dd hh24:mi:ss')) or";
			}
			sql = sql.substring(0, sql.length() - 2);
			sql += ")";
			
		}

		return sql;
	}

	/**
	 * 获取总天数
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int getAllDays(String startTime, String endTime) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		int days = -1;
		try {
			Date beginTime = sdf1.parse(startTime);
			Date stopTime = sdf1.parse(endTime);

			days = (int) ((stopTime.getTime() - beginTime.getTime()) / (1000 * 3600 * 24) + 1);
			return days;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return days;
	}
}
