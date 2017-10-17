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
    private String physical_unit_text;
    
    @Lob
    @Column(name = "normal_storage_location")
    private String normal_storage_location;
    
    @JoinColumn(name = "belongs_to_cataloged_unit_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @CollectionsManyToOne(name = "belongs_to_cataloged_unit_id", type="CatalogedUnit")
    private CatalogedUnit belongs_to_cataloged_unit_id;
    
    @JoinColumn(name = "represents_individual_group_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @CollectionsManyToOne(name = "represents_individual_group_id", type="IndividualGroup")
    private IndividualGroup represents_individual_group_id;
    
    @JoinColumn(name = "is_collected_at_occurrence_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @CollectionsManyToOne(name = "is_collected_at_occurrence_id", type="Occurrence")
    private Occurrence is_collected_at_occurrence_id;

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

    public String getPhysical_unit_text() {
        return physical_unit_text;
    }

    public void setPhysical_unit_text(String physical_unit_text) {
        this.physical_unit_text = physical_unit_text;
    }

    public String getNormal_storage_location() {
        return normal_storage_location;
    }

    public void setNormal_storage_location(String normal_storage_location) {
        this.normal_storage_location = normal_storage_location;
    }

    public CatalogedUnit getBelongs_to_cataloged_unit_id() {
        return belongs_to_cataloged_unit_id;
    }

    public void setBelongs_to_cataloged_unit_id(CatalogedUnit belongs_to_cataloged_unit_id) {
        this.belongs_to_cataloged_unit_id = belongs_to_cataloged_unit_id;
    }

    public IndividualGroup getRepresents_individual_group_id() {
        return represents_individual_group_id;
    }

    public void setRepresents_individual_group_id(IndividualGroup represents_individual_group_id) {
        this.represents_individual_group_id = represents_individual_group_id;
    }

    public Occurrence getIs_collected_at_occurrence_id() {
        return is_collected_at_occurrence_id;
    }

    public void setIs_collected_at_occurrence_id(Occurrence is_collected_at_occurrence_id) {
        this.is_collected_at_occurrence_id = is_collected_at_occurrence_id;
    }
    
    
    
//    public String getPhysicalUnitText() {
//        return physical_unit_text;
//    }
//
//    public void setPhysicalUnitText(String physical_unit_text) {
//        this.physical_unit_text = physical_unit_text;
//    }
//
//    public String getNormalStorageLocation() {
//        return normal_storage_location;
//    }
//
//    public void setNormalStorageLocation(String normal_storage_location) {
//        this.normal_storage_location = normal_storage_location;
//    }
//
//    public CatalogedUnit getBelongsToCatalogedUnitId() {
//        return belongs_to_cataloged_unit_id;
//    }
//
//    public void setBelongsToCatalogedUnitId(CatalogedUnit belongs_to_cataloged_unit_id) {
//        this.belongs_to_cataloged_unit_id = belongs_to_cataloged_unit_id;
//    }
//
//    public IndividualGroup getRepresentsIndividualGroupId() {
//        return represents_individual_group_id;
//    }
//
//    public void setRepresentsIndividualGroupId(IndividualGroup represents_individual_group_id) {
//        this.represents_individual_group_id = represents_individual_group_id;
//    }
//
//    public Occurrence getIsCollectedAtOccurrenceId() {
//        return is_collected_at_occurrence_id;
//    }
//
//    public void setIsCollectedAtOccurrenceId(Occurrence is_collected_at_occurrence_id) {
//        this.is_collected_at_occurrence_id = is_collected_at_occurrence_id;
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
