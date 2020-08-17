/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.company;

import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.inventory.TblMoldInventory;
import com.kmcj.karte.resources.mold.location.history.TblMoldLocationHistory;
import com.kmcj.karte.resources.apiuser.MstApiUser;
import com.kmcj.karte.resources.external.MstExternalDataGetSetting;
import com.kmcj.karte.resources.mgmt.company.MstMgmtCompany;
import com.kmcj.karte.resources.standard.worktime.MstStandardWorkTime;
import com.kmcj.karte.resources.user.MstUser;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hangju
 */
@Entity
@Table(name = "mst_company")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstCompany.findAll", query = "SELECT m FROM MstCompany m"),
    @NamedQuery(name = "MstCompany.findById", query = "SELECT m FROM MstCompany m WHERE m.id = :id"),
    @NamedQuery(name = "MstCompany.findByIdAndExternalFlg", query = "SELECT m FROM MstCompany m WHERE m.id = :id And m.externalFlg = :externalFlg "),
    //2016-11-30 jiangxiaosong Update start
    @NamedQuery(name = "MstCompany.findOnlyByCompanyCode", query = "SELECT m FROM MstCompany m WHERE m.companyCode = :companyCode And m.externalFlg = :externalFlg "),
    @NamedQuery(name = "MstCompany.findByCompanyCode", query = "SELECT m FROM MstCompany m WHERE m.companyCode = :companyCode And m.externalFlg = :externalFlg "),
    @NamedQuery(name = "MstCompany.findByCompanyName", query = "SELECT m FROM MstCompany m WHERE m.companyName = :companyName And m.externalFlg = :externalFlg "),
    //2016-11-30 jiangxiaosong Update end
    @NamedQuery(name = "MstCompany.findByZipCode", query = "SELECT m FROM MstCompany m WHERE m.zipCode = :zipCode"),
    @NamedQuery(name = "MstCompany.findByAddress", query = "SELECT m FROM MstCompany m WHERE m.address = :address"),
    @NamedQuery(name = "MstCompany.findByTelNo", query = "SELECT m FROM MstCompany m WHERE m.telNo = :telNo"),
    
    // 会社マスタの自社フラグ1（自社）は1レコードしか登録できない仕様としますので、登録時のチェック用
    @NamedQuery(name = "MstCompany.countMyCompany", query = "SELECT COUNT(1) FROM MstCompany m WHERE m.myCompany = :myCompany And m.externalFlg = :externalFlg And m.companyCode <> :companyCode"),
    
    @NamedQuery(name = "MstCompany.findByMyCompany", query = "SELECT m FROM MstCompany m WHERE m.myCompany = :myCompany And m.externalFlg = :externalFlg "),
    @NamedQuery(name = "MstCompany.findByCreateDate", query = "SELECT m FROM MstCompany m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstCompany.findByUpdateDate", query = "SELECT m FROM MstCompany m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstCompany.findByCreateUserUuid", query = "SELECT m FROM MstCompany m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstCompany.findByUpdateUserUuid", query = "SELECT m FROM MstCompany m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstCompany.findByExternalFlg", query = "SELECT m FROM MstCompany m WHERE m.externalFlg = :externalFlg"),
    @NamedQuery(name = "MstCompany.updateByCompanyCode", query = "UPDATE MstCompany m SET "
            + "m.companyName = :companyName,"
            + "m.zipCode = :zipCode, "
            + "m.address = :address, "
            + "m.telNo = :telNo,"
            + "m.myCompany = :myCompany,"
            + "m.updateDate = :updateDate,"
            + "m.mgmtCompanyCode = :mgmtCompanyCode,"
            + "m.updateUserUuid = :updateUserUuid  WHERE m.companyCode = :companyCode And m.externalFlg = :externalFlg"),
    @NamedQuery(name = "MstCompany.delete", query = "DELETE FROM MstCompany m WHERE m.companyCode = :companyCode And m.externalFlg = :externalFlg"),
    // Ite.5 Add S
    @NamedQuery(name = "MstCompany.findByMgmtCompanyCode", query = "SELECT m FROM MstCompany m WHERE m.mgmtCompanyCode = :mgmtCompanyCode")
    // Ite.5 Add E
})

@Cacheable(value = false)
public class MstCompany implements Serializable {

