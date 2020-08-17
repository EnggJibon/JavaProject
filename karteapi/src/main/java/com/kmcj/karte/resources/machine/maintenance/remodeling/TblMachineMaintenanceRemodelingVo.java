/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.maintenance.remodeling;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.direction.TblDirection;
import com.kmcj.karte.resources.machine.*;
import com.kmcj.karte.resources.machine.inspection.result.TblMachineInspectionResultVo;
import com.kmcj.karte.resources.machine.maintenance.detail.TblMachineMaintenanceDetailImageFileVo;
import com.kmcj.karte.resources.machine.maintenance.detail.TblMachineMaintenanceDetailVo;
import com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetailImageFileVo;
import com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetailVo;
import com.kmcj.karte.resources.machine.remodeling.inspection.TblMachineRemodelingInspectionResultVo;
import com.kmcj.karte.resources.machine.spec.history.MstMachineSpecHistory;
import com.kmcj.karte.resources.mold.issue.TblIssue;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMachineMaintenanceRemodelingVo extends BasicResponse {

    private String id;

    private Integer mainteOrRemodel;
    private String mainteOrRemodelText;

    private Integer mainteType;

    private Integer remodelingType;

    private String mainteTypeText;
    private String remodelingTypeText;

    private Date mainteDate;
    private String mainteDateStr;

    private Date startDatetime;
    private String startDatetimeStr;

    private Date startDatetimeStz;
    private String startDatetimeStzStr;

    private Date endDatetime;
    private String endDatetimeStr;

    private Date endDatetimeStz;
    private String endDatetimeStzStr;

    private Date mainteDateStart;
    private Date mainteDateEnd;

    private String report;
    private String reportPersonName;

    private Date createDate;
    private String createDateStr;

    private Date updateDate;
    private String updateDateStr;

    private String createUserUuid;

    private String updateUserUuid;

    private MstMachineSpecHistory mstMachineSpecHistory;

    private String machineSpecHstId;
    private String machineSpecHstName;

    private MstMachine mstMachine;

    private String machineUuid;

    private String machineId;
    private String machineName;

    // 4.2 対応　BY LiuYoudong S
    private boolean resetAfterMainteTotalProducingTimeHourFlag;
    private boolean resetAfterMainteTotalShotCountFlag;
    private BigDecimal afterMainteTotalProducingTimeHour;
    private int afterMainteTotalShotCount;    
    // 4.2 対応　BY LiuYoudong E

    private TblIssue tblIssue;

    private String issueId;
    private String issueText;
    private String IssueReportCategory1;
    private String IssueReportCategory1Text;
    private String measureStatus;
    private Date measureDueDate;
    
    private TblDirection tblDirection;

    private String tblDirectionId;
    private String tblDirectionCode;
    private String mainteStatus;
    private String externalFlg;
	// メンテナンスの一次保存機能 2018/09/13 -S
    private int temporarilySaved;//0：登録　　1：一時保存
	// メンテナンスの一次保存機能 2018/09/13 -E
    
    // KM-361 メンテナンス所要時間の追加
    private int workingTimeMinutes;
    
    private long count;
    private int pageNumber;
    private int pageTotal;
    
    private String orderKey;

    private List<TblMachineMaintenanceRemodelingVo> machineMaintenanceRemodelingVo;
    private List<TblMachineMaintenanceDetailVo> machineMaintenanceDetailVo;
    private List<TblMachineRemodelingDetailVo> machineRemodelingDetailVo;
    private List<TblMachineInspectionResultVo> machineInspectionResultVo;
    private List<TblMachineRemodelingInspectionResultVo> remodelingInspectionResultVo;
    private List<TblMachineMaintenanceDetailImageFileVo> imageFileVos;
    private List<TblMachineRemodelingDetailImageFileVo> rimageFileVos;
    private TblMachineMaintenanceRemodeling tblMachineMaintenanceRemodeling;

    public TblMachineMaintenanceRemodelingVo() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMainteOrRemodel(Integer mainteOrRemodel) {
        this.mainteOrRemodel = mainteOrRemodel;
    }

    public void setMainteType(Integer mainteType) {
        this.mainteType = mainteType;
    }

    public void setRemodelingType(Integer remodelingType) {
        this.remodelingType = remodelingType;
    }

    public void setMainteTypeText(String mainteTypeText) {
        this.mainteTypeText = mainteTypeText;
    }

    public void setMainteDate(Date mainteDate) {
        this.mainteDate = mainteDate;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public void setStartDatetimeStz(Date startDatetimeStz) {
        this.startDatetimeStz = startDatetimeStz;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    public void setEndDatetimeStz(Date endDatetimeStz) {
        this.endDatetimeStz = endDatetimeStz;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public void setMstMachineSpecHistory(MstMachineSpecHistory mstMachineSpecHistory) {
        this.mstMachineSpecHistory = mstMachineSpecHistory;
    }

    public void setMachineSpecHstId(String machineSpecHstId) {
        this.machineSpecHstId = machineSpecHstId;
    }

    public void setMstMachine(MstMachine mstMachine) {
        this.mstMachine = mstMachine;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public void setTblIssue(TblIssue tblIssue) {
        this.tblIssue = tblIssue;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
    
    public void setMeasureDueDate(Date measureDueDate) {
        this.measureDueDate = measureDueDate;
    }

    public void setTblDirection(TblDirection tblDirection) {
        this.tblDirection = tblDirection;
    }

    public void setTblDirectionId(String tblDirectionId) {
        this.tblDirectionId = tblDirectionId;
    }

    public String getId() {
        return id;
    }

    public Integer getMainteOrRemodel() {
        return mainteOrRemodel;
    }

    public Integer getMainteType() {
        return mainteType;
    }

    public Integer getRemodelingType() {
        return remodelingType;
    }

    public String getMainteTypeText() {
        return mainteTypeText;
    }

    public Date getMainteDate() {
        return mainteDate;
    }

    public Date getStartDatetime() {
        return startDatetime;
    }

    public Date getStartDatetimeStz() {
        return startDatetimeStz;
    }

    public Date getEndDatetime() {
        return endDatetime;
    }

    public Date getEndDatetimeStz() {
        return endDatetimeStz;
    }

    public String getReport() {
        return report;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public MstMachineSpecHistory getMstMachineSpecHistory() {
        return mstMachineSpecHistory;
    }

    public String getMachineSpecHstId() {
        return machineSpecHstId;
    }

    public MstMachine getMstMachine() {
        return mstMachine;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public TblIssue getTblIssue() {
        return tblIssue;
    }

    public String getIssueId() {
        return issueId;
    }
    
    public Date getMeasureDueDate() {
        return measureDueDate;
    }

    public TblDirection getTblDirection() {
        return tblDirection;
    }

    public String getTblDirectionId() {
        return tblDirectionId;
    }

    public String getMainteDateStr() {
        return mainteDateStr;
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

    public String getCreateDateStr() {
        return createDateStr;
    }

    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public void setMainteDateStr(String mainteDateStr) {
        this.mainteDateStr = mainteDateStr;
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

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
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

    /**
     * @return the machineName
     */
    public String getMachineName() {
        return machineName;
    }

    /**
     * @param machineName the machineName to set
     */
    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    /**
     * @return the tblDirectionCode
     */
    public String getTblDirectionCode() {
        return tblDirectionCode;
    }

    /**
     * @param tblDirectionCode the tblDirectionCode to set
     */
    public void setTblDirectionCode(String tblDirectionCode) {
        this.tblDirectionCode = tblDirectionCode;
    }

    /**
     * @return the machineMaintenanceRemodelingVo
     */
    public List<TblMachineMaintenanceRemodelingVo> getMachineMaintenanceRemodelingVo() {
        return machineMaintenanceRemodelingVo;
    }

    /**
     * @param machineMaintenanceRemodelingVo the machineMaintenanceRemodelingVo
     * to set
     */
    public void setMachineMaintenanceRemodelingVo(List<TblMachineMaintenanceRemodelingVo> machineMaintenanceRemodelingVo) {
        this.machineMaintenanceRemodelingVo = machineMaintenanceRemodelingVo;
    }

    /**
     * @return the machineMaintenanceDetailVo
     */
    public List<TblMachineMaintenanceDetailVo> getMachineMaintenanceDetailVo() {
        return machineMaintenanceDetailVo;
    }

    /**
     * @param machineMaintenanceDetailVo the machineMaintenanceDetailVo to set
     */
    public void setMachineMaintenanceDetailVo(List<TblMachineMaintenanceDetailVo> machineMaintenanceDetailVo) {
        this.machineMaintenanceDetailVo = machineMaintenanceDetailVo;
    }

    /**
     * @return the machineRemodelingDetailVo
     */
    public List<TblMachineRemodelingDetailVo> getMachineRemodelingDetailVo() {
        return machineRemodelingDetailVo;
    }

    /**
     * @param machineRemodelingDetailVo the machineRemodelingDetailVo to set
     */
    public void setMachineRemodelingDetailVo(List<TblMachineRemodelingDetailVo> machineRemodelingDetailVo) {
        this.machineRemodelingDetailVo = machineRemodelingDetailVo;
    }

    /**
     * @return the machineInspectionResultVo
     */
    public List<TblMachineInspectionResultVo> getMachineInspectionResultVo() {
        return machineInspectionResultVo;
    }

    /**
     * @param machineInspectionResultVo the machineInspectionResultVo to set
     */
    public void setMachineInspectionResultVo(List<TblMachineInspectionResultVo> machineInspectionResultVo) {
        this.machineInspectionResultVo = machineInspectionResultVo;
    }

    /**
     * @return the externalFlg
     */
    public String getExternalFlg() {
        return externalFlg;
    }

    /**
     * @param externalFlg the externalFlg to set
     */
    public void setExternalFlg(String externalFlg) {
        this.externalFlg = externalFlg;
    }

    /**
     * @return the remodelingTypeText
     */
    public String getRemodelingTypeText() {
        return remodelingTypeText;
    }

    /**
     * @param remodelingTypeText the remodelingTypeText to set
     */
    public void setRemodelingTypeText(String remodelingTypeText) {
        this.remodelingTypeText = remodelingTypeText;
    }

    /**
     * @return the issueText
     */
    public String getIssueText() {
        return issueText;
    }

    /**
     * @param issueText the issueText to set
     */
    public void setIssueText(String issueText) {
        this.issueText = issueText;
    }

    /**
     * @return the IssueReportCategory1
     */
    public String getIssueReportCategory1() {
        return IssueReportCategory1;
    }

    /**
     * @param IssueReportCategory1 the IssueReportCategory1 to set
     */
    public void setIssueReportCategory1(String IssueReportCategory1) {
        this.IssueReportCategory1 = IssueReportCategory1;
    }

    /**
     * @return the IssueReportCategory1Text
     */
    public String getIssueReportCategory1Text() {
        return IssueReportCategory1Text;
    }

    /**
     * @param IssueReportCategory1Text the IssueReportCategory1Text to set
     */
    public void setIssueReportCategory1Text(String IssueReportCategory1Text) {
        this.IssueReportCategory1Text = IssueReportCategory1Text;
    }

    /**
     * @return the machineSpecHstName
     */
    public String getMachineSpecHstName() {
        return machineSpecHstName;
    }

    /**
     * @param machineSpecHstName the machineSpecHstName to set
     */
    public void setMachineSpecHstName(String machineSpecHstName) {
        this.machineSpecHstName = machineSpecHstName;
    }

    /**
     * @return the reportPersonName
     */
    public String getReportPersonName() {
        return reportPersonName;
    }

    /**
     * @param reportPersonName the reportPersonName to set
     */
    public void setReportPersonName(String reportPersonName) {
        this.reportPersonName = reportPersonName;
    }

    /**
     * @return the mainteDateStart
     */
    public Date getMainteDateStart() {
        return mainteDateStart;
    }

    /**
     * @param mainteDateStart the mainteDateStart to set
     */
    public void setMainteDateStart(Date mainteDateStart) {
        this.mainteDateStart = mainteDateStart;
    }

    /**
     * @return the mainteDateEnd
     */
    public Date getMainteDateEnd() {
        return mainteDateEnd;
    }

    /**
     * @param mainteDateEnd the mainteDateEnd to set
     */
    public void setMainteDateEnd(Date mainteDateEnd) {
        this.mainteDateEnd = mainteDateEnd;
    }

    /**
     * @return the mainteStatus
     */
    public String getMainteStatus() {
        return mainteStatus;
    }

    /**
     * @param mainteStatus the mainteStatus to set
     */
    public void setMainteStatus(String mainteStatus) {
        this.mainteStatus = mainteStatus;
    }

    /**
     * @return the mainteOrRemodelText
     */
    public String getMainteOrRemodelText() {
        return mainteOrRemodelText;
    }

    /**
     * @param mainteOrRemodelText the mainteOrRemodelText to set
     */
    public void setMainteOrRemodelText(String mainteOrRemodelText) {
        this.mainteOrRemodelText = mainteOrRemodelText;
    }

    /**
     * @return the tblMachineMaintenanceRemodeling
     */
    public TblMachineMaintenanceRemodeling getTblMachineMaintenanceRemodeling() {
        return tblMachineMaintenanceRemodeling;
    }

    /**
     * @param tblMachineMaintenanceRemodeling the
     * tblMachineMaintenanceRemodeling to set
     */
    public void setTblMachineMaintenanceRemodeling(TblMachineMaintenanceRemodeling tblMachineMaintenanceRemodeling) {
        this.tblMachineMaintenanceRemodeling = tblMachineMaintenanceRemodeling;
    }

    /**
     * @return the remodelingInspectionResultVo
     */
    public List<TblMachineRemodelingInspectionResultVo> getRemodelingInspectionResultVo() {
        return remodelingInspectionResultVo;
    }

    /**
     * @param remodelingInspectionResultVo the remodelingInspectionResultVo to
     * set
     */
    public void setRemodelingInspectionResultVo(List<TblMachineRemodelingInspectionResultVo> remodelingInspectionResultVo) {
        this.remodelingInspectionResultVo = remodelingInspectionResultVo;
    }

    public void setImageFileVos(List<TblMachineMaintenanceDetailImageFileVo> imageFileVos) {
        this.imageFileVos = imageFileVos;
    }

    public List<TblMachineMaintenanceDetailImageFileVo> getImageFileVos() {
        return imageFileVos;
    }

    public List<TblMachineRemodelingDetailImageFileVo> getRimageFileVos() {
        return rimageFileVos;
    }

    public void setRimageFileVos(List<TblMachineRemodelingDetailImageFileVo> rimageFileVos) {
        this.rimageFileVos = rimageFileVos;
    }

    /**
     * @return the measureStatus
     */
    public String getMeasureStatus() {
        return measureStatus;
    }

    /**
     * @param measureStatus the measureStatus to set
     */
    public void setMeasureStatus(String measureStatus) {
        this.measureStatus = measureStatus;
    }

    /**
     * @return the resetAfterMainteTotalProducingTimeHourFlag
     */
    public boolean isResetAfterMainteTotalProducingTimeHourFlag() {
        return resetAfterMainteTotalProducingTimeHourFlag;
    }

    /**
     * @param resetAfterMainteTotalProducingTimeHourFlag the resetAfterMainteTotalProducingTimeHourFlag to set
     */
    public void setResetAfterMainteTotalProducingTimeHourFlag(boolean resetAfterMainteTotalProducingTimeHourFlag) {
        this.resetAfterMainteTotalProducingTimeHourFlag = resetAfterMainteTotalProducingTimeHourFlag;
    }

    /**
     * @return the resetAfterMainteTotalShotCountFlag
     */
    public boolean isResetAfterMainteTotalShotCountFlag() {
        return resetAfterMainteTotalShotCountFlag;
    }

    /**
     * @param resetAfterMainteTotalShotCountFlag the resetAfterMainteTotalShotCountFlag to set
     */
    public void setResetAfterMainteTotalShotCountFlag(boolean resetAfterMainteTotalShotCountFlag) {
        this.resetAfterMainteTotalShotCountFlag = resetAfterMainteTotalShotCountFlag;
    }

    /**
     * @return the workingTimeMinutes
     */
    public int getWorkingTimeMinutes() {
        return workingTimeMinutes;
    }

    /**
     * @param workingTimeMinutes the workingTimeMinutes to set
     */
    public void setWorkingTimeMinutes(int workingTimeMinutes) {
        this.workingTimeMinutes = workingTimeMinutes;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }
    /**
     * @return the afterMainteTotalProducingTimeHour
     */
    public BigDecimal getAfterMainteTotalProducingTimeHour() {
        return afterMainteTotalProducingTimeHour;
    }

    /**
     * @param afterMainteTotalProducingTimeHour the
     * afterMainteTotalProducingTimeHour to set
     */
    public void setAfterMainteTotalProducingTimeHour(BigDecimal afterMainteTotalProducingTimeHour) {
        this.afterMainteTotalProducingTimeHour = afterMainteTotalProducingTimeHour;
    }

    /**
     * @return the afterMainteTotalShotCount
     */
    public int getAfterMainteTotalShotCount() {
        return afterMainteTotalShotCount;
    }

    /**
     * @param afterMainteTotalShotCount the afterMainteTotalShotCount to set
     */
    public void setAfterMainteTotalShotCount(int afterMainteTotalShotCount) {
        this.afterMainteTotalShotCount = afterMainteTotalShotCount;
    }

    // メンテナンスの一次保存機能 2018/09/13 -S
    /**
     * @return the temporarilySaved
     */
    public int getTemporarilySaved() {
        return temporarilySaved;
    }

    /**
     * @param registrFlag the temporarilySaved to set
     */
    public void setTemporarilySaved(int temporarilySaved) {
        this.temporarilySaved = temporarilySaved;
    }
    // メンテナンスの一次保存機能 2018/09/13 -E

    /**
     * @return the orderKey
     */
    public String getOrderKey() {
        return orderKey;
    }

    /**
     * @param orderKey the orderKey to set
     */
    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

}
