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
 
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appliesToIndividualGroupId", fetch = FetchType.LAZY)
    @CollectionsOneToMany
    private List<Identification> identificationList;
    
    @OneToMany(mappedBy = "appliesToIndividualGroupId", fetch = FetchType.LAZY)
    @CollectionsOneToMany
    private List<FeatureObservation> featureObservationList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "involvesIndividualGroupId", fetch = FetchType.LAZY)
    @CollectionsOneToMany
    private List<Occurrence> occurrenceList;
    
    @OneToMany(mappedBy = "representsIndividualGroupId", fetch = FetchType.LAZY)
    @CollectionsOneToMany
    private List<PhysicalUnit> physicalUnitList;

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
 
    @XmlTransient
    public List<Identification> getIdentificationList() {
        return identificationList;
    }

    public void setIdentificationList(List<Identification> identificationList) {
        this.identificationList = identificationList;
    }

    @XmlTransient
    public List<FeatureObservation> getFeatureObservationList() {
        return featureObservationList;
    }

    public void setFeatureObservationList(List<FeatureObservation> featureObservationList) {
        this.featureObservationList = featureObservationList;
    }

    @XmlTransient
    public List<Occurrence> getOccurrenceList() {
        return occurrenceList;
    }

    public void setOccurrenceList(List<Occurrence> occurrenceList) {
        this.occurrenceList = occurrenceList;
    }

    @XmlTransient
    public List<PhysicalUnit> getPhysicalUnitList() {
        return physicalUnitList;
    }

    public void setPhysicalUnitList(List<PhysicalUnit> physicalUnitList) {
        this.physicalUnitList = physicalUnitList;
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
