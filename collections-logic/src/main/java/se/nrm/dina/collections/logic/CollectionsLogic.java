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
//        mapper = new ObjectMapper();
    }
    
    public JsonObject getIndividualGroupById(long id, String include) {
        log.info("getIndividualGroupById");
        
        return json2.convertIndividualGroup((IndividualGroup) dao.findById(id, IndividualGroup.class), include);
    }
    
    public JsonObject getIndividualGroup(String catalogNumber, String include) {
        log.info("getIndividualGroup : {}", catalogNumber);
 
        return json2.convertIndividualGroups(dao.findByJPQL(QueryBuilder.getInstance()
                                                    .getQueryFindIndividualGroupsByCatalogNumber(catalogNumber)), include);
    }
    
    public void validateCatalogNumber(String catalogNumber, String source) {
        if(catalogNumber == null || catalogNumber.isEmpty()) { 
            throw new CollectionsBadRequestException(source, 
                                                     ErrorCode.BAD_REQUEST_MISSING_PARAMETER.getDetail("catalogNumer = " + catalogNumber), 
                                                     ErrorCode.BAD_REQUEST_MISSING_PARAMETER.name(), 
                                                     ErrorCode.BAD_REQUEST_MISSING_PARAMETER.getMessage()); 
        } 
    }
    
    public JsonObject updateIndvidualGroup(String theJson, long id) {
        
        JsonObject dataJson = json2.readInJson(theJson).getJsonObject(CommonString.getInstance().getData());
        JsonObject attributesJson = dataJson.getJsonObject(CommonString.getInstance().getAttributes());
        
        IndividualGroup indivialGroup = (IndividualGroup) dao.findByReference(id, IndividualGroup.class);
        
        List<FeatureObservation> featureObservations = indivialGroup.getFeatureObservations();
        
        
        return null;
    }
    


   
    public JsonObject saveIndividualGroup(String theJson) {
        log.info("saveIndividualGroup");
          
        JsonObject dataJson = json2.readInJson(theJson).getJsonObject(CommonString.getInstance().getData());
        JsonObject attrJson = json2.getAttributes(dataJson);
        JsonArray additionalData = json2.getJsonArray(dataJson, "additionalData");
         
        IndividualGroup individualGroup = new IndividualGroup();  
        
        try {
            addFeatureObservationsFromJson(attrJson, individualGroup, false);
            addOccurrences(attrJson, individualGroup, false);
            addIdentifications(attrJson, individualGroup, false);
            addPhysicalUnit(additionalData, attrJson, individualGroup, false);
        } catch(CollectionsBadRequestException e) {
            return json2.convertError(e);
        }
         
//        individualGroup.setFeatureObservations(getFeatureObservationFromJson(featureObservationsJson, individualGroup));
//        individualGroup.setOccurrences(occurrences);
//        individualGroup.setIdentifications(identifications);
//        individualGroup.setPhysicalUnits(physicalUnits);
        try { 
            return json2.convertIndividualGroup((IndividualGroup) dao.create(individualGroup), null);
        } catch(CollectionsException e) {
            return json2.convertError(e);
        }  
    }

    private CatalogedUnit getCatalogedUnitFromJson(JsonObject catalogedUnitJson) { 
        CatalogedUnit catalogedUnit;
        if(catalogedUnitJson.containsKey(CommonString.getInstance().getId())) {
            int id = catalogedUnitJson.getInt(CommonString.getInstance().getId());
            catalogedUnit = (CatalogedUnit) dao.findByReference(id, CatalogedUnit.class);
        } else {
            catalogedUnit = new CatalogedUnit();
        } 
        catalogedUnit.setCatalogNumber(catalogedUnitJson.getString("catalogNumber"));
        return catalogedUnit;
    }
    
    
    
    private PhysicalUnit getPhysicalUnitFromJson(CatalogedUnit catalogedUnit, JsonObject physicalUnitJson, boolean isEditing) {
         
        PhysicalUnit physicalUnit = new PhysicalUnit();
        if(isEditing) {
            if(physicalUnitJson.containsKey(CommonString.getInstance().getId())) {
                int id = physicalUnitJson.getInt(CommonString.getInstance().getId());
                physicalUnit = (PhysicalUnit) dao.findByReference(id, PhysicalUnit.class);
                catalogedUnit = getCatalogedUnitFromJson(physicalUnitJson.getJsonObject("catalogedUnit"));
            }  
        }  
       
        if(physicalUnitJson.containsKey("physicalUnitText")) {
            physicalUnit.setPhysicalUnitText(physicalUnitJson.getString("physicalUnitText"));
        }
        
        if(physicalUnitJson.containsKey("normalStorageLocation")) {
            physicalUnit.setNormalStorageLocation(physicalUnitJson.getString("normalStorageLocation"));
        }
        physicalUnit.setBelongsToCatalogedUnit(catalogedUnit);
//        physicalUnit.setRepresentsIndividualGroup(individualGroup); 
        return physicalUnit;
    }
 
    private void addPhysicalUnit(JsonArray additionalData, JsonObject attrJson, IndividualGroup individualGroup, boolean isEditing) {

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
//                    String physicalUnitText = physicalUnitJson.getString("physicalUnitText"); 
//                    String normalStorageLocation = physicalUnitJson.getString("normalStorageLocation");
//                    
//                    physicalUnit.setPhysicalUnitText(physicalUnitText);
//                    physicalUnit.setNormalStorageLocation(normalStorageLocation);
//                    physicalUnit.setRepresentsIndividualGroup(individualGroup);
//                    physicalUnit.setBelongsToCatalogedUnit(catalogedUnit);
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
                identification = (Identification) dao.findByReference(id, Identification.class);
            }
        }
        String identificationText = jsonObject.getString("identificationText"); 
        identification.setIdentificationText(identificationText);
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
        String name = null;
        if (featureObservationJson.containsKey("featureObservationType")) {
            JsonObject typeJson = featureObservationJson.getJsonObject("featureObservationType");
            if(isEditing) {
                featureObservationTypeId = typeJson.getInt(CommonString.getInstance().getId()); 
            } 
            name = typeJson.getString("name");
        } else if (featureObservationJson.containsKey("featureObservationTypeId")) {
            featureObservationTypeId = featureObservationJson.getInt("featureObservationTypeId");
        } else {
            throw new CollectionsBadRequestException(FeatureObservationType.class.getSimpleName(), 
                                                     ErrorCode.BAD_REQUEST_MISSING_PARAMETER.getDetail(FeatureObservationType.class.getSimpleName()), 
                                                     ErrorCode.BAD_REQUEST_MISSING_PARAMETER.name(),
                                                     ErrorCode.BAD_REQUEST_MISSING_PARAMETER.getMessage());
        }
         
        if (featureObservationTypeId > 0) {
            type = (FeatureObservationType) dao.findByReference(featureObservationTypeId, FeatureObservationType.class); 
        } else {
            type = new FeatureObservationType(); 
            type.setFeatureObservationTypeName(name); 
        }  
        return type;
    }
    
    private FeatureObservation getFeatureObservationFromJson(JsonObject featureObservationJson, boolean isEditing) {

        FeatureObservation featureObservation; 
        if(isEditing) {
            int id = featureObservationJson.getInt(CommonString.getInstance().getId());
            featureObservation = (FeatureObservation) dao.findByReference(id, FeatureObservation.class);
        } else {
            featureObservation = new FeatureObservation();   
        }
        
        try {
            featureObservation.setIsOfFeatureObservationType(getFeatureObservationTypeFromJson(featureObservationJson, isEditing));
        } catch (CollectionsBadRequestException e) {
            throw e;
        }
        
        String featureObservationText = featureObservationJson.getString("featureObservationText");
        featureObservation.setFeatureObservationText(featureObservationText);
        
//        featureObservation.setAppliesToIndividualGroup(individualGroup);
        return featureObservation;
    }

    private void addFeatureObservationsFromJson(JsonObject attrJson, IndividualGroup individualGroup, boolean isEditing) {
        
        JsonArray featureObservationsJson = json2.getJsonArray(attrJson, "featureObservations");  
        
        List<FeatureObservation> featureObservations = new ArrayList<>();
        
        IntStream.range(0, featureObservationsJson.size())
                .forEach(i -> {
                    JsonObject featureObservationJson = featureObservationsJson.getJsonObject(i);
//                    String featureObservationText = featureObservationJson.getString("featureObservationText");
//                    featureObservation.setFeatureObservationText(featureObservationText);
// 
//                    FeatureObservationType type = null; 
//                    if (featureObservationJson.containsKey("featureObservationTypeId")) {
//                        int featureObservationTypeId = featureObservationJson.getInt("featureObservationTypeId");
//                        if (featureObservationTypeId != 0) {
//                            type = (FeatureObservationType) dao.findByReference(featureObservationTypeId, FeatureObservationType.class);
//                        }
//                    } else {
//                        type = new FeatureObservationType();
//                        JsonObject typeJson = featureObservationJson.getJsonObject("featureObservationType");
//                        String typeName = typeJson.getString("name");
//                        type.setFeatureObservationTypeName(typeName);
//                        featureObservation.setIsOfFeatureObservationType(type);
//                    }
//                    featureObservation.setIsOfFeatureObservationType(type);
//                    featureObservation.setAppliesToIndividualGroup(individualGroup);

                    
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
                occurrence = (Occurrence) dao.findByReference(id, Occurrence.class);
            }
        }
        String collectorText = jsonObject.getString("collectorsText");
        String localityText = jsonObject.getString("localityText");
        String occurrenceDateText = jsonObject.getString("occurrenceDateText");

        occurrence.setCollectorsText(collectorText);
        
        occurrence.setLocalityText(localityText);
        occurrence.setOccurrenceDateText(occurrenceDateText);

        return occurrence;
    }

    private void addOccurrences(JsonObject attrJson, IndividualGroup individualGroup, boolean isEditing) {

        JsonArray occurrencesJson = json2.getJsonArray(attrJson, "occurrences");
        List<Occurrence> occurrences = new ArrayList<>();
  
        IntStream.range(0, occurrencesJson.size())
                .forEach(i -> {
                    JsonObject occurrenceJson = occurrencesJson.getJsonObject(i);
                    Occurrence occurrence = getOccurrenceFromJson(occurrenceJson, isEditing);
                    occurrence.setInvolvesIndividualGroup(individualGroup);
                    occurrences.add(occurrence); 
                });
        individualGroup.setOccurrences(occurrences);
    }
    
    
    
    
    //    public JsonObject getAll(String entityName) {
