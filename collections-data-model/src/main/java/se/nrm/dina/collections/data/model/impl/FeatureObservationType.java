/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.data.model.impl;
 
//import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;  
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table; 
import javax.xml.bind.annotation.XmlRootElement; 
import javax.xml.bind.annotation.XmlTransient;
import se.nrm.dina.collections.annotation.CollectionsOneToMany;
import se.nrm.dina.collections.data.model.BaseEntity;

/**
 *
 * @author idali
 */
@Entity
@Table(name = "feature_observation_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FeatureObservationType.findAll", query = "SELECT f FROM FeatureObservationType f"), 
    @NamedQuery(name = "FeatureObservationType.findById", query = "SELECT f FROM FeatureObservationType f WHERE f.id = :id")
//    @NamedQuery(name = "FeatureObservationType.findByFeatureObservationTypeName", query = "SELECT f FROM FeatureObservationType f WHERE f.featureObservationTypeName = :featureObservationTypeName")
})
public class FeatureObservationType extends BaseEntity {
 
    @Basic(optional = false)
    @Column(name = "feature_observation_type_name")
    private String featureObservationTypeName;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "isOfFeatureObservationType", fetch = FetchType.LAZY)
    @CollectionsOneToMany(name = "featureObservations", type = "FeatureObservation") 
    private List<FeatureObservation> featureObservations;

    public FeatureObservationType() {
    }

    public FeatureObservationType(Long id) {
        this.id = id;
    }

    public FeatureObservationType(Long id, int version, String featureObservationTypeName) {
        this.id = id;
        this.version = version;
        this.featureObservationTypeName = featureObservationTypeName;
    }
     
    @Override
    public long getEntityId() {
        return id;
    }

    public String getFeatureObservationTypeName() {
        return featureObservationTypeName;
    }

    public void setFeatureObservationTypeName(String featureObservationTypeName) {
        this.featureObservationTypeName = featureObservationTypeName;
    }

    @XmlTransient
    public List<FeatureObservation> getFeatureObservations() {
        return featureObservations;
    }

    public void setFeatureObservations(List<FeatureObservation> featureObservations) {
        this.featureObservations = featureObservations;
    }
 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FeatureObservationType)) {
            return false;
        }
        FeatureObservationType other = (FeatureObservationType) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "se.nrm.dina.collections.data.model.FeatureObservationType[ id=" + id + " ]";
    } 
}
