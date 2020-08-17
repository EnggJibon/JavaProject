/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.externalassetdisposal;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.Credential;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.company.MstCompanyService;
import com.kmcj.karte.resources.external.MstExternalDataGetSetting;
import com.kmcj.karte.resources.external.MstExternalDataGetSettingService;
import com.kmcj.karte.util.FileUtil;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * このバッチが外部廃棄資産データ取得する
 *
 * @author admin
 */
@Named
@Dependent
public class ExternalAssetDisposalBatchlet extends AbstractBatchlet {

    /**
     * API path
     */
    

    public final static String BATCH_NAME = "externalAssetDisposalBatchlet";

    @Inject
    JobContext jobContext;

    @Inject
    public MstExternalDataGetSettingService externalDataGetSettingService;

    @Inject
    private ExternalAssetDisposalService externalAssetDisposalService;

    @Inject
    private MstCompanyService mstCompanyService;

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(ExternalAssetDisposalBatchlet.class.getName());
    private final static Level logLevel = Level.FINE;

    @Override
    public String process() {
        LOGGER.log(Level.INFO, "  ---> [[{0}]] Start", BATCH_NAME);

        try {
            //  外部データ取得設定から有効フラグ=1のレコードを取得し、レコード数分、以下の処理をループ。  
            List<MstExternalDataGetSetting> externalDataSettings = this.externalDataGetSettingService.getExternalDataGetSettringForExternalAssetDisposal();
            if (externalDataSettings.isEmpty()) {
                LOGGER.log(Level.INFO, "  <--- [[{0}]] End", BATCH_NAME);
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
                String pathUrl = apiBaseUrl + CommonConstants.EXT_LOGIN_API_URL;
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

                //　認証できたら、Tokenを取得
                String token = result.getToken();

                String outgoingCompanyId = externalDataSetting.getCompanyId();

                // 外部資産廃棄データを取得する
                LOGGER.log(logLevel, "  ---> [[{0}]] Start", BATCH_NAME + "_" + outgoingCompanyId);
                this.externalAssetDisposalService.getExternalAssetDisposalTable(apiBaseUrl, token, outgoingCompanyId, selfCompanyId, BATCH_NAME);
                LOGGER.log(logLevel, "  <--- [[{0}]] End", BATCH_NAME + "_" + outgoingCompanyId);

            }

            LOGGER.log(Level.INFO, "  <--- [[{0}]] End", BATCH_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "  < Error >", e.getMessage());
            
        }
        return BatchStatus.COMPLETED.toString();
    }
}
