/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result.model;

/**
 * Component poInfo model.
 *
 * @author penggd
 */
public class TblShipmentOutboundVo {
    
    private String shipmentComponentCode;

    private TblShipmentOutbound shipmentOutbound;

    public String getShipmentComponentCode() {
        return shipmentComponentCode;
    }

    public void setShipmentComponentCode(String shipmentComponentCode) {
        this.shipmentComponentCode = shipmentComponentCode;
    }

    public TblShipmentOutbound getShipmentOutbound() {
        return shipmentOutbound;
    }

    public void setShipmentOutbound(TblShipmentOutbound shipmentOutbound) {
        this.shipmentOutbound = shipmentOutbound;
    }
    
}
