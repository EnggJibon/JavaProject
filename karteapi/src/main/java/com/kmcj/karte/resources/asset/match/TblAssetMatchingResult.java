/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.match;

import com.kmcj.karte.resources.asset.MstAsset;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_asset_matching_result")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblAssetMatchingResult.findAll", query = "SELECT t FROM TblAssetMatchingResult t"),
    @NamedQuery(name = "TblAssetMatchingResult.findByAssetId", query = "SELECT t FROM TblAssetMatchingResult t WHERE t.assetId = :assetId"),
    @NamedQuery(name = "TblAssetMatchingResult.delete", query = "DELETE FROM TblAssetMatchingResult "),
    @NamedQuery(name = "TblAssetMatchingResult.findByMatchingResult", query = "SELECT t FROM TblAssetMatchingResult t WHERE t.matchingResult = :matchingResult"),
    @NamedQuery(name = "TblAssetMatchingResult.findByBatchId", query = "SELECT t FROM TblAssetMatchingResult t WHERE t.batchId = :batchId")
})
@Cacheable(value = false)
public class TblAssetMatchingResult implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ASSET_ID")
    private String assetId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MATCHING_RESULT")
    private int matchingResult;
    @Size(max = 45)
    @Column(name = "BATCH_ID")
    private String batchId;
    
    @PrimaryKeyJoinColumn(name = "ASSET_ID", referencedColumnName = "UUID")
    @OneToOne(fetch = FetchType.LAZY)
    private MstAsset mstAsset;
    
    @OneToMany(mappedBy = "tblAssetMatchingResult")
    private Collection<TblAssetMatchingResultDetail> tblAssetMatchingResultDetailCollection;
    
    @PrimaryKeyJoinColumn(name = "BATCH_ID", referencedColumnName = "ID")
    @OneToOne(fetch = FetchType.LAZY)
    private TblAssetMatchingBatch tblAssetMatchingBatch;

    public TblAssetMatchingResult() {
    }

    public TblAssetMatchingResult(String assetId) {
        this.assetId = assetId;
    }

    public TblAssetMatchingResult(String assetId, int matchingResult) {
        this.assetId = assetId;
        this.matchingResult = matchingResult;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public int getMatchingResult() {
        return matchingResult;
    }

    public void setMatchingResult(int matchingResult) {
        this.matchingResult = matchingResult;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public MstAsset getMstAsset() {
        return mstAsset;
    }

    public void setMstAsset(MstAsset mstAsset) {
        this.mstAsset = mstAsset;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (assetId != null ? assetId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblAssetMatchingResult)) {
            return false;
        }
        TblAssetMatchingResult other = (TblAssetMatchingResult) object;
        if ((this.assetId == null && other.assetId != null) || (this.assetId != null && !this.assetId.equals(other.assetId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.asset.TblAssetMatchingResult[ assetId=" + assetId + " ]";
    }

    /**
     * @return the tblAssetMatchingBatch
     */
    public TblAssetMatchingBatch getTblAssetMatchingBatch() {
        return tblAssetMatchingBatch;
    }

    /**
     * @param tblAssetMatchingBatch the tblAssetMatchingBatch to set
     */
    public void setTblAssetMatchingBatch(TblAssetMatchingBatch tblAssetMatchingBatch) {
        this.tblAssetMatchingBatch = tblAssetMatchingBatch;
    }

    /**
     * @return the tblAssetMatchingResultDetailCollection
     */
    @XmlTransient
    public Collection<TblAssetMatchingResultDetail> getTblAssetMatchingResultDetailCollection() {
        return tblAssetMatchingResultDetailCollection;
    }

    /**
     * @param tblAssetMatchingResultDetailCollection the
     * tblAssetMatchingResultDetailCollection to set
     */
    public void setTblAssetMatchingResultDetailCollection(Collection<TblAssetMatchingResultDetail> tblAssetMatchingResultDetailCollection) {
        this.tblAssetMatchingResultDetailCollection = tblAssetMatchingResultDetailCollection;
    }
}
