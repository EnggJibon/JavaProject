/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.mgmt.company;

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
public class TblInventoryMgmtCompanyPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "INVENTORY_ID")
    private String inventoryId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "MGMT_COMPANY_CODE")
    private String mgmtCompanyCode;

    public TblInventoryMgmtCompanyPK() {
    }

    public TblInventoryMgmtCompanyPK(String inventoryId, String mgmtCompanyCode) {
        this.inventoryId = inventoryId;
        this.mgmtCompanyCode = mgmtCompanyCode;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getMgmtCompanyCode() {
        return mgmtCompanyCode;
    }

    public void setMgmtCompanyCode(String mgmtCompanyCode) {
        this.mgmtCompanyCode = mgmtCompanyCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (inventoryId != null ? inventoryId.hashCode() : 0);
        hash += (mgmtCompanyCode != null ? mgmtCompanyCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblInventoryMgmtCompanyPK)) {
            return false;
        }
        TblInventoryMgmtCompanyPK other = (TblInventoryMgmtCompanyPK) object;
        if ((this.inventoryId == null && other.inventoryId != null) || (this.inventoryId != null && !this.inventoryId.equals(other.inventoryId))) {
            return false;
        }
        if ((this.mgmtCompanyCode == null && other.mgmtCompanyCode != null) || (this.mgmtCompanyCode != null && !this.mgmtCompanyCode.equals(other.mgmtCompanyCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.inventory.mgmt.company.TblInventoryMgmtCompanyPK[ inventoryId=" + inventoryId + ", mgmtCompanyCode=" + mgmtCompanyCode + " ]";
    }
    
}
