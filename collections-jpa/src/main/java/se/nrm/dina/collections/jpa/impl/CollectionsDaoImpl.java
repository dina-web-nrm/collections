/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.jpa.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List; 
import java.util.Set;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.collections.data.model.EntityBean;
import se.nrm.dina.collections.exceptions.CollectionsConstraintViolationException; 
import se.nrm.dina.collections.exceptions.CollectionsDatabaseException;
import se.nrm.dina.collections.exceptions.Util.ErrorCode;
import se.nrm.dina.collections.exceptions.Util.Util;
import se.nrm.dina.collections.jpa.CollectionsDao;

/**
 *
 * @author idali
 * @param <T>
 */
@Slf4j
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CollectionsDaoImpl<T extends EntityBean> implements CollectionsDao<T>, Serializable  {
    
    private Query query;
  
    @PersistenceContext(unitName = "jpaPU")                  //  persistence unit connect to production database  
    private EntityManager entityManager;
    
    public CollectionsDaoImpl() {
        
    }
    
    public CollectionsDaoImpl(EntityManager entyEntityManager) {
        this.entityManager = entyEntityManager;
    }
    
    public CollectionsDaoImpl(EntityManager entyEntityManager, Query query) {
        this.entityManager = entyEntityManager;
        this.query = query;
    }
    
    @Override
    public T create(T entity) {
        
        log.info("create : {}", entity);
        
        T tmp = entity;
        try {
            entityManager.persist(entity);
            entityManager.flush();  
        } catch (PersistenceException ex) {  
            System.out.println("test");
            throw new CollectionsDatabaseException( entity.getClass().getSimpleName(),
                                                    Util.getInstance().getRootCauseName(ex), 
                                                    ex.getMessage(),
                                                    ErrorCode.DATABASE_EXCEPTION_CODE);
    
        } catch(ConstraintViolationException e) { 
            throw new CollectionsConstraintViolationException(entity.getClass().getSimpleName(), 
                                                              handleConstraintViolations(e).toString(), 
                                                              e.getMessage(), 
                                                              ErrorCode.CONSTRAINT_VIOLATION_EXCEPTION_CODE);
        } catch (Exception e) {  
            throw new CollectionsDatabaseException( entity.getClass().getSimpleName(),
                                                    Util.getInstance().getRootCauseName(e), 
                                                    e.getMessage(),
                                                    ErrorCode.DATABASE_EXCEPTION_CODE);
        }
        return tmp;
    }
    
     
     
    @Override
    public T merge(T entity) {
        
        log.info("merge: {}", entity);

        T tmp = entity;
        try { 
            tmp = entityManager.merge(entity); 
            entityManager.flush();                              // this one used for throwing OptimisticLockException if method called with web service
        } catch (OptimisticLockException e) { 
            log.warn(e.getMessage());
        } catch (ConstraintViolationException e) { 
            log.warn(e.getMessage());
            throw e;
        } catch (Exception e) {  
            log.warn(e.getMessage());
        }
        return tmp;  }

    @Override
    public void delete(T entity) {
        log.info("delete - {}", entity);

        try {
            entityManager.remove(entity);
            entityManager.flush();                              // this is needed for throwing internal exception
        } catch (ConstraintViolationException e) {
            log.warn(e.getMessage());
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    @Override
    public T findById(long id, Class<T> clazz) {
        log.info("findById - class : {} - id : {}", clazz, id);
 
        T tmp = null;
        try { 
            tmp = entityManager.find(clazz, id, LockModeType.OPTIMISTIC);  
            entityManager.flush();
            return tmp; 
        } catch (OptimisticLockException ex) { 
            entityManager.refresh(tmp); 
            throw ex;
        } catch(Exception ex) {
            throw ex;
        }   
    }

    @Override
    public T findByReference(long id, Class<T> clazz) {
        return entityManager.getReference(clazz, id);  
    }

    @Override
    public List<T> findAll(Class<T> clazz) { 
        log.info("findAll");
        
        query = entityManager.createNamedQuery(clazz.getSimpleName() + ".findAll"); 
        return query.getResultList();   
    } 
    
    @Override
    public T findByNamedQuery(String namedQuery, String parameter, long id) {
        query = entityManager.createNamedQuery(namedQuery); 
        query.setParameter(parameter, id);
        return (T) query.getSingleResult();
    }
    
    @Override
    public List<T> findByNamedQuery(String namedQuery, String parameter, String value) {
        query = entityManager.createNamedQuery(namedQuery); 
        query.setParameter(parameter, value);
        return query.getResultList(); 
    }

    @Override
    public List<T> findByJPQL(String jpql) {
        log.info("findByJPQL");
        
        query = entityManager.createQuery(jpql);
        return query.getResultList(); 
    }
    
    
    private List<String> handleConstraintViolations(ConstraintViolationException e) {  

        StringBuilder sb = new StringBuilder();
        List<String> msgs = new ArrayList<>();
        Set<ConstraintViolation<?>> cvs = e.getConstraintViolations();

        if (cvs != null) {
            cvs.stream().forEach(cv -> {
                sb.append(cv.getMessage());
                sb.append(": ");
                sb.append(cv.getRootBeanClass().getSimpleName());
                sb.append(" [Constrian Key:");
                sb.append(cv.getPropertyPath().toString());
                sb.append(" Value: ");
                sb.append(cv.getInvalidValue() == null ? null : cv.getInvalidValue().toString());
                sb.append("]");
                msgs.add(sb.toString()); 
            });
        } 
        return msgs;
    }
}
