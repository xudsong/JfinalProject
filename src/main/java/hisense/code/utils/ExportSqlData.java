package hisense.code.utils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*
 * by dragon
 */
public class ExportSqlData {
	
	public File exportSql(String route,String order,String sql,String names,String file_name)  {
		/*
		 * 由于Db.find返回的结果并不是我们sql查询语句的顺序 所以我们只能设置order字符串将顺序指定好
		 */
		Calendar cal = Calendar.getInstance();//获取当前时间 作为导出的Excel的文件名
		String fileName = file_name;
		fileName += cal.get(1)+"_"+(cal.get(2)+1)+"_"+cal.get(5)+"_"+cal.get(11)+"_"+cal.get(12)+"_"+cal.get(13);
		File file = new File(route,fileName+".xlsx");
		String[] orders = order.split(",");//分割排序字段的字符串(参数名 用来取数据的值)
		String[] ns = names.split(",");//分割在外面设定的Excel表头字段
		//System.out.println("数组长度："+ns.length+" 数组内容："+names);
		//System.out.println("数组1取值顺序："+order);
		// 第一步，创建一个webbook，对应一个Excel文件  
	    SXSSFWorkbook wb = new SXSSFWorkbook(1000); 
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
	    Sheet  sheet = wb.createSheet("ExportSheet");
    	sheet.setDefaultColumnWidth(20);
    	CellStyle  style = wb.createCellStyle(); 
        style.setAlignment(CellStyle.ALIGN_CENTER);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        Row row = sheet.createRow(0);
        Cell cell = null;
        for(int x = 0; x<ns.length;x++){
//            sheet.autoSizeColumn(x);
        	cell = row.createCell(x);  
        	cell.setCellValue(ns[x]);
        	cell.setCellStyle(style);
        }
        List<Record> list = new ArrayList<Record>();
        Record record = null;
    		list = Db.find(sql);
        	for(int j = 0;j<list.size();j++){//开始嵌套循环遍历 装填文件
        		row = sheet.createRow( j + 1);
    			record = list.get(j);//获得每个record对象
    			for(int k = 0;k<orders.length;k++){
    	        	cell = row.createCell(k);  
    	        	cell.setCellValue(record.get(orders[k])==null ? "" : record.get(orders[k]).toString());
    	        	cell.setCellStyle(style);
    			}
    		}
        FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  
        try {
			wb.write(fout);
		} catch (IOException e) {
			e.printStackTrace();
		}  
        try {
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return file;
	}
}
