/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.data.model.helper;

/**
 *
 * @author idali
 */
public class DataModelHelper {
    
    private final String ENTITY_PACKAGE = "se.nrm.dina.collections.data.model.impl."; 
    
    private static DataModelHelper instance = null;
     
    public static synchronized DataModelHelper getInstance() {
        if (instance == null) {
            instance = new DataModelHelper();
        }
        return instance;
    }
    
    public String getEntityPackage() {
        return ENTITY_PACKAGE;
    }
    
}
