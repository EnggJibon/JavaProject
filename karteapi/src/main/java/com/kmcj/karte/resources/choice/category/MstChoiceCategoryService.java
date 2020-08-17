/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.choice.category;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.excelhandle.read.ReadExcel;
import com.kmcj.karte.excelhandle.read.ReadListExcel;
import com.kmcj.karte.excelhandle.write.WriteExcelList;
import com.kmcj.karte.excelhandle.write.WriteListExcel;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceCategory;
import com.kmcj.karte.resources.choice.MstChoicePK;
import com.kmcj.karte.resources.choice.MstChoicePrime;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.choice.MstChoiceVo;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.language.MstLanguage;
import com.kmcj.karte.resources.language.MstLanguageService;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author
 */
@Dependent
public class MstChoiceCategoryService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private TblCsvImportService tblCsvImportService;

    @Inject
    private MstLanguageService mstLanguageService;

    // リターン情報を初期化
    long succeededCount = 0;//成功件数
    long addedCount = 0;//追加件数
    long updatedCount = 0;//更新件数
    long failedCount = 0; //失敗件数
    long blankCount = 0; //NumberOfBlankRecord

    /**
     *
     * @param langId
     * @param loginUser
     * @return
     */
    public MstChoiceCategoryVo getChoiceCategories(String langId) {
        MstChoiceCategoryVo choiceCategoryVo = new MstChoiceCategoryVo();
        List<MstChoiceCategoryVo> resVos = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT cc.id, dt.dictValue tabName, d.dictValue categoryName, dp.dictValue parentCategory,cc.canAddDelete,cc.category,cc.parentCategory "
                + " FROM MstChoiceCategory cc "
                + " left join MstDictionary dt on cc.tableId = dt.mstDictionaryPK.dictKey and dt.mstDictionaryPK.langId = :langId "
                + " left join MstDictionary d on cc.categoryNameDictKey = d.mstDictionaryPK.dictKey and d.mstDictionaryPK.langId = :langId "
                + " left join MstChoiceCategory cp on cc.parentCategory = cp.category "
                + " left join MstDictionary dp on cp.categoryNameDictKey = dp.mstDictionaryPK.dictKey and dp.mstDictionaryPK.langId = :langId "
                + " where 1=1 ");
        sql.append(" order by cc.seq asc ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("langId", langId);
        List<Object[]> list = query.getResultList();
        for (int i = 0; i < list.size(); i++) {
            MstChoiceCategoryVo resVo = new MstChoiceCategoryVo();
            Object[] aRes = list.get(i);
            resVo.setId(aRes[0].toString());
            resVo.setCategoryTypeDictValue(null == aRes[1] ? "" : aRes[1].toString());
            resVo.setCategory(null == aRes[2] ? "" : aRes[2].toString());
            resVo.setParentCategory(null == aRes[3] ? "" : aRes[3].toString());
            resVo.setCanAddDelete(null == aRes[4] ? "" : aRes[4].toString());
            resVo.setCategoryCode(null == aRes[5] ? "" : aRes[5].toString());
            resVo.setParentCategoryCode(null == aRes[6] ? "" : aRes[6].toString());
            resVos.add(resVo);
        }
        choiceCategoryVo.setChoiceCategoryVos(resVos);

        return choiceCategoryVo;
    }

    /**
     *
     * @param mstChoiceCategoryVo
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse postChoiceCategories(MstChoiceCategoryVo mstChoiceCategoryVo, LoginUser loginUser) {
        TypedQuery<String> langQuery = entityManager.createQuery("SELECT ml.id FROM MstLanguage ml", String.class);
        List<String> langList = langQuery.getResultList();
        if (null != mstChoiceCategoryVo.getMstChoiceVo() && mstChoiceCategoryVo.getMstChoiceVo().isEmpty() == false) {
            List<MstChoiceVo> mstChoiceVos = mstChoiceCategoryVo.getMstChoiceVo();
            String currentLang = mstChoiceVos.get(0).getLangId();
            MstChoiceVo oldMstChoiceVo = null;
            Map<MstChoicePK, MstChoice> registered = new HashMap<>();
            List<MstChoice> addPojos = new ArrayList<>();
            List<MstChoice> toUpd = new ArrayList<>();
            List<MstChoice> toReodr = new ArrayList<>();
            
            for (int i = 0, tmpSeq = 1; i < mstChoiceVos.size(); i++, tmpSeq++) {
                MstChoiceVo aChoiceVo = mstChoiceVos.get(i);
                if (null == aChoiceVo) {//之前有提交空数据的情况，这里是处理空数据的情况
                    tmpSeq--;
                    continue;
                }
                if (null == oldMstChoiceVo) {
                    oldMstChoiceVo = aChoiceVo; //for delete all
                    registered = getChoicePkInCtg(aChoiceVo.getCategory());
                }
                for (String userLang : langList) {
                    MstChoice theChoice = new MstChoice();
                    String generatedId = IDGenerator.generate();
                    if(aChoiceVo.getId().length() == generatedId.length()){
                        theChoice.setId(aChoiceVo.getId());
                    } else {
                        theChoice.setId(generatedId);
                    }
                    theChoice.setCreateDate(new Date());
                    theChoice.setCreateUserUuid(loginUser.getUserUuid());
                    MstChoicePK mstChoicePK = new MstChoicePK();
                    mstChoicePK.setCategory(null == aChoiceVo.getCategory() ? "" : aChoiceVo.getCategory());
                    mstChoicePK.setLangId(userLang);
                    mstChoicePK.setSeq(aChoiceVo.getSeq());
                    theChoice.setMstChoicePK(mstChoicePK);
                    
                    theChoice.setCreateDate(new Date());
                    theChoice.setCreateUserUuid(loginUser.getUserUuid());
                    theChoice.setUpdateDate(new Date());
                    theChoice.setUpdateUserUuid(loginUser.getUserUuid());
                    theChoice.setDisplaySeq(aChoiceVo.getDisplaySeq());
                    if (null != aChoiceVo.getParentSeq() && !aChoiceVo.getParentSeq().equals("")) {
                        theChoice.setParentSeq(aChoiceVo.getParentSeq());
                    }
                    if(null == aChoiceVo.getDeleteFlg()){
                        theChoice.setDeleteFlg(0);
                    } else {
                        theChoice.setDeleteFlg(aChoiceVo.getDeleteFlg() == 1 ? 1 : 0);
                    }
                    
                    if(userLang.equals(currentLang)){
                        theChoice.setChoice(aChoiceVo.getChoice());
                    } else {
                        theChoice.setChoice("");
                    }
                    if(registered.containsKey(theChoice.getMstChoicePK())) {
                        if(currentLang.equals(theChoice.getMstChoicePK().getLangId())) {
                            toUpd.add(theChoice);
                        } else {
                            toReodr.add(theChoice);
                        }
                    } else {
                        addPojos.add(theChoice);
                    }
                }
            }

            for (MstChoice theChoice : addPojos) {
                entityManager.persist(theChoice);
            }
            for (MstChoice theChoice : toUpd) {
                //upd displayseq and choice.
                MstChoice upd = registered.get(theChoice.getMstChoicePK());
                upd.setChoice(theChoice.getChoice());
                upd.setDisplaySeq(theChoice.getDisplaySeq());
                upd.setParentSeq(theChoice.getParentSeq());
                upd.setDeleteFlg(theChoice.getDeleteFlg());
                upd.setUpdateDate(theChoice.getUpdateDate());
                upd.setUpdateUserUuid(theChoice.getUpdateUserUuid());
            }
            for (MstChoice theChoice : toReodr) {
                MstChoice upd = registered.get(theChoice.getMstChoicePK());
                upd.setDisplaySeq(theChoice.getDisplaySeq());
                upd.setParentSeq(theChoice.getParentSeq());
                upd.setDeleteFlg(theChoice.getDeleteFlg());
                upd.setUpdateDate(theChoice.getUpdateDate());
                upd.setUpdateUserUuid(theChoice.getUpdateUserUuid());
            }
        }

        return new BasicResponse();
    }
    
    private Map<MstChoicePK, MstChoice> getChoicePkInCtg(String category) {
        List<MstChoice> choices = entityManager.createNamedQuery("MstChoice.findByCategoryOnly", MstChoice.class)
            .setParameter("category", category).getResultList();
        Map<MstChoicePK, MstChoice> ret = new HashMap<>();
        choices.stream().forEach(choice->ret.put(choice.getMstChoicePK(), choice));
        return ret;
    }

    /**
     *
     * @param langId
     * @param category
     * @param parentSeq
     * @return
     */
    public boolean deleteChoicesByParentSeq(String langId, String category, String parentSeq) {

        Query query = entityManager.createQuery("Select c FROM MstChoice c WHERE c.mstChoiceCategory.parentCategory = :category and c.mstChoicePK.langId = :langId and c.parentSeq = :seq");
        query.setParameter("langId", langId);
        query.setParameter("category", category);
        query.setParameter("seq", parentSeq);
        List<MstChoice> mstChoices = query.getResultList();
        if (null != mstChoices && !mstChoices.isEmpty()) {

            for (int i = 0; i < mstChoices.size(); i++) {
                MstChoice aMstChoice = mstChoices.get(i);
                deleteChoicesByParentSeq(langId, aMstChoice.getMstChoicePK().getCategory(), aMstChoice.getMstChoicePK().getSeq());
                Query queryOld = entityManager.createNamedQuery("MstChoice.DeleteByPK");
                queryOld.setParameter("langId", langId);
                queryOld.setParameter("category", aMstChoice.getMstChoicePK().getCategory());
                queryOld.setParameter("seq", aMstChoice.getMstChoicePK().getSeq());
                queryOld.executeUpdate();
            }

            return true;
        } else {
            Query queryOld = entityManager.createNamedQuery("MstChoice.DeleteByPK");
            queryOld.setParameter("langId", langId);
            queryOld.setParameter("category", category);
            queryOld.setParameter("seq", parentSeq);
            queryOld.executeUpdate();

            return false;
        }

    }

    /**
     * 分類項目CSV出力
     *
     * @param langId
     * @param loginUser
     * @return
     */
    public FileReponse getOutputCsv(String langId, LoginUser loginUser) {

        // CSV出力項目取得用　SQL文
        StringBuilder sql;
        sql = new StringBuilder("SELECT cc.category, dt.dictValue tabName, d.dictValue categoryName, dp.dictValue parentCategory, choice.mstChoicePK.seq, choice.displaySeq, choice.deleteFlg, choice.choice, choice.parentSeq "
                + " FROM MstChoiceCategory cc  "
                + " left join MstDictionary dt on cc.tableId = dt.mstDictionaryPK.dictKey and dt.mstDictionaryPK.langId = :langId "
                + " left join MstDictionary d on cc.categoryNameDictKey = d.mstDictionaryPK.dictKey and d.mstDictionaryPK.langId = :langId "
                + " left join MstChoiceCategory cp on cc.parentCategory = cp.category "
                + " left join MstDictionary dp on cp.categoryNameDictKey = dp.mstDictionaryPK.dictKey and dp.mstDictionaryPK.langId = :langId "
                + " left join MstChoice choice on choice.mstChoicePK.category = cc.category and choice.mstChoicePK.langId = :langId"
                + " where 1=1 and cc.canAddDelete = 1 ");
        sql.append(" order by cc.seq asc ");
        Query query = entityManager.createQuery(sql.toString());
        
        if (StringUtils.isNotEmpty(langId)) {
            query.setParameter("langId", langId);
        } else {
            langId = loginUser.getLangId();
            query.setParameter("langId", langId);
        }
        // 分類項目取得
        List list = query.getResultList();

        // 出力へーだー準備
        ArrayList<ArrayList> gLineList = getHeaderes(langId);
        MstLanguage mstLanguage = mstLanguageService.getByIDLanguage(langId);
        String language = mstLanguage == null ? "" : mstLanguage.getLang();
       
        ArrayList lineList;
        Object[] returnValue;
        for (int i = 0; i < list.size(); i++) {
            lineList = new ArrayList();
            returnValue = (Object[]) list.get(i);
            lineList.add(returnValue[0] == null ? "" : String.valueOf(returnValue[0]));  // CATEGORY
            lineList.add(language);  // 言語
            lineList.add(returnValue[1] == null ? "" : String.valueOf(returnValue[1]));  // 種別
            lineList.add(returnValue[2] == null ? "" : String.valueOf(returnValue[2]));  // 分類項目
            lineList.add(returnValue[3] == null ? "" : String.valueOf(returnValue[3]));  // 上位分類項目
            lineList.add(returnValue[4] == null ? "" : String.valueOf(returnValue[4]));  // 連番 (ID)
            lineList.add(returnValue[5] == null ? "" : String.valueOf(returnValue[5]));  //displaySeq
            lineList.add(returnValue[6] == null ? "" : String.valueOf(returnValue[6]));  //deleteFlg
            lineList.add(returnValue[7] == null ? "" : String.valueOf(returnValue[7]));  // 区分値
            lineList.add(returnValue[8] == null ? "" : String.valueOf(returnValue[8].toString().replaceAll(",","@")));
            gLineList.add(lineList);
        }
        FileReponse fileReponse = new FileReponse();
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);

        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.MST_CHOICE_CATEGORY);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MST_CHOICE_CATEGORY);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, "category_item_setting");
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);

        //csvファイルのUUIDをリターンする
        fileReponse.setFileUuid(uuid);

        return fileReponse;
    }

    /**
     * CSV出力へーだー準備
     *
     * @return
     */
    private ArrayList<ArrayList> getHeaderes(String langId) {
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();
        /*Head*/
        //CSVファイル出力ヘーダー CATEGORY,言語,種別,分類項目,上位分類項目,連番,区分値
        Map<String, String> csvHeader = getDictValues(langId);
        headList.add("CATEGORY");
        headList.add(csvHeader.get("lang"));
        headList.add(csvHeader.get("categoryType"));
        headList.add(csvHeader.get("categoryItem"));
        headList.add(csvHeader.get("parentCategoryItem"));
        headList.add(csvHeader.get("id"));
        headList.add(csvHeader.get("displaySeq"));
        headList.add(csvHeader.get("deleteFlg"));
        headList.add(csvHeader.get("categoryValue"));
        headList.add(csvHeader.get("parentSeq"));
        gLineList.add(headList);
        return gLineList;
    }

    /**
     * CSVヘーダー用
     *
     * @param langId
     * @return
     */
    private Map<String, String> getDictValues(String langId) {

        Map<String, String> dictMap = new HashMap<>();

        List<String> dictKeyList = Arrays.asList("lang", "category_type", "category_item", "parent_category_item", "id", "category_value", "parent_category_value","display_seq","delete_flg");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        dictMap.put("lang", headerMap.get("lang"));
        dictMap.put("categoryType", headerMap.get("category_type"));
        dictMap.put("categoryItem", headerMap.get("category_item"));
        dictMap.put("parentCategoryItem", headerMap.get("parent_category_item"));
        dictMap.put("id", headerMap.get("id"));
        dictMap.put("displaySeq", headerMap.get("display_seq"));
        dictMap.put("deleteFlg", headerMap.get("delete_flg"));
        dictMap.put("categoryValue", headerMap.get("category_value"));
        dictMap.put("parentSeq", headerMap.get("parent_category_value"));
        return dictMap;
    }

    /**
     * CSV Info Check MSG
     *
     * @param langId
     * @return
     */
    private Map<String, String> getCsvInfoCheckMsg(String langId) {
        Map<String, String> msgMap = new HashMap<>();
        List<String> dictKeyList = Arrays.asList("row_number", "msg_error_not_null", "msg_error_over_length", "msg_error_not_isnumber", "error", "error_detail",
                "mst_error_record_not_found", "msg_error_wrong_csv_layout", "db_process", "msg_record_added", "msg_data_modified","msg_record_skipped", "msg_error_duplicate_value");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        // info
        msgMap.put("rowNumber", headerMap.get("row_number"));
        // error msg
        msgMap.put("msgErrorNotNull", headerMap.get("msg_error_not_null"));
        msgMap.put("msgErrorOverLength", headerMap.get("msg_error_over_length"));
        msgMap.put("msgErrorNotIsnumber", headerMap.get("msg_error_not_isnumber"));
        msgMap.put("error", headerMap.get("error"));
        msgMap.put("errorDetail", headerMap.get("error_detail"));
        msgMap.put("mstErrorRecordNotFound", headerMap.get("mst_error_record_not_found"));
        msgMap.put("msgErrorWrongCsvLayout", headerMap.get("msg_error_wrong_csv_layout"));
        msgMap.put("msgRecordSkipped",headerMap.get("msg_record_skipped"));
        msgMap.put("msgErrorDuplicate",headerMap.get("msg_error_duplicate_value"));
        // db info
        msgMap.put("dbProcess", headerMap.get("db_process"));
        msgMap.put("msgRecordAdded", headerMap.get("msg_record_added"));
        msgMap.put("msgDataModified", headerMap.get("msg_data_modified"));

        return msgMap;
    }

    /**
     * 部類項目CSV取込
     *
     * @param fileUuid
     * @param loginUser
     * @return
     */
    @Transactional
    public ImportResultResponse importCsv(String fileUuid, LoginUser loginUser) {
        //リターン用
        ImportResultResponse importResultResponse = new ImportResultResponse();
        // CVSファイルであるかどうか確認用
        String csvFile = FileUtil.getCsvFilePath(kartePropertyService, fileUuid);
        if (!csvFile.endsWith(CommonConstants.CSV)) {
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_wrong_csv_layout");
            importResultResponse.setErrorMessage(msg);
            return importResultResponse;
        }

        // SCVデータを取込み
        ArrayList readList = CSVFileUtil.readCsv(csvFile);
        int csvInfoSize = readList.size();
        if (csvInfoSize <= 1) {
            return importResultResponse;
        } else {
            String logFileUuid = IDGenerator.generate();//ログファイルUUID用意
            String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
            Set<DispSeqDupKey> duplicated = duplicateImportDataCheck(readList);
            
            csvProcesStart(loginUser, readList, logFile, duplicated);
            succeededCount = addedCount + updatedCount + blankCount;
            csvProcesExit(loginUser, fileUuid, csvInfoSize, logFileUuid);   
            // リターン情報
            importResultResponse.setTotalCount(csvInfoSize - 1);
            importResultResponse.setSucceededCount(succeededCount);
            importResultResponse.setAddedCount(addedCount);
            importResultResponse.setUpdatedCount(updatedCount);
            importResultResponse.setDeletedCount(0);
            importResultResponse.setFailedCount(failedCount);
            importResultResponse.setLog(logFileUuid);
            return importResultResponse;
        }
    }

   

    /**
     * CSV処理終了
     *
     * @param loginUser
     * @param fileUuid
     * @param csvInfoSize
     * @param logFileUuid
     */
    @Transactional
    private void csvProcesExit(LoginUser loginUser, String fileUuid, int csvInfoSize, String logFileUuid) {

        //アップロードログをテーブルに書き出し
        TblCsvImport tblCsvImport = new TblCsvImport();
        tblCsvImport.setImportUuid(IDGenerator.generate());
        tblCsvImport.setImportUserUuid(loginUser.getUserUuid());
        tblCsvImport.setImportDate(new Date());
        tblCsvImport.setImportTable(CommonConstants.MST_CHOICE_CATEGORY);
        TblUploadFile tblUploadFile = new TblUploadFile();
        tblUploadFile.setFileUuid(fileUuid);
        tblCsvImport.setUploadFileUuid(tblUploadFile);
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MST_CHOICE_CATEGORY);
        tblCsvImport.setFunctionId(mstFunction);
        tblCsvImport.setRecordCount(csvInfoSize - 1);
        tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
        tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
        tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
        tblCsvImport.setDeletedCount(0);
        tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
        tblCsvImport.setLogFileUuid(logFileUuid);

        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "category_item_setting");
        tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

        tblCsvImportService.createCsvImpor(tblCsvImport);

    }

    /**
     * CSV取込み処理開始
     *
     * @param loginUser
     * @param csvInfoList
     * @param logFile
     */
    private void csvProcesStart(LoginUser loginUser, ArrayList csvInfoList, String logFile, Set<DispSeqDupKey> duplicated) {
        
        FileUtil fileUtil = new FileUtil();
        Map<String,String>  dbCategoryData= getCategoryDBData();
        Map<String, String> csvHeader = getDictValues(loginUser.getLangId());
        Map<String, String> csvCheckMsg = getCsvInfoCheckMsg(loginUser.getLangId());
        Map<String, MstChoice> mapMstChoice;
        Map<MstChoicePrime.Pk, MstChoicePrime> dbChoices = mstChoiceService.getAll();
        Map<String, MstLanguage> langs = getLanguages();
        Set<String> editableCtg = getEditableCategories();
        for (int i = 1; i < csvInfoList.size(); i++) {
            // CATEGORY,言語,種別,分類項目,上位分類項目,連番,区分値
            
            ArrayList comList = (ArrayList) csvInfoList.get(i);
            mapMstChoice = new HashMap<>();
            // ①入力データチェック
            MstChoice mstChoice = checkCsvInfo(comList, logFile, i, fileUtil, csvHeader, csvCheckMsg, dbCategoryData, mapMstChoice, langs, editableCtg);
            if(mstChoice != null && mstChoice.getMstChoicePK() != null){
                DispSeqDupKey dispSeqKey = new DispSeqDupKey(mstChoice.getMstChoicePK().getCategory(), mstChoice.getDisplaySeq().toString());
                if(!mstChoice.isError() && duplicated.contains(dispSeqKey)) {
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvCheckMsg.get("rowNumber"), i, csvHeader.get("displaySeq"), dispSeqKey.displaySeq, csvCheckMsg.get("error"), 1, csvCheckMsg.get("errorDetail"), csvCheckMsg.get("msgErrorDuplicate")));
                    failedCount = failedCount + 1;
                    mstChoice.setIsError(true);
                }
                if (!mstChoice.isError()) {
                    MstChoicePK mstChoicePK;
                    for (MstChoice saveChoice : mapMstChoice.values()) {
                        // mapping to mstChoice
                        mstChoicePK = saveChoice.getMstChoicePK();
                        MstChoicePrime choice = dbChoices.get(new MstChoicePrime.Pk(mstChoicePK));
                        String msg = mstChoicePK.getCategory() + " : " + mstChoicePK.getLangId() + " : " + mstChoicePK.getSeq();
                        String lang = String.valueOf(comList.get(1));
                        MstLanguage mstLanguage = langs.get(lang);
                        String langId = mstLanguage.getId();
                        if (choice != null) {
                            if(langId.equals(choice.getLangId())){
                                if(isMainLangChanged(mstChoice, choice)) {
                                    // ②更新
                                    choice.setChoice(mstChoice.getChoice());
                                    choice.setDisplaySeq(mstChoice.getDisplaySeq());
                                    choice.setDeleteFlg(mstChoice.getDeleteFlg());
                                    choice.setParentSeq(mstChoice.getParentSeq().replace("@",","));
                                    choice.setUpdateDate(new Date());
                                    choice.setUpdateUserUuid(loginUser.getUserUuid());
                                }
                                updatedCount = updatedCount + 1;
                                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvCheckMsg.get("rowNumber"), i, "CATEGORY", msg, csvCheckMsg.get("error"), 0, csvCheckMsg.get("dbProcess"), csvCheckMsg.get("msgDataModified")));
                            } else if(isSubLangChanged(mstChoice, choice)) {
                                choice.setDisplaySeq(mstChoice.getDisplaySeq());
                                choice.setDeleteFlg(mstChoice.getDeleteFlg());
                                choice.setUpdateDate(new Date());
                                choice.setUpdateUserUuid(loginUser.getUserUuid());
                            }
                        } else {
                            if(langId.equals(mstChoicePK.getLangId())){
                                // ③追加
                                //DB操作情報をログファイルに記入
                                mstChoiceService.createMstChoice(mstChoicePK, mstChoice.getChoice(), mstChoice.getParentSeq().replace("@",","), loginUser, mstChoice.getDisplaySeq(), mstChoice.getDeleteFlg());
                                addedCount = addedCount + 1;
                                //DB操作情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvCheckMsg.get("rowNumber"), i, "CATEGORY", msg, csvCheckMsg.get("error"), 0, csvCheckMsg.get("dbProcess"), csvCheckMsg.get("msgRecordAdded")));
                            } else {
                                String parentSeq = "";
                                if (StringUtils.isNotEmpty(saveChoice.getParentSeq())) {
                                    parentSeq = saveChoice.getParentSeq().replace("@", ",");
                                }
                                String emptyChoice = "";
                                mstChoiceService.createMstChoice(mstChoicePK, emptyChoice, parentSeq, loginUser, mstChoice.getDisplaySeq(), mstChoice.getDeleteFlg());
                            }
                        }
                    }
                }
            }
        }
    }
    
    private boolean isMainLangChanged(MstChoice imprted, MstChoicePrime regstered) {
        return !(
            imprted.getDeleteFlg().equals(regstered.getDeleteFlg()) && 
            imprted.getDisplaySeq().equals(regstered.getDisplaySeq()) &&
            imprted.getChoice().equals(regstered.getChoice()) &&
            imprted.getParentSeq().equals(regstered.getParentSeq())
        );
    }
    
    private boolean isSubLangChanged(MstChoice imprted, MstChoicePrime regstered) {
        return !(
            imprted.getDeleteFlg().equals(regstered.getDeleteFlg()) && 
            imprted.getDisplaySeq().equals(regstered.getDisplaySeq())
        );
    }
    
    private Set<DispSeqDupKey> duplicateImportDataCheck(ArrayList<ArrayList<String>> csvDataList){
        Set<DispSeqDupKey> duplicate = new HashSet<>();
        Set<DispSeqDupKey> checked = new HashSet<>();
        for(ArrayList<String> csvdata : csvDataList){
            if(csvdata.get(7) == null || (csvdata.get(7) != null && csvdata.get(7).equals("0"))){
                DispSeqDupKey key = new DispSeqDupKey (csvdata.get(0).toString(), csvdata.get(6).toString());
                if(checked.contains(key)) {
                    duplicate.add(key);
                } else {
                    checked.add(key);
                }
            }
        }
        return duplicate;
    }
    
    private Map<String, MstLanguage> getLanguages() {
        Map<String, MstLanguage> ret = new HashMap<>();
        List<MstLanguage> langs = mstLanguageService.getMstLanguages().getMstLanguages();
        for(MstLanguage lang : langs) {
            ret.put(lang.getLang(), lang);
        }
        return ret;
    }
    
    private Set<String> getEditableCategories() {
        return entityManager.createNamedQuery("MstChoiceCategory.findByCanAddDelete", MstChoiceCategory.class)
            .setParameter("canAddDelete", "1")
            .getResultList().stream().map(cat -> cat.getCategory()).collect(Collectors.toSet());
    }
 
    /**
     * CSV中身 単純チェック
     *
     * @param logFile return ADD Or UPDATE
     */
    private MstChoice checkCsvInfo(ArrayList csvClmList, String logFile, int index, FileUtil fileUtil, Map<String, String> csvHeader, Map<String, String> csvInfoMsg, Map<String, String> dbCategoryData, Map<String, MstChoice> mapMstChoice, Map<String, MstLanguage> langs, Set<String> editableCtg) {
        MstChoice mstChoice = new MstChoice();
        MstChoice mstChoiceLangTwo = new MstChoice();
        MstChoice mstChoiceLangThree = new MstChoice();
        String langOne = null;
        String langTwo = null;
        String langThree = null;
        if (csvClmList.size() != 10) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, "CATEGORY", "", csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorWrongCsvLayout")));
            failedCount = failedCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        }

        String category = (null == csvClmList.get(0) ? "" : String.valueOf(csvClmList.get(0)));
        if (StringUtils.isEmpty(category)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, "CATEGORY", category, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotNull")));
            failedCount = failedCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        } else if (!editableCtg.contains(category)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, "CATEGORY", category, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("mstErrorRecordNotFound")));
            failedCount = failedCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;           
        }

        String lang = (null == csvClmList.get(1) ? "" : String.valueOf(csvClmList.get(1)));
        if (StringUtils.isEmpty(lang)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("lang"), lang, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotNull")));
            failedCount = failedCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        }
        
        if (!langs.containsKey(lang)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("lang"), lang, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("mstErrorRecordNotFound")));
            failedCount = failedCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        }

        String seq = (null == csvClmList.get(5) ? "" : String.valueOf(csvClmList.get(5)));        
        if (StringUtils.isBlank(seq)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("id"), seq, csvInfoMsg.get("error"), 0, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgRecordSkipped")));
            blankCount = blankCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        } else if (fileUtil.maxLangthCheck(seq, 11)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("id"), seq, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            failedCount = failedCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        } else {
            try {
                Integer.parseInt(seq);
            } catch (NumberFormatException e) {
                //エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("id"), seq, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
                failedCount = failedCount + 1;
                mstChoice.setIsError(true);
                return mstChoice;
            }
        }       
       
        String choice = (null == csvClmList.get(8) ? "" : String.valueOf(csvClmList.get(8)));
        if (fileUtil.maxLangthCheck(choice, 100)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("categoryValue"), choice, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            failedCount = failedCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        }
        
        String displaySeq = (null == csvClmList.get(6) ? "" : String.valueOf(csvClmList.get(6)));
        if (StringUtils.isBlank(displaySeq)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("displaySeq"), displaySeq, csvInfoMsg.get("error"), 0, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgRecordSkipped")));
            blankCount = blankCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        } else if (fileUtil.maxLangthCheck(displaySeq, 11)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("displaySeq"), displaySeq, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            failedCount = failedCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        } else {
            try {
                Integer.parseInt(displaySeq);
            } catch (NumberFormatException e) {
                //エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("displaySeq"), displaySeq, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
                failedCount = failedCount + 1;
                mstChoice.setIsError(true);
                return mstChoice;
            }
        }  
        
        String deleteFlg = (null == csvClmList.get(7) ? "" : String.valueOf(csvClmList.get(7)));
        if (StringUtils.isBlank(deleteFlg)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("deleteFlg"), deleteFlg, csvInfoMsg.get("error"), 0, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgRecordSkipped")));
            blankCount = blankCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        } else if (fileUtil.maxLangthCheck(deleteFlg, 11)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("deleteFlg"), deleteFlg, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            failedCount = failedCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        } else {
            try {
                Integer.parseInt(displaySeq);
            } catch (NumberFormatException e) {
                //エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("deleteFlg"), deleteFlg, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
                failedCount = failedCount + 1;
                mstChoice.setIsError(true);
                return mstChoice;
            }
        }
        
        String parentSeq = (null == csvClmList.get(9) ? "" : String.valueOf(csvClmList.get(9)));
        String parentSeqInt [] = parentSeq.split("@|,");   
        if(dbCategoryData.containsKey(category)){
            for (int i = 0; i < parentSeqInt.length; i++) {
                try{
                    Integer.parseInt(parentSeqInt[i]);
                }
                catch(NumberFormatException e){
                    if(parentSeqInt[i].equals("")){
                        //ignore
                    }else{
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("parentSeq"), parentSeq, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
                        mstChoice.setIsError(true);
                        return mstChoice;
                    }
                }
            }
            if(parentSeq.equals(",")){
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("parentSeq"), parentSeq, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
                mstChoice.setIsError(true);
                return mstChoice;
            }
            else if(parentSeq.equals("@")){
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("parentSeq"), parentSeq, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
                mstChoice.setIsError(true);
                return mstChoice;
            }
            else if (fileUtil.maxLangthCheck(parentSeq, 500))
            {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("parentSeq"), parentSeq, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
                failedCount = failedCount + 1;
                mstChoice.setIsError(true);
                return mstChoice;
            }           
        }
        MstLanguage mstLanguage = langs.get(lang);
        String selectedLang = mstLanguage.getId();
        if("ja".equals(selectedLang)) {
                langOne = "ja";
                langTwo = "en";
                langThree = "zh";
            } else if("zh".equals(selectedLang)) {
                langOne = "zh";
                langTwo = "en";
                langThree = "ja";
            } else {
                langOne = "en";
                langTwo = "ja";
                langThree = "zh";
        }
        MstChoicePK mstChoicePK = new MstChoicePK();
        mstChoicePK.setCategory(category);
        mstChoicePK.setLangId(mstLanguage.getId());
        mstChoicePK.setSeq(seq);
        mstChoice.setChoice(choice);
        mstChoice.setDisplaySeq(Integer.parseInt(displaySeq));
        mstChoice.setDeleteFlg(Integer.parseInt(deleteFlg));
        mstChoice.setParentSeq(parentSeq);
        mstChoice.setMstChoicePK(mstChoicePK);
        mstChoice.setIsError(false);
        mapMstChoice.put(langOne, mstChoice);
        
        MstChoicePK mstChoicePKLangTwo = new MstChoicePK();
        mstChoicePKLangTwo.setCategory(category);
        mstChoicePKLangTwo.setLangId(langTwo);
        mstChoicePKLangTwo.setSeq(seq);
        mstChoiceLangTwo.setMstChoicePK(mstChoicePKLangTwo);
        mstChoiceLangTwo.setDisplaySeq(Integer.parseInt(displaySeq));
        mstChoiceLangTwo.setDeleteFlg(Integer.parseInt(deleteFlg));
        mapMstChoice.put(langTwo, mstChoiceLangTwo);
        
        MstChoicePK mstChoicePKLangThree = new MstChoicePK();
        mstChoicePKLangThree.setCategory(category);
        mstChoicePKLangThree.setLangId(langThree);
        mstChoicePKLangThree.setSeq(seq);
        mstChoiceLangThree.setMstChoicePK(mstChoicePKLangThree);
        mstChoiceLangThree.setDisplaySeq(Integer.parseInt(displaySeq));
        mstChoiceLangThree.setDeleteFlg(Integer.parseInt(deleteFlg));
        mapMstChoice.put(langThree, mstChoiceLangThree);
        return mstChoice;

    }

    
    private Map<String,String>getCategoryDBData(){
        Map<String,String> categoryData = new HashMap<>();    
        StringBuilder sql;
        sql= new StringBuilder("SELECT cc.category,cc.parentCategory FROM MstChoiceCategory cc WHERE cc.parentCategory is not null AND cc.parentCategory != '' ");     
        sql.append(" order by cc.seq asc ");
        Query queryData = entityManager.createQuery(sql.toString());
        List dbDataList = queryData.getResultList();
        Object[] singleDbData;
        for (int i = 0; i < dbDataList.size(); i++) {
            singleDbData = (Object[]) dbDataList.get(i);
            categoryData.put( String.valueOf(singleDbData[0]), String.valueOf(singleDbData[1]));
        }
        return categoryData;
    }

    /**
     * カテゴリ存在チェック && 編集可能であるかどうか
     *
     * @return
     */
    private boolean categoryExistCheck(String category) {
        Query query = entityManager.createNamedQuery("MstChoiceCategory.findByCategory");
        query.setParameter("category", category);
        return query.getResultList().size() > 0;
    }

    /**
     * カテゴリリスト取得
     *
     * @return
     */
    private List<Object> getcategoryList(String category, List allcategoryList) {
        Object[] returnValue;
        List categorylist = new ArrayList();
        boolean flg = false;
        for (int i = 0; i < allcategoryList.size(); i++) {
            returnValue = (Object[]) allcategoryList.get(i);
            if (category.equals(returnValue[0])) {
                categorylist.add(returnValue);
                flg = true;
            }else{
                if(flg){
                    break;
                }
            }
        }
        return categorylist;
    }

    /**
     * Excel出力
     *
     * @param loginUser
     * @return
     */
    public FileReponse exportAll(LoginUser loginUser) {
        FileReponse file = new FileReponse();
        try {
            WriteExcelList we = new WriteListExcel();

            String uuid = IDGenerator.generate();
            String outExclePath = FileUtil.outExcelFile(kartePropertyService, uuid);

            /**
             * Header
             */
            Map<String, String> headerMap = getDictionaryList(loginUser.getLangId());

            Map<String, Object> param = new HashMap();
            param.put("outFilePath", outExclePath);// パスを設定
            param.put("workbook", new XSSFWorkbook());
            param.put("isConvertWorkbook", false);
            param.put("sheetName", headerMap.get("category_item_setting"));

            // CSV出力項目取得用　SQL文
            StringBuilder sql;
            sql = new StringBuilder("");
            sql.append("SELECT cc.category, dt.dictValue tabName, d.dictValue categoryName, dp.dictValue parentCategory, choice.mstChoicePK.seq, choice.choice, '', '', choice.parentSeq, choice.displaySeq, choice.deleteFlg ");
            sql.append(" FROM MstChoiceCategory cc  ");
            sql.append(" left join MstDictionary dt on cc.tableId = dt.mstDictionaryPK.dictKey and dt.mstDictionaryPK.langId = :langId ");
            sql.append(" left join MstDictionary d on cc.categoryNameDictKey = d.mstDictionaryPK.dictKey and d.mstDictionaryPK.langId = :langId ");
            sql.append(" left join MstChoiceCategory cp on cc.parentCategory = cp.category ");
            sql.append(" left join MstDictionary dp on cp.categoryNameDictKey = dp.mstDictionaryPK.dictKey and dp.mstDictionaryPK.langId = :langId ");
            sql.append(" left join MstChoice choice on choice.mstChoicePK.category = cc.category and choice.mstChoicePK.langId = :lang");
            sql.append(" where 1=1 and cc.canAddDelete = 1 ");
            sql.append(" order by cc.seq asc ");
            Query query = entityManager.createQuery(sql.toString());
            query.setParameter("langId", loginUser.getLangId());
            query.setParameter("lang", "en");
            List enlist = query.getResultList();
            query = entityManager.createQuery(sql.toString());
            query.setParameter("langId", loginUser.getLangId());
            query.setParameter("lang", "ja");
            List jaList = query.getResultList();
            query = entityManager.createQuery(sql.toString());
            query.setParameter("langId", loginUser.getLangId());
            query.setParameter("lang", "zh");
            List zhlist = query.getResultList();
            MstLanguage defaultLang = mstLanguageService.getDefaultLanguage();
            String langOne = null;
            String langTwo = null;
            String langThree = null;
            List mainList = null;
            List secondList = null;
            List thirdList = null;
            if("ja".equals(defaultLang.getId())) {
                mainList = jaList;
                secondList = enlist;
                thirdList = zhlist;
                langOne = "ja";
                langTwo = "en";
                langThree = "zh";
            } else if("zh".equals(defaultLang.getId())) {
                mainList = zhlist;
                secondList = enlist;
                thirdList = jaList;
                langOne = "zh";
                langTwo = "en";
                langThree = "ja";
            } else {
                mainList = enlist;
                secondList = jaList;
                thirdList = zhlist;
                langOne = "en";
                langTwo = "ja";
                langThree = "zh";
            }
                    
            List<Object> categorylist = new ArrayList();
            Object[] returnValue;
            Object[] mainValue;
            List<Object> allList = new ArrayList();
            for (int i = 0; i < mainList.size(); i++) {
                returnValue = (Object[]) mainList.get(i);
                List<Object> categoryAllList = new ArrayList();
                if (!categorylist.contains(returnValue[0])) {
                    String category = (String)returnValue[0];
                    categorylist.add(returnValue[0]);
                    List<Object> categoryEnlist = new ArrayList();
                    List<Object> categoryJalist = new ArrayList();
                    List<Object> categoryZhlist = new ArrayList();
                    categoryJalist = getcategoryList(category, mainList);
                    categoryEnlist = getcategoryList(category, secondList);
                    categoryZhlist = getcategoryList(category, thirdList);
                    if (categoryJalist.size() >= categoryEnlist.size()) {
                        for (int e = 0; e < categoryEnlist.size(); e++) {
                            returnValue = (Object[]) categoryEnlist.get(e);
                            String str = getString(returnValue[0]) + getString(returnValue[4]);
                            for (int j = 0; j < categoryJalist.size(); j++) {
                                mainValue = (Object[]) categoryJalist.get(j);
                                String oldStr = getString(mainValue[0]) + getString(mainValue[4]);
                                if (str.equals(oldStr)) {
                                    mainValue[6] = returnValue[5];
                                    break;
                                }
                            }
                        }
                        categoryAllList = categoryJalist;
                    } else {
                        for (int e = 0; e < categoryEnlist.size(); e++) {
                            mainValue = (Object[]) categoryEnlist.get(e);
                            mainValue[6] = mainValue[5];
                            mainValue[5] = "";
                        }
                        for (int j = 0; j < categoryJalist.size(); j++) {
                            returnValue = (Object[]) categoryJalist.get(j);
                            String str = getString(returnValue[0]) + getString(returnValue[4]);
                            for (int e = 0; e < categoryEnlist.size(); e++) {
                                mainValue = (Object[]) categoryEnlist.get(e);
                                String oldStr = getString(mainValue[0]) + getString(mainValue[4]);
                                if (str.equals(oldStr)) {
                                    mainValue[5] = returnValue[5];
                                    break;
                                }
                            }
                        }
                        categoryAllList = categoryEnlist;
                    }

                    if (categoryAllList.size() >= categoryZhlist.size()) {
                        for (int z = 0; z < categoryZhlist.size(); z++) {
                            returnValue = (Object[]) categoryZhlist.get(z);
                            String str = getString(returnValue[0]) + getString(returnValue[4]);
                            for (int m = 0; m < categoryAllList.size(); m++) {
                                mainValue = (Object[]) categoryAllList.get(m);
                                String oldStr = getString(mainValue[0]) + getString(mainValue[4]);
                                if (str.equals(oldStr)) {
                                    mainValue[7] = returnValue[5];
                                    break;
                                }
                            }
                        }
                    } else {
                        for (int m = 0; m < categoryZhlist.size(); m++) {
                            mainValue = (Object[]) categoryZhlist.get(m);
                            mainValue[7] = mainValue[5];
                            mainValue[5] = "";
                        }
                        for (int m = 0; m < categoryAllList.size(); m++) {
                            returnValue = (Object[]) categoryAllList.get(m);
                            String str = getString(returnValue[0]) + getString(returnValue[4]);
                            for (int z = 0; z < categoryZhlist.size(); z++) {
                                mainValue = (Object[]) categoryZhlist.get(z);
                                String oldStr = getString(mainValue[0]) + getString(mainValue[4]);
                                if (str.equals(oldStr)) {
                                    mainValue[6] = returnValue[6];
                                    mainValue[5] = returnValue[5];
                                    break;
                                }
                            }
                        }
                        categoryAllList = categoryZhlist;
                    }
                    allList.addAll(categoryAllList);
                }
            }

            List outList = new ArrayList();
            // HeaderSet
            CategoryExcelOutPut categoryExcelOutPut = new CategoryExcelOutPut();
            categoryExcelOutPut.setCategory("CATEGORY");
            categoryExcelOutPut.setCategoryType(headerMap.get("category_type"));
            categoryExcelOutPut.setCategoryItem(headerMap.get("category_item"));
            categoryExcelOutPut.setParentCategoryItem(headerMap.get("parent_category_item"));
            categoryExcelOutPut.setCategorySeq(headerMap.get("id"));
            categoryExcelOutPut.setCategoryValueJa(headerMap.get("category_value") + "_" + langOne);
            categoryExcelOutPut.setCategoryValueEn(headerMap.get("category_value") + "_" + langTwo);
            categoryExcelOutPut.setCategoryValueZh(headerMap.get("category_value") + "_" + langThree);
            categoryExcelOutPut.setParentCategoryValue(headerMap.get("parent_category_value"));
            categoryExcelOutPut.setDisplaySeq(headerMap.get("display_seq"));
            categoryExcelOutPut.setDeleteFlg(headerMap.get("delete_flg"));
            outList.add(categoryExcelOutPut);

            for (int i = 0; i < allList.size(); i++) {
                returnValue = (Object[]) allList.get(i);
                categoryExcelOutPut = new CategoryExcelOutPut();
                categoryExcelOutPut.setCategory(getString(returnValue[0]));  // CATEGORY
                categoryExcelOutPut.setCategoryType(getString(returnValue[1]));  // 種別
                categoryExcelOutPut.setCategoryItem(getString(returnValue[2]));  // 分類項目
                categoryExcelOutPut.setParentCategoryItem(getString(returnValue[3]));  // 上位分類項目
                categoryExcelOutPut.setCategorySeq(getString(returnValue[4]));  // 連番
                categoryExcelOutPut.setCategoryValueJa(getString(returnValue[5]));  // 区分値JA
                categoryExcelOutPut.setCategoryValueEn(getString(returnValue[6]));  // 区分値EN
                categoryExcelOutPut.setCategoryValueZh(getString(returnValue[7]));  // 区分値ZH
                categoryExcelOutPut.setParentCategoryValue(getString(returnValue[8]).replaceAll(",", "@"));
                categoryExcelOutPut.setDisplaySeq(getString(returnValue[9])); //displaySeq
                categoryExcelOutPut.setDeleteFlg(getString(returnValue[10])); //deleteFlg

                outList.add(categoryExcelOutPut);
            }

            we.write(param, outList);

            TblCsvExport tblCsvExport = new TblCsvExport();
            tblCsvExport.setFileUuid(uuid);
            tblCsvExport.setExportTable(CommonConstants.MST_CHOICE_CATEGORY);
            tblCsvExport.setExportDate(new Date());
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId(CommonConstants.FUN_ID_MST_CHOICE_CATEGORY);
            tblCsvExport.setFunctionId(mstFunction);
            tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
            tblCsvExport.setClientFileName(FileUtil.getExcelFileName(headerMap.get("category_item_setting")));
            tblCsvExportService.createTblCsvExport(tblCsvExport);

            // ファイルのUUIDをリターンする
            file.setFileUuid(uuid);
        } catch (Exception e) {
            Logger.getLogger(MstChoiceCategoryService.class.getName()).log(Level.SEVERE, null, e);
            file.setError(true);
            file.setErrorCode(ErrorMessages.E201_APPLICATION);
            file.setErrorMessage(e.getMessage());
        }

        return file;
    }

    private String getString(Object obj) {
        if (obj == null) {
            return "";
        }
        return String.valueOf(obj);
    }

    private Map<String, String> getDictionaryList(String langId) {
        List<String> dictKeyList = Arrays.asList("category_item_setting", "category_type", "category_item", "parent_category_item", "category_seq", "category_value", "parent_category_value",
                "row_number", "msg_error_not_null", "msg_error_over_length", "msg_error_not_isnumber", "error", "error_detail",
                "mst_error_record_not_found", "msg_error_wrong_csv_layout", "db_process", "msg_record_added", "msg_data_modified", "msg_record_skipped", "display_seq", "delete_flg",
                "msg_error_duplicate_value","id"
        );
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        return headerMap;
    }

    /**
     *
     * @param path
     * @param fileUuid
     * @param loginUser
     * @return
     */
    @Transactional
    public ImportResultResponse importAll(String path, String fileUuid, LoginUser loginUser) {

        ImportResultResponse result = new ImportResultResponse();
        try {
            ReadExcel<CategoryExcelOutPut> re = new ReadListExcel<CategoryExcelOutPut>();
            Map<String, Object> param = new HashMap<String, Object>();
            Map<MstChoicePrime.Pk, MstChoicePrime> dbChoices = mstChoiceService.getAll();
            Set<String> editableCtg = getEditableCategories();
            // パスを設定
            Map<String, String> dictMap = getDictionaryList(loginUser.getLangId());
            param.put("inFilePath", path);
            param.put("sheetName", dictMap.get("category_item_setting"));

            List<CategoryExcelOutPut> list = re.read(param, CategoryExcelOutPut.class);
            Set<DispSeqDupKey> duplicated = duplicateImportAllDataCheck(list);
            if(!result.isError()){
                if (list != null && list.size() > 0) {
                    // ログファイルUUID用意
                    String logFileUuid = IDGenerator.generate();
                    Map<String, String> parentCategoryMap = getCategory();
                    String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
                    FileUtil fileUtil = new FileUtil();
                    Map<String, MstChoice> mapMstChoice;
                    boolean isUpdate;
                    MstChoicePK mstChoicePK;
                    int index = 0;
                    for (int i = 0; i < list.size(); i++) {
                        CategoryExcelOutPut categoryExcelOutPut = list.get(i);
                        mapMstChoice = new HashMap<>();
                        // ①入力データチェック
                        index++;
                        MstChoice mstChoice = checkExcelInfo(categoryExcelOutPut, logFile, index, fileUtil, dictMap, mapMstChoice, parentCategoryMap, editableCtg);
                        if(mstChoice != null && mstChoice.getMstChoicePK() != null){
                            DispSeqDupKey dispSeqKey = new DispSeqDupKey(mstChoice.getMstChoicePK().getCategory(), mstChoice.getDisplaySeq().toString());
                            if(!mstChoice.isError() && duplicated.contains(dispSeqKey)) {
                                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), index, dictMap.get("display_seq"), dispSeqKey.displaySeq, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_duplicate_value")));
                                failedCount = failedCount + 1;
                                mstChoice.setIsError(true);
                            }
                            if (!mstChoice.isError()) {
                                // mapping to mstChoice
                                isUpdate = false;
                                for (MstChoice saveChoice : mapMstChoice.values()) {
                                    mstChoicePK = saveChoice.getMstChoicePK();
                                    MstChoicePrime choice = dbChoices.get(new MstChoicePrime.Pk(mstChoicePK));
                                    if (choice != null) {
                                        // ②更新
                                        choice.setChoice(saveChoice.getChoice());
                                        if (StringUtils.isNotEmpty(saveChoice.getParentSeq())) {
                                            choice.setParentSeq(saveChoice.getParentSeq().replace("@", ","));
                                        }
                                        choice.setDisplaySeq(saveChoice.getDisplaySeq());
                                        choice.setDeleteFlg(saveChoice.getDeleteFlg());
                                        choice.setUpdateDate(new Date());
                                        choice.setUpdateUserUuid(loginUser.getUserUuid());
                                        isUpdate = true;
                                    } else {
                                        // ③追加
                                        //DB操作情報をログファイルに記入
                                        String parentSeq = "";
                                        if (StringUtils.isNotEmpty(saveChoice.getParentSeq())) {
                                            parentSeq = saveChoice.getParentSeq().replace("@", ",");
                                        }
                                        mstChoiceService.createMstChoice(mstChoicePK, saveChoice.getChoice(), parentSeq, loginUser, saveChoice.getDisplaySeq(), saveChoice.getDeleteFlg());
                                    }
                                }

                                if (isUpdate) {
                                    updatedCount = updatedCount + 1;
                                    //DB操作情報をログファイルに記入
                                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), index, "CATEGORY", mstChoice.getMstChoicePK().getCategory(), dictMap.get("error"), 0, dictMap.get("db_process"), dictMap.get("msg_data_modified")));
                                } else {
                                    addedCount = addedCount + 1;
                                    //DB操作情報をログファイルに記入
                                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), index, "CATEGORY", mstChoice.getMstChoicePK().getCategory(), dictMap.get("error"), 0, dictMap.get("db_process"), dictMap.get("msg_record_added")));
                                }
                            }
                        }
                    }

                    // リターン情報
                    succeededCount = addedCount + updatedCount;
                    result.setTotalCount(list.size() - blankCount);
                    result.setSucceededCount(succeededCount);
                    result.setAddedCount(addedCount);
                    result.setUpdatedCount(updatedCount);
                    result.setDeletedCount(0);
                    result.setFailedCount(failedCount);
                    result.setLog(logFileUuid);

                    // アップロードログをテーブルに書き出し
                    TblCsvImport tblCsvImport = new TblCsvImport();
                    tblCsvImport.setImportUuid(IDGenerator.generate());
                    tblCsvImport.setImportUserUuid(loginUser.getUserUuid());
                    tblCsvImport.setImportDate(new Date());
                    tblCsvImport.setImportTable(CommonConstants.MST_CHOICE_CATEGORY);
                    TblUploadFile tblUploadFile = new TblUploadFile();
                    tblUploadFile.setFileUuid(fileUuid);
                    tblCsvImport.setUploadFileUuid(tblUploadFile);
                    MstFunction mstFunction = new MstFunction();
                    mstFunction.setId(CommonConstants.FUN_ID_MST_CHOICE_CATEGORY);
                    tblCsvImport.setFunctionId(mstFunction);
                    tblCsvImport.setRecordCount(Integer.valueOf(String.valueOf(list.size() - blankCount)));
                    tblCsvImport.setSuceededCount(Integer.valueOf(String.valueOf(succeededCount)));
                    tblCsvImport.setAddedCount(Integer.valueOf(String.valueOf(addedCount)));
                    tblCsvImport.setUpdatedCount(Integer.valueOf(String.valueOf(updatedCount)));
                    tblCsvImport.setDeletedCount(0);
                    tblCsvImport.setFailedCount(Integer.valueOf(String.valueOf(failedCount)));
                    tblCsvImport.setLogFileUuid(logFileUuid);

                    tblCsvImport.setLogFileName(FileUtil.getLogFileName(dictMap.get("category_item_setting")));

                    tblCsvImportService.createCsvImpor(tblCsvImport);
                } else {
                    result.setError(true);
                    result.setErrorCode(ErrorMessages.E201_APPLICATION);
                    result.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_no_processing_record"));
                    return result;

                }
            }
        } catch (Exception e) {
            Logger.getLogger(MstChoiceCategoryService.class.getName()).log(Level.SEVERE, null, e);
            result.setError(true);
            result.setErrorCode(ErrorMessages.E201_APPLICATION);
            result.setErrorMessage(e.toString());
            return result;
        }
        return result;

    }

    /**
     *
     * @param categoryExcelOutPut
     * @param logFile
     * @param index
     * @param fileUtil
     * @param dictKey
     * @param mapMstChoice
     * @return
     */
    
    private Set<DispSeqDupKey> duplicateImportAllDataCheck(List<CategoryExcelOutPut> csvDataList){
        Set<DispSeqDupKey> duplicate = new HashSet<>();
        Set<DispSeqDupKey> checked = new HashSet<>();
        for(CategoryExcelOutPut csvdata : csvDataList){
            if(csvdata.getDeleteFlg() == null || (csvdata.getDeleteFlg() != null && csvdata.getDeleteFlg().equals("0"))){
            
                DispSeqDupKey key = new DispSeqDupKey (csvdata.getCategory(), csvdata.getDisplaySeq());  
                if(checked.contains(key)) {
                    duplicate.add(key);
                } else {
                    checked.add(key);
                }
            }
        }
        return duplicate;
    }
    
    private static class DispSeqDupKey {
        private final String category;
        private final String displaySeq;
        
        private DispSeqDupKey(String category, String displaySeq) {
            this.category = category;
            this.displaySeq = displaySeq;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 47 * hash + Objects.hashCode(this.category);
            hash = 47 * hash + Objects.hashCode(this.displaySeq);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final DispSeqDupKey other = (DispSeqDupKey) obj;
            if (!Objects.equals(this.category, other.category)) {
                return false;
            }
            if (!Objects.equals(this.displaySeq, other.displaySeq)) {
                return false;
            }
            return true;
        }
        
    }
    
    private MstChoice checkExcelInfo(CategoryExcelOutPut categoryExcelOutPut, String logFile, int index, FileUtil fileUtil,
            Map<String, String> dictKey, Map<String, MstChoice> mapMstChoice, Map<String, String> parentCategoryMap, Set<String> editableCtg) {
        MstLanguage defaultLang = mstLanguageService.getDefaultLanguage();
        String langOne = null;
        String langTwo = null;
        String langThree = null;
        MstChoice mstChoice = new MstChoice();
        MstChoice mstChoiceEn = new MstChoice();
        MstChoice mstChoiceZh = new MstChoice();
        if (StringUtils.isEmpty(categoryExcelOutPut.getCategory())) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictKey.get("row_number"), index, "CATEGORY", "", dictKey.get("error"), 1, dictKey.get("error_detail"), dictKey.get("msg_error_not_null")));
            failedCount = failedCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        } else if (!categoryExistCheck(categoryExcelOutPut.getCategory())) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictKey.get("row_number"), index, "CATEGORY", categoryExcelOutPut.getCategory(), dictKey.get("error"), 1, dictKey.get("error_detail"), dictKey.get("msg_error_record_not_found")));
            failedCount = failedCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        } else if (!editableCtg.contains(categoryExcelOutPut.getCategory())) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictKey.get("row_number"), index, "CATEGORY", categoryExcelOutPut.getCategory(), dictKey.get("error"), 1, dictKey.get("error_detail"), dictKey.get("msg_error_record_not_found")));
            failedCount = failedCount + 1;
            mstChoice.setIsError(true);
            return mstChoice; 
        }
        

        // 連番が空白の行は無視する。エラーにはしない。総件数にもカウントしない。ログにはスキップされた旨、出力する。
        //categorySeq is DB 'seq' value and is represented as field 'ID' in excel.
        String seq = categoryExcelOutPut.getCategorySeq();
        if (StringUtils.isBlank(seq)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictKey.get("row_number"), index, dictKey.get("id"), seq, dictKey.get("error"), 1, dictKey.get("error_detail"), dictKey.get("msg_record_skipped")));
            blankCount = blankCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        } else if (fileUtil.maxLangthCheck(seq, 11)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictKey.get("row_number"), index, dictKey.get("id"), seq, dictKey.get("error"), 1, dictKey.get("error_detail"), dictKey.get("msg_error_over_length")));
            failedCount = failedCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        } else {
            try {
                Integer.parseInt(seq);
            } catch (NumberFormatException e) {
                //エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictKey.get("row_number"), index, dictKey.get("id"), seq, dictKey.get("error"), 1, dictKey.get("error_detail"), dictKey.get("msg_error_not_isnumber")));
                failedCount = failedCount + 1;
                mstChoice.setIsError(true);
                return mstChoice;
            }
        }
        
        String displaySeq = categoryExcelOutPut.getDisplaySeq();
        if (StringUtils.isBlank(displaySeq)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictKey.get("row_number"), index, dictKey.get("display_seq"), displaySeq, dictKey.get("error"), 1, dictKey.get("error_detail"), dictKey.get("msg_record_skipped")));
            blankCount = blankCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        } else if (fileUtil.maxLangthCheck(displaySeq, 11)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictKey.get("row_number"), index, dictKey.get("display_seq"), displaySeq, dictKey.get("error"), 1, dictKey.get("error_detail"), dictKey.get("msg_error_over_length")));
            failedCount = failedCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        } else {
            try {
                Integer.parseInt(displaySeq);
            } catch (NumberFormatException e) {
                //エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictKey.get("row_number"), index, dictKey.get("display_seq"), displaySeq, dictKey.get("error"), 1, dictKey.get("error_detail"), dictKey.get("msg_error_not_isnumber")));
                failedCount = failedCount + 1;
                mstChoice.setIsError(true);
                return mstChoice;
            }
        }

        String deleteFlg = categoryExcelOutPut.getDeleteFlg();
        if (StringUtils.isBlank(deleteFlg)) {
            //エラー情報をログファイルに記入
            deleteFlg = "0";
//            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictKey.get("row_number"), index, dictKey.get("delete_flg"), deleteFlg, dictKey.get("error"), 1, dictKey.get("error_detail"), dictKey.get("msg_record_skipped")));
//            blankCount = blankCount + 1;
//            mstChoice.setIsError(true);
//            return mstChoice;
        } else if (fileUtil.maxLangthCheck(deleteFlg, 11)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictKey.get("row_number"), index, dictKey.get("delete_flg"), deleteFlg, dictKey.get("error"), 1, dictKey.get("error_detail"), dictKey.get("msg_error_over_length")));
            failedCount = failedCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        } else {
            try {
                Integer.parseInt(deleteFlg);
            } catch (NumberFormatException e) {
                //エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictKey.get("row_number"), index, dictKey.get("delete_flg"), deleteFlg, dictKey.get("error"), 1, dictKey.get("error_detail"), dictKey.get("msg_error_not_isnumber")));
                failedCount = failedCount + 1;
                mstChoice.setIsError(true);
                return mstChoice;
            }
        }
        
        String choiceJa = getString(categoryExcelOutPut.getCategoryValueJa());
        if (fileUtil.maxLangthCheck(choiceJa, 100)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictKey.get("row_number"), index, dictKey.get("category_value") + "_ja", choiceJa, dictKey.get("error"), 1, dictKey.get("error_detail"), dictKey.get("msg_error_over_length")));
            failedCount = failedCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        }

        String choiceEn = getString(categoryExcelOutPut.getCategoryValueEn());
        if (fileUtil.maxLangthCheck(choiceEn, 100)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictKey.get("row_number"), index, dictKey.get("category_value") + "_en", choiceEn, dictKey.get("error"), 1, dictKey.get("error_detail"), dictKey.get("msg_error_over_length")));
            failedCount = failedCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        }

        String choiceZh = getString(categoryExcelOutPut.getCategoryValueZh());
        if (fileUtil.maxLangthCheck(choiceZh, 100)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictKey.get("row_number"), index, dictKey.get("category_value") + "_zh", choiceZh, dictKey.get("error"), 1, dictKey.get("error_detail"), dictKey.get("msg_error_over_length")));
            failedCount = failedCount + 1;
            mstChoice.setIsError(true);
            return mstChoice;
        }

        String parentItem = getString(categoryExcelOutPut.getParentCategoryItem());//上位分類項目
        String parentSeq = getString(categoryExcelOutPut.getParentCategoryValue());//上位区分値
        // 上位分類項目のない分類項目については、上位区分値のセルに値が記載されても無視する。
        if (StringUtils.isNotEmpty(parentItem) && StringUtils.isNotEmpty(parentSeq)) {
            if (parentCategoryMap.containsKey(categoryExcelOutPut.getCategory())) {
                String parentSeqInt[] = parentSeq.split("@|,");
                for (int i = 0; i < parentSeqInt.length; i++) {
                    try {
                        Integer.parseInt(parentSeqInt[i]);
                    } catch (NumberFormatException e) {
                        if (parentSeqInt[i].equals("")) {
                            //ignore
                        } else {
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictKey.get("row_number"), index, dictKey.get("parent_seq"), parentSeq, dictKey.get("error"), 1, dictKey.get("error_detail"), dictKey.get("msg_error_not_isnumber")));
                            mstChoice.setIsError(true);
                            return mstChoice;
                        }
                    }
                }
                if (parentSeq.equals(",")) {
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictKey.get("row_number"), index, dictKey.get("parent_seq"), parentSeq, dictKey.get("error"), 1, dictKey.get("error_detail"), dictKey.get("msg_error_not_isnumber")));
                    mstChoice.setIsError(true);
                    return mstChoice;
                } else if (parentSeq.equals("@")) {
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictKey.get("row_number"), index, dictKey.get("parent_seq"), parentSeq, dictKey.get("error"), 1, dictKey.get("error_detail"), dictKey.get("msg_error_not_isnumber")));
                    mstChoice.setIsError(true);
                    return mstChoice;
                } else if (fileUtil.maxLangthCheck(parentSeq, 500)) {
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictKey.get("row_number"), index, dictKey.get("parent_seq"), parentSeq, dictKey.get("error"), 1, dictKey.get("error_detail"), dictKey.get("msg_error_over_length")));
                    failedCount = failedCount + 1;
                    mstChoice.setIsError(true);
                    return mstChoice;
                }
            }
        }
        
        if("ja".equals(defaultLang.getId())) {
            langOne = "ja";
            langTwo = "en";
            langThree = "zh";
        } else if("zh".equals(defaultLang.getId())) {
            langOne = "zh";
            langTwo = "en";
            langThree = "ja";
        } else {
            langOne = "en";
            langTwo = "ja";
            langThree = "zh";
        }
        MstChoicePK mstChoicePK = new MstChoicePK();
        mstChoicePK.setCategory(categoryExcelOutPut.getCategory());
        mstChoicePK.setLangId(langOne);
        mstChoicePK.setSeq(seq);
        mstChoice.setChoice(choiceJa);
        mstChoice.setDisplaySeq(Integer.parseInt(displaySeq));
        mstChoice.setDeleteFlg(Integer.parseInt(deleteFlg));
        mstChoice.setParentSeq(parentSeq);
        mstChoice.setMstChoicePK(mstChoicePK);
        mstChoice.setIsError(false);
        mapMstChoice.put(langOne, mstChoice);

        MstChoicePK mstChoicePKEn = new MstChoicePK();
        mstChoicePKEn.setCategory(categoryExcelOutPut.getCategory());
        mstChoicePKEn.setLangId(langTwo);
        mstChoicePKEn.setSeq(seq);
        mstChoiceEn.setChoice(choiceEn);
        mstChoiceEn.setDisplaySeq(Integer.parseInt(displaySeq));
        mstChoiceEn.setDeleteFlg(Integer.parseInt(deleteFlg));
        mstChoiceEn.setMstChoicePK(mstChoicePKEn);
        mapMstChoice.put(langTwo, mstChoiceEn);

        MstChoicePK mstChoicePKZh = new MstChoicePK();
        mstChoicePKZh.setCategory(categoryExcelOutPut.getCategory());
        mstChoicePKZh.setLangId(langThree);
        mstChoicePKZh.setSeq(seq);
        mstChoiceZh.setChoice(choiceZh);
        mstChoiceZh.setDisplaySeq(Integer.parseInt(displaySeq));
        mstChoiceZh.setDeleteFlg(Integer.parseInt(deleteFlg));
        mstChoiceZh.setMstChoicePK(mstChoicePKZh);
        mapMstChoice.put(langThree, mstChoiceZh);

        return mstChoice;
    }

    private Map<String, String> getCategory() {
        Map<String, String> categoryMap = new HashMap<>();
        StringBuilder sql;
        sql = new StringBuilder("SELECT cc.category,cc.parentCategory FROM MstChoiceCategory cc WHERE cc.parentCategory is not null AND cc.parentCategory != '' AND cc.canAddDelete = 1 ");
        sql.append(" order by cc.seq asc ");
        Query query = entityManager.createQuery(sql.toString());
        List list = query.getResultList();
        Object[] obj;
        for (int i = 0; i < list.size(); i++) {
            obj = (Object[]) list.get(i);
            categoryMap.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
        }
        return categoryMap;
    }

}
