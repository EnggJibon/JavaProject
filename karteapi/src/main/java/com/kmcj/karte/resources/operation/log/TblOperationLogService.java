/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.operation.log;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.util.BeanCopyUtil;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author kmc
 */
@Dependent
public class TblOperationLogService {
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;
    
    @Inject
    private MstDictionaryService mstDictionaryService;

    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("createDate", " ORDER BY tblOperationLog.tblOperationLogPK.createDate ");//操作日付
        orderKey.put("userId", " ORDER BY tblOperationLog.tblOperationLogPK.userId ");//ユーザーID
        orderKey.put("userName", " ORDER BY tblOperationLog.mstUser.userName ");//ユーザー名称
        orderKey.put("dictValue", " ORDER BY tblOperationLog.mstDictionary.dictValue ");//操作
        orderKey.put("operationParm", " ORDER BY tblOperationLog.operationParm ");//パラメーター
        orderKey.put("screenType", " ORDER BY tblOperationLog.operationProc ");//画面
    }

    /**
     * 操作ログ照会データ取得
     *
     * @param operationDateFrom
     * @param operationDateTo
     * @param userId
     * @param pageNumber
     * @param pageSize
     * @param langId
     * @param isPage
     * @param isList
     * @param sidx
     * @param sord
     * @return
     */
    public TblOperationLogList getTblOperationLogList(String operationDateFrom, String operationDateTo, String userId,
            int pageNumber, int pageSize, String langId, boolean isPage, boolean isList,
            String sidx,//order Key
            String sord//order 順
    ) {
        TblOperationLogList tblOperationLogList = new TblOperationLogList();

        if (!isPage) {
            List count = getTblOperationLogSql(operationDateFrom, operationDateTo, userId, pageNumber, pageSize, langId, true, null, null);
            // ページをめくる
            Pager pager = new Pager();
            tblOperationLogList.setPageNumber(pageNumber);
            long counts = (long) count.get(0);
            tblOperationLogList.setCount(counts);
            tblOperationLogList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));
        }

        List list = getTblOperationLogSql(operationDateFrom, operationDateTo, userId, pageNumber, pageSize, langId, false, sidx, sord);
        List<TblOperationLogVo> tblOperationLogVos = new ArrayList<>();

        if (list != null && list.size() > 0) {
            TblOperationLogVo tblOperationLogVo;
            TblOperationLog tblOperationLog;
            for (int i = 0; i < list.size(); i++) {
                tblOperationLog = (TblOperationLog) list.get(i);
                tblOperationLogVo = new TblOperationLogVo();

                BeanCopyUtil.copyFields(tblOperationLog, tblOperationLogVo);
                if (tblOperationLog.getTblOperationLogPK() != null){
                    if (tblOperationLog.getTblOperationLogPK().getCreateDate() != null){
                        tblOperationLogVo.setCreateDate(DateFormat.dateToStrMill(tblOperationLog.getTblOperationLogPK().getCreateDate()));
                    }
                    if (tblOperationLog.getTblOperationLogPK().getUserId() != null){
                        tblOperationLogVo.setUserId(tblOperationLog.getTblOperationLogPK().getUserId());
                    }
                    if (tblOperationLog.getTblOperationLogPK().getOperationPath() != null){
                        if (tblOperationLog.getOperationProc().equals("php")) {
                            tblOperationLogVo.setScreenType("Web");
                        } else if (tblOperationLog.getOperationProc().equals("api")) {
                            String[] opePathArray = tblOperationLog.getTblOperationLogPK().getOperationPath().trim().split("/");
                            if (opePathArray[0].equals("authctrl") && opePathArray[1].equals("checkfunc")){
                                tblOperationLogVo.setScreenType("Tablet");
                            } else {
                                tblOperationLogVo.setScreenType("");
                            }
                        } else {
                            tblOperationLogVo.setScreenType("");
                        }
                    }
                }
                
                if(tblOperationLog.getMstUser() != null){
                    if (tblOperationLog.getMstUser().getUuid() != null){
                        tblOperationLogVo.setUuid(tblOperationLog.getMstUser().getUuid());
                    } else {
                    }
                    if (tblOperationLog.getMstUser().getUserName() != null){
                        tblOperationLogVo.setUserName(tblOperationLog.getMstUser().getUserName());
                    }
                } else {
                    if (tblOperationLog.getTblOperationLogPK() != null){
                        tblOperationLogVo.setUuid("");
                        tblOperationLogVo.setUserName("");
                    }
                }
                
                if (tblOperationLog.getMstDictionary() != null){
                    if (tblOperationLog.getMstDictionary().getDictKey() != null){
                        tblOperationLogVo.setDictKey(tblOperationLog.getMstDictionary().getDictKey());
                    }
                    if (tblOperationLog.getMstDictionary().getDictValue() != null){
                        tblOperationLogVo.setDictValue(tblOperationLog.getMstDictionary().getDictValue());
                    }
                } else {
                    tblOperationLogVo.setDictKey("");
                    tblOperationLogVo.setDictValue("");
                }
                
                if (tblOperationLog.getOperationParm() != null){
                    tblOperationLogVo.setOperationParm(tblOperationLog.getOperationParm());
                } else {
                    tblOperationLogVo.setOperationParm("");
                }
                
                
                tblOperationLogVos.add(tblOperationLogVo);
            }
            tblOperationLogList.setTblOperationLogList(tblOperationLogVos);
        } else {
            tblOperationLogList.setError(true);
            tblOperationLogList.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(langId, "mst_error_record_not_found");
            tblOperationLogList.setErrorMessage(msg);
        }

        return tblOperationLogList;
    }
    
    /**
     * @param operationDateFrom
     * @param operationDateTo
     * @param userId
     * @param pageNumber
     * @param pageSize
     * @param langId
     * @param isCount
     * @param sidx
     * @param sord
     * @return
     */
    private List getTblOperationLogSql(
            String operationDateFrom, 
            String operationDateTo, 
            String userId,
            int pageNumber, 
            int pageSize, 
            String langId,
            boolean isCount,
            String sidx,//order Key
            String sord//order 順
    ) {
        StringBuilder sql = new StringBuilder(" SELECT ");
        if (isCount) {
            sql = sql.append(" COUNT(1) ");
        } else {
            sql = sql.append(" tblOperationLog ");
        }

        sql = sql.append(" FROM TblOperationLog tblOperationLog");
        sql = sql.append(" JOIN FETCH tblOperationLog.mstUser mstUser"
                + " JOIN FETCH tblOperationLog.mstDictionary mstDictionary"
                + " WHERE 1=1 ");

        if (StringUtils.isNotEmpty(langId)) {
            sql = sql.append(" AND mstDictionary.mstDictionaryPK.langId = :langId ");
        }
        if (StringUtils.isNotEmpty(userId)) {
            sql = sql.append(" AND tblOperationLog.tblOperationLogPK.userId LIKE :userId ");
        }
        if (StringUtils.isNotEmpty(operationDateFrom)) {
            sql = sql.append(" AND tblOperationLog.tblOperationLogPK.createDate >= :operationDateFrom ");
        }
        if (StringUtils.isNotEmpty(operationDateTo)) {
            sql = sql.append(" AND tblOperationLog.tblOperationLogPK.createDate <= :operationDateTo ");
        }
        if (!isCount) {
            // 
            if (StringUtils.isNotEmpty(sidx)) {
                sql = sql.append(orderKey.get(sidx));

                if (StringUtils.isNotEmpty(sord)) {
                    sql = sql.append(sord);
                }

            } else {

                sql.append(" ORDER BY tblOperationLog.tblOperationLogPK.createDate ");
            }
        }

        Query query = entityManager.createQuery(sql.toString());

        if (StringUtils.isNotEmpty(langId)) {
            query.setParameter("langId", langId);
        }

        if (StringUtils.isNotEmpty(userId)) {
            query.setParameter("userId", "%" + userId + "%");
        }

        if (StringUtils.isNotEmpty(operationDateFrom)) {
            Date operationDateFromDate = null;
            if (operationDateFrom.length() < 26){
                operationDateFrom = operationDateFrom + " 00:00:00.000000";
                operationDateFrom = operationDateFrom.substring(0, 26);
                operationDateFromDate = DateFormat.strToDateMill(operationDateFrom);
            }
            query.setParameter("operationDateFrom", operationDateFromDate);
        }

        if (StringUtils.isNotEmpty(operationDateTo)) {
            Date operationDateToDate = null;
            if (operationDateTo.length() < 26){
                operationDateTo = operationDateTo + " 23:59:59.999999";
                operationDateTo = operationDateTo.substring(0, 26);
                operationDateToDate = DateFormat.strToDateMill(operationDateTo);
            }
            query.setParameter("operationDateTo", operationDateToDate);
        }
        
        if (!isCount) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }

        List list = query.getResultList();

        return list;
    }
    
    /**
     * 操作ログ照会データ取得
     *
     * @param operationDateFrom
     * @param operationDateTo
     * @param userId
     * @param loginUser
     * @return
     */
    public FileReponse getTblOperationLogCsvOutPut(String operationDateFrom, String operationDateTo, String userId, LoginUser loginUser) {
        TblOperationLogList tblOperationLogList = getTblOperationLogList(operationDateFrom, operationDateTo, userId, 0, 0, loginUser.getLangId(), false, true, null, null);
        FileReponse fileReponse = new FileReponse();
        ArrayList lineList;
        String uuid = IDGenerator.generate();
        String langId = loginUser.getLangId();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList("operation_datetime", "user_id", "user_name", "operation_column", "operation_parm");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);

        /*Head*/
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();
        headList.add(headerMap.get("operation_datetime"));  // 操作日時
        headList.add(headerMap.get("user_id")); // ユーザーID
        headList.add(headerMap.get("user_name"));  // ユーザー名称
        headList.add(headerMap.get("operation_column")); // 操作機能
        headList.add(headerMap.get("operation_parm")); // パラメーター 
        
        gLineList.add(headList);
        if (tblOperationLogList != null && tblOperationLogList.getTblOperationLogList() != null && tblOperationLogList.getTblOperationLogList().size() > 0) {
            for (int i = 0; i < tblOperationLogList.getTblOperationLogList().size(); i++) {
                TblOperationLogVo tblOperationLogVo = tblOperationLogList.getTblOperationLogList().get(i);
                lineList = new ArrayList();
                lineList.add(tblOperationLogVo.getCreateDate());// 操作日時
                lineList.add(tblOperationLogVo.getUserId());// ユーザーID
                lineList.add(tblOperationLogVo.getUserName());// ユーザー名称
                lineList.add(tblOperationLogVo.getDictValue());// 操作機能
                if (StringUtils.isNotEmpty(tblOperationLogVo.getOperationParm())) {// パラメーター
                    lineList.add(tblOperationLogVo.getOperationParm());
                } else {
                    lineList.add("");
                }
                
                gLineList.add(lineList);
            }
        }

        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_MOLD_ATTRIBUTE);
        tblCsvExport.setExportDate(new Date());
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, "opelog_reference");
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fileReponse.setFileUuid(uuid);

        return fileReponse;
    }

    /**
     *
     * @param tblOperationLog
     * @return 
     */
    @Transactional
    public BasicResponse createTblOperationLog(TblOperationLog tblOperationLog){
        BasicResponse response = new BasicResponse();
        try {
            entityManager.persist(tblOperationLog);
            response.setError(false);
        } catch  (Exception e) {
            response.setError(true);
        }
        return response;
    }

}
