/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.lot.balance;

import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.production.detail.TblProductionDetail;
import com.kmcj.karte.resources.production.detail.TblProductionDetailService;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 *
 * @author f.kitaoji
 */
@Dependent
public class LotBalanceUpdater {
    
    private List<LotBalanceUpdate> lotBalanceUpdateList = new ArrayList();

    @Inject
    private TblProductionLotBalanceService lotBalanceService;
    @Inject
    private TblProductionDetailService productionDetailService;
    
    public void addBalance(String productionDetailId, int completeCount) {
        for (LotBalanceUpdate lotBalanceUpdate : lotBalanceUpdateList) {
            if (lotBalanceUpdate.getProductionDetailId().equals(productionDetailId)) {
                lotBalanceUpdate.setCompleteCount(lotBalanceUpdate.getCompleteCount() + completeCount);
                return;
            }
        }
        LotBalanceUpdate newLotBalanceUpdate = new LotBalanceUpdate();
        newLotBalanceUpdate.setProductionDetailId(productionDetailId);
        newLotBalanceUpdate.setCompleteCount(completeCount);
        lotBalanceUpdateList.add(newLotBalanceUpdate);
    }
    
    @Transactional
    public void updateLotBalance(LoginUser loginUSer) {
        for (LotBalanceUpdate lotBalanceUpdate : lotBalanceUpdateList) {
            //生産実績明細取得
            TblProductionDetail productionDetail = productionDetailService.getProductionDetailSingleById(lotBalanceUpdate.getProductionDetailId());
            if (productionDetail == null) continue;
            //前回ロット残高テーブルから前回ロット残高レコード取得
            if (productionDetail.getPrevLotBalanceId() != null) {
                TblProductionLotBalance prevLotBalance = lotBalanceService.getProductionBalanceSingleById(productionDetail.getPrevLotBalanceId());
                if (prevLotBalance != null) {
                    //前回ロットがあるとき
                    if (prevLotBalance.getBalance() < 0) {
                        prevLotBalance.setBalance(0);
                    }
                    //前回ロット残高を更新
                    prevLotBalance.setBalance(prevLotBalance.getBalance() - lotBalanceUpdate.getCompleteCount()); //残高から今回生産数を控除
                    prevLotBalance.setNextCompleteCount(prevLotBalance.getFirstCompleteCount() - prevLotBalance.getBalance()); //次工程完成数に「当初完成数 - 更新後の残高」を設定
                    lotBalanceService.updateTblProductionLotBalance(prevLotBalance, loginUSer);
                }
            }
            //生産実績明細IDから今回のロット残高レコード取得
            TblProductionLotBalance lotBalance = lotBalanceService.getProductionBalanceSingleByDetailId(productionDetail);
            if (lotBalance == null) continue;
            //生産開始時は残高が-1なので完成数をそのままセット
            if (lotBalance.getBalance().equals(-1)) {
                lotBalance.setBalance(lotBalanceUpdate.getCompleteCount());
            }
            else {
                //機械日報2作成のたびに残高を増やす必要があるので加算
                lotBalance.setBalance(lotBalance.getBalance() + lotBalanceUpdate.getCompleteCount());
            }
            lotBalance.setFirstCompleteCount(lotBalance.getFirstCompleteCount() + lotBalanceUpdate.getCompleteCount()); //当初完成数の更新
            lotBalanceService.updateTblProductionLotBalance(lotBalance, loginUSer);
        }
    }
    
}
