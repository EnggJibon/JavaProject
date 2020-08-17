/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.assetno;

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
public class MstMoldAssetNoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MOLD_UUID")
    private String moldUuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ASSET_NO")
    private String assetNo;

    public MstMoldAssetNoPK() {
    }

    public MstMoldAssetNoPK(String moldUuid, String assetNo) {
        this.moldUuid = moldUuid;
        this.assetNo = assetNo;
    }

    public String getMoldUuid() {
        return moldUuid;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    public String getAssetNo() {
        return assetNo;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (moldUuid != null ? moldUuid.hashCode() : 0);
        hash += (assetNo != null ? assetNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMoldAssetNoPK)) {
            return false;
        }
        MstMoldAssetNoPK other = (MstMoldAssetNoPK) object;
        if ((this.moldUuid == null && other.moldUuid != null) || (this.moldUuid != null && !this.moldUuid.equals(other.moldUuid))) {
            return false;
        }
        if ((this.assetNo == null && other.assetNo != null) || (this.assetNo != null && !this.assetNo.equals(other.assetNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.assetno.MstMoldAssetNoPK[ moldUuid=" + moldUuid + ", assetNo=" + assetNo + " ]";
    }
    
}
