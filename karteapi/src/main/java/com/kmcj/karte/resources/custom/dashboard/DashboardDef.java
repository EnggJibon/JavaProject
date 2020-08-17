/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.dashboard;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * ダッシュボード編集画面で扱うデータ群をまとめるためのクラス
 * @author t.takasaki
 */
@XmlRootElement
public class DashboardDef implements Serializable {
    private static final long serialVersionUID = 1L;

    private TblCtDashboard dashboard;
    private List<TblCtDashboardAuth> auths;
    private List<TblCtDashboardFilter> filters;
    private List<TblCtDashboardLayout> layouts;

    public TblCtDashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(TblCtDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public List<TblCtDashboardAuth> getAuths() {
        return auths;
    }

    public void setAuths(List<TblCtDashboardAuth> auths) {
        this.auths = auths;
    }
    
    public List<TblCtDashboardFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<TblCtDashboardFilter> filters) {
        this.filters = filters;
    }

    public List<TblCtDashboardLayout> getLayouts() {
        return layouts;
    }

    public void setLayouts(List<TblCtDashboardLayout> layouts) {
        this.layouts = layouts;
    }
}
