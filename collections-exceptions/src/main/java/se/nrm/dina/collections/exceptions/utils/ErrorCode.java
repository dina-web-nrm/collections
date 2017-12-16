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

    DB_EXCEPTION ("database error"),
    DB_CONSTRAINT_VIOLATION (""),
    DB_OPTIMISTIC_LOCK (""),
    
    BAD_REQUEST (""),
    BAD_REQUEST_MISSING_PARAMETER ("Missing parameter."),
    BAD_REQUEST_ENTITY_NOT_IN_DB ("Entity is not in database."),
    BAD_REQUEST_INVALID_JSON ("Invalid json");
    
    private final String detail;
    

    ErrorCode(String detail) {
        this.detail = detail; 
    }
 
    public String getMessage() {
        return this.detail;
    }
  
    public String getDetail(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(detail);
        sb.append(" [");
        sb.append(message);
        sb.append("]");
        return sb.toString();
    }
     
    public String getText(ErrorCode errorCode) {
        return errorCode.name();
    } 
}
