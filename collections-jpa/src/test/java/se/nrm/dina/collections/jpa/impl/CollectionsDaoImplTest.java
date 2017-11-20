/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.jpa.impl;
 
import java.util.List; 
import javax.persistence.EntityManager; 
import javax.persistence.Query; 
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals; 
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass; 
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;   
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify; 
import org.mockito.runners.MockitoJUnitRunner;    
import se.nrm.dina.collections.data.model.EntityBean;
import se.nrm.dina.collections.data.model.impl.CatalogedUnit;
import se.nrm.dina.collections.jpa.CollectionsDao;

/**
 *
 * @author idali
 */
@RunWith(MockitoJUnitRunner.class)
public class CollectionsDaoImplTest {

    @Mock
    static EntityManager entityManager; 
    @Mock
    static Query query;
    
    private static CatalogedUnit testCatalogedUnit;
    private static String catalogNumber;

    private static CollectionsDao dao;

    public CollectionsDaoImplTest() {
    }

    @BeforeClass
    public static void setUpClass() { 
        testCatalogedUnit = new CatalogedUnit((long) 50);
        catalogNumber = "abc123";
        testCatalogedUnit.setCatalogNumber(catalogNumber);
    }
    
    @AfterClass
    public static void tearDownClass() {
        dao = null;
        testCatalogedUnit = null;
    } 
     
    @Test
    public void testCollectionsDaoImplConstractor() throws Exception {
        System.out.println("testCollectionsDaoImplConstractor");
        dao = new CollectionsDaoImpl();
        assertNotNull(dao);
    }
      
    @Test
    public void testCollectionsDaoImplConstractorWithEntityManager() throws Exception {
        System.out.println("testCollectionsDaoImplConstractorWithEntityManager");
        
        dao = new CollectionsDaoImpl(entityManager);
        assertNotNull(dao);
        assertNotNull(entityManager);
    }
      
    @Test
    public void testCollectionsDaoImplConstractorWithEntityManagerAndQuery() throws Exception {
        System.out.println("testCollectionsDaoImplConstractorWithEntityManager");
        
        dao = new CollectionsDaoImpl(entityManager, query);
        assertNotNull(dao);
        assertNotNull(entityManager);
        assertNotNull(query);
    }
    
    /**
     * Test of create method, of class CollectionsDaoImpl.
     * @throws java.lang.Exception
     */ 
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
          
        int expResult = 50;
         
        dao = new CollectionsDaoImpl(entityManager);
        CatalogedUnit result = (CatalogedUnit) dao.create(testCatalogedUnit);
        verify(entityManager).persist(testCatalogedUnit);
        verify(entityManager).flush();        
        verify(entityManager, times(1)).persist(testCatalogedUnit);
        verify(entityManager, times(1)).flush();
        assertEquals(expResult, (int) result.getEntityId());  
        assertEquals(catalogNumber, result.getCatalogNumber());
    }

    /**
     * Test of merge method, of class CollectionsDaoImpl.
     */
    @Ignore
    @Test
    public void testMerge() throws Exception {
        System.out.println("merge");
        Object entity = null;
        CollectionsDaoImpl instance = new CollectionsDaoImpl();
        Object expResult = null;
//        Object result = instance.merge(entity);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class CollectionsDaoImpl.
     */
    @Ignore
    @Test
    public void testDelete() throws Exception {
        System.out.println("delete");
        Object entity = null;
        CollectionsDaoImpl instance = new CollectionsDaoImpl();
//        instance.delete(entity);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of findById method, of class CollectionsDaoImpl.
     */
    @Ignore
    @Test
    public void testFindById() throws Exception {
        System.out.println("findById");
        CollectionsDaoImpl instance = new CollectionsDaoImpl();
        Object expResult = null;
//        Object result = instance.findById(null);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of findByReference method, of class CollectionsDaoImpl.
     */
    @Ignore
    @Test
    public void testFindByReference() throws Exception {
        System.out.println("findByReference");
        CollectionsDaoImpl instance = new CollectionsDaoImpl();
        Object expResult = null;
//        Object result = instance.findByReference(null);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of findAll method, of class CollectionsDaoImpl.
     */
    @Ignore
    @Test
    public void testFindAll() throws Exception {
        System.out.println("findAll");
        CollectionsDaoImpl instance = new CollectionsDaoImpl();
        List expResult = null;
        List result = instance.findAll(null);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of findByNamedQuery method, of class CollectionsDaoImpl.
     */
    @Ignore
    @Test
    public void testFindByNamedQuery_3args_1() throws Exception {
        System.out.println("findByNamedQuery");
        String namedQuery = "";
        String parameter = "";
        long id = 0L;
        CollectionsDaoImpl instance = new CollectionsDaoImpl();
        Object expResult = null;
        Object result = instance.findByNamedQuery(namedQuery, parameter, id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of findByNamedQuery method, of class CollectionsDaoImpl.
     */
    @Ignore
    @Test
    public void testFindByNamedQuery_3args_2() throws Exception {
        System.out.println("findByNamedQuery");
        String namedQuery = "";
        String parameter = "";
        String value = "";
        CollectionsDaoImpl instance = new CollectionsDaoImpl();
        List expResult = null;
        List result = instance.findByNamedQuery(namedQuery, parameter, value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of findByJPQL method, of class CollectionsDaoImpl.
     */
    @Ignore
    @Test
    public void testFindByJPQL() throws Exception {
        System.out.println("findByJPQL");
        String jpql = "";
        CollectionsDaoImpl instance = new CollectionsDaoImpl();
        List expResult = null;
        List result = instance.findByJPQL(jpql);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
}
