/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.contact;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.util.BeanCopyUtil;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.Pager;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author admin
 */
@Dependent
public class MstContactService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private CnfSystemService cnfSystemService;

    @Inject
    private TblCsvImportService tblCsvImportService;
    
    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("companyCode", " ORDER BY mstCompany.companyCode ");//会社コード
        orderKey.put("companyName", " ORDER BY mstCompany.companyName ");//会社名称
        orderKey.put("locationCode", " ORDER BY mstLocation.locationCode ");//所在地コード
        orderKey.put("locationName", " ORDER BY mstLocation.locationName ");//所在地名称
        orderKey.put("mgmtCompanyCode", " ORDER BY mstContact.mgmtCompanyCode ");//管理先コード
        orderKey.put("contactName", " ORDER BY mstContact.contactName ");//氏名
        orderKey.put("department", "ORDER BY mstContact.department ");//部署
        orderKey.put("position", " ORDER BY mstContact.position ");//役職
        orderKey.put("mailAddress", " ORDER BY mstContact.mailAddress ");//メールアドレス
        orderKey.put("telNo", " ORDER BY mstContact.telNo ");//電話番号
        orderKey.put("assetManagementFlg", " ORDER BY mstContact.assetManagementFlg ");//フラグ
       
    }
    /**
     * 担当者マスタ複数取得
     *
     * @param companyName
     * @param locationName
     * @param mgmtCompanyCode
     * @param contactName
     * @param companyId
     * @param locationId
     * @param pageNumber
     * @param pageSize
     * @param isCount
     * @param loginUser
     * @param isPage
     * @param isList
     * @return
     */
    public MstContactList getContactList(
            String companyName,
            String locationName,
            String mgmtCompanyCode,
            String contactName,
            String companyId,
            String locationId,
            int pageNumber,
            int pageSize,
            boolean isCount,
            LoginUser loginUser,
            boolean isPage,
            boolean isList,
            String sord,
            String sidx
    ) {
        MstContactList response = new MstContactList();

        if (!isPage) {
            List count = getMstContactSql(companyName, locationName, mgmtCompanyCode, contactName, companyId, locationId, pageNumber, pageSize, true,  null,
            null);
            // ページをめぐる
            Pager pager = new Pager();
            response.setPageNumber(pageNumber);
            long counts = (long) count.get(0);
            response.setCount(counts);
            response.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));
            if (counts == 0) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found");
                response.setErrorMessage(msg);
                return response;
            }
        }
        List list = getMstContactSql(companyName, locationName, mgmtCompanyCode, contactName, companyId, locationId, pageNumber, pageSize, isCount, sord,
                sidx);
        if (isCount) {
            response.setCount((long) list.get(0));
            CnfSystem cnf = cnfSystemService.findByKey("system", "max_list_record_count");
            long sysCount = Long.parseLong(cnf.getConfigValue());

            if (response.getCount() <= 0) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found");
                response.setErrorMessage(msg);
            } else if (response.getCount() > sysCount) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_confirm_max_record_count");
                msg = String.format(msg, sysCount);
                response.setErrorMessage(msg);
            }
        } else if (null != list && list.size() > 0) {
            response.setMstContacts(list);
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found");
            response.setErrorMessage(msg);
        }

        return response;
    }

    /**
     *
     * @param companyName
     * @param locationName
     * @param mgmtCompanyCode
     * @param contactName
     * @param contactName1
     * @param action
     * @return
     */
    private List getMstContactSql(String companyName,
            String locationName,
            String mgmtCompanyCode,
            String contactName,
            String companyId,
            String locationId,
            int pageNumber,
            int pageSize,
            boolean isCount,
            String sord,
            String sidx) {

        StringBuilder sql = new StringBuilder("SELECT ");
        if (isCount) {
            sql = sql.append(" COUNT(1) ");
        } else {
            sql = sql.append(" mstContact ");

        }
        sql = sql.append(" FROM MstContact mstContact "
                + " LEFT JOIN FETCH mstContact.mstCompany mstCompany "
                + " LEFT JOIN FETCH mstContact.mstLocation mstLocation "
                + " LEFT JOIN FETCH mstContact.mstMgmtCompany mstMgmtCompany "
                + " WHERE 1=1 ");

        if (StringUtils.isNotEmpty(companyName)) {
            sql = sql.append(" and mstCompany.companyName LIKE :companyName ");
        }
        if (StringUtils.isNotEmpty(locationName)) {
            sql = sql.append(" and mstLocation.locationName LIKE :locationName ");
        }
        if (StringUtils.isNotEmpty(mgmtCompanyCode)) {
            sql = sql.append(" and mstContact.mgmtCompanyCode LIKE :mgmtCompanyCode ");
        }
        if (StringUtils.isNotEmpty(contactName)) {
            sql = sql.append(" and mstContact.contactName LIKE :contactName ");
        }
        if (StringUtils.isNotEmpty(companyId)) {
            sql = sql.append(" and mstContact.companyId LIKE :companyId ");
        }
        if (StringUtils.isNotEmpty(locationId)) {
            sql = sql.append(" and mstContact.locationId LIKE :locationId ");
        }
        
        // 
        if (StringUtils.isNotEmpty(sidx)) {
            sql = sql.append(orderKey.get(sidx));

            if (StringUtils.isNotEmpty(sord)) {
                sql = sql.append(sord);
            }

        } else {
            sql = sql.append(" ORDER BY mstContact.mstCompany.companyCode ");
        }

        Query query = entityManager.createQuery(sql.toString());

        if (StringUtils.isNotEmpty(companyName)) {
            query.setParameter("companyName", "%" + companyName + "%");
        }
        if (StringUtils.isNotEmpty(locationName)) {
            query.setParameter("locationName", "%" + locationName + "%");
        }
        if (StringUtils.isNotEmpty(mgmtCompanyCode)) {
            query.setParameter("mgmtCompanyCode", "%" + mgmtCompanyCode + "%");
        }
        if (StringUtils.isNotEmpty(contactName)) {
            query.setParameter("contactName", "%" + contactName + "%");
        }
        if (StringUtils.isNotEmpty(companyId)) {
            query.setParameter("companyId", companyId);
        }
        if (StringUtils.isNotEmpty(locationId)) {
            query.setParameter("locationId", locationId);
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
     * 担当者マスタ詳細取得する
     *
     * @param uuid
     * @param loginUser
     * @return
     */
    public MstContactList getMstContactDetailByUuid(String uuid, LoginUser loginUser) {
        Query query = entityManager.createNamedQuery("MstContact.findByUuid");
        query.setParameter("uuid", uuid);

        List list = query.getResultList();
        MstContactList mstContactList = new MstContactList();

        if (null != list && list.size() <= 0) {
            mstContactList.setError(true);
            mstContactList.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found");
            mstContactList.setErrorMessage(msg);
        } else {
            mstContactList.setMstContacts(list);
        }

        return mstContactList;
    }

    /**
     * 担当者マスタ情報を修正
     *
     * @param mstContactVo
     * @param loginUser
     * @return
     */
    @Transactional
    public MstContactVo updateMstContact(MstContactVo mstContactVo, LoginUser loginUser) {

        if (!checkMstContact(mstContactVo, loginUser)) {
            return mstContactVo;
        }
        if (StringUtils.isNotEmpty(mstContactVo.getMgmtCompanyCode())) {
            if (getContactMgmtCompanyCodeExist(mstContactVo.getMgmtCompanyCode(), mstContactVo.getCompanyId())) {
                mstContactVo.setError(true);
                mstContactVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstContactVo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_mgmt_company_code_is_used"));
                return mstContactVo;
            }
        }
        
        if (1 == mstContactVo.getAssetManagementFlg()) {
            if (getAssetManagementFlgDuplicate(mstContactVo.getMgmtCompanyCode(), mstContactVo.getCompanyId(),mstContactVo.getUuid())) {
                mstContactVo.setError(true);
                mstContactVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstContactVo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_asset_management_flg_duplicate"));
                return mstContactVo;
            }
        }
        
        if (StringUtils.isEmpty(mstContactVo.getLocationId())) {
            mstContactVo.setLocationId(null);
        }

        MstContact mstContact = entityManager.find(MstContact.class, mstContactVo.getUuid());
        BeanCopyUtil.copyFields(mstContactVo, mstContact);
        mstContact.setUpdateUserUuid(loginUser.getUserUuid());
        mstContact.setUpdateDate(new java.util.Date());
        entityManager.merge(mstContact);

        return mstContactVo;
    }

    /**
     * 担当者マスタ情報を修正&追加のCheck
     *
     * @param mstContact
     * @param basicResponse
     * @param loginUser
     * @return
     */
    private boolean checkMstContact(MstContactVo mstContactVo, LoginUser loginUser) {
        //        単体チェック			
        //mgmtCompanyCode・長さチェック（DB定義参照）/桁数がオーバーしています。(dict_key = msg_error_over_length)				
        if (StringUtils.isNotEmpty(mstContactVo.getMgmtCompanyCode())) {
            if (mstContactVo.getMgmtCompanyCode().length() > 100) {
                mstContactVo.setError(true);
                mstContactVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length");
                mstContactVo.setErrorMessage(msg);
                return false;
            }
        }
        if (StringUtils.isNotEmpty(mstContactVo.getContactName())) {
            if (mstContactVo.getContactName().length() > 100) {
                mstContactVo.setError(true);
                mstContactVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length");
                mstContactVo.setErrorMessage(msg);
                return false;
            }
        }
        //department・長さチェック（DB定義参照）/桁数がオーバーしています。(dict_key = msg_error_over_length)
        if (StringUtils.isNotEmpty(mstContactVo.getDepartment())) {
            if (mstContactVo.getDepartment().length() > 100) {
                mstContactVo.setError(true);
                mstContactVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length");
                mstContactVo.setErrorMessage(msg);
                return false;
            }
        }
        //position・長さチェック（DB定義参照）/桁数がオーバーしています。(dict_key = msg_error_over_length)
        if (StringUtils.isNotEmpty(mstContactVo.getPosition())) {
            if (mstContactVo.getPosition().length() > 100) {
                mstContactVo.setError(true);
                mstContactVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length");
                mstContactVo.setErrorMessage(msg);
                return false;
            }
        }
        //telNo・長さチェック（DB定義参照）/桁数がオーバーしています。(dict_key = msg_error_over_length)
        if (StringUtils.isNotEmpty(mstContactVo.getTelNo())) {
            if (mstContactVo.getTelNo().length() > 25) {
                mstContactVo.setError(true);
                mstContactVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length");
                mstContactVo.setErrorMessage(msg);
                return false;
            }
        }
        //assetManagementFlag・数字チェック・長さチェック（DB定義参照）/数字ではありません。（dict_key = msg_error_not_isnumber）
        if (mstContactVo.getAssetManagementFlg() != 0 && mstContactVo.getAssetManagementFlg() != 1) {
            mstContactVo.setError(true);
            mstContactVo.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_isnumber");
            mstContactVo.setErrorMessage(msg);
            return false;
        }
        //mailAddress
        if (StringUtils.isNotEmpty(mstContactVo.getMailAddress())) {
            if (!FileUtil.isValidEmail(mstContactVo.getMailAddress())) {
                mstContactVo.setError(true);
                mstContactVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_not_valid_mail_address");
                mstContactVo.setErrorMessage(msg);
                return false;
            }
        }
        return true;
    }

    /**
     * 担当者マスタ情報を追加
     *
     * @param mstContactVo
     * @param loginUser
     * @param csvFile
     * @param index
     * @return
     */
    @Transactional
    public MstContactVo createMstContact(MstContactVo mstContactVo, LoginUser loginUser, String csvFile, int index) {
        if (!checkMstContact(mstContactVo, loginUser)) {
            return mstContactVo;
        }
        if (StringUtils.isNotEmpty(mstContactVo.getMgmtCompanyCode())) {
            if (getContactMgmtCompanyCodeExist(mstContactVo.getMgmtCompanyCode(), mstContactVo.getCompanyId())) {
                mstContactVo.setError(true);
                mstContactVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstContactVo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_mgmt_company_code_is_used"));
                return mstContactVo;
            }
        }
        
        if (1 == mstContactVo.getAssetManagementFlg()) {
            if (getAssetManagementFlgDuplicate(mstContactVo.getMgmtCompanyCode(), mstContactVo.getCompanyId(),null)) {
                mstContactVo.setError(true);
                mstContactVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstContactVo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_asset_management_flg_duplicate"));
                return mstContactVo;
            }
        }
        
        if (StringUtils.isEmpty(mstContactVo.getLocationId())) {
            mstContactVo.setLocationId(null);
        }
        MstContact mstContact = new MstContact();
        mstContactVo.setUuid(IDGenerator.generate());
        mstContactVo.setUpdateDate(new java.util.Date());
        mstContactVo.setUpdateUserUuid(loginUser.getUserUuid());
        BeanCopyUtil.copyFields(mstContactVo, mstContact);
        mstContact.setCreateDate(new java.util.Date());
        mstContact.setCreateUserUuid(loginUser.getUserUuid());
        entityManager.persist(mstContact);

        return mstContactVo;
    }

    /**
     * 担当者マスタ削除
     *
     * @param uuid
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse deleteContact(String uuid, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        if (!getContactIsExistCheck(uuid)) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return basicResponse;
        }

        Query query = entityManager.createNamedQuery("MstContact.deleteByUuid");
        query.setParameter("uuid", uuid);
        query.executeUpdate();

        return basicResponse;
    }

    /**
     *
     * @param uuid
     * @return
     */
    public boolean getContactIsExistCheck(String uuid) {
        Query query = entityManager.createNamedQuery("MstContact.findByUuid");
        query.setParameter("uuid", uuid);
        try {
            query.getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    /**
     * 担当者マスタ情報をCSV出力
     *
     * @param companyName
     * @param locationName
     * @param mgmtCompanyCode
     * @param contactName
     * @param companyId
     * @param locationId
     * @param pageNumber
     * @param loginUser
     * @param pageSize
     * @return
     */
    public FileReponse getContactCSV(
            String companyName,
            String locationName,
            String mgmtCompanyCode,
            String contactName,
            String companyId,
            String locationId,
            int pageNumber,
            int pageSize,
            LoginUser loginUser) {

        FileReponse reponse = new FileReponse();

        //CSVファイル出力
        String fileUuid = IDGenerator.generate();
        String langId = loginUser.getLangId();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, fileUuid);

        Map<String, String> headerMap = getDictionaryList(langId);

        /*Head*/
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();
        headList.add(headerMap.get("company_code")); // 会社コード
        headList.add(headerMap.get("company_name")); // 会社名称
        headList.add(headerMap.get("location_code")); //所在地コード
        headList.add(headerMap.get("location_name")); //所在地名称
        headList.add(headerMap.get("mgmt_company_code")); //管理先コード
        headList.add(headerMap.get("contact_person_name")); //氏名
        headList.add(headerMap.get("contact_department")); //部署
        headList.add(headerMap.get("contact_position")); //役職
        headList.add(headerMap.get("mail_address")); //メールアドレス
        headList.add(headerMap.get("tel_no")); //電話番号
        headList.add(headerMap.get("asset_management_flg")); //資産管理担当フラグ   
        gLineList.add(headList);

        MstContactList mstContactList = this.getContactList(companyName, locationName, mgmtCompanyCode, contactName, companyId, locationId, pageNumber, pageSize, false, loginUser, false, true, null, null);
        ArrayList lineList;
        // 情報取得
        if (mstContactList != null && mstContactList.getMstContacts() != null && mstContactList.getMstContacts().size() > 0) {
            for (int i = 0; i < mstContactList.getMstContacts().size(); i++) {
                MstContact mstContact = mstContactList.getMstContacts().get(i);
                lineList = new ArrayList();
                lineList.add(mstContact.getMstCompany().getCompanyCode()); //会社コード
                lineList.add(mstContact.getMstCompany().getCompanyName());// 会社名称
                if (mstContact.getMstLocation() != null) {
                    lineList.add(mstContact.getMstLocation().getLocationCode());//所在地コード
                    lineList.add(mstContact.getMstLocation().getLocationName());//所在地名称
                } else {
                    lineList.add(null);//所在地コード
                    lineList.add(null);//所在地名称
                }
                lineList.add(mstContact.getMgmtCompanyCode());//管理先コード
                lineList.add(mstContact.getContactName());//氏名
                lineList.add(mstContact.getDepartment());//部署
                lineList.add(mstContact.getPosition());//役職
                lineList.add(mstContact.getMailAddress());//メールアドレス
                lineList.add(mstContact.getTelNo());//電話番号
                lineList.add(String.valueOf(mstContact.getAssetManagementFlg())); //資産管理担当フラグ

                gLineList.add(lineList);
            }
        }

        // csv 出力
        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(fileUuid);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MST_CONTACT_MAINTENANCE);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());

        // ファイル名称
        String fileName = mstDictionaryService.getDictionaryValue(langId, "mst_contact");
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        reponse.setFileUuid(fileUuid);
        return reponse;
    }

    /**
     *
     * @param langId
     * @return
     */
    private Map<String, String> getDictionaryList(String langId) {
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList("company_code", "company_name", "location_code", "location_name", "mgmt_company_code", "contact_person_name", "contact_department",
                "contact_position", "mail_address", "tel_no", "asset_management_flg", "row_number", "error", "error_detail", "db_process", "msg_record_added", "msg_error_wrong_csv_layout", "mst_contact_maintenance", "msg_error_not_null", "msg_error_over_length", "msg_not_valid_mail_address", "msg_error_value_invalid", "msg_record_added", "mst_contact", "mst_error_record_not_found", "msg_error_not_null", "db_process");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        return headerMap;
    }

    /**
     * 担当者マスタ情報をCSV取込
     *
     * @param fileUuid
     * @param loginUser
     * @return
     */
    @Transactional
    public ImportResultResponse postContactCSV(String fileUuid, LoginUser loginUser) {

//2017-06-27
        ImportResultResponse importResultResponse = new ImportResultResponse();
        //①CSVファイルを取込み
        long succeededCount;
        long addedCount = 0;
        long failedCount = 0;

        String logFileUuid = IDGenerator.generate();
        String csvFile = FileUtil.getCsvFilePath(kartePropertyService, fileUuid);
        if (!csvFile.endsWith(CommonConstants.CSV)) {
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_wrong_csv_layout");
            importResultResponse.setErrorMessage(msg);
            return importResultResponse;
        }

        ArrayList readList = CSVFileUtil.readCsv(csvFile);
        String userLangId = loginUser.getLangId();
        Map<String, String> headerMap = getDictionaryList(userLangId);
        String lineNo = headerMap.get("row_number");
        String companyCode = headerMap.get("company_code");
        String locationCode = headerMap.get("location_code");

        String contactName = headerMap.get("contact_person_name");
        String department = headerMap.get("contact_department");
        String position = headerMap.get("contact_position");
        String mailAddress = headerMap.get("mail_address");
        String telNo = headerMap.get("tel_no");
        String assetManagementFlg = headerMap.get("asset_management_flg");
        String error = headerMap.get("error");
        String errorDetail = headerMap.get("error_detail");
        String process = headerMap.get("db_process");
        String notFound = headerMap.get("mst_error_record_not_found");
        String maxLangth = headerMap.get("msg_error_over_length");
        String layout = headerMap.get("msg_error_wrong_csv_layout");
        String invalidValue = headerMap.get("msg_error_value_invalid");
        String notnumber = headerMap.get("msg_error_not_isnumber");
        String nullMsg = headerMap.get("msg_error_not_null");
        String notvalidmail = headerMap.get("msg_not_valid_mail_address");
        String addedMsg = headerMap.get("msg_record_added");

        if (readList.size() <= 1) {
            return importResultResponse;
        } else {
            String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);

            FileUtil fu = new FileUtil();
            for (int i = 1; i < readList.size(); i++) {
                ArrayList comList = (ArrayList) readList.get(i);
                if (comList.size() != 11) {
                    //エラー情報をログファイルに記入
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, companyCode, "", error, 1, errorDetail, layout));
                    failedCount++;
                    continue;
                }

                //2017-06-29
                String strCompanyCode = String.valueOf(comList.get(0)).trim();
                String strLocationCode = String.valueOf(comList.get(2)).trim();
                String strContactName = String.valueOf(comList.get(5)).trim();
                String strDepartment = String.valueOf(comList.get(6));
                String strPosition = String.valueOf(comList.get(7));
                String strMailAddress = String.valueOf(comList.get(8));
                String strTelNo = String.valueOf(comList.get(9));
                String strAssetManagementFlg = String.valueOf(comList.get(10));

                if (StringUtils.isEmpty(strCompanyCode)) {
                    failedCount++;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, companyCode, strCompanyCode, error, 1, errorDetail, nullMsg));
                    continue;
                } else if (fu.maxLangthCheck(strCompanyCode, 45)) {
                    failedCount++;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, companyCode, strCompanyCode, error, 1, errorDetail, maxLangth));
                    continue;
                }
                if (fu.maxLangthCheck(strLocationCode, 100)) {
                    failedCount++;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, locationCode, strLocationCode, error, 1, errorDetail, maxLangth));
                    continue;
                }

                MstContactVo newMstContact = new MstContactVo();
                newMstContact.setCompanyCode(strCompanyCode);
                newMstContact.setLocationCode(strLocationCode);

                if (fu.maxLangthCheck(strContactName, 100)) {
                    failedCount++;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, contactName, strContactName, error, 1, errorDetail, maxLangth));
                    continue;
                }
                newMstContact.setContactName(strContactName);

                if (fu.maxLangthCheck(strDepartment, 100)) {
                    failedCount++;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, department, strDepartment, error, 1, errorDetail, maxLangth));
                    continue;
                }
                newMstContact.setDepartment(strDepartment);

                if (fu.maxLangthCheck(strPosition, 100)) {
                    failedCount++;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, position, strPosition, error, 1, errorDetail, maxLangth));
                    continue;
                }
                newMstContact.setPosition(strPosition);

                if (StringUtils.isNotEmpty(strMailAddress)) {
                    if (!FileUtil.isValidEmail(strMailAddress)) {
                        failedCount++;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, mailAddress, strMailAddress, error, 1, errorDetail, notvalidmail));
                        continue;
                    } else if (fu.maxLangthCheck(strMailAddress, 100)) {
                        failedCount++;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, mailAddress, strMailAddress, error, 1, errorDetail, maxLangth));
                        continue;
                    }
                    newMstContact.setMailAddress(strMailAddress);
                }

                if (fu.maxLangthCheck(strTelNo, 25)) {
                    failedCount++;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, telNo, strTelNo, error, 1, errorDetail, maxLangth));
                    continue;
                }
                newMstContact.setTelNo(strTelNo);

                if (StringUtils.isNotEmpty(strAssetManagementFlg)) {
                    if (!"1".equals(strAssetManagementFlg) && !"0".equals(strAssetManagementFlg)) {
                        failedCount++;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assetManagementFlg, strAssetManagementFlg, error, 1, errorDetail, invalidValue));
                        continue;
                    }
                    newMstContact.setAssetManagementFlg(Integer.parseInt(strAssetManagementFlg));
                } else {
                    newMstContact.setAssetManagementFlg(0);
                }

                Query mstCompanyQuery = entityManager.createNamedQuery("MstCompany.findByCompanyCode");
                mstCompanyQuery.setParameter("companyCode", strCompanyCode);
                mstCompanyQuery.setParameter("externalFlg", 0);
                List<MstCompany> mstcompany = mstCompanyQuery.getResultList();
                if (mstcompany == null || mstcompany.size() <= 0) {
                    failedCount++;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, companyCode, strCompanyCode, error, 1, errorDetail, notFound));
                    continue;
                }
                Iterator<MstLocation> mstLocations= mstcompany.get(0).getMstLocationCollection().iterator();
                boolean existFlag = false;
                if(mstLocations != null && mstLocations.hasNext()) {
                    while(mstLocations.hasNext()) {
                        MstLocation mstLocation = mstLocations.next();
                        if(StringUtils.isEmpty(strLocationCode) || mstLocation.getLocationCode().equals(strLocationCode)) {
                            existFlag = true;
                            break;
                        }
                    }
                } else {
                    existFlag = true;
                }
                if(!existFlag) {
                    failedCount++;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, locationCode, strLocationCode, error, 1, errorDetail, notFound));
                    continue;
                }
                newMstContact.setCompanyId(mstcompany.get(0).getId());
                newMstContact.setMgmtCompanyCode(mstcompany.get(0).getMgmtCompanyCode());

                Query mstLocationQuery = entityManager.createNamedQuery("MstLocation.findByLocationCode");
                mstLocationQuery.setParameter("locationCode", strLocationCode);
                mstLocationQuery.setParameter("externalFlg", 0);
                List<MstLocation> mstLocationList = mstLocationQuery.getResultList();
                if (StringUtils.isNotEmpty(strLocationCode)) {
                    if (mstLocationList == null || mstLocationList.size() <= 0) {
                        failedCount++;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, locationCode, strLocationCode, error, 1, errorDetail, notFound));
                        continue;
                    }
                    if (mstLocationList.get(0).getMstCompany() != null && !mstLocationList.get(0).getMstCompany().getCompanyCode().equals(strCompanyCode)) {
                        failedCount++;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, companyCode, strCompanyCode, error, 1, errorDetail, notFound));
                        continue;
                    }
                    newMstContact.setLocationId(mstLocationList.get(0).getId());
                    if (StringUtils.isNotEmpty(mstLocationList.get(0).getMgmtCompanyCode())) {
                        newMstContact.setMgmtCompanyCode(mstLocationList.get(0).getMgmtCompanyCode());
                    }
                }
                

                MstContactVo mstContactVo = createMstContact(newMstContact, loginUser, csvFile, i);
                if (mstContactVo.isError()) {
                    failedCount++;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, companyCode, strCompanyCode, error, 1, errorDetail, mstContactVo.getErrorMessage()));
                    continue;
                }
                addedCount++;
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, companyCode, strCompanyCode, error, 0, process, addedMsg));

            }

        }
        // リターン情報
        succeededCount = addedCount;
        importResultResponse.setTotalCount(readList.size() - 1);
        importResultResponse.setSucceededCount(succeededCount);
        importResultResponse.setAddedCount(addedCount);
        importResultResponse.setFailedCount(failedCount);
        importResultResponse.setLog(logFileUuid);

        //アップロードログをテーブルに書き出し
        TblCsvImport tblCsvImport = new TblCsvImport();
        tblCsvImport.setImportUuid(IDGenerator.generate());
        tblCsvImport.setImportUserUuid(loginUser.getUserUuid());
        tblCsvImport.setImportDate(new Date());
        tblCsvImport.setImportTable(CommonConstants.FUN_ID_MST_CONTACT_MAINTENANCE);
        tblCsvImport.setRecordCount(readList.size() - 1);
        tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
        tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
        tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
        tblCsvImport.setLogFileUuid(logFileUuid);
        String langId = loginUser.getLangId();
        String fileName = mstDictionaryService.getDictionaryValue(langId, "mst_contact");
        tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

        tblCsvImportService.createCsvImpor(tblCsvImport);

        return importResultResponse;

    }

    /**
     * この管理先コードが他の会社ではすでに使われています
     *
     * @param mgmtCompanyCode
     * @param companyId
     * @return
     */
    public boolean getContactMgmtCompanyCodeExist(String mgmtCompanyCode, String companyId) {
        Query query = entityManager.createQuery("SELECT m FROM MstContact m WHERE m.mgmtCompanyCode = :mgmtCompanyCode AND m.companyId <> :companyId");
        query.setParameter("mgmtCompanyCode", mgmtCompanyCode);
        query.setParameter("companyId", companyId);

        List<MstContact> list = query.getResultList();
        return list != null && list.size() > 0;
    }
    
    
    /**
     * 同じの管理先コードはひとつだけの資産管理担当フラグが設定できます。
     *
     * @param mgmtCompanyCode
     * @param companyId
     * @param uuid
     * @return
     */
    public boolean getAssetManagementFlgDuplicate(String mgmtCompanyCode, String companyId,String uuid) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT m FROM MstContact m "
                + "WHERE m.mgmtCompanyCode = :mgmtCompanyCode "
                + "AND m.companyId = :companyId "
                + "AND m.assetManagementFlg = :assetManagementFlg");
        if(StringUtils.isNotEmpty(uuid)) {
            sql.append(" AND m.uuid <> :uuid");
        }
        Query query = entityManager.createQuery(sql.toString());
        
        query.setParameter("mgmtCompanyCode", mgmtCompanyCode);
        query.setParameter("companyId", companyId);
        query.setParameter("assetManagementFlg", 1);
        if (StringUtils.isNotEmpty(uuid)) {
            query.setParameter("uuid", uuid);
        }

        List<MstContact> list = query.getResultList();
        return list != null && list.size() >= 1;
    }

}
