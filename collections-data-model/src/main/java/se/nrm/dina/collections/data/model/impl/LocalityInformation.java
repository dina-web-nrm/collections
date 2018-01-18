/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.data.model.impl;
 
import java.util.List;  
import javax.persistence.Column;
import javax.persistence.Entity;  
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
@Table(name = "locality_information")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LocalityInformation.findAll", query = "SELECT l FROM LocalityInformation l"),
    @NamedQuery(name = "LocalityInformation.findById", query = "SELECT l FROM LocalityInformation l WHERE l.id = :id"), 
    @NamedQuery(name = "LocalityInformation.findByContinentStandardized", query = "SELECT l FROM LocalityInformation l WHERE l.continentStandardized = :continentStandardized"),
    @NamedQuery(name = "LocalityInformation.findByCoordinatesVerbatim", query = "SELECT l FROM LocalityInformation l WHERE l.coordinatesVerbatim = :coordinatesVerbatim"),
    @NamedQuery(name = "LocalityInformation.findByCountryStandardized", query = "SELECT l FROM LocalityInformation l WHERE l.countryStandardized = :countryStandardized"),
    @NamedQuery(name = "LocalityInformation.findByDistrictStandardized", query = "SELECT l FROM LocalityInformation l WHERE l.districtStandardized = :districtStandardized"),
    @NamedQuery(name = "LocalityInformation.findByGeodeticDatumStandardized", query = "SELECT l FROM LocalityInformation l WHERE l.geodeticDatumStandardized = :geodeticDatumStandardized"),
    @NamedQuery(name = "LocalityInformation.findByGeoreferenceSourcesText", query = "SELECT l FROM LocalityInformation l WHERE l.georeferenceSourcesText = :georeferenceSourcesText"),
    @NamedQuery(name = "LocalityInformation.findByLatitudeStandardized", query = "SELECT l FROM LocalityInformation l WHERE l.latitudeStandardized = :latitudeStandardized"),
    @NamedQuery(name = "LocalityInformation.findByLocalityStandardized", query = "SELECT l FROM LocalityInformation l WHERE l.localityStandardized = :localityStandardized"),
    @NamedQuery(name = "LocalityInformation.findByLocalityVerbatim", query = "SELECT l FROM LocalityInformation l WHERE l.localityVerbatim = :localityVerbatim"),
    @NamedQuery(name = "LocalityInformation.findByLongitudeStandardized", query = "SELECT l FROM LocalityInformation l WHERE l.longitudeStandardized = :longitudeStandardized"),   
    @NamedQuery(name = "LocalityInformation.findByProvinceStandardized", query = "SELECT l FROM LocalityInformation l WHERE l.provinceStandardized = :provinceStandardized")})
public class LocalityInformation extends BaseEntity {
 
    @Size(max = 255)
    @Column(name = "continent_standardized")
    private String continentStandardized;
    
    @Size(max = 100)
    @Column(name = "coordinates_verbatim")
    private String coordinatesVerbatim;
    
    @Size(max = 45)
    @Column(name = "coordinate_uncertainty_in_meters")
    private String coordinateUncertaintyInMeters;
    
    @Size(max = 255)
    @Column(name = "country_standardized")
    private String countryStandardized;
    
    @Size(max = 255)
    @Column(name = "district_standardized")
    private String districtStandardized;
    
    @Size(max = 100)
    @Column(name = "geodetic_datum_standardized")
    private String geodeticDatumStandardized;
    
    @Size(max = 255)
    @Column(name = "georeference_sources_text")
    private String georeferenceSourcesText;
    
