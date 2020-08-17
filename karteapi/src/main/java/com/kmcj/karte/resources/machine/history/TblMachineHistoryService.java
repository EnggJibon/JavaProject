package com.kmcj.karte.resources.machine.history;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.MstMachineService;
import com.kmcj.karte.resources.machine.operating.rate.TblMachineOperatingRateRecalc;
import com.kmcj.karte.resources.production.detail.TblProductionDetail;
import com.kmcj.karte.resources.production.detail.TblProductionDetailList;
import com.kmcj.karte.resources.work.TblWork;
import com.kmcj.karte.resources.work.TblWorkList;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author penggd
 */
@Dependent
public class TblMachineHistoryService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private CnfSystemService cnfSystemService;
    
    @Inject
    private MstMachineService mstMachineService;
    
    @Inject
    private MstChoiceService mstChoiceService;

    
    @Transactional
    public void recalcMachineOperatingRate(String machineUuid) {
        Query query = entityManager.createNamedQuery("TblMachineOperatingRateRecalc.findByMachineUuid");
        query.setParameter("machineUuid", machineUuid);
        List list = query.getResultList();
        if (list == null || list.size() == 0) {
            TblMachineOperatingRateRecalc recalc = new TblMachineOperatingRateRecalc();
            recalc.setMachineUuid(machineUuid);
            entityManager.persist(recalc);
        }
    }
    
    @Transactional
    public void deleteMachineHistory(String machineUuid,  String baseDate) {
        String jpql = "DELETE FROM TblMachineHistory t WHERE t.tblMachineHistoryPK.machineUuid = :machineUuid AND t.tblMachineHistoryPK.startDate >= :baseDate";
        Date pBaseDate = DateFormat.strToDateMill(baseDate);
        Query query = entityManager.createQuery(jpql);
        query.setParameter("machineUuid", machineUuid);
        query.setParameter("baseDate", pBaseDate);
        query.executeUpdate();

        //削除後、最終レコードのend_dateをnullにする
        String getLast = "SELECT MAX(t.tblMachineHistoryPK.startDate) FROM TblMachineHistory t  WHERE  t.tblMachineHistoryPK.machineUuid = :machineUuid";
        query = entityManager.createQuery(getLast);
        query.setParameter("machineUuid", machineUuid);
        List<Date> list = query.getResultList();
        if (list.size() > 0) {
            Date maxStartDate = list.get(0);
            String updateLast = "UPDATE TblMachineHistory t SET t.endDate = null WHERE t.tblMachineHistoryPK.machineUuid = :machineUuid " + 
                    " AND t.tblMachineHistoryPK.startDate = :startDate";
            query = entityManager.createQuery(updateLast);
            query.setParameter("machineUuid", machineUuid);
            query.setParameter("startDate", maxStartDate);
            query.executeUpdate();
        }
    }
    
    /**
     * getTblMachineHistory設備稼働履歴情報取得
     *
     * @param machineUuid
     * @param startEventNo
     * @param createDate
     * @return
     */
    public TblMachineHistoryVo getTblMachineHistory(String machineUuid, String startEventNo, String createDate) {

        // 設備稼働履歴情報取得
        String sql = "SELECT t FROM TblMachineHistory t WHERE t.tblMachineHistoryPK.machineUuid = :machineUuid AND t.endDate is null";

        Query query = entityManager.createQuery(sql);

        query.setParameter("machineUuid", machineUuid);

        List<TblMachineHistory> result = query.getResultList();

        TblMachineHistoryVo tblMachineHistoryVo = new TblMachineHistoryVo();

        // 設備履歴情報取得する場合
        if (result.size() > 0) {

            tblMachineHistoryVo.setMachineUuid(result.get(0).getTblMachineHistoryPK().getMachineUuid());

            tblMachineHistoryVo.setFirstEventNo(String.valueOf((result.get(0).getTblMachineHistoryPK().getFirstEventNo())));

            tblMachineHistoryVo.setStartDate(DateFormat.dateToStrMill(result.get(0).getTblMachineHistoryPK().getStartDate()));

            return tblMachineHistoryVo;

        } else {// 設備履歴情報取得できない場合

            tblMachineHistoryVo.setFirstEventNo(startEventNo);

            tblMachineHistoryVo.setStartDate(createDate);

            return tblMachineHistoryVo;
        }

    }

    /**
     * T1101_設備稼働状況照会 一覧データをCSVファイルに出力する
     *
     * @param machineHistoryList
     * @param loginUser
     * @return CSVファイルID
     */
    public FileReponse outoutMachineHistoriesToCsv(TblMachineHistoryList machineHistoryList, LoginUser loginUser) {

        FileReponse reponse = new FileReponse();
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String langId = loginUser.getLangId();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList("machine_id", "machine_name", "mac_key", "machine_status", "production_time_minutes",
                "machine_working_rate", "machine_stop_count", "machine_work_pfm", "work_actual_time_minutes", "shot_count",
                "machine_last_event_date", "machine_working_status_reference");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);

        /*Head*/
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();
        headList.add(headerMap.get("machine_id"));  // 設備ID
        headList.add(headerMap.get("machine_name")); // 設備名称
        headList.add(headerMap.get("mac_key")); // 連携コード
        headList.add(headerMap.get("machine_status")); // 状態
        headList.add(headerMap.get("production_time_minutes")); // 生産時間
        headList.add(headerMap.get("machine_working_rate")); // 稼働率
        headList.add(headerMap.get("machine_stop_count")); // 停止回数
        headList.add(headerMap.get("machine_work_pfm"));  // 設備生産効率
        headList.add(headerMap.get("work_actual_time_minutes")); // 実稼働時間
        headList.add(headerMap.get("shot_count")); // ショット数
        headList.add(headerMap.get("machine_last_event_date")); // 最終同期時刻
        gLineList.add(headList); 

        ArrayList lineList;
        // 画面検索がある場合、画面から値をCSVに出力する。そうでない場合、検索条件により、DB値からCSVに出力する
        if (machineHistoryList.getTblMachineHistoryVos() == null || machineHistoryList.getTblMachineHistoryVos().isEmpty()) {
            machineHistoryList = getTblMachineHistories(machineHistoryList.getStartDate(),
                machineHistoryList.getEndDate(), machineHistoryList.getDepartment(),
                machineHistoryList.getMachineId(), loginUser);
        }
        // 設備稼働履歴情報取得
        //TblMachineHistoryList machineHistoryList = getTblMachineHistories(startDate, endDate, department, machineId, loginUser);
        if (machineHistoryList.getTblMachineHistoryVos() != null && machineHistoryList.getTblMachineHistoryVos().size() > 0) {
            for (int i = 0; i < machineHistoryList.getTblMachineHistoryVos().size(); i++) {
                lineList = new ArrayList();
                TblMachineHistoryVo machineHistoryVo = machineHistoryList.getTblMachineHistoryVos().get(i);
                // 設備ID
                lineList.add(machineHistoryVo.getMachineId());
                // 設備名称
                lineList.add(machineHistoryVo.getMachineName());
                // 連携コード
                lineList.add(machineHistoryVo.getMacKey());
                // 状態
                lineList.add(machineHistoryVo.getPower());
                // 生産時間
                lineList.add(machineHistoryVo.getWorkTime());
                // 稼働率
                lineList.add(machineHistoryVo.getCalcMachineReport());
                // 停止回数{
                lineList.add(machineHistoryVo.getStopCnt());
                // 設備生産効率
                lineList.add(machineHistoryVo.getWorkPfm());
                // 実稼働時間
                lineList.add(machineHistoryVo.getOpeTime());
                // ショット数
                lineList.add(machineHistoryVo.getShotCnt());
                // 最終同期時刻
                lineList.add(machineHistoryVo.getLastEventDate());
                gLineList.add(lineList);
            }
        }

        // csv 出力
        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_MACHINE_HISTORY);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_BL_MACHINE_HISTORY);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(headerMap.get("machine_working_status_reference")));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        reponse.setFileUuid(uuid);
        return reponse;
    }

    /**
     * T1101_設備稼働状況照会 一覧データ(CSV AND LIST)を取得
     *
     * @param startDate
     * @param endDate
     * @param machineId
     * @param loginUser
     * @return CSV LIST
     */
    public TblMachineHistoryList getTblMachineHistories(String startDate, String endDate, String department, String machineId, LoginUser loginUser) {

        FileUtil fileUtil = new FileUtil();
        String standardWorkTime;
        Date itemStartDate;
        CnfSystem cnf = cnfSystemService.findByKey("system", "business_start_time");
        // 作業日は開始日の業務開始時刻を開始日時
        if (startDate != null && !"".equals(startDate)) {
            standardWorkTime = startDate + " " + cnf.getConfigValue();
            itemStartDate = FileUtil.dateTime(standardWorkTime);
            startDate = fileUtil.getDateTimeFormatForStr(itemStartDate);
        }
        // 選択日の翌日の業務開始時刻
        if (endDate != null && !"".equals(endDate)) {
            standardWorkTime = endDate + " " + cnf.getConfigValue();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(FileUtil.dateTime(standardWorkTime));
            calendar.add(Calendar.DATE, 1);
            endDate = fileUtil.getDateTimeFormatForStr(calendar.getTime());
        }

        StringBuffer sql;
        sql = new StringBuffer("SELECT");
        sql.append(" m.MACHINE_ID "); // 設備ID 
        sql.append(",m.MACHINE_NAME "); // 設備名称
        sql.append(",m.MAC_KEY "); //連携コード
        //状態
        sql.append(",(SELECT CASE mh.status WHEN 'ON' THEN '1' WHEN 'OFF' THEN '0'  ELSE NULL  END FROM tbl_machine_history mh WHERE mh.machine_uuid = m.uuid ");
        sql.append(" ORDER BY mh.start_date DESC, mh.first_event_no DESC LIMIT 1) AS SPOWER ");
        sql.append(" ,IFNULL(counter.STOPCNT,'0') AS STOPCNT ");
        sql.append(" ,IFNULL(counter.SHOTCNT,'0') AS SHOTCNT ");
        /*
        //停止回数
        sql.append(",(SELECT COUNT(1) FROM tbl_sigma_log slog WHERE m.uuid = slog.machine_uuid AND slog.alert = 'ON' ");
        if (null != startDate && !"".equals(startDate)) {
            sql.append(" AND slog.create_date >= '").append(startDate).append("' ");
        }
        if (null != endDate && !"".equals(endDate)) {
            sql.append(" AND slog.create_date <= '").append(endDate).append("' ");
        }
        sql.append(" ) AS STOPCNT ");
        // ショット数
        sql.append(" ,(SELECT COUNT(1) FROM tbl_sigma_log sig1 WHERE m.uuid = sig1.machine_uuid ");
        sql.append("  AND  sig1.cycle_start = 'ON' ");

        if (null != startDate && !"".equals(startDate)) {
            sql.append(" AND sig1.create_date >= '").append(startDate).append("' ");
        }
        if (null != endDate && !"".equals(endDate)) {
            sql.append(" AND sig1.create_date <= '").append(endDate).append("' ");
        }
        sql.append("  ) AS SHOTCNT ");
        */

        // 最終同期時刻
        sql.append(" ,(SELECT mh.last_event_date FROM tbl_machine_history mh WHERE mh.machine_uuid = m.uuid ORDER BY mh.last_event_date DESC LIMIT 1) SYNCDATE ");
        // 基準サイクル時間
        sql.append(",m.BASE_CYCLE_TIME ");
        // 不良数
        sql.append(",(SELECT SUM(b.DEFECT_COUNT) FROM tbl_production a LEFT JOIN tbl_production_detail b ON a.ID = b.PRODUCTION_ID WHERE a.MACHINE_UUID = m.uuid ");
        if (null != startDate && !"".equals(startDate)) {
            //sql.append(" AND a.START_DATETIME >= '").append(startDate).append("' ");
            sql.append(" AND a.START_DATETIME >= ? ");
        }
        if (null != endDate && !"".equals(endDate)) {
            //sql.append(" AND a.END_DATETIME <= '").append(endDate).append("' ");
            sql.append(" AND a.END_DATETIME <= ? ");
        }
        sql.append(" ) AS DEFECT_COUNT ");
        // 所属
        sql.append(" ,m.department ");
        
        sql.append("  FROM  mst_machine m ");
        sql.append("  LEFT JOIN (SELECT slog.machine_uuid, ");
        sql.append("  SUM(CASE slog.alert WHEN 'ON' THEN '1' ELSE '0' END) AS STOPCNT, ");
        sql.append("  SUM(CASE slog.cycle_start WHEN 'ON' THEN '1' ELSE '0' END) AS SHOTCNT ");
        sql.append("  FROM tbl_sigma_log slog WHERE 1=1 ");
        if (null != startDate && !"".equals(startDate)) {
            //sql.append(" AND slog.create_date >= '").append(startDate).append("' ");
            sql.append(" AND slog.create_date >= ? ");
        }
        if (null != endDate && !"".equals(endDate)) {
            //sql.append(" AND slog.create_date <= '").append(endDate).append("' ");
            sql.append(" AND slog.create_date <= ? ");
        }
        sql.append("  GROUP BY slog.machine_uuid ORDER BY NULL) counter ON m.uuid = counter.machine_uuid ");
        sql.append(" WHERE m.SIGMA_ID IS NOT NULL ");
        if (null != department && !"".equals(department)) {
            //sql.append("  AND m.department = ").append(department).append(" ");
            sql.append("  AND m.department = ? ");
        }
        
        if (null != machineId && !"".equals(machineId)) {
            //sql.append("  AND m.machine_id = '").append(machineId.trim()).append("' ");
            sql.append("  AND m.machine_id = ? ");
        }

        sql.append(" ORDER BY m.MACHINE_ID  ");

        Query query = entityManager.createNativeQuery(sql.toString());
        int index = 1;
        if (null != startDate && !"".equals(startDate)) {
            query.setParameter(index, startDate);
            index++;
        }
        if (null != endDate && !"".equals(endDate)) {
            query.setParameter(index, endDate);
            index++;
        }
        if (null != startDate && !"".equals(startDate)) {
            query.setParameter(index, startDate);
            index++;
        }
        if (null != endDate && !"".equals(endDate)) {
            query.setParameter(index, endDate);
            index++;
        }
        if (null != department && !"".equals(department)) {
            query.setParameter(index, department);
            index++;
        }
        if (null != machineId && !"".equals(machineId)) {
            query.setParameter(index, machineId);
            index++;
        }

        TblMachineHistoryList machineHistoryList = new TblMachineHistoryList();

        // 設備稼働履歴情報取得
        List<TblMachineHistoryVo> machineHistoryVos = new ArrayList<>();

        TblMachineHistoryVo machineHistoryVo;
        List list = query.getResultList();

        // 設備毎でソートして開始日時と終了日時を取得する
        sql.delete(0, sql.length());
        sql.append(" SELECT m.machine_id, ");
        // 生産時間. 開始日
        sql.append(" mh.start_date startTime, ");
        // 生産時間. 終了日
        sql.append(" IFNULL(mh.end_date, mh.last_event_date) endTime ");
        sql.append(" FROM tbl_machine_history mh, mst_machine m ");
        sql.append(" WHERE m.uuid = mh.machine_uuid AND mh.status = 'ON' ");
        if (null != endDate && !"".equals(endDate)) {
            //sql.append(" AND mh.start_date < '").append(endDate).append("' ");
            sql.append(" AND mh.start_date < ? ");
        }
        if (null != startDate && !"".equals(startDate)) {
            //sql.append(" AND mh.last_event_date > '").append(startDate).append("' ");
            sql.append(" AND IFNULL(mh.end_date, mh.last_event_date) > ? ");
        }
        sql.append(" AND m.SIGMA_ID IS NOT NULL ");
        if (null != department && !"".equals(department)) {
            //sql.append("  AND m.department = ").append(department).append(" ");
            sql.append("  AND m.department = ? ");
        }
        
        if (null != machineId && !"".equals(machineId)) {
            //sql.append("  AND m.machine_id = '").append(machineId.trim()).append("' ");
            sql.append("  AND m.machine_id = ? ");
        }

        sql.append(" ORDER BY m.MACHINE_ID  ");

        query = entityManager.createNativeQuery(sql.toString());
        int index2 = 1;
        if (null != endDate && !"".equals(endDate)) {
            query.setParameter(index2, endDate);
            index2++;
        }
        if (null != startDate && !"".equals(startDate)) {
            query.setParameter(index2, startDate);
            index2++;
        }
        if (null != department && !"".equals(department)) {
            query.setParameter(index2, department);
            index2++;
        }
        if (null != machineId && !"".equals(machineId)) {
            query.setParameter(index2, machineId);
            index2++;
        }

        List listDateTime = query.getResultList();

        // マップを作成する(設備ID：キー、該当設備の開始、終了日時データ：value)
        Map<String, List<String[]>> dateTimeMap = getDateTimeMap(listDateTime);
        java.util.Date startDateTime = null;
        java.util.Date endDateTime = null;

        java.util.Date formatWhereStartDateTime = null;
        java.util.Date formatWhereEndDateTime = null;
        FileUtil fu = new FileUtil();

        if (list != null && list.size() > 0) {
            String running = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "running");
            String stopped = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "stopped");

            //for (int )
            for (int i = 0; i < list.size(); i++) {
                machineHistoryVo = new TblMachineHistoryVo();
                Object[] objes = (Object[]) list.get(i);
                // 設備ID
                machineHistoryVo.setMachineId(String.valueOf(objes[0] == null ? "" : objes[0]));
                // 設備名称
                machineHistoryVo.setMachineName(String.valueOf(objes[1] == null ? "" : objes[1]));
                // 連携コード
                machineHistoryVo.setMacKey(String.valueOf(objes[2] == null ? "" : objes[2]));
                // 状態
                if (objes[3] != null && "1".equals(String.valueOf(objes[3]))) {

                    machineHistoryVo.setPower(running);
                } else {
                    machineHistoryVo.setPower(stopped);
                }
                long dateTimeDiffSec = 0L;
                if (dateTimeMap.containsKey(machineHistoryVo.getMachineId())) {
                    for (String[] dateTime : dateTimeMap.get(machineHistoryVo.getMachineId())) {
                        // 生産時間
                        String strStartDate = dateTime[0];
                        if (!"".equals(strStartDate)) {
                            strStartDate = strStartDate.replace("-", "/");
                            startDateTime = DateFormat.strToDateMill(strStartDate);
                        }
                        // 条件
                        if (null != startDate && !"".equals(startDate)) {
                            formatWhereStartDateTime = fu.strDateTimeFormatToDate(startDate);
                        }

                        if (formatWhereStartDateTime != null && startDateTime != null && formatWhereStartDateTime.compareTo(startDateTime) > 0) {
                            startDateTime = formatWhereStartDateTime;
                        }

                        // end
                        String strEndDate = dateTime[1];
                        if (!"".equals(strEndDate)) {
                            strEndDate = strEndDate.replace("-", "/");
                            endDateTime = DateFormat.strToDateMill(strEndDate);
                        }
                        // 条件
                        if (null != endDate && !"".equals(endDate)) {
                            formatWhereEndDateTime = fu.strDateTimeFormatToDate(endDate);
                        }

                        if (formatWhereEndDateTime != null && endDateTime != null && formatWhereEndDateTime.compareTo(endDateTime) < 0) {
                            endDateTime = formatWhereEndDateTime;
                        }

                        if (endDateTime != null && startDateTime != null) {
                            dateTimeDiffSec += DateFormat.dateDiff(startDateTime, endDateTime, DateFormat.DIFF_SECONDE);
                        } else {
                            dateTimeDiffSec += 0L;
                        }
                    }
                }
                long temp = 0L;
                temp = (dateTimeDiffSec / 60) % 60; 
                machineHistoryVo.setWorkTime(FileUtil.formatSeconds(dateTimeDiffSec));
                machineHistoryVo.setWorkTimeSec(dateTimeDiffSec / 3600 * 60 + temp);
                // 稼働率
                machineHistoryVo.setCalcMachineReport("0%"); //画面計算
                // 停止回数
                machineHistoryVo.setStopCnt(String.valueOf(objes[4] == null ? "" : objes[4]));
                // 設備生産効率
                machineHistoryVo.setWorkPfm(""); //画面計算
                // 実稼働時間
                //machineHistoryVo.setOpeTime("-");  //画面計算
                // ショット数
                machineHistoryVo.setShotCnt(String.valueOf(objes[5] == null ? "" : objes[5]));
                // 最終同期時刻
                // 実稼働時間
                if (objes[6] == null) {
                    machineHistoryVo.setLastEventDate("");
                    machineHistoryVo.setOpeTime("");  //画面計算
                } else {
                    machineHistoryVo.setLastEventDate(String.valueOf(fu.getDateTimeFormatForStr(objes[6])));
                    machineHistoryVo.setOpeTime("-");  //画面計算
                }

                // 基準サイクル時間
                if (objes[7] != null && !"".equals(String.valueOf(objes[7]))) {
                    machineHistoryVo.setBaseCycleTime(new BigDecimal(String.valueOf(objes[7])));
                } else {
                    machineHistoryVo.setBaseCycleTime(BigDecimal.ZERO);
                }

                // 不良数
                if (objes[8] != null && !"".equals(String.valueOf(objes[8]))) {
                    machineHistoryVo.setFailureCnt(Integer.parseInt(String.valueOf(objes[8])));
                } else {
                    machineHistoryVo.setFailureCnt(0);
                }

                // 所属
                if (objes[9] != null) {
                    machineHistoryVo.setDepartment(Integer.parseInt(objes[9].toString()));
                }
                machineHistoryVos.add(machineHistoryVo);
            }
        }
        machineHistoryList.setTblMachineHistoryVos(machineHistoryVos);
        return machineHistoryList;
    }

    /**
     * T1101_設備稼働状況照会詳細データを取得
     *
     * @param workDate
     * @param machineId
     * @return 設備稼働状況照会
     */
    public TblMachineHistoryList getTblMachineHistory(String workDate, String machineId) {

        TblMachineHistoryList machineHistoryList = new TblMachineHistoryList();

        CnfSystem cnf = cnfSystemService.findByKey("system", "business_start_time");

        FileUtil fileUtil = new FileUtil();
        // 選択日の業務開始時刻
        String standardWorkTime = workDate + " " + cnf.getConfigValue();
        Date itemStartDate = FileUtil.dateTime(standardWorkTime);
        machineHistoryList.setStartDate(fileUtil.getDateTimeFormatForStr(itemStartDate));

        // 選択日の翌日の業務開始時刻
        Date itemEndDate = itemStartDate;
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(itemEndDate);
        calendar.add(Calendar.DATE, 1);

        machineHistoryList.setEndDate(fileUtil.getDateTimeFormatForStr(calendar.getTime()));

        StringBuffer sql;
        sql = new StringBuffer();
        sql.append(" SELECT t FROM TblMachineHistory t JOIN FETCH t.mstMachine WHERE 1=1  ");
        sql.append(" AND t.mstMachine.machineId = :machineId ");
        sql.append(" AND t.tblMachineHistoryPK.startDate < :endDate ");
        //sql.append(" AND t.lastEventDate > :startDate ");
        sql.append(" AND ((t.endDate IS NULL AND t.lastEventDate > :startDate) ");
        sql.append(" OR (t.endDate IS NOT NULL AND t.endDate > :startDate)) ");
        sql.append(" ORDER BY t.tblMachineHistoryPK.startDate ");

        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("machineId", machineId);
        query.setParameter("startDate", itemStartDate);
        query.setParameter("endDate", calendar.getTime());

        List list = query.getResultList();

        if (null != list && list.size() > 0) {
            ;
        } else {
            Date now = new Date();
            if (itemStartDate.compareTo(now) == 1) {
                ;//開始時刻がシステム時刻より大きければ処理なし
            } else {
                //これを現在時刻まで表示されるようにしてください。
                //最終同期時刻時点でのON/OFFの状態を現在時刻まで伸ばしてください。
                sql = new StringBuffer();
                sql.append(" SELECT t FROM TblMachineHistory t JOIN FETCH t.mstMachine WHERE 1=1  ");
                sql.append(" AND t.mstMachine.machineId = :machineId ");
                sql.append(" AND t.tblMachineHistoryPK.startDate < :startDate ");

                sql.append(" AND t.endDate IS NULL ");
                sql.append(" ORDER BY t.tblMachineHistoryPK.startDate DESC ");
                query = entityManager.createQuery(sql.toString());

                query.setParameter("machineId", machineId);
                query.setParameter("startDate", itemStartDate);
                query.setMaxResults(1);
                list = query.getResultList();
            }
        }
        // 設備稼働履歴情報取得
        List<TblMachineHistoryVo> machineHistoryVos = new ArrayList<>();
        if (null != list && list.size() > 0) {
            FileUtil fu = new FileUtil();
            TblMachineHistoryVo machineHistoryVo;
            for (int i = 0; i < list.size(); i++) {
                machineHistoryVo = new TblMachineHistoryVo();
                TblMachineHistory machineHistory = (TblMachineHistory) list.get(i);
                TblMachineHistoryPK machineHistoryPK = machineHistory.getTblMachineHistoryPK();
                // 開始日   
                machineHistoryVo.setStartDate(fu.getDateTimeFormatForStr(machineHistoryPK.getStartDate()));
                if (machineHistory.getEndDate() != null) {
                    machineHistoryVo.setEndDate(fu.getDateTimeFormatForStr(machineHistory.getEndDate()));
                } else {
                    Date now = new Date();
                    if (now.compareTo(itemStartDate) == -1) {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        String standardWorkTime1 = workDate + " " + sdf.format(now);
                        Date itemStartDate1 = FileUtil.dateTime(standardWorkTime1);
                        machineHistoryVo.setEndDate(fu.getDateTimeFormatForStr(itemStartDate1));
                    } else {
                        machineHistoryVo.setEndDate(fu.getDateTimeFormatForStr(now));
                    }
                }

                machineHistoryVo.setLastEventDate(fu.getDateTimeFormatForStr(machineHistory.getLastEventDate()));
                if ("ON".equals(machineHistory.getStatus())) {
                    machineHistoryVo.setStatus("0");
                } else {
                    machineHistoryVo.setStatus("1");
                }
                machineHistoryVos.add(machineHistoryVo);
            }
        }

        machineHistoryList.setTblMachineHistoryVos(machineHistoryVos);
        return machineHistoryList;
    }

    /**
     * deleteMachineHistoryByPK 削除設備稼働履歴情報
     *
     * @param machineUuid
     * @param firstEventNo
     * @param startDate
     * @return
     */
    @Transactional
    public int deleteMachineHistoryByPK(String machineUuid, String firstEventNo, String startDate) {

        Query query = entityManager.createNamedQuery("TblMachineHistory.deleteByPK");

        query.setParameter("machineUuid", machineUuid);
        query.setParameter("firstEventNo", Long.valueOf(firstEventNo));
        query.setParameter("startDate", DateFormat.strToDateMill(startDate));

        int count = query.executeUpdate();

        return count;

    }

    /**
     * batchInsert設備稼動履歴データ登録
     *
     * @param list
     * @return
     */
    @Transactional
    public int batchInsert(List<TblMachineHistory> list) {

        int insertCount = 0;

        int count = 0;

        for (int i = 1; i <= list.size(); i++) {

            entityManager.persist(list.get(i - 1));
            // 5000件毎にDBへ登録する
            if (i % 5000 == 0) {
                entityManager.flush();
                entityManager.clear();

                insertCount += 5000;
            }

            count = i;

        }

        insertCount += count % 5000;

        return insertCount;
    }

    /**
     * バッチで設備履歴テーブルデータを取得
     *
     * @param histStartDate　最新作成日時
     * @param machineUuid
     * @return
     */
    public TblMachineHistoryList getExtMachineHistorysByBatch(String histStartDate, String machineUuid) {
        TblMachineHistoryList resList = new TblMachineHistoryList();

        StringBuilder sql = new StringBuilder("SELECT distinct t FROM TblMachineHistory t join MstMachine m on m.uuid = t.tblMachineHistoryPK.machineUuid join MstApiUser u on u.companyId = m.ownerCompanyId WHERE 1 = 1 ");

//        StringBuilder sql = new StringBuilder("SELECT t FROM TblMachineHistory t WHERE 1 = 1 ");

        if (null != machineUuid && !machineUuid.trim().equals("")) {
            sql.append(" and t.tblMachineHistoryPK.machineUuid = :machineUuid ");
        }
        if (null != histStartDate && !histStartDate.trim().equals("")) {
            sql.append(" and t.tblMachineHistoryPK.startDate >= :startDate ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            query.setParameter("machineUuid", machineUuid);
        }
        if (null != histStartDate && !histStartDate.trim().equals("")) {
            histStartDate = histStartDate.replace("-", " ");
            query.setParameter("startDate", DateFormat.strToDateMill(histStartDate));
        }
        List<TblMachineHistory> tmpList = query.getResultList();
        List<TblMachineHistoryVo> vos = new ArrayList<>();
        for (TblMachineHistory tblMachineHistory : tmpList) {
            TblMachineHistoryVo aVo = new TblMachineHistoryVo();
            if (null != tblMachineHistory.getMstMachine()) {
                aVo.setMachineId(tblMachineHistory.getMstMachine().getMachineId());
                tblMachineHistory.setMstMachine(null);
            }
            aVo.setTblMachineHistory(tblMachineHistory);
            vos.add(aVo);
        }
        resList.setTblMachineHistoryVos(vos);
        return resList;
    }

    /**
     * バッチで設備履歴テーブルデータを更新
     *
     * @param tblMachineHistoryVos
     * @return
     */
    @Transactional
    public BasicResponse updateExtMachineHistorysByBatch(List<TblMachineHistoryVo> tblMachineHistoryVos) {
        BasicResponse response = new BasicResponse();
        if (tblMachineHistoryVos != null && !tblMachineHistoryVos.isEmpty()) {
            for (TblMachineHistoryVo aVo : tblMachineHistoryVos) {
                MstMachine ownerMachine = entityManager.find(MstMachine.class, aVo.getMachineId());
                if (null != ownerMachine) {
                    TblMachineHistory newMachineHistory;
                    List<TblMachineHistory> oldMachineHistorys = entityManager.createQuery("SELECT t FROM TblMachineHistory t WHERE t.tblMachineHistoryPK.firstEventNo = :firstEventNo and t.tblMachineHistoryPK.machineUuid = :machineUuid and t.tblMachineHistoryPK.startDate = :startDate  ")
                            .setParameter("firstEventNo", aVo.getTblMachineHistory().getTblMachineHistoryPK().getFirstEventNo())
                            .setParameter("machineUuid", ownerMachine.getUuid())
                            .setParameter("startDate", aVo.getTblMachineHistory().getTblMachineHistoryPK().getStartDate())
                            .setMaxResults(1)
                            .getResultList();
                    if (null == oldMachineHistorys || oldMachineHistorys.isEmpty()) {
                        newMachineHistory = new TblMachineHistory();
                        TblMachineHistoryPK pk = new TblMachineHistoryPK();
                        pk.setFirstEventNo(aVo.getTblMachineHistory().getTblMachineHistoryPK().getFirstEventNo());
                        pk.setMachineUuid(ownerMachine.getUuid());
                        pk.setStartDate(aVo.getTblMachineHistory().getTblMachineHistoryPK().getStartDate());
                        newMachineHistory.setTblMachineHistoryPK(pk);
                    } else {
                        newMachineHistory = oldMachineHistorys.get(0);
                    }

                    newMachineHistory.setEndDate(aVo.getTblMachineHistory().getEndDate());
                    newMachineHistory.setLastEventDate(aVo.getTblMachineHistory().getLastEventDate());
                    newMachineHistory.setLastEventNo(aVo.getTblMachineHistory().getLastEventNo());
                    newMachineHistory.setShotCnt(aVo.getTblMachineHistory().getShotCnt());
                    newMachineHistory.setStatus(aVo.getTblMachineHistory().getStatus());

                    if (null == oldMachineHistorys || oldMachineHistorys.isEmpty()) {
                        entityManager.persist(newMachineHistory);
                    } else {
                        entityManager.merge(newMachineHistory);
                    }
                }

            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }

    private Map<String, List<String[]>> getDateTimeMap(List list) {
        Map<String, List<String[]>> map = new HashMap();
        List<String[]> dateTime = new ArrayList();
        String machineIdPre = "";
        for (int j = 0; j < list.size(); j++) {
            Object[] objs = (Object[]) list.get(j);
            String machineIdCurrent = String.valueOf(objs[0] == null ? "" : objs[0]);
            if (!"".equals(machineIdPre)) {
                if (machineIdPre.equals(machineIdCurrent)) {
                    dateTime.add(new String[]{String.valueOf(objs[1] == null ? "" : objs[1]), String.valueOf(objs[2] == null ? "" : objs[2])});
                } else {
                    map.put(machineIdPre, dateTime);
                    dateTime = new ArrayList();
                    dateTime.add(new String[]{String.valueOf(objs[1] == null ? "" : objs[1]), String.valueOf(objs[2] == null ? "" : objs[2])});
                    machineIdPre = machineIdCurrent;
                }
            } else {
                dateTime.add(new String[]{String.valueOf(objs[1] == null ? "" : objs[1]), String.valueOf(objs[2] == null ? "" : objs[2])});
                machineIdPre = machineIdCurrent;
            }
        }
        if (!dateTime.isEmpty()) {
            map.put(machineIdPre, dateTime);
        }
        return map;
    }

    /**
     * 自社の今ＭＡＸ開始日時を取得
     *
     * @param machineUuid
     * @return
     */
    public TblMachineHistory getLatestCreateDate(String machineUuid) {
        Query query = entityManager.createNamedQuery("TblMachineHistory.findMaxStartDateByMachineUuid");
        query.setParameter("machineUuid", machineUuid);
        query.setMaxResults(1);
        TblMachineHistory tblMachineHistory = null;
        try {
            tblMachineHistory = (TblMachineHistory) query.getSingleResult();
        } catch (NoResultException e) {
            //nothing
        }
        return tblMachineHistory;
    }
    
    /**
     * ある設備の指定時間内のショット数を取得する
     * @param machineUuid
     * @param machineId
     * @param startDatetime
     * @param endDatetime
     * @return 
     */
    public long getShotCount(String machineUuid, String machineId, String startDatetime, String endDatetime) {
        long shotCount = 0;
        String pMachineUuid;
        //UUIDが指定されていなければIDからUUIDを取得する
        if (machineUuid == null || machineUuid.equals("")) {
            MstMachine machine = mstMachineService.getMstMachineByMachineId(machineId);
            if (machine == null) {
                return 0;
            }
            else {
                pMachineUuid = machine.getUuid();
            }
        }
        else {
            //UUIDが指定されている場合はそれをパラメータとして使う
            pMachineUuid = machineUuid;
        }
        Date sDate = DateFormat.strToDatetime(startDatetime);
        Date eDate = DateFormat.strToDatetime(endDatetime);
        Query query = entityManager.createQuery(
            "SELECT COUNT(t) FROM TblSigmaLog t WHERE t.cycleStart = 'ON' AND t.tblSigmaLogPK.machineUuid = :machineUuid " +
                    " AND t.tblSigmaLogPK.createDate BETWEEN :sDate AND :eDate "
        );
        query.setParameter("machineUuid", pMachineUuid);
        query.setParameter("sDate", sDate);
        query.setParameter("eDate", eDate);
        shotCount = (long)query.getSingleResult();
        return shotCount;
    }
    
    /**
     * 設備履歴テーブルに更新
     *
     * @param list
     * @return
     */
    @Transactional
    public int batchUpdate(List<TblMachineHistory> list) {

        int updateCount = 0;

        int count = 0;

        for (int i = 1; i <= list.size(); i++) {

            entityManager.merge(list.get(i - 1));

            // 50件毎にDBへ登録する
            if (i % 50 == 0) {
                entityManager.flush();
                entityManager.clear();

                updateCount += 50;
            } 

            count = i;

        }

        updateCount += count % 50;

        return updateCount;
    }
    
    /**
     * 設備履歴テーブルに更新
     *
     * @param tblMachineHistoryList
     * @param tblWorks
     * @param tblProductionDetailList
     * @param langId
     */
    public void setMachineHistoryDetailVo(TblMachineHistoryList tblMachineHistoryList,
        TblWorkList tblWorks, TblProductionDetailList tblProductionDetailList, String langId) {

        MstChoiceList mstChoiceDepartmentList = mstChoiceService.getChoice(langId, "mst_user.department");
        List<TblMachineHistoryDetailVo> vos = new ArrayList();
        
        // 作業実績情報を一覧Voに設定
        if (tblWorks != null && tblWorks.getTblWorks() != null && !tblWorks.getTblWorks().isEmpty()) {
            for (TblWork tblWork : tblWorks.getTblWorks()) {
                TblMachineHistoryDetailVo vo = new TblMachineHistoryDetailVo();
                vo.setStartDatetime(tblWork.getStartDatetime());
                vo.setEndDatetime(tblWork.getEndDatetime());
                vo.setWorkPhaseName(tblWork.getMstWorkPhase() == null ? null : tblWork.getMstWorkPhase().getWorkPhaseName());
                vo.setComponentCode(tblWork.getMstComponent() == null ? null : tblWork.getMstComponent().getComponentCode());
                vo.setComponentName(tblWork.getMstComponent() == null ? null : tblWork.getMstComponent().getComponentName());
                vo.setUserName(tblWork.getMstUser() == null ? null : tblWork.getMstUser().getUserName());
                if (tblWork.getMstUser() != null) {
                    for (MstChoice mstChoice : mstChoiceDepartmentList.getMstChoice()) {
                        if (mstChoice.getMstChoicePK().getSeq().equals("" + tblWork.getMstUser().getDepartment())) {
                            vo.setDepartmentName(mstChoice.getChoice());
                            break;
                        }
                    }
                } else {
                    vo.setDepartmentName(null);
                }
                vo.setMoldId(tblWork.getMstMold() == null ? null : tblWork.getMstMold().getMoldId());
                vo.setMoldName(tblWork.getMstMold() == null ? null : tblWork.getMstMold().getMoldName());
                
                vos.add(vo);
            }
        }
        
        // 生産実績情報を一覧Voに設定
        if (tblProductionDetailList != null && tblProductionDetailList.getTblProductionDetails() != null && !tblProductionDetailList.getTblProductionDetails().isEmpty()) {
            for (TblProductionDetail tblProductionDetail : tblProductionDetailList.getTblProductionDetails()) {
                TblMachineHistoryDetailVo vo = new TblMachineHistoryDetailVo();
                vo.setStartDatetime(tblProductionDetail.getProductionId().getStartDatetime());
                vo.setEndDatetime(tblProductionDetail.getProductionId().getEndDatetime());
                vo.setWorkPhaseName(tblProductionDetail.getProductionId().getMstWorkPhase() == null ? null : tblProductionDetail.getProductionId().getMstWorkPhase().getWorkPhaseName());
                vo.setComponentCode(tblProductionDetail.getMstComponent() == null ? null : tblProductionDetail.getMstComponent().getComponentCode());
                vo.setComponentName(tblProductionDetail.getMstComponent() == null ? null : tblProductionDetail.getMstComponent().getComponentName());
                vo.setUserName(tblProductionDetail.getProductionId().getMstUser() == null ? null : tblProductionDetail.getProductionId().getMstUser().getUserName());
                if (tblProductionDetail.getProductionId().getMstUser() != null) {
                    for (MstChoice mstChoice : mstChoiceDepartmentList.getMstChoice()) {
                        if (mstChoice.getMstChoicePK().getSeq().equals("" + tblProductionDetail.getProductionId().getMstUser().getDepartment())) {
                            vo.setDepartmentName(mstChoice.getChoice());
                            break;
                        }
                    }
                } else {
                    vo.setDepartmentName(null);
                }
                vo.setMoldId(tblProductionDetail.getProductionId().getMstMold() == null ? null : tblProductionDetail.getProductionId().getMstMold().getMoldId());
                vo.setMoldName(tblProductionDetail.getProductionId().getMstMold() == null ? null : tblProductionDetail.getProductionId().getMstMold().getMoldName());
                vo.setProcedureCode(tblProductionDetail.getMstProcedure() == null ? null : tblProductionDetail.getMstProcedure().getProcedureCode());
                vo.setProcedureName(tblProductionDetail.getMstProcedure() == null ? null : tblProductionDetail.getMstProcedure().getProcedureName());
                vo.setLotNumber(tblProductionDetail.getProductionId().getLotNumber());
                vo.setShotCount(tblProductionDetail.getProductionId().getShotCount());
                
                vos.add(vo);
            }
        }
        
        // ソート
        Collections.sort(vos, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                TblMachineHistoryDetailVo vo1 = (TblMachineHistoryDetailVo) o1;
                TblMachineHistoryDetailVo vo2 = (TblMachineHistoryDetailVo) o2;
                
                return vo1.getStartDatetime().compareTo(vo2.getStartDatetime());
            }
        });
        
        tblMachineHistoryList.setTblMachineHistoryDetailVos(vos);
    }
    
    /**
     * 作業実績テーブルCSV出力
     * @param tblMachineHistoryList
     * @param langId
     * @return 
     */
    public FileReponse getWorkproductionCSV(TblMachineHistoryList tblMachineHistoryList, String langId, String userUuid) {
        /*
         * Header用意
         */
        List<String> dictKeyList = Arrays.asList("work_list", "production_list", "work_start_time", "work_end_time", "work_phase", "component_code", "component_name", "work_user_name",
            "work_user_department", "mold_id", "mold_name", "procedure_code", "procedure_name", "lot_number", "shot_count");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        
        FileReponse fr = new FileReponse();

        /*
         * Header設定
         */
        ArrayList csvOutHeadList = new ArrayList();
        csvOutHeadList.add(headerMap.get("work_start_time"));
        csvOutHeadList.add(headerMap.get("work_end_time"));
        csvOutHeadList.add(headerMap.get("work_phase"));
        csvOutHeadList.add(headerMap.get("component_code"));
        csvOutHeadList.add(headerMap.get("component_name"));
        csvOutHeadList.add(headerMap.get("work_user_name"));
        csvOutHeadList.add(headerMap.get("work_user_department"));
        csvOutHeadList.add(headerMap.get("mold_id"));
        csvOutHeadList.add(headerMap.get("mold_name"));
        csvOutHeadList.add(headerMap.get("procedure_code"));
        csvOutHeadList.add(headerMap.get("procedure_name"));
        csvOutHeadList.add(headerMap.get("lot_number"));
        csvOutHeadList.add(headerMap.get("shot_count"));
        
        // 出力データ準備
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        gLineList.add(csvOutHeadList);
        
        // CSV明細用
        ArrayList<ArrayList> contents = new ArrayList<ArrayList>();
        // 行用
        ArrayList<String> line;
        
        List<TblMachineHistoryDetailVo> tblMachineHistoryDetailVos = tblMachineHistoryList.getTblMachineHistoryDetailVos();
        if (tblMachineHistoryDetailVos != null && !tblMachineHistoryDetailVos.isEmpty()) {
            for (TblMachineHistoryDetailVo detailVo : tblMachineHistoryDetailVos) {
                line = new ArrayList<String>();
                // 開始時刻
                if (detailVo.getStartDatetime() != null) {
                    String outputStartDatetime = FileUtil.getDateTimeFormatMMDDHHMMStr(detailVo.getStartDatetime());
                    line.add(outputStartDatetime);
                } else {
                    line.add("");
                }
                // 終了時刻
                if (detailVo.getEndDatetime() != null) {
                    String outputEndDatetime = FileUtil.getDateTimeFormatMMDDHHMMStr(detailVo.getEndDatetime());
                    line.add(outputEndDatetime);
                } else {
                    line.add("");
                }
                // 工程
                line.add(detailVo.getWorkPhaseName());
                // 部品コード
                line.add(detailVo.getComponentCode());
                // 部品名称
                line.add(detailVo.getComponentName());
                // 作業者氏名(生産実績では生産者氏名)
                line.add(detailVo.getUserName());
                // 部署
                line.add(detailVo.getDepartmentName());
                // 金型ID
                line.add(detailVo.getMoldId());
                // 金型名称
                line.add(detailVo.getMoldName());
                // 部品工程番号
                line.add(detailVo.getProcedureCode());
                // 部品工程名称
                line.add(detailVo.getProcedureName());
                // ロット番号
                line.add(detailVo.getLotNumber());
                // ショット数
                line.add(String.valueOf(detailVo.getShotCount()));
                
                // 行を明細リストに追加
                contents.add(line);
            }
        }

        for (int i = 0; i < contents.size(); i++) {
            gLineList.add(contents.get(i));
        }

        CSVFileUtil csvFileUtil = null;
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        try {
            csvFileUtil = new CSVFileUtil(outCsvPath, "csvOutput");
            Iterator<ArrayList> iter = gLineList.iterator();
            while (iter.hasNext()) {
                csvFileUtil.toCSVLine(iter.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // CSVファイルwriterのクローズ処理
            if (csvFileUtil != null) {
                csvFileUtil.close();
            }
        }

        FileUtil fu = new FileUtil();

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportDate(new java.util.Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_BL_MACHINE_HISTORY);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(userUuid);
        tblCsvExport.setClientFileName(fu.getCsvFileName(headerMap.get("work_list") + "_" + headerMap.get("production_list")));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }
}
