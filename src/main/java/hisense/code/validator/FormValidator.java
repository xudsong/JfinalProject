package hisense.code.validator;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.validate.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*by dragon*/
public abstract class FormValidator extends Validator {
    /*
     * 在表单验证中 统一使用"error"作为返回错误信息的key fieldName作为传递表单项name的key
     * success:function(result){ alert(result["error"]); }
     * 要求fieldName跟表单项name值完全相同
     */
    // 大部分正则表达式 以提供正则校验 1.0系统里带的
    protected static final String error = "error";
    protected static final String pin = "^\\d{4,8}$";
    protected static final String jingweidu = "^(([1-9][0-9]{0,2})(\\.{1}[0-9]{1,6}))?|(([0]{1})(\\.{1}[0-9]{1,6}))?$";
    protected static final String anzhuo = "^[0-9]{1,6}[m]{1}\\_{1}[0-9]{1,6}[m]{1}$";
    protected static final String numAndWord = "^[a-zA-Z0-9]+$";
    protected static final String special = "^([\u4e00-\u9fa5]|[a-zA-Z0-9])+$";
    protected static final String special2 = "^([\u4e00-\u9fa5]|[a-zA-Z0-9]|\\(|\\)|\\（|\\）|\\.|\\。|\\,|\\，)+$";
    protected static final String address = "^(?=.*?[\u4E00-\u9FA5])[\\dA-za-z\u4E00-\u9FA5]+";
    protected static final String carNumberPattern = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4,5}[A-Z0-9挂学警港澳]{1}$";
    protected static final String emailPattern = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    protected static final String englishPattern = "^[A-Za-z]+$";
    protected static final String chinesePattern = "^[\u0391-\uFFE5]+$";
    protected static final String urlPattern = "^(http:\\/\\/)?[A-Za-z0-9]+\\.[A-Za-z0-9]+[\\/=\\?%\\-&_~`@[\\]\\':+!]*([^<>\\'\\'])*$";
    protected static final String ipPattern = "^(0|[1-9]\\d?|[0-1]\\d{2}|2[0-4]\\d|25[0-5]).(0|[1-9]\\d?|[0-1]\\d{2}|2[0-4]\\d|25[0-5]).(0|[1-9]\\d?|[0-1]\\d{2}|2[0-4]\\d|25[0-5]).(0|[1-9]\\d?|[0-1]\\d{2}|2[0-4]\\d|25[0-5])$";
    protected static final String zip = "^[1-9]\\d{5}$";
    protected static final String qq = "^[1-9]\\d{4,15}$";
    protected static final String number = "^[0-9]\\d{0,12}$";
    protected static final String sbnumber = "^[0-9]+$";
    protected static final String qqnum = "^[0-9]\\d{4,15}$";
    protected static final String usercardid = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$";
    protected static final String username = "^[\u4e00-\u9fa5]{2,20}$";
    protected static final String funcname = "^[a-zA-Z0-9\u4e00-\u9fa5]+$";
    protected static final String mobile = "^(((\\+86)?1[34578]\\d{9})|(400\\d{7}))$";
    protected static final String doub = "^\\d{1,8}(\\.\\d{1,3})?$";
    protected static final String alpha = "^[0-9a-zA-Z\\_]+$";
    protected static final String strid = "^[a-zA-Z\\_]*[0-9]*[a-zA-Z\\_]+[0-9]*[a-zA-Z\\_]*$";
    protected static final String phone = "^(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$";//"^((\\+86)?(0[1-9]{1}\\d{9,10})|([2-9]\\d{6,7}))|(400\\d{7})$";
    protected static final String callnum = "^(1[4358]\\d{9})|[8]\\d{7}$";
    protected static final String callnum1 = "^(^((0\\d{2,3})(-)?)?(\\d{7,8})(-(\\d{3,}))?$)|(^(((\\+86)?1\\d{10})|(400\\d{7}))$)$";
    protected static final String jw = "^-?((0|[1-9]\\d?|1[1-7]\\d)(\\.\\d{1,7})?|180(\\.0{1,7})?)?$";
    protected static final String wd = "^-?((\\d|[1-8]\\d)(\\.\\d{1,7})?|90(\\.0{1,7})?)?$";
    protected static final String numAndWordLength = "^[0-9-Za-z]{2,20}$";;
    protected static final String CHINESE = "^[\u4E00-\u9FA5]{2,4}$";
    protected static final String ISIPADDRESS = "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])(\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])){3}$";
    protected static final String NOSINGLECOLUMN = "^[^']*$";
    protected static final String OnlyNum = "^[0-9]+$";

    /**
     * 判断是否是数字 传入的是fieldName
     * 
     * @param field
     *            字段
     * @param fieldName
     *            字段显示名
     * @param minValue
     *            最小值（为空不判断）
     * @param maxValue
     *            最大值（为空不判断）
     * @return
     */
    protected boolean ifInteger(String field, String fieldName,
            Integer minValue, Integer maxValue) {
    	boolean result = true;
    	String value = controller.getPara(field);
        if(value == null || value.equals("")) return result;
        Integer tmp = null;
        try {
            tmp = Integer.parseInt(controller.getPara(field));
        } catch (Exception e) {
            addError2(fieldName + "请输入长度小于9位的整数数字", field);
            result = false;
            return result;
        }
        if (maxValue != null && tmp > maxValue) {
            result = false;
            addError2(fieldName + "不能大于" + maxValue, field);
        }
        if (minValue != null && tmp <= minValue) {
            result = false;
            addError2(fieldName + "不能小于" + minValue, field);
        }
        return result;
    }
    
    /**
     * 判断是否是数字 传入的是fieldName,大于等于,小于等于
     * 
     * @param field
     *            字段
     * @param fieldName
     *            字段显示名
     * @param minValue
     *            最小值（为空不判断）
     * @param maxValue
     *            最大值（为空不判断）
     * @return
     */
    protected boolean ifInteger_1(String field, String fieldName,
            Integer minValue, Integer maxValue) {
    	boolean result = true;
    	String value = controller.getPara(field);
        if(value == null || value.equals("")) return result;
        Integer tmp = null;
        try {
            tmp = Integer.parseInt(controller.getPara(field));
        } catch (Exception e) {
            addError2(fieldName + "请输入长度小于9位的整数数字", field);
            result = false;
            return result;
        }
        if (maxValue != null && tmp > maxValue) {
            result = false;
            addError2(fieldName + "不能大于" + maxValue, field);
        }
        if (minValue != null && tmp < minValue) {
            result = false;
            addError2(fieldName + "不能小于" + minValue, field);
        }
        return result;
    }

    /**
     * 是否为空或者为数字
     * 
     * @param field
     *            字段名
     * @param fieldName
     *            字段显示名
     * @param minValue
     *            最小值（为空不判断）
     * @param maxValue
     *            最大值（为空不判断）
     * @param n
     *            小数点后最大位数（为空不判断）
     * @return
     */
    protected boolean ifNullorNumber(String leftField,String rightField, String fieldName) {
        boolean result = true;
        String leftValue = controller.getPara(leftField);
        String rightValue = controller.getPara(rightField);
//    			右为空：true
//    	左为空 ：
//    			右不为空：:判断右边是否为数字，不是数字，返回false，是返回true
//
//    			右为空：判断左边是否为数字，不是数字，返回false，是返回true
//    	左不为空：						
//    			右不为空：判断左右两边是否为数字，都是数字：判断左右大小，左小于等于右 true 否则为false
        if(valueNull(leftValue)){//左为空
        	if(valueNull(rightValue)){//右为空
        		result = true;
        		return result;
        	} else {//右不为空
        		Double tmp = null;
                try {
                    tmp = Double.parseDouble(rightValue);
                    } catch (Exception e) {
                    addError2("报警值范围请输入数字", rightValue);
                    result = false;
                    return result;
                }
                return true;
        	}
        }else{
        	if(valueNull(rightValue)){
        		Double tmp = null;
                try {
                    tmp = Double.parseDouble(leftValue);
                    } catch (Exception e) {
                    addError2("报警值范围请输入数字", leftValue);
                    result = false;
                    return result;
                }
                return true;
        	}else {
        		Double tmp = null;
        		Double tmp1 = null;
                try {
                    tmp = Double.parseDouble(leftValue);
                    } catch (Exception e) {
                    addError2("报警值范围请输入数字", leftValue);
                    result = false;
                    return result;
                }
                try {
                	tmp1 = Double.parseDouble(rightValue);
                    } catch (Exception e) {
                    addError2("报警值范围请输入数字", rightValue);
                    result = false;
                    return result;
                }
                if(tmp>tmp1){
                	addError2("报警值范围左不能大于报警值范围右", leftValue);
                	return false;
                }else{
                	return true;
                }
        	}
        }
    }
    //判断是否为空，为空返回true，不为空返回false
    protected boolean valueNull(String value){
    	if(value==null || value.equals("")){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    
    
    /**
     * 是否double
     * 
     * @param field
     *            字段名
     * @param fieldName
     *            字段显示名
     * @param minValue
     *            最小值（为空不判断）
     * @param maxValue
     *            最大值（为空不判断）
     * @param n
     *            小数点后最大位数（为空不判断）
     * @return
     */
    protected boolean ifDouble(String field, String fieldName, 
            Integer minValue, Integer maxValue, Integer n) {
        boolean result = true;
        String value = controller.getPara(field);
        if(value == null || value.equals("")) return result;
        Double tmp = null;
        try {
            tmp = Double.parseDouble(value);
            } catch (Exception e) {
            addError2("请输入数字，且不能大于"+maxValue, field);
            result = false;
            return result;
        }
        if (maxValue != null && tmp > maxValue) {
            addError2(fieldName + "不能大于" + maxValue, field);
        }
        if (minValue != null && tmp <= minValue) {
            addError2(fieldName + "不能小于" + (minValue+1), field);
        }
        if (n != null) {
            String[] tmpD = value.split(".");
            if (tmpD.length > 1 && tmpD[1].length() > n) {
                addError2(fieldName + "小数点后不得超过" + n + "位", field);
            }
        }
        return result;
    }

    /**
     * 多正则校验 满足一个即可
     * 
     * @param field
     *            字段
     * @param regExpression
     *            校验用正则表达式
     * @param isCaseSensitive
     * @param errorMessage
     */
    protected void validateRegexMany(String field, String[] regExpression,
            boolean isCaseSensitive, String errorMessage) {
        String value = controller.getPara(field);
        if (value == null) {
            addError2(errorMessage, field);
            return;
        }
        int y = 0;
        for (int x = 0; x < regExpression.length; x++) {
            Pattern pattern = isCaseSensitive ? Pattern
                    .compile(regExpression[x]) : Pattern.compile(
                    regExpression[x], Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(value);
            if (matcher.matches()) {
                y = 1;
            }
        }
        if (y == 0)
            addError2(errorMessage, field);
    }

    /**
     * 排重 需要当前要查重的值 
     * @param field 字段
     * @param tName 数据库表名
     * @param wName 数据库字段名
     * @param errorMessage 错误信息
     */
    protected void ifRepeat(String field, String tName, String wName,
            String errorMessage) {
        ifRepeat(field, tName, wName, errorMessage, true);
    }
    
    /**
     * 排重 需要当前要查重的值 
     * @param field 字段
     * @param tName 数据库表名
     * @param wName 数据库字段名
     * @param errorMessage 错误信息
     * @param isactiveFlag 是否物理删除
     */
    protected void ifRepeat(String field, String tName, String wName,
            String errorMessage, boolean isactiveFlag) {
        String value = controller.getPara(field).trim();
        if (value == null || value.equals(""))
            return;
        value = value.replaceAll("'", "''");
        String sql = "select count(0) count from " + tName
                + " where " + wName + "='" + value + "'";
        if(isactiveFlag) {
            sql += " and ISACTIVE = 1";
        }
        Record record = Db.findFirst(sql);
        if (record.getBigDecimal("count").intValue() != 0)
            addError2(errorMessage, field);
    }
    /**
     * 排重 需要当前要查重的值 更新的时候
     * @param filedId 当前记录主键（更新时不需要判断本身）
     * @param field
     * @param tName
     * @param wName
     * @param pkCol 主键列名
     * @param errorMessage
     */
    protected void ifRepeat2(String filedId, String field, String tName,
            String wName, String pkCol, String errorMessage) {
        ifRepeat2(filedId, field, tName, wName, pkCol, errorMessage, true);
    }
    /**
     * 排重 需要当前要查重的值 更新的时候
     * @param filedId 当前记录主键（更新时不需要判断本身）
     * @param field
     * @param tName
     * @param wName
     * @param pkCol 主键列名
     * @param errorMessage
     * @param isactiveFlag 是否物理删除
     */
    protected void ifRepeat2(String filedId, String field, String tName,
            String wName, String pkCol, String errorMessage,
            boolean isactiveFlag) {
        String idValue = controller.getPara(filedId);
        String value = controller.getPara(field);
        if (idValue == null || value == null || value.equals("")
                || idValue.equals(""))
            return;
        value = value.replaceAll("'", "''");
        String sql = "select count(0) count from " + tName + " where " + wName
                + "='" + value + "' and " + pkCol + "<> '" + idValue + "'";
        if (isactiveFlag) {
            sql += " and ISACTIVE = 1";
        }
        Record record = Db.findFirst(sql);
        if (record.getBigDecimal("count").intValue() != 0)
            addError2(errorMessage, field);
    }
    
    protected boolean compareTime(String field){
    	String time_1 = controller.getPara(field);
    	boolean result = true;
    	SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
    	Date timeNow = null;
    	Date timeInput = null;
    	try {
    		timeNow = sdf.parse(sdf.format(new Date()));
    		timeInput = sdf.parse(time_1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	if(timeInput.getTime()<timeNow.getTime()){
        	addError2("有效截止日期不能早于今日", "template.VALIDITYDAY");
        	result = false;
    	}
    	
    	return result;
    }

    /**
     * 非空验证
     * 
     * @param field
     * @param fieldName
     * @return
     */
    protected boolean isEmpty(String field, String fieldName) {
        boolean result = true;
        String value = controller.getPara(field) + "";
        if (StrKit.isBlank(value.trim())) {
            addError2("请输入" + fieldName, field);
            result = false;
        }
        return result;
    }

    /**
     * 长度验证
     * 
     * @param field
     * @param fieldName
     * @param min
     *            最小值
     * @param max
     *            最大值
     * @return
     */
    protected boolean LengthValidate(String field, String fieldName, int min,
            int max) {
        boolean result = true;
        String value = controller.getPara(field) + "";
        if (value.length() < min) {
            addError2(fieldName + "输入的内容过短,长度不得少于" + min + "个字符", field);
            result = false;
        } else if (value.length() > max) {
            addError2(fieldName + "输入的内容过长,长度不得大于" + max + "个字符", field);
            result = false;
        }
        return result;
    }
    
    protected boolean compareDate(String field, String fieldName) {
    	boolean result = false;
    	String value = controller.getPara(field) + "";
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	
    	Date date = new Date();
    	
    	String currentDay = sdf.format(date);
    	
    	try {
			if(sdf.parse(value).getTime()  > sdf.parse(currentDay).getTime() ){
				result = true;
				 addError2(fieldName + "不能大于当前日期", field);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return result;
    }
    
    
    
    /**
     * 校验空格
     * @param field
     * @param fieldName
     * @return
     */
    protected boolean isContainsSpace(String field, String fieldName) {
    	 boolean result = true;
    	String value = controller.getPara(field) + "";
    	if (value.trim().contains(" ")){
    		 addError2(fieldName + "不能包含空格", field);
    		 result = false;
    	}
    	return result;
    }

    @Override
    protected void validate(Controller c) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void handleError(Controller c) {
        // addError2后 都需要renderJson才能返回给前台正确解析 相当于 renderJson("error","错误信息");
        c.renderJson();
    }

    /**
     * 向前台发送错误信息
     * 
     * @param errorMessage
     * @param fieldName
     */
    protected void addError2(String errorMessage, String fieldName) {
        controller.setAttr("fieldName", fieldName);
        addError(error, errorMessage);
    }

    @Override
    /**
     * 正则验证
     */
    protected void validateRegex(String field, String regExpression,
            boolean isCaseSensitive, String errorKey, String errorMessage) {
        String value = controller.getPara(field);
        if (value == null) {
            addError2(errorMessage, field);
            return;
        }
        Pattern pattern = isCaseSensitive ? Pattern.compile(regExpression)
                : Pattern.compile(regExpression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(value);
        if (!matcher.matches())
            addError2(errorMessage, field);
    }
    
    protected void validateRegex_null(String field, String regExpression,
            boolean isCaseSensitive, String errorKey, String errorMessage) {
        String value = controller.getPara(field);
        if (value == "") {
            
        }else{
        	Pattern pattern = isCaseSensitive ? Pattern.compile(regExpression)
        			: Pattern.compile(regExpression, Pattern.CASE_INSENSITIVE);
        	Matcher matcher = pattern.matcher(value);
        	if (!matcher.matches())
        		addError2(errorMessage, field);
        }
    }
    
    /**
     * 敏感区域重复校验
     * @param field1
     * @param field2
     * @param errorMessage
     */
    protected void ifRepeat3(String field1, String field2,
            String errorMessage) {
    	//keyId
        String keyId = controller.getPara(field1);
        //areaId
        String areaId = controller.getPara(field2);
        if (keyId == null || keyId.equals(""))
            return;
        if (areaId == null || areaId.equals(""))
            return;
        String sql = "select count(0) count from MCR_SENSEALARMPARAM " 
                + " where AREAID = '"+ areaId + "' and KEYID = '" +keyId +"' ";
        Record record = Db.findFirst(sql);
        if (record.getBigDecimal("count").intValue() != 0)
            addError2(errorMessage, field2);
    }
    
    /**
     * 敏感区域重复校验
     * @param field1
     * @param field2
     * @param errorMessage
     */
    protected void ifRepeat4(String field1, String field2,
            String errorMessage) {
    	//keyId
        String keyId = controller.getPara(field1);
        //areaId
        String areaId = controller.getPara(field2);
        if (keyId == null || keyId.equals(""))
            return;
        if (areaId == null || areaId.equals(""))
            return;
        String sql = "select count(0) count from MCR_SENSEALARMPARAM " 
                + " where AREAID = '"+ areaId + "' and KEYID in "
                		+ "(select m.keyid from MC_ALARMPARAM m where m.paramtype in "
                		+ "(select t.paramtype from MC_ALARMPARAM t where t.keyid ='"+keyId+"'))";
        Record record = Db.findFirst(sql);
        if (record.getBigDecimal("count").intValue() != 0)
            addError2(errorMessage, field2);
    }
}
