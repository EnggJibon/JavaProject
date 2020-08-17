/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.extercomponentinspection;

import com.kmcj.karte.resources.authentication.Credential;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.company.MstCompanyService;
import com.kmcj.karte.resources.external.MstExternalDataGetSetting;
import com.kmcj.karte.resources.external.MstExternalDataGetSettingService;
import com.kmcj.karte.util.FileUtil;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * このバッチが外部部品検査データ取得する
 * 
 * @author duanlin
 */
@Named
@Dependent
public class ExternalComponentInspectionBatchlet extends AbstractBatchlet {
    /**
     * API path
     */
    private static final String EXT_LOGIN_API_URL = "ws/karte/api/authentication/extlogin?lang=ja";
    
    public static String PROCESS_MODE_ALL = "all";
    public static String PROCESS_MODE_ITEM = "item";
    public static String PROCESS_MODE_OUTGOING = "outgoing";
    public static String PROCESS_MODE_INCOMING = "incoming";
    public static String PROCESS_MODE_PO = "po";
    public static String PROCESS_MODE_FILE_SETTINGS = "file_settings";

    public static String BATCH_PROPERTY_PROCESS_MODE = "processMode";
    public final static String BATCH_NAME = "externalComponentInspectionBatchlet";

    @Inject
    JobContext jobContext;
    @Inject
    public MstExternalDataGetSettingService externalDataGetSettingService;
    @Inject
    private ExternalComponentInspectionService externalComponentInspectionService;
    @Inject
    private MstCompanyService mstCompanyService;
    /** Logger */
    private static final Logger LOGGER = Logger.getLogger(ExternalComponentInspectionBatchlet.class.getName());
    private final static Level logLevel = Level.FINE;

