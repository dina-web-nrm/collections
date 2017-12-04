/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.json.converter.impl;

import java.util.ArrayList;
import java.util.List;
import javax.json.JsonArray;
import javax.json.JsonObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import se.nrm.dina.collections.data.model.impl.CatalogedUnit;
import se.nrm.dina.collections.data.model.impl.FeatureObservation;
import se.nrm.dina.collections.data.model.impl.IndividualGroup;
import se.nrm.dina.collections.data.model.impl.PhysicalUnit;
import se.nrm.dina.collections.exceptions.CollectionsBadRequestException;
import se.nrm.dina.collections.exceptions.CollectionsException;
import se.nrm.dina.collections.exceptions.utils.ErrorCode;
import se.nrm.dina.collections.json.converter.JsonConverterV2;
import se.nrm.dina.collections.json.converter.util.CommonString;

/**
 *
 * @author idali
 */
@RunWith(MockitoJUnitRunner.class)
public class JsonConverterV2ImplTest {

    private final IndividualGroup testIndividualGroup;
    private final PhysicalUnit testPhysicalUnit;
    private final CatalogedUnit testCatalogedUnit;
    private final List<PhysicalUnit> physicalUnits;
    private final List<FeatureObservation> featureObservations;
    private final FeatureObservation testFeatureObservation;

    private final JsonConverterV2 testInstance;
    
    private final StringBuilder sb;

