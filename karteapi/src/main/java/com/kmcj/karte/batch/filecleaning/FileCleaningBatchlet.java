/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.filecleaning;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.util.FileUtil;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author kmc
 */
@Named
@Dependent
public class FileCleaningBatchlet extends AbstractBatchlet {

    //public static String BATCH_PROPERTY_PROCESS_MODE = "processMode";
    //public final static String BATCH_NAME = "fileCleaningBatchlet";

    /**
     * ログ
     */
    private Logger logger = Logger.getLogger(FileCleaningBatchlet.class.getName());
    private Level info = Level.FINE;
    private Level severe = Level.SEVERE;

    @Inject
    JobContext jobContext;    
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private KartePropertyService kartePropertyService;

    @Override
    public String process(){
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(info, "  ---> [[{0}]] Start", methodName);
        
        StringBuilder csvFilePath;
        StringBuilder workFilePath;
        StringBuilder excelFilePath;

        try{
            // CSVフォルダ内のファイル削除
            csvFilePath = new StringBuilder(kartePropertyService.getDocumentDirectory());
            csvFilePath.append(FileUtil.SEPARATOR).append(CommonConstants.CSV);
            String csvFilePathString = csvFilePath.toString();
            File dirCsvPath = new File(csvFilePathString);
            File[] dirCsvPathList = dirCsvPath.listFiles();
            if (dirCsvPathList.length > 0){
                deleteFiles(dirCsvPathList);
            }
            
            // Workフォルダ内のファイル削除
            workFilePath = new StringBuilder(kartePropertyService.getDocumentDirectory());
            workFilePath.append(FileUtil.SEPARATOR).append(CommonConstants.WORK);
            String workFilePathString = workFilePath.toString();
            File dirWorkPath = new File(workFilePathString);
            File[] dirWorkPathList = dirWorkPath.listFiles();
            if (dirWorkPathList.length > 0){
                deleteFiles(dirWorkPathList);
            }
            
            //Excelフォルダ内のファイル削除
            excelFilePath =  new StringBuilder(kartePropertyService.getDocumentDirectory());
            excelFilePath.append(FileUtil.SEPARATOR).append(CommonConstants.EXCEL_FILE);
            File dirExcelPath = new File(excelFilePath.toString());
            File[] dirExcelPathList = dirExcelPath.listFiles();
            if (dirExcelPathList.length > 0){
                deleteFiles(dirExcelPathList);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(severe, "  < Error >{0}", e.getMessage());
        }        
        
        return "SUCCESS";
    }
    
    private void deleteFiles(File[] dirPathList){
        if (dirPathList == null) return;
        File[] dirList = dirPathList;
        for (File chkFile : dirList){
            // ディレクトリ（フォルダ）の場合の処理
            if (chkFile.isDirectory()){
                deleteFiles(chkFile.listFiles());
//                File[] chkFileList = chkFile.listFiles();
//                if (chkFileList.length == 0) {
//                    chkFile.delete();
//                }
            // ファイルの場合の処理
            } else if (chkFile.isFile()){
                Long lastUpdatedDay = chkFile.lastModified();
                Date lastUpdatedDate = new Date(lastUpdatedDay);
                Calendar lastUpdatedCal = Calendar.getInstance();
                lastUpdatedCal.setTime(lastUpdatedDate);
                lastUpdatedCal.add(lastUpdatedCal.DAY_OF_MONTH, 1);
                Calendar nowCal = Calendar.getInstance();
                if (nowCal.compareTo(lastUpdatedCal) > 0 ){
                    chkFile.delete();
                }
            }
        }
    }
}

