/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.match;

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
public class TblAssetMatchingResultDetailPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ASSET_ID")
    private String assetId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MOLD_UUID")
    private String moldUuid;
    public TblAssetMatchingResultDetailPK() {
    }

    public TblAssetMatchingResultDetailPK(String assetId, String moldUuid) {
        this.assetId = assetId;
        this.moldUuid = moldUuid;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getMoldUuid() {
        return moldUuid;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (assetId != null ? assetId.hashCode() : 0);
        hash += (moldUuid != null ? moldUuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblAssetMatchingResultDetailPK)) {
            return false;
        }
        TblAssetMatchingResultDetailPK other = (TblAssetMatchingResultDetailPK) object;
        if ((this.assetId == null && other.assetId != null) || (this.assetId != null && !this.assetId.equals(other.assetId))) {
            return false;
        }
        if ((this.moldUuid == null && other.moldUuid != null) || (this.moldUuid != null && !this.moldUuid.equals(other.moldUuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.asset.TblAssetMatchingResultDetailPK[ assetId=" + assetId + ", moldUuid=" + moldUuid + " ]";
    }
}
