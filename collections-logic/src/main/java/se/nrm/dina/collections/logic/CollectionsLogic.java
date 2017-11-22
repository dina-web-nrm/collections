/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.logic;
 
import se.nrm.dina.collections.logic.utils.Util;
import com.fasterxml.jackson.databind.ObjectMapper; 
import java.util.List; 
import java.io.IOException;
import java.io.Serializable;     
import javax.ejb.EJB; 
import javax.inject.Inject;
import javax.json.JsonObject; 
import lombok.extern.slf4j.Slf4j;  
import se.nrm.dina.collections.data.model.EntityBean; 
import se.nrm.dina.collections.data.model.impl.FeatureObservation;
import se.nrm.dina.collections.data.model.impl.IndividualGroup;
import se.nrm.dina.collections.data.model.impl.Occurrence;
import se.nrm.dina.collections.data.model.impl.PhysicalUnit;
import se.nrm.dina.collections.jpa.CollectionsDao; 
import se.nrm.dina.collections.json.converter.JsonConverter; 
import se.nrm.dina.collections.json.converter.JsonConverterV2;  
import se.nrm.dina.collections.logic.query.QueryBuilder;

/**
 *
 * @author idali
 */
@Slf4j
public class CollectionsLogic implements Serializable  {
    
    private final ObjectMapper mapper;
    
    @Inject
    private JsonConverter json;
    
    @Inject 
    private JsonConverterV2 json2;
     
    
    @EJB
    private CollectionsDao dao;

    public CollectionsLogic() {
        mapper = new ObjectMapper();
    }
    
    public JsonObject getIndividualGroupById(long id, String include) {
        log.info("getIndividualGroupById");
        
        return json2.convertIndividualGroup((IndividualGroup) dao.findById(id, IndividualGroup.class), include);
    }
    
    public JsonObject getIndividualGroup(String catalogNumber, String include) {
        log.info("getIndividualGroup : {}", catalogNumber);
         
        return json2.convertIndividualGroups(dao.findByJPQL(QueryBuilder.getInstance().getQueryFindIndividualGroupsByCatalogNumber(catalogNumber)), include);
    }
    
//    public JsonObject getCatalogedUnits(String include) {
//        log.info("getCatalogedUnits");
//        
//        return json2.convertPhysicalUnits(dao.findAll(CatalogedUnit.class), include); 
//    }
//
//    public JsonObject getPhysicalUnits(String include) {
//        log.info("getPhysiclUnits");
//   
//        return json2.convertPhysicalUnits(dao.findAll(PhysicalUnit.class), include); 
//    }
//    
    
    
    public JsonObject getAll(String entityName) {
        log.info("getAll : {}", entityName);
        
        Class clazz = Util.getInstance().convertClassNameToClass(entityName); 
        return json.convert(dao.findAll(clazz));
    }
    
    public JsonObject getById(String entityName, long id) {
        Class clazz = Util.getInstance().convertClassNameToClass(entityName);
        return json.convert(dao.findById(id, clazz));
    }
    
    public JsonObject saveEntity(String entityName, String theJson ) {
        log.info("saveEntity : {} -- {}", entityName, theJson);
        
        IndividualGroup ig;
        try { 
            Class clazz = Util.getInstance().convertClassNameToClass(entityName);
            EntityBean bean = (EntityBean) mapper.readValue(theJson, clazz);
            if(bean instanceof PhysicalUnit) {
                PhysicalUnit pu = (PhysicalUnit) bean;
                ig = pu.getRepresentsIndividualGroup();
                Occurrence occurrence = pu.getIsCollectedAtOccurrence();
                if(occurrence != null) {
                    occurrence.setInvolvesIndividualGroup(ig); 
                }
            } else if(bean instanceof IndividualGroup) {
                ig = (IndividualGroup) bean; 
                
                List<Occurrence> occurrences = ig.getOccurrences();
                if(occurrences != null) {
                    occurrences.stream().forEach(o -> {
                        o.setInvolvesIndividualGroup(ig); 
                    });
                }
                
                List<PhysicalUnit> physicalUnits = ig.getPhysicalUnits(); 
                if(physicalUnits != null) {
                    physicalUnits.stream().forEach(p -> {
                        p.setRepresentsIndividualGroup(ig);
                        if(occurrences != null && !occurrences.isEmpty()) {
                            Occurrence o = occurrences.get(0);
                            p.setIsCollectedAtOccurrence(o);
                        }
                    });
                } 
 
                List<FeatureObservation> foList = ig.getFeatureObservations();
                if(foList != null) {
                    foList.stream().forEach(f -> {
                        f.setAppliesToIndividualGroup(ig);
                    });
                }
            }
            return json.convert(dao.create(bean));
        } catch (IOException ex) {
            log.error(ex.getMessage());
            return null;
        } 
    }

    public JsonObject savePhysicalUnit(String theJson) {
        log.info("savePhysicalUnit : {}", theJson);
 
        try {
            EntityBean bean = mapper.readValue(theJson, PhysicalUnit.class); 
            return json.convert(dao.create(bean));
        } catch (IOException ex) {
            log.error(ex.getMessage());
            return null;
        } 
    }

    /**
     * Deletes an entity in database
     *
     * @param entityName
     * @param id
     */
    public void delete(String entityName, long id) {
        log.info("delete");

        Class clazz = Util.getInstance().convertClassNameToClass(entityName);
        EntityBean bean = dao.findByReference(id, clazz);

        if (bean != null) {
            dao.delete(bean);
        }
    }
 
}
