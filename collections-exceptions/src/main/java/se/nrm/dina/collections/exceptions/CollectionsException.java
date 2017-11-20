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
public abstract class CollectionsException extends RuntimeException {
     
    private final String source;
    private final String detail;
     
    public CollectionsException(String source, String detail, String errorMsg) {
        super(errorMsg); 
        this.source = source;
        this.detail = detail;
    }
 
    public String getSource() {
        return source;
    }

    public String getDetail() {
        return detail;
    } 
     
    public String getErrorMessage() {
        return getMessage();
    }
}
