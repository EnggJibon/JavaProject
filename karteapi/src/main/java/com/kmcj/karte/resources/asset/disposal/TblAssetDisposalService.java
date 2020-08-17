/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.disposal;

import com.google.gson.Gson;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.assignment.TblNumberAssignmentService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.excelhandle.read.ReadExcel;
import com.kmcj.karte.excelhandle.read.ReadListExcel;
import com.kmcj.karte.excelhandle.write.WriteExcelList;
import com.kmcj.karte.excelhandle.write.WriteListExcel;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.asset.MstAsset;
import com.kmcj.karte.resources.asset.MstAssetService;
import com.kmcj.karte.resources.asset.MstAssetVo;
import com.kmcj.karte.resources.asset.disposal.excelvo.TblAssetDisposalRequestExcelFormOutPutVo;
import com.kmcj.karte.resources.asset.disposal.excelvo.TblAssetDisposalRequestExcelOutPutVo;
import com.kmcj.karte.resources.authentication.Credential;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.company.MstCompanyService;
import com.kmcj.karte.resources.currency.MstCurrencyService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.external.MstExternalDataGetSetting;
import com.kmcj.karte.resources.external.MstExternalDataGetSettingService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.asset.inventory.TblInventoryService;
import com.kmcj.karte.resources.item.MstItem;
import com.kmcj.karte.resources.item.MstItemService;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.resources.user.MstUserService;
import com.kmcj.karte.util.BeanCopyUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;

import java.beans.PropertyDescriptor;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author admin
 */
