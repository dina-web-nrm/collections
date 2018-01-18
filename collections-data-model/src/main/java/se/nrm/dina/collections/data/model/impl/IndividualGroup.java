/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.data.model.impl;
  
import java.util.List;      
import javax.persistence.CascadeType;  
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;      
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;     
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement; 
import javax.xml.bind.annotation.XmlTransient;  
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
 
    @Size(max = 255)
    @Column(name = "cause_of_death_standardized")
    private String causeOfDeathStandardized;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "cause_of_death_text")
    private String causeOfDeathText;
    
    @Size(max = 100)
    @Column(name = "origin_standardized")
    private String originStandardized;
 
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appliesToIndividualGroup", fetch = FetchType.LAZY) 
    private List<Identification> identifications;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appliesToIndividualGroup", fetch = FetchType.LAZY) 
    private List<FeatureObservation> featureObservations;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "involvesIndividualGroup", fetch = FetchType.LAZY)  
    private List<Occurrence> occurrences;
    
    @OneToMany( cascade = CascadeType.ALL, 
                mappedBy = "representsIndividualGroup",  
                fetch = FetchType.LAZY)  
    private List<PhysicalUnit> physicalUnits;

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
    public List<Identification> getIdentifications() {
        return identifications;
    }

    public void setIdentifications(List<Identification> identifications) {
        this.identifications = identifications;
    }
  
    @XmlTransient
    public List<Occurrence> getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(List<Occurrence> occurrences) {
        this.occurrences = occurrences;
    }

    @XmlTransient
    public List<FeatureObservation> getFeatureObservations() {
        return featureObservations;
    }

    public void setFeatureObservations(List<FeatureObservation> featureObservations) {
        this.featureObservations = featureObservations;
    }

    @XmlTransient
    public List<PhysicalUnit> getPhysicalUnits() {
        return physicalUnits;
    }

    public void setPhysicalUnits(List<PhysicalUnit> physicalUnits) {
        this.physicalUnits = physicalUnits;
    }
 
    public String getCauseOfDeathStandardized() {
        return causeOfDeathStandardized;
    }

    public void setCauseOfDeathStandardized(String causeOfDeathStandardized) {
        this.causeOfDeathStandardized = causeOfDeathStandardized;
    }

    public String getCauseOfDeathText() {
        return causeOfDeathText;
    }

    public void setCauseOfDeathText(String causeOfDeathText) {
        this.causeOfDeathText = causeOfDeathText;
    }

    public String getOriginStandardized() {
        return originStandardized;
    }

    public void setOriginStandardized(String originStandardized) {
        this.originStandardized = originStandardized;
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
