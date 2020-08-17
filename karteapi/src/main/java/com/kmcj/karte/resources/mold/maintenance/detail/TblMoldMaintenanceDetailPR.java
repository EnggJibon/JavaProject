/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.detail;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.mold.part.stock.MoldPartStock;
 
public class TblMoldMaintenanceDetailPR extends BasicResponse {

    private String moldPartRelId;
    private boolean replaceIsChecked;
    private boolean repairIsChecked;
    private boolean partialReplaceIsChecked;
    private MoldPartStock replaceStock;
    private int replaceQty;
    private int recycleQty;
    private int disposeQty;
    private int fromNewQty;
    private int fromUsedQty;

    public TblMoldMaintenanceDetailPR(){
        
    }
    public TblMoldMaintenanceDetailPR(String moldPartRelId, Boolean replaceIsChecked, Boolean repairIsChecked){
        this.moldPartRelId = moldPartRelId;
        this.replaceIsChecked = replaceIsChecked;
        this.repairIsChecked = repairIsChecked;
    }
    public String getMoldPartRelId() {
        return moldPartRelId;
    }

    public void setMoldPartRelId(String moldPartRelId) {
        this.moldPartRelId = moldPartRelId;
    }

    public boolean getReplaceIsChecked() {
        return replaceIsChecked;
    }

    public void setReplaceIsChecked(boolean replaceIsChecked) {
        this.replaceIsChecked = replaceIsChecked;
    }

    public boolean getRepairIsChecked() {
        return repairIsChecked;
    }

    public void setRepairIsChecked(boolean repairIsChecked) {
        this.repairIsChecked = repairIsChecked;
    }

    public boolean getPartialReplaceIsChecked() {
        return partialReplaceIsChecked;
    }

    public void setPartialReplaceIsChecked(boolean partialReplaceIsChecked) {
        this.partialReplaceIsChecked = partialReplaceIsChecked;
    }

    public MoldPartStock getReplaceStock() {
        return replaceStock;
    }

    public void setReplaceStock(MoldPartStock replaceStock) {
        this.replaceStock = replaceStock;
    }

    public int getReplaceQty() {
        return replaceQty;
    }

    public void setReplaceQty(int replaceQty) {
        this.replaceQty = replaceQty;
    }

    public int getRecycleQty() {
        return recycleQty;
    }

    public void setRecycleQty(int recycleQty) {
        this.recycleQty = recycleQty;
    }

    public int getDisposeQty() {
        return disposeQty;
    }

    public void setDisposeQty(int disposeQty) {
        this.disposeQty = disposeQty;
    }

    public int getFromNewQty() {
        return fromNewQty;
    }

    public void setFromNewQty(int fromNewQty) {
        this.fromNewQty = fromNewQty;
    }

    public int getFromUsedQty() {
        return fromUsedQty;
    }

    public void setFromUsedQty(int fromUsedQty) {
        this.fromUsedQty = fromUsedQty;
    }
}
