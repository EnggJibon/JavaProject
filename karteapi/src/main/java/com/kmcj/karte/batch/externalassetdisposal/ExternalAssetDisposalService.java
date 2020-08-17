/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.externalassetdisposal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.asset.disposal.AssetDisposalRequestNoticeService;
import com.kmcj.karte.resources.asset.disposal.TblAssetDisposalRequestVo;
import com.kmcj.karte.resources.asset.disposal.TblAssetDisposalRequestVoList;
import com.kmcj.karte.resources.asset.disposal.TblAssetDisposalService;
import com.kmcj.karte.util.FileUtil;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author admin
 */
@Dependent
public class ExternalAssetDisposalService {

    private static final String EXT_ASSET_DISPOSAL_GET_API_URL = "ws/karte/api/asset/disposal/extdata/get";

    private static final String EXT_ASSET_DISPOSAL_PUSH_API_URL = "ws/karte/api/asset/disposal/extdata/push";

    private static final Logger LOGGER = Logger.getLogger(ExternalAssetDisposalService.class.getName());

    @Inject
    private TblAssetDisposalService tblAssetDisposalService;
    
    @Inject
    private AssetDisposalRequestNoticeService assetDisposalRequestNoticeService;

    /**
     * Get external outgoing inspection result
     *
     * @param apiBaseUrl
     * @param token
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @param methodName
     */
    public void getExternalAssetDisposalTable(String apiBaseUrl, String token,
            String outgoingCompanyId, String incomingCompanyId, String methodName) {
        // 1,サプライヤーから廃棄データ未送信のデータを取得する

        String resultJson = FileUtil.getDataGet(apiBaseUrl + EXT_ASSET_DISPOSAL_GET_API_URL, token);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        TblAssetDisposalRequestVoList outgoingTblAssetDisposalRequestVoList = gson.fromJson(resultJson, TblAssetDisposalRequestVoList.class);
        if (outgoingTblAssetDisposalRequestVoList == null) {
            return;
        }

        if (outgoingTblAssetDisposalRequestVoList.isError()) {
            LOGGER.log(Level.WARNING, "サプライヤーへ通信失敗しました。", methodName);
        }

        List<TblAssetDisposalRequestVo> outgoingResultList = outgoingTblAssetDisposalRequestVoList.getTblAssetDisposalRequestVos();
        if (outgoingResultList == null || outgoingResultList.isEmpty()) {
            LOGGER.log(Level.FINE, "外部資産廃棄データが見つかりません。", methodName);
            return;
        }

        // 2,取得してもらったデータを所有会社のＤＢに格納する
        tblAssetDisposalService.saveExternalAssetDisposalList(outgoingTblAssetDisposalRequestVoList, outgoingCompanyId, incomingCompanyId, methodName);

        // 3,　1：申請済・・・カルテ間データ連携により資産所有会社サーバーに送信完了
        this.pushExternalAssetDisposalTable(apiBaseUrl, token, methodName, outgoingTblAssetDisposalRequestVoList);
        
         // 送信用のUUID
        String[] sendMailRequestUuid = new String[outgoingResultList.size()];
        int i = 0;
        for (TblAssetDisposalRequestVo tblAssetDisposalRequest : (List<TblAssetDisposalRequestVo>) outgoingResultList) {
            sendMailRequestUuid[i] = tblAssetDisposalRequest.getUuid();
            i++;
        }
        
        assetDisposalRequestNoticeService.sendNotice(1, sendMailRequestUuid);
    }

    /**
     *
     * @param apiBaseUrl
     * @param token
     * @param outgoingTblAssetDisposalRequestVoList
     */
    private void pushExternalAssetDisposalTable(String apiBaseUrl, String token, String methodName, TblAssetDisposalRequestVoList outgoingTblAssetDisposalRequestVoList) {

        // call API
        String resultJson = FileUtil.sendPost(apiBaseUrl + EXT_ASSET_DISPOSAL_PUSH_API_URL, token, outgoingTblAssetDisposalRequestVoList);

        if (StringUtils.isNotEmpty(resultJson)) {
            Gson gson = new Gson();
            BasicResponse response = gson.fromJson(resultJson, BasicResponse.class);
            // if error then output log
            if (response.isError()) {
                LOGGER.log(Level.WARNING, response.getErrorMessage(), methodName);
                return;
            }
        }
    }

}
