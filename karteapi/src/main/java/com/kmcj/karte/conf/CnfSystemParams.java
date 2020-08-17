/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.conf;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author f.kitaoji
 */

@XmlRootElement
public class CnfSystemParams {
    
    private List<CnfSystemPK> cnfSystemPKs;
    
    public void CnfSystemParams() {
        cnfSystemPKs = new ArrayList<>();
    }

    /**
     * @return the cnfSystemPKs
     */
    public List<CnfSystemPK> getCnfSystemPKs() {
        return cnfSystemPKs;
    }

    /**
     * @param cnfSystemPKs the cnfSystemPKs to set
     */
    public void setCnfSystemPKs(List<CnfSystemPK> cnfSystemPKs) {
        this.cnfSystemPKs = cnfSystemPKs;
    }
    
    
}
