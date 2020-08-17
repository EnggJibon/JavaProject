/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.dailyreport2;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.downtime.MstMachineDowntime;
import com.kmcj.karte.resources.machine.downtime.MstMachineDowntimeService;
import com.kmcj.karte.resources.production.defect.TblProductionDefectDaily;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.XmlDateAdapter2;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author f.kitaoji
 */
@Entity
@Table(name = "tbl_machine_daily_report2")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineDailyReport2.findAll", 
            query = "SELECT t FROM TblMachineDailyReport2 t LEFT JOIN FETCH t.tblMachineDailyReport2DetailList "),
    @NamedQuery(name = "TblMachineDailyReport2.findById", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.id = :id"),
    @NamedQuery(name = "TblMachineDailyReport2.findByReportDate", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.reportDate = :reportDate"),
    @NamedQuery(name = "TblMachineDailyReport2.findByUniqueKey", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.reportDate = :reportDate AND t.machineUuid = :machineUuid"),
    @NamedQuery(name = "TblMachineDailyReport2.findByTotalOperatingMinutes", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.totalOperatingMinutes = :totalOperatingMinutes"),
    @NamedQuery(name = "TblMachineDailyReport2.findByTotalDowntimeMinutes", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.totalDowntimeMinutes = :totalDowntimeMinutes"),
    @NamedQuery(name = "TblMachineDailyReport2.findByTotalPlannedDowntimeMinutes", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.totalPlannedDowntimeMinutes = :totalPlannedDowntimeMinutes"),
    @NamedQuery(name = "TblMachineDailyReport2.findByTotalUnplannedDowntimeMinutes", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.totalUnplannedDowntimeMinutes = :totalUnplannedDowntimeMinutes"),
    @NamedQuery(name = "TblMachineDailyReport2.findByTotalShotCount", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.totalShotCount = :totalShotCount"),
    @NamedQuery(name = "TblMachineDailyReport2.findByTotalCompleteCount", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.totalCompleteCount = :totalCompleteCount"),
    @NamedQuery(name = "TblMachineDailyReport2.findByTotalDefectCount", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.totalDefectCount = :totalDefectCount"),
    @NamedQuery(name = "TblMachineDailyReport2.findByTotalDefectCountType01", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.totalDefectCountType01 = :totalDefectCountType01"),
    @NamedQuery(name = "TblMachineDailyReport2.findByTotalDefectCountType02", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.totalDefectCountType02 = :totalDefectCountType02"),
    @NamedQuery(name = "TblMachineDailyReport2.findByTotalDefectCountType03", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.totalDefectCountType03 = :totalDefectCountType03"),
    @NamedQuery(name = "TblMachineDailyReport2.findByTotalDefectCountType04", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.totalDefectCountType04 = :totalDefectCountType04"),
    @NamedQuery(name = "TblMachineDailyReport2.findByTotalDefectCountType05", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.totalDefectCountType05 = :totalDefectCountType05"),
    @NamedQuery(name = "TblMachineDailyReport2.findByTotalDefectCountType06", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.totalDefectCountType06 = :totalDefectCountType06"),
    @NamedQuery(name = "TblMachineDailyReport2.findByTotalDefectCountType07", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.totalDefectCountType07 = :totalDefectCountType07"),
    @NamedQuery(name = "TblMachineDailyReport2.findByTotalDefectCountType08", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.totalDefectCountType08 = :totalDefectCountType08"),
    @NamedQuery(name = "TblMachineDailyReport2.findByTotalDefectCountType09", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.totalDefectCountType09 = :totalDefectCountType09"),
    @NamedQuery(name = "TblMachineDailyReport2.findByTotalDefectCountType10", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.totalDefectCountType10 = :totalDefectCountType10"),
    @NamedQuery(name = "TblMachineDailyReport2.findByTotalDefectCountOther", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.totalDefectCountOther = :totalDefectCountOther"),
    @NamedQuery(name = "TblMachineDailyReport2.findByReportPersonUuid", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.reportPersonUuid = :reportPersonUuid"),
    @NamedQuery(name = "TblMachineDailyReport2.findByCreateDate", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMachineDailyReport2.findByUpdateDate", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMachineDailyReport2.findByCreateUserUuid", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblMachineDailyReport2.findByUpdateUserUuid", query = "SELECT t FROM TblMachineDailyReport2 t WHERE t.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class TblMachineDailyReport2 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Column(name = "REPORT_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter2.class)
    private Date reportDate;
    @Size(max = 45)
    @Column(name = "MACHINE_UUID")
    private String machineUuid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_TIME_MINUTES")
    private int totalTimeMinutes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_OPERATING_MINUTES")
    private int totalOperatingMinutes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_DOWNTIME_MINUTES")
    private int totalDowntimeMinutes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_PLANNED_DOWNTIME_MINUTES")
    private int totalPlannedDowntimeMinutes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_UNPLANNED_DOWNTIME_MINUTES")
    private int totalUnplannedDowntimeMinutes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_SHOT_COUNT")
    private int totalShotCount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_COMPLETE_COUNT")
    private int totalCompleteCount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_DEFECT_COUNT")
    private int totalDefectCount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_DEFECT_COUNT_TYPE01")
    private int totalDefectCountType01;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_DEFECT_COUNT_TYPE02")
    private int totalDefectCountType02;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_DEFECT_COUNT_TYPE03")
    private int totalDefectCountType03;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_DEFECT_COUNT_TYPE04")
    private int totalDefectCountType04;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_DEFECT_COUNT_TYPE05")
    private int totalDefectCountType05;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_DEFECT_COUNT_TYPE06")
    private int totalDefectCountType06;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_DEFECT_COUNT_TYPE07")
    private int totalDefectCountType07;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_DEFECT_COUNT_TYPE08")
    private int totalDefectCountType08;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_DEFECT_COUNT_TYPE09")
    private int totalDefectCountType09;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_DEFECT_COUNT_TYPE10")
    private int totalDefectCountType10;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_DEFECT_COUNT_OTHER")
    private int totalDefectCountOther;
    @Size(max = 45)
    @Column(name = "REPORT_PERSON_UUID")
    private String reportPersonUuid;
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
    @OneToMany(mappedBy = "report")
    @XmlElement(name = "machineDailyReportDetails")
    @OrderBy("startDatetime ASC")
    private List<TblMachineDailyReport2Detail> tblMachineDailyReport2DetailList;

    @JoinColumn(name = "MACHINE_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne
    private MstMachine mstMachine;
    @JoinColumn(name = "REPORT_PERSON_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne
    private MstUser mstUser;
    
    @Transient
    private String machineId;
    @Transient
    private String machineName;
    @Transient
    private String reportPersonId;
    @Transient
    private String reportPersonName;
    @Transient
    private boolean deleted = false;    // 削除対象制御
    @Transient
    private boolean modified = false;   // 更新対象制御
    @Transient
    private boolean added = false;      // 登録対象制御
    @Transient
    private BigDecimal operatingRate = new BigDecimal("0.00"); //稼働率
    @Transient
    private BigDecimal plannedOperatingRate = new BigDecimal("0.00"); //計画稼働率
    

    @Transient
    public static int DETAIL_TYPE_WORK = 1;
    @Transient
    public static int DETAIL_TYPE_PROD = 2;
    @Transient
    public static int DETAIL_TYPE_DOWNTIME = 3;
    
    @Transient
    private List<TblProductionDefectDaily> defectInfoList;// 機械日報不良情報

    public TblMachineDailyReport2() {
    }

    public TblMachineDailyReport2(String id) {
        this.id = id;
    }

    public TblMachineDailyReport2(String id, int totalOperatingMinutes, int totalDowntimeMinutes, int totalPlannedDowntimeMinutes, int totalUnplannedDowntimeMinutes, int totalShotCount, int totalCompleteCount, int totalDefectCount, int totalDefectCountType01, int totalDefectCountType02, int totalDefectCountType03, int totalDefectCountType04, int totalDefectCountType05, int totalDefectCountType06, int totalDefectCountType07, int totalDefectCountType08, int totalDefectCountType09, int totalDefectCountType10, int totalDefectCountOther, String reportPersonUuid) {
        this.id = id;
        this.totalOperatingMinutes = totalOperatingMinutes;
        this.totalDowntimeMinutes = totalDowntimeMinutes;
        this.totalPlannedDowntimeMinutes = totalPlannedDowntimeMinutes;
        this.totalUnplannedDowntimeMinutes = totalUnplannedDowntimeMinutes;
        this.totalShotCount = totalShotCount;
        this.totalCompleteCount = totalCompleteCount;
        this.totalDefectCount = totalDefectCount;
        this.totalDefectCountType01 = totalDefectCountType01;
        this.totalDefectCountType02 = totalDefectCountType02;
        this.totalDefectCountType03 = totalDefectCountType03;
        this.totalDefectCountType04 = totalDefectCountType04;
        this.totalDefectCountType05 = totalDefectCountType05;
        this.totalDefectCountType06 = totalDefectCountType06;
        this.totalDefectCountType07 = totalDefectCountType07;
        this.totalDefectCountType08 = totalDefectCountType08;
        this.totalDefectCountType09 = totalDefectCountType09;
        this.totalDefectCountType10 = totalDefectCountType10;
        this.totalDefectCountOther = totalDefectCountOther;
        this.reportPersonUuid = reportPersonUuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public int getTotalOperatingMinutes() {
        return totalOperatingMinutes;
    }

    public void setTotalOperatingMinutes(int totalOperatingMinutes) {
        this.totalOperatingMinutes = totalOperatingMinutes;
    }

    public int getTotalDowntimeMinutes() {
        return totalDowntimeMinutes;
    }

    public void setTotalDowntimeMinutes(int totalDowntimeMinutes) {
        this.totalDowntimeMinutes = totalDowntimeMinutes;
    }

    public int getTotalPlannedDowntimeMinutes() {
        return totalPlannedDowntimeMinutes;
    }

    public void setTotalPlannedDowntimeMinutes(int totalPlannedDowntimeMinutes) {
        this.totalPlannedDowntimeMinutes = totalPlannedDowntimeMinutes;
    }

    public int getTotalUnplannedDowntimeMinutes() {
        return totalUnplannedDowntimeMinutes;
    }

    public void setTotalUnplannedDowntimeMinutes(int totalUnplannedDowntimeMinutes) {
        this.totalUnplannedDowntimeMinutes = totalUnplannedDowntimeMinutes;
    }

    public int getTotalShotCount() {
        return totalShotCount;
    }

    public void setTotalShotCount(int totalShotCount) {
        this.totalShotCount = totalShotCount;
    }

    public int getTotalCompleteCount() {
        return totalCompleteCount;
    }

    public void setTotalCompleteCount(int totalCompleteCount) {
        this.totalCompleteCount = totalCompleteCount;
    }

    public int getTotalDefectCount() {
        return totalDefectCount;
    }

    public void setTotalDefectCount(int totalDefectCount) {
        this.totalDefectCount = totalDefectCount;
    }

    public int getTotalDefectCountType01() {
        return totalDefectCountType01;
    }

    public void setTotalDefectCountType01(int totalDefectCountType01) {
        this.totalDefectCountType01 = totalDefectCountType01;
    }

    public int getTotalDefectCountType02() {
        return totalDefectCountType02;
    }

    public void setTotalDefectCountType02(int totalDefectCountType02) {
        this.totalDefectCountType02 = totalDefectCountType02;
    }

    public int getTotalDefectCountType03() {
        return totalDefectCountType03;
    }

    public void setTotalDefectCountType03(int totalDefectCountType03) {
        this.totalDefectCountType03 = totalDefectCountType03;
    }

    public int getTotalDefectCountType04() {
        return totalDefectCountType04;
    }

    public void setTotalDefectCountType04(int totalDefectCountType04) {
        this.totalDefectCountType04 = totalDefectCountType04;
    }

    public int getTotalDefectCountType05() {
        return totalDefectCountType05;
    }

    public void setTotalDefectCountType05(int totalDefectCountType05) {
        this.totalDefectCountType05 = totalDefectCountType05;
    }

    public int getTotalDefectCountType06() {
        return totalDefectCountType06;
    }

    public void setTotalDefectCountType06(int totalDefectCountType06) {
        this.totalDefectCountType06 = totalDefectCountType06;
    }

    public int getTotalDefectCountType07() {
        return totalDefectCountType07;
    }

    public void setTotalDefectCountType07(int totalDefectCountType07) {
        this.totalDefectCountType07 = totalDefectCountType07;
    }

    public int getTotalDefectCountType08() {
        return totalDefectCountType08;
    }

    public void setTotalDefectCountType08(int totalDefectCountType08) {
        this.totalDefectCountType08 = totalDefectCountType08;
    }

    public int getTotalDefectCountType09() {
        return totalDefectCountType09;
    }

    public void setTotalDefectCountType09(int totalDefectCountType09) {
        this.totalDefectCountType09 = totalDefectCountType09;
    }

    public int getTotalDefectCountType10() {
        return totalDefectCountType10;
    }

    public void setTotalDefectCountType10(int totalDefectCountType10) {
        this.totalDefectCountType10 = totalDefectCountType10;
    }

    public int getTotalDefectCountOther() {
        return totalDefectCountOther;
    }

    public void setTotalDefectCountOther(int totalDefectCountOther) {
        this.totalDefectCountOther = totalDefectCountOther;
    }

    public String getReportPersonUuid() {
        return reportPersonUuid;
    }

    public void setReportPersonUuid(String reportPersonUuid) {
        this.reportPersonUuid = reportPersonUuid;
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

    //@XmlTransient
    public List<TblMachineDailyReport2Detail> getTblMachineDailyReport2DetailList() {
        return tblMachineDailyReport2DetailList;
    }

    public void setTblMachineDailyReport2DetailList(List<TblMachineDailyReport2Detail> tblMachineDailyReport2DetailList) {
        this.tblMachineDailyReport2DetailList = tblMachineDailyReport2DetailList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineDailyReport2)) {
            return false;
        }
        TblMachineDailyReport2 other = (TblMachineDailyReport2) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.dailyreport2.TblMachineDailyReport2[ id=" + id + " ]";
    }

    /**
     * @return the mstMachine
     */
    @XmlTransient
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
     * @return the deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * @return the modified
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * @param modified the modified to set
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    /**
     * @return the added
     */
    public boolean isAdded() {
        return added;
    }

    /**
     * @param added the added to set
     */
    public void setAdded(boolean added) {
        this.added = added;
    }

    public void formatJson(Date inReportDate) {
        formatJson(true, inReportDate);
    }
    
    public void formatJson(boolean sortProdDetailByComponentCode, Date inReportDate) {
        if (mstMachine != null) {
            this.machineId = mstMachine.getMachineId();
            this.machineName = mstMachine.getMachineName();
        }
        if (mstUser != null) {
            this.reportPersonId = mstUser.getUserId();
            this.reportPersonName = mstUser.getUserName();
        }
        for (TblMachineDailyReport2Detail detail : tblMachineDailyReport2DetailList) {
            detail.setStartDatetimeStr(new FileUtil().getDateTimeFormatForStr(detail.getStartDatetime()));
            detail.setEndDatetimeStr(new FileUtil().getDateTimeFormatForStr(detail.getEndDatetime()));
            detail.formatJson(sortProdDetailByComponentCode, inReportDate);
        }
    }
    
    public void getMstMachineDowntimes(MstMachineDowntimeService downtimeService) {
        if (this.tblMachineDailyReport2DetailList == null) return;
        for (TblMachineDailyReport2Detail detail : this.getTblMachineDailyReport2DetailList()) {
            if (detail.isDeleted()) continue; //削除済みの時除外
            if (detail.getDetailType() == DETAIL_TYPE_DOWNTIME) {
                if (detail.getMachineDowntimeId() != null && detail.getMstMachineDowntime() == null) {
                    MstMachineDowntime downtime = downtimeService.getMstMachineDowntimeById(detail.getMachineDowntimeId());
                    detail.setMstMachineDowntime(downtime);
                }
            }
        }
    }
    
    public void calcTotal() {
        if (this.tblMachineDailyReport2DetailList == null) return;
        this.totalTimeMinutes = 0;
        this.totalOperatingMinutes = 0;
        this.totalDowntimeMinutes = 0;
        this.totalUnplannedDowntimeMinutes = 0;
        this.totalPlannedDowntimeMinutes = 0;
        this.totalShotCount = 0;
        this.totalCompleteCount = 0;
        this.totalDefectCount = 0;
        this.totalDefectCountType01 = 0;
        this.totalDefectCountType02 = 0;
        this.totalDefectCountType03 = 0;
        this.totalDefectCountType04 = 0;
        this.totalDefectCountType05 = 0;
        this.totalDefectCountType06 = 0;
        this.totalDefectCountType07 = 0;
        this.totalDefectCountType08 = 0;
        this.totalDefectCountType09 = 0;
        this.totalDefectCountType10 = 0;
        this.totalDefectCountOther = 0;
        for (TblMachineDailyReport2Detail detail : this.getTblMachineDailyReport2DetailList()) {
            if (detail.isDeleted()) continue; //削除済みの時除外
            this.totalTimeMinutes += detail.getDurationMinitues();
            if (detail.getOperatingFlg() == 1) {
                this.totalOperatingMinutes += detail.getDurationMinitues();
            }
            else {
                this.totalDowntimeMinutes += detail.getDurationMinitues();
                if (detail.getDetailType() == DETAIL_TYPE_WORK) {
                    //作業のときは必ず計画停止
                    this.totalPlannedDowntimeMinutes += detail.getDurationMinitues();
                }
                else if (detail.getDetailType() == DETAIL_TYPE_DOWNTIME) {
                    //停止時間マスタの定義に従う
                    if (detail.getMstMachineDowntime() != null) {
                        if (detail.getMstMachineDowntime().getPlannedFlg() == 0) {
                            this.totalUnplannedDowntimeMinutes += detail.getDurationMinitues();
                        }
                        else {
                            this.totalPlannedDowntimeMinutes += detail.getDurationMinitues();
                        }
                    }
                }
            }
            this.totalShotCount += detail.getShotCount() + detail.getDisposedShotCount();
            if (detail.getDetailType() == 2 && detail.getTblMachineDailyReport2ProdDetailList() != null) {
                int prodCount = 0;
                for (TblMachineDailyReport2ProdDetail prodDetail: detail.getTblMachineDailyReport2ProdDetailList()) {
                    if (prodDetail.isDeleted()) continue;
                    this.totalCompleteCount += prodDetail.getCompleteCount();
                    this.totalDefectCount += prodDetail.getDefectCount();
                    this.totalDefectCountType01 += prodDetail.getDefectCountType01();
                    this.totalDefectCountType02 += prodDetail.getDefectCountType02();
                    this.totalDefectCountType03 += prodDetail.getDefectCountType03();
                    this.totalDefectCountType04 += prodDetail.getDefectCountType04();
                    this.totalDefectCountType05 += prodDetail.getDefectCountType05();
                    this.totalDefectCountType06 += prodDetail.getDefectCountType06();
                    this.totalDefectCountType07 += prodDetail.getDefectCountType07();
                    this.totalDefectCountType08 += prodDetail.getDefectCountType08();
                    this.totalDefectCountType09 += prodDetail.getDefectCountType09();
                    this.totalDefectCountType10 += prodDetail.getDefectCountType10();
                    this.totalDefectCountOther += prodDetail.getDefectCountOther();
                    prodCount++;
                }
                //時間を部品ごとに案分する
                if (prodCount > 0) {
                    int componentDurationMinutes = detail.getDurationMinitues() / prodCount;
                    int counter = 0;
                    int addedDuration = 0;
                    for (TblMachineDailyReport2ProdDetail prodDetail: detail.getTblMachineDailyReport2ProdDetailList()) {
                        if (prodDetail.isDeleted()) continue;
                        counter++;
                        if (prodCount == counter) {
                            prodDetail.setComponentOperatingMinutes(detail.getDurationMinitues() - addedDuration);
                        }
                        else {
                            prodDetail.setComponentOperatingMinutes(componentDurationMinutes);
                            addedDuration += componentDurationMinutes;
                        }
                    }
                }
            }
        }
        calcOperatingRates();
    }
    
    public void calcOperatingRates() {
        //稼働率計算
        BigDecimal dayMinutes = new BigDecimal(1440);
        BigDecimal bdTotalOperatingMinutes = new BigDecimal(totalOperatingMinutes);
        BigDecimal bdTotalPlannedDowntimeMinutes = new BigDecimal(totalPlannedDowntimeMinutes);
        BigDecimal bdPlannedTime = dayMinutes.subtract(bdTotalPlannedDowntimeMinutes); //1440分から計画停止時間を除いた時間
        operatingRate = bdTotalOperatingMinutes.divide(dayMinutes, 4, BigDecimal.ROUND_HALF_UP);
        if (bdPlannedTime.compareTo(BigDecimal.ZERO) != 0) { //0除算回避
            plannedOperatingRate = bdTotalOperatingMinutes.divide(bdPlannedTime, 4, BigDecimal.ROUND_HALF_UP);
        }
    }
    
    public void sortDatailByStartTime() {
        if (this.tblMachineDailyReport2DetailList == null) return;
        //EclipseLinkのバグ回避のため、別のListに移してからソート。
        List<TblMachineDailyReport2Detail> list = new ArrayList<>();
        for (TblMachineDailyReport2Detail detail : this.tblMachineDailyReport2DetailList) {
            list.add(detail);
        }
        Collections.sort(list, new Comparator<TblMachineDailyReport2Detail>(){
            @Override
            public int compare(TblMachineDailyReport2Detail a, TblMachineDailyReport2Detail b){
                if (a.getStartDatetime()== null && b.getStartDatetime() == null) return 0;
                else if (a.getStartDatetime() == null && b.getStartDatetime() != null) return 1;
                else if (a.getStartDatetime() != null && b.getStartDatetime() == null) return -1;
                else return a.getStartDatetime().compareTo( b.getStartDatetime() );  
            }
        });
        this.tblMachineDailyReport2DetailList = list;
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
     * @return the mstUser
     */
    @XmlTransient
    public MstUser getMstUser() {
        return mstUser;
    }

    /**
     * @param mstUser the mstUser to set
     */
    public void setMstUser(MstUser mstUser) {
        this.mstUser = mstUser;
    }

    /**
     * @return the reportPersonId
     */
    public String getReportPersonId() {
        return reportPersonId;
    }

    /**
     * @param reportPersonId the reportPersonId to set
     */
    public void setReportPersonId(String reportPersonId) {
        this.reportPersonId = reportPersonId;
    }

    /**
     * @return the reportPersonName
     */
    public String getReportPersonName() {
        return reportPersonName;
    }

    /**
     * @param reportPersonName the reportPersonName to set
     */
    public void setReportPersonName(String reportPersonName) {
        this.reportPersonName = reportPersonName;
    }

    /**
     * @return the totalTimeMinutes
     */
    public int getTotalTimeMinutes() {
        return totalTimeMinutes;
    }

    /**
     * @param totalTimeMinutes the totalTimeMinutes to set
     */
    public void setTotalTimeMinutes(int totalTimeMinutes) {
        this.totalTimeMinutes = totalTimeMinutes;
    }

    /**
     * IDを指定して明細オブジェクトを取得
     * @param reportDetailId
     * @return 
     */
    public TblMachineDailyReport2Detail getReportDetailById(String reportDetailId) {
        if (this.tblMachineDailyReport2DetailList != null) {
            for (TblMachineDailyReport2Detail detail: this.tblMachineDailyReport2DetailList) {
                if (detail.getId() != null && detail.getId().equals(reportDetailId)) {
                    return detail;
                }
            }
            return null;
        }
        else {
            return null;
        }
    }
    
    /**
     * IDを指定して生産明細オブジェクトを取得
     * @param reportProdDetailId
     * @return 
     */ 
    public TblMachineDailyReport2ProdDetail getReportProdDetailById(String reportProdDetailId) {
        if (this.tblMachineDailyReport2DetailList != null) {
            for (TblMachineDailyReport2Detail detail: this.tblMachineDailyReport2DetailList) {
                if (detail.getTblMachineDailyReport2ProdDetailList() != null) {
                    for (TblMachineDailyReport2ProdDetail prodDetail : detail.getTblMachineDailyReport2ProdDetailList()) {
                        if (prodDetail.getId() != null && prodDetail.getId().equals(reportProdDetailId)) {
                            return prodDetail;
                        }
                    }
                }
            }
            return null;
        }
        else {
            return null;
        }
        
    }

    /**
     * @return the operatingRate
     */
    public BigDecimal getOperatingRate() {
        return operatingRate;
    }

    /**
     * @param operatingRate the operatingRate to set
     */
    public void setOperatingRate(BigDecimal operatingRate) {
        this.operatingRate = operatingRate;
    }

    /**
     * @return the plannedOperatingRate
     */
    public BigDecimal getPlannedOperatingRate() {
        return plannedOperatingRate;
    }

    /**
     * @param plannedOperatingRate the plannedOperatingRate to set
     */
    public void setPlannedOperatingRate(BigDecimal plannedOperatingRate) {
        this.plannedOperatingRate = plannedOperatingRate;
    }

    public List<TblProductionDefectDaily> getDefectInfoList() {
        return defectInfoList;
    }

    public void setDefectInfoList(List<TblProductionDefectDaily> defectInfoList) {
        this.defectInfoList = defectInfoList;
    }
    
}
