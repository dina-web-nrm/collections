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
import se.nrm.dina.collections.data.model.impl.LocalityInformation;
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
        log.info("convertIndividualGroups : {}", individualGroups);
        
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder jsonBuilder = factory.createObjectBuilder();
        JsonObjectBuilder dataBuilder = Json.createObjectBuilder();
        JsonArrayBuilder dataArrBuilder = Json.createArrayBuilder(); 
        
        if(individualGroups != null && !individualGroups.isEmpty()) {
            individualGroups.stream()
                    .forEach(individualGroup -> {
                        buildIndividualGroup(individualGroup, dataBuilder, include);
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
        addOccurrences(individualGroup.getOccurrences(), attBuilder, include);
  
        dataBuilder.add(CommonString.getInstance().getAttributes(), attBuilder);
    }
    
    private void addPhysicalUnits(List<PhysicalUnit> physicalUnits, JsonObjectBuilder attBuilder, String include) {
        
        JsonObjectBuilder subBuilder = Json.createObjectBuilder();
        JsonArrayBuilder dataArrBuilder = Json.createArrayBuilder(); 
        if(physicalUnits != null && !physicalUnits.isEmpty()) {
            physicalUnits.stream()
                    .forEach(physicalUnit -> {
                        subBuilder.add(CommonString.getInstance().getId(), physicalUnit.getId()); 
                        if(include != null && include.contains("physicalUnit")) {
                            if(physicalUnit.getAlternateIdentifiersText() != null) {
                                subBuilder.add("alternateIdentifiersText", physicalUnit.getAlternateIdentifiersText());
                            } 
                            if(physicalUnit.getNormalStorageLocationText() != null) {
                                subBuilder.add("normalStorageLocationText", physicalUnit.getNormalStorageLocationText());
                            }
                            if(physicalUnit.getPhysicalUnitText() != null) {
                                subBuilder.add("physicalUnitText", physicalUnit.getPhysicalUnitText());
                            }  
                            addCatalogedUnit(physicalUnit.getBelongsToCatalogedUnit(), subBuilder, include.contains("catalogedUnit"));  
                        } 
                        dataArrBuilder.add(subBuilder);
                    });  
        }  
        attBuilder.add("physicalUnits", dataArrBuilder);
    }

    private void addCatalogedUnit(CatalogedUnit catalogedUnit, JsonObjectBuilder attBuilder, boolean isCatalogedUnit) { 
        JsonObjectBuilder subBuilder = Json.createObjectBuilder();
        if(catalogedUnit != null) {
            subBuilder.add(CommonString.getInstance().getId(), catalogedUnit.getId()); 
            if(isCatalogedUnit) { 
                if(catalogedUnit.getPublishRecord() != null) {
                    subBuilder.add("publishRecord", catalogedUnit.getPublishRecord());
                }
                if(catalogedUnit.getRemarks() != null) {
                    subBuilder.add("remarks", catalogedUnit.getRemarks());
                }
                if(catalogedUnit.getStoredUnderTaxonName() != null) {
                    subBuilder.add("storedUnderTaxonName", catalogedUnit.getStoredUnderTaxonName());
                }
                if(catalogedUnit.getCatalogNumber() != null) {
                    subBuilder.add("catalogNumber", catalogedUnit.getCatalogNumber());
                }    
//                attBuilder.add("catalogedUnit", subBuilder);
            } 
//            else {
//                attBuilder.add(CommonString.getInstance().getId(), catalogedUnit.getId()); 
//          //      attBuilder.add("catalogedUnitId", catalogedUnit.getId()); // TODO: ?
//            } 
        } 
        attBuilder.add("catalogedUnit", subBuilder); 
    }
    
    private void addFeatureObservations(List<FeatureObservation> featureObservations, JsonObjectBuilder attBuilder, String include) {
        
        log.info("addFeatureObservations : {} -- {}", featureObservations, include);
         
        JsonObjectBuilder subBuilder = Json.createObjectBuilder();
        JsonArrayBuilder dataArrBuilder = Json.createArrayBuilder(); 
        if(featureObservations != null && !featureObservations.isEmpty()) { 
            featureObservations.stream()
                    .forEach(featureObservation -> {
                        subBuilder.add(CommonString.getInstance().getId(), featureObservation.getId());  
                        
                        if(include != null && include.contains("featureObservation")) { 
                            if(featureObservation.getFeatureObservationAgent() != null) {
                                subBuilder.add("featureObservationAgent", featureObservation.getFeatureObservationAgent());
                            } 
                            if(featureObservation.getFeatureObservationDate() != null) {
                                subBuilder.add("featureObservationDate", featureObservation.getFeatureObservationDate());
                            } 
                            if(featureObservation.getFeatureObservationText() != null) {
                                subBuilder.add("featureObservationText", featureObservation.getFeatureObservationText());
                            } 
                            if(featureObservation.getMethodText() != null) {
                                subBuilder.add("methodText", featureObservation.getMethodText());
                            }  
                            addFeatureObservationType(featureObservation.getIsOfFeatureObservationType(), subBuilder, include.contains("featureObservationType")); 
                        } 
                        dataArrBuilder.add(subBuilder);
                    });  
        }  
        attBuilder.add("featureObservations", dataArrBuilder);
        
    }
            
    private void addFeatureObservationType(FeatureObservationType featureObservationType, JsonObjectBuilder attBuilder, boolean isType) {
        log.info("addFeatureObservationType : {} -- {}", featureObservationType, isType);
        
        JsonObjectBuilder subBuilder = Json.createObjectBuilder();
        if(featureObservationType != null) { 
            subBuilder.add(CommonString.getInstance().getId(), featureObservationType.getId());  
            if(isType) { 
                if(featureObservationType.getFeatureObservationTypeName() != null) {
                    subBuilder.add("featureObservationTypeName", featureObservationType.getFeatureObservationTypeName());   
                }  
            }  
        } 
        attBuilder.add("featureObservationType", subBuilder); 
    }
        
    private void addIdentifications(List<Identification> identifications, JsonObjectBuilder attBuilder, boolean isIdentification) {
        log.info("addIdentifications" );
        
        JsonObjectBuilder subBuilder = Json.createObjectBuilder();
        JsonArrayBuilder dataArrBuilder = Json.createArrayBuilder(); 
        if(identifications != null && !identifications.isEmpty()) {
            identifications.stream()
                    .forEach(identification -> { 
                        subBuilder.add(CommonString.getInstance().getId(), identification.getId());
                        if(isIdentification) {
                            if(identification.getIdentificationRemarks() != null) {
                                subBuilder.add("identificationRemarks", identification.getIdentificationRemarks());
                            }
                            if(identification.getIdentificationText() != null) {
                                subBuilder.add("identificationText", identification.getIdentificationText()); 
                            }
                            if(identification.getIdentifiedAsVerbatim() != null) {
                                subBuilder.add("identifiedAsVerbatim", identification.getIdentifiedAsVerbatim());
                            }
                            if(identification.getIdentifiedByAgentText() != null) {
                                subBuilder.add("identifiedByAgentText", identification.getIdentifiedByAgentText());
                            }
                            if(identification.getIdentifiedDateText()!= null) {
                                subBuilder.add("identifiedDateText", identification.getIdentifiedDateText());
                            }
                            
                            if(identification.getIdentifiedDay()!= null) {
                                subBuilder.add("identifiedDay", identification.getIdentifiedDay());
                            }
                            
                            if(identification.getIdentifiedMonth()!= null) {
                                subBuilder.add("identifiedMonth", identification.getIdentifiedMonth());
                            }
                            
                            if(identification.getIdentifiedYear()!= null) {
                                subBuilder.add("identifiedYear", identification.getIdentifiedYear());
                            }
                             
                            if(identification.getIsCurrentIdentification() != null) {
                                subBuilder.add("isCurrentIdentification", identification.getIsCurrentIdentification()); 
                            } 
                        }
                        dataArrBuilder.add(subBuilder);
                    });  
        }  
        attBuilder.add("identifications", dataArrBuilder);
    }
    
    private void addOccurrences(List<Occurrence> occurrences, JsonObjectBuilder attBuilder, String include) {

        JsonObjectBuilder subBuilder = Json.createObjectBuilder();
        JsonArrayBuilder dataArrBuilder = Json.createArrayBuilder(); 
        if(occurrences != null && !occurrences.isEmpty()) {
            occurrences.stream()
                    .forEach(occurrence -> {
                        subBuilder.add(CommonString.getInstance().getId(), occurrence.getId());
                        if(include != null && include.contains("occurrence")) {
                            if(occurrence.getCollectorsText() != null) {
                                subBuilder.add("collectorsText", occurrence.getCollectorsText()); 
                            }
                            if(occurrence.getDayEnd() != null) {
                                subBuilder.add("dayEnd", occurrence.getDayEnd());
                            }
                            if(occurrence.getDayStart() != null) {
                                subBuilder.add("dayStart", occurrence.getDayStart());
                            }
                            if(occurrence.getEstablishmentMeansStandardized() != null) {
                                subBuilder.add("establishmentMeansStandardized", occurrence.getEstablishmentMeansStandardized());
                            }
                            if(occurrence.getExpeditionText() != null) {
                                subBuilder.add("expeditionText", occurrence.getExpeditionText());
                            } 
                            if(occurrence.getIsDeathEvent() != null) {
                                subBuilder.add("isDeathEvent", occurrence.getIsDeathEvent());
                            } 
              
                            if(occurrence.getMonthEnd() != null) {
                                subBuilder.add("monthEnd", occurrence.getMonthEnd());
                            }
                            if(occurrence.getMonthStart() != null) {
                                subBuilder.add("monthStart", occurrence.getMonthStart());
                            } 
                            if(occurrence.getOccurrenceDateText() != null) {
                                subBuilder.add("occurrenceDateText", occurrence.getOccurrenceDateText());
                            }
                            if(occurrence.getYearEnd() != null) {
                                subBuilder.add("yearEnd", occurrence.getYearEnd());
                            }
                            if(occurrence.getYearStart() != null) {
                                subBuilder.add("yearStart", occurrence.getYearStart());
                            }  
                            addLocalityInformation(occurrence.getLocalityInformation(), subBuilder, include.contains("localityInformation"));
                        }
                        dataArrBuilder.add(subBuilder);
                    });  
        } 
        attBuilder.add("occurrences", dataArrBuilder);
    }

    private void addLocalityInformation(LocalityInformation localityInformation, JsonObjectBuilder attBuilder, boolean isLocality) {
        JsonObjectBuilder subBuilder = Json.createObjectBuilder();
        if (localityInformation != null) {
            subBuilder.add(CommonString.getInstance().getId(), localityInformation.getId());
            if (isLocality) {
                if (localityInformation.getContinentStandardized() != null) {
                    subBuilder.add("coordinatesVerbatim", localityInformation.getContinentStandardized());
                }

                if (localityInformation.getCoordinateUncertaintyInMeters() != null) {
                    subBuilder.add("coordinateUncertaintyInMeters", localityInformation.getCoordinateUncertaintyInMeters());
                }

                if (localityInformation.getCountryStandardized() != null) {
                    subBuilder.add("countryStandardized", localityInformation.getCountryStandardized());
                }
                
                if (localityInformation.getDistrictStandardized()!= null) {
                    subBuilder.add("districtStandardized", localityInformation.getDistrictStandardized());
                }
                
                if (localityInformation.getGeodeticDatumStandardized()!= null) {
                    subBuilder.add("geodeticDatumStandardized", localityInformation.getGeodeticDatumStandardized());
                }
                
                if (localityInformation.getGeoreferenceSourcesText()!= null) {
                    subBuilder.add("georeferenceSourcesText", localityInformation.getGeoreferenceSourcesText());
                }
                
                if (localityInformation.getLatitudeStandardized()!= null) {
                    subBuilder.add("latitudeStandardized", localityInformation.getLatitudeStandardized());
                }
                
                if (localityInformation.getLocalityRemarks()!= null) {
                    subBuilder.add("localityRemarks", localityInformation.getLocalityRemarks());
                }
                
                if (localityInformation.getLocalityStandardized()!= null) {
                    subBuilder.add("localityStandardized", localityInformation.getLocalityStandardized());
                }
                
                if (localityInformation.getLocalityVerbatim()!= null) {
                    subBuilder.add("localityVerbatim", localityInformation.getLocalityVerbatim());
                }
                
                if (localityInformation.getLongitudeStandardized()!= null) {
                    subBuilder.add("longitudeStandardized", localityInformation.getLongitudeStandardized());
                }
                
                if (localityInformation.getMaximumDepthInMeters()!= null) {
                    subBuilder.add("maximumDepthInMeters", localityInformation.getMaximumDepthInMeters());
                }
                
                if (localityInformation.getMaximumElevationInMeters()!= null) {
                    subBuilder.add("maximumElevationInMeters", localityInformation.getMaximumElevationInMeters());
                }
                
                if (localityInformation.getMinimumDepthInMeters()!= null) {
                    subBuilder.add("minimumDepthInMeters", localityInformation.getMinimumDepthInMeters());
                }
                
                if (localityInformation.getMinimumElevationInMeters()!= null) {
                    subBuilder.add("minimumElevationInMeters", localityInformation.getMinimumElevationInMeters());
                }
                
                if (localityInformation.getProvinceStandardized()!= null) {
                    subBuilder.add("provinceStandardized", localityInformation.getProvinceStandardized());
                }
            }
        }
        attBuilder.add("localityInformation", subBuilder); 
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
