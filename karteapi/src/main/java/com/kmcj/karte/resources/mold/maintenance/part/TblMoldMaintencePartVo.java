/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.part;

/**
 *
 * @author BnK Win10 2010
 */
public class TblMoldMaintencePartVo {
    
    protected TblMoldMaintenancePartPK tblMoldMaintenancePartPK;
    private int replaceOrRepair;
    private int shotCntAtManit;
    private String moldPartId;
    private String moldPartCode;
    private String moldPartName;
    private String location;
    private boolean isError;
    private int disposeQuantity;
    private int recycleQuantity;
    
    public TblMoldMaintencePartVo() {
    }

    public TblMoldMaintenancePartPK getTblMoldMaintenancePartPK() {
        return tblMoldMaintenancePartPK;
    }

    public void setTblMoldMaintenancePartPK(TblMoldMaintenancePartPK tblMoldMaintenancePartPK) {
        this.tblMoldMaintenancePartPK = tblMoldMaintenancePartPK;
    }

    public int getReplaceOrRepair() {
        return replaceOrRepair;
    }

    public void setReplaceOrRepair(int replaceOrRepair) {
        this.replaceOrRepair = replaceOrRepair;
    }

    public int getShotCntAtManit() {
        return shotCntAtManit;
    }

    public void setShotCntAtManit(int shotCntAtManit) {
        this.shotCntAtManit = shotCntAtManit;
    }

    public String getMoldPartId() {
        return moldPartId;
    }

    public void setMoldPartId(String moldPartId) {
        this.moldPartId = moldPartId;
    }

    public String getMoldPartCode() {
        return moldPartCode;
    }

    public void setMoldPartCode(String moldPartCode) {
        this.moldPartCode = moldPartCode;
    }

    public String getMoldPartName() {
        return moldPartName;
    }

    public void setMoldPartName(String moldPartName) {
        this.moldPartName = moldPartName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isIsError() {
        return isError;
    }

    public void setIsError(boolean isError) {
        this.isError = isError;
    }

    public Integer getDisposeQuantity() {
        return disposeQuantity;
    }

    public void setDisposeQuantity(Integer disposeQuantity) {
        this.disposeQuantity = disposeQuantity;
    }
    
    public Integer getRecycleQuantity() {
        return recycleQuantity;
    }

    public void setRecycleQuantity(Integer recycleQuantity) {
        this.recycleQuantity = recycleQuantity;
    }
    
}
