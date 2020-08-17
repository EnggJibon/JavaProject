/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.work.phase;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * MstWorkPhaseエンティティ
 * @author t.ariki
 */
@Entity
@Table(name = "mst_work_phase")
@XmlRootElement
@NamedQueries({
    //SELECT
    @NamedQuery(name = "MstWorkPhase.findAll", query = "SELECT m FROM MstWorkPhase m"),
    @NamedQuery(name = "MstWorkPhase.findAllOrderByWorkPhaseCode", query = "SELECT m FROM MstWorkPhase m ORDER BY m.workPhaseCode"),
    @NamedQuery(name = "MstWorkPhase.findNextWorkPhase", query = "SELECT m FROM MstWorkPhase m WHERE EXISTS (SELECT f FROM MstWorkPhaseFlow f WHERE m.id = f.nextWorkPhaseId AND f.workPhaseId = :workPhaseId) ORDER BY m.workPhaseCode"),
    @NamedQuery(name = "MstWorkPhase.findById", query = "SELECT m FROM MstWorkPhase m WHERE m.id = :id"),
    @NamedQuery(name = "MstWorkPhase.findByWorkPhaseCode", query = "SELECT m FROM MstWorkPhase m WHERE m.workPhaseCode = :workPhaseCode"),
    @NamedQuery(name = "MstWorkPhase.findByWorkPhaseName", query = "SELECT m FROM MstWorkPhase m WHERE m.workPhaseName = :workPhaseName"),
    @NamedQuery(name = "MstWorkPhase.findByWorkPhaseType", query = "SELECT m FROM MstWorkPhase m WHERE m.workPhaseType = :workPhaseType"),
    @NamedQuery(name = "MstWorkPhase.findByChoiceSeq", query = "SELECT m FROM MstWorkPhase m WHERE m.choiceSeq = :choiceSeq"),
    @NamedQuery(name = "MstWorkPhase.findByCreateDate", query = "SELECT m FROM MstWorkPhase m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstWorkPhase.findByUpdateDate", query = "SELECT m FROM MstWorkPhase m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstWorkPhase.findByCreateUserUuid", query = "SELECT m FROM MstWorkPhase m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstWorkPhase.findByUpdateUserUuid", query = "SELECT m FROM MstWorkPhase m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstWorkPhase.findMaxChoiceSeq", query = "SELECT MAX(m.choiceSeq) FROM MstWorkPhase m "),
    //DELETE
    @NamedQuery(name = "MstWorkPhase.delete", query = "DELETE FROM MstWorkPhase m WHERE m.id = :id"),
    //UPDATE
    @NamedQuery(name = "MstWorkPhase.update", query = "UPDATE MstWorkPhase m SET m.workPhaseName = :workPhaseName, m.workPhaseType = :workPhaseType, m.updateDate = :updateDate, m.updateUserUuid = :updateUserUuid,m.directFlg = :directFlg WHERE m.id = :id")
})
@Cacheable(value = false)
public class MstWorkPhase implements Serializable {
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "WORK_PHASE_CODE")
    private String workPhaseCode;
    @Size(max = 45)
    @Column(name = "WORK_PHASE_NAME")
    private String workPhaseName;
    @Size(max = 45)
    @Column(name = "WORK_PHASE_TYPE")
    private String workPhaseType;
    @Column(name = "CHOICE_SEQ")
    private Integer choiceSeq;
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
    
    @Column(name = "DIRECT_FLG")
    private Integer directFlg; //直接・間接区分(0:間接、1:直接)

    @Transient
    private List<String> nextWorkPhaseIds;
    
    @Transient
    private List<String> departmentIds;
    
    @Transient
    private boolean deleted = false;    // 削除対象制御
    @Transient
    private boolean modified = false;   // 更新対象制御
    @Transient
    private boolean added = false;      // 登録対象制御
    
    public MstWorkPhase() {
    }

    public MstWorkPhase(String workPhaseCode) {
        this.workPhaseCode = workPhaseCode;
    }

    public MstWorkPhase(String workPhaseCode, String id) {
        this.workPhaseCode = workPhaseCode;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkPhaseCode() {
        return workPhaseCode;
    }

    public void setWorkPhaseCode(String workPhaseCode) {
        this.workPhaseCode = workPhaseCode;
    }

    public String getWorkPhaseName() {
        return workPhaseName;
    }

    public void setWorkPhaseName(String workPhaseName) {
        this.workPhaseName = workPhaseName;
    }

    public String getWorkPhaseType() {
        return workPhaseType;
    }

    public void setWorkPhaseType(String workPhaseType) {
        this.workPhaseType = workPhaseType;
    }

    public Integer getChoiceSeq() {
        return choiceSeq;
    }

    public void setChoiceSeq(Integer choiceSeq) {
        this.choiceSeq = choiceSeq;
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
        hash += (workPhaseCode != null ? workPhaseCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstWorkPhase)) {
            return false;
        }
        MstWorkPhase other = (MstWorkPhase) object;
        if ((this.workPhaseCode == null && other.workPhaseCode != null) || (this.workPhaseCode != null && !this.workPhaseCode.equals(other.workPhaseCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.work.phase.MstWorkPhase[ workPhaseCode=" + workPhaseCode + " ]";
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

    /**
     * @return the nextWorkPhaseIds
     */
    @XmlElement
    public List<String> getNextWorkPhaseIds() {
        return nextWorkPhaseIds;
    }

    /**
     * @param nextWorkPhaseIds the nextWorkPhaseIds to set
     */
    @XmlElement
    public void setNextWorkPhaseIds(List<String> nextWorkPhaseIds) {
        this.nextWorkPhaseIds = nextWorkPhaseIds;
    }

    /**
     * @return the directFlg
     */
    public Integer getDirectFlg() {
        return directFlg;
    }

    /**
     * @param directFlg the directFlg to set
     */
    public void setDirectFlg(Integer directFlg) {
        this.directFlg = directFlg;
    }

    @XmlElement
    public void setDepartmentIds(List<String> departmentIds) {
        this.departmentIds = departmentIds;
    }

    @XmlElement
    public List<String> getDepartmentIds() {
        return departmentIds;
    }
}
