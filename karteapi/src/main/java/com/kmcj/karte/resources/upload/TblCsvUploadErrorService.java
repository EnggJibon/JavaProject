/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.upload;

import java.util.List;
import java.util.Date;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import javax.transaction.Transactional;
import java.io.File;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.MailSender;
import com.kmcj.karte.resources.files.TblUploadFileService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.user.mail.reception.MstUserMailReceptionService;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.ZipCompressor;
import java.util.Optional;

/**
 *
 * @author admin
 */
@Dependent
public class TblCsvUploadErrorService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    private static final String LANGID = "ja";

    @Inject
    private MailSender mailSender;
    @Inject
    private MstUserMailReceptionService mstUserMailReceptionService;
    @Inject
    private KartePropertyService kartePropertyService;
    @Inject
    private TblUploadFileService tblUploadFileService;
    @Inject
    private MstDictionaryService mstDictionaryService;
    @Inject
    private TblCsvImportService tblCsvImportService;

    private Map<String, String> dictKeyMap;

    /**
     * エラー登録
     *
     * @param tblCsvUploadError
     * @param userUuid
     * @return
     */
    @Transactional
    public BasicResponse createCsvUploadError(TblCsvUploadError tblCsvUploadError, String userUuid) {
        BasicResponse response = new BasicResponse();
        try {
            Date time = new Date();
            String errorId = IDGenerator.generate();
            tblCsvUploadError.setId(errorId);
            tblCsvUploadError.setErrorDatetime(time);
            tblCsvUploadError.setCreateDate(time);
            tblCsvUploadError.setUpdateDate(time);
            tblCsvUploadError.setCreateUserUuid(userUuid);
            tblCsvUploadError.setUpdateUserUuid(userUuid);
            entityManager.persist(tblCsvUploadError);
        } catch (Exception e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(e.getMessage());
        }

        return response;
    }

    /**
     * エラー通知
     *
     * @param fileUuid
     * @param logFileUuid
     * @param errorType
     * @param errorHttpStatusCode
     * @param processType
     * @param intervals
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse getCsvUploadErrorByType(
            String fileUuid,
            String logFileUuid,
            int errorType,
            int errorHttpStatusCode,
            String processType,
            int intervals,
            LoginUser loginUser) {

        BasicResponse response = new BasicResponse();
        List<TblCsvUploadError> errorRecordList = null;
        Date errorDatetime = null;
        Boolean shouldReportError = false;
        Query query;
        String jpql;
        String companyName;

        try {
            switch (errorType) {
                case 1:  //<1>CSVファイルが見つからないとき
                    //設定ファイルに各ファイルの出力間隔(分)を定義できるよう項目追加し、その間隔を超えてファイルが見つからないときエラー通知する。
                    // <判定方法>	
                    //・サーバーの現在時刻から出力間隔(分)を差し引いて検索基準時刻を求める。
                    //・CSVアップロードエラーテーブルから処理種別が同一かつ、エラー種別が1かつ、解決フラグが0かつ、エラー日時が検索基準時刻以前のレコードを探す。
                    //レコードが見つかればエラー通知を行う。
                    //だだし、メール送信Flagが立っているレコードが見つかった場合、送信はしない。
                    Date currentDate = new Date();
                    Calendar c = Calendar.getInstance();
                    c.setTime(currentDate);
                    c.add(Calendar.MINUTE, -1 * intervals); //検索基準時刻以前
                    Date dateMinusDay = c.getTime();
                    
                    //検索基準時刻前のエラーデータ抽出
                    jpql = "SELECT t FROM TblCsvUploadError t WHERE "
                            + "t.errorDatetime < :dateMinusDay "
                            + "AND t.solvedFlg = 0 "
                            + "AND t.processType = :processType "
                            + "AND t.errorType = :errorType "
                            + "ORDER BY t.errorDatetime DESC";
                    query = entityManager.createQuery(jpql);
                    query.setParameter("dateMinusDay", dateMinusDay);
                    query.setParameter("processType", processType);
                    query.setParameter("errorType", errorType);
                    List<TblCsvUploadError> beforeErrorRecordList = query.getResultList();

                    //検索基準時刻前のエラーデータが存在している場合、間隔時間以内にエラー通知したか
                    if (beforeErrorRecordList != null && beforeErrorRecordList.size() > 0) {
                        jpql = "SELECT t FROM TblCsvUploadError t WHERE "
                                + "t.errorDatetime > :dateMinusDay "
                                + "AND t.solvedFlg = 0 "
                                + "AND t.processType = :processType "
                                + "AND t.errorType = :errorType "
                                + "AND t.emailSentFlg = 1 "
                                + "ORDER BY t.errorDatetime DESC";
                        query = entityManager.createQuery(jpql);
                        query.setParameter("dateMinusDay", dateMinusDay);
                        query.setParameter("processType", processType);
                        query.setParameter("errorType", errorType);
                        List<TblCsvUploadError> emailSentRecordList = query.getResultList();
                        
                        //間隔時間以内にエラー通知を行ったことがない場合、通知する
                        if (emailSentRecordList == null || emailSentRecordList.isEmpty()) {
                            shouldReportError = true;
                            jpql = "SELECT t FROM TblCsvUploadError t WHERE "
                                    + "t.solvedFlg = 0 "
                                    + "AND t.processType = :processType "
                                    + "AND t.errorType = :errorType "
                                    + "ORDER BY t.errorDatetime DESC";
                            query = entityManager.createQuery(jpql);
                            query.setParameter("processType", processType);
                            query.setParameter("errorType", errorType);
                            query.setFirstResult(0);
                            query.setMaxResults(1);
                            errorRecordList = query.getResultList();
                            errorDatetime = errorRecordList.get(0).getErrorDatetime();
                        }
                    }
                    break;

                case 2:  //<2>CSVファイルにデータ不正があるとき
                    // 条件なし。必ずメール送信する。
                    shouldReportError = true;
                    jpql = "SELECT t FROM TblCsvUploadError t WHERE "
                            + "t.solvedFlg = 0 "
                            + "AND t.emailSentFlg = 0 "
                            + "AND t.processType = :processType "
                            + "AND t.errorType = :errorType "
                            + "ORDER BY t.errorDatetime DESC";
                    query = entityManager.createQuery(jpql);
                    query.setParameter("processType", processType);
                    query.setParameter("errorType", errorType);
                    errorRecordList = query.getResultList();
                    errorDatetime = errorRecordList.get(0).getErrorDatetime();
                    break;

                case 3: //<3>HTTPステータスエラーのとき
                    // CSVアップロードエラーテーブルから処理種別が同一かつ、エラー種別が3かつ、解決フラグが0のレコードを探す。
                    // レコードが見つかればエラー通知を行わない。
                    //復旧するまでエラー通知の再送は行わない。
                    jpql = "SELECT t FROM TblCsvUploadError t WHERE "
                            + "t.solvedFlg = 0 "
                            + "AND t.emailSentFlg = 1 "
                            + "AND t.processType = :processType "
                            + "AND t.errorType =:errorType";
                    query = entityManager.createQuery(jpql);
                    query.setParameter("processType", processType);
                    query.setParameter("errorType", errorType);
                    List<TblCsvUploadError> sentEmailErrorRecordList = query.getResultList();

                    //エラー通知を行ったことがない場合、通知する
                    if (sentEmailErrorRecordList == null || sentEmailErrorRecordList.isEmpty()) {
                        shouldReportError = true;
                        jpql = "SELECT t FROM TblCsvUploadError t WHERE "
                                + "t.solvedFlg = 0 "
                                + "AND t.emailSentFlg = 0 "
                                + "AND t.processType = :processType "
                                + "AND t.errorType =:errorType "
                                + "ORDER BY t.errorDatetime DESC";
                        query = entityManager.createQuery(jpql);
                        query.setParameter("processType", processType);
                        query.setParameter("errorType", errorType);
                        errorRecordList = query.getResultList();
                        errorDatetime = errorRecordList.get(0).getErrorDatetime();
                    }
                    break;
                default:
                    break;
            }

            if (shouldReportError) {
                companyName = getMyCompanyName();

                // send Error Mail
                sendErrorMail(response, logFileUuid, fileUuid, errorDatetime, errorType, errorHttpStatusCode, processType, companyName);

                //EmailSentFlg更新
                errorRecordList.forEach(errorRecord -> {
                    errorRecord.setUpdateDate(new Date());
                    errorRecord.setUpdateUserUuid(loginUser.getUserUuid());
                    errorRecord.setEmailSentFlg(1);
                    entityManager.merge(errorRecord);
                });
            }

        } catch (Exception e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(e.getMessage());
        }

        return response;
    }

    /**
     * エラー復旧通知
     *
     * @param processType
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse getCsvUploadErroSolvedByType(String processType, LoginUser loginUser) {

        BasicResponse response = new BasicResponse();
        List<TblCsvUploadError> solvedRecordList = null;
        Date errorDatetime;
        Date solvedDatetime = new Date();
        Query query;
        String jpql;
        String companyName;

        try {
            jpql = "SELECT t FROM TblCsvUploadError t WHERE "
                    + "t.solvedFlg = 0 "
                    + "AND t.processType = :processType "
                    + "ORDER BY t.errorDatetime DESC";
            query = entityManager.createQuery(jpql);
            query.setParameter("processType", processType);
            solvedRecordList = query.getResultList();
            if (solvedRecordList != null && solvedRecordList.size() > 0) {
                companyName = getMyCompanyName();

                //同一種類の処理にerrorType2 or errorType3の未解決エラーがあった場合、直接に復旧メール送信
                Optional<TblCsvUploadError> containedErrorType2 = solvedRecordList.stream().filter(item -> item.getErrorType() == 2).findFirst();
                Optional<TblCsvUploadError> containedErrorType3 = solvedRecordList.stream().filter(item -> item.getErrorType() == 3).findFirst();
                if (containedErrorType2.isPresent() || containedErrorType3.isPresent()) {
                    errorDatetime = getErrorDateTime(processType);
                    //send sloved error mail
                    sendSolvedErrorMail(response, errorDatetime, solvedDatetime, processType, companyName);
                } else {
                    //同一種類の処理にerrorType1の未解決エラーのみが存在し、且つ送信したことがある場合、復旧メール送信
                    errorDatetime = getErrorDateTime(processType);
                    if (errorDatetime != null) {
                        //send sloved error mail
                        sendSolvedErrorMail(response, errorDatetime, solvedDatetime, processType, companyName);
                    }
                }
                
                //emailSolvedFlg更新
                solvedRecordList.forEach(solvedRecord -> {
                    solvedRecord.setUpdateDate(new Date());
                    solvedRecord.setUpdateUserUuid(loginUser.getUserUuid());
                    solvedRecord.setSolvedFlg(1);
                    entityManager.merge(solvedRecord);
                });
            }
        } catch (Exception e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(e.getMessage());
        }

        return response;
    }
    /**
     * errorDateTime 取得
     *
     * @ProcessType
     * @return
     */
    private Date getErrorDateTime(String processType) {
        String jpql;
        Query query;
        Date errorDateTime = null;
        List<TblCsvUploadError> errorDatetimeRecordList = null;
        
        //メール送信したデータ対象からerrorDatetime取得
        jpql = "SELECT t FROM TblCsvUploadError t WHERE "
                + "t.solvedFlg = 0 "
                + "AND t.processType = :processType "
                + "AND t.emailSentFlg = 1 "
                + "ORDER BY t.errorDatetime DESC";
        query = entityManager.createQuery(jpql);
        query.setParameter("processType", processType);
        errorDatetimeRecordList = query.getResultList();
        if (errorDatetimeRecordList != null && errorDatetimeRecordList.size() > 0) {
            errorDateTime = errorDatetimeRecordList.get(0).getErrorDatetime();
        }
        return errorDateTime;
    }
    
    /**
     * user company 取得
     *
     * @return
     */
    private String getMyCompanyName() {
        String jpql;
        Query query;
        String companyName = "";
        List<MstCompany> companyList = null;

        jpql = "SELECT m FROM MstCompany m WHERE m.myCompany = :myCompany And m.externalFlg = :externalFlg ";
        query = entityManager.createQuery(jpql);
        query.setParameter("myCompany", 1);
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        companyList = query.getResultList();
        if (companyList != null && companyList.size() > 0) {
            companyName = companyList.get(0).getCompanyName();
        }
        return companyName;
    }

    /**
     * send Error Mail
     *
     * @param response
     * @param logFileUuid
     * @param csvFileUuid
     * @param errorDatetime
     * @param errorType
     * @param errorHttpStatusCode
     * @param processType
     * @param companyName
     */
    private void sendErrorMail(BasicResponse response,
            String logFileUuid,
            String csvFileUuid,
            Date errorDatetime,
            int errorType,
            int errorHttpStatusCode,
            String processType,
            String companyName) {
        String fileName;
        String zipFileName;
        String langId;
        StringBuffer workFilePath = new StringBuffer();
        StringBuffer zipFilePath = new StringBuffer();
        List<String> attachmentList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String mailErrorDatetime = sdf.format(errorDatetime);

        //send email user list
        List<MstUser> users = mstUserMailReceptionService.getMailReceiveDepartmentUsers("mail021");
        if (users.size() <= 0) {
            return;
        }
        langId = users.get(0).getLangId();

        //Attach Log file to e-mail (if null skip process)
        if (logFileUuid != null && !logFileUuid.equals("")) {
            fileNameList.add(tblCsvImportService.getLogFileNameByUuid(logFileUuid));

            StringBuffer filePath = new StringBuffer();
            filePath.append(kartePropertyService.getDocumentDirectory());
            filePath.append(FileUtil.SEPARATOR);
            filePath.append(CommonConstants.LOG);
            filePath.append(FileUtil.SEPARATOR);
            filePath.append(logFileUuid);
            filePath.append(".log");
            attachmentList.add(filePath.toString());
        }

        //Attach zipped CSV file to e-mail (if null skip process)
        if (csvFileUuid != null && !csvFileUuid.equals("")) {
            TblUploadFile uploadFile = tblUploadFileService.getTblUploadFile(csvFileUuid);
            fileName = uploadFile.getUploadFileName();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmmss");
            zipFileName = "KarteCsvUploader_" + sdf1.format(errorDatetime);
            fileNameList.add(zipFileName + ".zip");

            //対象error csvファイル Path作成
            StringBuffer filePath = new StringBuffer();
            filePath.append(kartePropertyService.getDocumentDirectory());
            filePath.append(FileUtil.SEPARATOR);
            filePath.append(CommonConstants.CSV);
            filePath.append(FileUtil.SEPARATOR);
            filePath.append(csvFileUuid);
            filePath.append(".csv");

            //Work File Path作成
            workFilePath.append(kartePropertyService.getDocumentDirectory());
            workFilePath.append(FileUtil.SEPARATOR);
            workFilePath.append(CommonConstants.WORK);
            workFilePath.append(FileUtil.SEPARATOR);
            workFilePath.append(fileName);

            //対象error csvファイルをworkフォルダにCopy
            FileUtil.fileChannelCopy(filePath.toString(), workFilePath.toString(), true);

            //workフォルダの対象error csvファイルを圧縮する
            boolean charsetFlg = false;
            if (LANGID.equalsIgnoreCase(langId)) {
                charsetFlg = true;
            }
            ZipCompressor.zip(workFilePath.toString(), zipFileName, charsetFlg);

            //Zip File Path作成
            zipFilePath.append(kartePropertyService.getDocumentDirectory());
            zipFilePath.append(FileUtil.SEPARATOR);
            zipFilePath.append(CommonConstants.WORK);
            zipFilePath.append(FileUtil.SEPARATOR);
            zipFilePath.append(zipFileName);
            zipFilePath.append(".zip");

            attachmentList.add(zipFilePath.toString());
        }

        //Send mail by language
        List<MstUser> tempUsers = new ArrayList();
        String oldLangId = users.get(0).getLangId();
        for (MstUser user : users) {
            if (user.getLangId().equals(oldLangId)) {
                tempUsers.add(user);
            } else {
                sendMailByLanguage(tempUsers, mailErrorDatetime, response, attachmentList, fileNameList, errorType, errorHttpStatusCode, processType, companyName);
                tempUsers.clear();
                tempUsers.add(user);
            }
            oldLangId = user.getLangId();
        }
        if (tempUsers.size() > 0) {
            sendMailByLanguage(tempUsers, mailErrorDatetime, response, attachmentList, fileNameList, errorType, errorHttpStatusCode, processType, companyName);
        }

        //workフォルダの対象error csvファイル、ZIPファイルを削除する
        File deleteCsvFile = new File(workFilePath.toString());
        File deleteZipFile = new File(zipFilePath.toString());
        if(deleteCsvFile.exists())
        {
            deleteCsvFile.delete();
        }
        if(deleteZipFile.exists())
        {
            deleteZipFile.delete();
        }
    }

     /**
     * Send error mail by language
     *
     * @param users
     * @param currentTime
     * @param response
     * @param attachmentList
     * @param fileNameList
     * @param errorType
     * @param errorHttpStatusCode
     * @param processType
     * @param companyName
     */
    private void sendMailByLanguage(List<MstUser> users,
            String currentTime,
            BasicResponse response,
            List attachmentList,
            List fileNameList,
            int errorType,
            int errorHttpStatusCode,
            String processType,
            String companyName) {
        if (users.size() <= 0) {
            return;
        }
        List<String> toList = new ArrayList<>();
        for (MstUser user : users) {
            if (user.getMailAddress() != null) {
                toList.add(user.getMailAddress());
            }
        }
        if (toList.size() <= 0) {
            return;
        }
        String url = kartePropertyService.getBaseUrl();
        String langId = users.get(0).getLangId();
        List<String> dictKeyList = Arrays.asList("m-karte_csv_upload_error_mail_title", "m-karte_csv_upload_error_mail_body", "m-karte_csv_upload_error_datetime",
                "url", "file_type", "error_type", "msg_details_ attached_log_file", "m-karte_csv_upload_error_type_1", "m-karte_csv_upload_error_type_2", "m-karte_csv_upload_error_type_3",
                "m-karte_csv_upload_error_recover_mail_title", "m-karte_csv_upload_error_recover_mail_body", "error_datetime", "import_success_datetime", "file_type",
                "mold","component","company", "location", "mgmt_location_name", "material", "component_material", "direction", "production_plan", "user", "asset", "installation_site", "mold_production", "machine_production");
        dictKeyMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        String errorTypeDict = "";
        String processTypeDic = "";

        switch (errorType) {
            case 1:
                //CSVファイルが見つからない
                errorTypeDict = "m-karte_csv_upload_error_type_1";
                break;
            case 2:
                //CSVファイル取り込みエラー
                errorTypeDict = "m-karte_csv_upload_error_type_2";
                break;
            case 3:
                //HTTPステータスエラー。ステータスコード
                errorTypeDict = "m-karte_csv_upload_error_type_3";
                break;
            default:
                break;
        }

        switch (processType) {
            case "MOLD":
                //金型
                processTypeDic = "mold";
                break;
            case "COMPONENT":
                //部品
                processTypeDic = "component";
                break;
            case "COMPANY":
                //会社
                processTypeDic = "company";
                break;
            case "LOCATION":
                //所在地
                processTypeDic = "location";
                break;
            case "INSTALLATION_SITE":
                //設置場所
                processTypeDic = "installation_site";
                break;
            case "MATERIAL":
                //材料
                processTypeDic = "material";
                break;
            case "COMPONENT_MATERIAL":
                //部品別材料
                processTypeDic = "component_material";
                break;
            case "DIRECTION":
                //手配
                processTypeDic = "direction";
                break;
            case "PRODUCTION_PLAN":
                //生産計画
                processTypeDic = "production_plan";
                break;
            case "USER":
                //ユーザー
                processTypeDic = "user";
                break;
            case "ASSET":
                //資産
                processTypeDic = "asset";
                break;
            case "MOLD_PRODUCTION":
                //金型生産実績追加
                processTypeDic = "mold_production";
                break;
            case "MACHINE_PRODUCTION":
                //設備生産実績追加
                processTypeDic = "machine_production";
            default:
                break;
        }

        //Make E-mail message
        String mailSubject;
        if (companyName.equals("") || companyName == null) {
            mailSubject = dictKeyMap.get("m-karte_csv_upload_error_mail_title");
        } else {
            mailSubject = dictKeyMap.get("m-karte_csv_upload_error_mail_title") + " " + companyName;
        }
        StringBuilder mailBody = new StringBuilder();
        mailBody.append(dictKeyMap.get("m-karte_csv_upload_error_mail_body"));
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(dictKeyMap.get("m-karte_csv_upload_error_datetime") + ": " + currentTime);
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(dictKeyMap.get("url") + ": " + url);
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(dictKeyMap.get("file_type") + ": " + dictKeyMap.get(processTypeDic));
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        if (errorType == 3) {
            mailBody.append(dictKeyMap.get("error_type") + ": " + dictKeyMap.get(errorTypeDict) + ": " + errorHttpStatusCode);
        } else {
            mailBody.append(dictKeyMap.get("error_type") + ": " + dictKeyMap.get(errorTypeDict));
        }
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        if (errorType == 2) {
        mailBody.append(dictKeyMap.get("msg_details_ attached_log_file"));
        }
        //Send e-mail
        try {
            mailSender.setMakePlainTextBody(true);
            if (attachmentList.size() <= 0) {
                mailSender.sendMail(toList, null, mailSubject, mailBody.toString());
            } else {
                mailSender.sendMailWithMultiAttachment(toList, null, mailSubject, mailBody.toString(), attachmentList, fileNameList);
            }
        } catch (Exception e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage("Failed to send email.");
        }
    }
    
    /**
     * Send solved error mail
     *
     * @param response
     * @param errorDatetime
     * @param slovedDatetime
     * @param processType
     * @param companyName
     */
    private void sendSolvedErrorMail(BasicResponse response,
            Date errorDatetime,
            Date slovedDatetime,
            String processType,
            String companyName) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String mailErrorDatetime = sdf.format(errorDatetime);
        String mailSlovedDatetime = sdf.format(slovedDatetime);
                
        //send email user list
        List<MstUser> users = mstUserMailReceptionService.getMailReceiveDepartmentUsers("mail021");
        if (users.size() <= 0) {
            return;
        }

        //Send mail by language
        List<MstUser> tempUsers = new ArrayList();
        String oldLangId = users.get(0).getLangId();
        for (MstUser user : users) {
            if (user.getLangId().equals(oldLangId)) {
                tempUsers.add(user);
            } else {
                sendMailByLanguage(tempUsers, mailErrorDatetime, mailSlovedDatetime, response, processType, companyName);
                tempUsers.clear();
                tempUsers.add(user);
            }
            oldLangId = user.getLangId();
        }
        if (tempUsers.size() > 0) {
            sendMailByLanguage(tempUsers, mailErrorDatetime, mailSlovedDatetime, response, processType, companyName);
        }
    }
    
    /**
     * Send solved error mail by language
     *
     * @param users
     * @param errorDatetime
     * @param slovedDatetime
     * @param response
     * @param processType
     * @param companyName
     */
    private void sendMailByLanguage(List<MstUser> users,
            String errorDatetime,
            String slovedDatetime,
            BasicResponse response,
            String processType,
            String companyName) {
        if (users.size() <= 0) {
            return;
        }
        List<String> toList = new ArrayList<>();
        for (MstUser user : users) {
            if (user.getMailAddress() != null) {
                toList.add(user.getMailAddress());
            }
        }
        if (toList.size() <= 0) {
            return;
        }

        String langId = users.get(0).getLangId();
        List<String> dictKeyList = Arrays.asList("m-karte_csv_upload_error_recover_mail_title", "m-karte_csv_upload_error_recover_mail_body", "error_datetime", "import_success_datetime", "file_type",
                "mold", "component", "company", "location", "mgmt_location_name", "material", "component_material", "direction", "production_plan", "user", "asset", "installation_site", "mold_production", "machine_production");
        dictKeyMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        String processTypeDic = "";

        switch (processType) {
            case "MOLD":
                //金型
                processTypeDic = "mold";
                break;
            case "COMPONENT":
                //部品
                processTypeDic = "component";
                break;
            case "COMPANY":
                //会社
                processTypeDic = "company";
                break;
            case "LOCATION":
                //所在地
                processTypeDic = "location";
                break;
            case "INSTALLATION_SITE":
                //設置場所
                processTypeDic = "installation_site";
                break;
            case "MATERIAL":
                //材料
                processTypeDic = "material";
                break;
            case "COMPONENT_MATERIAL":
                //部品別材料
                processTypeDic = "component_material";
                break;
            case "DIRECTION":
                //手配
                processTypeDic = "direction";
                break;
            case "PRODUCTION_PLAN":
                //生産計画
                processTypeDic = "production_plan";
                break;
            case "USER":
                //ユーザー
                processTypeDic = "user";
                break;
            case "ASSET":
                //資産
                processTypeDic = "asset";
                break;
            case "MOLD_PRODUCTION":
                //金型生産実績追加
                processTypeDic = "mold_production";
                break;
            case "MACHINE_PRODUCTION":
                //設備生産実績追加
                processTypeDic = "machine_production";
            default:
                break;
        }

        //Make E-mail message
        String mailSubject;
        if (companyName.equals("") || companyName == null) {
            mailSubject = dictKeyMap.get("m-karte_csv_upload_error_recover_mail_title");
        } else {
            mailSubject = dictKeyMap.get("m-karte_csv_upload_error_recover_mail_title") + " " + companyName;
        }
        StringBuilder mailBody = new StringBuilder();
        mailBody.append(dictKeyMap.get("m-karte_csv_upload_error_recover_mail_body"));
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(dictKeyMap.get("error_datetime") + ": " + errorDatetime);
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(dictKeyMap.get("import_success_datetime") + ": " + slovedDatetime);
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        mailBody.append(dictKeyMap.get("file_type") + ": " + dictKeyMap.get(processTypeDic));
        mailBody.append(mailSender.MAIL_RETURN_CODE);
        //Send e-mail
        try {
            mailSender.setMakePlainTextBody(true);
            mailSender.sendMail(toList, null, mailSubject, mailBody.toString());

        } catch (Exception e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage("Failed to send email.");
        }
    }
}
