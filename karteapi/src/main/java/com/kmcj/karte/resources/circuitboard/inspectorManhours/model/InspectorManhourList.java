/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspectorManhours.model;

import com.kmcj.karte.resources.circuitboard.inspectorManhours.TblInspectorManhours;
import java.util.List;

/**
 *
 * @author h.ishihara
 */
public class InspectorManhourList {
   private List<TblInspectorManhours> manhourList = null;

    /**
     * @return the manhourList
     */
    public List<TblInspectorManhours> getManhourList() {
        return manhourList;
    }

    /**
     * @param manhourList the manhourList to set
     */
    public void setManhourList(List<TblInspectorManhours> manhourList) {
        this.manhourList = manhourList;
    }
   
}
