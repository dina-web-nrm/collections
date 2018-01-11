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
        sb.append("FROM IndividualGroup ig ");
        sb.append("LEFT JOIN ig.physicalUnits pu ");
        sb.append("LEFT JOIN pu.belongsToCatalogedUnit cu ");
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
        
        boolean isCondition = false;
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ig ");
        sb.append("FROM IndividualGroup ig");
        if(catalogNumber != null && !catalogNumber.isEmpty()) {
            sb.append(" JOIN ig.physicalUnits pu");
            sb.append(" JOIN pu.belongsToCatalogedUnit cu");
            isCondition = true;
        }
        if(taxonStanderized != null && !taxonStanderized.isEmpty()) {
            sb.append(" JOIN ig.identifications id");
            isCondition = true;
        } 
        if(isCondition) {
            sb.append(" WHERE");
            
            if(catalogNumber != null && !catalogNumber.isEmpty()) {
                sb.append(" pu.representsIndividualGroup = ig");
                sb.append(" AND pu.belongsToCatalogedUnit = cu");
                sb.append(" AND cu.catalogNumber = '");
                sb.append(catalogNumber);
                sb.append("'");
            }
            
            if(taxonStanderized != null && !taxonStanderized.isEmpty()) {
                if(catalogNumber != null && !catalogNumber.isEmpty()) {
                    sb.append(" AND");
                }
                sb.append(" id.appliesToIndividualGroup = ig");
                sb.append(" AND id.identifiedTaxonNameStandardized = '");
                sb.append(taxonStanderized);
                sb.append("'");
            }  
            sb.append(" GROUP BY ig"); 
        } 
        return sb.toString();
    }

}
