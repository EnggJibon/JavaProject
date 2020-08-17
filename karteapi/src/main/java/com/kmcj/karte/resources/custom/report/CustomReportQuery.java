/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report;

import java.util.List;

/**
 *
 * @author zdsoft
 */
public class CustomReportQuery {
    private List<QueryDefinition> queryDefinition;
    
    public List<QueryDefinition> getQueryDefinition(){
        return queryDefinition;
    }

    public void setQueryDefinition(List<QueryDefinition> queryDefinition) {
        this.queryDefinition = queryDefinition;
    }
}
