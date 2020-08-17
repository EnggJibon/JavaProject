/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.defect;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.machine.dailyreport2.TblMachineDailyReport2ProdDetail;
import com.kmcj.karte.resources.production2.Production;
import com.kmcj.karte.resources.production2.ProductionDetail;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.IDGenerator;
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
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 *
 */
@Dependent
public class TblProductionDefectService {
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private CnfSystemService cnfSystemService;
    
    /**
     * 生産不良登録画面に不良数取得
     * @param productionDetailId
     * @return 
     */
    public TblProductionDefectList getProductionDefectList(String productionDetailId) {
        TblProductionDefectList res = new TblProductionDefectList();
        Query query = entityManager.createNamedQuery("TblProductionDefect.findByProductionDetailId");
        query.setParameter("productionDetailId", productionDetailId);
        List<TblProductionDefect> list = query.getResultList();
        res.setProductionDefects(list);
        return res;
    }

    /**
     * 日別生産不良登録画面に不良数取得
     * @param productionDetailId
     * @param reportDate
     * @return 
     */
    public TblProductionDefectList getMdrProdDetailDefectList(String productionDetailId, String reportDate) {
        TblProductionDefectList res = new TblProductionDefectList();
        
        Query query = entityManager.createNamedQuery("TblProductionDefect.findByProductionDetailId");
        query.setParameter("productionDetailId", productionDetailId);
        List<TblProductionDefect> list = query.getResultList();
        if (list != null && !list.isEmpty()) {
            java.util.Date paramReportDate = null;
            if (reportDate == null || reportDate.equals("")) {
                res.setError(true);
                res.setErrorCode(ErrorMessages.E201_APPLICATION);
                res.setErrorMessage("reportDate parmeter is required.");
                return res;
            } else {
                paramReportDate = DateFormat.strToDate(reportDate);
                if (paramReportDate == null) {
                    res.setError(true);
                    res.setErrorCode(ErrorMessages.E201_APPLICATION);
                    res.setErrorMessage("Invalid date format: reportDate.");
                    return res;
                }
            }
            List<String> prodDefectIds = new ArrayList<>();
            for (TblProductionDefect defect : list) {
                prodDefectIds.add(defect.getId());
            }
            res.setProductionDefectsDaily(getCurrentDayDailyInfo(paramReportDate, prodDefectIds));
        }
        
        return res;
    }
    
