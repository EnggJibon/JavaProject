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
public class TblPoOutboundVo {
    
    private String poComponentCode;

    private TblPoOutbound poOutbound;

    public String getPoComponentCode() {
        return poComponentCode;
    }

    public void setPoComponentCode(String poComponentCode) {
        this.poComponentCode = poComponentCode;
    }

    public TblPoOutbound getPoOutbound() {
        return poOutbound;
    }

    public void setPoOutbound(TblPoOutbound poOutbound) {
        this.poOutbound = poOutbound;
    }
    
}
