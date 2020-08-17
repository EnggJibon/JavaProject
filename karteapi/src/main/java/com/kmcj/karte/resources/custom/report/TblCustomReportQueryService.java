/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authctrl.MstAuthCtrlService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.custom.report.category.TblCustomReportCategory;
import com.kmcj.karte.resources.custom.report.user.MstQueryUserService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.config.ResultType;
import org.eclipse.persistence.internal.helper.DatabaseField;

/**
 *
 * @author admin
 */
@Dependent
public class TblCustomReportQueryService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME_VIEWONLY)
    private EntityManager entityManagerViewOnly;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private MstAuthCtrlService mstAuthCtrlService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;
    
    @Inject
    private TblCsvImportService tblCsvImportService;
    
    @Inject
    private MstQueryUserService mstQueryUserService;

    // リターン情報を初期化
    int addedCount = 0;//追加件数
    int updatedCount = 0;//更新件数
    int failedCount = 0; //失敗件数
    
    /**
     *
     * @param langId
     * @param reportId
     * @return
     */
    public TblCustomReportQueryVo getCustomReportQuery(Long reportId, String langId) {
        TblCustomReportQueryVo queryVo = new TblCustomReportQueryVo();
        Query query = entityManager.createNamedQuery("TblCustomReportQuery.findByReportId");
        query.setParameter("reportId", reportId);

        List<TblCustomReportQuery> list = query.getResultList();
        TblCustomReportQueryParamVo paramVo;
        if (!list.isEmpty()) {
            TblCustomReportQuery tblCustomReportQuery = (TblCustomReportQuery) list.get(0);
            queryVo.setReportId(tblCustomReportQuery.getReportId());
            queryVo.setReportName(tblCustomReportQuery.getReportName());
            queryVo.setReportSql(tblCustomReportQuery.getReportSql());
            queryVo.setDescription(tblCustomReportQuery.getDescription());
            if (tblCustomReportQuery.getCategoryId() != null) {
                queryVo.setCategoryId(tblCustomReportQuery.getCategoryId());
                queryVo.setCategoryName(tblCustomReportQuery.getCategoryName());
            } else {
                queryVo.setCategoryId("");
                queryVo.setCategoryName("");
            }
            //queryVo.setCreateDate(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(tblCustomReportQuery.getCreateDate()));
            //queryVo.setUpdateDate(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(tblCustomReportQuery.getUpdateDate()));
            if (tblCustomReportQuery.getTblCustomReportQueryParamCollection() != null) {
                Iterator<TblCustomReportQueryParam> iterator = tblCustomReportQuery.getTblCustomReportQueryParamCollection().iterator();

                List<TblCustomReportQueryParamVo> paramVos = new ArrayList();
                while (iterator.hasNext()) {
                    TblCustomReportQueryParam tblCustomReportQueryParam = iterator.next();
                    paramVo = new TblCustomReportQueryParamVo();
                    paramVo.setParamId(tblCustomReportQueryParam.getParamId());
                    paramVo.setReportId(tblCustomReportQueryParam.getReportId());
                    paramVo.setParamName(tblCustomReportQueryParam.getParamName());
                    paramVo.setParamValue(tblCustomReportQueryParam.getParamValue());
                    paramVos.add(paramVo);
                }

                queryVo.setTblCustomReportQueryParamVos(paramVos);
            }
        } else {
            queryVo.setError(true);
            queryVo.setErrorCode(ErrorMessages.E201_APPLICATION);
            queryVo.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_data_deleted"));
        }

        return queryVo;
    }
    
    public TblCustomReportQuery getQuery(Long reportId) {
        return entityManager.createNamedQuery("TblCustomReportQuery.findByReportId", TblCustomReportQuery.class)
            .setParameter("reportId", reportId).getResultList().stream().findFirst().orElse(null);
    }
    
    /**
     *
     * @return
     */
    public TblCustomReportQueryVoList getCustomReportQueryList() {

        List<TblCustomReportQueryVo> listVo = new ArrayList();
        TblCustomReportQueryVo queryVo;
        TblCustomReportQueryVoList listvo = new TblCustomReportQueryVoList();

        Query query = entityManager.createNamedQuery("TblCustomReportQuery.findAll");
        List<TblCustomReportQuery> list = query.getResultList();

        for (TblCustomReportQuery tblCustomReportQuery : list) {
            queryVo = new TblCustomReportQueryVo();
            queryVo.setReportId(tblCustomReportQuery.getReportId());
            queryVo.setReportName(tblCustomReportQuery.getReportName());
            queryVo.setDescription(tblCustomReportQuery.getDescription());
            if (tblCustomReportQuery.getCategoryId() != null) {
                queryVo.setCategoryId(tblCustomReportQuery.getCategoryId());
                queryVo.setCategoryName(tblCustomReportQuery.getCategoryName());
            } else {
                queryVo.setCategoryId("");
                queryVo.setCategoryName("");
            }
            queryVo.setCreateDate(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(tblCustomReportQuery.getCreateDate()));
            queryVo.setUpdateDate(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(tblCustomReportQuery.getUpdateDate()));
            if (tblCustomReportQuery.getMstCreateUser() != null) {
                queryVo.setCreateUserName(tblCustomReportQuery.getMstCreateUser().getUserName());
            } else {
                queryVo.setCreateUserName("");
            }

            if (tblCustomReportQuery.getMstUpdateUser() != null) {
                queryVo.setUpdateUserName(tblCustomReportQuery.getMstUpdateUser().getUserName());
            } else {
                queryVo.setUpdateUserName("");
            }

            listVo.add(queryVo);
        }
        listvo.setTblCustomReportQueryVos(listVo);
        return listvo;
    }
    
    /**
     * @param categoryId
     * @return
     */
    @Transactional
    public TblCustomReportQueryVoList getCustomReportQueryCategoryList(String categoryId) {

        List<TblCustomReportQueryVo> listVo = new ArrayList();
        TblCustomReportQueryVo queryVo;
        TblCustomReportQueryVoList listvo = new TblCustomReportQueryVoList();
        StringBuilder sql = new StringBuilder("SELECT t FROM TblCustomReportQuery t ");
        sql.append("LEFT JOIN FETCH t.mstCreateUser mstCreateUser ");
        sql.append("LEFT JOIN FETCH t.mstUpdateUser mstUpdateUser ");
        sql.append("LEFT JOIN TblCustomReportCategory tcrc on tcrc.id = t.categoryId ");
        if(categoryId != null){
            sql.append("WHERE t.categoryId = :categoryId ");
        } else {
            sql.append("WHERE t.categoryId IS NULL ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if(categoryId != null){
            query.setParameter("categoryId", categoryId);
        }
        List<TblCustomReportQuery> list = query.getResultList();

        for (TblCustomReportQuery tblCustomReportQuery : list) {
            queryVo = new TblCustomReportQueryVo();
            queryVo.setReportId(tblCustomReportQuery.getReportId());
            queryVo.setReportName(tblCustomReportQuery.getReportName());
            queryVo.setDescription(tblCustomReportQuery.getDescription());
            if (tblCustomReportQuery.getCategoryId() != null) {
                queryVo.setCategoryId(tblCustomReportQuery.getCategoryId());
                queryVo.setCategoryName(tblCustomReportQuery.getCategoryName());
            } else {
                queryVo.setCategoryId("");
                queryVo.setCategoryName("");
            }
            queryVo.setCreateDate(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(tblCustomReportQuery.getCreateDate()));
            queryVo.setUpdateDate(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(tblCustomReportQuery.getUpdateDate()));
            if (tblCustomReportQuery.getMstCreateUser() != null) {
                queryVo.setCreateUserName(tblCustomReportQuery.getMstCreateUser().getUserName());
            } else {
                queryVo.setCreateUserName("");
            }

            if (tblCustomReportQuery.getMstUpdateUser() != null) {
                queryVo.setUpdateUserName(tblCustomReportQuery.getMstUpdateUser().getUserName());
            } else {
                queryVo.setUpdateUserName("");
            }

            listVo.add(queryVo);
        }
        listvo.setTblCustomReportQueryVos(listVo);
        return listvo;
    }

    /**
     *
     * @param reportId
     * @return
     */
    @Transactional
    public int deleteCustomReportQuery(long reportId) {

        Query query = entityManager.createNamedQuery("TblCustomReportQuery.deleteByReportId");
        query.setParameter("reportId", reportId);
        int delCount = query.executeUpdate();
        return delCount;
    }

    /**
     *
     * @param tblCustomReportQueryVo
     * @param userUuid
     * @return
     */
    @Transactional
    public TblCustomReportQueryVo addCustomReportQuery(TblCustomReportQueryVo tblCustomReportQueryVo, String userUuid) {
        TblCustomReportQuery tblCustomReportQuery = new TblCustomReportQuery();

        // tbl_custom_report_query
        long reportId = getMaxReportId();
        tblCustomReportQuery.setReportId(reportId);
        tblCustomReportQuery.setDescription(tblCustomReportQueryVo.getDescription());
        tblCustomReportQuery.setReportName(tblCustomReportQueryVo.getReportName());
        tblCustomReportQuery.setReportSql(tblCustomReportQueryVo.getReportSql());
        tblCustomReportQuery.setCategoryId(tblCustomReportQueryVo.getCategoryId());
        //tblCustomReportQuery.setCategoryName(tblCustomReportQuery.getCategoryName());
        tblCustomReportQuery.setCreateDate(new Date());//作成时间
        tblCustomReportQuery.setUpdateDate(new Date());//更新时间
        tblCustomReportQuery.setUpdateUserUuid(userUuid);
        tblCustomReportQuery.setCreateUserUuid(userUuid);

        // tbl_custom_report_query_param List
        Set<TblCustomReportQueryParam> setParams = new HashSet();
        TblCustomReportQueryParam queryParam;

        // PARAM SETTING
        if (tblCustomReportQueryVo.getTblCustomReportQueryParamVos() != null) {
            for (TblCustomReportQueryParamVo paramVo : tblCustomReportQueryVo.getTblCustomReportQueryParamVos()) {
                queryParam = new TblCustomReportQueryParam();
                queryParam.setParamId(IDGenerator.generate());
                queryParam.setReportId(reportId);
                queryParam.setParamName(paramVo.getParamName());
                queryParam.setParamValue(paramVo.getParamValue());
                setParams.add(queryParam);
            }

            if (setParams.size() > 0) {
                tblCustomReportQuery.setTblCustomReportQueryParamCollection(setParams);
            }
        }
        // DB SAVE
        entityManager.persist(tblCustomReportQuery);
        
        
        //在庫STEP2により処理追加
        //公開ユーザー一覧にチェックされている内容に合わせてレポートクエリユーザー関連テーブルを登録する。
        mstQueryUserService.addTblReportQueryUser(reportId, tblCustomReportQueryVo.getTblReportQueryUserVos(), userUuid);
        
        tblCustomReportQueryVo.setReportId(reportId);
        return tblCustomReportQueryVo;
    }

    /**
     *
     * @param tblCustomReportQueryVo
     * @param userUuid
     * @return
     */
    @Transactional
    public int updateCustomReportQuery(TblCustomReportQueryVo tblCustomReportQueryVo, String userUuid) {

        TblCustomReportQuery tblCustomReportQuery = entityManager.find(TblCustomReportQuery.class, tblCustomReportQueryVo.getReportId());

        if (tblCustomReportQuery != null) {

            Query query = entityManager.createNamedQuery("TblCustomReportQueryParam.deleteQueryParamByReportId");
            query.setParameter("reportId", tblCustomReportQueryVo.getReportId());
            query.executeUpdate();
            entityManager.flush();
            entityManager.clear();

            tblCustomReportQuery.setDescription(tblCustomReportQueryVo.getDescription());
            tblCustomReportQuery.setReportName(tblCustomReportQueryVo.getReportName());
            tblCustomReportQuery.setReportSql(tblCustomReportQueryVo.getReportSql());
            tblCustomReportQuery.setCategoryId(tblCustomReportQueryVo.getCategoryId());
            //tblCustomReportQuery.setCategoryName(tblCustomReportQuery.getCategoryName());
            tblCustomReportQuery.setUpdateDate(new Date());
            tblCustomReportQuery.setUpdateUserUuid(userUuid);

            // tbl_custom_report_query_param List
            Set<TblCustomReportQueryParam> setParams = new HashSet();
            TblCustomReportQueryParam queryParam;

            // PARAM SETTING
            if (tblCustomReportQueryVo.getTblCustomReportQueryParamVos() != null) {
                for (TblCustomReportQueryParamVo paramVo : tblCustomReportQueryVo.getTblCustomReportQueryParamVos()) {
                    queryParam = new TblCustomReportQueryParam();
                    queryParam.setParamId(IDGenerator.generate());
                    queryParam.setReportId(tblCustomReportQueryVo.getReportId());
                    queryParam.setParamName(paramVo.getParamName());
                    queryParam.setParamValue(paramVo.getParamValue());
                    setParams.add(queryParam);
                }

                if (setParams.size() > 0) {
                    tblCustomReportQuery.setTblCustomReportQueryParamCollection(setParams);
                }
            }

            entityManager.merge(tblCustomReportQuery);
            
            //在庫STEP2により処理追加
            //公開ユーザー一覧にチェックされている内容に合わせてレポートクエリユーザー関連テーブルを登録する。
            mstQueryUserService.updateTblReportQueryUser(tblCustomReportQueryVo.getReportId(), tblCustomReportQueryVo.getTblReportQueryUserVos(), userUuid, tblCustomReportQueryVo.getSelectedFlag());

        } else {

            return 0;
        }

        return 1;
    }

    /**
     * カスタム戻る結果取得
     *
     * @param tblCustomReportQueryVo
     * @param loginUser
     * @return
     */
    public TblCustomReportQueryResultList getCustomReportQueryJson(TblCustomReportQueryVo tblCustomReportQueryVo, LoginUser loginUser) {

        TblCustomReportQueryResultList result = new TblCustomReportQueryResultList();
        try {
            return executeSql(tblCustomReportQueryVo.getReportSql(), tblCustomReportQueryVo.getTblCustomReportQueryParamVos(), loginUser.getLangId());
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            result.setError(true);
            result.setErrorCode(ErrorMessages.E201_APPLICATION);
            result.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_sql_or_parameter_is_incorrect"));
            result.setExceptionMessage(sw.toString());
            return result;
        }
    }
    
    /**
     * カスタム戻る結果取得
     *
     * @param tblCustomReportQueryVo
     * @param loginUser
     * @return
     */
    public String getCustomReportQueryJsonForUrl(TblCustomReportQueryVo tblCustomReportQueryVo, LoginUser loginUser) {

        Gson gson = new Gson();
        
        Map<String, Object> map = null;
        if (!mstAuthCtrlService.isAuthorizedFunction(loginUser.getAuthId(), CommonConstants.FUN_ID_CUSTOM_REPORT_QUERY)) {
            map = new HashMap();
            map.put("error", true);
            map.put("errorCode", ErrorMessages.E105_OPP_NOT_ALLOWED);
            map.put("errorMessage", mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_opp_not_allowed"));
            return gson.toJson(map);
        } else {
            TblCustomReportQueryResultList tblCustomReportQueryResultList = null;
            try {
                tblCustomReportQueryResultList = executeSql(tblCustomReportQueryVo.getReportSql(), tblCustomReportQueryVo.getTblCustomReportQueryParamVos(), loginUser.getLangId());
            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                map = new HashMap();
                map.put("error", true);
                map.put("errorCode", ErrorMessages.E105_OPP_NOT_ALLOWED);
                map.put("errorMessage", mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_opp_not_allowed"));
                map.put("exceptionMessage", sw.toString());
                return gson.toJson(map);
            }
            
            if (tblCustomReportQueryResultList != null && tblCustomReportQueryResultList.getRowList() != null && !tblCustomReportQueryResultList.getRowList().isEmpty()) {
                List<Map<String, Object>> rowList = new ArrayList();
                for (TblCustomReportQueryColList colList : tblCustomReportQueryResultList.getRowList()) {
                    map = new LinkedHashMap<>();
                    List<String> headerList = new ArrayList();
                    for (int i = 0; i < colList.getColList().size(); i++) {
                        if (headerList.contains(tblCustomReportQueryResultList.getHeaderList().get(i))) {
                            map.put(new String(tblCustomReportQueryResultList.getHeaderList().get(i)), colList.getColList().get(i));
                        } else {
                            map.put(tblCustomReportQueryResultList.getHeaderList().get(i), colList.getColList().get(i));
                        }
                        headerList.add(tblCustomReportQueryResultList.getHeaderList().get(i));
                    }
                    rowList.add(map);
                }
                Map<String, Object> resultMap = new HashMap();
                resultMap.put("resultList", rowList);
                return gson.toJson(resultMap);
            } else {
                map = new HashMap();
                map.put("resultList", "");
                return gson.toJson(map);
            }
        }
    }

    /**
     *
     * @param tblCustomReportQueryVo
     * @param loginUser
     * @return
     */
    public TblCustomReportQueryResultList getCustomReportQueryCsv(TblCustomReportQueryVo tblCustomReportQueryVo, LoginUser loginUser) {
        
        TblCustomReportQueryResultList fileReponse = new TblCustomReportQueryResultList();
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        
        try {
            TblCustomReportQueryResultList tblCustomReportQueryResultList
                    = executeSql(tblCustomReportQueryVo.getReportSql(), tblCustomReportQueryVo.getTblCustomReportQueryParamVos(), loginUser.getLangId());

            if (tblCustomReportQueryResultList.isError()) {
                fileReponse.setError(true);
                fileReponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                fileReponse.setErrorMessage(tblCustomReportQueryResultList.getErrorMessage());
                return fileReponse;
            }
            
            String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "custom_report_query");
            getCustomReportSetValueCsv(tblCustomReportQueryResultList, uuid, fileName, outCsvPath, loginUser.getUserUuid());
            
        } catch (Exception e) {
            e.printStackTrace();
            
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            fileReponse.setError(true);
            fileReponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            fileReponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_sql_or_parameter_is_incorrect"));
            fileReponse.setExceptionMessage(sw.toString());
            return fileReponse;
            
        }
        //csvファイルのUUIDをリターンする
        fileReponse.setFileUuid(uuid);
        return fileReponse;
    }

    /**
     *
     * @param tblCustomReportQueryVo
     * @param loginUser
     * @return
     */
    public Response getCustomReportQueryCsv2(TblCustomReportQueryVo tblCustomReportQueryVo, LoginUser loginUser) {
        
        if (!mstAuthCtrlService.isAuthorizedFunction(loginUser.getAuthId(), CommonConstants.FUN_ID_CUSTOM_REPORT_QUERY)) {
            
            // 権限なし　エラーコード403をリターンする
            Response.ResponseBuilder response = Response.status(403);
            return response.build();
            
        } else {
            String uuid = IDGenerator.generate();
            String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);

            try {
                TblCustomReportQueryResultList tblCustomReportQueryResultList
                        = executeSql(tblCustomReportQueryVo.getReportSql(), tblCustomReportQueryVo.getTblCustomReportQueryParamVos(), loginUser.getLangId());

                if (tblCustomReportQueryResultList.isError()) {
                    Response.ResponseBuilder response = Response.status(500);
                    return response.build();
                }
                
                String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "custom_report_query");
                getCustomReportSetValueCsv(tblCustomReportQueryResultList, uuid, fileName, outCsvPath, loginUser.getUserUuid());
                
                File file = new File(outCsvPath);
                Response.ResponseBuilder response = Response.ok(file);
                String encodeStr = FileUtil.getEncod(fileName);
                if (encodeStr != null) {
                    encodeStr = encodeStr.replace("+", "%20");
                }
                
                response.header(CommonConstants.CONTENT_DISPOSITION, "attachment; filename=\"" + encodeStr + ".csv\"");
                return response.build();
                
            } catch (Exception e) {
                e.printStackTrace();
                
                Response.ResponseBuilder response = Response.status(500);
                return response.build();
                
            }
        }
    }

    /**
     * CSV set value
     *
     * @param tblCustomReportQueryResultList
     * @param uuid
     * @param fileName
     * @param outCsvPath
     * @param userUuid
     */
    public void getCustomReportSetValueCsv(TblCustomReportQueryResultList tblCustomReportQueryResultList, String uuid, String fileName, String outCsvPath, String userUuid) {

        ArrayList lineList;
        if (tblCustomReportQueryResultList != null) {
            /*Head*/
            ArrayList<ArrayList> gLineList = new ArrayList<>();
            ArrayList headList = new ArrayList();
            if (tblCustomReportQueryResultList.getHeaderList() != null && tblCustomReportQueryResultList.getHeaderList().size() > 0) {
                for (String header : tblCustomReportQueryResultList.getHeaderList()) {
                    headList.add(header);
                }
                gLineList.add(headList);
            }
            /*DATA*/
            if (tblCustomReportQueryResultList.getRowList() != null && tblCustomReportQueryResultList.getRowList().size() > 0) {
                for (TblCustomReportQueryColList colData : tblCustomReportQueryResultList.getRowList()) {
                    lineList = new ArrayList();
                    for (String col : colData.getColList()) {
                        if (StringUtils.isNotEmpty(col)) {
                            lineList.add(col);
                        } else {
                            lineList.add("");
                        }
                    }
                    gLineList.add(lineList);
                }
            }
            CSVFileUtil.writeCsv(outCsvPath, gLineList);
            TblCsvExport tblCsvExport = new TblCsvExport();
            tblCsvExport.setFileUuid(uuid);
            MstFunction functionId = new MstFunction();
            functionId.setId(CommonConstants.FUN_ID_CUSTOM_REPORT_QUERY);
            tblCsvExport.setFunctionId(functionId);
            tblCsvExport.setExportTable(CommonConstants.TBL_CUSTOM_REPORT_QUERY);
            tblCsvExport.setExportDate(new Date());
            tblCsvExport.setExportUserUuid(userUuid);
            tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
            tblCsvExportService.createTblCsvExport(tblCsvExport);
        }
    }
      
    /**
     * カスタム戻る結果SQL
     *
     * @param sql
     * @param params
     * @param langId
     * @return
     */
    private TblCustomReportQueryResultList executeSql(String sql, List<TblCustomReportQueryParamVo> params, String langId) throws Exception {
        TblCustomReportQueryResultList result = new TblCustomReportQueryResultList();
        List<TblCustomReportQueryColList> rowList = new ArrayList();
        Map<String, String> paramMap = new HashMap();
        List<String> remainParamList = new ArrayList();
        TblCustomReportQueryColList colList;
        List<String> clos;
        List<String> headerList;
        String key;

        String regexParam = ":(\\S+)";
        String regexInParam = "(?i)IN[\\s]*\\(([^:]*)";
        String queryParam;
        String strSql;
        String remain;
        String temp = "";
        int index = 1;
        if (StringUtils.isNotEmpty(sql)) {
            Pattern patternParam = Pattern.compile(regexParam);
            Matcher matcherParam = patternParam.matcher(sql);
            Pattern patternInParam = Pattern.compile(regexInParam);
            Matcher strSqlInParam;
            Query query;
            if (params != null && params.size() > 0) {
                for (TblCustomReportQueryParamVo vo : params) {
                    key = vo.getParamName().trim();
                    paramMap.put(key, vo.getParamValue());
                }
            }
            while (matcherParam.find()) {
                queryParam = matcherParam.group(1);
                strSql = sql.substring(0, sql.indexOf(":" + queryParam) + queryParam.length() + 1);
                remain = sql.substring(sql.indexOf(":" + queryParam) + queryParam.length() + 1, sql.length());
                strSqlInParam = patternInParam.matcher(strSql);
                if (strSqlInParam.find()) {
                    String gridInParam = "";
                    if (queryParam.indexOf(")") != -1) {
                        queryParam = queryParam.substring(0, queryParam.indexOf(")")).trim();
                    }
                    if (!paramMap.containsKey(queryParam)) {
                        result.setError(true);
                        result.setErrorCode(ErrorMessages.E201_APPLICATION);
                        String msg = mstDictionaryService.getDictionaryValue(langId, "msg_error_param_not_setting");
                        msg = String.format(msg, queryParam);
                        result.setErrorMessage(msg);
                        return result;
                    }
                    if (StringUtils.isNotEmpty(paramMap.get(queryParam))) {
                        for (String param : paramMap.get(queryParam).split(",")) {
                            gridInParam = gridInParam.concat("'").concat(param).concat("',");
                        }
                        gridInParam = gridInParam.substring(0, gridInParam.length() - 1);
                    }
                    temp += strSql.replace(":" + queryParam, gridInParam);
                } else {
                    if (queryParam.indexOf(")") != -1) {
                        queryParam = queryParam.substring(0, queryParam.indexOf(")")).trim();
                    }
                    if (queryParam.indexOf(",") != -1) {
                        queryParam = queryParam.substring(0, queryParam.indexOf(",")).trim();
                    }
                    temp += strSql.replace(":" + queryParam, "?");
                    remainParamList.add(queryParam.trim());
                }
                sql = remain;
            }
            if(StringUtils.isNotEmpty(temp)) {
                sql = temp + sql;
            }
            query = entityManagerViewOnly.createNativeQuery(sql);
            if (remainParamList.size() > 0) {
                for (String remainParam : remainParamList) {
                    if (!paramMap.containsKey(remainParam)) {
                        result.setError(true);
                        result.setErrorCode(ErrorMessages.E201_APPLICATION);
                        String msg = mstDictionaryService.getDictionaryValue(langId, "msg_error_param_not_setting");
                        msg = String.format(msg, remainParam);
                        result.setErrorMessage(msg);
                        return result;
                    }
                    query.setParameter(index, paramMap.get(remainParam));
                    index++;
                }
            }
            query.setHint(QueryHints.RESULT_TYPE, ResultType.Map);
            List<Map<DatabaseField, Object>> list;
            try {
                list = query.getResultList();
            } catch (Exception e) {
                throw e;
            }
            boolean flag = false;
            if (list != null && list.size() > 0) {
                for (Map<DatabaseField, Object> map : list) {
                    colList = new TblCustomReportQueryColList();
                    clos = new ArrayList();
                    headerList = new ArrayList();
                    for (Map.Entry<DatabaseField, Object> entry : map.entrySet()) {
                        headerList.add(entry.getKey().getName());
                        if (entry.getValue() != null) {
                            clos.add(String.valueOf(entry.getValue()));
                        } else {
                            clos.add("");
                        }
                    }
                    colList.setColList(clos);
                    rowList.add(colList);
                    if (!flag) {
                        result.setHeaderList(headerList);
                        flag = true;
                    }
                    result.setRowList(rowList);
                }
            } else {
                headerList = new ArrayList();
                result.setHeaderList(headerList);
                result.setRowList(rowList);
            }
            return result;
        } else {
            result.setError(true);
            result.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(langId, "msg_error_not_null_with_item");
            msg = String.format(msg, mstDictionaryService.getDictionaryValue(langId, "report_sql"));
            result.setErrorMessage(msg);
            return result;
        }
    }

    /**
     *
     * @param map
     * @param langId
     * @return
     */
    public TblCustomReportQueryVo getUrlCustomReportQuery(MultivaluedMap<String, String> map, String langId) {
        TblCustomReportQueryVo queryVo = new TblCustomReportQueryVo();
        String reportIdKey = "reportId";
        String reportNameKey = "reportName";
        if (map != null && map.size() > 0) {
            if (map.get(reportIdKey) != null || map.get(reportNameKey) != null) {
                Query query = entityManager.createNamedQuery("TblCustomReportQuery.findByReportId");
  
                if (map.get(reportIdKey) != null && map.get(reportIdKey).get(0) != null) {
                    query.setParameter("reportId", Long.parseLong(map.get(reportIdKey).get(0)));
                } else if (map.get(reportNameKey) != null && map.get(reportNameKey).get(0) != null) {
                    String reportName = FileUtil.getDecode(map.get(reportNameKey).get(0));
                    TblCustomReportQuery tblCustomReportQuery = getEntityByReportName(reportName);
                    query.setParameter("reportId", tblCustomReportQuery.getReportId());
                }

                List list = query.getResultList();
                TblCustomReportQueryParamVo paramVo;
                List<TblCustomReportQueryParamVo> paramVos;
                if (list != null && list.size() > 0) {
                    TblCustomReportQuery tblCustomReportQuery = (TblCustomReportQuery) list.get(0);
                    queryVo.setReportId(tblCustomReportQuery.getReportId());
                    queryVo.setReportSql(tblCustomReportQuery.getReportSql());
                    if (tblCustomReportQuery.getTblCustomReportQueryParamCollection() != null) {
                        Iterator<TblCustomReportQueryParam> iterator = tblCustomReportQuery.getTblCustomReportQueryParamCollection().iterator();
                        paramVos = new ArrayList();
                        while (iterator.hasNext()) {
                            TblCustomReportQueryParam tblCustomReportQueryParam = iterator.next();
                            paramVo = new TblCustomReportQueryParamVo();
                            paramVo.setParamName(tblCustomReportQueryParam.getParamName());
                            if (map.get(tblCustomReportQueryParam.getParamName()) != null) {
                                paramVo.setParamValue(map.get(tblCustomReportQueryParam.getParamName()).get(0));
                            } else {
                                paramVo.setParamValue(tblCustomReportQueryParam.getParamValue());
                            }
                            paramVos.add(paramVo);
                        }
                        queryVo.setTblCustomReportQueryParamVos(paramVos);
                    } else {
                        map.remove(reportIdKey);
                        if (map.size() > 0) {
                            paramVos = new ArrayList();
                            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                                paramVo = new TblCustomReportQueryParamVo();
                                paramVo.setParamName(entry.getKey());
                                paramVo.setParamValue(entry.getValue().get(0));
                                paramVos.add(paramVo);
                            }
                            queryVo.setTblCustomReportQueryParamVos(paramVos);
                        }
                    }
                }
            } else {
                queryVo.setError(true);
                queryVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(langId, "msg_error_param_not_setting");
                msg = String.format(msg, reportIdKey);
                queryVo.setErrorMessage(msg);
                return queryVo;
            }
        } else {
            queryVo.setError(true);
            queryVo.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(langId, "msg_error_param_not_setting");
            msg = String.format(msg, reportIdKey);
            queryVo.setErrorMessage(msg);
            return queryVo;
        }

        return queryVo;
    }

    /**
     *
     * @return
     */
    private long getMaxReportId() {
        Query query = entityManager.createNamedQuery("TblCustomReportQuery.findMaxReportId");
        // 排他処理のため、採番テーブルをロックする
        query.setLockMode(LockModeType.PESSIMISTIC_WRITE);

        List list = query.getResultList();
        if (list != null && list.size() > 0) {
            Object obj = list.get(0);
            if (obj != null) {
                return Long.parseLong(obj.toString()) + 1;
            } else {
                return 1;
            }
        } else {
            return 1;
        }

    }
    
    /**
     *
     * @param tblCustomReportQueryVo
     * @param langId
     * @param userId
     * @return
     */
    public FileReponse getCustomReportQueryCsv3(TblCustomReportQueryVo tblCustomReportQueryVo, String langId, String userId) {
        FileReponse fileReponse = new FileReponse();
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);

        try {
            TblCustomReportQueryResultList tblCustomReportQueryResultList
                    = executeSql(tblCustomReportQueryVo.getReportSql(), tblCustomReportQueryVo.getTblCustomReportQueryParamVos(), langId);

            if (tblCustomReportQueryResultList.isError()) {
                fileReponse.setError(tblCustomReportQueryResultList.isError());
                fileReponse.setErrorCode(tblCustomReportQueryResultList.getErrorCode());
                fileReponse.setErrorMessage(tblCustomReportQueryResultList.getErrorMessage());
                return fileReponse;
            }

            String fileName = mstDictionaryService.getDictionaryValue(langId, "custom_report_query");
            getCustomReportSetValueCsv(tblCustomReportQueryResultList, uuid, fileName, outCsvPath, userId);
            fileReponse.setFileUuid(uuid);
            return fileReponse;
        } catch (Exception e) {
            fileReponse.setError(true);
            fileReponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            fileReponse.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_sql_or_parameter_is_incorrect"));
            return fileReponse;

        }

    }

    /**
     * Json set value カスタムレポートクエリの定義情報出力
     *
     * @param all
     * @param tblCustomReportQueryVoList
     * @return
     */
    public CustomReportQueryJson getCustomReportExportJson(int all, TblCustomReportQueryVoList tblCustomReportQueryVoList) {

        CustomReportQueryJson customReportQueryJson
                = new CustomReportQueryJson();
        CustomReportQuery customReportQuery = new CustomReportQuery();
        List<QueryDefinition> queryDefinitions = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT t FROM TblCustomReportQuery t LEFT JOIN FETCH t.mstCreateUser mstCreateUser LEFT JOIN FETCH t.mstUpdateUser mstUpdateUser ");
        sql = sql.append("LEFT JOIN TblCustomReportCategory tcrc on tcrc.id = t.categoryId ");
        sql = sql.append("WHERE 1=1 ");
//        if(catId != null){
//            sql = sql.append("AND categoryId = t.catId ");
//        }
        if (all != 1) {//すべてチェックされている場合
            if (tblCustomReportQueryVoList != null) {
                List<TblCustomReportQueryVo> tblCustomReportQueryVos = tblCustomReportQueryVoList.getTblCustomReportQueryVos();
                if (tblCustomReportQueryVos != null && tblCustomReportQueryVos.size() > 0) {
                    sql = sql.append(" AND t.reportId IN :reportIds ");
                }
            }
        }
        Query query = entityManager.createQuery(sql.toString());
        if (all != 1) {//すべてチェックされている場合
            if (tblCustomReportQueryVoList != null) {
                List<TblCustomReportQueryVo> tblCustomReportQueryVos = tblCustomReportQueryVoList.getTblCustomReportQueryVos();
                if (tblCustomReportQueryVos != null && tblCustomReportQueryVos.size() > 0) {
                    Long[] reportIds = new Long[tblCustomReportQueryVos.size()];
                    for (int i = 0; i < tblCustomReportQueryVos.size(); i++) {
                        TblCustomReportQueryVo tblCustomReportQueryVo = (TblCustomReportQueryVo) tblCustomReportQueryVos.get(i);
                        reportIds[i] = tblCustomReportQueryVo.getReportId();
                    }
                    query.setParameter("reportIds", Arrays.asList(reportIds));
                }
            }
        }

        List<TblCustomReportQuery> list = query.getResultList();
        QueryDefinition queryDefinition;
        Parameter parameter;
        for (TblCustomReportQuery tblCustomReportQuery : list) {
            queryDefinition = new QueryDefinition();

            queryDefinition.setQuery(tblCustomReportQuery.getReportSql());
            queryDefinition.setReportId(tblCustomReportQuery.getReportId());
            queryDefinition.setReportName(tblCustomReportQuery.getReportName());
            queryDefinition.setDescription(tblCustomReportQuery.getDescription());
            if (tblCustomReportQuery.getCategoryId() != null) {
                queryDefinition.setCategoryName(tblCustomReportQuery.getCategoryName());
            } else {
                queryDefinition.setCategoryName("");
            }
            Collection<TblCustomReportQueryParam> tblCustomReportQueryParamCollection = tblCustomReportQuery.getTblCustomReportQueryParamCollection();
            if (tblCustomReportQueryParamCollection != null && !tblCustomReportQueryParamCollection.isEmpty()) {
                parameter = new Parameter();
                String parameterName = "";
                String parameterValue = "";
                for (TblCustomReportQueryParam parameters : tblCustomReportQueryParamCollection) {
                    parameterName += parameters.getParamName() + ",";
                    parameterValue += parameters.getParamValue() + ",";
                }
                parameterName = parameterName.substring(0, parameterName.length() - 1);
                parameterValue = parameterValue.substring(0, parameterValue.length() - 1);
                parameter.setParameterName(parameterName.split(","));
                parameter.setParameterValue(parameterValue.split(","));
                queryDefinition.setParameters(parameter);
            }
            queryDefinitions.add(queryDefinition);
        }

        customReportQuery.setQueryDefinition(queryDefinitions);
        customReportQueryJson.setCoustomReportQuery(customReportQuery);

        return customReportQueryJson;
    }

    /**
     * カスタムレポートクエリの定義情報出力
     *
     * @param loginUser
     * @param all
     * @param tblCustomReportQueryVoList
     * @return
     */
    public FileReponse getCustomReportSetValueJson(LoginUser loginUser,int all, TblCustomReportQueryVoList tblCustomReportQueryVoList) {

        FileReponse fileReponse = new FileReponse();
        try {
            FileUtil fileUtil = new FileUtil();
            Gson gson = new Gson();
            String uuid = IDGenerator.generate();
            String outJsonPath = FileUtil.outJsonFile(kartePropertyService, uuid);
            String str = gson.toJson(getCustomReportExportJson(all, tblCustomReportQueryVoList));
            fileUtil.writeInfoToFile(outJsonPath, str);

            TblCsvExport tblCsvExport = new TblCsvExport();
            tblCsvExport.setFileUuid(uuid);
            tblCsvExport.setExportTable("tbl_custom_report_query");
            tblCsvExport.setExportDate(new Date());
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId(CommonConstants.FUN_ID_CUSTOM_REPORT_QUERY);
            tblCsvExport.setFunctionId(mstFunction);
            tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
            String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "custom_report_query");
            tblCsvExport.setClientFileName(fileName + CommonConstants.EXT_JSON);
            tblCsvExportService.createTblCsvExport(tblCsvExport);

            fileReponse.setFileUuid(uuid);
        } catch (Exception e) {
            fileReponse.setError(true);
            fileReponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            fileReponse.setErrorMessage(e.getMessage());
        }
        return fileReponse;
    }

    public int checkReportName(String reportName) {
        Query query = entityManager.createNamedQuery("TblCustomReportQuery.findByReportName");
        query.setParameter("reportName", reportName);
        List list = query.getResultList();
        int flag = 0;//insert
        if (list.size() > 1) {
            flag = 2;//error
        } else if (list.size() == 1) {
            flag = 1;//update
        }

        return flag;
    }

    public TblCustomReportQuery getEntityByReportName(String reportName) {
        Query query = entityManager.createNamedQuery("TblCustomReportQuery.findByReportName");
        query.setParameter("reportName", reportName);
        List list = query.getResultList();
        if (!list.isEmpty()) {
            TblCustomReportQuery tblCustomReportQuery = (TblCustomReportQuery) list.get(0);
            return tblCustomReportQuery;
        } else {
            return null;
        }

    }

    /**
     *
     * @param langId
     * @return
     */
    private Map<String, String> getDictionaryList(String langId) {
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList(
                "row_number",
                "report_name",
                "param_name",
                "param_value",
                "description",
                "custom_report_query",
                "error",
                "error_detail",
                "msg_error_not_null",
                "msg_error_over_length",
                "msg_record_added",
                "msg_record_updated",
                "msg_error_not_null",
                "msg_report_name_multiple_records_found",
                "db_process");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        return headerMap;
    }

    /**
     * カスタムレポートクエリの定義情報取込
     *
     * @param filePath
     * @param fileUuid
     * @param userId
     * @param langId
     * @return
     */
    @Transactional
    public ImportResultResponse importCustomReportJson(String filePath, String fileUuid, String userId, String langId) {

        ImportResultResponse fileReponse = new ImportResultResponse();
        try {
            Gson gson = new Gson();
            FileUtil fu = new FileUtil();
            String json = fu.readInfoFormTxtFile(filePath);
            if (StringUtils.isEmpty(json)) {
                fileReponse.setError(true);
                fileReponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(langId, "msg_error_no_processing_record");
                fileReponse.setErrorMessage(msg);
                return fileReponse;
            }
            JsonReader jsonReader = new JsonReader(new StringReader(json));
            jsonReader.setLenient(true);
            CustomReportQueryJson customReportQueryJson = gson.fromJson(jsonReader, CustomReportQueryJson.class);
            TblCustomReportQueryVo tblCustomReportQueryVo;
            TblCustomReportQueryParamVo tblCustomReportQueryParamVo;
            QueryDefinition querydefintion;
            String logFileUuid = IDGenerator.generate();//ログファイルUUID用意
            String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
            
            Query query = entityManager.createQuery("SELECT t FROM TblCustomReportCategory t ORDER BY t.name ASC");
            List<TblCustomReportCategory> categoryQueryList = query.getResultList();
            List<TblCustomReportCategory> newCategoryNameList = new ArrayList<TblCustomReportCategory>();
            if (customReportQueryJson != null && customReportQueryJson.getCoustomReportQuery() != null) {
                Map<String, String> dictMap = getDictionaryList(langId);
                List<TblCustomReportQueryParamVo> tblCustomReportQueryParamVos;
                int jsonlength = customReportQueryJson.getCoustomReportQuery().getQueryDefinition().size();
                int i = 0;
                int rowNum = 0;
                Error:
                for (; i < jsonlength; i++) {
                    rowNum = i + 1;
                    tblCustomReportQueryVo = new TblCustomReportQueryVo();
                    querydefintion = customReportQueryJson.getCoustomReportQuery().getQueryDefinition().get(i);
                    if (StringUtils.isEmpty(querydefintion.getReportName())) {
                        //ログファイルを記入querydefintion.getReportName()
                        fu.writeInfoToFile(logFile, fu.outValue(dictMap.get("row_number"), rowNum, dictMap.get("report_name"), querydefintion.getReportName(), dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    int flag = checkReportName(querydefintion.getReportName());
                    if (2 == flag) {
                        //ログファイルを記入
                        fu.writeInfoToFile(logFile, fu.outValue(dictMap.get("row_number"), rowNum, dictMap.get("report_name"), querydefintion.getReportName(), dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_report_name_multiple_records_found")));
                        failedCount = failedCount + 1;
                        continue;
                    }

                    if (0 == flag) {
                        if (fu.maxLangthCheck(querydefintion.getReportName(), 100)) {
                            //ログファイルを記入
                            fu.writeInfoToFile(logFile, fu.outValue(dictMap.get("row_number"), rowNum, dictMap.get("report_name"), querydefintion.getReportName(), dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                            failedCount = failedCount + 1;
                            continue;
                        }
                    }

                    if (fu.maxLangthCheck(querydefintion.getDescription(), 256)) {
                        //ログファイルを記入
                        fu.writeInfoToFile(logFile, fu.outValue(dictMap.get("row_number"), rowNum, dictMap.get("description"), querydefintion.getDescription(), dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                        failedCount = failedCount + 1;
                        continue;
                    }

                    if (fu.maxLangthCheck(querydefintion.getQuery(), 10000)) {
                        //ログファイルを記入
                        fu.writeInfoToFile(logFile, fu.outValue(dictMap.get("row_number"), rowNum, dictMap.get("custom_report_query"), querydefintion.getQuery(), dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    
                    tblCustomReportQueryVo.setReportName(querydefintion.getReportName());
                    tblCustomReportQueryVo.setDescription(querydefintion.getDescription());
                    tblCustomReportQueryVo.setReportSql(querydefintion.getQuery());
                    
                    TblCustomReportCategory tblCustomReportCategory = new TblCustomReportCategory();
                    boolean noMatch = false;
                    boolean noNewMatch = true;
                    int counter = 1;
                    
                    for(TblCustomReportCategory category : categoryQueryList){
                        counter ++;
                        if(category.getName().equals(querydefintion.getCategoryName())){
                            tblCustomReportQueryVo.setCategoryId(category.getId());
                            tblCustomReportQueryVo.setCategoryName(category.getName());
                            noMatch = false;
                            break;
                        } else {
                            noMatch = true;
                        }
                    }
                    if(noMatch == true){
                        if(newCategoryNameList.size() > 0){
                            for(TblCustomReportCategory newCategory : newCategoryNameList){
                                if(newCategory.getName() != null){
                                    if(newCategory.getName().equals(querydefintion.getCategoryName())){
                                        tblCustomReportQueryVo.setCategoryId(newCategory.getId());
                                        noNewMatch = false;
                                    } else {
                                        noNewMatch = true;
                                    }
                                }
                            }
                        } 
                        if(noNewMatch == true){
                            String newId = IDGenerator.generate();
                            if(!querydefintion.getCategoryName().equals("")){
                                tblCustomReportCategory.setId(newId);
                                tblCustomReportCategory.setName(querydefintion.getCategoryName());
                                entityManager.persist(tblCustomReportCategory);
                            }
                            if(!querydefintion.getCategoryName().equals("")){
                                tblCustomReportQueryVo.setCategoryId(newId);
                                tblCustomReportQueryVo.setCategoryName(querydefintion.getCategoryName());
                            }
                            if(!querydefintion.getCategoryName().equals("")){
                                TblCustomReportCategory newCategoryName = new TblCustomReportCategory();
                                newCategoryName.setId(newId);
                                newCategoryName.setName(querydefintion.getCategoryName());
                                newCategoryNameList.add(newCategoryName);
                            }
                        }
                    }
                    
                    
                    
                    if (querydefintion.getParameters() != null && querydefintion.getParameters().getParameterName() != null) {
                        int paramlength = querydefintion.getParameters().getParameterName().length;
                        tblCustomReportQueryParamVos = new ArrayList<>();
                        for (int j = 0; j < paramlength; j++) {
                            tblCustomReportQueryParamVo = new TblCustomReportQueryParamVo();
                            tblCustomReportQueryParamVo.setParamName(querydefintion.getParameters().getParameterName()[j]);
                            tblCustomReportQueryParamVo.setParamValue(querydefintion.getParameters().getParameterValue()[j]);
                            System.out.println(tblCustomReportQueryParamVo.getParamName());
                            if (fu.maxLangthCheck(tblCustomReportQueryParamVo.getParamName(), 100)) {
                                //ログファイルを記入
                                fu.writeInfoToFile(logFile, fu.outValue(dictMap.get("row_number"), rowNum, dictMap.get("param_name"), tblCustomReportQueryParamVo.getParamName(), dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                                failedCount = failedCount + 1;
                                continue Error;
                            }

                            if (fu.maxLangthCheck(tblCustomReportQueryParamVo.getParamValue(), 256)) {
                                //ログファイルを記入
                                fu.writeInfoToFile(logFile, fu.outValue(dictMap.get("row_number"), rowNum, dictMap.get("param_value"), tblCustomReportQueryParamVo.getParamValue(), dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                                failedCount = failedCount + 1;
                                continue Error;
                            }

                            tblCustomReportQueryParamVos.add(tblCustomReportQueryParamVo);
                        }
                        tblCustomReportQueryVo.setTblCustomReportQueryParamVos(tblCustomReportQueryParamVos);
                    }
                    if (flag == 1) {
                        //存在時
                        TblCustomReportQuery tblCustomReportQuery = getEntityByReportName(querydefintion.getReportName());
                        tblCustomReportQueryVo.setReportId(tblCustomReportQuery.getReportId());
                        updateCustomReportQuery(tblCustomReportQueryVo, userId);

                        updatedCount = updatedCount + 1;
                        //DB操作情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(dictMap.get("row_number"), rowNum, dictMap.get("report_name"), querydefintion.getReportName(), dictMap.get("error"), 0, dictMap.get("db_process"), dictMap.get("msg_record_updated")));
                    } else {
                        //存在していないとき                        
                        addCustomReportQuery(tblCustomReportQueryVo, userId);
                        addedCount = addedCount + 1;
                        //DB操作情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(dictMap.get("row_number"), rowNum, dictMap.get("report_name"), querydefintion.getReportName(), dictMap.get("error"), 0, dictMap.get("db_process"), dictMap.get("msg_record_added")));
                    }
                }
                int succeededCount = addedCount + updatedCount;
                procesExit(userId, fileUuid, jsonlength, logFileUuid, langId);
                // リターン情報
                fileReponse.setTotalCount(jsonlength);
                fileReponse.setSucceededCount(succeededCount);
                fileReponse.setAddedCount(addedCount);
                fileReponse.setUpdatedCount(updatedCount);
                fileReponse.setDeletedCount(0);
                fileReponse.setFailedCount(failedCount);
                fileReponse.setLog(logFileUuid);
            }
        } catch (Exception e) {
            fileReponse.setError(true);
            fileReponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            fileReponse.setErrorMessage(e.getMessage());
            Logger.getLogger(TblCustomReportQueryService.class.getName()).log(Level.SEVERE, null, e);
        }
        return fileReponse;
    }

    /**
     * 取込処理完了処理
     *
     * @param loginUser
     * @param fileUuid
     * @param jsonInfoSize
     * @param logFileUuid
     */
    private void procesExit(String userId, String fileUuid, int jsonInfoSize, String logFileUuid, String langId) {

        //アップロードログをテーブルに書き出し
        TblCsvImport tblCsvImport = new TblCsvImport();
        tblCsvImport.setImportUuid(IDGenerator.generate());
        tblCsvImport.setImportUserUuid(userId);
        tblCsvImport.setImportDate(new Date());
        tblCsvImport.setImportTable("tbl_custom_report_query");
        TblUploadFile tblUploadFile = new TblUploadFile();
        tblUploadFile.setFileUuid(fileUuid);
        tblCsvImport.setUploadFileUuid(tblUploadFile);
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_CUSTOM_REPORT_QUERY_EDIT);
        tblCsvImport.setFunctionId(mstFunction);
        tblCsvImport.setRecordCount(jsonInfoSize - 1);
        tblCsvImport.setSuceededCount(addedCount + updatedCount);
        tblCsvImport.setAddedCount(addedCount);
        tblCsvImport.setUpdatedCount(updatedCount);
        tblCsvImport.setDeletedCount(0);
        tblCsvImport.setFailedCount(failedCount);
        tblCsvImport.setLogFileUuid(logFileUuid);

        String fileName = mstDictionaryService.getDictionaryValue(langId, "custom_report_query");
        tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

        tblCsvImportService.createCsvImpor(tblCsvImport);
    }

}
