/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.json.converter.util;

import java.sql.Date;
import org.junit.After; 
import org.junit.AfterClass;
import org.junit.Before; 
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author idali
 */
public class UtilTest {
    
    private Util instance;
    
    public UtilTest() {
    }
 
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class Util.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
         
        instance = Util.getInstance();
        assertNotNull(instance); 
    }

    /**
     * Test of dateToString method, of class Util.
     */
    @Test
    public void testDateToString_jsDate() {
        System.out.println("dateToString");
        
        String strDate ="2017-10-10"; 
        Date date = Date.valueOf(strDate);
        
        instance = Util.getInstance();
  
        String expResult = "Oct 10, 2017";
        String result = instance.dateToString(date);
        assertNotNull(result); 
        assertEquals(expResult, result);
    }
    
    @Test
    public void testDateToString_jsDateWithNull() {
        System.out.println("dateToStringWithNull");
         
        Date date = null;
        
        instance = Util.getInstance();
   
        String result = instance.dateToString(date);
        assertNull(result);  
    }

    /**
     * Test of dateToString method, of class Util.
     */
    @Test
    public void testDateToString_juDate() {
        System.out.println("dateToString");
        
        String strDate ="2017-10-10"; 
        java.util.Date date = Date.valueOf(strDate); 
        instance = Util.getInstance();
        
        String expResult = "Oct 10, 2017";
        String result = instance.dateToString(date);
        assertEquals(expResult, result); 
    }
    
    /**
     * Test of dateToString method, of class Util.
     */
    @Test
    public void testDateToString_juDateWithNull() {
        System.out.println("dateToStringWithNull");
         
        java.util.Date date = null; 
        instance = Util.getInstance();
         
        String result = instance.dateToString(date);
        assertNull(result);  
    }

    /**
     * Test of reformClassName method, of class Util.
     */
    @Test
    public void testReformClassName() {
        System.out.println("reformClassName");
        String name = "PhysicalUnit";
        
        instance = Util.getInstance();
        String expResult = "physicalUnit";
        String result = instance.reformClassName(name);
        assertEquals(expResult, result); 
    }
    
    /**
     * Test of reformClassName method, of class Util.
     */
    @Test
    public void testReformClassNameWithEmptyString() {
        System.out.println("reformClassName");
        String name = "";
        
        instance = Util.getInstance();
        String expResult = "";
        String result = instance.reformClassName(name);
        assertEquals(expResult, result); 
    }
}
