/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.dailyreport2;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationVoList;
import com.kmcj.karte.resources.component.material.MstComponentMaterial;
import com.kmcj.karte.resources.material.MstMaterial;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.production.defect.TblProductionDefectDaily;
import com.kmcj.karte.resources.production2.Production;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author f.kitaoji
 */
@Entity
@Table(name = "tbl_machine_daily_report2_prod_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findAll", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findById", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.id = :id"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByCountPerShot", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.countPerShot = :countPerShot"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByDefectCount", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.defectCount = :defectCount"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByCompleteCount", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.completeCount = :completeCount"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByMaterial01LotNo", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.material01LotNo = :material01LotNo"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByMaterial01Amount", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.material01Amount = :material01Amount"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByMaterial01PurgedAmount", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.material01PurgedAmount = :material01PurgedAmount"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByMaterial02LotNo", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.material02LotNo = :material02LotNo"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByMaterial02Amount", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.material02Amount = :material02Amount"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByMaterial02PurgedAmount", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.material02PurgedAmount = :material02PurgedAmount"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByMaterial03LotNo", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.material03LotNo = :material03LotNo"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByMaterial03Amount", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.material03Amount = :material03Amount"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByMaterial03PurgedAmount", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.material03PurgedAmount = :material03PurgedAmount"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByComponentOperatingMinutes", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.componentOperatingMinutes = :componentOperatingMinutes"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByDefectCountType01", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.defectCountType01 = :defectCountType01"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByDefectCountType02", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.defectCountType02 = :defectCountType02"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByDefectCountType03", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.defectCountType03 = :defectCountType03"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByDefectCountType04", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.defectCountType04 = :defectCountType04"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByDefectCountType05", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.defectCountType05 = :defectCountType05"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByDefectCountType06", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.defectCountType06 = :defectCountType06"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByDefectCountType07", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.defectCountType07 = :defectCountType07"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByDefectCountType08", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.defectCountType08 = :defectCountType08"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByDefectCountType09", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.defectCountType09 = :defectCountType09"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByDefectCountType10", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.defectCountType10 = :defectCountType10"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByDefectCountOther", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.defectCountOther = :defectCountOther"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByCreateDate", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMachineDailyReport2ProdDetail.findByUpdateDate", query = "SELECT t FROM TblMachineDailyReport2ProdDetail t WHERE t.updateDate = :updateDate")
})
@Cacheable(value = false)
public class TblMachineDailyReport2ProdDetail implements Serializable {

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
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "MATERIAL01_AMOUNT")
    private BigDecimal material01Amount;
    @Column(name = "MATERIAL01_PURGED_AMOUNT")
    private BigDecimal material01PurgedAmount;
    @Column(name = "MATERIAL02_AMOUNT")
    private BigDecimal material02Amount;
    @Column(name = "MATERIAL02_PURGED_AMOUNT")
    private BigDecimal material02PurgedAmount;
    @Column(name = "MATERIAL03_AMOUNT")
    private BigDecimal material03Amount;
    @Column(name = "MATERIAL03_PURGED_AMOUNT")
    private BigDecimal material03PurgedAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEFECT_COUNT_OTHER")
    private int defectCountOther;

    private static long serialVersionUID = 1L;

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
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 45)
    @Column(name = "PRODUCTION_DETAIL_ID")
    private String productionDetailId;
    @Size(max = 45)
    @Column(name = "COMPONENT_ID")
    private String componentId;
    @Size(max = 45)
    @Column(name = "PROCEDURE_ID")
    private String procedureId;
    @Size(max = 45)
    @Column(name = "MATERIAL01_ID")
    private String material01Id;
    @Size(max = 45)
    @Column(name = "MATERIAL02_ID")
    private String material02Id;
    @Size(max = 45)
    @Column(name = "MATERIAL03_ID")
    private String material03Id;
    @Size(max = 100)
    @Column(name = "MATERIAL01_LOT_NO")
    private String material01LotNo;
    @Size(max = 100)
    @Column(name = "MATERIAL02_LOT_NO")
    private String material02LotNo;
    @Size(max = 100)
    @Column(name = "MATERIAL03_LOT_NO")
    private String material03LotNo;
    @Column(name = "COMPONENT_OPERATING_MINUTES")
    private int componentOperatingMinutes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEFECT_COUNT_TYPE01")
    private int defectCountType01;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEFECT_COUNT_TYPE02")
    private int defectCountType02;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEFECT_COUNT_TYPE03")
    private int defectCountType03;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEFECT_COUNT_TYPE04")
    private int defectCountType04;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEFECT_COUNT_TYPE05")
    private int defectCountType05;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEFECT_COUNT_TYPE06")
    private int defectCountType06;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEFECT_COUNT_TYPE07")
    private int defectCountType07;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEFECT_COUNT_TYPE08")
    private int defectCountType08;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEFECT_COUNT_TYPE09")
    private int defectCountType09;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEFECT_COUNT_TYPE10")
    private int defectCountType10;
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
    @Size(max = 45)
    @Column(name = "REPORT_DETAIL_ID")
    private String reportDetailId;
    @JoinColumn(name = "REPORT_DETAIL_ID", referencedColumnName = "ID",  insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TblMachineDailyReport2Detail reportDetail;

    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstComponent mstComponent;
    @JoinColumn(name = "PROCEDURE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstProcedure mstProcedure;
    @JoinColumn(name = "MATERIAL01_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstMaterial mstMaterial01;
    @JoinColumn(name = "MATERIAL02_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstMaterial mstMaterial02;
    @JoinColumn(name = "MATERIAL03_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstMaterial mstMaterial03;
    
//    @OneToMany(mappedBy = "reportProdDetail")
    @Transient
    @XmlElement(name = "productionDefectsDaily")
    private List<TblProductionDefectDaily> tblProductionDefectDailyList;
    
    @Transient
    private String componentCode;
    @Transient
    private String componentName;
    @Transient
    private String procedureCode;
    @Transient
    private String procedureName;
    @Transient
    private String material01Code;
    @Transient
    private String material01Name;
    @Transient
    private String material01Type;
    @Transient
    private String material01Grade;
    @Transient
    private BigDecimal material01numerator;
    @Transient
    private BigDecimal material01denominator;
    @Transient
    private String material02Code;
    @Transient
    private String material02Name;
    @Transient
    private String material02Type;
    @Transient
    private String material02Grade;
    @Transient
    private BigDecimal material02numerator;
    @Transient
    private BigDecimal material02denominator;
    @Transient
    private String material03Code;
    @Transient
    private String material03Name;
    @Transient
    private String material03Type;
    @Transient
    private String material03Grade;
    @Transient
    private BigDecimal material03numerator;
    @Transient
    private BigDecimal material03denominator;
    
    @Transient
    private boolean deleted = false;    // 削除対象制御
    @Transient
    private boolean modified = false;   // 更新対象制御
    @Transient
    private boolean added = false;      // 登録対象制御

    @Transient
    private TblComponentLotRelationVoList tblComponentLotRelationVoList; //部品ロット関係リスト。在庫更新用
    
    @Transient
    private List<Production> componentLotNumberList; //部品ロット番号リスト

    @Transient
    private String lotNumber;
    
    public TblMachineDailyReport2ProdDetail() {
    }

    public TblMachineDailyReport2ProdDetail(String id) {
        this.id = id;
    }

    public TblMachineDailyReport2ProdDetail(String id, int countPerShot, int defectCount, int completeCount, int defectCountType01, int defectCountType02, int defectCountType03, int defectCountType04, int defectCountType05, int defectCountType06, int defectCountType07, int defectCountType08, int defectCountType09, int defectCountType10) {
        this.id = id;
        this.countPerShot = countPerShot;
        this.defectCount = defectCount;
        this.completeCount = completeCount;
        this.defectCountType01 = defectCountType01;
        this.defectCountType02 = defectCountType02;
        this.defectCountType03 = defectCountType03;
        this.defectCountType04 = defectCountType04;
        this.defectCountType05 = defectCountType05;
        this.defectCountType06 = defectCountType06;
        this.defectCountType07 = defectCountType07;
        this.defectCountType08 = defectCountType08;
        this.defectCountType09 = defectCountType09;
        this.defectCountType10 = defectCountType10;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getComponentOperatingMinutes() {
        return componentOperatingMinutes;
    }

    public void setComponentOperatingMinutes(int componentOperatingMinutes) {
        this.componentOperatingMinutes = componentOperatingMinutes;
    }

    public int getDefectCountType01() {
        return defectCountType01;
    }

    public void setDefectCountType01(int defectCountType01) {
        this.defectCountType01 = defectCountType01;
    }

    public int getDefectCountType02() {
        return defectCountType02;
    }

    public void setDefectCountType02(int defectCountType02) {
        this.defectCountType02 = defectCountType02;
    }

    public int getDefectCountType03() {
        return defectCountType03;
    }

    public void setDefectCountType03(int defectCountType03) {
        this.defectCountType03 = defectCountType03;
    }

    public int getDefectCountType04() {
        return defectCountType04;
    }

    public void setDefectCountType04(int defectCountType04) {
        this.defectCountType04 = defectCountType04;
    }

    public int getDefectCountType05() {
        return defectCountType05;
    }

    public void setDefectCountType05(int defectCountType05) {
        this.defectCountType05 = defectCountType05;
    }

    public int getDefectCountType06() {
        return defectCountType06;
    }

    public void setDefectCountType06(int defectCountType06) {
        this.defectCountType06 = defectCountType06;
    }

    public int getDefectCountType07() {
        return defectCountType07;
    }

    public void setDefectCountType07(int defectCountType07) {
        this.defectCountType07 = defectCountType07;
    }

    public int getDefectCountType08() {
        return defectCountType08;
    }

    public void setDefectCountType08(int defectCountType08) {
        this.defectCountType08 = defectCountType08;
    }

    public int getDefectCountType09() {
        return defectCountType09;
    }

    public void setDefectCountType09(int defectCountType09) {
        this.defectCountType09 = defectCountType09;
    }

    public int getDefectCountType10() {
        return defectCountType10;
    }

    public void setDefectCountType10(int defectCountType10) {
        this.defectCountType10 = defectCountType10;
    }

    public int getDefectCountOther() {
        return defectCountOther;
    }

    public void setDefectCountOther(int defectCountOther) {
        this.defectCountOther = defectCountOther;
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

    @XmlTransient
    public TblMachineDailyReport2Detail getReportDetail() {
        return reportDetail;
    }

    public void setReportDetail(TblMachineDailyReport2Detail reportDetail) {
        this.reportDetail = reportDetail;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineDailyReport2ProdDetail)) {
            return false;
        }
        TblMachineDailyReport2ProdDetail other = (TblMachineDailyReport2ProdDetail) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.dailyreport2.TblMachineDailyReport2ProdDetail[ id=" + getId() + " ]";
    }

    /**
     * @return the deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * @return the modified
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * @param modified the modified to set
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    /**
     * @return the added
     */
    public boolean isAdded() {
        return added;
    }

    /**
     * @param added the added to set
     */
    public void setAdded(boolean added) {
        this.added = added;
    }

    /**
     * @return the componentId
     */
    public String getComponentId() {
        return componentId;
    }

    /**
     * @param componentId the componentId to set
     */
    public void setComponentId(String componentId) {
        this.componentId = componentId;
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

    /**
     * @return the material01Id
     */
    public String getMaterial01Id() {
        return material01Id;
    }

    /**
     * @param material01Id the material01Id to set
     */
    public void setMaterial01Id(String material01Id) {
        this.material01Id = material01Id;
    }

    /**
     * @return the material02Id
     */
    public String getMaterial02Id() {
        return material02Id;
    }

    /**
     * @param material02Id the material02Id to set
     */
    public void setMaterial02Id(String material02Id) {
        this.material02Id = material02Id;
    }

    /**
     * @return the material03Id
     */
    public String getMaterial03Id() {
        return material03Id;
    }

    /**
     * @param material03Id the material03Id to set
     */
    public void setMaterial03Id(String material03Id) {
        this.material03Id = material03Id;
    }

    /**
     * @return the mstComponent
     */
    @XmlTransient
    public MstComponent getMstComponent() {
        return mstComponent;
    }

    /**
     * @param mstComponent the mstComponent to set
     */
    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }

    /**
     * @return the mstProcedure
     */
    @XmlTransient
    public MstProcedure getMstProcedure() {
        return mstProcedure;
    }

    /**
     * @param mstProcedure the mstProcedure to set
     */
    public void setMstProcedure(MstProcedure mstProcedure) {
        this.mstProcedure = mstProcedure;
    }

    /**
     * @return the mstMaterial01
     */
    @XmlTransient
    public MstMaterial getMstMaterial01() {
        return mstMaterial01;
    }

    /**
     * @param mstMaterial01 the mstMaterial01 to set
     */
    public void setMstMaterial01(MstMaterial mstMaterial01) {
        this.mstMaterial01 = mstMaterial01;
    }

    /**
     * @return the mstMaterial02
     */
    @XmlTransient
    public MstMaterial getMstMaterial02() {
        return mstMaterial02;
    }

    /**
     * @param mstMaterial02 the mstMaterial02 to set
     */
    public void setMstMaterial02(MstMaterial mstMaterial02) {
        this.mstMaterial02 = mstMaterial02;
    }

    /**
     * @return the mstMaterial03
     */
    @XmlTransient
    public MstMaterial getMstMaterial03() {
        return mstMaterial03;
    }

    /**
     * @param mstMaterial03 the mstMaterial03 to set
     */
    public void setMstMaterial03(MstMaterial mstMaterial03) {
        this.mstMaterial03 = mstMaterial03;
    }

    /**
     * @return the componentCode
     */
    public String getComponentCode() {
        return componentCode;
    }

    /**
     * @param componentCode the componentCode to set
     */
    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    /**
     * @return the componentName
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * @param componentName the componentName to set
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    /**
     * @return the procedureCode
     */
    public String getProcedureCode() {
        return procedureCode;
    }

    /**
     * @param procedureCode the procedureCode to set
     */
    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    /**
     * @return the procedureName
     */
    public String getProcedureName() {
        return procedureName;
    }

    /**
     * @param procedureName the procedureName to set
     */
    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    /**
     * @return the material01Code
     */
    public String getMaterial01Code() {
        return material01Code;
    }

    /**
     * @param material01Code the material01Code to set
     */
    public void setMaterial01Code(String material01Code) {
        this.material01Code = material01Code;
    }

    /**
     * @return the material01Name
     */
    public String getMaterial01Name() {
        return material01Name;
    }

    /**
     * @param material01Name the material01Name to set
     */
    public void setMaterial01Name(String material01Name) {
        this.material01Name = material01Name;
    }

    /**
     * @return the material02Code
     */
    public String getMaterial02Code() {
        return material02Code;
    }

    /**
     * @param material02Code the material02Code to set
     */
    public void setMaterial02Code(String material02Code) {
        this.material02Code = material02Code;
    }

    /**
     * @return the material02Name
     */
    public String getMaterial02Name() {
        return material02Name;
    }

    /**
     * @param material02Name the material02Name to set
     */
    public void setMaterial02Name(String material02Name) {
        this.material02Name = material02Name;
    }

    /**
     * @return the material03Code
     */
    public String getMaterial03Code() {
        return material03Code;
    }

    /**
     * @param material03Code the material03Code to set
     */
    public void setMaterial03Code(String material03Code) {
        this.material03Code = material03Code;
    }

    /**
     * @return the material03Name
     */
    public String getMaterial03Name() {
        return material03Name;
    }

    /**
     * @param material03Name the material03Name to set
     */
    public void setMaterial03Name(String material03Name) {
        this.material03Name = material03Name;
    }

    public void formatJson(Date inReportDate) {
        if (this.mstComponent != null) {
            this.componentCode = mstComponent.getComponentCode();
            this.componentName = mstComponent.getComponentName();
        }
        if (this.mstProcedure != null) {
            this.procedureCode = mstProcedure.getProcedureCode();
            this.procedureName = mstProcedure.getProcedureName();
        }
        this.material01numerator = BigDecimal.ZERO;
        this.material01denominator = BigDecimal.ONE;
        this.material02numerator = BigDecimal.ZERO;
        this.material02denominator = BigDecimal.ONE;
        this.material03numerator = BigDecimal.ZERO;
        this.material03denominator = BigDecimal.ONE;

        if (this.mstMaterial01 != null) {
            this.material01Code = mstMaterial01.getMaterialCode();
            this.material01Name = mstMaterial01.getMaterialName();
            this.material01Type = mstMaterial01.getMaterialType();
            this.material01Grade = mstMaterial01.getMaterialGrade();

            if (this.mstComponent != null && this.mstProcedure != null && inReportDate != null) {

                if (mstMaterial01.getMstComponentMaterialCollection() != null && mstMaterial01.getMstComponentMaterialCollection().size() > 0) {
                    Iterator<MstComponentMaterial> mstComponentMateriales = mstMaterial01.getMstComponentMaterialCollection().iterator();
                    while (mstComponentMateriales.hasNext()) {
                        MstComponentMaterial mcm = mstComponentMateriales.next();
                        if (mcm.getComponentId().equals(this.mstComponent.getId()) && mcm.getProceduerCode().equals(mstProcedure.getProcedureCode())) {
                            if (mcm.getStartDate() != null && mcm.getEndDate() != null) {
                                if (mcm.getStartDate().compareTo(inReportDate) <= 0 && mcm.getEndDate().compareTo(inReportDate) >= 0) {
                                    this.material01numerator = mcm.getNumerator();
                                    this.material01denominator = mcm.getDenominator();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (this.mstMaterial02 != null) {
            this.material02Code = mstMaterial02.getMaterialCode();
            this.material02Name = mstMaterial02.getMaterialName();
            this.material02Type = mstMaterial02.getMaterialType();
            this.material02Grade = mstMaterial02.getMaterialGrade();
            if (this.mstComponent != null && this.mstProcedure != null && inReportDate != null) {

                if (mstMaterial02.getMstComponentMaterialCollection() != null && mstMaterial02.getMstComponentMaterialCollection().size() > 0) {
                    Iterator<MstComponentMaterial> mstComponentMateriales = mstMaterial02.getMstComponentMaterialCollection().iterator();
                    while (mstComponentMateriales.hasNext()) {
                        MstComponentMaterial mcm = mstComponentMateriales.next();
                        if (mcm.getComponentId().equals(this.mstComponent.getId()) && mcm.getProceduerCode().equals(mstProcedure.getProcedureCode())) {
                            if (mcm.getStartDate() != null && mcm.getEndDate() != null) {
                                if (mcm.getStartDate().compareTo(inReportDate) <= 0 && mcm.getEndDate().compareTo(inReportDate) >= 0) {
                                    this.material02numerator = mcm.getNumerator();
                                    this.material02denominator = mcm.getDenominator();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (this.mstMaterial03 != null) {
            this.material03Code = mstMaterial03.getMaterialCode();
            this.material03Name = mstMaterial03.getMaterialName();
            this.material03Type = mstMaterial03.getMaterialType();
            this.material03Grade = mstMaterial03.getMaterialGrade();
            if (this.mstComponent != null && this.mstProcedure != null && inReportDate != null) {

                if (mstMaterial03.getMstComponentMaterialCollection() != null && mstMaterial03.getMstComponentMaterialCollection().size() > 0) {
                    Iterator<MstComponentMaterial> mstComponentMateriales = mstMaterial03.getMstComponentMaterialCollection().iterator();
                    while (mstComponentMateriales.hasNext()) {
                        MstComponentMaterial mcm = mstComponentMateriales.next();
                        if (mcm.getComponentId().equals(this.mstComponent.getId()) && mcm.getProceduerCode().equals(mstProcedure.getProcedureCode())) {
                            if (mcm.getStartDate() != null && mcm.getEndDate() != null) {
                                if (mcm.getStartDate().compareTo(inReportDate) <= 0 && mcm.getEndDate().compareTo(inReportDate) >= 0) {
                                    this.material03numerator = mcm.getNumerator();
                                    this.material03denominator = mcm.getDenominator();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @return the reportDetailId
     */
    public String getReportDetailId() {
        return reportDetailId;
    }

    /**
     * @param reportDetailId the reportDetailId to set
     */
    public void setReportDetailId(String reportDetailId) {
        this.reportDetailId = reportDetailId;
    }

    /**
     * @return the productionDetailId
     */
    public String getProductionDetailId() {
        return productionDetailId;
    }

    /**
     * @param productionDetailId the productionDetailId to set
     */
    public void setProductionDetailId(String productionDetailId) {
        this.productionDetailId = productionDetailId;
    }

    /**
     * @return the material01Type
     */
    public String getMaterial01Type() {
        return material01Type;
    }

    /**
     * @param material01Type the material01Type to set
     */
    public void setMaterial01Type(String material01Type) {
        this.material01Type = material01Type;
    }

    /**
     * @return the material01Grade
     */
    public String getMaterial01Grade() {
        return material01Grade;
    }

    /**
     * @param material01Grade the material01Grade to set
     */
    public void setMaterial01Grade(String material01Grade) {
        this.material01Grade = material01Grade;
    }

    /**
     * @return the material02Type
     */
    public String getMaterial02Type() {
        return material02Type;
    }

    /**
     * @param material02Type the material02Type to set
     */
    public void setMaterial02Type(String material02Type) {
        this.material02Type = material02Type;
    }

    /**
     * @return the material02Grade
     */
    public String getMaterial02Grade() {
        return material02Grade;
    }

    /**
     * @param material02Grade the material02Grade to set
     */
    public void setMaterial02Grade(String material02Grade) {
        this.material02Grade = material02Grade;
    }

    /**
     * @return the material03Type
     */
    public String getMaterial03Type() {
        return material03Type;
    }

    /**
     * @param material03Type the material03Type to set
     */
    public void setMaterial03Type(String material03Type) {
        this.material03Type = material03Type;
    }

    /**
     * @return the material03Grade
     */
    public String getMaterial03Grade() {
        return material03Grade;
    }

    /**
     * @param material03Grade the material03Grade to set
     */
    public void setMaterial03Grade(String material03Grade) {
        this.material03Grade = material03Grade;
    }

    /**
     * @return the tblComponentLotRelationVoList
     */
    public TblComponentLotRelationVoList getTblComponentLotRelationVoList() {
        return tblComponentLotRelationVoList;
    }

    /**
     * @param tblComponentLotRelationVoList the tblComponentLotRelationVoList to set
     */
    public void setTblComponentLotRelationVoList(TblComponentLotRelationVoList tblComponentLotRelationVoList) {
        this.tblComponentLotRelationVoList = tblComponentLotRelationVoList;
    }

    public List<TblProductionDefectDaily> getTblProductionDefectDailyList() {
        return tblProductionDefectDailyList;
    }

    public void setTblProductionDefectDailyList(List<TblProductionDefectDaily> tblProductionDefectDailyList) {
        this.tblProductionDefectDailyList = tblProductionDefectDailyList;
    }

    /**
     * @return the material01numerator
     */
    public BigDecimal getMaterial01numerator() {
        return material01numerator;
    }

    /**
     * @param material01numerator the material01numerator to set
     */
    public void setMaterial01numerator(BigDecimal material01numerator) {
        this.material01numerator = material01numerator;
    }

    /**
     * @return the material01denominator
     */
    public BigDecimal getMaterial01denominator() {
        return material01denominator;
    }

    /**
     * @param material01denominator the material01denominator to set
     */
    public void setMaterial01denominator(BigDecimal material01denominator) {
        this.material01denominator = material01denominator;
    }

    /**
     * @return the material02numerator
     */
    public BigDecimal getMaterial02numerator() {
        return material02numerator;
    }

    /**
     * @param material02numerator the material02numerator to set
     */
    public void setMaterial02numerator(BigDecimal material02numerator) {
        this.material02numerator = material02numerator;
    }

    /**
     * @return the material02denominator
     */
    public BigDecimal getMaterial02denominator() {
        return material02denominator;
    }

    /**
     * @param material02denominator the material02denominator to set
     */
    public void setMaterial02denominator(BigDecimal material02denominator) {
        this.material02denominator = material02denominator;
    }

    /**
     * @return the material03numerator
     */
    public BigDecimal getMaterial03numerator() {
        return material03numerator;
    }

    /**
     * @param material03numerator the material03numerator to set
     */
    public void setMaterial03numerator(BigDecimal material03numerator) {
        this.material03numerator = material03numerator;
    }

    /**
     * @return the material03denominator
     */
    public BigDecimal getMaterial03denominator() {
        return material03denominator;
    }

    /**
     * @param material03denominator the material03denominator to set
     */
    public void setMaterial03denominator(BigDecimal material03denominator) {
        this.material03denominator = material03denominator;
    }

    public List<Production> getComponentLotNumberList() {
        return componentLotNumberList;
    }

    public void setComponentLotNumberList(List<Production> componentLotNumberList) {
        this.componentLotNumberList = componentLotNumberList;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }
    
}
