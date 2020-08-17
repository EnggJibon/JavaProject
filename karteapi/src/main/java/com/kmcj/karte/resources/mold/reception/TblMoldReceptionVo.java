package com.kmcj.karte.resources.mold.reception;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 * 
 * @author Apeng
 */
public class TblMoldReceptionVo extends BasicResponse {
    
    private String ownerCompanyCode;//所有会社CODE
    private String ownerCompanyId;//所有会社ID
    private String ownerCompanyName;//所有会社名称
    private String moldId;//金型ID
    private String moldName;//金型名称
    private String otherComponentCode;//先方部品コード
    private String otherComponentName;//先方部品名称
    private String ownerContactName;//担当者名称
    private String receptionTime;//受信日時
    private String receptionId;

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
     * @return the moldId
     */
    public String getMoldId() {
        return moldId;
    }

    /**
     * @param moldId the moldId to set
     */
    public void setMoldId(String moldId) {
        this.moldId = moldId;
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
     * @return the otherComponentCode
     */
    public String getOtherComponentCode() {
        return otherComponentCode;
    }

    /**
     * @param otherComponentCode the otherComponentCode to set
     */
    public void setOtherComponentCode(String otherComponentCode) {
        this.otherComponentCode = otherComponentCode;
    }

    /**
     * @return the otherComponentName
     */
    public String getOtherComponentName() {
        return otherComponentName;
    }

    /**
     * @param otherComponentName the otherComponentName to set
     */
    public void setOtherComponentName(String otherComponentName) {
        this.otherComponentName = otherComponentName;
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
