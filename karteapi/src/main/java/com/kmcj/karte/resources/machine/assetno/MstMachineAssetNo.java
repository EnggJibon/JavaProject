package com.kmcj.karte.resources.machine.assetno;

import com.kmcj.karte.resources.machine.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "mst_machine_asset_no")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMachineAssetNo.findAll", query = "SELECT m FROM MstMachineAssetNo m"),
    @NamedQuery(name = "MstMachineAssetNo.findById", query = "SELECT m FROM MstMachineAssetNo m WHERE m.id = :id"),
    @NamedQuery(name = "MstMachineAssetNo.deleteById", query = "DELETE FROM MstMachineAssetNo m WHERE m.id = :id"),
    @NamedQuery(name = "MstMachineAssetNo.findByPK", query = "SELECT m FROM MstMachineAssetNo m WHERE m.mstMachineAssetNoPK.assetNo = :assetNo and m.mstMachineAssetNoPK.machineUuid = :machineUuid "),
    @NamedQuery(name = "MstMachineAssetNo.findByMachineUuid", query = "SELECT m FROM MstMachineAssetNo m WHERE m.mstMachineAssetNoPK.machineUuid = :machineUuid"),
    @NamedQuery(name = "MstMachineAssetNo.findByAssetNo", query = "SELECT m FROM MstMachineAssetNo m WHERE m.mstMachineAssetNoPK.assetNo = :assetNo"),
    @NamedQuery(name = "MstMachineAssetNo.findByMainFlg", query = "SELECT m FROM MstMachineAssetNo m WHERE m.mainFlg = :mainFlg"),
    @NamedQuery(name = "MstMachineAssetNo.findByNumberedDate", query = "SELECT m FROM MstMachineAssetNo m WHERE m.numberedDate = :numberedDate"),
    @NamedQuery(name = "MstMachineAssetNo.findByAssetAmount", query = "SELECT m FROM MstMachineAssetNo m WHERE m.assetAmount = :assetAmount"),
    @NamedQuery(name = "MstMachineAssetNo.findByCreateDate", query = "SELECT m FROM MstMachineAssetNo m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMachineAssetNo.findByUpdateDate", query = "SELECT m FROM MstMachineAssetNo m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMachineAssetNo.findByCreateUserUuid", query = "SELECT m FROM MstMachineAssetNo m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMachineAssetNo.findByUpdateUserUuid", query = "SELECT m FROM MstMachineAssetNo m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstMachineAssetNo.updateByPK", query = "UPDATE MstMachineAssetNo m SET "
            + "m.mainFlg = :mainFlg, "
            + "m.assetAmount = :assetAmount,"
            + "m.numberedDate = :numberedDate,"
            + "m.updateDate = :updateDate,"
            + "m.updateUserUuid = :updateUserUuid  "
            + "WHERE m.mstMachineAssetNoPK.assetNo = :assetNo "
            + "and m.mstMachineAssetNoPK.machineUuid = :machineUuid")
})
@Cacheable(value = false)
public class MstMachineAssetNo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MstMachineAssetNoPK mstMachineAssetNoPK;
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
    @JoinColumn(name = "MACHINE_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachine mstMachine;

    public MstMachineAssetNo() {
    }

    public MstMachineAssetNo(MstMachineAssetNoPK mstMachineAssetNoPK) {
        this.mstMachineAssetNoPK = mstMachineAssetNoPK;
    }

    public MstMachineAssetNo(MstMachineAssetNoPK mstMachineAssetNoPK, String id, int mainFlg, BigDecimal assetAmount) {
        this.mstMachineAssetNoPK = mstMachineAssetNoPK;
        this.id = id;
        this.mainFlg = mainFlg;
        this.assetAmount = assetAmount;
    }

    public MstMachineAssetNo(String machineUuid, String assetNo) {
        this.mstMachineAssetNoPK = new MstMachineAssetNoPK(machineUuid, assetNo);
    }

    public MstMachineAssetNoPK getMstMachineAssetNoPK() {
        return mstMachineAssetNoPK;
    }

    public void setMstMachineAssetNoPK(MstMachineAssetNoPK mstMachineAssetNoPK) {
        this.mstMachineAssetNoPK = mstMachineAssetNoPK;
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

    public MstMachine getMstMachine() {
        return mstMachine;
    }

    public void setMstMachine(MstMachine mstMachine) {
        this.mstMachine = mstMachine;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mstMachineAssetNoPK != null ? mstMachineAssetNoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMachineAssetNo)) {
            return false;
        }
        MstMachineAssetNo other = (MstMachineAssetNo) object;
        if ((this.mstMachineAssetNoPK == null && other.mstMachineAssetNoPK != null) || (this.mstMachineAssetNoPK != null && !this.mstMachineAssetNoPK.equals(other.mstMachineAssetNoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.MstMachineAssetNo[ mstMachineAssetNoPK=" + mstMachineAssetNoPK + " ]";
    }
    
}
