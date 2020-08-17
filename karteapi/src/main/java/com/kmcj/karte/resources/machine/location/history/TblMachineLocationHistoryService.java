package com.kmcj.karte.resources.machine.location.history;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.MstMachineService;
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
public class TblMachineLocationHistoryService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     * 設備所在履歴照会 設備所在履歴マスタ複数取得
     *
     * @param machineId
     * @param user
     * @return
     */
    public TblMachineLocationHistoryVo getMachineLocationHistories(String machineId, LoginUser user) {
        TblMachineLocationHistoryVo resMachineLocationHistoryVo = new TblMachineLocationHistoryVo();

        List<TblMachineLocationHistoryVo> tblMachineLocationHistoryList = new ArrayList<>();

        StringBuffer sql = new StringBuffer(" SELECT t FROM TblMachineLocationHistory t WHERE 1=1 ");

        if (machineId != null && !"".equals(machineId)) {
            sql = sql.append(" And t.mstMachine.machineId = :machineId ");
        }
        sql = sql.append(" order by t.endDate desc,t.startDate desc ");
        Query query = entityManager.createQuery(sql.toString());

        if (machineId != null && !"".equals(machineId)) {
            query.setParameter("machineId", machineId);
        }
        try {
            List<TblMachineLocationHistory> list = query.getResultList();
            for (int i = 0; i < list.size(); i++) {
                TblMachineLocationHistory aTblMachineLocationHistory = list.get(i);
                TblMachineLocationHistoryVo aMachineLocationHistoryVo = new TblMachineLocationHistoryVo();
                aMachineLocationHistoryVo.setId(aTblMachineLocationHistory.getId());
                aMachineLocationHistoryVo.setChangeReason(null == aTblMachineLocationHistory.getChangeReason() ? 0 : aTblMachineLocationHistory.getChangeReason());
                aMachineLocationHistoryVo.setChangeReasonText(aTblMachineLocationHistory.getChangeReasonText());
                aMachineLocationHistoryVo.setCompanyId(null == aTblMachineLocationHistory.getCompanyId() ? "" : aTblMachineLocationHistory.getCompanyId());
                aMachineLocationHistoryVo.setCompanyName(aTblMachineLocationHistory.getCompanyName());
                aMachineLocationHistoryVo.setLocationId(null == aTblMachineLocationHistory.getLocationId() ? "" : aTblMachineLocationHistory.getLocationId());
                aMachineLocationHistoryVo.setLocationName(aTblMachineLocationHistory.getLocationName());
                aMachineLocationHistoryVo.setInstllationSiteId(null == aTblMachineLocationHistory.getInstllationSiteId() ? "" : aTblMachineLocationHistory.getInstllationSiteId());
                aMachineLocationHistoryVo.setInstllationSiteName(aTblMachineLocationHistory.getInstllationSiteName());

                aMachineLocationHistoryVo.setStartDateStr(null == aTblMachineLocationHistory.getStartDate() ? "" : new FileUtil().getDateTimeFormatForStr(aTblMachineLocationHistory.getStartDate()));
                if (aTblMachineLocationHistory.getEndDate() != null) {
                    aMachineLocationHistoryVo.setEndDateStr(aTblMachineLocationHistory.getEndDate().compareTo(CommonConstants.SYS_MAX_DATE) == 0 ? "-" : new FileUtil().getDateTimeFormatForStr(aTblMachineLocationHistory.getEndDate()));
                } else {
                    aMachineLocationHistoryVo.setEndDateStr("-");
                }

                aMachineLocationHistoryVo.setMachineName(aTblMachineLocationHistory.getMstMachine().getMachineName());

                tblMachineLocationHistoryList.add(aMachineLocationHistoryVo);
            }
            resMachineLocationHistoryVo.setTblMachineLocationHistoryVos(tblMachineLocationHistoryList);
        } catch (NoResultException e) {
            resMachineLocationHistoryVo.setError(true);
            resMachineLocationHistoryVo.setErrorCode(ErrorMessages.E201_APPLICATION);
            resMachineLocationHistoryVo.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "mst_error_record_not_found"));
            return resMachineLocationHistoryVo;
        }
        return resMachineLocationHistoryVo;
    }

    /**
     *
     * @param tblMachineLocationHistory
     */
    @Transactional
    public void updateMachineLocationHistories(TblMachineLocationHistory tblMachineLocationHistory) {
        Query query = entityManager.createNamedQuery("TblMachineLocationHistory.updateTblMachineLocationHistory");
        query.setParameter("changeReason", tblMachineLocationHistory.getChangeReason());
        query.setParameter("changeReasonText", tblMachineLocationHistory.getChangeReasonText());
        query.setParameter("companyId", tblMachineLocationHistory.getCompanyId());
        query.setParameter("companyName", tblMachineLocationHistory.getCompanyName());
        query.setParameter("locationId", tblMachineLocationHistory.getLocationId());
        query.setParameter("locationName", tblMachineLocationHistory.getLocationName());
        query.setParameter("instllationSiteId", tblMachineLocationHistory.getInstllationSiteId());
        query.setParameter("instllationSiteName", tblMachineLocationHistory.getInstllationSiteName());
        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", tblMachineLocationHistory.getUpdateUserUuid());
        query.setParameter("machineUuid", tblMachineLocationHistory.getMstMachine().getUuid());
        query.setParameter("startDate", tblMachineLocationHistory.getStartDate());
        query.setParameter("endDate", tblMachineLocationHistory.getEndDate());
        query.executeUpdate();
    }

    /**
     * 設備移動登録 保存 updateEndDate
     *
     * @param tblMachineLocationHistory
     */
    @Transactional
    public void updateMachineLocationHistoriesByEndDate(TblMachineLocationHistory tblMachineLocationHistory) {
        Query query = entityManager.createNamedQuery("TblMachineLocationHistory.updateMachineLocationHistoriesByEndDate");
        query.setParameter("endDate", tblMachineLocationHistory.getEndDate());
        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", tblMachineLocationHistory.getUpdateUserUuid());
        //where
        query.setParameter("id", tblMachineLocationHistory.getId());

        query.executeUpdate();
    }

    /**
     *
     * @param tblMachineLocationHistory
     */
    @Transactional
    public void deleteMachineLocationHistories(TblMachineLocationHistory tblMachineLocationHistory) {
        Query query = entityManager.createNamedQuery("TblMachineLocationHistory.deleteTblMachineLocationHistory");
        query.setParameter("id", tblMachineLocationHistory.getId());
        query.executeUpdate();
    }

    @Transactional
    public void deleteMachineLocationHistorieByMachineUuid(String machineUuid) {
        StringBuilder sql = new StringBuilder("DELETE FROM TblMachineLocationHistory t WHERE t.mstMachine.uuid = :machineUuid");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("machineUuid", machineUuid);
        query.executeUpdate();
    }
    
    @Transactional
    public void deleteMachineLocationHistorieLatest(String machineUuid) {
        List<TblMachineLocationHistory> machineLocationHistorys = entityManager.createQuery("Select h from TblMachineLocationHistory h where h.mstMachine.uuid = :machineUuid")
                .setParameter("machineUuid", machineUuid).getResultList();
        if (null != machineLocationHistorys && !machineLocationHistorys.isEmpty()){
            entityManager.remove(machineLocationHistorys.get(0));
        }
    }

    /**
     * 設備移動登録 新規
     *
     * @param tblMachineLocationHistory
     */
    @Transactional
    public void creatMachineLocationHistories(TblMachineLocationHistory tblMachineLocationHistory) {
        entityManager.persist(tblMachineLocationHistory);
    }

    @Transactional
    public void modifyTblMachineLocationHistory(TblMachineLocationHistory tblMachineLocationHistory) {
        entityManager.merge(tblMachineLocationHistory);
    }

    @Transactional
    public void updateLatestTblMachineLocationHistoryDateByMachineUuid(String machineUuid) {
        FileUtil fu = new FileUtil();
        Date befInstalledDate = fu.getSpecifiedDayBefore(new Date());
        Query query = entityManager.createQuery("Update TblMachineLocationHistory h set h.endDate = :enddate where h.mstMachine.uuid = :machineUuid and h.endDate = :oldEnddate ");
        query.setParameter("enddate", befInstalledDate);
        query.setParameter("machineUuid", machineUuid);
        query.setParameter("oldEnddate", CommonConstants.SYS_MAX_DATE);
        query.executeUpdate();
    }

    /**
     * 設備移動登録 保存
     *
     * @param tblMachineLocationHistoryVo
     * @param loginUser
     * @param mstMachineService
     */
    @Transactional
    public void creatMachineLocationHistoriesByChang(TblMachineLocationHistoryVo tblMachineLocationHistoryVo, LoginUser loginUser, MstMachineService mstMachineService) {
        TblMachineLocationHistory inData;
        String machineId = tblMachineLocationHistoryVo.getMstMachine().getMachineId();//設備ID
        Date installedDate = tblMachineLocationHistoryVo.getMstMachine().getInstalledDate();//設置日   
        String machineUuid = tblMachineLocationHistoryVo.getMachineUuid();//設備Uuid
        Integer machineChangeReason = tblMachineLocationHistoryVo.getChangeReason();//変更事由
        String machineChangeReasonText = tblMachineLocationHistoryVo.getChangeReasonText();//変更事由テキスト
        String companyId = tblMachineLocationHistoryVo.getCompanyId();//会社ID
        String companyName = tblMachineLocationHistoryVo.getCompanyName();//会社名称
        String locationId = tblMachineLocationHistoryVo.getLocationId();//所在地ID
        String locationName = tblMachineLocationHistoryVo.getLocationName();//所在地名称
        String installationSiteId = tblMachineLocationHistoryVo.getInstllationSiteId(); //設置場所ID
        String installationSiteName = tblMachineLocationHistoryVo.getInstllationSiteName();  //設置場所名称    

        MstMachine mstMachine = new MstMachine();
        mstMachine.setUuid(machineUuid);
        mstMachine.setMachineId(machineId);
        mstMachine.setInstalledDate(installedDate);
        if (companyId != null && !"".equals(companyId)) {
            mstMachine.setCompanyId(companyId);
            mstMachine.setCompanyName(companyName);
        }

        if (locationId != null && !"".equals(locationId)) {
            mstMachine.setLocationId(locationId);
            mstMachine.setLocationName(locationName);
        }

        if (installationSiteId != null && !"".equals(installationSiteId)) {
            mstMachine.setInstllationSiteId(installationSiteId);
            mstMachine.setInstllationSiteName(installationSiteName);
        }

        mstMachine.setUpdateDate(new Date());
        mstMachine.setUpdateUserUuid(loginUser.getUserUuid());
        //update MstMachine
        mstMachineService.updateByMachineIdForLocationHistory(mstMachine);

        List list = getMachineLocationHistoriesByMachineId(machineId);
        if (list.size() > 0) {
            if (list.get(0) != null) {
                inData = new TblMachineLocationHistory();
                String strId = list.get(0).toString();
                FileUtil fu = new FileUtil();
                Date befInstalledDate = fu.getSpecifiedDayBefore(installedDate);
                inData.setEndDate(befInstalledDate);//現在の履歴は終了日に新しい設置日の前日をセットして更新する。
                inData.setUpdateDate(new Date());
                inData.setUpdateUserUuid(loginUser.getUserUuid());
                inData.setId(strId);
                //update 現在の履歴 MachineLocationHistory
                updateMachineLocationHistoriesByEndDate(inData);
            }
        }
        inData = new TblMachineLocationHistory();
        inData.setId(IDGenerator.generate());
        inData.setMachineUuid(machineUuid);
        inData.setStartDate(installedDate);//開始日に設置日
        inData.setEndDate(CommonConstants.SYS_MAX_DATE);//終了日にシステム最大日付/todo
        inData.setChangeReason(machineChangeReason);
        inData.setChangeReasonText(machineChangeReasonText);

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
        //creat MachineLocationHistory(new) 新たな設備移動履歴
        creatMachineLocationHistories(inData);
    }

    /**
     *
     * @param machineId
     * @return
     */
    public List<TblMachineLocationHistory> getMachineLocationHistoriesByMachineId(String machineId) {
        Query query = entityManager.createNamedQuery("TblMachineLocationHistory.findByMachineUuidAndEnddate");
        query.setParameter("machineId", machineId);
        query.setParameter("endDate", CommonConstants.SYS_MAX_DATE);
        query.setMaxResults(1);
        List list = query.getResultList();
        return list;
    }

    /**
     *
     * @param machineId
     * @return
     */
    public TblMachineLocationHistory getLatestLocationHistoriesByMachineId(String machineId) {
        Query query = entityManager.createNamedQuery("TblMachineLocationHistory.findLatestByMachineUuidAndEnddate");
        query.setParameter("machineId", machineId);
        query.setParameter("endDate", CommonConstants.SYS_MAX_DATE);
        query.setMaxResults(1);
        List<TblMachineLocationHistory> list = query.getResultList();
        return null == list || list.isEmpty() ? null : list.get(0);
    }

    /**
     * バッチで設備所在履歴テーブルデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    TblMachineLocationHistoryList getExtMachineLocationHistorysByBatch(String latestExecutedDate, String machineUuid) {
        TblMachineLocationHistoryList resList = new TblMachineLocationHistoryList();
        List<TblMachineLocationHistoryVo> resVo = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM TblMachineLocationHistory t join MstApiUser u on u.companyId = t.mstMachine.ownerCompanyId WHERE 1 = 1 ");
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            sql.append(" and t.mstMachine.uuid = :machineUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null)  ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            query.setParameter("machineUuid", machineUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<TblMachineLocationHistory> tmpList = query.getResultList();
        for (TblMachineLocationHistory tblMachineLocationHistory : tmpList) {
            TblMachineLocationHistoryVo aVo = new TblMachineLocationHistoryVo();
            aVo.setId(tblMachineLocationHistory.getId());
            if (null != tblMachineLocationHistory.getMstCompany()) {
                aVo.setCompanyCode(tblMachineLocationHistory.getMstCompany().getCompanyCode());
                aVo.setCompanyId(tblMachineLocationHistory.getMstCompany().getId());
            }
            if (null != tblMachineLocationHistory.getLocationId()) {
                aVo.setLocationId(tblMachineLocationHistory.getLocationId());
                aVo.setLocationName(tblMachineLocationHistory.getLocationName());
            }
            if (null != tblMachineLocationHistory.getInstllationSiteId()) {
                aVo.setInstllationSiteId(tblMachineLocationHistory.getInstllationSiteId());
                aVo.setInstllationSiteName(tblMachineLocationHistory.getInstllationSiteName());
            }
            if (null != tblMachineLocationHistory.getMstMachine()) {
                aVo.setMachineId(tblMachineLocationHistory.getMstMachine().getMachineId());
            }
            if (null != tblMachineLocationHistory.getStartDate()) {
                aVo.setStartDateStr(new FileUtil().getDateFormatForStr(tblMachineLocationHistory.getStartDate()));
            }
            if (null != tblMachineLocationHistory.getEndDate()) {
                aVo.setEndDateStr(new FileUtil().getDateFormatForStr(tblMachineLocationHistory.getEndDate()));
            }

            if (null != tblMachineLocationHistory.getChangeReason()) {
                aVo.setChangeReason(tblMachineLocationHistory.getChangeReason());
                aVo.setChangeReasonText(tblMachineLocationHistory.getChangeReasonText());
            }

            aVo.setCreateDate(tblMachineLocationHistory.getCreateDate());
            aVo.setCreateUserUuid(tblMachineLocationHistory.getCreateUserUuid());
            aVo.setUpdateUserUuid(tblMachineLocationHistory.getUpdateUserUuid());
            resVo.add(aVo);
        }
        resList.setTblMachineLocationHistoryVos(resVo);
        return resList;
    }

    /**
     * バッチで設備所在履歴テーブルデータを更新
     *
     * @param machineLocationHistorys
     * @return
     */
    @Transactional
    public BasicResponse updateExtMachineLocationHistorysByBatch(List<TblMachineLocationHistoryVo> machineLocationHistorys) {
        BasicResponse response = new BasicResponse();
        if (machineLocationHistorys != null && !machineLocationHistorys.isEmpty()) {
            for (TblMachineLocationHistoryVo aMachineLocationHistorys : machineLocationHistorys) {
                List<TblMachineLocationHistory> oldMachineLocationHistorys = entityManager.createQuery("SELECT t FROM TblMachineLocationHistory t WHERE 1=1 and t.id=:id ")
                        .setParameter("id", aMachineLocationHistorys.getId())
                        .setMaxResults(1)
                        .getResultList();
                TblMachineLocationHistory newMachineLocationHistory;
                if (null != oldMachineLocationHistorys && !oldMachineLocationHistorys.isEmpty()) {
                    newMachineLocationHistory = oldMachineLocationHistorys.get(0);
                } else {
                    newMachineLocationHistory = new TblMachineLocationHistory();
                    newMachineLocationHistory.setId(aMachineLocationHistorys.getId());
                }
                //自社の設備UUIDに変換
                MstMachine ownerMachine = entityManager.find(MstMachine.class, aMachineLocationHistorys.getMachineId());
                if (null != ownerMachine) {
                    newMachineLocationHistory.setMachineUuid(ownerMachine.getUuid());

                    if (null != aMachineLocationHistorys.getStartDateStr() && !"".equals(aMachineLocationHistorys.getStartDateStr().trim())) {
                        newMachineLocationHistory.setStartDate(new FileUtil().getDateParseForDate(aMachineLocationHistorys.getStartDateStr()));
                    }
                    if (null != aMachineLocationHistorys.getEndDateStr() && !"".equals(aMachineLocationHistorys.getEndDateStr().trim())) {
                        newMachineLocationHistory.setEndDate(new FileUtil().getDateParseForDate(aMachineLocationHistorys.getEndDateStr()));
                    }
                    if (null != aMachineLocationHistorys.getChangeReason()) {
                        newMachineLocationHistory.setChangeReason(aMachineLocationHistorys.getChangeReason());
                        newMachineLocationHistory.setChangeReasonText(aMachineLocationHistorys.getChangeReasonText());
                    }

                    if (null != aMachineLocationHistorys.getCompanyCode() && !aMachineLocationHistorys.getCompanyCode().trim().equals("")) {
                        MstCompany com = entityManager.find(MstCompany.class, aMachineLocationHistorys.getCompanyId());
                        if (null != com) {
                            newMachineLocationHistory.setCompanyId(com.getId());
                            newMachineLocationHistory.setCompanyName(com.getCompanyName());
                        }
                    }
                    if (null != aMachineLocationHistorys.getLocationId() && !"".equals(aMachineLocationHistorys.getLocationId().trim())) {
                        newMachineLocationHistory.setLocationId(aMachineLocationHistorys.getLocationId());
                        newMachineLocationHistory.setLocationName(aMachineLocationHistorys.getLocationName());
                    } else {
                        newMachineLocationHistory.setLocationId(null);
                        newMachineLocationHistory.setLocationName(null);
                    }
                    if (null != aMachineLocationHistorys.getInstllationSiteId() && !"".equals(aMachineLocationHistorys.getInstllationSiteId().trim())) {
                        newMachineLocationHistory.setInstllationSiteId(aMachineLocationHistorys.getInstllationSiteId());
                        newMachineLocationHistory.setInstllationSiteName(aMachineLocationHistorys.getInstllationSiteName());
                    } else {
                        newMachineLocationHistory.setInstllationSiteId(null);
                        newMachineLocationHistory.setInstllationSiteName(null);
                    }

                    newMachineLocationHistory.setCreateDate(aMachineLocationHistorys.getCreateDate());
                    newMachineLocationHistory.setCreateUserUuid(aMachineLocationHistorys.getCreateUserUuid());
                    newMachineLocationHistory.setUpdateDate(new Date());
                    newMachineLocationHistory.setUpdateUserUuid(aMachineLocationHistorys.getUpdateUserUuid());

                    if (null != oldMachineLocationHistorys && !oldMachineLocationHistorys.isEmpty()) {
                        entityManager.merge(newMachineLocationHistory);
                    } else {
                        List<TblMachineLocationHistory> lasMachineLocationHistorys = entityManager.createQuery("SELECT h from TblMachineLocationHistory h where h.machineUuid = :machineUuid ORDER BY h.endDate DESC ")
                                .setParameter("machineUuid", ownerMachine.getUuid()).setMaxResults(1).getResultList();
                        if (null != lasMachineLocationHistorys && !lasMachineLocationHistorys.isEmpty()) {
                            lasMachineLocationHistorys.get(0).setEndDate(new FileUtil().getSpecifiedDayBefore(newMachineLocationHistory.getStartDate()));
                            entityManager.merge(lasMachineLocationHistorys.get(0));
                        }
                        entityManager.persist(newMachineLocationHistory);
                    }
                }
            }
        }
        response.setError(false);
        return response;
    }
}
