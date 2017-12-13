/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.logic;
 
import se.nrm.dina.collections.logic.utils.Util; 
import java.util.List;  
import java.io.Serializable;      
import java.util.ArrayList;
import java.util.stream.IntStream; 
import javax.ejb.EJB; 
import javax.inject.Inject;  
import javax.json.JsonArray; 
import javax.json.JsonObject;     
import lombok.extern.slf4j.Slf4j;  
import se.nrm.dina.collections.data.model.EntityBean; 
import se.nrm.dina.collections.data.model.impl.CatalogedUnit;
import se.nrm.dina.collections.data.model.impl.FeatureObservation;
import se.nrm.dina.collections.data.model.impl.FeatureObservationType;
import se.nrm.dina.collections.data.model.impl.Identification;
import se.nrm.dina.collections.data.model.impl.IndividualGroup;
import se.nrm.dina.collections.data.model.impl.Occurrence;
import se.nrm.dina.collections.data.model.impl.PhysicalUnit; 
import se.nrm.dina.collections.exceptions.CollectionsBadRequestException; 
import se.nrm.dina.collections.exceptions.CollectionsConstraintViolationException;
import se.nrm.dina.collections.exceptions.CollectionsDatabaseException;
import se.nrm.dina.collections.exceptions.CollectionsException;
import se.nrm.dina.collections.exceptions.utils.ErrorCode;
import se.nrm.dina.collections.jpa.CollectionsDao;  
import se.nrm.dina.collections.json.converter.JsonConverterV2;  
import se.nrm.dina.collections.json.converter.util.CommonString;
import se.nrm.dina.collections.logic.query.QueryBuilder;

/**
 *
 * @author idali
 */
@Slf4j
public class CollectionsLogic implements Serializable  {
      
    @Inject 
    private JsonConverterV2 json2;
     
    
    @EJB
    private CollectionsDao dao;

    public CollectionsLogic() {
    }
    
    public JsonObject getIndividualGroupById(long id, String include) {
        log.info("getIndividualGroupById");
        
        try {
            return json2.convertIndividualGroup((IndividualGroup) dao.findById(id, IndividualGroup.class), include);
        } catch(CollectionsException e) {
            throw e;
        } 
    }
    
    public JsonObject getIndividualGroup(String catalogNumber, String taxonStandarized, String include) {
        log.info("getIndividualGroup : {} -- {}", catalogNumber, taxonStandarized);
 
        if(catalogNumber == null || catalogNumber.isEmpty()) { 
            throw new CollectionsBadRequestException("individualGroup.physicalUnit.catalogedUnit", 
                                                     ErrorCode.BAD_REQUEST_MISSING_PARAMETER.getDetail("catalogNumber = " + catalogNumber), 
                                                     ErrorCode.BAD_REQUEST_MISSING_PARAMETER.name(), 
                                                     ErrorCode.BAD_REQUEST_MISSING_PARAMETER.getMessage()); 
        } 
        
 
     
        return json2.convertIndividualGroups(dao.findByJPQL(QueryBuilder.getInstance()
                                                .getQueryFindIndividualGroupsByCatalogNumberAndIdentificationTaxonStanderized(catalogNumber, taxonStandarized)),
                                                include);
    }
 
    
    private void buildIndividualGroup(String theJson, boolean isEditing, IndividualGroup individualGroup) {
        JsonObject dataJson = json2.readInJson(theJson).getJsonObject(CommonString.getInstance().getData());
        JsonObject attrJson = json2.getAttributes(dataJson);
        JsonArray additionalData = json2.getJsonArray(dataJson, "additionalData");
 
        if(attrJson.containsKey("causeOfDeathStandardized")) {
            individualGroup.setCauseOfDeathStandardized(attrJson.getString("causeOfDeathStandardized")); 
        }
        
        if(attrJson.containsKey("causeOfDeathText")) {
            individualGroup.setCauseOfDeathText(attrJson.getString("causeOfDeathText")); 
        }
        
        if(attrJson.containsKey("originStandardized")) {
            individualGroup.setOriginStandardized(attrJson.getString("originStandardized")); 
        }
        
        try { 
            addFeatureObservationsFromJson(attrJson, individualGroup, isEditing);
            addOccurrences(attrJson, individualGroup, isEditing);
            addIdentifications(attrJson, individualGroup, isEditing);
            addPhysicalUnit(additionalData, attrJson, individualGroup, isEditing);
        } catch (CollectionsException e) {
            throw e;
        } 
    }

