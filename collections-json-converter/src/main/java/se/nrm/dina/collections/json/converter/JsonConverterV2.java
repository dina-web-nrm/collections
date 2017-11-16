/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.json.converter;

import java.util.List;
import javax.json.JsonObject;
import se.nrm.dina.collections.data.model.impl.PhysicalUnit;

/**
 *
 * @author idali
 * @param <T>
 */
public interface JsonConverterV2<T extends Object> {
    
    JsonObject convertPhysicalUnits(List<PhysicalUnit> physicalUnits);

    JsonObject convert(T bean);

    JsonObject convertErrors();
}
