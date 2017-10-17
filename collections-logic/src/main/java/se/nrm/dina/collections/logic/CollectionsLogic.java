/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.logic;

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
        
        IndividualGroup ig;
        try { 
            Class clazz = Util.getInstance().convertClassNameToClass(entityName);
            EntityBean bean = (EntityBean) mapper.readValue(theJson, clazz);
            if(bean instanceof PhysicalUnit) {
                PhysicalUnit pu = (PhysicalUnit) bean;
                ig = pu.getRepresentsIndividualGroupId();
                Occurrence occurrence = pu.getIsCollectedAtOccurrenceId();
                if(occurrence != null) {
                    occurrence.setInvolvesIndividualGroupId(ig); 
                }
            } else if(bean instanceof IndividualGroup) {
                ig = (IndividualGroup) bean; 
                
                List<Occurrence> occurrences = ig.getOccurrenceList();
                if(occurrences != null) {
                    occurrences.stream().forEach(o -> {
                        o.setInvolvesIndividualGroupId(ig); 
                    });
                }
                
                List<PhysicalUnit> physicalUnits = ig.getPhysicalUnitList(); 
                if(physicalUnits != null) {
                    physicalUnits.stream().forEach(p -> {
                        p.setRepresentsIndividualGroupId(ig);
                        if(occurrences != null && !occurrences.isEmpty()) {
                            Occurrence o = occurrences.get(0);
                            p.setIsCollectedAtOccurrenceId(o);
                        }
                    });
                } 
 
                List<FeatureObservation> foList = ig.getFeatureObservationList();
                if(foList != null) {
                    foList.stream().forEach(f -> {
                        f.setAppliesToIndividualGroupId(ig);
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
    
//    
//    private void setValueToBean(EntityBean parent, Field f) {
//        try {
//            if (Util.getInstance().isEntity(parent.getClass(), f.getName())) {
//                setChildToBean(parent, f);
//            } else if (JpaReflectionHelper.getInstance().isCollection(parent.getClass(), f.getName())) {
//                setChildrenToBean(parent, f);
//            }
//        } catch (DinaException e) {
//            throw e;
//        }
//    }
//    
//    
//    private void setChildToBean(EntityBean parent, Field f) { 
//        try {
//            f.setAccessible(true);
//            EntityBean child = (EntityBean) f.get(parent); 
//            if (child != null) {
//                Field field = JpaReflectionHelper.getInstance().getIDField(child);
//
//                field.setAccessible(true);
//                if (field.get(child) != null && (Integer) field.get(child) > 0) { 
//                    Class clazz = child.getClass(); 
//                    EntityBean entity = dao.findById((Integer) field.get(child), clazz, JpaReflectionHelper.getInstance().isVersioned(clazz)); 
//                    if (entity == null) {
//                        setTimeStampCreated(child);
//                        setCreatedByUser(child, createdByUserBean);
//                        f.set(parent, child);
//                        Field[] fields = child.getClass().getDeclaredFields();
//                        Arrays.stream(fields)
//                                .forEach(fd -> {
//                                    setValueToBean(child, fd);
//                                });
//                        setParentToChild(fields, child, parent);
//                    } else {
//                        f.set(parent, entity);
//                    }
//                } else {
//                    setTimeStampCreated(child);
//                    setCreatedByUser(child, createdByUserBean);
//                    f.set(parent, child);
//                    Field[] fields = child.getClass().getDeclaredFields();
//                    Arrays.stream(fields)
//                            .forEach(fd -> {
//                                setValueToBean(child, fd);
//                            });
//                    setParentToChild(fields, child, parent);
//                }
//            }
//        } catch (IllegalArgumentException | IllegalAccessException ex) {
//            throw new DinaException(400, ex.getCause().getClass().getSimpleName(), ex.getMessage());
//        }
//    }
//
//    private void setChildrenToBean(EntityBean parent, Field field) {
//        try {
//            field.setAccessible(true);
//            List<EntityBean> children = (List) field.get(parent);
//            Field[] fields;
//            if (children != null && !children.isEmpty()) {
//                for (EntityBean child : children) {
//                    setTimeStampCreated(child);
//                    setCreatedByUser(child, createdByUserBean);
//                    fields = child.getClass().getDeclaredFields();
//                    setParentToChild(fields, child, parent);
//                }
//                field.set(parent, children);
//
//            }
//        } catch (IllegalArgumentException | IllegalAccessException ex) {
//            throw new DinaException(400, ex.getCause().getClass().getSimpleName(), ex.getMessage());
//        }
//    }
//
//    private void setParentToChild(Field[] fields, EntityBean child, EntityBean parent) {
//        Arrays.asList(fields).stream()
//                .forEach(f -> {
//                    if (JpaReflectionHelper.getInstance().isEntity(child.getClass(), f.getName())) {
//                        try {
//                            setChildToBean(child, f);
//                            f.setAccessible(true);
//                            if (f.getName().toLowerCase().contains(parent.getClass().getSimpleName().toLowerCase())) {
//                                f.set(child, parent);
//                            }
//                        } catch (IllegalArgumentException | IllegalAccessException ex) {
//                            throw new DinaException(400, ex.getCause().getClass().getSimpleName(), ex.getMessage());
//                        }
//                    }
//                });
//    }

//    public List<EntityBean> getAllPhysicalUnit() {
//        
//        log.info("getAllPhysicalUnit"); 
//        
//        return dao.findAll(PhysicalUnit.class); 
//    }
}
