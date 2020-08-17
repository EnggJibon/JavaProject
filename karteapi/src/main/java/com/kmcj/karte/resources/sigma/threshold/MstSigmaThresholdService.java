/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.threshold;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.component.MstComponents;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.MstMachineList;
import com.kmcj.karte.resources.machine.MstMachineService;
import com.kmcj.karte.resources.machine.filedef.MstMachineFileDef;
import com.kmcj.karte.resources.machine.filedef.MstMachineFileDefService;
import com.kmcj.karte.resources.machine.filedef.MstMachineFileDefVo;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.NumberUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;


/**
 *
 * @author jiangxs
 */
@Dependent
public class MstSigmaThresholdService {
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private KartePropertyService kartePropertyService;
    
    @Inject
    private TblCsvExportService tblCsvExportService;
    
    @Inject
    private MstMachineService mstMachineService;
    
    @Inject
    private MstComponentService mstComponentService;
    
    @Inject
    private MstMachineFileDefService mstMachineFileDefService;
    
    @Inject
    private TblCsvImportService tblCsvImportService;
    
    private Map<String, String> headerMap;

    long succeededCount = 0;
    long addedCount = 0;
    long updatedCount = 0;
    long failedCount = 0;
    long deletedCount = 0;
    boolean checkError = false;

    /**
     * M1103 設備ログ閾値設定
     * データ取得
     * @param machineId
     * @param componentId
     * @return 
     */
    public MstSigmaThresholdList getMstMachineThresholds(String machineId,String componentId){
        MstSigmaThresholdList response = new MstSigmaThresholdList();
        List<MstSigmaThresholdVo> mstSigmaThresholdVos = new ArrayList<>();
        MstSigmaThresholdVo mstSigmaThresholdVo;
        StringBuilder sql = new StringBuilder("SELECT m FROM MstSigmaThreshold m JOIN FETCH m.mstComponent mc JOIN FETCH m.mstMachineFileDef m1 ");
        sql.append("JOIN FETCH MstMachine m2 ON m1.machineUuid = m2.uuid WHERE 1=1 ");
        sql.append("AND m2.machineId = :machineId ");
        sql.append("AND m.componentId = :componentId ");
        sql.append("AND m1.dispGraphFlg = 1 ");
        
        Query query = entityManager.createQuery(sql.toString());
        
        query.setParameter("machineId", machineId);
        query.setParameter("componentId", componentId);
        List list = query.getResultList();
        
        for (int i = 0; i < list.size(); i++) {
            MstSigmaThreshold sigmaThreshold = (MstSigmaThreshold) list.get(i);
            mstSigmaThresholdVo = new MstSigmaThresholdVo();

            mstSigmaThresholdVo.setId(sigmaThreshold.getId());
            mstSigmaThresholdVo.setFileDefId(sigmaThreshold.getFileDefId());
            if(sigmaThreshold.getMstMachineFileDef() != null){
                mstSigmaThresholdVo.setHeaderLabel(sigmaThreshold.getMstMachineFileDef().getHeaderLabel());
            }else{
                mstSigmaThresholdVo.setHeaderLabel("");
            }
            
            if(sigmaThreshold.getMaxVal() != null){
                mstSigmaThresholdVo.setMaxVal(String.valueOf(sigmaThreshold.getMaxVal()));
            }else{
                mstSigmaThresholdVo.setMaxVal("");
            }
            
            if(sigmaThreshold.getMinVal() != null){
                mstSigmaThresholdVo.setMinVal(String.valueOf(sigmaThreshold.getMinVal()));
            }else{
                mstSigmaThresholdVo.setMinVal("");
            }
            
            
            mstSigmaThresholdVos.add(mstSigmaThresholdVo);
        }
        response.setMstSigmaThresholdVos(mstSigmaThresholdVos);
        
        return response;
    }
    
    
    
