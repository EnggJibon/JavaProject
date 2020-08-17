/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.inspection.item;

import com.kmcj.karte.resources.mold.inspection.choice.MstMoldInspectionChoice;
import com.kmcj.karte.resources.mold.inspection.result.TblMoldInspectionResult;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "mst_mold_inspection_item")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMoldInspectionItem.findAll", query = "SELECT m FROM MstMoldInspectionItem m order by m.taskCategory1,m.taskCategory2,m.taskCategory3"),
    @NamedQuery(name = "MstMoldInspectionItem.findAllGroupbyTaskCategory", query = "SELECT m FROM MstMoldInspectionItem m where m.externalFlg=0 group by m.taskCategory1,m.taskCategory2,m.taskCategory3 order by m.taskCategory1,m.taskCategory2,m.taskCategory3"),
    @NamedQuery(name = "MstMoldInspectionItem.findById", query = "SELECT m FROM MstMoldInspectionItem m WHERE m.id = :id"),
    @NamedQuery(name = "MstMoldInspectionItem.findByTaskCategory1", query = "SELECT m FROM MstMoldInspectionItem m WHERE m.taskCategory1 = :taskCategory1"),
    @NamedQuery(name = "MstMoldInspectionItem.findByTaskCategory2", query = "SELECT m FROM MstMoldInspectionItem m WHERE m.taskCategory2 = :taskCategory2"),
    @NamedQuery(name = "MstMoldInspectionItem.findByTaskCategory3", query = "SELECT m FROM MstMoldInspectionItem m WHERE m.taskCategory3 = :taskCategory3"),
    @NamedQuery(name = "MstMoldInspectionItem.findBySeq", query = "SELECT m FROM MstMoldInspectionItem m WHERE m.seq = :seq"),
    @NamedQuery(name = "MstMoldInspectionItem.findByInspectionItemName", query = "SELECT m FROM MstMoldInspectionItem m WHERE m.inspectionItemName = :inspectionItemName"),
    @NamedQuery(name = "MstMoldInspectionItem.findByResultType", query = "SELECT m FROM MstMoldInspectionItem m WHERE m.resultType = :resultType"),
    @NamedQuery(name = "MstMoldInspectionItem.findByCreateDate", query = "SELECT m FROM MstMoldInspectionItem m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMoldInspectionItem.findByUpdateDate", query = "SELECT m FROM MstMoldInspectionItem m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMoldInspectionItem.findByCreateDateUuid", query = "SELECT m FROM MstMoldInspectionItem m WHERE m.createDateUuid = :createDateUuid"),
    @NamedQuery(name = "MstMoldInspectionItem.findByUpdateUserUuid", query = "SELECT m FROM MstMoldInspectionItem m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstMoldInspectionItem.deleteByTaskCategory", query = "DELETE FROM MstMoldInspectionItem m WHERE m.taskCategory1 = :taskCategory1 and m.taskCategory2 = :taskCategory2 and m.taskCategory3 = :taskCategory3"),
    @NamedQuery(name = "MstMoldInspectionItem.deleteById", query = "DELETE FROM MstMoldInspectionItem m WHERE m.id = :id")
})
@Cacheable(value = false)
public class MstMoldInspectionItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TASK_CATEGORY1")
    private int taskCategory1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TASK_CATEGORY2")
    private int taskCategory2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TASK_CATEGORY3")
    private int taskCategory3;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private int seq;
    @Size(max = 50)
    @Column(name = "INSPECTION_ITEM_NAME")
    private String inspectionItemName;
    @Column(name = "RESULT_TYPE")
    private Integer resultType;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Size(max = 45)
    @Column(name = "CREATE_DATE_UUID")
    private String createDateUuid;
    @Size(max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;
    @Column(name = "EXTERNAL_FLG")
    private Integer externalFlg;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mstMoldInspectionItem")
    private Collection<TblMoldInspectionResult> tblMoldInspectionResultCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mstMoldInspectionItem")
    private Collection<MstMoldInspectionChoice> mstMoldInspectionChoiceCollection;

    public MstMoldInspectionItem() {
    }

    public MstMoldInspectionItem(String id) {
        this.id = id;
    }

    public MstMoldInspectionItem(int taskCategory1, int taskCategory2, int taskCategory3) {
        this.taskCategory1 = taskCategory1;
        this.taskCategory2 = taskCategory2;
        this.taskCategory3 = taskCategory3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTaskCategory1() {
        return taskCategory1;
    }

    public void setTaskCategory1(int taskCategory1) {
        this.taskCategory1 = taskCategory1;
    }

    public int getTaskCategory2() {
        return taskCategory2;
    }

    public void setTaskCategory2(int taskCategory2) {
        this.taskCategory2 = taskCategory2;
    }

    public int getTaskCategory3() {
        return taskCategory3;
    }

    public void setTaskCategory3(int taskCategory3) {
        this.taskCategory3 = taskCategory3;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getInspectionItemName() {
        return inspectionItemName;
    }

    public void setInspectionItemName(String inspectionItemName) {
        this.inspectionItemName = inspectionItemName;
    }

    public Integer getResultType() {
        return resultType;
    }

    public void setResultType(Integer resultType) {
        this.resultType = resultType;
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

    public String getCreateDateUuid() {
        return createDateUuid;
    }

    public void setCreateDateUuid(String createDateUuid) {
        this.createDateUuid = createDateUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public Integer getExternalFlg() {
        return externalFlg;
    }

    public void setExternalFlg(Integer externalFlg) {
        this.externalFlg = externalFlg;
    }

    @XmlTransient
    public Collection<TblMoldInspectionResult> getTblMoldInspectionResultCollection() {
        return tblMoldInspectionResultCollection;
    }

    public void setTblMoldInspectionResultCollection(Collection<TblMoldInspectionResult> tblMoldInspectionResultCollection) {
        this.tblMoldInspectionResultCollection = tblMoldInspectionResultCollection;
    }

    @XmlTransient
    public Collection<MstMoldInspectionChoice> getMstMoldInspectionChoiceCollection() {
        return mstMoldInspectionChoiceCollection;
    }

    public void setMstMoldInspectionChoiceCollection(Collection<MstMoldInspectionChoice> mstMoldInspectionChoiceCollection) {
        this.mstMoldInspectionChoiceCollection = mstMoldInspectionChoiceCollection;
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
        if (!(object instanceof MstMoldInspectionItem)) {
            return false;
        }
        MstMoldInspectionItem other = (MstMoldInspectionItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "test.MstMoldInspectionItem[ id=" + id + " ]";
    }
}
