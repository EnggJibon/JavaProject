/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.asset.MstAssetVo;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.asset.inventory.detail.TblInventoryDetail;
import com.kmcj.karte.resources.asset.inventory.detail.TblInventoryDetailVo;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

/**
 *
 * @author admin
 */
@Dependent
public class TblInventoryService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    private final static String SEPARATOR_STR = "\t";
    
    private static final String LINE_END = "\r\n";
    
    /**
     * 棚卸実施情報検索
     * <P>
     * 1:データ抽出中　2:開始　3:一部実施依頼済み　4:実施依頼済み　5:一部回収済み　6:回収済み　7:完了
     *
     * @param status
     * @param langId
     * @return
     */
    //@Transactional
    public TblInventoryVoList getTblInventoryList(int status, String langId) {

        TblInventoryVoList tblInventoryList = new TblInventoryVoList();

        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT tblInventory FROM TblInventory tblInventory ");
        if (status > 0) {
            sql.append(" WHERE tblInventory.status <> :status"); //完了以外
        }
        // 登録日の降順
        sql.append(" ORDER BY tblInventory.registrationDate DESC ");

        Query query = entityManager.createQuery(sql.toString());

        if (status > 0) {
            // 「6:回収済み」ステータスの追加　2017/07/28仕様変更
            query.setParameter("status", CommonConstants.INVENTORY_STATUS_COMPLETE);
        }

        List<TblInventoryVo> tblInventoryVoList = new ArrayList<>();

        List list = query.getResultList();

        if (list != null && list.size() > 0) {
            // ステータスの文言を取得
            Map<Integer, String> dictionaryMap = getDictionary(langId);

            for (Object obj : list) {
                TblInventory tblInventory = (TblInventory) obj;
                TblInventoryVo tblInventoryVo = new TblInventoryVo();

                tblInventoryVo.setUuid(tblInventory.getUuid());
                tblInventoryVo.setName(tblInventory.getName());
                tblInventoryVo.setStatus(tblInventory.getStatus());
                tblInventoryVo.setStatusText(dictionaryMap.get(tblInventory.getStatus()));
                tblInventoryVo.setRegistrationDate(tblInventory.getRegistrationDate());
                tblInventoryVo.setRequestedDate(tblInventory.getRequestedDate());
                tblInventoryVo.setReceivedMgmtCompanyCount(tblInventory.getReceivedMgmtCompanyCount());
                tblInventoryVo.setMgmtCompanyCount(tblInventory.getMgmtCompanyCount());
                tblInventoryVo.setCompletedDate(tblInventory.getCompletedDate());
                tblInventoryVo.setFinalDueDate(tblInventory.getFinalDueDate());

                tblInventoryVoList.add(tblInventoryVo);
            }
        }
        tblInventoryList.setTblInventoryVos(tblInventoryVoList);
        return tblInventoryList;
    }

    /**
     * 棚卸実施を追加
     *
     * @param inventoryName
     * @param loginUserUuid
     * @return
     */
    @Transactional
    public String postTblInventory(String inventoryName, String loginUserUuid) {

        String uuid = IDGenerator.generate();
        // １-1、棚卸の開始が宣言され、情報を棚卸実施テーブルに追加されること
        TblInventory tblInventory = new TblInventory();
        tblInventory.setUuid(uuid); // Uuid 強制設定
        tblInventory.setName(inventoryName); // 棚卸実施名称
        tblInventory.setRegistrationDate(new Date()); // 登録日
        // ステータスを「1:データ抽出中」にする。
        tblInventory.setStatus(CommonConstants.INVENTORY_STATUS_LOADING); // ステータス
        tblInventory.setCreateDate(new Date()); // 作成日
        tblInventory.setUpdateDate(new Date()); // 更新日
        tblInventory.setCreateUserUuid(loginUserUuid); // 作成者
        tblInventory.setUpdateUserUuid(loginUserUuid); // 更新者
        entityManager.persist(tblInventory);

        return uuid;
    }

    /**
     * 棚卸変更データ出力
     *
     * @param inventoryId
     * @param loginUser
     * @return
     */
    @Transactional
    public FileReponse getChangeTblInventoryData(String inventoryId, LoginUser loginUser) {

        FileReponse fileResponse = new FileReponse();
        
        List<TblInventoryDetail> tblInventoryDetailList = getTblInventoryChangeData(inventoryId);
        
        // 変更データが見つからないとき、ファイルを出力しなく、メッセージをリターンする
        if (tblInventoryDetailList == null || tblInventoryDetailList.size() <= 0) {
            fileResponse.setError(true);
            fileResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            fileResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
            return fileResponse;
        }
        
        String uuid = IDGenerator.generate();
        String txtCsvPath = FileUtil.outTxtFile(kartePropertyService, uuid);

        try {
            Writer writer = new FileWriter(txtCsvPath);

            StringBuilder writeStr = new StringBuilder();

            for (TblInventoryDetail detail : tblInventoryDetailList) {

                writeStr.append(detail.getMstAsset().getMstAssetPK().getAssetNo());
                writeStr.append(SEPARATOR_STR);
                writeStr.append(detail.getMstAsset().getMstAssetPK().getBranchNo());
                writeStr.append(SEPARATOR_STR);
                writeStr.append(detail.getMstAsset().getMstItem().getItemCode());
                writeStr.append(SEPARATOR_STR);
                writeStr.append((detail.getNewMgmtLocationCode() != null && !detail.getNewMgmtLocationCode().isEmpty())
                        ? detail.getNewMgmtLocationCode() : detail.getMstAsset().getMgmtLocationCode());
                writeStr.append(SEPARATOR_STR);
                writeStr.append((detail.getNewMgmtCompanyCode() != null && !detail.getNewMgmtCompanyCode().isEmpty()) 
                        ? detail.getNewMgmtCompanyCode() : detail.getMstAsset().getMgmtCompanyCode());
                writeStr.append(LINE_END);
            }
            writer.write(writeStr.toString());
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(TblInventoryService.class.getName()).log(Level.SEVERE, null, ex);
        }

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_INVENTORY);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_INVENTORY_EXECUTION);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "change_data");

        tblCsvExport.setClientFileName(FileUtil.getTxtFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fileResponse.setFileUuid(uuid);

        return fileResponse;
    }

    /**
     * 変更データ抽出SQL
     *
     * @param inventoryId
     * @return
     */
    private List getTblInventoryChangeData(String inventoryId) {
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT t "
                + " FROM TblInventoryDetail t "
                + " JOIN FETCH t.tblInventory ti "
                + " WHERE t.tblInventoryDetailPK.inventoryId = :inventoryId "
                + " AND (t.newMgmtLocationCode IS NOT NULL "
                + " OR t.newMgmtCompanyCode IS NOT NULL) ");

        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("inventoryId", inventoryId);

        return query.getResultList();
    }

    /**
     * 棚卸実施データを削除する
     *
     * @param inventoryId
     * @param langId
     * @return
     */
    @Transactional
    public BasicResponse deleteTblInventory(String inventoryId, String langId) {

        BasicResponse response = new BasicResponse();

        // 対象棚卸実施データを抽出
        Query selectQuery = entityManager.createNamedQuery("TblInventory.findByUuid");
        selectQuery.setParameter("uuid", inventoryId);

        try {
            TblInventory tblInventory = (TblInventory) selectQuery.getSingleResult();

            // ステータスが「1：データ抽出中」以外の場合、削除する
            if (tblInventory.getStatus() == CommonConstants.INVENTORY_STATUS_LOADING) {
                // ステータスが「1：データ抽出中」の場合、エラーにする。
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                //抽出中であるため、削除できません。
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_ststus_data_extraction_not_delete"));

            } else {
                Query deleteQuery = entityManager.createNamedQuery("TblInventory.deleteByUuid");
                deleteQuery.setParameter("uuid", inventoryId);
                deleteQuery.executeUpdate();

            }
        } catch (NoResultException e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            // 他人により削除された
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_data_deleted"));

        }
        return response;
    }
    
    /**
     * 棚卸情報を取得
     *
     * @param inventoryId
     * @return
     */
    public TblInventory getInventoryById(String inventoryId) {

        Query query = entityManager.createNamedQuery("TblInventory.findByUuid");

        query.setParameter("uuid", inventoryId);

        try {

            return (TblInventory) query.getSingleResult();

        } catch (NoResultException e) {
            return null;
        }

    }

    /**
     * 1:データ抽出中　2:開始　3:一部実施依頼済み　4:実施依頼済み　5:一部回収済み　6:回収済み　7:完了
     *
     * @return
     */
    private Map<Integer, String> getDictionary(String langId) {
        Map<Integer, String> dictionariesMap = new HashMap<>();
        List listKey = new ArrayList();
        listKey.add("inventory_status_data_extraction");
        listKey.add("inventory_status_action");
        listKey.add("inventory_status_partial_execution_requested");
        listKey.add("inventory_status_execution_requested");
        listKey.add("inventory_status_partially_collected");
        // 「6:回収済み」ステータスの追加　2017/07/28仕様変更
        listKey.add("inventory_status_collected");
        listKey.add("inventory_status_completed");
        Map<String, String> map = FileUtil.getDictionaryList(mstDictionaryService, langId, listKey);
        // 1:データ抽出中
        dictionariesMap.put(CommonConstants.INVENTORY_STATUS_LOADING, map.get("inventory_status_data_extraction"));
        // 2:開始
        dictionariesMap.put(CommonConstants.INVENTORY_STATUS_START, map.get("inventory_status_action"));
        // 3:一部実施依頼済み
        dictionariesMap.put(CommonConstants.INVENTORY_STATUS_PART_REQUEST, map.get("inventory_status_partial_execution_requested"));
        // 4:実施依頼済み
        dictionariesMap.put(CommonConstants.INVENTORY_STATUS_ALL_REQUEST, map.get("inventory_status_execution_requested"));
        // 5:一部回収済み
        dictionariesMap.put(CommonConstants.INVENTORY_STATUS_PART_RECEIVE, map.get("inventory_status_partially_collected"));
        // 6:回収済み
        dictionariesMap.put(CommonConstants.INVENTORY_STATUS_ALL_RECEIVE, map.get("inventory_status_collected"));
        // 7:完了
        dictionariesMap.put(CommonConstants.INVENTORY_STATUS_COMPLETE, map.get("inventory_status_completed"));

        return dictionariesMap;
    }

    /**
     * KM-336棚卸実施登録で抽出条件追加
     *
     * @param inventoryId
     * @param mstAssetList
     */
    @Transactional
    public void postWkAssetInventory(String inventoryId, List<MstAssetVo> mstAssetList) {
        //KM-336棚卸実施登録で抽出条件追加
        if (mstAssetList != null && mstAssetList.size() > 0) {
            TblInventoryAssetClassCond tblInventoryAssetClassCond;
            for (int i = 0; i < mstAssetList.size(); i++) {
                MstAssetVo mstAssetVo = mstAssetList.get(i);

                tblInventoryAssetClassCond = new TblInventoryAssetClassCond();
                TblInventoryAssetClassCondPK pk = new TblInventoryAssetClassCondPK();

                pk.setAssetClass(mstAssetVo.getAssetClass());
                pk.setInventoryId(inventoryId);
                tblInventoryAssetClassCond.setTblInventoryAssetClassCondPK(pk);

                entityManager.persist(tblInventoryAssetClassCond);

                if (i % 50 == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
        }

    }
    
    /**
     * 棚卸の資産情報を取得
     * 
     * @param inventoryId
     * 
     * @return
     */
    public List<TblInventoryDetailVo> getMgmtLoctionList(String inventoryId) {

        List<TblInventoryDetailVo> result = new ArrayList<TblInventoryDetailVo>();

        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT inventoryDetail ");
        sql.append(" FROM TblInventoryDetail inventoryDetail ");
        sql.append(" JOIN FETCH inventoryDetail.mstAsset mstAsset ");
        sql.append(" LEFT JOIN FETCH mstAsset.mstMgmtLocation mstMgmtLocation ");
        sql.append(" LEFT JOIN FETCH mstAsset.mstItem mstItem ");
        sql.append(" LEFT JOIN FETCH inventoryDetail.mstMgmtLocation newMstMgmtLocation ");
        sql.append(" LEFT JOIN FETCH inventoryDetail.mstMgmtCompany newMstMgmtCompany ");
        sql.append(" WHERE inventoryDetail.newAddedMgmtLocation = 1 ");

        // 棚卸実施ID
        if (!StringUtils.isEmpty(inventoryId)) {
            sql.append("AND inventoryDetail.tblInventoryDetailPK.inventoryId = :inventoryId ");
        }

        sql.append(" ORDER BY mstAsset.mstAssetPK.assetNo, mstAsset.mstAssetPK.branchNo");

        Query query = entityManager.createQuery(sql.toString());

        // パラーメタ設定
        if (!StringUtils.isEmpty(inventoryId)) {
            query.setParameter("inventoryId", inventoryId);
        }

        List list = query.getResultList();

        if (list.size() > 0) {

            for (TblInventoryDetail tblInventoryDetail : (List<TblInventoryDetail>) list) {
                
                TblInventoryDetailVo tblInventoryDetailVo = new TblInventoryDetailVo();
                
                tblInventoryDetailVo.setMgmtLocationCode(tblInventoryDetail.getNewMgmtLocationCode());
                
                if(null != tblInventoryDetail.getMstMgmtLocation()){
                    tblInventoryDetailVo.setMgmtLocationName(tblInventoryDetail.getMstMgmtLocation().getMgmtLocationName());
                }
                
                tblInventoryDetailVo.setAssetNo(tblInventoryDetail.getMstAsset().getMstAssetPK().getAssetNo());
                tblInventoryDetailVo.setBranchNo(tblInventoryDetail.getMstAsset().getMstAssetPK().getBranchNo());
                
                tblInventoryDetailVo.setItemCode(tblInventoryDetail.getMstAsset().getItemCode());
                if(null != tblInventoryDetail.getMstAsset().getMstItem()){
                    tblInventoryDetailVo.setItemName(tblInventoryDetail.getMstAsset().getMstItem().getItemName());
                }
                
                tblInventoryDetailVo.setMgmtCompanyCode(tblInventoryDetail.getMstAsset().getMgmtCompanyCode());
                if(null != tblInventoryDetail.getMstAsset().getMstMgmtCompany()){
                    tblInventoryDetailVo.setMgmtCompanyName(tblInventoryDetail.getMstAsset().getMstMgmtCompany().getMgmtCompanyName());
                }

                result.add(tblInventoryDetailVo);
                
            }

        }

        return result;
    }
    
    /**
     * 棚卸ステータスを更新
     *
     * @param inventoryId
     * @param completeFlag
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse updTblInventoryStatus(String inventoryId, Boolean completeFlag, LoginUser loginUser) {

        BasicResponse response = new BasicResponse();

        // 変更データ出力のとき、対象棚卸実施データを完了する。 20170731
        if (completeFlag) {
            TblInventory tblInventory = entityManager.find(TblInventory.class, inventoryId);
            if (tblInventory != null) {
                tblInventory.setCompletedDate(new Date());
                tblInventory.setStatus(CommonConstants.INVENTORY_STATUS_COMPLETE); // 完了にする

                tblInventory.setUpdateDate(new Date());
                tblInventory.setUpdateUserUuid(loginUser.getUserUuid());

                entityManager.merge(tblInventory);
            }
        }

        return response;
    }

}
