/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component;

import com.kmcj.karte.resources.component.material.MstComponentMaterial;
import com.kmcj.karte.resources.component.spec.MstComponentSpec;
import com.kmcj.karte.resources.direction.TblDirection;
import com.kmcj.karte.resources.mold.issue.TblIssue;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelation;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.production.plan.TblProductionPlan;
import com.kmcj.karte.resources.user.MstUser;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
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
 * @author admin
 */
@Entity
@Table(name = "mst_component")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstComponent.findAll", query = "SELECT m FROM MstComponent m"),
    @NamedQuery(name = "MstComponent.findById", query = "SELECT m FROM MstComponent m WHERE m.id = :id"),
    @NamedQuery(name = "MstComponent.findByComponentCode", query = "SELECT m FROM MstComponent m WHERE m.componentCode = :componentCode"),
    @NamedQuery(name = "MstComponent.findByComponentName", query = "SELECT m FROM MstComponent m WHERE m.componentName = :componentName"),
    @NamedQuery(name = "MstComponent.findByComponentType", query = "SELECT m FROM MstComponent m WHERE m.componentType = :componentType"),
    @NamedQuery(name = "MstComponent.findByImgFilePath01", query = "SELECT m FROM MstComponent m WHERE m.imgFilePath01 = :imgFilePath01"),
    @NamedQuery(name = "MstComponent.findByImgFilePath02", query = "SELECT m FROM MstComponent m WHERE m.imgFilePath02 = :imgFilePath02"),
    @NamedQuery(name = "MstComponent.findByImgFilePath03", query = "SELECT m FROM MstComponent m WHERE m.imgFilePath03 = :imgFilePath03"),
    @NamedQuery(name = "MstComponent.findByImgFilePath04", query = "SELECT m FROM MstComponent m WHERE m.imgFilePath04 = :imgFilePath04"),
    @NamedQuery(name = "MstComponent.findByImgFilePath05", query = "SELECT m FROM MstComponent m WHERE m.imgFilePath05 = :imgFilePath05"),
    @NamedQuery(name = "MstComponent.findByImgFilePath06", query = "SELECT m FROM MstComponent m WHERE m.imgFilePath06 = :imgFilePath06"),
    @NamedQuery(name = "MstComponent.findByImgFilePath07", query = "SELECT m FROM MstComponent m WHERE m.imgFilePath07 = :imgFilePath07"),
    @NamedQuery(name = "MstComponent.findByImgFilePath08", query = "SELECT m FROM MstComponent m WHERE m.imgFilePath08 = :imgFilePath08"),
    @NamedQuery(name = "MstComponent.findByImgFilePath09", query = "SELECT m FROM MstComponent m WHERE m.imgFilePath09 = :imgFilePath09"),
    @NamedQuery(name = "MstComponent.findByImgFilePath10", query = "SELECT m FROM MstComponent m WHERE m.imgFilePath10 = :imgFilePath10"),
    @NamedQuery(name = "MstComponent.findByIsCircuitBoard", query = "SELECT m FROM MstComponent m WHERE m.isCircuitBoard = :isCircuitBoard"),
    @NamedQuery(name = "MstComponent.findByIsCircuitBoardAuto", query = "SELECT m FROM MstComponent m WHERE m.componentCode like :componentCode and m.isCircuitBoard = :isCircuitBoard"),
    @NamedQuery(name = "MstComponent.findByIsCircuitBoardSerialNumber", query = "SELECT m FROM MstComponent m WHERE locate(m.snFixedValue, :serialNumber) = 1 and m.isCircuitBoard = :isCircuitBoard and length(m.snFixedValue) > 0 "  ),
    @NamedQuery(name = "MstComponent.findByIsCircuitBoardCode", query = "SELECT m FROM MstComponent m WHERE m.componentCode like :componentCode and m.isCircuitBoard = :isCircuitBoard"  ),
    @NamedQuery(name = "MstComponent.findByCreateDate", query = "SELECT m FROM MstComponent m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstComponent.findByUpdateDate", query = "SELECT m FROM MstComponent m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstComponent.findByCreateUserUuid", query = "SELECT m FROM MstComponent m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstComponent.findByUpdateUserUuid", query = "SELECT m FROM MstComponent m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstComponent.findByComponentCodeList", query = "SELECT m FROM MstComponent m WHERE m.componentCode IN :componentCode"),
    @NamedQuery(name = "MstComponent.updateByComponentCode", query = "UPDATE MstComponent m SET "
            + "m.componentCode = :componentCode,"
            + "m.componentName = :componentName, "
            + "m.componentType = :componentType, "
            + "m.imgFilePath01 = :imgFilePath01, "
            + "m.imgFilePath02 = :imgFilePath02, "
            + "m.imgFilePath03 = :imgFilePath03, "
            + "m.imgFilePath04 = :imgFilePath04, "
            + "m.imgFilePath05 = :imgFilePath05, "
            + "m.imgFilePath06 = :imgFilePath06, "
            + "m.imgFilePath07 = :imgFilePath07, "
            + "m.imgFilePath08 = :imgFilePath08, "
            + "m.imgFilePath09 = :imgFilePath09, "
            + "m.imgFilePath10 = :imgFilePath10, "
            + "m.isCircuitBoard = :isCircuitBoard, "
            // 20171101 在庫により追加 S
            + "m.unitPrice = :unitPrice, "
            + "m.currencyCode = :currencyCode, "
            + "m.stockUnit = :stockUnit, "
            + "m.isPurchasedPart = :isPurchasedPart, "
            // 20171101 在庫により追加 E
            // 20171206 基板により追加 S
            + "m.snLength = :snLength, "
            + "m.snFixedValue = :snFixedValue, "
            + "m.snFixedLength = :snFixedLength, "
            // 20171206 基板により追加 E
            + "m.updateDate = :updateDate, "
            + "m.updateUserUuid = :updateUserUuid"
            + " WHERE m.componentCode = :componentCode"),
    @NamedQuery(name = "MstComponent.updateByComponentCodeRegistration", query = "UPDATE MstComponent m SET "
            + "m.componentCode = :componentCode,"
            + "m.componentName = :componentName, "
            + "m.componentType = :componentType, "
            + "m.isCircuitBoard = :isCircuitBoard, "
            + "m.unitPrice = :unitPrice, "
            + "m.currencyCode = :currencyCode, "
            + "m.stockUnit = :stockUnit, "
            + "m.isPurchasedPart = :isPurchasedPart, "
            + "m.snLength = :snLength, "
            + "m.snFixedValue = :snFixedValue, "
            + "m.snFixedLength = :snFixedLength, "
            + "m.updateDate = :updateDate, "
            + "m.updateUserUuid = :updateUserUuid"
            + " WHERE m.componentCode = :componentCode"),
    @NamedQuery(name = "MstComponent.updateByComponentCode1", query = "UPDATE MstComponent m SET "
            + "m.componentCode = :componentCode,"
            + "m.componentName = :componentName, "
            + "m.componentType = :componentType, "
            + "m.isCircuitBoard = :isCircuitBoard, "
            + "m.snLength = :snLength, "
            + "m.snFixedValue = :snFixedValue, "
            + "m.snFixedLength = :snFixedLength, "
            + "m.updateDate = :updateDate, "
            + "m.updateUserUuid = :updateUserUuid"
            + " WHERE m.componentCode = :componentCode"),

    @NamedQuery(name = "MstComponent.delete", query = "DELETE FROM MstComponent m WHERE m.componentCode = :componentCode")
})
@Cacheable(value = false)
public class MstComponent implements Serializable {

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

