/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.dashboard;

import com.kmcj.karte.resources.custom.report.TblCustomReportQuery;
import com.kmcj.karte.resources.user.MstUserMin;
import com.kmcj.karte.util.XmlDateAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * カスタムダッシュボード用ウィジェット
 * @author t.takasaki
 */
@Entity
@Table(name = "tbl_ct_widget")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblCtWidget.findAll", query = "SELECT m FROM TblCtWidget m LEFT OUTER JOIN FETCH m.createUser LEFT OUTER JOIN FETCH m.updateUser order by m.createDate"),
    @NamedQuery(
        name = "TblCtWidget.findById", 
        query = "SELECT m FROM TblCtWidget m "
                + "LEFT OUTER JOIN FETCH m.dataset1Query "
                + "LEFT OUTER JOIN FETCH m.dataset2Query "
                + "LEFT OUTER JOIN FETCH m.label1Query "
                + "LEFT OUTER JOIN FETCH m.label2Query "
                + "LEFT OUTER JOIN FETCH m.label3Query "
            + "WHERE m.id = :id order by m.createDate"
    ),
    @NamedQuery(
        name = "TblCtWidget.findByIds",
        query = "SELECT m FROM TblCtWidget m "
                + "LEFT OUTER JOIN FETCH m.dataset1Query "
                + "LEFT OUTER JOIN FETCH m.dataset2Query "
                + "LEFT OUTER JOIN FETCH m.label1Query "
                + "LEFT OUTER JOIN FETCH m.label2Query "
                + "LEFT OUTER JOIN FETCH m.label3Query "
            + "WHERE m.id in :ids order by m.createDate"
    )
})
public class TblCtWidget implements Serializable {

    /**ウィジェットタイプ */
    public enum Type {
        /** テーブル*/
        TABLE,
        /** マトリクス*/
        MATRIX,
        /** 棒グラフ*/
        BAR,
        /** 積み上げグラフ*/
        STACK,
        /** 折れ線グラフ*/
        LINE,
        /** 円グラフ*/
        PIE,
        /** 棒/折れ線複合グラフ*/
        BAR_COMB,
        /** 積み上げ/折れ線複合グラフ*/
        STACK_COMB
    }
    
    /** グラフの向き*/
    public enum Orientation {
        /** Vertical*/
        V,
        /** Horizontal*/
        H,
        /** No answer*/
        NA
    }
    
    private static final long serialVersionUID = 1L;
    
    public TblCtWidget() {}
    
    public TblCtWidget(String id) {
        this.id = id;
    }
    
    @Id
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    
    @NotNull
    @Size(min = 0, max = 100)
    @Column(name = "WIDGET_NAME")
    private String name = "";
    
    @NotNull
    @Column(name = "WIDGET_TYPE")
    @Enumerated(EnumType.STRING)
    private Type widgetType;
    
    @NotNull
    @Size(min = 0, max = 100)
    @Column(name = "GRAPH_TITLE")
    private String title = "";
    
    @Column(name = "DATASET_1_QUERY_ID")
    private Long dataset1QueryId;
    
    @NotNull
    @Size(min = 0, max = 45)
    @Column(name = "DATASET_1_ITEM_1")
    private String dataset1Item1 = "";
    
    @NotNull
    @Size(min = 0, max = 45)
    @Column(name = "DATASET_1_ITEM_2")
    private String dataset1Item2 = "";
    
    @NotNull
    @Size(min = 0, max = 45)
    @Column(name = "DATASET_1_ITEM")
    private String dataset1Item = "";
    
    @NotNull
    @Size(min = 0, max = 100)
    @Column(name = "DATASET_1_AXIS_TITLE")
    private String dataset1AxisTitle = "";
    
    @Column(name = "DATASET_2_QUERY_ID")
    private Long dataset2QueryId;
    
    @NotNull
    @Size(min = 0, max = 45)
    @Column(name = "DATASET_2_ITEM_1")
    private String dataset2Item1 = "";
    
    @NotNull
    @Size(min = 0, max = 45)
    @Column(name = "DATASET_2_ITEM_2")
    private String dataset2Item2 = "";
    
