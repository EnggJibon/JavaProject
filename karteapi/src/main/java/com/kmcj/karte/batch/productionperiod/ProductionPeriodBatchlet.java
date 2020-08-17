package com.kmcj.karte.batch.productionperiod;

import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.aggregated.TblAggregated;
import com.kmcj.karte.resources.aggregated.TblAggregatedService;
import com.kmcj.karte.resources.machine.daily.report.detail.TblMachineDailyReportDetail;
import com.kmcj.karte.resources.machine.production.TblMachineProductionForDay;
import com.kmcj.karte.resources.machine.production.TblMachineProductionForDayPK;
import com.kmcj.karte.resources.machine.production.TblMachineProductionForMonth;
import com.kmcj.karte.resources.machine.production.TblMachineProductionForMonthPK;
import com.kmcj.karte.resources.machine.production.TblMachineProductionForWeek;
import com.kmcj.karte.resources.machine.production.TblMachineProductionForWeekPK;
import com.kmcj.karte.resources.machine.production.TblMachineProductionPeriodService;
import com.kmcj.karte.resources.machine.production.TblMachineProductionPeriodVo;
import com.kmcj.karte.resources.mold.production.TblMoldProductionForDay;
import com.kmcj.karte.resources.mold.production.TblMoldProductionForDayPK;
import com.kmcj.karte.resources.mold.production.TblMoldProductionForMonth;
import com.kmcj.karte.resources.mold.production.TblMoldProductionForMonthPK;
import com.kmcj.karte.resources.mold.production.TblMoldProductionForWeek;
import com.kmcj.karte.resources.mold.production.TblMoldProductionForWeekPK;
import com.kmcj.karte.resources.mold.production.TblMoldProductionPeriodService;
import com.kmcj.karte.resources.mold.production.TblMoldProductionPeriodVo;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author penggd
 */
@Named
@Dependent
public class ProductionPeriodBatchlet extends AbstractBatchlet {

    // ジョブ名称を取得するためにJobContextをインジェクション
    @Inject
    JobContext jobContext;

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private TblMoldProductionPeriodService tblMoldProductionPeriodService;

    @Inject
    private TblMachineProductionPeriodService tblMachineProductionPeriodService;

    @Inject
    private TblAggregatedService tblAggregatedService;
    
    @Inject
    private CnfSystemService cnfSystemService;

    private Logger logger = Logger.getLogger(ProductionPeriodBatchlet.class.getName());
    private final static String BATCH_NAME = "productionPeriodBatchlet";
    private final static Level LOG_LEVEL = Level.FINE;

    @Override
    @Transactional
    public String process() throws Exception {

        logger.log(Level.INFO, "ProductionPeriodBatchlet: Started.");

        logger.log(LOG_LEVEL, "getUnAggregateReportList: 未集計の機械日報レコードを抽出する Started.");
        List<TblMachineDailyReportDetail> tblMachineDailyReportDetails = getUnAggregateReportList();
        logger.log(LOG_LEVEL, "getUnAggregateReportList: 未集計の機械日報レコードを抽出する Ended.");

        logger.log(LOG_LEVEL, "productionForMold: 金型の生産実績を集計する Started.");
        productionForMold(tblMachineDailyReportDetails);
        logger.log(LOG_LEVEL, "productionForMold: 金型の生産実績を集計する Ended.");

        logger.log(LOG_LEVEL, "productionForMachine: 設備の生産実績を集計する Started.");
        productionForMachine(tblMachineDailyReportDetails);
        logger.log(LOG_LEVEL, "productionForMachine: 設備の生産実績を集計する Ended.");

        logger.log(LOG_LEVEL, "insertAggregated: 集計済みテーブルの登録処理 Started.");
        insertAggregated(tblMachineDailyReportDetails);
        logger.log(LOG_LEVEL, "insertAggregated: 集計済みテーブルの登録処理 Ended.");

        logger.log(Level.INFO, "ProductionPeriodBatchlet: Ended.");

        return "SUCCESS";
    }

    /**
     * 金型の生産実績を集計する
     *
     * @param tblMachineDailyReportDetails
     *
     */
    private void productionForMold(List<TblMachineDailyReportDetail> tblMachineDailyReportDetails) {

        logger.log(LOG_LEVEL, "getMoldProductionPerList: 金型生産実績の集計する Started.");
        List<TblMoldProductionPeriodVo> tblMoldProductionPeriodVos = getMoldProductionPerList(
                tblMachineDailyReportDetails);
        logger.log(LOG_LEVEL, "getMoldProductionPerList: 金型生産実績の集計する Ended.");

        logger.log(LOG_LEVEL, "insertMoldProductionPerDay: 金型日別完成数集計テーブルを登録する Started.");
        insertMoldProductionPerDay(tblMoldProductionPeriodVos);
        logger.log(LOG_LEVEL, "insertMoldProductionPerDay: 金型日別完成数集計テーブルを登録する Ended.");

        logger.log(LOG_LEVEL, "insertMoldProductionPerWeek: 金型週別完成数集計テーブルを登録する Started.");
        insertMoldProductionPerWeek(tblMoldProductionPeriodVos);
        logger.log(LOG_LEVEL, "insertMoldProductionPerWeek: 金型週別完成数集計テーブルを登録する Ended.");

        logger.log(LOG_LEVEL, "insertMoldProductionPerDay: 金型月別完成数集計テーブルを登録する Started.");
        insertMoldProductionPerMonth(tblMoldProductionPeriodVos);
        logger.log(LOG_LEVEL, "insertMoldProductionPerDay: 金型月別完成数集計テーブルを登録する Ended.");

    }

    /**
     * 設備の生産実績を集計する
     *
     * @param tblMachineDailyReportDetails
     *
     */
    private void productionForMachine(List<TblMachineDailyReportDetail> tblMachineDailyReportDetails) {

        logger.log(LOG_LEVEL, "getMachineProductionPerList: 設備生産実績の集計する Started.");
        List<TblMachineProductionPeriodVo> tblMachineProductionPeriodVos = getMachineProductionPerList(
                tblMachineDailyReportDetails);
        logger.log(LOG_LEVEL, "getMachineProductionPerList: 設備生産実績の集計する Ended.");

        logger.log(LOG_LEVEL, "insertMachineProductionPerDay: 設備日別完成数集計テーブルを登録する Started.");
        insertMachineProductionPerDay(tblMachineProductionPeriodVos);
        logger.log(LOG_LEVEL, "insertMachineProductionPerDay: 設備日別完成数集計テーブルを登録する Ended.");

        logger.log(LOG_LEVEL, "insertMachineProductionPerWeek: 設備週別完成数集計テーブルを登録する Started.");
        insertMachineProductionPerWeek(tblMachineProductionPeriodVos);
        logger.log(LOG_LEVEL, "insertMachineProductionPerWeek: 設備週別完成数集計テーブルを登録する Ended.");

        logger.log(LOG_LEVEL, "insertMachineProductionPerMonth: 設備月別完成数集計テーブルを登録する Started.");
        insertMachineProductionPerMonth(tblMachineProductionPeriodVos);
        logger.log(LOG_LEVEL, "insertMachineProductionPerMonth: 設備月別完成数集計テーブルを登録する Ended.");

    }

    /**
     * 未集計の機械日報を取得する
     *
     */
    private List<TblMachineDailyReportDetail> getUnAggregateReportList() {

        // 未集計の機械日報を取得する
        Query query = entityManager.createQuery(
                "SELECT t FROM TblMachineDailyReportDetail t JOIN FETCH t.tblMachineDailyReport.tblProduction "
                        + "WHERE t.tblMachineDailyReport.tblProduction.machineUuid IS NOT NULL AND NOT EXISTS "
                        + "(SELECT a FROM TblAggregated a WHERE t.id = a.macReportDetailId AND a.updDeleteFlg = 0) "
                        + "ORDER BY t.tblMachineDailyReport.tblMachineDailyReportPK.productionDate ASC");

        return (List<TblMachineDailyReportDetail>) query.getResultList();

    }

