/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset;

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
public class MstAssetPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ASSET_NO")
    private String assetNo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "BRANCH_NO")
    private String branchNo;

    public MstAssetPK() {
    }

    public MstAssetPK(String assetNo, String branchNo) {
        this.assetNo = assetNo;
        this.branchNo = branchNo;
    }

    public String getAssetNo() {
        return assetNo;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    public String getBranchNo() {
        return branchNo;
    }

    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (assetNo != null ? assetNo.hashCode() : 0);
        hash += (branchNo != null ? branchNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstAssetPK)) {
            return false;
        }
        MstAssetPK other = (MstAssetPK) object;
        if ((this.assetNo == null && other.assetNo != null) || (this.assetNo != null && !this.assetNo.equals(other.assetNo))) {
            return false;
        }
        if ((this.branchNo == null && other.branchNo != null) || (this.branchNo != null && !this.branchNo.equals(other.branchNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.asset.MstAssetPK[ assetNo=" + assetNo + ", branchNo=" + branchNo + " ]";
    }
    
}