    @NotNull
    @Size(min = 0, max = 45)
    @Column(name = "DATASET_2_ITEM")
    private String dataset2Item = "";
    
    @NotNull
    @Size(min = 0, max = 100)
    @Column(name = "DATASET_2_AXIS_TITLE")
    private String dataset2AxisTitle = "";
    
    @Column(name = "LABEL_1_QUERY_ID")
    private Long label1QueryId;
    
    @NotNull
    @Size(min = 0, max = 45)
    @Column(name = "LABEL_1_ITEM")
    private String label1Item = "";
    
    @NotNull
    @Size(min = 0, max = 100)
    @Column(name = "LABEL_1_AXIS_TITLE")
    private String label1AxisTitle = "";
    
    @Column(name = "LABEL_2_QUERY_ID")
    private Long label2QueryId;
    
    @NotNull
    @Size(min = 0, max = 45)
    @Column(name = "LABEL_2_ITEM")
    private String label2Item = "";
    
    @Column(name = "LABEL_3_QUERY_ID")
    private Long label3QueryId;
    
    @NotNull
    @Size(min = 0, max = 45)
    @Column(name = "LABEL_3_ITEM")
    private String label3Item = "";
    
    @NotNull
    @Column(name = "SHOW_LEGEND")
    private boolean showLegend;
    
    @NotNull
    @Column(name = "ORIENTATION")
    @Enumerated(EnumType.STRING)
    private Orientation orientation = Orientation.NA;
    
    @NotNull
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date createDate;
    
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUUID;
    
    @NotNull
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date updateDate;
    
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUUID;
    
