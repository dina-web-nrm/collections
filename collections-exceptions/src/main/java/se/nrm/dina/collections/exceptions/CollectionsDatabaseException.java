/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.exceptions;

import javax.ejb.ApplicationException;

/**
 *
 * @author idali
 */ 
@ApplicationException
public class CollectionsDatabaseException extends CollectionsException {
    
    private final String code; 
    
    public CollectionsDatabaseException(String source, String detail, String errorMsg, String code) {
        super(source, detail, errorMsg);
        this.code = code;
    }

    public String getCode() {
        return code;
    } 
}
