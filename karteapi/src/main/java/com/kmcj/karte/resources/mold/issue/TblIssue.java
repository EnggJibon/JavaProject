/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.issue;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.maintenance.remodeling.TblMachineMaintenanceRemodeling;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodeling;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.user.MstUser;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_issue")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblIssue.findAll", query = "SELECT t FROM TblIssue t"),
    @NamedQuery(name = "TblIssue.findById", query = "SELECT t FROM TblIssue t WHERE t.id = :id"),
    @NamedQuery(name = "TblIssue.findByReportDate", query = "SELECT t FROM TblIssue t WHERE t.reportDate = :reportDate"),
    @NamedQuery(name = "TblIssue.findByHappenedAt", query = "SELECT t FROM TblIssue t WHERE t.happenedAt = :happenedAt"),
    @NamedQuery(name = "TblIssue.findByHappenedAtStz", query = "SELECT t FROM TblIssue t WHERE t.happenedAtStz = :happenedAtStz"),
    @NamedQuery(name = "TblIssue.findByReportPersonUuid", query = "SELECT t FROM TblIssue t WHERE t.reportPersonUuid = :reportPersonUuid"),
    @NamedQuery(name = "TblIssue.findByReportPersonName", query = "SELECT t FROM TblIssue t WHERE t.reportPersonName = :reportPersonName"),
    @NamedQuery(name = "TblIssue.findByReportDepartment", query = "SELECT t FROM TblIssue t WHERE t.reportDepartment = :reportDepartment"),
    @NamedQuery(name = "TblIssue.findByReportDepartmentName", query = "SELECT t FROM TblIssue t WHERE t.reportDepartmentName = :reportDepartmentName"),
    @NamedQuery(name = "TblIssue.findByReportPhase", query = "SELECT t FROM TblIssue t WHERE t.reportPhase = :reportPhase"),
    @NamedQuery(name = "TblIssue.findByReportPhaseText", query = "SELECT t FROM TblIssue t WHERE t.reportPhaseText = :reportPhaseText"),
    @NamedQuery(name = "TblIssue.findByReportCategory1", query = "SELECT t FROM TblIssue t WHERE t.reportCategory1 = :reportCategory1"),
    @NamedQuery(name = "TblIssue.findByReportCategory1Text", query = "SELECT t FROM TblIssue t WHERE t.reportCategory1Text = :reportCategory1Text"),
    @NamedQuery(name = "TblIssue.findByReportCategory2", query = "SELECT t FROM TblIssue t WHERE t.reportCategory2 = :reportCategory2"),
    @NamedQuery(name = "TblIssue.findByReportCategory2Text", query = "SELECT t FROM TblIssue t WHERE t.reportCategory2Text = :reportCategory2Text"),
    @NamedQuery(name = "TblIssue.findByReportCategory3", query = "SELECT t FROM TblIssue t WHERE t.reportCategory3 = :reportCategory3"),
    @NamedQuery(name = "TblIssue.findByReportCategory3Text", query = "SELECT t FROM TblIssue t WHERE t.reportCategory3Text = :reportCategory3Text"),
    @NamedQuery(name = "TblIssue.memo01", query = "SELECT t FROM TblIssue t WHERE t.memo01 = :memo01"),
    @NamedQuery(name = "TblIssue.memo02", query = "SELECT t FROM TblIssue t WHERE t.memo02 = :memo02"),
    @NamedQuery(name = "TblIssue.memo03", query = "SELECT t FROM TblIssue t WHERE t.memo03 = :memo03"),
    @NamedQuery(name = "TblIssue.findByIssue", query = "SELECT t FROM TblIssue t WHERE t.issue = :issue"),
    @NamedQuery(name = "TblIssue.findByMeasureDueDate", query = "SELECT t FROM TblIssue t WHERE t.measureDueDate = :measureDueDate"),
    @NamedQuery(name = "TblIssue.findByMeasureStatus", query = "SELECT t FROM TblIssue t WHERE t.measureStatus = :measureStatus"),
    @NamedQuery(name = "TblIssue.findByMeasuerCompletedDate", query = "SELECT t FROM TblIssue t WHERE t.measuerCompletedDate = :measuerCompletedDate"),
    @NamedQuery(name = "TblIssue.findByMeasureSummary", query = "SELECT t FROM TblIssue t WHERE t.measureSummary = :measureSummary"),
    @NamedQuery(name = "TblIssue.findByReportFilePath01", query = "SELECT t FROM TblIssue t WHERE t.reportFilePath01 = :reportFilePath01"),
    @NamedQuery(name = "TblIssue.findByReportFilePath02", query = "SELECT t FROM TblIssue t WHERE t.reportFilePath02 = :reportFilePath02"),
    @NamedQuery(name = "TblIssue.findByReportFilePath03", query = "SELECT t FROM TblIssue t WHERE t.reportFilePath03 = :reportFilePath03"),
    @NamedQuery(name = "TblIssue.findByReportFilePath04", query = "SELECT t FROM TblIssue t WHERE t.reportFilePath04 = :reportFilePath04"),
    @NamedQuery(name = "TblIssue.findByReportFilePath05", query = "SELECT t FROM TblIssue t WHERE t.reportFilePath05 = :reportFilePath05"),
    @NamedQuery(name = "TblIssue.findByReportFilePath06", query = "SELECT t FROM TblIssue t WHERE t.reportFilePath06 = :reportFilePath06"),
    @NamedQuery(name = "TblIssue.findByReportFilePath07", query = "SELECT t FROM TblIssue t WHERE t.reportFilePath07 = :reportFilePath07"),
    @NamedQuery(name = "TblIssue.findByReportFilePath08", query = "SELECT t FROM TblIssue t WHERE t.reportFilePath08 = :reportFilePath08"),
    @NamedQuery(name = "TblIssue.findByReportFilePath09", query = "SELECT t FROM TblIssue t WHERE t.reportFilePath09 = :reportFilePath09"),
    @NamedQuery(name = "TblIssue.findByReportFilePath10", query = "SELECT t FROM TblIssue t WHERE t.reportFilePath10 = :reportFilePath10"),
    @NamedQuery(name = "TblIssue.findByCreateDate", query = "SELECT t FROM TblIssue t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblIssue.findByUpdateDate", query = "SELECT t FROM TblIssue t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblIssue.findByCreateUserUuid", query = "SELECT t FROM TblIssue t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblIssue.findByUpdateUserUuid", query = "SELECT t FROM TblIssue t WHERE t.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "TblIssue.deleteTblIssue", query = "DELETE FROM TblIssue t WHERE t.id = :id ")
})
@Cacheable(value = false)
public class TblIssue implements Serializable {
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
    
