/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.conf;

import com.kmcj.karte.BasicResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author f.kitaoji
 */
public class CnfSystemList extends BasicResponse {
    private List<CnfSystem> cnfSystems;

    public CnfSystemList() {
        cnfSystems = new ArrayList<>();
    }
    
    /**
     * @return the cnfSystems
     */
    public List<CnfSystem> getCnfSystems() {
        return cnfSystems;
    }

    /**
     * @param cnfSystems the CnfSystems to set
     */
    public void setCnfSystems(List<CnfSystem> cnfSystems) {
        this.cnfSystems = cnfSystems;
    }
    
}
