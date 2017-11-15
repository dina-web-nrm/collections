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
@Table(name = "physical_unit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PhysicalUnit.findAll", query = "SELECT p FROM PhysicalUnit p"), 
    @NamedQuery(name = "PhysicalUnit.findById", query = "SELECT p FROM PhysicalUnit p WHERE p.id = :id")})
public class PhysicalUnit extends BaseEntity {
 
    @Lob
    @Column(name = "physical_unit_text")
    private String physicalUnitText;
    
    @Lob
    @Column(name = "normal_storage_location")
    private String normalStorageLocation;
    
    @JoinColumn(name = "belongs_to_cataloged_unit_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @CollectionsManyToOne(name = "belongsToCatalogedUnit", type="CatalogedUnit")
    private CatalogedUnit belongsToCatalogedUnit;
    
    @JoinColumn(name = "represents_individual_group_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @CollectionsManyToOne(name = "representsIndividualGroup", type="IndividualGroup")
    private IndividualGroup representsIndividualGroup;
    
    @JoinColumn(name = "is_collected_at_occurrence_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @CollectionsManyToOne(name = "isCollectedAtOccurrence", type="Occurrence")
    private Occurrence isCollectedAtOccurrence;

    public PhysicalUnit() {
    }

    public PhysicalUnit(Long id) {
        this.id = id;
    }

    public PhysicalUnit(Long id, int version) {
        this.id = id;
        this.version = version;
    }
 
    @Override
    public long getEntityId() {
        return id; 
    }

    public String getPhysicalUnitText() {
        return physicalUnitText;
    }

    public void setPhysicalUnitText(String physicalUnitText) {
        this.physicalUnitText = physicalUnitText;
    }

    public String getNormalStorageLocation() {
        return normalStorageLocation;
    }

    public void setNormalStorageLocation(String normalStorageLocation) {
        this.normalStorageLocation = normalStorageLocation;
    }

    public CatalogedUnit getBelongsToCatalogedUnit() {
        return belongsToCatalogedUnit;
    }

    public void setBelongsToCatalogedUnit(CatalogedUnit belongsToCatalogedUnit) {
        this.belongsToCatalogedUnit = belongsToCatalogedUnit;
    }

    public IndividualGroup getRepresentsIndividualGroup() {
        return representsIndividualGroup;
    }

    public void setRepresentsIndividualGroup(IndividualGroup representsIndividualGroup) {
        this.representsIndividualGroup = representsIndividualGroup;
    }

    public Occurrence getIsCollectedAtOccurrence() {
        return isCollectedAtOccurrence;
    }

    public void setIsCollectedAtOccurrence(Occurrence isCollectedAtOccurrence) {
        this.isCollectedAtOccurrence = isCollectedAtOccurrence;
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
        if (!(object instanceof PhysicalUnit)) {
            return false;
        }
        PhysicalUnit other = (PhysicalUnit) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "se.nrm.dina.collections.data.model.PhysicalUnit[ id=" + id + " ]";
    } 
}
