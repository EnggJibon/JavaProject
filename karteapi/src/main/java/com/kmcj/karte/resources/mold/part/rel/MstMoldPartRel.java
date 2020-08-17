/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.rel;

import com.kmcj.karte.resources.mold.part.*;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.user.MstUser;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "mst_mold_part_rel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMoldPartRel.findAll", query = "SELECT t FROM MstMoldPartRel t"),
    @NamedQuery(name = "MstMoldPartRel.findByItem", query = "SELECT t FROM MstMoldPartRel t WHERE t.moldUuid LIKE :moldUuid AND t.location LIKE :location AND t.moldPartId LIKE :moldPartId"),
    @NamedQuery(name = "MstMoldPartRel.findByKey", query = "SELECT t FROM MstMoldPartRel t WHERE t.moldUuid LIKE :moldUuid AND t.location LIKE :location AND t.moldPartId LIKE :moldPartId"),
    @NamedQuery(name = "MstMoldPartRel.findByCreateDate", query = "SELECT t FROM MstMoldPartRel t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "MstMoldPartRel.findByUpdateDate", query = "SELECT t FROM MstMoldPartRel t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "MstMoldPartRel.findByCreateUserUuid", query = "SELECT t FROM MstMoldPartRel t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMoldPartRel.findByUpdateUserUuid", query = "SELECT t FROM MstMoldPartRel t WHERE t.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstMoldPartRel.updateReplaceById", query = "UPDATE MstMoldPartRel m SET "
            + "m.rplClShotCnt = :rplClShotCnt,"
            + "m.rplClProdTimeHour = :rplClProdTimeHour, "
            + "m.lastRplDatetime = :lastRplDatetime, "
            + "m.updateDate = :updateDate, "
            + "m.updateUserUuid = :updateUserUuid  WHERE m.id = :id"),
    @NamedQuery(name = "MstMoldPartRel.updateRepairById", query = "UPDATE MstMoldPartRel m SET "
            + "m.rprClShotCnt = :rprClShotCnt, "
            + "m.rprClProdTimeHour = :rprClProdTimeHour, "
            + "m.lastRprDatetime = :lastRprDatetime, "
            + "m.updateDate = :updateDate, "
            + "m.updateUserUuid = :updateUserUuid  WHERE m.id = :id"),
    @NamedQuery(name = "MstMoldPartRel.findByUpdateUserUuid", query = "SELECT t FROM MstMoldPartRel t WHERE t.updateUserUuid = :updateUserUuid"), 
    @NamedQuery(name = "MstMoldPartRel.updateById", query = "UPDATE MstMoldPartRel m SET "
            + "m.moldUuid = :moldUuid,"
            + "m.location = :location, "
            + "m.moldPartId = :moldPartId, "
            + "m.quantity = :quantity, "
            + "m.rplClShotCnt = :rplClShotCnt,"
            + "m.rplClProdTimeHour = :rplClProdTimeHour,"
            + "m.rplClLappsedDay = :rplClLappsedDay,"
            + "m.rprClShotCnt = :rprClShotCnt, "
            + "m.rprClProdTimeHour = :rprClProdTimeHour, "
            + "m.rprClLappsedDay = :rprClLappsedDay,"
            + "m.aftRplShotCnt = :aftRplShotCnt,"
            + "m.aftRplProdTimeHour = :aftRplProdTimeHour,"
            + "m.lastRplDatetime = :lastRplDatetime, "
            + "m.aftRprShotCnt = :aftRprShotCnt, "
            + "m.aftRprProdTimeHour = :aftRprProdTimeHour,"
            + "m.lastRprDatetime = :lastRprDatetime,"
            + "m.updateDate = :updateDate,"
            + "m.updateUserUuid = :updateUserUuid  WHERE m.id = :id"),
    @NamedQuery(name = "MstMoldPartRel.delete", query = "DELETE FROM MstMoldPartRel t WHERE t.moldUuid = :moldUuid AND t.location = :location AND t.moldPartId = :moldPartId"),
    @NamedQuery(name = "MstMoldPartRel.deleleById", query = "DELETE FROM MstMoldPartRel t WHERE t.id = :id")
})

@Cacheable(value = false)
public class MstMoldPartRel implements Serializable {
    private static long serialVersionUID = 1L;
    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(min = 1, max = 45)
    @Column(name = "MOLD_UUID")
    private String moldUuid;
    @Size(min = 1, max = 45)
    @Column(name = "LOCATION")
    private String location;
    @Size(min = 1, max = 45)
    @Column(name = "MOLD_PART_ID")
    private String moldPartId;
    @Column(name = "QUANTITY")
    private Integer quantity;
    @Size(max = 100)
    @Column(name = "ALIAS")
    private String alias;
    @Column(name = "RPL_CL_SHOT_CNT")
    private Integer rplClShotCnt;
    @Column(name = "RPL_CL_PROD_TIME_HOUR")
    private Integer rplClProdTimeHour;
    @Column(name = "RPL_CL_LAPPSED_DAY")
    private Integer rplClLappsedDay;
    @Column(name = "RPR_CL_SHOT_CNT")
    private Integer rprClShotCnt;
    @Column(name = "RPR_CL_PROD_TIME_HOUR")
    private Integer rprClProdTimeHour;
    @Column(name = "RPR_CL_LAPPSED_DAY")
    private Integer rprClLappsedDay;
    @Column(name = "AFT_RPL_SHOT_CNT")
    private Integer aftRplShotCnt;
    @Column(name = "AFT_RPL_PROD_TIME_HOUR")
    private BigDecimal aftRplProdTimeHour;
    @Column(name = "LAST_RPL_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastRplDatetime;
    @Column(name = "AFT_RPR_SHOT_CNT")
    private Integer aftRprShotCnt;
    @Column(name = "AFT_RPR_PROD_TIME_HOUR")
    private BigDecimal aftRprProdTimeHour;
    @Column(name = "LAST_RPR_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastRprDatetime;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Size(max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUuid;
    @Size(max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;
    @JoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(optional = false)//,cascade = CascadeType.REMOVE)
    private MstMold mstMold;
    @JoinColumn(name = "MOLD_PART_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)//,cascade = CascadeType.REMOVE)
    private MstMoldPart mstMoldPart;
    
    @Transient
    private boolean isError; //csvチェック　エラー有無　判定用
    
     public MstMoldPartRel() {
    }

    public MstMoldPartRel(String id) {
        this.id = id;
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMoldPartRel)) {
            return false;
        }
        MstMoldPartRel other = (MstMoldPartRel) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.part.rel.MstMoldPartRel[ id=" + getId() + " ]";
    }
     /**
     * @return the isError
     */
    public boolean isError() {
        return isError;
    }
    /**
     * @param isError the isError to set
     */
    public void setIsError(boolean isError) {
        this.isError = isError;
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
