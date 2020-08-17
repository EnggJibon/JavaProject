/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.defect;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.excelhandle.write.WriteExcelList;
import com.kmcj.karte.excelhandle.write.WriteListExcel;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionResultService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Apeng
 */
@Dependent
public class TblComponentInspectionDefectService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;
    
    @Inject
    private TblComponentInspectionResultService tblComponentInspectionResultService;

    private static final String DEFECT_CATEGORY = "tbl_component_inspection_defect.defect_seq";
    
    private static final int M_TYPE_MAN = 1;
    private static final int M_TYPE_MACHINE = 2;
    private static final int M_TYPE_METERIAL = 3;
    private static final int M_TYPE_METHOD = 4;
    private static final String PARETO_CHART = "pareto_chart";

    /**
     * 部品検査不具合情報を取得
     *
     * @param componentInspectionResultId
     * @param langId
     * @return
     */
    public TblComponentInspectionDefectList getTblComponentInspectionDefectList(String componentInspectionResultId, String langId) {

        TblComponentInspectionDefectList tblComponentInspectionDefectList = new TblComponentInspectionDefectList();

        List<TblComponentInspectionDefect> list = entityManager.createNamedQuery("TblComponentInspectionDefect.findByComponentInspectionResultId", TblComponentInspectionDefect.class)
                .setParameter("componentInspectionResultId", componentInspectionResultId).getResultList();

        if (list.isEmpty()) {
            tblComponentInspectionDefectList.setError(true);
            tblComponentInspectionDefectList.setErrorCode(ErrorMessages.E201_APPLICATION);
            tblComponentInspectionDefectList.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_inspection_data_not_exist"));
        } else {
            tblComponentInspectionDefectList.setTblComponentInspectionDefectList(list);
        }
        return tblComponentInspectionDefectList;
    }

    /**
     * 部品検査不具合情報の保存
     *
     * @param tblComponentInspectionDefectList
     * @return
     */
    @Transactional
    public TblComponentInspectionDefectList saveTblComponentInspectionDefect(TblComponentInspectionDefectList tblComponentInspectionDefectList) {

        if (null != tblComponentInspectionDefectList && null != tblComponentInspectionDefectList.getTblComponentInspectionDefectList()) {
            for (TblComponentInspectionDefect tblComponentInspectionDefect : tblComponentInspectionDefectList.getTblComponentInspectionDefectList()) {
                if (StringUtils.isEmpty(tblComponentInspectionDefect.getId())) {// 検査部品の不具合情報の作成
                    tblComponentInspectionDefect.setId(IDGenerator.generate());
                    entityManager.persist(tblComponentInspectionDefect);
                } else {// 検査部品の不具合情報の更新
                    entityManager.merge(tblComponentInspectionDefect);
                }
            }
        }
        return tblComponentInspectionDefectList;
    }

    /**
     * 部品検査不具合情報の削除
     *
     * @param id
     * @return
     */
    @Transactional
    public BasicResponse delTblComponentInspectionDefect(String id) {

        BasicResponse response = new BasicResponse();
        entityManager.createNamedQuery("TblComponentInspectionDefect.deleteById").setParameter("id", id).executeUpdate();
        return response;
    }

    /**
     * 部品検査不具合のパレート表示用の情報を取得
     *
     * @param incomingCompanyId
     * @param componentId
     * @param formatComponentInspectionFrom
     * @param formatComponentInspectionTo
     * @param cavityPrefix
     * @param cavityNum
     * @param langId
     * @return
     */
    public TblComponentInspectionDefectList getTblComponentInspectionDefectPraetoList(String incomingCompanyId, String componentId, Date formatComponentInspectionFrom, Date formatComponentInspectionTo, String cavityPrefix, int cavityNum, String langId) {

        TblComponentInspectionDefectList tblComponentInspectionDefectPraetoList = new TblComponentInspectionDefectList();

        Map<String, String> mTypeMap = getDictionaryList(langId);
        
        tblComponentInspectionDefectPraetoList.setCavList(tblComponentInspectionResultService.getInspectDefectCavNoList(incomingCompanyId, componentId, formatComponentInspectionFrom, formatComponentInspectionTo, cavityPrefix, cavityNum));

        // パレート情報を取得する
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT "
                + " t.defectSeq,"
                + " m.choice,"
                + " t.mType,"
                + " SUM(t.quantity) quantityByGropu"
                + " FROM TblComponentInspectionDefect t"
                + " JOIN FETCH TblComponentInspectionResult r"
                + " JOIN FETCH r.mstComponent"
                + " JOIN FETCH r.mstCompanyIncoming"
                + " JOIN FETCH MstChoice m"
                + " WHERE t.componentInspectionResultId = r.id"
                + " AND t.defectSeq = m.mstChoicePK.seq"
                + " AND m.mstChoicePK.category = :category"
                + " AND m.mstChoicePK.langId = :langId");

        setWhereCondition(queryBuilder, incomingCompanyId, componentId, formatComponentInspectionFrom, formatComponentInspectionTo, cavityPrefix, cavityNum);

        queryBuilder.append(" GROUP BY t.defectSeq, m.choice, t.mType");
        queryBuilder.append(" ORDER BY quantityByGropu DESC, m.choice");

        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("category", DEFECT_CATEGORY);
        query.setParameter("langId", langId);
        setValueCondition(query, incomingCompanyId, componentId, formatComponentInspectionFrom, formatComponentInspectionTo, cavityPrefix, cavityNum);

        List<Object> list = query.getResultList();
        if (list.isEmpty()) {
            tblComponentInspectionDefectPraetoList.setError(true);
            tblComponentInspectionDefectPraetoList.setErrorCode(ErrorMessages.E201_APPLICATION);
            tblComponentInspectionDefectPraetoList.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_inspection_data_not_exist"));
        } else {
            int total = 0;
            List<TblComponentInspectionDefect> tblComponentInspectionDefectList = new ArrayList<>();

            for (Object obj : list) {
                Object[] item = (Object[]) obj;
                TblComponentInspectionDefect tblComponentInspectionDefect = new TblComponentInspectionDefect();
                tblComponentInspectionDefect.setDefectSeq(Integer.valueOf(item[0].toString()));
                tblComponentInspectionDefect.setDefectSeqName(item[1].toString());
                tblComponentInspectionDefect.setmType(Integer.valueOf(item[2].toString()));
                setMTypeName(tblComponentInspectionDefect, mTypeMap);
                tblComponentInspectionDefect.setQuantityByGropu(Integer.valueOf(item[3].toString()));
                total += tblComponentInspectionDefect.getQuantityByGropu();
                tblComponentInspectionDefectList.add(tblComponentInspectionDefect);
            }

            settblComponentInspectionDefectPraeto(tblComponentInspectionDefectPraetoList, tblComponentInspectionDefectList, total);

            tblComponentInspectionDefectPraetoList.setTblComponentInspectionDefectList(tblComponentInspectionDefectList);
        }

        // 4mType円グラフ情報を取得する
        StringBuilder queryBuilderMtype = new StringBuilder();
        queryBuilderMtype.append("SELECT "
                + " t.mType,"
                + " SUM(t.quantity) quantityByGropu"
                + " FROM TblComponentInspectionDefect t"
                + " JOIN FETCH TblComponentInspectionResult r"
                + " JOIN FETCH r.mstComponent"
                + " JOIN FETCH r.mstCompanyIncoming"
                + " WHERE t.componentInspectionResultId = r.id");

        setWhereCondition(queryBuilderMtype, incomingCompanyId, componentId, formatComponentInspectionFrom, formatComponentInspectionTo, cavityPrefix, cavityNum);

        queryBuilderMtype.append(" GROUP BY t.mType");
        queryBuilderMtype.append(" ORDER BY quantityByGropu DESC");

        Query queryMtype = entityManager.createQuery(queryBuilderMtype.toString());
        setValueCondition(queryMtype, incomingCompanyId, componentId, formatComponentInspectionFrom, formatComponentInspectionTo, cavityPrefix, cavityNum);

        List<Object> listMtype = queryMtype.getResultList();
        if (!listMtype.isEmpty()) {

            int total = 0;
            List<TblComponentInspectionDefect> tblComponentInspectionDefecMTypetList = new ArrayList<>();

            for (Object obj : listMtype) {
                Object[] item = (Object[]) obj;
                TblComponentInspectionDefect tblComponentInspectionDefect = new TblComponentInspectionDefect();
                tblComponentInspectionDefect.setmType(Integer.valueOf(item[0].toString()));
                setMTypeName(tblComponentInspectionDefect, mTypeMap);
                tblComponentInspectionDefect.setQuantityByGropu(Integer.valueOf(item[1].toString()));
                total += tblComponentInspectionDefect.getQuantityByGropu();
                tblComponentInspectionDefecMTypetList.add(tblComponentInspectionDefect);
            }

            settblComponentInspectionDefectPraeto(tblComponentInspectionDefectPraetoList, tblComponentInspectionDefecMTypetList, total);

            tblComponentInspectionDefectPraetoList.setTblComponentInspectionDefectMTypeList(tblComponentInspectionDefecMTypetList);
        }
        
        // パレートグラフ情報を取得する
        StringBuilder queryBuilderPareto = new StringBuilder();
        queryBuilderPareto.append("SELECT "
                + " t.defectSeq,"
                + " m.choice,"
                + " SUM(t.quantity) quantityByGropu"
                + " FROM TblComponentInspectionDefect t"
                + " JOIN FETCH TblComponentInspectionResult r"
                + " JOIN FETCH r.mstComponent"
                + " JOIN FETCH r.mstCompanyIncoming"
                + " JOIN FETCH MstChoice m"
                + " WHERE t.componentInspectionResultId = r.id"
                + " AND t.defectSeq = m.mstChoicePK.seq"
                + " AND m.mstChoicePK.category = :category"
                + " AND m.mstChoicePK.langId = :langId");

        setWhereCondition(queryBuilderPareto, incomingCompanyId, componentId, formatComponentInspectionFrom, formatComponentInspectionTo, cavityPrefix, cavityNum);

        queryBuilderPareto.append(" GROUP BY t.defectSeq, m.choice");
        queryBuilderPareto.append(" ORDER BY quantityByGropu DESC");

        Query queryPareto = entityManager.createQuery(queryBuilderPareto.toString());
        queryPareto.setParameter("category", DEFECT_CATEGORY);
        queryPareto.setParameter("langId", langId);
        setValueCondition(queryPareto, incomingCompanyId, componentId, formatComponentInspectionFrom, formatComponentInspectionTo, cavityPrefix, cavityNum);

        List<Object> listPareto = queryPareto.getResultList();
        if (!listPareto.isEmpty()) {

            int total = 0;
            List<TblComponentInspectionDefect> tblComponentInspectionDefecParetotList = new ArrayList<>();

            for (Object obj : listPareto) {
                Object[] item = (Object[]) obj;
                TblComponentInspectionDefect tblComponentInspectionDefect = new TblComponentInspectionDefect();
                tblComponentInspectionDefect.setDefectSeq(Integer.valueOf(item[0].toString()));
                tblComponentInspectionDefect.setDefectSeqName(item[1].toString());
                tblComponentInspectionDefect.setQuantityByGropu(Integer.valueOf(item[2].toString()));
                total += tblComponentInspectionDefect.getQuantityByGropu();
                tblComponentInspectionDefecParetotList.add(tblComponentInspectionDefect);
            }

            settblComponentInspectionDefectPraeto(tblComponentInspectionDefectPraetoList, tblComponentInspectionDefecParetotList, total);
            int stackedPareto = 0;
            for(TblComponentInspectionDefect pareto : tblComponentInspectionDefecParetotList) {
                stackedPareto += Math.round((double)pareto.getQuantityByGropu() / total * 100);
                pareto.setPareto(String.valueOf(Math.min(stackedPareto, 100)));
            }

            tblComponentInspectionDefectPraetoList.setTblComponentInspectionDefecParetotList(tblComponentInspectionDefecParetotList);
            //パレート図の割合は累積比率なので、Maxは100となる。
            tblComponentInspectionDefectPraetoList.setMaxPareto(100);
        }

        return tblComponentInspectionDefectPraetoList;
    }
    
    /**
     * 部品検査不具合のパレート帳票を出力
     *
     * @param incomingCompanyId
     * @param componentId
     * @param formatComponentInspectionFrom
     * @param formatComponentInspectionTo
     * @param cavityPrefix
     * @param cavityNum
     * @param langId
     * @return
     * @throws IOException
     */
    public Response exportTblComponentInspectionDefectPraeto(String incomingCompanyId,
            String componentId,
            Date formatComponentInspectionFrom,
            Date formatComponentInspectionTo,
            String cavityPrefix,
            int cavityNum,
            String langId) throws IOException {

        WriteExcelList we = new WriteListExcel();
        Map<String, Object> param = new HashMap();
        String uuid = IDGenerator.generate();
        String outExclePath = FileUtil.outExcelFile(kartePropertyService, uuid);
        param.put("outFilePath", outExclePath);
        param.put("workbook", new XSSFWorkbook());
        param.put("isConvertWorkbook", false);

        // ヘッダー
        Map<String, String> excelHeaderMap = getDictionaryList(langId);

        // パレート情報を取得する
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT "
                + " t.defectSeq,"
                + " m.choice,"
                + " t.mType,"
                + " SUM(t.quantity) quantityByGropu"
                + " FROM TblComponentInspectionDefect t"
                + " JOIN FETCH TblComponentInspectionResult r"
                + " JOIN FETCH r.mstComponent"
                + " JOIN FETCH r.mstCompanyIncoming"
                + " JOIN FETCH MstChoice m"
                + " WHERE t.componentInspectionResultId = r.id"
                + " AND t.defectSeq = m.mstChoicePK.seq"
                + " AND m.mstChoicePK.category = :category"
                + " AND m.mstChoicePK.langId = :langId");

        setWhereCondition(queryBuilder, incomingCompanyId, componentId, formatComponentInspectionFrom, formatComponentInspectionTo, cavityPrefix, cavityNum);

        queryBuilder.append(" GROUP BY t.defectSeq, m.choice, t.mType");
        queryBuilder.append(" ORDER BY quantityByGropu DESC, m.choice");

        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("category", DEFECT_CATEGORY);
        query.setParameter("langId", langId);
        setValueCondition(query, incomingCompanyId, componentId, formatComponentInspectionFrom, formatComponentInspectionTo, cavityPrefix, cavityNum);

        List<Object> list = query.getResultList();
        int total = 0;
        List<TblComponentInspectionDefect> tblComponentInspectionDefectList = new ArrayList<>();

        for (Object obj : list) {
            Object[] item = (Object[]) obj;
            TblComponentInspectionDefect tblComponentInspectionDefect = new TblComponentInspectionDefect();
            tblComponentInspectionDefect.setDefectSeq(Integer.valueOf(item[0].toString()));
            tblComponentInspectionDefect.setDefectSeqName(item[1].toString());
            tblComponentInspectionDefect.setmType(Integer.valueOf(item[2].toString()));
            tblComponentInspectionDefect.setQuantityByGropu(Integer.valueOf(item[3].toString()));
            total += tblComponentInspectionDefect.getQuantityByGropu();
            tblComponentInspectionDefectList.add(tblComponentInspectionDefect);
        }

        settblComponentInspectionDefectPraeto(null, tblComponentInspectionDefectList, total);

        try {
            /**
             * Header
             */
            List excelList = new ArrayList();
            // Exceｌ出力Headerを設定
            setExcelExportHeader(excelList, excelHeaderMap);

            // Exceｌ出力データを設定
            setExcelExportData(excelList, tblComponentInspectionDefectList, excelHeaderMap);

            we.write(param, excelList);
        } catch (IOException | IllegalArgumentException | IllegalAccessException e) {
            Logger.getLogger(TblComponentInspectionDefectService.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException(e);
        }

        File file = new File(outExclePath);

        Response.ResponseBuilder response = Response.ok(file);
        // 出力ファイル名称を設定する
        String fileName = excelHeaderMap.get(PARETO_CHART) + ".xlsx";
        //String encodeStr = FileUtil.getExcelFileName(fileName);
        fileName = fileName.replace("+", "%20");
        response.header("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
        return response.build();
    }

    /**
     * パレートの計算
     *
     * @param tblComponentInspectionDefectPraetoList
     * @param tblComponentInspectionDefectList
     * @param total
     * @return
     */
    private void settblComponentInspectionDefectPraeto(TblComponentInspectionDefectList tblComponentInspectionDefectPraetoList, List<TblComponentInspectionDefect> tblComponentInspectionDefectList, int total) {
        
        int maxQuantityByGropu = 0;
        int maxPraeto = 0;

        for (TblComponentInspectionDefect DefectPraeto : tblComponentInspectionDefectList) {
            
            // maxQuantityByGropuの設定
            if (DefectPraeto.getQuantityByGropu() > maxQuantityByGropu) {
                maxQuantityByGropu = DefectPraeto.getQuantityByGropu();
            }

            double praeto = (double) DefectPraeto.getQuantityByGropu() / total * 100;
            int praetoInt = (int) Math.round(praeto);
            
            // maxPraetoの設定
            if (praetoInt > maxPraeto) {
                maxPraeto = praetoInt;
            }
            
            DefectPraeto.setPareto(String.valueOf(praetoInt));
        }
        
        if (null != tblComponentInspectionDefectPraetoList) {
            tblComponentInspectionDefectPraetoList.setMaxQuantityByGropu(maxQuantityByGropu);
            tblComponentInspectionDefectPraetoList.setMaxPareto(maxPraeto);
        }
    }

    /**
     * 検索用の条件を設定する
     *
     * @param queryBuilder
     * @param incomingCompanyId
     * @param componentId
     * @param formatComponentInspectionFrom
     * @param formatComponentInspectionTo
     * @param cavityPrefix
     * @param cavityNum
     * @return
     */
    private void setWhereCondition(StringBuilder queryBuilder, String incomingCompanyId, String componentId, Date formatComponentInspectionFrom, Date formatComponentInspectionTo, String cavityPrefix, int cavityNum) {
        if (StringUtils.isNotBlank(incomingCompanyId)) {
            queryBuilder.append(" AND r.mstCompanyIncoming.companyCode = :incomingCompanyId ");
        }

        if (StringUtils.isNotBlank(componentId)) {
            queryBuilder.append(" AND r.mstComponent.componentCode = :componentId ");
        }

        if (formatComponentInspectionFrom != null && formatComponentInspectionTo != null) {
            queryBuilder.append(" AND ((r.outgoingInspectionDate >= :componentInspectionFrom AND r.outgoingInspectionDate <= :componentInspectionTo))");
        }
        
        if (null != cavityPrefix) {
            queryBuilder.append(" AND r.cavityPrefix = :cavityPrefix");
        }
        if (0 != cavityNum) {
            queryBuilder.append(" AND t.cavityNum = :cavityNum");
        }
    }

    /**
     * 検索用のパラメータを設定する
     *
     * @param query
     * @param incomingCompanyId
     * @param componentId
     * @param formatComponentInspectionFrom
     * @param formatComponentInspectionTo
     * @param cavityPrefix
     * @param cavityNum
     * @return
     */
    private void setValueCondition(Query query, String incomingCompanyId, String componentId, Date formatComponentInspectionFrom, Date formatComponentInspectionTo, String cavityPrefix, int cavityNum) {
        if (StringUtils.isNotBlank(incomingCompanyId)) {
            query.setParameter("incomingCompanyId", incomingCompanyId);
        }

        if (StringUtils.isNotBlank(componentId)) {
            query.setParameter("componentId", componentId);
        }
        if (formatComponentInspectionFrom != null && formatComponentInspectionTo != null) {
            query.setParameter("componentInspectionFrom", formatComponentInspectionFrom);
            query.setParameter("componentInspectionTo", formatComponentInspectionTo);
        }
        if (null != cavityPrefix) {
            query.setParameter("cavityPrefix", cavityPrefix);
        }
        if (0 != cavityNum) {
            query.setParameter("cavityNum", cavityNum);
        }
    }
    
    /**
     *
     * Excel出力のヘッダ情報を取得する
     * 
     * @param langId
     * @return
     */
    private Map<String, String> getDictionaryList(String langId) {
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList("defect_seq", "inspection_quantity", "4m_type", "pareto", "4m_type_man", "4m_type_machine", "4m_type_meterial", "4m_type_method", PARETO_CHART);

        Map<String, String> excelHeaderMap = mstDictionaryService.getDictionaryHashMap(langId, dictKeyList);

        return excelHeaderMap;
    }
    
     /**
     * Excel出力HEADERを設定
     *
     * @param list
     * @param excelHeaderMap
     */
    private void setExcelExportHeader(List list, Map<String, String> excelHeaderMap) {
        
        TblComponentInspectionDefectExcel excelExport = new TblComponentInspectionDefectExcel();
        excelExport.setDefectSeqName(excelHeaderMap.get("defect_seq"));
        excelExport.setmTypeName(excelHeaderMap.get("4m_type"));
        excelExport.setQuantityByGropu(excelHeaderMap.get("inspection_quantity"));
        excelExport.setPareto(excelHeaderMap.get("pareto"));

        list.add(excelExport);
    }
    
    /**
     * Excel出力データを設定
     *
     * @param list
     * @param tblComponentInspectionDefectList
     * @param dictMap
     */
    private void setExcelExportData(List list, List<TblComponentInspectionDefect> tblComponentInspectionDefectList, Map<String, String> dictMap) {

        TblComponentInspectionDefectExcel excelExport;
        for (TblComponentInspectionDefect tblComponentInspectionDefect : tblComponentInspectionDefectList) {
            excelExport = new TblComponentInspectionDefectExcel();
            excelExport.setDefectSeqName(FileUtil.getStringValue(tblComponentInspectionDefect.getDefectSeqName()));
            switch (tblComponentInspectionDefect.getmType()) {
                case M_TYPE_MAN:
                    excelExport.setmTypeName(dictMap.get("4m_type_man"));
                    break;
                case M_TYPE_MACHINE:
                    excelExport.setmTypeName(dictMap.get("4m_type_machine"));
                    break;
                case M_TYPE_METERIAL:
                    excelExport.setmTypeName(dictMap.get("4m_type_meterial"));
                    break;
                case M_TYPE_METHOD:
                    excelExport.setmTypeName(dictMap.get("4m_type_method"));
                    break;
                default:
                    excelExport.setmTypeName("");
                    break;
            }
            excelExport.setQuantityByGropu(FileUtil.getStringValue(String.valueOf(tblComponentInspectionDefect.getQuantityByGropu())));
            excelExport.setPareto(FileUtil.getStringValue(tblComponentInspectionDefect.getPareto()) + "%");
            list.add(excelExport);
        }
    }

     /**
     * 4M区分の名称を設定する
     *
     * @param tblComponentInspectionDefect
     * @param dictMap
     */
    private void setMTypeName(TblComponentInspectionDefect tblComponentInspectionDefect, Map<String, String> dictMap) {

        switch (tblComponentInspectionDefect.getmType()) {
            case M_TYPE_MAN:
                tblComponentInspectionDefect.setmTypeName(dictMap.get("4m_type_man"));
                break;
            case M_TYPE_MACHINE:
                tblComponentInspectionDefect.setmTypeName(dictMap.get("4m_type_machine"));
                break;
            case M_TYPE_METERIAL:
                tblComponentInspectionDefect.setmTypeName(dictMap.get("4m_type_meterial"));
                break;
            case M_TYPE_METHOD:
                tblComponentInspectionDefect.setmTypeName(dictMap.get("4m_type_method"));
                break;
            default:
                tblComponentInspectionDefect.setmTypeName("");
                break;
        }
    }
}
