/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspectorManhours;

import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.circuitboard.inspectorManhours.model.InspectorManhour;
import com.kmcj.karte.resources.circuitboard.inspectorManhours.model.InspectorManhourList;
import com.kmcj.karte.util.FileUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author h.ishihara
 */
@Dependent
public class TblInspectorManhoursService {
    @PersistenceContext(unitName = "pu_karte") 
    private EntityManager entityManager;
    
     public InspectorManhourList getInsInspectorManhourList(String inspectorUuid, Date inspectionDate){
       Query query =  entityManager.createNamedQuery("TblInspectorManhours.findByUser");
       query.setParameter("inspectorUuid", inspectorUuid);
       query.setParameter("inspectionDate", inspectionDate);
       InspectorManhourList list = new InspectorManhourList();
       list.setManhourList(query.getResultList());
       
       return list;
    }
        
    public InspectorManhourList getInsInspectorManhourListByPK(String inspectorUuid, String componentId, String procedureId, Date inspectionDate){
       Query query =  entityManager.createNamedQuery("TblInspectorManhours.deleteByID");
       query.setParameter("inspectorUuid", inspectorUuid);
       query.setParameter("componentId", componentId);
       query.setParameter("procedureId", procedureId);
       query.setParameter("inspectionDate", inspectionDate);
       InspectorManhourList list = new InspectorManhourList();
       list.setManhourList(query.getResultList());
       
       return list;
    }
    
    @Transactional
//    public void updateInspectorManhours(List<InspectorManhour> manhourList, LoginUser user){
//        this.insertManhour(manhourList, user);
//    }
    
    public void updateInspectorManhours(List<InspectorManhour> manhourList, LoginUser user){
        SimpleDateFormat sdf = new SimpleDateFormat(FileUtil.DATETIME_FORMAT);
        
        for(InspectorManhour item : manhourList){
            int i = 1;
            TblInspectorManhoursPK pk = new TblInspectorManhoursPK();
            TblInspectorManhours manhour = new TblInspectorManhours();
            pk.setInspectorUuid(item.getInspectorUuid());
            pk.setComponentId(item.getComponentId());
            pk.setProcedureId(item.getProcedureId());
            pk.setInspectionDate(item.getInspectionDate());
            //.setComponentId(item.getComponentId());
            
            manhour.setTblInspectorManhoursPK(pk);
            manhour.setDownTime(item.getDownTime());
            manhour.setManhoursForRepairing(item.getManhoursForRepairing());
            manhour.setResultManhours(item.getResultManhours());
            manhour.setOutputManhours(item.getOutputManhours());
            manhour.setEfficiency(item.getEfficiency());
            manhour.setCommentText(item.getCommentText());
            manhour.setCreateDate(item.getCreateDate());
            manhour.setUpdateDate(item.getUpdateDate());
            manhour.setCreateDateUuid(item.getCreateUserUuid());
            manhour.setUpdateUserUuid(item.getUpdateUserUuid());
            
            //manhour.setCreateDate(item.get);
            //entityManager.persist(manhour);
            entityManager.merge(manhour);
        }
    }
    
    @Transactional
    public void updateInspectorManhour(InspectorManhour item, LoginUser user){
        SimpleDateFormat sdf = new SimpleDateFormat(FileUtil.DATETIME_FORMAT);        
try{
            int i = 1;
            TblInspectorManhoursPK pk = new TblInspectorManhoursPK();
            TblInspectorManhours manhour = new TblInspectorManhours();
            pk.setInspectorUuid(item.getInspectorUuid());
            pk.setComponentId(item.getComponentId());
            pk.setProcedureId(item.getProcedureId());
            pk.setInspectionDate(item.getInspectionDate());
            
            manhour.setTblInspectorManhoursPK(pk);
            manhour.setDownTime(item.getDownTime());
            manhour.setManhoursForRepairing(item.getManhoursForRepairing());
            manhour.setResultManhours(item.getResultManhours());
            manhour.setOutputManhours(item.getOutputManhours());
            manhour.setEfficiency(item.getEfficiency());
            manhour.setCommentText(item.getCommentText());
            manhour.setCreateDate(item.getCreateDate());
            manhour.setUpdateDate(item.getUpdateDate());
            manhour.setCreateDateUuid(item.getCreateUserUuid());
            manhour.setUpdateUserUuid(item.getUpdateUserUuid());
            

            entityManager.merge(manhour);
}catch(Exception ex){
         String msg = ex.getMessage();
    }
    }
    private void deleteFiles(List<InspectorManhour> items) {
        // keys
        //List<String> detailIds = new ArrayList<>();
        // make detail id list 
        //items.forEach(item -> detailIds.add(item.getInsepectorManhourId()));
        
        items.forEach(item -> {
             StringBuilder deleteSql = new StringBuilder("DELETE FROM TblInspectorManhours t WHERE t.inspectorUuid =:inspectorUuid AND m.inspectionDate =:inspectionDate");
                Query query = entityManager.createQuery(deleteSql.toString());
                query.setParameter("componentId", item.getInspectorUuid());
                query.setParameter("inspectionDate", item.getInspectionDate());
                query.executeUpdate();
            //detailIds.add(item.getInspectorUuid());
                    }
        );
        // delete visual inspection files with item.
        //entityManager.createNamedQuery("TblInspectorManhours.deleteByItems")
        //        .setParameter("inspectorManhourId", detailIds).executeUpdate();
    }
    
    @Transactional
    public void deleteInspectorManhour(String inspectorUuid, String componentId, String procedureId, Date inspectionDate) {
       Query query = entityManager.createNamedQuery("TblInspectorManhours.deleteByID");
       query.setParameter("inspectorUuid", inspectorUuid);
       query.setParameter("componentId", componentId);
       query.setParameter("procedureId", procedureId);
       query.setParameter("inspectionDate", inspectionDate);
       query.executeUpdate();
    }
}
