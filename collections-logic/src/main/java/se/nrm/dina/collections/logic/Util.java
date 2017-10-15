/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.logic;
 
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.collections.data.model.helper.DataModelHelper;

/**
 *
 * @author idali
 */
@Slf4j
public class Util { 
    
    private static Util instance = null;
  
    public static synchronized Util getInstance() {
        if (instance == null) {
            instance = new Util();
        }
        return instance;
    }
     
    /**
     * Converts class name to class
     * 
     * @param classname
     * @return Class
     */
    public Class convertClassNameToClass(String classname) {
 
        log.info("convertClassNameToClass : {}", classname);
        
        try {
            return Class.forName(DataModelHelper.getInstance().getEntityPackage()+ capitalizeFirstChar(classname));   
        } catch (ClassNotFoundException ex) {  
            return null;
        }  
    } 
    
    /**
     * Reforms class name to uppercase on first letter
     *
     * @param s
     * @return
     */
    public String capitalizeFirstChar(String s) { 

        if (s.length() == 0) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    
}
