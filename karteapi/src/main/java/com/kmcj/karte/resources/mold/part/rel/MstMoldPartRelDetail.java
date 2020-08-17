/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.rel;

import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.part.MstMoldPart;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Transient;

/**
 *
 * @author BnK Win10 2010
 */
public class MstMoldPartRelDetail {
    private String id;
    private String moldUuid;
    private String location;
    private String moldPartId;
    private Integer quantity;
    private String alias;
    private Integer rplClShotCnt;
    private Integer rplClProdTimeHour;
    private Integer rplClLappsedDay;
    private Integer rprClShotCnt;
    private Integer rprClProdTimeHour;
    private Integer rprClLappsedDay;
    private Integer aftRplShotCnt;
    private BigDecimal aftRplProdTimeHour;
    private Date lastRplDatetime;
    private Integer aftRprShotCnt;
    private BigDecimal aftRprProdTimeHour;
    private Date lastRprDatetime;
    private Date createDate;
    private Date updateDate;
    private String createUserUuid;
    private String updateUserUuid;
    private MstMold mstMold;
    private MstMoldPart mstMoldPart;
    @Transient
    private Integer deleteFlag;
     
    @Transient
    private boolean isError; //csvチェック　エラー有無　判定用
    
    MstMoldPartRelDetail() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoldUuid() {
        return moldUuid;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMoldPartId() {
        return moldPartId;
    }

    public void setMoldPartId(String moldPartId) {
        this.moldPartId = moldPartId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getRplClShotCnt() {
        return rplClShotCnt;
    }

    public void setRplClShotCnt(Integer rplClShotCnt) {
        this.rplClShotCnt = rplClShotCnt;
    }

    public Integer getRplClProdTimeHour() {
        return rplClProdTimeHour;
    }

    public void setRplClProdTimeHour(Integer rplClProdTimeHour) {
        this.rplClProdTimeHour = rplClProdTimeHour;
    }

    public Integer getRplClLappsedDay() {
        return rplClLappsedDay;
    }

    public void setRplClLappsedDay(Integer rplClLappsedDay) {
        this.rplClLappsedDay = rplClLappsedDay;
    }

    public Integer getRprClShotCnt() {
        return rprClShotCnt;
    }

    public void setRprClShotCnt(Integer rprClShotCnt) {
        this.rprClShotCnt = rprClShotCnt;
    }

    public Integer getRprClProdTimeHour() {
        return rprClProdTimeHour;
    }

    public void setRprClProdTimeHour(Integer rprClProdTimeHour) {
        this.rprClProdTimeHour = rprClProdTimeHour;
    }

    public Integer getRprClLappsedDay() {
        return rprClLappsedDay;
    }

    public void setRprClLappsedDay(Integer rprClLappsedDay) {
        this.rprClLappsedDay = rprClLappsedDay;
    }

    public Integer getAftRplShotCnt() {
        return aftRplShotCnt;
    }

    public void setAftRplShotCnt(Integer aftRplShotCnt) {
        this.aftRplShotCnt = aftRplShotCnt;
    }

    public BigDecimal getAftRplProdTimeHour() {
        return aftRplProdTimeHour;
    }

    public void setAftRplProdTimeHour(BigDecimal aftRplProdTimeHour) {
        this.aftRplProdTimeHour = aftRplProdTimeHour;
    }

    public Date getLastRplDatetime() {
        return lastRplDatetime;
    }

    public void setLastRplDatetime(Date lastRplDatetime) {
        this.lastRplDatetime = lastRplDatetime;
    }

    public Integer getAftRprShotCnt() {
        return aftRprShotCnt;
    }

    public void setAftRprShotCnt(Integer aftRprShotCnt) {
        this.aftRprShotCnt = aftRprShotCnt;
    }

    public BigDecimal getAftRprProdTimeHour() {
        return aftRprProdTimeHour;
    }

    public void setAftRprProdTimeHour(BigDecimal aftRprProdTimeHour) {
        this.aftRprProdTimeHour = aftRprProdTimeHour;
    }

    public Date getLastRprDatetime() {
        return lastRprDatetime;
    }

    public void setLastRprDatetime(Date lastRprDatetime) {
        this.lastRprDatetime = lastRprDatetime;
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

    public MstMold getMstMold() {
        return mstMold;
    }

    public void setMstMold(MstMold mstMold) {
        this.mstMold = mstMold;
    }

    public MstMoldPart getMstMoldPart() {
        return mstMoldPart;
    }

    public void setMstMoldPart(MstMoldPart mstMoldPart) {
        this.mstMoldPart = mstMoldPart;
    }

    public boolean isIsError() {
        return isError;
    }

    public void setIsError(boolean isError) {
        this.isError = isError;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }
}
