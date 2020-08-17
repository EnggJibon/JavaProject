/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.prifix;

import com.kmcj.karte.BasicResponse;

import java.util.List;

/**
 *
 * @author bacpd
 */
public class PrefixData extends BasicResponse {
    private List<MstCircuitNamePrefix> prefixList = null;
    private String prefix = "";
    private String id = "";
    private String prefixId = "";

    /**
     * @return the prefixList
     */
    public List<MstCircuitNamePrefix> getPrefixList() {
        return prefixList;
    }

    /**
     * @param prefixList the prefixList to set
     */
    public void setPrefixList(List<MstCircuitNamePrefix> prefixList) {
        this.prefixList = prefixList;
    }

    public void setPrefix(String newPrefix) {
        this.prefix = newPrefix;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setId(String newId) {
        this.id = newId;
    }

    public String getId() {
        return this.id;
    }

    public void setPrefixId(String newId) {
        this.prefixId = newId;
    }

    public String getPrefixId() {
        return this.prefixId;
    }
}
