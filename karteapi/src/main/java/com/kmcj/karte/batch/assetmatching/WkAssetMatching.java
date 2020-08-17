/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.assetmatching;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author zds
 */
@Entity
@Table(name = "wk_asset_matching")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WkAssetMatching.findByItemCode", query = "SELECT w FROM WkAssetMatching w WHERE w.itemCode = :itemCode"),
    @NamedQuery(name = "WkAssetMatching.delete", query = "DELETE FROM WkAssetMatching")
})
@Cacheable(value = false)
public class WkAssetMatching implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ASSET_ID")
    private String assetId;
    @Size(max = 100)
    @Column(name = "ITEM_CODE")
    private String itemCode;
    
    @PrimaryKeyJoinColumn(name = "ITEM_CODE", referencedColumnName = "COMPONENT_CODE")
    @ManyToOne(fetch = FetchType.LAZY)
    private WkAssetMatchingComponent wkAssetMatchingComponent;

    public WkAssetMatching() {
    }

    public WkAssetMatching(String assetId) {
        this.assetId = assetId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (assetId != null ? assetId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof WkAssetMatching)) {
            return false;
        }
        WkAssetMatching other = (WkAssetMatching) object;
        if ((this.assetId == null && other.assetId != null) || (this.assetId != null && !this.assetId.equals(other.assetId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.batch.assetmatching.WkAssetMatching[ assetId=" + assetId + " ]";
    }

    /**
     * @return the wkAssetMatchingComponent
     */
    public WkAssetMatchingComponent getWkAssetMatchingComponent() {
        return wkAssetMatchingComponent;
    }

    /**
     * @param wkAssetMatchingComponent the wkAssetMatchingComponent to set
     */
    public void setWkAssetMatchingComponent(WkAssetMatchingComponent wkAssetMatchingComponent) {
        this.wkAssetMatchingComponent = wkAssetMatchingComponent;
    }
    
}
