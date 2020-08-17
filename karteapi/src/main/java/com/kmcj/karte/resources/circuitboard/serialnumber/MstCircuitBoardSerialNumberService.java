/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.serialnumber;

import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.util.IDGenerator;
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
public class MstCircuitBoardSerialNumberService {
    @PersistenceContext(unitName = "pu_karte") 
    private EntityManager entityManager;
    
    public MstCircuitBoardSerialNumber getCircuitBoardBySerialNumber(String serialNumber){
//        MstCircuitBoardSerialNumber data = new MstCircuitBoardSerialNumber();
        Query query = entityManager.createNamedQuery("MstCircuitBoardSerialNumber.findBySerialNumber");
        query.setParameter("serialNumber", serialNumber);
        List<MstCircuitBoardSerialNumber> list = query.getResultList();
        if(list != null && list.size() >0){
            return list.get(0);
        }
        
        return null;
    }

    @Transactional
    public ImportResultResponse insertCircuitBoardSerialNumbers(List<CircuitBoardSerialNumber> circuitBoardSerialNumberList, LoginUser user) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ImportResultResponse response = new ImportResultResponse();
        int addedCount = 0;
        int failedCount = 0;
        for (CircuitBoardSerialNumber item : circuitBoardSerialNumberList) {
            try {
                MstCircuitBoardSerialNumber mstCircuitBoardSerialNumber = new MstCircuitBoardSerialNumber();

                mstCircuitBoardSerialNumber.setCircuitBoardSnId(item.getCircuitBoardSnId());
                mstCircuitBoardSerialNumber.setComponentId(item.getComponentId());
                mstCircuitBoardSerialNumber.setSerialNumber(item.getSerialNumber());
                entityManager.merge(mstCircuitBoardSerialNumber);
                addedCount++;
            }catch(Exception ex){
                            failedCount++;
                StringBuilder sb = new StringBuilder();
                sb.append(item.getComponentId());
                sb.append(",");
                sb.append(item.getSerialNumber());
                sb.append(System.getProperty("line.separator"));
                response.setLog(response.getLog() + sb.toString());
                response.setErrorMessage(ex.getMessage());
            }
        }
        response.setSucceededCount(addedCount);
        response.setFailedCount(failedCount);
        return response;
    }    
    
    public CircuitBoardSerialNumber getIdBySerialNumber(String serialNumber){
        CircuitBoardSerialNumber data = new CircuitBoardSerialNumber();
        Query query = entityManager.createNamedQuery("MstCircuitBoardSerialNumber.findBySerialNumber");
        query.setParameter("serialNumber", serialNumber);
        List<MstCircuitBoardSerialNumber> list = query.getResultList();
        if(list != null && list.size() >0){
            MstCircuitBoardSerialNumber m = list.get(0);
            data.setCircuitBoardSnId(m.getCircuitBoardSnId());
            data.setComponentId(m.getComponentId());
            data.setSerialNumber(m.getSerialNumber());
            data.setCreateDate(m.getCreateDate());
            data.setUpdateDate(m.getUpdateDate());
            data.setCreateUserUuid(m.getCreateUserUuid());
            data.setUpdateUserUuid(m.getUpdateUserUuid());
        }
        
        return data;
    }
    
    @Transactional
    public void updateSerialNumber(CircuitBoardSerialNumber serialNumber, LoginUser loginUser){
       MstCircuitBoardSerialNumber updateData = new MstCircuitBoardSerialNumber();
       Date today =new Date();
       String snid = IDGenerator.generate();
       serialNumber.setCircuitBoardSnId(snid);
       serialNumber.setCreateDate(today);
       serialNumber.setUpdateDate(today);
       
       updateData.setCircuitBoardSnId(serialNumber.getCircuitBoardSnId());
       updateData.setComponentId(serialNumber.getComponentId());
       updateData.setSerialNumber(serialNumber.getSerialNumber());
       updateData.setCreateDate(serialNumber.getCreateDate());
       updateData.setCreateUserUuid(serialNumber.getCreateUserUuid());
       updateData.setUpdateDate(serialNumber.getUpdateDate());
       updateData.setUpdateUserUuid(serialNumber.getUpdateUserUuid());
       
       entityManager.merge(updateData);
    }
}
