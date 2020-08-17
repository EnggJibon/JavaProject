/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.detail;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationVoList;
import com.kmcj.karte.resources.material.MstMaterial;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.production.TblProduction;
import com.kmcj.karte.resources.production.lot.balance.TblProductionLotBalance;
import com.kmcj.karte.resources.production.machine.proc.cond.TblProductionMachineProcCond;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
 * TblProductionDetailエンティティ
 * @author t.ariki
 */
@Entity
@Table(name = "tbl_production_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblProductionDetail.findAll", query = "SELECT t FROM TblProductionDetail t"),
    @NamedQuery(name = "TblProductionDetail.findById", query = "SELECT t FROM TblProductionDetail t WHERE t.id = :id"),
    @NamedQuery(name = "TblProductionDetail.findByProductionId", query = "SELECT t FROM TblProductionDetail t WHERE t.productionId = :productionId"),
    @NamedQuery(name = "TblProductionDetail.findByComponentId", query = "SELECT t FROM TblProductionDetail t WHERE t.componentId = :componentId"),
    @NamedQuery(name = "TblProductionDetail.findByProcedureId", query = "SELECT t FROM TblProductionDetail t WHERE t.procedureId = :procedureId"),
    @NamedQuery(name = "TblProductionDetail.findByCountPerShot", query = "SELECT t FROM TblProductionDetail t WHERE t.countPerShot = :countPerShot"),
    @NamedQuery(name = "TblProductionDetail.findByDefectCount", query = "SELECT t FROM TblProductionDetail t WHERE t.defectCount = :defectCount"),
    @NamedQuery(name = "TblProductionDetail.findByCompleteCount", query = "SELECT t FROM TblProductionDetail t WHERE t.completeCount = :completeCount"),
    @NamedQuery(name = "TblProductionDetail.findByMaterial01Id", query = "SELECT t FROM TblProductionDetail t WHERE t.material01Id = :material01Id"),
    @NamedQuery(name = "TblProductionDetail.findByMaterial01LotNo", query = "SELECT t FROM TblProductionDetail t WHERE t.material01LotNo = :material01LotNo"),
    @NamedQuery(name = "TblProductionDetail.findByMaterial01Amount", query = "SELECT t FROM TblProductionDetail t WHERE t.material01Amount = :material01Amount"),
    @NamedQuery(name = "TblProductionDetail.findByMaterial01PurgedAmount", query = "SELECT t FROM TblProductionDetail t WHERE t.material01PurgedAmount = :material01PurgedAmount"),
    @NamedQuery(name = "TblProductionDetail.findByMaterial02Id", query = "SELECT t FROM TblProductionDetail t WHERE t.material02Id = :material02Id"),
    @NamedQuery(name = "TblProductionDetail.findByMaterial02LotNo", query = "SELECT t FROM TblProductionDetail t WHERE t.material02LotNo = :material02LotNo"),
    @NamedQuery(name = "TblProductionDetail.findByMaterial02Amount", query = "SELECT t FROM TblProductionDetail t WHERE t.material02Amount = :material02Amount"),
    @NamedQuery(name = "TblProductionDetail.findByMaterial02PurgedAmount", query = "SELECT t FROM TblProductionDetail t WHERE t.material02PurgedAmount = :material02PurgedAmount"),
    @NamedQuery(name = "TblProductionDetail.findByMaterial03Id", query = "SELECT t FROM TblProductionDetail t WHERE t.material03Id = :material03Id"),
    @NamedQuery(name = "TblProductionDetail.findByMaterial03LotNo", query = "SELECT t FROM TblProductionDetail t WHERE t.material03LotNo = :material03LotNo"),
    @NamedQuery(name = "TblProductionDetail.findByMaterial03Amount", query = "SELECT t FROM TblProductionDetail t WHERE t.material03Amount = :material03Amount"),
    @NamedQuery(name = "TblProductionDetail.findByMaterial03PurgedAmount", query = "SELECT t FROM TblProductionDetail t WHERE t.material03PurgedAmount = :material03PurgedAmount"),
    @NamedQuery(name = "TblProductionDetail.findByPrevLotBalanceId", query = "SELECT t FROM TblProductionDetail t WHERE t.prevLotBalanceId = :prevLotBalanceId"),
    @NamedQuery(name = "TblProductionDetail.findByPlanAppropriatedCount", query = "SELECT t FROM TblProductionDetail t WHERE t.planAppropriatedCount = :planAppropriatedCount"),
    @NamedQuery(name = "TblProductionDetail.findByPlanNotAppropriatedCount", query = "SELECT t FROM TblProductionDetail t WHERE t.planNotAppropriatedCount = :planNotAppropriatedCount"),
    @NamedQuery(name = "TblProductionDetail.findByCreateDate", query = "SELECT t FROM TblProductionDetail t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblProductionDetail.findByUpdateDate", query = "SELECT t FROM TblProductionDetail t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblProductionDetail.findByCreateUserUuid", query = "SELECT t FROM TblProductionDetail t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblProductionDetail.findByUpdateUserUuid", query = "SELECT t FROM TblProductionDetail t WHERE t.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "TblProductionDetail.delete", query = "delete FROM TblProductionDetail t WHERE t.id = :id")
})
@Cacheable(value = false)
public class TblProductionDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 結合テーブル定義
     */
    // 生産実績
    @PrimaryKeyJoinColumn(name = "PRODUCTION_ID", referencedColumnName = "ID")  
    @ManyToOne
    private TblProduction tblProduction;
    public TblProduction getTblProduction() {
        return this.tblProduction;
    }
    public void setTblProduction(TblProduction tblProduction) {
        this.tblProduction = tblProduction;
    }
    
    // 部品マスタ
    @PrimaryKeyJoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID")  
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;
    public MstComponent getMstComponent() {
        return this.mstComponent;
    }
    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }
    
    // 工程マスタ（部品ごとの製造手順）
    @PrimaryKeyJoinColumn(name = "PROCEDURE_ID", referencedColumnName = "ID")  
    @ManyToOne(fetch = FetchType.LAZY)
    private MstProcedure mstProcedure;
    public MstProcedure getMstProcedure() {
        return this.mstProcedure;
    }
    public void setMstProcedure(MstProcedure mstProcedure) {
        this.mstProcedure = mstProcedure;
    }
    
        // 材料マスタ01
    @PrimaryKeyJoinColumn(name = "MATERIAL01_ID", referencedColumnName = "ID")  
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMaterial mstMaterial01;
    public MstMaterial getMstMaterial01() {
        return this.mstMaterial01;
    }
    public void setMstMaterial01(MstMaterial mstMaterial01) {
        this.mstMaterial01 = mstMaterial01;
    }
    
    // 材料マスタ02
    @PrimaryKeyJoinColumn(name = "MATERIAL02_ID", referencedColumnName = "ID")  
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMaterial mstMaterial02;
    public MstMaterial getMstMaterial02() {
        return this.mstMaterial02;
    }
    public void setMstMaterial02(MstMaterial mstMaterial02) {
        this.mstMaterial02 = mstMaterial02;
    }
    
    // 材料マスタ03
    @PrimaryKeyJoinColumn(name = "MATERIAL03_ID", referencedColumnName = "ID")  
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMaterial mstMaterial03;
    public MstMaterial getMstMaterial03() {
        return this.mstMaterial03;
    }
    public void setMstMaterial03(MstMaterial mstMaterial03) {
        this.mstMaterial03 = mstMaterial03;
    }
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 45)
    @Column(name = "COMPONENT_ID")
    private String componentId;
    @Size(max = 45)
    @Column(name = "PROCEDURE_ID")
    private String procedureId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COUNT_PER_SHOT")
    private int countPerShot;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEFECT_COUNT")
    private int defectCount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COMPLETE_COUNT")
    private int completeCount;
    @Size(max = 45)
    @Column(name = "MATERIAL01_ID")
    private String material01Id;
    @Size(max = 100)
    @Column(name = "MATERIAL01_LOT_NO")
    private String material01LotNo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "MATERIAL01_AMOUNT")
    private BigDecimal material01Amount;
    @Column(name = "MATERIAL01_PURGED_AMOUNT")
    private BigDecimal material01PurgedAmount;
    @Size(max = 45)
    @Column(name = "MATERIAL02_ID")
    private String material02Id;
    @Size(max = 100)
    @Column(name = "MATERIAL02_LOT_NO")
    private String material02LotNo;
    @Column(name = "MATERIAL02_AMOUNT")
    private BigDecimal material02Amount;
    @Column(name = "MATERIAL02_PURGED_AMOUNT")
    private BigDecimal material02PurgedAmount;
    @Size(max = 45)
    @Column(name = "MATERIAL03_ID")
    private String material03Id;
    @Size(max = 100)
    @Column(name = "MATERIAL03_LOT_NO")
    private String material03LotNo;
    @Column(name = "MATERIAL03_AMOUNT")
    private BigDecimal material03Amount;
    @Column(name = "MATERIAL03_PURGED_AMOUNT")
    private BigDecimal material03PurgedAmount;
    @Size(max = 45)
    @Column(name = "PREV_LOT_BALANCE_ID")
    private String prevLotBalanceId;
    @Column(name = "PLAN_APPROPRIATED_COUNT")
    private Integer planAppropriatedCount;
    @Column(name = "PLAN_NOT_APPROPRIATED_COUNT")
    private Integer planNotAppropriatedCount;
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
    @JoinColumn(name = "PRODUCTION_ID", referencedColumnName = "ID")
    @ManyToOne
    private TblProduction productionId;
    @OneToMany(mappedBy = "productionDetailId")
    private Collection<TblProductionLotBalance> tblProductionLotBalanceCollection;
    @OneToMany(mappedBy = "tblProductionDetail")
    private Collection<TblProductionMachineProcCond> tblProductionMachineProcCond;

    /*
     * マスタ系テーブル項目定義
     */
    // 部品マスタ
    @Transient
    private String componentCode; // 部品コード
    @Transient
    private String componentName; // 部品名称
    @Transient
    private Integer componentType;// 部品種類
    // 工程マスタ(部品ごとの製造手順)
    @Transient
    private String procedureCode; // 工番
    @Transient
    private String procedureName; // 工程名称
    // 材料マスタ(材料01～材料03)
    @Transient
    private String material01Code; // 材料01コード
    @Transient
    private String material02Code; // 材料02コード
    @Transient
    private String material03Code; // 材料03コード
    @Transient
    private String material01Name; // 材料01名称
    @Transient
    private String material02Name; // 材料02名称
    @Transient
    private String material03Name; // 材料03名称
    /*
     * 一括反映時制御フラグ
     */
    @Transient
    private boolean deleted = false;    // 削除対象制御
    @Transient
    private boolean modified = false;   // 更新対象制御
    @Transient
    private boolean added = false;      // 登録対象制御
    @Transient
    private TblComponentLotRelationVoList tblComponentLotRelationVoList;
    @Transient
    private int completeCountBeforeUpd;
    
    public TblProductionDetail() {
    }

    public TblProductionDetail(String id) {
        this.id = id;
    }

    public TblProductionDetail(String id, int countPerShot, int defectCount, int completeCount) {
        this.id = id;
        this.countPerShot = countPerShot;
        this.defectCount = defectCount;
        this.completeCount = completeCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    public int getCountPerShot() {
        return countPerShot;
    }

    public void setCountPerShot(int countPerShot) {
        this.countPerShot = countPerShot;
    }

    public int getDefectCount() {
        return defectCount;
    }

    public void setDefectCount(int defectCount) {
        this.defectCount = defectCount;
    }

    public int getCompleteCount() {
        return completeCount;
    }

    public void setCompleteCount(int completeCount) {
        this.completeCount = completeCount;
    }
    
    public int getCompleteCountBeforeUpd() {
        return completeCountBeforeUpd;
    }

    public void setCompleteCountBeforeUpd(int completeCountBeforeUpd) {
        this.completeCountBeforeUpd = completeCountBeforeUpd;
    }

    public String getMaterial01Id() {
        return material01Id;
    }

    public void setMaterial01Id(String material01Id) {
        this.material01Id = material01Id;
    }

    public String getMaterial01LotNo() {
        return material01LotNo;
    }

    public void setMaterial01LotNo(String material01LotNo) {
        this.material01LotNo = material01LotNo;
    }

    public BigDecimal getMaterial01Amount() {
        return material01Amount;
    }

    public void setMaterial01Amount(BigDecimal material01Amount) {
        this.material01Amount = material01Amount;
    }

    public BigDecimal getMaterial01PurgedAmount() {
        return material01PurgedAmount;
    }

    public void setMaterial01PurgedAmount(BigDecimal material01PurgedAmount) {
        this.material01PurgedAmount = material01PurgedAmount;
    }

    public String getMaterial02Id() {
        return material02Id;
    }

    public void setMaterial02Id(String material02Id) {
        this.material02Id = material02Id;
    }

    public String getMaterial02LotNo() {
        return material02LotNo;
    }

    public void setMaterial02LotNo(String material02LotNo) {
        this.material02LotNo = material02LotNo;
    }

    public BigDecimal getMaterial02Amount() {
        return material02Amount;
    }

    public void setMaterial02Amount(BigDecimal material02Amount) {
        this.material02Amount = material02Amount;
    }

    public BigDecimal getMaterial02PurgedAmount() {
        return material02PurgedAmount;
    }

    public void setMaterial02PurgedAmount(BigDecimal material02PurgedAmount) {
        this.material02PurgedAmount = material02PurgedAmount;
    }

    public String getMaterial03Id() {
        return material03Id;
    }

    public void setMaterial03Id(String material03Id) {
        this.material03Id = material03Id;
    }

    public String getMaterial03LotNo() {
        return material03LotNo;
    }

    public void setMaterial03LotNo(String material03LotNo) {
        this.material03LotNo = material03LotNo;
    }

    public BigDecimal getMaterial03Amount() {
        return material03Amount;
    }

    public void setMaterial03Amount(BigDecimal material03Amount) {
        this.material03Amount = material03Amount;
    }

    public BigDecimal getMaterial03PurgedAmount() {
        return material03PurgedAmount;
    }

    public void setMaterial03PurgedAmount(BigDecimal material03PurgedAmount) {
        this.material03PurgedAmount = material03PurgedAmount;
    }

    public String getPrevLotBalanceId() {
        return prevLotBalanceId;
    }

    public void setPrevLotBalanceId(String prevLotBalanceId) {
        this.prevLotBalanceId = prevLotBalanceId;
    }

    public Integer getPlanAppropriatedCount() {
        return planAppropriatedCount;
    }

    public void setPlanAppropriatedCount(Integer planAppropriatedCount) {
        this.planAppropriatedCount = planAppropriatedCount;
    }

    public Integer getPlanNotAppropriatedCount() {
        return planNotAppropriatedCount;
    }

    public void setPlanNotAppropriatedCount(Integer planNotAppropriatedCount) {
        this.planNotAppropriatedCount = planNotAppropriatedCount;
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

    public TblProduction getProductionId() {
        return productionId;
    }

    public void setProductionId(TblProduction productionId) {
        this.productionId = productionId;
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
    
    public String getMaterial01Code() {
        return material01Code;
    }

    public void setMaterial01Code(String material01Code) {
        this.material01Code = material01Code;
    }

    public String getMaterial02Code() {
        return material02Code;
    }

    public void setMaterial02Code(String material02Code) {
        this.material02Code = material02Code;
    }

    public String getMaterial03Code() {
        return material03Code;
    }

    public void setMaterial03Code(String material03Code) {
        this.material03Code = material03Code;
    }

    public String getMaterial01Name() {
        return material01Name;
    }

    public void setMaterial01Name(String material01Name) {
        this.material01Name = material01Name;
    }

    public String getMaterial02Name() {
        return material02Name;
    }

    public void setMaterial02Name(String material02Name) {
        this.material02Name = material02Name;
    }

    public String getMaterial03Name() {
        return material03Name;
    }

    public void setMaterial03Name(String material03Name) {
        this.material03Name = material03Name;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public boolean isAdded() {
        return added;
    }
    
    public void setAdded(boolean added) {
        this.added = added;
    }

    @XmlTransient
    public Collection<TblProductionLotBalance> getTblProductionLotBalanceCollection() {
        return tblProductionLotBalanceCollection;
    }

    public void setTblProductionLotBalanceCollection(Collection<TblProductionLotBalance> tblProductionLotBalanceCollection) {
        this.tblProductionLotBalanceCollection = tblProductionLotBalanceCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblProductionDetail)) {
            return false;
        }
        TblProductionDetail other = (TblProductionDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.production.TblProductionDetail[ id=" + id + " ]";
    }

    /**
     * @return the tblProductionMachineProcCond
     */
    @XmlTransient
    public Collection<TblProductionMachineProcCond> getTblProductionMachineProcCond() {
        return tblProductionMachineProcCond;
    }

    /**
     * @param tblProductionMachineProcCond the tblProductionMachineProcCond to set
     */
    public void setTblProductionMachineProcCond(Collection<TblProductionMachineProcCond> tblProductionMachineProcCond) {
        this.tblProductionMachineProcCond = tblProductionMachineProcCond;
    }

    public TblComponentLotRelationVoList getTblComponentLotRelationVoList() {
        return tblComponentLotRelationVoList;
    }

    public void setTblComponentLotRelationVoList(TblComponentLotRelationVoList tblComponentLotRelationVoList) {
        this.tblComponentLotRelationVoList = tblComponentLotRelationVoList;
    }
    
}