    public JsonObject updateIndvidualGroup(String theJson, long id) {
        log.info("updateIndvidualGroup");

        try { 
            IndividualGroup individualGroup = (IndividualGroup) dao.findById(id, IndividualGroup.class);
            if(individualGroup == null) {
                return json2.convertError(new CollectionsBadRequestException("IndividualGroup [id = " + id + "]", 
                                                                             ErrorCode.BAD_REQUEST_ENTITY_NOT_IN_DB.getDetail("IndividualGroup"), 
                                                                             ErrorCode.BAD_REQUEST_ENTITY_NOT_IN_DB.name(), 
                                                                             "Update IndividualGroup [id = " + id + "] not in database"));
            }
            buildIndividualGroup(theJson, true, individualGroup);
            return json2.convertIndividualGroup((IndividualGroup) dao.merge(individualGroup), null);
        } catch (CollectionsException e) {
//            return json2.convertError(e);
            throw e;
        }
    }

    public JsonObject saveIndividualGroup(String theJson) {
        log.info("saveIndividualGroup");
           
        IndividualGroup individualGroup = new IndividualGroup();  
         
        buildIndividualGroup(theJson, false, individualGroup);
          
        try { 
            return json2.convertIndividualGroup((IndividualGroup) dao.create(individualGroup), null);
        } catch(CollectionsException e) {
//            return json2.convertError(e);
            throw e;
        }  
    }

    private CatalogedUnit getCatalogedUnitFromJson(JsonObject catalogedUnitJson) { 
        CatalogedUnit catalogedUnit;
        if(catalogedUnitJson.containsKey(CommonString.getInstance().getId())) {
            int id = catalogedUnitJson.getInt(CommonString.getInstance().getId());
            catalogedUnit = (CatalogedUnit) dao.findById(id, CatalogedUnit.class);
            if(catalogedUnit == null) {
                throw new CollectionsBadRequestException("CatalogedUnit [id = " + id + "]", 
                                                             ErrorCode.BAD_REQUEST_ENTITY_NOT_IN_DB.getDetail("CatalogedUnit with id = " + id + " not in database"),
                                                             ErrorCode.BAD_REQUEST_ENTITY_NOT_IN_DB.name(), 
                                                             "Entity not in database");
            }
        } else {
            catalogedUnit = new CatalogedUnit();
        } 
        if(catalogedUnitJson.containsKey("catalogNumber")) {
            catalogedUnit.setCatalogNumber(catalogedUnitJson.getString("catalogNumber"));
        } 
        
        if(catalogedUnitJson.containsKey("publishRecord")) {
            catalogedUnit.setPublishRecord(catalogedUnitJson.getBoolean("publishRecord"));
        } 
        
        if(catalogedUnitJson.containsKey("remarks")) {
            catalogedUnit.setRemarks(catalogedUnitJson.getString("remarks"));
        } 
        
        if(catalogedUnitJson.containsKey("storedUnderTaxonName")) {
            catalogedUnit.setStoredUnderTaxonName(catalogedUnitJson.getString("storedUnderTaxonName"));
        } 
        return catalogedUnit;
    }
     
