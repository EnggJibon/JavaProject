package com.kmcj.karte.resources.machine.reception;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 * 
 * @author Apeng
 */
public class TblMachineReceptionVo extends BasicResponse {
    
    private String ownerCompanyCode;//所有会社CODE
    private String ownerCompanyId;//所有会社ID
    private String ownerCompanyName;//所有会社名称
    private String machineId;//設備ID
    private String machineName;//設備名称
    private String ownerContactName;//担当者名称
    private String receptionTime;//受信日時
    private String receptionId;

    /**
     * @return the ownerCompanyCode
     */
    public String getOwnerCompanyCode() {
        return ownerCompanyCode;
    }

    /**
     * @param ownerCompanyCode the ownerCompanyCode to set
     */
    public void setOwnerCompanyCode(String ownerCompanyCode) {
        this.ownerCompanyCode = ownerCompanyCode;
    }

    /**
     * @return the ownerCompanyId
     */
    public String getOwnerCompanyId() {
        return ownerCompanyId;
    }

    /**
     * @param ownerCompanyId the ownerCompanyId to set
     */
    public void setOwnerCompanyId(String ownerCompanyId) {
        this.ownerCompanyId = ownerCompanyId;
    }

    /**
     * @return the ownerCompanyName
     */
    public String getOwnerCompanyName() {
        return ownerCompanyName;
    }

    /**
     * @param ownerCompanyName the ownerCompanyName to set
     */
    public void setOwnerCompanyName(String ownerCompanyName) {
        this.ownerCompanyName = ownerCompanyName;
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
     * @return the ownerContactName
     */
    public String getOwnerContactName() {
        return ownerContactName;
    }

    /**
     * @param ownerContactName the ownerContactName to set
     */
    public void setOwnerContactName(String ownerContactName) {
        this.ownerContactName = ownerContactName;
    }

    /**
     * @return the receptionTime
     */
    public String getReceptionTime() {
        return receptionTime;
    }

    /**
     * @param receptionTime the receptionTime to set
     */
    public void setReceptionTime(String receptionTime) {
        this.receptionTime = receptionTime;
    }

    /**
     * @return the receptionId
     */
    public String getReceptionId() {
        return receptionId;
    }

    /**
     * @param receptionId the receptionId to set
     */
    public void setReceptionId(String receptionId) {
        this.receptionId = receptionId;
    }
    
}
