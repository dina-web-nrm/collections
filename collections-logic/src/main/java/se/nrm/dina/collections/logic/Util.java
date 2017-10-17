/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.logic;
 
import java.lang.reflect.Field;
import java.util.Arrays;
import javax.persistence.Id;
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.collections.data.model.EntityBean;
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

    /**
     * Checks if the field is an Entity
     *
     * @param clazz
     * @param fieldName
     * @return boolean
     */
    public boolean isEntity(Class clazz, String fieldName) {
//        logger.info("isEntity : {} -- {}", clazz, fieldName);
        try {
            return clazz.getDeclaredField(fieldName).getType().getName().contains(DataModelHelper.getInstance().getEntityPackage());
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
                return false;
            } else {
                return isEntity(superClass, fieldName);
            }
        }
    }
    
     
    private Field getIdField(Field[] fields, String entityName) {  
        try {
            return Arrays.asList(fields)
                .stream()
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findAny() 
                .get();
        } catch (java.util.NoSuchElementException ex) {
            throw ex;
        }
    }

    /**
     * Find id field name for the entity bean
     *
     * @param bean
     * @return String, name of the id field of this entity bean
     */
    public Field getIDField(EntityBean bean) {

        return getIdField(bean.getClass().getDeclaredFields(), bean.getClass().getSimpleName());

    }

}