    public JsonConverterV2ImplTest() {
        testInstance = new JsonConverterV2Impl();

        testIndividualGroup = new IndividualGroup((long) 20);
        testPhysicalUnit = new PhysicalUnit((long) 18);
        testPhysicalUnit.setNormalStorageLocation("Stockholm");
        testPhysicalUnit.setPhysicalUnitText("test");

        physicalUnits = new ArrayList();
        physicalUnits.add(testPhysicalUnit);

        testCatalogedUnit = new CatalogedUnit((long) 20);
        testCatalogedUnit.setCatalogNumber("abc123");
        
        featureObservations = new ArrayList<>();
        testFeatureObservation = new FeatureObservation((long) 50);
        testFeatureObservation.setFeatureObservationText("test");
  
       
        sb = new StringBuilder();
        sb.append("{\n");
        sb.append("	\"data\": {\n");
        sb.append("		\"type\": \"individualGroup\",\n");
        sb.append("		\"id\": 50,\n");
        sb.append("		\"attributes\": {\n");
        sb.append("			\"physicalUnits\": [{\n");
        sb.append("				\"id\": 31\n");
        sb.append("			}],\n");
        sb.append("			\"featureObservations\": [{\n");
        sb.append("				\"id\": 28\n");
        sb.append("			}],\n");
        sb.append("			\"identifications\": [{\n");
        sb.append("				\"id\": 10\n");
        sb.append("			}],\n");
        sb.append("			\"occurrences\": [{\n");
        sb.append("				\"id\": 35\n");
        sb.append("			}]\n");
        sb.append("		}\n");
        sb.append("	}\n");
        sb.append("}");
        
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of convertIndividualGroup method, of class JsonConverterV2Impl.
     */
    @Test
    public void testConvertIndividualGroup() {
        System.out.println("convertIndividualGroup");

        String include = null;
        JsonObject result = testInstance.convertIndividualGroup(testIndividualGroup, include);

        JsonObject dataJson = result.getJsonObject("data");
        assertNotNull(dataJson);
        assertEquals(dataJson.getString("type"), "individualGroup");
        assertEquals(dataJson.getInt("id"), 20);

        JsonObject attrJson = dataJson.getJsonObject("attributes");
        assertNotNull(attrJson);
        assertEquals(attrJson.getString("physicalUnits"), "null");
        assertEquals(attrJson.getString("featureObservations"), "null");
        assertEquals(attrJson.getString("identifications"), "null");
        assertEquals(attrJson.getString("occurrences"), "null");
    }

    @Test
    public void testConvertIndividualGroupWithNull() {
        System.out.println("testConvertIndividualGroupWithNull");

        String include = null;
        JsonObject result = testInstance.convertIndividualGroup(null, include);

        System.out.println("result : " + result);
        JsonObject dataJson = result.getJsonObject("data");
        assertNotNull(dataJson);

        JsonObject attrJson = dataJson.getJsonObject("attributes");
        assertNull(attrJson);
    }

    /**
     * Test of convertIndividualGroup method, of class JsonConverterV2Impl.
     */
    @Test
    public void testConvertIndividualGroupWithPhysicalUnit() {
        System.out.println("testConvertIndividualGroupWithPhysicalUnit");

        String include = "physicalUnit";

        testIndividualGroup.setPhysicalUnits(physicalUnits);

        JsonObject result = testInstance.convertIndividualGroup(testIndividualGroup, include);

        JsonObject dataJson = result.getJsonObject("data");
        assertNotNull(dataJson);
        assertEquals(dataJson.getString("type"), "individualGroup");
        assertEquals(dataJson.getInt("id"), 20);

        JsonObject attrJson = dataJson.getJsonObject("attributes");
        assertNotNull(attrJson);

        JsonArray physicalUnitsJson = attrJson.getJsonArray("physicalUnits");
        assertNotNull(physicalUnitsJson);
        assertEquals(physicalUnitsJson.size(), 1);

        assertEquals(attrJson.getString("featureObservations"), "null");
        assertEquals(attrJson.getString("identifications"), "null");
        assertEquals(attrJson.getString("occurrences"), "null");
    }
    
    /**
     * Test of convertIndividualGroup method, of class JsonConverterV2Impl.
     */
    @Test
    public void testConvertIndividualGroupWithPhysicalUnitsIsEmpty() {
        System.out.println("testConvertIndividualGroupWithPhysicalUnit");

        String include = "physicalUnit";

        testIndividualGroup.setPhysicalUnits(new ArrayList());

        JsonObject result = testInstance.convertIndividualGroup(testIndividualGroup, include);

        JsonObject dataJson = result.getJsonObject("data");
        assertNotNull(dataJson);
        assertEquals(dataJson.getString("type"), "individualGroup");
        assertEquals(dataJson.getInt("id"), 20);

        JsonObject attrJson = dataJson.getJsonObject("attributes");
        assertNotNull(attrJson);
 
        assertEquals(attrJson.getString("physicalUnits"), "null");
        assertEquals(attrJson.getString("featureObservations"), "null");
        assertEquals(attrJson.getString("identifications"), "null");
        assertEquals(attrJson.getString("occurrences"), "null");
    }

    @Test
    public void testConvertIndividualGroupWithPhysicalUnitWithoutCatalogedUnit() {
        System.out.println("testConvertIndividualGroupWithPhysicalUnit");

        String include = "physicalUnits";

        physicalUnits.clear();
        testPhysicalUnit.setBelongsToCatalogedUnit(testCatalogedUnit);
        physicalUnits.add(testPhysicalUnit);

        testIndividualGroup.setPhysicalUnits(physicalUnits);

        JsonObject result = testInstance.convertIndividualGroup(testIndividualGroup, include);

        System.out.println("result : " + result);
        JsonObject dataJson = result.getJsonObject("data");
        assertNotNull(dataJson);
        assertEquals(dataJson.getString("type"), "individualGroup");
        assertEquals(dataJson.getInt("id"), 20);

        JsonObject attrJson = dataJson.getJsonObject("attributes");
        assertNotNull(attrJson);

        JsonArray physicalUnitsJson = attrJson.getJsonArray("physicalUnits");
        assertNotNull(physicalUnitsJson);
        assertEquals(physicalUnitsJson.size(), 1);
        
        JsonObject physicalUnitJson = physicalUnitsJson.getJsonObject(0);
        int catalogedUnitId = physicalUnitJson.getInt("catalogedUnitId");
         
        assertEquals(catalogedUnitId, 20);

        assertEquals(attrJson.getString("featureObservations"), "null");
        assertEquals(attrJson.getString("identifications"), "null");
        assertEquals(attrJson.getString("occurrences"), "null");
    }
    
    @Test
    public void testConvertIndividualGroupWithPhysicalUnitAndCatalogedUnit() {
        System.out.println("testConvertIndividualGroupWithPhysicalUnit");

        String include = "physicalUnits.catalogedUnit";

        physicalUnits.clear();
        testPhysicalUnit.setBelongsToCatalogedUnit(testCatalogedUnit);
        physicalUnits.add(testPhysicalUnit);

        testIndividualGroup.setPhysicalUnits(physicalUnits);

        JsonObject result = testInstance.convertIndividualGroup(testIndividualGroup, include);

        System.out.println("result : " + result);
        JsonObject dataJson = result.getJsonObject("data");
        assertNotNull(dataJson);
        assertEquals(dataJson.getString("type"), "individualGroup");
        assertEquals(dataJson.getInt("id"), 20);

        JsonObject attrJson = dataJson.getJsonObject("attributes");
        assertNotNull(attrJson);

        JsonArray physicalUnitsJson = attrJson.getJsonArray("physicalUnits");
        assertNotNull(physicalUnitsJson);
        assertEquals(physicalUnitsJson.size(), 1);

        assertEquals(attrJson.getString("featureObservations"), "null");
        assertEquals(attrJson.getString("identifications"), "null");
        assertEquals(attrJson.getString("occurrences"), "null");
    }
     
 

    @Test
    public void testConvertIndividualGroupWithPhysicalUnitAndCatalogedUnitIsNull() {
        System.out.println("testConvertIndividualGroupWithPhysicalUnit");

        String include = "physicalUnits.catalogedUnit";

        physicalUnits.clear();
        testPhysicalUnit.setBelongsToCatalogedUnit(null);
        physicalUnits.add(testPhysicalUnit);

        testIndividualGroup.setPhysicalUnits(physicalUnits);

        JsonObject result = testInstance.convertIndividualGroup(testIndividualGroup, include);

        JsonObject dataJson = result.getJsonObject("data");
        assertNotNull(dataJson);
        assertEquals(dataJson.getString("type"), "individualGroup");
        assertEquals(dataJson.getInt("id"), 20);

        JsonObject attrJson = dataJson.getJsonObject("attributes");
        assertNotNull(attrJson);

        JsonArray physicalUnitsJson = attrJson.getJsonArray("physicalUnits");
        assertNotNull(physicalUnitsJson);
        assertEquals(physicalUnitsJson.size(), 1);
 
        assertEquals(attrJson.getString("featureObservations"), "null");
        assertEquals(attrJson.getString("identifications"), "null");
        assertEquals(attrJson.getString("occurrences"), "null");
    }
 
    
    @Test
    public void testConvertIndividualGroupWithObservationsIsEmpty() {
        System.out.println("testConvertIndividualGroupWithPhysicalUnit");

        String include = "observations";
  
        testIndividualGroup.setFeatureObservations(featureObservations);

        JsonObject result = testInstance.convertIndividualGroup(testIndividualGroup, include);

        JsonObject dataJson = result.getJsonObject("data");
        assertNotNull(dataJson);
        assertEquals(dataJson.getString("type"), "individualGroup");
        assertEquals(dataJson.getInt("id"), 20);

        JsonObject attrJson = dataJson.getJsonObject("attributes");
        assertNotNull(attrJson);
 
        assertEquals(attrJson.getString("featureObservations"), "null");
    }
    
        
    @Test
    public void testConvertIndividualGroupWithObservationsIsNull() {
        System.out.println("testConvertIndividualGroupWithPhysicalUnit");

        String include = "observations";
  
        testIndividualGroup.setFeatureObservations(null);

        JsonObject result = testInstance.convertIndividualGroup(testIndividualGroup, include);

        JsonObject dataJson = result.getJsonObject("data");
        assertNotNull(dataJson);
        assertEquals(dataJson.getString("type"), "individualGroup");
        assertEquals(dataJson.getInt("id"), 20);

        JsonObject attrJson = dataJson.getJsonObject("attributes");
        assertNotNull(attrJson);
 
        assertEquals(attrJson.getString("featureObservations"), "null");
    }
    
    @Test
    public void testConvertIndividualGroupWithObservations() {
        System.out.println("testConvertIndividualGroupWithPhysicalUnit");

        String include = "observations";

        featureObservations.add(testFeatureObservation);

        testIndividualGroup.setFeatureObservations(featureObservations);

        JsonObject result = testInstance.convertIndividualGroup(testIndividualGroup, include);

        JsonObject dataJson = result.getJsonObject("data");
        assertNotNull(dataJson);
        assertEquals(dataJson.getString("type"), "individualGroup");
        assertEquals(dataJson.getInt("id"), 20);

        JsonObject attrJson = dataJson.getJsonObject("attributes");
        assertNotNull(attrJson);

        JsonArray featureObservationJson = attrJson.getJsonArray("featureObservations");
        assertNotNull(featureObservationJson);
        assertEquals(featureObservationJson.size(), 1);
  
    } 

    /**
     * Test of convertIndividualGroups method, of class JsonConverterV2Impl.
     */
    @Test
    public void testConvertIndividualGroups() {
        System.out.println("convertIndividualGroups");

        List<IndividualGroup> individualGroups = new ArrayList();
        individualGroups.add(testIndividualGroup);
        String include = "";

        JsonObject result = testInstance.convertIndividualGroups(individualGroups, include);

        JsonArray dataJson = result.getJsonArray("data");
        assertNotNull(dataJson);
        assertEquals(dataJson.size(), 1);
    }

    /**
     * Test of convertIndividualGroups method, of class JsonConverterV2Impl.
     */
    @Test
    public void testConvertIndividualGroupsWithEmptyData() {
        System.out.println("testConvertIndividualGroupsWithNull");

        List<IndividualGroup> individualGroups = new ArrayList();
        String include = "";

        JsonObject result = testInstance.convertIndividualGroups(individualGroups, include);

        JsonArray dataJson = result.getJsonArray("data");
        assertNotNull(dataJson);
        assertEquals(dataJson.size(), 0);

    }

    /**
     * Test of convertIndividualGroups method, of class JsonConverterV2Impl.
     */
    @Test
    public void testConvertIndividualGroupsWithNull() {
        System.out.println("testConvertIndividualGroupsWithNull");

        List<IndividualGroup> individualGroups = null;
        String include = "";

        JsonObject result = testInstance.convertIndividualGroups(individualGroups, include);

        JsonArray dataJson = result.getJsonArray("data");
        assertNotNull(dataJson);
        assertEquals(dataJson.size(), 0);

    }

    /**
     * Test of convertErrors method, of class JsonConverterV2Impl.
     */
    @Test
    public void testConvertError() {
        System.out.println("convertErrors");

        CollectionsException e = new CollectionsBadRequestException(CatalogedUnit.class.getSimpleName(), ErrorCode.BAD_REQUEST.name(), ErrorCode.BAD_REQUEST.getDetail("test"), ErrorCode.BAD_REQUEST.getMessage());

        JsonObject result = testInstance.convertError(e);
        System.out.println("result : " + result.toString());

        assertNotNull(result);

        JsonArray errorsJson = result.getJsonArray("errors");

        assertNotNull(errorsJson);
        assertEquals(errorsJson.size(), 1);
    }

    /**
     * Test of convertError method, of class JsonConverterV2Impl.
     */
    @Test
    public void testConvertErrors() {
        System.out.println("convertErrors");

        CollectionsException e = new CollectionsBadRequestException(CatalogedUnit.class.getSimpleName(), ErrorCode.BAD_REQUEST.name(), ErrorCode.BAD_REQUEST.getDetail("test"), ErrorCode.BAD_REQUEST.getMessage());
        List<CollectionsException> errors = new ArrayList<>();
        errors.add(e);

        JsonObject result = testInstance.convertErrors(errors);
        System.out.println("result : " + result.toString());

        assertNotNull(result);

        JsonArray errorsJson = result.getJsonArray("errors");

        assertNotNull(errorsJson);
        assertEquals(errorsJson.size(), 1);
    }
    
    /**
     * Test of convertError method, of class JsonConverterV2Impl.
     */
    @Test
    public void testConvertErrorsWithNull() {
        System.out.println("testConvertErrorsWithNull");
  
        JsonObject result = testInstance.convertErrors(null); 

        assertNotNull(result);

        JsonArray errorsJson = result.getJsonArray("errors");

        assertNotNull(errorsJson);
        assertEquals(errorsJson.size(), 0);
    }

    /**
     * Test of readInJson method, of class JsonConverterV2Impl.
     */
    @Test
    public void testReadInJson() {
        System.out.println("testReadInJson");
 
        JsonObject result = testInstance.readInJson(sb.toString()); 
        assertNotNull(result);

        JsonObject dataJson = result.getJsonObject(CommonString.getInstance().getData());

        assertNotNull(dataJson);
        assertEquals(dataJson.getString(CommonString.getInstance().getType()), "individualGroup");
        assertEquals(dataJson.getInt(CommonString.getInstance().getId()), 50);
    }
             
    /**
     * Test of getAttributes method, of class JsonConverterV2Impl.
     */
    @Test
    public void testGetAttributes() {
        System.out.println("testReadInJson");
         
        JsonObject result = testInstance.readInJson(sb.toString()); 
        assertNotNull(result);

        JsonObject dataJson = result.getJsonObject(CommonString.getInstance().getData());
         
        JsonObject attrJson = testInstance.getAttributes(dataJson);
        assertNotNull(attrJson);
        
        JsonArray jsonArray = attrJson.getJsonArray("physicalUnits");
        assertNotNull(jsonArray);
        
        assertEquals(jsonArray.size(), 1); 
    }
    
    
        /**
     * Test of getAttributes method, of class JsonConverterV2Impl.
     */
    @Test
    public void testGetJsonArray() {
        System.out.println("testReadInJson");
         
        JsonObject result = testInstance.readInJson(sb.toString()); 
        assertNotNull(result);

        JsonObject dataJson = result.getJsonObject(CommonString.getInstance().getData());

        assertNotNull(dataJson);
        
        JsonObject attrJson = dataJson.getJsonObject(CommonString.getInstance().getAttributes());
        assertNotNull(attrJson);
        
        JsonArray jsonArray = testInstance.getJsonArray(attrJson, "physicalUnits") ;
        assertNotNull(jsonArray); 
        assertEquals(jsonArray.size(), 1); 
        
        jsonArray = testInstance.getJsonArray(attrJson, "test") ;
        assertNull(jsonArray);
    }
}
