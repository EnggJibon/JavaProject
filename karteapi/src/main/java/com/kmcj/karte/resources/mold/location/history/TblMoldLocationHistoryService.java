/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.location.history;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.MstMoldService;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author jiangxs
 */
@Dependent
public class TblMoldLocationHistoryService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     * 金型所在履歴照会 金型所在履歴マスタ複数取得
     *
     * @param moldId
     * @param user
     * @return
     */
    public TblMoldLocationHistorys getMoldLocationHistories(String moldId, LoginUser user) {
        TblMoldLocationHistorys resTblMoldLocationHistorys = new TblMoldLocationHistorys();

        List<TblMoldLocationHistorys> tblMoldLocationHistoryList = new ArrayList<>();

        StringBuffer sql = new StringBuffer(" SELECT t FROM TblMoldLocationHistory t WHERE 1=1 ");

        if (moldId != null && !"".equals(moldId)) {
            sql = sql.append(" And t.mstMold.moldId = :moldId ");
        }
        sql = sql.append(" order by t.endDate desc,t.startDate desc ");
        Query query = entityManager.createQuery(sql.toString());

        if (moldId != null && !"".equals(moldId)) {
            query.setParameter("moldId", moldId);
        }
        try {
            List<TblMoldLocationHistory> list = query.getResultList();
            for (int i = 0; i < list.size(); i++) {
                TblMoldLocationHistory aTblMoldLocationHistory = list.get(i);
                TblMoldLocationHistorys aResTblMoldLocationHistorys = new TblMoldLocationHistorys();
                aResTblMoldLocationHistorys.setId(aTblMoldLocationHistory.getId());
                aResTblMoldLocationHistorys.setChangeReason(null == aTblMoldLocationHistory.getChangeReason() ? 0 : aTblMoldLocationHistory.getChangeReason());
                aResTblMoldLocationHistorys.setChangeReasonText(aTblMoldLocationHistory.getChangeReasonText());
                aResTblMoldLocationHistorys.setCompanyId(null == aTblMoldLocationHistory.getCompanyId() ? "" : aTblMoldLocationHistory.getCompanyId());
                aResTblMoldLocationHistorys.setCompanyName(aTblMoldLocationHistory.getCompanyName());
                aResTblMoldLocationHistorys.setLocationId(null == aTblMoldLocationHistory.getLocationId() ? "" : aTblMoldLocationHistory.getLocationId());
                aResTblMoldLocationHistorys.setLocationName(aTblMoldLocationHistory.getLocationName());
                aResTblMoldLocationHistorys.setInstllationSiteId(null == aTblMoldLocationHistory.getInstllationSiteId() ? "" : aTblMoldLocationHistory.getInstllationSiteId());
                aResTblMoldLocationHistorys.setInstllationSiteName(aTblMoldLocationHistory.getInstllationSiteName());
                
                aResTblMoldLocationHistorys.setStartDate(null == aTblMoldLocationHistory.getStartDate() ? "" : new FileUtil().getDateTimeFormatForStr(aTblMoldLocationHistory.getStartDate()));
                if(aTblMoldLocationHistory.getEndDate() != null){
                    aResTblMoldLocationHistorys.setEndDate(aTblMoldLocationHistory.getEndDate().compareTo(CommonConstants.SYS_MAX_DATE) == 0 ? "-" : new FileUtil().getDateTimeFormatForStr(aTblMoldLocationHistory.getEndDate()));
                }else{
                    aResTblMoldLocationHistorys.setEndDate("-");
                }
                
                aResTblMoldLocationHistorys.setMoldName(aTblMoldLocationHistory.getMstMold().getMoldName());
                
                tblMoldLocationHistoryList.add(aResTblMoldLocationHistorys);
            }
            resTblMoldLocationHistorys.setTblMoldLocationHistoryVos(tblMoldLocationHistoryList);
        } catch (NoResultException e) {
            resTblMoldLocationHistorys.setError(true);
            resTblMoldLocationHistorys.setErrorCode(ErrorMessages.E201_APPLICATION);
            resTblMoldLocationHistorys.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "mst_error_record_not_found"));
            return resTblMoldLocationHistorys;
        }
        return resTblMoldLocationHistorys;
    }

    /**
     *
     * @param tblMoldLocationHistory
     */
    @Transactional
    public void updateMoldLocationHistories(TblMoldLocationHistory tblMoldLocationHistory) {
        Query query = entityManager.createNamedQuery("TblMoldLocationHistory.updateTblMoldLocationHistory");
        query.setParameter("changeReason", tblMoldLocationHistory.getChangeReason());
        query.setParameter("changeReasonText", tblMoldLocationHistory.getChangeReasonText());
        query.setParameter("companyId", tblMoldLocationHistory.getCompanyId());
        query.setParameter("companyName", tblMoldLocationHistory.getCompanyName());
        query.setParameter("locationId", tblMoldLocationHistory.getLocationId());
        query.setParameter("locationName", tblMoldLocationHistory.getLocationName());
        query.setParameter("instllationSiteId", tblMoldLocationHistory.getInstllationSiteId());
        query.setParameter("instllationSiteName", tblMoldLocationHistory.getInstllationSiteName());
        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", tblMoldLocationHistory.getUpdateUserUuid());
        query.setParameter("moldUuid", tblMoldLocationHistory.getMstMold().getUuid());
        query.setParameter("startDate", tblMoldLocationHistory.getStartDate());
        query.setParameter("endDate", tblMoldLocationHistory.getEndDate());
        query.executeUpdate();
    }

    /**
     * 金型移動登録 保存 updateEndDate
     *
     * @param tblMoldLocationHistory
     */
    @Transactional
    public void updateMoldLocationHistoriesByEndDate(TblMoldLocationHistory tblMoldLocationHistory) {
        Query query = entityManager.createNamedQuery("TblMoldLocationHistory.updateMoldLocationHistoriesByEndDate");
        query.setParameter("endDate", tblMoldLocationHistory.getEndDate());
        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", tblMoldLocationHistory.getUpdateUserUuid());
        //where
        query.setParameter("id", tblMoldLocationHistory.getId());

        query.executeUpdate();
    }

    /**
     *
     * @param tblMoldLocationHistory
     */
    @Transactional
    public void deleteMoldLocationHistories(TblMoldLocationHistory tblMoldLocationHistory) {
        Query query = entityManager.createNamedQuery("TblMoldLocationHistory.deleteTblMoldLocationHistory");
        query.setParameter("id", tblMoldLocationHistory.getId());
        query.executeUpdate();
    }
    
    @Transactional
    public void deleteMoldLocationHistorieByMoldUuid(String moldUuid) {
        Query query = entityManager.createNamedQuery("TblMoldLocationHistory.deleteTblMoldLocationHistory");
        query.setParameter("moldUuid", moldUuid);
        query.executeUpdate();
    }

    /**
     * 金型移動登録 新規
     *
     * @param tblMoldLocationHistory
     */
    @Transactional
    public void creatMoldLocationHistories(TblMoldLocationHistory tblMoldLocationHistory) {
        entityManager.persist(tblMoldLocationHistory);
    }

    @Transactional
    public void modifyTblMoldLocationHistory(TblMoldLocationHistory tblMoldLocationHistory) {
        entityManager.merge(tblMoldLocationHistory);
    }
    
    @Transactional
    public void updateLatestTblMoldLocationHistoryDateByMoldUuid(String moldUuid) {
        FileUtil fu = new FileUtil();
        Date befInstalledDate = fu.getSpecifiedDayBefore(new Date());
        Query query = entityManager.createQuery("Update TblMoldLocationHistory h set h.endDate = :enddate where h.mstMold.uuid = :moldUuid and h.endDate = :oldEnddate ");
        query.setParameter("enddate", befInstalledDate);
        query.setParameter("moldUuid", moldUuid);
        query.setParameter("oldEnddate", CommonConstants.SYS_MAX_DATE);
        query.executeUpdate();
    }
    
    /**
     * 金型移動登録 保存
     *
     * @param tblMoldLocationHistorys
     * @param loginUser
     * @param mstMoldService
     */
    @Transactional
    public void creatMoldLocationHistoriesByChang(TblMoldLocationHistorys tblMoldLocationHistorys, LoginUser loginUser, MstMoldService mstMoldService) {
        TblMoldLocationHistory inData;
        String moldId = tblMoldLocationHistorys.getMoldId();//金型ID
        Date installedDate = tblMoldLocationHistorys.getInstalledDate();//設置日   
        String moldUuid = tblMoldLocationHistorys.getMoldUuid();//金型Uuid
        Integer moldChangeReason = tblMoldLocationHistorys.getChangeReason();//変更事由
        String moldChangeReasonText = tblMoldLocationHistorys.getChangeReasonText();//変更事由テキスト
        String companyId = tblMoldLocationHistorys.getCompanyId();//会社ID
        String companyName = tblMoldLocationHistorys.getCompanyName();//会社名称
        String locationId = tblMoldLocationHistorys.getLocationId();//所在地ID
        String locationName = tblMoldLocationHistorys.getLocationName();//所在地名称
        String installationSiteId = tblMoldLocationHistorys.getInstllationSiteId(); //設置場所ID
        String installationSiteName = tblMoldLocationHistorys.getInstllationSiteName();  //設置場所名称    

        MstMold mstMold = new MstMold();
        mstMold.setUuid(moldUuid);
        mstMold.setMoldId(moldId);
        mstMold.setInstalledDate(installedDate);
        if (companyId != null && !"".equals(companyId)) {
            mstMold.setCompanyId(companyId);
            mstMold.setCompanyName(companyName);
        }

        if (locationId != null && !"".equals(locationId)) {
            mstMold.setLocationId(locationId);
            mstMold.setLocationName(locationName);
        }

        if (installationSiteId != null && !"".equals(installationSiteId)) {
            mstMold.setInstllationSiteId(installationSiteId);
            mstMold.setInstllationSiteName(installationSiteName);
        }

        mstMold.setUpdateDate(new Date());
        mstMold.setUpdateUserUuid(loginUser.getUserUuid());
        //update MstMold
        mstMoldService.updateByMoldIdForLocationHistory(mstMold);

        List list = getMoldLocationHistoriesByMoldId(moldId);
        if (list.size() > 0) {
            if (list.get(0) != null) {
                inData = new TblMoldLocationHistory();
                String strId = list.get(0).toString();
                FileUtil fu = new FileUtil();
                Date befInstalledDate = fu.getSpecifiedDayBefore(installedDate);
                inData.setEndDate(befInstalledDate);//現在の履歴は終了日に新しい設置日の前日をセットして更新する。
                inData.setUpdateDate(new Date());
                inData.setUpdateUserUuid(loginUser.getUserUuid());
                inData.setId(strId);
                //update 現在の履歴 MoldLocationHistory
                updateMoldLocationHistoriesByEndDate(inData);
            }
        }
        inData = new TblMoldLocationHistory();
        inData.setId(IDGenerator.generate());
        inData.setMoldUuid(moldUuid);
        inData.setStartDate(installedDate);//開始日に設置日
        inData.setEndDate(CommonConstants.SYS_MAX_DATE);//終了日にシステム最大日付/todo
        inData.setChangeReason(moldChangeReason);
        inData.setChangeReasonText(moldChangeReasonText);

        if (companyId != null && !"".equals(companyId)) {
            inData.setCompanyId(companyId);
            inData.setCompanyName(companyName);
        }

        if (locationId != null && !"".equals(locationId)) {

            inData.setLocationId(locationId);
            inData.setLocationName(locationName);

        }

        if (installationSiteId != null && !"".equals(installationSiteId)) {
            inData.setInstllationSiteId(installationSiteId);
            inData.setInstllationSiteName(installationSiteName);
        }

        inData.setCreateDate(new Date());
        inData.setCreateUserUuid(loginUser.getUserUuid());
        inData.setUpdateDate(new Date());
        inData.setUpdateUserUuid(loginUser.getUserUuid());
        //creat MoldLocationHistory(new) 新たな金型移動履歴
        creatMoldLocationHistories(inData);
    }

    /**
     *
     * @param moldId
     * @return
     */
    public List<TblMoldLocationHistory> getMoldLocationHistoriesByMoldId(String moldId) {
        Query query = entityManager.createNamedQuery("TblMoldLocationHistory.findByMoldUuidAndEnddate");
        query.setParameter("moldId", moldId);
        query.setParameter("endDate", CommonConstants.SYS_MAX_DATE);
        query.setMaxResults(1);
        List list = query.getResultList();
        return list;
    }
    
    /**
     *
     * @param moldId
     * @return
     */
    public TblMoldLocationHistory getLatestLocationHistoriesByMoldId(String moldId) {
        Query query = entityManager.createNamedQuery("TblMoldLocationHistory.findLatestByMoldUuidAndEnddate");
        query.setParameter("moldId", moldId);
        query.setParameter("endDate", CommonConstants.SYS_MAX_DATE);
        query.setMaxResults(1);
        List<TblMoldLocationHistory> list = query.getResultList();
        return null == list || list.isEmpty() ? null : list.get(0);
    }
    
    /**
     * バッチで金型所在履歴テーブルデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    TblMoldLocationHistoryList getExtMoldLocationHistorysByBatch(String latestExecutedDate, String moldUuid) {
        TblMoldLocationHistoryList resList = new TblMoldLocationHistoryList();
        List<TblMoldLocationHistorys> resVo = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM TblMoldLocationHistory t join MstApiUser u on u.companyId = t.mstMold.ownerCompanyId WHERE 1 = 1 ");
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            sql.append(" and t.mstMold.uuid = :moldUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null)  ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            query.setParameter("moldUuid", moldUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<TblMoldLocationHistory> tmpList = query.getResultList();
        for (TblMoldLocationHistory tblMoldLocationHistory : tmpList) {            
            TblMoldLocationHistorys aVo = new TblMoldLocationHistorys();
            aVo.setId(tblMoldLocationHistory.getId());
            if (null != tblMoldLocationHistory.getMstCompany()){
                aVo.setCompanyCode(tblMoldLocationHistory.getMstCompany().getCompanyCode());
                aVo.setCompanyId(tblMoldLocationHistory.getMstCompany().getId());
            }
            if (null != tblMoldLocationHistory.getLocationId()){
                aVo.setLocationId(tblMoldLocationHistory.getLocationId());
                aVo.setLocationName(tblMoldLocationHistory.getLocationName());
            }
            if (null != tblMoldLocationHistory.getInstllationSiteId()){
                aVo.setInstllationSiteId(tblMoldLocationHistory.getInstllationSiteId());
                aVo.setInstllationSiteName(tblMoldLocationHistory.getInstllationSiteName());
            }
            if (null != tblMoldLocationHistory.getMstMold()){
                aVo.setMoldId(tblMoldLocationHistory.getMstMold().getMoldId());
            }
            if (null != tblMoldLocationHistory.getStartDate()){
                aVo.setStartDate(new FileUtil().getDateFormatForStr(tblMoldLocationHistory.getStartDate()));
            }
            if (null != tblMoldLocationHistory.getEndDate()){
                aVo.setEndDate(new FileUtil().getDateFormatForStr(tblMoldLocationHistory.getEndDate()));
            }
            
            if (null != tblMoldLocationHistory.getChangeReason()){
                aVo.setChangeReason(tblMoldLocationHistory.getChangeReason());
                aVo.setChangeReasonText(tblMoldLocationHistory.getChangeReasonText());
            }
            
            aVo.setCreateDate(tblMoldLocationHistory.getCreateDate());
            aVo.setCreateUserUuid(tblMoldLocationHistory.getCreateUserUuid());
            aVo.setUpdateUserUuid(tblMoldLocationHistory.getUpdateUserUuid());
            resVo.add(aVo);
        }
        resList.setTblMoldLocationHistorys(resVo);
        return resList;
    }

    /**
     * バッチで金型所在履歴テーブルデータを更新
     *
     * @param moldLocationHistorys
     * @return
     */
    @Transactional
    public BasicResponse updateExtMoldLocationHistorysByBatch(List<TblMoldLocationHistorys> moldLocationHistorys) {
        BasicResponse response = new BasicResponse();
        if (moldLocationHistorys != null && !moldLocationHistorys.isEmpty()) {
            for (TblMoldLocationHistorys aMoldLocationHistorys : moldLocationHistorys) {
                List<TblMoldLocationHistory> oldMoldLocationHistorys = entityManager.createQuery("SELECT t FROM TblMoldLocationHistory t WHERE 1=1 and t.id=:id ")
                        .setParameter("id", aMoldLocationHistorys.getId())
                        .setMaxResults(1)
                        .getResultList();
                TblMoldLocationHistory newMoldLocationHistory;
                if (null != oldMoldLocationHistorys && !oldMoldLocationHistorys.isEmpty()) {
                    newMoldLocationHistory = oldMoldLocationHistorys.get(0);
                } else {
                    newMoldLocationHistory = new TblMoldLocationHistory();
                    newMoldLocationHistory.setId(aMoldLocationHistorys.getId());
                }
                //自社の金型UUIDに変換
                MstMold ownerMold = entityManager.find(MstMold.class, aMoldLocationHistorys.getMoldId());
                if (null != ownerMold) {
                    newMoldLocationHistory.setMoldUuid(ownerMold.getUuid());

                    if (null != aMoldLocationHistorys.getStartDate() && !"".equals(aMoldLocationHistorys.getStartDate().trim())) {
                        newMoldLocationHistory.setStartDate(new FileUtil().getDateParseForDate(aMoldLocationHistorys.getStartDate()));
                    }
                    if (null != aMoldLocationHistorys.getEndDate() && !"".equals(aMoldLocationHistorys.getEndDate().trim())) {
                        newMoldLocationHistory.setEndDate(new FileUtil().getDateParseForDate(aMoldLocationHistorys.getEndDate()));
                    }
                    if (null != aMoldLocationHistorys.getChangeReason()) {
                        newMoldLocationHistory.setChangeReason(aMoldLocationHistorys.getChangeReason());
                        newMoldLocationHistory.setChangeReasonText(aMoldLocationHistorys.getChangeReasonText());
                    }

                    if (null != aMoldLocationHistorys.getCompanyCode() && !aMoldLocationHistorys.getCompanyCode().trim().equals("")) {
                        MstCompany com = entityManager.find(MstCompany.class, aMoldLocationHistorys.getCompanyId());
                        if (null != com) {
                            newMoldLocationHistory.setCompanyId(com.getId());
                            newMoldLocationHistory.setCompanyName(com.getCompanyName());
                        }
                    }
                    if (null != aMoldLocationHistorys.getLocationId() && !"".equals(aMoldLocationHistorys.getLocationId().trim())) {
                        newMoldLocationHistory.setLocationId(aMoldLocationHistorys.getLocationId());
                        newMoldLocationHistory.setLocationName(aMoldLocationHistorys.getLocationName());
                    } else {
                        newMoldLocationHistory.setLocationId(null);
                        newMoldLocationHistory.setLocationName(null);
                    }
                    if (null != aMoldLocationHistorys.getInstllationSiteId() && !"".equals(aMoldLocationHistorys.getInstllationSiteId().trim())) {
                        newMoldLocationHistory.setInstllationSiteId(aMoldLocationHistorys.getInstllationSiteId());
                        newMoldLocationHistory.setInstllationSiteName(aMoldLocationHistorys.getInstllationSiteName());
                    } else {
                        newMoldLocationHistory.setInstllationSiteId(null);
                        newMoldLocationHistory.setInstllationSiteName(null);
                    }

                    newMoldLocationHistory.setCreateDate(aMoldLocationHistorys.getCreateDate());
                    newMoldLocationHistory.setCreateUserUuid(aMoldLocationHistorys.getCreateUserUuid());
                    newMoldLocationHistory.setUpdateDate(new Date());
                    newMoldLocationHistory.setUpdateUserUuid(aMoldLocationHistorys.getUpdateUserUuid());

                    if (null != oldMoldLocationHistorys && !oldMoldLocationHistorys.isEmpty()) {
                        entityManager.merge(newMoldLocationHistory);
                    } else {
                        List<TblMoldLocationHistory> lasMoldLocationHistorys = entityManager.createQuery("SELECT h from TblMoldLocationHistory h where h.moldUuid = :moldUuid ORDER BY h.endDate DESC ")
                                .setParameter("moldUuid", ownerMold.getUuid()).setMaxResults(1).getResultList();
                        if (null != lasMoldLocationHistorys && !lasMoldLocationHistorys.isEmpty()){
                            lasMoldLocationHistorys.get(0).setEndDate(new FileUtil().getSpecifiedDayBefore(newMoldLocationHistory.getStartDate()));
                            entityManager.merge(lasMoldLocationHistorys.get(0));
                        }
                        entityManager.persist(newMoldLocationHistory);
                    }
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }

    @Transactional
    public void deleteMoldLocationHistorieLatest(String moldUuid) {
        List<TblMoldLocationHistory> moldLocationHistorys = entityManager.createQuery("Select h from TblMoldLocationHistory h where h.mstMold.uuid = :moldUuid")
                .setParameter("moldUuid", moldUuid).getResultList();
        if (null != moldLocationHistorys && !moldLocationHistorys.isEmpty()){
            entityManager.remove(moldLocationHistorys.get(0));
        }
    }    
}
