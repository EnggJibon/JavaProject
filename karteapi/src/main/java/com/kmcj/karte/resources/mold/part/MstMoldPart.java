/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part;

import com.kmcj.karte.resources.user.MstUser;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "mst_mold_part")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMoldPart.findAll", query = "SELECT m FROM MstMoldPart m"),
    @NamedQuery(name = "MstMoldPart.findById", query = "SELECT m FROM MstMoldPart m WHERE m.id = :id"),
    //
    @NamedQuery(name = "MstMoldPart.findOnlyByMoldPartCode", query = "SELECT m FROM MstMoldPart m WHERE m.moldPartCode = :moldPartCode"),
    @NamedQuery(name = "MstMoldPart.findByMoldPartCode", query = "SELECT m FROM MstMoldPart m WHERE m.moldPartCode = :moldPartCode"),
    @NamedQuery(name = "MstMoldPart.findByMoldPartName", query = "SELECT m FROM MstMoldPart m WHERE m.moldPartName = :moldPartName"),
    //
    @NamedQuery(name = "MstMoldPart.findByManufacturer", query = "SELECT m FROM MstMoldPart m WHERE m.manufacturer = :manufacturer"),
    @NamedQuery(name = "MstMoldPart.findByModelNumber", query = "SELECT m FROM MstMoldPart m WHERE m.modelNumber = :modelNumber"),
    @NamedQuery(name = "MstMoldPart.findByMemo", query = "SELECT m FROM MstMoldPart m WHERE m.memo = :memo"),  
    //
    @NamedQuery(name = "MstMoldPart.findByCreateDate", query = "SELECT m FROM MstMoldPart m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMoldPart.findByUpdateDate", query = "SELECT m FROM MstMoldPart m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMoldPart.findByCreateUserUuid", query = "SELECT m FROM MstMoldPart m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMoldPart.findByUpdateUserUuid", query = "SELECT m FROM MstMoldPart m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstMoldPart.updateByMoldPartCode", query = "UPDATE MstMoldPart m SET "
            + "m.moldPartName = :moldPartName,"
            + "m.manufacturer = :manufacturer, "
            + "m.modelNumber = :modelNumber, "
            + "m.memo = :memo,"
            + "m.unitPrice = :unitPrice,"
            + "m.usedUnitPrice = :usedUnitPrice,"
            + "m.intrExtProduct = :intrExtProduct,"
            + "m.recyclableFlg = :recyclableFlg,"
            + "m.updateDate = :updateDate,"
            + "m.updateUserUuid = :updateUserUuid  WHERE m.moldPartCode = :moldPartCode"),
    @NamedQuery(name = "MstMoldPart.deleteByCode", query = "DELETE FROM MstMoldPart m WHERE m.moldPartCode = :moldPartCode"),
    @NamedQuery(name = "MstMoldPart.deleteById", query = "DELETE FROM MstMoldPart m WHERE m.id = :id")
})

@Cacheable(value = false)
public class MstMoldPart implements Serializable {
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

    private static long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "MOLD_PART_CODE")
    private String moldPartCode;
    @Size(max = 100)
    @Column(name = "MOLD_PART_NAME")
    private String moldPartName;
    @Size(max = 100)
    @Column(name = "MANUFACTURER")
    private String manufacturer;
    @Size(max = 100)
    @Column(name = "MODEL_NUMBER")
    private String modelNumber;
    @Size(max = 200)
    @Column(name = "MEMO")
    private String memo;
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
    
    @Column(name = "UNIT_PRICE")
    private BigDecimal unitPrice = BigDecimal.ZERO;
    
    @Column(name = "USED_UNIT_PRICE")
    private BigDecimal usedUnitPrice = BigDecimal.ZERO;
    
    @Column(name = "INTR_EXT_PRODUCT")
    private Integer intrExtProduct;
    
    @Column(name = "RECYCLABLE_FLG")
    private Integer recyclableFlg;
    
    @Transient
    private boolean isError; //csvチェック　エラー有無　判定用
    
    
    // ユーザーマスタ
    @PrimaryKeyJoinColumn(name = "CREATE_USER_UUID", referencedColumnName = "UUID")
    //@ManyToOne(optional = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUser mstCreateUser;
    
    // ユーザーマスタ
    @PrimaryKeyJoinColumn(name = "UPDATE_USER_UUID", referencedColumnName = "UUID")
    //@ManyToOne(optional = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUser mstUpdateUser;
    
     public MstMoldPart() {
    }

    public MstMoldPart(String moldPartCode) {
        this.moldPartCode = moldPartCode;
    }

    public MstMoldPart(String moldPartCode, String id) {
        this.moldPartCode = moldPartCode;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoldPartCode() {
        return moldPartCode;
    }

    public void setMoldPartCode(String moldPartCode) {
        this.moldPartCode = moldPartCode;
    }

    public String getMoldPartName() {
        return moldPartName;
    }

    public void setMoldPartName(String moldPartName) {
        this.moldPartName = moldPartName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUsedUnitPrice() {
        return usedUnitPrice;
    }

    public void setUsedUnitPrice(BigDecimal usedUnitPrice) {
        this.usedUnitPrice = usedUnitPrice;
    }

    public Integer getIntrExtProduct() {
        return intrExtProduct;
    }

    public void setIntrExtProduct(Integer intrExtProduct) {
        this.intrExtProduct = intrExtProduct;
    }

    public Integer getRecyclableFlg() {
        return recyclableFlg;
    }

    public void setRecyclableFlg(Integer recyclableFlg) {
        this.recyclableFlg = recyclableFlg;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getMoldPartCode() != null ? getMoldPartCode().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMoldPart)) {
            return false;
        }
        MstMoldPart other = (MstMoldPart) object;
        if ((this.getMoldPartCode() == null && other.getMoldPartCode() != null) || (this.getMoldPartCode() != null && !this.moldPartCode.equals(other.moldPartCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.part.MstMoldPart[ moldPartCode=" + getMoldPartCode() + " ]";
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
     * @return the mstCreateUser
     */
    public MstUser getMstCreateUser() {
        return mstCreateUser;
    }

    /**
     * @param mstCreateUser the mstCreateUser to set
     */
    public void setMstCreateUser(MstUser mstCreateUser) {
        this.mstCreateUser = mstCreateUser;
    }

    /**
     * @return the mstUpdateUser
     */
    public MstUser getMstUpdateUser() {
        return mstUpdateUser;
    }

    /**
     * @param mstUpdateUser the mstUpdateUser to set
     */
    public void setMstUpdateUser(MstUser mstUpdateUser) {
        this.mstUpdateUser = mstUpdateUser;
    }
}
