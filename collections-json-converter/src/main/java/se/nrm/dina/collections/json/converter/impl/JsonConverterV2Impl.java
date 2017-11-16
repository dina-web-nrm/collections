/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.json.converter.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject; 
import javax.json.JsonObjectBuilder;
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.collections.annotation.CollectionsManyToOne;
import se.nrm.dina.collections.annotation.CollectionsOneToMany;
import se.nrm.dina.collections.data.model.impl.CatalogedUnit;
import se.nrm.dina.collections.data.model.impl.IndividualGroup;
import se.nrm.dina.collections.data.model.impl.Occurrence;
import se.nrm.dina.collections.data.model.impl.PhysicalUnit;
import se.nrm.dina.collections.json.converter.JsonConverterV2;
import se.nrm.dina.collections.json.converter.util.CommonString;
import se.nrm.dina.collections.json.converter.util.Util;

/**
 *
 * @author idali
 * @param <T>
 */
@Slf4j
public class JsonConverterV2Impl<T extends Object> implements JsonConverterV2<T>, Serializable {
     
    
    @Override
    public JsonObject convertPhysicalUnits(List<PhysicalUnit> physicalUnits) { 
        
        log.info("convertPhysicalUnits");
        
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder jsonBuilder = factory.createObjectBuilder();
        JsonObjectBuilder dataBuilder = Json.createObjectBuilder();
        JsonArrayBuilder dataArrBuilder = Json.createArrayBuilder(); 
        
        if(!physicalUnits.isEmpty()) {
            physicalUnits.stream()
                    .forEach(p -> {
                        buildDataJson(p, dataBuilder);
                        dataArrBuilder.add(dataBuilder);
                    });
        }
        
        jsonBuilder.add(CommonString.getInstance().getData(), dataArrBuilder);
        return jsonBuilder.build();
    }

     
    private void buildDataJson(PhysicalUnit physicalUnit, JsonObjectBuilder dataBuilder) {
         
        JsonObjectBuilder attBuilder = Json.createObjectBuilder(); 
           
        String type = Util.getInstance().reformClassName(physicalUnit.getClass().getSimpleName()); 
        long id = physicalUnit.getId();
           
        dataBuilder.add(CommonString.getInstance().getType(), type);
        dataBuilder.add(CommonString.getInstance().getId(), id);
        
        attBuilder.add("physicalUnitText", physicalUnit.getPhysicalUnitText());
        attBuilder.add("normalStorageLocation", physicalUnit.getNormalStorageLocation());
        
        addCatalogedUnit(physicalUnit.getBelongsToCatalogedUnit(), attBuilder);
        addOccurrence(physicalUnit.getIsCollectedAtOccurrence(), attBuilder);
        addIndividualGroup(physicalUnit.getRepresentsIndividualGroup(), attBuilder);

        dataBuilder.add(CommonString.getInstance().getAttributes(), attBuilder); 
    }
    
        
    private void addOccurrence(Occurrence occurrence, JsonObjectBuilder attBuilder) { 
        JsonObjectBuilder subBuilder = Json.createObjectBuilder();
        if(occurrence != null) {
            subBuilder.add("collectorsText", occurrence.getCollectorsText());
            subBuilder.add("id", occurrence.getId()); 
            subBuilder.add("localityText", occurrence.getLocalityText()); 
            subBuilder.add("occurrenceDateText", occurrence.getOccurrenceDateText());
            
            addIndividualGroup(occurrence.getInvolvesIndividualGroup(), subBuilder);
        }
        attBuilder.add("occurrence", subBuilder);
    }
    
    private void addIndividualGroup(IndividualGroup individualGroup, JsonObjectBuilder attBuilder) {
        JsonObjectBuilder subBuilder = Json.createObjectBuilder();
        if(individualGroup != null) {
            subBuilder.add("id", individualGroup.getId()); 
        }
        attBuilder.add("individualGroup", subBuilder);
    }
    
    private void addCatalogedUnit(CatalogedUnit catalogedUnit, JsonObjectBuilder attBuilder) { 
        JsonObjectBuilder subBuilder = Json.createObjectBuilder();
        if(catalogedUnit != null) {
            subBuilder.add("catalogNumber", catalogedUnit.getCatalogNumber());
            subBuilder.add("id", catalogedUnit.getId()); 
            
        }
        attBuilder.add("catalogedUnit", subBuilder);
    }
    
    
    
    
    
    
    
    

    @Override
    public JsonObject convert(T object) {
        log.info("convert");
        
        if(object instanceof List) { 
            List<Object> beans = (List<Object>)object;
            return convertList(beans); 
        } else { 
            return convertBean(object);
        } 
    }
    
    private JsonObject convertList(List<Object> beans) {
        log.info("convertList");

        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder jsonBuilder = factory.createObjectBuilder();
        JsonObjectBuilder dataBuilder = Json.createObjectBuilder();
        JsonArrayBuilder dataArrBuilder = Json.createArrayBuilder(); 

        if (!beans.isEmpty()) { 
            beans.stream()
                    .forEach(b -> { 
                        buildDataJson(b, dataBuilder);
                        dataArrBuilder.add(dataBuilder);
                    });
        } 
        jsonBuilder.add(CommonString.getInstance().getData(), dataArrBuilder);
        return jsonBuilder.build();
    }

    
    private JsonObject convertBean(Object object) {
        log.info("convertBean");
        
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder jsonBuilder = factory.createObjectBuilder();
        JsonObjectBuilder dataBuilder = Json.createObjectBuilder(); 

        if(object != null) { 
            buildDataJson(object, dataBuilder);
        } 
//        jsonBuilder.add(CommonString.getInstance().getMeta(), buildMetadata(map, 1, status));
        jsonBuilder.add(CommonString.getInstance().getData(), dataBuilder); 
        return jsonBuilder.build();
    }
 
