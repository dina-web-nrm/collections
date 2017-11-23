/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.exceptions;

/**
 *
 * @author idali
 */
public class CollectionsOptimisticLockException extends CollectionsException {
    
    public CollectionsOptimisticLockException(String source, String detail, String errorCode, String errorMsg) {
        super(source, detail, errorCode, errorMsg);
    } 
}
