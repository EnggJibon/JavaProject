/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.work;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.direction.TblDirection;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.resources.work.phase.MstWorkPhase;
import com.kmcj.karte.util.XmlDateAdapter2;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * TblWorkエンティティ
 * @author t.ariki
 */
@Entity
@Table(name = "tbl_work")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblWork.findAll", query = "SELECT t FROM TblWork t"),
    @NamedQuery(name = "TblWork.findById", query = "SELECT t FROM TblWork t WHERE t.id = :id"),
    @NamedQuery(name = "TblWork.findByWorkingDate", query = "SELECT t FROM TblWork t WHERE t.workingDate = :workingDate"),
    @NamedQuery(name = "TblWork.findByStartDatetime", query = "SELECT t FROM TblWork t WHERE t.startDatetime = :startDatetime"),
    @NamedQuery(name = "TblWork.findByStartDatetimeStz", query = "SELECT t FROM TblWork t WHERE t.startDatetimeStz = :startDatetimeStz"),
    @NamedQuery(name = "TblWork.findByEndDatetime", query = "SELECT t FROM TblWork t WHERE t.endDatetime = :endDatetime"),
    @NamedQuery(name = "TblWork.findByEndDatetimeStz", query = "SELECT t FROM TblWork t WHERE t.endDatetimeStz = :endDatetimeStz"),
    @NamedQuery(name = "TblWork.findByWorkingTimeMinutes", query = "SELECT t FROM TblWork t WHERE t.workingTimeMinutes = :workingTimeMinutes"),
    @NamedQuery(name = "TblWork.findByActualTimeMinutes", query = "SELECT t FROM TblWork t WHERE t.actualTimeMinutes = :actualTimeMinutes"),
    @NamedQuery(name = "TblWork.findByBreakTimeMinutes", query = "SELECT t FROM TblWork t WHERE t.breakTimeMinutes = :breakTimeMinutes"),
    @NamedQuery(name = "TblWork.findByPersonUuid", query = "SELECT t FROM TblWork t WHERE t.personUuid = :personUuid"),
    @NamedQuery(name = "TblWork.findByPersonUuidOrderByStartDatetime", query = "SELECT t FROM TblWork t WHERE t.personUuid = :personUuid  ORDER BY t.startDatetime ASC "),
    @NamedQuery(name = "TblWork.findByWorkPhaseId", query = "SELECT t FROM TblWork t WHERE t.workPhaseId = :workPhaseId"),
    @NamedQuery(name = "TblWork.findByWorkCategory", query = "SELECT t FROM TblWork t WHERE t.workCategory = :workCategory"),
    @NamedQuery(name = "TblWork.findByDirectionId", query = "SELECT t FROM TblWork t WHERE t.directionId = :directionId"),
    @NamedQuery(name = "TblWork.findByComponentId", query = "SELECT t FROM TblWork t WHERE t.componentId = :componentId"),
    @NamedQuery(name = "TblWork.findByProcedureId", query = "SELECT t FROM TblWork t WHERE t.procedureId = :procedureId"),
    @NamedQuery(name = "TblWork.findByMoldUuid", query = "SELECT t FROM TblWork t WHERE t.moldUuid = :moldUuid"),
    @NamedQuery(name = "TblWork.findByWorkCode", query = "SELECT t FROM TblWork t WHERE t.workCode = :workCode"),
    @NamedQuery(name = "TblWork.findByProcCd", query = "SELECT t FROM TblWork t WHERE t.procCd = :procCd"),
    @NamedQuery(name = "TblWork.findByLocked", query = "SELECT t FROM TblWork t WHERE t.locked = :locked"),
    @NamedQuery(name = "TblWork.findByMaintenanceId", query = "SELECT t FROM TblWork t WHERE t.maintenanceId = :maintenanceId"),
    @NamedQuery(name = "TblWork.findByCreateDate", query = "SELECT t FROM TblWork t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblWork.findByUpdateDate", query = "SELECT t FROM TblWork t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblWork.findByCreateUserUuid", query = "SELECT t FROM TblWork t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblWork.findByUpdateUserUuid", query = "SELECT t FROM TblWork t WHERE t.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "TblWork.delete", query = "DELETE FROM TblWork t WHERE t.id = :id"),
})
@Cacheable(value = false)
public class TblWork implements Serializable {
    private static final long serialVersionUID = 1L;
        
    /**
     * 結合テーブル定義
     */
    // ユーザーマスタ
    @PrimaryKeyJoinColumn(name = "PERSON_UUID", referencedColumnName = "UUID")
    //@ManyToOne(optional = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUser mstUser;
    public MstUser getMstUser() {
        return this.mstUser;
    }
    public void setMstUser(MstUser mstUser) {
        this.mstUser = mstUser;
    }
    
