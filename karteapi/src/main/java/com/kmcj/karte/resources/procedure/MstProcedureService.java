/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.procedure;

import com.kmcj.karte.resources.work.*;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponents;
import com.kmcj.karte.resources.component.company.MstComponentCompanyVo;
import com.kmcj.karte.resources.component.company.MstComponentCompanyVoList;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.installationsite.MstInstallationSite;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 * 工程マスタ（部品ごとの製造手順）サービス
 *
 * @author t.ariki
 */
@Dependent
public class MstProcedureService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

//    @Inject
//    MstComponentService mstComponentService;
    
    private static final String FINAL_FLAG_CHECKED = "1";

    /**
     * 最初の工程取得
     *
     * @param componentId
     * @return
     */
    public MstProcedureList getFirstProcedure(String componentId) {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT mstProcedure FROM MstProcedure mstProcedure WHERE 1=1 "
        );
        sql.append(" AND mstProcedure.seq = :seq ");
        sql.append(" AND mstProcedure.componentId = :componentId ");
        sql.append(" AND mstProcedure.externalFlg = :externalFlg ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("seq", 1);
        query.setParameter("componentId", componentId);
        query.setParameter("externalFlg", 0);
        List list = query.getResultList();
        MstProcedureList response = new MstProcedureList();
        response.setMstProcedures(list);
        return response;
    }
    
    /**
     * その部品の最初の部品工程を取得
     * @param componentId
     * @return 
     */
    public MstProcedure getFirstMstProcedure(String componentId) {
        MstProcedureList list = this.getFirstProcedure(componentId);
        if (list.getMstProcedures() != null && list.getMstProcedures().size() > 0) {
            return (MstProcedure)list.getMstProcedures().get(0);
        }
        else {
            return null;
        }
    }

    /**
     * 最終工程工程
     *
     * @param componentId
     * @return
     */
    public MstProcedureList getFinalProcedure(String componentId) {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT mstProcedure FROM MstProcedure mstProcedure WHERE 1=1 "
        );
        sql.append(" AND mstProcedure.finalFlg = :finalFlg ");
        sql.append(" AND mstProcedure.componentId = :componentId ");
        sql.append(" AND mstProcedure.externalFlg = :externalFlg ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("finalFlg", 1);
        query.setParameter("componentId", componentId);
        query.setParameter("externalFlg", 0);
        List list = query.getResultList();
        MstProcedureList response = new MstProcedureList();
        response.setMstProcedures(list);
        return response;
    }
    
    /**
     * 最終工程取得(1レコードのみ返す)
     * @param componentId
     * @return 
     */
    public MstProcedure getFinalProcedureByComponentId(String componentId) {
        MstProcedureList list = getFinalProcedure(componentId);
        if (list != null && list.getMstProcedures() != null && list.getMstProcedures().size() > 0) {
            return list.getMstProcedures().get(0);
        }
        else {
            return null;
        }
    }
    
    /**
     * 最大工番取得
     *
     * @param componentId
     * @return
     */
    public MstProcedure getMaxProcedureCode(String componentId) {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT mstProcedure FROM MstProcedure mstProcedure WHERE "
        );
        sql.append(" mstProcedure.componentId = :componentId ");
        sql.append(" AND mstProcedure.externalFlg = :externalFlg ");
        sql.append(" ORDER BY mstProcedure.procedureCode DESC ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("componentId", componentId);
        query.setParameter("externalFlg", 0);
        
        List<MstProcedure> list = query.getResultList();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
    
    /**
     * 当該工番の前工番取得
     *
     * @param componentId
     * @param procedureCode
     * @return
     */
    public MstProcedure getPrevProcedureCode(String componentId, String procedureCode) {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT mstProcedure FROM MstProcedure mstProcedure WHERE "
        );
        sql.append(" mstProcedure.componentId = :componentId ");
        sql.append(" AND mstProcedure.procedureCode < :procedureCode ");
        sql.append(" AND mstProcedure.externalFlg = :externalFlg ");
        sql.append(" ORDER BY mstProcedure.procedureCode DESC ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("componentId", componentId);
        query.setParameter("procedureCode", procedureCode);
        query.setParameter("externalFlg", 0);
        
        List<MstProcedure> list = query.getResultList();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 工程マスタ（部品ごとの製造手順）リスト取得(部品ID指定)
     *
     * @param componentId
     * @return
     */
    public MstProcedureList getComponentProcedures(String componentId) {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT mstProcedure FROM MstProcedure mstProcedure "
                // // 20171101 在庫により追加 S
                + " LEFT JOIN FETCH mstProcedure.mstInstallationSite mstInstallationSite "
                // // 20171101 在庫により追加 E
                + " WHERE 1=1 "
        );
        sql.append(" AND mstProcedure.componentId = :componentId ");
        sql.append(" AND mstProcedure.externalFlg = :externalFlg ");
        sql.append(" ORDER BY mstProcedure.seq ASC ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("componentId", componentId);
        query.setParameter("externalFlg", 0);
        List list = query.getResultList();
        MstProcedureList response = new MstProcedureList();
        response.setMstProcedures(list);
        return response;
    }

    /**
     * 工程マスタ（部品ごとの製造手順）リスト取得
     *
     * @return
     */
    public MstProcedureList getAllProcedures() {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT mstProcedure FROM MstProcedure mstProcedure WHERE 1=1 "
        );
        sql.append(" AND mstProcedure.externalFlg = :externalFlg ");
        sql.append(" ORDER BY mstProcedure.componentId ASC,  mstProcedure.seq ASC ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("externalFlg", 0);
        List list = query.getResultList();
        MstProcedureList response = new MstProcedureList();
        response.setMstProcedures(list);
        return response;
    }

    /**
     * 部品IDと工番で取得
     *
     * @param componentId
     * @param procedureCode
     * @return
     */
    public MstProcedure getMstProcedureByComponentIdAndProcedureCode(String componentId, String procedureCode) {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT mstProcedure FROM MstProcedure mstProcedure WHERE 1=1 "
        );
        sql.append(" AND mstProcedure.componentId = :componentId ");
        sql.append(" AND mstProcedure.procedureCode = :procedureCode ");
        sql.append(" AND mstProcedure.externalFlg = :externalFlg ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("componentId", componentId);
        query.setParameter("procedureCode", procedureCode);
        query.setParameter("externalFlg", 0);
        List list = query.getResultList();
        if (list.size() > 0) {
            MstProcedure mstProcedure = (MstProcedure) list.get(0);
            return mstProcedure;
        } else {
            return null;
        }
    }

    /**
     * 部品IDとSEQで取得
     *
     * @param componentId
     * @param seq
     * @return
     */
    public MstProcedure getMstProcedureByComponentIdAndSeq(String componentId, int seq) {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT mstProcedure FROM MstProcedure mstProcedure WHERE 1=1 "
        );
        sql.append(" AND mstProcedure.componentId = :componentId ");
        sql.append(" AND mstProcedure.seq = :seq ");
        sql.append(" AND mstProcedure.externalFlg = :externalFlg ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("componentId", componentId);
        query.setParameter("seq", seq);
        query.setParameter("externalFlg", 0);
        try {
            MstProcedure mstProcedure = (MstProcedure) query.getSingleResult();
            return mstProcedure;
        } catch (NoResultException e) {
            return null;
        }
    }

    private BasicResponse setApplicationError(BasicResponse response, LoginUser loginUser, String dictKey, String errorContentsName) {
        setBasicResponseError(
                response, true, ErrorMessages.E201_APPLICATION, mstDictionaryService.getDictionaryValue(loginUser.getLangId(), dictKey)
        );
        String logMessage = response.getErrorMessage() + ":" + errorContentsName;
        Logger.getLogger(TblWorkResource.class.getName()).log(Level.FINE, logMessage);
        return response;
    }

    private BasicResponse setBasicResponseError(BasicResponse response, boolean error, String errorCode, String errorMessage) {
        response.setError(error);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        return response;
    }


    /**
     * M0018 工程登録 画面に入力された値で工程マスタへ追加・更新・削除を行う。
     * @param procedureVo
     * @param loginUser
     * @return 
     */
    @Transactional
    public BasicResponse postProcedures(MstProcedureVo procedureVo, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        FileUtil fu = new FileUtil();
        if (null != procedureVo && null != procedureVo.getMstProcedureVos() && procedureVo.getMstProcedureVos().size() > 0) {
            String componentId = "";
            
            boolean isSetFinal = false;
            for (MstProcedureVo aVo : procedureVo.getMstProcedureVos()) {
                if (componentId.equals("")){
                    componentId = aVo.getComponentId();
                }
                if (null != aVo.getOperationFlag() && aVo.getOperationFlag().equals("2")) {
                    continue;
                } else {
                    MstProcedure aProcedure = null;
                    if (null != aVo.getOperationFlag() && aVo.getOperationFlag().equals("1")) {
                        //delete                    
                        if (null != aVo.getId() && !aVo.getId().trim().equals("")) {
                            aProcedure = entityManager.find(MstProcedure.class, aVo.getId());
                            if (null != aProcedure) {
                                entityManager.remove(aProcedure);
                            }
                        } else {
                            response.setError(true);
                            response.setErrorCode(ErrorMessages.E201_APPLICATION);
                            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
                            return response;
                        }
                    } else if (null != aVo.getId() && null != aVo.getOperationFlag() && aVo.getOperationFlag().equals("3")) {
                        //modify
                        if (FINAL_FLAG_CHECKED.equals(aVo.getFinalFlg())) {
                            isSetFinal = true;
                        }
                        aProcedure = entityManager.find(MstProcedure.class, aVo.getId());
                        if (null == aProcedure) {
                            response.setError(true);
                            response.setErrorCode(ErrorMessages.E201_APPLICATION);
                            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
                            return response;
                        }
                    } else if (null != aVo.getOperationFlag() && aVo.getOperationFlag().equals("4")) {
                        //create
                        if (FINAL_FLAG_CHECKED.equals(aVo.getFinalFlg())) {
                            isSetFinal = true;
                        }
                        aProcedure = new MstProcedure();
                        aProcedure.setId(IDGenerator.generate());
                        aProcedure.setComponentId(aVo.getComponentId());
                    }
                    if(aVo.getProcedureCode() == null || "".equals(aVo.getProcedureCode())){
                        response.setError(true);
                        response.setErrorCode(ErrorMessages.E201_APPLICATION);
                        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
                        return response;
                    }else if(fu.maxLangthCheck(aVo.getProcedureCode(), 45)){
                        response.setError(true);
                        response.setErrorCode(ErrorMessages.E201_APPLICATION);
                        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length"));
                        return response;
                    }
                    
                    if(fu.maxLangthCheck(aVo.getProcedureName(), 45)){
                        response.setError(true);
                        response.setErrorCode(ErrorMessages.E201_APPLICATION);
                        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length"));
                        return response;
                    }
                    
                    if (null != aVo.getSeq() && !aVo.getSeq().trim().equals("")) {
                        aProcedure.setSeq(Integer.parseInt(aVo.getSeq()));
                    }
                    aProcedure.setProcedureCode(aVo.getProcedureCode().trim());
                    aProcedure.setProcedureName(aVo.getProcedureName().trim());
                    // 20171101 在庫により追加 S
                    if (StringUtils.isNotEmpty(aVo.getInstallationSiteId())) {
                        aProcedure.setInstallationSiteId(aVo.getInstallationSiteId());
                    } else {
                        aProcedure.setInstallationSiteId(null);
                    }
                    // 20171101 在庫により追加 E
                    aProcedure.setExternalFlg(0);
                    aProcedure.setFinalFlg(Integer.valueOf(aVo.getFinalFlg()));
                    aProcedure.setCreateDate(new Date());
                    aProcedure.setCreateUserUuid(loginUser.getUserUuid());
                    aProcedure.setUpdateDate(new Date());
                    aProcedure.setUpdateUserUuid(loginUser.getUserUuid());

                    if (null != aVo.getId() && !aVo.getId().trim().equals("") && null != aVo.getOperationFlag() && aVo.getOperationFlag().equals("3")) {
                        //modify
                            entityManager.merge(aProcedure);
                    } else if (null != aVo.getOperationFlag() && aVo.getOperationFlag().equals("4")) {
                        //create
                        if(!checkProcedure(aVo.getComponentId(),aVo.getProcedureCode())){
                            entityManager.persist(aProcedure);
                        }else{
                            response.setError(true);
                            response.setErrorCode(ErrorMessages.E201_APPLICATION);
                            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
                            return response;
                        }
                    }
                }
            }
            
            if (!isSetFinal) {
                updateFinalFlag(componentId);
            }
        }
        return response;
    }
    
    //最新seq的记录设置最终flag   
    @Transactional
    public void updateFinalFlag(String componentId) {
        if (null != componentId && !componentId.trim().equals("")) {
            MstProcedure mstProcedure = null;
            List<MstProcedure> mstProcedures = entityManager.createQuery("SELECT m FROM MstProcedure m WHERE m.componentId = :componentId AND m.externalFlg = 0 order by m.seq desc")
                    .setParameter("componentId", componentId)
                    .setMaxResults(1)
                    .getResultList();
            if (null != mstProcedures && !mstProcedures.isEmpty() && null != (mstProcedure = mstProcedures.get(0))) {
                entityManager.createQuery("UPDATE MstProcedure m SET m.finalFlg = 0,m.updateDate = :updateDate where m.componentId = :componentId AND m.externalFlg = 0 ")
                        .setParameter("componentId", componentId)
                        .setParameter("updateDate", new Date())
                        .executeUpdate();
                entityManager.createQuery("UPDATE MstProcedure m SET m.finalFlg = 1,m.updateDate = :updateDate where m.id = :id AND m.externalFlg = 0 ")
                        .setParameter("id", mstProcedure.getId())
                        .setParameter("updateDate", new Date())
                        .executeUpdate();
            }
        }
    }
    
    /**
     * 工程マスタ情報をCSVファイルに出力する
     *
     * @param queryVo
     * @param loginUser
     * @return
     */
    public FileReponse getMstProceduresOutputCsv(MstComponents queryVo, LoginUser loginUser) {

        ArrayList<ArrayList> procOutList = new ArrayList<>();
        ArrayList tempOutList;
        String langId = loginUser.getLangId();
        FileReponse fr = new FileReponse();

        StringBuilder sql;
        if (null != queryVo.getProcessesNotRegistered() && !queryVo.getProcessesNotRegistered().trim().equals("") && queryVo.getProcessesNotRegistered().equals("1")) {
            sql = new StringBuilder("SELECT c FROM MstComponent c where NOT EXISTS ( SELECT p from MstProcedure p WHERE p.componentId = c.id AND p.externalFlg = 0 ) ");
        } else {
            //sql = new StringBuilder("SELECT p FROM MstProcedure p left join MstComponent c on c.id = p.componentId WHERE 1=1 And p.externalFlg = 0 ");
            sql = new StringBuilder("SELECT p FROM MstProcedure p "
                    + " left join MstComponent c on c.id = p.componentId "
                    + " left join MstInstallationSite m on m.id = p.installationSiteId "
                    + " WHERE 1=1 And p.externalFlg = 0 ");
        }

        if (null != queryVo.getProcessesNotRegistered() && !queryVo.getProcessesNotRegistered().trim().equals("") && queryVo.getProcessesNotRegistered().equals("1")) {
            if (null != queryVo.getComponentCode() && !queryVo.getComponentCode().trim().equals("")) {
                sql.append(" and c.componentCode LIKE :componentCode ");
            }
            if (null != queryVo.getComponentName() && !queryVo.getComponentName().trim().equals("")) {
                sql.append(" and c.componentName like :componentName ");
            }
            if (null != queryVo.getComponentType() && !queryVo.getComponentType().trim().equals("") && !queryVo.getComponentType().equals("0")) {
                sql.append(" and c.componentType = :componentType ");
            }
            sql.append(" order by c.componentCode ");//componentCodeの昇順
            Query query = entityManager.createQuery(sql.toString());
            if (null != queryVo.getComponentCode() && !queryVo.getComponentCode().trim().equals("")) {
                query.setParameter("componentCode", "%" + queryVo.getComponentCode().trim() + "%");
            }
            if (null != queryVo.getComponentName() && !queryVo.getComponentName().trim().equals("")) {
                query.setParameter("componentName", "%" + queryVo.getComponentName().trim() + "%");
            }
            if (null != queryVo.getComponentType() && !queryVo.getComponentType().trim().equals("") && !queryVo.getComponentType().equals("0")) {
                query.setParameter("componentType", Integer.parseInt(queryVo.getComponentType()));
            }
            List<MstComponent> list = query.getResultList();

            if (null != list && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    MstComponent csvMstProcedure = list.get(i);
                    tempOutList = new ArrayList<>();
                    tempOutList.add(csvMstProcedure.getComponentCode());
                    tempOutList.add(csvMstProcedure.getComponentName());
                    tempOutList.add("");
                    tempOutList.add("");
                    tempOutList.add("");
                    tempOutList.add("");
                    tempOutList.add("");
                    tempOutList.add("");
                    tempOutList.add("");// delete
                    procOutList.add(tempOutList);
                }
            }
        } else {
            if (null != queryVo.getComponentCode() && !queryVo.getComponentCode().trim().equals("")) {
                sql.append(" and c.componentCode LIKE :componentCode ");
            }
            if (null != queryVo.getComponentName() && !queryVo.getComponentName().trim().equals("")) {
                sql.append(" and c.componentName like :componentName ");
            }
            if (null != queryVo.getComponentType() && !queryVo.getComponentType().trim().equals("") && !queryVo.getComponentType().equals("0")) {
                sql.append(" and c.componentType = :componentType ");
            }
            sql.append(" order by c.componentCode,p.seq asc ");//componentCodeの昇順

            Query query = entityManager.createQuery(sql.toString());
            if (null != queryVo.getComponentCode() && !queryVo.getComponentCode().trim().equals("")) {
                query.setParameter("componentCode", "%" + queryVo.getComponentCode().trim() + "%");
            }
            if (null != queryVo.getComponentName() && !queryVo.getComponentName().trim().equals("")) {
                query.setParameter("componentName", "%" + queryVo.getComponentName().trim() + "%");
            }
            if (null != queryVo.getComponentType() && !queryVo.getComponentType().trim().equals("") && !queryVo.getComponentType().equals("0")) {
                query.setParameter("componentType", Integer.parseInt(queryVo.getComponentType()));
            }
            List<MstProcedure> list = query.getResultList();
            //明細データを取得
            if (null != list && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    MstProcedure csvMstProcedure = list.get(i);
                    tempOutList = new ArrayList<>();
                    if (null != csvMstProcedure.getMstComponent()) {
                        tempOutList.add(csvMstProcedure.getMstComponent().getComponentCode());
                        tempOutList.add(csvMstProcedure.getMstComponent().getComponentName());
                        tempOutList.add("" + csvMstProcedure.getFinalFlg());
                        tempOutList.add("" + csvMstProcedure.getSeq());
                        tempOutList.add(csvMstProcedure.getProcedureCode());
                        tempOutList.add(csvMstProcedure.getProcedureName());
                        MstInstallationSite mstInstallationSite = csvMstProcedure.getMstInstallationSite();
                        if (mstInstallationSite != null) {
                            tempOutList.add(mstInstallationSite.getInstallationSiteCode());
                            tempOutList.add(mstInstallationSite.getInstallationSiteName());
                        } else {
                            tempOutList.add("");
                            tempOutList.add("");
                        }
                        tempOutList.add("");// delete
                        procOutList.add(tempOutList);
                    }
                }
            }
        }

        /**
         * Header
         */
        ArrayList dictKeyList = new ArrayList();

        dictKeyList.add("component_code");
        dictKeyList.add("component_name");
        dictKeyList.add("final_procedure");
        dictKeyList.add("category_seq");
        dictKeyList.add("procedure_code");
        dictKeyList.add("procedure_name");
        dictKeyList.add("instration_site_code");
        dictKeyList.add("instration_site");
        dictKeyList.add("delete_record");

        Map<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(langId, dictKeyList);

        /*Head*/
        ArrayList headList = new ArrayList();
        headList.add(dictMap.get("component_code"));
        headList.add(dictMap.get("component_name"));

        //工程Header
        headList.add(dictMap.get("final_procedure"));
        headList.add(dictMap.get("category_seq"));
        headList.add(dictMap.get("procedure_code"));
        headList.add(dictMap.get("procedure_name"));
        headList.add(dictMap.get("instration_site_code"));
        headList.add(dictMap.get("instration_site"));
        // Header準備完了
        headList.add(dictMap.get("delete_record"));

        ArrayList<ArrayList> gLineList = new ArrayList<>();
        gLineList.add(headList);

        // 出力データ準備
        for (int i = 0; i < procOutList.size(); i++) {
            gLineList.add(procOutList.get(i));
        }

        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);

        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.MST_PROCEDURE);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_PROCEDURE);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, CommonConstants.MST_PROCEDURE);
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }

    /**
     * 工程マスタデータがチェック
     *
     * @param componentId
     * @param strProcedureCode
     * @param strseq
     * @return
     */
    public boolean checkMstProcedure(String componentId, String strProcedureCode, String strseq) {

        String sql = " SELECT m FROM MstProcedure m WHERE  m.componentId = :componentId And m.procedureCode = :procedureCode And m.seq = :seq and m.externalFlg = 0 ";
        Query query = entityManager.createQuery(sql);
        query.setParameter("componentId", componentId);
        query.setParameter("procedureCode", strProcedureCode);
        query.setParameter("seq", Integer.parseInt(strseq));
        
        try {
            query.getSingleResult();
        } catch (NoResultException e) {
            return false;//データがない
        }
        return true;//データが存在
    }

    /**
     * 工程マスタデータIDが取得
     *
     * @param componentId
     * @param strProcedureCode
     * @param strseq
     * @return 
     */
    public String getMstProcedureId(String componentId,String strProcedureCode, String strseq){
        String procedureId = "";
        String sql = " SELECT m FROM MstProcedure m WHERE  m.componentId = :componentId And m.procedureCode = :procedureCode And m.seq = :seq and m.externalFlg = 0 ";
        Query query = entityManager.createQuery(sql);
        query.setParameter("componentId", componentId);
        query.setParameter("procedureCode", strProcedureCode);
        query.setParameter("seq", Integer.parseInt(strseq));
        try{
            MstProcedure mstProcedure = (MstProcedure)query.getSingleResult();
            procedureId = mstProcedure.getId();
        } catch (NoResultException e) {
            procedureId = "";
        }
        return procedureId;
    }

    /**
     * CSVFILEチェック
     *
     * @param logParm
     * @param csvArray
     * @param userLangId
     * @param logFile
     * @param i
     * @return
     */
    public boolean checkCsvFileData(Map<String, String> logParm, String csvArray[], String userLangId, String logFile, int i) {

        //ログ出力内容を用意する
        String lineNo = logParm.get("lineNo");
        String componentName = logParm.get("componentName");
        String procedureName = logParm.get("procedureName");

        String error = logParm.get("error");
        String errorContents = logParm.get("errorContents");
        String nullMsg = logParm.get("nullMsg");
        String maxLangth = logParm.get("maxLangth");

        FileUtil fu = new FileUtil();
        String inComponentName = csvArray[1];
        if (inComponentName == null || "".equals(inComponentName)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentName, inComponentName, error, 1, errorContents, nullMsg));
            return false;
        }

        String inProcedureName = csvArray[5];
        if (fu.maxLangthCheck(inProcedureName, 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, procedureName, inProcedureName, error, 1, errorContents, maxLangth));
            return false;
        }
        return true;
    }

    /**
     * CSV入力Transactional
     *
     * @param id
     * @param readCsvInfo
     * @param operationFlag
     */
    @Transactional
    public void transactionalMstProcedure(String id, MstProcedure readCsvInfo, String operationFlag) {
        if (id != null && !"".equals(id) && "1".equals(operationFlag)) {
            entityManager.createNamedQuery("MstProcedure.deleteById").setParameter("id", id).setParameter("externalFlg", 0).executeUpdate();
        }
        if (readCsvInfo != null && "3".equals(operationFlag)) {
            entityManager.merge(readCsvInfo);
        }
        if (readCsvInfo != null && "4".equals(operationFlag)) {
            readCsvInfo.setExternalFlg(0);
            entityManager.persist(readCsvInfo);
        }
    }
    
    /**
     * 工程マスタデータチェック
     * @param componentId
     * @param strProcedureCode
     * @return 
     */
    public boolean checkProcedure(String componentId,String strProcedureCode){
        StringBuilder sql = new StringBuilder(" SELECT m FROM MstProcedure m WHERE ");
        sql.append(" m.componentId = :componentId AND m.procedureCode = :procedureCode ");
        sql.append(" AND m.externalFlg = :externalFlg ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("componentId", componentId);
        query.setParameter("procedureCode", strProcedureCode);
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        return query.getResultList().size() > 0;
    }
    
    
    /**
     * バッチで工程データを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    public MstProcedureList getExtProceduresByBatch(String latestExecutedDate) {
        MstProcedureList resList = new MstProcedureList();
        List<MstProcedureVo> resVo = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT distinct p FROM MstProcedure p where p.externalFlg = 0 ");
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (p.updateDate > :latestExecutedDate or p.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<MstProcedure> tmpList = query.getResultList();
        for (MstProcedure procedure : tmpList) {
            MstProcedureVo aRes = new MstProcedureVo();
            MstComponent mstComponent = null;
            if (null != (mstComponent = procedure.getMstComponent())){
                aRes.setComponentCode(mstComponent.getComponentCode());
            }
            procedure.setMstComponent(null);
            procedure.setTblProductionPlanCollection(null);
            
            aRes.setMstProcedure(procedure);            
            resVo.add(aRes);
        }
        resList.setMstProcedureVos(resVo);
        return resList;
    }
    
    /**
     * データが連番の昇順
     * @param whereKey 
     */
    @Transactional
    public void updateSeq(List<String> whereKey){
        
        for(String componentId : whereKey){
            List list = getProcedureDate(componentId);
            for(int j=0;j<list.size();j++){
                MstProcedure mstProcedure = (MstProcedure)list.get(j);
                mstProcedure.setSeq((j+1));
                entityManager.merge(mstProcedure);
            }
        }
    }
    
    /**
     * 工程マスタデータを取得
     * @param componentId
     * @return 
     */
    public List getProcedureDate(String componentId){
        StringBuilder sql = new StringBuilder(" SELECT m FROM MstProcedure m WHERE 1=1 ");
        sql.append(" AND m.componentId = :componentId AND m.externalFlg = 0 ORDER BY m.componentId,m.procedureCode,m.seq ASC");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("componentId", componentId);
        List list = query.getResultList();
        return list;
    }

    /**
     * バッチで工程データを更新
     *
     * @param procedureVos
     * @param mstComponentCompanyVoList
     * @return
     */
    @Transactional
    public BasicResponse updateExtProceduresByBatch(List<MstProcedureVo> procedureVos, MstComponentCompanyVoList mstComponentCompanyVoList) {
        BasicResponse response = new BasicResponse();

        if (procedureVos != null && !procedureVos.isEmpty()) {

            for (MstProcedureVo procedureVo : procedureVos) {

                //該当工程が存在かチェックする
                List<MstProcedure> oldProcedures = entityManager.createQuery("SELECT mstProcedure FROM MstProcedure mstProcedure WHERE 1=1 AND mstProcedure.id = :id ")
                        .setParameter("id", procedureVo.getMstProcedure().getId())
                        .setMaxResults(1)
                        .getResultList();

                MstProcedure newProcedure;
                if (null != oldProcedures && !oldProcedures.isEmpty()) {
                    newProcedure = oldProcedures.get(0);
                } else {
                    newProcedure = new MstProcedure();
                    newProcedure.setId(procedureVo.getMstProcedure().getId());
                }

                newProcedure.setProcedureCode(procedureVo.getMstProcedure().getProcedureCode());
                newProcedure.setProcedureName(procedureVo.getMstProcedure().getProcedureName());
                newProcedure.setSeq(procedureVo.getMstProcedure().getSeq());
                try {
                    newProcedure.setFinalFlg(procedureVo.getMstProcedure().getFinalFlg());
                } catch (NumberFormatException e) {
                    newProcedure.setFinalFlg(0);
                }
                newProcedure.setExternalFlg(1);

                newProcedure.setCreateDate(procedureVo.getMstProcedure().getCreateDate());
                newProcedure.setCreateUserUuid(procedureVo.getMstProcedure().getCreateUserUuid());
                newProcedure.setUpdateDate(new Date());
                newProcedure.setUpdateUserUuid(procedureVo.getMstProcedure().getUpdateUserUuid());

                //自社の部品コード
                Query query = entityManager.createNamedQuery("MstComponent.findByComponentCode");
                query.setParameter("componentCode", procedureVo.getComponentCode());
                List<MstComponent> mstComponentList = query.getResultList();

                //自社の部品コードと一致である場合
                if (mstComponentList != null && mstComponentList.size() > 0) {
                    newProcedure.setComponentId(mstComponentList.get(0).getId());

                    if (null != oldProcedures && !oldProcedures.isEmpty()) {
                        entityManager.merge(newProcedure);
                    } else {
                        entityManager.persist(newProcedure);
                    }

                } else //自社の部品コードと先方部品コード一致である場合
                {
                    if (mstComponentCompanyVoList.getMstComponentCompanyVos().size() > 0) {
                        for (MstComponentCompanyVo mstComponentCompanyVo : mstComponentCompanyVoList.getMstComponentCompanyVos()) {
                            if (StringUtils.isNotEmpty(procedureVo.getComponentCode()) && StringUtils.isNotEmpty(mstComponentCompanyVo.getOtherComponentCode())) {
                                if (procedureVo.getComponentCode().equals(mstComponentCompanyVo.getComponentCode())) {
                                    // 先方部品により自社の部品IDを置換
                                    query.setParameter("componentCode", mstComponentCompanyVo.getOtherComponentCode());
                                    List<MstComponent> otherMstComponentList = query.getResultList();
                                    newProcedure.setComponentId(otherMstComponentList.get(0).getId());

                                    if (null != oldProcedures && !oldProcedures.isEmpty()) {
                                        entityManager.merge(newProcedure);
                                    } else {
                                        entityManager.persist(newProcedure);
                                    }

                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        response.setError(false);

        return response;
    }

    /**
     * 指定部品に紐付くすべての工程を取得
     * @param componentId
     * @return
     */
    public List<MstProcedureVo> getProcedureByComponentId(String componentId) {
        List<MstProcedureVo> mstProcedureVos = new ArrayList();
        MstProcedureVo mstProcedureVo;
        StringBuilder sql;
        sql = new StringBuilder("SELECT DISTINCT mstProcedure FROM MstMachineProcedure mstMachineProcedure "
                + " LEFT JOIN mstMachineProcedure.mstProcedure mstProcedure ");
        Query query = entityManager.createQuery(sql.toString());
        List<MstProcedure> list = query.getResultList();
        if (list != null && list.size() > 0) {
            for (MstProcedure mstProcedure : list) {
                mstProcedureVo = new MstProcedureVo();
                mstProcedureVo.setId(mstProcedure.getId());
                mstProcedureVo.setProcedureName(mstProcedure.getProcedureName());
                mstProcedureVos.add(mstProcedureVo);
            }
        }
        return mstProcedureVos;
    }

    /**
     * 部品工程IDから部品工程レコード取得
     * @param procedureId
     * @return 
     */
    public MstProcedure getMstProcedureById(String procedureId) {
        Query query = entityManager.createQuery(" SELECT t FROM MstProcedure t WHERE t.id = :id ");
        query.setParameter("id", procedureId);
        List<MstProcedure> list = query.getResultList();
        if (list != null && !list.isEmpty()) {
            return (MstProcedure)list.get(0);
        }
        else {
            return null;
        }
    }

}
