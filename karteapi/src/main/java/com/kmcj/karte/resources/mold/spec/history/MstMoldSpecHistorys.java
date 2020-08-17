/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.spec.history;

import com.kmcj.karte.BasicResponse;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jiangxs
 */
public class MstMoldSpecHistorys extends BasicResponse {
    private String id;
    private String attrCode;
    private String attrName;
    private String attrValue;
    private String moldSpecName;
    private String moldSpecHstId;
    private String attrId;
    private String moldUuid;
    private String moldId;
    private String moldName;
    private Date startDate;
    private Date endDate;
    private String endDateStr;
    private Long endDateHs;
    private int attrType;
    
    private List<MstMoldSpecHistory> moldSpecHistorys;
    private List<MstMoldSpecHistorys> moldSpecHistoryVos;
    private MstMoldSpecHistory mstMoldSpecHistory;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the attrCode
     */
    public String getAttrCode() {
        return attrCode;
    }

    public String getAttrCode(String attrCode, String attrName, String attrValue) {
        return attrCode;
    }

    /**
     * @param attrCode the attrCode to set
     */
    public void setAttrCode(String attrCode) {
        this.attrCode = attrCode;
    }

    /**
     * @return the attrName
     */
    public String getAttrName() {
        return attrName;
    }

    /**
     * @param attrName the attrName to set
     */
    public void setAttrName(String attrName) {
        this.attrName = attrName;
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
     * @return the attrType
     */
    public int getAttrType() {
        return attrType;
    }

    /**
     * @param attrType the attrType to set
     */
    public void setAttrType(int attrType) {
        this.attrType = attrType;
    }

    public List<MstMoldSpecHistory> getMoldSpecHistorys() {
        return moldSpecHistorys;
    }

    public void setMoldSpecHistorys(List<MstMoldSpecHistory> moldSpecHistorys) {
        this.moldSpecHistorys = moldSpecHistorys;
    }

    public String getMoldSpecName() {
        return moldSpecName;
    }

    public void setMoldSpecName(String moldSpecName) {
        this.moldSpecName = moldSpecName;
    }

    public List<MstMoldSpecHistorys> getMoldSpecHistoryVos() {
        return moldSpecHistoryVos;
    }

    public void setMoldSpecHistoryVos(List<MstMoldSpecHistorys> moldSpecHistoryVos) {
        this.moldSpecHistoryVos = moldSpecHistoryVos;
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

    public MstMoldSpecHistory getMstMoldSpecHistory() {
        return mstMoldSpecHistory;
    }

    public void setMstMoldSpecHistory(MstMoldSpecHistory mstMoldSpecHistory) {
        this.mstMoldSpecHistory = mstMoldSpecHistory;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

    public Long getEndDateHs() {
        return endDateHs;
    }

    public void setEndDateHs(Long endDateHs) {
        this.endDateHs = endDateHs;
    }
    
}
