/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.structure;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponentService;
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
import com.kmcj.karte.util.Pager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
public class MstComponentStructureService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private MstComponentService mstComponentService;

    @Inject
    private TblCsvImportService tblCsvImportService;

    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("componentCode", " ORDER BY mstComponent.componentCode ");//部品コード
        orderKey.put("parentComponentCode", " ORDER BY mstParentComponent.componentCode ");//親部品コード
        orderKey.put("rootComponentCode", " ORDER BY mstRootComponent.componentCode ");//製品部品コード
        orderKey.put("quantity", " ORDER BY mstComponentStructure.quantity ");//個数
    }
    
    /**
     * 檢索
     *
     * @param componentCode
     * @param rootComponentCode
     * @param parentComponentCode
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isPage
     * @param orderBy
     * @return MstComponentStructureVoList
     */
    public MstComponentStructureVoList getMstComponentStructureVoList(
            String componentCode,
            String rootComponentCode,
            String parentComponentCode,
            String sidx, 
            String sord, 
            int pageNumber, 
            int pageSize, 
            boolean isPage,
            int orderBy
    ) {

        MstComponentStructureVoList response = new MstComponentStructureVoList();
        if (isPage) {
            List count = getMstComponentStructureVoListSQL(componentCode, parentComponentCode, rootComponentCode, sidx, sord, pageNumber, pageSize, true, orderBy);
            // ページをめぐる
            Pager pager = new Pager();
            response.setPageNumber(pageNumber);
            long counts = (long) count.get(0);
            response.setCount(counts);
            response.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));
        }
        
        List<MstComponentStructureVo> mstComponentStructureVos = new ArrayList();
        MstComponentStructureVo mstcomponentStructureVos;
        List<MstComponentStructure> list = getMstComponentStructureVoListSQL(componentCode, parentComponentCode, rootComponentCode, sidx, sord, pageNumber, pageSize, false, orderBy);

        if (list != null && list.size() > 0) {
            for (MstComponentStructure mstComponentStructure : list) {
                mstcomponentStructureVos = new MstComponentStructureVo();
                //uuid
                mstcomponentStructureVos.setUuid(mstComponentStructure.getUuid());
                //componentId  componentCode
                if (mstComponentStructure.getMstComponent() != null) {
                    mstcomponentStructureVos.setComponentId(mstComponentStructure.getMstComponent().getId());
                    mstcomponentStructureVos.setComponentCode(mstComponentStructure.getMstComponent().getComponentCode());
                } else {
                    mstcomponentStructureVos.setComponentCode("");
                    mstcomponentStructureVos.setComponentId("");
                }
                //parentComponentId   parentComponentCode
                if (mstComponentStructure.getMstParentComponent() != null) {
                    mstcomponentStructureVos.setParentComponentId(mstComponentStructure.getMstParentComponent().getId());
                    mstcomponentStructureVos.setParentComponentCode(mstComponentStructure.getMstParentComponent().getComponentCode());
                } else {
                    mstcomponentStructureVos.setParentComponentCode("");
                    mstcomponentStructureVos.setParentComponentId("");
                }
                //RootComponentId
                if (mstComponentStructure.getMstRootComponent() != null) {
                    mstcomponentStructureVos.setRootComponentId(mstComponentStructure.getMstRootComponent().getId());
                    mstcomponentStructureVos.setRootComponentCode(mstComponentStructure.getMstRootComponent().getComponentCode());
                } else {
                    mstcomponentStructureVos.setRootComponentCode("");
                    mstcomponentStructureVos.setRootComponentId("");
                }
                //quantity
                mstcomponentStructureVos.setQuantity(mstComponentStructure.getQuantity());
                
                mstcomponentStructureVos.setOperationFlag("2");

                mstComponentStructureVos.add(mstcomponentStructureVos);

            }
            response.setMstComponentStructureVos(mstComponentStructureVos);
        }
        return response;
    }

    /**
     * 部品構成マスタ複数取得
     *
     * @param componentCode
     * @param parentComponentCode
     * @param rootComponentCode
     * @return
     */
    private List getMstComponentStructureVoListSQL(
            String componentCode,
            String parentComponentCode,
            String rootComponentCode,
            String sidx, 
            String sord, 
            int pageNumber, 
            int pageSize,
            boolean isCount,
            int orderBy
    ) {
        StringBuilder sql = new StringBuilder();
        if (isCount) {
            sql.append("SELECT count(1) FROM MstComponentStructure mstComponentStructure ");

        } else {
            sql.append("SELECT mstComponentStructure  FROM MstComponentStructure mstComponentStructure ");
        }
        
        sql.append(" LEFT JOIN FETCH mstComponentStructure.mstComponent mstComponent "
            + " LEFT JOIN FETCH mstComponentStructure.mstParentComponent mstParentComponent "
            + " LEFT JOIN FETCH mstComponentStructure.mstRootComponent mstRootComponent "
            + " WHERE 1=1 ");
        if (StringUtils.isNotEmpty(componentCode)) {
            sql = sql.append(" AND mstComponent.componentCode LIKE :componentCode ");
        }
        if (StringUtils.isNotEmpty(parentComponentCode)) {
            sql = sql.append(" AND mstParentComponent.componentCode LIKE :parentComponentCode ");
        }
        if (StringUtils.isNotEmpty(rootComponentCode)) {
            sql = sql.append(" AND mstRootComponent.componentCode LIKE :rootComponentCode ");
        }
        if (orderBy == 1) {
            // 表示順は製品部品コード、親部品コード、部品コードの昇順
            sql = sql.append(" ORDER BY mstRootComponent.componentCode, mstParentComponent.componentCode ,mstComponent.componentCode");
        } else {
            if (!isCount) {
                if (StringUtils.isNotEmpty(sidx)) {

                    String sortStr = orderKey.get(sidx) + " " + sord;

                    sql.append(sortStr);
                } else {
                    // 表示順は部品コード、親部品コード、製品部品コードの昇順
                    sql = sql.append(" ORDER BY mstComponent.componentCode, mstParentComponent.componentCode, mstRootComponent.componentCode");
                }
            }
        }
        Query query = entityManager.createQuery(sql.toString(), MstComponentStructure.class);

        if (StringUtils.isNotEmpty(componentCode)) {
            query.setParameter("componentCode", "%" + componentCode + "%");
        }
        if (StringUtils.isNotEmpty(parentComponentCode)) {
            query.setParameter("parentComponentCode", "%" + parentComponentCode + "%");
        }
        if (StringUtils.isNotEmpty(rootComponentCode)) {
            query.setParameter("rootComponentCode", "%" + rootComponentCode + "%");
        }
        
        // 画面改ページを設定する
        if (!isCount && orderBy == 0) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }
        
        List list = query.getResultList();
        return list;
    }

    /**
     * CSV出力
     *
     * @param componentCode
     * @param rootComponentCode
     * @param parentComponentCode
     * @param loginUser
     * @return
     */
    public FileReponse getMstComponentStructureCSV(
            String componentCode,
            String rootComponentCode,
            String parentComponentCode,
            LoginUser loginUser) {
        FileReponse reponse = new FileReponse();
        //CSVファイル出力
        String fileUuid = IDGenerator.generate();
        String langId = loginUser.getLangId();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, fileUuid);

        List<String> dictKeyList = Arrays.asList("component_code", "root_component_code", "parent_component_code", "quantity", "component_structure_registration", "delete_record");
        Map<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(langId, dictKeyList);
        /*Head*/
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();
        headList.add(dictMap.get("component_code")); // 部品コード
        headList.add(dictMap.get("parent_component_code")); //親部品IDコード
        headList.add(dictMap.get("root_component_code")); // 製品部品コード
        headList.add(dictMap.get("quantity"));//構成個数
        headList.add(dictMap.get("delete_record"));//DELETE_FLG
        gLineList.add(headList);

        MstComponentStructureVoList mstComponentStructureVoList = getMstComponentStructureVoList(componentCode, parentComponentCode, rootComponentCode, "", "", 0, 0, false, 1);
        ArrayList lineList;
        if (mstComponentStructureVoList != null && mstComponentStructureVoList.getMstComponentStructureVos() != null && !mstComponentStructureVoList.getMstComponentStructureVos().isEmpty()) {

            for (MstComponentStructureVo mstComponentStructureVo : mstComponentStructureVoList.getMstComponentStructureVos()) {

                lineList = new ArrayList();
                //  componentCode
                lineList.add(mstComponentStructureVo.getComponentCode());//  componentCode
                //   parentComponentCode
                lineList.add(mstComponentStructureVo.getParentComponentCode());
                //RootComponentCode
                lineList.add(mstComponentStructureVo.getRootComponentCode());
                //quantity
                lineList.add(String.valueOf(mstComponentStructureVo.getQuantity()));
                //DELETE_FLG
                lineList.add("");
                gLineList.add(lineList);
            }
        }
        // csv 出力
        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(fileUuid);
        tblCsvExport.setExportDate(new Date());
        tblCsvExport.setExportTable("mst_component_structure");
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_COMPONENT_STRUCTURE);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        // ファイル名称
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(dictMap.get("component_structure_registration")));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        reponse.setFileUuid(fileUuid);
        return reponse;

    }

    /**
     * check
     *
     * @param uuid
     * @return
     */
    public MstComponentStructure getMstComponentStructureCheck(String uuid) {
        Query query = entityManager.createNamedQuery("MstComponentStructure.findByUuid");
        query.setParameter("UUID", uuid);
        try {
            MstComponentStructure mstComponentStructure = (MstComponentStructure) query.getSingleResult();
            return mstComponentStructure;
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * データが削除する
     * <P>
     * 選択した行の製品部品コードと部品コードが一致する場合は部品構成マスタの製品部品IDをキーにすべて削除する。
     * <P>
     * また、選択した行の親部品コードと部品コードが一致する場合は部品構成マスタの親部品IDと製品部品IDキーにすべて削除する。
     *
     * @param flag
     * @param uuid
     * @param rootComponentId
     * @param parentComponentId
     * @return
     */
    @Transactional
    private int deleteMstComponentStructure(String uuid) {

        MstComponentStructure oldMstComponentStructure = entityManager.find(MstComponentStructure.class, uuid);
        int count = 0;
        Query query;
        if (oldMstComponentStructure != null) {
            if (StringUtils.isNotEmpty(oldMstComponentStructure.getRootComponentId()) && oldMstComponentStructure.getRootComponentId().equals(oldMstComponentStructure.getComponentId())) {
                query = entityManager.createNamedQuery("MstComponentStructure.deleteByRootComponentId");
                query.setParameter("rootComponentId", oldMstComponentStructure.getRootComponentId());
                count = query.executeUpdate();
            } else if (StringUtils.isNotEmpty(oldMstComponentStructure.getRootComponentId()) && StringUtils.isNotEmpty(oldMstComponentStructure.getParentComponentId()) && oldMstComponentStructure.getParentComponentId().equals(oldMstComponentStructure.getComponentId())) {
                query = entityManager.createNamedQuery("MstComponentStructure.deleteByPRId");
                query.setParameter("parentComponentId", oldMstComponentStructure.getParentComponentId());
                query.setParameter("rootComponentId", oldMstComponentStructure.getRootComponentId());
                count = query.executeUpdate();
            } else {
                query = entityManager.createNamedQuery("MstComponentStructure.deleteByUuid");
                query.setParameter("uuid", uuid);
                count = query.executeUpdate();
            }

        }
        return count;

    }

    /**
     * UKにより存在チェックを実施
     *
     * @param componentId
     * @param rootComponentId
     * @param parentComponentId
     * @return
     */
    public MstComponentStructure checkMstComponentStructureByUK(String componentId, String rootComponentId, String parentComponentId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT m FROM MstComponentStructure m WHERE 1=1 ");
        if (StringUtils.isNotEmpty(componentId)) {
            sql.append("and m.componentId = :componentId ");
        } else {
            sql.append("and m.componentId IS NULL ");
        }

        if (StringUtils.isNotEmpty(rootComponentId)) {
            sql.append("and m.rootComponentId = :rootComponentId ");
        } else {
            sql.append("and m.rootComponentId IS NULL ");
        }

        if (StringUtils.isNotEmpty(parentComponentId)) {
            sql.append("and m.parentComponentId = :parentComponentId");
        } else {
            sql.append("and m.parentComponentId IS NULL ");
        }

        Query query = entityManager.createQuery(sql.toString());
        if (StringUtils.isNotEmpty(componentId)) {
            query.setParameter("componentId", componentId);
        }
        if (StringUtils.isNotEmpty(rootComponentId)) {
            query.setParameter("rootComponentId", rootComponentId);
        }
        if (StringUtils.isNotEmpty(parentComponentId)) {
            query.setParameter("parentComponentId", parentComponentId);
        }

        try {
            MstComponentStructure mstComponentStructure = (MstComponentStructure) query.getSingleResult();
            return mstComponentStructure;
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     *
     * 変更内容（追加、変更、削除）で部品構成テーブルを更新する。
     *
     * @param intputMstComponentStructureVos
     * @param loginUser
     * @return an instance of BasicResponse
     */
    @Transactional
    public BasicResponse postMstComponentStructure(MstComponentStructureVoList intputMstComponentStructureVos, LoginUser loginUser) {

        if (intputMstComponentStructureVos != null && intputMstComponentStructureVos.getMstComponentStructureVos() != null) {
            Date sysDate = new Date();
            MstComponentStructure mstComponentStructure;
            for (int i = 0; i < intputMstComponentStructureVos.getMstComponentStructureVos().size(); i++) {

                MstComponentStructureVo mstComponentStructureVos = intputMstComponentStructureVos.getMstComponentStructureVos().get(i);

                if (mstComponentStructureVos.getOperationFlag() != null) {
                    switch (mstComponentStructureVos.getOperationFlag()) {// 1:delete,3:update,4:add
                        case "1": {   // 1:delete 
                            deleteMstComponentStructure(mstComponentStructureVos.getUuid());
                            break;
                        }
                        case "3": {// 3:update

                            MstComponentStructure oldMstComponentStructure = entityManager.find(MstComponentStructure.class, mstComponentStructureVos.getUuid());

                            //componentId componentcode
                            if (StringUtils.isNotEmpty(mstComponentStructureVos.getComponentId())) {
                                oldMstComponentStructure.setComponentId(mstComponentStructureVos.getComponentId());
                            } else {
                                oldMstComponentStructure.setComponentId(null);
                            }

                            //ParentComponentId ParentComponentcode
                            if (StringUtils.isNotEmpty(mstComponentStructureVos.getParentComponentId())) {
                                oldMstComponentStructure.setParentComponentId(mstComponentStructureVos.getParentComponentId());
                            } else {
                                oldMstComponentStructure.setParentComponentId(null);
                            }

                            //RootComponentId RootComponentcode
                            if (StringUtils.isNotEmpty(mstComponentStructureVos.getRootComponentId())) {
                                oldMstComponentStructure.setRootComponentId(mstComponentStructureVos.getRootComponentId());
                            } else {
                                oldMstComponentStructure.setRootComponentId(null);
                            }
                            //Quantity
                            oldMstComponentStructure.setQuantity(mstComponentStructureVos.getQuantity());

                            oldMstComponentStructure.setUpdateDate(sysDate);
                            oldMstComponentStructure.setUpdateUserUuid(loginUser.getUserUuid());

                            //update
                            entityManager.merge(oldMstComponentStructure);
                            break;
                        }
                        //4:add 
                        case "4": {

                            MstComponentStructure oldMstComponentStructure = checkMstComponentStructureByUK(
                                    mstComponentStructureVos.getComponentId(),
                                    mstComponentStructureVos.getRootComponentId(),
                                    mstComponentStructureVos.getParentComponentId());

                            if (oldMstComponentStructure != null) {
                                //Quantity
                                oldMstComponentStructure.setQuantity(mstComponentStructureVos.getQuantity());
                                oldMstComponentStructure.setUuid(oldMstComponentStructure.getUuid());
                                oldMstComponentStructure.setUpdateDate(sysDate);
                                oldMstComponentStructure.setUpdateUserUuid(loginUser.getUserUuid());
                                entityManager.merge(oldMstComponentStructure);
                            } else {
                                //新規
                                mstComponentStructure = new MstComponentStructure();
                                //componentId componentcode
                                if (StringUtils.isNotEmpty(mstComponentStructureVos.getComponentId())) {
                                    mstComponentStructure.setComponentId(mstComponentStructureVos.getComponentId());
                                } else {
                                    mstComponentStructure.setComponentId(null);
                                }

                                //ParentComponentId ParentComponentcode
                                if (StringUtils.isNotEmpty(mstComponentStructureVos.getParentComponentId())) {
                                    mstComponentStructure.setParentComponentId(mstComponentStructureVos.getParentComponentId());
                                } else {
                                    mstComponentStructure.setParentComponentId(null);
                                }

                                //RootComponentId RootComponentcode
                                if (StringUtils.isNotEmpty(mstComponentStructureVos.getRootComponentId())) {
                                    mstComponentStructure.setRootComponentId(mstComponentStructureVos.getRootComponentId());
                                } else {
                                    mstComponentStructure.setRootComponentId(null);
                                }
                                mstComponentStructure.setQuantity(mstComponentStructureVos.getQuantity());
                                mstComponentStructure.setCreateDate(sysDate);
                                mstComponentStructure.setCreateUserUuid(loginUser.getUserUuid());
                                mstComponentStructure.setUpdateDate(sysDate);
                                mstComponentStructure.setUpdateUserUuid(loginUser.getUserUuid());
                                mstComponentStructure.setUuid(IDGenerator.generate());
                                entityManager.persist(mstComponentStructure);
                            }
                            break;
                        }
                        default:
                            break;
                    }
                }
            }
        }
        return new BasicResponse();
    }

    /**
     * 部品構成マスタ情報をCSV取込
     *
     * @param fileUuid
     * @param loginUser
     * @return
     */
    @Transactional
    public ImportResultResponse postMstComponentStructureCSV(String fileUuid, LoginUser loginUser) {

        long succeededCount;
        long addedCount = 0;
        long updatedCount = 0;
        long deletedCount = 0;
        long failedCount = 0;
        String logFileUuid = IDGenerator.generate();
        ImportResultResponse importResultResponse = new ImportResultResponse();
        String csvFile = FileUtil.getCsvFilePath(kartePropertyService, fileUuid);

        if (!csvFile.endsWith(CommonConstants.CSV)) {
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_wrong_csv_layout");
            importResultResponse.setErrorMessage(msg);
            return importResultResponse;
        }

        ArrayList readList = CSVFileUtil.readCsv(csvFile);

        if (readList.size() <= 1) {
            return importResultResponse;
        } else {
            FileUtil fileUtil = new FileUtil();

            List<String> dictKeyList = Arrays.asList("component_code", "root_component_code", "parent_component_code", "quantity",
                    "component_structure_registration", "row_number", "error", "error_detail", "msg_record_deleted", "msg_record_updated",
                    "db_process", "msg_record_added", "msg_error_wrong_csv_layout", "msg_error_over_length", "msg_error_value_invalid", "mst_error_record_not_found", "msg_error_not_null");
            Map<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(loginUser.getLangId(), dictKeyList);

            String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
            MstComponentStructure mstComponentStructure;

            for (int i = 1; i < readList.size(); i++) {
                ArrayList comList = (ArrayList) readList.get(i);
                if (comList.size() != 5) {
                    //エラー情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_code"), "", dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_wrong_csv_layout")));
                    failedCount = failedCount + 1;
                    continue;
                }

                mstComponentStructure = new MstComponentStructure();
                String componentCode = String.valueOf(comList.get(0)).trim();

                if (StringUtils.isNotEmpty(componentCode)) {
                    MstComponent mstComponent = mstComponentService.getMstComponent(componentCode);
                    if (mstComponent != null) {
                        mstComponentStructure.setComponentId(mstComponent.getId());
                    } else {
                        //エラー情報をログファイルに記入
                        failedCount = failedCount + 1;
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_code"), componentCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                        continue;
                    }
                } else {
                    //部品コードが空白はエラー
                    failedCount = failedCount + 1;
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_code"), componentCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                    continue;
                }

                String rootComponentCode = String.valueOf(comList.get(2)).trim();
                if (StringUtils.isNotEmpty(rootComponentCode)) {
                    MstComponent rootMstComponent = mstComponentService.getMstComponent(rootComponentCode);
                    if (rootMstComponent != null) {
                        mstComponentStructure.setRootComponentId(rootMstComponent.getId());
                    } else {
                        //エラー情報をログファイルに記入
                        failedCount = failedCount + 1;
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("root_component_code"), rootComponentCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                        continue;
                    }
                } else {
                    //製品部品コードが空白はエラー
                    failedCount = failedCount + 1;
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("root_component_code"), rootComponentCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                    continue;
                }

                String parentComponentCode = String.valueOf(comList.get(1)).trim();
                if (StringUtils.isNotEmpty(parentComponentCode)) {
                    MstComponent parentMstComponent = mstComponentService.getMstComponent(parentComponentCode);
                    if (parentMstComponent != null) {
                        mstComponentStructure.setParentComponentId(parentMstComponent.getId());
                    } else {
                        //エラー情報をログファイルに記入
                        failedCount = failedCount + 1;
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("parent_component_code"), parentComponentCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                        continue;
                    }
                } else if (!rootComponentCode.equals(componentCode)) {
                    //部品コードと製品部品コードが違う場合、親部品コードが空白はエラー
                    failedCount = failedCount + 1;
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("parent_component_code"), parentComponentCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                    continue;
                }
                String quantity = String.valueOf(comList.get(3)).trim();
                try {
                    if (StringUtils.isEmpty(quantity)) {
                        quantity = "0";
                        int formatQuantity = Integer.parseInt(quantity);
                        mstComponentStructure.setQuantity(formatQuantity);
                        break;
                    }

                    int formatQuantity = Integer.parseInt(quantity);
                    mstComponentStructure.setQuantity(formatQuantity);

                    if (quantity.length() > 9) {
                        failedCount = failedCount + 1;
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("quantity"), quantity, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                        continue;
                    }

                } catch (NumberFormatException e) {
                    failedCount = failedCount + 1;
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("quantity"), quantity, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_value_invalid")));
                    continue;
                }

                // delete
                String deleteFlag = String.valueOf(comList.get(4)).trim();
                if ("1".equals(deleteFlag)) {
                    MstComponentStructure oldMstComponentStructure = checkMstComponentStructureByUK(mstComponentStructure.getComponentId(),
                            mstComponentStructure.getRootComponentId(),
                            mstComponentStructure.getParentComponentId());
                    if (oldMstComponentStructure != null) {
                        deleteMstComponentStructure(oldMstComponentStructure.getUuid());
                    }
                    //エラー情報をログファイルに記入
                    deletedCount = deletedCount + 1;
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("parent_component_code"), parentComponentCode, dictMap.get("error"), 0, dictMap.get("db_process"), dictMap.get("msg_record_deleted")));
                    continue;
                }

                MstComponentStructure oldMstComponentStructure = checkMstComponentStructureByUK(mstComponentStructure.getComponentId(),
                        mstComponentStructure.getRootComponentId(),
                        mstComponentStructure.getParentComponentId());
                Date sysDate = new Date();
                if (oldMstComponentStructure != null) {
                    oldMstComponentStructure.setQuantity(mstComponentStructure.getQuantity());
                    oldMstComponentStructure.setUpdateDate(sysDate);
                    oldMstComponentStructure.setUpdateUserUuid(loginUser.getUserUuid());
                    entityManager.merge(oldMstComponentStructure);
                    updatedCount = updatedCount + 1;
                    String dataAdd = componentCode + "," + parentComponentCode + "," + rootComponentCode;//record  added  to use
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_code").concat("," + dictMap.get("parent_component_code")).concat("," + dictMap.get("root_component_code")), dataAdd, dictMap.get("error"), 0, dictMap.get("db_process"), dictMap.get("msg_record_updated")));

                } else {
                    //新規
                    mstComponentStructure.setCreateDate(sysDate);
                    mstComponentStructure.setCreateUserUuid(loginUser.getUserUuid());
                    mstComponentStructure.setUpdateDate(sysDate);
                    mstComponentStructure.setUpdateUserUuid(loginUser.getUserUuid());
                    mstComponentStructure.setUuid(IDGenerator.generate());
                    entityManager.persist(mstComponentStructure);
                    addedCount = addedCount + 1;
                    String dataAdd = componentCode + "," + parentComponentCode + "," + rootComponentCode;//record  added  to use
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_code").concat("," + dictMap.get("parent_component_code")).concat("," + dictMap.get("root_component_code")), dataAdd, dictMap.get("error"), 0, dictMap.get("db_process"), dictMap.get("msg_record_added")));

                }
            }

            // リターン情報
            succeededCount = addedCount + updatedCount + deletedCount;
            importResultResponse.setTotalCount(readList.size() - 1);
            importResultResponse.setSucceededCount(succeededCount);
            importResultResponse.setAddedCount(addedCount);
            importResultResponse.setUpdatedCount(updatedCount);
            importResultResponse.setDeletedCount(deletedCount);
            importResultResponse.setFailedCount(failedCount);
            importResultResponse.setLog(logFileUuid);

            //アップロードログをテーブルに書き出し
            TblCsvImport tblCsvImport = new TblCsvImport();
            tblCsvImport.setImportUuid(IDGenerator.generate());
            tblCsvImport.setImportUserUuid(loginUser.getUserUuid());
            tblCsvImport.setImportDate(new Date());
            tblCsvImport.setImportTable("mst_component_structure");
            TblUploadFile tblUploadFile = new TblUploadFile();
            tblUploadFile.setFileUuid(fileUuid);
            tblCsvImport.setUploadFileUuid(tblUploadFile);
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId(CommonConstants.FUN_ID_COMPONENT_STRUCTURE);
            tblCsvImport.setFunctionId(mstFunction);
            tblCsvImport.setRecordCount(readList.size() - 1);
            tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
            tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
            tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
            tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
            tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
            tblCsvImport.setLogFileUuid(logFileUuid);

            tblCsvImport.setLogFileName(FileUtil.getLogFileName(dictMap.get("component_structure_registration")));

            tblCsvImportService.createCsvImpor(tblCsvImport);
        }

        return importResultResponse;

    }

}
