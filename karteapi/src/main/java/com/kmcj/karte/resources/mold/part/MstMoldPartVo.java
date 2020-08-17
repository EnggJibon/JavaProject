package com.kmcj.karte.resources.mold.part;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 *
 * @author admin
 */
public class MstMoldPartVo extends BasicResponse {

    private String id;
    private String moldUuid;
    private String localtion;
    private String moldPartId;
    private Integer rplClShotCnt;
    private Integer rplClProdTimeHour;
    private Integer rplClLappsedDay;
    private Integer rprClShotCnt;
    private Integer rprClProdTimeHour;
    private Integer rprClLappsedDay;
    private Integer aftRplShotCnt;
    private Integer aftRplProdTimeHour;
    private Date lastRplDateTime;
    private Integer aftRprShotCnt;
    private Integer aftRprProdTimeHour;
    private Date lastRprDateTime;
    private Integer replaceOrRepair;
    private Integer hitCondition;
    private Integer maintainedFlag;
    private Date createDate;
    private Date updateDate;
    private String createUserUuid;
    private String updateUserUuid;
    private String moldPartMaintenanceId;

    public MstMoldPartVo() {
    }
    
    public MstMoldPartVo(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getMoldUuid() {
        return moldUuid;
    }

    public String getLocaltion() {
        return localtion;
    }

    public String getMoldPartId() {
        return moldPartId;
    }

    public Integer getRplClShotCnt() {
        return rplClShotCnt;
    }

    public Integer getRplClProdTimeHour() {
        return rplClProdTimeHour;
    }

    public Integer getRplClLappsedDay() {
        return rplClLappsedDay;
    }

    public Integer getRprClShotCnt() {
        return rprClShotCnt;
    }

    public Integer getRprClProdTimeHour() {
        return rprClProdTimeHour;
    }

    public Integer getRprClLappsedDay() {
        return rprClLappsedDay;
    }

    public Integer getAftRplShotCnt() {
        return aftRplShotCnt;
    }

    public Integer getAftRplProdTimeHour() {
        return aftRplProdTimeHour;
    }

    public Date getLastRplDateTime() {
        return lastRplDateTime;
    }

    public Integer getAftRprShotCnt() {
        return aftRprShotCnt;
    }

    public Integer getAftRprProdTimeHour() {
        return aftRprProdTimeHour;
    }

    public Date getLastRprDateTime() {
        return lastRprDateTime;
    }

    public Integer getReplaceOrRepair() {
        return replaceOrRepair;
    }

    public Integer getHitCondition() {
        return hitCondition;
    }

    public Integer getMaintainedFlag() {
        return maintainedFlag;
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

    public String getMoldPartMaintenanceId() {
        return moldPartMaintenanceId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    public void setLocaltion(String localtion) {
        this.localtion = localtion;
    }

    public void setMoldPartId(String moldPartId) {
        this.moldPartId = moldPartId;
    }

    public void setRplClShotCnt(Integer rplClShotCnt) {
        this.rplClShotCnt = rplClShotCnt;
    }

    public void setRplClProdTimeHour(Integer rplClProdTimeHour) {
        this.rplClProdTimeHour = rplClProdTimeHour;
    }

    public void setRplClLappsedDay(Integer rplClLappsedDay) {
        this.rplClLappsedDay = rplClLappsedDay;
    }

    public void setRprClShotCnt(Integer rprClShotCnt) {
        this.rprClShotCnt = rprClShotCnt;
    }

    public void setRprClProdTimeHour(Integer rprClProdTimeHour) {
        this.rprClProdTimeHour = rprClProdTimeHour;
    }

    public void setRprClLappsedDay(Integer rprClLappsedDay) {
        this.rprClLappsedDay = rprClLappsedDay;
    }

    public void setAftRplShotCnt(Integer aftRplShotCnt) {
        this.aftRplShotCnt = aftRplShotCnt;
    }

    public void setAftRplProdTimeHour(Integer aftRplProdTimeHour) {
        this.aftRplProdTimeHour = aftRplProdTimeHour;
    }

    public void setLastRplDateTime(Date lastRplDateTime) {
        this.lastRplDateTime = lastRplDateTime;
    }

    public void setAftRprShotCnt(Integer aftRprShotCnt) {
        this.aftRprShotCnt = aftRprShotCnt;
    }

    public void setAftRprProdTimeHour(Integer aftRprProdTimeHour) {
        this.aftRprProdTimeHour = aftRprProdTimeHour;
    }

    public void setLastRprDateTime(Date lastRprDateTime) {
        this.lastRprDateTime = lastRprDateTime;
    }

    public void setReplaceOrRepair(Integer replaceOrRepair) {
        this.replaceOrRepair = replaceOrRepair;
    }

    public void setHitCondition(Integer hitCondition) {
        this.hitCondition = hitCondition;
    }

    public void setMaintainedFlag(Integer maintainedFlag) {
        this.maintainedFlag = maintainedFlag;
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

    public void setMoldPartMaintenanceId(String moldPartMaintenanceId) {
        this.moldPartMaintenanceId = moldPartMaintenanceId;
    }
    
}
