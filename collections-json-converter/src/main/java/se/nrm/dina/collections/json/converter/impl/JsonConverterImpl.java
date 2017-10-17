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
import se.nrm.dina.collections.annotation.CollectionsId;
import se.nrm.dina.collections.annotation.CollectionsManyToOne;
import se.nrm.dina.collections.annotation.CollectionsOneToMany;
import se.nrm.dina.collections.json.converter.JsonConverter;
import se.nrm.dina.collections.json.converter.util.CommonString;
import se.nrm.dina.collections.json.converter.util.Util;

/**
 *
 * @author idali
 * @param <T>
 */
@Slf4j
public class JsonConverterImpl<T extends Object>implements JsonConverter<T>, Serializable  {
    
    public JsonConverterImpl() {
        
    }

    @Override
    public JsonObject convert(T object) {
        if(object instanceof List) { 
            List<Object> beans = (List<Object>)object;
            return convertList(beans); 
        } else { 
            return convertBean(object);
        } 
    }
    
    private JsonObject convertList(List<Object> beans) {

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

//        jsonBuilder.add(CommonString.getInstance().getMeta(), buildMetadata(map, beans.size(), status));
        jsonBuilder.add(CommonString.getInstance().getData(), dataArrBuilder);
        return jsonBuilder.build();
    }

    
    private JsonObject convertBean(Object object) {
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
         
        JsonObjectBuilder attBuilder = Json.createObjectBuilder();
        JsonObjectBuilder relBuilder = Json.createObjectBuilder();
           
        String type = bean.getClass().getSimpleName(); 
        Field[] fields = bean.getClass().getDeclaredFields();
           
        dataBuilder.add(CommonString.getInstance().getType(), type);
        dataBuilder.add(CommonString.getInstance().getId(), getIdValue(bean));
         
        Arrays.stream(fields) 
                .forEach((Field f) -> {
                    Object fieldValue = getFieldValue(f, bean);
                    if (fieldValue != null) {
                        if (f.isAnnotationPresent(CollectionsManyToOne.class)) {
                            buildManyToOneRelationship(f, fieldValue, relBuilder);
                        } else if (f.isAnnotationPresent(CollectionsOneToMany.class)) {
                            buildOneToManyRelationship(f, fieldValue, relBuilder); 
                        } else {
                            addAttributes(attBuilder, fieldValue, f.getName());
                        }
                    }
                });

        dataBuilder.add(CommonString.getInstance().getAttributes(), attBuilder);
        dataBuilder.add(CommonString.getInstance().getRelationships(), relBuilder); 
    }
    
    private void buildOneToManyRelationship(Field field, Object bean, JsonObjectBuilder relBuilder) {
        log.info("buildOneToManyRelationship");
        JsonObjectBuilder subBuilder = Json.createObjectBuilder();
        JsonObjectBuilder subDataBuilder = Json.createObjectBuilder();
        JsonArrayBuilder subDataArrBuilder = Json.createArrayBuilder();
 
        List<Object> subBeans = (List<Object>) bean;
        if (subBeans != null && !subBeans.isEmpty()) {
            
            String fieldName = field.getAnnotation(CollectionsOneToMany.class).name();
            
            subBeans.stream()
                    .forEach(b -> {  
                        subDataBuilder.add(CommonString.getInstance().getType(), fieldName);
                        subDataBuilder.add(CommonString.getInstance().getId(), getIdValue(b));
                        subDataArrBuilder.add(subDataBuilder);
                    });
            subBuilder.add(CommonString.getInstance().getData(), subDataArrBuilder);
            relBuilder.add(fieldName, subBuilder);
        } 
    }

    private void buildManyToOneRelationship(Field field, Object bean, JsonObjectBuilder relBuilder) {
        log.info("buildManyToOneRelationship : {}", bean);
        
        JsonObjectBuilder subBuilder = Json.createObjectBuilder();
        if (bean != null) {
            String fieldName = field.getAnnotation(CollectionsManyToOne.class).name();
            String type = field.getAnnotation(CollectionsManyToOne.class).type();
       
            JsonObjectBuilder subDataBuilder = Json.createObjectBuilder(); 
            subDataBuilder.add(CommonString.getInstance().getType(), type);
            
            subDataBuilder.add(CommonString.getInstance().getId(), getIdValue(bean));
            subBuilder.add(CommonString.getInstance().getData(), subDataBuilder);
            relBuilder.add(fieldName, subBuilder);
        }  
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

    private Field getIdField(Field[] fields) {

        return Arrays.asList(fields)
                .stream()
                .filter(f -> f.isAnnotationPresent(CollectionsId.class))
                .findAny()
                .get();
    }

    public Long getField(Class clazz, String fieldName) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);

            return (Long) field.get(clazz);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw ex;
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
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
}