    /**
     * M1103 設備ログ閾値設定
     * データ取得
     * @param machineId
     * @param loginUser
     * @return 
     */
    public MstSigmaThresholdList getMstMachineThreshold(String machineId,LoginUser loginUser){
        MstSigmaThresholdList response = new MstSigmaThresholdList();
        List<MstMachineFileDefVo> mstMachineFileDefVo = new ArrayList<>();
        MstMachineFileDefVo mstFileDefVo;
        
        List<MstComponents> mstComponentVo = new ArrayList<>();
        MstComponents mstComponents;
        
        StringBuilder sql = new StringBuilder("SELECT m1 FROM MstMachineFileDef m1 ");
        sql.append("JOIN FETCH MstMachine m2 ON m1.machineUuid = m2.uuid WHERE 1=1 ");
        sql.append("AND m2.machineId = :machineId ");
        sql.append("AND m1.dispGraphFlg = 1 ");
        
        Query query = entityManager.createQuery(sql.toString());
        
        query.setParameter("machineId", machineId);
        List list = query.getResultList();
        
        for (int i = 0; i < list.size(); i++) {
            MstMachineFileDef mstMachineFileDef = (MstMachineFileDef) list.get(i);
            mstFileDefVo = new MstMachineFileDefVo();
            mstFileDefVo.setId(mstMachineFileDef.getId());

            mstFileDefVo.setHeaderLabel(mstMachineFileDef.getHeaderLabel());
            mstFileDefVo.setMaxVal(String.valueOf(mstMachineFileDef.getMaxVal()));
            mstFileDefVo.setMinVal(String.valueOf(mstMachineFileDef.getMinVal()));

            mstMachineFileDefVo.add(mstFileDefVo);
        }
        
        response.setMstMachineFileDefVo(mstMachineFileDefVo);
        
        String id = "";
        for(MstMachineFileDefVo machineFileDefVo : mstMachineFileDefVo){
            id = id + "'"+machineFileDefVo.getId()+"',";
        }
        
        if (id != null && !"".equals(id)) {
            id = id.substring(0, id.length() - 1);

            StringBuilder newSql = new StringBuilder("SELECT DISTINCT m.componentId,mc.componentCode,mc.componentName FROM MstSigmaThreshold m LEFT JOIN FETCH m.mstComponent mc ");
            newSql.append("WHERE m.fileDefId in (" + id + ") ORDER BY mc.componentCode");

            Query querys = entityManager.createQuery(newSql.toString());

            List lists = querys.getResultList();

            for (int i = 0; i < lists.size(); i++) {
                Object[] objs = (Object[]) lists.get(i);
                mstComponents = new MstComponents();
                mstComponents.setId(String.valueOf(objs[0]));
                mstComponents.setComponentCode(String.valueOf(objs[1]));
                mstComponents.setComponentName(String.valueOf(objs[2]));
                mstComponentVo.add(mstComponents);
            }

            response.setMstComponent(mstComponentVo);
        }else{
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
            return response;
        }


        return response;
    }
    
    /**
     * M1103 設備ログ閾値設定
     * 画面で入力された値を用いて設備ログ閾値設定テーブルへ追加・更新を行う。
     * @param mstSigmaThresholdList
     * @param loginUser
     * @return 
     */
    @Transactional
    public BasicResponse postMstMachineThreshold(MstSigmaThresholdList mstSigmaThresholdList,LoginUser loginUser){
        BasicResponse response = new BasicResponse();
        List<MstSigmaThresholdVo> sigmaThresholdVos = mstSigmaThresholdList.getMstSigmaThresholdVos();
        for(int i=0; i<sigmaThresholdVos.size(); i++){
            MstSigmaThresholdVo mstSigmaThresholdVo = sigmaThresholdVos.get(i);

            if ("1".equals(mstSigmaThresholdVo.getOperationFlag())) {
                //削除
                if (mstSigmaThresholdVo.getComponentId() != null && !"".equals(mstSigmaThresholdVo.getComponentId())) {
                    StringBuilder deleteSql = new StringBuilder("DELETE FROM MstSigmaThreshold m WHERE m.componentId =:componentId ");
                    Query query = entityManager.createQuery(deleteSql.toString());
                    query.setParameter("componentId", mstSigmaThresholdVo.getComponentId());
                    query.executeUpdate();
                    response.setError(false);
                    response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
                } 
                
            } else if ("3".equals(mstSigmaThresholdVo.getOperationFlag())) {

                //データチェック
                if (!checkMachineThresholdData(mstSigmaThresholdVo)) {
                    response.setError(true);
                    response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_no_processing_record"));
                    return response;
                }

                //初始データを削除
                StringBuilder deleteSql = new StringBuilder("DELETE FROM MstSigmaThreshold m WHERE m.componentId =:componentId AND m.fileDefId =:fileDefId");
                Query query = entityManager.createQuery(deleteSql.toString());
                query.setParameter("componentId", mstSigmaThresholdVo.getComponentId());
                query.setParameter("fileDefId", mstSigmaThresholdVo.getFileDefId());
                query.executeUpdate();
                this.deletedCount ++;

                //追加
                MstSigmaThreshold inSigmaThreshold = new MstSigmaThreshold();
                inSigmaThreshold.setId(IDGenerator.generate());
                inSigmaThreshold.setFileDefId(mstSigmaThresholdVo.getFileDefId());
                inSigmaThreshold.setComponentId(mstSigmaThresholdVo.getComponentId());
                if (mstSigmaThresholdVo.getMaxVal() != null && !"".equals(mstSigmaThresholdVo.getMaxVal())) {
                    inSigmaThreshold.setMaxVal(new BigDecimal(mstSigmaThresholdVo.getMaxVal()));
                } else {
                    inSigmaThreshold.setMaxVal(null);
                }
                if (mstSigmaThresholdVo.getMinVal() != null && !"".equals(mstSigmaThresholdVo.getMinVal())) {
                    inSigmaThreshold.setMinVal(new BigDecimal(mstSigmaThresholdVo.getMinVal()));
                } else {
                    inSigmaThreshold.setMinVal(null);
                }
                inSigmaThreshold.setCreateDate(new Date());
                inSigmaThreshold.setCreateUserUuid(loginUser.getUserUuid());
                inSigmaThreshold.setUpdateDate(new Date());
                inSigmaThreshold.setUpdateUserUuid(loginUser.getUserUuid());
                entityManager.persist(inSigmaThreshold);
                response.setError(false);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_added"));
                this.addedCount ++;
            }
        }
        return response;
    }
    
