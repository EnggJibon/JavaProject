package com.kmcj.karte.resources.machine.history;

import com.kmcj.karte.resources.machine.MstMachine;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_machine_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineHistory.findAll", query = "SELECT t FROM TblMachineHistory t"),
    
    @NamedQuery(name = "TblMachineHistory.findMaxStartDateByMachineUuid", query = "SELECT t FROM TblMachineHistory t WHERE t.tblMachineHistoryPK.machineUuid = :machineUuid ORDER BY t.tblMachineHistoryPK.firstEventNo DESC, t.tblMachineHistoryPK.startDate DESC"),
    
    @NamedQuery(name = "TblMachineHistory.findByMachineUuid", query = "SELECT t FROM TblMachineHistory t WHERE t.tblMachineHistoryPK.machineUuid = :machineUuid"),
    @NamedQuery(name = "TblMachineHistory.findByFirstEventNo", query = "SELECT t FROM TblMachineHistory t WHERE t.tblMachineHistoryPK.firstEventNo = :firstEventNo"),
    @NamedQuery(name = "TblMachineHistory.findByLastEventNo", query = "SELECT t FROM TblMachineHistory t WHERE t.lastEventNo = :lastEventNo"),
    @NamedQuery(name = "TblMachineHistory.findByStartDate", query = "SELECT t FROM TblMachineHistory t WHERE t.tblMachineHistoryPK.startDate = :startDate"),
    @NamedQuery(name = "TblMachineHistory.findByEndDate", query = "SELECT t FROM TblMachineHistory t WHERE t.endDate = :endDate"),
    @NamedQuery(name = "TblMachineHistory.findByStatus", query = "SELECT t FROM TblMachineHistory t WHERE t.status = :status"),
    @NamedQuery(name = "TblMachineHistory.findByShotCnt", query = "SELECT t FROM TblMachineHistory t WHERE t.shotCnt = :shotCnt"),
    @NamedQuery(name = "TblMachineHistory.findByLastEventDate", query = "SELECT t FROM TblMachineHistory t WHERE t.lastEventDate = :lastEventDate"),
    @NamedQuery(name = "TblMachineHistory.deleteByPK", query = "DELETE FROM TblMachineHistory t WHERE t.tblMachineHistoryPK.machineUuid = :machineUuid AND t.tblMachineHistoryPK.firstEventNo = :firstEventNo AND t.tblMachineHistoryPK.startDate = :startDate")
})
@Cacheable(value = false)
public class TblMachineHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMachineHistoryPK tblMachineHistoryPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LAST_EVENT_NO")
    private long lastEventNo;
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "STATUS")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SHOT_CNT")
    private long shotCnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LAST_EVENT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastEventDate;
    
    @Transient
    private boolean deductionFlg;
    
    @Column(name = "AGGREGATED_FLG")
    private int aggregatedFlg;
    
    @JoinColumn(name = "MACHINE_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachine mstMachine;

    public TblMachineHistory() {
    }

    public TblMachineHistory(TblMachineHistoryPK tblMachineHistoryPK) {
        this.tblMachineHistoryPK = tblMachineHistoryPK;
    }

    public TblMachineHistory(TblMachineHistoryPK tblMachineHistoryPK, long lastEventNo, String status, long shotCnt, Date lastEventDate) {
        this.tblMachineHistoryPK = tblMachineHistoryPK;
        this.lastEventNo = lastEventNo;
        this.status = status;
        this.shotCnt = shotCnt;
        this.lastEventDate = lastEventDate;
    }

    public TblMachineHistory(String machineUuid, long firstEventNo, Date startDate) {
        this.tblMachineHistoryPK = new TblMachineHistoryPK(machineUuid, firstEventNo, startDate);
    }

    public TblMachineHistoryPK getTblMachineHistoryPK() {
        return tblMachineHistoryPK;
    }

    public void setTblMachineHistoryPK(TblMachineHistoryPK tblMachineHistoryPK) {
        this.tblMachineHistoryPK = tblMachineHistoryPK;
    }

    public long getLastEventNo() {
        return lastEventNo;
    }

    public void setLastEventNo(long lastEventNo) {
        this.lastEventNo = lastEventNo;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getShotCnt() {
        return shotCnt;
    }

    public void setShotCnt(long shotCnt) {
        this.shotCnt = shotCnt;
    }

    public Date getLastEventDate() {
        return lastEventDate;
    }

    public void setLastEventDate(Date lastEventDate) {
        this.lastEventDate = lastEventDate;
    }

    public boolean getDeductionFlg() {
        return deductionFlg;
    }

    public void setDeductionFlg(boolean deductionFlg) {
        this.deductionFlg = deductionFlg;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblMachineHistoryPK != null ? tblMachineHistoryPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineHistory)) {
            return false;
        }
        TblMachineHistory other = (TblMachineHistory) object;
        if ((this.tblMachineHistoryPK == null && other.tblMachineHistoryPK != null) || (this.tblMachineHistoryPK != null && !this.tblMachineHistoryPK.equals(other.tblMachineHistoryPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.history.TblMachineHistory[ tblMachineHistoryPK=" + tblMachineHistoryPK + " ]";
    }

    /**
     * @return the mstMachine
     */
    public MstMachine getMstMachine() {
        return mstMachine;
    }

    /**
     * @param mstMachine the mstMachine to set
     */
    public void setMstMachine(MstMachine mstMachine) {
        this.mstMachine = mstMachine;
    }

    /**
     * @return the aggregatedFlg
     */
    public int getAggregatedFlg() {
        return aggregatedFlg;
    }

    /**
     * @param aggregatedFlg the aggregatedFlg to set
     */
    public void setAggregatedFlg(int aggregatedFlg) {
        this.aggregatedFlg = aggregatedFlg;
    }

}