    private PhysicalUnit getPhysicalUnitFromJson(CatalogedUnit catalogedUnit, JsonObject physicalUnitJson, boolean isEditing) {
         
        PhysicalUnit physicalUnit = new PhysicalUnit();
        if(isEditing) {
            if(physicalUnitJson.containsKey(CommonString.getInstance().getId())) {
                int id = physicalUnitJson.getInt(CommonString.getInstance().getId());
                physicalUnit = (PhysicalUnit) dao.findById(id, PhysicalUnit.class); 
                if(physicalUnit == null) {
                    throw new CollectionsBadRequestException("PhysicalUnit [id = " + id + "]", 
                                                             ErrorCode.BAD_REQUEST_ENTITY_NOT_IN_DB.getDetail("PhysicalUnit with id = " + id + " not in database"),
                                                             ErrorCode.BAD_REQUEST_ENTITY_NOT_IN_DB.name(), 
                                                             "Entity not in database");
                } 
                try { 
                    catalogedUnit = getCatalogedUnitFromJson(physicalUnitJson.getJsonObject("catalogedUnit"));
                } catch(CollectionsException e) {
                    throw e;
                } 
            }  
        }  
       
        if(physicalUnitJson.containsKey("physicalUnitText")) {
            physicalUnit.setPhysicalUnitText(physicalUnitJson.getString("physicalUnitText"));
        }
        
        if(physicalUnitJson.containsKey("normalStorageLocationText")) {
            physicalUnit.setNormalStorageLocationText(physicalUnitJson.getString("normalStorageLocationText"));
        }
                
        if(physicalUnitJson.containsKey("alternateIdentifiersText")) {
            physicalUnit.setAlternateIdentifiersText(physicalUnitJson.getString("alternateIdentifiersText"));
        }
                
        physicalUnit.setBelongsToCatalogedUnit(catalogedUnit); 
        return physicalUnit;
    }
 
    private void addPhysicalUnit(JsonArray additionalData, JsonObject attrJson, 
                                IndividualGroup individualGroup, boolean isEditing) throws CollectionsException {

        JsonArray physicalUnitsJson = json2.getJsonArray(attrJson, "physicalUnits");
        List<PhysicalUnit> physicalUnits = new ArrayList<>();  
         
        CatalogedUnit catalogedUnit = getCatalogedUnitFromJson(additionalData);
        if(catalogedUnit != null) { 
            catalogedUnit.setPhysicalUnits(physicalUnits);
        }
 
        IntStream.range(0, physicalUnitsJson.size())
                .forEach(i -> {
                    JsonObject physicalUnitJson = physicalUnitsJson.getJsonObject(i);
                    PhysicalUnit physicalUnit = getPhysicalUnitFromJson(catalogedUnit, physicalUnitJson, isEditing); 
                    physicalUnit.setRepresentsIndividualGroup(individualGroup);
 
                    physicalUnits.add(physicalUnit);
                });
        individualGroup.setPhysicalUnits(physicalUnits);
    }

    private CatalogedUnit getCatalogedUnitFromJson(JsonArray additionalData) {
        if (additionalData == null) {
            return null;
        } else {
            CatalogedUnit catalogedUnit = new CatalogedUnit();
            IntStream.range(0, additionalData.size())
                    .forEach(i -> {
                        JsonObject additionalJson = additionalData.getJsonObject(i);
                        String type = additionalJson.getString(CommonString.getInstance().getType());

                        if (type.equals("catalogedUnit")) {
                            JsonObject catalogedUnitAttrs = additionalJson.getJsonObject(CommonString.getInstance().getAttributes());
                            catalogedUnit.setCatalogNumber(catalogedUnitAttrs.getString("catalogNumber"));

                            if (catalogedUnitAttrs.containsKey("catalogNumber")) {
                                catalogedUnit.setCatalogNumber(catalogedUnitAttrs.getString("catalogNumber"));
                            }

                            if (catalogedUnitAttrs.containsKey("publishRecord")) {
                                catalogedUnit.setPublishRecord(catalogedUnitAttrs.getBoolean("publishRecord"));
                            }

                            if (catalogedUnitAttrs.containsKey("remarks")) {
                                catalogedUnit.setRemarks(catalogedUnitAttrs.getString("remarks"));
                            }

                            if (catalogedUnitAttrs.containsKey("storedUnderTaxonName")) {
                                catalogedUnit.setStoredUnderTaxonName(catalogedUnitAttrs.getString("storedUnderTaxonName"));
                            }

//                        catalogedUnit.setPhysicalUnits(physicalUnits);
                        }
                    });
            return catalogedUnit;
        }
    }
    
