/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.jpa;

import java.util.List;
import se.nrm.dina.collections.data.model.EntityBean;

/**
 *
 * @author idali
 * @param <T>{@link BaseEntity}
 * 
 */
public interface CollectionsDao<T extends EntityBean> {
    
    /**
     * Saves a transient or persistent {@link BaseEntity} to the database.
     *
     * @param entity the entity to save.
     * @return a persistent copy of the entity.
     */
    public T create(T entity);

    /**
     * Merges a transient {@link BaseEntity} to the database.
     *
     * @param entity the entity to merge.
     * @return a persistent copy of the entity.
     */
    public T merge(T entity);
    
    
    /**
     * Deletes a {@link BaseEntity} from the database. If the delete was
     * successful, the entity's ID will be null.
     *
     * @param entity the entity to delete.
     */
    public void delete(T entity);

    /**
     * Finds a {@link BaseEntity} by its database ID.
     *
     * @param id the database id of the entity we want.
     * @param clazz 
     *
     * @return the instance of the entity from the database with the given id.
     */
    public T findById(long id, Class<T> clazz);
    
    /**
     * Finds an instance whose state may be lazily fetched
     *
     * @param id the database id of the entity we want.
     * @param clazz
     *
     * @return the instance of the entity from the database with the given id.
     */
    public T findByReference(long id, Class<T> clazz);
    
    /**
     * Finds all the instances of an entity in the database.
     *
     * @param clazz
     * @return a <code>List</code> of all the entities in the database.
     */
    public List<T> findAll(Class<T> clazz);


    public List<T> findByNamedQuery(String namedQuery, String parameter, String value);
    
    public List<T> findByJPQL(String jpql);
}
