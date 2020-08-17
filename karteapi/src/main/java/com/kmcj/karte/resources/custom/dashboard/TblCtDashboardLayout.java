/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.dashboard;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author t.takasaki
 */
@Entity
@Table(name = "tbl_ct_dashboard_layout")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblCtDashboardLayout.findByDashboardId", query = "SELECT m FROM TblCtDashboardLayout m WHERE m.dashboardId = :dashboardId"),
})
public class TblCtDashboardLayout implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DASHBOARD_ID")
    private String dashboardId;
    
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "WIDGET_ID")
    private String widgetId;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "X")
    private int x;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "Y")
    private int y;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "W")
    private int w;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "H")
    private int h;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(String dashboardId) {
        this.dashboardId = dashboardId;
    }

    public String getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final TblCtDashboardLayout other = (TblCtDashboardLayout) obj;
        return Objects.equals(this.id, other.id);
    }
}