    private Identification getIdentificationFromJson(JsonObject jsonObject, boolean isEditing) {
        Identification identification = new Identification();
        if(isEditing) {
            if(jsonObject.containsKey(CommonString.getInstance().getId())) {
                int id = jsonObject.getInt(CommonString.getInstance().getId());
                identification = (Identification) dao.findById(id, Identification.class);
                if(identification == null) {
                     throw new CollectionsBadRequestException("Identification [id = " + id + "]", 
                                                             ErrorCode.BAD_REQUEST_ENTITY_NOT_IN_DB.getDetail("Identification with id = " + id + " not in database"),
                                                             ErrorCode.BAD_REQUEST_ENTITY_NOT_IN_DB.name(), 
                                                             "Entity not in database");
                }
            }
        } 
        
        if(jsonObject.containsKey("identificationRemarks")) {
            identification.setIdentificationRemarks(jsonObject.getString("identificationRemarks"));
        }
          
        if(jsonObject.containsKey("identificationText")) {
            identification.setIdentificationText(jsonObject.getString("identificationText"));
        }
      
        if(jsonObject.containsKey("identifiedAsVerbatim")) {
            identification.setIdentifiedAsVerbatim(jsonObject.getString("identifiedAsVerbatim"));
        }
        
        if(jsonObject.containsKey("identifiedByAgentText")) {
            identification.setIdentifiedByAgentText(jsonObject.getString("identifiedByAgentText"));
        }
        
        if(jsonObject.containsKey("identifiedDay")) {
            identification.setIdentifiedDay(jsonObject.getInt("identifiedDay"));
        } 
        
        if(jsonObject.containsKey("identifiedMonth")) {
            identification.setIdentifiedMonth(jsonObject.getInt("identifiedMonth"));
        }
        
        if(jsonObject.containsKey("identifiedYear")) {
            identification.setIdentifiedYear(jsonObject.getInt("identifiedYear"));
        }
        
        if(jsonObject.containsKey("identifiedTaxonNameStandardized")) {
            identification.setIdentifiedTaxonNameStandardized(jsonObject.getString("identifiedTaxonNameStandardized"));
        } 
        
        if(jsonObject.containsKey("isCurrentIdentification")) {
            identification.setIsCurrentIdentification(jsonObject.getBoolean("isCurrentIdentification"));
        } 
        return identification;
    }

    private void addIdentifications(JsonObject attrJson, IndividualGroup individualGroup, boolean isEditing) {
         JsonArray identificationsJson = json2.getJsonArray(attrJson, "identifications");
         
        List<Identification> identifications = new ArrayList<>();  
        
        IntStream.range(0, identificationsJson.size())
                .forEach(i -> {
                    JsonObject identificationJson = identificationsJson.getJsonObject(i);
                    Identification identification = getIdentificationFromJson(identificationJson, isEditing);
                    identification.setAppliesToIndividualGroup(individualGroup);
 
                    identifications.add(identification);
                });
        individualGroup.setIdentifications(identifications);
    }
 
