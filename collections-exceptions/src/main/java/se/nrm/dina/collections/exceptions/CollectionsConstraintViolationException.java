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
public class CollectionsConstraintViolationException extends CollectionsException {
    
    private final int code;
    
    public CollectionsConstraintViolationException(String source, String detail, String errorMsg, int code) {
        super(source, detail, errorMsg);
        this.code = code;
    }

    public int getCode() {
        return code;
    } 
}
