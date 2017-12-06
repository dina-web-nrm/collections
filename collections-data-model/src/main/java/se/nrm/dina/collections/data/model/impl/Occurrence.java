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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import se.nrm.dina.collections.annotation.CollectionsManyToOne;
import se.nrm.dina.collections.annotation.CollectionsOneToMany;
import se.nrm.dina.collections.annotation.CollectionsResource;
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
@CollectionsResource(type = "occurrence")
public class Occurrence extends BaseEntity {
 
    @Column(name = "day_start")
    private Integer dayStart;
    
    @Column(name = "day_end")
    private Integer dayEnd;
    
    @Size(max = 255)
    @Column(name = "expedition_text")
    private String expeditionText;
    
    @Column(name = "month_start")
    private Integer monthStart;
    
    @Column(name = "month_end")
    private Integer monthEnd;
    
    @Column(name = "year_start")
    private Integer yearStart;
    
    @Column(name = "year_end")
    private Integer yearEnd;
 
    @Lob
    @Column(name = "collectors_text")
    private String collectorsText;
    
    @Lob
    @Column(name = "locality_text")
    private String localityText;
    
    @Lob
    @Column(name = "occurrence_date_text")
    private String occurrenceDateText;
    
    @JoinColumn(name = "involves_individual_group_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @CollectionsManyToOne(name = "involvesIndividualGroup", type = "IndividualGroup") 
    private IndividualGroup involvesIndividualGroup;
    
    @OneToMany(mappedBy = "isCollectedAtOccurrence", fetch = FetchType.LAZY)
    @CollectionsOneToMany(name = "physicalUnits", type = "PhysicalUnit") 
    private List<PhysicalUnit> physicalUnits;

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

    public String getCollectorsText() {
        return collectorsText;
    }

    public void setCollectorsText(String collectorsText) {
        this.collectorsText = collectorsText;
    }

    public String getLocalityText() {
        return localityText;
    }

    public void setLocalityText(String localityText) {
        this.localityText = localityText;
    }

    public String getOccurrenceDateText() {
        return occurrenceDateText;
    }

    public void setOccurrenceDateText(String occurrenceDateText) {
        this.occurrenceDateText = occurrenceDateText;
    }

    public IndividualGroup getInvolvesIndividualGroup() {
        return involvesIndividualGroup;
    }

    public void setInvolvesIndividualGroup(IndividualGroup involvesIndividualGroup) {
        this.involvesIndividualGroup = involvesIndividualGroup;
    }

    @XmlTransient
    public List<PhysicalUnit> getPhysicalUnits() {
        return physicalUnits;
    }

    public void setPhysicalUnits(List<PhysicalUnit> physicalUnits) {
        this.physicalUnits = physicalUnits;
    }
   
    public Integer getDayStart() {
        return dayStart;
    }

    public void setDayStart(Integer dayStart) {
        this.dayStart = dayStart;
    }

    public Integer getDayEnd() {
        return dayEnd;
    }

    public void setDayEnd(Integer dayEnd) {
        this.dayEnd = dayEnd;
    }

    public String getExpeditionText() {
        return expeditionText;
    }

    public void setExpeditionText(String expeditionText) {
        this.expeditionText = expeditionText;
    }

    public Integer getMonthStart() {
        return monthStart;
    }

    public void setMonthStart(Integer monthStart) {
        this.monthStart = monthStart;
    }

    public Integer getMonthEnd() {
        return monthEnd;
    }

    public void setMonthEnd(Integer monthEnd) {
        this.monthEnd = monthEnd;
    }

    public Integer getYearStart() {
        return yearStart;
    }

    public void setYearStart(Integer yearStart) {
        this.yearStart = yearStart;
    }

    public Integer getYearEnd() {
        return yearEnd;
    }

    public void setYearEnd(Integer yearEnd) {
        this.yearEnd = yearEnd;
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
