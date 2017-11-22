/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.logic.query;

/**
 *
 * @author idali
 */
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

}