    private void buildDataJson(Object bean, JsonObjectBuilder dataBuilder) {
        log.info("buildDataJson");
         
        JsonObjectBuilder attBuilder = Json.createObjectBuilder();
//        JsonObjectBuilder relBuilder = Json.createObjectBuilder();
           
        String type = Util.getInstance().reformClassName(bean.getClass().getSimpleName()); 
        Field[] fields = bean.getClass().getDeclaredFields();
           
        dataBuilder.add(CommonString.getInstance().getType(), type);
        dataBuilder.add(CommonString.getInstance().getId(), getIdValue(bean));
         
        Arrays.stream(fields) 
                .forEach((Field f) -> {
                    Object fieldValue = getFieldValue(f, bean);
                    if (fieldValue != null) {
                        if (f.isAnnotationPresent(CollectionsManyToOne.class)) { 
                            buildManyToOneAttributes(f, fieldValue, attBuilder);
                        } else if (f.isAnnotationPresent(CollectionsOneToMany.class)) {
                            buildOneToManyAttributes(f, fieldValue, attBuilder); 
                        } else {
                            addAttributes(attBuilder, fieldValue, f.getName());
                        }
                    }
                });

        dataBuilder.add(CommonString.getInstance().getAttributes(), attBuilder);
//        dataBuilder.add(CommonString.getInstance().getRelationships(), relBuilder); 
    }
    
    private void buildManyToOneAttributes(Field field, Object bean, JsonObjectBuilder attBuilder) {
        log.info("buildManyToOneAttributes : {}", bean);
        
        JsonObjectBuilder subBuilder = Json.createObjectBuilder();
        String attrName = field.getAnnotation(CollectionsManyToOne.class).name(); 
        log.info("attrName : {}", attrName);
        
        if(bean != null) {
            subBuilder.add(CommonString.getInstance().getId(), getIdValue(bean));
            Field[] fields = bean.getClass().getDeclaredFields();
            Arrays.stream(fields)
                    .filter(f -> !f.isAnnotationPresent(CollectionsManyToOne.class) && !f.isAnnotationPresent(CollectionsOneToMany.class))
                    .forEach((Field f) -> {  
                        log.info("field : {}", f.getName());
                        Object fieldValue = getFieldValue(f, bean);
                        log.info("fieldValue : {}", fieldValue);
                        addAttributes(subBuilder, fieldValue, f.getName());
                    });
        }
        attBuilder.add(attrName, subBuilder);
    }
    
    private void buildOneToManyAttributes(Field field, Object bean, JsonObjectBuilder attBuilder) {
        log.info("buildOneToManyAttributes");
        JsonObjectBuilder subBuilder = Json.createObjectBuilder();
        String attrName = field.getAnnotation(CollectionsOneToMany.class).name(); 
        JsonArrayBuilder subDataArrBuilder = Json.createArrayBuilder();
        
        List<Object> subBeans = (List<Object>) bean;
        if (subBeans != null && !subBeans.isEmpty()) { 
            
            subBeans.stream()
                    .forEach(b -> {   
                        subBuilder.add(CommonString.getInstance().getId(), getIdValue(b));
                        Field[] fields = b.getClass().getDeclaredFields();
                        Arrays.stream(fields)
                            .filter(f -> !f.isAnnotationPresent(CollectionsManyToOne.class) && !f.isAnnotationPresent(CollectionsOneToMany.class))
                            .forEach((Field f) -> {  
                                Object fieldValue = getFieldValue(f, bean);
                                addAttributes(subBuilder, fieldValue, f.getName());
                            });
                        subDataArrBuilder.add(subBuilder);
                    }); 
        }   
        attBuilder.add(attrName, subDataArrBuilder);
    }
    
    private Object getFieldValue(Field field, Object bean) {
        try {
            field.setAccessible(true);
            return field.get(bean); 
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            log.error(ex.getMessage());
            return null;
        } 
    }
    
    
    private Long getIdValue(Object bean) {
        log.info("getIdValue : {}", bean);

        try {
            Method method = bean.getClass().getMethod("getEntityId");
            Long l = (Long) method.invoke(bean); 
            return l;
        } catch (NoSuchMethodException | SecurityException | InvocationTargetException | IllegalAccessException | IllegalArgumentException ex) {
            log.error(ex.getMessage());
            return 0l;
        } 
    }
    
    private void addAttributes(JsonObjectBuilder attBuilder, Object value, String key) { 
        
        log.info("addAttributes : {} -- {}", key, value);
        
        if (value instanceof Integer) {
            attBuilder.add(key, (int) value);
        } else if (value instanceof Short) {
            attBuilder.add(key, (Short) value);
        } else if (value instanceof Date) {
            attBuilder.add(key, Util.getInstance().dateToString((Date) value));
        } else if (value instanceof java.util.Date) {
            attBuilder.add(key, Util.getInstance().dateToString((java.util.Date) value));
        } else if (value instanceof BigDecimal) {
            attBuilder.add(key, (BigDecimal) value);
        } else if (value instanceof Boolean) {
            attBuilder.add(key, (Boolean) value);
        } else if (value instanceof Double) {
            attBuilder.add(key, (Double) value);
        } else if (value instanceof Float) {
            attBuilder.add(key, (Float) value);
        } else if (value instanceof Long) {
            attBuilder.add(key, (Long) value);
        } else {
            attBuilder.add(key, (String) value);
        }
    }
    
    
    

    @Override
    public JsonObject convertErrors() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    
    
    
}
