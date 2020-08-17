/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.imp;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.MstMachineService;
import com.kmcj.karte.resources.sigma.MstSigma;
import com.kmcj.karte.resources.sigma.MstSigmaService;
import com.kmcj.karte.properties.KartePropertyService;
import java.util.List;
import java.util.Date;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Calendar;
import com.kmcj.karte.util.DateFormat;
import java.text.SimpleDateFormat;
import javax.inject.Inject;
import com.kmcj.karte.MailSender;
import com.kmcj.karte.resources.files.TblUploadFileService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.resources.user.mail.reception.MstUserMailReceptionService;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
/**
 *
 * @author f.kitaoji
 */

@Dependent
public class SigmaImportService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private MailSender mailSender;
    @Inject
    private MstMachineService mstMachineService;
    @Inject
    private MstSigmaService mstSigmaService;
    @Inject
    private MstUserMailReceptionService mstUserMailReceptionService;
    @Inject
    private KartePropertyService kartePropertyService;
    @Inject
    private TblUploadFileService tblUploadFileService;
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    private Map<String, String> dictKeyMap;
    
    public boolean shouldReportError(String macKey) {
        int errCount = 0;
        int errInterval;
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        MstSigma errorCountInterval;
        List<TblSigmaImportError> list;
        errorCountInterval = getErrorCountInterval(macKey);
        if(errorCountInterval == null){
            return false;
        }else{
        
            if (macKey.equals("") || macKey.equals(null)){
                return false;
            }
            else{         
                errCount = errorCountInterval.getCountErrorNotice();
                errInterval = errorCountInterval.getErrorNoticeInterval();
                c.add(Calendar.HOUR, -errInterval);
            }

            Date dateMinusHours = c.getTime();
            list = getImportErrorList(macKey, dateMinusHours);

            return list.size() < errCount;
        }
    }
    
    public MstSigma getErrorCountInterval(String macKey){
        MstSigma countInterval = null;
        String sql = ("SELECT ms FROM MstSigma ms INNER JOIN MstMachine mm ON ms.id = mm.sigmaId WHERE mm.macKey = :macKey ");
        Query queryCountInterval = entityManager.createQuery(sql);
        queryCountInterval.setParameter("macKey", macKey);
        try {
            countInterval = (MstSigma)queryCountInterval.getSingleResult();
        }
            catch (NoResultException e) {
                return null;
        }
        return countInterval;
    }
    public List getImportErrorList(String macKey, Date dateMinusHours){
        String jpql = "SELECT t FROM TblSigmaImportError t WHERE t.errorDatetime > :dateMinusHours AND t.solvedFlg = 0 AND t.macKey = :macKey ORDER BY t.errorDatetime DESC";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("dateMinusHours", dateMinusHours);
        query.setParameter("macKey", macKey);
        List<TblSigmaImportError> list = query.getResultList();
        
        return list;
    }
    public List getMailSentList(String macKey, Date dateMinusHours){
        String jpql = "SELECT t FROM TblSigmaImportError t WHERE t.errorDatetime > :dateMinusHours AND t.solvedFlg = 0 AND t.macKey = :macKey AND t.mailFlg > 0 "
                + "ORDER BY t.errorDatetime DESC";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("dateMinusHours", dateMinusHours);
        query.setParameter("macKey", macKey);
        List<TblSigmaImportError> list = query.getResultList();
        
        return list;
    }
    
    /**
     *@param loginUser
     */
    @Transactional
    public BasicResponse sigmaError(String macKey, String logFileUuid, String gunshiCsvFileUuid, LoginUser loginUser){
        String userUuid = loginUser.getUserUuid();
        BasicResponse response = new BasicResponse();
        int errCount;
        int errInterval;
        int mailFlg = 0;
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        MstSigma errorCountInterval;
        List<TblSigmaImportError> list;
        List<TblSigmaImportError> mailSentList;

        errorCountInterval = getErrorCountInterval(macKey);
        
        if (macKey.equals("") || macKey.equals("")){
            return response;
        }
        else{         
            errCount = errorCountInterval.getCountErrorNotice();
            errInterval = errorCountInterval.getErrorNoticeInterval();
            c.add(Calendar.HOUR, -errInterval);
        }
   
        Date dateMinusHours = c.getTime();
        list = getImportErrorList(macKey, dateMinusHours);
        
        //Get Machine master
        MstMachine machine = mstMachineService.getMstMachineFromMacKey(macKey);
        if (machine == null) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage("Machine master is not found by macKey = " + macKey);
            return response;
//            throw new Exception("Machine master is not found by macKey = " + macKey);
        }
        
        //Get Sigma Gunshi Master
        MstSigma sigma = mstSigmaService.getMstSigmaFromSigmaId(machine.getSigmaId());
        if (sigma == null){
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage("Sigma Master is not found by id = " + machine.getSigmaId());
            return response;
//            throw new Exception("Sigma Master is not found by id = " + machine.getSigmaId());
        }
        
        //Send error email
        if (list.size() == errCount-1){
            mailSentList = getMailSentList(macKey, dateMinusHours);
            if(mailSentList.size() < 1){
                sendErrorMail(machine, sigma, response, logFileUuid, gunshiCsvFileUuid, loginUser);
                //mailFlg flag for createSigmaImportError function
                mailFlg = 1;
            }
        }
        
        if (response.isError()) return response;
        
        //Add record to tbl_sigma_import_error
        try {
            createSigmaImportError(machine, sigma, response, logFileUuid, gunshiCsvFileUuid, userUuid, mailFlg);
        }
        catch (Exception e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }
    
    private void sendErrorMail(MstMachine machine, MstSigma sigma, BasicResponse response, String logFileUuid, String gunshiCsvFileUuid, LoginUser loginUser) {
        //Get URL Karte Property Service
        String currentTime = DateFormat.getCurrentDateTime();
        
        List<String> attachmentList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        
        //Attach Log file to e-mail (if null skip process)
        if (logFileUuid != null){
            TblUploadFile uploadFile = tblUploadFileService.getTblUploadFile(logFileUuid);
            fileNameList.add(uploadFile.getUploadFileName());
            
            StringBuffer filePath = new StringBuffer();
            filePath.append(kartePropertyService.getDocumentDirectory());
            filePath.append(FileUtil.SEPARATOR);
            filePath.append(CommonConstants.WORK);
            filePath.append(FileUtil.SEPARATOR);
            filePath.append(logFileUuid);
            filePath.append(".log");
            attachmentList.add(filePath.toString());
        }
        
        //Attach zipped CSV file to e-mail (if null skip process)
        if (gunshiCsvFileUuid != null){
            TblUploadFile uploadFile = tblUploadFileService.getTblUploadFile(gunshiCsvFileUuid);
            fileNameList.add(uploadFile.getUploadFileName());
            
            StringBuffer filePath = new StringBuffer();
            filePath.append(kartePropertyService.getDocumentDirectory());
            filePath.append(FileUtil.SEPARATOR);
            filePath.append(CommonConstants.WORK);
            filePath.append(FileUtil.SEPARATOR);
            filePath.append(gunshiCsvFileUuid);
            filePath.append(".zip");
            attachmentList.add(filePath.toString());
            
        }
        
        //email list (alert by department)
        List<MstUser> users = mstUserMailReceptionService.getMailReceiveDepartmentUsers("mail016");
        if (users.size() <= 0) return;
        List<MstUser> tempUsers = new ArrayList();
        String oldLangId = users.get(0).getLangId();
        for (MstUser user : users) {
            if (user.getLangId().equals(oldLangId)) {
                tempUsers.add(user);
            }
            else {
                sendMailByLanguage(tempUsers, currentTime, machine, sigma, response, attachmentList, fileNameList);
                tempUsers.clear();
                tempUsers.add(user);
            }
            oldLangId = user.getLangId();
        }
        if (tempUsers.size() > 0) {
            sendMailByLanguage(tempUsers, currentTime, machine, sigma, response, attachmentList, fileNameList);
        }
    }
    
    public BasicResponse recoveryMail(String macKey, LoginUser loginUser){
        BasicResponse response = new BasicResponse();
        StringBuilder sql = new StringBuilder();
        MstMachine machine = mstMachineService.getMstMachineFromMacKey(macKey);
        if (machine == null){
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage("Machine master is not found by macKey = " + macKey);
            return response;
        }
        MstSigma sigma = mstSigmaService.getMstSigmaFromSigmaId(machine.getSigmaId());
        if (sigma == null){
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage("Sigma Master is not found by id = " + machine.getSigmaId());
            return response;
//            throw new Exception("Sigma Master is not found by id = " + machine.getSigmaId());
        }
        sql.append("SELECT t ");
        sql.append("FROM TblSigmaImportError t ");
        sql.append("WHERE t.solvedFlg = 0 ");
        sql.append("AND t.macKey = :macKey ");
        sql.append("ORDER BY t.errorDatetime DESC ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("macKey", machine.getMacKey());
        int recordCount = query.getResultList().size();
        List<TblSigmaImportError> tblSigmaImportData = query.getResultList();
        
        //loop result list check for solved flg then break if found and send recovery email 
        for(int i=0;i<tblSigmaImportData.size();i++){
            int mailFlgCheck = tblSigmaImportData.get(i).getMailFlg();
            if (mailFlgCheck == 1){
                Date errorDatetime = tblSigmaImportData.get(i).getErrorDatetime();
                sendRecoveryMail(machine, sigma, response, loginUser, errorDatetime);
                break;
            }
        }
        
        StringBuilder sqlSolved = new StringBuilder();
        //update solvedFlg to 1
        sqlSolved.append("DELETE ");
        sqlSolved.append("FROM TblSigmaImportError t ");
        sqlSolved.append("WHERE t.macKey = :macKey ");
        Query querySolved = entityManager.createQuery(sqlSolved.toString());
        querySolved.setParameter("macKey", machine.getMacKey());
        querySolved.executeUpdate();
        return response;
    }
    
    private void sendRecoveryMail(MstMachine machine, MstSigma sigma, BasicResponse response, LoginUser loginUser, Date errorDatetime){
        //String currentTime = DateFormat.getCurrentDateTime();
        String errorTime = DateFormat.dateToStr(errorDatetime, DateFormat.DATETIME_FORMAT);
        List<MstUser> users = mstUserMailReceptionService.getMailReceiveDepartmentUsers("mail016");
        if (users.size() <= 0) return;
        List<MstUser> tempUsers = new ArrayList();
        String oldLangId = users.get(0).getLangId();
        for (MstUser user : users) {
            if(user.getLangId() != null){
                if (user.getLangId().equals(oldLangId)) {
                    tempUsers.add(user);
                }
                else {
                    sendRecoveryMailByLanguage(tempUsers, errorTime, machine, sigma, response);
                    tempUsers.clear();
                    tempUsers.add(user);
                }
                oldLangId = user.getLangId();
            }
        }
        if (tempUsers.size() > 0) {
            sendRecoveryMailByLanguage(tempUsers, errorTime, machine, sigma, response);
        }
    }
    
    private void sendRecoveryMailByLanguage(List<MstUser> users, String currentTime, MstMachine machine, MstSigma sigma, BasicResponse response){
        if (users.size() <= 0) return;
        List<String> toList = new ArrayList<>();
        for(MstUser user : users){
            if (user.getMailAddress() != null) {
                toList.add(user.getMailAddress());
            }
        }
        if (toList.size() <= 0) return;
        String url = kartePropertyService.getBaseUrl();
        String langId = users.get(0).getLangId();
        
        //Change sigma title and sigma body dict keys
        List<String> dictKeyList = Arrays.asList("sigma_import_error_mail_title","sigma_import_error_mail_body_recovery","sigma_import_error_mail_body","sigma_import_error_recovery", "sigma_import_error_datetime", 
                        "url", "sigma_code", "sigma_ip_address", "machine_id", "machine_name", "mac_key");
        dictKeyMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        String mailSubject = dictKeyMap.get("sigma_import_error_mail_title")+" ("+dictKeyMap.get("sigma_import_error_recovery")+")"+" ["+sigma.getMachineName()+"]"; //"M-Karte Sigma Import Error";
        StringBuilder mailBody = new StringBuilder();
        mailBody.append(dictKeyMap.get("sigma_import_error_mail_body_recovery"));
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(dictKeyMap.get("sigma_import_error_datetime")+": "+currentTime);
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(dictKeyMap.get("url")+": "+url);
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(dictKeyMap.get("sigma_code")+": "+sigma.getSigmaCode());
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(dictKeyMap.get("sigma_ip_address")+": "+sigma.getIpAddress());
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(dictKeyMap.get("machine_id")+": "+machine.getMachineId());
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(dictKeyMap.get("machine_name")+": "+machine.getMachineName());
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(dictKeyMap.get("mac_key")+": "+machine.getMacKey());
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        //Send e-mail
        try {
            mailSender.setMakePlainTextBody(true);
            mailSender.sendMail(toList, null, mailSubject, mailBody.toString());
        }
            catch (Exception e) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage("Failed to send email.");
        }
    }
    
    private void sendMailByLanguage(List<MstUser> users, String currentTime, MstMachine machine, MstSigma sigma, 
            BasicResponse response, List attachmentList, List fileNameList){
        if (users.size() <= 0) return;
        List<String> toList = new ArrayList<>();
        for(MstUser user : users){
            if (user.getMailAddress() != null) {
                toList.add(user.getMailAddress());
            }
        }
        if (toList.size() <= 0) return;
        String url = kartePropertyService.getBaseUrl();
        String langId = users.get(0).getLangId();
        List<String> dictKeyList = Arrays.asList("sigma_import_error_mail_title", "sigma_import_error_mail_body", "sigma_import_error_datetime", 
                        "url", "sigma_code", "sigma_ip_address", "machine_id", "machine_name", "mac_key");
        dictKeyMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        //Make E-mail message
        String mailSubject = dictKeyMap.get("sigma_import_error_mail_title")+" ["+sigma.getMachineName()+"]"; //"M-Karte Sigma Import Error";
        StringBuilder mailBody = new StringBuilder();
        mailBody.append(dictKeyMap.get("sigma_import_error_datetime")+": "+currentTime);
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(dictKeyMap.get("url")+": "+url);
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(dictKeyMap.get("sigma_code")+": "+sigma.getSigmaCode());
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(dictKeyMap.get("sigma_ip_address")+": "+sigma.getIpAddress());
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(dictKeyMap.get("machine_id")+": "+machine.getMachineId());
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(dictKeyMap.get("machine_name")+": "+machine.getMachineName());
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(dictKeyMap.get("mac_key")+": "+machine.getMacKey());
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        //Send e-mail
        try {
            mailSender.setMakePlainTextBody(true);
            if(attachmentList.size() <= 0){
                mailSender.sendMail(toList, null, mailSubject, mailBody.toString());
            } else {
                mailSender.sendMailWithMultiAttachment(toList, null, mailSubject, mailBody.toString(), attachmentList, fileNameList);
            }
        }
            catch (Exception e) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage("Failed to send email.");
        }
    }
    
    //Create data in tbl_sigma_import_error
    @Transactional
    private void createSigmaImportError(MstMachine machine, MstSigma sigma, BasicResponse response, String logFileUuid, String gunshiCsvFileUuid, String userUuid, int mailFlg) {
        Date time = new Date();
        String sigmaErrorId = IDGenerator.generate();

        TblSigmaImportError sigmaImportError = new TblSigmaImportError();
        sigmaImportError.setId(sigmaErrorId);
        sigmaImportError.setErrorDatetime(time);
        sigmaImportError.setMacKey(machine.getMacKey());
        sigmaImportError.setSigmaGunshiId(sigma.getId());
        sigmaImportError.setMachineUuid(machine.getUuid());
        sigmaImportError.setLogFileUuid(logFileUuid);
        sigmaImportError.setGunshiCsvFileUuid(gunshiCsvFileUuid);
        sigmaImportError.setSolvedFlg(0);
        sigmaImportError.setCreateDate(time);
        sigmaImportError.setUpdateDate(time);
        sigmaImportError.setCreateUserUuid(userUuid);
        sigmaImportError.setUpdateUserUuid(userUuid);
        sigmaImportError.setMailFlg(mailFlg);
        entityManager.persist(sigmaImportError);
    }
}