    @Transactional
    public BasicResponse importMstMachineThreshold(MstSigmaThresholdList mstSigmaThresholdList,LoginUser loginUser, String logFile, FileUtil fileUtil, Map<String, String> csvHeader, Map<String, String> csvInfoMsg){
        BasicResponse response = new BasicResponse();
        List<MstSigmaThresholdVo> sigmaThresholdVos = mstSigmaThresholdList.getMstSigmaThresholdVos();
        for(int i=0; i<sigmaThresholdVos.size(); i++){
            MstSigmaThresholdVo mstSigmaThresholdVo = sigmaThresholdVos.get(i);

            if ("1".equals(mstSigmaThresholdVo.getOperationFlag())) {
                //削除
                if (mstSigmaThresholdVo.getComponentId() != null && !"".equals(mstSigmaThresholdVo.getComponentId())) {
                    String deleteSql = "DELETE FROM MstSigmaThreshold m WHERE m.componentId =:componentId";
                    Query query = entityManager.createQuery(deleteSql);
                    query.setParameter("componentId", mstSigmaThresholdVo.getComponentId());
                    query.executeUpdate();
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), i, csvHeader.get("componentCode"), mstSigmaThresholdVo.getComponentId(), csvInfoMsg.get("error"), 0, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgRecordDeleted")));
                } 
                
            } else if ("3".equals(mstSigmaThresholdVo.getOperationFlag())) {

                //データチェック
                if (!checkMachineThresholdData(mstSigmaThresholdVo)) {
                    response.setError(true);
                    response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_no_processing_record"));
                    return response;
                }

                String mstSigmaThresholdSql = "SELECT t FROM MstSigmaThreshold t WHERE t.componentId =:componentId AND t.fileDefId =:fileDefId";
                
                Query query = entityManager.createQuery(mstSigmaThresholdSql);
                query.setParameter("componentId", mstSigmaThresholdVo.getComponentId());
                query.setParameter("fileDefId", mstSigmaThresholdVo.getFileDefId());
                
                MstSigmaThreshold mstSigmaThreshold = new MstSigmaThreshold();
                Boolean isUpdate = true;
                try {
                    mstSigmaThreshold = (MstSigmaThreshold) query.getSingleResult();
                } catch (Exception e) {
                    isUpdate = false;
                    mstSigmaThreshold.setId(IDGenerator.generate());
                    mstSigmaThreshold.setCreateDate(new Date());
                    mstSigmaThreshold.setCreateUserUuid(loginUser.getUserUuid());
                }

                mstSigmaThreshold.setFileDefId(mstSigmaThresholdVo.getFileDefId());
                mstSigmaThreshold.setComponentId(mstSigmaThresholdVo.getComponentId());
                if (mstSigmaThresholdVo.getMaxVal() != null && !"".equals(mstSigmaThresholdVo.getMaxVal())) {
                    mstSigmaThreshold.setMaxVal(new BigDecimal(mstSigmaThresholdVo.getMaxVal()));
                } else {
                    mstSigmaThreshold.setMaxVal(null);
                }
                if (mstSigmaThresholdVo.getMinVal() != null && !"".equals(mstSigmaThresholdVo.getMinVal())) {
                    mstSigmaThreshold.setMinVal(new BigDecimal(mstSigmaThresholdVo.getMinVal()));
                } else {
                    mstSigmaThreshold.setMinVal(null);
                }
                
                mstSigmaThreshold.setUpdateDate(new Date());
                mstSigmaThreshold.setUpdateUserUuid(loginUser.getUserUuid());
                if (isUpdate) {
                    entityManager.merge(mstSigmaThreshold);
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), i, csvHeader.get("componentCode"), mstSigmaThresholdVo.getComponentId(), csvInfoMsg.get("error"), 0, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgRecordUpdated")));
                    this.updatedCount ++;
                } else {
                    entityManager.persist(mstSigmaThreshold);
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), i, csvHeader.get("componentCode"), mstSigmaThresholdVo.getComponentId(), csvInfoMsg.get("error"), 0, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgRecordAdded")));
                    this.addedCount ++;
                }
            }
        }
        return response;
    }
    
    
    /**
     * データチェック
     * @param componentId
     * @param fileDefId
     * @return 
     */
    public boolean checkMachineThreshold(String componentId,String fileDefId){
        StringBuilder sql = new StringBuilder("SELECT m FROM MstSigmaThreshold m WHERE m.componentId =:componentId And m.fileDefId =:fileDefId ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("componentId", componentId);
        query.setParameter("fileDefId", fileDefId);
        try{
            query.getSingleResult();
        }catch(NoResultException e){
            return false;
        }
        return true;
    }
    
    
    /**
     * データチェック
     * @param mstSigmaThresholdVo
     * @return 
     */
    public boolean checkMachineThresholdData(MstSigmaThresholdVo mstSigmaThresholdVo){
        
        FileUtil fu = new FileUtil();
        
        String fileDefId = mstSigmaThresholdVo.getFileDefId();
        String componentId = mstSigmaThresholdVo.getComponentId();
        
//        String maxVal = mstSigmaThresholdVo.getMaxVal();
//        String minVal = mstSigmaThresholdVo.getMinVal();
        
        if(fileDefId == null || "".equals(fileDefId)){
            return false;
        }else if(fu.maxLangthCheck(fileDefId, 45)){
            return false;
        }
        
        if(componentId == null || "".equals(componentId)){
            return false;
        }else if(fu.maxLangthCheck(componentId, 45)){
            return false;
        }
        
//        if(!fu.isDouble(maxVal)){
//            return false;
//        }
//        
//        if(!fu.isDouble(minVal)){
//            return false;
//        }
        
        return true;
    }
    /**
     *
     * @param machineId
     * @param loginUser
     * @return
     */
    public FileReponse getMstSigmaThresholdOutputExcel(String machineId, LoginUser loginUser) {
        String langId = loginUser.getLangId();
        List<String> dictKeyList = Arrays.asList("mst_sigma_threshold_setting", "component_code", "sigma_header_label", "minimum_value", "maximum_value");
        headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        FileReponse response = new FileReponse();
        String sheetName = headerMap.get("mst_sigma_threshold_setting");
        List<String> excelOutHeadList = new ArrayList<>();
        excelOutHeadList.add(headerMap.get("component_code"));
        excelOutHeadList.add(headerMap.get("sigma_header_label"));
        excelOutHeadList.add(headerMap.get("minimum_value"));
        excelOutHeadList.add(headerMap.get("maximum_value"));
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        Row row = sheet.createRow(0);
        for(int i = 0; i < excelOutHeadList.size(); i++){
            Cell cell = row.createCell(i);
            String val = excelOutHeadList.get(i);
            cell.setCellValue(val);
        }
        ArrayList<ArrayList> excelData = new ArrayList<>();
        MstSigmaThresholdList mstSigmaThresholdList = this.getMstMachineThreshold(machineId, loginUser);
        ArrayList lineList;
        if(mstSigmaThresholdList.getMstComponent()!= null && mstSigmaThresholdList.getMstComponent().size()!=0){
            for(int i=0; i<mstSigmaThresholdList.getMstComponent().size();i++){
            MstComponents mstComponents = mstSigmaThresholdList.getMstComponent().get(i);
            MstSigmaThresholdList mstSigmaThresholdMCList = this.getMstMachineThresholds(machineId, mstComponents.getId());        
            if(mstSigmaThresholdMCList.getMstSigmaThresholdVos() != null){
                for(int j=0; j<mstSigmaThresholdList.getMstMachineFileDefVo().size();j++){
                    MstMachineFileDefVo mstMachineFileDefVo = mstSigmaThresholdList.getMstMachineFileDefVo().get(j);
                    MstSigmaThresholdVo mstSigmaThresholdVo = mstSigmaThresholdMCList.getMstSigmaThresholdVos().stream().filter(x -> x.getHeaderLabel().equals(mstMachineFileDefVo.getHeaderLabel())).findFirst().orElse(null);
                    lineList = new ArrayList();
                    if(mstSigmaThresholdVo != null){
                       
                        lineList.add( mstComponents.getComponentCode());
                        lineList.add( mstMachineFileDefVo.getHeaderLabel());
                        lineList.add( mstSigmaThresholdVo.getMinVal() != null ? mstSigmaThresholdVo.getMinVal(): "");
                        lineList.add( mstSigmaThresholdVo.getMaxVal() != null? mstSigmaThresholdVo.getMaxVal(): "");
                    }
                    else
                    {
                        lineList.add( mstComponents.getComponentCode());
                        lineList.add( mstMachineFileDefVo.getHeaderLabel());
                        lineList.add( mstMachineFileDefVo.getMinVal() != null? mstMachineFileDefVo.getMinVal(): "");
                        lineList.add( mstMachineFileDefVo.getMaxVal() != null? mstMachineFileDefVo.getMaxVal(): "");
                    }
                    excelData.add(lineList);
                }
            }
          }
        }else{
            if(mstSigmaThresholdList.getMstMachineFileDefVo()!=null){
                for(int j=0; j<mstSigmaThresholdList.getMstMachineFileDefVo().size();j++){
                    MstMachineFileDefVo mstMachineFileDefVo = mstSigmaThresholdList.getMstMachineFileDefVo().get(j);
                    lineList = new ArrayList();
                    lineList.add("");
                    lineList.add( mstMachineFileDefVo.getHeaderLabel());
                    lineList.add( mstMachineFileDefVo.getMinVal());
                    lineList.add( mstMachineFileDefVo.getMaxVal());
                    excelData.add(lineList);
                }
            }
        }
        for(int d = 0; d < excelData.size(); d++){
            Row dataRow = sheet.createRow(d+1);
            for(int i = 0; i < excelData.get(d).size(); i++){
                Cell cell = dataRow.createCell(i);
                if(excelData.get(d).get(i)!=null){
                   String val = excelData.get(d).get(i).toString();
                   cell.setCellValue(val); 
                }else{
                    cell.setCellValue("");
                }
            }
        }
        String uuid = IDGenerator.generate();
        try{String outExcelPath = FileUtil.outExcelFile(kartePropertyService, uuid);
            FileOutputStream fout=new FileOutputStream(outExcelPath);
            workbook.write(fout);    
            workbook.close();
            System.out.println("success...");    
        }catch(Exception e){System.out.println(e);}
        FileUtil fu = new FileUtil();
        TblCsvExport tblExcelExport = new TblCsvExport();
        tblExcelExport.setFileUuid(uuid);
        tblExcelExport.setExportTable(CommonConstants.MST_SIGMA_THRESHOLD_SETTING);
        tblExcelExport.setExportDate(new java.util.Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MST_SIGMA_THRESHOLD_SETTING);
        tblExcelExport.setFunctionId(mstFunction);
        tblExcelExport.setExportUserUuid(loginUser.getUserUuid());
        tblExcelExport.setClientFileName(fu.getExcelFileName(headerMap.get("mst_sigma_threshold_setting")));
        tblCsvExportService.createTblCsvExport(tblExcelExport);
        response.setFileUuid(uuid);
        return response;    
    }
    /**
     * 
     * @param fileUuid
     * @param machineId
     * @param loginUser
     * @return 
     */
    @Transactional
    public ImportResultResponse postSigmaThresholdExcel(String fileUuid, String machineId, LoginUser loginUser){
        ImportResultResponse importResultResponse = new ImportResultResponse();
        String excelFile = FileUtil.getExcelFilePath(kartePropertyService, fileUuid);
        if (!excelFile.endsWith(CommonConstants.EXCEL)) {
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_invalid_file_layout");
            importResultResponse.setErrorMessage(msg);
            return importResultResponse;
        }
        ArrayList<ArrayList> excelData = new ArrayList<>();
        try
        {
            FileInputStream file = new FileInputStream(new File(excelFile));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
        
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            // validate number of column of the imported file is correct
            int numberOfColumn = 4;
            Row headerRow = sheet.getRow(0);
            int colCount = headerRow.getLastCellNum();
            if(colCount != numberOfColumn) {
                file.close();
                importResultResponse.setError(true);
                importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_invalid_file_layout");
                importResultResponse.setErrorMessage(msg);
                return importResultResponse;
            }

            
            DataFormatter formatter = new DataFormatter();

        //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext())
            {
            Row r = rowIterator.next();
            if (r != null) {
                ArrayList lineList = new ArrayList();
                for (int cn = 0; cn < 4; cn++) {
                Cell c = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                    if (c == null) {
                        lineList.add("");
                    } else {
                        String cellTextValue = formatter.formatCellValue(c);
                        lineList.add(cellTextValue);
                    }
                }
                excelData.add(lineList);
            }
        }
            file.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        int csvInfoSize = excelData.size();
        if (csvInfoSize <= 1) {
            return importResultResponse;
        } else {
            String logFileUuid = IDGenerator.generate();
            String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
            importResultResponse = excelProcesStart(loginUser, machineId, excelData, logFile);
            succeededCount = addedCount + updatedCount + deletedCount;
            csvProcessExit(loginUser, fileUuid, csvInfoSize, logFileUuid);
         
            importResultResponse.setTotalCount(csvInfoSize - 1);
            importResultResponse.setSucceededCount(succeededCount);
            importResultResponse.setAddedCount(addedCount);
            importResultResponse.setUpdatedCount(updatedCount);
            importResultResponse.setDeletedCount(deletedCount);
            importResultResponse.setFailedCount(failedCount);
            importResultResponse.setLog(logFileUuid);
            return importResultResponse;
        }
    }
    /**
     * 
     * @param loginUser
     * @param csvInfoList
     * @param logFile
     */
    private ImportResultResponse excelProcesStart(LoginUser loginUser, String machineId, ArrayList csvInfoList, String logFile) {
        ImportResultResponse importResultResponse = new ImportResultResponse();
        checkError = false;
        FileUtil fileUtil = new FileUtil();
        MstSigmaThresholdList mstSigmaThresholdList = new MstSigmaThresholdList();
        List<MstSigmaThresholdVo> sigmaThresholdVos = new ArrayList();
        Map<String, String> excelHeader = getDictValues(loginUser.getLangId());
        Map<String, String> excelCheckMsg = getExcelInfoCheckMsg(loginUser.getLangId());
        for (int i = 1; i < csvInfoList.size(); i++) {
                ArrayList comList = (ArrayList) csvInfoList.get(i);
                sigmaThresholdVos.addAll(checkExcelInfo(machineId, loginUser, comList, logFile, i, fileUtil, excelHeader, excelCheckMsg));      
        }
        if(checkError == false){      
            mstSigmaThresholdList.setMstSigmaThresholdVos(sigmaThresholdVos);
            BasicResponse postResult = this.importMstMachineThreshold(mstSigmaThresholdList, loginUser, logFile, fileUtil, excelHeader, excelCheckMsg);
            importResultResponse.setError(postResult.isError());
            importResultResponse.setErrorCode(postResult.getErrorCode());
            importResultResponse.setErrorMessage(postResult.getErrorMessage());
        }
        else{
           importResultResponse.setError(true);
           importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
           String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_field_check");
           importResultResponse.setErrorMessage(msg);
        }
            
        return importResultResponse;
    } 
     /**
     * CSV
     * @param loginUser
     * @param fileUuid
     * @param csvInfoSize
     * @param logFileUuid
     */
    private void csvProcessExit(LoginUser loginUser, String fileUuid, int csvInfoSize, String logFileUuid) {
        //
        TblCsvImport tblCsvImport = new TblCsvImport();
        tblCsvImport.setImportUuid(IDGenerator.generate());
        tblCsvImport.setImportUserUuid(loginUser.getUserUuid());
        tblCsvImport.setImportDate(new Date());
        tblCsvImport.setImportTable(CommonConstants.MST_SIGMA_THRESHOLD_SETTING);
        TblUploadFile tblUploadFile = new TblUploadFile();
        tblUploadFile.setFileUuid(fileUuid);
        tblCsvImport.setUploadFileUuid(tblUploadFile);
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MST_SIGMA_THRESHOLD_SETTING);
        tblCsvImport.setFunctionId(mstFunction);
        tblCsvImport.setRecordCount(csvInfoSize - 1);
        tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
        tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
        tblCsvImport.setLogFileUuid(logFileUuid);

        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.MST_SIGMA_THRESHOLD_SETTING);
        tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

        tblCsvImportService.createCsvImpor(tblCsvImport);
    }
    /**
     *
     * @param langId
     * @return
     */
    private Map<String, String> getDictValues(String langId) {

        Map<String, String> dictMap = new HashMap<>();
        dictMap.put("componentCode", mstDictionaryService.getDictionaryValue(langId, "component_code"));
        dictMap.put("headerLabel", mstDictionaryService.getDictionaryValue(langId, "sigma_header_label"));
        dictMap.put("minVal", mstDictionaryService.getDictionaryValue(langId, "minimum_value"));
        dictMap.put("maxVal", mstDictionaryService.getDictionaryValue(langId, "maximum_value"));
        return dictMap;
    }
    private Map<String, String> getExcelInfoCheckMsg(String langId) {
        Map<String, String> msgMap = new HashMap<>();
        // info
        msgMap.put("rowNumber", mstDictionaryService.getDictionaryValue(langId, "row_number"));
        // error msg
        msgMap.put("msgErrorNotNull", mstDictionaryService.getDictionaryValue(langId, "msg_error_not_null"));
        msgMap.put("msgErrorOverLength", mstDictionaryService.getDictionaryValue(langId, "msg_error_over_length"));
        msgMap.put("msgErrorNotIsnumber", mstDictionaryService.getDictionaryValue(langId, "msg_error_not_isnumber"));
        msgMap.put("error", mstDictionaryService.getDictionaryValue(langId, "error"));
        msgMap.put("errorDetail", mstDictionaryService.getDictionaryValue(langId, "error_detail"));
        msgMap.put("msgErrorValueInvalid", mstDictionaryService.getDictionaryValue(langId, "msg_error_value_invalid"));
        msgMap.put("mstErrorRecordNotFound", mstDictionaryService.getDictionaryValue(langId, "mst_error_record_not_found"));
        msgMap.put("msgErrorInvalidFileLayout", mstDictionaryService.getDictionaryValue(langId, "msg_error_invalid_file_layout"));
        msgMap.put("msgErrorThresholdValueOrder", mstDictionaryService.getDictionaryValue(langId, "msg_error_threshold_value_order"));
        msgMap.put("msgErrorInputOneOrZero", mstDictionaryService.getDictionaryValue(langId, "msg_error_input_one_or_zero"));
        // inform msg
        msgMap.put("msgRecordAdded", mstDictionaryService.getDictionaryValue(langId, "msg_record_added"));
        msgMap.put("msgRecordUpdated", mstDictionaryService.getDictionaryValue(langId, "msg_record_updated"));
        msgMap.put("msgRecordDeleted", mstDictionaryService.getDictionaryValue(langId, "msg_record_deleted"));
        return msgMap;
    }
    /**
     * @param logMap
     * @param lineCsv
     * @param userLangId
     * @param logFile
     * @param index
     * @return 
     */
    private List<MstSigmaThresholdVo> checkExcelInfo(String machineId, LoginUser loginUser, ArrayList lineCsv, String logFile, int index, FileUtil fileUtil, Map<String, String> csvHeader, Map<String, String> csvInfoMsg) {
        boolean checkBlankorNull = false;
        boolean checkErrorDetail = false;  
        List<MstSigmaThresholdVo> mstSigmaThresholdVos = new ArrayList();
        MstSigmaThresholdVo mstSigmaThresholdVo = new MstSigmaThresholdVo();
        int arrayLength = lineCsv.size();
        
        MstMachineList MstMachineList = mstMachineService.getMstMachineDetailByMachineId(machineId);
        MstMachine mstMachine = MstMachineList.getMstMachines().get(0);
        
        if (arrayLength != 4) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("headerLabel"), "", csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorInvalidFileLayout")));
            checkErrorDetail = true;
        }
        String strComponentCode = (null == lineCsv.get(0) ? "" : String.valueOf(lineCsv.get(0)).trim());
        if (fileUtil.maxLangthCheck(strComponentCode, 45)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("componentCode"), strComponentCode, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            checkErrorDetail = true;
        }else{
            if(strComponentCode.equals("") == true){
                checkBlankorNull = true;
            }
           if(!mstComponentService.getMstComponentExistCheck(strComponentCode)){
               fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("componentCode"), strComponentCode, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("mstErrorRecordNotFound")));
               checkErrorDetail = true;
           }
        }
        String strHeaderLabel = (null == lineCsv.get(1) ? "" : String.valueOf(lineCsv.get(1)).trim());
        if (fileUtil.maxLangthCheck(strHeaderLabel, 100)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("headerLabel"), strHeaderLabel, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            checkErrorDetail = true;
        }else{
            if(strHeaderLabel.equals("") == true){
                checkBlankorNull = true;
            }
            if(!mstMachineFileDefService.getHeaderLabelExistCheck(strHeaderLabel, mstMachine.getUuid())){
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("headerLabel"), strHeaderLabel, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("mstErrorRecordNotFound")));
                checkErrorDetail = true;
            }
        }
        String strMinVal = (null == lineCsv.get(2) ? "" : String.valueOf(lineCsv.get(2)).trim());
        BigDecimal minValue = null;
        if(strMinVal.equals("") == false){
            try {
                minValue = new BigDecimal(strMinVal);
            } catch(NumberFormatException e) {
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("minVal"), strMinVal, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
                checkErrorDetail = true;
            }
            if (!checkErrorDetail) {
                if (!NumberUtil.validateDecimal(strMinVal, 18, 2)) {
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("minVal"), strMinVal, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
                    checkErrorDetail = true;
                } else {
                    minValue = new BigDecimal(strMinVal);
                }
            }
        }else{
            checkBlankorNull = true;
        }
        String strMaxVal = (null == lineCsv.get(3) ? "" : String.valueOf(lineCsv.get(3)).trim());
        BigDecimal maxValue = null;
            if(strMaxVal.equals("") == false){
                try {
                    maxValue = new BigDecimal(strMaxVal);
                } catch(NumberFormatException e) {
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("maxVal"), strMaxVal, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
                    checkErrorDetail = true;
                }
                if (!checkErrorDetail) {
                    if (!NumberUtil.validateDecimal(strMaxVal, 18, 2)) {
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("maxVal"), strMaxVal, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
                        checkErrorDetail = true;
                    } else {
                        maxValue = new BigDecimal(strMaxVal);
                    }
                }
            }else{
                checkBlankorNull = true;
            }
        if (maxValue != null && minValue != null && maxValue.compareTo(minValue) < 0){
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("maxVal"), strMaxVal, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorThresholdValueOrder")));
            checkErrorDetail = true;
        }
        if(checkErrorDetail == true ){
            checkError = true;
            this.failedCount++;
        }
        
        if(checkBlankorNull == false){
       
        
        MstComponent mstComponent = mstComponentService.getMstComponentByCode(strComponentCode);
        // find machineFileDefList with condition  (where): machineUUID, headerLabel, Disp_Flag = 1
        List<MstMachineFileDef> mstMachineFileDefList = mstMachineFileDefService.getMstMachineFileDefByMachineUuid(mstMachine.getUuid(), strHeaderLabel);
        
        for(MstMachineFileDef mstMachineFileDef : mstMachineFileDefList){
            if(mstComponent != null){
                mstSigmaThresholdVo.setComponentId(mstComponent.getId());
                mstSigmaThresholdVo.setComponentCode(strComponentCode);
                mstSigmaThresholdVo.setHeaderLabel(strHeaderLabel);
                mstSigmaThresholdVo.setMstMachineFileDef(mstMachineFileDef);
                mstSigmaThresholdVo.setFileDefId(mstMachineFileDef.getId());
                mstSigmaThresholdVo.setMinVal(strMinVal);
                mstSigmaThresholdVo.setMaxVal(strMaxVal);
                mstSigmaThresholdVo.setOperationFlag("3");
                mstSigmaThresholdVos.add(mstSigmaThresholdVo);
            }  
          }
        } 
        return mstSigmaThresholdVos;
    }
}