    /**
     * 金型生産実績の集計する
     *
     * @param tblMachineDailyReportDetails
     *
     * @return
     *
     */
    private List<TblMoldProductionPeriodVo> getMoldProductionPerList(
            List<TblMachineDailyReportDetail> tblMachineDailyReportDetails) {

        List<TblMoldProductionPeriodVo> tblMoldProductionPeriodVos = new ArrayList();

        List<TblMachineDailyReportDetail> moldProductionList = new ArrayList();

        // 金型UUID存在の場合、集計する
        for (TblMachineDailyReportDetail tblMachineDailyReportDetail : tblMachineDailyReportDetails) {

            if (StringUtils.isNotEmpty(
                    tblMachineDailyReportDetail.getTblMachineDailyReport().getTblProduction().getMoldUuid())) {

                moldProductionList.add(tblMachineDailyReportDetail);
            }

        }

        // 機械日報リストのソート処理（生産日、金型UUID、部品UUID）
        Collections.sort(moldProductionList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                TblMachineDailyReportDetail obj1 = (TblMachineDailyReportDetail) o1;
                TblMachineDailyReportDetail obj2 = (TblMachineDailyReportDetail) o2;

                // 比較用の生産日
                String productionDateStr1 = DateFormat.dateToStr(
                        obj1.getTblMachineDailyReport().getTblMachineDailyReportPK().getProductionDate(),
                        DateFormat.DATE_FORMAT);
                String productionDateStr2 = DateFormat.dateToStr(
                        obj2.getTblMachineDailyReport().getTblMachineDailyReportPK().getProductionDate(),
                        DateFormat.DATE_FORMAT);

                // 比較用金型UUID
                String moldUuid1 = obj1.getTblMachineDailyReport().getTblProduction().getMoldUuid();
                String moldUuid2 = obj2.getTblMachineDailyReport().getTblProduction().getMoldUuid();

                if (moldUuid1.equals(moldUuid2)) {

                    if (FileUtil.getStr(obj1.getComponentId()).equals(FileUtil.getStr(obj2.getComponentId()))) {

                        return productionDateStr1.compareTo(productionDateStr2);

                    } else {

                        return FileUtil.getStr(obj1.getComponentId()).compareTo(FileUtil.getStr(obj2.getComponentId()));

                    }

                } else {

                    return moldUuid1.compareTo(moldUuid2);
                }

            }
        });

        String tempProductionDateStr = "";
        String tempMoldUuid = "";
        String tempComponentId = "";

        TblMoldProductionPeriodVo tblMoldProductionPeriodVo = new TblMoldProductionPeriodVo();

        // 生産日と金型UUIDと部品UUIDをPKとして、完成数を集計する
        for (int i = 0; i < moldProductionList.size(); i++) {

            TblMachineDailyReportDetail tTblMachineDailyReportDetail = moldProductionList.get(i);

            if (i == 0) {

                tempProductionDateStr = DateFormat.dateToStr(tTblMachineDailyReportDetail.getTblMachineDailyReport()
                        .getTblMachineDailyReportPK().getProductionDate(), DateFormat.DATE_FORMAT);
                tempMoldUuid = tTblMachineDailyReportDetail.getTblMachineDailyReport().getTblProduction().getMoldUuid();
                tempComponentId = FileUtil.getStr(tTblMachineDailyReportDetail.getComponentId());

                tblMoldProductionPeriodVo.setProductionDate(tTblMachineDailyReportDetail.getTblMachineDailyReport()
                        .getTblMachineDailyReportPK().getProductionDate());
                tblMoldProductionPeriodVo.setMoldUuid(
                        tTblMachineDailyReportDetail.getTblMachineDailyReport().getTblProduction().getMoldUuid());
                tblMoldProductionPeriodVo
                        .setComponentId(FileUtil.getStr(tTblMachineDailyReportDetail.getComponentId()));
                tblMoldProductionPeriodVo
                        .setTotal(FileUtil.getIntegerValue(tTblMachineDailyReportDetail.getCompleteCount()));

            } else {

                String productionDateStr = DateFormat.dateToStr(tTblMachineDailyReportDetail.getTblMachineDailyReport()
                        .getTblMachineDailyReportPK().getProductionDate(), DateFormat.DATE_FORMAT);

                // PK一致しない場合、新しいレコードを作製する
                if (!tempProductionDateStr.equals(productionDateStr)
                        || !tempMoldUuid.equals(tTblMachineDailyReportDetail.getTblMachineDailyReport()
                                .getTblProduction().getMoldUuid())
                        || !tempComponentId.equals(FileUtil.getStr(tTblMachineDailyReportDetail.getComponentId()))) {

                    tblMoldProductionPeriodVos.add(tblMoldProductionPeriodVo);

                    tblMoldProductionPeriodVo = new TblMoldProductionPeriodVo();

                    tempProductionDateStr = DateFormat.dateToStr(tTblMachineDailyReportDetail.getTblMachineDailyReport()
                            .getTblMachineDailyReportPK().getProductionDate(), DateFormat.DATE_FORMAT);
                    tempMoldUuid = tTblMachineDailyReportDetail.getTblMachineDailyReport().getTblProduction()
                            .getMoldUuid();
                    tempComponentId = FileUtil.getStr(tTblMachineDailyReportDetail.getComponentId());

                    tblMoldProductionPeriodVo.setProductionDate(tTblMachineDailyReportDetail.getTblMachineDailyReport()
                            .getTblMachineDailyReportPK().getProductionDate());
                    tblMoldProductionPeriodVo.setMoldUuid(
                            tTblMachineDailyReportDetail.getTblMachineDailyReport().getTblProduction().getMoldUuid());
                    tblMoldProductionPeriodVo
                            .setComponentId(FileUtil.getStr(tTblMachineDailyReportDetail.getComponentId()));
                    tblMoldProductionPeriodVo
                            .setTotal(FileUtil.getIntegerValue(tTblMachineDailyReportDetail.getCompleteCount()));

                } else {// PK一致の場合、完成数の加算する

                    tblMoldProductionPeriodVo.setTotal(tblMoldProductionPeriodVo.getTotal()
                            + FileUtil.getIntegerValue(tTblMachineDailyReportDetail.getCompleteCount()));
                }
            }

            // 最終のレコードを追加する
            if (i == tblMachineDailyReportDetails.size() - 1) {
                tblMoldProductionPeriodVos.add(tblMoldProductionPeriodVo);
            }

        }

        return tblMoldProductionPeriodVos;

    }

    /**
     * 金型日別集計テーブルに登録する
     *
     * @param tblMoldProductionPeriodVos
     *
     *
     */
    @Transactional
    private void insertMoldProductionPerDay(List<TblMoldProductionPeriodVo> tblMoldProductionPeriodVos) {

        List<TblMoldProductionForDay> insertTblMoldProductionForDayList = new ArrayList();
        List<TblMoldProductionForDay> updTblMoldProductionForDayList = new ArrayList();

        for (TblMoldProductionPeriodVo tblMoldProductionPeriodVo : tblMoldProductionPeriodVos) {

            TblMoldProductionForDay tblMoldProductionForDay = new TblMoldProductionForDay();

            TblMoldProductionForDay chkTblMoldProductionForDay = tblMoldProductionPeriodService
                    .getProductionForDaySingleByPK(tblMoldProductionPeriodVo.getMoldUuid(),
                            tblMoldProductionPeriodVo.getComponentId(), tblMoldProductionPeriodVo.getProductionDate());

            if (null == chkTblMoldProductionForDay) {

                setTblMoldProductionForDay(tblMoldProductionForDay, tblMoldProductionPeriodVo);

                tblMoldProductionForDay.setCreateUserUuid(BATCH_NAME);
                tblMoldProductionForDay.setUpdateUserUuid(BATCH_NAME);

                Date nowDate = new Date();
                tblMoldProductionForDay.setCreateDate(nowDate);
                tblMoldProductionForDay.setUpdateDate(nowDate);

                insertTblMoldProductionForDayList.add(tblMoldProductionForDay);

            } else {

                setTblMoldProductionForDay(tblMoldProductionForDay, tblMoldProductionPeriodVo);
                tblMoldProductionForDay.setUpdateUserUuid(BATCH_NAME);
                tblMoldProductionForDay.setCompletedCount(tblMoldProductionForDay.getCompletedCount()
                            + chkTblMoldProductionForDay.getCompletedCount());
                Date nowDate = new Date();
                tblMoldProductionForDay.setUpdateDate(nowDate);
                tblMoldProductionForDay.setCreateDate(chkTblMoldProductionForDay.getCreateDate());
                tblMoldProductionForDay.setCreateUserUuid(chkTblMoldProductionForDay.getCreateUserUuid());

                updTblMoldProductionForDayList.add(tblMoldProductionForDay);

            }
        }

        int insertCount = tblMoldProductionPeriodService.batchInsertByType(insertTblMoldProductionForDayList, 1);
        logger.log(LOG_LEVEL, "insertMoldProductionPerDay:金型日別集計テーブルの登録件数：" + insertCount);

        int updateCount = tblMoldProductionPeriodService.batchUpdateByType(updTblMoldProductionForDayList, 1);
        logger.log(LOG_LEVEL, "updMoldProductionPerDay:金型日別集計テーブルの更新件数：" + updateCount);
    }

    /**
     * 金型週別集計テーブルに登録する
     *
     * @param tblMoldProductionPeriodVos
     *
     *
     */
    @Transactional
    private void insertMoldProductionPerWeek(List<TblMoldProductionPeriodVo> tblMoldProductionPeriodVos) {

        List<TblMoldProductionForWeek> tblMoldProductionForWeekList = new ArrayList();

        int listSize = tblMoldProductionPeriodVos.size();
        
        CnfSystem cnf = cnfSystemService.findByKey("system", "business_start_day_of_week");
        
        int firstDay = Integer.parseInt(cnf.getConfigValue());

        if (listSize > 0) {

            Date minDate = tblMoldProductionPeriodVos.get(0).getProductionDate();

            Date startDay = DateFormat.getFirstDayOfWeek(firstDay, minDate);
            String startDayStr = DateFormat.dateToStr(startDay, DateFormat.DATE_FORMAT);
            
            Date sunDay = DateFormat.getAfterDays(startDay, 6);
            String sunDayStr = DateFormat.dateToStr(sunDay, DateFormat.DATE_FORMAT);

            String tempMoldUuid = "";
            String tempComponentId = "";

            TblMoldProductionForWeek tempTblMoldProductionForWeek = new TblMoldProductionForWeek();

            // 週別生産実績の完成数を集計する
            for (int i = 0; i < listSize; i++) {

                TblMoldProductionPeriodVo tblMoldProductionPeriodVo = tblMoldProductionPeriodVos.get(i);

                if (i == 0) {

                    setTblMoldProductionForWeek(tempTblMoldProductionForWeek, tblMoldProductionPeriodVo, startDay,
                            sunDay);

                    tempMoldUuid = tblMoldProductionPeriodVo.getMoldUuid();
                    tempComponentId = tblMoldProductionPeriodVo.getComponentId();

                    tempTblMoldProductionForWeek.setCompletedCount(tblMoldProductionPeriodVo.getTotal());

                } else {

                    Date productionDate = tblMoldProductionPeriodVo.getProductionDate();
                    
                    Date tempStartDay = DateFormat.getFirstDayOfWeek(firstDay, productionDate);
                    String tempStartDayStr = DateFormat.dateToStr(tempStartDay, DateFormat.DATE_FORMAT);
                    
                    Date tempSunDay = DateFormat.getAfterDays(tempStartDay, 6);
                    String tempSunDayStr = DateFormat.dateToStr(tempSunDay, DateFormat.DATE_FORMAT);

                    if (!tempMoldUuid.equals(tblMoldProductionPeriodVo.getMoldUuid())
                            || !tempComponentId.equals(FileUtil.getStr(tblMoldProductionPeriodVo.getComponentId()))
                            || !startDayStr.equals(tempStartDayStr) || !sunDayStr.equals(tempSunDayStr)) {

                        tblMoldProductionForWeekList.add(tempTblMoldProductionForWeek);

                        tempTblMoldProductionForWeek = new TblMoldProductionForWeek();

                        startDay = tempStartDay;
                        sunDay = tempSunDay;
                        startDayStr = tempStartDayStr;
                        sunDayStr = tempSunDayStr;

                        setTblMoldProductionForWeek(tempTblMoldProductionForWeek, tblMoldProductionPeriodVo, startDay,
                                sunDay);

                        tempMoldUuid = tblMoldProductionPeriodVo.getMoldUuid();
                        tempComponentId = tblMoldProductionPeriodVo.getComponentId();

                        tempTblMoldProductionForWeek.setCompletedCount(tblMoldProductionPeriodVo.getTotal());

                    } else {

                        tempTblMoldProductionForWeek.setCompletedCount(tempTblMoldProductionForWeek.getCompletedCount()
                                + tblMoldProductionPeriodVo.getTotal());

                    }

                }

                // 最終のレコードを追加する
                if (i == listSize - 1) {
                    tblMoldProductionForWeekList.add(tempTblMoldProductionForWeek);
                }

            }

            List<TblMoldProductionForWeek> insertTblMoldProductionForWeekList = new ArrayList();
            List<TblMoldProductionForWeek> updateTblMoldProductionForWeekList = new ArrayList();

            for (TblMoldProductionForWeek tblMoldProductionForWeek : tblMoldProductionForWeekList) {

                String moldUuid = tblMoldProductionForWeek.getTblMoldProductionForWeekPK().getMoldUuid();
                String componentId = tblMoldProductionForWeek.getTblMoldProductionForWeekPK().getComponentId();
                Date startDate = tblMoldProductionForWeek.getTblMoldProductionForWeekPK().getProductionDateStart();
                Date endDate = tblMoldProductionForWeek.getTblMoldProductionForWeekPK().getProductionDateEnd();

                TblMoldProductionForWeek chkTblMoldProductionForWeek = tblMoldProductionPeriodService
                        .getProductionForWeekSingleByPK(moldUuid, componentId, startDate, endDate);

                if (null == chkTblMoldProductionForWeek) {

                    tblMoldProductionForWeek.setCreateUserUuid(BATCH_NAME);
                    tblMoldProductionForWeek.setUpdateUserUuid(BATCH_NAME);

                    Date nowDate = new Date();
                    tblMoldProductionForWeek.setCreateDate(nowDate);
                    tblMoldProductionForWeek.setUpdateDate(nowDate);

                    insertTblMoldProductionForWeekList.add(tblMoldProductionForWeek);

                } else {

                    tblMoldProductionForWeek.setUpdateUserUuid(BATCH_NAME);
                    tblMoldProductionForWeek.setCompletedCount(tblMoldProductionForWeek.getCompletedCount()
                            + chkTblMoldProductionForWeek.getCompletedCount());
                    Date nowDate = new Date();
                    tblMoldProductionForWeek.setUpdateDate(nowDate);
                    tblMoldProductionForWeek.setCreateDate(chkTblMoldProductionForWeek.getCreateDate());
                    tblMoldProductionForWeek.setCreateUserUuid(chkTblMoldProductionForWeek.getCreateUserUuid());

                    updateTblMoldProductionForWeekList.add(tblMoldProductionForWeek);
                }
            }

            int insertCount = tblMoldProductionPeriodService.batchInsertByType(insertTblMoldProductionForWeekList, 2);
            logger.log(LOG_LEVEL, "insertMoldProductionPerWeek:金型週別集計テーブルの登録件数：" + insertCount);

            int updateCount = tblMoldProductionPeriodService.batchUpdateByType(updateTblMoldProductionForWeekList, 2);
            logger.log(LOG_LEVEL, "updMoldProductionPerWeek:金型週別集計テーブルの更新件数：" + updateCount);

        }

    }

    /**
     * 金型日別集計テーブル登録用のObjectを設定
     *
     * @param tblMoldProductionForDay
     * @param tblMoldProductionPeriodVo
     *
     *
     */
    private void setTblMoldProductionForDay(TblMoldProductionForDay tblMoldProductionForDay,
            TblMoldProductionPeriodVo tblMoldProductionPeriodVo) {

        TblMoldProductionForDayPK tblMoldProductionForDayPK = new TblMoldProductionForDayPK();

        tblMoldProductionForDayPK.setMoldUuid(tblMoldProductionPeriodVo.getMoldUuid());
        tblMoldProductionForDayPK.setComponentId(tblMoldProductionPeriodVo.getComponentId());
        tblMoldProductionForDayPK.setProductionDate(tblMoldProductionPeriodVo.getProductionDate());
        tblMoldProductionForDay.setTblMoldProductionForDayPK(tblMoldProductionForDayPK);
        tblMoldProductionForDay.setCompletedCount(tblMoldProductionPeriodVo.getTotal());

    }

    /**
     * 金型週別集計テーブル登録用のObjectを設定
     *
     * @param tblMoldProductionForWeek
     * @param TblMoldProductionPeriodVo
     * @param startDay
     * @param endDay
     *
     *
     */
    private void setTblMoldProductionForWeek(TblMoldProductionForWeek tblMoldProductionForWeek,
            TblMoldProductionPeriodVo tblMoldProductionPeriodVo, Date startDay, Date endDay) {

        TblMoldProductionForWeekPK tblMoldProductionForWeekPK = new TblMoldProductionForWeekPK();

        tblMoldProductionForWeekPK.setMoldUuid(tblMoldProductionPeriodVo.getMoldUuid());
        tblMoldProductionForWeekPK.setComponentId(tblMoldProductionPeriodVo.getComponentId());
        tblMoldProductionForWeekPK.setProductionDateStart(startDay);
        tblMoldProductionForWeekPK.setProductionDateEnd(endDay);
        tblMoldProductionForWeek.setTblMoldProductionForWeekPK(tblMoldProductionForWeekPK);
    }

    /**
     * 金型月別集計テーブルに登録する
     *
     * @param tblMoldProductionPeriodVos
     *
     *
     */
    @Transactional
    private void insertMoldProductionPerMonth(List<TblMoldProductionPeriodVo> tblMoldProductionPeriodVos) {

        List<TblMoldProductionForMonth> tblMoldProductionForMonthList = new ArrayList();

        int listSize = tblMoldProductionPeriodVos.size();

        if (listSize > 0) {

            Date minDate = tblMoldProductionPeriodVos.get(0).getProductionDate();

            String firstDayStr = DateFormat.getFirstDay(minDate);
            String lastDayStr = DateFormat.getLastDay(minDate);

            Date firstDay = DateFormat.strToDate(firstDayStr);
            Date lastDay = DateFormat.strToDate(lastDayStr);

            String tempMoldUuid = "";
            String tempComponentId = "";

            TblMoldProductionForMonth tempTblMoldProductionForMonth = new TblMoldProductionForMonth();

            // 月別生産実績の完成数を集計する
            for (int i = 0; i < listSize; i++) {

                TblMoldProductionPeriodVo tblMoldProductionPeriodVo = tblMoldProductionPeriodVos.get(i);

                if (i == 0) {

                    setTblMoldProductionForMonth(tempTblMoldProductionForMonth, tblMoldProductionPeriodVo, firstDay);

                    tempMoldUuid = tblMoldProductionPeriodVo.getMoldUuid();
                    tempComponentId = tblMoldProductionPeriodVo.getComponentId();

                    tempTblMoldProductionForMonth.setCompletedCount(tblMoldProductionPeriodVo.getTotal());

                } else {

                    Date productionDate = tblMoldProductionPeriodVo.getProductionDate();

                    String tempFirstDayStr = DateFormat.getFirstDay(productionDate);
                    Date tempFirstDay = DateFormat.strToDate(tempFirstDayStr);

                    String tempLastDayStr = DateFormat.getLastDay(productionDate);

                    if (!tempMoldUuid.equals(tblMoldProductionPeriodVo.getMoldUuid())
                            || !tempComponentId.equals(FileUtil.getStr(tblMoldProductionPeriodVo.getComponentId()))
                            || !firstDayStr.equals(tempFirstDayStr) || !lastDayStr.equals(tempLastDayStr)) {

                        tblMoldProductionForMonthList.add(tempTblMoldProductionForMonth);

                        tempTblMoldProductionForMonth = new TblMoldProductionForMonth();

                        firstDay = tempFirstDay;
                        firstDayStr = tempFirstDayStr;
                        lastDayStr = tempLastDayStr;

                        setTblMoldProductionForMonth(tempTblMoldProductionForMonth, tblMoldProductionPeriodVo,
                                firstDay);

                        tempMoldUuid = tblMoldProductionPeriodVo.getMoldUuid();
                        tempComponentId = tblMoldProductionPeriodVo.getComponentId();

                        tempTblMoldProductionForMonth.setCompletedCount(tblMoldProductionPeriodVo.getTotal());

                    } else {

                        tempTblMoldProductionForMonth
                                .setCompletedCount(tempTblMoldProductionForMonth.getCompletedCount()
                                        + tblMoldProductionPeriodVo.getTotal());

                    }

                }

                // 最終のレコードを追加する
                if (i == listSize - 1) {
                    tblMoldProductionForMonthList.add(tempTblMoldProductionForMonth);
                }

            }

            List<TblMoldProductionForMonth> insertTblMoldProductionForMonthList = new ArrayList();
            List<TblMoldProductionForMonth> updateTblMoldProductionForMonthList = new ArrayList();

            for (TblMoldProductionForMonth tblMoldProductionForMonth : tblMoldProductionForMonthList) {

                String moldUuid = tblMoldProductionForMonth.getTblMoldProductionForMonthPK().getMoldUuid();
                String componentId = tblMoldProductionForMonth.getTblMoldProductionForMonthPK().getComponentId();
                String productionMonth = tblMoldProductionForMonth.getTblMoldProductionForMonthPK()
                        .getProductionMonth();

                TblMoldProductionForMonth chkTblMoldProductionForMonth = tblMoldProductionPeriodService
                        .getProductionForMonthSingleByPK(moldUuid, componentId, productionMonth);

                if (null == chkTblMoldProductionForMonth) {

                    tblMoldProductionForMonth.setCreateUserUuid(BATCH_NAME);
                    tblMoldProductionForMonth.setUpdateUserUuid(BATCH_NAME);

                    Date nowDate = new Date();
                    tblMoldProductionForMonth.setCreateDate(nowDate);
                    tblMoldProductionForMonth.setUpdateDate(nowDate);

                    insertTblMoldProductionForMonthList.add(tblMoldProductionForMonth);

                } else {

                    tblMoldProductionForMonth.setUpdateUserUuid(BATCH_NAME);
                    tblMoldProductionForMonth.setCompletedCount(tblMoldProductionForMonth.getCompletedCount()
                            + chkTblMoldProductionForMonth.getCompletedCount());
                    Date nowDate = new Date();
                    tblMoldProductionForMonth.setUpdateDate(nowDate);
                    tblMoldProductionForMonth.setCreateDate(chkTblMoldProductionForMonth.getCreateDate());
                    tblMoldProductionForMonth.setCreateUserUuid(chkTblMoldProductionForMonth.getCreateUserUuid());

                    updateTblMoldProductionForMonthList.add(tblMoldProductionForMonth);
                }
            }

            int insertCount = tblMoldProductionPeriodService.batchInsertByType(insertTblMoldProductionForMonthList, 3);
            logger.log(LOG_LEVEL, "insertMoldProductionPerWeek:金型月別集計テーブルの登録件数：" + insertCount);

            int updateCount = tblMoldProductionPeriodService.batchUpdateByType(updateTblMoldProductionForMonthList, 3);
            logger.log(LOG_LEVEL, "updMoldProductionPerWeek:金型月別集計テーブルの更新件数：" + updateCount);

        }

    }

    /**
     * 金型月別集計テーブル登録用のObjectを設定
     *
     * @param tblMoldProductionForMonth
     * @param TblMoldProductionPeriodVo
     * @param productionMonth
     *
     *
     */
    private void setTblMoldProductionForMonth(TblMoldProductionForMonth tblMoldProductionForMonth,
            TblMoldProductionPeriodVo tblMoldProductionPeriodVo, Date productionMonth) {

        TblMoldProductionForMonthPK tblMoldProductionForMonthPK = new TblMoldProductionForMonthPK();

        tblMoldProductionForMonthPK.setMoldUuid(tblMoldProductionPeriodVo.getMoldUuid());
        tblMoldProductionForMonthPK.setComponentId(tblMoldProductionPeriodVo.getComponentId());
        tblMoldProductionForMonthPK
                .setProductionMonth(DateFormat.dateToStr(productionMonth, DateFormat.DATE_FORMAT_MONTH));
        tblMoldProductionForMonth.setTblMoldProductionForMonthPK(tblMoldProductionForMonthPK);
    }

    /**
     * 設備生産実績の集計する
     *
     * @param tblMachineDailyReportDetails
     *
     * @return
     *
     */
    private List<TblMachineProductionPeriodVo> getMachineProductionPerList(
            List<TblMachineDailyReportDetail> tblMachineDailyReportDetails) {

        List<TblMachineProductionPeriodVo> tblMachineProductionPeriodVos = new ArrayList();

        // 機械日報リストのソート処理（生産日、設備UUID、部品UUID）
        Collections.sort(tblMachineDailyReportDetails, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                TblMachineDailyReportDetail obj1 = (TblMachineDailyReportDetail) o1;
                TblMachineDailyReportDetail obj2 = (TblMachineDailyReportDetail) o2;

                // 比較用の生産日
                String productionDateStr1 = DateFormat.dateToStr(
                        obj1.getTblMachineDailyReport().getTblMachineDailyReportPK().getProductionDate(),
                        DateFormat.DATE_FORMAT);
                String productionDateStr2 = DateFormat.dateToStr(
                        obj2.getTblMachineDailyReport().getTblMachineDailyReportPK().getProductionDate(),
                        DateFormat.DATE_FORMAT);

                // 比較用設備UUID
                String machineUuid1 = obj1.getTblMachineDailyReport().getTblProduction().getMachineUuid();
                String machineUuid2 = obj2.getTblMachineDailyReport().getTblProduction().getMachineUuid();

                // 比較用金型UUID
                String moldUuid1 = FileUtil.getStr(obj1.getTblMachineDailyReport().getTblProduction().getMoldUuid());
                String moldUuid2 = FileUtil.getStr(obj2.getTblMachineDailyReport().getTblProduction().getMoldUuid());

                if (machineUuid1.equals(machineUuid2)) {

                    if (moldUuid1.equals(moldUuid2)) {

                        if (FileUtil.getStr(obj1.getComponentId()).equals(FileUtil.getStr(obj2.getComponentId()))) {

                            return productionDateStr1.compareTo(productionDateStr2);

                        } else {

                            return FileUtil.getStr(obj1.getComponentId())
                                    .compareTo(FileUtil.getStr(obj2.getComponentId()));
                        }

                    } else {

                        return moldUuid1.compareTo(moldUuid2);

                    }
                } else {

                    return machineUuid1.compareTo(machineUuid2);
                }

            }
        });

        String tempProductionDateStr = "";
        String tempMachineUuid = "";
        String tempComponentId = "";
        String tempMoldUuid = "";

        TblMachineProductionPeriodVo tblMachineProductionPeriodVo = new TblMachineProductionPeriodVo();

        // 生産日と設備UUIDと部品UUIDをPKとして、完成数を集計する
        for (int i = 0; i < tblMachineDailyReportDetails.size(); i++) {

            TblMachineDailyReportDetail tTblMachineDailyReportDetail = tblMachineDailyReportDetails.get(i);

            if (i == 0) {

                tempProductionDateStr = DateFormat.dateToStr(tTblMachineDailyReportDetail.getTblMachineDailyReport()
                        .getTblMachineDailyReportPK().getProductionDate(), DateFormat.DATE_FORMAT);
                tempMachineUuid = tTblMachineDailyReportDetail.getTblMachineDailyReport().getTblProduction()
                        .getMachineUuid();
                tempComponentId = FileUtil.getStr(tTblMachineDailyReportDetail.getComponentId());
                tempMoldUuid = FileUtil.getStr(
                        tTblMachineDailyReportDetail.getTblMachineDailyReport().getTblProduction().getMoldUuid());

                tblMachineProductionPeriodVo.setProductionDate(tTblMachineDailyReportDetail.getTblMachineDailyReport()
                        .getTblMachineDailyReportPK().getProductionDate());
                tblMachineProductionPeriodVo.setMachineUuid(
                        tTblMachineDailyReportDetail.getTblMachineDailyReport().getTblProduction().getMachineUuid());
                tblMachineProductionPeriodVo
                        .setComponentId(FileUtil.getStr(tTblMachineDailyReportDetail.getComponentId()));
                tblMachineProductionPeriodVo
                        .setTotal(FileUtil.getIntegerValue(tTblMachineDailyReportDetail.getCompleteCount()));
                tblMachineProductionPeriodVo.setMoldUuid(FileUtil.getStr(
                        tTblMachineDailyReportDetail.getTblMachineDailyReport().getTblProduction().getMoldUuid()));

            } else {

                String productionDateStr = DateFormat.dateToStr(tTblMachineDailyReportDetail.getTblMachineDailyReport()
                        .getTblMachineDailyReportPK().getProductionDate(), DateFormat.DATE_FORMAT);

                // PK一致しない場合、新しいレコードを作製する
                if (!tempProductionDateStr.equals(productionDateStr)
                        || !tempMachineUuid.equals(tTblMachineDailyReportDetail.getTblMachineDailyReport()
                                .getTblProduction().getMachineUuid())
                        || !tempComponentId.equals(FileUtil.getStr(tTblMachineDailyReportDetail.getComponentId()))
                        || !tempMoldUuid.equals(FileUtil.getStr(tTblMachineDailyReportDetail.getTblMachineDailyReport()
                                .getTblProduction().getMoldUuid()))) {

                    tblMachineProductionPeriodVos.add(tblMachineProductionPeriodVo);

                    tblMachineProductionPeriodVo = new TblMachineProductionPeriodVo();

                    tempProductionDateStr = DateFormat.dateToStr(tTblMachineDailyReportDetail.getTblMachineDailyReport()
                            .getTblMachineDailyReportPK().getProductionDate(), DateFormat.DATE_FORMAT);
                    tempMachineUuid = tTblMachineDailyReportDetail.getTblMachineDailyReport().getTblProduction()
                            .getMachineUuid();
                    tempComponentId = FileUtil.getStr(tTblMachineDailyReportDetail.getComponentId());
                    tempMoldUuid = FileUtil.getStr(
                            tTblMachineDailyReportDetail.getTblMachineDailyReport().getTblProduction().getMoldUuid());

                    tblMachineProductionPeriodVo.setProductionDate(tTblMachineDailyReportDetail
                            .getTblMachineDailyReport().getTblMachineDailyReportPK().getProductionDate());
                    tblMachineProductionPeriodVo.setMachineUuid(tTblMachineDailyReportDetail.getTblMachineDailyReport()
                            .getTblProduction().getMachineUuid());
                    tblMachineProductionPeriodVo
                            .setComponentId(FileUtil.getStr(tTblMachineDailyReportDetail.getComponentId()));
                    tblMachineProductionPeriodVo
                            .setTotal(FileUtil.getIntegerValue(tTblMachineDailyReportDetail.getCompleteCount()));
                    tblMachineProductionPeriodVo.setMoldUuid(FileUtil.getStr(
                            tTblMachineDailyReportDetail.getTblMachineDailyReport().getTblProduction().getMoldUuid()));

                } else {// PK一致の場合、完成数の加算する

                    tblMachineProductionPeriodVo.setTotal(tblMachineProductionPeriodVo.getTotal()
                            + FileUtil.getIntegerValue(tTblMachineDailyReportDetail.getCompleteCount()));
                }

            }

            // 最終のレコードを追加する
            if (i == tblMachineDailyReportDetails.size() - 1) {
                tblMachineProductionPeriodVos.add(tblMachineProductionPeriodVo);
            }

        }

        return tblMachineProductionPeriodVos;

    }

    /**
     * 設備日別集計テーブルに登録する
     *
     * @param tblMachineProductionPeriodVos
     *
     *
     */
    @Transactional
    private void insertMachineProductionPerDay(List<TblMachineProductionPeriodVo> tblMachineProductionPeriodVos) {

        List<TblMachineProductionForDay> insertTblMachineProductionForDayList = new ArrayList();
        List<TblMachineProductionForDay> updTblMachineProductionForDayList = new ArrayList();

        for (TblMachineProductionPeriodVo tblMachineProductionPeriodVo : tblMachineProductionPeriodVos) {

            TblMachineProductionForDay tblMachineProductionForDay = new TblMachineProductionForDay();

            TblMachineProductionForDay chkMachineProductionForDay = tblMachineProductionPeriodService
                    .getProductionForDaySingleByPK(tblMachineProductionPeriodVo.getMachineUuid(),
                            tblMachineProductionPeriodVo.getComponentId(), tblMachineProductionPeriodVo.getMoldUuid(),
                            tblMachineProductionPeriodVo.getProductionDate());

            if (null == chkMachineProductionForDay) {

                setTblMachineProductionForDay(tblMachineProductionForDay, tblMachineProductionPeriodVo);

                tblMachineProductionForDay.setCreateUserUuid(BATCH_NAME);
                tblMachineProductionForDay.setUpdateUserUuid(BATCH_NAME);

                Date nowDate = new Date();
                tblMachineProductionForDay.setCreateDate(nowDate);
                tblMachineProductionForDay.setUpdateDate(nowDate);

                insertTblMachineProductionForDayList.add(tblMachineProductionForDay);

            } else {

                setTblMachineProductionForDay(tblMachineProductionForDay, tblMachineProductionPeriodVo);
                tblMachineProductionForDay.setUpdateUserUuid(BATCH_NAME);
                tblMachineProductionForDay.setCompletedCount(tblMachineProductionForDay.getCompletedCount()
                            + chkMachineProductionForDay.getCompletedCount());
                Date nowDate = new Date();
                tblMachineProductionForDay.setUpdateDate(nowDate);
                tblMachineProductionForDay.setCreateDate(chkMachineProductionForDay.getCreateDate());
                tblMachineProductionForDay.setCreateUserUuid(chkMachineProductionForDay.getCreateUserUuid());

                updTblMachineProductionForDayList.add(tblMachineProductionForDay);

            }
        }

        int insertCount = tblMachineProductionPeriodService.batchInsertByType(insertTblMachineProductionForDayList, 1);
        logger.log(LOG_LEVEL, "insertMachineProductionPerDay:設備日別集計テーブルの登録件数：" + insertCount);

        int updateCount = tblMachineProductionPeriodService.batchUpdateByType(updTblMachineProductionForDayList, 1);
        logger.log(LOG_LEVEL, "updMachineProductionPerDay:設備日別集計テーブルの更新件数：" + updateCount);
    }

    /**
     * 設備日別集計テーブル登録用のObjectを設定
     *
     * @param tblMachineProductionForDay
     * @param tblMachineProductionPeriodVo
     *
     *
     */
    private void setTblMachineProductionForDay(TblMachineProductionForDay tblMachineProductionForDay,
            TblMachineProductionPeriodVo tblMachineProductionPeriodVo) {

        TblMachineProductionForDayPK tblMachineProductionForDayPK = new TblMachineProductionForDayPK();

        tblMachineProductionForDayPK.setMachineUuid(tblMachineProductionPeriodVo.getMachineUuid());
        tblMachineProductionForDayPK.setComponentId(tblMachineProductionPeriodVo.getComponentId());
        tblMachineProductionForDayPK.setProductionDate(tblMachineProductionPeriodVo.getProductionDate());
        if (StringUtils.isEmpty(tblMachineProductionPeriodVo.getMoldUuid())) {
            tblMachineProductionForDayPK.setMoldUuid("NULL");
        } else {
            tblMachineProductionForDayPK.setMoldUuid(tblMachineProductionPeriodVo.getMoldUuid());
        }
        tblMachineProductionForDay.setTblMachineProductionForDayPK(tblMachineProductionForDayPK);
        tblMachineProductionForDay.setCompletedCount(tblMachineProductionPeriodVo.getTotal());

    }

    /**
     * 設備週別集計テーブルに登録する
     *
     * @param tblMachineProductionPeriodVos
     *
     *
     */
    @Transactional
    private void insertMachineProductionPerWeek(List<TblMachineProductionPeriodVo> tblMachineProductionPeriodVos) {

        List<TblMachineProductionForWeek> tblMachineProductionForWeekList = new ArrayList();

        int listSize = tblMachineProductionPeriodVos.size();
        
        CnfSystem cnf = cnfSystemService.findByKey("system", "business_start_day_of_week");
        
        int firstDay = Integer.parseInt(cnf.getConfigValue());

        if (listSize > 0) {

            Date minDate = tblMachineProductionPeriodVos.get(0).getProductionDate();

            Date startDay = DateFormat.getFirstDayOfWeek(firstDay, minDate);
            String startDayStr = DateFormat.dateToStr(startDay, DateFormat.DATE_FORMAT);
            
            Date sunDay = DateFormat.getAfterDays(startDay, 6);
            String sunDayStr = DateFormat.dateToStr(sunDay, DateFormat.DATE_FORMAT);

            String tempMachineUuid = "";
            String tempComponentId = "";
            String tempMoldUuid = "";

            TblMachineProductionForWeek tempTblMachineProductionForWeek = new TblMachineProductionForWeek();

            // 週別生産実績の完成数を集計する
            for (int i = 0; i < listSize; i++) {

                TblMachineProductionPeriodVo tblMachineProductionPeriodVo = tblMachineProductionPeriodVos.get(i);

                if (i == 0) {

                    setTblMachineProductionForWeek(tempTblMachineProductionForWeek, tblMachineProductionPeriodVo,
                            startDay, sunDay);

                    tempMachineUuid = tblMachineProductionPeriodVo.getMachineUuid();
                    tempComponentId = tblMachineProductionPeriodVo.getComponentId();
                    tempMoldUuid = FileUtil.getStr(tblMachineProductionPeriodVo.getMoldUuid());

                    tempTblMachineProductionForWeek.setCompletedCount(tblMachineProductionPeriodVo.getTotal());

                } else {

                    Date productionDate = tblMachineProductionPeriodVo.getProductionDate();
                    
                    Date tempStartDay = DateFormat.getFirstDayOfWeek(firstDay, productionDate);
                    String tempStartDayStr = DateFormat.dateToStr(tempStartDay, DateFormat.DATE_FORMAT);
                    
                    Date tempSunDay = DateFormat.getAfterDays(tempStartDay, 6);
                    String tempSunDayStr = DateFormat.dateToStr(tempSunDay, DateFormat.DATE_FORMAT);

                    if (!tempMachineUuid.equals(tblMachineProductionPeriodVo.getMachineUuid())
                            || !tempComponentId.equals(tblMachineProductionPeriodVo.getComponentId())
                            || !tempMoldUuid.equals(FileUtil.getStr(tblMachineProductionPeriodVo.getMoldUuid()))
                            || !startDayStr.equals(tempStartDayStr) || !sunDayStr.equals(tempSunDayStr)) {

                        tblMachineProductionForWeekList.add(tempTblMachineProductionForWeek);

                        tempTblMachineProductionForWeek = new TblMachineProductionForWeek();

                        startDay = tempStartDay;
                        sunDay = tempSunDay;
                        startDayStr = tempStartDayStr;
                        sunDayStr = tempSunDayStr;

                        setTblMachineProductionForWeek(tempTblMachineProductionForWeek, tblMachineProductionPeriodVo,
                                startDay, sunDay);

                        tempMachineUuid = tblMachineProductionPeriodVo.getMachineUuid();
                        tempComponentId = tblMachineProductionPeriodVo.getComponentId();
                        tempMoldUuid = FileUtil.getStr(tblMachineProductionPeriodVo.getMoldUuid());

                        tempTblMachineProductionForWeek.setCompletedCount(tblMachineProductionPeriodVo.getTotal());

                    } else {

                        tempTblMachineProductionForWeek
                                .setCompletedCount(tempTblMachineProductionForWeek.getCompletedCount()
                                        + tblMachineProductionPeriodVo.getTotal());

                    }

                }

                // 最終のレコードを追加する
                if (i == listSize - 1) {
                    tblMachineProductionForWeekList.add(tempTblMachineProductionForWeek);
                }

            }

            List<TblMachineProductionForWeek> insertTblMachineProductionForWeekList = new ArrayList();
            List<TblMachineProductionForWeek> updateTblMachineProductionForWeekList = new ArrayList();

            for (TblMachineProductionForWeek tblMachineProductionForWeek : tblMachineProductionForWeekList) {

                String machineUuid = tblMachineProductionForWeek.getTblMachineProductionForWeekPK().getMachineUuid();
                String componentId = tblMachineProductionForWeek.getTblMachineProductionForWeekPK().getComponentId();
                String moldUuid = tblMachineProductionForWeek.getTblMachineProductionForWeekPK().getMoldUuid();
                Date startDate = tblMachineProductionForWeek.getTblMachineProductionForWeekPK()
                        .getProductionDateStart();
                Date endDate = tblMachineProductionForWeek.getTblMachineProductionForWeekPK().getProductionDateEnd();

                TblMachineProductionForWeek chkMachineProductionForWeek = tblMachineProductionPeriodService
                        .getProductionForWeekSingleByPK(machineUuid, componentId, moldUuid, startDate, endDate);

                if (null == chkMachineProductionForWeek) {

                    tblMachineProductionForWeek.setCreateUserUuid(BATCH_NAME);
                    tblMachineProductionForWeek.setUpdateUserUuid(BATCH_NAME);

                    Date nowDate = new Date();
                    tblMachineProductionForWeek.setCreateDate(nowDate);
                    tblMachineProductionForWeek.setUpdateDate(nowDate);

                    insertTblMachineProductionForWeekList.add(tblMachineProductionForWeek);

                } else {

                    tblMachineProductionForWeek.setUpdateUserUuid(BATCH_NAME);
                    tblMachineProductionForWeek.setCompletedCount(tblMachineProductionForWeek.getCompletedCount()
                            + chkMachineProductionForWeek.getCompletedCount());
                    Date nowDate = new Date();
                    tblMachineProductionForWeek.setUpdateDate(nowDate);
                    tblMachineProductionForWeek.setCreateDate(chkMachineProductionForWeek.getCreateDate());
                    tblMachineProductionForWeek.setCreateUserUuid(chkMachineProductionForWeek.getCreateUserUuid());

                    updateTblMachineProductionForWeekList.add(tblMachineProductionForWeek);
                }
            }

            int insertCount = tblMachineProductionPeriodService.batchInsertByType(insertTblMachineProductionForWeekList,
                    2);
            logger.log(LOG_LEVEL, "insertMachineProductionPerWeek:設備週別集計テーブルの登録件数：" + insertCount);

            int updateCount = tblMachineProductionPeriodService.batchUpdateByType(updateTblMachineProductionForWeekList,
                    2);
            logger.log(LOG_LEVEL, "updMachineProductionPerWeek:設備週別集計テーブルの更新件数：" + updateCount);

        }

    }

    /**
     * 設備週別集計テーブル登録用のObjectを設定
     *
     * @param tblMachineProductionForWeek
     * @param tblMachineProductionPeriodVo
     * @param startDay
     * @param endDay
     *
     *
     */
    private void setTblMachineProductionForWeek(TblMachineProductionForWeek tblMachineProductionForWeek,
            TblMachineProductionPeriodVo tblMachineProductionPeriodVo, Date startDay, Date endDay) {

        TblMachineProductionForWeekPK tblMachineProductionForWeekPK = new TblMachineProductionForWeekPK();

        tblMachineProductionForWeekPK.setMachineUuid(tblMachineProductionPeriodVo.getMachineUuid());
        tblMachineProductionForWeekPK.setComponentId(tblMachineProductionPeriodVo.getComponentId());
        tblMachineProductionForWeekPK.setProductionDateStart(startDay);
        tblMachineProductionForWeekPK.setProductionDateEnd(endDay);
        if (StringUtils.isEmpty(tblMachineProductionPeriodVo.getMoldUuid())) {
            tblMachineProductionForWeekPK.setMoldUuid("NULL");
        } else {
            tblMachineProductionForWeekPK.setMoldUuid(tblMachineProductionPeriodVo.getMoldUuid());
        }
        tblMachineProductionForWeek.setTblMachineProductionForWeekPK(tblMachineProductionForWeekPK);
    }

    /**
     * 設備月別集計テーブルに登録する
     *
     * @param tblMachineProductionPeriodVos
     *
     *
     */
    @Transactional
    private void insertMachineProductionPerMonth(List<TblMachineProductionPeriodVo> tblMachineProductionPeriodVos) {

        List<TblMachineProductionForMonth> tblMachineProductionForMonthList = new ArrayList();

        int listSize = tblMachineProductionPeriodVos.size();

        if (listSize > 0) {

            Date minDate = tblMachineProductionPeriodVos.get(0).getProductionDate();

            String firstDayStr = DateFormat.getFirstDay(minDate);
            String lastDayStr = DateFormat.getLastDay(minDate);

            Date firstDay = DateFormat.strToDate(firstDayStr);

            String tempMachineUuid = "";
            String tempComponentId = "";
            String tempMoldUuid = "";

            TblMachineProductionForMonth tempTblMachineProductionForMonth = new TblMachineProductionForMonth();

            // 月別生産実績の完成数を集計する
            for (int i = 0; i < listSize; i++) {

                TblMachineProductionPeriodVo tblMachineProductionPeriodVo = tblMachineProductionPeriodVos.get(i);

                if (i == 0) {

                    setTblMachineProductionForMonth(tempTblMachineProductionForMonth, tblMachineProductionPeriodVo,
                            firstDay);

                    tempMachineUuid = tblMachineProductionPeriodVo.getMachineUuid();
                    tempComponentId = tblMachineProductionPeriodVo.getComponentId();
                    tempMoldUuid = FileUtil.getStr(tblMachineProductionPeriodVo.getMoldUuid());

                    tempTblMachineProductionForMonth.setCompletedCount(tblMachineProductionPeriodVo.getTotal());

                } else {

                    Date productionDate = tblMachineProductionPeriodVo.getProductionDate();

                    String tempFirstDayStr = DateFormat.getFirstDay(productionDate);
                    Date tempFirstDay = DateFormat.strToDate(tempFirstDayStr);

                    String tempLastDayStr = DateFormat.getLastDay(productionDate);

                    if (!tempMachineUuid.equals(tblMachineProductionPeriodVo.getMachineUuid())
                            || !tempComponentId.equals(tblMachineProductionPeriodVo.getComponentId())
                            || !tempMoldUuid.equals(FileUtil.getStr(tblMachineProductionPeriodVo.getMoldUuid()))
                            || !firstDayStr.equals(tempFirstDayStr) || !lastDayStr.equals(tempLastDayStr)) {

                        tblMachineProductionForMonthList.add(tempTblMachineProductionForMonth);

                        tempTblMachineProductionForMonth = new TblMachineProductionForMonth();

                        firstDay = tempFirstDay;
                        firstDayStr = tempFirstDayStr;
                        lastDayStr = tempLastDayStr;

                        setTblMachineProductionForMonth(tempTblMachineProductionForMonth, tblMachineProductionPeriodVo,
                                firstDay);

                        tempMachineUuid = tblMachineProductionPeriodVo.getMachineUuid();
                        tempComponentId = tblMachineProductionPeriodVo.getComponentId();
                        tempMoldUuid = FileUtil.getStr(tblMachineProductionPeriodVo.getMoldUuid());

                        tempTblMachineProductionForMonth.setCompletedCount(tblMachineProductionPeriodVo.getTotal());

                    } else {

                        tempTblMachineProductionForMonth
                                .setCompletedCount(tempTblMachineProductionForMonth.getCompletedCount()
                                        + tblMachineProductionPeriodVo.getTotal());

                    }

                }

                // 最終のレコードを追加する
                if (i == listSize - 1) {
                    tblMachineProductionForMonthList.add(tempTblMachineProductionForMonth);
                }

            }

            List<TblMachineProductionForMonth> insertTblMachineProductionForMonthList = new ArrayList();
            List<TblMachineProductionForMonth> updateTblMachineProductionForMonthList = new ArrayList();

            for (TblMachineProductionForMonth tblMachineProductionForMonth : tblMachineProductionForMonthList) {

                String machineUuid = tblMachineProductionForMonth.getTblMachineProductionForMonthPK().getMachineUuid();
                String componentId = tblMachineProductionForMonth.getTblMachineProductionForMonthPK().getComponentId();
                String productionMonth = tblMachineProductionForMonth.getTblMachineProductionForMonthPK()
                        .getProductionMonth();
                String moldUuid = tblMachineProductionForMonth.getTblMachineProductionForMonthPK().getMoldUuid();

                TblMachineProductionForMonth chkMachineProductionForMonth = tblMachineProductionPeriodService
                        .getProductionForMonthSingleByPK(machineUuid, componentId, moldUuid, productionMonth);

                if (null == chkMachineProductionForMonth) {

                    tblMachineProductionForMonth.setCreateUserUuid(BATCH_NAME);
                    tblMachineProductionForMonth.setUpdateUserUuid(BATCH_NAME);

                    Date nowDate = new Date();
                    tblMachineProductionForMonth.setCreateDate(nowDate);
                    tblMachineProductionForMonth.setUpdateDate(nowDate);

                    insertTblMachineProductionForMonthList.add(tblMachineProductionForMonth);

                } else {

                    tblMachineProductionForMonth.setUpdateUserUuid(BATCH_NAME);
                    tblMachineProductionForMonth.setCompletedCount(tblMachineProductionForMonth.getCompletedCount()
                            + chkMachineProductionForMonth.getCompletedCount());
                    Date nowDate = new Date();
                    tblMachineProductionForMonth.setUpdateDate(nowDate);
                    tblMachineProductionForMonth.setCreateDate(chkMachineProductionForMonth.getCreateDate());
                    tblMachineProductionForMonth.setCreateUserUuid(chkMachineProductionForMonth.getCreateUserUuid());

                    updateTblMachineProductionForMonthList.add(tblMachineProductionForMonth);
                }
            }

            int insertCount = tblMachineProductionPeriodService
                    .batchInsertByType(insertTblMachineProductionForMonthList, 3);
            logger.log(LOG_LEVEL, "insertMachineProductionPerWeek:設備月別集計テーブルの登録件数：" + insertCount);

            int updateCount = tblMachineProductionPeriodService
                    .batchUpdateByType(updateTblMachineProductionForMonthList, 3);
            logger.log(LOG_LEVEL, "updMachineProductionPerWeek:設備月別集計テーブルの更新件数：" + updateCount);

        }

    }

    /**
     * 設備月別集計テーブル登録用のObjectを設定
     *
     * @param tblMachineProductionForMonth
     * @param tblMachineProductionPeriodVo
     * @param productionMonth
     *
     *
     */
    private void setTblMachineProductionForMonth(TblMachineProductionForMonth tblMachineProductionForMonth,
            TblMachineProductionPeriodVo tblMachineProductionPeriodVo, Date productionMonth) {

        TblMachineProductionForMonthPK tblMachineProductionForMonthPK = new TblMachineProductionForMonthPK();

        tblMachineProductionForMonthPK.setMachineUuid(tblMachineProductionPeriodVo.getMachineUuid());
        tblMachineProductionForMonthPK.setComponentId(tblMachineProductionPeriodVo.getComponentId());
        tblMachineProductionForMonthPK
                .setProductionMonth(DateFormat.dateToStr(productionMonth, DateFormat.DATE_FORMAT_MONTH));
        if (StringUtils.isEmpty(tblMachineProductionPeriodVo.getMoldUuid())) {
            tblMachineProductionForMonthPK.setMoldUuid("NULL");
        } else {
            tblMachineProductionForMonthPK.setMoldUuid(tblMachineProductionPeriodVo.getMoldUuid());
        }
        tblMachineProductionForMonth.setTblMachineProductionForMonthPK(tblMachineProductionForMonthPK);

    }

    /**
     * `集計済みテーブルに登録
     *
     * @param tblMachineDailyReportDetails
     *
     */
    @Transactional
    private void insertAggregated(List<TblMachineDailyReportDetail> tblMachineDailyReportDetails) {

        List<TblAggregated> TblAggregatedList = new ArrayList();

        for (TblMachineDailyReportDetail tblMachineDailyReportDetail : tblMachineDailyReportDetails) {

            TblAggregated tblAggregated = new TblAggregated();

            tblAggregated.setMacReportDetailId(tblMachineDailyReportDetail.getId());
            tblAggregated.setProductionDate(tblMachineDailyReportDetail.getTblMachineDailyReport()
                    .getTblMachineDailyReportPK().getProductionDate());
            tblAggregated.setUpdDeleteFlg(0);

            tblAggregated.setId(IDGenerator.generate());
            tblAggregated.setCompletedCount(FileUtil.getIntegerValue(tblMachineDailyReportDetail.getCompleteCount()));

            TblAggregatedList.add(tblAggregated);

        }

        int count = tblAggregatedService.batchInsert(TblAggregatedList);
        logger.log(LOG_LEVEL, "insertAggregated:集計済みテーブルの登録件数：" + count);

    }

}