    /**
     * 生産不具合テーブルへデータを保存する
     * 
     * @param tblProductionDefectList
     * @param productionDateTime
     * @param userUuid
     * @return  
     */
    @Transactional
    public BasicResponse postProductionDefects(List<TblProductionDefect> tblProductionDefectList, String productionDateTime, String userUuid) {
        BasicResponse response = new BasicResponse();
        if (tblProductionDefectList != null && !tblProductionDefectList.isEmpty()) {
            Query query = entityManager.createNamedQuery("TblProductionDefect.findByProductionDetailId");
            query.setParameter("productionDetailId", tblProductionDefectList.get(0).getProductionDetailId());
            List<TblProductionDefect> list = query.getResultList();
            List<TblProductionDefect> beforeUpdDefectList = new ArrayList<TblProductionDefect>();
            
            List<String> prodDefectIds = new ArrayList<>();
            Date nowDate = new Date();
            
            int pDetailTotalDefect = 0;
            // 生産不具合テーブル登録
            if (list != null && !list.isEmpty()) {
                for (TblProductionDefect pd : tblProductionDefectList) {
                    boolean isFind = false;
                    for (TblProductionDefect tblProductionDefect : list) {
                        if (tblProductionDefect.getDefectSeq() == pd.getDefectSeq()) {
                            isFind = true;
                            prodDefectIds.add(tblProductionDefect.getId());
                            pDetailTotalDefect += pd.getQuantity();
                            TblProductionDefect beforeDefect = new TblProductionDefect();
                            beforeDefect.setDefectSeq(tblProductionDefect.getDefectSeq());
                            beforeDefect.setQuantity(tblProductionDefect.getQuantity());
                            beforeUpdDefectList.add(beforeDefect);
                            
                            pd.setId(tblProductionDefect.getId());
                            tblProductionDefect.setQuantity(pd.getQuantity());
                            tblProductionDefect.setUpdateUserUuid(userUuid);
                            tblProductionDefect.setUpdateDate(nowDate);
                            entityManager.merge(tblProductionDefect);
                            break;
                        }
                    }
                    if (!isFind) {
                        pd.setId(IDGenerator.generate());
                        prodDefectIds.add(pd.getId());
                        pDetailTotalDefect += pd.getQuantity();

                        pd.setCreateUserUuid(userUuid);
                        pd.setCreateDate(nowDate);
                        pd.setUpdateUserUuid(userUuid);
                        pd.setUpdateDate(nowDate);
                        entityManager.persist(pd);
                    }
                }
            } else {
                for (TblProductionDefect tblProductionDefect : tblProductionDefectList) {
                    tblProductionDefect.setId(IDGenerator.generate());
                    prodDefectIds.add(tblProductionDefect.getId());
                    pDetailTotalDefect += tblProductionDefect.getQuantity();
                    
                    tblProductionDefect.setCreateUserUuid(userUuid);
                    tblProductionDefect.setCreateDate(nowDate);
                    tblProductionDefect.setUpdateUserUuid(userUuid);
                    tblProductionDefect.setUpdateDate(nowDate);
                    entityManager.persist(tblProductionDefect);
                }
            }
            
            // 生産情報取得
            query = entityManager.createQuery("SELECT pDetail FROM ProductionDetail pDetail JOIN FETCH pDetail.productionId WHERE pDetail.id = :id");
            query.setParameter("id", tblProductionDefectList.get(0).getProductionDetailId());
            List<ProductionDetail> pd = query.getResultList();
            if (pd == null || pd.isEmpty()) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage("The production is not exist.");
                return response;
            }
            
            // 生産実績明細不良数更新
            updateProductionDetailDefectCount(pd.get(0), pDetailTotalDefect, userUuid);
            
            try {
                // 日別生産不具合テーブルへデータを保存する
                response = postProductionDefectsDaily(tblProductionDefectList, beforeUpdDefectList, prodDefectIds, productionDateTime, pd.get(0).getProductionId(), userUuid);
            } catch (Exception ex) {
                Logger.getLogger(TblProductionDefectService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return response;
    }
    
    /**
     * 日別生産不具合テーブルへデータを保存する
     * 
     * @param tblProductionDefectList
     * @param beforeUpdDefectList
     * @param prodDefectIds
     * @param productionDateTime
     * @param production
     * @param userUuid
     * @return 
     * @throws java.lang.Exception 
     */
    @Transactional
    public BasicResponse postProductionDefectsDaily(List<TblProductionDefect> tblProductionDefectList, List<TblProductionDefect> beforeUpdDefectList, List<String> prodDefectIds, String productionDateTime, Production production, String userUuid) throws Exception {
        BasicResponse response = new BasicResponse();
        CnfSystem cnfSystem =  cnfSystemService.findByKey("system", "business_start_time");
        Date reportDate = null;
        if (StringUtils.isEmpty(productionDateTime)) {
            reportDate = DateFormat.strToDate(DateFormat.getBusinessDate(DateFormat.getCurrentDateTime(), cnfSystem));
        } else {
            reportDate = DateFormat.strToDate(DateFormat.getBusinessDate(productionDateTime, cnfSystem));
        }

        boolean firstDay = checkProductionFirstDay(production);
        Date nowDate = new Date();
        
        int dailyReportDetailTotalDefect = 0;
        String dailyReportProdDetailId = null;
        if (firstDay) {
            List<TblProductionDefectDaily> dailyListByDate = getCurrentDayDailyInfo(reportDate, prodDefectIds);
            if (dailyListByDate != null && !dailyListByDate.isEmpty()) {
                for (TblProductionDefect pDefect : tblProductionDefectList) {
                    boolean isFind = false;
                    for (TblProductionDefectDaily tblProductionDefectDaily : dailyListByDate) {
                        if (tblProductionDefectDaily.getDefectSeq() == pDefect.getDefectSeq()) {
                            isFind = true;
                            dailyReportDetailTotalDefect += pDefect.getQuantity();
                            dailyReportProdDetailId = tblProductionDefectDaily.getMdr2ProdDetailId();
                            
                            tblProductionDefectDaily.setQuantity(pDefect.getQuantity());
                            tblProductionDefectDaily.setUpdateUserUuid(userUuid);
                            tblProductionDefectDaily.setUpdateDate(nowDate);
                            entityManager.merge(tblProductionDefectDaily);
                            break;
                        }
                    }
                    if (!isFind) {
                        TblProductionDefectDaily tblProductionDefectDaily = new TblProductionDefectDaily();
                        tblProductionDefectDaily.setId(IDGenerator.generate());
                        tblProductionDefectDaily.setProductionDetectId(pDefect.getId());
                        tblProductionDefectDaily.setDefectDate(reportDate);
                        tblProductionDefectDaily.setDefectSeq(pDefect.getDefectSeq());
                        tblProductionDefectDaily.setQuantity(pDefect.getQuantity());
                        tblProductionDefectDaily.setMdr2ProdDetailId(null);
                        tblProductionDefectDaily.setCreateUserUuid(userUuid);
                        tblProductionDefectDaily.setCreateDate(nowDate);
                        tblProductionDefectDaily.setUpdateUserUuid(userUuid);
                        tblProductionDefectDaily.setUpdateDate(nowDate);
                        entityManager.persist(tblProductionDefectDaily);
                    }
                }
            } else {
                for (TblProductionDefect pDefect : tblProductionDefectList) {
                    TblProductionDefectDaily tblProductionDefectDaily = new TblProductionDefectDaily();
                    tblProductionDefectDaily.setId(IDGenerator.generate());
                    tblProductionDefectDaily.setProductionDetectId(pDefect.getId());
                    tblProductionDefectDaily.setDefectDate(reportDate);
                    tblProductionDefectDaily.setDefectSeq(pDefect.getDefectSeq());
                    tblProductionDefectDaily.setQuantity(pDefect.getQuantity());
                    tblProductionDefectDaily.setMdr2ProdDetailId(null);
                    tblProductionDefectDaily.setCreateUserUuid(userUuid);
                    tblProductionDefectDaily.setCreateDate(nowDate);
                    tblProductionDefectDaily.setUpdateUserUuid(userUuid);
                    tblProductionDefectDaily.setUpdateDate(nowDate);
                    entityManager.persist(tblProductionDefectDaily);
                }
            }
        } else {
            Date tmpReportDate = reportDate;
            // 前日日別生産不具合情報取得
            List<TblProductionDefectDaily> dailyListByDate = getCurrentDayDailyInfo(reportDate, prodDefectIds);
            List<TblProductionDefectDaily> beforeDailyListByDate = null;
            while(beforeDailyListByDate == null || beforeDailyListByDate.isEmpty()) {
                tmpReportDate = DateFormat.getBeforeDay(tmpReportDate);
                if (DateFormat.daysBetween(production.getStartDatetime(), tmpReportDate) <= 0) {
                    beforeDailyListByDate = getCurrentDayDailyInfo(tmpReportDate, prodDefectIds);
                    break;
                }
                beforeDailyListByDate = getCurrentDayDailyInfo(tmpReportDate, prodDefectIds);
            }
            if (dailyListByDate != null && !dailyListByDate.isEmpty()) {
                for (TblProductionDefectDaily tblProductionDefectDaily : dailyListByDate) {
                    for (TblProductionDefect pDefect : tblProductionDefectList) {
                        if (tblProductionDefectDaily.getDefectSeq() == pDefect.getDefectSeq()) {
                            int minusQuantity = 0;
                            if (beforeDailyListByDate != null && !beforeDailyListByDate.isEmpty()) {
                                for (TblProductionDefectDaily beforeDaily : beforeDailyListByDate) {
                                    if (beforeDaily.getDefectSeq() == pDefect.getDefectSeq()) {
                                        minusQuantity = beforeDaily.getQuantity();
                                        break;
                                    }
                                }
                            }
                            
                            dailyReportDetailTotalDefect += (pDefect.getQuantity() - minusQuantity);
                            dailyReportProdDetailId = tblProductionDefectDaily.getMdr2ProdDetailId();
                            
                            tblProductionDefectDaily.setQuantity(pDefect.getQuantity() - minusQuantity);
                            tblProductionDefectDaily.setUpdateUserUuid(userUuid);
                            tblProductionDefectDaily.setUpdateDate(nowDate);
                            entityManager.merge(tblProductionDefectDaily);
                            break;
                        }
                    }
                }
            } else {
                int minusQuantity = 0;
                tmpReportDate = DateFormat.getAfterDay(tmpReportDate);
                for (TblProductionDefect pDefect : tblProductionDefectList) {
                    if (beforeUpdDefectList != null && !beforeUpdDefectList.isEmpty()) {
                        for (TblProductionDefect beforeDefect : beforeUpdDefectList) {
                            if (beforeDefect.getDefectSeq() == pDefect.getDefectSeq()) {
                                minusQuantity = beforeDefect.getQuantity();
                                break;
                            }
                        }
                    }
                    TblProductionDefectDaily tblProductionDefectDaily = new TblProductionDefectDaily();
                    tblProductionDefectDaily.setId(IDGenerator.generate());
                    tblProductionDefectDaily.setProductionDetectId(pDefect.getId());
                    tblProductionDefectDaily.setDefectDate(tmpReportDate);
                    tblProductionDefectDaily.setDefectSeq(pDefect.getDefectSeq());
                    tblProductionDefectDaily.setQuantity(pDefect.getQuantity() - minusQuantity);
                    tblProductionDefectDaily.setMdr2ProdDetailId(null);
                    tblProductionDefectDaily.setCreateUserUuid(userUuid);
                    tblProductionDefectDaily.setCreateDate(nowDate);
                    tblProductionDefectDaily.setUpdateUserUuid(userUuid);
                    tblProductionDefectDaily.setUpdateDate(nowDate);
                    entityManager.persist(tblProductionDefectDaily);
                }
                
                int zeroDefectDays = DateFormat.daysBetween(tmpReportDate, reportDate);
                for (int i = 0; i < zeroDefectDays; i++) {
                    for (TblProductionDefect pDefect : tblProductionDefectList) {
                        TblProductionDefectDaily tblProductionDefectDaily = new TblProductionDefectDaily();
                        tblProductionDefectDaily.setId(IDGenerator.generate());
                        tblProductionDefectDaily.setProductionDetectId(pDefect.getId());
                        tblProductionDefectDaily.setDefectDate(DateFormat.getAfterDays(tmpReportDate, 1 + i));
                        tblProductionDefectDaily.setDefectSeq(pDefect.getDefectSeq());
                        tblProductionDefectDaily.setQuantity(0);
                        tblProductionDefectDaily.setMdr2ProdDetailId(null);
                        tblProductionDefectDaily.setCreateUserUuid(userUuid);
                        tblProductionDefectDaily.setCreateDate(nowDate);
                        tblProductionDefectDaily.setUpdateUserUuid(userUuid);
                        tblProductionDefectDaily.setUpdateDate(nowDate);
                        entityManager.persist(tblProductionDefectDaily);
                    }
                }
            }
        }
        
        if (!StringUtils.isEmpty(dailyReportProdDetailId)) {
            // 機械日報生産明細情報取得
            Query query = entityManager.createNamedQuery("TblMachineDailyReport2ProdDetail.findById");
            query.setParameter("id", dailyReportProdDetailId);
            List<TblMachineDailyReport2ProdDetail> dailyReportProdDetailList = query.getResultList();
            if (dailyReportProdDetailList == null || dailyReportProdDetailList.isEmpty()) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage("The daily report is not exist.");
                return response;
            }

            // 機械日報生産実績明細不良数更新
            updateDailyReportDetailDefectCount(dailyReportProdDetailList.get(0), dailyReportDetailTotalDefect, userUuid);
        }
        
        return response;
    }
    
    private List<TblProductionDefectDaily> getCurrentDayDailyInfo(Date reportDate, List<String> prodDefectIds) {
        // 当日日別生産不具合データ取得
        StringBuilder sbSql = new StringBuilder();
        sbSql.append("SELECT daily FROM TblProductionDefectDaily daily ");
        sbSql.append("WHERE daily.defectDate = :defectDate ");
        sbSql.append("AND daily.productionDefectId IN :productionDefectIds ");
        Query query = entityManager.createQuery(sbSql.toString());
        query.setParameter("defectDate", reportDate);
        query.setParameter("productionDefectIds", prodDefectIds);
        return query.getResultList();
    }
    
    private ProductionDetail updateProductionDetailDefectCount(ProductionDetail pDetail, int pDetailTotalDefect, String userUuid) {
        pDetail.setDefectCount(pDetailTotalDefect);
        pDetail.setUpdateUserUuid(userUuid);
        pDetail.setUpdateDate(new Date());
        entityManager.merge(pDetail);
        return pDetail;
    }
    
    private TblMachineDailyReport2ProdDetail updateDailyReportDetailDefectCount(TblMachineDailyReport2ProdDetail dailyReportProdDetail, int dailyReportDetailTotalDefect, String userUuid) {
        dailyReportProdDetail.setDefectCount(dailyReportDetailTotalDefect);
        dailyReportProdDetail.setUpdateUserUuid(userUuid);
        dailyReportProdDetail.setUpdateDate(new Date());
        entityManager.merge(dailyReportProdDetail);
        return dailyReportProdDetail;
    }
    
    private boolean checkProductionFirstDay(Production production) throws Exception {
        //業務開始時刻、終了時刻の取得
        CnfSystem cnfSystem =  cnfSystemService.findByKey("system", "business_start_time");
        if (cnfSystem == null) {
            throw new Exception("Business start time is not defined in system setting.");
        }
        String businessStartTime = cnfSystem.getConfigValue();
        if (businessStartTime.length() == 4) {
            businessStartTime = "0" + businessStartTime;
        }
        java.util.Date startTime = DateFormat.strToDatetime(DateFormat.getCurrentDate() + " " + businessStartTime + ":00");
        java.util.Date endTime = DateFormat.getAfterDay(startTime);

        return production.getStartDatetime().compareTo(startTime) >= 0 && production.getStartDatetime().compareTo(endTime) <= 0;
    }
    
    @Transactional
    public BasicResponse postProductionDefectsDailyFromMdr(List<TblProductionDefectDaily> tblProductionDefectDailyList, Date reportDate, String productionDetailId, String userUuid) throws Exception {
        BasicResponse response = new BasicResponse();
        
        Query query = entityManager.createNamedQuery("TblProductionDefect.findByProductionDetailId");
        query.setParameter("productionDetailId", productionDetailId);
        List<TblProductionDefect> list = query.getResultList();
        
        
//        Date reportDate = DateFormat.strToDate(DateFormat.getBusinessDate(mdrDateTime, cnfSystem));
        // 生産情報取得
        query = entityManager.createQuery("SELECT pDetail FROM ProductionDetail pDetail JOIN FETCH pDetail.productionId WHERE pDetail.id = :id");
        query.setParameter("id", productionDetailId);
        List<ProductionDetail> pd = query.getResultList();
        if (pd == null || pd.isEmpty()) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage("The production is not exist.");
            return response;
        }
        boolean firstDay = checkProductionFirstDay(pd.get(0).getProductionId());
        
        if (firstDay) {
            if (list == null || list.isEmpty()) {
                for (TblProductionDefectDaily defectDaily : tblProductionDefectDailyList) {
                    TblProductionDefect defect = new TblProductionDefect();
                    defect.setId(IDGenerator.generate());
                    defect.setProductionDetailId(productionDetailId);
                    defect.setDefectSeq(defectDaily.getDefectSeq());
                    defect.setQuantity(defectDaily.getQuantity());
                    defect.setCreateDate(new Date());
                    defect.setCreateUserUuid(userUuid);
                    defect.setUpdateDate(defect.getCreateDate());
                    defect.setUpdateUserUuid(userUuid);
                    entityManager.persist(defect);
                    
                    defectDaily.setId(IDGenerator.generate());
                    defectDaily.setProductionDetectId(defect.getId());
                    defectDaily.setDefectDate(reportDate);
                    defectDaily.setCreateDate(new Date());
                    defectDaily.setCreateUserUuid(userUuid);
                    defectDaily.setUpdateDate(defect.getCreateDate());
                    defectDaily.setUpdateUserUuid(userUuid);
                    entityManager.persist(defectDaily);
                }
            } else {
                List<String> prodDefectIds = new ArrayList<>();
                for (TblProductionDefect defect : list) {
                    prodDefectIds.add(defect.getId());
                    for (TblProductionDefectDaily defectDaily : tblProductionDefectDailyList) {
                        if (defect.getDefectSeq() == defectDaily.getDefectSeq()) {
                            defect.setQuantity(defectDaily.getQuantity());
                            defect.setUpdateDate(new Date());
                            defect.setUpdateUserUuid(userUuid);
                            entityManager.merge(defect);
                            
                            defectDaily.setProductionDetectId(defect.getId());
                            break;
                        }
                    }
                }
                
                List<TblProductionDefectDaily> defectDailyList = getCurrentDayDailyInfo(reportDate, prodDefectIds);
                if (defectDailyList != null && !defectDailyList.isEmpty()) {
                    for (TblProductionDefectDaily tblProductionDefectDaily : defectDailyList) {
                        for (TblProductionDefectDaily defectDaily : tblProductionDefectDailyList) {
                            if (tblProductionDefectDaily.getDefectSeq() == defectDaily.getDefectSeq()) {
                                tblProductionDefectDaily.setQuantity(defectDaily.getQuantity());
                                tblProductionDefectDaily.setMdr2ProdDetailId(defectDaily.getMdr2ProdDetailId());
                                tblProductionDefectDaily.setUpdateDate(new Date());
                                tblProductionDefectDaily.setUpdateUserUuid(userUuid);
                                entityManager.merge(tblProductionDefectDaily);
                                break;
                            }
                        }
                    }
                } else {
                    for (TblProductionDefectDaily defectDaily : tblProductionDefectDailyList) {
                        defectDaily.setId(IDGenerator.generate());
                        defectDaily.setDefectDate(reportDate);
                        defectDaily.setCreateDate(new Date());
                        defectDaily.setCreateUserUuid(userUuid);
                        defectDaily.setUpdateDate(defectDaily.getCreateDate());
                        defectDaily.setUpdateUserUuid(userUuid);
                        entityManager.persist(defectDaily);
                    }
                }
            }
        } else {
            if (list == null || list.isEmpty()) {
                for (TblProductionDefectDaily defectDaily : tblProductionDefectDailyList) {
                    TblProductionDefect defect = new TblProductionDefect();
                    defect.setId(IDGenerator.generate());
                    defect.setProductionDetailId(productionDetailId);
                    defect.setDefectSeq(defectDaily.getDefectSeq());
                    defect.setQuantity(defectDaily.getQuantity());
                    defect.setCreateDate(new Date());
                    defect.setCreateUserUuid(userUuid);
                    defect.setUpdateDate(defect.getCreateDate());
                    defect.setUpdateUserUuid(userUuid);
                    entityManager.persist(defect);
                    
                    defectDaily.setId(IDGenerator.generate());
                    defectDaily.setProductionDetectId(defect.getId());
                    defectDaily.setDefectDate(reportDate);
                    defectDaily.setCreateDate(new Date());
                    defectDaily.setCreateUserUuid(userUuid);
                    defectDaily.setUpdateDate(defect.getCreateDate());
                    defectDaily.setUpdateUserUuid(userUuid);
                    entityManager.persist(defectDaily);
                }
            } else {
                List<String> prodDefectIds = new ArrayList<>();
                for (TblProductionDefect defect : list) {
                    prodDefectIds.add(defect.getId());
                }
                List<TblProductionDefectDaily> defectDailyList = getCurrentDayDailyInfo(reportDate, prodDefectIds);
                if (defectDailyList != null && !defectDailyList.isEmpty()) {
                    Map<Integer, Integer> dailyMinus = new HashMap<>();
                    for (TblProductionDefectDaily defectDaily : tblProductionDefectDailyList) {
                        for (TblProductionDefectDaily oldDefectDaily : defectDailyList) {
                            if (defectDaily.getDefectSeq() == oldDefectDaily.getDefectSeq()) {
                                dailyMinus.put(defectDaily.getDefectSeq(), defectDaily.getQuantity() - oldDefectDaily.getQuantity());
                                oldDefectDaily.setQuantity(defectDaily.getQuantity());
                                oldDefectDaily.setMdr2ProdDetailId(defectDaily.getMdr2ProdDetailId());
                                oldDefectDaily.setUpdateDate(new Date());
                                oldDefectDaily.setUpdateUserUuid(userUuid);
                                entityManager.merge(oldDefectDaily);
                                break;
                            }
                        }
                    }
                    
                    for (TblProductionDefect defect : list) {
                        if (!dailyMinus.containsKey(defect.getDefectSeq())) {continue;}
                        defect.setQuantity(defect.getQuantity() + dailyMinus.get(defect.getDefectSeq()));
                        defect.setUpdateDate(new Date());
                        defect.setUpdateUserUuid(userUuid);
                        entityManager.merge(defect);
                    }
                } else {
                    for (TblProductionDefect defect : list) {
                        for (TblProductionDefectDaily defectDaily : tblProductionDefectDailyList) {
                            if (defect.getDefectSeq() == defectDaily.getDefectSeq()) {
                                defect.setQuantity(defect.getQuantity() + defectDaily.getQuantity());
                                defect.setUpdateDate(new Date());
                                defect.setUpdateUserUuid(userUuid);
                                entityManager.merge(defect);

                                defectDaily.setProductionDetectId(defect.getId());
                                break;
                            }
                        }
                    }
                    
                    for (TblProductionDefectDaily defectDaily : tblProductionDefectDailyList) {
                        defectDaily.setId(IDGenerator.generate());
                        defectDaily.setDefectDate(reportDate);
                        defectDaily.setCreateDate(new Date());
                        defectDaily.setCreateUserUuid(userUuid);
                        defectDaily.setUpdateDate(defectDaily.getCreateDate());
                        defectDaily.setUpdateUserUuid(userUuid);
                        entityManager.persist(defectDaily);
                    }
                }
            }
        }
        
        return response;
    }
}