    @PrimaryKeyJoinColumn(name = "DATASET_1_QUERY_ID", referencedColumnName = "REPORT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblCustomReportQuery dataset1Query;
    
    @PrimaryKeyJoinColumn(name = "DATASET_2_QUERY_ID", referencedColumnName = "REPORT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblCustomReportQuery dataset2Query;
    
    @PrimaryKeyJoinColumn(name = "LABEL_1_QUERY_ID", referencedColumnName = "REPORT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblCustomReportQuery label1Query;
    
    @PrimaryKeyJoinColumn(name = "LABEL_2_QUERY_ID", referencedColumnName = "REPORT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblCustomReportQuery label2Query;
    
    @PrimaryKeyJoinColumn(name = "LABEL_3_QUERY_ID", referencedColumnName = "REPORT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblCustomReportQuery label3Query;
    
    @PrimaryKeyJoinColumn(name = "CREATE_USER_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUserMin createUser;

    @PrimaryKeyJoinColumn(name = "UPDATE_USER_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUserMin updateUser;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "widget")
    private Collection<TblCtWidgetColor> colors = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(Type widgetType) {
        this.widgetType = widgetType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getDataset1QueryId() {
        return dataset1QueryId;
    }

    public void setDataset1QueryId(Long dataset1QueryId) {
        this.dataset1QueryId = dataset1QueryId;
    }

    public String getDataset1Item1() {
        return dataset1Item1;
    }

    public void setDataset1Item1(String dataset1Item1) {
        this.dataset1Item1 = dataset1Item1;
    }

    public String getDataset1Item2() {
        return dataset1Item2;
    }

    public void setDataset1Item2(String dataset1Item2) {
        this.dataset1Item2 = dataset1Item2;
    }

    public String getDataset1Item() {
        return dataset1Item;
    }

    public void setDataset1Item(String dataset1Item) {
        this.dataset1Item = dataset1Item;
    }

    public String getDataset1AxisTitle() {
        return dataset1AxisTitle;
    }

    public void setDataset1AxisTitle(String dataset1AxisTitle) {
        this.dataset1AxisTitle = dataset1AxisTitle;
    }

    public Long getDataset2QueryId() {
        return dataset2QueryId;
    }

    public void setDataset2QueryId(Long dataset2QueryId) {
        this.dataset2QueryId = dataset2QueryId;
    }

    public String getDataset2Item1() {
        return dataset2Item1;
    }

    public void setDataset2Item1(String dataset2Item1) {
        this.dataset2Item1 = dataset2Item1;
    }

    public String getDataset2Item2() {
        return dataset2Item2;
    }

    public void setDataset2Item2(String dataset2Item2) {
        this.dataset2Item2 = dataset2Item2;
    }

    public String getDataset2Item() {
        return dataset2Item;
    }

    public void setDataset2Item(String dataset2Item) {
        this.dataset2Item = dataset2Item;
    }

    public String getDataset2AxisTitle() {
        return dataset2AxisTitle;
    }

    public void setDataset2AxisTitle(String dataset2AxisTitle) {
        this.dataset2AxisTitle = dataset2AxisTitle;
    }

    public Long getLabel1QueryId() {
        return label1QueryId;
    }

    public void setLabel1QueryId(Long label1QueryId) {
        this.label1QueryId = label1QueryId;
    }

    public String getLabel1Item() {
        return label1Item;
    }

    public void setLabel1Item(String label1Item) {
        this.label1Item = label1Item;
    }

    public String getLabel1AxisTitle() {
        return label1AxisTitle;
    }

    public void setLabel1AxisTitle(String label1AxisTitle) {
        this.label1AxisTitle = label1AxisTitle;
    }

    public Long getLabel2QueryId() {
        return label2QueryId;
    }

    public void setLabel2QueryId(Long label2QueryId) {
        this.label2QueryId = label2QueryId;
    }

    public String getLabel2Item() {
        return label2Item;
    }

    public void setLabel2Item(String label2Item) {
        this.label2Item = label2Item;
    }

    public Long getLabel3QueryId() {
        return label3QueryId;
    }

    public void setLabel3QueryId(Long label3QueryId) {
        this.label3QueryId = label3QueryId;
    }

    public String getLabel3Item() {
        return label3Item;
    }

    public void setLabel3Item(String label3Item) {
        this.label3Item = label3Item;
    }

    public boolean isShowLegend() {
        return showLegend;
    }

    public void setShowLegend(boolean showLegend) {
        this.showLegend = showLegend;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserUUID() {
        return createUserUUID;
    }

    public void setCreateUserUUID(String createUserUUID) {
        this.createUserUUID = createUserUUID;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUserUUID() {
        return updateUserUUID;
    }

    public void setUpdateUserUUID(String updateUserUUID) {
        this.updateUserUUID = updateUserUUID;
    }

    public TblCustomReportQuery getDataset1Query() {
        return dataset1Query;
    }

    public void setDataset1Query(TblCustomReportQuery dataset1Query) {
        this.dataset1Query = dataset1Query;
    }

    public TblCustomReportQuery getDataset2Query() {
        return dataset2Query;
    }

    public void setDataset2Query(TblCustomReportQuery dataset2Query) {
        this.dataset2Query = dataset2Query;
    }

    public TblCustomReportQuery getLabel1Query() {
        return label1Query;
    }

    public void setLabel1Query(TblCustomReportQuery label1Query) {
        this.label1Query = label1Query;
    }

    public TblCustomReportQuery getLabel2Query() {
        return label2Query;
    }

    public void setLabel2Query(TblCustomReportQuery label2Query) {
        this.label2Query = label2Query;
    }

    public TblCustomReportQuery getLabel3Query() {
        return label3Query;
    }

    public void setLabel3Query(TblCustomReportQuery label3Query) {
        this.label3Query = label3Query;
    }

    public MstUserMin getCreateUser() {
        return createUser;
    }

    public void setCreateUser(MstUserMin createUser) {
        this.createUser = createUser;
    }

    public MstUserMin getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(MstUserMin updateUser) {
        this.updateUser = updateUser;
    }

    public Collection<TblCtWidgetColor> getColors() {
        return colors;
    }

    public void setColors(Collection<TblCtWidgetColor> colors) {
        this.colors = colors;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TblCtWidget other = (TblCtWidget) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.custom.dashboard.TblCtWidget[ id=" + id + " ]";
    }
    
}
