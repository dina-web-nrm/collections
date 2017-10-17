/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.data.model.impl;
  
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType; 
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import se.nrm.dina.collections.annotation.CollectionsManyToOne; 
import se.nrm.dina.collections.data.model.BaseEntity;

/**
 *
 * @author idali
 */
@Entity
@Table(name = "feature_observation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FeatureObservation.findAll", query = "SELECT f FROM FeatureObservation f"), 
    @NamedQuery(name = "FeatureObservation.findById", query = "SELECT f FROM FeatureObservation f WHERE f.id = :id")})
public class FeatureObservation extends BaseEntity {
 
    @Lob
    @Column(name = "feature_observation_text")
    private String feature_observation_text;
    
    @JoinColumn(name = "is_of_feature_observation_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @CollectionsManyToOne(name = "is_of_feature_observation_type_id", type="FeatureObservationType")
    private FeatureObservationType is_of_feature_observation_type_id;
    
    @JoinColumn(name = "applies_to_individual_group_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @CollectionsManyToOne(name = "applies_to_individual_group_id", type="IndividualGroup")
    private IndividualGroup applies_to_individual_group_id;

    public FeatureObservation() {
    }

    public FeatureObservation(Long id) {
        this.id = id;
    }

    public FeatureObservation(Long id, int version) {
        this.id = id;
        this.version = version;
    }
     
    @Override
    public long getEntityId() {
        return id;
    }

    public String getFeature_observation_text() {
        return feature_observation_text;
    }

    public void setFeature_observation_text(String feature_observation_text) {
        this.feature_observation_text = feature_observation_text;
    }

    public FeatureObservationType getIs_of_feature_observation_type_id() {
        return is_of_feature_observation_type_id;
    }

    public void setIs_of_feature_observation_type_id(FeatureObservationType is_of_feature_observation_type_id) {
        this.is_of_feature_observation_type_id = is_of_feature_observation_type_id;
    }

    public IndividualGroup getApplies_to_individual_group_id() {
        return applies_to_individual_group_id;
    }

    public void setApplies_to_individual_group_id(IndividualGroup applies_to_individual_group_id) {
        this.applies_to_individual_group_id = applies_to_individual_group_id;
    }
     
    
    
//    public String getFeatureObservationText() {
//        return feature_observation_text;
//    }
//
//    public void setFeatureObservationText(String feature_observation_text) {
//        this.feature_observation_text = feature_observation_text;
//    }
//
//    public FeatureObservationType getIsOfFeatureObservationTypeId() {
//        return is_of_feature_observation_type_id;
//    }
//
//    public void setIsOfFeatureObservationTypeId(FeatureObservationType is_of_feature_observation_type_id) {
//        this.is_of_feature_observation_type_id = is_of_feature_observation_type_id;
//    }
//
//    public IndividualGroup getAppliesToIndividualGroupId() {
//        return applies_to_individual_group_id;
//    }
//
//    public void setAppliesToIndividualGroupId(IndividualGroup applies_to_individual_group_id) {
//        this.applies_to_individual_group_id = applies_to_individual_group_id;
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
        if (!(object instanceof FeatureObservation)) {
            return false;
        }
        FeatureObservation other = (FeatureObservation) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "se.nrm.dina.collections.data.model.FeatureObservation[ id=" + id + " ]";
    } 
}
