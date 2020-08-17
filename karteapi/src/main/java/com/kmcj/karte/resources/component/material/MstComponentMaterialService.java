/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.material;

import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.MstComponentList;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.material.MstMaterialList;
import com.kmcj.karte.resources.material.MstMaterialService;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.util.BeanCopyUtil;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
 * @author admin
 */
@Dependent
//@Transactional
public class MstComponentMaterialService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private MstComponentService mstComponentService;
    @Inject
    private MstMaterialService mstMaterialService;
    
    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("componentCode", " ORDER BY mc.componentCode ");// 部品コード
        orderKey.put("componentName", " ORDER BY mc.componentName ");// 部品名称
        orderKey.put("procedureCode", " ORDER BY m.proceduerCode ");// 部品工程番号
        orderKey.put("procedureName", " ORDER BY m1.procedureName ");// 部品工程名称
        orderKey.put("materialCode", " ORDER BY mm.materialCode ");// 材料コード
        orderKey.put("materialName", " ORDER BY mm.materialName ");// 材料名称
        orderKey.put("requiredQuantityMolecule", " ORDER BY m.numerator ");// 所要数量分子
        orderKey.put("requiredQuantityDenominator", " ORDER BY m.denominator ");// 所要数量分母
        orderKey.put("applicationStartDate", " ORDER BY m.startDate ");// 適用開始日
        orderKey.put("applicationEndDate", " ORDER BY m.endDate ");// 適用終了日
    }

    /**
     * 部品別材料件数を取得する
     *
     * @param componentCode
     * @param componentName
     * @return
     */
    CountResponse getComponentMaterialCount(String componentCode, String componentName) {
        List list = getSql(componentCode, componentName, "", "count");
        CountResponse count = new CountResponse();
        count.setCount(((Long) list.get(0)));
        return count;
    }

    /**
     * 部品別材料複数取得
     *
     * @param componentCode
     * @param componentName
     * @return
     */
    MstComponentMaterialList getComponentMaterials(String componentCode, String componentName) {
        List list = getSql(componentCode, componentName, "", "");

        MstComponentMaterialList response = new MstComponentMaterialList();

        List<MstComponentMaterialVo> tblMstComponentMaterialVoList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Object objs = list.get(i);
            Object[] obj = (Object[]) objs;
            MstComponentMaterial mstComponentMaterial = (MstComponentMaterial) obj[0];
            MstProcedure mstProcedure = (MstProcedure) obj[1];
            //MstComponentMaterial mstComponentMaterial = (MstComponentMaterial) list.get(i);
            MstComponentMaterialVo mstComponentMaterialVo = setMstComponentMaterialVo(mstComponentMaterial, mstProcedure);
            tblMstComponentMaterialVoList.add(mstComponentMaterialVo);
        }
        response.setMstComponentMaterialsList(tblMstComponentMaterialVoList);
        return response;
    }

    /**
     * 部品別材料Vo
     *
     * @param mstComponentMaterial
     * @param mstProcedure
     * @return
     */
    private MstComponentMaterialVo setMstComponentMaterialVo(MstComponentMaterial mstComponentMaterial, MstProcedure mstProcedure) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
        MstComponentMaterialVo mstComponentMaterialVo = new MstComponentMaterialVo();
        mstComponentMaterialVo.setId(mstComponentMaterial.getId());
        mstComponentMaterialVo.setComponentId(mstComponentMaterial.getComponentId());
        mstComponentMaterialVo.setMaterialId(mstComponentMaterial.getMaterialId());
        mstComponentMaterialVo.setProcedureCode(mstComponentMaterial.getProceduerCode() == null ? "" : mstComponentMaterial.getProceduerCode());
        mstComponentMaterialVo.setRequiredQuantityDenominator(mstComponentMaterial.getDenominator() == null ? "" : mstComponentMaterial.getDenominator().toString());
        mstComponentMaterialVo.setRequiredQuantityMolecule(mstComponentMaterial.getNumerator() == null ? "" : mstComponentMaterial.getNumerator().toString());
        mstComponentMaterialVo.setApplicationStartDate(mstComponentMaterial.getStartDate() == null ? "" : sdf.format(mstComponentMaterial.getStartDate()));
        mstComponentMaterialVo.setApplicationEndDate(mstComponentMaterial.getEndDate() == null ? "" : sdf.format(mstComponentMaterial.getEndDate()));

        if (mstComponentMaterial.getMstMaterial() != null) {
            mstComponentMaterialVo.setMaterialId(mstComponentMaterial.getMstMaterial().getId());
            mstComponentMaterialVo.setMaterialCode(mstComponentMaterial.getMstMaterial().getMaterialCode());
            mstComponentMaterialVo.setMaterialName(mstComponentMaterial.getMstMaterial().getMaterialName());
        } else {
            mstComponentMaterialVo.setMaterialId("");
            mstComponentMaterialVo.setMaterialCode("");
            mstComponentMaterialVo.setMaterialName("");
        }

        if (mstComponentMaterial.getMstComponent() != null) {
            mstComponentMaterialVo.setComponentId(mstComponentMaterial.getMstComponent().getId());
            mstComponentMaterialVo.setComponentCode(mstComponentMaterial.getMstComponent().getComponentCode());
            mstComponentMaterialVo.setComponentName(mstComponentMaterial.getMstComponent().getComponentName());
        } else {
            mstComponentMaterialVo.setComponentId("");
            mstComponentMaterialVo.setComponentCode("");
            mstComponentMaterialVo.setComponentName("");
        }

        if (mstProcedure != null) {
            mstComponentMaterialVo.setProcedureCodeSeq(String.valueOf(mstProcedure.getSeq()));
            mstComponentMaterialVo.setProcedureName(mstProcedure.getProcedureName());
        } else {
            mstComponentMaterialVo.setProcedureCodeSeq(null);
            mstComponentMaterialVo.setProcedureName("");
        }
        return mstComponentMaterialVo;
    }

    /**
     * 部品別材料sql
     *
     * @param componentCode
     * @param componentName
     * @param action
     * @return
     */
    private List getSql(String componentCode, String componentName, String id, String action) {
        StringBuilder sql;
        String strComponentCode = "";
        String strComponentName = "";
        String strId = "";

        if ("count".equals(action)) {//m.mstProcedure.procedureCode
            sql = new StringBuilder("SELECT count(m)  FROM MstComponentMaterial m   "
                    + " LEFT JOIN MstProcedure m1 ON  m.proceduerCode = m1.procedureCode AND m.componentId = m1.componentId  "
                    + "WHERE 1=1  "
            );

        } else {//m.componentId ASC,m.materialId ASC,
            sql = new StringBuilder("SELECT m,m1 FROM MstComponentMaterial m JOIN FETCH m.mstComponent JOIN FETCH m.mstMaterial "
                    + " LEFT JOIN MstProcedure m1 ON  m.proceduerCode = m1.procedureCode AND m.componentId = m1.componentId "
                    + "WHERE 1=1  "
            );
        }

        if (componentCode != null && !"".equals(componentCode)) {
            strComponentCode = componentCode.trim();
            sql.append(" and m.mstComponent.componentCode LIKE :componentCode ");
        }

        if (componentName != null && !"".equals(componentName)) {
            strComponentName = componentName.trim();
            sql.append(" and m.mstComponent.componentName like :componentName ");
        }

        if (id != null && !"".equals(id)) {
            strId = id.trim();
            sql.append(" and m.id = :id ");
        }

        sql.append(" ORDER BY  m.mstComponent.componentCode ASC ,m.proceduerCode ASC,m.mstMaterial.materialCode ASC ");
        Query query = entityManager.createQuery(sql.toString());

        if (componentCode != null && !"".equals(componentCode)) {
            query.setParameter("componentCode", "%" + strComponentCode + "%");
        }

        if (componentName != null && !"".equals(componentName)) {
            query.setParameter("componentName", "%" + componentName + "%");
        }

        if (id != null && !"".equals(id)) {
            query.setParameter("id", strId);
        }
        List list = query.getResultList();

        return list;
    }

    /**
     * 新規の部品別材
     *
     * @param mstComponentMaterialV0
     * @param loginUser
     */
    @Transactional
    public MstComponentMaterial createMstComponentMaterial(MstComponentMaterialVo mstComponentMaterialV0, LoginUser loginUser) {
        MstComponentMaterial mstComponentMaterial = new MstComponentMaterial();
        mstComponentMaterial.setId(mstComponentMaterialV0.getId());
        if (mstComponentMaterialV0.getComponentId() != null && !"".equals(mstComponentMaterialV0.getComponentId())) {
            mstComponentMaterial.setComponentId(mstComponentMaterialV0.getComponentId().trim());
        }

        if (mstComponentMaterialV0.getProcedureCode() != null && !"".equals(mstComponentMaterialV0.getProcedureCode())) {
            mstComponentMaterial.setProceduerCode(mstComponentMaterialV0.getProcedureCode().trim());
        }

        if (mstComponentMaterialV0.getRequiredQuantityMolecule() != null && !"".equals(mstComponentMaterialV0.getRequiredQuantityMolecule().trim())) {
            BigDecimal bd = new BigDecimal(mstComponentMaterialV0.getRequiredQuantityMolecule().trim());
            mstComponentMaterial.setNumerator(bd);
        }

        if (mstComponentMaterialV0.getRequiredQuantityDenominator() != null && !"".equals(mstComponentMaterialV0.getRequiredQuantityDenominator().trim())) {
            BigDecimal bd = new BigDecimal(mstComponentMaterialV0.getRequiredQuantityDenominator().trim());
            mstComponentMaterial.setDenominator(bd);
        }

        if (mstComponentMaterialV0.getApplicationStartDate() != null && !"".equals(mstComponentMaterialV0.getApplicationStartDate())) {

            mstComponentMaterial.setStartDate(new Date(mstComponentMaterialV0.getApplicationStartDate()));
        }

        if (mstComponentMaterialV0.getApplicationEndDate() != null && !"".equals(mstComponentMaterialV0.getApplicationEndDate())) {
            mstComponentMaterial.setEndDate(new Date(mstComponentMaterialV0.getApplicationEndDate()));
        }

        if (mstComponentMaterialV0.getComponentId() != null && !"".equals(mstComponentMaterialV0.getComponentId())) {
            mstComponentMaterial.setComponentId(mstComponentMaterialV0.getComponentId());
        }

        if (mstComponentMaterialV0.getMaterialId() != null && !"".equals(mstComponentMaterialV0.getMaterialId())) {
            mstComponentMaterial.setMaterialId(mstComponentMaterialV0.getMaterialId());
        }

        mstComponentMaterial.setUpdateDate(new Date());
        mstComponentMaterial.setUpdateUserUuid(loginUser.getUserUuid());
        mstComponentMaterial.setCreateDate(new Date());
        mstComponentMaterial.setCreateUserUuid(loginUser.getUserUuid());

        entityManager.persist(mstComponentMaterial);
        return mstComponentMaterial;
    }

    /**
     * 部品別材更新
     *
     * @param mstComponentMaterialV0
     * @param loginUser
     * @return
     */
    @Transactional
    public int updateMstComponentMaterialByQuery(MstComponentMaterialVo mstComponentMaterialV0, LoginUser loginUser) {
        //更新
        StringBuilder sql = new StringBuilder(" UPDATE MstComponentMaterial t SET   ");

        if (mstComponentMaterialV0.getProcedureCode() != null && !"".equals(mstComponentMaterialV0.getProcedureCode())) {
            sql.append(" t.proceduerCode = :proceduerCode, ");
        }

        if (mstComponentMaterialV0.getRequiredQuantityMolecule() != null && !"".equals(mstComponentMaterialV0.getRequiredQuantityMolecule().trim())) {
            sql.append(" t.numerator = :numerator, ");
        }

        if (mstComponentMaterialV0.getRequiredQuantityDenominator() != null && !"".equals(mstComponentMaterialV0.getRequiredQuantityDenominator().trim())) {
            sql.append(" t.denominator = :denominator, ");
        }

        if (mstComponentMaterialV0.getApplicationStartDate() != null && !"".equals(mstComponentMaterialV0.getApplicationStartDate())) {
            sql.append(" t.startDate = :startDate, ");
        }

        if (mstComponentMaterialV0.getApplicationEndDate() != null && !"".equals(mstComponentMaterialV0.getApplicationEndDate())) {
            sql.append(" t.endDate = :endDate, ");
        }

        if (mstComponentMaterialV0.getComponentId() != null && !"".equals(mstComponentMaterialV0.getComponentId())) {
            sql.append(" t.componentId = :componentId, ");
        }

        if (mstComponentMaterialV0.getMaterialId() != null && !"".equals(mstComponentMaterialV0.getMaterialId())) {
            sql.append(" t.materialId = :materialId, ");
        }

        sql.append(" t.updateDate = :updateDate, t.updateUserUuid = :updateUserUuid ");

        sql.append(" WHERE t.id = :id ");
        Query query = entityManager.createQuery(sql.toString());

        if (mstComponentMaterialV0.getProcedureCode() != null && !"".equals(mstComponentMaterialV0.getProcedureCode())) {
            query.setParameter("proceduerCode", mstComponentMaterialV0.getProcedureCode());
        }

        if (mstComponentMaterialV0.getRequiredQuantityMolecule() != null && !"".equals(mstComponentMaterialV0.getRequiredQuantityMolecule().trim())) {
            query.setParameter("numerator", new BigDecimal(mstComponentMaterialV0.getRequiredQuantityMolecule().trim()));
        }

        if (mstComponentMaterialV0.getRequiredQuantityDenominator() != null && !"".equals(mstComponentMaterialV0.getRequiredQuantityDenominator().trim())) {
            query.setParameter("denominator", new BigDecimal(mstComponentMaterialV0.getRequiredQuantityDenominator().trim()));
        }

        if (mstComponentMaterialV0.getApplicationStartDate() != null && !"".equals(mstComponentMaterialV0.getApplicationStartDate())) {
            query.setParameter("startDate", new Date(mstComponentMaterialV0.getApplicationStartDate()));
        }

        if (mstComponentMaterialV0.getApplicationEndDate() != null && !"".equals(mstComponentMaterialV0.getApplicationEndDate())) {
            query.setParameter("endDate", new Date(mstComponentMaterialV0.getApplicationEndDate()));
        }

        if (mstComponentMaterialV0.getComponentId() != null && !"".equals(mstComponentMaterialV0.getComponentId())) {
            query.setParameter("componentId", mstComponentMaterialV0.getComponentId());
        }

        if (mstComponentMaterialV0.getMaterialId() != null && !"".equals(mstComponentMaterialV0.getMaterialId())) {
            query.setParameter("materialId", mstComponentMaterialV0.getMaterialId());
        }

        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", loginUser.getUserUuid());
        query.setParameter("id", mstComponentMaterialV0.getId());
        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     * 部品別材料ById DELETE
     *
     * @param id
     * @return
     */
    @Transactional
    public int deleteMstComponentMaterial(String id) {
        Query query = entityManager.createNamedQuery("MstComponentMaterial.delete");
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    boolean getMstComponentMaterialExistCheck(String id) {
        Query query = entityManager.createNamedQuery("MstComponentMaterial.findById");
        query.setParameter("id", id);
        return query.getResultList().size() > 0;
    }

    /**
     * 部品別材料ById GET
     *
     * @param id
     * @return
     */
    public MstComponentMaterialList getComponentMaterial(String id) {
        List list = getSql("", "", id, "");

        MstComponentMaterialList response = new MstComponentMaterialList();

        List<MstComponentMaterialVo> tblMstComponentMaterialVoList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            //MstComponentMaterial mstComponentMaterial = (MstComponentMaterial) list.get(i);
            Object objs = list.get(i);
            Object[] obj = (Object[]) objs;
            MstComponentMaterial mstComponentMaterial = (MstComponentMaterial) obj[0];
            MstProcedure mstProcedure = (MstProcedure) obj[1];
            MstComponentMaterialVo mstComponentMaterialVo = setMstComponentMaterialVo(mstComponentMaterial, mstProcedure);
            tblMstComponentMaterialVoList.add(mstComponentMaterialVo);
        }
        response.setMstComponentMaterialsList(tblMstComponentMaterialVoList);
        return response;
    }

    /**
     * 部品別材CSV出力
     *
     * @param componentCode
     * @param componentName
     * @param loginUser
     * @return
     */
    FileReponse getMstComponentMaterialsOutputCsv(String componentCode, String componentName, LoginUser loginUser) {
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList HeadList = new ArrayList();
        ArrayList lineList;
        String langId = loginUser.getLangId();
        FileReponse fr = new FileReponse();
        FileUtil fu = new FileUtil();
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);

        String outComponentCode = mstDictionaryService.getDictionaryValue(langId, "component_code");
        String outComponentName = mstDictionaryService.getDictionaryValue(langId, "component_name");
        String outProcedureCode = mstDictionaryService.getDictionaryValue(langId, "procedure_code");
        String outProcedureName = mstDictionaryService.getDictionaryValue(langId, "procedure_name");
        String outMaterialCode = mstDictionaryService.getDictionaryValue(langId, "material_code");
        String outMaterialName = mstDictionaryService.getDictionaryValue(langId, "material_name");
        String outNumerator = mstDictionaryService.getDictionaryValue(langId, "numerator");
        String outDenominator = mstDictionaryService.getDictionaryValue(langId, "denominator");
        String outApplyingStartDate = mstDictionaryService.getDictionaryValue(langId, "applying_start_date");
        String outApplyingEndDate = mstDictionaryService.getDictionaryValue(langId, "applying_end_date");
        String delete = mstDictionaryService.getDictionaryValue(langId, "delete_record");
        /*Head*/
        HeadList.add(outComponentCode);
        HeadList.add(outComponentName);
        HeadList.add(outProcedureCode);
        HeadList.add(outProcedureName);
        HeadList.add(outMaterialCode);
        HeadList.add(outMaterialName);
        HeadList.add(outNumerator);
        HeadList.add(outDenominator);
        HeadList.add(outApplyingStartDate);
        HeadList.add(outApplyingEndDate);
        HeadList.add(delete);
        gLineList.add(HeadList);

        //明細データを取得
        List list = getSql(
                componentCode,
                componentName,
                "", "");
        MstComponentMaterialList response = new MstComponentMaterialList();
        response.setMstComponentMaterials(list);

        for (int i = 0; i < list.size(); i++) {
            Object objs = list.get(i);
            Object[] obj = (Object[]) objs;
            MstComponentMaterial mstComponentMaterial = (MstComponentMaterial) obj[0];
            MstProcedure mstProcedure = (MstProcedure) obj[1];
            lineList = new ArrayList();
            if (mstComponentMaterial != null) {
                if (mstComponentMaterial.getMstComponent() != null) {
                    lineList.add(mstComponentMaterial.getMstComponent().getComponentCode());
                    lineList.add(mstComponentMaterial.getMstComponent().getComponentName());
                } else {
                    lineList.add("");
                    lineList.add("");
                }
                lineList.add(mstComponentMaterial.getProceduerCode());
                if (mstProcedure != null) {
                    lineList.add(mstProcedure.getProcedureName());
                } else {
                    lineList.add("");
                }
                if (mstComponentMaterial.getMstMaterial() != null) {
                    lineList.add(mstComponentMaterial.getMstMaterial().getMaterialCode());
                    lineList.add(mstComponentMaterial.getMstMaterial().getMaterialName());
                } else {
                    lineList.add("");
                    lineList.add("");
                }
                lineList.add(String.valueOf(mstComponentMaterial.getNumerator()));
                lineList.add(String.valueOf(mstComponentMaterial.getDenominator()));
                lineList.add(fu.getDateFormatForStr(mstComponentMaterial.getStartDate()));
                lineList.add(fu.getDateFormatForStr(mstComponentMaterial.getEndDate()));
                lineList.add("");
                gLineList.add(lineList);
            }
        }
        CSVFileUtil csvFileUtil = null;
        try {
            csvFileUtil = new CSVFileUtil(outCsvPath, "csvOutput");
            Iterator<ArrayList> iter = gLineList.iterator();
            while (iter.hasNext()) {
                csvFileUtil.toCSVLine(iter.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // CSVファイルwriterのクローズ処理
            if (csvFileUtil != null) {
                csvFileUtil.close();
            }
        }
        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_COMPONENT_MATERIAL);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_MST_COMPONENT_MATERIAL);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());//"mst_component_material_maintentance"
        String fileName = mstDictionaryService.getDictionaryValue(langId, "mst_component_material_maintentance");
        tblCsvExport.setClientFileName(fu.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }

    /**
     * 部品別材料BycomponentId proceduerCode materialId
     *
     * @param componentId
     * @param proceduerCode
     * @param materialId
     * @return
     */
    public MstComponentMaterial getComponentMaterialExistCheck(String componentId, String proceduerCode, String materialId, String startDate) {
        // boolean flag;
        Query query = entityManager.createNamedQuery("MstComponentMaterial.findByUniqueKey");
        query.setParameter("componentId", componentId);
        query.setParameter("proceduerCode", proceduerCode);
        query.setParameter("materialId", materialId);
        query.setParameter("startDate", DateFormat.strToDate(startDate));
        List list = query.getResultList();
        if (list.size() > 0) {
            return (MstComponentMaterial) list.get(0);
        }
        return null;
    }

    /**
     * CSVの中身に対してチェックを行う
     *
     * @param logParm
     * @param lineCsv
     * @param userLangId
     * @param logFile
     * @param index
     * @return
     */
    public boolean checkCsvFileData(Map<String, String> logParm, String lineCsv[], String userLangId, String logFile, int index) {

        //ログ出力内容を用意する
        String lineNo = logParm.get("lineNo");
        String componentCode = logParm.get("componentCode");
        String componentName = logParm.get("componentName");
        String procedureCode = logParm.get("procedureCode");
        String procedureName = logParm.get("procedureName");
        String materialCode = logParm.get("materialCode");
        String materialName = logParm.get("materialName");
        String numerator = logParm.get("numerator");
        String denominator = logParm.get("denominator");
        String applyingStartDate = logParm.get("applyingStartDate");
        String applyingEndDate = logParm.get("applyingEndDate");

        String error = logParm.get("error");
        String errorContents = logParm.get("errorContents");
        String nullMsg = logParm.get("nullMsg");
        String notFound = logParm.get("notFound");
        String maxLangth = logParm.get("maxLangth");
        String notNumber = logParm.get("notNumber");
        String layout = logParm.get("layout");
        String invalidDate = logParm.get("invalidDate");
        String notZero = logParm.get("notZero");

        int arrayLength = lineCsv.length;
        FileUtil fu = new FileUtil();
        if (arrayLength != 11) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, componentCode, "", error, 1, errorContents, layout));
            return false;
        }
        //分割した文字をObjectに格納する
        String strComponentCode = lineCsv[0].trim();
        if (fu.isNullCheck(strComponentCode)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, componentCode, strComponentCode, error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(strComponentCode, 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, componentCode, strComponentCode, error, 1, errorContents, maxLangth));
            return false;
        } else {
            MstComponentList mstComponentList = mstComponentService.getComponentByCodeOrName(strComponentCode, null);
            if (mstComponentList.getMstComponents().isEmpty()) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, componentCode, strComponentCode, error, 1, errorContents, notFound));
                return false;
            }
        }

        String strComponentName = lineCsv[1].trim();
        if (fu.maxLangthCheck(strComponentName, 100)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, componentName, strComponentName, error, 1, errorContents, maxLangth));
            return false;
        }

        String strProcedureCode = lineCsv[2].trim();
        if (fu.isNullCheck(strProcedureCode)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, procedureCode, strProcedureCode, error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(strProcedureCode, 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, procedureCode, strProcedureCode, error, 1, errorContents, maxLangth));
            return false;
        }
        String strProcedureName = lineCsv[3].trim();
        if (fu.maxLangthCheck(strProcedureName, 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, procedureName, strProcedureName, error, 1, errorContents, maxLangth));
            return false;
        }
        String strMaterialCode = lineCsv[4].trim();
        if (fu.isNullCheck(strMaterialCode)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, materialCode, strMaterialCode, error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(strMaterialCode, 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, materialCode, strMaterialCode, error, 1, errorContents, maxLangth));
            return false;
        } else {
            MstMaterialList response = mstMaterialService.getMaterialByCodeOrName(strMaterialCode, null);
            if (response.getMstMaterialList().isEmpty()) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, materialCode, strMaterialCode, error, 1, errorContents, notFound));
                return false;
            }
        }

        String strMaterialName = lineCsv[5].trim();
        if (fu.maxLangthCheck(strMaterialName, 100)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, materialName, strMaterialName, error, 1, errorContents, maxLangth));
            return false;
        }
        String strNumerator = lineCsv[6].trim();

        if (!fu.isDouble(strNumerator)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, numerator, strNumerator, error, 1, errorContents, notNumber));
            return false;
        } else if (!fu.validateDeciamlLen(strNumerator, 18, 6)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, numerator, strNumerator, error, 1, errorContents, maxLangth));
            return false;
        }
