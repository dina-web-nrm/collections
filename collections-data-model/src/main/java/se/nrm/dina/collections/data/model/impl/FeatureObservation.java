/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.data.model.impl;
  
//import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private String featureObservationText;
    
    @JoinColumn(name = "is_of_feature_observation_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @CollectionsManyToOne(name = "isOfFeatureObservationType", type="FeatureObservationType")
//    @JsonBackReference
    private FeatureObservationType isOfFeatureObservationType;
    
    @JoinColumn(name = "applies_to_individual_group_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @CollectionsManyToOne(name = "appliesToIndividualGroup", type="IndividualGroup")
//    @JsonBackReference
    private IndividualGroup appliesToIndividualGroup;

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

    public String getFeatureObservationText() {
        return featureObservationText;
    }

    public void setFeatureObservationText(String featureObservationText) {
        this.featureObservationText = featureObservationText;
    }

    public FeatureObservationType getIsOfFeatureObservationType() {
        return isOfFeatureObservationType;
    }

    public void setIsOfFeatureObservationType(FeatureObservationType isOfFeatureObservationType) {
        this.isOfFeatureObservationType = isOfFeatureObservationType;
    }

    public IndividualGroup getAppliesToIndividualGroup() {
        return appliesToIndividualGroup;
    }

    public void setAppliesToIndividualGroup(IndividualGroup appliesToIndividualGroup) {
        this.appliesToIndividualGroup = appliesToIndividualGroup;
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
