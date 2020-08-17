/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.external;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.Credential;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author jiangxs
 */
@Dependent
public class MstExternalDataGetSettingService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     * 外部データ取得設定 外部データ取得設定テーブルから全件データを取得し、会社コードの昇順で表示する。
     *
     * @param companyCode
     * @param companyName
     * @return
     */
    public List<MstExternalDataGetSetting> getExternalDataGetSettings(String companyCode, String companyName) {

        String strCompanyCode = "";
        String strCompanyName = "";

        StringBuffer sql;
        sql = new StringBuffer("SELECT m FROM MstExternalDataGetSetting m JOIN FETCH m.mstCompany WHERE 1=1 ");

        if (companyCode != null && !"".equals(companyCode)) {
            strCompanyCode = companyCode.trim();
            sql.append(" And m.mstCompany.companyCode = :companyCode ");
        }

        if (companyName != null && !"".equals(companyName)) {
            strCompanyName = companyName.trim();
            sql.append(" And m.mstCompany.companyName like :companyName ");
        }

        sql.append(" Order by m.mstCompany.companyCode "); //companyCodeの昇順

        Query query = entityManager.createQuery(sql.toString());

        if (companyCode != null && !"".equals(companyCode)) {
            query.setParameter("companyCode", strCompanyCode);
        }

        if (companyName != null && !"".equals(companyName)) {
            query.setParameter("companyName", "%" + strCompanyName + "%");
        }

        return query.getResultList();
    }

    /**
     * データが選択されていない場合のチェック
     *
     * @param id
     * @return
     */
    public boolean checkExternal(String id) {
        Query query = entityManager.createNamedQuery("MstExternalDataGetSetting.findById");
        query.setParameter("id", id);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     * 外部データ取得設定 外部データ取得情報を変更
     *
     * @param id
     * @return
     */
    public MstExternalDataGetSetting getExternalDataGetSetting(String id) {
        Query query = entityManager.createNamedQuery("MstExternalDataGetSetting.findById");
        query.setParameter("id", id);
        List list = query.getResultList();
        MstExternalDataGetSetting response = (MstExternalDataGetSetting) list.get(0);
        return response;
    }

    /**
     * 外部データ取得設定 外部データ取得情報を削除
     *
     * @param id
     * @return
     */
    @Transactional
    public int deleteExternalDataGetSetting(String id) {
        Query query = entityManager.createNamedQuery("MstExternalDataGetSetting.deleteExternal");
        query.setParameter("id", id);
        return query.executeUpdate();
    }


    /**
     * 外部データ取得設定 接続テスト
     * @param userid
     * @param password
     * @param apiBaseUrl
     * @param langId
     * @return 
     */
    public BasicResponse testExternalDataGetSettingUrl(String userid, String password, String apiBaseUrl, String langId) {
        BasicResponse response = new BasicResponse();
        URL urlStr;

        try {
            if (null != apiBaseUrl) {
                if (!apiBaseUrl.endsWith("/")) {
                    apiBaseUrl = apiBaseUrl + "/";
                }
            }
            //　相手のURLが有効かどうか検証
            FileUtil.SSL();
            
            urlStr = new URL(apiBaseUrl);
            HttpURLConnection connection = (HttpURLConnection) urlStr.openConnection();

            int state = connection.getResponseCode();
            // ステータスコード404(Not Found)の場合
            if (state != 200) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_url_invalid"));
            } else {

                Credential credential = new Credential();
                credential.setUserid(userid);
                credential.setPassword(password);
                String pathUrl = apiBaseUrl + "ws/karte/api/authentication/extlogin?lang=" + langId;
                Credential result = FileUtil.sendPost(pathUrl, credential);

                if (result.isError()) {
                    response.setError(result.isError());
                    if (null == result.getErrorCode()) {
                        response.setErrorCode(ErrorMessages.E201_APPLICATION);
                        response.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_url_invalid"));
                    } else {
                        response.setErrorCode(result.getErrorCode());
                        response.setErrorMessage(result.getErrorMessage());
                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_url_invalid"));
        } 

        return response;
    }

    /**
     * 外部データ取得設定 入力された外部データ取得設定をデータベースに反映する。パスワードは暗号化して保存する。
     *
     * @param mstExternalDataGetSettingVo
     * @param user
     */
    @Transactional
    public void postExternalDataGetSetting(MstExternalDataGetSettingVo mstExternalDataGetSettingVo, LoginUser user) {
        MstExternalDataGetSetting mstExternalDataGetSetting = new MstExternalDataGetSetting();
        mstExternalDataGetSetting.setId(IDGenerator.generate());
        mstExternalDataGetSetting.setCompanyId(mstExternalDataGetSettingVo.getCompanyId());
        mstExternalDataGetSetting.setUserId(mstExternalDataGetSettingVo.getUserId());
        mstExternalDataGetSetting.setApiBaseUrl(mstExternalDataGetSettingVo.getApiBaseUrl());
        //パスワードは暗号化して保存する。
        mstExternalDataGetSetting.setEncryptedPassword(FileUtil.encrypt(mstExternalDataGetSettingVo.getEncryptedPassword()));
        mstExternalDataGetSetting.setValidFlg(mstExternalDataGetSettingVo.getValidFlg());
        mstExternalDataGetSetting.setLatestExecutedDate(mstExternalDataGetSettingVo.getLatestExecutedDate());
        mstExternalDataGetSetting.setCreateDate(new Date());
        mstExternalDataGetSetting.setCreateUserUuid(user.getUserUuid());
        mstExternalDataGetSetting.setUpdateDate(new Date());
        mstExternalDataGetSetting.setUpdateUserUuid(user.getUserUuid());
        entityManager.persist(mstExternalDataGetSetting);
    }

    /**
     * 外部データ取得設定 入力された外部データ取得設定をデータベースに反映する。パスワードは暗号化して保存する。 編集の場合
     *
     * @param mstExternalDataGetSettingVo
     * @param user
     * @return
     */
    @Transactional
    public void putExternalDataGetSetting(MstExternalDataGetSettingVo mstExternalDataGetSettingVo, LoginUser user) {

        StringBuffer sql = new StringBuffer(" UPDATE MstExternalDataGetSetting m SET ");
        if (mstExternalDataGetSettingVo.getCompanyId() != null && !"".equals(mstExternalDataGetSettingVo.getCompanyId())) {
            sql.append(" m.companyId = :companyId, ");
        }
        if (mstExternalDataGetSettingVo.getUserId() != null && !"".equals(mstExternalDataGetSettingVo.getUserId())) {
            sql.append(" m.userId = :userId, ");
        }
        if (mstExternalDataGetSettingVo.getEncryptedPassword() != null && !"".equals(mstExternalDataGetSettingVo.getEncryptedPassword())) {
            sql.append(" m.encryptedPassword = :encryptedPassword, ");
        }
        if (mstExternalDataGetSettingVo.getApiBaseUrl() != null && !"".equals(mstExternalDataGetSettingVo.getApiBaseUrl())) {
            sql.append(" m.apiBaseUrl = :apiBaseUrl, ");
        }
        if (mstExternalDataGetSettingVo.getLatestExecutedDate() != null) {
            sql.append(" m.latestExecutedDate = :latestExecutedDate, ");
        }
        //validFlg yes:1 no:0
        sql.append(" m.validFlg = :validFlg, ");
        sql.append(" m.updateDate = :updateDate, ");
        sql.append(" m.updateUserUuid = :updateUserUuid ");
        sql.append(" WHERE m.id = :id ");

        Query query = entityManager.createQuery(sql.toString());

        if (mstExternalDataGetSettingVo.getCompanyId() != null && !"".equals(mstExternalDataGetSettingVo.getCompanyId())) {
            query.setParameter("companyId", mstExternalDataGetSettingVo.getCompanyId());
        }
        if (mstExternalDataGetSettingVo.getUserId() != null && !"".equals(mstExternalDataGetSettingVo.getUserId())) {
            query.setParameter("userId", mstExternalDataGetSettingVo.getUserId());
        }
        if (mstExternalDataGetSettingVo.getEncryptedPassword() != null && !"".equals(mstExternalDataGetSettingVo.getEncryptedPassword())) {
            if (equalEncryptedPassword(mstExternalDataGetSettingVo.getId(), mstExternalDataGetSettingVo.getEncryptedPassword())) {
                query.setParameter("encryptedPassword", mstExternalDataGetSettingVo.getEncryptedPassword());
            } else {
                query.setParameter("encryptedPassword", FileUtil.encrypt(mstExternalDataGetSettingVo.getEncryptedPassword()));
            }
        }
        if (mstExternalDataGetSettingVo.getApiBaseUrl() != null && !"".equals(mstExternalDataGetSettingVo.getApiBaseUrl())) {
            query.setParameter("apiBaseUrl", mstExternalDataGetSettingVo.getApiBaseUrl());
        }
        if (mstExternalDataGetSettingVo.getLatestExecutedDate() != null) {
            query.setParameter("latestExecutedDate", mstExternalDataGetSettingVo.getLatestExecutedDate());
        }
        query.setParameter("validFlg", mstExternalDataGetSettingVo.getValidFlg());
        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", user.getUserUuid());
        query.setParameter("id", mstExternalDataGetSettingVo.getId());

        query.executeUpdate();
    }

    /**
     * EncryptedPasswordをチェックして
     *
     * @param id
     * @param encryptedPassword
     * @return
     */
    public boolean equalEncryptedPassword(String id, String encryptedPassword) {
        Query query = entityManager.createNamedQuery("MstExternalDataGetSetting.findById");
        query.setParameter("id", id);
        MstExternalDataGetSetting mstExternalDataGetSetting = (MstExternalDataGetSetting) query.getSingleResult();
        String strExternalDataGetSetting = mstExternalDataGetSetting.getEncryptedPassword();
        if (strExternalDataGetSetting != null && !"".equals(strExternalDataGetSetting)) {
            if (strExternalDataGetSetting.equals(encryptedPassword)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    /**
     * バッチ molds
     *
     * @return
     */
    public List<MstExternalDataGetSetting> getExternalDataMoldValids() {
        List<MstExternalDataGetSetting> res = entityManager.createNamedQuery("MstExternalDataGetSetting.findByValidFlg")
                .setParameter("validFlg", 1)
                .getResultList();                
        return res;
    }
    
    /**
     * バッチ machines
     *
     * @return
     */
    public List<MstExternalDataGetSetting> getExternalDataMachineValids() {
        List<MstExternalDataGetSetting> res = entityManager.createNamedQuery("MstExternalDataGetSetting.findMachinesByValidFlg")
                .setParameter("validFlg", 1)
                .getResultList();        
        return res;
    }
    
    /**
     * バッチ component inspection
     *
     * @return
     */
    public List<MstExternalDataGetSetting> getExternalDataGetSettringForComponentInspection() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT DISTINCT me FROM MstExternalDataGetSetting me");
        queryBuilder.append(" JOIN MstComponentInspectionItemsTable m ON m.outgoingCompanyId = me.companyId");
        queryBuilder.append(" WHERE me.validFlg = :validFlg");
        
        List<MstExternalDataGetSetting> res = this.entityManager
                .createQuery(queryBuilder.toString())
                .setParameter("validFlg", 1)
                .getResultList();
        return res;
    }
    
    /**
     * バッチ mold&machines
     *
     * @return
     */
    public List<MstExternalDataGetSetting> getExternalDataAllValids() {
        List<MstExternalDataGetSetting> res = entityManager.createNamedQuery("MstExternalDataGetSetting.findByValidFlg")
                .setParameter("validFlg", 1)
                .getResultList();
        res = null == res ? new ArrayList<MstExternalDataGetSetting>() : res;
        
        List<MstExternalDataGetSetting> resMachines = entityManager.createNamedQuery("MstExternalDataGetSetting.findMachinesByValidFlg")
                .setParameter("validFlg", 1)
                .getResultList();
        if (null != resMachines && !resMachines.isEmpty()) {
            res.addAll(resMachines);
            res = new ArrayList(new HashSet(res));
        }
        
        return res;
    }
    
    /**
     * バッチ
     *
     * @param companyId
     * @return
     */
    public List<MstMold> getMoldByExternalDataCompanyId(String companyId) {
        Query query = entityManager.createNamedQuery("MstExternalDataGetSetting.findMoldByExternalDataCompanyId");
        query.setParameter("companyId", companyId);
        return query.getResultList();
    }
    
    /**
     * バッチ
     *
     * @param companyId
     * @return
     */
    public List<MstMachine> getMachineByExternalDataCompanyId(String companyId) {
        Query query = entityManager.createNamedQuery("MstExternalDataGetSetting.findMachineByExternalDataCompanyId");
        query.setParameter("companyId", companyId);
        return query.getResultList();
    }
    
    /**
     * バッチ external asset disposal
     *
     * @return
     */
    public List<MstExternalDataGetSetting> getExternalDataGetSettringForExternalAssetDisposal() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT DISTINCT me FROM MstExternalDataGetSetting me");
        queryBuilder.append(" WHERE me.validFlg = :validFlg");

        List<MstExternalDataGetSetting> res = this.entityManager
                .createQuery(queryBuilder.toString())
                .setParameter("validFlg", 1)
                .getResultList();
        return res;
    }
    
    
    /**
     * external asset disposal
     *
     * @param companyId
     * @return
     */
    public List<MstExternalDataGetSetting> getExternalDataGetSettringByCompanyId(String companyId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT DISTINCT me FROM MstExternalDataGetSetting me");
        queryBuilder.append(" WHERE me.companyId = :companyId");
        queryBuilder.append(" AND me.validFlg = :validFlg");

        List<MstExternalDataGetSetting> res = this.entityManager
                .createQuery(queryBuilder.toString())
                .setParameter("validFlg", 1)
                .setParameter("companyId", companyId)
                .getResultList();
        return res;
    }
    /**
     * バッチでMstExternalDataGetSetting's LatestExecutedDateを更新
     *
     * @param aExternalDataGetSetting
     * @param executedDate
     */
    @Transactional
    public void updateExtLatestExecutedDateByBatch(MstExternalDataGetSetting aExternalDataGetSetting, Date executedDate) {
        if (null != executedDate) {
            aExternalDataGetSetting.setLatestExecutedDate(executedDate);
            entityManager.merge(aExternalDataGetSetting);
        }        
    }
    
    /**
     * 更新前回取得日時
     * @param id
     * @param loginUser
     * @return 
     */
    @Transactional
    public BasicResponse putLatestExecutedDateNull(String id,LoginUser loginUser){
        BasicResponse response = new BasicResponse();
        StringBuilder sql = new StringBuilder("UPDATE MstExternalDataGetSetting m SET m.latestExecutedDate = null WHERE m.id= :id ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("id", id);
        int count = query.executeUpdate();
        if (count > 0) {
            response.setError(false);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        }
        return response;
    }
}
