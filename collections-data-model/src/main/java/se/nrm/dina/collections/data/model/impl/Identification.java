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
@Table(name = "identification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Identification.findAll", query = "SELECT i FROM Identification i"), 
    @NamedQuery(name = "Identification.findById", query = "SELECT i FROM Identification i WHERE i.id = :id")})
public class Identification extends BaseEntity {
 
    @Lob
    @Column(name = "identification_text")
    private String identificationText;
    
    @JoinColumn(name = "applies_to_individual_group_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @CollectionsManyToOne(name = "individual_group", type="IndividualGroup")
    private IndividualGroup appliesToIndividualGroupId;

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

    public IndividualGroup getAppliesToIndividualGroupId() {
        return appliesToIndividualGroupId;
    }

    public void setAppliesToIndividualGroupId(IndividualGroup appliesToIndividualGroupId) {
        this.appliesToIndividualGroupId = appliesToIndividualGroupId;
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
