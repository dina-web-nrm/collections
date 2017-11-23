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
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import se.nrm.dina.collections.data.model.impl.IndividualGroup;
import se.nrm.dina.collections.data.model.impl.PhysicalUnit;
import se.nrm.dina.collections.json.converter.JsonConverterV2;

/**
 *
 * @author idali
 */
@RunWith(MockitoJUnitRunner.class)
public class JsonConverterV2ImplTest {

    private IndividualGroup testIndividualGroup;
    private PhysicalUnit testPhysicalUnit;
    private List<PhysicalUnit> physicalUnits;
    
    private final JsonConverterV2 testInstance;

    public JsonConverterV2ImplTest() {
        testInstance = new JsonConverterV2Impl();

        testIndividualGroup = new IndividualGroup((long) 20);
        testPhysicalUnit = new PhysicalUnit((long) 18);
        testPhysicalUnit.setNormalStorageLocation("Stockholm");
        testPhysicalUnit.setPhysicalUnitText("test");
        
        physicalUnits = new ArrayList();
        physicalUnits.add(testPhysicalUnit);
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
    
    /**
     * Test of convertIndividualGroup method, of class JsonConverterV2Impl.
     */
    @Test
    public void testConvertIndividualGroupWithPhysicalUnit() {
        System.out.println("testConvertIndividualGroupWithPhysicalUnit");

        String include = "physicalUnit";
        
        
        testIndividualGroup.setPhysicalUnits(physicalUnits);
        
        JsonObject result = testInstance.convertIndividualGroup(testIndividualGroup, include);
 
        System.out.println("result : " +result);
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
     * Test of convertErrors method, of class JsonConverterV2Impl.
     */
    @Ignore
    @Test
    public void testConvertErrors() {
        System.out.println("convertErrors");
        JsonConverterV2Impl instance = new JsonConverterV2Impl();
        JsonObject expResult = null;
        JsonObject result = instance.convertErrors();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
