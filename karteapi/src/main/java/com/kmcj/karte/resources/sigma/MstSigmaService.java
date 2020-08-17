/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author jiangxs
 */
@Dependent
public class MstSigmaService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     * sigmaデータ取得
     *
     * @return
     */
    
    public MstSigmaList getSigmaValue(String sigmaCode) {
        MstSigmaList response = new MstSigmaList();
        StringBuilder sql = new StringBuilder("SELECT m FROM MstSigma m WHERE m.validFlg = 1 ");
        if (sigmaCode != null) {
            sql.append(" AND m.sigmaCode = :sigmaCode ");
        }
        sql.append(" Order By m.sigmaCode ");
        Query query = entityManager.createQuery(sql.toString());
        if (sigmaCode != null) {
            query.setParameter("sigmaCode", sigmaCode);
        }
        List sigmaList = query.getResultList();
        response.setMstSigmas(sigmaList);
        for (int i = 0; i < sigmaList.size(); i++){
            MstSigma sigma = (MstSigma) sigmaList.get(i);
            if(sigma.getGunshiPassword() != null){
                sigma.setGunshiPassword(FileUtil.decrypt(sigma.getGunshiPassword()));
            }
        }
        return response;
    }
        
        
    public MstSigmaList getSigma(String machineId) {
        MstSigmaList response = new MstSigmaList();
        StringBuilder sql = new StringBuilder("SELECT m FROM MstSigma m JOIN FETCH MstMachine b ON m.id = b.sigmaId WHERE 1 = 1 ");
        if (machineId != null) {
            sql.append(" AND b.machineId = :machineId ");
        }
        sql.append(" Order By m.sigmaCode ");
        Query query = entityManager.createQuery(sql.toString());
        if (machineId != null) {
            query.setParameter("machineId", machineId);
        }
        List list = query.getResultList();
        List<MstSigmaVo> mstSigmaVoList = new ArrayList<>();
        MstSigmaVo sigmaVo;
        for (int i = 0; i < list.size(); i++) {
            MstSigma sigma = (MstSigma) list.get(i);
            sigmaVo = new MstSigmaVo();
            sigmaVo.setId(sigma.getId());
            sigmaVo.setSigmaCode(sigma.getSigmaCode() == null ? "" : sigma.getSigmaCode());
            sigmaVo.setFilesPath(sigma.getFilesPath() == null ? "" : sigma.getFilesPath());
            sigmaVo.setBackupFilesPath(sigma.getBackupFilesPath() == null ? "" : sigma.getBackupFilesPath());
            sigmaVo.setMachineName(sigma.getMachineName() == null ? "" : sigma.getMachineName());
            sigmaVo.setIpAddress(sigma.getIpAddress() == null ? "" : sigma.getIpAddress());
            sigmaVo.setCountErrorNotice(sigma.getCountErrorNotice());   
            sigmaVo.setErrorNoticeInterval(sigma.getErrorNoticeInterval());   
            sigmaVo.setGunshiUser(sigma.getGunshiUser() == null ? "" : sigma.getGunshiUser());
            sigmaVo.setGunshiPassword(sigma.getGunshiPassword() == null ? "" : FileUtil.decrypt(sigma.getGunshiPassword()));
            if (sigma.getValidFlg() != null) {
                sigmaVo.setValidFlg(String.valueOf(sigma.getValidFlg()));
            } else {
                sigmaVo.setValidFlg("");
            }
            mstSigmaVoList.add(sigmaVo);
        }
        response.setMstSigmaVos(mstSigmaVoList);
        return response;
    }

    /**
     * M1101 Σ軍師登録 編集された内容でΣ軍師マスタに追加・更新・削除を実行する。
     *
     * @param mstSigmaList
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse postMstSigmas(MstSigmaList mstSigmaList, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        List<MstSigma> mstSigmas = mstSigmaList.getMstSigmas();
        for (int i = 0; i < mstSigmas.size(); i++) {
            MstSigma sigmas = mstSigmas.get(i);
            String sigmaId = sigmas.getId();
            if (sigmaId != null && !"".equals(sigmaId)) {
                if (!checkSigmaId(sigmaId)) {
                    response.setError(true);
                    response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
                    return response;
                }
            }
            if ("1".equals(sigmas.getOperationFlag())) {
                //削除
                StringBuilder sql = new StringBuilder("DELETE FROM MstSigma m WHERE m.id = :id ");
                Query query = entityManager.createQuery(sql.toString());
                query.setParameter("id", sigmaId);
                query.executeUpdate();
                response.setError(false);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
            } else {
                //データルールチェック
                response = checkSigmaData(sigmas, loginUser);
                if (response.isError()) {
                    return response;
                }

                if ("3".equals(sigmas.getOperationFlag())) {
                    StringBuilder sql = new StringBuilder("UPDATE MstSigma m SET ");
                    sql.append(" m.sigmaCode = :sigmaCode, ");
                    sql.append(" m.filesPath = :filesPath, ");
                    sql.append(" m.backupFilesPath = :backupFilesPath, ");
                    sql.append(" m.machineName = :machineName, ");
                    sql.append(" m.ipAddress = :ipAddress, ");
                    sql.append(" m.countErrorNotice = :countErrorNotice, ");
                    sql.append(" m.errorNoticeInterval = :errorNoticeInterval, ");
                    sql.append(" m.validFlg = :validFlg, ");
                    sql.append(" m.updateDate = :updateDate, ");
                    sql.append(" m.updateUserUuid = :updateUserUuid ");
                    sql.append(" WHERE m.id = :id ");
                    Query query = entityManager.createQuery(sql.toString());
                    //更新 msg_record_updated
                    query.setParameter("sigmaCode", sigmas.getSigmaCode());
                    query.setParameter("filesPath", sigmas.getFilesPath());
                    query.setParameter("backupFilesPath", sigmas.getBackupFilesPath());
                    query.setParameter("machineName", sigmas.getMachineName());
                    query.setParameter("ipAddress", sigmas.getIpAddress());
                    query.setParameter("countErrorNotice", sigmas.getCountErrorNotice());
                    query.setParameter("errorNoticeInterval", sigmas.getErrorNoticeInterval());
                    if (sigmas.getValidFlg() != null) {
                        query.setParameter("validFlg", sigmas.getValidFlg());
                    } else {
                        query.setParameter("validFlg", 0);
                    }
                    query.setParameter("updateDate", new Date());
                    query.setParameter("updateUserUuid", loginUser.getUserUuid());
                    query.setParameter("id", sigmas.getId());
                    query.executeUpdate();
                    response.setError(false);
                    response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
                } else if ("4".equals(sigmas.getOperationFlag())) {
                    if (!checkSigmaCode(sigmas.getSigmaCode())) {
                        //追加 msg_record_added
                        sigmas.setId(IDGenerator.generate());
                        sigmas.setCreateDate(new Date());
                        sigmas.setCreateUserUuid(loginUser.getUserUuid());
                        sigmas.setUpdateDate(new Date());
                        sigmas.setUpdateUserUuid(loginUser.getUserUuid());
                        entityManager.persist(sigmas);
                        response.setError(false);
                        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_added"));
                    } else {
                        response.setError(true);
                        response.setErrorCode(ErrorMessages.E201_APPLICATION);
                        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
                        return response;
                    }
                }
            }
        }
        return response;
    }

    /**
     * データチェック
     *
     * @param id
     * @return
     */
    public boolean checkSigmaId(String id) {
        Query query = entityManager.createNamedQuery("MstSigma.findById");
        query.setParameter("id", id);
        try {
            query.getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }
    
    public MstSigma getMstSigmaFromSigmaId(String sigmaId){
        Query query = entityManager.createNamedQuery("MstSigma.findBySigmaId");
        query.setParameter("sigmaId", sigmaId);
        List<MstSigma> list = query.getResultList();
        if (list != null && list.size() > 0) {
            return (MstSigma)list.get(0);
        }
        else {
            return null;
        }
    }
    
    /**
     * データチェック
     *
     * @param sigmaCode
     * @return
     */
    public boolean checkSigmaCode(String sigmaCode) {
        Query query = entityManager.createNamedQuery("MstSigma.findBySigmaCode");
        query.setParameter("sigmaCode", sigmaCode);
        try {
            query.getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    /**
     * データチェック
     *
     * @param sigma
     * @param loginUser
     * @return
     */
    public BasicResponse checkSigmaData(MstSigma sigma, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        FileUtil fu = new FileUtil();
        String filePath = sigma.getFilesPath();
        if (filePath == null || "".equals(filePath)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        } else if (fu.maxLangthCheck(filePath, 400)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length"));
            return response;
        }
        String backupFilePath = sigma.getBackupFilesPath();
        if (backupFilePath == null || "".equals(backupFilePath)) {
            if (fu.maxLangthCheck(backupFilePath, 400)) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length"));
                return response;
            }
        }
        String sigmaCode = sigma.getSigmaCode();
        if (fu.maxLangthCheck(sigmaCode, 45)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length"));
            return response;
        }
        String machineName = sigma.getMachineName();
        if (machineName != null && !"".equals(machineName)) {
            if (fu.maxLangthCheck(machineName, 100)) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length"));
                return response;
            }
        }

        String ipAddress = sigma.getIpAddress();
        if (ipAddress != null && !"".equals(ipAddress)) {
            if (fu.maxLangthCheck(ipAddress, 100)) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length"));
                return response;
            }
        }

        return response;
    }

    /**
     * 
     * @param sigma 
     */
    @Transactional
    public void updateSigmaPassword(MstSigma sigma) {
        StringBuilder sql = new StringBuilder("UPDATE MstSigma m SET ");
        sql.append(" m.gunshiUser = :gunshiUser, ");
        sql.append(" m.gunshiPassword = :gunshiPassword ");
        sql.append(" WHERE m.id = :id ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("id", sigma.getId());
        query.setParameter("gunshiUser", sigma.getGunshiUser());
        query.setParameter("gunshiPassword", FileUtil.encrypt(sigma.getGunshiPassword()));
        query.executeUpdate();
    }
    
    /**
     * getSigmaMachine軍師と設備関連データ取得
     *
     * @param sigmaCode
     * @return
     */
    public MstSigmaList getSigmaMachine(String sigmaCode) {

        MstSigmaList response = new MstSigmaList();

        // 軍師マスト情報を取得
        StringBuilder sql = new StringBuilder("SELECT m FROM MstMachine m JOIN FETCH m.mstSigma WHERE m.mstSigma.validFlg = 1");

        // sigmaCode設定した場合
        if (StringUtils.isNotEmpty(sigmaCode)) {
            sql.append(" AND m.mstSigma.sigmaCode = :sigmaCode");
        }

        Query query = entityManager.createQuery(sql.toString());
        
        if (StringUtils.isNotEmpty(sigmaCode)) {
            query.setParameter("sigmaCode", sigmaCode);
        }
        List list = query.getResultList();

        HashMap<String, String> sigmaMachine = new HashMap();

        for (int i = 0; i < list.size(); i++) {

            MstMachine sigma = (MstMachine) list.get(i);

            sigmaMachine.put(sigma.getMacKey(), sigma.getMstSigma().getFilesPath());

        }

        response.setResourceMap(sigmaMachine);
        return response;
    }

}
