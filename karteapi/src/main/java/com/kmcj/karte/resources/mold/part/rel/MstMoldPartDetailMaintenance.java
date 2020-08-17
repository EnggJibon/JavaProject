/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.rel;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.mold.maintenance.part.TblMoldMaintenancePart;
import com.kmcj.karte.resources.mold.part.MstMoldPart;
import com.kmcj.karte.resources.mold.part.stock.MoldPartStock;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class MstMoldPartDetailMaintenance extends BasicResponse {

    //金型マスタ
    private MstMoldPart mstMoldPart;
    //金型マスタ
    private MstMoldPartRel mstMoldPartRel;
    //部品マスタ
    private List<MstMoldPart> mstMoldPartList;

    //金型加工条件属性マスタを取得する
    private List<MstMoldPartRel> mstMoldPartRelList;
    
    /** 金型部品在庫*/
    private List<MoldPartStock> stocks;
    /** 金型部品メンテ*/
    private TblMoldMaintenancePart maintPart;

    private String moldId;
    private String moldPartRelId;
    private String location;
    private String moldPartCode;
    private Integer quantity;
    private int rplClShotCnt;
    private int rplClProdTimeHour;
    private int rplClLappsedDay;
    private int rprClShotCnt;
    private int rprClProdTimeHour;
    private int rprClLappsedDay;
    private int aftRplShotCnt;
    private BigDecimal aftRplProdTimeHour;
    private Date lastRplDatetime;
    private int aftRprShotCnt;
    private BigDecimal aftRprProdTimeHour;
    private Date lastRprDatetime;
    private boolean replaceIsChecked;
    private boolean repairIsChecked;
    private boolean partialReplaceIsChecked;
    private Integer recyclableFlg;

    /**
     * @return the mstMoldPart
     */
    public MstMoldPart getMstMoldPart() {
        return mstMoldPart;
    }

    /**
     * @param mstMoldPart the mstMoldPart to set
     */
    public void setMstMoldPart(MstMoldPart mstMoldPart) {
        this.mstMoldPart = mstMoldPart;
    }
    
    /**
     * @return the mstMoldPartRel
     */
    public MstMoldPartRel getMstMoldPartRel() {
        return mstMoldPartRel;
    }

    /**
     * @param mstMoldPartRel the mstMoldPartRel to set
     */
    public void setMstMoldPartRel(MstMoldPartRel mstMoldPartRel) {
        this.mstMoldPartRel = mstMoldPartRel;
    }

    /**
     * @return the mstMoldPartList
     */
    public List<MstMoldPart> getMstMoldPartList() {
        return mstMoldPartList;
    }

    /**
     * @param mstMoldPartList the mstMoldPartList to set
     */
    public void setMstMoldPartList(List<MstMoldPart> mstMoldPartList) {
        this.mstMoldPartList = mstMoldPartList;
    }

    /**
     * @return the mstMoldPartRelList
     */
    public List<MstMoldPartRel> getMstMoldPartRelList() {
        return mstMoldPartRelList;
    }

    /**
     * @param mstMoldPartRelList the mstMoldPartRelList to
     * set
     */
    public void setMstMoldPartRelList(List<MstMoldPartRel> mstMoldPartRelList) {
        this.mstMoldPartRelList = mstMoldPartRelList;
    }

    public List<MoldPartStock> getStocks() {
        return stocks;
    }

    public void setStocks(List<MoldPartStock> stocks) {
        this.stocks = stocks;
    }

    public TblMoldMaintenancePart getMaintPart() {
        return maintPart;
    }

    public void setMaintPart(TblMoldMaintenancePart maintPart) {
        this.maintPart = maintPart;
    }

    public Integer getRecyclableFlg() {
        return recyclableFlg;
    }

    public void setRecyclableFlg(Integer recyclableFlg) {
        this.recyclableFlg = recyclableFlg;
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
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMoldPartRelId() {
        return moldPartRelId;
    }

    public void setMoldPartRelId(String moldPartRelId) {
        this.moldPartRelId = moldPartRelId;
    }
    
    /**
     * @return the moldPartCode
     */
    public String getMoldPartCode() {
        return moldPartCode;
    }

    public void setMoldPartCode(String moldPartCode) {
        this.moldPartCode = moldPartCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    /**
     * @return the rplClShotCnt
     */
    public int getRplClShotCnt() {
        return rplClShotCnt;
    }

    public void setRplClShotCnt(int rplClShotCnt) {
        this.rplClShotCnt = rplClShotCnt;
    }
    /**
     * @return the rplClProdTimeHour
     */
    public int getRplClProdTimeHour() {
        return rplClProdTimeHour;
    }

    public void setRplClProdTimeHour(int rplClProdTimeHour) {
        this.rplClProdTimeHour = rplClProdTimeHour;
    }
    /**
     * @return the rplClLappsedDay
     */
    public int getRplClLappsedDay() {
        return rplClLappsedDay;
    }
    
    public void setRplClLappsedDay(int rplClLappsedDay) {
        this.rplClLappsedDay = rplClLappsedDay;
    }
    /**
     * @return the rprClShotCnt
     */
    public int getRprClShotCnt() {
        return rprClShotCnt;
    }

    public void setRprClShotCnt(int rprClShotCnt) {
        this.rprClShotCnt = rprClShotCnt;
    }
    /**
     * @return the rprClProdTimeHour
     */
    public int getRprClProdTimeHour() {
        return rprClProdTimeHour;
    }

    public void setRprClProdTimeHour(int rprClProdTimeHour) {
        this.rprClProdTimeHour = rprClProdTimeHour;
    }
    /**
     * @return the rprClLappsedDay
     */
    public int getRprClLappsedDay() {
        return rprClLappsedDay;
    }

    public void setRprClLappsedDay(int rprClLappsedDay) {
        this.rprClLappsedDay = rprClLappsedDay;
    }
    /**
     * @return the aftRplShotCnt
     */
    public int getAftRplShotCnt() {
        return aftRplShotCnt;
    }

    public void setAftRplShotCnt(int aftRplShotCnt) {
        this.aftRplShotCnt = aftRplShotCnt;
    }
    /**
     * @return the aftRplProdTimeHour
     */
    public BigDecimal getAftRplProdTimeHour() {
        return aftRplProdTimeHour;
    }

    public void setAftRplProdTimeHour(BigDecimal aftRplProdTimeHour) {
        this.aftRplProdTimeHour = aftRplProdTimeHour;
    }
    /**
     * @return the lastRplDatetime
     */
    public Date getLastRplDatetime() {
        return lastRplDatetime;
    }

    public void setLastRplDatetime(Date lastRplDatetime) {
        this.lastRplDatetime = lastRplDatetime;
    }
    /**
     * @return the aftRprShotCnt
     */
    public int getAftRprShotCnt() {
        return aftRprShotCnt;
    }

    public void setAftRprShotCnt(int aftRprShotCnt) {
        this.aftRprShotCnt = aftRprShotCnt;
    }
    /**
     * @return the aftRprProdTimeHour
     */
    public BigDecimal getAftRprProdTimeHour() {
        return aftRprProdTimeHour;
    }

    public void setAftRprProdTimeHour(BigDecimal aftRprProdTimeHour) {
        this.aftRprProdTimeHour = aftRprProdTimeHour;
    }
    /**
     * @return the lastRprDatetime
     */
    public Date getLastRprDatetime() {
        return lastRprDatetime;
    }

    public void setLastRprDatetime(Date lastRprDatetime) {
        this.lastRprDatetime = lastRprDatetime;
    }

    public boolean getReplaceIsChecked() {
        return replaceIsChecked;
    }

    public void setReplaceIsChecked(boolean replaceIsChecked) {
        this.replaceIsChecked = replaceIsChecked;
    }

    public boolean getRepairIsChecked() {
        return repairIsChecked;
    }

    public void setRepairIsChecked(boolean repairIsChecked) {
        this.repairIsChecked = repairIsChecked;
    }

    public boolean getPartialReplaceIsChecked() {
        return partialReplaceIsChecked;
    }

    public void setPartialReplaceIsChecked(boolean partialReplaceIsChecked) {
        this.partialReplaceIsChecked = partialReplaceIsChecked;
    }
}