//        log.info("getAll : {}", entityName);
//        
//        Class clazz = Util.getInstance().convertClassNameToClass(entityName); 
//        return json.convert(dao.findAll(clazz));
//    }
//    
//    public JsonObject getById(String entityName, long id) {
//        Class clazz = Util.getInstance().convertClassNameToClass(entityName);
//        return json.convert(dao.findById(id, clazz));
//    }
    
    
//    public JsonObject saveEntity(String entityName, String theJson ) {
//        log.info("saveEntity : {} -- {}", entityName, theJson);
//        
//        IndividualGroup ig;
//        try { 
//            Class clazz = Util.getInstance().convertClassNameToClass(entityName);
//            EntityBean bean = (EntityBean) mapper.readValue(theJson, clazz);
//            if(bean instanceof PhysicalUnit) {
//                PhysicalUnit pu = (PhysicalUnit) bean;
//                ig = pu.getRepresentsIndividualGroup();
//                Occurrence occurrence = pu.getIsCollectedAtOccurrence();
//                if(occurrence != null) {
//                    occurrence.setInvolvesIndividualGroup(ig); 
//                }
//            } else if(bean instanceof IndividualGroup) {
//                ig = (IndividualGroup) bean; 
//                
//                List<Occurrence> occurrences = ig.getOccurrences();
//                if(occurrences != null) {
//                    occurrences.stream().forEach(o -> {
//                        o.setInvolvesIndividualGroup(ig); 
//                    });
//                }
//                
//                List<PhysicalUnit> physicalUnits = ig.getPhysicalUnits(); 
//                if(physicalUnits != null) {
//                    physicalUnits.stream().forEach(p -> {
//                        p.setRepresentsIndividualGroup(ig);
//                        if(occurrences != null && !occurrences.isEmpty()) {
//                            Occurrence o = occurrences.get(0);
//                            p.setIsCollectedAtOccurrence(o);
//                        }
//                    });
//                } 
// 
//                List<FeatureObservation> foList = ig.getFeatureObservations();
//                if(foList != null) {
//                    foList.stream().forEach(f -> {
//                        f.setAppliesToIndividualGroup(ig);
//                    });
//                }
//            }
//            return json.convert(dao.create(bean));
//        } catch (IOException ex) {
//            log.error(ex.getMessage());
//            return null;
//        } 
//    }
//
//    public JsonObject savePhysicalUnit(String theJson) {
//        log.info("savePhysicalUnit : {}", theJson);
// 
//        try {
//            EntityBean bean = mapper.readValue(theJson, PhysicalUnit.class); 
//            return json.convert(dao.create(bean));
//        } catch (IOException ex) {
//            log.error(ex.getMessage());
//            return null;
//        } 
//    }

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