    // 作業工程マスタ
    @PrimaryKeyJoinColumn(name = "WORK_PHASE_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstWorkPhase mstWorkPhase;
    public MstWorkPhase getMstWorkPhase() {
        return mstWorkPhase;
    }
    public void setMstWorkPhase(MstWorkPhase mstWorkPhase) {
        this.mstWorkPhase = mstWorkPhase;
    }
    
    // 手配テーブル
    @PrimaryKeyJoinColumn(name = "DIRECTION_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblDirection tblDirection;
    public TblDirection getTblDirection() {
        return tblDirection;
    }
    public void setTblDirection(TblDirection tblDirection) {
        this.tblDirection = tblDirection;
    }
    
    // 部品マスタ
    @PrimaryKeyJoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;
    public MstComponent getMstComponent() {
        return mstComponent;
    }
    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }
    
    // 金型マスタ
    @PrimaryKeyJoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMold mstMold;
    public MstMold getMstMold() {
        return mstMold;
    }
    public void setMstMold(MstMold mstMold) {
        this.mstMold = mstMold;
    }
    
    // 設備マスタ
    @PrimaryKeyJoinColumn(name = "MACHINE_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachine mstMachine;
    public MstMachine getMstMachine() {
        return this.mstMachine;
    }
    public void setMstMachine(MstMachine mstMachine) {
        this.mstMachine = mstMachine;
    }

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "WORKING_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter2.class)
    private Date workingDate;
    @Column(name = "START_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter2.class)
    private Date startDatetime;
    @Column(name = "START_DATETIME_STZ")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDatetimeStz;
    @Column(name = "END_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter2.class)
    private Date endDatetime;
    @Column(name = "END_DATETIME_STZ")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDatetimeStz;
    @Column(name = "WORKING_TIME_MINUTES")
    private Integer workingTimeMinutes;
    @Column(name = "ACTUAL_TIME_MINUTES")
    private Integer actualTimeMinutes;
    @Column(name = "BREAK_TIME_MINUTES")
    private Integer breakTimeMinutes;
    @Size(max = 45)
    @Column(name = "PERSON_UUID")
    private String personUuid;
    @Size(max = 45)
    @Column(name = "WORK_PHASE_ID")
    private String workPhaseId;
    @Column(name = "WORK_CATEGORY")
    private Integer workCategory;
    @Size(max = 45)
    @Column(name = "DIRECTION_ID")
    private String directionId;
    @Size(max = 45)
    @Column(name = "COMPONENT_ID")
    private String componentId;
    @Size(max = 45)
    @Column(name = "PROCEDURE_ID")
    private String procedureId;
    @Size(max = 45)
    @Column(name = "MOLD_UUID")
    private String moldUuid;
    
    @Size(max = 45)
    @Column(name = "MACHINE_UUID")
    private String machineUuid;
    
    @Size(max = 45)
    @Column(name = "WORK_CODE")
    private String workCode;
    @Size(max = 45)
    @Column(name = "PROC_CD")
    private String procCd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LOCKED")
    private int locked;
    @Size(max = 45)
    @Column(name = "MAINTENANCE_ID")
    private String maintenanceId;
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
    
    /*
     * マスタテーブル類の項目設定
     */
    @Transient
    private Integer workPhaseChoiceSeq; // 作業工程SEQ
    @Transient
    private String directionCode;       // 手配・工事コード
    @Transient
    private String componentCode;       // 部品コード
    @Transient
    private String moldId;              // 金型ID
    @Transient
    private String moldName;              // 金型名称
    @Transient
    private String workPhaseName;       // 作業工程名称
    @Transient
    private String workCategoryName;    // 作業内容名称
    @Transient
    private String componentName;       // 部品名
    @Transient
    private String userId;              // ユーザーID
    @Transient
    private String userName;            // ユーザー名称
    @Transient
    private Integer department;         // 部署
    @Transient
    private String departmentName;      // 部署名
    
    @Transient
    private boolean deleted = false;    // 削除対象制御
    @Transient
    private boolean modified = false;   // 更新対象制御
    @Transient
    private boolean added = false;      // 登録対象制御
    
    //部品複数登録
    @Transient
    private List<String> componentIdArray;
    
    @Transient
    private String productionId;      // 機械日報　作業開始画面に遷移し、作業登録がされるとき
    
    @Transient
    private boolean isStart;      // 作業開始画面メンテサイクル管理用フラグ
    
    @Transient
    private String machineId;      // 設備ID

    @Transient
    private String startDatetimeStr;

    @Transient
    private String endDatetimeStr;


    public TblWork() {
    }

    public TblWork(String id) {
        this.id = id;
    }

    public TblWork(String id, Date workingDate, int locked) {
        this.id = id;
        this.workingDate = workingDate;
        this.locked = locked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getWorkingDate() {
        return workingDate;
    }

    public void setWorkingDate(Date workingDate) {
        this.workingDate = workingDate;
    }

    public Date getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Date getStartDatetimeStz() {
        return startDatetimeStz;
    }

    public void setStartDatetimeStz(Date startDatetimeStz) {
        this.startDatetimeStz = startDatetimeStz;
    }

    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    public Date getEndDatetimeStz() {
        return endDatetimeStz;
    }

    public void setEndDatetimeStz(Date endDatetimeStz) {
        this.endDatetimeStz = endDatetimeStz;
    }

    public Integer getWorkingTimeMinutes() {
        return workingTimeMinutes;
    }

    public void setWorkingTimeMinutes(Integer workingTimeMinutes) {
        this.workingTimeMinutes = workingTimeMinutes;
    }

    public Integer getActualTimeMinutes() {
        return actualTimeMinutes;
    }

    public void setActualTimeMinutes(Integer actualTimeMinutes) {
        this.actualTimeMinutes = actualTimeMinutes;
    }

    public Integer getBreakTimeMinutes() {
        return breakTimeMinutes;
    }

    public void setBreakTimeMinutes(Integer breakTimeMinutes) {
        this.breakTimeMinutes = breakTimeMinutes;
    }

    public String getPersonUuid() {
        return personUuid;
    }

    public void setPersonUuid(String personUuid) {
        this.personUuid = personUuid;
    }

    public String getWorkPhaseId() {
        return workPhaseId;
    }

    public void setWorkPhaseId(String workPhaseId) {
        this.workPhaseId = workPhaseId;
    }

    public Integer getWorkCategory() {
        return workCategory;
    }

    public void setWorkCategory(Integer workCategory) {
        this.workCategory = workCategory;
    }

    public String getDirectionId() {
        return directionId;
    }

    public void setDirectionId(String directionId) {
        this.directionId = directionId;
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

    public String getMoldUuid() {
        return moldUuid;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    public String getWorkCode() {
        return workCode;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }

    public String getProcCd() {
        return procCd;
    }

    public void setProcCd(String procCd) {
        this.procCd = procCd;
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public String getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(String maintenanceId) {
        this.maintenanceId = maintenanceId;
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
    
    public Integer getWorkPhaseChoiceSeq() {
        return workPhaseChoiceSeq;
    }

    public void setWorkPhaseChoiceSeq(Integer workPhaseChoiceSeq) {
        this.workPhaseChoiceSeq = workPhaseChoiceSeq;
    }

    public String getDirectionCode() {
        return directionCode;
    }

    public void setDirectionCode(String directionCode) {
        this.directionCode = directionCode;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public String getMoldId() {
        return moldId;
    }

    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }
    
    public String getWorkPhaseName() {
        return workPhaseName;
    }

    public void setWorkPhaseName(String workPhaseName) {
        this.workPhaseName = workPhaseName;
    }
    
    public String getWorkCategoryName() {
        return workCategoryName;
    }

    public void setWorkCategoryName(String workCategoryName) {
        this.workCategoryName = workCategoryName;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

    public String getStartDatetimeStr() {
        return startDatetimeStr;
    }

    public void setStartDatetimeStr(String startDatetimeStr) {
        this.startDatetimeStr = startDatetimeStr;
    }

    public String getEndDatetimeStr() {
        return endDatetimeStr;
    }

    public void setEndDatetimeStr(String endDatetimeStr) {
        this.endDatetimeStr = endDatetimeStr;
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
        if (!(object instanceof TblWork)) {
            return false;
        }
        TblWork other = (TblWork) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.work.TblWork[ id=" + id + " ]";
    }

    /**
     * @return the machineUuid
     */
    public String getMachineUuid() {
        return machineUuid;
    }

    /**
     * @param machineUuid the machineUuid to set
     */
    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }
    
    /**
     * @return the moldName
     */
    public String getMoldName() {
        return moldName;
    }

    /**
     * @param moldName the moldName to set
     */
    public void setMoldName(String moldName) {
        this.moldName = moldName;
    }

    /**
     * @return the componentIdArray
     */
    public List<String> getComponentIdArray() {
        return componentIdArray;
    }

    /**
     * @param componentIdArray the componentIdArray to set
     */
    public void setComponentIdArray(List<String> componentIdArray) {
        this.componentIdArray = componentIdArray;
    }

    /**
     * @return the productionId
     */
    public String getProductionId() {
        return productionId;
    }

    /**
     * @param productionId the productionId to set
     */
    public void setProductionId(String productionId) {
        this.productionId = productionId;
    }
    
    /**
     * @return the isStart
     */
    public boolean getIsStart() {
        return isStart;
    }

    /**
     * @param isStart the isStart to set
     */
    public void setIsStart(boolean isStart) {
        this.isStart = isStart;
    }
    
    /**
     * @return the machineId
     */
    public String getMachineId() {
        return machineId;
    }

    /**
     * @param machineId the machineId to set
     */
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }
}
