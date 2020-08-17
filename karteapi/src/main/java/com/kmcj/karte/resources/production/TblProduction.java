/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production;

import com.kmcj.karte.resources.direction.TblDirection;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.production.detail.TblProductionDetail;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.resources.work.phase.MstWorkPhase;
import com.kmcj.karte.util.XmlDateAdapter2;
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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * TblProductionエンティティ
 * @author t.ariki
 */
@Entity
@Table(name = "tbl_production")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblProduction.findAll", query = "SELECT t FROM TblProduction t"),
    @NamedQuery(name = "TblProduction.findById", query = "SELECT t FROM TblProduction t WHERE t.id = :id"),
    @NamedQuery(name = "TblProduction.findByProductionDate", query = "SELECT t FROM TblProduction t WHERE t.productionDate = :productionDate"),
    @NamedQuery(name = "TblProduction.findByStartDatetime", query = "SELECT t FROM TblProduction t WHERE t.startDatetime = :startDatetime"),
    @NamedQuery(name = "TblProduction.findByStartDatetimeStz", query = "SELECT t FROM TblProduction t WHERE t.startDatetimeStz = :startDatetimeStz"),
    @NamedQuery(name = "TblProduction.findByEndDatetime", query = "SELECT t FROM TblProduction t WHERE t.endDatetime = :endDatetime"),
    @NamedQuery(name = "TblProduction.findByEndDatetimeStz", query = "SELECT t FROM TblProduction t WHERE t.endDatetimeStz = :endDatetimeStz"),
    @NamedQuery(name = "TblProduction.findByProducingTimeMinutes", query = "SELECT t FROM TblProduction t WHERE t.producingTimeMinutes = :producingTimeMinutes"),
    @NamedQuery(name = "TblProduction.findByPersonUuid", query = "SELECT t FROM TblProduction t WHERE t.personUuid = :personUuid"),
    @NamedQuery(name = "TblProduction.findByProductionPhase", query = "SELECT t FROM TblProduction t WHERE t.productionPhase = :productionPhase"),
    @NamedQuery(name = "TblProduction.findByWorkCategory", query = "SELECT t FROM TblProduction t WHERE t.workCategory = :workCategory"),
    @NamedQuery(name = "TblProduction.findByDirectionId", query = "SELECT t FROM TblProduction t WHERE t.directionId = :directionId"),
    @NamedQuery(name = "TblProduction.findByMoldUuid", query = "SELECT t FROM TblProduction t WHERE t.moldUuid = :moldUuid"),
    @NamedQuery(name = "TblProduction.findByMachineUuid", query = "SELECT t FROM TblProduction t WHERE t.machineUuid = :machineUuid"),
    @NamedQuery(name = "TblProduction.countByMoldId", query = "SELECT COUNT(t) FROM TblProduction t WHERE t.mstMold.moldId = :moldId"),
    //@NamedQuery(name = "TblProduction.countByMachineId", query = "SELECT COUNT(t) FROM TblProduction t WHERE t.mstMachine.machineId = :machineId"),
    @NamedQuery(name = "TblProduction.findByWorkCode", query = "SELECT t FROM TblProduction t WHERE t.workCode = :workCode"),
    @NamedQuery(name = "TblProduction.findByProcCd", query = "SELECT t FROM TblProduction t WHERE t.procCd = :procCd"),
    @NamedQuery(name = "TblProduction.findByPrevLotNumber", query = "SELECT t FROM TblProduction t WHERE t.prevLotNumber = :prevLotNumber"),
    @NamedQuery(name = "TblProduction.findByLotNumber", query = "SELECT t FROM TblProduction t WHERE t.lotNumber = :lotNumber"),
    @NamedQuery(name = "TblProduction.findByShotCount", query = "SELECT t FROM TblProduction t WHERE t.shotCount = :shotCount"),
    @NamedQuery(name = "TblProduction.findByDisposedShotCount", query = "SELECT t FROM TblProduction t WHERE t.disposedShotCount = :disposedShotCount"),
    @NamedQuery(name = "TblProduction.findByCreateDate", query = "SELECT t FROM TblProduction t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblProduction.findByUpdateDate", query = "SELECT t FROM TblProduction t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblProduction.findByCreateUserUuid", query = "SELECT t FROM TblProduction t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblProduction.findByUpdateUserUuid", query = "SELECT t FROM TblProduction t WHERE t.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "TblProduction.delete", query = "DELETE FROM TblProduction t WHERE t.id = :id"),
    // 　設備マスタ削除する前にＦＫチェックＳＱＬ文
    @NamedQuery(name = "TblProduction.findFkByMachineUuid", query = "SELECT t FROM TblProduction t WHERE t.machineUuid = :machineUuid "),
    @NamedQuery(name = "TblProduction.findMaxEndDateByMoldUuid", query = "SELECT t FROM TblProduction t WHERE t.moldUuid = :moldUuid AND t.endDatetime IS NOT NULL ORDER BY t.endDatetime DESC"),
    @NamedQuery(name = "TblProduction.findMaxEndDateByMachineUuid", query = "SELECT t FROM TblProduction t WHERE t.machineUuid = :machineUuid AND t.endDatetime IS NOT NULL ORDER BY t.endDatetime DESC"),
})
@Cacheable(value = false)
public class TblProduction implements Serializable {

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
    