    @OneToMany(mappedBy = "mstCompany")
    private Collection<MstStandardWorkTime> mstStandardWorkTimeCollection;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }

    @OneToMany(mappedBy = "mstCompany")
    private Collection<MstUser> mstUserCollection;

    @OneToMany(mappedBy = "mstCompany")
    private Collection<MstExternalDataGetSetting> mstExternalDataGetSettingCollection;
    
    @OneToMany(mappedBy = "mstCompany")
    private Collection<MstApiUser> mstApiUserCollection;

    @OneToMany(mappedBy = "mstCompany")
    private Collection<TblMoldLocationHistory> tblMoldLocationHistoryCollection;

    @OneToMany(mappedBy = "mstCompany")
    private Collection<TblMoldInventory> tblMoldInventoryCollection;

    @OneToMany(mappedBy = "mstCompanyByCompanyId")
    private Collection<MstMold> mstMoldCollection;
    
    @OneToMany(mappedBy = "mstCompanyByOwnerCompanyId")
    private Collection<MstMold> mstMoldCollection1;

    @OneToMany(mappedBy = "mstCompany")
    private Collection<MstLocation> mstLocationCollection;

    private static long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COMPANY_CODE")
    private String companyCode;
    @Size(max = 100)
    @Column(name = "COMPANY_NAME")
    private String companyName;
    @Size(max = 15)
    @Column(name = "ZIP_CODE")
    private String zipCode;
    @Size(max = 100)
    @Column(name = "ADDRESS")
    private String address;
    @Size(max = 25)
    @Column(name = "TEL_NO")
    private String telNo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MY_COMPANY")
    private int myCompany;
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
    //2016-11-30 jiangxiaosong add start
    @Column(name = "EXTERNAL_FLG")
    private Integer externalFlg;
    //2016-11-30 jiangxiaosong add end
    
    // Ite.5 Add S
    @Column(name = "MGMT_COMPANY_CODE")
    private String mgmtCompanyCode;
    // Ite.5 Add E
    
    @Transient
    private boolean isError; //csvチェック　エラー有無　判定用
    
    /**
     * 管理先マスタ
     */
    @PrimaryKeyJoinColumn(name = "MGMT_COMPANY_CODE", referencedColumnName = "MGMT_COMPANY_CODE")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMgmtCompany mstMgmtCompany;
    
    public MstCompany() {
    }

    public MstCompany(String companyCode) {
        this.companyCode = companyCode;
    }

    public MstCompany(String companyCode, String id, int myCompany) {
        this.companyCode = companyCode;
        this.id = id;
        this.myCompany = myCompany;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public int getMyCompany() {
        return myCompany;
    }

    public void setMyCompany(int myCompany) {
        this.myCompany = myCompany;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getCompanyCode() != null ? getCompanyCode().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstCompany)) {
            return false;
        }
        MstCompany other = (MstCompany) object;
        if ((this.getCompanyCode() == null && other.getCompanyCode() != null) || (this.getCompanyCode() != null && !this.companyCode.equals(other.companyCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.company.MstCompany[ companyCode=" + getCompanyCode() + " ]";
    }

    @XmlTransient
    public Collection<MstLocation> getMstLocationCollection() {
        return mstLocationCollection;
    }

    public void setMstLocationCollection(Collection<MstLocation> mstLocationCollection) {
        this.mstLocationCollection = mstLocationCollection;
    }

    @XmlTransient
    public Collection<MstMold> getMstMoldCollection() {
        return mstMoldCollection;
    }

    public void setMstMoldCollection(Collection<MstMold> mstMoldCollection) {
        this.mstMoldCollection = mstMoldCollection;
    }

    @XmlTransient
    public Collection<MstMold> getMstMoldCollection1() {
        return mstMoldCollection1;
    }

    public void setMstMoldCollection1(Collection<MstMold> mstMoldCollection1) {
        this.mstMoldCollection1 = mstMoldCollection1;
    }

    @XmlTransient
    public Collection<TblMoldInventory> getTblMoldInventoryCollection() {
        return tblMoldInventoryCollection;
    }

    public void setTblMoldInventoryCollection(Collection<TblMoldInventory> tblMoldInventoryCollection) {
        this.tblMoldInventoryCollection = tblMoldInventoryCollection;
    }

    @XmlTransient
    public Collection<TblMoldLocationHistory> getTblMoldLocationHistoryCollection() {
        return tblMoldLocationHistoryCollection;
    }

    public void setTblMoldLocationHistoryCollection(Collection<TblMoldLocationHistory> tblMoldLocationHistoryCollection) {
        this.tblMoldLocationHistoryCollection = tblMoldLocationHistoryCollection;
    }

    @XmlTransient
    public Collection<MstExternalDataGetSetting> getMstExternalDataGetSettingCollection() {
        return mstExternalDataGetSettingCollection;
    }

    public void setMstExternalDataGetSettingCollection(Collection<MstExternalDataGetSetting> mstExternalDataGetSettingCollection) {
        this.mstExternalDataGetSettingCollection = mstExternalDataGetSettingCollection;
    }

    @XmlTransient
    public Collection<MstApiUser> getMstApiUserCollection() {
        return mstApiUserCollection;
    }

    public void setMstApiUserCollection(Collection<MstApiUser> mstApiUserCollection) {
        this.mstApiUserCollection = mstApiUserCollection;
    }

    @XmlTransient
    public Collection<MstUser> getMstUserCollection() {
        return mstUserCollection;
    }

    public void setMstUserCollection(Collection<MstUser> mstUserCollection) {
        this.mstUserCollection = mstUserCollection;
    }

    /**
     * @return the externalFlg
     */
    public Integer getExternalFlg() {
        return externalFlg;
    }

    /**
     * @param externalFlg the externalFlg to set
     */
    public void setExternalFlg(Integer externalFlg) {
        this.externalFlg = externalFlg;
    }

    @XmlTransient
    public Collection<MstStandardWorkTime> getMstStandardWorkTimeCollection() {
        return mstStandardWorkTimeCollection;
    }

    public void setMstStandardWorkTimeCollection(Collection<MstStandardWorkTime> mstStandardWorkTimeCollection) {
        this.mstStandardWorkTimeCollection = mstStandardWorkTimeCollection;
    }

    /**
     * @return the isError
     */
    public boolean isError() {
        return isError;
    }

    /**
     * @param isError the isError to set
     */
    public void setIsError(boolean isError) {
        this.isError = isError;
    }

    /**
     * @return the mgmtCompanyCode
     */
    public String getMgmtCompanyCode() {
        return mgmtCompanyCode;
    }

    /**
     * @param mgmtCompanyCode the mgmtCompanyCode to set
     */
    public void setMgmtCompanyCode(String mgmtCompanyCode) {
        this.mgmtCompanyCode = mgmtCompanyCode;
    }

    /**
     * @return the mstMgmtCompany
     */
    public MstMgmtCompany getMstMgmtCompany() {
        return mstMgmtCompany;
    }

    /**
     * @param mstMgmtCompany the mstMgmtCompany to set
     */
    public void setMstMgmtCompany(MstMgmtCompany mstMgmtCompany) {
        this.mstMgmtCompany = mstMgmtCompany;
    }
    
    
}
