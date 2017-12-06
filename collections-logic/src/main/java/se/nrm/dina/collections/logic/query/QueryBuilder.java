/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.logic.query;

import lombok.extern.slf4j.Slf4j; 

/**
 *
 * @author idali
 */
@Slf4j
public class QueryBuilder {
      
    private static QueryBuilder instance = null;
  
    public static synchronized QueryBuilder getInstance() {
        if (instance == null) {
            instance = new QueryBuilder();
        }
        return instance;
    }

    public String getQueryFindIndividualGroupsByCatalogNumber(String catalogNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ig ");
        sb.append("FROM IndividualGroup AS ig ");
        sb.append("JOIN ig.physicalUnits pu ");
        sb.append("JOIN pu.belongsToCatalogedUnit cu ");
        sb.append("WHERE pu.representsIndividualGroup = ig ");
        sb.append("AND pu.belongsToCatalogedUnit = cu ");
        if (catalogNumber != null && !catalogNumber.isEmpty()) {
            sb.append("AND cu.catalogNumber = '");
            sb.append(catalogNumber);
            sb.append("'");
        } 
        return sb.toString();
    }
    
    public String getQueryFindIndividualGroupsByCatalogNumberAndIdentificationTaxonStanderized(String catalogNumber, String taxonStanderized) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ig ");
        sb.append("FROM IndividualGroup AS ig ");
        sb.append("JOIN ig.physicalUnits pu ");
        sb.append("JOIN pu.belongsToCatalogedUnit cu ");
        sb.append("JOIN ig.identifications id ");
        sb.append("WHERE pu.representsIndividualGroup = ig ");
        sb.append("AND id.appliesToIndividualGroup = ig ");
        sb.append("AND pu.belongsToCatalogedUnit = cu");
        if (catalogNumber != null && !catalogNumber.isEmpty()) {
            sb.append(" AND cu.catalogNumber = '");
            sb.append(catalogNumber);
            sb.append("'");
        } 
        if(taxonStanderized != null && !taxonStanderized.isEmpty()) {
            sb.append(" AND id.identifiedTaxonNameStandardized = '");
            sb.append(taxonStanderized);
            sb.append("'");
        } 
        return sb.toString();
    }

}