    @OneToMany(mappedBy = "mstComponent")
    private Collection<TblProductionPlan> tblProductionPlanCollection;
    
    @OneToMany(mappedBy = "mstComponent")
    private Collection<TblDirection> tblDirectionCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mstComponent")
    private Collection<MstProcedure> mstProcedureCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mstComponent")
    private Collection<MstComponentMaterial> mstComponentMaterialCollection;
    
    @OneToMany(mappedBy = "mstComponent")
    private Collection<TblIssue> tblIssueCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mstComponent")
    private Collection<MstMoldComponentRelation> mstMoldComponentRelationCollection;

    @OneToMany(cascade = {CascadeType.REMOVE,CascadeType.PERSIST}, mappedBy = "mstComponent")
    private Collection<MstComponentSpec> mstComponentSpecCollection;

    private static long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COMPONENT_CODE")
    private String componentCode;
    @Size(max = 100)
    @Column(name = "COMPONENT_NAME")
    private String componentName;
    @Column(name = "COMPONENT_TYPE")
    private Integer componentType;
    @Size(max = 256)
    @Column(name = "IMG_FILE_PATH01")
    private String imgFilePath01;
    @Size(max = 256)
    @Column(name = "IMG_FILE_PATH02")
    private String imgFilePath02;
    @Size(max = 256)
    @Column(name = "IMG_FILE_PATH03")
    private String imgFilePath03;
    @Size(max = 256)
    @Column(name = "IMG_FILE_PATH04")
    private String imgFilePath04;
    @Size(max = 256)
    @Column(name = "IMG_FILE_PATH05")
    private String imgFilePath05;
    @Size(max = 256)
    @Column(name = "IMG_FILE_PATH06")
    private String imgFilePath06;
    @Size(max = 256)
    @Column(name = "IMG_FILE_PATH07")
    private String imgFilePath07;
    @Size(max = 256)
    @Column(name = "IMG_FILE_PATH08")
    private String imgFilePath08;
    @Size(max = 256)
    @Column(name = "IMG_FILE_PATH09")
    private String imgFilePath09;
    @Size(max = 256)
    @Column(name = "IMG_FILE_PATH10")
    private String imgFilePath10;
    @Column(name = "IS_CIRCUIT_BOARD")
    private Integer isCircuitBoard;
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
    private BigDecimal unitPrice;

