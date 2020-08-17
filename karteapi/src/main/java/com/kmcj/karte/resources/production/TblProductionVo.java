/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.production.detail.TblProductionDetailVo;
import com.kmcj.karte.resources.work.TblWorkVo;
import com.kmcj.karte.resources.work.phase.MstWorkPhase;
import com.kmcj.karte.util.XmlDateAdapter2;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 生産実績テーブル JSON送受信用クラス
 *
 * @author t.ariki
 */
public class TblProductionVo extends BasicResponse {

    /**
     * 作業実績テーブル
     */
    private TblWorkVo tblWorkVo;

    /**
     * 生産実績明細テーブルリスト定義
     */
    private List<TblProductionDetailVo> tblProductionDetailVos;

    private TblProduction tblProduction;

    /**
     * テーブル定義と同一内容
     */
    private String id;
    @XmlJavaTypeAdapter(XmlDateAdapter2.class)
    private Date productionDate;
    @XmlJavaTypeAdapter(XmlDateAdapter2.class)
    private Date startDatetime;
    private Date startDatetimeStz;
    @XmlJavaTypeAdapter(XmlDateAdapter2.class)
    private Date endDatetime;
    private Date endDatetimeStz;
    private Integer producingTimeMinutes;
    private String personUuid;
    private String personName;
    private Integer productionPhase;
    private Integer workCategory;
    private String directionId;
    private String moldUuid;
    private String machineUuid;
    private String workPhaseId;
    private String workCode;
    private String procCd;
    private String prevLotNumber;
    private String lotNumber;
    private int shotCount;
    private int disposedShotCount;
    private Date createDate;
    private Date updateDate;
    private String createUserUuid;
    private String updateUserUuid;
    private Integer suspendedTimeMinutes;
    private Integer netProducintTimeMinutes;
    private Integer status;
    private String statusText;
    
    private Integer isEdit;
    private boolean isStart;
    
    private int netProducintTimeMinutesBeforeUpd;
    private int shotCountBeforeUpd;
    private int disposedShotCountBeforeUpd;
    
    private Integer prodDepartment;
    /** 機械日報画面生産終了チェックボックス用 */
    private boolean productionEndFlg;
    
    /** 機械日報一覧画面用作成済み日報の最大の日付 */
    private String maxDailyReportMMDD;
    private int maxDailyReportColor;
    
    //-------------Apeng 20471106 add------------
    private int poNumberCount;
    private String productionLotNumber;

    /**
     * 独自定義内容
     */
    // 選択肢マスタ
    private String workCategoryName;    // 作業内容名称
    // ユーザーマスタ
    private String userId;         // ユーザーID
    private String userName;       // ユーザー名称
    private Integer department;    // 部署
    private String departmentName; // 部署名
    // 手配テーブル
    private String directionCode; // 手配・工事コード    
    // 金型マスタ
    private String moldId;    // 金型ID
    private String moldName;  // 金型名称
    private Integer moldType; // 金型種類
    // 作業工程マスタ
    private Integer workPhaseChoiceSeq; // SEQ

    private String machineId;
    private String machineName;
    private Integer machineType;

    // 生産実績残高 前ロット番号絞り込み用
    private Integer processNumber;  // 工程番号
    // 一括反映時制御フラグ
    private boolean deleted = false;    // 削除対象制御
    private boolean modified = false;   // 更新対象制御
    private boolean added = false;      // 登録対象制御

    private String procedureName;
    private String procedureId;
    private String componentId;
    private String componentCode;
    private String componentName;

    private String productionDateStr;
    private String startDatetimeStr;
    private String startDatetimeStzStr;
    private String endDatetimeStr;
    private String endDatetimeStzStr;
    
    private int structureFlg;

    private MstWorkPhase mstWorkPhase;
    
    public TblWorkVo getTblWorkVo() {
        return tblWorkVo;
    }

    public void setTblWorkVo(TblWorkVo tblWorkVo) {
        this.tblWorkVo = tblWorkVo;
    }

    public List<TblProductionDetailVo> getTblProductionDetailVos() {
        return tblProductionDetailVos;
    }

