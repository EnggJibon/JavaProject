/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.dashboard;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author t.takasaki
 */
@Entity
@Table(name = "tbl_ct_widget_color_settings")
@XmlRootElement
public class TblCtWidgetColor implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static enum Dataset {
        Dataset1, Dataset2
    }
    
    @EmbeddedId
    private PK pk;
    
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COLOR")
    private String color;
    
    @JoinColumn(name = "WIDGET_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @Expose(serialize = false, deserialize = true)
    private TblCtWidget widget;

    public PK getPk() {
        return pk;
    }

    public void setPk(PK pk) {
        this.pk = pk;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public TblCtWidget getWidget() {
        return widget;
    }

    public void setWidget(TblCtWidget widget) {
        this.widget = widget;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.pk);
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
        final TblCtWidgetColor other = (TblCtWidgetColor) obj;
        return Objects.equals(this.pk, other.pk);
    }
    
    @Embeddable
    public static class PK implements Serializable {
        @NotNull
        @Size(min = 1, max = 45)
        @Column(name = "WIDGET_ID")
        private String widgetId;
        
        @NotNull
        @Size(min = 1, max = 45)
        @Column(name = "TGT_DATASET")
        @Enumerated(EnumType.STRING)
        private Dataset tgtDataset;
        
        @NotNull
        @Column(name = "SEQ")
        private Integer seq;

        public String getWidgetId() {
            return widgetId;
        }

        public void setWidgetId(String widgetId) {
            this.widgetId = widgetId;
        }

        public Dataset getTgtDataset() {
            return tgtDataset;
        }

        public void setTgtDataset(Dataset tgtDataset) {
            this.tgtDataset = tgtDataset;
        }

        public Integer getSeq() {
            return seq;
        }

        public void setSeq(Integer seq) {
            this.seq = seq;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 41 * hash + Objects.hashCode(this.widgetId);
            hash = 41 * hash + Objects.hashCode(this.tgtDataset);
            hash = 41 * hash + Objects.hashCode(this.seq);
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
            final PK other = (PK) obj;
            if (!Objects.equals(this.widgetId, other.widgetId)) {
                return false;
            }
            if (this.tgtDataset != other.tgtDataset) {
                return false;
            }
            return Objects.equals(this.seq, other.seq);
        }
    }
}
