/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.operationlog;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.operation.MstOperation;
import com.kmcj.karte.resources.operation.MstOperationDef;
import com.kmcj.karte.resources.operation.MstOperationList;
import com.kmcj.karte.resources.operation.MstOperationService;
import com.kmcj.karte.resources.operation.log.TblOperationLog;
import com.kmcj.karte.resources.operation.log.TblOperationLogPK;
import com.kmcj.karte.resources.operation.log.TblOperationLogService;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.AbstractBatchlet;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author kmc
 */
@Named
@Dependent
public class OperationLogBatchlet extends AbstractBatchlet {

    /**
     * ログ
     */
    private Logger logger = Logger.getLogger(OperationLogBatchlet.class.getName());
    private Level info = Level.FINE;
    private Level fine = Level.FINE;
    private Level severe = Level.SEVERE;
    private Level warning = Level.WARNING;

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblOperationLogService tblOperationLogService;

    @Inject
    private MstOperationService mstOperationService;

    @Override
    public String process(){
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.INFO, "  ---> [[{0}]] Start", methodName);
        
        StringBuilder filePath;
        String filePathString = null;

        try{
            // 対象操作ログファイルのパス設定            
            filePath = new StringBuilder(kartePropertyService.getDocumentDirectory());
            filePath.append(FileUtil.SEPARATOR).append(CommonConstants.OPELOG);
            int fpLength = filePath.length();
            File dirPath = new File(filePath.toString());
            String[] dirLists = dirPath.list();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR, -1);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHH");
            int cnt = 0;
            
            // 対象操作ログファイルのリストが取得できた場合にDictKey取得用データの取得
            HashMap<String, MstOperationDef> MstOperationMapPhp = new HashMap();
            HashMap<String, MstOperationDef> MstOperationMapApi = new HashMap();
            if (dirLists.length > 0){
                MstOperationList mstOperationList = new MstOperationList();
                mstOperationList = mstOperationService.getMstOperationAll();
                MstOperationDef mstOperationDef = new MstOperationDef();
                List<MstOperationDef> mstOperationDefList = new ArrayList();
                for (MstOperation mstOoerationListSingle : mstOperationList.getMstOperationList()){
                    String opeProc = mstOoerationListSingle.getOperationProc();
                    String opePath = mstOoerationListSingle.getOperationPath();
                    if(opeProc.equals("php")){
                        HashMap<String, String> DictMapDef = new HashMap();
                        if(MstOperationMapPhp.get(opePath) == null){
                            mstOperationDef.setOperationPathDef(opePath);
                            if(mstOoerationListSingle.getOperationParm() != null && !mstOoerationListSingle.getOperationParm().equals("")){
                                mstOperationDef.setHasParamDef(true);
                                mstOperationDef.setParamNameDef(mstOoerationListSingle.getOperationParm());
                                DictMapDef.put(mstOoerationListSingle.getParmValue(), mstOoerationListSingle.getDictKey());
                            } else {
                                mstOperationDef.setHasParamDef(false);
                                DictMapDef.put("0", mstOoerationListSingle.getDictKey());
                            }
                        } else {
                            DictMapDef = MstOperationMapPhp.get(opePath).getDictMapDef();
                            if(MstOperationMapPhp.get(opePath).getHasParamDef()){
                                DictMapDef.put(mstOoerationListSingle.getParmValue(), mstOoerationListSingle.getDictKey());
                            } else {
                                DictMapDef.put("0", mstOoerationListSingle.getDictKey());
                            }
                        }
                        mstOperationDef.setDictMapDef(DictMapDef);
                        mstOperationDefList.add(new MstOperationDef(mstOperationDef));
                        MstOperationMapPhp.put(opePath, mstOperationDefList.get(cnt));
                    } else if(opeProc.equals("api")) {
                        HashMap<String, String> DictMapDef = new HashMap();
                        if(MstOperationMapApi.get(opePath) == null){
                            mstOperationDef.setOperationPathDef(opePath);
                            if(mstOoerationListSingle.getOperationParm() != null && !mstOoerationListSingle.getOperationParm().equals("")){
                                mstOperationDef.setHasParamDef(true);
                                mstOperationDef.setParamNameDef(mstOoerationListSingle.getOperationParm());
                                DictMapDef.put(mstOoerationListSingle.getParmValue(), mstOoerationListSingle.getDictKey());
                            } else {
                                mstOperationDef.setHasParamDef(false);
                                DictMapDef.put("0", mstOoerationListSingle.getDictKey());
                            }
                        } else {
                            DictMapDef = MstOperationMapApi.get(opePath).getDictMapDef();
                            if(MstOperationMapApi.get(opePath).getHasParamDef()){
                                DictMapDef.put(mstOoerationListSingle.getParmValue(), mstOoerationListSingle.getDictKey());
                            } else {
                                DictMapDef.put("0", mstOoerationListSingle.getDictKey());
                            }
                        }
                        mstOperationDef.setDictMapDef(DictMapDef);
                        mstOperationDefList.add(new MstOperationDef(mstOperationDef));
                        MstOperationMapApi.put(opePath, mstOperationDefList.get(cnt));
                    }
                    cnt++;
                }
            }

