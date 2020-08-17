package com.kmcj.karte.resources.machine.daily.report;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.component.structure.MstComponentStructureVoList;
import com.kmcj.karte.resources.machine.daily.report.detail.TblMachineDailyReportDetailVo;
import com.kmcj.karte.resources.production.TblProduction;
import com.kmcj.karte.resources.production.suspension.TblProductionSuspensionVo;
import com.kmcj.karte.resources.user.MstUser;
import java.util.Date;
import java.util.List;

/**
 *
 * @author zds
 */
public class TblMachineDailyReportVo extends BasicResponse {

    private String productionId;

    private Date productionDate;

    private String productionDateStr;

    private String id;

    private String reporterUuid;

    private Date startDatetime;

    private Date startDatetimeStz;

    private Date endDatetime;

    private Date endDatetimeStz;

    private String startDatetimeStr;

    private String startDatetimeStzStr;

    private String endDatetimeStr;

    private String endDatetimeStzStr;

    private String producingTimeMinutes;

    private String suspendedTimeMinutes;
    
    private String personName;

    private String netProducintTimeMinutes;

    private String shotCount;

    private String disposedShotCount;
    
    private String messageForPreviousDay;

    private String messageForNextDay;

    private Date createDate;

    private Date updateDate;

    private String createDateStr;

    private String updateDateStr;

    private String createUserUuid;

    private String updateUserUuid;

    private TblProduction tblProduction;

    private MstUser mstUser;
    
    private String machineUuid;

    private String machineId;
    
    private String machineName;
    
    private String reporterName;
    
    private String directionCode;
    
    private String lotNumber;
    
    private String prevLotNumber;
    
    private List<TblMachineDailyReportDetailVo> machineDailyReportDetailVos;
    private List<TblMachineDailyReportVo> machineDailyReportVos;
    private List<TblProductionSuspensionVo> productionSuspensionVos;
    
    private String noRegistrationFlag;
    
    private String noRegistrationFlagText;
    
    private boolean productionEndFlag;
    
    private int shotCountBeforeUpd;
    
    private int disposedShotCountBeforeUpd;
    
    private int netProducintTimeMinutesBeforeUpd;
    
    private List<MstComponentStructureVoList> mstComponentStructureVoList;
    
    private int structureFlg;

    public String getProductionId() {
        return productionId;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public String getProductionDateStr() {
        return productionDateStr;
    }

    public String getId() {
        return id;
    }

    public String getReporterUuid() {
        return reporterUuid;
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

    public String getProducingTimeMinutes() {
        return producingTimeMinutes;
    }

    public String getSuspendedTimeMinutes() {
        return suspendedTimeMinutes;
    }

    public String getNetProducintTimeMinutes() {
        return netProducintTimeMinutes;
    }

    public String getShotCount() {
        return shotCount;
    }

    public String getDisposedShotCount() {
        return disposedShotCount;
    }

    public String getMessageForNextDay() {
        return messageForNextDay;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public TblProduction getTblProduction() {
        return tblProduction;
    }

    public MstUser getMstUser() {
        return mstUser;
    }

    public List<TblMachineDailyReportDetailVo> getMachineDailyReportDetailVos() {
        return machineDailyReportDetailVos;
    }

    public void setProductionId(String productionId) {
        this.productionId = productionId;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public void setProductionDateStr(String productionDateStr) {
        this.productionDateStr = productionDateStr;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setReporterUuid(String reporterUuid) {
        this.reporterUuid = reporterUuid;
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

    public void setProducingTimeMinutes(String producingTimeMinutes) {
        this.producingTimeMinutes = producingTimeMinutes;
    }

    public void setSuspendedTimeMinutes(String suspendedTimeMinutes) {
        this.suspendedTimeMinutes = suspendedTimeMinutes;
    }

    public void setNetProducintTimeMinutes(String netProducintTimeMinutes) {
        this.netProducintTimeMinutes = netProducintTimeMinutes;
    }

    public void setShotCount(String shotCount) {
        this.shotCount = shotCount;
    }

    public void setDisposedShotCount(String disposedShotCount) {
        this.disposedShotCount = disposedShotCount;
    }

    public void setMessageForNextDay(String messageForNextDay) {
        this.messageForNextDay = messageForNextDay;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public void setTblProduction(TblProduction tblProduction) {
        this.tblProduction = tblProduction;
    }

    public void setMstUser(MstUser mstUser) {
        this.mstUser = mstUser;
    }

    public void setMachineDailyReportDetailVos(List<TblMachineDailyReportDetailVo> machineDailyReportDetailVos) {
        this.machineDailyReportDetailVos = machineDailyReportDetailVos;
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
     * @return the reporterName
     */
    public String getReporterName() {
        return reporterName;
    }

    /**
     * @param reporterName the reporterName to set
     */
    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public void setMachineDailyReportVos(List<TblMachineDailyReportVo> machineDailyReportVos) {
        this.machineDailyReportVos = machineDailyReportVos;
    }

    public List<TblMachineDailyReportVo> getMachineDailyReportVos() {
        return machineDailyReportVos;
    }

    public String getDirectionCode() {
        return directionCode;
    }

    public void setDirectionCode(String directionCode) {
        this.directionCode = directionCode;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }
    
    public String getPrevLotNumber() {
        return prevLotNumber;
    }

    public void setPrevLotNumber(String prevLotNumber) {
        this.prevLotNumber = prevLotNumber;
    }

    public List<TblProductionSuspensionVo> getProductionSuspensionVos() {
        return productionSuspensionVos;
    }

    public void setProductionSuspensionVos(List<TblProductionSuspensionVo> productionSuspensionVos) {
        this.productionSuspensionVos = productionSuspensionVos;
    }

    public String getNoRegistrationFlag() {
        return noRegistrationFlag;
    }

    public void setNoRegistrationFlag(String noRegistrationFlag) {
        this.noRegistrationFlag = noRegistrationFlag;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getMessageForPreviousDay() {
        return messageForPreviousDay;
    }

    public void setMessageForPreviousDay(String messageForPreviousDay) {
        this.messageForPreviousDay = messageForPreviousDay;
    }

    public String getNoRegistrationFlagText() {
        return noRegistrationFlagText;
    }

    public void setNoRegistrationFlagText(String noRegistrationFlagText) {
        this.noRegistrationFlagText = noRegistrationFlagText;
    }

    /**
     * @return the productionEndFlag
     */
    public boolean getProductionEndFlag() {
        return productionEndFlag;
    }

    /**
     * @param productionEndFlag the productionEndFlag to set
     */
    public void setProductionEndFlag(boolean productionEndFlag) {
        this.productionEndFlag = productionEndFlag;
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

    public List<MstComponentStructureVoList> getMstComponentStructureVoList() {
        return mstComponentStructureVoList;
    }

    public void setMstComponentStructureVoList(List<MstComponentStructureVoList> mstComponentStructureVoList) {
        this.mstComponentStructureVoList = mstComponentStructureVoList;
    }

    /**
     * @return the structureFlg
     */
    public int getStructureFlg() {
        return structureFlg;
    }

    /**
     * @param structureFlg the structureFlg to set
     */
    public void setStructureFlg(int structureFlg) {
        this.structureFlg = structureFlg;
    }
    
}
