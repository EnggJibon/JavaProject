/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.remodeling;

import com.kmcj.karte.resources.direction.TblDirection;
import com.kmcj.karte.resources.mold.issue.TblIssue;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.maintenance.detail.TblMoldMaintenanceDetail;
import com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetail;
import com.kmcj.karte.resources.mold.spec.history.MstMoldSpecHistory;
import com.kmcj.karte.resources.user.MstUser;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_mold_maintenance_remodeling")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.findAll", query = "SELECT t FROM TblMoldMaintenanceRemodeling t"),
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.findById", query = "SELECT t FROM TblMoldMaintenanceRemodeling t WHERE t.id = :id"),
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.findByMainteOrRemodel", query = "SELECT t FROM TblMoldMaintenanceRemodeling t WHERE t.mainteOrRemodel = :mainteOrRemodel"),
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.findByIssueId", query = "SELECT t FROM TblMoldMaintenanceRemodeling t WHERE t.issueId = :issueId"),
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.findByMainteType", query = "SELECT t FROM TblMoldMaintenanceRemodeling t WHERE t.mainteType = :mainteType"),
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.findByMainteTypeText", query = "SELECT t FROM TblMoldMaintenanceRemodeling t WHERE t.mainteTypeText = :mainteTypeText"),
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.findByMainteDate", query = "SELECT t FROM TblMoldMaintenanceRemodeling t WHERE t.mainteDate = :mainteDate"),
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.findByStartDatetime", query = "SELECT t FROM TblMoldMaintenanceRemodeling t WHERE t.startDatetime = :startDatetime"),
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.findByStartDatetimeStz", query = "SELECT t FROM TblMoldMaintenanceRemodeling t WHERE t.startDatetimeStz = :startDatetimeStz"),
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.findByEndDatetime", query = "SELECT t FROM TblMoldMaintenanceRemodeling t WHERE t.endDatetime = :endDatetime"),
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.findByEndDatetimeStz", query = "SELECT t FROM TblMoldMaintenanceRemodeling t WHERE t.endDatetimeStz = :endDatetimeStz"),
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.findByReport", query = "SELECT t FROM TblMoldMaintenanceRemodeling t WHERE t.report = :report"),
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.findByCreateDate", query = "SELECT t FROM TblMoldMaintenanceRemodeling t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.findByUpdateDate", query = "SELECT t FROM TblMoldMaintenanceRemodeling t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.findByCreateUserUuid", query = "SELECT t FROM TblMoldMaintenanceRemodeling t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.findByUpdateUserUuid", query = "SELECT t FROM TblMoldMaintenanceRemodeling t WHERE t.updateUserUuid = :updateUserUuid"),
    //set mstMoldSpecHistoryId = null
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.setMstMoldSpecHistoryIdNuLL", query = "UPDATE TblMoldMaintenanceRemodeling t SET t.moldSpecHstId = NULL WHERE t.moldSpecHstId.id = :moldSpecHstId"),
    //set Issue = null
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.setIssueNull", query = "UPDATE TblMoldMaintenanceRemodeling t SET t.issueId = NULL WHERE t.issueId = :issueId"),
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.deleteByMoldUuidAndIssueId", query = "DELETE FROM TblMoldMaintenanceRemodeling t WHERE t.moldUuid = :moldUuid And t.issueId = :issueId "),
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.deleteByMoldUuid", query = "DELETE FROM TblMoldMaintenanceRemodeling t WHERE t.moldUuid = :moldUuid"),
    // 改造選択画面 画面描画時
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.findAllByRemodel", query = "SELECT t FROM TblMoldMaintenanceRemodeling t WHERE t.mainteOrRemodel = :mainteOrRemodel  AND t.endDatetime != :endDatetime AND t.moldSpecHstIdStr IS NULL  ORDER BY t.startDatetime DESC"),
    //一覧で選択されているレコードを削除し、金型改造をなかったころにする。確認メッセージを表示し、キャンセルボタンが押されれば処理を取り消す。							
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.deleteById", query = "DELETE FROM TblMoldMaintenanceRemodeling t WHERE t.id=:id"),
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.findByMoldId", query = "SELECT t FROM TblMoldMaintenanceRemodeling t WHERE t.mstMold.moldId = :moldId And t.mainteOrRemodel = :mainteOrRemodel Order By t.startDatetime DESC "),
    @NamedQuery(name = "TblMoldMaintenanceRemodeling.findMaxEndDateByMoldUuid", query = "SELECT t FROM TblMoldMaintenanceRemodeling t WHERE t.moldUuid = :moldUuid And t.endDatetime IS NOT NULL Order By t.endDatetime DESC")
})
@Cacheable(value = false)
public class TblMoldMaintenanceRemodeling implements Serializable {

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
    @Column(name = "MAINTE_OR_REMODEL")
    private Integer mainteOrRemodel;
    @Size(max = 45)
    @Column(name = "ISSUE_ID")
    private String issueId;
    @Column(name = "MAINTE_TYPE")
    private Integer mainteType;
    @Column(name = "REMODELING_TYPE")
    private Integer remodelingType;
    @Size(max = 100)
    @Column(name = "MAINTE_TYPE_TEXT")
    private String mainteTypeText;
    @Column(name = "MAINTE_DATE")
    @Temporal(TemporalType.DATE)
    private Date mainteDate;
    @Column(name = "START_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDatetime;
    @Column(name = "START_DATETIME_STZ")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDatetimeStz;
    @Column(name = "END_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDatetime;
    @Column(name = "END_DATETIME_STZ")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDatetimeStz;
    @Size(max = 200)
    @Column(name = "REPORT")
    private String report;
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
    @Size(max = 45)
    @Column(name = "MOLD_UUID")
    private String moldUuid;

    @Size(max = 45)
    @Column(name = "MOLD_SPEC_HST_ID")
    private String moldSpecHstIdStr;
    
    @Size(max = 45)
    @Column(name = "DIRECTION_ID")
    private String directionId;

    @Size(max = 45)
    @Column(name = "DIRECTION_CODE")
    private String directionCode;
    
    // KM-361 メンテナンス所要時間の追加
    @Column(name = "WORKING_TIME_MINUTES")
    private int workingTimeMinutes;
    
    public String getMoldSpecHstIdStr() {
        return moldSpecHstIdStr;
    }

    public void setMoldSpecHstIdStr(String moldSpecHstIdStr) {
        this.moldSpecHstIdStr = moldSpecHstIdStr;
    }

    /**
     * 結合テーブル定義
     */
    // ユーザーマスタ
    //@PrimaryKeyJoinColumn(name = "UPDATE_USER_UUID", referencedColumnName = "UUID")
    @PrimaryKeyJoinColumn(name = "CREATE_USER_UUID", referencedColumnName = "UUID") //実施者は開始者なのでCREATE_USERが正しい(Kitaoji)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUser mstUser; // 実施者を取得用

    public MstUser getMstUser() {
        return this.mstUser;
    }

    public void setMstUser(MstUser mstUser) {
        this.mstUser = mstUser;
    }
    
    @OneToMany(mappedBy = "tblMoldMaintenanceRemodeling")
    private Collection<TblIssue> tblIssueCollection;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "tblMoldMaintenanceRemodeling")
    @OrderBy(value = " tblMoldRemodelingDetailPK.seq asc ")
    private Collection<TblMoldRemodelingDetail> tblMoldRemodelingDetailCollection;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "tblMoldMaintenanceRemodeling")
    @OrderBy(value = " tblMoldMaintenanceDetailPK.seq asc ")
    private Collection<TblMoldMaintenanceDetail> tblMoldMaintenanceDetailCollection;
    
