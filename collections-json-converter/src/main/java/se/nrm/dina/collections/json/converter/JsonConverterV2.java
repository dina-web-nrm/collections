/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.json.converter;

import java.util.List;
import javax.json.JsonObject;
import se.nrm.dina.collections.data.model.impl.CatalogedUnit;
import se.nrm.dina.collections.data.model.impl.IndividualGroup;
import se.nrm.dina.collections.data.model.impl.PhysicalUnit;

/**
 *
 * @author idali
 * @param <T>
 */
public interface JsonConverterV2<T extends Object> {
    
    JsonObject convertIndividualGroups(IndividualGroup individualGroup);
    
    JsonObject convertIndividualGroups(List<IndividualGroup> individualGroups);
    
    JsonObject convertCatalogedUnits(List<CatalogedUnit> catalogedUnits, String include);
    
    JsonObject convertPhysicalUnits(List<PhysicalUnit> physicalUnits, String include);

    JsonObject convert(T bean);

    JsonObject convertErrors();
}
