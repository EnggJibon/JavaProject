/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result.model;

import java.util.List;

/**
 * Component poInfo model.
 *
 * @author penggd
 */
public class ComponentPoInfoForBatch {

    private List<TblPoOutboundVo> tblPoOutboundVoList;
    private List<TblShipmentOutboundVo> tblShipmentOutboundVoList;

    public List<TblPoOutboundVo> getTblPoOutboundVoList() {
        return tblPoOutboundVoList;
    }

    public void setTblPoOutboundVoList(List<TblPoOutboundVo> tblPoOutboundVoList) {
        this.tblPoOutboundVoList = tblPoOutboundVoList;
    }

    public List<TblShipmentOutboundVo> getTblShipmentOutboundVoList() {
        return tblShipmentOutboundVoList;
    }

    public void setTblShipmentOutboundVoList(List<TblShipmentOutboundVo> tblShipmentOutboundVoList) {
        this.tblShipmentOutboundVoList = tblShipmentOutboundVoList;
    }
    
}