    private FeatureObservationType getFeatureObservationTypeFromJson(JsonObject featureObservationJson, boolean isEditing) {
        log.info("getFeatureObservationTypeFromJson : {}", isEditing);
        
        FeatureObservationType type;
        
        int featureObservationTypeId = 0;
        String featureObservationTypeName = null;
        if (featureObservationJson.containsKey("featureObservationType")) {
            JsonObject typeJson = featureObservationJson.getJsonObject("featureObservationType");
            if(isEditing) {
                if(typeJson.containsKey(CommonString.getInstance().getId())) {
                    featureObservationTypeId = typeJson.getInt(CommonString.getInstance().getId()); 
                } 
            } 
            if(typeJson.containsKey("featureObservationTypeName")) {
                featureObservationTypeName = typeJson.getString("featureObservationTypeName");
            } 
        } else if (featureObservationJson.containsKey("featureObservationTypeId")) {
            featureObservationTypeId = featureObservationJson.getInt("featureObservationTypeId");
        } else {
            throw new CollectionsBadRequestException(FeatureObservationType.class.getSimpleName(),
                    ErrorCode.BAD_REQUEST_MISSING_PARAMETER.getDetail(FeatureObservationType.class.getSimpleName()),
                    ErrorCode.BAD_REQUEST_MISSING_PARAMETER.name(),
                    ErrorCode.BAD_REQUEST_MISSING_PARAMETER.getMessage());
        }

        if (featureObservationTypeId > 0) { 
            type = (FeatureObservationType) dao.findById(featureObservationTypeId, FeatureObservationType.class);
            if(type == null) {
                throw new CollectionsBadRequestException("FeatureObservationType [id = " + featureObservationTypeId + "]", 
                                                          ErrorCode.BAD_REQUEST_ENTITY_NOT_IN_DB.getDetail("FeatureObservationType with id = " + featureObservationTypeId + " not in database"),
                                                          ErrorCode.BAD_REQUEST_ENTITY_NOT_IN_DB.name(), 
                                                          "Entity not in database");
            }
        } else {
            type = new FeatureObservationType();
            type.setFeatureObservationTypeName(featureObservationTypeName);
        }
        return type;
    }

    private FeatureObservation getFeatureObservationFromJson(JsonObject featureObservationJson, boolean isEditing) {

        FeatureObservation featureObservation = new FeatureObservation();

        try {
            if (isEditing) {
                if(featureObservationJson.containsKey(CommonString.getInstance().getId())) {
                    int id = featureObservationJson.getInt(CommonString.getInstance().getId());
                    featureObservation = (FeatureObservation) dao.findById(id, FeatureObservation.class);
                    if(featureObservation == null) {
                        throw new CollectionsBadRequestException("FeatureObservation [id = ]" + id,
                                                                 ErrorCode.BAD_REQUEST_ENTITY_NOT_IN_DB.getDetail("FeatureObservation [id = ]" + id), 
                                                                 ErrorCode.BAD_REQUEST_ENTITY_NOT_IN_DB.name(),
                                                                 ErrorCode.BAD_REQUEST_ENTITY_NOT_IN_DB.getMessage());
                    }
                }  
            }   

            featureObservation.setIsOfFeatureObservationType(getFeatureObservationTypeFromJson(featureObservationJson, isEditing));
        } catch (CollectionsException e) {
            throw e;
        }

        if (featureObservationJson.containsKey("featureObservationText")) { 
            featureObservation.setFeatureObservationText(featureObservationJson.getString("featureObservationText")); 
        } 
          
        if(featureObservationJson.containsKey("methodText")) {
            featureObservation.setMethodText(featureObservationJson.getString("methodText"));
        } 
        
        if(featureObservationJson.containsKey("featureObservationAgent")) {
            featureObservation.setFeatureObservationAgent(featureObservationJson.getString("featureObservationAgent"));
        } 
        
        if(featureObservationJson.containsKey("featureObservationDate")) {
            featureObservation.setFeatureObservationDate(featureObservationJson.getString("featureObservationDate"));
        } 
        return featureObservation;
    }

    private void addFeatureObservationsFromJson(JsonObject attrJson, IndividualGroup individualGroup, boolean isEditing) {
        
        JsonArray featureObservationsJson = json2.getJsonArray(attrJson, "featureObservations");   
        
        List<FeatureObservation> featureObservations = new ArrayList<>();
        
        IntStream.range(0, featureObservationsJson.size())
                .forEach(i -> {
                    JsonObject featureObservationJson = featureObservationsJson.getJsonObject(i); 
                    try {
                        FeatureObservation featureObservation = getFeatureObservationFromJson(featureObservationJson, isEditing);
                        featureObservation.setAppliesToIndividualGroup(individualGroup);
                        featureObservations.add(featureObservation);
                    } catch(CollectionsBadRequestException e) {
                        throw e;
                    }
                    
                });
        individualGroup.setFeatureObservations(featureObservations);
    } 
    
