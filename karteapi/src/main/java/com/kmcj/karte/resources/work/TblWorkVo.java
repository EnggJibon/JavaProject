/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.work;
import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 * 作業実績テーブル JSON送受信用クラス
 *  生産終了入力時の同時登録時に使用想定
 * @author t.ariki
 */
public class TblWorkVo extends BasicResponse {
    /**
     * テーブル定義と同一内容
     */
    private String id;
    private Date workingDate;
    private Date startDatetime;
    private Date startDatetimeStz;
    private Date endDatetime;
    private Date endDatetimeStz;
    private Integer workingTimeMinutes;
    private Integer actualTimeMinutes;
    private Integer breakTimeMinutes;
    private String personUuid;
    private String workPhaseId;
    private Integer workCategory;
    private String directionId;
    private String componentId;
    private String procedureId;
    private String moldUuid;
    private String workCode;
    private String procCd;
    private int locked;
    private String maintenanceId;
    private Date createDate;
    private Date updateDate;
    private String createUserUuid;
    private String updateUserUuid;
    
    /**
     * 独自定義内容
     */
    private Integer workPhaseChoiceSeq; // 作業工程SEQ
    private String directionCode;       // 手配・工事コード
    private String componentCode;       // 部品コード
    private String moldId;              // 金型ID
    private String workPhaseName;       // 作業工程名称
    private String workCategoryName;    // 作業内容名称
    private String componentName;       // 部品名
    private String userName;            // ユーザー名称
    private Integer department;         // 部署
    private String departmentName;      // 部署名
    // 一括反映時制御フラグ
    private boolean deleted = false;    // 削除対象制御
    private boolean modified = false;   // 更新対象制御
    private boolean added = false;      // 登録対象制御

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
    
    
}
