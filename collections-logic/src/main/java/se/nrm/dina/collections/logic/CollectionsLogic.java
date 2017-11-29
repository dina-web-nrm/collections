/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.logic;
 
import se.nrm.dina.collections.logic.utils.Util; 
import java.util.List;  
import java.io.Serializable;     
import java.io.StringReader;
import java.util.ArrayList;
import java.util.stream.IntStream; 
import javax.ejb.EJB; 
import javax.inject.Inject; 
import javax.json.Json;
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
   
    public JsonObject saveIndividualGroup(String theJson) {
        log.info("saveIndividualGroup");
         
        JsonObject individualGroupJson = Json.createReader(new StringReader(theJson)).readObject();
        JsonObject dataJson = individualGroupJson.getJsonObject(CommonString.getInstance().getData());
 
        IndividualGroup individualGroup = new IndividualGroup();
         
        JsonObject attrJson = dataJson.getJsonObject(CommonString.getInstance().getAttributes());
        
        JsonArray featureObservationsJson = attrJson.getJsonArray("featureObservations"); 
        List<FeatureObservation> featureObservations = new ArrayList<>();
        FeatureObservation featureObservation = new FeatureObservation();

        IntStream.range(0, featureObservationsJson.size())
                .forEach(i -> {
                    JsonObject featureObservationJson = featureObservationsJson.getJsonObject(i);
                    String featureObservationText = featureObservationJson.getString("featureObservationText");
                    featureObservation.setFeatureObservationText(featureObservationText);
 
                    FeatureObservationType type = null; 
                    if (featureObservationJson.containsKey("featureObservationTypeId")) {
                        int featureObservationTypeId = featureObservationJson.getInt("featureObservationTypeId");
                        if (featureObservationTypeId != 0) {
                            type = (FeatureObservationType) dao.findByReference(featureObservationTypeId, FeatureObservationType.class);
                        }
                    } else {
                        type = new FeatureObservationType();
                        JsonObject typeJson = featureObservationJson.getJsonObject("featureObservationType");
                        String typeName = typeJson.getString("name");
                        type.setFeatureObservationTypeName(typeName);
                        featureObservation.setIsOfFeatureObservationType(type);
                    }
                    featureObservation.setIsOfFeatureObservationType(type);
                    featureObservation.setAppliesToIndividualGroup(individualGroup);
                    featureObservations.add(featureObservation);
                });
 
        JsonArray occurrencesJson = attrJson.getJsonArray("occurrences");
        List<Occurrence> occurrences = new ArrayList<>(); 
        Occurrence occurrence = new Occurrence();
        
        IntStream.range(0, occurrencesJson.size())
                .forEach(i -> {
                    JsonObject occurrenceJson = occurrencesJson.getJsonObject(i);
                    String collectorText = occurrenceJson.getString("collectorsText"); 
                    String localityText = occurrenceJson.getString("localityText");
                    String occurrenceDateText = occurrenceJson.getString("occurrenceDateText");
                    
                    occurrence.setCollectorsText(collectorText);
                    occurrence.setInvolvesIndividualGroup(individualGroup);
                    occurrence.setLocalityText(localityText);
                    occurrence.setOccurrenceDateText(occurrenceDateText);  
                    occurrences.add(occurrence); 
                });
        
        
        JsonArray identificationsJson = attrJson.getJsonArray("identifications");
        List<Identification> identifications = new ArrayList<>(); 
        Identification identification = new Identification();
        
        IntStream.range(0, identificationsJson.size())
                .forEach(i -> {
                    JsonObject identificationJson = identificationsJson.getJsonObject(i);
                    String identificationText = identificationJson.getString("identificationText"); 
                    
                    identification.setIdentificationText(identificationText);
                    identification.setAppliesToIndividualGroup(individualGroup);
 
                    identifications.add(identification);
                });
        
        JsonArray physicalUnitsJson = attrJson.getJsonArray("physicalUnits");
        List<PhysicalUnit> physicalUnits = new ArrayList<>(); 
        PhysicalUnit physicalUnit = new PhysicalUnit();
        
        CatalogedUnit catalogedUnit = new CatalogedUnit();
        JsonArray additionalData = dataJson.getJsonArray("additionalData");  
        IntStream.range(0, additionalData.size())
                .forEach(i -> { 
                    JsonObject additionalJson = additionalData.getJsonObject(i);
                    String type = additionalJson.getString(CommonString.getInstance().getType()); 
                    
                    if(type.equals("catalogedUnit")) {
                        JsonObject catalogedUnitAttrs = additionalJson.getJsonObject(CommonString.getInstance().getAttributes());
                        catalogedUnit.setCatalogNumber(catalogedUnitAttrs.getString("catalogNumber"));
                        catalogedUnit.setPhysicalUnits(physicalUnits);
                    }
                });
         
        IntStream.range(0, physicalUnitsJson.size())
                .forEach(i -> {
                    JsonObject physicalUnitJson = physicalUnitsJson.getJsonObject(i);
                    String physicalUnitText = physicalUnitJson.getString("physicalUnitText"); 
                    String normalStorageLocation = physicalUnitJson.getString("normalStorageLocation");
                    
                    physicalUnit.setPhysicalUnitText(physicalUnitText);
                    physicalUnit.setNormalStorageLocation(normalStorageLocation);
                    physicalUnit.setRepresentsIndividualGroup(individualGroup);
                    physicalUnit.setBelongsToCatalogedUnit(catalogedUnit);
 
                    physicalUnits.add(physicalUnit);
                });
        
        
        
        individualGroup.setFeatureObservations(featureObservations);
        individualGroup.setOccurrences(occurrences);
        individualGroup.setIdentifications(identifications);
        individualGroup.setPhysicalUnits(physicalUnits);
        
        IndividualGroup result = (IndividualGroup) dao.create(individualGroup);
        
        return json2.convertIndividualGroup(result, null); 
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