    @JoinColumn(name = "MOLD_SPEC_HST_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstMoldSpecHistory moldSpecHstId;
    
    @JoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMold mstMold;

    @JoinColumn(name = "ISSUE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TblIssue tblIssue;
    
    @JoinColumn(name = "DIRECTION_ID", referencedColumnName = "ID" , insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TblDirection tblDirection;

    public TblIssue getTblIssue() {
        return tblIssue;
    }

    public void setTblIssue(TblIssue tblIssue) {
        this.tblIssue = tblIssue;
    }

    public TblMoldMaintenanceRemodeling() {
    }

    public TblMoldMaintenanceRemodeling(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMainteOrRemodel() {
        return mainteOrRemodel;
    }

    public void setMainteOrRemodel(Integer mainteOrRemodel) {
        this.mainteOrRemodel = mainteOrRemodel;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public Integer getMainteType() {
        return mainteType;
    }

    public void setMainteType(Integer mainteType) {
        this.mainteType = mainteType;
    }

    public String getMainteTypeText() {
        return mainteTypeText;
    }

    public void setMainteTypeText(String mainteTypeText) {
        this.mainteTypeText = mainteTypeText;
    }

    public Date getMainteDate() {
        return mainteDate;
    }

    public void setMainteDate(Date mainteDate) {
        this.mainteDate = mainteDate;
    }

    public Date getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Date getStartDatetimeStz() {
        return startDatetimeStz;
    }

    public void setStartDatetimeStz(Date startDatetimeStz) {
        this.startDatetimeStz = startDatetimeStz;
    }

    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    public Date getEndDatetimeStz() {
        return endDatetimeStz;
    }

    public void setEndDatetimeStz(Date endDatetimeStz) {
        this.endDatetimeStz = endDatetimeStz;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
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

    @XmlTransient
    public Collection<TblMoldRemodelingDetail> getTblMoldRemodelingDetailCollection() {
        return tblMoldRemodelingDetailCollection;
    }

    public void setTblMoldRemodelingDetailCollection(Collection<TblMoldRemodelingDetail> tblMoldRemodelingDetailCollection) {
        this.tblMoldRemodelingDetailCollection = tblMoldRemodelingDetailCollection;
    }

    @XmlTransient
    public Collection<TblIssue> getTblIssueCollection() {
        return tblIssueCollection;
    }

    public void setTblIssueCollection(Collection<TblIssue> tblIssueCollection) {
        this.tblIssueCollection = tblIssueCollection;
    }

    @XmlTransient
    public Collection<TblMoldMaintenanceDetail> getTblMoldMaintenanceDetailCollection() {
        return tblMoldMaintenanceDetailCollection;
    }

    public void setTblMoldMaintenanceDetailCollection(Collection<TblMoldMaintenanceDetail> tblMoldMaintenanceDetailCollection) {
        this.tblMoldMaintenanceDetailCollection = tblMoldMaintenanceDetailCollection;
    }

    public MstMoldSpecHistory getMoldSpecHstId() {
        return moldSpecHstId;
    }

    public void setMoldSpecHstId(MstMoldSpecHistory moldSpecHstId) {
        this.moldSpecHstId = moldSpecHstId;
    }

    public String getMoldUuid() {
        return moldUuid;
    }

    public MstMold getMstMold() {
        return mstMold;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    public void setMstMold(MstMold mstMold) {
        this.mstMold = mstMold;
    }

    public Integer getRemodelingType() {
        return remodelingType;
    }

    public void setRemodelingType(Integer remodelingType) {
        this.remodelingType = remodelingType;
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
        if (!(object instanceof TblMoldMaintenanceRemodeling)) {
            return false;
        }
        TblMoldMaintenanceRemodeling other = (TblMoldMaintenanceRemodeling) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodeling[ id=" + getId() + " ]";
    }

    /**
     * @return the directionId
     */
    public String getDirectionId() {
        return directionId;
    }

    /**
     * @param directionId the directionId to set
     */
    public void setDirectionId(String directionId) {
        this.directionId = directionId;
    }

    /**
     * @return the tblDirection
     */
    public TblDirection getTblDirection() {
        return tblDirection;
    }

    /**
     * @param tblDirection the tblDirection to set
     */
    public void setTblDirection(TblDirection tblDirection) {
        this.tblDirection = tblDirection;
    }

    /**
     * @return the directionCode
     */
    public String getDirectionCode() {
        return directionCode;
    }

    /**
     * @param directionCode the directionCode to set
     */
    public void setDirectionCode(String directionCode) {
        this.directionCode = directionCode;
    }

    /**
     * @return the workingTimeMinutes
     */
    public int getWorkingTimeMinutes() {
        return workingTimeMinutes;
    }

    /**
     * @param workingTimeMinutes the workingTimeMinutes to set
     */
    public void setWorkingTimeMinutes(int workingTimeMinutes) {
        this.workingTimeMinutes = workingTimeMinutes;
    }

    void setStartDatetime(String javaZoneId, Date strToDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
