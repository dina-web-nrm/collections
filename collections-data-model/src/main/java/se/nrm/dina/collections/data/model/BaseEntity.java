/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.data.model;
 
import java.io.Serializable; 
import javax.persistence.Basic;
import javax.persistence.Column; 
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version; 
import se.nrm.dina.collections.annotation.CollectionsId;


/**
 *
 * @author idali
 */
@MappedSuperclass 
public abstract class BaseEntity implements Serializable, EntityBean {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    @CollectionsId
    protected Long id;
      
    @Version
    @Column(name = "Version")
    protected Integer version = 1;
     
    public BaseEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
     
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    } 
    
    @Override
    public String toString() {
        return "BaseEntity";
    }
}
