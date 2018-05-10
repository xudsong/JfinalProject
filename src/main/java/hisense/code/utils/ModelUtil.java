package hisense.code.utils;

import com.jfinal.plugin.activerecord.Model;

public class ModelUtil {
	
    /**
     * 
     * @param model
     * @return
     */
    public static void trimModel(Model model) {
        String[] aname = model._getAttrNames();
        for(int i = 0; i < aname.length; i++) {
            Object aaa = model.get(aname[i]);
            if(aaa == null) {
                continue;
            }
            if(aaa.getClass() == String.class) {
                String value =  model.getStr(aname[i]).trim();
                model.set(aname[i],value);
            }
        }
    }

}