    public void setTblProductionDetailVos(List<TblProductionDetailVo> tblProductionDetailVos) {
        this.tblProductionDetailVos = tblProductionDetailVos;
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

    public String getMachineUuid() {
        return machineUuid;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public String getWorkPhaseId() {
        return workPhaseId;
    }

    public void setWorkPhaseId(String workPhaseId) {
        this.workPhaseId = workPhaseId;
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

    public Integer getWorkPhaseChoiceSeq() {
        return workPhaseChoiceSeq;
    }

    public void setWorkPhaseChoiceSeq(Integer workPhaseChoiceSeq) {
        this.workPhaseChoiceSeq = workPhaseChoiceSeq;
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

    public TblProduction getTblProduction() {
        return tblProduction;
    }

    public void setTblProduction(TblProduction tblProduction) {
        this.tblProduction = tblProduction;
    }

    public String getProductionDateStr() {
        return productionDateStr;
    }

    public void setProductionDateStr(String productionDateStr) {
        this.productionDateStr = productionDateStr;
    }

    public String getStartDatetimeStr() {
        return startDatetimeStr;
    }

    public String getStartDatetimeStzStr() {
        return startDatetimeStzStr;
    }

    public String getEndDatetimeStr() {
        return endDatetimeStr;
    }

    public String getEndDatetimeStzStr() {
        return endDatetimeStzStr;
    }

    public void setStartDatetimeStr(String startDatetimeStr) {
        this.startDatetimeStr = startDatetimeStr;
    }

    public void setStartDatetimeStzStr(String startDatetimeStzStr) {
        this.startDatetimeStzStr = startDatetimeStzStr;
    }

    public void setEndDatetimeStr(String endDatetimeStr) {
        this.endDatetimeStr = endDatetimeStr;
    }

    public void setEndDatetimeStzStr(String endDatetimeStzStr) {
        this.endDatetimeStzStr = endDatetimeStzStr;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public void setMachineType(Integer machineType) {
        this.machineType = machineType;
    }

    public String getMachineId() {
        return machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public Integer getMachineType() {
        return machineType;
    }

    public void setSuspendedTimeMinutes(Integer suspendedTimeMinutes) {
        this.suspendedTimeMinutes = suspendedTimeMinutes;
    }

    public void setNetProducintTimeMinutes(Integer netProducintTimeMinutes) {
        this.netProducintTimeMinutes = netProducintTimeMinutes;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSuspendedTimeMinutes() {
        return suspendedTimeMinutes;
    }

    public Integer getNetProducintTimeMinutes() {
        return netProducintTimeMinutes;
    }

    public Integer getStatus() {
        return status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public void setIsEdit(Integer isEdit) {
        this.isEdit = isEdit;
    }

    public Integer getIsEdit() {
        return isEdit;
    }
    
    public void setIsStart(boolean isStart) {
        this.isStart = isStart;
    }

    public boolean getIsStart() {
        return isStart;
    }

    /**
     * @return the shotCountBeforeUpd
     */
    public int getShotCountBeforeUpd() {
        return shotCountBeforeUpd;
    }

    /**
     * @param shotCountBeforeUpd the shotCountBeforeUpd to set
     */
    public void setShotCountBeforeUpd(int shotCountBeforeUpd) {
        this.shotCountBeforeUpd = shotCountBeforeUpd;
    }

    /**
     * @return the disposedShotCountBeforeUpd
     */
    public int getDisposedShotCountBeforeUpd() {
        return disposedShotCountBeforeUpd;
    }

    /**
     * @param disposedShotCountBeforeUpd the disposedShotCountBeforeUpd to set
     */
    public void setDisposedShotCountBeforeUpd(int disposedShotCountBeforeUpd) {
        this.disposedShotCountBeforeUpd = disposedShotCountBeforeUpd;
    }

    /**
     * @return the netProducintTimeMinutesBeforeUpd
     */
    public int getNetProducintTimeMinutesBeforeUpd() {
        return netProducintTimeMinutesBeforeUpd;
    }

    /**
     * @param netProducintTimeMinutesBeforeUpd the netProducintTimeMinutesBeforeUpd to set
     */
    public void setNetProducintTimeMinutesBeforeUpd(int netProducintTimeMinutesBeforeUpd) {
        this.netProducintTimeMinutesBeforeUpd = netProducintTimeMinutesBeforeUpd;
    }

    /**
     * @return the productionEndFlg
     */
    public boolean getProductionEndFlg() {
        return productionEndFlg;
    }

    /**
     * @param productionEndFlg the productionEndFlg to set
     */
    public void setProductionEndFlg(boolean productionEndFlg) {
        this.productionEndFlg = productionEndFlg;
    }

    /**
     * @return the maxDailyReportMMDD
     */
    public String getMaxDailyReportMMDD() {
        return maxDailyReportMMDD;
    }

    /**
     * @param maxDailyReportMMDD the maxDailyReportMMDD to set
     */
    public void setMaxDailyReportMMDD(String maxDailyReportMMDD) {
        this.maxDailyReportMMDD = maxDailyReportMMDD;
    }

    /**
     * @return the maxDailyReportColorFlg
     */
    public int isMaxDailyReportColor() {
        return maxDailyReportColor;
    }

    /**
     * @param maxDailyReportColor the maxDailyReportColor to set
     */
    public void setMaxDailyReportColor(int maxDailyReportColor) {
        this.maxDailyReportColor = maxDailyReportColor;
    }

    /**
     * @return the poNumberCount
     */
    public int getPoNumberCount() {
        return poNumberCount;
    }

    /**
     * @param poNumberCount the poNumberCount to set
     */
    public void setPoNumberCount(int poNumberCount) {
        this.poNumberCount = poNumberCount;
    }

    /**
     * @return the productionLotNumber
     */
    public String getProductionLotNumber() {
        return productionLotNumber;
    }

    /**
     * @param productionLotNumber the productionLotNumber to set
     */
    public void setProductionLotNumber(String productionLotNumber) {
        this.productionLotNumber = productionLotNumber;
    }

    public int getStructureFlg() {
        return structureFlg;
    }

    public void setStructureFlg(int structureFlg) {
        this.structureFlg = structureFlg;
    }

	public MstWorkPhase getMstWorkPhase() {
		return mstWorkPhase;
	}

	public void setMstWorkPhase(MstWorkPhase mstWorkPhase) {
		this.mstWorkPhase = mstWorkPhase;
	}

    public Integer getProdDepartment() {
        return prodDepartment;
    }

    public void setProdDepartment(Integer prodDepartment) {
        this.prodDepartment = prodDepartment;
    }
    
    
}
