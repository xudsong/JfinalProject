package hisense.code.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DTOUtils {
	public static Map<String,Object> getDTO(Map<String,String[]> temp) {
	    Map<String, Object> dto = null;
		if (temp != null && !temp.isEmpty()) {
		    dto = new HashMap<String,Object>();
			Set<Entry<String,String[]>> entrySet = temp.entrySet();
			String val[] = null;
			for(Entry<String,String[]> entry:entrySet) {
			    val = entry.getValue();
			    for(int i = 0; i < val.length; i++) {
			        val[i] = val[i].replaceAll("'", "''");
			    }
				if (val.length == 1) dto.put( entry.getKey(), val[0]);
				else dto.put(entry.getKey(), val);	
			}
		}
		return dto;
	}
}
