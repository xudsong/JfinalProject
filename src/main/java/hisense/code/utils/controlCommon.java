package hisense.code.utils;


import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * control 文件下共通方法提取
 *
 * @author mark
 */
public class controlCommon {
    /**
     * 设置数据颜色
     */
    public static List<Record> setDataColorByAlarm(List<Record> data, List<Record> alarmData, String type) {
        //根据报警值，设置显示颜色
        for (Record item : data) {
            String areacode = item.getStr("AREACODE");
            boolean HasAlarmData = false;
            for (Record alarmItem : alarmData) {
                String alarmAreaCode = alarmItem.getStr("AREACODE");
                if (alarmAreaCode.equals(areacode)) {

                    HasAlarmData = true;
                    Double value;
                    if ("NOW".equals(type)) {
                        value = StringUtils.getDoubleValue(item.getStr("PFDATA1"));
                    } else {
                        value = StringUtils.getDoubleValue(item.getBigDecimal("PFDATA1").toString());
                    }

                    //值和颜色初始设置
                    item.set("COLOR", '1');
                    item.set("VALUE", value);

                    Double left = StringUtils.getDoubleValue(alarmItem.getStr("LEFTVALUE") == null ? "0" : alarmItem.getStr("LEFTVALUE"));
                    if (alarmItem.getStr("RIGHTVALUE") == null) {
                        if (value >= left) {
                            item.set("COLOR", alarmItem.getStr("SHOWCOLORTYPE"));
                            break;
                        }
                    } else {
                        Double right = StringUtils.getDoubleValue((alarmItem.getStr("RIGHTVALUE")));
                        if (value >= left && value < right) {
                            item.set("COLOR", alarmItem.getStr("SHOWCOLORTYPE"));
                            break;
                        }
                    }
                    //如果没有设颜色的话,就设为绿色
                    if (!StringUtils.isNotNull((alarmItem.getStr("SHOWCOLORTYPE")))) {
                        item.set("COLOR", "1");
                        break;
                    }
                }
            }
            if (!HasAlarmData) {
                Double value;
                if ("NOW".equals(type)) {
                    value = StringUtils.getDoubleValue(item.getStr("PFDATA1"));
                } else {
                    value = StringUtils.getDoubleValue(item.getBigDecimal("PFDATA1").toString());
                }
                item.set("COLOR", "1");
                item.set("VALUE", value);
            }


        }
        return data;
    }

    public static List<Record> dataReset(List<Record> list1, String clientTime, int length) {
        if (list1 == null || list1.size() == 0) {
            return list1;
        }
        SimpleDateFormat  df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = df.parse(clientTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int endIndex=calendar.get(Calendar.HOUR_OF_DAY);
            String[] xIndex = new String[length];
            for (int i = endIndex-length+1; i < endIndex+1; i++) {
                String str = "";
                if (i < 10) {
                    str =  i + ":00";
                } else {
                    str = i + ":00";
                }
                xIndex[i-(endIndex-length+1)] = str;
            }

            String targetcode="";
            for (int j = 0; j < list1.size(); j++) {
                if(!targetcode.equals(list1.get(j).get("AREACODE")))
                {
                    setZero(list1,list1.get(j).get("AREACODE").toString(),list1.get(j).get("AREANAME").toString(),xIndex);
                }
                targetcode= list1.get(j).get("AREACODE");
           }
            Collections.sort(list1, new Comparator<Record>() {
                @Override
                public int compare(Record s1, Record s2) {
                    if(!s1.get("AREACODE").toString().equals(s2.get("AREACODE"))){
                        return s1.get("AREACODE").toString().compareTo(s2.get("AREACODE").toString());
                    }else{
                        return  s1.getBigDecimal("ORDERBY").intValue()-s2.getBigDecimal("ORDERBY").intValue();
                    }
                }
            });
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return list1;
    }
    public  static  void setZero(List<Record> list1, String targetCode,String targetName,String[] xIndex) {
        for (int i = 0; i < xIndex.length; i++) {
            boolean index = true;
            for (int j = 0; j < list1.size(); j++) {
                if (targetCode.equals(list1.get(j).get("AREACODE"))&&list1.get(j).get("HOUR").equals(xIndex[i])) {
                    index = false;
                    break;
                }
            }
            if (index) {
                Record newRecord=new Record();
                newRecord.set("AREACODE",targetCode);
                newRecord.set("AREANAME",targetName);
                newRecord.set("DATA",0);
                newRecord.set("HOUR",xIndex[i]);
                newRecord.set("ORDERBY",new BigDecimal(Integer.parseInt(xIndex[i].split(":")[0])-1));
                list1.add(newRecord);
            }
        }

    }
}
