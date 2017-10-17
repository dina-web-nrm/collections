/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.data.model.impl;
 
import java.util.List; 
import javax.persistence.CascadeType; 
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
@Table(name = "individual_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndividualGroup.findAll", query = "SELECT i FROM IndividualGroup i"), 
    @NamedQuery(name = "IndividualGroup.findById", query = "SELECT i FROM IndividualGroup i WHERE i.id = :id")})
public class IndividualGroup extends BaseEntity {
 
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "applies_to_individual_group_id", fetch = FetchType.LAZY)
    @CollectionsOneToMany(name = "identifications", type = "Identification")
    private List<Identification> identifications;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "applies_to_individual_group_id", fetch = FetchType.LAZY)
    @CollectionsOneToMany(name = "feature_observations", type = "FeatureObservation")
    private List<FeatureObservation> feature_observations;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "involves_individual_group_id", fetch = FetchType.LAZY)
    @CollectionsOneToMany(name = "occurrences", type = "Occurrence")
    private List<Occurrence> occurrences;
    
    @OneToMany( cascade = CascadeType.ALL, 
                mappedBy = "represents_individual_group_id",  
                fetch = FetchType.LAZY)
    @CollectionsOneToMany(name = "physical_units", type = "PhysicalUnit")
    private List<PhysicalUnit> physical_units;

    public IndividualGroup() {
    }

    public IndividualGroup(Long id) {
        this.id = id;
    }

    public IndividualGroup(Long id, int version) {
        this.id = id;
        this.version = version;
    }
    
    @Override
    public long getEntityId() {
        return id;
    }

    public List<Identification> getIdentifications() {
        return identifications;
    }

    public void setIdentifications(List<Identification> identifications) {
        this.identifications = identifications;
    }

    public List<FeatureObservation> getFeature_observations() {
        return feature_observations;
    }

    public void setFeature_observations(List<FeatureObservation> feature_observations) {
        this.feature_observations = feature_observations;
    }

    public List<Occurrence> getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(List<Occurrence> occurrences) {
        this.occurrences = occurrences;
    }

    public List<PhysicalUnit> getPhysical_units() {
        return physical_units;
    }

    public void setPhysical_units(List<PhysicalUnit> physical_units) {
        this.physical_units = physical_units;
    }
    
    
 
//    @XmlTransient
//    public List<Identification> getIdentificationList() {
//        return identifications;
//    }
//
//    public void setIdentificationList(List<Identification> identifications) {
//        this.identifications = identifications;
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
//
//    @XmlTransient
//    public List<Occurrence> getOccurrenceList() {
//        return occurrences;
//    }
//
//    public void setOccurrenceList(List<Occurrence> occurrences) {
//        this.occurrences = occurrences;
//    }
//
//    @XmlTransient
//    public List<PhysicalUnit> getPhysicalUnitList() {
//        return physical_units;
//    }
//
//    public void setPhysicalUnitList(List<PhysicalUnit> physical_units) {
//        this.physical_units = physical_units;
//    }
// 
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IndividualGroup)) {
            return false;
        }
        IndividualGroup other = (IndividualGroup) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "se.nrm.dina.collections.data.model.IndividualGroup[ id=" + id + " ]";
    } 
}
