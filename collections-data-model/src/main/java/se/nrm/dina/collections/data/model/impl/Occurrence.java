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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import se.nrm.dina.collections.annotation.CollectionsManyToOne;
import se.nrm.dina.collections.annotation.CollectionsOneToMany;
import se.nrm.dina.collections.data.model.BaseEntity;

/**
 *
 * @author idali
 */
@Entity
@Table(name = "occurrence")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Occurrence.findAll", query = "SELECT o FROM Occurrence o"), 
    @NamedQuery(name = "Occurrence.findById", query = "SELECT o FROM Occurrence o WHERE o.id = :id")})
public class Occurrence extends BaseEntity {
 
    @Lob
    @Column(name = "collectors_text")
    private String collectors_text;
    
    @Lob
    @Column(name = "locality_text")
    private String locality_text;
    
    @Lob
    @Column(name = "occurrence_date_text")
    private String occurrence_date_text;
    
    @JoinColumn(name = "involves_individual_group_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @CollectionsManyToOne(name = "involves_individual_group_id", type = "IndividualGroup")
    private IndividualGroup involves_individual_group_id;
    
    @OneToMany(mappedBy = "is_collected_at_occurrence_id", fetch = FetchType.LAZY)
    @CollectionsOneToMany(name = "physical_units", type = "PhysicalUnit")
    private List<PhysicalUnit> physical_units;

    public Occurrence() {
    }

    public Occurrence(Long id) {
        this.id = id;
    }

    public Occurrence(Long id, int version) {
        this.id = id;
        this.version = version;
    }
     
    @Override
    public long getEntityId() {
        return id;
    }

    public String getCollectors_text() {
        return collectors_text;
    }

    public void setCollectors_text(String collectors_text) {
        this.collectors_text = collectors_text;
    }

    public String getLocality_text() {
        return locality_text;
    }

    public void setLocality_text(String locality_text) {
        this.locality_text = locality_text;
    }

    public String getOccurrence_date_text() {
        return occurrence_date_text;
    }

    public void setOccurrence_date_text(String occurrence_date_text) {
        this.occurrence_date_text = occurrence_date_text;
    }

    public IndividualGroup getInvolves_individual_group_id() {
        return involves_individual_group_id;
    }

    public void setInvolves_individual_group_id(IndividualGroup involves_individual_group_id) {
        this.involves_individual_group_id = involves_individual_group_id;
    }

    public List<PhysicalUnit> getPhysical_units() {
        return physical_units;
    }

    public void setPhysical_units(List<PhysicalUnit> physical_units) {
        this.physical_units = physical_units;
    }
    
    
     
//    public String getCollectorsText() {
//        return collectors_text;
//    }
//
//    public void setCollectorsText(String collectors_text) {
//        this.collectors_text = collectors_text;
//    }
//
//    public String getLocalityText() {
//        return locality_text;
//    }
//
//    public void setLocalityText(String locality_text) {
//        this.locality_text = locality_text;
//    }
//
//    public String getOccurrenceDateText() {
//        return occurrence_date_text;
//    }
//
//    public void setOccurrenceDateText(String occurrence_date_text) {
//        this.occurrence_date_text = occurrence_date_text;
//    }
//
//    public IndividualGroup getInvolvesIndividualGroupId() {
//        return involves_individual_group_id;
//    }
//
//    public void setInvolvesIndividualGroupId(IndividualGroup involves_individual_group_id) {
//        this.involves_individual_group_id = involves_individual_group_id;
//    }

    @XmlTransient
    public List<PhysicalUnit> getPhysicalUnitList() {
        return physical_units;
    }

    public void setPhysicalUnitList(List<PhysicalUnit> physical_units) {
        this.physical_units = physical_units;
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
        if (!(object instanceof Occurrence)) {
            return false;
        }
        Occurrence other = (Occurrence) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "se.nrm.dina.collections.data.model.Occurrence[ id=" + id + " ]";
    } 
}
