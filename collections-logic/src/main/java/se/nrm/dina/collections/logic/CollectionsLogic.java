/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;   
import javax.ejb.EJB; 
import javax.inject.Inject;
import javax.json.JsonObject;
import lombok.extern.slf4j.Slf4j;  
import se.nrm.dina.collections.data.model.EntityBean;
import se.nrm.dina.collections.data.model.impl.PhysicalUnit;
import se.nrm.dina.collections.jpa.CollectionsDao; 
import se.nrm.dina.collections.json.converter.JsonConverter;

/**
 *
 * @author idali
 */
@Slf4j
public class CollectionsLogic implements Serializable  {
    
    private final ObjectMapper mapper;
    
    @Inject
    private JsonConverter json;
    
    @EJB
    private CollectionsDao dao;
    
    public CollectionsLogic() { 
        mapper = new ObjectMapper();
    }
    
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
        
        try { 
            Class clazz = Util.getInstance().convertClassNameToClass(entityName);
            EntityBean bean = (EntityBean) mapper.readValue(theJson, clazz);
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

//    public List<EntityBean> getAllPhysicalUnit() {
//        
//        log.info("getAllPhysicalUnit"); 
//        
//        return dao.findAll(PhysicalUnit.class); 
//    }
}
