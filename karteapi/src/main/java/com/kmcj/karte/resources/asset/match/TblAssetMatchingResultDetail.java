/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.match;

import com.kmcj.karte.resources.mold.MstMold;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_asset_matching_result_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblAssetMatchingResultDetail.findAll", query = "SELECT t FROM TblAssetMatchingResultDetail t"),
    @NamedQuery(name = "TblAssetMatchingResultDetail.findByAssetId", query = "SELECT t FROM TblAssetMatchingResultDetail t WHERE t.tblAssetMatchingResultDetailPK.assetId = :assetId"),
    @NamedQuery(name = "TblAssetMatchingResultDetail.findByMoldUuid", query = "SELECT t FROM TblAssetMatchingResultDetail t WHERE t.tblAssetMatchingResultDetailPK.moldUuid = :moldUuid"),
    @NamedQuery(name = "TblAssetMatchingResultDetail.delete", query = "DELETE FROM TblAssetMatchingResultDetail")
})
@Cacheable(value = false)
public class TblAssetMatchingResultDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblAssetMatchingResultDetailPK tblAssetMatchingResultDetailPK;

    @PrimaryKeyJoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMold mstMold;

    @PrimaryKeyJoinColumn(name = "ASSET_ID", referencedColumnName = "ASSET_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblAssetMatchingResult tblAssetMatchingResult;

    public TblAssetMatchingResultDetail() {
    }

    public TblAssetMatchingResultDetail(TblAssetMatchingResultDetailPK tblAssetMatchingResultDetailPK) {
        this.tblAssetMatchingResultDetailPK = tblAssetMatchingResultDetailPK;
    }


    public TblAssetMatchingResultDetail(String assetId, String moldUuid) {
        this.tblAssetMatchingResultDetailPK = new TblAssetMatchingResultDetailPK(assetId, moldUuid);
    }

    public TblAssetMatchingResultDetailPK getTblAssetMatchingResultDetailPK() {
        return tblAssetMatchingResultDetailPK;
    }

    public void setTblAssetMatchingResultDetailPK(TblAssetMatchingResultDetailPK tblAssetMatchingResultDetailPK) {
        this.tblAssetMatchingResultDetailPK = tblAssetMatchingResultDetailPK;
    }

    public MstMold getMstMold() {
        return mstMold;
    }

    public void setMstMold(MstMold mstMold) {
        this.mstMold = mstMold;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblAssetMatchingResultDetailPK != null ? tblAssetMatchingResultDetailPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TblAssetMatchingResultDetail)) {
            return false;
        }
        TblAssetMatchingResultDetail other = (TblAssetMatchingResultDetail) object;
        if ((this.tblAssetMatchingResultDetailPK == null && other.tblAssetMatchingResultDetailPK != null) || (this.tblAssetMatchingResultDetailPK != null && !this.tblAssetMatchingResultDetailPK.equals(other.tblAssetMatchingResultDetailPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.asset.TblAssetMatchingResultDetail[ tblAssetMatchingResultDetailPK=" + tblAssetMatchingResultDetailPK + " ]";
    }

    /**
     * @return the tblAssetMatchingResult
     */
    public TblAssetMatchingResult getTblAssetMatchingResult() {
        return tblAssetMatchingResult;
    }

    /**
     * @param tblAssetMatchingResult the tblAssetMatchingResult to set
     */
    public void setTblAssetMatchingResult(TblAssetMatchingResult tblAssetMatchingResult) {
        this.tblAssetMatchingResult = tblAssetMatchingResult;
    }

}
