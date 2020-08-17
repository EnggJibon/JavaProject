/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.inventory;

import java.util.List;

/**
 *
 * @author admin
 */
public class SearchConditionList {

    private List<SearchCondition> searchConditions;
   

    /**
     * @return the searchConditions
     */
    public List<SearchCondition> getSearchConditions() {
        return searchConditions;
    }

    /**
     * @param searchConditions the searchConditions to set
     */
    public void setSearchConditions(List<SearchCondition> searchConditions) {
        this.searchConditions = searchConditions;
    }

}
