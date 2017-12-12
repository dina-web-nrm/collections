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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement; 
import se.nrm.dina.collections.annotation.CollectionsResource; 
//import se.nrm.dina.collections.annotation.CollectionsManyToOne; 
import se.nrm.dina.collections.data.model.BaseEntity;

/**
 *
 * @author idali
 */
@Entity
@Table(name = "identification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Identification.findAll", query = "SELECT i FROM Identification i"), 
    @NamedQuery(name = "Identification.findById", query = "SELECT i FROM Identification i WHERE i.id = :id")})
@CollectionsResource(type = "identification")
public class Identification extends BaseEntity {
 
    @Column(name = "is_current_identification")
    private Boolean isCurrentIdentification;
 
    @Lob
    @Size(max = 65535)
    @Column(name = "identification_remarks")
    private String identificationRemarks;
    
    @Size(max = 255)
    @Column(name = "identified_as_verbatim")
    private String identifiedAsVerbatim;
    
    @Size(max = 100)
    @Column(name = "identified_by_agent_text")
    private String identifiedByAgentText;
    
    @Column(name = "identified_day")
    private Integer identifiedDay;
    
    @Column(name = "identified_month")
    private Integer identifiedMonth;
    
    @Column(name = "identified_year")
    private Integer identifiedYear;
    
    @Size(max = 255)
    @Column(name = "identified_taxon_name_standardized")
    private String identifiedTaxonNameStandardized;
 
    @Lob
    @Column(name = "identification_text")
    private String identificationText;
    
    @JoinColumn(name = "applies_to_individual_group_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}) 
//    @CollectionsManyToOne(name = "appliesToIndividualGroup", type="IndividualGroup")  
    private IndividualGroup appliesToIndividualGroup;

    public Identification() {
    }

    public Identification(Long id) {
        this.id = id;
    }

    public Identification(Long id, int version) {
        this.id = id;
        this.version = version;
    }
 
    @Override
    public long getEntityId() {
        return id;
    }

    public String getIdentificationText() {
        return identificationText;
    }

    public void setIdentificationText(String identificationText) {
        this.identificationText = identificationText;
    }

    public IndividualGroup getAppliesToIndividualGroup() {
        return appliesToIndividualGroup;
    }

    public void setAppliesToIndividualGroup(IndividualGroup appliesToIndividualGroup) {
        this.appliesToIndividualGroup = appliesToIndividualGroup;
    }
    
    public String getIdentificationRemarks() {
        return identificationRemarks;
    }

    public void setIdentificationRemarks(String identificationRemarks) {
        this.identificationRemarks = identificationRemarks;
    }

    public String getIdentifiedAsVerbatim() {
        return identifiedAsVerbatim;
    }

    public void setIdentifiedAsVerbatim(String identifiedAsVerbatim) {
        this.identifiedAsVerbatim = identifiedAsVerbatim;
    }

    public String getIdentifiedByAgentText() {
        return identifiedByAgentText;
    }

    public void setIdentifiedByAgentText(String identifiedByAgentText) {
        this.identifiedByAgentText = identifiedByAgentText;
    }

    public Integer getIdentifiedDay() {
        return identifiedDay;
    }

    public void setIdentifiedDay(Integer identifiedDay) {
        this.identifiedDay = identifiedDay;
    }

    public Integer getIdentifiedMonth() {
        return identifiedMonth;
    }

    public void setIdentifiedMonth(Integer identifiedMonth) {
        this.identifiedMonth = identifiedMonth;
    }

    public Integer getIdentifiedYear() {
        return identifiedYear;
    }

    public void setIdentifiedYear(Integer identifiedYear) {
        this.identifiedYear = identifiedYear;
    }

    public String getIdentifiedTaxonNameStandardized() {
        return identifiedTaxonNameStandardized;
    }

    public void setIdentifiedTaxonNameStandardized(String identifiedTaxonNameStandardized) {
        this.identifiedTaxonNameStandardized = identifiedTaxonNameStandardized;
    }

    public Boolean getIsCurrentIdentification() {
        return isCurrentIdentification;
    }

    public void setIsCurrentIdentification(Boolean isCurrentIdentification) {
        this.isCurrentIdentification = isCurrentIdentification;
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
        if (!(object instanceof Identification)) {
            return false;
        }
        Identification other = (Identification) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "se.nrm.dina.collections.data.model.Identification[ id=" + id + " ]";
    }    
 
}
