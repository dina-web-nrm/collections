/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.exceptions.utils;
  


/**
 *
 * @author idali
 */
public class Util {
    
    private static Util instance = null; 
    
    public static synchronized Util getInstance() {
        if (instance == null) {
            instance = new Util();
        }
        return instance;
    }
    
    public String buildEntityNameWithIdString(Class clazz, long id) {
        StringBuilder sb = new StringBuilder();
        sb.append(clazz.getSimpleName());
        sb.append(": [ id = ");
        sb.append(id);
        sb.append(" ]");
        return sb.toString();
    }  

}
