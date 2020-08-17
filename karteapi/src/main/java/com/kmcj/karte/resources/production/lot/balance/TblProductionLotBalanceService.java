/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.lot.balance;

import com.kmcj.karte.resources.work.*;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.procedure.MstProcedureList;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import com.kmcj.karte.resources.production.TblProduction;
import com.kmcj.karte.resources.production.detail.TblProductionDetail;
import com.kmcj.karte.util.IDGenerator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 * 生産実績ロット残高サービス
 * @author t.ariki
 */
@Dependent
public class TblProductionLotBalanceService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    MstProcedureService mstProcedureService;
    
    private Logger logger = Logger.getLogger(TblProductionLotBalanceService.class.getName());
    /**
     * 生産実績ロット残高テーブルリスト取得 残高1以上
     *
     * @param tblProductionLotBalance
     * @param componentIds
     * @return
     */
    public TblProductionLotBalanceList getProductionLotBalancesDistinctByLotNumber(TblProductionLotBalance tblProductionLotBalance, String componentIds) {
        /* 
         * JPQLで条件定義
         *   結合条件はエンティティクラスに定義済み
         */
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT tblProductionLotBalance FROM TblProductionLotBalance tblProductionLotBalance WHERE 1=1 "
        );
        sql.append(" AND tblProductionLotBalance.balance >= :balance");

        /*
         * 任意の検索条件を指定
         */
        if (tblProductionLotBalance.getProcessNumber() != null) {
            sql.append(" AND tblProductionLotBalance.processNumber = :processNumber");
        }

        if (StringUtils.isNotEmpty(componentIds)) {
            sql.append(" AND tblProductionLotBalance.componentId in :componentIdsList");
        }

        sql.append(" ORDER BY tblProductionLotBalance.lotNumber DESC ");
        Query query = entityManager.createQuery(sql.toString());

        if (tblProductionLotBalance.getProcessNumber() != null) {
            query.setParameter("processNumber", tblProductionLotBalance.getProcessNumber());
        }

        if (StringUtils.isNotEmpty(componentIds)) {
            String[] array = componentIds.split(",");
            List<String> componentIdsList = Arrays.asList(array);
            query.setParameter("componentIdsList", componentIdsList);
        }

        query.setParameter("balance", 1);
        List list = query.getResultList();

        /**
         * ロット番号が重複するデータは除外
         */
        ArrayList<String> lotNumbers = new ArrayList<>();
        ArrayList<TblProductionLotBalance> distinctTblProductionLotBalances = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                TblProductionLotBalance selectedTblProductionLotBalance = (TblProductionLotBalance) list.get(i);
                if (selectedTblProductionLotBalance.getLotNumber() != null) {
                    // 重複していなければ結果配列に格納
                    if (lotNumbers.indexOf(selectedTblProductionLotBalance.getLotNumber()) < 0) {
                        lotNumbers.add(selectedTblProductionLotBalance.getLotNumber());
                        distinctTblProductionLotBalances.add(selectedTblProductionLotBalance);
                    }
                }
            }
        }
        TblProductionLotBalanceList response = new TblProductionLotBalanceList();
        response.setTblProductionLotBalances(distinctTblProductionLotBalances);
        return response;
    }

    /**
     * ロット番号採番
     * @param prevLotNumber 前ロット番号
     * @return 
     */
    public String makeNewLotNumber(String prevLotNumber) {
        /**
         * ロット番号を採番
         * 　※生産実績ロット残高の工程番号にも採番した数値と同じ値を設定する
         */
        // 正規表現検索文字列
        String searchLotNumber = null;
        
        // 前ロット番号に値がない場合(画面から未指定を想定)
        if (prevLotNumber == null || "".equals(prevLotNumber)) {
            // 第一工程のデータとなる 20161221-01など子番号がついていない値となる
            // yyyyMMdd-NNを採番
            java.text.DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            searchLotNumber = dateFormat.format(new Date());
        }
        // 前ロット番号に値がある場合はそのまま検索条件に使用
        else {
            searchLotNumber = prevLotNumber;
        }
        
        // LIKE検索で現在の番号を取得
        String maxLotNumber = getMaxLotNumber(searchLotNumber);
        if (maxLotNumber != null) {
            try  {
                int currentLotNumberBranch = Integer.parseInt(maxLotNumber.replaceAll(searchLotNumber, "").replaceFirst("-", ""));
                currentLotNumberBranch ++;
                return searchLotNumber + "-" + String.format("%02d", currentLotNumberBranch);
            } catch (NumberFormatException numberFormatException) {
                Logger.getLogger(TblWorkResource.class.getName()).log(Level.FINE, null, "現在のロット番号最大値の値不正のため、強制で01を設定 maxLotNumber[" + maxLotNumber + "]"); 
                return searchLotNumber + "-" + "01";
            }
        }
        // 存在しない場合は01を付与して設定
        else {
            return searchLotNumber + "-" + "01";
        }
    }
    
    /**
     * ロット番号 正規表現LIKE検索 最大値取得
     *  指定されたロット番号の - NN であるロット番号の最大値を取得
     *   パラメータが「20160101」)なら ⇒ REGEX '20160101-[0-9]*$' で検索(第一工程用の番号検索を想定)
     *   パラメータが「20160101-01」なら ⇒ REGEX '20160101-01-[0-9]*$' で検索(第二工程以降の親ロット番号で検索を想定)
     * @param searchLotNumber
     * @return 
     */
    public String getMaxLotNumber(String searchLotNumber) {
        /* 
         * JPQLで条件定義
         *   結合条件はエンティティクラスに定義済み
         */
        StringBuilder sql;
        sql = new StringBuilder(
// 文字列ではMAXで100より99が大きいと判定されてしまうため、ロット番号を一旦全て取得しJAVA側で判定
            //"SELECT MAX(tblProductionLotBalance.lotNumber) FROM TblProductionLotBalance tblProductionLotBalance WHERE 1=1 "
            "SELECT tblProduction.lotNumber FROM TblProduction tblProduction WHERE 1=1 "
        );
        sql.append(" AND tblProduction.lotNumber REGEXP :searchLotNumber");
        Query query = entityManager.createQuery(sql.toString());
        query.setLockMode(LockModeType.PESSIMISTIC_READ);
        
        // 正規表現部分を追加
        String regexpSearchLotNumber = searchLotNumber + "-[0-9]*$";
        query.setParameter("searchLotNumber", regexpSearchLotNumber);
        
        List<String> lotNumbers = query.getResultList();
        if (lotNumbers == null || lotNumbers.size() < 1) {
            return  null;
        }
        String maxLotNumber = null;
        int maxLotNumberSuffix = 0;
        for (String lotNumber : lotNumbers) {
            try {
                // 末尾の数値部分を比較
                int tmpMaxLotNumberSuffix = Integer.parseInt(lotNumber.replaceAll(searchLotNumber, "").replaceFirst("-", ""));
                if (tmpMaxLotNumberSuffix >= maxLotNumberSuffix) {
                    maxLotNumber = lotNumber;
                    maxLotNumberSuffix = tmpMaxLotNumberSuffix;
                }
            } catch (NumberFormatException numberFormatException) {
                return null;
            }
        }
        return maxLotNumber;
    }
    
    /**
     * ロット番号で検索を行い工程番号の最大値+1を取得
     *   取得できない場合は1を返却
     * @param lotNumber
     * @return 
     */
    public int getMaxProcessNumber(String lotNumber) {
        /* 
         * JPQLで条件定義
         *   結合条件はエンティティクラスに定義済み
         */
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT MAX(tblProductionLotBalance.processNumber) FROM TblProductionLotBalance tblProductionLotBalance WHERE 1=1 "
        );
        sql.append(" AND tblProductionLotBalance.lotNumber = :lotNumber");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("lotNumber", lotNumber);
        
        try {
            Integer maxProcessNumber = (Integer)query.getSingleResult();
            if (maxProcessNumber == null || maxProcessNumber == 0) {
                return 1;
            }
            return ++maxProcessNumber;
        } catch(Exception e) {
            return 1;
        }
    }
    
    public TblProductionLotBalance getByLotNumber(String lotNumber, String componentId) {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT tblProductionLotBalance FROM TblProductionLotBalance tblProductionLotBalance WHERE 1=1 "
        );
        sql.append(" AND tblProductionLotBalance.lotNumber = :lotNumber ");
        sql.append(" AND tblProductionLotBalance.componentId = :componentId ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("lotNumber", lotNumber);
        query.setParameter("componentId", componentId);
        try {
            return (TblProductionLotBalance)query.getSingleResult();
        } catch(NoResultException e) {
            return null;
        } catch(NonUniqueResultException nonUniqueResultException) {
            nonUniqueResultException.printStackTrace();
            return null;
        }
    }
    
    public TblProductionLotBalanceList getByLotNumberAndMaxProcessNumber(String lotNumber, int maxProcessNumber) {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT tblProductionLotBalance FROM TblProductionLotBalance tblProductionLotBalance WHERE 1=1 "
        );
        sql.append(" AND tblProductionLotBalance.lotNumber = :lotNumber ");
        sql.append(" AND tblProductionLotBalance.processNumber = :processNumber ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("lotNumber", lotNumber);
        query.setParameter("processNumber", maxProcessNumber);
        
        List list = query.getResultList();
        TblProductionLotBalanceList response = new TblProductionLotBalanceList();
        response.setTblProductionLotBalances(list);
        return response;
    }
    
    /**
     * ロット番号、部品ID、工程番号最大値で1件取得
     * @param lotNumber
     * @param componentId
     * @param maxProcessNumber
     * @return 
     */
    public TblProductionLotBalanceList getByLotNumberAndComponentIdAndMaxProcessNumber(String lotNumber, String componentId, int maxProcessNumber) {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT tblProductionLotBalance FROM TblProductionLotBalance tblProductionLotBalance WHERE 1=1 "
        );
        sql.append(" AND tblProductionLotBalance.lotNumber = :lotNumber ");
        sql.append(" AND tblProductionLotBalance.componentId = :componentId ");
        sql.append(" AND tblProductionLotBalance.processNumber = :processNumber ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("lotNumber", lotNumber);
        query.setParameter("componentId", componentId);
        query.setParameter("processNumber", maxProcessNumber);
        
        List list = query.getResultList();
        TblProductionLotBalanceList response = new TblProductionLotBalanceList();
        response.setTblProductionLotBalances(list);
        return response;
    }
    
    /**
     * 生産実績ロット残高を生産実績明細IDで1件取得(TblProductionLotBalance型で返却)
     * @param productionDetailId TblProductionDetail型
     * @return 
     */
    public TblProductionLotBalance getProductionBalanceSingleByDetailId(TblProductionDetail productionDetailId) {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT tblProductionLotBalance FROM TblProductionLotBalance tblProductionLotBalance WHERE 1=1 "
        );
        sql.append(" AND tblProductionLotBalance.productionDetailId = :productionDetailId ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("productionDetailId", productionDetailId);
        try {
            return (TblProductionLotBalance)query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    /**
     * 生産実績ロット残高をIDで1件取得(TblProductionLotBalance型で返却)
     * @param id
     * @return 
     */
    public TblProductionLotBalance getProductionBalanceSingleById(String id) {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT tblProductionLotBalance FROM TblProductionLotBalance tblProductionLotBalance WHERE 1=1 "
        );
        sql.append(" AND tblProductionLotBalance.id = :id ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("id", id);
        try {
            return (TblProductionLotBalance)query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    /**
     * 生産開始入力登録データ設定
     * @param response
     * @param tblProduction
     * @param tblProductionDetail
     * @param lotNumber
     * @param loginUser
     * @return 
     */
    public TblProductionLotBalance setCreateDataForPoductionStart(
            BasicResponse response
           ,TblProduction tblProduction
           ,TblProductionDetail tblProductionDetail
           ,String lotNumber
           ,LoginUser loginUser
    ) { 
        TblProductionLotBalance tblProductionLotBalance = new TblProductionLotBalance();
        /**
         * 必須チェックおよび値設定
         */
        // 部品ID componentCode ⇒ componentId
        if (tblProductionDetail.getComponentId() == null || "".equals(tblProductionDetail.getComponentId())) {
            setApplicationError(response, loginUser, "msg_error_not_null", "tblProductionDetail.componentId");
            return tblProductionLotBalance;
        } else {
            tblProductionLotBalance.setComponentId(tblProductionDetail.getComponentId());
        }
        
        /**
         * 工程番号
         *   初期値 1
         *   ※前ロット番号が指定されている場合は、前ロット番号で残高テーブルを取得し、前ロットの工程番号+1を設定
         */
        if (tblProduction.getPrevLotNumber() != null && !"".equals(tblProduction.getPrevLotNumber())) {
            tblProductionLotBalance.setProcessNumber(getMaxProcessNumber(tblProduction.getPrevLotNumber()));
        } else {
            tblProductionLotBalance.setProcessNumber(1);
        }
        
        /**
         * 最終工程チェック
         *  自身が最終工程の場合は残高の登録は行わない
         *  制御フラグがすべてfalseのデータを返却
         */
        // 工程マスタ（部品ごとの製造手順）から最終データ取得
        MstProcedureList mstProcedureList = mstProcedureService.getFinalProcedure(tblProductionLotBalance.getComponentId());
        if (mstProcedureList.getMstProcedures() != null && !mstProcedureList.getMstProcedures().isEmpty()) {
            if (mstProcedureList.getMstProcedures().size() > 0) {
                MstProcedure finalMstProcedure = mstProcedureList.getMstProcedures().get(0);
                // 最終工程のSEQと、工程番号が同じ値の場合は最終工程
                if (finalMstProcedure.getSeq() == tblProductionLotBalance.getProcessNumber()) {
                    tblProductionLotBalance = new TblProductionLotBalance();
                    tblProductionLotBalance.setAdded(false);
                    tblProductionLotBalance.setModified(false);
                    tblProductionLotBalance.setDeleted(false);
                    Logger.getLogger(TblWorkResource.class.getName()).log(Level.FINE, "最終工程のため残高の登録をスキップ");
                    return tblProductionLotBalance;
                }
            }
        }
        
        /*
         * その他登録必須項目変換および設定
         */
        // ロット番号
        tblProductionLotBalance.setLotNumber(lotNumber);
        // 当初完成数
        tblProductionLotBalance.setFirstCompleteCount(0);
        // 次工程完成数
        tblProductionLotBalance.setNextCompleteCount(0);
        // 残高 -1
        tblProductionLotBalance.setBalance(-1);
        
        tblProductionLotBalance.setAdded(true); // 登録
        tblProductionLotBalance.setModified(false);
        tblProductionLotBalance.setDeleted(false);
                    
        return tblProductionLotBalance;
    }
    
    /**
     * 生産終了入力登録データ設定
     * @param response
     * @param tblProduction
     * @param tblProductionDetail
     * @param tblProductionLotBalance
     * @param loginUser
     * @return 
     */
    public TblProductionLotBalance setCreateDataForPoductionEnd(
            BasicResponse response
           ,TblProduction tblProduction
           ,TblProductionDetail tblProductionDetail
           ,TblProductionLotBalance tblProductionLotBalance
           ,LoginUser loginUser
    ) { 
        /**
         * 登録更新時の共通項目設定
         */
        setCommonData(response, tblProduction, tblProductionDetail, tblProductionLotBalance, loginUser);
        if (response.isError()) {
            return tblProductionLotBalance;
        }
        
        /**
         * 工程番号
         *   初期値 1
         *   ※前ロット番号が指定されている場合は、前ロット番号で残高テーブルを取得し、前ロットの工程番号+1を設定
         */
        if (tblProduction.getPrevLotNumber() != null && !"".equals(tblProduction.getPrevLotNumber())) {
            tblProductionLotBalance.setProcessNumber(getMaxProcessNumber(tblProduction.getPrevLotNumber()));
        } else {
            tblProductionLotBalance.setProcessNumber(1);
        }

        /*
         * その他登録必須項目変換および設定
         */
        // ロット番号
        tblProductionLotBalance.setLotNumber(tblProduction.getLotNumber());

// 残高は更新登録直前で再設定させる
//// 当初完成数
//tblProductionLotBalance.setFirstCompleteCount(500);
//// 次工程完成数
//tblProductionLotBalance.setNextCompleteCount(500);
//// 残高 -1
//tblProductionLotBalance.setBalance(500);
        
        // 制御フラグを設定
        tblProductionLotBalance.setAdded(true);  // 登録
        tblProductionLotBalance.setModified(false);
        tblProductionLotBalance.setDeleted(false);
        return tblProductionLotBalance;
    }
    
    /**
     * 生産終了入力更新データ設定
     * @param response
     * @param tblProduction
     * @param tblProductionDetail
     * @param tblProductionLotBalance
     * @param loginUser
     * @return 
     */
    public TblProductionLotBalance setUpdateDataForPoductionEnd(
            BasicResponse response
           ,TblProduction tblProduction
           ,TblProductionDetail tblProductionDetail
           ,TblProductionLotBalance tblProductionLotBalance
           ,LoginUser loginUser
    ) {
        /**
         * 登録更新時の共通項目設定
         */
        setCommonData(response, tblProduction, tblProductionDetail, tblProductionLotBalance, loginUser);
        if (response.isError()) {
            return tblProductionLotBalance;
        }
        
        /*
         * 計算
         */
// 残高は更新登録直前で再設定させる
//// 当初完成数
//tblProductionLotBalance.setFirstCompleteCount(500);
//// 次工程完成数
//tblProductionLotBalance.setNextCompleteCount(500);
//// 残高 -1
//tblProductionLotBalance.setBalance(500);
        
        // 制御フラグを設定
        tblProductionLotBalance.setAdded(false);
        tblProductionLotBalance.setModified(true); // 更新
        tblProductionLotBalance.setDeleted(false);
        return tblProductionLotBalance;
    }
    
    /**
     * 登録更新時の共通項目を設定
     * @param response
     * @param tblProduction
     * @param tblProductionDetail
     * @param tblProductionLotBalance
     * @param loginUser
     * @return 
     */
    public TblProductionLotBalance setCommonData(
            BasicResponse response
           ,TblProduction tblProduction
           ,TblProductionDetail tblProductionDetail
           ,TblProductionLotBalance tblProductionLotBalance
           ,LoginUser loginUser
    ) {
        /**
         * 必須チェックおよび値設定
         */
        // 部品ID componentCode ⇒ componentId
        if (tblProductionDetail.getComponentId() == null || "".equals(tblProductionDetail.getComponentId())) {
            setApplicationError(response, loginUser, "msg_error_not_null", "tblProductionDetail.componentId");
            return tblProductionLotBalance;
        } else {
            tblProductionLotBalance.setComponentId(tblProductionDetail.getComponentId());
        }
        
        /**
         * 明細行の操作を一部ここで行う
         */
        // 残高が未設定もしくは-1以下の場合(生産終了入力からの初更新)の場合、明細の計画未充当数に完成数をセット
        if (tblProductionLotBalance.getBalance() == null || tblProductionLotBalance.getBalance() < 0) {
            tblProductionDetail.setPlanNotAppropriatedCount(tblProductionDetail.getCompleteCount());
        }
        return tblProductionLotBalance;
    }
    
    /**
     * 生産実績ロット残高テーブル登録
     * @param tblProductionLotBalance
     * @param loginUser
     * @return 
     */
    @Transactional
    public TblProductionLotBalance createTblProductionLotBalance(TblProductionLotBalance tblProductionLotBalance, LoginUser loginUser) {
        // 登録処理時の強制設定パラメータセット
        tblProductionLotBalance.setId(IDGenerator.generate());
        tblProductionLotBalance.setCreateDate(new java.util.Date());
        tblProductionLotBalance.setCreateUserUuid(loginUser.getUserUuid());
        tblProductionLotBalance.setUpdateDate(tblProductionLotBalance.getCreateDate());
        tblProductionLotBalance.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.persist(tblProductionLotBalance);
        return tblProductionLotBalance;
    }
    
    /**
     * 生産実績ロット残高テーブル更新
     * @param tblProductionLotBalance
     * @param loginUser
     * @return 
     */
    @Transactional
    public TblProductionLotBalance updateTblProductionLotBalance(TblProductionLotBalance tblProductionLotBalance, LoginUser loginUser) {
        tblProductionLotBalance.setUpdateDate(new java.util.Date());
        tblProductionLotBalance.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.merge(tblProductionLotBalance);
        return tblProductionLotBalance;
    }
    
    /**
     * 生産実績ロット残高テーブル削除（生産実績明細ID指定)
     * @param productionDetailId
     * @param loginUser
     * @return 
     */
    @Transactional
    public void deleteTblProductionLotBalanceByProductionDetail(TblProductionDetail detail, LoginUser loginUser){
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        try{
            Query query = entityManager.createNamedQuery("TblProductionLotBalance.deleteByProductionDetail");
            query.setParameter("productionDetailId", detail);
            query.executeUpdate();
        }catch(Exception e){
            logger.log(Level.WARNING, e.toString());
            throw e;
        }
    }
    
    private BasicResponse setApplicationError(BasicResponse response, LoginUser loginUser, String dictKey, String errorContentsName) {
        setBasicResponseError(
                response
               ,true
               ,ErrorMessages.E201_APPLICATION
               ,mstDictionaryService.getDictionaryValue(loginUser.getLangId(), dictKey)
        );
        String logMessage = response.getErrorMessage() + ":" + errorContentsName;
        Logger.getLogger(TblWorkResource.class.getName()).log(Level.FINE, logMessage);
        return response;
    }
    
    private BasicResponse setBasicResponseError(BasicResponse response, boolean error, String errorCode, String errorMessage) {
        response.setError(error);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        return response;
    }
}
