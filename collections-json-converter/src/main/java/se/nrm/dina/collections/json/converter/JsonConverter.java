/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.json.converter;

import javax.json.JsonObject;

/**
 *
 * @author idali
 * @param <T>
 */
public interface JsonConverter<T extends Object> {
    
    JsonObject convert(T bean);
     
//    JsonObject convert(int count, Map<String, ?>  meta, String source);
//    
//    JsonObject convert(Map<String, ?> meta, List<String> errors, int statusCode, String errorType, String source);  
}