@Dependent
public class TblAssetDisposalService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private TblNumberAssignmentService tblNumberAssignmentService;

    @Inject
    private MstUserService mstUserService;

    @Inject
    private MstCurrencyService mstCurrencyService;

    @Inject
    private MstCompanyService mstCompanyService;

    @Inject
    private MstAssetService mstAssetService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private TblCsvImportService tblCsvImportService;

    @Inject
    private AssetDisposalRequestNoticeService assetDisposalRequestNoticeService;

    @Inject
    private MstItemService mstItemService;

    @Inject
    public MstExternalDataGetSettingService externalDataGetSettingService;

    private static final String EXT_ASSET_DISPOSAL_PUSH_ONE_API_URL = "ws/karte/api/asset/disposal/extdata/push/one";

    // 廃棄申請理由
    public static final String DISPOSAL_REQUEST_REASON = "tbl_asset_disposal_request.disposal_request_reason";
    // EOL確認区分
    public static final String EOL_CONFIRMATION = "tbl_asset_disposal_request.eol_confirmation";
    // 廃棄可否判断
    public static final String DISPOSAL_JUDGMENT = "tbl_asset_disposal_request.disposal_judgment";
    // 受付却下理由
    public static final String RECEIVE_REJECT_REASON = "tbl_asset_disposal_request.receive_reject_reason";
    // OEM先
    public static final String OEM_DESTINATION = "tbl_asset_disposal_request.oem_destination";
    // AP廃棄可否判断
    public static final String AP_DISPOSAL_JUDGMENT = "tbl_asset_disposal_request.ap_disposal_judgment";
    // AP最終まとめ発注
    public static final String AP_FINAL_BULK_ORDER = "tbl_asset_disposal_request.ap_final_bulk_order";
    // AP却下理由
    public static final String AP_REJECT_REASON = "tbl_asset_disposal_request.ap_reject_reason";
    // 最終回答
    public static final String FINAL_REPLY = "tbl_asset_disposal_request.final_reply";
    // 最終却下理由
    public static final String FINAL_REJECT_REASON = "tbl_asset_disposal_request.final_reject_reason";
    // 社内ステータス
    public static final String INTERNAL_STATUS = "tbl_asset_disposal_request.internal_status";
    // 管理地域
    public static final String MST_ASSET_MGMT_REGION = "mst_asset.mgmt_region";
    // 取得区分
    public static final String MST_ASSET_ACQUISITION_TYPE = "mst_asset.acquisition_type";
    // 採番用の項目名称（社内）
    public static final String AD_REQUEST_NUMBER_INTERNAL = "AD_REQUEST_NUMBER_INTERNAL";
    // 採番用の項目名称（社外）
    public static final String AD_REQEUST_NUMBER_EXTERNAL = "AD_REQEUST_NUMBER_EXTERNAL";

    // Excel出力のタイトル
    public static final String EXCEL_EXPORT_TITLE = "excel_export_title";
    // xcel出力のタイトル項目
    public static final String EXCEL_EXPORT_TITLE_ITEM = "excel_export_title_item";
    public static final String SEPARATOR_STR = "\t";
    public static final String LINE_END = "\r\n";
    // 申請フォームー出力のファイル名称
    public static final String FORM_FILE_NAME = "asset_disposal_request";

    // メール送信用のArray
    public String[] sendRequestUuid;
    // メール送信用のFunctionId
    public String sendFunctionId;

    // 資産廃棄管理用選択肢
    public static final String[] ASSET_DISPOSAL_ARRAY = { DISPOSAL_REQUEST_REASON, EOL_CONFIRMATION, DISPOSAL_JUDGMENT,
            RECEIVE_REJECT_REASON, OEM_DESTINATION, AP_DISPOSAL_JUDGMENT, AP_FINAL_BULK_ORDER, AP_REJECT_REASON,
            FINAL_REPLY, FINAL_REJECT_REASON, INTERNAL_STATUS, MST_ASSET_MGMT_REGION, MST_ASSET_ACQUISITION_TYPE };

    // リターン情報を初期化
    long succeededCount = 0;// 成功件数
    long addedCount = 0;// 追加件数
    long updatedCount = 0;// 更新件数
    long failedCount = 0; // 失敗件数

    /**
     * 資産廃棄管理情報更新
     *
     * @param tblAssetDisposalRequestVo
     * @param loginUserId
     * @param loginUserUuid
     * @param langId
     * @param sendFlg(true：メソッド内で送信APIをコール；false：メソッド内で送信APいをコールしない)
     *
     * @return
     * @throws Exception
     */
    @Transactional
    public TblAssetDisposalRequestVo updateTblAssetDisposalRequest(TblAssetDisposalRequestVo tblAssetDisposalRequestVo,
            String loginUserId, String loginUserUuid, String langId, boolean sendFlg) throws Exception {

        TblAssetDisposalRequestVo result = new TblAssetDisposalRequestVo();

        BeanCopyUtil.copyFields(tblAssetDisposalRequestVo, result);

        // 採番フラグを取得する
        String[] getRequestNoFlgStr = AssetDisposalControlConstant.assetDisposalControlConstantMap
                .get(CommonConstants.ASSET_DISPOSAL_REQUEST_REGISTRATION_GET_REQUEST_NO
                        + tblAssetDisposalRequestVo.getFunctionId())
                .split(",");

        boolean getRequestNoFlg = false;

        // 資産廃棄登録の場合、採番が必要の場合
        if ("true".equals(getRequestNoFlgStr[0])) {

            getRequestNoFlg = true;
        }

        // 変更後のステータスを取得
        String controlTypeStr = getControlStr(tblAssetDisposalRequestVo,
                AssetDisposalControlConstant.assetDisposalControlConstantMap.get(
                        CommonConstants.ASSET_DISPOSAL_PROPERTY_CONTROL + tblAssetDisposalRequestVo.getFunctionId()));

        String[] statusStrs = AssetDisposalControlConstant.assetDisposalControlConstantMap
                .get(CommonConstants.ASSET_DISPOSAL_STATUS_CONTROL + tblAssetDisposalRequestVo.getFunctionId()
                        + controlTypeStr)
                .split(",");

        // 変更後のステータスを設定
        tblAssetDisposalRequestVo.setInternalStatus(Integer.valueOf(statusStrs[0]));
        tblAssetDisposalRequestVo.setExternalStatus(Integer.valueOf(statusStrs[1]));

        int count = 1;

        if (null != tblAssetDisposalRequestVo.getAssetNoList()) {

            count = tblAssetDisposalRequestVo.getAssetNoList().size();
        }

        // 送信用のUUID
        String[] sendMailRequestUuid = new String[count];

        // 入力下日付文字列からDateへ変換
        setVoInfoStrToDate(tblAssetDisposalRequestVo);

        TblAssetDisposalRequest insertTblAssetDisposalRequest = new TblAssetDisposalRequest();

        for (int i = 0; i < count; i++) {

            TblAssetDisposalRequest tblAssetDisposalRequest = new TblAssetDisposalRequest();

            BeanCopyUtil.copyFields(tblAssetDisposalRequestVo, tblAssetDisposalRequest);

            Date nowDate = new Date();

            // 資産廃棄申請情報を更新する
            if (i == 0 && StringUtils.isNotEmpty(tblAssetDisposalRequest.getRequestNo())) {

                Query query = entityManager.createNamedQuery("TblAssetDisposalRequest.findByUuid");

                query.setParameter("uuid", tblAssetDisposalRequest.getUuid());

                TblAssetDisposalRequest tempTblAssetDisposalRequest = (TblAssetDisposalRequest) query.getSingleResult();

                int beforeUpdStatus = tempTblAssetDisposalRequest.getInternalStatus();

                // 画面別変更情報を設定
                setUpdateInfo(tblAssetDisposalRequestVo.getFunctionId(), loginUserUuid, tblAssetDisposalRequest,
                        tempTblAssetDisposalRequest, langId);

                // 資産情報を設定
                if (null != tblAssetDisposalRequestVo.getAssetNoList()) {

                    MstAssetVo mstAssetVo = tblAssetDisposalRequestVo.getAssetNoList().get(i);

                    if (null != mstAssetVo) {

                        // 資産番号を設定
                        tempTblAssetDisposalRequest.setAssetNo(mstAssetVo.getAssetNo());
                        // 補助番号を設定
                        tempTblAssetDisposalRequest.setBranchNo(mstAssetVo.getBranchNo());
                    }
                }

                setAssetDisposalRequestUserInfo(tblAssetDisposalRequest, tblAssetDisposalRequestVo.getFunctionId(),
                        loginUserId, loginUserUuid);

                tempTblAssetDisposalRequest.setInternalStatus(tblAssetDisposalRequest.getInternalStatus());
                tempTblAssetDisposalRequest.setExternalStatus(tblAssetDisposalRequest.getExternalStatus());
                tempTblAssetDisposalRequest.setUpdateUserUuid(loginUserUuid);
                tempTblAssetDisposalRequest.setUpdateDate(nowDate);

                entityManager.merge(tempTblAssetDisposalRequest);

                // 外部会社の場合
                if (mstCompanyService.getMstCompanyExistCheckById(tempTblAssetDisposalRequest.getFromCompanyId())) {

                    // 所有会社からアクションにより使用会社のステータスを更新する
                    pushGetExternalAssetDisposalAction(tempTblAssetDisposalRequest);

                }

                // 新規登録用のグラスをコピー
                BeanCopyUtil.copyFields(tempTblAssetDisposalRequest, insertTblAssetDisposalRequest);

                // 送信用の廃棄申請情報のUUIDを設定
                sendMailRequestUuid[i] = tblAssetDisposalRequest.getUuid();

                boolean mstAssetUpdFlg = false;
                int disposalStatus = 0;

                // 更新後のステータスは6:廃棄処理済の場合、資産マスタの廃棄ステータスは1；廃棄済に更新する
                if (CommonConstants.ASSET_DISPOSAL_INTERNAL_STATUS_COMPLETED == tempTblAssetDisposalRequest
                        .getInternalStatus()) {

                    mstAssetUpdFlg = true;
                    disposalStatus = 1;

                }

                // 変更前のステータスは6:廃棄処理済、且つ更新後のステータスは6:廃棄処理済以外の場合、資産マスタの廃棄ステータスは0；未廃棄に更新する
                if (CommonConstants.ASSET_DISPOSAL_INTERNAL_STATUS_COMPLETED == beforeUpdStatus
                        && CommonConstants.ASSET_DISPOSAL_INTERNAL_STATUS_COMPLETED != tempTblAssetDisposalRequest
                                .getInternalStatus()) {

                    mstAssetUpdFlg = true;
                    disposalStatus = 0;

                }

                // 資産マスタに更新する
                if (mstAssetUpdFlg) {

                    MstAsset mstAsset = mstAssetService.getMstAssetByPK(tempTblAssetDisposalRequest.getAssetNo(),
                            tempTblAssetDisposalRequest.getBranchNo());

                    if (null != mstAsset) {

                        mstAsset.setDisposalStatus(disposalStatus);
                        mstAsset.setUpdateDate(nowDate);
                        mstAsset.setUpdateUserUuid(loginUserUuid);

                        entityManager.merge(mstAsset);
                    }
                }

            } else { // 資産廃棄申請情報を登録する

                // 資産情報を設定
                if (null != tblAssetDisposalRequestVo.getAssetNoList()) {

                    MstAssetVo mstAssetVo = tblAssetDisposalRequestVo.getAssetNoList().get(i);

                    if (null != mstAssetVo) {

                        // 資産番号を設定
                        insertTblAssetDisposalRequest.setAssetNo(mstAssetVo.getAssetNo());
                        // 補助番号を設定
                        insertTblAssetDisposalRequest.setBranchNo(mstAssetVo.getBranchNo());
                    }
                }

                insertTblAssetDisposalRequest.setUuid(IDGenerator.generate());

                // 申請番号を採番する
                if (getRequestNoFlg) {

                    String requestNo = tblNumberAssignmentService.findByKey(getRequestNoFlgStr[1]);
                    insertTblAssetDisposalRequest.setRequestNo(requestNo);
                }

                insertTblAssetDisposalRequest.setCreateUserUuid(loginUserUuid);
                insertTblAssetDisposalRequest.setUpdateUserUuid(loginUserUuid);

                insertTblAssetDisposalRequest.setCreateDate(nowDate);
                insertTblAssetDisposalRequest.setUpdateDate(nowDate);

                entityManager.persist(insertTblAssetDisposalRequest);

                // 送信用の廃棄申請情報のUUIDを設定
                sendMailRequestUuid[i] = insertTblAssetDisposalRequest.getUuid();

                // 採番テーブルを更新する
                if (getRequestNoFlg) {

                    tblNumberAssignmentService.updateTblNumberAssignment(getRequestNoFlgStr[1], loginUserUuid);
                }

            }

        }

        if (sendFlg) {

            // 送信制御情報を取得
            sendRequestUuid = sendMailRequestUuid;
            sendFunctionId = tblAssetDisposalRequestVo.getFunctionId();

            // // 送信制御情報を取得
            // chkSendAssetDisposalMailInfo(tblAssetDisposalRequestVo.getFunctionId(),
            // sendMailRequestUuid);
        }

        return result;

    }

    /**
     * 資産廃棄管理情報登録
     *
     * @param tblAssetDisposalRequestVo
     * @param loginUserId
     * @param loginUserUuid
     * @param sendFlg(true：メソッド内で送信APIをコール；false：メソッド内で送信APいをコールしない)
     *
     * @return
     * @throws Exception
     */
    @Transactional
    public TblAssetDisposalRequestVo createTblAssetDisposalRequest(TblAssetDisposalRequestVo tblAssetDisposalRequestVo,
            String loginUserId, String loginUserUuid, boolean sendFlg) throws Exception {

        TblAssetDisposalRequestVo result = new TblAssetDisposalRequestVo();

        // 採番フラグを取得する
        String[] getRequestNoFlgStr = AssetDisposalControlConstant.assetDisposalControlConstantMap
                .get(CommonConstants.ASSET_DISPOSAL_REQUEST_REGISTRATION_GET_REQUEST_NO
                        + tblAssetDisposalRequestVo.getFunctionId())
                .split(",");

        int count = 1;

        if (null != tblAssetDisposalRequestVo.getAssetNoList()) {

            count = tblAssetDisposalRequestVo.getAssetNoList().size();
        }

        boolean getRequestNoFlg = false;

        // 資産廃棄登録の場合、採番が必要の場合
        if ("true".equals(getRequestNoFlgStr[0])) {

            getRequestNoFlg = true;
        }

        // 変更後のステータスを取得
        String controlTypeStr = getControlStr(tblAssetDisposalRequestVo,
                AssetDisposalControlConstant.assetDisposalControlConstantMap.get(
                        CommonConstants.ASSET_DISPOSAL_PROPERTY_CONTROL + tblAssetDisposalRequestVo.getFunctionId()));

        String[] statusStrs = AssetDisposalControlConstant.assetDisposalControlConstantMap
                .get(CommonConstants.ASSET_DISPOSAL_STATUS_CONTROL + tblAssetDisposalRequestVo.getFunctionId()
                        + controlTypeStr)
                .split(",");

        // 変更後のステータスを設定
        tblAssetDisposalRequestVo.setInternalStatus(Integer.valueOf(statusStrs[0]));
        tblAssetDisposalRequestVo.setExternalStatus(Integer.valueOf(statusStrs[1]));

        // 送信用のUUID
        String[] sendMailRequestUuid = new String[count];

        for (int i = 0; i < count; i++) {

            TblAssetDisposalRequest tblAssetDisposalRequest = new TblAssetDisposalRequest();

            // 入力下日付文字列からDateへ変換
            setVoInfoStrToDate(tblAssetDisposalRequestVo);

            BeanCopyUtil.copyFields(tblAssetDisposalRequestVo, tblAssetDisposalRequest);

            if ("".equals(tblAssetDisposalRequest.getRequestNo())) {

                tblAssetDisposalRequest.setRequestNo(null);
            }

            tblAssetDisposalRequest.setUuid(IDGenerator.generate());

            if (i == 0) {
                result.setUuid(tblAssetDisposalRequest.getUuid());
            }

            setAssetDisposalRequestUserInfo(tblAssetDisposalRequest, tblAssetDisposalRequestVo.getFunctionId(),
                    loginUserId, loginUserUuid);

            if (null != tblAssetDisposalRequestVo.getAssetNoList()) {

                MstAssetVo mstAssetVo = tblAssetDisposalRequestVo.getAssetNoList().get(i);

                if (null != mstAssetVo) {

                    // 資産番号を設定
                    tblAssetDisposalRequest.setAssetNo(mstAssetVo.getAssetNo());
                    // 補助番号を設定
                    tblAssetDisposalRequest.setBranchNo(mstAssetVo.getBranchNo());
                }
            }

            // 申請番号を採番する
            if (getRequestNoFlg) {

                tblAssetDisposalRequest.setRequestNo(tblNumberAssignmentService.findByKey(getRequestNoFlgStr[1]));
            }

            tblAssetDisposalRequest.setCreateUserUuid(loginUserUuid);
            tblAssetDisposalRequest.setUpdateUserUuid(loginUserUuid);

            Date nowDate = new Date();

            if (null == tblAssetDisposalRequest.getRequestDate()) {
                tblAssetDisposalRequest.setRequestDate(nowDate);
            }
            tblAssetDisposalRequest.setCreateDate(nowDate);
            tblAssetDisposalRequest.setUpdateDate(nowDate);

            entityManager.persist(tblAssetDisposalRequest);

            // 採番テーブルを更新する
            if (getRequestNoFlg) {

                tblNumberAssignmentService.updateTblNumberAssignment(getRequestNoFlgStr[1], loginUserUuid);
            }

            // 送信用の廃棄申請情報のUUIDを設定
            sendMailRequestUuid[i] = tblAssetDisposalRequest.getUuid();

        }

        if (sendFlg) {

            // 送信制御情報を取得
            sendRequestUuid = sendMailRequestUuid;
            sendFunctionId = tblAssetDisposalRequestVo.getFunctionId();

            // // 送信制御情報を取得
            // chkSendAssetDisposalMailInfo(tblAssetDisposalRequestVo.getFunctionId(),
            // sendMailRequestUuid);
        }

        return result;
    }

    /**
     * 資産廃棄管理詳細画面へ遷移可否をチェックする
     *
     * @param assetDisposalUuid
     * @param functionId
     * @param langId
     *
     * @return
     * @throws Exception
     */
    public BasicResponse chkAssetDisposalDetailJump(String assetDisposalUuid, String functionId, String langId)
            throws Exception {

        BasicResponse response = new BasicResponse();

        Query query = entityManager.createNamedQuery("TblAssetDisposalRequest.findByUuid");

        query.setParameter("uuid", assetDisposalUuid);

        TblAssetDisposalRequest tempTblAssetDisposalRequest = (TblAssetDisposalRequest) query.getSingleResult();

        // 画面遷移フラグ取得用の属性を抽出
        String controlJumpStr = getControlJumpStr(tempTblAssetDisposalRequest,
                AssetDisposalControlConstant.assetDisposalControlConstantMap
                        .get(CommonConstants.ASSET_DISPOSAL_JUMP_PROPERTY_CONTROL + functionId));

        // 画面遷移可否フラグを取得
        List<String> jumpFlgList = Arrays.asList(AssetDisposalControlConstant.assetDisposalControlConstantMap
                .get(CommonConstants.ASSET_DISPOSAL_JUMP_CONTROL + functionId).split(";"));

        boolean checkFlg = false;

        // 遷移可否の判断
        if (jumpFlgList.size() > 0) {

            for (int i = 0; i < jumpFlgList.size(); i++) {

                if (controlJumpStr.equals(jumpFlgList.get(i))) {

                    checkFlg = true;

                    break;
                }

            }

        }

        //
        if (!checkFlg) {

            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_not_editable_status"));
        }

        return response;

    }

    /**
     * 資産廃棄管理情報取得
     *
     * @param requestDateFrom
     * @param requestDateTo
     * @param requestNo
     * @param disposalCompletionBefore
     * @param status
     * @param assetNo
     * @param itemCode
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param assetDisposalUuid
     * @param langId
     * @param toComapanyFlg
     * @param isPage
     * @param isList
     *
     * @return
     */
    public TblAssetDisposalRequestVoList getAssetDisposalList(String requestDateFrom, String requestDateTo,
            String requestNo, int disposalCompletionBefore, int status, String assetNo, String itemCode, String sidx,
            String sord, int pageNumber, int pageSize, String assetDisposalUuid, String langId, Boolean toComapanyFlg,
            boolean isPage, boolean isList) {

        TblAssetDisposalRequestVoList tblAssetDisposalRequestVoList = new TblAssetDisposalRequestVoList();

        List<TblAssetDisposalRequestVo> tblAssetDisposalRequestVos = new ArrayList<TblAssetDisposalRequestVo>();

        if (isPage) {

            List count = getAssetDisposalSql(requestDateFrom, requestDateTo, requestNo, disposalCompletionBefore,
                    status, assetNo, itemCode, sidx, sord, pageNumber, pageSize, assetDisposalUuid, toComapanyFlg, true,
                    isList);

            // ページをめぐる
            Pager pager = new Pager();
            tblAssetDisposalRequestVoList.setPageNumber(pageNumber);
            long counts = (long) count.get(0);
            tblAssetDisposalRequestVoList.setCount(counts);
            tblAssetDisposalRequestVoList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));

        }

        List assetDisposalList = getAssetDisposalSql(requestDateFrom, requestDateTo, requestNo,
                disposalCompletionBefore, status, assetNo, itemCode, sidx, sord, pageNumber, pageSize,
                assetDisposalUuid, toComapanyFlg, false, isList);

        // 検索結果＝0の場合
        if (assetDisposalList.size() == 0) {
            return tblAssetDisposalRequestVoList;
        }

        // 金型・設備区分Map
        Map<Integer, String> moldMachineMap = new HashMap<Integer, String>();

        // 現品有無Map
        Map<Integer, String> existenceMap = new HashMap<Integer, String>();

        // 品目コードVer確認Map
        Map<Integer, String> itemVerMap = new HashMap<Integer, String>();

        // 資産管理部門確認Map
        Map<Integer, String> assetMgmtMap = new HashMap<Integer, String>();

        // 社外ステータスMap
        Map<Integer, String> externalStatusMap = new HashMap<Integer, String>();

        getDictionaryMap(moldMachineMap, existenceMap, itemVerMap, assetMgmtMap, externalStatusMap, langId);

        // 選択肢Map
        Map<String, String> choiceMap = new HashMap<String, String>();

        choiceMap = mstChoiceService.getChoiceMap(langId, ASSET_DISPOSAL_ARRAY);

        for (TblAssetDisposalRequest tblAssetDisposalRequest : (List<TblAssetDisposalRequest>) assetDisposalList) {

            TblAssetDisposalRequestVo tblAssetDisposalRequestVo = new TblAssetDisposalRequestVo();

            BeanCopyUtil.copyFields(tblAssetDisposalRequest, tblAssetDisposalRequestVo);

            setAssetDisposalJoinMstInfo(tblAssetDisposalRequest, tblAssetDisposalRequestVo);

            // 金型・設備区分テキストを設定
            if (tblAssetDisposalRequestVo.getMoldMachineType() > 0) {

                tblAssetDisposalRequestVo
                        .setMoldMachineTypeText(moldMachineMap.get(tblAssetDisposalRequestVo.getMoldMachineType()));
            }

            // 現品有無テキストを設定
            if (tblAssetDisposalRequestVo.getExistence() > 0) {

                tblAssetDisposalRequestVo.setExistenceText(existenceMap.get(tblAssetDisposalRequestVo.getExistence()));
            }

            // 廃棄申請理由テキストを取得
            if (tblAssetDisposalRequestVo.getDisposalRequestReason() > 0) {

                tblAssetDisposalRequestVo.setDisposalRequestReasonText(choiceMap.get(DISPOSAL_REQUEST_REASON
                        + String.valueOf(tblAssetDisposalRequestVo.getDisposalRequestReason())));

            }

            // EOL確認区分を設定
            tblAssetDisposalRequestVo.setEolConfirmationText(
                    choiceMap.get(EOL_CONFIRMATION + String.valueOf(tblAssetDisposalRequestVo.getEolConfirmation())));

            // 受付却下理由を設定
            tblAssetDisposalRequestVo.setReceiveRejectReasonText(choiceMap
                    .get(RECEIVE_REJECT_REASON + String.valueOf(tblAssetDisposalRequestVo.getReceiveRejectReason())));

            // 品目コードVer確認を設定
            tblAssetDisposalRequestVo
                    .setItemVerConfirmationText(itemVerMap.get(tblAssetDisposalRequestVo.getItemVerConfirmation()));

            // 廃棄判断理由を設定
            tblAssetDisposalRequestVo.setDisposalJudgmentText(
                    choiceMap.get(DISPOSAL_JUDGMENT + String.valueOf(tblAssetDisposalRequestVo.getDisposalJudgment())));

            // OEM先を設定
            tblAssetDisposalRequestVo.setOemDestinationText(
                    choiceMap.get(OEM_DESTINATION + String.valueOf(tblAssetDisposalRequestVo.getOemDestination())));

            // 資産管理部門確認を設定
            tblAssetDisposalRequestVo
                    .setAssetMgmtConfirmText(assetMgmtMap.get(tblAssetDisposalRequestVo.getAssetMgmtConfirm()));

            // AP廃棄可否判断を設定
            tblAssetDisposalRequestVo.setApDisposalJudgmentText(choiceMap
                    .get(AP_DISPOSAL_JUDGMENT + String.valueOf(tblAssetDisposalRequestVo.getApDisposalJudgment())));

            // AP最終まとめ発注を設定
            tblAssetDisposalRequestVo.setApFinalBulkOrderText(choiceMap
                    .get(AP_FINAL_BULK_ORDER + String.valueOf(tblAssetDisposalRequestVo.getApFinalBulkOrder())));

            // AP却下理由を設定
            tblAssetDisposalRequestVo.setApRejectReasonText(
                    choiceMap.get(AP_REJECT_REASON + String.valueOf(tblAssetDisposalRequestVo.getApRejectReason())));

            // 最終回答を設定
            tblAssetDisposalRequestVo.setFinalReplyText(
                    choiceMap.get(FINAL_REPLY + String.valueOf(tblAssetDisposalRequestVo.getFinalReply())));

            // 最終却下理由を設定
            tblAssetDisposalRequestVo.setFinalRejectReasonText(choiceMap
                    .get(FINAL_REJECT_REASON + String.valueOf(tblAssetDisposalRequestVo.getFinalRejectReason())));

            // 社内ステータスを設定
            tblAssetDisposalRequestVo.setInternalStatusText(
                    choiceMap.get(INTERNAL_STATUS + String.valueOf(tblAssetDisposalRequestVo.getInternalStatus())));

            // 社外ステータスを設定
            tblAssetDisposalRequestVo
                    .setExternalStatusText(externalStatusMap.get(tblAssetDisposalRequestVo.getExternalStatus()));

            // 管理地域を設定
            tblAssetDisposalRequestVo.setMgmtRegionText(
                    choiceMap.get(MST_ASSET_MGMT_REGION + String.valueOf(tblAssetDisposalRequestVo.getMgmtRegion())));

            // 取得区分を設定
            tblAssetDisposalRequestVo.setAcquisitionTypeText(choiceMap
                    .get(MST_ASSET_ACQUISITION_TYPE + String.valueOf(tblAssetDisposalRequestVo.getAcquisitionType())));

            tblAssetDisposalRequestVos.add(tblAssetDisposalRequestVo);

        }

        tblAssetDisposalRequestVoList.setTblAssetDisposalRequestVos(tblAssetDisposalRequestVos);

        return tblAssetDisposalRequestVoList;

    }

    /**
     * 
     * 申請フォーム出力
     *
     * @param functionId
     * @param langId
     * @param loginUserUuid
     *
     * @return
     * @throws Exception
     */
    public FileReponse getExcelFormExport(String functionId, String langId, String loginUserUuid) throws Exception {

        FileReponse file = new FileReponse();

        WriteExcelList we = new WriteListExcel();
        Map<String, Object> param = new HashMap();
        String uuid = IDGenerator.generate();
        String outExclePath = FileUtil.outExcelFile(kartePropertyService, uuid);

        // パスを設定
        param.put("outFilePath", outExclePath);
        param.put("workbook", new XSSFWorkbook());
        param.put("isConvertWorkbook", false);

        // 申請フォーム出力タイトル制御を取得する
        String[] controlgetFormExportStr = AssetDisposalControlConstant.assetDisposalControlConstantMap
                .get(CommonConstants.ASSET_DISPOSAL_EXCELFORM_TITLE_CONTROL + functionId).split(";");

        /**
         * Header
         */
        List<String> dictKeyList = Arrays.asList(controlgetFormExportStr[1].split(","));
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);

        Class c = Class.forName(controlgetFormExportStr[2]);

        Constructor con = c.getConstructor();

        Object obj = con.newInstance();

        List list = new ArrayList();

        // 申請フォーム出力制御を取得する
        List<String> getFormItemExportList = Arrays.asList(AssetDisposalControlConstant.assetDisposalControlConstantMap
                .get(CommonConstants.ASSET_DISPOSAL_EXCELFORM_CONTROL + functionId).split(","));

        // タイトルをを設定
        for (int i = 0; i < getFormItemExportList.size(); i++) {

            setControlStr(obj, getFormItemExportList.get(i), headerMap.get(dictKeyList.get(i + 1)));
        }

        list.add(obj);

        getExcelFormDate(list, langId);

        try {
            we.write(param, list);
        } catch (Exception e) {
            // return e;
        }

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(controlgetFormExportStr[0]);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(functionId);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUserUuid);
        tblCsvExport.setClientFileName(mstDictionaryService.getDictionaryValue(langId, FORM_FILE_NAME) + ".xlsx");
        tblCsvExportService.createTblCsvExport(tblCsvExport);

        // ファイルのUUIDをリターンする
        file.setFileUuid(uuid);

        return file;

    }

    /**
     * 
     * 申請フォーム取込
     *
     * @param fileUuid
     * @param functionId
     * @param loginUserId
     * @param loginUserUuid
     * @param langId
     *
     * @return
     * @throws Exception
     */
    @Transactional
    public ImportResultResponse postExcelFormImport(String fileUuid, String functionId, String loginUserId,
            String loginUserUuid, String langId) throws Exception {

        ImportResultResponse result = new ImportResultResponse();

        ReadExcel<TblAssetDisposalRequestExcelFormOutPutVo> re = new ReadListExcel<TblAssetDisposalRequestExcelFormOutPutVo>();
        Map<String, Object> param = new HashMap<String, Object>();
        String filePath = FileUtil.getExcelFilePath(kartePropertyService, fileUuid);

        // パスを設定
        param.put("inFilePath", filePath);

        List<?> list = new ArrayList<>();

        list = re.read(param, TblAssetDisposalRequestExcelFormOutPutVo.class);

        // 廃棄送信用のUUID
        List<String> sendMailRequestUuidList = new ArrayList<String>();

        if (list.size() > 0) {

            // ログファイルUUID用意
            String logFileUuid = IDGenerator.generate();
            String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);

            excelFormProcesStart(loginUserId, loginUserUuid, langId, list, logFile, functionId,
                    sendMailRequestUuidList);
            succeededCount = addedCount;
            excelProcesExit(loginUserUuid, langId, fileUuid, list.size(), logFileUuid, functionId);

            // リターン情報
            result.setTotalCount(list.size());
            result.setSucceededCount(succeededCount);
            result.setAddedCount(addedCount);
            result.setUpdatedCount(updatedCount);
            result.setDeletedCount(0);
            result.setFailedCount(failedCount);
            result.setLog(logFileUuid);

        }

        if (sendMailRequestUuidList.size() > 0) {

            String[] sendMailRequestUuid = new String[sendMailRequestUuidList.size()];

            // 送信制御情報を取得
            sendRequestUuid = sendMailRequestUuidList.toArray(sendMailRequestUuid);
            sendFunctionId = functionId + "form";

            // // 送信制御情報を取得
            // chkSendAssetDisposalMailInfo(functionId + "form",
            // sendMailRequestUuidList.toArray(sendMailRequestUuid));

        }

        return result;

    }

    /**
     * 申請フォーム取込処理完了処理
     * 
     * @param loginUserUuid
     * @param langId
     * @param fileUuid
     * @param csvInfoSize
     * @param logFileUuid
     * @param functionId
     */
    private void excelProcesExit(String loginUserUuid, String langId, String fileUuid, int csvInfoSize,
            String logFileUuid, String functionId) {

        // アップロードログをテーブルに書き出し
        TblCsvImport tblCsvImport = new TblCsvImport();
        tblCsvImport.setImportUuid(IDGenerator.generate());
        tblCsvImport.setImportUserUuid(loginUserUuid);
        tblCsvImport.setImportDate(new Date());
        tblCsvImport.setImportTable(CommonConstants.TBL_ASSET_DISPOSAL_REQUEST);
        TblUploadFile tblUploadFile = new TblUploadFile();
        tblUploadFile.setFileUuid(fileUuid);
        tblCsvImport.setUploadFileUuid(tblUploadFile);
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(functionId);
        tblCsvImport.setFunctionId(mstFunction);
        tblCsvImport.setRecordCount(csvInfoSize - 1);
        tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
        tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
        tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
        tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(0)));
        tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
        tblCsvImport.setLogFileUuid(logFileUuid);

        String fileName = mstDictionaryService.getDictionaryValue(langId, CommonConstants.TBL_ASSET_DISPOSAL_REQUEST);
        tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

        tblCsvImportService.createCsvImpor(tblCsvImport);
    }

    /**
     * 
     * Excel取込
     *
     * @param fileUuid
     * @param functionId
     * @param loginUserId
     * @param loginUserUuid
     * @param langId
     *
     * @return
     * @throws Exception
     */
    @Transactional
    public ImportResultResponse postExcelImport(String fileUuid, String functionId, String loginUserId,
            String loginUserUuid, String langId) throws Exception {

        ImportResultResponse result = new ImportResultResponse();

        ReadExcel<TblAssetDisposalRequestExcelFormOutPutVo> re = new ReadListExcel<TblAssetDisposalRequestExcelFormOutPutVo>();
        Map<String, Object> param = new HashMap<String, Object>();
        String filePath = FileUtil.getExcelFilePath(kartePropertyService, fileUuid);

        // パスを設定
        param.put("inFilePath", filePath);

        List<?> list = new ArrayList<>();

        try {

            list = re.read(param, TblAssetDisposalRequestExcelOutPutVo.class);

        } catch (FileNotFoundException e) {

            result.setError(true);
            result.setErrorCode(ErrorMessages.E201_APPLICATION);
            result.setErrorMessage(
                    mstDictionaryService.getDictionaryValue(langId, "msg_error_upload_file_type_invalid"));
            return result;
        }

        // 廃棄送信用のUUID
        List<String> sendMailRequestUuidList = new ArrayList<String>();

        if (list.size() > 0) {

            // ログファイルUUID用意
            String logFileUuid = IDGenerator.generate();
            String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);

            excelProcesStart(loginUserId, loginUserUuid, langId, list, logFile, functionId, sendMailRequestUuidList);
            succeededCount = updatedCount;
            excelProcesExit(loginUserUuid, langId, fileUuid, list.size(), logFileUuid, functionId);

            // リターン情報
            result.setTotalCount(list.size());
            result.setSucceededCount(succeededCount);
            result.setAddedCount(addedCount);
            result.setUpdatedCount(updatedCount);
            result.setDeletedCount(0);
            result.setFailedCount(failedCount);
            result.setLog(logFileUuid);

        }

        if (sendMailRequestUuidList.size() > 0) {

            String[] sendMailRequestUuid = new String[sendMailRequestUuidList.size()];

            // 送信制御情報を取得
            sendRequestUuid = sendMailRequestUuidList.toArray(sendMailRequestUuid);
            sendFunctionId = functionId + "excel";

            // // 送信制御情報を取得
            // chkSendAssetDisposalMailInfo(functionId + "excel",
            // sendMailRequestUuidList.toArray(sendMailRequestUuid));

        }

        return result;

    }

    /**
     * 
     * Excel出力
     *
     * @param functionId
     * @param langId
     * @param loginUserUuid
     *
     * @return
     * @throws Exception
     */
    public FileReponse getExcelExport(String functionId, List<TblAssetDisposalRequestVo> tblAssetDisposalRequestVos,
            String langId, String loginUserUuid) throws Exception {

        FileReponse file = new FileReponse();

        WriteExcelList we = new WriteListExcel();
        Map<String, Object> param = new HashMap();
        String uuid = IDGenerator.generate();
        String outExclePath = FileUtil.outExcelFile(kartePropertyService, uuid);

        // パスを設定
        param.put("outFilePath", outExclePath);
        param.put("workbook", new XSSFWorkbook());
        param.put("isConvertWorkbook", false);

        // Excel出力タイトル制御を取得する
        String[] controlgetFormExportStr = AssetDisposalControlConstant.assetDisposalControlConstantMap
                .get(CommonConstants.ASSET_DISPOSAL_EXCEL_TITLE_CONTROL + EXCEL_EXPORT_TITLE).split(";");

        /**
         * Header
         */
        List<String> dictKeyList = Arrays.asList(controlgetFormExportStr[1].split(","));
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);

        Class c = Class.forName(controlgetFormExportStr[2]);

        Constructor con = c.getConstructor();

        Object obj = con.newInstance();

        List list = new ArrayList();

        // Excel出力制御を取得する
        List<String> getExcelItemExportList = Arrays.asList(AssetDisposalControlConstant.assetDisposalControlConstantMap
                .get(CommonConstants.ASSET_DISPOSAL_EXCEL_ITEM_CONTROL + EXCEL_EXPORT_TITLE_ITEM).split(","));

        // タイトルを設定
        for (int i = 0; i < getExcelItemExportList.size(); i++) {

            setControlStr(obj, getExcelItemExportList.get(i), headerMap.get(dictKeyList.get(i + 1)));
        }

        list.add(obj);

        if (null != tblAssetDisposalRequestVos && tblAssetDisposalRequestVos.size() > 0) {

            // Exceｌ出力データを設定
            getExcelExportData(list, tblAssetDisposalRequestVos, langId);
        }

        try {
            we.write(param, list);
        } catch (Exception e) {
            // return e;
        }

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(controlgetFormExportStr[0]);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(functionId);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUserUuid);
        tblCsvExport.setClientFileName(FileUtil.getExcelFileName(headerMap.get(controlgetFormExportStr[0])));
        tblCsvExportService.createTblCsvExport(tblCsvExport);

        // ファイルのUUIDをリターンする
        file.setFileUuid(uuid);

        return file;

    }

    /**
     * 申請フォーム取込み処理開始
     * 
     * @param loginUser
     * @param csvInfoList
     * @param logFile
     * @param functionId
     * @param sendMailRequestUuidList
     * 
     * @throws Exception
     */
    private void excelFormProcesStart(String loginUserId, String loginUserUuid, String langId, List infoList,
            String logFile, String functionId, List<String> sendMailRequestUuidList) throws Exception {

        FileUtil fileUtil = new FileUtil();

        Map<String, String> excelHeader = getDictValues(langId);
        Map<String, String> excelCheckMsg = getExcelInfoCheckMsg(langId);

        String[] keyArray = { "mold", "machine", "existence_yes", "existence_no" };

        Map<String, String> keyMap = FileUtil.getDictionaryList(mstDictionaryService, langId, Arrays.asList(keyArray));

        // 金型・設備区分
        String moldType = keyMap.get("mold");
        String machineType = keyMap.get("machine");

        // 現品有無
        String existenceYes = keyMap.get("existence_yes");
        String existenceNo = keyMap.get("existence_no");

        for (int i = 0; i < infoList.size(); i++) {

            TblAssetDisposalRequestExcelFormOutPutVo tblAssetDisposalRequestExcelFormOutPutVo = (TblAssetDisposalRequestExcelFormOutPutVo) infoList
                    .get(i);

            // 入力データチェック
            TblAssetDisposalRequestVo ｔblAssetDisposalRequestVo = checkExcelFormInfo(
                    tblAssetDisposalRequestExcelFormOutPutVo, logFile, i + 1, fileUtil, excelHeader, excelCheckMsg,
                    moldType, machineType, existenceYes, existenceNo, langId);

            // チェックエラーなし場合
            if (!ｔblAssetDisposalRequestVo.isError()) {

                ｔblAssetDisposalRequestVo.setFunctionId(functionId + "add");

                // 追加
                TblAssetDisposalRequestVo resultAssetDisposalRequestVo = createTblAssetDisposalRequest(
                        ｔblAssetDisposalRequestVo, loginUserId, loginUserUuid, false);

                addedCount = addedCount + 1;

                sendMailRequestUuidList.add(resultAssetDisposalRequestVo.getUuid());

                // DB操作情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile,
                        fileUtil.outValue(excelCheckMsg.get("rowNumber"), i + 1, excelHeader.get("companyCode"),
                                tblAssetDisposalRequestExcelFormOutPutVo.getFromCompanyId(), excelCheckMsg.get("error"),
                                0, excelCheckMsg.get("dbProcess"), excelCheckMsg.get("msgRecordAdded")));

            }
        }
    }

    /**
     * excel取込み処理開始
     * 
     * @param loginUserId
     * @param loginUserUuid
     * @param langId
     * @param langId
     * @param logFile
     * @param functionId
     * @param functionId
     * @param sendMailRequestUuidList
     * 
     * @throws Exception
     */
    private void excelProcesStart(String loginUserId, String loginUserUuid, String langId, List infoList,
            String logFile, String functionId, List<String> sendMailRequestUuidList) throws Exception {

        FileUtil fileUtil = new FileUtil();

        Map<String, String> excelHeader = getDictValues(langId);
        Map<String, String> excelCheckMsg = getExcelInfoCheckMsg(langId);

        String[] keyArray = { "item_ver_unconfirmed", "item_ver_confirmed", "asset_mgmt_unconfirmed",
                "asset_mgmt_confirmed" };

        Map<String, String> keyMap = FileUtil.getDictionaryList(mstDictionaryService, langId, Arrays.asList(keyArray));

        // 品目コードVer確認M
        String itemVer0 = keyMap.get("item_ver_unconfirmed");
        String itemVer1 = keyMap.get("item_ver_confirmed");

        // 資産管理部門確認
        String assetMgmt0 = keyMap.get("asset_mgmt_unconfirmed");
        String assetMgmt1 = keyMap.get("asset_mgmt_confirmed");

        for (int i = 0; i < infoList.size(); i++) {

            TblAssetDisposalRequestExcelOutPutVo ｔblAssetDisposalRequestExcelOutPutVo = (TblAssetDisposalRequestExcelOutPutVo) infoList
                    .get(i);

            // 入力データチェック
            TblAssetDisposalRequestVo tblAssetDisposalRequestVo = checkExcelInfo(ｔblAssetDisposalRequestExcelOutPutVo,
                    logFile, i + 1, fileUtil, excelHeader, excelCheckMsg, itemVer0, itemVer1, assetMgmt0, assetMgmt1,
                    langId, functionId);

            // チェックエラーなし場合
            if (!tblAssetDisposalRequestVo.isError()) {

                tblAssetDisposalRequestVo.setFunctionId(functionId + "confirm");

                // 変更
                updateTblAssetDisposalRequest(tblAssetDisposalRequestVo, loginUserId, loginUserUuid, langId, false);

                updatedCount = updatedCount + 1;

                sendMailRequestUuidList.add(tblAssetDisposalRequestVo.getUuid());

                // DB操作情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile,
                        fileUtil.outValue(excelCheckMsg.get("rowNumber"), i + 1,
                                excelHeader.get("assetDisposalRequestNo"),
                                ｔblAssetDisposalRequestExcelOutPutVo.getRequestNo(), excelCheckMsg.get("error"), 0,
                                excelCheckMsg.get("dbProcess"), excelCheckMsg.get("msgDataModified")));

            }
        }
    }

    /**
     * 
     * 廃棄データ出力
     *
     * @param functionId
     * @param langId
     * @param loginUserUuid
     *
     * @return
     * @throws Exception
     */
    @Transactional
    public FileReponse getAssetDisposalExport(String functionId, String langId, String loginUserUuid) throws Exception {

        FileReponse fileResponse = new FileReponse();

        List<TblAssetDisposalRequest> tblAssetDisposalRequestList = getAssetDisposalInfoList();

        // データがない場合、エラーメッセージを設定
        if (tblAssetDisposalRequestList.size() == 0) {

            fileResponse.setError(true);
            fileResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            fileResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "mst_error_record_not_found"));

            return fileResponse;
        }

        String uuid = IDGenerator.generate();
        String txtPath = FileUtil.outTxtFile(kartePropertyService, uuid);

        try {
            Writer writer = new FileWriter(txtPath);

            StringBuilder writeStr = new StringBuilder();

            for (TblAssetDisposalRequest detail : tblAssetDisposalRequestList) {

                writeStr.append(detail.getMstAsset().getMstAssetPK().getAssetNo());
                writeStr.append(SEPARATOR_STR);
                writeStr.append(detail.getMstAsset().getMstAssetPK().getBranchNo());
                writeStr.append(SEPARATOR_STR);
                writeStr.append(FileUtil.getStr(detail.getMstAsset().getUsingSection()));
                writeStr.append(SEPARATOR_STR);
                writeStr.append(FileUtil.getStr(detail.getMstAsset().getMgmtCompanyCode()));
                writeStr.append(SEPARATOR_STR);
                writeStr.append(FileUtil.getStr(detail.getMstAsset().getAcquisitionYyyymm()));
                writeStr.append(SEPARATOR_STR);
                writeStr.append(
                        String.valueOf(setNumScale(FileUtil.getNum((detail.getMstAsset().getAcquisitionAmount())))));
                writeStr.append(LINE_END);

            }
            writer.write(writeStr.toString());
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(TblInventoryService.class.getName()).log(Level.SEVERE, null, ex);
        }

        // 廃棄データ出力フラグ更新
        for (TblAssetDisposalRequest tblAssetDisposalRequest : tblAssetDisposalRequestList) {

            Query query = entityManager.createNamedQuery("TblAssetDisposalRequest.findByUuid");

            query.setParameter("uuid", tblAssetDisposalRequest.getUuid());

            TblAssetDisposalRequest tempTblAssetDisposalRequest = (TblAssetDisposalRequest) query.getSingleResult();

            tempTblAssetDisposalRequest.setExportFlg(1);

            Date nowDate = new Date();
            tempTblAssetDisposalRequest.setUpdateDate(nowDate);
            tempTblAssetDisposalRequest.setUpdateUserUuid(loginUserUuid);

            entityManager.merge(tempTblAssetDisposalRequest);
        }

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_ASSET_DISPOSAL_REQUEST);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(functionId);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUserUuid);
        String fileName = mstDictionaryService.getDictionaryValue(langId, "disposal_data");

        tblCsvExport.setClientFileName(FileUtil.getTxtFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        // csvファイルのUUIDをリターンする
        fileResponse.setFileUuid(uuid);

        return fileResponse;

    }

    /**
     * 
     * 廃棄データ再出力
     *
     * @param functionId
     * @param completeDateFrom
     * @param completeDateTo
     * @param langId
     * @param loginUserUuid
     *
     * @return
     * @throws Exception
     */
    public FileReponse getAssetDisposalExportAgain(String functionId, String completeDateFrom, String completeDateTo,
            String langId, String loginUserUuid) throws Exception {

        FileReponse fileResponse = new FileReponse();

        List<TblAssetDisposalRequest> tblAssetDisposalRequestList = getAgainAssetDisposalInfoList(completeDateFrom,
                completeDateTo);

        // データがない場合、エラーメッセージを設定
        if (tblAssetDisposalRequestList.size() == 0) {

            fileResponse.setError(true);
            fileResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            fileResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "mst_error_record_not_found"));

            return fileResponse;
        }

        String uuid = IDGenerator.generate();
        String txtPath = FileUtil.outTxtFile(kartePropertyService, uuid);

        try {
            Writer writer = new FileWriter(txtPath);

            StringBuilder writeStr = new StringBuilder();

            for (TblAssetDisposalRequest detail : tblAssetDisposalRequestList) {

                writeStr.append(detail.getMstAsset().getMstAssetPK().getAssetNo());
                writeStr.append(SEPARATOR_STR);
                writeStr.append(detail.getMstAsset().getMstAssetPK().getBranchNo());
                writeStr.append(SEPARATOR_STR);
                writeStr.append(FileUtil.getStr(detail.getMstAsset().getUsingSection()));
                writeStr.append(SEPARATOR_STR);
                writeStr.append(FileUtil.getStr(detail.getMstAsset().getMgmtCompanyCode()));
                writeStr.append(SEPARATOR_STR);
                writeStr.append(FileUtil.getStr(detail.getMstAsset().getAcquisitionYyyymm()));
                writeStr.append(SEPARATOR_STR);
                writeStr.append(
                        String.valueOf(setNumScale(FileUtil.getNum((detail.getMstAsset().getAcquisitionAmount())))));
                writeStr.append(LINE_END);

            }
            writer.write(writeStr.toString());
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(TblInventoryService.class.getName()).log(Level.SEVERE, null, ex);
        }

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_ASSET_DISPOSAL_REQUEST);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(functionId);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUserUuid);
        String fileName = mstDictionaryService.getDictionaryValue(langId, "disposal_data");

        tblCsvExport.setClientFileName(FileUtil.getTxtFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        // csvファイルのUUIDをリターンする
        fileResponse.setFileUuid(uuid);

        return fileResponse;

    }

    /**
     * 
     * 出力用の廃棄データを取得
     *
     * @return
     */
    private List<TblAssetDisposalRequest> getAssetDisposalInfoList() {

        List<TblAssetDisposalRequest> result = new ArrayList<TblAssetDisposalRequest>();

        String sql = "SELECT t FROM TblAssetDisposalRequest t JOIN FETCH t.mstAsset m WHERE t.internalStatus = 6 AND t.exportFlg = 0 ORDER BY m.mstAssetPK.assetNo";

        Query query = entityManager.createQuery(sql);

        result = query.getResultList();

        return result;
    }

    /**
     * 
     * 再出力用の廃棄データを取得
     *
     * @param completeDateFrom
     * @param completeDateTo
     *
     * @return
     */
    private List<TblAssetDisposalRequest> getAgainAssetDisposalInfoList(String completeDateFrom,
            String completeDateTo) {

        List<TblAssetDisposalRequest> result = new ArrayList<TblAssetDisposalRequest>();

        StringBuilder sql = new StringBuilder(" SELECT tblAssetDisposalRequest");

        // 検索用のSQLを作成する
        sql = sql.append(" FROM TblAssetDisposalRequest tblAssetDisposalRequest"
                + " LEFT JOIN FETCH tblAssetDisposalRequest.mstAsset m"
                + " WHERE tblAssetDisposalRequest.internalStatus = 6 ");

        if (StringUtils.isNotEmpty(completeDateFrom)) {
            sql = sql.append(" AND tblAssetDisposalRequest.disposalProcessingCompletionDate >= :completeDateFrom");
        }
        if (StringUtils.isNotEmpty(completeDateTo)) {
            sql = sql.append(" AND tblAssetDisposalRequest.disposalProcessingCompletionDate <= :completeDateTo ");
        }

        sql.append(" ORDER BY m.mstAssetPK.assetNo ");

        Query query = entityManager.createQuery(sql.toString());

        // 検索用の条件を作成する
        if (StringUtils.isNotEmpty(completeDateFrom)) {
            query.setParameter("completeDateFrom", DateFormat.strToDate(completeDateFrom));
        }

        if (StringUtils.isNotEmpty(completeDateTo)) {
            query.setParameter("completeDateTo", DateFormat.strToDate(completeDateTo));
        }

        result = query.getResultList();

        return result;
    }

    /**
     * CSVヘーダー用
     *
     * @param langId
     * @return
     */
    private Map<String, String> getDictValues(String langId) {

        String[] keyArray = { "company_code", "request_user_name", "request_mail_address", "mold_machine_type",
                "item_code", "item_code2", "disposal_request_existence", "disposal_reason",
                "disposal_request_reason_other", "asset_no", "asset_disposal_request_no", "branch_no",
                "eol_confirmation", "disposal_judgment", "disposal_judgment_reason", "receive_reject_reason",
                "item_ver_confirmation", "oem_destination", "oem_asset_no", "asset_mgmt_confirm",
                "ap_disposal_judgment", "ap_reject_reason", "ap_supply_remaining_month", "ap_final_bulk_order",
                "final_reply", "final_reject_reason", "doc_request_date", "doc_approval_date",
                "disposal_report_sent_date", "disposal_report_receipt_date", "disposal_processing_completion_date",
                "remarks" };

        Map<String, String> dictionaryMap = FileUtil.getDictionaryList(mstDictionaryService, langId,
                Arrays.asList(keyArray));

        Map<String, String> dictMap = new HashMap<>();
        dictMap.put("companyCode", dictionaryMap.get("company_code"));
        dictMap.put("requestUserName", dictionaryMap.get("request_user_name"));
        dictMap.put("requestMailAddress", dictionaryMap.get("request_mail_address"));
        dictMap.put("moldMachineType", dictionaryMap.get("mold_machine_type"));
        dictMap.put("itemCode", dictionaryMap.get("item_code"));
        dictMap.put("itemCode2", dictionaryMap.get("item_code2"));
        dictMap.put("disposalRequestExistence", dictionaryMap.get("disposal_request_existence"));
        dictMap.put("disposalReason", dictionaryMap.get("disposal_reason"));
        dictMap.put("disposalRequestReasonOther", dictionaryMap.get("disposal_request_reason_other"));

        dictMap.put("assetNo", dictionaryMap.get("asset_no"));
        dictMap.put("assetDisposalRequestNo", dictionaryMap.get("asset_disposal_request_no"));
        dictMap.put("branchNo", dictionaryMap.get("branch_no"));
        dictMap.put("eolConfirmation", dictionaryMap.get("eol_confirmation"));
        dictMap.put("disposalJudgment", dictionaryMap.get("disposal_judgment"));
        dictMap.put("disposalJudgmentReason", dictionaryMap.get("disposal_judgment_reason"));
        dictMap.put("receiveRejectReason", dictionaryMap.get("receive_reject_reason"));

        dictMap.put("itemVerConfirmation", dictionaryMap.get("item_ver_confirmation"));
        dictMap.put("oemDestination", dictionaryMap.get("oem_destination"));
        dictMap.put("oemAssetNo", dictionaryMap.get("oem_asset_no"));
        dictMap.put("assetMgmtConfirm", dictionaryMap.get("asset_mgmt_confirm"));

        dictMap.put("apDisposalJudgment", dictionaryMap.get("ap_disposal_judgment"));
        dictMap.put("apRejectReason", dictionaryMap.get("ap_reject_reason"));
        dictMap.put("apSupplyRemainingMonth", dictionaryMap.get("ap_supply_remaining_month"));
        dictMap.put("apFinalBulkOrder", dictionaryMap.get("ap_final_bulk_order"));

        dictMap.put("finalReply", dictionaryMap.get("final_reply"));
        dictMap.put("finalRejectReason", dictionaryMap.get("final_reject_reason"));
        dictMap.put("doRequestDate", dictionaryMap.get("doc_request_date"));
        dictMap.put("docApprovalDate", dictionaryMap.get("doc_approval_date"));
        dictMap.put("disposalReportSentDate", dictionaryMap.get("disposal_report_sent_date"));
        dictMap.put("disposalReportReceiptDate", dictionaryMap.get("disposal_report_receipt_date"));
        dictMap.put("disposalProcessingCompletionDate", dictionaryMap.get("disposal_processing_completion_date"));
        dictMap.put("remarks", dictionaryMap.get("remarks"));

        return dictMap;
    }

    /**
     * Excel Info Check MSG
     *
     * @param langId
     * @return
     */
    private Map<String, String> getExcelInfoCheckMsg(String langId) {

        String[] keyArray = { "row_number", "msg_error_not_null", "msg_error_over_length", "msg_error_not_isnumber",
                "msg_not_valid_mail_address", "error", "error_detail", "mst_error_record_not_found",
                "msg_error_value_invalid", "msg_error_date_format_invalid", "msg_cannot_delete_used_record",
                "db_process", "msg_record_added", "msg_data_modified", "msg_error_data_deleted",
                "msg_error_asset_disposal_doc_approval_order",
                "msg_error_asset_disposal_report_send_order",
                "msg_error_asset_disposal_report_receipent_order",
                "msg_error_asset_disposal_completion_order",
                "msg_error_my_company_duplicate" };

        Map<String, String> dictionaryMap = FileUtil.getDictionaryList(mstDictionaryService, langId,
                Arrays.asList(keyArray));

        Map<String, String> msgMap = new HashMap<>();
        // info
        msgMap.put("rowNumber", dictionaryMap.get("row_number"));
        // error msg
        msgMap.put("msgErrorNotNull", dictionaryMap.get("msg_error_not_null"));
        msgMap.put("msgErrorOverLength", dictionaryMap.get("msg_error_over_length"));
        msgMap.put("msgErrorNotIsnumber", dictionaryMap.get("msg_error_not_isnumber"));
        msgMap.put("msgNotValidMailAddress", dictionaryMap.get("msg_not_valid_mail_address"));
        msgMap.put("error", dictionaryMap.get("error"));
        msgMap.put("errorDetail", dictionaryMap.get("error_detail"));
        msgMap.put("mstErrorRecordNotFound", dictionaryMap.get("mst_error_record_not_found"));
        msgMap.put("msgErrorValueInvalid", dictionaryMap.get("msg_error_value_invalid"));
        msgMap.put("msgErrorDateFormatInvalid", dictionaryMap.get("msg_error_date_format_invalid"));
        msgMap.put("msgCannotDeleteUsedRecord", dictionaryMap.get("msg_cannot_delete_used_record"));
        // db info
        msgMap.put("dbProcess", dictionaryMap.get("db_process"));
        msgMap.put("msgRecordAdded", dictionaryMap.get("msg_record_added"));
        msgMap.put("msgDataModified", dictionaryMap.get("msg_data_modified"));
        msgMap.put("msgErrorDataDeleted", dictionaryMap.get("msg_error_data_deleted"));

        msgMap.put("msg_error_my_company_duplicate", dictionaryMap.get("msg_error_my_company_duplicate"));
        
        msgMap.put("msg_error_asset_disposal_doc_approval_order", dictionaryMap.get("msg_error_asset_disposal_doc_approval_order"));
        msgMap.put("msg_error_asset_disposal_report_send_order", dictionaryMap.get("msg_error_asset_disposal_report_send_order"));
        msgMap.put("msg_error_asset_disposal_report_receipent_order", dictionaryMap.get("msg_error_asset_disposal_report_receipent_order"));
        msgMap.put("msg_error_asset_disposal_completion_order", dictionaryMap.get("msg_error_asset_disposal_completion_order"));

        return msgMap;
    }

    /**
     * CSVの中身に対してチェックを行う
     * 
     * @param tblAssetDisposalRequestExcelFormOutPutVo
     * @param logFile
     * @param index
     * @param fileUtil
     * @param excelHeader
     * @param excelInfoMsg
     * @param moldType
     * @param machineType
     * @param existenceYes
     * @param existenceNo
     * @param langId
     * @return
     */
    private TblAssetDisposalRequestVo checkExcelFormInfo(
            TblAssetDisposalRequestExcelFormOutPutVo tblAssetDisposalRequestExcelFormOutPutVo, String logFile,
            int index, FileUtil fileUtil, Map<String, String> excelHeader, Map<String, String> excelInfoMsg,
            String moldType, String machineType, String existenceYes, String existenceNo, String langId) {

        TblAssetDisposalRequestVo tblAssetDisposalRequestVo = new TblAssetDisposalRequestVo();

        // 会社コードをチェックする
        String companyCode = tblAssetDisposalRequestExcelFormOutPutVo.getFromCompanyId();

        // 必須チェック
        if (chkFileImportNotNull(companyCode, "companyCode", index, fileUtil, logFile, excelInfoMsg, excelHeader)) {

            failedCount = failedCount + 1;

            tblAssetDisposalRequestVo.setError(true);
            return tblAssetDisposalRequestVo;
        }

        // 存在チェック
        MstCompany mstCompany = mstCompanyService.getMstCompanyByCode(companyCode);
        if (null == mstCompany) {

            // エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile,
                    fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get("companyCode"), companyCode,
                            excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                            excelInfoMsg.get("mstErrorRecordNotFound")));

            failedCount = failedCount + 1;

            tblAssetDisposalRequestVo.setError(true);
            return tblAssetDisposalRequestVo;

        }

        // 申請者氏名をチェックする
        String requestUserName = tblAssetDisposalRequestExcelFormOutPutVo.getRequestUserName();

        // 必須チェック
        if (chkFileImportNotNull(requestUserName, "requestUserName", index, fileUtil, logFile, excelInfoMsg,
                excelHeader)) {

            failedCount = failedCount + 1;

            tblAssetDisposalRequestVo.setError(true);
            return tblAssetDisposalRequestVo;
        }

        // 入力サイズチェック
        if (chkFileImportMaxLength(requestUserName, "requestUserName", 100, index, fileUtil, logFile, excelInfoMsg,
                excelHeader)) {

            failedCount = failedCount + 1;

            tblAssetDisposalRequestVo.setError(true);
            return tblAssetDisposalRequestVo;

        }

        // 申請者メールアドレスをチェック
        String requestMailAddress = tblAssetDisposalRequestExcelFormOutPutVo.getRequestMailAddress();

        // 申請者メールアドレスが入力の場合
        if (StringUtils.isNotEmpty(requestMailAddress)) {

            // 入力サイズチェック
            if (chkFileImportMaxLength(requestMailAddress, "requestMailAddress", 100, index, fileUtil, logFile,
                    excelInfoMsg, excelHeader)) {

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;

            }

            // 有効なメールアドレスチェック
            if (chkFileImportNotMail(requestMailAddress, "requestMailAddress", index, fileUtil, logFile, excelInfoMsg,
                    excelHeader)) {

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;

            }

        }

        // 金型・設備区分をチェック
        String moldMachineType = tblAssetDisposalRequestExcelFormOutPutVo.getMoldMachineType();

        // 必須チェック
        if (chkFileImportNotNull(moldMachineType, "moldMachineType", index, fileUtil, logFile, excelInfoMsg,
                excelHeader)) {

            failedCount = failedCount + 1;

            tblAssetDisposalRequestVo.setError(true);
            return tblAssetDisposalRequestVo;
        }

        // 入力範囲チェック
        if (!(moldType.equals(moldMachineType) || machineType.equals(moldMachineType))) {

            // エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile,
                    fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get("moldMachineType"),
                            moldMachineType, excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                            excelInfoMsg.get("msgErrorValueInvalid")));

            failedCount = failedCount + 1;

            tblAssetDisposalRequestVo.setError(true);
            return tblAssetDisposalRequestVo;
        }

        // 部品コードをチェック
        String itemCode = tblAssetDisposalRequestExcelFormOutPutVo.getItemCode();

        // 必須チェック
        if (chkFileImportNotNull(itemCode, "itemCode", index, fileUtil, logFile, excelInfoMsg, excelHeader)) {

            failedCount = failedCount + 1;

            tblAssetDisposalRequestVo.setError(true);
            return tblAssetDisposalRequestVo;
        }

        MstItem mstItem = mstItemService.getSingleItem(itemCode);

        if (null == mstItem) {

            // エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile,
                    fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get("itemCode"), itemCode,
                            excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                            excelInfoMsg.get("mstErrorRecordNotFound")));

            failedCount = failedCount + 1;

            tblAssetDisposalRequestVo.setError(true);
            return tblAssetDisposalRequestVo;

        }

        // 部品コード２をチェック
        String itemCode2 = tblAssetDisposalRequestExcelFormOutPutVo.getItemCode2();

        MstItem mstItem2 = new MstItem();

        if (StringUtils.isNotEmpty(itemCode2)) {

            mstItem2 = mstItemService.getSingleItem(itemCode2);

            if (null == mstItem2) {

                // エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile,
                        fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get("itemCode2"), itemCode2,
                                excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                                excelInfoMsg.get("mstErrorRecordNotFound")));

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;

            }

        }

        // 現品有無をチェック
        String existenceText = tblAssetDisposalRequestExcelFormOutPutVo.getExistenceText();

        // 必須チェック
        if (chkFileImportNotNull(existenceText, "disposalRequestExistence", index, fileUtil, logFile, excelInfoMsg,
                excelHeader)) {

            failedCount = failedCount + 1;

            tblAssetDisposalRequestVo.setError(true);
            return tblAssetDisposalRequestVo;
        }

        // 入力範囲チェック
        if (!(existenceYes.equals(existenceText) || existenceNo.equals(existenceText))) {

            // エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile,
                    fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get("disposalRequestExistence"),
                            existenceText, excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                            excelInfoMsg.get("msgErrorValueInvalid")));

            failedCount = failedCount + 1;

            tblAssetDisposalRequestVo.setError(true);
            return tblAssetDisposalRequestVo;
        }

        // 廃棄理由をチェックする
        String disposalRequestReason = tblAssetDisposalRequestExcelFormOutPutVo.getDisposalRequestReason();

        // 必須チェック
        if (chkFileImportNotNull(disposalRequestReason, "disposalReason", index, fileUtil, logFile, excelInfoMsg,
                excelHeader)) {

            failedCount = failedCount + 1;

            tblAssetDisposalRequestVo.setError(true);
            return tblAssetDisposalRequestVo;
        }

        MstChoice mstChoice = mstChoiceService.findByCategoryANDLangIdAndChoice(DISPOSAL_REQUEST_REASON, langId,
                disposalRequestReason);

        if (null == mstChoice) {

            // エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile,
                    fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get("disposalReason"),
                            disposalRequestReason, excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                            excelInfoMsg.get("msgErrorValueInvalid")));

            failedCount = failedCount + 1;

            tblAssetDisposalRequestVo.setError(true);
            return tblAssetDisposalRequestVo;

        } else {

            // 未選択の場合
            if ("0".equals(mstChoice.getMstChoicePK().getSeq())) {

                // エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile,
                        fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get("disposalReason"),
                                disposalRequestReason, excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                                excelInfoMsg.get("msgErrorNotNull")));

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;
            }

        }

        // 廃棄理由はその他の場合、その他理由をチェックする
        if ("5".equals(mstChoice.getMstChoicePK().getSeq())) {

            String disposalRequestReasonOther = tblAssetDisposalRequestExcelFormOutPutVo
                    .getDisposalRequestReasonOther();

            // 必須チェック
            if (chkFileImportNotNull(disposalRequestReasonOther, "disposalRequestReasonOther", index, fileUtil, logFile,
                    excelInfoMsg, excelHeader)) {

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;
            }

            // 入力サイズチェック
            if (chkFileImportMaxLength(disposalRequestReasonOther, "disposalRequestReasonOther", 256, index, fileUtil,
                    logFile, excelInfoMsg, excelHeader)) {

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;
            }

        }

        tblAssetDisposalRequestVo.setFromCompanyId(mstCompany.getId());
        tblAssetDisposalRequestVo.setRequestUserName(requestUserName);
        tblAssetDisposalRequestVo.setRequestMailAddress(requestMailAddress);

        if (moldType.equals(moldMachineType)) {

            tblAssetDisposalRequestVo.setMoldMachineType(1);

        } else if (machineType.equals(moldMachineType)) {

            tblAssetDisposalRequestVo.setMoldMachineType(2);
        }

        tblAssetDisposalRequestVo.setItemCode(itemCode);
        tblAssetDisposalRequestVo.setItemName(mstItem.getItemName());

        if (StringUtils.isNotEmpty(itemCode2)) {

            tblAssetDisposalRequestVo.setItemCode2(itemCode2);
            tblAssetDisposalRequestVo.setItemName2(mstItem2.getItemName());
        }

        if (existenceYes.equals(existenceText)) {

            tblAssetDisposalRequestVo.setExistence(1);

        } else if (existenceNo.equals(existenceText)) {

            tblAssetDisposalRequestVo.setExistence(2);
        }

        tblAssetDisposalRequestVo.setDisposalRequestReason(Integer.valueOf(mstChoice.getMstChoicePK().getSeq()));

        if (5 == tblAssetDisposalRequestVo.getDisposalRequestReason()) {

            tblAssetDisposalRequestVo.setDisposalRequestReasonOther(
                    tblAssetDisposalRequestExcelFormOutPutVo.getDisposalRequestReasonOther());
        }

        return tblAssetDisposalRequestVo;

    }

    /**
     * Excelの中身に対してチェックを行う
     * 
     * @param tblAssetDisposalRequestExcelOutPutVo
     * @param logFile
     * @param index
     * @param fileUtil
     * @param excelHeader
     * @param excelInfoMsg
     * @param itemVer0
     * @param itemVer1
     * @param assetMgmt0
     * @param assetMgmt1
     * @param langId
     * @return
     * @throws Exception
     */
    private TblAssetDisposalRequestVo checkExcelInfo(
            TblAssetDisposalRequestExcelOutPutVo tblAssetDisposalRequestExcelOutPutVo, String logFile, int index,
            FileUtil fileUtil, Map<String, String> excelHeader, Map<String, String> excelInfoMsg, String itemVer0,
            String itemVer1, String assetMgmt0, String assetMgmt1, String langId, String functionId) throws Exception {

        TblAssetDisposalRequestVo tblAssetDisposalRequestVo = new TblAssetDisposalRequestVo();

        // 申請番号をチェック
        String requestNo = tblAssetDisposalRequestExcelOutPutVo.getRequestNo();

        // 必須チェック
        if (chkFileImportNotNull(requestNo, "assetDisposalRequestNo", index, fileUtil, logFile, excelInfoMsg,
                excelHeader)) {

            failedCount = failedCount + 1;

            tblAssetDisposalRequestVo.setError(true);
            return tblAssetDisposalRequestVo;
        }

        TblAssetDisposalRequest tblAssetDisposalRequest = getAssetDisposalRequestInfo(requestNo);

        if (null != tblAssetDisposalRequest) {

            BasicResponse chkFlg = chkAssetDisposalDetailJump(tblAssetDisposalRequest.getUuid(), functionId + "confirm",
                    langId);

            // 該当画面に対して、編集不可の場合
            if (chkFlg.isError()) {

                // エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile,
                        fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                                excelHeader.get("assetDisposalRequestNo"), requestNo, excelInfoMsg.get("error"), 1,
                                excelInfoMsg.get("errorDetail"), chkFlg.getErrorMessage()));

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;
            }
        }

        if (null == tblAssetDisposalRequest) {

            // エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile,
                    fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get("assetDisposalRequestNo"),
                            requestNo, excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                            excelInfoMsg.get("mstErrorRecordNotFound")));

            failedCount = failedCount + 1;

            tblAssetDisposalRequestVo.setError(true);
            return tblAssetDisposalRequestVo;

        }

        // 資産廃棄UUIDと申請番号を設定
        tblAssetDisposalRequestVo.setUuid(tblAssetDisposalRequest.getUuid());
        tblAssetDisposalRequestVo.setRequestNo(requestNo);

        // 資産廃棄申請受付登録画面編集の場合
        if (functionId.startsWith(CommonConstants.ASSET_DISPOSAL_REQUEST_RECEPTION)) {

            // 資産番号を取得
            String assetNo = tblAssetDisposalRequestExcelOutPutVo.getAssetNo();

            // 必須チェック
            if (chkFileImportNotNull(assetNo, "assetNo", index, fileUtil, logFile, excelInfoMsg, excelHeader)) {

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;
            }

            // 資産補助番号
            String branchNo = tblAssetDisposalRequestExcelOutPutVo.getBranchNo();

            // 必須チェック
            if (chkFileImportNotNull(branchNo, "branchNo", index, fileUtil, logFile, excelInfoMsg, excelHeader)) {

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;
            }

            // 存在チェック
            MstAsset mstAsset = mstAssetService.getSingleAssetByPkAndItemCode(assetNo, branchNo,
                    FileUtil.getStr(tblAssetDisposalRequest.getItemCode()));

            if (null == mstAsset) {

                // エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile,
                        fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                                excelHeader.get("assetNo") + "," + excelHeader.get("branchNo"),
                                assetNo + "," + branchNo, excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                                excelInfoMsg.get("mstErrorRecordNotFound")));

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;

            }

            // Eol確認
            String eolConfirmationText = tblAssetDisposalRequestExcelOutPutVo.getEolConfirmationText();

            // 必須チェック
            if (chkFileImportNotNull(eolConfirmationText, "eolConfirmation", index, fileUtil, logFile, excelInfoMsg,
                    excelHeader)) {

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;
            }

            MstChoice eolConfirmationChoice = mstChoiceService.findByCategoryANDLangIdAndChoice(EOL_CONFIRMATION,
                    langId, eolConfirmationText);

            // 入力範囲チェック
            if (null == eolConfirmationChoice) {

                // エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile,
                        fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get("eolConfirmation"),
                                eolConfirmationText, excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                                excelInfoMsg.get("msgErrorValueInvalid")));

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;

            } else {

                // 未選択の場合
                if ("0".equals(eolConfirmationChoice.getMstChoicePK().getSeq())) {

                    // エラー情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile,
                            fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get("eolConfirmation"),
                                    eolConfirmationText, excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                                    excelInfoMsg.get("msgErrorNotNull")));

                    failedCount = failedCount + 1;

                    tblAssetDisposalRequestVo.setError(true);
                    return tblAssetDisposalRequestVo;
                }

            }

            // 廃棄可否判断
            String disposalJudgmentText = tblAssetDisposalRequestExcelOutPutVo.getDisposalJudgmentText();

            // 必須チェック
            if (chkFileImportNotNull(disposalJudgmentText, "disposalJudgment", index, fileUtil, logFile, excelInfoMsg,
                    excelHeader)) {

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;
            }

            MstChoice disposalJudgmentChoice = mstChoiceService.findByCategoryANDLangIdAndChoice(DISPOSAL_JUDGMENT,
                    langId, disposalJudgmentText);

            // 入力範囲チェック
            if (null == disposalJudgmentChoice) {

                // エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile,
                        fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get("disposalJudgment"),
                                disposalJudgmentText, excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                                excelInfoMsg.get("msgErrorValueInvalid")));

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;

            } else {

                // 未選択の場合
                if ("0".equals(disposalJudgmentChoice.getMstChoicePK().getSeq())) {

                    // エラー情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile,
                            fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get("disposalJudgment"),
                                    disposalJudgmentText, excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                                    excelInfoMsg.get("msgErrorNotNull")));

                    failedCount = failedCount + 1;

                    tblAssetDisposalRequestVo.setError(true);
                    return tblAssetDisposalRequestVo;
                }

            }

            // 廃棄可否判断理由
            String judgmentReason = tblAssetDisposalRequestExcelOutPutVo.getJudgmentReason();

            // 必須チェック
            if (chkFileImportNotNull(judgmentReason, "disposalJudgmentReason", index, fileUtil, logFile, excelInfoMsg,
                    excelHeader)) {

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;
            }

            // 入力サイズチェック
            if (chkFileImportMaxLength(judgmentReason, "disposalJudgmentReason", 256, index, fileUtil, logFile,
                    excelInfoMsg, excelHeader)) {

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;
            }

            MstChoice receiveRejectReasonChoice = new MstChoice();

            // 廃棄判定は却下の場合
            if ("2".equals(disposalJudgmentChoice.getMstChoicePK().getSeq())) {

                // 受付却下理由
                String receiveRejectReasonText = tblAssetDisposalRequestExcelOutPutVo.getReceiveRejectReasonText();

                // 必須チェック
                if (chkFileImportNotNull(receiveRejectReasonText, "receiveRejectReason", index, fileUtil, logFile,
                        excelInfoMsg, excelHeader)) {

                    failedCount = failedCount + 1;

                    tblAssetDisposalRequestVo.setError(true);
                    return tblAssetDisposalRequestVo;
                }

                receiveRejectReasonChoice = mstChoiceService.findByCategoryANDLangIdAndChoice(RECEIVE_REJECT_REASON,
                        langId, receiveRejectReasonText);

                // 入力範囲チェック
                if (null == receiveRejectReasonChoice) {

                    // エラー情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                            excelHeader.get("receiveRejectReason"), receiveRejectReasonText, excelInfoMsg.get("error"),
                            1, excelInfoMsg.get("errorDetail"), excelInfoMsg.get("msgErrorValueInvalid")));

                    failedCount = failedCount + 1;

                    tblAssetDisposalRequestVo.setError(true);
                    return tblAssetDisposalRequestVo;
                } else {

                    // 未選択の場合
                    if ("0".equals(receiveRejectReasonChoice.getMstChoicePK().getSeq())) {

                        // エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile,
                                fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                                        excelHeader.get("receiveRejectReason"), receiveRejectReasonText,
                                        excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                                        excelInfoMsg.get("msgErrorNotNull")));

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;
                    }

                }
            }

            // 更新用の廃棄情報を設定
            tblAssetDisposalRequestVo.setAssetNo(assetNo);
            tblAssetDisposalRequestVo.setBranchNo(branchNo);

            // 資産番号と補助番号を設定する
            List<MstAssetVo> assetNoList = new ArrayList<MstAssetVo>();
            MstAssetVo mstAssetVo = new MstAssetVo();
            mstAssetVo.setAssetNo(assetNo);
            mstAssetVo.setBranchNo(branchNo);
            assetNoList.add(mstAssetVo);
            tblAssetDisposalRequestVo.setAssetNoList(assetNoList);

            tblAssetDisposalRequestVo
                    .setEolConfirmation(Integer.valueOf(eolConfirmationChoice.getMstChoicePK().getSeq()));
            tblAssetDisposalRequestVo
                    .setDisposalJudgment(Integer.valueOf(disposalJudgmentChoice.getMstChoicePK().getSeq()));
            tblAssetDisposalRequestVo.setJudgmentReason(judgmentReason);

            // 却下の場合
            if (2 == tblAssetDisposalRequestVo.getDisposalJudgment()) {
                tblAssetDisposalRequestVo
                        .setReceiveRejectReason(Integer.valueOf(receiveRejectReasonChoice.getMstChoicePK().getSeq()));
            }

        } else if (functionId.startsWith(CommonConstants.ASSET_DISPOSAL_REQUEST_CONFIRMATION)) {// 資産廃棄申請確認登録画面編集の場合

            // 資産管理部門確認
            String assetMgmtConfirmText = FileUtil
                    .getStr(tblAssetDisposalRequestExcelOutPutVo.getAssetMgmtConfirmText());

            // 品目コードVer確認
            String itemVerConfirmationText = tblAssetDisposalRequestExcelOutPutVo.getItemVerConfirmationText();

            MstChoice oemDestinationChoice = new MstChoice();

            // 資産管理部門確認済の場合
            if (assetMgmt1.equals(assetMgmtConfirmText)) {

                // 必須チェック
                if (chkFileImportNotNull(itemVerConfirmationText, "itemVerConfirmation", index, fileUtil, logFile,
                        excelInfoMsg, excelHeader)) {

                    failedCount = failedCount + 1;

                    tblAssetDisposalRequestVo.setError(true);
                    return tblAssetDisposalRequestVo;
                }

                // 入力範囲チェック
                if (!itemVer1.equals(itemVerConfirmationText)) {

                    // エラー情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                            excelHeader.get("itemVerConfirmation"), itemVerConfirmationText, excelInfoMsg.get("error"),
                            1, excelInfoMsg.get("errorDetail"), excelInfoMsg.get("msgErrorValueInvalid")));

                    failedCount = failedCount + 1;

                    tblAssetDisposalRequestVo.setError(true);
                    return tblAssetDisposalRequestVo;
                }

                // OEM先
                String oemDestinationText = tblAssetDisposalRequestExcelOutPutVo.getOemDestinationText();

                // 必須チェック
                if (chkFileImportNotNull(oemDestinationText, "oemDestination", index, fileUtil, logFile, excelInfoMsg,
                        excelHeader)) {

                    failedCount = failedCount + 1;

                    tblAssetDisposalRequestVo.setError(true);
                    return tblAssetDisposalRequestVo;
                }

                oemDestinationChoice = mstChoiceService.findByCategoryANDLangIdAndChoice(OEM_DESTINATION, langId,
                        oemDestinationText);

                // 入力範囲チェック
                if (null == oemDestinationChoice) {

                    // エラー情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile,
                            fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get("oemDestination"),
                                    oemDestinationText, excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                                    excelInfoMsg.get("msgErrorValueInvalid")));

                    failedCount = failedCount + 1;

                    tblAssetDisposalRequestVo.setError(true);
                    return tblAssetDisposalRequestVo;
                } 

                // OEM先はOEMの場合
                if ("1".equals(oemDestinationChoice.getMstChoicePK().getSeq())) {

                    // OEM資産番号
                    String oemAssetNo = tblAssetDisposalRequestExcelOutPutVo.getOemAssetNo();

                    // 必須チェック
                    /*if (chkFileImportNotNull(oemAssetNo, "oemAssetNo", index, fileUtil, logFile, excelInfoMsg,
                            excelHeader)) {

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;
                    }*/

                    // 入力サイズチェック
                    if (chkFileImportMaxLength(oemAssetNo, "oemAssetNo", 45, index, fileUtil, logFile, excelInfoMsg,
                            excelHeader)) {

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;

                    }

                }
            }

            // 必須チェック
            if (chkFileImportNotNull(assetMgmtConfirmText, "assetMgmtConfirm", index, fileUtil, logFile, excelInfoMsg,
                    excelHeader)) {

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;
            }

            // 入力範囲チェック
            if (!(assetMgmt0.equals(assetMgmtConfirmText) || assetMgmt1.equals(assetMgmtConfirmText))) {

                // エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile,
                        fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get("assetMgmtConfirm"),
                                assetMgmtConfirmText, excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                                excelInfoMsg.get("msgErrorValueInvalid")));

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;
            }

            // 更新用の廃棄情報を設定
            if (itemVer0.equals(itemVerConfirmationText)) {

                tblAssetDisposalRequestVo.setItemVerConfirmation(0);
            } else if (itemVer1.equals(itemVerConfirmationText)) {

                tblAssetDisposalRequestVo.setItemVerConfirmation(1);

            }

            if (assetMgmt0.equals(assetMgmtConfirmText)) {

                tblAssetDisposalRequestVo.setAssetMgmtConfirm(0);
            } else if (assetMgmt1.equals(assetMgmtConfirmText)) {

                tblAssetDisposalRequestVo.setAssetMgmtConfirm(1);

                // 更新用の廃棄情報を設定
                if (itemVer0.equals(itemVerConfirmationText)) {

                    tblAssetDisposalRequestVo.setItemVerConfirmation(0);
                } else if (itemVer1.equals(itemVerConfirmationText)) {

                    tblAssetDisposalRequestVo.setItemVerConfirmation(1);

                }

                tblAssetDisposalRequestVo
                        .setOemDestination(Integer.valueOf(oemDestinationChoice.getMstChoicePK().getSeq()));

                // OEM先はOEMの場合
                if (1 == tblAssetDisposalRequestVo.getOemDestination()) {

                    tblAssetDisposalRequestVo.setOemAssetNo(tblAssetDisposalRequestExcelOutPutVo.getOemAssetNo());
                }

            }

        } else if (functionId.startsWith(CommonConstants.ASSET_DISPOSAL_REQUEST_AP_CONFIRMATION)) {// 資産廃棄申請AP確認登録画面編集の場合

            // AP廃棄可否判断
            String apDisposalJudgmentText = tblAssetDisposalRequestExcelOutPutVo.getApDisposalJudgmentText();

            // 必須チェック
            if (chkFileImportNotNull(apDisposalJudgmentText, "apDisposalJudgment", index, fileUtil, logFile,
                    excelInfoMsg, excelHeader)) {

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;
            }

            MstChoice apDisposalJudgmentChoice = mstChoiceService.findByCategoryANDLangIdAndChoice(AP_DISPOSAL_JUDGMENT,
                    langId, apDisposalJudgmentText);

            // 入力範囲チェック
            if (null == apDisposalJudgmentChoice) {

                // エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile,
                        fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get("apDisposalJudgment"),
                                apDisposalJudgmentText, excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                                excelInfoMsg.get("msgErrorValueInvalid")));

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;
            }

            MstChoice apRejectReasonChoice = new MstChoice();

            // AP廃棄判定は却下の場合
            if ("2".equals(apDisposalJudgmentChoice.getMstChoicePK().getSeq())) {

                // AP却下理由
                String apRejectReasonText = tblAssetDisposalRequestExcelOutPutVo.getApRejectReasonText();

                // 必須チェック
                if (chkFileImportNotNull(apRejectReasonText, "apRejectReason", index, fileUtil, logFile, excelInfoMsg,
                        excelHeader)) {

                    failedCount = failedCount + 1;

                    tblAssetDisposalRequestVo.setError(true);
                    return tblAssetDisposalRequestVo;
                }

                apRejectReasonChoice = mstChoiceService.findByCategoryANDLangIdAndChoice(AP_DISPOSAL_JUDGMENT, langId,
                        apRejectReasonText);

                // 入力範囲チェック
                if (null == apRejectReasonChoice) {

                    // エラー情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile,
                            fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get("apRejectReason"),
                                    apRejectReasonText, excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                                    excelInfoMsg.get("msgErrorValueInvalid")));

                    failedCount = failedCount + 1;

                    tblAssetDisposalRequestVo.setError(true);
                    return tblAssetDisposalRequestVo;
                } else {

                    // 未選択の場合
                    if ("0".equals(apRejectReasonChoice.getMstChoicePK().getSeq())) {

                        // エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                                excelHeader.get("apRejectReason"), apRejectReasonText, excelInfoMsg.get("error"), 1,
                                excelInfoMsg.get("errorDetail"), excelInfoMsg.get("msgErrorNotNull")));

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;
                    }

                }
            }

            // AP供給期限残月数
            String apSupplyRemainingMonth = tblAssetDisposalRequestExcelOutPutVo.getApSupplyRemainingMonth();

            MstChoice apFinalBulkOrderChoice = new MstChoice();

            if (!"0".equals(apDisposalJudgmentChoice.getMstChoicePK().getSeq())) {

                // 必須チェック
                if (chkFileImportNotNull(apSupplyRemainingMonth, "apSupplyRemainingMonth", index, fileUtil, logFile,
                        excelInfoMsg, excelHeader)) {

                    failedCount = failedCount + 1;

                    tblAssetDisposalRequestVo.setError(true);
                    return tblAssetDisposalRequestVo;
                }

                // 数字チェック
                if (!FileUtil.isInteger(apSupplyRemainingMonth)) {

                    // エラー情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile,
                            fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                                    excelHeader.get("apSupplyRemainingMonth"), apSupplyRemainingMonth,
                                    excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                                    excelInfoMsg.get("msgErrorNotIsnumber")));

                    failedCount = failedCount + 1;

                    tblAssetDisposalRequestVo.setError(true);
                    return tblAssetDisposalRequestVo;

                }

                // 入力サイズチェック
                if (chkFileImportMaxLength(apSupplyRemainingMonth, "apSupplyRemainingMonth", 9, index, fileUtil,
                        logFile, excelInfoMsg, excelHeader)) {

                    failedCount = failedCount + 1;

                    tblAssetDisposalRequestVo.setError(true);
                    return tblAssetDisposalRequestVo;

                }

                // AP最終まとめ発注
                String apFinalBulkOrderText = tblAssetDisposalRequestExcelOutPutVo.getApFinalBulkOrderText();

                // 必須チェック
                if (chkFileImportNotNull(apFinalBulkOrderText, "apFinalBulkOrder", index, fileUtil, logFile,
                        excelInfoMsg, excelHeader)) {

                    failedCount = failedCount + 1;

                    tblAssetDisposalRequestVo.setError(true);
                    return tblAssetDisposalRequestVo;
                }

                apFinalBulkOrderChoice = mstChoiceService.findByCategoryANDLangIdAndChoice(AP_FINAL_BULK_ORDER, langId,
                        apFinalBulkOrderText);

                // 入力範囲チェック
                if (null == apFinalBulkOrderChoice) {

                    // エラー情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile,
                            fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get("apFinalBulkOrder"),
                                    apFinalBulkOrderText, excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                                    excelInfoMsg.get("msgErrorValueInvalid")));

                    failedCount = failedCount + 1;

                    tblAssetDisposalRequestVo.setError(true);
                    return tblAssetDisposalRequestVo;
                } else {

                    // 未選択の場合
                    if ("0".equals(apFinalBulkOrderChoice.getMstChoicePK().getSeq())) {

                        // エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                                excelHeader.get("apFinalBulkOrder"), apFinalBulkOrderText, excelInfoMsg.get("error"), 1,
                                excelInfoMsg.get("errorDetail"), excelInfoMsg.get("msgErrorNotNull")));

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;
                    }

                }
            }

            // 更新用の廃棄情報を設定
            tblAssetDisposalRequestVo
                    .setApDisposalJudgment(Integer.valueOf(apDisposalJudgmentChoice.getMstChoicePK().getSeq()));

            // 却下の場合
            if (2 == tblAssetDisposalRequestVo.getApDisposalJudgment()) {
                tblAssetDisposalRequestVo
                        .setApRejectReason(Integer.valueOf(apRejectReasonChoice.getMstChoicePK().getSeq()));

            } else if (1 == tblAssetDisposalRequestVo.getApDisposalJudgment()) {

                tblAssetDisposalRequestVo.setApSupplyRemainingMonth(Integer.valueOf(apSupplyRemainingMonth));
                tblAssetDisposalRequestVo
                        .setApFinalBulkOrder(Integer.valueOf(apFinalBulkOrderChoice.getMstChoicePK().getSeq()));
            }

        } else if (functionId.startsWith(CommonConstants.ASSET_DISPOSAL_PROCESSING)) {// 資産廃棄処理登録画面編集の場合

            // 最終回答
            String finalReplyText = tblAssetDisposalRequestExcelOutPutVo.getFinalReplyText();

            // 必須チェック
            if (chkFileImportNotNull(finalReplyText, "finalReply", index, fileUtil, logFile, excelInfoMsg,
                    excelHeader)) {

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;
            }

            MstChoice finalReplyChoice = mstChoiceService.findByCategoryANDLangIdAndChoice(FINAL_REPLY, langId,
                    finalReplyText);

            // 入力範囲チェック
            if (null == finalReplyChoice) {

                // エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile,
                        fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get("finalReply"),
                                finalReplyText, excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                                excelInfoMsg.get("msgErrorValueInvalid")));

                failedCount = failedCount + 1;

                tblAssetDisposalRequestVo.setError(true);
                return tblAssetDisposalRequestVo;
            }

            MstChoice finalRejectReasonChoice = new MstChoice();

            // 最終回答は却下の場合
            if ("2".equals(finalReplyChoice.getMstChoicePK().getSeq())) {

                // 最終回答却下理由
                String finalRejectReasonText = tblAssetDisposalRequestExcelOutPutVo.getFinalRejectReasonText();

                // 必須チェック
                if (chkFileImportNotNull(finalRejectReasonText, "finalRejectReason", index, fileUtil, logFile,
                        excelInfoMsg, excelHeader)) {

                    failedCount = failedCount + 1;

                    tblAssetDisposalRequestVo.setError(true);
                    return tblAssetDisposalRequestVo;
                }

                finalRejectReasonChoice = mstChoiceService.findByCategoryANDLangIdAndChoice(FINAL_REJECT_REASON, langId,
                        finalRejectReasonText);

                // 入力範囲チェック
                if (null == finalRejectReasonChoice) {

                    // エラー情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                            excelHeader.get("finalRejectReason"), finalRejectReasonText, excelInfoMsg.get("error"), 1,
                            excelInfoMsg.get("errorDetail"), excelInfoMsg.get("msgErrorValueInvalid")));

                    failedCount = failedCount + 1;

                    tblAssetDisposalRequestVo.setError(true);
                    return tblAssetDisposalRequestVo;
                } else {

                    // 未選択の場合
                    if ("0".equals(finalRejectReasonChoice.getMstChoicePK().getSeq())) {

                        // エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                                excelHeader.get("finalRejectReason"), finalRejectReasonText, excelInfoMsg.get("error"),
                                1, excelInfoMsg.get("errorDetail"), excelInfoMsg.get("msgErrorNotNull")));

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;
                    }

                }

            }

            // 最終回答は廃棄の場合
            if ("1".equals(finalReplyChoice.getMstChoicePK().getSeq())) {

                // 廃棄処理完了日
                String disposalProcessingCompletionDateStr = tblAssetDisposalRequestExcelOutPutVo
                        .getDisposalProcessingCompletionDateStr();

                if (StringUtils.isNotEmpty(disposalProcessingCompletionDateStr)) {

                    // 稟議書申請日
                    String docRequestDateStr = tblAssetDisposalRequestExcelOutPutVo.getDocRequestDateStr();

                    // 必須チェック
                    if (chkFileImportNotNull(docRequestDateStr, "doRequestDate", index, fileUtil, logFile, excelInfoMsg,
                            excelHeader)) {

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;
                    }

                    // 有効な日付チェック
                    if (!FileUtil.isValidDate(docRequestDateStr)) {

                        // エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                                excelHeader.get("doRequestDate"), docRequestDateStr, excelInfoMsg.get("error"), 1,
                                excelInfoMsg.get("errorDetail"), excelInfoMsg.get("msgErrorDateFormatInvalid")));

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;

                    }
                    
                    // 稟議書承認日
                    String docApprovalDateStr = tblAssetDisposalRequestExcelOutPutVo.getDocApprovalDateStr();

                    // 必須チェック
                    if (chkFileImportNotNull(docApprovalDateStr, "docApprovalDate", index, fileUtil, logFile,
                            excelInfoMsg, excelHeader)) {

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;
                    }

                    // 有効な日付チェック
                    if (!FileUtil.isValidDate(docApprovalDateStr)) {

                        // エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                                excelHeader.get("docApprovalDate"), docApprovalDateStr, excelInfoMsg.get("error"), 1,
                                excelInfoMsg.get("errorDetail"), excelInfoMsg.get("msgErrorDateFormatInvalid")));

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;

                    }

                    /**
                     * KM-405 関係チェック*
                     */
                    // A > Bのとき dict_key: msg_error_asset_disposal_doc_approval_order
                    if (diffCheckDate(strToDate(docRequestDateStr), strToDate(docApprovalDateStr))) {
                        // エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                                excelHeader.get("docApprovalDate"),
                                docApprovalDateStr, excelInfoMsg.get("error"), 1,
                                excelInfoMsg.get("errorDetail"), excelInfoMsg.get("msg_error_asset_disposal_doc_approval_order")));

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;
                    }
                    
                    // 廃棄報告書送付日
                    String disposalReportSentDateStr = tblAssetDisposalRequestExcelOutPutVo
                            .getDisposalReportSentDateStr();

                    // 必須チェック
                    if (chkFileImportNotNull(disposalReportSentDateStr, "disposalReportSentDate", index, fileUtil,
                            logFile, excelInfoMsg, excelHeader)) {

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;
                    }

                    // 有効な日付チェック
                    if (!FileUtil.isValidDate(disposalReportSentDateStr)) {

                        // エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile,
                                fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                                        excelHeader.get("disposalReportSentDate"), disposalReportSentDateStr,
                                        excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                                        excelInfoMsg.get("msgErrorDateFormatInvalid")));

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;

                    }
                    
                    /**
                     * KM-405 関係チェック*
                     */
                    // B > Cのとき 下にエラーメッセージ dict_key: msg_error_asset_disposal_report_send_order
                    if (diffCheckDate(strToDate(docApprovalDateStr), strToDate(disposalReportSentDateStr))) {
                        // エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                                excelHeader.get("disposalReportSentDate"),
                                disposalReportSentDateStr, excelInfoMsg.get("error"), 1,
                                excelInfoMsg.get("errorDetail"), excelInfoMsg.get("msg_error_asset_disposal_report_send_order")));

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;
                    }

                    // 廃棄報告書受領日
                    String disposalReportReceiptDateStr = tblAssetDisposalRequestExcelOutPutVo
                            .getDisposalReportReceiptDateStr();

                    // 必須チェック
                    if (chkFileImportNotNull(disposalReportReceiptDateStr, "disposalReportReceiptDate", index, fileUtil,
                            logFile, excelInfoMsg, excelHeader)) {

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;
                    }

                    // 有効な日付チェック
                    if (!FileUtil.isValidDate(disposalReportReceiptDateStr)) {

                        // エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile,
                                fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                                        excelHeader.get("disposalReportReceiptDate"), disposalReportReceiptDateStr,
                                        excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                                        excelInfoMsg.get("msgErrorDateFormatInvalid")));

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;

                    }
                    
                    /**
                     * KM-405 関係チェック*
                     */
                    // C > Dのとき Dの下にエラーメッセージdict_key: msg_error_asset_disposal_report_receipent_order
                    if (diffCheckDate(strToDate(disposalReportSentDateStr), strToDate(disposalReportReceiptDateStr))) {
                        // エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                                excelHeader.get("disposalReportReceiptDate"),
                                disposalReportReceiptDateStr, excelInfoMsg.get("error"), 1,
                                excelInfoMsg.get("errorDetail"), excelInfoMsg.get("msg_error_asset_disposal_report_receipent_order")));

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;
                    }
                    
                    // 廃棄処理完了日
                    // 有効な日付チェック
                    if (!FileUtil.isValidDate(disposalProcessingCompletionDateStr)) {

                        // エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                                excelHeader.get("disposalProcessingCompletionDate"),
                                disposalProcessingCompletionDateStr, excelInfoMsg.get("error"), 1,
                                excelInfoMsg.get("errorDetail"), excelInfoMsg.get("msgErrorDateFormatInvalid")));

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;

                    }
                    
                    /**
                     * KM-405 関係チェック*
                     */
                    // D > Eのとき Dの下にエラーメッセージdict_key: msg_error_asset_disposal_completion_order
                    if (diffCheckDate(strToDate(disposalReportReceiptDateStr), strToDate(disposalProcessingCompletionDateStr))) {
                        // エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                                excelHeader.get("disposalProcessingCompletionDate"),
                                disposalProcessingCompletionDateStr, excelInfoMsg.get("error"), 1,
                                excelInfoMsg.get("errorDetail"), excelInfoMsg.get("msg_error_asset_disposal_completion_order")));

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;
                    }
                    
                } else {

                    // 稟議書申請日
                    String docRequestDateStr = tblAssetDisposalRequestExcelOutPutVo.getDocRequestDateStr();

                    // 有効な日付チェック
                    if (StringUtils.isNotEmpty(docRequestDateStr) && !FileUtil.isValidDate(docRequestDateStr)) {

                        // エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                                excelHeader.get("doRequestDate"), docRequestDateStr, excelInfoMsg.get("error"), 1,
                                excelInfoMsg.get("errorDetail"), excelInfoMsg.get("msgErrorDateFormatInvalid")));

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;

                    }

                    // 稟議書承認日
                    String docApprovalDateStr = tblAssetDisposalRequestExcelOutPutVo.getDocApprovalDateStr();

                    // 有効な日付チェック
                    if (StringUtils.isNotEmpty(docApprovalDateStr) && !FileUtil.isValidDate(docApprovalDateStr)) {

                        // エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                                excelHeader.get("docApprovalDate"), docApprovalDateStr, excelInfoMsg.get("error"), 1,
                                excelInfoMsg.get("errorDetail"), excelInfoMsg.get("msgErrorDateFormatInvalid")));

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;

                    }

                    // 廃棄報告書送付日
                    String disposalReportSentDateStr = tblAssetDisposalRequestExcelOutPutVo
                            .getDisposalReportSentDateStr();

                    // 有効な日付チェック
                    if (StringUtils.isNotEmpty(disposalReportSentDateStr)
                            && !FileUtil.isValidDate(disposalReportSentDateStr)) {

                        // エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile,
                                fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                                        excelHeader.get("disposalReportSentDate"), disposalReportSentDateStr,
                                        excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                                        excelInfoMsg.get("msgErrorDateFormatInvalid")));

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;

                    }

                    // 廃棄報告書受領日
                    String disposalReportReceiptDateStr = tblAssetDisposalRequestExcelOutPutVo
                            .getDisposalReportReceiptDateStr();

                    // 有効な日付チェック
                    if (StringUtils.isNotEmpty(disposalReportReceiptDateStr)
                            && !FileUtil.isValidDate(disposalReportReceiptDateStr)) {

                        // エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile,
                                fileUtil.outValue(excelInfoMsg.get("rowNumber"), index,
                                        excelHeader.get("disposalReportReceiptDate"), disposalReportReceiptDateStr,
                                        excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                                        excelInfoMsg.get("msgErrorDateFormatInvalid")));

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;

                    }

                }

                // 備考
                String remarks = tblAssetDisposalRequestExcelOutPutVo.getRemarks();

                // 入力サイズチェック
                if (StringUtils.isNotEmpty(remarks)) {

                    if (chkFileImportMaxLength(remarks, "remarks", 256, index, fileUtil, logFile, excelInfoMsg,
                            excelHeader)) {

                        failedCount = failedCount + 1;

                        tblAssetDisposalRequestVo.setError(true);
                        return tblAssetDisposalRequestVo;

                    }
                }

            }

            // 更新用の廃棄情報を設定
            tblAssetDisposalRequestVo.setFinalReply(Integer.valueOf(finalReplyChoice.getMstChoicePK().getSeq()));

            if (2 == tblAssetDisposalRequestVo.getFinalReply()) {

                tblAssetDisposalRequestVo
                        .setFinalRejectReason(Integer.valueOf(finalRejectReasonChoice.getMstChoicePK().getSeq()));

            } else if (1 == tblAssetDisposalRequestVo.getFinalReply()) {

                tblAssetDisposalRequestVo
                        .setDocRequestDateStr(tblAssetDisposalRequestExcelOutPutVo.getDocRequestDateStr());
                tblAssetDisposalRequestVo
                        .setDocApprovalDateStr(tblAssetDisposalRequestExcelOutPutVo.getDocApprovalDateStr());
                tblAssetDisposalRequestVo.setDisposalReportSentDateStr(
                        tblAssetDisposalRequestExcelOutPutVo.getDisposalReportSentDateStr());
                tblAssetDisposalRequestVo.setDisposalReportReceiptDateStr(
                        tblAssetDisposalRequestExcelOutPutVo.getDisposalReportReceiptDateStr());
                tblAssetDisposalRequestVo.setDisposalProcessingCompletionDateStr(
                        tblAssetDisposalRequestExcelOutPutVo.getDisposalProcessingCompletionDateStr());
                tblAssetDisposalRequestVo.setRemarks(tblAssetDisposalRequestExcelOutPutVo.getRemarks());
            }

        }

        return tblAssetDisposalRequestVo;

    }

    private boolean chkFileImportNotNull(String properties, String itemName, int index, FileUtil fileUtil,
            String logFile, Map<String, String> excelInfoMsg, Map<String, String> excelHeader) {

        boolean result = false;

        if (StringUtils.isEmpty(properties)) {

            // エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile,
                    fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get(itemName), properties,
                            excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                            excelInfoMsg.get("msgErrorNotNull")));

            result = true;
        }

        return result;

    }

    private boolean chkFileImportMaxLength(String properties, String itemName, int length, int index, FileUtil fileUtil,
            String logFile, Map<String, String> excelInfoMsg, Map<String, String> excelHeader) {

        boolean result = false;

        if (fileUtil.maxLangthCheck(properties, length)) {
            // エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile,
                    fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get(itemName), properties,
                            excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                            excelInfoMsg.get("msgErrorOverLength")));

            result = true;
        }

        return result;

    }

    private boolean chkFileImportNotMail(String properties, String itemName, int index, FileUtil fileUtil,
            String logFile, Map<String, String> excelInfoMsg, Map<String, String> excelHeader) {

        boolean result = false;

        if (!FileUtil.isValidEmail(properties)) {
            // エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile,
                    fileUtil.outValue(excelInfoMsg.get("rowNumber"), index, excelHeader.get(itemName), properties,
                            excelInfoMsg.get("error"), 1, excelInfoMsg.get("errorDetail"),
                            excelInfoMsg.get("msgNotValidMailAddress")));

            result = true;
        }

        return result;

    }

    /**
     * Excel出力データを設定
     *
     * @param list
     * @param langId
     * @throws Exception
     */
    private void getExcelFormDate(List list, String langId) throws Exception {

        // 選択肢Map
        Map<String, List<String>> choiceListMap = new HashMap<String, List<String>>();

        String[] choiceArray = { DISPOSAL_REQUEST_REASON };

        choiceListMap = mstChoiceService.getChoiceListMap(langId, choiceArray);

        String[] keyArray = { "mold", "machine", "existence_yes", "existence_no" };

        Map<String, String> keyMap = FileUtil.getDictionaryList(mstDictionaryService, langId, Arrays.asList(keyArray));

        // 金型・設備区分リストを作成
        String[] moldMachineTypeArray = { keyMap.get("mold"), keyMap.get("machine") };

        // 現物有無リストを作成
        String[] existenceTextArray = { keyMap.get("existence_yes"), keyMap.get("existence_no") };

        for (int i = 0; i < 1000; i++) {

            TblAssetDisposalRequestExcelFormOutPutVo tblAssetDisposalRequestExcelFormOutPutVo = new TblAssetDisposalRequestExcelFormOutPutVo();

            tblAssetDisposalRequestExcelFormOutPutVo.setMoldMachineTypeArray(moldMachineTypeArray);

            tblAssetDisposalRequestExcelFormOutPutVo.setExistenceTextArray(existenceTextArray);

            String[] disposalRequestReasonArray = new String[choiceListMap.get(DISPOSAL_REQUEST_REASON).size()];
            tblAssetDisposalRequestExcelFormOutPutVo.setDisposalRequestReasonArray(
                    choiceListMap.get(DISPOSAL_REQUEST_REASON).toArray(disposalRequestReasonArray));

            list.add(tblAssetDisposalRequestExcelFormOutPutVo);
        }

    }

    /**
     * Excel出力データを設定
     *
     * @param list
     * @param tblAssetDisposalRequestVos
     * @param langId
     * @throws Exception
     */
    private void getExcelExportData(List list, List<TblAssetDisposalRequestVo> tblAssetDisposalRequestVos,
            String langId) throws Exception {

        // 選択肢Map
        Map<String, List<String>> choiceListMap = new HashMap<String, List<String>>();

        choiceListMap = mstChoiceService.getChoiceListMap(langId, ASSET_DISPOSAL_ARRAY);

        String[] keyArray = { "item_ver_unconfirmed", "item_ver_confirmed", "asset_mgmt_unconfirmed",
                "asset_mgmt_confirmed" };

        Map<String, String> keyMap = FileUtil.getDictionaryList(mstDictionaryService, langId, Arrays.asList(keyArray));

        // 品目コードVer確認Arrayを作成
        String[] itemVerStrArray = { keyMap.get("item_ver_unconfirmed"), keyMap.get("item_ver_confirmed") };

        // 資産管理部門確認Arrayを作成
        String[] assetMgmtStrArray = { keyMap.get("asset_mgmt_unconfirmed"), keyMap.get("asset_mgmt_confirmed") };

        for (TblAssetDisposalRequestVo tblAssetDisposalRequestVo : tblAssetDisposalRequestVos) {

            TblAssetDisposalRequestExcelOutPutVo tblAssetDisposalRequestExcelOutPutVo = new TblAssetDisposalRequestExcelOutPutVo();

            BeanCopyUtil.copyFields(tblAssetDisposalRequestVo, tblAssetDisposalRequestExcelOutPutVo);

            tblAssetDisposalRequestExcelOutPutVo.setMgmtRegion(tblAssetDisposalRequestVo.getMgmtRegionText());

            tblAssetDisposalRequestExcelOutPutVo.setRequestDate(tblAssetDisposalRequestVo.getRequestDateStr());

            String[] eolConfirmationArray = new String[choiceListMap.get(EOL_CONFIRMATION).size()];
            tblAssetDisposalRequestExcelOutPutVo
                    .setEolConfirmationArray(choiceListMap.get(EOL_CONFIRMATION).toArray(eolConfirmationArray));

            String[] disposalJudgmentArray = new String[choiceListMap.get(DISPOSAL_JUDGMENT).size()];
            tblAssetDisposalRequestExcelOutPutVo
                    .setDisposalJudgmentArray(choiceListMap.get(DISPOSAL_JUDGMENT).toArray(disposalJudgmentArray));

            String[] receiveRejectReasonArray = new String[choiceListMap.get(RECEIVE_REJECT_REASON).size()];
            tblAssetDisposalRequestExcelOutPutVo.setReceiveRejectReasonArray(
                    choiceListMap.get(RECEIVE_REJECT_REASON).toArray(receiveRejectReasonArray));

            tblAssetDisposalRequestExcelOutPutVo.setItemVerConfirmationArray(itemVerStrArray);

            String[] oemDestinationArray = new String[choiceListMap.get(OEM_DESTINATION).size()];
            tblAssetDisposalRequestExcelOutPutVo
                    .setOemDestinationArray(choiceListMap.get(OEM_DESTINATION).toArray(oemDestinationArray));

            if (StringUtils.isEmpty(tblAssetDisposalRequestVo.getPeriodBookValue())) {

                tblAssetDisposalRequestExcelOutPutVo
                        .setPeriodBookValue(String.valueOf(setNumScale(FileUtil.getNum(new BigDecimal(0)))));
            } else {

                tblAssetDisposalRequestExcelOutPutVo.setPeriodBookValue(String.valueOf(
                        setNumScale(FileUtil.getNum(new BigDecimal(tblAssetDisposalRequestVo.getPeriodBookValue())))));
            }

            tblAssetDisposalRequestExcelOutPutVo.setAssetMgmtConfirmArray(assetMgmtStrArray);

            String[] apDisposalJudgmentArray = new String[choiceListMap.get(AP_DISPOSAL_JUDGMENT).size()];
            tblAssetDisposalRequestExcelOutPutVo.setApDisposalJudgmentArray(
                    choiceListMap.get(AP_DISPOSAL_JUDGMENT).toArray(apDisposalJudgmentArray));

            String[] apRejectReasonArray = new String[choiceListMap.get(AP_REJECT_REASON).size()];
            tblAssetDisposalRequestExcelOutPutVo
                    .setApRejectReasonArray(choiceListMap.get(AP_REJECT_REASON).toArray(apRejectReasonArray));

            tblAssetDisposalRequestExcelOutPutVo
                    .setApSupplyRemainingMonth(String.valueOf(tblAssetDisposalRequestVo.getApSupplyRemainingMonth()));
            String[] apFinalBulkOrderArray = new String[choiceListMap.get(AP_FINAL_BULK_ORDER).size()];
            tblAssetDisposalRequestExcelOutPutVo
                    .setApFinalBulkOrderArray(choiceListMap.get(AP_FINAL_BULK_ORDER).toArray(apFinalBulkOrderArray));

            String[] finalReplyArray = new String[choiceListMap.get(FINAL_REPLY).size()];
            tblAssetDisposalRequestExcelOutPutVo
                    .setFinalReplyArray(choiceListMap.get(FINAL_REPLY).toArray(finalReplyArray));

            String[] finalRejectReasonArray = new String[choiceListMap.get(FINAL_REJECT_REASON).size()];
            tblAssetDisposalRequestExcelOutPutVo
                    .setFinalRejectReasonArray(choiceListMap.get(FINAL_REJECT_REASON).toArray(finalRejectReasonArray));

            list.add(tblAssetDisposalRequestExcelOutPutVo);
        }

    }

    /**
     * 資産廃棄管理情報取得用のSQLを作成
     *
     * @param requestDateFrom
     * @param requestDateTo
     * @param requestNo
     * @param disposalCompletionBefore
     * @param status
     * @param assetNo
     * @param itemCode
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param assetDisposalUuid
     * @param toComapanyFlg
     * @param isCount
     * @param pageFlg
     *
     * @return
     */
    private List getAssetDisposalSql(String requestDateFrom, String requestDateTo, String requestNo,
            int disposalCompletionBefore, int status, String assetNo, String itemCode, String sidx, String sord,
            int pageNumber, int pageSize, String assetDisposalUuid, Boolean toComapanyFlg, boolean isCount,
            boolean pageFlg) {

        StringBuilder sql = new StringBuilder(" SELECT ");
        if (isCount) {
            sql = sql.append(" COUNT(1) ");
        } else {
            sql = sql.append(" tblAssetDisposalRequest ");
        }

        // 検索用のSQLを作成する
        sql = sql.append(" FROM TblAssetDisposalRequest tblAssetDisposalRequest"
                + " LEFT JOIN FETCH tblAssetDisposalRequest.fromMstCompany fc"
                + " LEFT JOIN FETCH tblAssetDisposalRequest.toMstCompany tc"
                + " LEFT JOIN FETCH tblAssetDisposalRequest.receiveMstUser rm"
                + " LEFT JOIN FETCH tblAssetDisposalRequest.assetMgmtConfirmMstUser am"
                + " LEFT JOIN FETCH tblAssetDisposalRequest.apConfirmMstUser apm"
                + " LEFT JOIN FETCH tblAssetDisposalRequest.finalReplyMstUser fm"
                + " LEFT JOIN FETCH tblAssetDisposalRequest.mstAsset m" + " WHERE 1=1 ");
        // + " LEFT JOIN FETCH tblAssetDisposalRequest.mstAsset.mstMgmtCompany
        // mc"
        // + " LEFT JOIN FETCH tblAssetDisposalRequest.mstAsset.mstMgmtLocation
        // ml" + " WHERE 1=1 ");

        if (StringUtils.isNotEmpty(requestDateFrom)) {
            sql = sql.append(" AND tblAssetDisposalRequest.requestDate >= :requestDateFrom");
        }
        if (StringUtils.isNotEmpty(requestDateTo)) {
            sql = sql.append(" AND tblAssetDisposalRequest.requestDate <= :requestDateTo ");
        }
        if (StringUtils.isNotEmpty(requestNo)) {
            sql = sql.append(" AND tblAssetDisposalRequest.requestNo LIKE :requestNo");
        }
        if (disposalCompletionBefore > 0) {
            sql = sql.append(
                    " AND tblAssetDisposalRequest.externalStatus <> 4 AND tblAssetDisposalRequest.externalStatus <> 8 AND tblAssetDisposalRequest.externalStatus <> 9 ");
        }
        if (status > 0) {
            sql = sql.append(" AND tblAssetDisposalRequest.internalStatus = :status ");
        }

        if (StringUtils.isNotEmpty(assetNo)) {
            sql = sql.append(" AND tblAssetDisposalRequest.assetNo LIKE :assetNo");
        }

        if (StringUtils.isNotEmpty(itemCode)) {
            sql = sql.append(
                    " AND ( tblAssetDisposalRequest.itemCode LIKE :itemCode OR tblAssetDisposalRequest.itemCode2 LIKE :itemCode2 )");
        }
        if (StringUtils.isNotEmpty(assetDisposalUuid)) {
            sql = sql.append(" AND tblAssetDisposalRequest.uuid = :assetDisposalUuid ");
        }

        if (null != toComapanyFlg) {

            if (toComapanyFlg) {
                sql = sql.append(" AND tc.myCompany = 1 ");
            } else {
                sql = sql.append(" AND fc.myCompany = 1");
            }
        }

        if (!isCount) {

            if (StringUtils.isNotEmpty(sidx)) {

                String sortStr = AssetDisposalControlConstant.assetDisposalControlConstantMap.get(sidx) + " " + sord;

                // 表示順は設備IDの昇順。
                sql.append(" ORDER BY " + sortStr);

            } else {

                // 表示順は設備IDの昇順。
                sql.append(" ORDER BY tblAssetDisposalRequest.createDate DESC ");

            }

        }

        Query query = entityManager.createQuery(sql.toString());

        // 検索用の条件を作成する
        if (StringUtils.isNotEmpty(requestDateFrom)) {
            query.setParameter("requestDateFrom", DateFormat.strToDate(requestDateFrom));
        }

        if (StringUtils.isNotEmpty(requestDateTo)) {
            query.setParameter("requestDateTo", DateFormat.strToDate(requestDateTo));
        }

        if (StringUtils.isNotEmpty(requestNo)) {
            query.setParameter("requestNo", "%" + requestNo + "%");
        }

        if (status > 0) {
            query.setParameter("status", status);
        }

        if (StringUtils.isNotEmpty(assetNo)) {
            query.setParameter("assetNo", "%" + assetNo + "%");
        }

        if (StringUtils.isNotEmpty(itemCode)) {
            query.setParameter("itemCode", "%" + itemCode + "%");
            query.setParameter("itemCode2", "%" + itemCode + "%");
        }

        if (StringUtils.isNotEmpty(assetDisposalUuid)) {
            query.setParameter("assetDisposalUuid", assetDisposalUuid);
        }

        // 画面改ページを設定する
        if (!isCount && pageFlg) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }

        List<?> list = query.getResultList();

        return list;
    }

    /**
     * 文言を取得する
     *
     * @param moldMachineMap
     * @param existenceMap
     * @param itemVerMap
     * @param assetMgmtMap
     * @param externalStatusMap
     * @param langId
     */
    private void getDictionaryMap(Map<Integer, String> moldMachineMap, Map<Integer, String> existenceMap,
            Map<Integer, String> itemVerMap, Map<Integer, String> assetMgmtMap, Map<Integer, String> externalStatusMap,
            String langId) {

        String[] keyArray = { "mold", "machine", "existence_yes", "existence_no", "item_ver_unconfirmed",
                "item_ver_confirmed", "asset_mgmt_unconfirmed", "asset_mgmt_confirmed", "external_status_unsent",
                "external_status_applying", "external_status_received", "external_status_confirmed",
                "external_status_disposal_processed", "external_status_no_target", "external_status_dismissal" };

        Map<String, String> dictionaryMap = FileUtil.getDictionaryList(mstDictionaryService, langId,
                Arrays.asList(keyArray));

        // 金型・設備区分Mapを作成
        moldMachineMap.put(1, dictionaryMap.get("mold"));
        moldMachineMap.put(2, dictionaryMap.get("machine"));

        // 現品有無Mapを作成
        existenceMap.put(1, dictionaryMap.get("existence_yes"));
        existenceMap.put(2, dictionaryMap.get("existence_no"));

        // 品目コードVer確認Mapを作成
        itemVerMap.put(0, dictionaryMap.get("item_ver_unconfirmed"));
        itemVerMap.put(1, dictionaryMap.get("item_ver_confirmed"));

        // 資産管理部門確認Mapを作成
        assetMgmtMap.put(0, dictionaryMap.get("asset_mgmt_unconfirmed"));
        assetMgmtMap.put(1, dictionaryMap.get("asset_mgmt_confirmed"));

        // 社外ステータスMapを作成
        externalStatusMap.put(0, dictionaryMap.get("external_status_unsent"));
        externalStatusMap.put(1, dictionaryMap.get("external_status_applying"));
        externalStatusMap.put(2, dictionaryMap.get("external_status_received"));
        externalStatusMap.put(3, dictionaryMap.get("external_status_confirmed"));
        externalStatusMap.put(4, dictionaryMap.get("external_status_disposal_processed"));
        externalStatusMap.put(8, dictionaryMap.get("external_status_no_target"));
        externalStatusMap.put(9, dictionaryMap.get("external_status_dismissal"));

    }

    /**
     * 資産廃棄登録者の関連情報を設定
     *
     * @param tblAssetDisposalRequest
     * @param functionId
     * @param loginUserId
     * @param loginUserUuid
     */
    private void setAssetDisposalRequestUserInfo(TblAssetDisposalRequest tblAssetDisposalRequest, String functionId,
            String loginUserId, String loginUserUuid) {

        // 資産廃棄申請登録画面の場合
        if (functionId.startsWith(CommonConstants.ASSET_DISPOSAL_REQUEST_REGISTRATION)) {

            // 申請者会社
            tblAssetDisposalRequest.setFromCompanyId(getSelfCompanyId());

            MstUser requestUserInfo = mstUserService.getMstUser(loginUserId);

            // 申請者メールアドレス
            tblAssetDisposalRequest.setRequestUserName(requestUserInfo.getUserName());
            // 申請者氏名
            tblAssetDisposalRequest.setRequestMailAddress(requestUserInfo.getMailAddress());

        } else if (functionId.startsWith(CommonConstants.ASSET_DISPOSAL_REQUEST_RECEPTION)) {// 資産廃棄申請受付登録画面の場合

            // 申請先会社
            tblAssetDisposalRequest.setToCompanyId(getSelfCompanyId());
            // 受付者
            tblAssetDisposalRequest.setReceiveUserUuid(loginUserUuid);

            Date nowDate = new Date();

            // 受付日
            tblAssetDisposalRequest.setReceiveDate(nowDate);

        }

    }

    /**
     * 一覧画面表示用の日付を変換
     *
     * @param date
     */
    private String dateToStr(Date date) {

        if (null == date) {
            return "";
        } else {

            return DateFormat.dateToStr(date, DateFormat.DATE_FORMAT);
        }

    }

    /**
     * 画面入力した日付文字列からDateへ変換
     *
     * @param dateStr
     * 
     */
    private Date strToDate(String dateStr) {

        if (StringUtils.isEmpty(dateStr)) {
            return null;
        } else {

            return DateFormat.strToDate(dateStr);
        }

    }

    /**
     * 画面IDに対して、制御情報を取得(ステータス制御用)
     *
     * @param obj
     * @param controlProperty
     * 
     */
    private String getControlStr(Object obj, String controlProperty) throws Exception {

        Class<?> property = obj.getClass();

        String[] propertyArray = controlProperty.split(",");

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < propertyArray.length; i++) {

            PropertyDescriptor pd = new PropertyDescriptor(propertyArray[i], property);
            Method getMethod = pd.getReadMethod();
            getMethod.setAccessible(true);
            Object tempObj = getMethod.invoke(obj);

            if (i == 1) {

                if (null != tempObj && "" != tempObj) {

                    result.append("1");
                    continue;
                }

            }

            if (tempObj instanceof Integer) {

                result.append(String.valueOf(tempObj));

            } else if (tempObj instanceof String) {

                result.append(String.valueOf(tempObj));

            }

        }

        return result.toString();
    }

    /**
     * 画面IDに対して、制御情報を取得(画面遷移制御用)
     *
     * @param obj
     * @param controlProperty
     * 
     */
    private String getControlJumpStr(Object obj, String controlProperty) throws Exception {

        Class<?> property = obj.getClass();

        String[] propertyArray = controlProperty.split(",");

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < propertyArray.length; i++) {

            PropertyDescriptor pd = new PropertyDescriptor(propertyArray[i], property);
            Method getMethod = pd.getReadMethod();
            getMethod.setAccessible(true);
            Object tempObj = getMethod.invoke(obj);

            if (tempObj instanceof Integer) {

                result.append(String.valueOf(tempObj));

            } else if (tempObj instanceof String) {

                result.append(String.valueOf(tempObj));

            }

        }

        return result.toString();
    }

    /**
     * 画面IDに対して、出力情報を設定
     *
     * @param obj
     * @param controlProperty
     * 
     */
    private void setControlStr(Object obj, String controlProperty, String value) throws Exception {

        Class<?> property = obj.getClass();

        // setメソッドを作成
        PropertyDescriptor pd = new PropertyDescriptor(controlProperty, property);
        Method getMethod = pd.getWriteMethod();
        getMethod.setAccessible(true);

        // valueを設定
        getMethod.invoke(obj, value);

    }

    /**
     * 画面入力した日付文字列からDateへ変換
     *
     * @param tblAssetDisposalRequestVo
     * 
     */
    private void setVoInfoStrToDate(TblAssetDisposalRequestVo tblAssetDisposalRequestVo) {

        tblAssetDisposalRequestVo.setRequestDate(strToDate(tblAssetDisposalRequestVo.getRequestDateStr()));
        tblAssetDisposalRequestVo.setReceiveDate(strToDate(tblAssetDisposalRequestVo.getReceiveDateStr()));
        tblAssetDisposalRequestVo
                .setAssetMgmtConfirmDate(strToDate(tblAssetDisposalRequestVo.getAssetMgmtConfirmDateStr()));
        tblAssetDisposalRequestVo.setApConfirmDate(strToDate(tblAssetDisposalRequestVo.getApConfirmDateStr()));
        tblAssetDisposalRequestVo.setFinalReplyDate(strToDate(tblAssetDisposalRequestVo.getFinalReplyDateStr()));
        tblAssetDisposalRequestVo.setDocRequestDate(strToDate(tblAssetDisposalRequestVo.getDocRequestDateStr()));
        tblAssetDisposalRequestVo.setDocApprovalDate(strToDate(tblAssetDisposalRequestVo.getDocApprovalDateStr()));
        tblAssetDisposalRequestVo
                .setDisposalReportSentDate(strToDate(tblAssetDisposalRequestVo.getDisposalReportSentDateStr()));
        tblAssetDisposalRequestVo
                .setDisposalReportReceiptDate(strToDate(tblAssetDisposalRequestVo.getDisposalReportReceiptDateStr()));
        tblAssetDisposalRequestVo.setDisposalProcessingCompletionDate(
                strToDate(tblAssetDisposalRequestVo.getDisposalProcessingCompletionDateStr()));
        tblAssetDisposalRequestVo.setAcquisitionDate(strToDate(tblAssetDisposalRequestVo.getAcquisitionDateStr()));

    }

    /**
     * 資産廃棄登録関連の情報を設定
     *
     * @param tblAssetDisposalRequest
     * @param tblAssetDisposalRequestVo
     */
    private void setAssetDisposalJoinMstInfo(TblAssetDisposalRequest tblAssetDisposalRequest,
            TblAssetDisposalRequestVo tblAssetDisposalRequestVo) {

        if (null != tblAssetDisposalRequest.getMstAsset()) {

            // 資産番号を設定
            tblAssetDisposalRequestVo.setAssetNo(tblAssetDisposalRequest.getMstAsset().getMstAssetPK().getAssetNo());
            // 補助番号を設定
            tblAssetDisposalRequestVo.setBranchNo(tblAssetDisposalRequest.getMstAsset().getMstAssetPK().getBranchNo());
            // 管理地域を設定
            tblAssetDisposalRequestVo.setMgmtRegion(tblAssetDisposalRequest.getMstAsset().getMgmtRegion());
            // 管理先コードを設定
            tblAssetDisposalRequestVo.setMgmtCompanyCode(tblAssetDisposalRequest.getMstAsset().getMgmtCompanyCode());
            // ベンダーコードを設定
            tblAssetDisposalRequestVo.setVendorCode(tblAssetDisposalRequest.getMstAsset().getVendorCode());
            // 取得区分を設定
            tblAssetDisposalRequestVo.setAcquisitionType(tblAssetDisposalRequest.getMstAsset().getAcquisitionType());
            // 使用部門を設定
            tblAssetDisposalRequestVo.setUsingSection(tblAssetDisposalRequest.getMstAsset().getUsingSection());
            // 取得日を設定
            tblAssetDisposalRequestVo.setAcquisitionDate(tblAssetDisposalRequest.getMstAsset().getAcquisitionDate());
            // 期初簿価を設定
            tblAssetDisposalRequestVo.setPeriodBookValue(String
                    .valueOf(setNumScale(FileUtil.getNum(tblAssetDisposalRequest.getMstAsset().getPeriodBookValue()))));
            // 原価センターを設定
            tblAssetDisposalRequestVo.setCostCenter(tblAssetDisposalRequest.getMstAsset().getCostCenter());
            // 資産クラスを設定
            tblAssetDisposalRequestVo.setAssetClass(tblAssetDisposalRequest.getMstAsset().getAssetClass());

            if (null != tblAssetDisposalRequest.getMstAsset().getMstMgmtCompany()) {
                // 管理先名称を設定
                tblAssetDisposalRequestVo.setMgmtCompanyName(
                        tblAssetDisposalRequest.getMstAsset().getMstMgmtCompany().getMgmtCompanyName());
            }

            if (null != tblAssetDisposalRequest.getMstAsset().getMstMgmtLocation()) {
                // 所在先名称を設定
                tblAssetDisposalRequestVo.setMgmtLocationName(
                        tblAssetDisposalRequest.getMstAsset().getMstMgmtLocation().getMgmtLocationName());
            }
        }

        if (null != tblAssetDisposalRequest.getFromMstCompany()) {
            tblAssetDisposalRequestVo.setFromCompanyName(tblAssetDisposalRequest.getFromMstCompany().getCompanyName());
        }
        if (null != tblAssetDisposalRequest.getToMstCompany()) {
            tblAssetDisposalRequestVo.setToCompanyName(tblAssetDisposalRequest.getToMstCompany().getCompanyName());
        }
        if (null != tblAssetDisposalRequest.getReceiveMstUser()) {
            tblAssetDisposalRequestVo.setReceiveUserName(tblAssetDisposalRequest.getReceiveMstUser().getUserName());
        }
        if (null != tblAssetDisposalRequest.getAssetMgmtConfirmMstUser()) {
            tblAssetDisposalRequestVo
                    .setAssetMgmtConfirmUserName(tblAssetDisposalRequest.getAssetMgmtConfirmMstUser().getUserName());
        }
        if (null != tblAssetDisposalRequest.getApConfirmMstUser()) {
            tblAssetDisposalRequestVo.setApConfirmUserName(tblAssetDisposalRequest.getApConfirmMstUser().getUserName());
        }
        if (null != tblAssetDisposalRequest.getFinalReplyMstUser()) {
            tblAssetDisposalRequestVo
                    .setFinalReplyUserName(tblAssetDisposalRequest.getFinalReplyMstUser().getUserName());
        }

        // 画面表示用の日付を設定する
        tblAssetDisposalRequestVo.setRequestDateStr(dateToStr(tblAssetDisposalRequestVo.getRequestDate()));
        tblAssetDisposalRequestVo.setReceiveDateStr(dateToStr(tblAssetDisposalRequestVo.getReceiveDate()));
        tblAssetDisposalRequestVo
                .setAssetMgmtConfirmDateStr(dateToStr(tblAssetDisposalRequestVo.getAssetMgmtConfirmDate()));
        tblAssetDisposalRequestVo.setApConfirmDateStr(dateToStr(tblAssetDisposalRequestVo.getApConfirmDate()));
        tblAssetDisposalRequestVo.setFinalReplyDateStr(dateToStr(tblAssetDisposalRequestVo.getFinalReplyDate()));
        tblAssetDisposalRequestVo.setDocRequestDateStr(dateToStr(tblAssetDisposalRequestVo.getDocRequestDate()));
        tblAssetDisposalRequestVo.setDocApprovalDateStr(dateToStr(tblAssetDisposalRequestVo.getDocApprovalDate()));
        tblAssetDisposalRequestVo
                .setDisposalReportSentDateStr(dateToStr(tblAssetDisposalRequestVo.getDisposalReportSentDate()));
        tblAssetDisposalRequestVo
                .setDisposalReportReceiptDateStr(dateToStr(tblAssetDisposalRequestVo.getDisposalReportReceiptDate()));
        tblAssetDisposalRequestVo.setDisposalProcessingCompletionDateStr(
                dateToStr(tblAssetDisposalRequestVo.getDisposalProcessingCompletionDate()));
        tblAssetDisposalRequestVo.setAcquisitionDateStr(dateToStr(tblAssetDisposalRequestVo.getAcquisitionDate()));

    }

    /**
     * 画面更新用の情報を設定する
     *
     * @param functionId
     * @param loginUserUuid
     * @param tblAssetDisposalRequestFrom
     * @param tblAssetDisposalRequestTo
     * @param langId
     * 
     */
    private void setUpdateInfo(String functionId, String loginUserUuid,
            TblAssetDisposalRequest tblAssetDisposalRequestFrom, TblAssetDisposalRequest tblAssetDisposalRequestTo,
            String langId) {

        Date nowDate = new Date();

        // 資産廃棄申請受付登録画面編集の場合
        if (functionId.startsWith(CommonConstants.ASSET_DISPOSAL_REQUEST_RECEPTION)) {

            // EOL確認
            tblAssetDisposalRequestTo.setEolConfirmation(tblAssetDisposalRequestFrom.getEolConfirmation());
            // 廃棄可否判断
            tblAssetDisposalRequestTo.setDisposalJudgment(tblAssetDisposalRequestFrom.getDisposalJudgment());
            // 廃棄可否判断理由
            tblAssetDisposalRequestTo.setJudgmentReason(tblAssetDisposalRequestFrom.getJudgmentReason());

            // 廃棄不可の場合
            if (2 == tblAssetDisposalRequestTo.getDisposalJudgment()) {

                // 受付却下理由
                tblAssetDisposalRequestTo.setReceiveRejectReason(tblAssetDisposalRequestFrom.getReceiveRejectReason());

                MstChoice rejectResonChoice = mstChoiceService.getBySeqChoice(
                        String.valueOf(tblAssetDisposalRequestTo.getReceiveRejectReason()), langId,
                        RECEIVE_REJECT_REASON);
                // 却下理由
                tblAssetDisposalRequestTo.setRejectReasonText(rejectResonChoice.getChoice());
            } else {

                tblAssetDisposalRequestTo.setRejectReasonText("");
                tblAssetDisposalRequestTo.setReceiveRejectReason(0);
            }

            // 受付日
            tblAssetDisposalRequestTo.setReceiveDate(nowDate);
            // 受付者
            tblAssetDisposalRequestTo.setReceiveUserUuid(loginUserUuid);

        } else if (functionId.startsWith(CommonConstants.ASSET_DISPOSAL_REQUEST_CONFIRMATION)) {// 資産廃棄申請確認登録画面編集の場合

            // 品目コードVer確認
            tblAssetDisposalRequestTo.setItemVerConfirmation(tblAssetDisposalRequestFrom.getItemVerConfirmation());
            // OEM先
            tblAssetDisposalRequestTo.setOemDestination(tblAssetDisposalRequestFrom.getOemDestination());
            // OEM資産番号
            tblAssetDisposalRequestTo.setOemAssetNo(tblAssetDisposalRequestFrom.getOemAssetNo());

            if (0 == tblAssetDisposalRequestTo.getOemDestination()) {

                tblAssetDisposalRequestTo.setOemAssetNo("");
            }

            // 資産管理部門確認
            tblAssetDisposalRequestTo.setAssetMgmtConfirm(tblAssetDisposalRequestFrom.getAssetMgmtConfirm());
            // 資産管理部門確認日
            tblAssetDisposalRequestTo.setAssetMgmtConfirmDate(nowDate);
            // 確認者
            tblAssetDisposalRequestTo.setAssetMgmtConfirmUserUuid(loginUserUuid);

            // // 未確認の場合、クリア処理
            // if (0 == tblAssetDisposalRequestTo.getAssetMgmtConfirm()) {
            //
            // // 品目コードVer確認
            // tblAssetDisposalRequestTo.setItemVerConfirmation(0);
            //
            // // OEM先
            // tblAssetDisposalRequestTo.setOemDestination(0);
            // // OEM資産番号
            // tblAssetDisposalRequestTo.setOemAssetNo("");
            //
            // // 資産管理部門確認日
            // tblAssetDisposalRequestTo.setAssetMgmtConfirmDate(null);
            // // 確認者
            // tblAssetDisposalRequestTo.setAssetMgmtConfirmUserUuid("");
            // }

        } else if (functionId.startsWith(CommonConstants.ASSET_DISPOSAL_REQUEST_AP_CONFIRMATION)) {// 資産廃棄申請AP確認登録画面編集の場合

            // AP廃棄可否判断
            tblAssetDisposalRequestTo.setApDisposalJudgment(tblAssetDisposalRequestFrom.getApDisposalJudgment());

            // AP供給期限残月数
            tblAssetDisposalRequestTo
                    .setApSupplyRemainingMonth(tblAssetDisposalRequestFrom.getApSupplyRemainingMonth());
            // AP最終まとめ発注
            tblAssetDisposalRequestTo.setApFinalBulkOrder(tblAssetDisposalRequestFrom.getApFinalBulkOrder());
            // AP確認日
            tblAssetDisposalRequestTo.setApConfirmDate(nowDate);
            // AP確認者
            tblAssetDisposalRequestTo.setApConfirmUserUuid(loginUserUuid);

            // 廃棄不可の場合
            if (2 == tblAssetDisposalRequestTo.getApDisposalJudgment()) {

                // AP却下理由
                tblAssetDisposalRequestTo.setApRejectReason(tblAssetDisposalRequestFrom.getApRejectReason());

                MstChoice rejectResonChoice = mstChoiceService.getBySeqChoice(
                        String.valueOf(tblAssetDisposalRequestTo.getApRejectReason()), langId, AP_REJECT_REASON);
                // 却下理由
                tblAssetDisposalRequestTo.setRejectReasonText(rejectResonChoice.getChoice());
            } else {

                tblAssetDisposalRequestTo.setRejectReasonText("");
                tblAssetDisposalRequestTo.setApRejectReason(0);
            }

            // // 未選択の場合、クリア処理
            // if (0 == tblAssetDisposalRequestTo.getApDisposalJudgment()) {
            //
            // // 最終回答日
            // tblAssetDisposalRequestTo.setApConfirmDate(null);
            // // 最終回答者
            // tblAssetDisposalRequestTo.setApConfirmUserUuid("");
            // }

        } else if (functionId.startsWith(CommonConstants.ASSET_DISPOSAL_PROCESSING)) {// 資産廃棄処理登録画面編集の場合

            // 最終回答
            tblAssetDisposalRequestTo.setFinalReply(tblAssetDisposalRequestFrom.getFinalReply());

            // 廃棄不可の場合
            if (2 == tblAssetDisposalRequestTo.getFinalReply()) {
                // 最終回答却下理由
                tblAssetDisposalRequestTo.setFinalRejectReason(tblAssetDisposalRequestFrom.getFinalRejectReason());

                MstChoice rejectResonChoice = mstChoiceService.getBySeqChoice(
                        String.valueOf(tblAssetDisposalRequestTo.getFinalRejectReason()), langId, FINAL_REJECT_REASON);
                // 却下理由
                tblAssetDisposalRequestTo.setRejectReasonText(rejectResonChoice.getChoice());
            } else {

                tblAssetDisposalRequestTo.setRejectReasonText("");
                tblAssetDisposalRequestTo.setFinalRejectReason(0);
            }
            // 最終回答日
            tblAssetDisposalRequestTo.setFinalReplyDate(nowDate);
            // 最終回答者
            tblAssetDisposalRequestTo.setFinalReplyUserUuid(loginUserUuid);

            // // 未選択の場合、クリア処理
            // if (0 == tblAssetDisposalRequestTo.getFinalReply()) {
            //
            // // 最終回答日
            // tblAssetDisposalRequestTo.setFinalReplyDate(null);
            // // 最終回答者
            // tblAssetDisposalRequestTo.setFinalReplyUserUuid("");
            // }

            // 最終回答は可の場合
            if (1 == tblAssetDisposalRequestTo.getFinalReply()) {

                // 稟議書申請日
                tblAssetDisposalRequestTo.setDocRequestDate(tblAssetDisposalRequestFrom.getDocRequestDate());
                // 稟議書承認日
                tblAssetDisposalRequestTo.setDocApprovalDate(tblAssetDisposalRequestFrom.getDocApprovalDate());
                // 廃棄報告書送付日
                tblAssetDisposalRequestTo
                        .setDisposalReportSentDate(tblAssetDisposalRequestFrom.getDisposalReportSentDate());
                // 廃棄報告書受領日
                tblAssetDisposalRequestTo
                        .setDisposalReportReceiptDate(tblAssetDisposalRequestFrom.getDisposalReportReceiptDate());
                // 廃棄処理完了日
                tblAssetDisposalRequestTo.setDisposalProcessingCompletionDate(
                        tblAssetDisposalRequestFrom.getDisposalProcessingCompletionDate());
                // 備考
                tblAssetDisposalRequestTo.setRemarks(tblAssetDisposalRequestFrom.getRemarks());

            } else {

                // 稟議書申請日
                tblAssetDisposalRequestTo.setDocRequestDate(null);
                // 稟議書承認日
                tblAssetDisposalRequestTo.setDocApprovalDate(null);
                // 廃棄報告書送付日
                tblAssetDisposalRequestTo.setDisposalReportSentDate(null);
                // 廃棄報告書受領日
                tblAssetDisposalRequestTo.setDisposalReportReceiptDate(null);
                // 廃棄処理完了日
                tblAssetDisposalRequestTo.setDisposalProcessingCompletionDate(null);
                // 備考
                tblAssetDisposalRequestTo.setRemarks("");

            }

        }

    }

    /**
     * 0：未送信・・・カルテ間データ連携実行前
     *
     * @param loginUser
     * @return
     */
    public TblAssetDisposalRequestVoList getExternalAssetDisposalList(LoginUser loginUser) {

        Query query = entityManager.createNamedQuery("TblAssetDisposalRequest.findExternalStatusIsUnsent");
        // 申請先会社へのまだ未送信のデータを取得
        query.setParameter("externalStatus", CommonConstants.ASSET_EXTERNAL_STATUS_UNSENT);
        query.setParameter("apiUserId", loginUser.getUserid());

        List assetDisposalList = query.getResultList();
        // リターン用
        TblAssetDisposalRequestVoList tblAssetDisposalRequestVoList = new TblAssetDisposalRequestVoList();
        List<TblAssetDisposalRequestVo> tblAssetDisposalRequestVos = new ArrayList<TblAssetDisposalRequestVo>();

        TblAssetDisposalRequestVo tblAssetDisposalRequestVo;

        // 送信用のUUID
        // String[] sendMailRequestUuid = new String[assetDisposalList.size()];

        for (TblAssetDisposalRequest tblAssetDisposalRequest : (List<TblAssetDisposalRequest>) assetDisposalList) {
            // int i = 0;
            tblAssetDisposalRequestVo = new TblAssetDisposalRequestVo();
            BeanCopyUtil.copyFields(tblAssetDisposalRequest, tblAssetDisposalRequestVo);
            tblAssetDisposalRequestVos.add(tblAssetDisposalRequestVo);
            // sendMailRequestUuid[i] = tblAssetDisposalRequestVo.getUuid();
            // i++;
        }

        // 廃棄メール送信
        // sendAssetDisposalMail(1, sendMailRequestUuid);

        tblAssetDisposalRequestVoList.setTblAssetDisposalRequestVos(tblAssetDisposalRequestVos);
        return tblAssetDisposalRequestVoList;
    }

    /**
     * 1：申請済・・・カルテ間データ連携により資産所有会社サーバーに送信完了
     *
     * @param tblAssetDisposalRequestVoList
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse pushGetExternalAssetDisposalByBatch(
            TblAssetDisposalRequestVoList tblAssetDisposalRequestVoList, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        for (TblAssetDisposalRequestVo tblAssetDisposalRequestVo : tblAssetDisposalRequestVoList
                .getTblAssetDisposalRequestVos()) {

            TblAssetDisposalRequest oldTblAssetDisposalRequest = entityManager.find(TblAssetDisposalRequest.class,
                    tblAssetDisposalRequestVo.getUuid());
            if (oldTblAssetDisposalRequest != null) {
                // 1：申請済 に更新
                oldTblAssetDisposalRequest.setExternalStatus(CommonConstants.ASSET_EXTERNAL_STATUS_APPLYING);
                entityManager.merge(oldTblAssetDisposalRequest);
            }

        }
        return basicResponse;
    }

    /**
     * 外部会社から取得された資産廃棄データを自社に格納する用
     *
     * @param tblAssetDisposalRequestVoList
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @param methodName
     */
    @Transactional
    public void saveExternalAssetDisposalList(TblAssetDisposalRequestVoList tblAssetDisposalRequestVoList,
            String outgoingCompanyId, String incomingCompanyId, String methodName) {

        TblAssetDisposalRequest tblAssetDisposalRequest;
        for (TblAssetDisposalRequestVo tblAssetDisposalRequestVo : tblAssetDisposalRequestVoList
                .getTblAssetDisposalRequestVos()) {
            tblAssetDisposalRequest = new TblAssetDisposalRequest();

            BeanCopyUtil.copyFields(tblAssetDisposalRequestVo, tblAssetDisposalRequest);

            TblAssetDisposalRequest oldTblAssetDisposalRequest = entityManager.find(TblAssetDisposalRequest.class,
                    tblAssetDisposalRequest.getUuid());
            if (oldTblAssetDisposalRequest == null) {
                // 申請番号を採番する
                String requestNo = tblNumberAssignmentService.findByKey(AD_REQEUST_NUMBER_EXTERNAL);
                tblAssetDisposalRequest.setRequestNo(requestNo);
                tblAssetDisposalRequest.setFromCompanyId(outgoingCompanyId); // 会社IDに変換
                tblAssetDisposalRequest.setToCompanyId(incomingCompanyId); // 自社の会社IDに変換
                tblAssetDisposalRequest.setInternalStatus(CommonConstants.ASSET_INTERNAL_STATUS_REQUESTED); // 1:申請済
                entityManager.persist(tblAssetDisposalRequest);
                tblNumberAssignmentService.updateTblNumberAssignment(AD_REQEUST_NUMBER_EXTERNAL, methodName);
            }
        }
    }

    /**
     * 資産廃棄申請メール送信（非同期処理）
     *
     * @param processType
     * @param requestUuids
     */
    public void sendAssetDisposalMail(int processType, String[] requestUuids) {

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        cachedThreadPool.execute(new Runnable() {

            public void run() {

                assetDisposalRequestNoticeService.sendNotice(processType, requestUuids);
            }
        });

    }

    /**
     * 資産廃棄申請メール送信を判定
     *
     * @param functionId
     * @param requestUuids
     */
    public void chkSendAssetDisposalMailInfo(String functionId, String[] requestUuids) {

        // 送信制御情報を取得
        String prossType = AssetDisposalControlConstant.assetDisposalControlConstantMap
                .get(CommonConstants.ASSET_DISPOSAL_MAIL_SEND_CONTROL + functionId);

        // 送信フラグはtrue、且つ送信制御情報存在の場合
        if (StringUtils.isNotEmpty(prossType)) {

            sendAssetDisposalMail(Integer.valueOf(prossType), requestUuids);
        }

    }

    /**
     * 所有会社からアクションにより使用会社のステータスを更新する
     *
     * @param tblAssetDisposalRequest
     * @return
     */
    @Transactional
    private BasicResponse pushGetExternalAssetDisposalAction(TblAssetDisposalRequest tblAssetDisposalRequest) {
        BasicResponse basicResponse = new BasicResponse();

        // 外部データ取得設定から有効フラグ=1のレコードを取得する
        List<MstExternalDataGetSetting> externalDataSettings = this.externalDataGetSettingService
                .getExternalDataGetSettringByCompanyId(tblAssetDisposalRequest.getFromCompanyId());
        if (!externalDataSettings.isEmpty()) {
            MstExternalDataGetSetting mstExternalDataGetSetting = externalDataSettings.get(0);

            String apiBaseUrl = mstExternalDataGetSetting.getApiBaseUrl();
            if (null == apiBaseUrl || apiBaseUrl.equals("")) {
                return basicResponse;
            }

            if (!apiBaseUrl.endsWith("/")) {
                apiBaseUrl = apiBaseUrl + "/";
                mstExternalDataGetSetting.setApiBaseUrl(apiBaseUrl);
            }

            Credential credential = externalCompanyLogin(mstExternalDataGetSetting);
            if (credential != null && StringUtils.isNotEmpty(credential.getToken())) {
                // call API
                String resultJson = FileUtil.sendPost(apiBaseUrl + EXT_ASSET_DISPOSAL_PUSH_ONE_API_URL,
                        credential.getToken(), tblAssetDisposalRequest);

                if (StringUtils.isNotEmpty(resultJson)) {
                    Gson gson = new Gson();
                    return gson.fromJson(resultJson, BasicResponse.class);
                }
            }

        }

        return basicResponse;
    }

    /**
     *
     * @param externalDataSetting
     * @return
     */
    private Credential externalCompanyLogin(MstExternalDataGetSetting externalDataSetting) {
        Credential result = null;

        try {
            // 外部データ取得する前に認証確認
            Credential credential = new Credential();
            credential.setUserid(externalDataSetting.getUserId());
            credential.setPassword(FileUtil.decrypt(externalDataSetting.getEncryptedPassword()));
            String pathUrl = externalDataSetting.getApiBaseUrl() + CommonConstants.EXT_LOGIN_API_URL;
            FileUtil.SSL();

            try {
                result = FileUtil.sendPost(pathUrl, credential);
            } catch (Exception e) {
                // 認証エラー発生場合、スキップ
                return result;
            }

            // 認証失敗 スキップ
            if (result.isValid() == false || result.isError() == true) {
                return result;
            }

            // 認証できたら、Tokenを取得
            return result;
        } catch (Exception e) {
            return result;
        }

    }

    /**
     * 資産廃棄申請情報を取得
     *
     * @param requestNo
     * @return
     */
    public TblAssetDisposalRequest getAssetDisposalRequestInfo(String requestNo) {

        Query query = entityManager.createNamedQuery("TblAssetDisposalRequest.findByRequestNoAndToCompanyId");

        query.setParameter("requestNo", requestNo);
        query.setParameter("toCompanyId", getSelfCompanyId());

        try {

            return (TblAssetDisposalRequest) query.getSingleResult();

        } catch (NoResultException e) {

            return null;
        }
    }

    /**
     *
     * @param tblAssetDisposalRequest
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse pushGetExternalAssetDisposalByBatch(TblAssetDisposalRequest tblAssetDisposalRequest,
            LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();

        TblAssetDisposalRequest oldTblAssetDisposalRequest = entityManager.find(TblAssetDisposalRequest.class,
                tblAssetDisposalRequest.getUuid());
        if (oldTblAssetDisposalRequest != null) {
            if (StringUtils.isEmpty(oldTblAssetDisposalRequest.getRequestNo())) {
                oldTblAssetDisposalRequest.setRequestNo(tblAssetDisposalRequest.getRequestNo());// 所有会社からもらう申請番号
            }

            oldTblAssetDisposalRequest.setExternalStatus(tblAssetDisposalRequest.getExternalStatus());// 社外ステータス
            oldTblAssetDisposalRequest.setRejectReasonText(tblAssetDisposalRequest.getRejectReasonText());// 受付却下理由、AP却下理由、最終却下理由のいずれかの文言

            entityManager.merge(oldTblAssetDisposalRequest);
        }

        return basicResponse;
    }

    private BigDecimal setNumScale(BigDecimal num) {

        int decimalPlaces = mstCurrencyService.getDecimalPlacesFromByCurrencyCode(null);

        return num.setScale(decimalPlaces, BigDecimal.ROUND_DOWN);

    }

    /**
     * 自社の会社IDを取得する
     *
     * @return
     */
    private String getSelfCompanyId() {

        MstCompany myCompany = mstCompanyService.getSelfCompany();

        return myCompany.getId();

    }

    public String[] getSendRequestUuid() {
        return sendRequestUuid;
    }

    public void setSendRequestUuid(String[] sendRequestUuid) {
        this.sendRequestUuid = sendRequestUuid;
    }

    public String getSendFunctionId() {
        return sendFunctionId;
    }

    public void setSendFunctionId(String sendFunctionId) {
        this.sendFunctionId = sendFunctionId;
    }
    
    /**
     * return true:ok,false:error
     * @param from
     * @param to
     * @return 
     */
    private boolean diffCheckDate(Date from, Date to) {

        if (from != null && to != null) {
            return from.compareTo(to) == 1;//from > to
        } else {
            return false;
        }

    }

}
