/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.jpa.impl;

import java.io.Serializable;
import java.util.List; 
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.collections.data.model.EntityBean;
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
    
    @Override
    public T create(T entity) {
        
        log.info("create : {}", entity);
        
        T tmp = entity;
        try {
            entityManager.persist(entity);
            entityManager.flush();  
        } catch(ConstraintViolationException e) {
            throw e;
        } catch (Exception e) {  
            throw e;
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
}