    @OneToMany(cascade = {CascadeType.REMOVE,CascadeType.PERSIST}, mappedBy = "tblIssue")
    private Collection<TblIssueImageFile> tblIssueImageFile;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Column(name = "REPORT_DATE")
    @Temporal(TemporalType.DATE)
    private Date reportDate;
    @Column(name = "HAPPENED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date happenedAt;
    @Column(name = "HAPPENED_AT_STZ")
    @Temporal(TemporalType.TIMESTAMP)
    private Date happenedAtStz;
    @Size(max = 45)
    @Column(name = "REPORT_PERSON_UUID")
    private String reportPersonUuid;
    @Size(max = 45)
    @Column(name = "REPORT_PERSON_NAME")
    private String reportPersonName;
    @Column(name = "REPORT_DEPARTMENT")
    private Integer reportDepartment;
    @Size(max = 100)
    @Column(name = "REPORT_DEPARTMENT_NAME")
    private String reportDepartmentName;
    @Column(name = "REPORT_PHASE")
    private Integer reportPhase;
    @Size(max = 100)
    @Column(name = "REPORT_PHASE_TEXT")
    private String reportPhaseText;
    @Column(name = "REPORT_CATEGORY1")
    private Integer reportCategory1;
    @Size(max = 100)
    @Column(name = "REPORT_CATEGORY1_TEXT")
    private String reportCategory1Text;
    @Column(name = "REPORT_CATEGORY2")
    private Integer reportCategory2;
    @Size(max = 100)
    @Column(name = "REPORT_CATEGORY2_TEXT")
    private String reportCategory2Text;
    @Column(name = "REPORT_CATEGORY3")
    private Integer reportCategory3;
    @Size(max = 100)
    @Column(name = "REPORT_CATEGORY3_TEXT")
    private String reportCategory3Text;
    @Size(max = 100)
    @Column(name = "MEMO01")
    private String memo01;
    @Size(max = 100)
    @Column(name = "MEMO02")
    private String memo02;
    @Size(max = 100)
    @Column(name = "MEMO03")
    private String memo03;
    @Size(max = 200)
    @Column(name = "ISSUE")
    private String issue;
    @Column(name = "MEASURE_DUE_DATE")
    @Temporal(TemporalType.DATE)
    private Date measureDueDate;
    @Column(name = "MEASURE_STATUS")
    private Integer measureStatus;
    @Column(name = "MEASUER_COMPLETED_DATE")
    @Temporal(TemporalType.DATE)
    private Date measuerCompletedDate;
    @Size(max = 200)
    @Column(name = "MEASURE_SUMMARY")
    private String measureSummary;
    @Size(max = 256)
    @Column(name = "REPORT_FILE_PATH01")
    private String reportFilePath01;
    @Size(max = 256)
    @Column(name = "REPORT_FILE_PATH02")
    private String reportFilePath02;
    @Size(max = 256)
    @Column(name = "REPORT_FILE_PATH03")
    private String reportFilePath03;
    @Size(max = 256)
    @Column(name = "REPORT_FILE_PATH04")
    private String reportFilePath04;
    @Size(max = 256)
    @Column(name = "REPORT_FILE_PATH05")
    private String reportFilePath05;
    @Size(max = 256)
    @Column(name = "REPORT_FILE_PATH06")
    private String reportFilePath06;
    @Size(max = 256)
    @Column(name = "REPORT_FILE_PATH07")
    private String reportFilePath07;
    @Size(max = 256)
    @Column(name = "REPORT_FILE_PATH08")
    private String reportFilePath08;
    @Size(max = 256)
    @Column(name = "REPORT_FILE_PATH09")
    private String reportFilePath09;
    @Size(max = 256)
    @Column(name = "REPORT_FILE_PATH10")
    private String reportFilePath10;
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
    @Column(name = "COMPONENT_ID")
    private String componentId;
    @Size(max = 100)
    @Column(name = "LOT_NUMBER")
    private String lotNumber;
    @Size(max = 45)
    @Column(name = "PROCEDURE_ID")
    private String procedureId;
    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;
    @JoinColumn(name = "PROCEDURE_ID", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstProcedure mstProcedure;
    
    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "SHOT_COUNT_AT_ISSUE")
    private int shotCountAtIssue;

    @Column(name = "MAINTE_TYPE")
    private int mainteType;

    @Size(max = 45)
    @Column(name = "MOLD_MAINTENANCE_ID") //金型メンテナンスID
    private String mainTenanceId; //金型メンテナンスID
    
    @JoinColumn(name = "MOLD_MAINTENANCE_ID", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TblMoldMaintenanceRemodeling tblMoldMaintenanceRemodeling;
    
    @Size(max = 45)
    @Column(name = "MACHINE_MAINTENANCE_ID") //設備メンテナンスID
    private String machineMainTenanceId; //設備メンテナンスID
    
    @JoinColumn(name = "MACHINE_MAINTENANCE_ID", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TblMachineMaintenanceRemodeling tblMachineMaintenanceRemodeling;
    
    @Size(max = 45)
    @Column(name = "MOLD_UUID")
    private String moldUuid;
    
    @JoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID",insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMold mstMold;
    
    @JoinColumn(name = "MACHINE_UUID", referencedColumnName = "UUID",insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachine mstMachine;
    
    @Column(name = "MACHINE_UUID")
    private String machineUuid;
    
    @PrimaryKeyJoinColumn(name = "REPORT_PERSON_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUser mstUser; // 報告者を取得用

    public MstUser getMstUser() {
        return this.mstUser;
    }

    public void setMstUser(MstUser mstUser) {
        this.mstUser = mstUser;
    }

    public TblIssue() {
    }

    public TblIssue(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public Date getHappenedAt() {
        return happenedAt;
    }

    public void setHappenedAt(Date happenedAt) {
        this.happenedAt = happenedAt;
    }

    public Date getHappenedAtStz() {
        return happenedAtStz;
    }

    public void setHappenedAtStz(Date happenedAtStz) {
        this.happenedAtStz = happenedAtStz;
    }

    public String getReportPersonUuid() {
        return reportPersonUuid;
    }

    public void setReportPersonUuid(String reportPersonUuid) {
        this.reportPersonUuid = reportPersonUuid;
    }

    public String getReportPersonName() {
        return reportPersonName;
    }

    public void setReportPersonName(String reportPersonName) {
        this.reportPersonName = reportPersonName;
    }

    public Integer getReportDepartment() {
        return reportDepartment;
    }

    public void setReportDepartment(Integer reportDepartment) {
        this.reportDepartment = reportDepartment;
    }

    public String getReportDepartmentName() {
        return reportDepartmentName;
    }

    public void setReportDepartmentName(String reportDepartmentName) {
        this.reportDepartmentName = reportDepartmentName;
    }

    public Integer getReportPhase() {
        return reportPhase;
    }

    public void setReportPhase(Integer reportPhase) {
        this.reportPhase = reportPhase;
    }

    public String getReportPhaseText() {
        return reportPhaseText;
    }

    public void setReportPhaseText(String reportPhaseText) {
        this.reportPhaseText = reportPhaseText;
    }

    public Integer getReportCategory1() {
        return reportCategory1;
    }

    public void setReportCategory1(Integer reportCategory1) {
        this.reportCategory1 = reportCategory1;
    }

    public String getReportCategory1Text() {
        return reportCategory1Text;
    }

    public void setReportCategory1Text(String reportCategory1Text) {
        this.reportCategory1Text = reportCategory1Text;
    }

    public Integer getReportCategory2() {
        return reportCategory2;
    }

    public void setReportCategory2(Integer reportCategory2) {
        this.reportCategory2 = reportCategory2;
    }

    public String getReportCategory2Text() {
        return reportCategory2Text;
    }

    public void setReportCategory2Text(String reportCategory2Text) {
        this.reportCategory2Text = reportCategory2Text;
    }

    public Integer getReportCategory3() {
        return reportCategory3;
    }

    public void setReportCategory3(Integer reportCategory3) {
        this.reportCategory3 = reportCategory3;
    }

    public String getReportCategory3Text() {
        return reportCategory3Text;
    }

    public void setReportCategory3Text(String reportCategory3Text) {
        this.reportCategory3Text = reportCategory3Text;
    }
    
    public String getMemo01() {
        return memo01;
    }
    
    public void setMemo01(String memo01) {
        this.memo01 = memo01;
    }
    
    public String getMemo02() {
        return memo02;
    }
    
    public void setMemo02(String memo02) {
        this.memo02 = memo02;
    }
    
    public String getMemo03() {
        return memo03;
    }
    
    public void setMemo03(String memo03) {
        this.memo03 = memo03;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public Date getMeasureDueDate() {
        return measureDueDate;
    }

    public void setMeasureDueDate(Date measureDueDate) {
        this.measureDueDate = measureDueDate;
    }

    public Integer getMeasureStatus() {
        return measureStatus;
    }

    public void setMeasureStatus(Integer measureStatus) {
        this.measureStatus = measureStatus;
    }

    public Date getMeasuerCompletedDate() {
        return measuerCompletedDate;
    }

    public void setMeasuerCompletedDate(Date measuerCompletedDate) {
        this.measuerCompletedDate = measuerCompletedDate;
    }

    public String getMeasureSummary() {
        return measureSummary;
    }

    public void setMeasureSummary(String measureSummary) {
        this.measureSummary = measureSummary;
    }

    public String getReportFilePath01() {
        return reportFilePath01;
    }

    public void setReportFilePath01(String reportFilePath01) {
        this.reportFilePath01 = reportFilePath01;
    }

    public String getReportFilePath02() {
        return reportFilePath02;
    }

    public void setReportFilePath02(String reportFilePath02) {
        this.reportFilePath02 = reportFilePath02;
    }

    public String getReportFilePath03() {
        return reportFilePath03;
    }

    public void setReportFilePath03(String reportFilePath03) {
        this.reportFilePath03 = reportFilePath03;
    }

    public String getReportFilePath04() {
        return reportFilePath04;
    }

    public void setReportFilePath04(String reportFilePath04) {
        this.reportFilePath04 = reportFilePath04;
    }

    public String getReportFilePath05() {
        return reportFilePath05;
    }

    public void setReportFilePath05(String reportFilePath05) {
        this.reportFilePath05 = reportFilePath05;
    }

    public String getReportFilePath06() {
        return reportFilePath06;
    }

    public void setReportFilePath06(String reportFilePath06) {
        this.reportFilePath06 = reportFilePath06;
    }

    public String getReportFilePath07() {
        return reportFilePath07;
    }

    public void setReportFilePath07(String reportFilePath07) {
        this.reportFilePath07 = reportFilePath07;
    }

    public String getReportFilePath08() {
        return reportFilePath08;
    }

    public void setReportFilePath08(String reportFilePath08) {
        this.reportFilePath08 = reportFilePath08;
    }

    public String getReportFilePath09() {
        return reportFilePath09;
    }

    public void setReportFilePath09(String reportFilePath09) {
        this.reportFilePath09 = reportFilePath09;
    }

    public String getReportFilePath10() {
        return reportFilePath10;
    }

    public void setReportFilePath10(String reportFilePath10) {
        this.reportFilePath10 = reportFilePath10;
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
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblIssue)) {
            return false;
        }
        TblIssue other = (TblIssue) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.issue.TblIssue[ id=" + getId() + " ]";
    }

    /**
     * @return the mstComponent
     */
    public MstComponent getMstComponent() {
        return mstComponent;
    }

    /**
     * @param mstComponent the mstComponent to set
     */
    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }

    public MstProcedure getMstProcedure() {
        return mstProcedure;
    }

    public void setMstProcedure(MstProcedure mstProcedure) {
        this.mstProcedure = mstProcedure;
    }

    /**
     * @return the tblMoldMaintenanceRemodeling
     */
    public TblMoldMaintenanceRemodeling getTblMoldMaintenanceRemodeling() {
        return tblMoldMaintenanceRemodeling;
    }

    /**
     * @param tblMoldMaintenanceRemodeling the tblMoldMaintenanceRemodeling to
     * set
     */
    public void setTblMoldMaintenanceRemodeling(TblMoldMaintenanceRemodeling tblMoldMaintenanceRemodeling) {
        this.tblMoldMaintenanceRemodeling = tblMoldMaintenanceRemodeling;
    }

    /**
     * @return the mstMold
     */
    public MstMold getMstMold() {
        return mstMold;
    }

    /**
     * @param mstMold the mstMold to set
     */
    public void setMstMold(MstMold mstMold) {
        this.mstMold = mstMold;
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

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    /**
     * @return the moldUuid
     */
    public String getMoldUuid() {
        return moldUuid;
    }

    /**
     * @param moldUuid the moldUuid to set
     */
    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    /**
     * @return the mainTenanceId
     */
    public String getMainTenanceId() {
        return mainTenanceId;
    }

    /**
     * @param mainTenanceId the mainTenanceId to set
     */
    public void setMainTenanceId(String mainTenanceId) {
        this.mainTenanceId = mainTenanceId;
    }

    /**
     * @return the tblIssueImageFile
     */
    public Collection<TblIssueImageFile> getTblIssueImageFile() {
        return tblIssueImageFile;
    }

    /**
     * @param tblIssueImageFile the tblIssueImageFile to set
     */
    public void setTblIssueImageFile(Collection<TblIssueImageFile> tblIssueImageFile) {
        this.tblIssueImageFile = tblIssueImageFile;
    }

    public MstMachine getMstMachine() {
        return mstMachine;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public void setMstMachine(MstMachine mstMachine) {
        this.mstMachine = mstMachine;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    /**
     * @return the tblMachineMaintenanceRemodeling
     */
    public TblMachineMaintenanceRemodeling getTblMachineMaintenanceRemodeling() {
        return tblMachineMaintenanceRemodeling;
    }

    /**
     * @param tblMachineMaintenanceRemodeling the tblMachineMaintenanceRemodeling to set
     */
    public void setTblMachineMaintenanceRemodeling(TblMachineMaintenanceRemodeling tblMachineMaintenanceRemodeling) {
        this.tblMachineMaintenanceRemodeling = tblMachineMaintenanceRemodeling;
    }

    /**
     * @return the machineMainTenanceId
     */
    public String getMachineMainTenanceId() {
        return machineMainTenanceId;
    }

    /**
     * @param machineMainTenanceId the machineMainTenanceId to set
     */
    public void setMachineMainTenanceId(String machineMainTenanceId) {
        this.machineMainTenanceId = machineMainTenanceId;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the shotCountAtIssue
     */
    public int getShotCountAtIssue() {
        return shotCountAtIssue;
    }

    /**
     * @param shotCountAtIssue the shotCountAtIssue to set
     */
    public void setShotCountAtIssue(int shotCountAtIssue) {
        this.shotCountAtIssue = shotCountAtIssue;
    }

    /**
     * @return the mainteType
     */
    public int getMainteType() {
        return mainteType;
    }

    /**
     * @param mainteType the mainteType to set
     */
    public void setMainteType(int mainteType) {
        this.mainteType = mainteType;
    }
}
