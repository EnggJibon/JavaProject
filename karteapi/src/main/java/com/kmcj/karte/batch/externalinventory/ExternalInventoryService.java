/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.externalinventory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.asset.inventory.mgmt.company.TblInventoryMgmtCompanyService;
import com.kmcj.karte.resources.asset.inventory.mgmt.company.TblInventoryMgmtCompanyVoList;
import com.kmcj.karte.resources.asset.inventory.request.detail.TblInventoryRequestDetailVoList;
import com.kmcj.karte.util.FileUtil;
import java.util.Set;
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
public class ExternalInventoryService {

    private static final String EXT_INVENTORY_GET_API_URL = "ws/karte/api/inventory/mgmt/company/extdata/get";

    private static final String EXT_INVENTORY_PUSH_API_URL = "ws/karte/api/inventory/mgmt/company/extdata/push";

    private static final Logger LOGGER = Logger.getLogger(ExternalInventoryService.class.getName());

    @Inject
    private TblInventoryMgmtCompanyService tblInventoryMgmtCompanyService;

    /**
     * Get external outgoing inventory result
     *
     * @param apiBaseUrl
     * @param token
     * @param methodName
     * @param companyId
     */
    public void getExternalInventoryTable(String apiBaseUrl, String token, String methodName, String companyId) {
        // 1,サプライヤーから棚卸依頼データを取得する
        String resultJson = FileUtil.getDataGet(apiBaseUrl + EXT_INVENTORY_GET_API_URL, token);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        TblInventoryRequestDetailVoList tblInventoryRequestDetailVoList = gson.fromJson(resultJson, TblInventoryRequestDetailVoList.class);

        if (tblInventoryRequestDetailVoList == null || tblInventoryRequestDetailVoList.getTblInventoryRequestDetailVos() == null || tblInventoryRequestDetailVoList.getTblInventoryRequestDetailVos().isEmpty()) {
            LOGGER.log(Level.FINE, "外部棚卸依頼回答データが見つかりません。", methodName);
            return;
        }

        // 2,取得してもらったデータを所有会社のＤＢに格納する
        boolean isFlg = tblInventoryMgmtCompanyService.saveExternalInventoryList(tblInventoryRequestDetailVoList.getTblInventoryRequestDetailVos(), methodName, companyId);
        if (isFlg) {
            // 3,サプライヤーの棚卸依頼ステータスを回答送信済に更新する
            this.pushExternalInventoryRequest(apiBaseUrl, token, methodName, tblInventoryRequestDetailVoList.getRequestUuids());
        }
        
    }

    /**
     *
     * @param apiBaseUrl
     * @param token
     * @param outgoingTblAssetDisposalRequestVoList
     */
    private void pushExternalInventoryRequest(String apiBaseUrl, String token, String methodName, Set<String> requestUuids) {

        // call API
        String resultJson = FileUtil.sendPost(apiBaseUrl + EXT_INVENTORY_PUSH_API_URL, token, requestUuids);

        if (StringUtils.isNotEmpty(resultJson)) {
            Gson gson = new Gson();
            BasicResponse response = gson.fromJson(resultJson, BasicResponse.class);
            // if error then output log
            if (response.isError()) {
                LOGGER.log(Level.WARNING, response.getErrorMessage(), methodName);
            }
        }
    }

}
