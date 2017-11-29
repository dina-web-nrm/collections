/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.json.converter;

import java.util.List;
import javax.json.JsonArray;
import javax.json.JsonObject; 
import se.nrm.dina.collections.data.model.impl.IndividualGroup; 
import se.nrm.dina.collections.exceptions.CollectionsException;

/**
 *
 * @author idali
 * @param <T>
 */
public interface JsonConverterV2<T extends Object> {
    
    JsonObject convertIndividualGroup(IndividualGroup individualGroup, String include);
    
    JsonObject convertIndividualGroups(List<IndividualGroup> individualGroups, String include);
    
//    JsonObject convertCatalogedUnits(List<CatalogedUnit> catalogedUnits, String include);
//    
//    JsonObject convertPhysicalUnits(List<PhysicalUnit> physicalUnits, String include);

//    JsonObject convert(T bean);
    
    JsonObject convertSuccessMessage(String message);
    
    JsonObject readInJson(String json);
    
    JsonObject getAttributes(JsonObject json);
    
    JsonArray getJsonArray(JsonObject json, String key);

    JsonObject convertError(CollectionsException error);
    
    JsonObject convertErrors(List<CollectionsException> errors);
}