//        
        String strDenominator = lineCsv[7].trim();
        if ("0".equals(strDenominator)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, denominator, strDenominator, error, 1, errorContents, notZero));
            return false;
        } else if (!fu.isDouble(strDenominator)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, denominator, strDenominator, error, 1, errorContents, notNumber));
            return false;
        } else if (!fu.validateDeciamlLen(strDenominator, 18, 5)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, denominator, strDenominator, error, 1, errorContents, maxLangth));
            return false;
        }

        String strApplyingStartDate = lineCsv[8].trim();
        if (!fu.isValidDate(strApplyingStartDate)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, applyingStartDate, strApplyingStartDate, error, 1, errorContents, invalidDate));
            return false;
        }

        String strApplyingEndDate = lineCsv[9].trim();
        if (!fu.isValidDate(strApplyingEndDate)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, applyingEndDate, strApplyingEndDate, error, 1, errorContents, invalidDate));
            return false;
        }

        return true;
    }

    /**
     * 部品別材料 update
     *
     * @param mstComponentMaterial
     * @return
     */
    @Transactional
    public int updateMstComponentMaterialByQuery(MstComponentMaterial mstComponentMaterial) {
        Query query = entityManager.createNamedQuery("MstComponentMaterial.update");
        query.setParameter("componentId", mstComponentMaterial.getComponentId());
        query.setParameter("materialId", mstComponentMaterial.getMaterialId());
        query.setParameter("proceduerCode", mstComponentMaterial.getProceduerCode());
        query.setParameter("updateDate", mstComponentMaterial.getUpdateDate());
        query.setParameter("updateUserUuid", mstComponentMaterial.getUpdateUserUuid());
        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     * 部品別材料 check
     *
     * @param mstComponentMaterialV0
     * @return
     */
    public boolean checkParam(MstComponentMaterialVo mstComponentMaterialV0) {
        FileUtil fu = new FileUtil();
        BigDecimal denominator = new BigDecimal(mstComponentMaterialV0.getRequiredQuantityDenominator());
        int flag = denominator.compareTo(new BigDecimal(0));
        if (fu.isNullCheck(mstComponentMaterialV0.getComponentCode())
                || fu.isNullCheck(mstComponentMaterialV0.getMaterialCode())
                || fu.isNullCheck(mstComponentMaterialV0.getProcedureCode())
                || !fu.isValidDate(mstComponentMaterialV0.getApplicationStartDate())
                || !fu.isValidDate(mstComponentMaterialV0.getApplicationEndDate())
                || flag == 0
                || !fu.isDouble(mstComponentMaterialV0.getRequiredQuantityDenominator().trim())
                || !fu.validateDeciamlLen(mstComponentMaterialV0.getRequiredQuantityDenominator().trim(), 18, 5)
                || !fu.validateDeciamlLen(mstComponentMaterialV0.getRequiredQuantityMolecule().trim(), 18, 6)
                || !fu.isDouble(mstComponentMaterialV0.getRequiredQuantityMolecule().trim())) {
            return false;
        }
        return true;
    }

    /**
     * 部品材料マスタリスト取得(部品IDおよび工番指定)
     *
     * @param componentId
     * @param proceduerCode
     * @return
     */
    public MstComponentMaterialList getListByComponentIdAndProceduerCode(String componentId, String proceduerCode) {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT mstComponentMaterial FROM MstComponentMaterial mstComponentMaterial"
                + " JOIN FETCH mstComponentMaterial.mstMaterial mstMaterial"
                + " WHERE 1=1 "
        );
        // 部品コード
        if (componentId != null && !"".equals(componentId)) {
            sql.append(" and mstComponentMaterial.componentId = :componentId ");
        }
        // 工番
        if (proceduerCode != null && !"".equals(proceduerCode)) {
            sql.append(" and mstComponentMaterial.proceduerCode = :proceduerCode ");
        }
        sql.append(" ORDER BY mstComponentMaterial.mstMaterial.materialCode ASC ");
        Query query = entityManager.createQuery(sql.toString());
        if (componentId != null && !"".equals(componentId)) {
            query.setParameter("componentId", componentId);
        }
        if (proceduerCode != null && !"".equals(proceduerCode)) {
            query.setParameter("proceduerCode", proceduerCode);
        }
        List list = query.getResultList();
        MstComponentMaterialList response = new MstComponentMaterialList();
        response.setMstComponentMaterials(list);
        return response;
    }

    /**
     * 部品材料マスタリスト取得(部品ID、材料IDおよび工番指定)
     *
     * @param componentId
     * @param materialId
     * @param proceduerCode
     * @return
     */
    public List<MstComponentMaterial> getComponentmaterialInfoByCondition(String componentId, String materialId, String proceduerCode) {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT mstComponentMaterial FROM MstComponentMaterial mstComponentMaterial"
                + " JOIN FETCH mstComponentMaterial.mstMaterial mstMaterial"
                + " WHERE 1=1 "
        );
        // 部品コード
        if (componentId != null && !"".equals(componentId)) {
            sql.append(" and mstComponentMaterial.componentId = :componentId ");
        }

        // 材料コード
        if (materialId != null && !"".equals(materialId)) {
            sql.append(" and mstComponentMaterial.materialId = :materialId ");
        }

        // 工番
        if (proceduerCode != null && !"".equals(proceduerCode)) {
            sql.append(" and mstComponentMaterial.proceduerCode = :proceduerCode ");
        }
        sql.append(" ORDER BY mstComponentMaterial.mstMaterial.materialCode ASC ");
        Query query = entityManager.createQuery(sql.toString());
        if (componentId != null && !"".equals(componentId)) {
            query.setParameter("componentId", componentId);
        }
        if (materialId != null && !"".equals(materialId)) {
            query.setParameter("materialId", materialId);
        }
        if (proceduerCode != null && !"".equals(proceduerCode)) {
            query.setParameter("proceduerCode", proceduerCode);
        }
        List list = query.getResultList();

        List<MstComponentMaterial> mstComponentMaterialList = list;

        return mstComponentMaterialList;
    }

    /**
     * 部品材料マスタリスト取得(部品ID、材料名称)
     *
     * @param componentId
     * @param materialName
     * @return
     */
    public MstComponentMaterialList getMaterialInfoByComponentIdAndName(String componentId, String materialName) {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT mstComponentMaterial FROM MstComponentMaterial mstComponentMaterial"
                + " JOIN FETCH mstComponentMaterial.mstMaterial mstMaterial"
                + " WHERE 1=1 "
        );
        // 部品コード
        if (componentId != null && !"".equals(componentId)) {
            sql.append(" and mstComponentMaterial.componentId = :componentId ");
        }

        // 材料名称
        if (materialName != null && !"".equals(materialName)) {
            sql.append(" and mstMaterial.materialName LIKE :materialName ");
        }

        sql.append(" ORDER BY mstComponentMaterial.mstMaterial.materialCode ASC ");
        Query query = entityManager.createQuery(sql.toString());
        if (componentId != null && !"".equals(componentId)) {
            query.setParameter("componentId", componentId);
        }
        if (materialName != null && !"".equals(materialName)) {
            query.setParameter("materialName", "%" + materialName + "%");
        }

        List list = query.getResultList();

        MstComponentMaterialList response = new MstComponentMaterialList();

        List<MstComponentMaterial> mstComponentMaterials = list;
        
         List<MstComponentMaterialVo> mstComponentMaterialVos = new ArrayList();

        if (mstComponentMaterials.size() > 0) {

            for (MstComponentMaterial mstComponentMaterial : mstComponentMaterials) {

                MstComponentMaterialVo mstComponentMaterialVo = new MstComponentMaterialVo();

                BeanCopyUtil.copyFields(mstComponentMaterial, mstComponentMaterialVo);

                mstComponentMaterialVo.setMaterialCode(mstComponentMaterial.getMstMaterial().getMaterialCode());
                mstComponentMaterialVo.setMaterialName(mstComponentMaterial.getMstMaterial().getMaterialName());
                mstComponentMaterialVo.setRequiredQuantityMolecule(String.valueOf(FileUtil.getNum(mstComponentMaterial.getNumerator())));
                mstComponentMaterialVo.setRequiredQuantityDenominator(String.valueOf(FileUtil.getNum(mstComponentMaterial.getDenominator())));
                mstComponentMaterialVo.setApplicationStartDate(DateFormat.dateToStr(mstComponentMaterial.getStartDate(), DateFormat.DATE_FORMAT));
                mstComponentMaterialVo.setApplicationEndDate(DateFormat.dateToStr(mstComponentMaterial.getEndDate(), DateFormat.DATE_FORMAT));
                mstComponentMaterialVo.setMaterialType(mstComponentMaterial.getMstMaterial().getMaterialType());
                mstComponentMaterialVo.setMaterialGrade(mstComponentMaterial.getMstMaterial().getMaterialGrade());

                mstComponentMaterialVos.add(mstComponentMaterialVo);

            }
        }
        
        response.setMstComponentMaterialsList(mstComponentMaterialVos);

        return response;
    }

    /**
     * 部品別材料複数取得
     *
     * @param componentCode
     * @param componentName
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isPage
     * @return
     */
    MstComponentMaterialList getComponentMaterialsByPage(String componentCode, String componentName,
            String sidx, String sord, int pageNumber, int pageSize, boolean isPage) {
        
        MstComponentMaterialList response = new MstComponentMaterialList();
        
        if (isPage) {

            List count = getSqlByPage(componentCode, componentName, "", sidx,
                    sord, pageNumber, pageSize, true);

            // ページをめぐる
            Pager pager = new Pager();
            response.setPageNumber(pageNumber);
            long counts = (long) count.get(0);
            response.setCount(counts);
            response.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));

        }
        
        List list = getSqlByPage(componentCode, componentName, "", sidx,
                sord, pageNumber, pageSize, false);

        List<MstComponentMaterialVo> tblMstComponentMaterialVoList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Object objs = list.get(i);
            Object[] obj = (Object[]) objs;
            MstComponentMaterial mstComponentMaterial = (MstComponentMaterial) obj[0];
            MstProcedure mstProcedure = (MstProcedure) obj[1];
            // MstComponentMaterial mstComponentMaterial =
            // (MstComponentMaterial) list.get(i);
            MstComponentMaterialVo mstComponentMaterialVo = setMstComponentMaterialVo(mstComponentMaterial,
                    mstProcedure);
            tblMstComponentMaterialVoList.add(mstComponentMaterialVo);
        }
        response.setMstComponentMaterialsList(tblMstComponentMaterialVoList);
        return response;
    }
    
    /**
     * 部品別材料sql
     *
     * @param componentCode
     * @param componentName
     * @param id
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isCount
     * @return
     */
    private List getSqlByPage(String componentCode, String componentName, String id, String sidx,
            String sord, int pageNumber, int pageSize, boolean isCount) {
        StringBuilder sql;
        String strComponentCode = "";
        String strComponentName = "";
        String strId = "";

        if (isCount) {// m.mstProcedure.procedureCode
            sql = new StringBuilder("SELECT count(m)  FROM MstComponentMaterial m   "
                    + " LEFT JOIN MstProcedure m1 ON  m.proceduerCode = m1.procedureCode AND m.componentId = m1.componentId  "
                    + "WHERE 1=1  ");

        } else {// m.componentId ASC,m.materialId ASC,
            sql = new StringBuilder(
                    "SELECT m,m1 FROM MstComponentMaterial m JOIN FETCH m.mstComponent mc JOIN FETCH m.mstMaterial mm "
                            + " LEFT JOIN MstProcedure m1 ON  m.proceduerCode = m1.procedureCode AND m.componentId = m1.componentId "
                            + "WHERE 1=1  ");
        }

        if (componentCode != null && !"".equals(componentCode)) {
            strComponentCode = componentCode.trim();
            sql.append(" and m.mstComponent.componentCode LIKE :componentCode ");
        }

        if (componentName != null && !"".equals(componentName)) {
            strComponentName = componentName.trim();
            sql.append(" and m.mstComponent.componentName like :componentName ");
        }

        if (id != null && !"".equals(id)) {
            strId = id.trim();
            sql.append(" and m.id = :id ");
        }

        if (!isCount) {

            if (StringUtils.isNotEmpty(sidx)) {

                String sortStr = orderKey.get(sidx) + " " + sord;

                sql.append(sortStr);

            } else {

                sql.append(" ORDER BY  mc.componentCode ASC ,m.proceduerCode ASC, mm.materialCode ASC ");

            }

        }
        
        Query query = entityManager.createQuery(sql.toString());

        if (componentCode != null && !"".equals(componentCode)) {
            query.setParameter("componentCode", "%" + strComponentCode + "%");
        }

        if (componentName != null && !"".equals(componentName)) {
            query.setParameter("componentName", "%" + componentName + "%");
        }

        if (id != null && !"".equals(id)) {
            query.setParameter("id", strId);
        }
        
        // 画面改ページを設定する
        if (!isCount) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }

        List list = query.getResultList();

        return list;
    }
    
    public MstComponentMaterial getSingleComponentMaterialFromHistory(
            String componentId, String procedureCode, String materialId, String date) {
        java.util.Date pDate = DateFormat.strToDate(date);
        String sql = "SELECT t FROM MstComponentMaterial t WHERE t.componentId = :componentId " +
                " AND t.proceduerCode = :procedureCode AND t.materialId = :materialId AND  t.startDate <= :date1 AND t.endDate >= :date2 ORDER BY t.startDate ";
        Query query = entityManager.createQuery(sql);
        query.setParameter("componentId", componentId);
        query.setParameter("procedureCode", procedureCode);
        query.setParameter("materialId", materialId);
        query.setParameter("date1", pDate);
        query.setParameter("date2", pDate);
        List list = query.getResultList();
        if (list.size() > 0) {
            return (MstComponentMaterial) list.get(0);
        }
        return null;
    }
}