    @Column(name = "CURRENCY_CODE")
    private String currencyCode;

    @Column(name = "STOCK_UNIT")
    private int stockUnit;
    
    @Column(name = "SN_LENGTH")
    private int snLength;

    @Size(max = 45)
    @Column(name = "SN_FIXED_VALUE")
    private String snFixedValue;

    @Column(name = "SN_FIXED_LENGTH")
    private int snFixedLength;
    
    // 在庫STEP2により追加 20171218 S
    @Column(name = "IS_PURCHASED_PART")
    private int isPurchasedPart;//購入部品フラグ
    // 在庫STEP2により追加 20171218 E

    /**
     * 結合テーブル定義
     */
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
    
    @Transient
    private Integer countPerShot;
    @Transient
    private String procedureId;
    @Transient
    private String procedureCode;
    @Transient
    private String procedureName;
    
    public MstComponent() {
    }

    public MstComponent(String componentCode) {
        this.componentCode = componentCode;
    }

    public MstComponent(String componentCode, String id) {
        this.componentCode = componentCode;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public Integer getComponentType() {
        return componentType;
    }

    public void setComponentType(Integer componentType) {
        this.componentType = componentType;
    }

    public String getImgFilePath01() {
        return imgFilePath01;
    }

    public void setImgFilePath01(String imgFilePath01) {
        this.imgFilePath01 = imgFilePath01;
    }

    public String getImgFilePath02() {
        return imgFilePath02;
    }

    public void setImgFilePath02(String imgFilePath02) {
        this.imgFilePath02 = imgFilePath02;
    }

    public String getImgFilePath03() {
        return imgFilePath03;
    }

    public void setImgFilePath03(String imgFilePath03) {
        this.imgFilePath03 = imgFilePath03;
    }

    public String getImgFilePath04() {
        return imgFilePath04;
    }

    public void setImgFilePath04(String imgFilePath04) {
        this.imgFilePath04 = imgFilePath04;
    }

    public String getImgFilePath05() {
        return imgFilePath05;
    }

    public void setImgFilePath05(String imgFilePath05) {
        this.imgFilePath05 = imgFilePath05;
    }

    public String getImgFilePath06() {
        return imgFilePath06;
    }

    public void setImgFilePath06(String imgFilePath06) {
        this.imgFilePath06 = imgFilePath06;
    }

    public String getImgFilePath07() {
        return imgFilePath07;
    }

    public void setImgFilePath07(String imgFilePath07) {
        this.imgFilePath07 = imgFilePath07;
    }

    public String getImgFilePath08() {
        return imgFilePath08;
    }

    public void setImgFilePath08(String imgFilePath08) {
        this.imgFilePath08 = imgFilePath08;
    }

    public String getImgFilePath09() {
        return imgFilePath09;
    }

    public void setImgFilePath09(String imgFilePath09) {
        this.imgFilePath09 = imgFilePath09;
    }

    public String getImgFilePath10() {
        return imgFilePath10;
    }

    public void setImgFilePath10(String imgFilePath10) {
        this.imgFilePath10 = imgFilePath10;
    }

    public Integer getIsCircuitBoard() {
        return isCircuitBoard;
    }

    public void setIsCircuitBoard(Integer isCircuitBoard) {
        this.isCircuitBoard = isCircuitBoard;
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
        hash += (getComponentCode() != null ? getComponentCode().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstComponent)) {
            return false;
        }
        MstComponent other = (MstComponent) object;
        if ((this.getComponentCode() == null && other.getComponentCode() != null) || (this.getComponentCode() != null && !this.componentCode.equals(other.componentCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.component.MstComponent[ componentCode=" + getComponentCode() + " ]";
    }

    @XmlTransient
    public Collection<MstComponentSpec> getMstComponentSpecCollection() {
        return mstComponentSpecCollection;
    }

    public void setMstComponentSpecCollection(Collection<MstComponentSpec> mstComponentSpecCollection) {
        this.mstComponentSpecCollection = mstComponentSpecCollection;
    }

    @XmlTransient
    public Collection<MstMoldComponentRelation> getMstMoldComponentRelationCollection() {
        return mstMoldComponentRelationCollection;
    }

    public void setMstMoldComponentRelationCollection(Collection<MstMoldComponentRelation> mstMoldComponentRelationCollection) {
        this.mstMoldComponentRelationCollection = mstMoldComponentRelationCollection;
    }

    @XmlTransient
    public Collection<TblIssue> getTblIssueCollection() {
        return tblIssueCollection;
    }

    public void setTblIssueCollection(Collection<TblIssue> tblIssueCollection) {
        this.tblIssueCollection = tblIssueCollection;
    }

    /**
     * @return the countPerShot
     */
    public Integer getCountPerShot() {
        return countPerShot;
    }

    /**
     * @param countPerShot the countPerShot to set
     */
    public void setCountPerShot(Integer countPerShot) {
        this.countPerShot = countPerShot;
    }
    
    /**
     * @return the procedureId
     */
    public String getProcedureId() {
        return procedureId;
    }

    /**
     * @param procedureId the procedureId to set
     */
    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    @XmlTransient
    public Collection<MstComponentMaterial> getMstComponentMaterialCollection() {
        return mstComponentMaterialCollection;
    }

    public void setMstComponentMaterialCollection(Collection<MstComponentMaterial> mstComponentMaterialCollection) {
        this.mstComponentMaterialCollection = mstComponentMaterialCollection;
    }

    @XmlTransient
    public Collection<MstProcedure> getMstProcedureCollection() {
        return mstProcedureCollection;
    }

    public void setMstProcedureCollection(Collection<MstProcedure> mstProcedureCollection) {
        this.mstProcedureCollection = mstProcedureCollection;
    }

    @XmlTransient
    public Collection<TblDirection> getTblDirectionCollection() {
        return tblDirectionCollection;
    }

    public void setTblDirectionCollection(Collection<TblDirection> tblDirectionCollection) {
        this.tblDirectionCollection = tblDirectionCollection;
    }

    @XmlTransient
    public Collection<TblProductionPlan> getTblProductionPlanCollection() {
        return tblProductionPlanCollection;
    }

    public void setTblProductionPlanCollection(Collection<TblProductionPlan> tblProductionPlanCollection) {
        this.tblProductionPlanCollection = tblProductionPlanCollection;
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

    /**
     * @return the unitPrice
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice the unitPrice to set
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * @return the currencyCode
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * @param currencyCode the currencyCode to set
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * @return the stockUnit
     */
    public int getStockUnit() {
        return stockUnit;
    }

    /**
     * @param stockUnit the stockUnit to set
     */
    public void setStockUnit(int stockUnit) {
        this.stockUnit = stockUnit;
    }

    /**
     * @return the snLength
     */
    public int getSnLength() {
        return snLength;
    }

    /**
     * @param snLength the snLength to set
     */
    public void setSnLength(int snLength) {
        this.snLength = snLength;
    }

    /**
     * @return the snFixedValue
     */
    public String getSnFixedValue() {
        return snFixedValue;
    }

    /**
     * @param snFixedValue the snFixedValue to set
     */
    public void setSnFixedValue(String snFixedValue) {
        this.snFixedValue = snFixedValue;
    }

    /**
     * @return the snFixedLength
     */
    public int getSnFixedLength() {
        return snFixedLength;
    }

    /**
     * @param snFixedLength the snFixedLength to set
     */
    public void setSnFixedLength(int snFixedLength) {
        this.snFixedLength = snFixedLength;
    }

    /**
     * @return the isPurchasedPart
     */
    public int getIsPurchasedPart() {
        return isPurchasedPart;
    }

    /**
     * @param isPurchasedPart the isPurchasedPart to set
     */
    public void setIsPurchasedPart(int isPurchasedPart) {
        this.isPurchasedPart = isPurchasedPart;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

}
