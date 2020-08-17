/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.dashboard;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authorization.MstAuthService;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author t.takasaki
 */
@Dependent
public class CustomDashboardService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private MstAuthService authService;
    
    public TblCtDashboard getDashboard(String id) {
        return entityManager.createNamedQuery("TblCtDashboard.findById", TblCtDashboard.class)
            .setParameter("id", id).getResultList().stream().findFirst().orElse(new TblCtDashboard(IDGenerator.generate()));
    }
    
    public List<TblCtDashboard> getAuthedDashboards(String authid) {
        return entityManager.createNamedQuery("TblCtDashboard.findAuthedList", TblCtDashboard.class)
            .setParameter("authId", authid).getResultList();
    }
    
    public  List<TblCtDashboard> getAllDashboards() {
        return entityManager.createNamedQuery("TblCtDashboard.findAll", TblCtDashboard.class).getResultList();
    }
    
    @Transactional
    public void putDashboard(DashboardDef dashboardDef) {
        getDashboardLayout(dashboardDef.getDashboard().getId())
            .forEach(old->{
                if(!dashboardDef.getLayouts().contains(old)) {
                    entityManager.remove(old);
                }
            });
        getDashboardFilterParam(dashboardDef.getDashboard().getId())
            .forEach(old->{
                if(!dashboardDef.getFilters().contains(old)) {
                    entityManager.remove(old);
                }
            });
        entityManager.merge(dashboardDef.getDashboard());
        dashboardDef.getAuths().forEach(auth->entityManager.merge(auth));
        dashboardDef.getFilters().forEach(filter->entityManager.merge(filter));
        dashboardDef.getLayouts().forEach(layout->entityManager.merge(layout));
    }
    
    @Transactional
    public void delDashboard(String dashboardId) {
        entityManager.createNamedQuery("TblCtDashboard.findById", TblCtDashboard.class)
            .setParameter("id", dashboardId).getResultList().stream().findFirst()
            .ifPresent(dashboard -> entityManager.remove(dashboard));
    }
    
    public List<TblCtWidget> getAllWidgets() {
        return entityManager.createNamedQuery("TblCtWidget.findAll", TblCtWidget.class).getResultList();
    }
    
    public List<TblCtWidget> getWidgets(List<String> ids) {
        if(ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return entityManager.createNamedQuery("TblCtWidget.findByIds", TblCtWidget.class)
            .setParameter("ids", ids).getResultList();
    }
    
    public TblCtWidget getOrNewWidget(String id) {
        return entityManager.createNamedQuery("TblCtWidget.findById", TblCtWidget.class)
            .setParameter("id", id).getResultList().stream().findFirst().orElse(new TblCtWidget(IDGenerator.generate()));
    }
    
    @Transactional
    public void putWidget(TblCtWidget widget) {
        entityManager.createNamedQuery("TblCtWidget.findById", TblCtWidget.class)
            .setParameter("id", widget.getId()).getResultList().stream().findFirst()
            .ifPresent(old -> {
                old.getColors().stream().filter(c->!widget.getColors().contains(c))
                .forEach(deleted -> entityManager.remove(deleted));
            });
        entityManager.merge(widget);
    }
    
    @Transactional
    public void delWidget(String widgetid) {
        entityManager.createNamedQuery("TblCtWidget.findById", TblCtWidget.class)
            .setParameter("id", widgetid).getResultList().stream().findFirst()
            .ifPresent(widget -> entityManager.remove(widget));
    }
    
    public List<TblCtDashboardLayout> getDashboardLayout(String dashboardId) {
        return entityManager.createNamedQuery("TblCtDashboardLayout.findByDashboardId", TblCtDashboardLayout.class)
            .setParameter("dashboardId", dashboardId).getResultList();
    }
    
    public List<TblCtDashboardFilter> getDashboardFilterParam(String dashboardId) {
        return entityManager.createNamedQuery("TblCtDashboardFilter.findByDashboardId", TblCtDashboardFilter.class)
            .setParameter("dashboardId", dashboardId).getResultList();
    }
    
    public List<TblCtDashboardAuth> getDashboardAuth(String dashboardId) {
        Set<TblCtDashboardAuth.PK> authed = entityManager.createNamedQuery("TblCtDashboardAuth.findByDashboardId", TblCtDashboardAuth.class)
            .setParameter("dashboardId", dashboardId).getResultList().stream()
            .filter(auth->auth.isAvailable()).map(auth->auth.getPk()).collect(Collectors.toSet());
        
        List<TblCtDashboardAuth> allAuth = authService.getMstAuths().getMstAuths().stream()
            .map(auth->new TblCtDashboardAuth(dashboardId, auth)).collect(Collectors.toList());
        
        allAuth.forEach(auth->auth.setAvailable(authed.contains(auth.getPk())));
        return allAuth;
    }
}
