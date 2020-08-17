/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_inventory_asset_class_cond")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblInventoryAssetClassCond.findAll", query = "SELECT t FROM TblInventoryAssetClassCond t"),
    @NamedQuery(name = "TblInventoryAssetClassCond.findByInventoryId", query = "SELECT t FROM TblInventoryAssetClassCond t WHERE t.tblInventoryAssetClassCondPK.inventoryId = :inventoryId"),
    @NamedQuery(name = "TblInventoryAssetClassCond.findByAssetClass", query = "SELECT t FROM TblInventoryAssetClassCond t WHERE t.tblInventoryAssetClassCondPK.assetClass = :assetClass")
})
@Cacheable(value = false)
public class TblInventoryAssetClassCond implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblInventoryAssetClassCondPK tblInventoryAssetClassCondPK;

    public TblInventoryAssetClassCond() {
    }

    public TblInventoryAssetClassCond(TblInventoryAssetClassCondPK tblInventoryAssetClassCondPK) {
        this.tblInventoryAssetClassCondPK = tblInventoryAssetClassCondPK;
    }

    public TblInventoryAssetClassCond(String inventoryId, String assetClass) {
        this.tblInventoryAssetClassCondPK = new TblInventoryAssetClassCondPK(inventoryId, assetClass);
    }

    public TblInventoryAssetClassCondPK getTblInventoryAssetClassCondPK() {
        return tblInventoryAssetClassCondPK;
    }

    public void setTblInventoryAssetClassCondPK(TblInventoryAssetClassCondPK tblInventoryAssetClassCondPK) {
        this.tblInventoryAssetClassCondPK = tblInventoryAssetClassCondPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblInventoryAssetClassCondPK != null ? tblInventoryAssetClassCondPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TblInventoryAssetClassCond)) {
            return false;
        }
        TblInventoryAssetClassCond other = (TblInventoryAssetClassCond) object;
        if ((this.tblInventoryAssetClassCondPK == null && other.tblInventoryAssetClassCondPK != null) || (this.tblInventoryAssetClassCondPK != null && !this.tblInventoryAssetClassCondPK.equals(other.tblInventoryAssetClassCondPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.asset.inventory.TblInventoryAssetClassCond[ tblInventoryAssetClassCondPK=" + tblInventoryAssetClassCondPK + " ]";
    }
    
}
