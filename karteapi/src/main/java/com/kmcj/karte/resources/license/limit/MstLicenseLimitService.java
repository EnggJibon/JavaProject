/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.license.limit;

import com.kmcj.karte.constants.CommonConstants;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.kmcj.karte.util.FileUtil;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import com.kmcj.karte.resources.authorization.MstAuthService;
import com.kmcj.karte.resources.language.MstLanguageService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;


@Dependent
public class MstLicenseLimitService {
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    @Inject
    private MstLanguageService mstLanguageService;
    @Inject
    private MstAuthService mstAuthService;

//    private MstLicenseLimit cacheLicenseLimit = null;
    
    private long cacheLicenseUserCount = -1;
    private long cacheLicenseMoldCount = -1;
    private long cacheLicenseMachineCount = -1;

    public boolean CheckUserLimit(String langId, BasicResponse response){
        Query query;
        String decryptUserCount= "";
        long licensedUserCount= 0;
        long userCount = 0;
        MstLicenseLimit mstLicenseLimit= null;

        query = entityManager.createNamedQuery("MstLicenseLimit.findById");
        query.setParameter("id","1");
        try {
            if (cacheLicenseUserCount == -1) {
                mstLicenseLimit = (MstLicenseLimit)query.getSingleResult();
                decryptUserCount = FileUtil.decrypt(mstLicenseLimit.getUserCount());
                licensedUserCount = Long.parseLong(decryptUserCount);  
                cacheLicenseUserCount = licensedUserCount;
            }
            else {
                licensedUserCount = cacheLicenseUserCount;
            }
            
        } 
        catch(NumberFormatException nfe){
           response.setError(true);
           response.setErrorCode(ErrorMessages.E201_APPLICATION);
           response.setErrorMessage("Irregular value is set in License Limit.");
           return false;
        }
        catch (NoResultException  nre) {
           response.setError(true);
           response.setErrorCode(ErrorMessages.E201_APPLICATION);
           response.setErrorMessage("License limit is not found.");
           return false;
        } 
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(m) FROM MstUser m WHERE  m.validFlg = '1' ");
            query = entityManager.createQuery(sql.toString());
            userCount = (Long)query.getSingleResult();    
       } 
        catch (NoResultException  nre) {
           response.setError(true);
           response.setErrorCode(ErrorMessages.E201_APPLICATION);
           response.setErrorMessage("Unexpected error happens while counting user master."); 
           return false;
       } 
        if (licensedUserCount <= userCount) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String errorMessage= mstDictionaryService.getDictionaryValue(langId, "msg_error_max_users");
            errorMessage = errorMessage.replace("%s", decryptUserCount);
            response.setErrorMessage(errorMessage);            
            return false;
        }
        else
        {
            return true;            
        }
    }  
    
    public  boolean CheckMoldLimit(String langId, BasicResponse response)
    {
        Query query;
        String decryptMoldCount = "";
        long licensedMoldCount = 0;
        long moldCount = 0;
        MstLicenseLimit mstLicenseLimit= null;
         
        query = entityManager.createNamedQuery("MstLicenseLimit.findById");
        query.setParameter("id","1");
        try {     
            if(cacheLicenseMoldCount == -1)
            {
            mstLicenseLimit = (MstLicenseLimit)query.getSingleResult();
            decryptMoldCount = FileUtil.decrypt(mstLicenseLimit.getMoldCount());
            licensedMoldCount = Long.parseLong(decryptMoldCount);  
            cacheLicenseMoldCount = licensedMoldCount;
            }
            else{
            licensedMoldCount = cacheLicenseMoldCount;
            }
        } catch (NumberFormatException nfe) {
            response.setError(true); 
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage("Irregular value is set in License Limit.");
            return false;
        }
        catch(NoResultException nre){
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage("License Limit is not found."); 
            return false;
        }
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(m) FROM MstMold m");
            query = entityManager.createQuery(sql.toString());
            moldCount = (Long)query.getSingleResult();
            
        } catch (NoResultException  nre) {
            response.setError(true); 
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage("Unexpected errorr happens while counting master mold."); 
            return false;
        }
        if(licensedMoldCount <= moldCount){
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String errorMessage = mstDictionaryService.getDictionaryValue(langId, "msg_error_max_molds");
            errorMessage = errorMessage.replace("%s", decryptMoldCount);
            response.setErrorMessage(errorMessage); 
            return false;
        }
        else{    
            return true;
        }
    }
    
    public boolean CheckMachineLimit(String langId, BasicResponse response){
        Query query;
        String decryptedMachineCount = "";
        long licensedMachineCount = 0;
        long machineCount = 0;
        MstLicenseLimit mstLicenseLimit= null;
        
        query = entityManager.createNamedQuery("MstLicenseLimit.findById");
        query.setParameter("id","1");
       
        try {
            
            if(cacheLicenseMachineCount == -1)
            {
            mstLicenseLimit = (MstLicenseLimit)query.getSingleResult();
            decryptedMachineCount = FileUtil.decrypt(mstLicenseLimit.getMachineCount());
            licensedMachineCount = Long.parseLong(decryptedMachineCount); 
            cacheLicenseMachineCount = licensedMachineCount;
            }
            else{
            licensedMachineCount = cacheLicenseMachineCount;
            }
        } catch (NumberFormatException nfe) {
            response.setError(true); 
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage("Irregular value is set in License Limit.");
            return false;
        }
        catch(NoResultException nre){
            response.setError(true); 
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage("License Limit is not found"); 
            return false;
        }

        StringBuilder sql = new StringBuilder("SELECT COUNT(m) FROM MstMachine m");
        query = entityManager.createQuery(sql.toString());
        machineCount = (long)query.getSingleResult();
        try {
            
        } catch (NoResultException nre) {
            response.setError(true); 
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage("Unexpected errorr happens while counting master machine."); 
            return false;
        }
        
        if(licensedMachineCount <= machineCount){
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION); 
            String errorMessage = mstDictionaryService.getDictionaryValue(langId, "msg_error_max_machines");
            errorMessage = errorMessage.replace("%s", decryptedMachineCount);
            response.setErrorMessage(errorMessage);
            return false;
        }
        else{    
            return true;
        }
    }
}
