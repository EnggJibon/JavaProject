/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.dailyreport2.stockupdate;

import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationVoList;

/**
 *
 * @author f.kitaoji
 */
public class ComponentStock {
    
    private String productionId;
    private String productionDetailId;
    private String componentId;
    private String procedureId;
    private String procedureCode;
    private long quantity;
    private TblComponentLotRelationVoList tblComponentLotRelationVoList;

    /**
     * @return the componentId
     */
    public String getComponentId() {
        return componentId;
    }

    /**
     * @param componentId the componentId to set
     */
    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    /**
     * @return the procedureId
     */
    public String getProcedureId() {
        return procedureId;
    }

    /**
     * @param procedureId the procedureId to set
     */
    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    /**
     * @return the quantity
     */
    public long getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the tblComponentLotRelationVoList
     */
    public TblComponentLotRelationVoList getTblComponentLotRelationVoList() {
        return tblComponentLotRelationVoList;
    }

    /**
     * @param tblComponentLotRelationVoList the tblComponentLotRelationVoList to set
     */
    public void setTblComponentLotRelationVoList(TblComponentLotRelationVoList tblComponentLotRelationVoList) {
        this.tblComponentLotRelationVoList = tblComponentLotRelationVoList;
    }

    /**
     * @return the productionDetailId
     */
    public String getProductionDetailId() {
        return productionDetailId;
    }

    /**
     * @param productionDetailId the productionDetailId to set
     */
    public void setProductionDetailId(String productionDetailId) {
        this.productionDetailId = productionDetailId;
    }

    /**
     * @return the productionId
     */
    public String getProductionId() {
        return productionId;
    }

    /**
     * @param productionId the productionId to set
     */
    public void setProductionId(String productionId) {
        this.productionId = productionId;
    }

    /**
     * @return the procedureCode
     */
    public String getProcedureCode() {
        return procedureCode;
    }

    /**
     * @param procedureCode the procedureCode to set
     */
    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }
    
    
    
}
