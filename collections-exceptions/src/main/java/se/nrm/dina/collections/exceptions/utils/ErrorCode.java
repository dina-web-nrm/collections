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
public enum ErrorCode {

    DB_EXCEPTION,
    DB_CONSTRAINT_VIOLATION,
    DB_OPTIMISTIC_LOCK; 

    public String getText(ErrorCode errorCode) {
        return errorCode.name();
    } 
}
