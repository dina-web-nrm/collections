/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.jpa.impl;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EntityBean;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import se.nrm.dina.collections.data.model.impl.CatalogedUnit;
import se.nrm.dina.collections.exceptions.CollectionsConstraintViolationException;
import se.nrm.dina.collections.exceptions.CollectionsDatabaseException;
import se.nrm.dina.collections.jpa.CollectionsDao;

/**
 *
 * @author idali
 */
@RunWith(MockitoJUnitRunner.class)
public class CollectionsDaoImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @Mock
    private Exception exception;

    @Mock
    private CollectionsConstraintViolationException collectionsConstraintViolationException;

    private static CatalogedUnit testCatalogedUnit;
    private static String catalogNumber;
    private static long testId;

    private static CollectionsDao dao;

    public CollectionsDaoImplTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        testId = 50;
        testCatalogedUnit = new CatalogedUnit(testId);
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
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");

        dao = new CollectionsDaoImpl(entityManager);
        CatalogedUnit result = (CatalogedUnit) dao.create(testCatalogedUnit);
        verify(entityManager).persist(testCatalogedUnit);
        verify(entityManager).flush();
        verify(entityManager, times(1)).persist(testCatalogedUnit);
        verify(entityManager, times(1)).flush();
        assertEquals(testId, (int) result.getEntityId());
        assertEquals(catalogNumber, result.getCatalogNumber());
    }

    @Test(expected = CollectionsConstraintViolationException.class)
    public void testCreateFailure() throws Exception {
        System.out.println("create");

        doThrow(PersistenceException.class).when(entityManager).persist(testCatalogedUnit);

        dao = new CollectionsDaoImpl(entityManager, query);
        dao.create(testCatalogedUnit);
        verify(entityManager).persist(testCatalogedUnit);
        verify(entityManager, times(1)).persist(testCatalogedUnit);
        verify(entityManager, times(0)).flush();
    }

    @Test(expected = CollectionsConstraintViolationException.class)
    public void testCreateFailure1() throws Exception {
        System.out.println("create");

        doThrow(ConstraintViolationException.class).when(entityManager).persist(testCatalogedUnit);

        dao = new CollectionsDaoImpl(entityManager);
        dao.create(testCatalogedUnit);
        verify(entityManager).persist(testCatalogedUnit);
        verify(entityManager, times(1)).persist(testCatalogedUnit);
        verify(entityManager, times(0)).flush();
    }

    @Ignore
    @Test(expected = CollectionsDatabaseException.class)
    public void testCreateFailure2() throws Exception {
        System.out.println("create");

        doThrow(exception).when(entityManager).persist(testCatalogedUnit);

        dao = new CollectionsDaoImpl(entityManager);
        dao.create(testCatalogedUnit);
        verify(entityManager).persist(testCatalogedUnit);
        verify(entityManager, times(1)).persist(testCatalogedUnit);
        verify(entityManager, times(0)).flush();
    }

    /**
     * Test of merge method, of class CollectionsDaoImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testMerge() throws Exception {
        System.out.println("merge");

        when(entityManager.merge(testCatalogedUnit)).thenReturn(testCatalogedUnit);

        dao = new CollectionsDaoImpl(entityManager);
        CatalogedUnit result = (CatalogedUnit) dao.merge(testCatalogedUnit);

        verify(entityManager).merge(testCatalogedUnit);
        verify(entityManager).flush();
        verify(entityManager, times(1)).merge(testCatalogedUnit);
        verify(entityManager, times(1)).flush();
        assertEquals(testId, (int) result.getEntityId());
        assertEquals(catalogNumber, result.getCatalogNumber());
    }

    /**
     * Test of delete method, of class CollectionsDaoImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDelete() throws Exception {
        System.out.println("delete");

        dao = new CollectionsDaoImpl(entityManager);
        dao.delete(testCatalogedUnit);

        verify(entityManager).remove(testCatalogedUnit);
        verify(entityManager).flush();
        verify(entityManager, times(1)).remove(testCatalogedUnit);
        verify(entityManager, times(1)).flush();
    }

    /**
     * Test of findById method, of class CollectionsDaoImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testFindById() throws Exception {
        System.out.println("findById");

        when(entityManager.find(CatalogedUnit.class, (long) 50, LockModeType.OPTIMISTIC)).thenReturn(testCatalogedUnit);

        dao = new CollectionsDaoImpl(entityManager);
        CatalogedUnit result = (CatalogedUnit) dao.findById((long) 50, CatalogedUnit.class);

        verify(entityManager).find(CatalogedUnit.class, (long) 50, LockModeType.OPTIMISTIC);
        verify(entityManager).flush();
        assertSame(testCatalogedUnit, result);
    }

    /**
     * Test of findByReference method, of class CollectionsDaoImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testFindByReference() throws Exception {
        System.out.println("findByReference");

        when(entityManager.getReference(CatalogedUnit.class, (long) 50)).thenReturn(testCatalogedUnit);
        dao = new CollectionsDaoImpl(entityManager);
        CatalogedUnit result = (CatalogedUnit) dao.findByReference((long) 50, CatalogedUnit.class);
        verify(entityManager).getReference(CatalogedUnit.class, (long) 50);
        assertEquals(testCatalogedUnit, result);
    }

    /**
     * Test of findAll method, of class CollectionsDaoImpl.
     * @throws java.lang.Exception
     */ 
    @Test
    public void testFindAll() throws Exception {
        System.out.println("findAll");
        
        Class clazz = CatalogedUnit.class; 
        List<CatalogedUnit> catalogedUnits = new ArrayList<>(); 
        catalogedUnits.add(testCatalogedUnit);
        
        when(entityManager.createNamedQuery(clazz.getSimpleName() + ".findAll")).thenReturn(query); 
        when(query.getResultList()).thenReturn(catalogedUnits);
        int expResult = 1;
        
        dao = new CollectionsDaoImpl(entityManager, query);
        List result = dao.findAll(clazz);
        
        verify(entityManager).createNamedQuery(clazz.getSimpleName() + ".findAll");
        verify(query).getResultList();
        assertEquals(expResult, result.size());  
        assertSame(result, catalogedUnits);
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
