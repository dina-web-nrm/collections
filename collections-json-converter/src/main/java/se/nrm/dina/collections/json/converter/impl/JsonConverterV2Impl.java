/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.json.converter.impl;

import java.io.Serializable;  
import java.io.StringReader; 
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject; 
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.collections.data.model.impl.CatalogedUnit;
import se.nrm.dina.collections.data.model.impl.FeatureObservation;
import se.nrm.dina.collections.data.model.impl.FeatureObservationType;
import se.nrm.dina.collections.data.model.impl.Identification;
import se.nrm.dina.collections.data.model.impl.IndividualGroup;
import se.nrm.dina.collections.data.model.impl.Occurrence;
import se.nrm.dina.collections.data.model.impl.PhysicalUnit;
import se.nrm.dina.collections.exceptions.CollectionsBadRequestException;
import se.nrm.dina.collections.exceptions.CollectionsException;
import se.nrm.dina.collections.exceptions.utils.ErrorCode;
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
    public JsonObject convertIndividualGroup(IndividualGroup individualGroup, String include) {
        log.info("convertIndividualGroup");
        
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder jsonBuilder = factory.createObjectBuilder();
        JsonObjectBuilder dataBuilder = Json.createObjectBuilder();
        
        if(individualGroup != null) {
            buildIndividualGroup(individualGroup, dataBuilder, include);
        }
        
        jsonBuilder.add(CommonString.getInstance().getData(), dataBuilder); 
        return jsonBuilder.build(); 
    }
    
    @Override
    public JsonObject convertIndividualGroups(List<IndividualGroup> individualGroups, String include) {
        log.info("convertIndividualGroups");
        
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder jsonBuilder = factory.createObjectBuilder();
        JsonObjectBuilder dataBuilder = Json.createObjectBuilder();
        JsonArrayBuilder dataArrBuilder = Json.createArrayBuilder(); 
        
        if(individualGroups != null && !individualGroups.isEmpty()) {
            individualGroups.stream()
                    .forEach(ig -> {
                        buildIndividualGroup(ig, dataBuilder, include);
                        dataArrBuilder.add(dataBuilder);
                    });
        }
        
        jsonBuilder.add(CommonString.getInstance().getData(), dataArrBuilder);
        return jsonBuilder.build();
    }
    
    private void buildIndividualGroup(IndividualGroup individualGroup, JsonObjectBuilder dataBuilder, String include) {
        JsonObjectBuilder attBuilder = Json.createObjectBuilder();
 
        String type = Util.getInstance().reformClassName(individualGroup.getClass().getSimpleName());
         
        dataBuilder.add(CommonString.getInstance().getType(), type);
        dataBuilder.add(CommonString.getInstance().getId(), individualGroup.getId());
        
        addPhysicalUnits(individualGroup.getPhysicalUnits(), attBuilder, include);
        addFeatureObservations(individualGroup.getFeatureObservations(), attBuilder, include);
        addIdentifications(individualGroup.getIdentifications(), attBuilder, include != null && include.contains("identification"));
        addOccurrences(individualGroup.getOccurrences(), attBuilder, include != null && include.contains("occurrence"));
  
        dataBuilder.add(CommonString.getInstance().getAttributes(), attBuilder);
    }
    
    private void addPhysicalUnits(List<PhysicalUnit> physicalUnits, JsonObjectBuilder attBuilder, String include) {
        
        JsonObjectBuilder subBuilder = Json.createObjectBuilder();
        JsonArrayBuilder dataArrBuilder = Json.createArrayBuilder(); 
        if(physicalUnits != null && !physicalUnits.isEmpty()) {
            physicalUnits.stream()
                    .forEach(pu -> {
                        subBuilder.add(CommonString.getInstance().getId(), pu.getId()); 
                        if(include != null && include.contains("physicalUnit")) {
                            if(pu.getPhysicalUnitText() != null) {
                                subBuilder.add("physicalUnitText", pu.getPhysicalUnitText());
                            }
                            if(pu.getPhysicalUnitText() != null) {
                                subBuilder.add("physicalUnitText", pu.getPhysicalUnitText());
                            }
                            if(pu.getNormalStorageLocationText() != null) {
                                subBuilder.add("normalStorageLocationText", pu.getNormalStorageLocationText());
                            }
                            if(pu.getAlternateIdentifiersText() != null) {
                                subBuilder.add("alternateIdentifiersText", pu.getAlternateIdentifiersText());
                            } 
                            addCatalogedUnit(pu.getBelongsToCatalogedUnit(), subBuilder, include.contains("catalogedUnit"));  
                        } 
                        dataArrBuilder.add(subBuilder);
                    });  
        }  
        attBuilder.add("physicalUnits", dataArrBuilder);
    }

    private void addCatalogedUnit(CatalogedUnit catalogedUnit, JsonObjectBuilder attBuilder, boolean isCatalogedUnit) { 
        JsonObjectBuilder subBuilder = Json.createObjectBuilder();
        if(catalogedUnit != null) {
            if(isCatalogedUnit) {
                if(catalogedUnit.getCatalogNumber() != null) {
                    subBuilder.add("catalogNumber", catalogedUnit.getCatalogNumber());
                } 
                if(catalogedUnit.getId() != null) {
                    subBuilder.add(CommonString.getInstance().getId(), catalogedUnit.getId()); 
                }
                if(catalogedUnit.getRemarks() != null) {
                    subBuilder.add("remarks", catalogedUnit.getRemarks());
                }
                if(catalogedUnit.getPublishRecord() != null) {
                    subBuilder.add("publishRecord", catalogedUnit.getPublishRecord());
                }
                if(catalogedUnit.getStoredUnderTaxonName() != null) {
                    subBuilder.add("storedUnderTaxonName", catalogedUnit.getStoredUnderTaxonName());
                }
                 
                attBuilder.add("catalogedUnit", subBuilder);
            } else {
                attBuilder.add("catalogedUnitId", catalogedUnit.getId());
            } 
        } else { 
            attBuilder.add("catalogedUnit", subBuilder);
        }
        
    }
    
    private void addFeatureObservations(List<FeatureObservation> featureObservations, JsonObjectBuilder attBuilder, String include) {
        
        log.info("addFeatureObservations : {} -- {}", featureObservations, include);
         
        JsonObjectBuilder subBuilder = Json.createObjectBuilder();
        JsonArrayBuilder dataArrBuilder = Json.createArrayBuilder(); 
        if(featureObservations != null && !featureObservations.isEmpty()) { 
            featureObservations.stream()
                    .forEach(fo -> {
                        subBuilder.add(CommonString.getInstance().getId(), fo.getId());  
                        
                        if(include != null && include.contains("featureObservations")) { 
                            if(fo.getFeatureObservationText() != null) {
                                subBuilder.add("featureObservationText", fo.getFeatureObservationText());
                            }
                            if(fo.getMethodText() != null) {
                                subBuilder.add("methodText", fo.getMethodText());
                            } 
                            
                            if(fo.getFeatureObservationAgent() != null) {
                                subBuilder.add("featureObservationAgent", fo.getFeatureObservationAgent());
                            } 
                            
                            if(fo.getFeatureObservationDate() != null) {
                                subBuilder.add("featureObservationDate", fo.getFeatureObservationDate());
                            } 
                            addFeatureObservationType(fo.getIsOfFeatureObservationType(), subBuilder, include.contains("featureObservationType")); 
                        } 
                        dataArrBuilder.add(subBuilder);
                    });  
        }  
        attBuilder.add("featureObservations", dataArrBuilder);
        
    }
            
    private void addFeatureObservationType(FeatureObservationType featureObservationType, JsonObjectBuilder attBuilder, boolean isType) {
        log.info("addFeatureObservations : {}", isType);
        
        JsonObjectBuilder subBuilder = Json.createObjectBuilder();
        if(featureObservationType != null) { 
            if(isType) {
                subBuilder.add(CommonString.getInstance().getId(), featureObservationType.getId());   
                if(featureObservationType.getFeatureObservationTypeName() != null) {
                    subBuilder.add("featureObservationTypeName", featureObservationType.getFeatureObservationTypeName());   
                } 
                attBuilder.add("featureObservationType", subBuilder);
            } else {
                attBuilder.add("featureObservationTypeId", featureObservationType.getId());
            } 
        } else {
            attBuilder.add("featureObservationType", subBuilder);
        } 
    }
        
    private void addIdentifications(List<Identification> identification, JsonObjectBuilder attBuilder, boolean isIdentification) {
        log.info("addIdentifications" );
        
        JsonObjectBuilder subBuilder = Json.createObjectBuilder();
        JsonArrayBuilder dataArrBuilder = Json.createArrayBuilder(); 
        if(identification != null && !identification.isEmpty()) {
            identification.stream()
                    .forEach(i -> { 
                        subBuilder.add(CommonString.getInstance().getId(), i.getId());
                        if(isIdentification) {
                            if(i.getIdentificationText() != null) {
                                subBuilder.add("identificationText", i.getIdentificationText()); 
                            }
                            if(i.getIdentificationRemarks() != null) {
                                subBuilder.add("identificationRemarks", i.getIdentificationRemarks());
                            }
                            if(i.getIdentifiedAsVerbatim() != null) {
                                subBuilder.add("identifiedAsVerbatim", i.getIdentifiedAsVerbatim());
                            }
                            if(i.getIdentifiedByAgentText() != null) {
                                subBuilder.add("identifiedByAgentText", i.getIdentifiedByAgentText());
                            }
                            if(i.getIdentifiedDay() != null) {
                                subBuilder.add("identifiedDay", i.getIdentifiedDay());
                            }
                            if(i.getIdentifiedMonth() != null) {
                                subBuilder.add("identifiedMonth", i.getIdentifiedMonth());
                            }
                            if(i.getIdentifiedYear() != null) {
                                subBuilder.add("identifiedYear", i.getIdentifiedYear());
                            }
                            if(i.getIdentifiedTaxonNameStandardized() != null) {
                                subBuilder.add("identifiedTaxonNameStandardized", i.getIdentifiedTaxonNameStandardized());
                            }
                            if(i.getIsCurrentIdentification() != null) {
                                subBuilder.add("isCurrentIdentification", i.getIsCurrentIdentification()); 
                            } 
                        }
                        dataArrBuilder.add(subBuilder);
                    });  
        }  
        attBuilder.add("identifications", dataArrBuilder);
    }
    
    private void addOccurrences(List<Occurrence> occurrences, JsonObjectBuilder attBuilder, boolean isOccurrences) {

        JsonObjectBuilder subBuilder = Json.createObjectBuilder();
        JsonArrayBuilder dataArrBuilder = Json.createArrayBuilder(); 
        if(occurrences != null && !occurrences.isEmpty()) {
            occurrences.stream()
                    .forEach(o -> {
                        subBuilder.add(CommonString.getInstance().getId(), o.getId());
                        if(isOccurrences) {
                            if(o.getCollectorsText() != null) {
                                subBuilder.add("collectorsText", o.getCollectorsText()); 
                            }
                            if(o.getLocalityText() != null) {
                                subBuilder.add("localityText", o.getLocalityText());
                            }
                            if(o.getOccurrenceDateText() != null) {
                                subBuilder.add("occurrenceDateText", o.getOccurrenceDateText());
                            }
                            if(o.getDayEnd() != null) {
                                subBuilder.add("dayEnd", o.getDayEnd());
                            }
                            if(o.getDayStart() != null) {
                                subBuilder.add("dayStart", o.getDayStart());
                            }
                            if(o.getEstablishmentMeansStandardized() != null) {
                                subBuilder.add("establishmentMeansStandardized", o.getEstablishmentMeansStandardized());
                            }
                            if(o.getExpeditionText() != null) {
                                subBuilder.add("expeditionText", o.getExpeditionText());
                            } 
                            if(o.getMonthStart() != null) {
                                subBuilder.add("monthStart", o.getMonthStart());
                            }
                            if(o.getMonthEnd() != null) {
                                subBuilder.add("monthEnd", o.getMonthEnd());
                            }
                            if(o.getYearStart() != null) {
                                subBuilder.add("yearStart", o.getYearStart());
                            }
                            if(o.getYearEnd() != null) {
                                subBuilder.add("yearEnd", o.getYearEnd());
                            }
                            if(o.getIsDeathEvent() != null) {
                                subBuilder.add("isDeathEvent", o.getIsDeathEvent());
                            } 
                        }
                        dataArrBuilder.add(subBuilder);
                    });  
        } 
        attBuilder.add("occurrences", dataArrBuilder); 
    }
    
    
    
    
    
    @Override
    public JsonObject convertSuccessMessage(String message) {
        return null;
    }
     
    
    
    @Override
    public JsonObject convertError(CollectionsException error) {
        List<CollectionsException> exceptions = new ArrayList<>();
        exceptions.add(error);
        return convertErrors(exceptions);
    }
    
    @Override
    public JsonObject convertErrors(List<CollectionsException> errors) { 
        log.info("convertErrors");

        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder jsonBuilder = factory.createObjectBuilder();
        JsonObjectBuilder errorsBuilder = Json.createObjectBuilder();
        JsonObjectBuilder sourceBuilder = Json.createObjectBuilder();
        JsonArrayBuilder errorArrBuilder = Json.createArrayBuilder();

        if (errors != null) {
            errors.stream()
                    .forEach(e -> {
                        errorsBuilder.add(CommonString.getInstance().getCODE(), e.getErrorCode());
                        sourceBuilder.add(CommonString.getInstance().getPoint(), e.getSource());
                        errorsBuilder.add(CommonString.getInstance().getSource(), sourceBuilder);
                        errorsBuilder.add(CommonString.getInstance().getDetail(), e.getDetail());
                        
                        errorArrBuilder.add(errorsBuilder);
                    });
        } 
        
        jsonBuilder.add(CommonString.getInstance().getErrors(), errorArrBuilder);
        return jsonBuilder.build();
    }

 
    @Override
    public JsonObject readInJson(String json) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(json))) {
            return jsonReader.readObject();
        }
    }
    
    @Override
    public JsonObject getAttributes(JsonObject json) { 
         
        if(json.containsKey(CommonString.getInstance().getAttributes())) {
            return json.getJsonObject(CommonString.getInstance().getAttributes());
        }
        throw new CollectionsBadRequestException(ErrorCode.BAD_REQUEST_INVALID_JSON.name(),
                            ErrorCode.BAD_REQUEST_INVALID_JSON.getDetail("No body"),
                            ErrorCode.BAD_REQUEST_INVALID_JSON.name(),
                            "No body");
    }
     
    @Override
    public JsonArray getJsonArray(JsonObject json, String key) {
        if(json.containsKey(key)) {
            return json.getJsonArray(key); 
        } else {
            return null;
        } 
    } 
}