    @Override
    public String process() {
        LOGGER.log(logLevel, "  ---> [[{0}]] Start", BATCH_NAME);
        
        Properties jobProperties = BatchRuntime.getJobOperator().getParameters(this.jobContext.getExecutionId());
        String processMode = PROCESS_MODE_ALL;
        if (jobProperties != null) {
            processMode = jobProperties.getProperty(BATCH_PROPERTY_PROCESS_MODE);
        }

        try {
            //  外部データ取得設定から有効フラグ=1のレコードを取得し、レコード数分、以下の処理をループ。  
            List<MstExternalDataGetSetting> externalDataSettings = this.externalDataGetSettingService.getExternalDataGetSettringForComponentInspection();
            if (externalDataSettings.isEmpty()) {
                LOGGER.log(logLevel, "  <--- [[{0}]] End", BATCH_NAME);
                return BatchStatus.COMPLETED.toString();
            }
            
            MstCompany selfCompany = this.mstCompanyService.getSelfCompany();
            String selfCompanyId = selfCompany.getId();
            for (MstExternalDataGetSetting externalDataSetting : externalDataSettings) {
                String apiBaseUrl = externalDataSetting.getApiBaseUrl();
                if (null == apiBaseUrl || apiBaseUrl.equals("")) {
                    continue;
                }

                if (!apiBaseUrl.endsWith("/")) {
                    apiBaseUrl = apiBaseUrl + "/";
                }

                // 外部データ取得する前に認証確認
                Credential credential = new Credential();
                credential.setUserid(externalDataSetting.getUserId());
                credential.setPassword(FileUtil.decrypt(externalDataSetting.getEncryptedPassword()));
                String pathUrl = apiBaseUrl + EXT_LOGIN_API_URL;
                FileUtil.SSL();
                Credential result;
                try {
                    result = FileUtil.sendPost(pathUrl, credential);
                } catch (Exception e) {
                    // 認証エラー発生場合、スキップ                    
                    LOGGER.log(Level.SEVERE, e.getMessage());
                    continue;
                }

                // 認証失敗　スキップ
                if (result.isValid() == false || result.isError() == true) {
                    LOGGER.log(Level.WARNING, result.getErrorMessage());
                    continue;
                }
                LOGGER.log(Level.INFO, "Company ID : {0} is authenticated.", externalDataSetting.getCompanyId());

                //　認証できたら、Tokenを取得
                String token = result.getToken();

                String outgoingCompanyId = externalDataSetting.getCompanyId();
                if (PROCESS_MODE_ALL.equals(processMode) || PROCESS_MODE_FILE_SETTINGS.equals(processMode)) {
                    LOGGER.log(logLevel, "  ---> [[{0}]] Start", BATCH_NAME + "_pushInpsectionFileSettings_" + outgoingCompanyId);
                    try {
                        externalComponentInspectionService.pushExtInspectFileSettings(apiBaseUrl, token, outgoingCompanyId);
                    } catch(Exception e) {
                        LOGGER.log(Level.SEVERE, "  < Error >", e.getMessage());
                        LOGGER.log(Level.SEVERE, "  < Error >", e);
                    }
                    
                    LOGGER.log(logLevel, "  <--- [[{0}]] End", BATCH_NAME + "_pushInpsectionFileSettings_" + outgoingCompanyId);
                }
                
                if (PROCESS_MODE_ALL.equals(processMode) || PROCESS_MODE_ITEM.equals(processMode)) {
                    // push inspection items table
                    LOGGER.log(logLevel, "  ---> [[{0}]] Start", BATCH_NAME + "_pushInpsectionItemsTable_" + outgoingCompanyId);
                    this.externalComponentInspectionService.pushExternalInspectionItemsTable(apiBaseUrl, token, outgoingCompanyId, selfCompanyId, BATCH_NAME);
                    LOGGER.log(logLevel, "  <--- [[{0}]] End", BATCH_NAME + "_pushInpsectionItemsTable_" + outgoingCompanyId);
                }

                if (PROCESS_MODE_ALL.equals(processMode) || PROCESS_MODE_OUTGOING.equals(processMode)) {
                    LOGGER.log(logLevel, "  ---> [[{0}]] Start", BATCH_NAME + "_getExtAborting_" + outgoingCompanyId);
                    try {
                        this.externalComponentInspectionService.getExtAborting(apiBaseUrl, token, BATCH_NAME);
                    } catch(Exception e) {
                        LOGGER.log(Level.SEVERE, "  < Error >", e.getMessage());
                        LOGGER.log(Level.SEVERE, "  < Error >", e);
                    }
                    LOGGER.log(logLevel, "  <--- [[{0}]] End", BATCH_NAME + "_getExtAborting_" + outgoingCompanyId);
                    // get outgoing inpsecion result
                    LOGGER.log(logLevel, "  ---> [[{0}]] Start", BATCH_NAME + "_getOutgoingInspectionResult_" + outgoingCompanyId);
                    this.externalComponentInspectionService.getExtOutgoingInspectionResult(apiBaseUrl, token, outgoingCompanyId, selfCompanyId, BATCH_NAME);
                    LOGGER.log(logLevel, "  <--- [[{0}]] End", BATCH_NAME + "_getOutgoingInspectionResult_" + outgoingCompanyId);
                }

                if (PROCESS_MODE_ALL.equals(processMode) || PROCESS_MODE_INCOMING.equals(processMode)) {
                    // push incoming inspection result
                    LOGGER.log(logLevel, "  ---> [[{0}]] Start", BATCH_NAME + "_pushIncomingInspectionResult_" + outgoingCompanyId);
                    this.externalComponentInspectionService.pushExtIncomingInspectionResult(apiBaseUrl, token, outgoingCompanyId, selfCompanyId, BATCH_NAME);
                    LOGGER.log(logLevel, "  <--- [[{0}]] End", BATCH_NAME + "_pushIncomingInspectionResult_" + outgoingCompanyId);
                }
                
                // KM-463 PO検査間のデータ構造変更 2017/12/5 by penggd Start
                if (PROCESS_MODE_ALL.equals(processMode) || PROCESS_MODE_PO.equals(processMode)) {
                    // get outgoing poInfo
                    LOGGER.log(logLevel, "  ---> [[{0}]] Start", BATCH_NAME + "_getOutgoingPoInfo_" + outgoingCompanyId);
                    this.externalComponentInspectionService.getExtOutgoingPoInfo(apiBaseUrl, token, outgoingCompanyId, selfCompanyId, BATCH_NAME);
                    LOGGER.log(logLevel, "  <--- [[{0}]] End", BATCH_NAME + "_getOutgoingPoInfo_" + outgoingCompanyId);
                }
                
                if (PROCESS_MODE_ALL.equals(processMode) || PROCESS_MODE_PO.equals(processMode)) {
                    // del poInfo 
                    LOGGER.log(logLevel, "  ---> [[{0}]] Start", BATCH_NAME + "_getOutgoingPoInfo_" + outgoingCompanyId);
                    this.externalComponentInspectionService.delＴblPoOutboundInfo(apiBaseUrl, token, outgoingCompanyId, selfCompanyId, BATCH_NAME);
                    LOGGER.log(logLevel, "  <--- [[{0}]] End", BATCH_NAME + "_getOutgoingPoInfo_" + outgoingCompanyId);
                }
                // KM-463 PO検査間のデータ構造変更 2017/12/5 by penggd Start
            }
            
            LOGGER.log(logLevel, "  <--- [[{0}]] End", BATCH_NAME);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "  < Error >", e.getMessage());
            LOGGER.log(Level.SEVERE, "  < Error >", e);
        }
        return BatchStatus.COMPLETED.toString();
    }
}
