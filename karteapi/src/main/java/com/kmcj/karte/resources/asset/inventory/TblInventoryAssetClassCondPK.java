/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author admin
 */
@Embeddable
public class TblInventoryAssetClassCondPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "INVENTORY_ID")
    private String inventoryId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ASSET_CLASS")
    private String assetClass;

    public TblInventoryAssetClassCondPK() {
    }

    public TblInventoryAssetClassCondPK(String inventoryId, String assetClass) {
        this.inventoryId = inventoryId;
        this.assetClass = assetClass;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getAssetClass() {
        return assetClass;
    }

    public void setAssetClass(String assetClass) {
        this.assetClass = assetClass;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (inventoryId != null ? inventoryId.hashCode() : 0);
        hash += (assetClass != null ? assetClass.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblInventoryAssetClassCondPK)) {
            return false;
        }
        TblInventoryAssetClassCondPK other = (TblInventoryAssetClassCondPK) object;
        if ((this.inventoryId == null && other.inventoryId != null) || (this.inventoryId != null && !this.inventoryId.equals(other.inventoryId))) {
            return false;
        }
        if ((this.assetClass == null && other.assetClass != null) || (this.assetClass != null && !this.assetClass.equals(other.assetClass))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.asset.inventory.TblInventoryAssetClassCondPK[ inventoryId=" + inventoryId + ", assetClass=" + assetClass + " ]";
    }
    
}
