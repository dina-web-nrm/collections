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
     
    public CollectionsDatabaseException(String source, String detail, String code, String errorMsg) {
        super(source, detail, code, errorMsg);
    } 
}
