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
public class CollectionsBadRequestException extends CollectionsException {
     
    public CollectionsBadRequestException(String source, String detail, String code, String errorMsg) {
        super(source, detail, code, errorMsg);
    } 
}