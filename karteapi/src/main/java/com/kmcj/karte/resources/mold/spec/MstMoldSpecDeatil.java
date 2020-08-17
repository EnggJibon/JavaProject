/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.spec;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 *
 * @author jiangxs
 */
public class MstMoldSpecDeatil extends BasicResponse {

    private String moldAttrbuteCode;
    private String moldAttrbuteName;
    private int moldAttrbuteType;
    private String moldSpecHistoryName;
    private String attrValue;
    private String attrId;
    private String moldSpecHstId;
    private Date endDate;
    private Date startDate;

    /**
     * @return the moldAttrbuteCode
     */
    public String getMoldAttrbuteCode() {
        return moldAttrbuteCode;
    }

    /**
     * @param moldAttrbuteCode the moldAttrbuteCode to set
     */
    public void setMoldAttrbuteCode(String moldAttrbuteCode) {
        this.moldAttrbuteCode = moldAttrbuteCode;
    }

    /**
     * @return the moldAttrbuteName
     */
    public String getMoldAttrbuteName() {
        return moldAttrbuteName;
    }

    /**
     * @param moldAttrbuteName the moldAttrbuteName to set
     */
    public void setMoldAttrbuteName(String moldAttrbuteName) {
        this.moldAttrbuteName = moldAttrbuteName;
    }

    /**
     * @return the moldSpecHistoryName
     */
    public String getMoldSpecHistoryName() {
        return moldSpecHistoryName;
    }

    /**
     * @param moldSpecHistoryName the moldSpecHistoryName to set
     */
    public void setMoldSpecHistoryName(String moldSpecHistoryName) {
        this.moldSpecHistoryName = moldSpecHistoryName;
    }

    /**
     * @return the attrId
     */
    public String getAttrId() {
        return attrId;
    }

    /**
     * @param attrId the attrId to set
     */
    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    /**
     * @return the moldSpecHstId
     */
    public String getMoldSpecHstId() {
        return moldSpecHstId;
    }

    /**
     * @param moldSpecHstId the moldSpecHstId to set
     */
    public void setMoldSpecHstId(String moldSpecHstId) {
        this.moldSpecHstId = moldSpecHstId;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the moldAttrbuteType
     */
    public int getMoldAttrbuteType() {
        return moldAttrbuteType;
    }

    /**
     * @param moldAttrbuteType the moldAttrbuteType to set
     */
    public void setMoldAttrbuteType(int moldAttrbuteType) {
        this.moldAttrbuteType = moldAttrbuteType;
    }

    /**
     * @return the attrValue
     */
    public String getAttrValue() {
        return attrValue;
    }

    /**
     * @param attrValue the attrValue to set
     */
    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

}