    private Occurrence getOccurrenceFromJson(JsonObject jsonObject, boolean isEditing) {
        Occurrence occurrence = new Occurrence();
        
        if(isEditing) {
            if (jsonObject.containsKey(CommonString.getInstance().getId())) {
                int id = jsonObject.getInt(CommonString.getInstance().getId());
                occurrence = (Occurrence) dao.findById(id, Occurrence.class);
                if(occurrence == null) {
                    throw new CollectionsBadRequestException("Occurrence [id = " + id + "]", 
                                                             ErrorCode.BAD_REQUEST_ENTITY_NOT_IN_DB.getDetail("Occurrence with id = " + id + " not in database"),
                                                             ErrorCode.BAD_REQUEST_ENTITY_NOT_IN_DB.name(), 
                                                             "Entity not in database");
                }
            }
        } 
       
        if(jsonObject.containsKey("collectorsText")) {
            occurrence.setCollectorsText(jsonObject.getString("collectorsText")); 
        }
 
        if(jsonObject.containsKey("dayStart")) {
            occurrence.setDayStart(jsonObject.getInt("dayStart"));
        }
        
        if(jsonObject.containsKey("dayEnd")) {
            occurrence.setDayEnd(jsonObject.getInt("dayEnd"));
        }
         
        if(jsonObject.containsKey("establishmentMeansStandardized")) {
            occurrence.setEstablishmentMeansStandardized(jsonObject.getString("establishmentMeansStandardized"));
        }
        
        if(jsonObject.containsKey("expeditionText")) {
            occurrence.setExpeditionText(jsonObject.getString("expeditionText"));
        }
 
        if(jsonObject.containsKey("isDeathEvent")) {
            occurrence.setIsDeathEvent(jsonObject.getBoolean("isDeathEvent"));
        }
         
        if(jsonObject.containsKey("localityText")) {
            occurrence.setLocalityText(jsonObject.getString("localityText"));
        }
        
        if(jsonObject.containsKey("monthStart")) {
            occurrence.setMonthStart(jsonObject.getInt("monthStart"));
        }
 
        if(jsonObject.containsKey("monthEnd")) {
            occurrence.setMonthEnd(jsonObject.getInt("monthEnd"));
        }
        
        if(jsonObject.containsKey("occurrenceDateText")) {
            occurrence.setOccurrenceDateText(jsonObject.getString("occurrenceDateText"));
        }
        
        if(jsonObject.containsKey("yearStart")) {
            occurrence.setYearStart(jsonObject.getInt("yearStart"));
        }
        
        if (jsonObject.containsKey("yearEnd")) {
            occurrence.setYearEnd(jsonObject.getInt("yearEnd"));
        }
        return occurrence;
    }

    private void addOccurrences(JsonObject attrJson, IndividualGroup individualGroup, boolean isEditing) {

        JsonArray occurrencesJson = json2.getJsonArray(attrJson, "occurrences");
        List<Occurrence> occurrences = new ArrayList<>();

        try {
            IntStream.range(0, occurrencesJson.size())
                    .forEach(i -> {
                        JsonObject occurrenceJson = occurrencesJson.getJsonObject(i);
                        Occurrence occurrence = getOccurrenceFromJson(occurrenceJson, isEditing);
                        occurrence.setInvolvesIndividualGroup(individualGroup);
                        occurrences.add(occurrence);
                    });
            individualGroup.setOccurrences(occurrences);
        } catch (CollectionsException e) {
            throw e;
        } 
    }
 
    /**
     * Deletes an entity in database
     *
     * @param entityName
     * @param id
     * @return 
     */
    public JsonObject delete(String entityName, long id) {
        log.info("delete");

        try {
            Class clazz = Util.getInstance().convertClassNameToClass(entityName); 
            EntityBean bean = dao.findByReference(id, clazz); 
            if (bean != null) {
                dao.delete(bean);
            }
        } catch(CollectionsBadRequestException | CollectionsConstraintViolationException | CollectionsDatabaseException e) { 
            throw e;
        }
        return json2.convertSuccessMessage("Delete success");
    }
    
    public JsonObject buildErrorJson(CollectionsException e) {
        return json2.convertError(e);
    }
 
}