            for (String dirList : dirLists) {
                int index = dirList.indexOf('.');
                if (index != -1) {
                    String fileName = dirList.substring(0, index);
                    String[] fileNameDate = fileName.split("_");
                    if (Integer.parseInt(fileNameDate[2]) <= Integer.parseInt(sdf1.format(cal.getTime()))){
                        filePath.delete(fpLength, filePath.length());
                        filePathString = filePath.append(FileUtil.SEPARATOR).append(dirList).toString();

                        BasicResponse response = new BasicResponse();
                        CSVFileUtil csvFileUtil = null;
                        ArrayList readList = new ArrayList();
                        try {
                            csvFileUtil = new CSVFileUtil(filePathString);
                            boolean readEnd = false;
                            do {
                                String readLine = csvFileUtil.readLine();
                                if (readLine == null) {
                                    readEnd = true;
                                } else {
                                    byte[] byteData = readLine.getBytes();
                                    readLine = new String(byteData, "UTF-8");
                                    readList.add(CSVFileUtil.fromCSVLinetoArray(readLine));
                                }
                            } while (!readEnd);
                        } catch (Exception e) {
                            response.setError(true);
                            logger.log(severe, "操作ログファイルの読み込みに失敗しました。(" + filePathString + ")", e.getMessage());
                        } finally {
                            // CSVファイルwriterのクローズ処理
                            if (csvFileUtil != null) {
                                csvFileUtil.close();
                                logger.log(info, "操作ログファイルの読み込みに成功しました。", methodName);
                            }
                        }

                        if (readList.size() >= 1) {
                            FileUtil fu = new FileUtil();
                            for (int i = 0; i < readList.size(); i++) {
                                ArrayList usrList = (ArrayList) readList.get(i);

                                if (usrList.size() > 4) {
                                    //エラー情報をログファイルに記入
                                    logger.log(info, i + "行目のCSVファイルのレイアウトまたは文字コードが正しくありません。文字コードはUTF-8である必要があります。", methodName);
                                    continue;
                                }
                                
                                TblOperationLog tblOperationLog = new TblOperationLog();
                                TblOperationLogPK tblOperationLogPK = new TblOperationLogPK();

                                // ユーザーID
                                String strUserId = String.valueOf(usrList.get(1));
                                if (fu.isNullCheck(strUserId) || strUserId.equals("")) {
                                    logger.log(info, i + "行目のユーザーIDが未入力です。", methodName);
                                    continue;
                                }
                                tblOperationLogPK.setUserId(fu.blankEscape(strUserId.trim()));
                                
                                // 作成日時
                                String strCreateDate = String.valueOf(usrList.get(0));
                                if (strCreateDate.length() < 23){
                                    strCreateDate = strCreateDate + "000";
                                    strCreateDate = strCreateDate.substring(0, 23);
                                }
                                if (fu.isNullCheck(strCreateDate) || strCreateDate.equals("")) {
                                    logger.log(info, i + "行目の作業開始日時が未入力です。", methodName);
                                    continue;
                                }
                                tblOperationLogPK.setCreateDate(DateFormat.strToDateMill(fu.blankEscape(strCreateDate.trim())));
                                if (tblOperationLogPK.getCreateDate() == null){
                                    logger.log(info, i + "行目の作業開始日時が未入力です。", methodName);
                                    continue;
                                }
                                
                                // 操作パス
                                String strOperationPath = String.valueOf(usrList.get(2));
                                if (fu.isNullCheck(strOperationPath) || strOperationPath.equals("")) {
                                    logger.log(info, i + "行目の操作パスが未入力です。", methodName);
                                    continue;
                                }
                                tblOperationLogPK.setOperationPath(fu.blankEscape(strOperationPath.trim()));

                                // プライマリーキーセット
                                tblOperationLog.setTblOperationLogPK(tblOperationLogPK);

                                // データ重複チェック
                                if (!(getTblOperationLogExistCheck(tblOperationLogPK))) {
                                    // 操作パラメーター
                                    String strOperationParm = String.valueOf(usrList.get(3));
                                    if (strOperationParm.length() > 1000) {
                                        strOperationParm = strOperationParm.substring(0, 1000);
                                    }
                                    tblOperationLog.setOperationParm(fu.blankEscape(strOperationParm.trim()));

                                    // 作業処理（php or api）
                                    tblOperationLog.setOperationProc(fu.blankEscape(fileNameDate[1].trim()));
                                    
                                    // 文言キー
                                    // PHPの場合
                                    if(fileNameDate[1].trim().equals("php") && MstOperationMapPhp.size() > 0){
                                        String[] opePathArray = strOperationPath.trim().split("/");
                                        String[] opePhpPathArray = opePathArray[opePathArray.length - 1].trim().split("\\.");
                                        if(MstOperationMapPhp.get(opePhpPathArray[0]) != null){
                                            if(MstOperationMapPhp.get(opePhpPathArray[0]).getHasParamDef()){
                                                HashMap<String,String> opeDictMap = new HashMap();
                                                String[] opeParmArray = strOperationParm.trim().split("&");
                                                for(String opeParmString : opeParmArray){
                                                    String[] opeDictArray = opeParmString.trim().split("=");
                                                    opeDictMap.put(opeDictArray[0], opeDictArray[1]);
                                                }
                                                String dictMapKey = opeDictMap.get(MstOperationMapPhp.get(opePhpPathArray[0]).getParamNameDef());
                                                tblOperationLog.setDictKey(MstOperationMapPhp.get(opePhpPathArray[0]).getDictMapDef().get(dictMapKey));
                                            } else {
                                                tblOperationLog.setDictKey(MstOperationMapPhp.get(opePhpPathArray[0]).getDictMapDef().get("0"));
                                            }
                                        }
                                    }
                                    // APIの場合
                                    if(fileNameDate[1].trim().equals("api") && MstOperationMapApi.size() > 0){
//                                        String[] opePathArray = strOperationPath.trim().split("/");
//                                        if(MstOperationMapApi.get(opePathArray[opePathArray.length - 1]) != null){
//                                            if(MstOperationMapApi.get(opePathArray[opePathArray.length - 1]).getHasParamDef()){
                                        if(MstOperationMapApi.get(strOperationPath) != null){
                                            if(MstOperationMapApi.get(strOperationPath).getHasParamDef()){
                                                HashMap<String,String> opeDictMap = new HashMap();
                                                String[] opeParmArray = strOperationParm.trim().split("&");
                                                for(String opeParmString : opeParmArray){
                                                    String[] opeDictArray = opeParmString.trim().split("=");
                                                    opeDictMap.put(opeDictArray[0], opeDictArray[1]);
                                                }
//                                                String dictMapKey = opeDictMap.get(MstOperationMapApi.get(opePathArray[opePathArray.length - 1]).getParamNameDef());
//                                                tblOperationLog.setDictKey(MstOperationMapApi.get(opePathArray[opePathArray.length - 1]).getDictMapDef().get(dictMapKey));
                                                String dictMapKey = opeDictMap.get(MstOperationMapApi.get(strOperationPath).getParamNameDef());
                                                tblOperationLog.setDictKey(MstOperationMapApi.get(strOperationPath).getDictMapDef().get(dictMapKey));
                                            } else {
//                                                tblOperationLog.setDictKey(MstOperationMapApi.get(opePathArray[opePathArray.length - 1]).getDictMapDef().get("0"));
                                                tblOperationLog.setDictKey(MstOperationMapApi.get(strOperationPath).getDictMapDef().get("0"));
                                            }
                                        }
                                    }
                                    // データ登録
                                    BasicResponse persistResponse = new BasicResponse();
                                    try {
                                        persistResponse = tblOperationLogService.createTblOperationLog(tblOperationLog);
                                    }
                                    catch (Exception e) {
                                    }
                                }
                            }
                        }
                        logger.log(Level.INFO, "  <--- [[{0}]] End", methodName);
                        
                        // ファイルバックアップ
                        File inFilePath = new File(filePathString);
                        try {
                            if (inFilePath.delete()) {
                                logger.log(info, "処理済の操作ログファイルを削除しました。", methodName);
                            } else {
                                logger.log(info, "処理済の操作ログファイルを削除できませんでした。", methodName);
                            }
                        } catch (Exception ex) {
                            logger.log(info, "処理済の操作ログファイルを削除できませんでした。", methodName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(severe, "  < Error >{0}", e.getMessage());
        }
        return "SUCCESS";
    }
    
    /**
     *
     * @param tblOperationLogPK
     * @return
     */
    public boolean getTblOperationLogExistCheck(TblOperationLogPK tblOperationLogPK) {
        Query query = entityManager.createNamedQuery("TblOperationLog.findByPK");
        query.setParameter("userId", tblOperationLogPK.getUserId());
        query.setParameter("createDate", tblOperationLogPK.getCreateDate());
        query.setParameter("operationPath", tblOperationLogPK.getOperationPath());
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }
}
