/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.daily.report.detail;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.machine.daily.report.TblMachineDailyReport;
import com.kmcj.karte.resources.material.MstMaterial;
import com.kmcj.karte.resources.procedure.MstProcedure;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author zds
 */
@Entity
@Table(name = "tbl_machine_daily_report_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineDailyReportDetail.findAll", query = "SELECT t FROM TblMachineDailyReportDetail t"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findById", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.id = :id"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByMacReportId", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.tblMachineDailyReportDetailPK.macReportId = :macReportId"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByProductionDetailId", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.tblMachineDailyReportDetailPK.productionDetailId = :productionDetailId"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByProductionDetailIdAndMacReportId", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.tblMachineDailyReportDetailPK.productionDetailId = :productionDetailId and t.tblMachineDailyReportDetailPK.macReportId = :macReportId"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByComponentId", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.componentId = :componentId"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByProcedureId", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.procedureId = :procedureId"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByCountPerShot", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.countPerShot = :countPerShot"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByDefectCount", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.defectCount = :defectCount"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByCompleteCount", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.completeCount = :completeCount"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByMaterial01Id", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.material01Id = :material01Id"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByMaterial01LotNo", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.material01LotNo = :material01LotNo"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByMaterial01Amount", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.material01Amount = :material01Amount"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByMaterial01PurgedAmount", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.material01PurgedAmount = :material01PurgedAmount"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByMaterial02Id", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.material02Id = :material02Id"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByMaterial02LotNo", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.material02LotNo = :material02LotNo"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByMaterial02Amount", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.material02Amount = :material02Amount"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByMaterial02PurgedAmount", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.material02PurgedAmount = :material02PurgedAmount"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByMaterial03Id", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.material03Id = :material03Id"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByMaterial03LotNo", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.material03LotNo = :material03LotNo"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByMaterial03Amount", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.material03Amount = :material03Amount"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByMaterial03PurgedAmount", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.material03PurgedAmount = :material03PurgedAmount"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByCreateDate", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByUpdateDate", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByCreateUserUuid", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblMachineDailyReportDetail.findByUpdateUserUuid", query = "SELECT t FROM TblMachineDailyReportDetail t WHERE t.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class TblMachineDailyReportDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMachineDailyReportDetailPK tblMachineDailyReportDetailPK;
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
    @Column(name = "COMPONENT_NET_PRODUCINT_TIME_MINUTES")
    private Integer componentNetProducintTimeMinutes;
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
    
    /**
     * 結合テーブル定義
     */
    // 機械日報
    @PrimaryKeyJoinColumn(name = "MAC_REPORT_ID", referencedColumnName = "ID")  
    @ManyToOne
    private TblMachineDailyReport tblMachineDailyReport;
    public TblMachineDailyReport getTblMachineDailyReport() {
        return this.tblMachineDailyReport;
    }
    public void setTblMachineDailyReport(TblMachineDailyReport tblMachineDailyReport) {
        this.tblMachineDailyReport = tblMachineDailyReport;
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
   
    
    public TblMachineDailyReportDetail() {
    }

    public TblMachineDailyReportDetail(TblMachineDailyReportDetailPK tblMachineDailyReportDetailPK) {
        this.tblMachineDailyReportDetailPK = tblMachineDailyReportDetailPK;
    }

    public TblMachineDailyReportDetail(TblMachineDailyReportDetailPK tblMachineDailyReportDetailPK, String id, int countPerShot, int defectCount, int completeCount) {
        this.tblMachineDailyReportDetailPK = tblMachineDailyReportDetailPK;
        this.id = id;
        this.countPerShot = countPerShot;
        this.defectCount = defectCount;
        this.completeCount = completeCount;
    }

    public TblMachineDailyReportDetail(String macReportId, String productionDetailId) {
        this.tblMachineDailyReportDetailPK = new TblMachineDailyReportDetailPK(macReportId, productionDetailId);
    }

    public TblMachineDailyReportDetailPK getTblMachineDailyReportDetailPK() {
        return tblMachineDailyReportDetailPK;
    }

    public void setTblMachineDailyReportDetailPK(TblMachineDailyReportDetailPK tblMachineDailyReportDetailPK) {
        this.tblMachineDailyReportDetailPK = tblMachineDailyReportDetailPK;
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
        hash += (tblMachineDailyReportDetailPK != null ? tblMachineDailyReportDetailPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof TblMachineDailyReportDetail)) {
            return false;
        }
        TblMachineDailyReportDetail other = (TblMachineDailyReportDetail) object;
        if ((this.tblMachineDailyReportDetailPK == null && other.tblMachineDailyReportDetailPK != null) || (this.tblMachineDailyReportDetailPK != null && !this.tblMachineDailyReportDetailPK.equals(other.tblMachineDailyReportDetailPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.daily.report.detail.TblMachineDailyReportDetail[ tblMachineDailyReportDetailPK=" + tblMachineDailyReportDetailPK + " ]";
    }

    /**
     * @return the componentNetProducintTimeMinutes
     */
    public Integer getComponentNetProducintTimeMinutes() {
        return componentNetProducintTimeMinutes;
    }

    /**
     * @param componentNetProducintTimeMinutes the componentNetProducintTimeMinutes to set
     */
    public void setComponentNetProducintTimeMinutes(Integer componentNetProducintTimeMinutes) {
        this.componentNetProducintTimeMinutes = componentNetProducintTimeMinutes;
    }
    
}
