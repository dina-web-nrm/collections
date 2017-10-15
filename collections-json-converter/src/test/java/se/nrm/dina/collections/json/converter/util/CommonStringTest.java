/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.json.converter.util;

import org.junit.After; 
import org.junit.Before; 
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author idali
 */
public class CommonStringTest {
    
    private CommonString instance;
    
    public CommonStringTest() {
    }
 
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class CommonString.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
 
        instance = CommonString.getInstance();
        assertNotNull(instance); 
    }

    /**
     * Test of getResultCount method, of class CommonString.
     */
    @Test
    public void testGetResultCount() {
        System.out.println("getResultCount");
      
        instance = CommonString.getInstance();
                
        String expResult = CommonString.getInstance().getResultCount();
        String result = instance.getResultCount();
        assertEquals(expResult, result); 
    }

    /**
     * Test of getStatusCode method, of class CommonString.
     */
    @Test
    public void testGetStatusCode() {
        System.out.println("getStatusCode");
        
        instance = CommonString.getInstance();
        String expResult = CommonString.getInstance().getStatusCode();
        String result = instance.getStatusCode();
        assertEquals(expResult, result); 
    }

    /**
     * Test of getSource method, of class CommonString.
     */
    @Test
    public void testGetSource() {
        System.out.println("getSource");
        
        instance = CommonString.getInstance();
        String expResult = CommonString.getInstance().getSource();
        String result = instance.getSource();
        assertEquals(expResult, result); 
    }

    /**
     * Test of getErrors method, of class CommonString.
     */
    @Test
    public void testGetErrors() {
        System.out.println("getErrors");
        
        instance = CommonString.getInstance();
        String expResult = CommonString.getInstance().getErrors();
        String result = instance.getErrors();
        assertEquals(expResult, result); 
    }

    /**
     * Test of getPoint method, of class CommonString.
     */
    @Test
    public void testGetPoint() {
        System.out.println("getPoint");
        
        instance = CommonString.getInstance();
        String expResult = CommonString.getInstance().getPoint();
        String result = instance.getPoint();
        assertEquals(expResult, result); 
    }

    /**
     * Test of getDetail method, of class CommonString.
     */
    @Test
    public void testGetDetail() {
        System.out.println("getDetail");
        
        instance = CommonString.getInstance();
        String expResult = CommonString.getInstance().getDetail();
        String result = instance.getDetail();
        assertEquals(expResult, result); 
    }

    /**
     * Test of getMeta method, of class CommonString.
     */
    @Test
    public void testGetMeta() {
        System.out.println("getMeta");
        
        instance = CommonString.getInstance();
        String expResult = CommonString.getInstance().getMeta();
        String result = instance.getMeta();
        assertEquals(expResult, result); 
    }

    /**
     * Test of getData method, of class CommonString.
     */
    @Test
    public void testGetData() {
        System.out.println("getData");
        
        instance = CommonString.getInstance();
        String expResult = CommonString.getInstance().getData();
        String result = instance.getData();
        assertEquals(expResult, result); 
    }

    /**
     * Test of getId method, of class CommonString.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        
        instance = CommonString.getInstance();
        String expResult = CommonString.getInstance().getId();
        String result = instance.getId();
        assertEquals(expResult, result); 
    }

    /**
     * Test of getType method, of class CommonString.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        
        instance = CommonString.getInstance();
        String expResult = CommonString.getInstance().getType();
        String result = instance.getType();
        assertEquals(expResult, result); 
    }

    /**
     * Test of getAttributes method, of class CommonString.
     */
    @Test
    public void testGetAttributes() {
        System.out.println("getAttributes");
        
        instance = CommonString.getInstance();
        String expResult = CommonString.getInstance().getAttributes();
        String result = instance.getAttributes();
        assertEquals(expResult, result); 
    }

    /**
     * Test of getRelationships method, of class CommonString.
     */
    @Test
    public void testGetRelationships() {
        System.out.println("getRelationships");
        
        instance = CommonString.getInstance();
        String expResult = CommonString.getInstance().getRelationships();
        String result = instance.getRelationships();
        assertEquals(expResult, result); 
    }

    /**
     * Test of getCount method, of class CommonString.
     */
    @Test
    public void testGetCount() {
        System.out.println("getCount");
        
        instance = CommonString.getInstance();
        String expResult = CommonString.getInstance().getCount();
        String result = instance.getCount();
        assertEquals(expResult, result); 
    }
    
}
