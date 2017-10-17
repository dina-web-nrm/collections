/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.data.model.impl;
 
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
    private String feature_observation_type_name;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "is_of_feature_observation_type_id", fetch = FetchType.LAZY)
    @CollectionsOneToMany(name = "feature_observations", type = "FeatureObservation")
    private List<FeatureObservation> feature_observations;

    public FeatureObservationType() {
    }

    public FeatureObservationType(Long id) {
        this.id = id;
    }

    public FeatureObservationType(Long id, int version, String feature_observation_type_name) {
        this.id = id;
        this.version = version;
        this.feature_observation_type_name = feature_observation_type_name;
    }
     
    @Override
    public long getEntityId() {
        return id;
    }

    public String getFeature_observation_type_name() {
        return feature_observation_type_name;
    }

    public void setFeature_observation_type_name(String feature_observation_type_name) {
        this.feature_observation_type_name = feature_observation_type_name;
    }

    public List<FeatureObservation> getFeature_observations() {
        return feature_observations;
    }

    public void setFeature_observations(List<FeatureObservation> feature_observations) {
        this.feature_observations = feature_observations;
    }
    
    
    
//    public String getFeatureObservationTypeName() {
//        return feature_observation_type_name;
//    }
//
//    public void setFeatureObservationTypeName(String feature_observation_type_name) {
//        this.feature_observation_type_name = feature_observation_type_name;
//    }
//
//    @XmlTransient
//    public List<FeatureObservation> getFeatureObservationList() {
//        return feature_observations;
//    }
//
//    public void setFeatureObservationList(List<FeatureObservation> feature_observations) {
//        this.feature_observations = feature_observations;
//    }

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
