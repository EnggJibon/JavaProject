/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.assetno;

import com.kmcj.karte.resources.mold.MstMold;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mst_mold_asset_no")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMoldAssetNo.findAll", query = "SELECT m FROM MstMoldAssetNo m"),
    @NamedQuery(name = "MstMoldAssetNo.findById", query = "SELECT m FROM MstMoldAssetNo m WHERE m.id = :id"),
    @NamedQuery(name = "MstMoldAssetNo.findByPK", query = "SELECT m FROM MstMoldAssetNo m WHERE m.mstMoldAssetNoPK.assetNo = :assetNo and m.mstMoldAssetNoPK.moldUuid = :moldUuid"),
    @NamedQuery(name = "MstMoldAssetNo.findByMoldUuid", query = "SELECT m FROM MstMoldAssetNo m WHERE m.mstMoldAssetNoPK.moldUuid = :moldUuid"),
    @NamedQuery(name = "MstMoldAssetNo.findByAssetNo", query = "SELECT m FROM MstMoldAssetNo m WHERE m.mstMoldAssetNoPK.assetNo = :assetNo"),
    @NamedQuery(name = "MstMoldAssetNo.findByMainFlg", query = "SELECT m FROM MstMoldAssetNo m WHERE m.mainFlg = :mainFlg"),
    @NamedQuery(name = "MstMoldAssetNo.findByNumberedDate", query = "SELECT m FROM MstMoldAssetNo m WHERE m.numberedDate = :numberedDate"),
    @NamedQuery(name = "MstMoldAssetNo.findByAssetAmount", query = "SELECT m FROM MstMoldAssetNo m WHERE m.assetAmount = :assetAmount"),
    @NamedQuery(name = "MstMoldAssetNo.findByCreateDate", query = "SELECT m FROM MstMoldAssetNo m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMoldAssetNo.findByUpdateDate", query = "SELECT m FROM MstMoldAssetNo m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMoldAssetNo.findByCreateUserUuid", query = "SELECT m FROM MstMoldAssetNo m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMoldAssetNo.findByUpdateUserUuid", query = "SELECT m FROM MstMoldAssetNo m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstMoldAssetNo.deleteByAssetNo", query = "DELETE FROM MstMoldAssetNo m WHERE m.mstMoldAssetNoPK.assetNo = :assetNo and m.mstMoldAssetNoPK.moldUuid = :moldUuid"),
    @NamedQuery(name = "MstMoldAssetNo.deleteByID", query = "DELETE FROM MstMoldAssetNo m WHERE m.id = :id"),
    @NamedQuery(name = "MstMoldAssetNo.updateByPK", query = "UPDATE MstMoldAssetNo m SET "
            + "m.mainFlg = :mainFlg, "
            + "m.assetAmount = :assetAmount,"
            + "m.numberedDate = :numberedDate,"
            + "m.updateDate = :updateDate,"
            + "m.updateUserUuid = :updateUserUuid  "
            + "WHERE m.mstMoldAssetNoPK.assetNo = :assetNo "
            + "and m.mstMoldAssetNoPK.moldUuid = :moldUuid"),
})

@Cacheable(value = false)
public class MstMoldAssetNo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MstMoldAssetNoPK mstMoldAssetNoPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MAIN_FLG")
    private int mainFlg;
    @Column(name = "NUMBERED_DATE")
    @Temporal(TemporalType.DATE)
    private Date numberedDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "ASSET_AMOUNT")
    private BigDecimal assetAmount;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Size(max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUuid;
    @Size(max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;
    @JoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(optional = false)//,cascade = CascadeType.REMOVE)
    private MstMold mstMold;
    

    public MstMoldAssetNo() {
    }

    public MstMoldAssetNo(MstMoldAssetNoPK mstMoldAssetNoPK) {
        this.mstMoldAssetNoPK = mstMoldAssetNoPK;
    }

    public MstMoldAssetNo(MstMoldAssetNoPK mstMoldAssetNoPK, String id, int mainFlg, BigDecimal assetAmount) {
        this.mstMoldAssetNoPK = mstMoldAssetNoPK;
        this.id = id;
        this.mainFlg = mainFlg;
        this.assetAmount = assetAmount;
    }

    public MstMoldAssetNo(String moldUuid, String assetNo) {
        this.mstMoldAssetNoPK = new MstMoldAssetNoPK(moldUuid, assetNo);
    }

    public MstMoldAssetNoPK getMstMoldAssetNoPK() {
        return mstMoldAssetNoPK;
    }

    public void setMstMoldAssetNoPK(MstMoldAssetNoPK mstMoldAssetNoPK) {
        this.mstMoldAssetNoPK = mstMoldAssetNoPK;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMainFlg() {
        return mainFlg;
    }

    public void setMainFlg(int mainFlg) {
        this.mainFlg = mainFlg;
    }

    public Date getNumberedDate() {
        return numberedDate;
    }

    public void setNumberedDate(Date numberedDate) {
        this.numberedDate = numberedDate;
    }

    public BigDecimal getAssetAmount() {
        return assetAmount;
    }

    public void setAssetAmount(BigDecimal assetAmount) {
        this.assetAmount = assetAmount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
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
        hash += (mstMoldAssetNoPK != null ? mstMoldAssetNoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMoldAssetNo)) {
            return false;
        }
        MstMoldAssetNo other = (MstMoldAssetNo) object;
        if ((this.mstMoldAssetNoPK == null && other.mstMoldAssetNoPK != null) || (this.mstMoldAssetNoPK != null && !this.mstMoldAssetNoPK.equals(other.mstMoldAssetNoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.assetno.MstMoldAssetNo[ mstMoldAssetNoPK=" + mstMoldAssetNoPK + " ]";
    }

}