    @Size(max = 45)
    @Column(name = "latitude_standardized")
    private String latitudeStandardized;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "locality_remarks")
    private String localityRemarks;
    
    @Size(max = 255)
    @Column(name = "locality_standardized")
    private String localityStandardized;
    
    @Size(max = 255)
    @Column(name = "locality_verbatim")
    private String localityVerbatim;
    
    @Size(max = 45)
    @Column(name = "longitude_standardized")
    private String longitudeStandardized;
    
    @Size(max = 20)
    @Column(name = "maximum_depth_in_meters")
    private String maximumDepthInMeters;
    
    @Size(max = 20)
    @Column(name = "maximum_elevation_in_meters")
    private String maximumElevationInMeters;
    
    @Size(max = 20)
    @Column(name = "minimum_depth_in_meters")
    private String minimumDepthInMeters;
    
    @Size(max = 20)
    @Column(name = "minimum_elevation_in_meters")
    private String minimumElevationInMeters;
    
    @Size(max = 255)
    @Column(name = "province_standardized")
    private String provinceStandardized;
    
    @OneToMany(mappedBy = "localityInformation")
    private List<Occurrence> occurrenceList;

    public LocalityInformation() {
    }

    public LocalityInformation(Long id) {
        this.id = id;
    }

    @Override
    public long getEntityId() {
        return id;
    }
    
    public LocalityInformation(Long id, int version) {
        this.id = id;
        this.version = version;
    }
 
    public String getContinentStandardized() {
        return continentStandardized;
    }

    public void setContinentStandardized(String continentStandardized) {
        this.continentStandardized = continentStandardized;
    }

    public String getCoordinatesVerbatim() {
        return coordinatesVerbatim;
    }

    public void setCoordinatesVerbatim(String coordinatesVerbatim) {
        this.coordinatesVerbatim = coordinatesVerbatim;
    }

    public String getCoordinateUncertaintyInMeters() {
        return coordinateUncertaintyInMeters;
    }

    public void setCoordinateUncertaintyInMeters(String coordinateUncertaintyInMeters) {
        this.coordinateUncertaintyInMeters = coordinateUncertaintyInMeters;
    }

    public String getCountryStandardized() {
        return countryStandardized;
    }

    public void setCountryStandardized(String countryStandardized) {
        this.countryStandardized = countryStandardized;
    }

    public String getDistrictStandardized() {
        return districtStandardized;
    }

    public void setDistrictStandardized(String districtStandardized) {
        this.districtStandardized = districtStandardized;
    }

    public String getGeodeticDatumStandardized() {
        return geodeticDatumStandardized;
    }

    public void setGeodeticDatumStandardized(String geodeticDatumStandardized) {
        this.geodeticDatumStandardized = geodeticDatumStandardized;
    }

    public String getGeoreferenceSourcesText() {
        return georeferenceSourcesText;
    }

    public void setGeoreferenceSourcesText(String georeferenceSourcesText) {
        this.georeferenceSourcesText = georeferenceSourcesText;
    }

    public String getLatitudeStandardized() {
        return latitudeStandardized;
    }

    public void setLatitudeStandardized(String latitudeStandardized) {
        this.latitudeStandardized = latitudeStandardized;
    }

    public String getLocalityRemarks() {
        return localityRemarks;
    }

    public void setLocalityRemarks(String localityRemarks) {
        this.localityRemarks = localityRemarks;
    }

    public String getLocalityStandardized() {
        return localityStandardized;
    }

    public void setLocalityStandardized(String localityStandardized) {
        this.localityStandardized = localityStandardized;
    }

    public String getLocalityVerbatim() {
        return localityVerbatim;
    }

    public void setLocalityVerbatim(String localityVerbatim) {
        this.localityVerbatim = localityVerbatim;
    }

    public String getLongitudeStandardized() {
        return longitudeStandardized;
    }

    public void setLongitudeStandardized(String longitudeStandardized) {
        this.longitudeStandardized = longitudeStandardized;
    }

    public String getMaximumDepthInMeters() {
        return maximumDepthInMeters;
    }

    public void setMaximumDepthInMeters(String maximumDepthInMeters) {
        this.maximumDepthInMeters = maximumDepthInMeters;
    }

    public String getMaximumElevationInMeters() {
        return maximumElevationInMeters;
    }

    public void setMaximumElevationInMeters(String maximumElevationInMeters) {
        this.maximumElevationInMeters = maximumElevationInMeters;
    }

    public String getMinimumDepthInMeters() {
        return minimumDepthInMeters;
    }

    public void setMinimumDepthInMeters(String minimumDepthInMeters) {
        this.minimumDepthInMeters = minimumDepthInMeters;
    }

    public String getMinimumElevationInMeters() {
        return minimumElevationInMeters;
    }

    public void setMinimumElevationInMeters(String minimumElevationInMeters) {
        this.minimumElevationInMeters = minimumElevationInMeters;
    }

    public String getProvinceStandardized() {
        return provinceStandardized;
    }

    public void setProvinceStandardized(String provinceStandardized) {
        this.provinceStandardized = provinceStandardized;
    }

    @XmlTransient
    public List<Occurrence> getOccurrenceList() {
        return occurrenceList;
    }

    public void setOccurrenceList(List<Occurrence> occurrenceList) {
        this.occurrenceList = occurrenceList;
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
        if (!(object instanceof LocalityInformation)) {
            return false;
        }
        LocalityInformation other = (LocalityInformation) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "se.nrm.dina.collections.data.model.impl.LocalityInformation[ id=" + id + " ]";
    }  
}
