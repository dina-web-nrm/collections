/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.data.model.impl;
  
import java.util.List;
import javax.persistence.Basic;
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
//import se.nrm.dina.collections.annotation.CollectionsOneToMany;
import se.nrm.dina.collections.data.model.BaseEntity;

/**
 *
 * @author idali
 */
@Entity
@Table(name = "cataloged_unit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatalogedUnit.findAll", query = "SELECT c FROM CatalogedUnit c"),
    @NamedQuery(name = "CatalogedUnit.findById", query = "SELECT c FROM CatalogedUnit c WHERE c.id = :id")
//    @NamedQuery(name = "CatalogedUnit.findByCatalogNumber", query = "SELECT c FROM CatalogedUnit c WHERE c.catalogNumber = :catalogNumber")
})
public class CatalogedUnit extends BaseEntity {
 
    @Size(max = 150)
    @Column(name = "stored_under_taxon_name")
    private String storedUnderTaxonName;

    @Basic(optional = false)
    @Column(name = "catalog_number")
    private String catalogNumber;
     
    @Column(name = "publish_record")
    private Boolean publishRecord;
     
    @Lob
    @Size(max = 65535)
    @Column(name = "remarks")
    private String remarks;
    
    @OneToMany(mappedBy = "belongsToCatalogedUnit", fetch = FetchType.LAZY)
//    @CollectionsOneToMany(name = "physicalUnits", type = "PhysicalUnit") 
    private List<PhysicalUnit> physicalUnits;

    public CatalogedUnit() {
    }

    public CatalogedUnit(Long id) {
        this.id = id;
    }

    public CatalogedUnit(Long id, int version, String catalogNumber) {
        this.id = id;
        this.version = version;
        this.catalogNumber = catalogNumber;
    }
 
    @Override
    public long getEntityId() {
        return id;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(String catalog_number) {
        this.catalogNumber = catalog_number;
    }

    @XmlTransient
    public List<PhysicalUnit> getPhysicalUnits() {
        return physicalUnits;
    }
 
    public void setPhysicalUnits(List<PhysicalUnit> physicalUnits) {
        this.physicalUnits = physicalUnits;
    }
  
    public CatalogedUnit(Long id, int version) {
        this.id = id;
        this.version = version;
    }
  
    public Boolean getPublishRecord() {
        return publishRecord;
    }

    public void setPublishRecord(Boolean publishRecord) {
        this.publishRecord = publishRecord;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStoredUnderTaxonName() {
        return storedUnderTaxonName;
    }

    public void setStoredUnderTaxonName(String storedUnderTaxonName) {
        this.storedUnderTaxonName = storedUnderTaxonName;
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
        if (!(object instanceof CatalogedUnit)) {
            return false;
        }
        CatalogedUnit other = (CatalogedUnit) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "se.nrm.dina.collections.data.model.CatalogedUnit[ id=" + id + " ]";
    }  
}
