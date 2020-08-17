/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.match;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.asset.MstAsset;
import com.kmcj.karte.resources.asset.MstAssetList;
import com.kmcj.karte.resources.asset.MstAssetVo;
import com.kmcj.karte.resources.asset.relation.TblMoldMachineAssetRelation;
import com.kmcj.karte.resources.asset.relation.TblMoldMachineAssetRelationVo;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.mgmt.location.MstMgmtLocation;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelation;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.batch.operations.JobOperator;
import javax.batch.operations.NoSuchJobException;
import javax.batch.runtime.BatchRuntime;
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
 * @author admin
 */
@Dependent
public class TblAssetMatchingService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private MstChoiceService mstChoiceService;

    private static final String MST_ASSET_ASSET_TYPE = "mst_asset.asset_type";

    private static final String MST_ASSET_ACQUISITION_TYPE = "mst_asset.acquisition_type";

    private static final String ASSET_MATCHING_BATCHLET = "assetMatchingBatchlet";

    private static final String NOT_APPLICABLE = "not_applicable";

    private static final String APPLICABLE = "applicable";

    private static final String MULTIPLE_APPLICABLE = "multiple_applicable";

    /**
     * 資産マスタ未確認データを検索
     *
     * @param langId
     *
     * @return
     */
    public MstAssetList getNotMatchingAsset(String langId) {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" SELECT t FROM MstAsset t "
                + " LEFT JOIN FETCH t.mstItem mstItem "
                + " LEFT JOIN FETCH t.mstMgmtLocation mstMgmtLocation "
                + " LEFT JOIN FETCH t.mstMgmtCompany mstMgmtCompany "
                + " WHERE t.moldMachineType = 0 "
                + " ORDER BY t.mstAssetPK.assetNo ASC ,t.mstAssetPK.branchNo ASC");

        Query query = entityManager.createQuery(sqlBuilder.toString());

        List list = query.getResultList();

        MstAssetList mstAssetList = new MstAssetList();

        if (list != null && list.size() > 0) {

            String[] choiceArray = {MST_ASSET_ASSET_TYPE, MST_ASSET_ACQUISITION_TYPE};
            Map<String, String> choiceMap = mstChoiceService.getChoiceMap(langId, choiceArray);

            List<MstAssetVo> assetResultList = new ArrayList<>();
            for (Object obj : list) {
                MstAsset mstAsset = (MstAsset) obj;

                // 資産データ、表示項目の設定
                MstAssetVo assetResult = new MstAssetVo();
                assetResult.setUuid(mstAsset.getUuid()); // 資産ID
                assetResult.setAssetNo(mstAsset.getMstAssetPK().getAssetNo()); // 資産番号
                assetResult.setBranchNo(mstAsset.getMstAssetPK().getBranchNo()); // 補助番号
                assetResult.setAssetType(mstAsset.getAssetType()); // 資産種類
                assetResult.setAssetTypeText(choiceMap.get(MST_ASSET_ASSET_TYPE + mstAsset.getAssetType()));
                assetResult.setAssetName(mstAsset.getAssetName()); // 資産名称
                assetResult.setMgmtCompanyCode(mstAsset.getMgmtCompanyCode()); // 管理先コード
                MstMgmtLocation mstMgmtLocation = mstAsset.getMstMgmtLocation();
                assetResult.setMgmtLocationName(mstMgmtLocation != null ? mstMgmtLocation.getMgmtLocationName() : ""); // 設置場所
                assetResult.setItemCode(mstAsset.getItemCode()); // 品目コード
                assetResult.setAcquisitionType(mstAsset.getAcquisitionType()); // 取得区分
                assetResult.setAcquisitionTypeText(choiceMap.get(MST_ASSET_ACQUISITION_TYPE + String.valueOf(mstAsset.getAcquisitionType())));
                assetResult.setAcquisitionDate(mstAsset.getAcquisitionYyyymm()); // 取得年月
                assetResult.setMoldMachineType(mstAsset.getMoldMachineType()); // 金型・設備タイプ
                assetResultList.add(assetResult);

            }
            mstAssetList.setMstAssetList(assetResultList);
        }

        return mstAssetList;
    }

    /**
     * 照合バッチ実行起動
     *
     * @param langId
     * @return
     */
    public BasicResponse startMatchingBatch(String langId) {

        BasicResponse basicResponse = new BasicResponse();
        // バッチを起動する。
        JobOperator job = BatchRuntime.getJobOperator();

        List<Long> karteBatchlets = null;

        try {
            karteBatchlets = job.getRunningExecutions(ASSET_MATCHING_BATCHLET);
        } catch (NoSuchJobException ne) {
            //do nothing
        }
        if (karteBatchlets != null && karteBatchlets.size() > 0) {
            // エラーメッセージのキーが設定待ち
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_mold_machine_processing"));
        } else {
            // バッチスタート
            job.start(ASSET_MATCHING_BATCHLET, null);
        }
        return basicResponse;
    }

    /**
     * フレッシュボタンをクリックして、バッチステータスを判断し、照合結果を画面に表示する。
     *
     * @param langId
     * @return
     */
    public TblAssetMatchingResultVoList refreshAssetMatchingBatch(String langId) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" SELECT result FROM TblAssetMatchingResult result "
                //+ " LEFT JOIN FETCH result.tblAssetMatchingResultDetailCollection detail "
                + " LEFT JOIN FETCH result.mstAsset mstAsset "
                + " LEFT JOIN FETCH result.tblAssetMatchingBatch batchStatus   "
                //+ " LEFT JOIN FETCH detail.mstMold mstMold "
                + " LEFT JOIN FETCH result.mstAsset.mstItem mstItem "
                + " LEFT JOIN FETCH result.mstAsset.mstMgmtLocation mstMgmtLocation "
                + " LEFT JOIN FETCH result.mstAsset.mstMgmtCompany mstMgmtCompany "
                + " ORDER BY mstAsset.mstAssetPK.assetNo ASC ,mstAsset.mstAssetPK.branchNo ASC");
        Query query = entityManager.createQuery(sqlBuilder.toString());

        List list = query.getResultList();

        TblAssetMatchingResultVoList tblAssetMatchingResultVoList = new TblAssetMatchingResultVoList();
        List<TblAssetMatchingResultVo> tblAssetMatchingResultVos = new ArrayList<>();
        if (list != null && list.size() > 0) {
            TblAssetMatchingResultVo tblAssetMatchingResultVo;
            MstAssetVo mstAssetVo;

            String[] choiceArray = {MST_ASSET_ASSET_TYPE, MST_ASSET_ACQUISITION_TYPE};
            Map<String, String> choiceMap = mstChoiceService.getChoiceMap(langId, choiceArray);

            // 文言：正常終了など。
            List<String> dictKeyList = Arrays.asList(NOT_APPLICABLE, APPLICABLE, MULTIPLE_APPLICABLE);
            Map<String, String> dictKey = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);

            for (Object obj : list) {
                //
                TblAssetMatchingResult tblAssetMatchingResult = (TblAssetMatchingResult) obj;

                //
                tblAssetMatchingResultVo = new TblAssetMatchingResultVo();

                //資産項目をセット
                MstAsset mstAsset = tblAssetMatchingResult.getMstAsset();
                mstAssetVo = new MstAssetVo();
                mstAssetVo.setUuid(mstAsset.getUuid()); // 資産ID
                mstAssetVo.setAssetNo(mstAsset.getMstAssetPK().getAssetNo()); // 資産番号
                mstAssetVo.setBranchNo(mstAsset.getMstAssetPK().getBranchNo()); // 補助番号
                mstAssetVo.setAssetType(mstAsset.getAssetType()); // 資産種類
                mstAssetVo.setAssetTypeText(choiceMap.get(MST_ASSET_ASSET_TYPE + mstAsset.getAssetType()));
                mstAssetVo.setAssetName(mstAsset.getAssetName()); // 資産名称
                mstAssetVo.setMgmtCompanyCode(mstAsset.getMgmtCompanyCode()); // 管理先コード
                MstMgmtLocation mstMgmtLocation = mstAsset.getMstMgmtLocation();
                mstAssetVo.setMgmtLocationName(mstMgmtLocation != null ? mstMgmtLocation.getMgmtLocationName() : ""); // 設置場所
                mstAssetVo.setItemCode(mstAsset.getItemCode()); // 品目コード
                mstAssetVo.setAcquisitionType(mstAsset.getAcquisitionType()); // 取得区分
                mstAssetVo.setAcquisitionTypeText(choiceMap.get(MST_ASSET_ACQUISITION_TYPE + String.valueOf(mstAsset.getAcquisitionType())));
                mstAssetVo.setAcquisitionDate(mstAsset.getAcquisitionYyyymm()); // 取得年月
                mstAssetVo.setMoldMachineType(mstAsset.getMoldMachineType()); // 金型・設備タイプ

                tblAssetMatchingResultVo.setMstAssetVo(mstAssetVo);

                //照合結果セット
                switch (tblAssetMatchingResult.getMatchingResult()) {
                    case 1:
                        tblAssetMatchingResultVo.setMatchingResult(dictKey.get(APPLICABLE));
                        break;
                    case 2:
                        tblAssetMatchingResultVo.setMatchingResult(dictKey.get(MULTIPLE_APPLICABLE));
                        break;
                    default:
                        tblAssetMatchingResultVo.setMatchingResult(dictKey.get(NOT_APPLICABLE));
                        break;
                }

                //照合金型をセット
                if (tblAssetMatchingResult.getTblAssetMatchingResultDetailCollection() != null) {

                    Iterator detailCollection = tblAssetMatchingResult.getTblAssetMatchingResultDetailCollection().iterator();
                    List<TblMoldMachineAssetRelationVo> tblMoldMachineAssetRelationVos = new ArrayList();

                    TblMoldMachineAssetRelationVo tblMoldMachineAssetRelationVo;
                    while (detailCollection.hasNext()) {
                        tblMoldMachineAssetRelationVo = new TblMoldMachineAssetRelationVo();
                        TblAssetMatchingResultDetail tblAssetMatchingResultDetail = (TblAssetMatchingResultDetail) detailCollection.next();

                        MstMold mstMold = tblAssetMatchingResultDetail.getMstMold();
                        tblMoldMachineAssetRelationVo.setMoldUuid(mstMold.getUuid());
                        tblMoldMachineAssetRelationVo.setMoldId(mstMold.getMoldId());
                        tblMoldMachineAssetRelationVo.setName(mstMold.getMoldName());

                        //部品コードを頭一件を取得
                        if (mstMold.getMstMoldComponentRelationCollection() != null) {
                            Iterator relationCollection = mstMold.getMstMoldComponentRelationCollection().iterator();
                            MstMoldComponentRelation mstMoldComponentRelation = (MstMoldComponentRelation) relationCollection.next();
                            tblMoldMachineAssetRelationVo.setComponentCode(mstMoldComponentRelation.getMstComponent().getComponentCode());
                        }

                        tblMoldMachineAssetRelationVo.setCompanyName(mstMold.getCompanyName()); // 会社名称
                        tblMoldMachineAssetRelationVo.setLocationName(mstMold.getLocationName()); // 所在地名称
                        tblMoldMachineAssetRelationVo.setInstallationSiteName(mstMold.getInstllationSiteName()); // 設置場所名称
                        tblMoldMachineAssetRelationVo.setMainFlg(0); // 一覧表示フラグ

                        tblMoldMachineAssetRelationVos.add(tblMoldMachineAssetRelationVo);
                    }
                    tblAssetMatchingResultVo.setTblMoldMachineAssetRelationVos(tblMoldMachineAssetRelationVos);
                }

                tblAssetMatchingResultVos.add(tblAssetMatchingResultVo);
            }

            // バッチデータを取得
            TblAssetMatchingResult tblAssetMatchingResult = (TblAssetMatchingResult) list.get(0);
            if (tblAssetMatchingResult.getTblAssetMatchingBatch() != null && tblAssetMatchingResult.getTblAssetMatchingBatch().getEndDate() != null) {
                tblAssetMatchingResultVoList.setBatchEndTime(DateFormat.dateToStrMill(tblAssetMatchingResult.getTblAssetMatchingBatch().getEndDate()));
            }
        } else {
            return getInit();
        }

        tblAssetMatchingResultVoList.setTblAssetMatchingResultVo(tblAssetMatchingResultVos);
        return tblAssetMatchingResultVoList;
    }

    /**
     * 登録した照合データを保存する。
     *
     * @param tblAssetMatchingResultVoList
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse postAsset(TblAssetMatchingResultVoList tblAssetMatchingResultVoList, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();

        if (tblAssetMatchingResultVoList != null && tblAssetMatchingResultVoList.getTblAssetMatchingResultVo() != null) {

            for (int i = 0; i < tblAssetMatchingResultVoList.getTblAssetMatchingResultVo().size(); i++) {

                TblAssetMatchingResultVo tblAssetMatchingResultVo = tblAssetMatchingResultVoList.getTblAssetMatchingResultVo().get(i);

                MstAssetVo mstAssetVo = tblAssetMatchingResultVo.getMstAssetVo();

                if (mstAssetVo != null && tblAssetMatchingResultVo.getTblMoldMachineAssetRelationVos() != null && tblAssetMatchingResultVo.getTblMoldMachineAssetRelationVos().size() > 0) {

                    //資産マスタを更新
                    Query query = entityManager.createNamedQuery("MstAsset.findByUuid");
                    query.setParameter("uuid", mstAssetVo.getUuid());

                    try {
                        // 資産存在のみ関係マスタを更新する
                        MstAsset mstAsset = (MstAsset) query.getSingleResult();

                        if (mstAsset != null) {
                            Date sysDate = new Date();
                            mstAsset.setMoldMachineType(mstAssetVo.getMoldMachineType());
                            mstAsset.setUpdateDate(sysDate);
                            mstAsset.setUpdateUserUuid(loginUser.getUserUuid());
                            entityManager.merge(mstAsset);

                            // 古資産関係を削除してから追加する
                            Query delQuery = entityManager.createNamedQuery("TblMoldMachineAssetRelation.deleteByAssetId");
                            delQuery.setParameter("assetId", mstAssetVo.getUuid());
                            delQuery.executeUpdate();

                            // 資産関係テーブルを更新
                            for (int j = 0; j < tblAssetMatchingResultVo.getTblMoldMachineAssetRelationVos().size(); j++) {

                                TblMoldMachineAssetRelationVo tblMoldMachineAssetRelationVo = tblAssetMatchingResultVo.getTblMoldMachineAssetRelationVos().get(j);

                                TblMoldMachineAssetRelation tblMoldMachineAssetRelation = new TblMoldMachineAssetRelation();

                                tblMoldMachineAssetRelation.setUuid(IDGenerator.generate());
                                tblMoldMachineAssetRelation.setAssetId(mstAssetVo.getUuid());

                                if (StringUtils.isNotEmpty(tblMoldMachineAssetRelationVo.getMachineUuid())) {
                                    tblMoldMachineAssetRelation.setMachineUuid(tblMoldMachineAssetRelationVo.getMachineUuid());
                                }
                                if (StringUtils.isNotEmpty(tblMoldMachineAssetRelationVo.getMoldUuid())) {
                                    tblMoldMachineAssetRelation.setMoldUuid(tblMoldMachineAssetRelationVo.getMoldUuid());
                                }
                                tblMoldMachineAssetRelation.setMainFlg(tblMoldMachineAssetRelationVo.getMainFlg());
                                tblMoldMachineAssetRelation.setCreateDate(sysDate);
                                tblMoldMachineAssetRelation.setCreateUserUuid(loginUser.getUserUuid());
                                tblMoldMachineAssetRelation.setUpdateDate(sysDate);
                                tblMoldMachineAssetRelation.setUpdateUserUuid(loginUser.getUserUuid());

                                entityManager.persist(tblMoldMachineAssetRelation);
                            }

                            entityManager.flush();
                            entityManager.clear();

                        }

                    } catch (NoResultException e) {
                    }

                }
            }
        }
        return basicResponse;
    }

    /**
     * 画面描画
     *
     * @return
     */
    public TblAssetMatchingResultVoList getInit() {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" SELECT t FROM TblAssetMatchingBatch t ORDER BY t.startDate DESC, t.endDate DESC ");
        Query query = entityManager.createQuery(sqlBuilder.toString());

        query.setMaxResults(1);
        List list = query.getResultList();

        TblAssetMatchingResultVoList tblAssetMatchingResultVoList = new TblAssetMatchingResultVoList();

        if (list != null && list.size() > 0) {

            TblAssetMatchingBatch tblAssetMatchingBatch = (TblAssetMatchingBatch) list.get(0);
            if (tblAssetMatchingBatch.getEndDate() != null) {
                tblAssetMatchingResultVoList.setBatchEndTime(DateFormat.dateToStrMill(tblAssetMatchingBatch.getEndDate()));
            }

        }

        return tblAssetMatchingResultVoList;
    }
}