    /**
     * 結合テーブル定義
     */
    // ユーザーマスタ
    @PrimaryKeyJoinColumn(name = "PERSON_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUser mstUser;
    public MstUser getMstUser() {
        return this.mstUser;
    }
    public void setMstUser(MstUser mstUser) {
        this.mstUser = mstUser;
    }
    
    // 手配テーブル
    @PrimaryKeyJoinColumn(name = "DIRECTION_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblDirection tblDirection;
    public TblDirection getTblDirection() {
        return this.tblDirection;
    }
    public void setTblDirection(TblDirection tblDirection) {
        this.tblDirection = tblDirection;
    }
    
    // 金型マスタ
    @PrimaryKeyJoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMold mstMold;
    public MstMold getMstMold() {
        return this.mstMold;
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
    
    //工程マスタ
    /*
    @PrimaryKeyJoinColumn(name = "PROC_CD",referencedColumnName = "PROCEDURE_CODE")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstProcedure mstProcedure;
    public MstProcedure getMstProcedure() {
        return mstProcedure;
    }
    public void setMstProcedure(MstProcedure mstProcedure) {
        this.mstProcedure = mstProcedure;
    }
    */

    //作業工程マスタ
    @PrimaryKeyJoinColumn(name = "WORK_PHASE_ID",referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstWorkPhase mstWorkPhase;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Column(name = "PRODUCTION_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter2.class)
    private Date productionDate;
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
    @Column(name = "PRODUCING_TIME_MINUTES")
    private Integer producingTimeMinutes;
    @Column(name = "SUSPENDED_TIME_MINUTES")
    private Integer suspendedTimeMinutes;
    @Column(name = "NET_PRODUCINT_TIME_MINUTES")
    private Integer netProducintTimeMinutes;
    @Column(name = "STATUS")
    private Integer status;
    @Size(max = 45)
    @Column(name = "PERSON_UUID")
    private String personUuid;
    @Column(name = "PRODUCTION_PHASE")
    private Integer productionPhase;
    @Column(name = "WORK_CATEGORY")
    private Integer workCategory;
    @Size(max = 45)
    @Column(name = "DIRECTION_ID")
    private String directionId;
    @Size(max = 45)
    @Column(name = "MOLD_UUID")
    private String moldUuid;
    @Size(max = 45)
    @Column(name = "MACHINE_UUID")
    private String machineUuid;
    @Size(max = 45)
    @Column(name = "WORK_PHASE_ID")
    private String workPhaseId;
    @Size(max = 45)
    @Column(name = "WORK_CODE")
    private String workCode;
    @Size(max = 45)
    @Column(name = "PROC_CD")
    private String procCd;
    @Size(max = 100)
    @Column(name = "PREV_LOT_NUMBER")
    private String prevLotNumber;
    @Size(max = 100)
    @Column(name = "LOT_NUMBER")
    private String lotNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SHOT_COUNT")
    private int shotCount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DISPOSED_SHOT_COUNT")
    private int disposedShotCount;
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
    @Column(name = "PROD_DEPARTMENT")
    private Integer prodDepartment;
    @OneToMany(mappedBy = "productionId")
    private Collection<TblProductionDetail> tblProductionDetailCollection;
    
    @Transient
    private TblProductionDetail firstDetail;
    
    /*
     * マスタ系テーブル項目定義
     */
//    // 作業工程マスタ
//    @Transient
//    private Integer workPhaseChoiceSeq; // 作業工程SEQ(生産では「生産工程」)
//    @Transient
//    private String productionPhaseName; // 生産工程名称(作業工程マスタ.作業工程名称)
    // 選択肢マスタ
    @Transient
    private String workCategoryName;    // 作業内容名称
    // ユーザーマスタ
    @Transient
    private String userId;         // ユーザーID
    @Transient
    private String userName;       // ユーザー名称
    @Transient
    private Integer department;    // 部署
    @Transient
    private String departmentName; // 部署名
    // 手配テーブル
    @Transient
    private String directionCode; // 手配・工事コード    
    // 金型マスタ
    @Transient
    private String moldId;    // 金型ID
    @Transient
    private String moldName;  // 金型名称
    @Transient
    private Integer moldType; // 金型種類
    @Transient
    // 生産実績残高 前ロット番号絞り込み用
    private Integer processNumber;  // 工程番号    
    @Transient
    private Integer machineType;
    /*
     * 一括反映時制御フラグ
     */
    @Transient
    private boolean deleted = false;    // 削除対象制御
    @Transient
    private boolean modified = false;   // 更新対象制御
    @Transient
    private boolean added = false;      // 登録対象制御
    
    /*
     * 工程マスタ 部品マスタなどの項目(他チームのVO項目追加を定義)
     */
    @Transient
    private String procedureName;
    @Transient
    private String procedureId;
    @Transient
    private String componentId;
    @Transient
    private String componentCode;
    @Transient
    private String componentName;

    public TblProduction() {
    }

    public TblProduction(String id) {
        this.id = id;
    }

    public TblProduction(String id, int shotCount, int disposedShotCount) {
        this.id = id;
        this.shotCount = shotCount;
        this.disposedShotCount = disposedShotCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
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

    public Integer getProducingTimeMinutes() {
        return producingTimeMinutes;
    }

    public void setProducingTimeMinutes(Integer producingTimeMinutes) {
        this.producingTimeMinutes = producingTimeMinutes;
    }

    public String getPersonUuid() {
        return personUuid;
    }

    public void setPersonUuid(String personUuid) {
        this.personUuid = personUuid;
    }

    public Integer getProductionPhase() {
        return productionPhase;
    }

    public void setProductionPhase(Integer productionPhase) {
        this.productionPhase = productionPhase;
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

    public String getPrevLotNumber() {
        return prevLotNumber;
    }

    public void setPrevLotNumber(String prevLotNumber) {
        this.prevLotNumber = prevLotNumber;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public int getShotCount() {
        return shotCount;
    }

    public void setShotCount(int shotCount) {
        this.shotCount = shotCount;
    }

    public int getDisposedShotCount() {
        return disposedShotCount;
    }

    public void setDisposedShotCount(int disposedShotCount) {
        this.disposedShotCount = disposedShotCount;
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
    public Collection<TblProductionDetail> getTblProductionDetailCollection() {
        return tblProductionDetailCollection;
    }

    public void setTblProductionDetailCollection(Collection<TblProductionDetail> tblProductionDetailCollection) {
        this.tblProductionDetailCollection = tblProductionDetailCollection;
    }

//    public Integer getWorkPhaseChoiceSeq() {
//        return workPhaseChoiceSeq;
//    }
//
//    public void setWorkPhaseChoiceSeq(Integer workPhaseChoiceSeq) {
//        this.workPhaseChoiceSeq = workPhaseChoiceSeq;
//    }
//
//    public String getProductionPhaseName() {
//        return productionPhaseName;
//    }
//
//    public void setProductionPhaseName(String productionPhaseName) {
//        this.productionPhaseName = productionPhaseName;
//    }

    public String getWorkCategoryName() {
        return workCategoryName;
    }

    public void setWorkCategoryName(String workCategoryName) {
        this.workCategoryName = workCategoryName;
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

    public String getDirectionCode() {
        return directionCode;
    }

    public void setDirectionCode(String directionCode) {
        this.directionCode = directionCode;
    }

    public String getMoldId() {
        return moldId;
    }

    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    public String getMoldName() {
        return moldName;
    }

    public void setMoldName(String moldName) {
        this.moldName = moldName;
    }

    public Integer getMoldType() {
        return moldType;
    }

    public void setMoldType(Integer moldType) {
        this.moldType = moldType;
    }

    public Integer getProcessNumber() {
        return processNumber;
    }

    public void setProcessNumber(Integer processNumber) {
        this.processNumber = processNumber;
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
    
    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblProduction)) {
            return false;
        }
        TblProduction other = (TblProduction) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.production.TblProduction[ id=" + getId() + " ]";
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
    
    public Integer getMachineType() {
        return machineType;
    }

    public void setMachineType(Integer machineType) {
        this.machineType = machineType;
    }

    /**
     * @return the workPhaseId
     */
    public String getWorkPhaseId() {
        return workPhaseId;
    }

    /**
     * @param workPhaseId the workPhaseId to set
     */
    public void setWorkPhaseId(String workPhaseId) {
        this.workPhaseId = workPhaseId;
    }

    /**
     * @return the mstWorkPhase
     */
    public MstWorkPhase getMstWorkPhase() {
        return mstWorkPhase;
    }

    /**
     * @param mstWorkPhase the mstWorkPhase to set
     */
    public void setMstWorkPhase(MstWorkPhase mstWorkPhase) {
        this.mstWorkPhase = mstWorkPhase;
    }

    /**
     * @return the suspendedTimeMinutes
     */
    public Integer getSuspendedTimeMinutes() {
        return suspendedTimeMinutes;
    }

    /**
     * @param suspendedTimeMinutes the suspendedTimeMinutes to set
     */
    public void setSuspendedTimeMinutes(Integer suspendedTimeMinutes) {
        this.suspendedTimeMinutes = suspendedTimeMinutes;
    }

    /**
     * @return the netProducintTimeMinutes
     */
    public Integer getNetProducintTimeMinutes() {
        return netProducintTimeMinutes;
    }

    /**
     * @param netProducintTimeMinutes the netProducintTimeMinutes to set
     */
    public void setNetProducintTimeMinutes(Integer netProducintTimeMinutes) {
        this.netProducintTimeMinutes = netProducintTimeMinutes;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the firstDetail
     */
    @XmlElement
    public TblProductionDetail getFirstDetail() {
        return firstDetail;
    }

    /**
     * @param firstDetail the firstDetail to set
     */
    public void setFirstDetail(TblProductionDetail firstDetail) {
        this.firstDetail = firstDetail;
    }

    public Integer getProdDepartment() {
        return prodDepartment;
    }

    public void setProdDepartment(Integer prodDepartment) {
        this.prodDepartment = prodDepartment;
    }
 
}
