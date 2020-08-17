/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.direction;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.production.TblProduction;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;

/**
 * 手配テーブルサービス
 *
 * @author t.arikis
 */
@Dependent
public class TblDirectionService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private KartePropertyService kartePropertyService;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private MstChoiceService mstChoiceService;
    
    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("directionCode", " ORDER BY t.directionCode ");// 手配・工事番号
        orderKey.put("categorytext", " ORDER BY t.directionCategory ");// 手配・工事内容
        orderKey.put("componentCode", " ORDER BY mc.componentCode ");// 部品コード
        orderKey.put("componentName", " ORDER BY mc.componentName ");// 部品名称
        orderKey.put("quantity", " ORDER BY t.quantity ");// 数量
        orderKey.put("dueDate", " ORDER BY t.dueDate ");// 納期
        orderKey.put("moldId", " ORDER BY mold.moldId ");// 金型ＩＤ
        orderKey.put("moldName", " ORDER BY mold.moldName ");// 金型名称
        orderKey.put("departmentText", " ORDER BY t.department ");// 部署
        orderKey.put("poNumber", " ORDER BY t.poNumber ");// 受注番号

    }

    /**
     * 手配・工事番号で1件取得
     *
     * @param directionCode
     * @return
     */
    public TblDirection getTblDirectionByDirectionCode(String directionCode) {
        Query query = entityManager.createNamedQuery("TblDirection.findByDirectionCode");
        query.setParameter("directionCode", directionCode);
        
         List list =  query.getResultList();
         if(list != null && list.size() > 0){
             return (TblDirection)list.get(0);
         }
         return null;
    }

    /**
     * 手配・工事番号でn件取得
     *
     * @param directionCode
     * @return
     */
    public List<TblDirection> getTblDirectionsByDirectionCode(String directionCode) {
        Query query = entityManager.createNamedQuery("TblDirection.findByDirectionCode");
        query.setParameter("directionCode", directionCode);
         List<TblDirection> list =  query.getResultList();
         return list;
    }

    /**
     * IDで1件取得
     *
     * @param id
     * @return
     */
    public TblDirection getTblDirectionById(String id) {
        Query query = entityManager.createNamedQuery("TblDirection.findById");
        query.setParameter("id", id);
        try {
            return (TblDirection) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * 手配テーブル 手配・工事番号によるLIKE検索
     *
     * @param directionCode
     * @return
     */
    public TblDirectionList getDirectionLikeCode(String directionCode) {
        StringBuilder sql;
        String sqlDirectionCode = "";
        sql = new StringBuilder("SELECT t FROM TblDirection t WHERE 1=1 ");

        if (directionCode != null && !"".equals(directionCode)) {
            sqlDirectionCode = directionCode.trim();
            sql = sql.append(" and t.directionCode like :directionCode ");
        }
        Query query = entityManager.createQuery(sql.toString());

        if (directionCode != null && !"".equals(directionCode)) {
            query.setParameter("directionCode", "%" + sqlDirectionCode + "%");
        }

        List list = query.getResultList();
        TblDirection tblDirection;
        List<TblDirection> tblDirections = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            tblDirection = new TblDirection();
            TblDirection iTblDirection = (TblDirection) list.get(i);
            tblDirection.setId(iTblDirection.getId());
            tblDirection.setDirectionCode(iTblDirection.getDirectionCode());
            tblDirections.add(tblDirection);
        }
        TblDirectionList tblDirectionList = new TblDirectionList();
        tblDirectionList.setTblDirections(tblDirections);
        return tblDirectionList;
    }

    /**
     * 手配テーブル 手配・工事番号による完全一致検索
     *
     * @param directionCode
     * @return
     */
    public TblDirectionList getDirectionByCode(String directionCode) {
        StringBuilder sql;
        String sqlDirectionCode = "";
        sql = new StringBuilder("SELECT t FROM TblDirection t WHERE 1=1 ");

        if (directionCode != null && !"".equals(directionCode)) {
            sqlDirectionCode = directionCode.trim();
            sql = sql.append(" and t.directionCode = :directionCode ");
        }
        Query query = entityManager.createQuery(sql.toString());

        if (directionCode != null && !"".equals(directionCode)) {
            query.setParameter("directionCode", sqlDirectionCode);
        }
  

        List list = query.getResultList();
        TblDirection tblDirection;
        List<TblDirection> tblDirections = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            tblDirection = new TblDirection();
            TblDirection iTblDirection = (TblDirection) list.get(i);
            tblDirection.setId(iTblDirection.getId());
            tblDirection.setDirectionCode(iTblDirection.getDirectionCode());
            tblDirections.add(tblDirection);
        }
        TblDirectionList tblDirectionList = new TblDirectionList();
        tblDirectionList.setTblDirections(tblDirections);
        return tblDirectionList;
    }

    /**
     * PK存在チェック
     *
     * @param id
     * @return
     */
    public boolean isExsistByPk(String id) {
        Query query = entityManager.createNamedQuery("TblDirection.findById");
        query.setParameter("id", id);
        try {
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (NoResultException noResultException) {
            return false;
        }
        return false;
    }

    /**
     * 手配テーブルから条件にあてはまるデータを取得し、手配・工事番号の昇順で表示する。
     *
     * @param directionCode
     * @param dueDateStart
     * @param dueDateEnd
     * @param department
     * @param poNumber
     * @param loginUser
     * @return
     */
    public TblDirectionList getTblDirections(String directionCode, String dueDateStart, String dueDateEnd, String department, String poNumber, LoginUser loginUser) {
        TblDirectionList response = new TblDirectionList();
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        FileUtil fu = new FileUtil();
        if (dueDateStart != null && !"".equals(dueDateStart)) {
            if (!fu.dateCheck(dueDateStart)) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid"));
                return response;
            }
        }
        
        if (dueDateEnd != null && !"".equals(dueDateEnd)) {
            if (!fu.dateCheck(dueDateEnd)) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid"));
                return response;
            }
        }
        
        List list = getDirectionSql(directionCode, dueDateStart, dueDateEnd, department, poNumber, 1);
        Map<String, String> departmentMap = new HashMap<>();
        Map<String, String> directionvalueMap = new HashMap<>();
        MstChoiceList mstChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");
        for (MstChoice mstChoice : mstChoiceList.getMstChoice()) {
            departmentMap.put(String.valueOf(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
        }
        TblDirectionVo tblDirectionVo = null;
        List<TblDirectionVo> tblDirectionVoList = new ArrayList<>();
        MstChoiceList mstChoiceList1 = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_direction.direction_caterogy");
        for (MstChoice mstChoice : mstChoiceList1.getMstChoice()) {
            directionvalueMap.put(String.valueOf(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
        }

        for (int i = 0; i < list.size(); i++) {
            TblDirection tblDirection = (TblDirection) list.get(i);
            tblDirectionVo = new TblDirectionVo();
            if (tblDirection.getDepartment() != null) {
                tblDirectionVo.setDepartment(String.valueOf(tblDirection.getDepartment()));
                if(departmentMap != null && departmentMap.size() > 0){
                    tblDirectionVo.setDepartmentText(departmentMap.get(String.valueOf(tblDirection.getDepartment())));
                }else{
                    tblDirectionVo.setDepartmentText("");
                }
            } else {
                tblDirectionVo.setDepartment("");
                tblDirectionVo.setDepartmentText("");
            }
            tblDirectionVo.setId(tblDirection.getId() == null ? "" : tblDirection.getId());

            tblDirectionVo.setDirectionCode(tblDirection.getDirectionCode() == null ? "" : tblDirection.getDirectionCode());
            tblDirectionVo.setComponentId(tblDirection.getComponentId() == null ? "" : tblDirection.getComponentId());
            if (tblDirection.getMstComponent() != null) {
                tblDirectionVo.setComponentCode(tblDirection.getMstComponent().getComponentCode());
                tblDirectionVo.setComponentName(tblDirection.getMstComponent().getComponentName());
            } else {
                tblDirectionVo.setComponentCode("");
                tblDirectionVo.setComponentName("");
            }

            tblDirectionVo.setQuantity(tblDirection.getQuantity().toString());

            if (tblDirection.getDirectionCategory() != null) {
                tblDirectionVo.setDirectionCategory(String.valueOf(tblDirection.getDirectionCategory()));
                if(directionvalueMap != null && directionvalueMap.size() > 0){
                    tblDirectionVo.setCategorytext(directionvalueMap.get(String.valueOf(tblDirection.getDirectionCategory())));
                }else{
                    tblDirectionVo.setCategorytext("");
                }
            } else {
                tblDirectionVo.setDirectionCategory("");
            }
            
            if(tblDirection.getDueDate() != null){
                tblDirectionVo.setDueDate(sdf.format(tblDirection.getDueDate()));
            }else{
                tblDirectionVo.setDueDate("");
            }
            
            if(tblDirection.getMstMold() != null){
                tblDirectionVo.setMoldId(tblDirection.getMstMold().getMoldId());
                tblDirectionVo.setMoldName(tblDirection.getMstMold().getMoldName());
            }else{
                tblDirectionVo.setMoldId("");
                tblDirectionVo.setMoldName("");
            }
            
            tblDirectionVo.setPoNumber(tblDirection.getPoNumber() == null ? "" : tblDirection.getPoNumber());

            tblDirectionVoList.add(tblDirectionVo);
        }

        response.setTblDirectionVoList(tblDirectionVoList);
        return response;
    }

    /**
     * sql文の用
     *
     * @param directionCode
     * @param dueDateStart
     * @param dueDateEnd
     * @param department
     * @param poNumber
     * @param flag
     * @return
     */
    public List getDirectionSql(String directionCode, String dueDateStart, String dueDateEnd, String department, String poNumber, int flag) {
        StringBuilder sql = null;
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        if (flag == 1) {
            sql = new StringBuilder("SELECT t FROM TblDirection t LEFT JOIN FETCH t.mstComponent LEFT JOIN FETCH t.mstMold WHERE 1=1 ");
        } else {
            sql = new StringBuilder("SELECT count(t) FROM TblDirection t WHERE 1=1 ");
        }

        String sqlDirectionCode = "";
        int sqlDepartment = 0;
        String sqlPoNumber = "";
        if (directionCode != null && !"".equals(directionCode)) {
            sqlDirectionCode = directionCode.trim();
            sql.append(" and t.directionCode LIKE :directionCode ");
        }
        if (dueDateStart != null && !"".equals(dueDateStart)) {
            sql.append(" and t.dueDate >= :dueDateStart ");
        }
        if (dueDateEnd != null && !"".equals(dueDateEnd)) {
            sql.append(" and t.dueDate <= :dueDateEnd ");
        }
        if (department != null && !"".equals(department)) {
            sql.append(" and t.department = :department ");
        }
        if (poNumber != null && !"".equals(poNumber)) {
            sqlPoNumber = poNumber.trim();
            sql.append(" and t.poNumber LIKE :poNumber ");
        }

        sql.append(" Order by t.directionCode ");    //directionCodeの昇順
        Query query = entityManager.createQuery(sql.toString());
        if (directionCode != null && !"".equals(directionCode)) {
            query.setParameter("directionCode", "%" + sqlDirectionCode + "%");
        }
        try {
            if (dueDateStart != null && !"".equals(dueDateStart)) {

                query.setParameter("dueDateStart", sdf.parse(dueDateStart));

            }
            if (dueDateEnd != null && !"".equals(dueDateEnd)) {
                query.setParameter("dueDateEnd", sdf.parse(dueDateEnd));
            }
        } catch (ParseException ex) {
            Logger.getLogger(TblDirectionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (department != null && !"".equals(department)) {
            query.setParameter("department", Integer.parseInt(department));
        }

        if (poNumber != null && !"".equals(poNumber)) {
            query.setParameter("poNumber", "%" + sqlPoNumber + "%");
        }

        return query.getResultList();
    }

    /*
    * @param directionCode
    * @param poNumber
    * @param dueDateStart
    * @param department
    * @param dueDateEnd
    *手配テーブル取得 T0011 手配一覧_検索 件数取得
    *システム設定の一覧表示最大件数を超える場合は警告。
     */

    public CountResponse getDirectionCount(String directionCode, String dueDateStart, String dueDateEnd, String department, String poNumber) {
        List list = getDirectionSql(directionCode, dueDateStart, dueDateEnd, department, poNumber, 0);
        CountResponse count = new CountResponse();
        if (list.size() > 0) {
            count.setCount((long) (list.get(0)));
        } else {
            count.setCount(0);
        }
        return count;

    }

    /**
     * 手配テーブル一件取得(getTblDirection) 選択されている手配・工事情報の詳細画面に編集モードで遷移する。
     * @param id
     * @return
     */
    public boolean getTblDirectionExistCheck(String id) {
        Query query = entityManager.createNamedQuery("TblDirection.findById");
        query.setParameter("id", id);
        return query.getResultList().size() > 0;
    }
    
    
    /**
     * 手配コードをチェック
     * @param directionCode
     * @param componentId
     * @return 
     */
    public boolean checkDirectionCodeExistCheck(String directionCode,String componentId){
        StringBuilder sql = new StringBuilder(" SELECT t FROM TblDirection t WHERE 1=1 ");
        sql.append("AND t.directionCode = :directionCode");
        if(componentId != null && !"".equals(componentId)){
           sql.append(" AND t.componentId = :componentId");
        }else{
            sql.append(" AND t.componentId IS NULL ");
        }
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("directionCode", directionCode);
        if(componentId != null && !"".equals(componentId)){
            query.setParameter("componentId", componentId);
        }
        return query.getResultList().size() > 0;
    }

    /**
     * 画面の各項目の値で手配テーブルへ追加・更新する
     *
     * @param tblDirection
     * @param loginUser
     */
    @Transactional
    public void createTblDirection(TblDirection tblDirection, LoginUser loginUser) {
        
        String tblDirectionId = IDGenerator.generate();
        tblDirection.setId(tblDirectionId);
        tblDirection.setCreateDate(new java.util.Date());
        tblDirection.setCreateUserUuid(loginUser.getUserUuid());
        tblDirection.setUpdateDate(new java.util.Date());
        tblDirection.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.persist(tblDirection);

    }

    /**
     *
     * @param id
     * @return
     */
    @Transactional
    public int deleteTblDirection(String id) {
        Query query = entityManager.createNamedQuery("TblDirection.delete");
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    /**
     * T0012 手配詳細_手配テーブル更新 画面の各項目の値で手配テーブルへ追加・更新する
     *
     * @param tblDirection
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse updateTblDirection(TblDirectionVo tblDirection, LoginUser loginUser) {
        
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        
        TblDirection updateTblDirection = new TblDirection();
        updateTblDirection.setId(tblDirection.getId());
        updateTblDirection.setDirectionCode(tblDirection.getDirectionCode());
        updateTblDirection.setDirectionCategory(Integer.parseInt(tblDirection.getDirectionCategory()));
        if(tblDirection.getComponentId() != null && !"".equals(tblDirection.getComponentId())){
            updateTblDirection.setComponentId(tblDirection.getComponentId());
        }else{
            updateTblDirection.setComponentId(null);
        }
        
        if(tblDirection.getMoldUuid() != null && !"".equals(tblDirection.getMoldUuid())){
            updateTblDirection.setMoldUuid(tblDirection.getMoldUuid());
        }else{
            updateTblDirection.setMoldUuid(null);
        }
        
        if(tblDirection.getDepartment() != null && !"".equals(tblDirection.getDepartment())){
            updateTblDirection.setDepartment(Integer.parseInt(tblDirection.getDepartment()));
        }else{
            updateTblDirection.setDepartment(0);
        }
        
        if (tblDirection.getDueDate() != null && !"".equals(tblDirection.getDueDate())) {
            try {
                updateTblDirection.setDueDate(sdf.parse(tblDirection.getDueDate()));
            } catch (ParseException ex) {
                Logger.getLogger(TblDirectionService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        updateTblDirection.setPoNumber(tblDirection.getPoNumber());
        if(tblDirection.getQuantity() != null && !"".equals(tblDirection.getQuantity())){
            updateTblDirection.setQuantity(new BigDecimal(tblDirection.getQuantity().trim()));
        }else{
            updateTblDirection.setQuantity(new BigDecimal("0.0000"));
        }
        
        
        //更新
        updateTblDirection.setUpdateDate(new Date());
        updateTblDirection.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.merge(updateTblDirection);
        BasicResponse response = new BasicResponse();
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }

    /*
    * @param directionCode
    * @param dueDateStart
    * @param dueDateEnd
    * @param department
    * @param poNumber
    * @param loginUser
     */

    public FileReponse getTblDirectionOutputCsv(String directionCode, String dueDateStart, String dueDateEnd, String department, String poNumber, LoginUser loginUser) {
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList HeadList = new ArrayList();
        ArrayList lineList;
        String langId = loginUser.getLangId();
        FileReponse fr = new FileReponse();
        FileUtil fu = new FileUtil();
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        String outDirectionCode = mstDictionaryService.getDictionaryValue(langId, "direction_code");
        String outDirectionValue = mstDictionaryService.getDictionaryValue(langId, "direction_value");
        
        //String outDirectionValue = "手配・工事内容";
        String outComponentCode = mstDictionaryService.getDictionaryValue(langId, "component_code");
        String outComponentName = mstDictionaryService.getDictionaryValue(langId, "component_name");
        String outQuantity = mstDictionaryService.getDictionaryValue(langId, "quantity");
        //String outQuantity = "数量";
        String outDueDate = mstDictionaryService.getDictionaryValue(langId, "due_date");
        //String outDueDate ="納期";
        String outMoldId = mstDictionaryService.getDictionaryValue(langId, "mold_id");
        String outMoldName = mstDictionaryService.getDictionaryValue(langId, "mold_name");
        String outDepartment = mstDictionaryService.getDictionaryValue(langId, "work_user_department");
        String outOrderedNumber = mstDictionaryService.getDictionaryValue(langId, "po_number");

        /*Head*/
        HeadList.add(outDirectionCode);
        HeadList.add(outDirectionValue);
        HeadList.add(outComponentCode);
        HeadList.add(outComponentName);
        HeadList.add(outQuantity);
        HeadList.add(outDueDate);
        HeadList.add(outMoldId);
        HeadList.add(outMoldName);
        HeadList.add(outDepartment);
        HeadList.add(outOrderedNumber);
        gLineList.add(HeadList);
        //明細データを取得
        List list = getDirectionSql(directionCode, dueDateStart, dueDateEnd, department, poNumber, 1);
        
        Map<String, String> departmentMap = new HashMap<>();
        MstChoiceList mstChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");
        for (MstChoice mstChoice : mstChoiceList.getMstChoice()) {
            departmentMap.put(String.valueOf(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
        }
        Map<String, String> directionCategoryMap = new HashMap<>();
        MstChoiceList mstDirectionChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_direction.direction_caterogy");
        for (MstChoice mstChoice : mstDirectionChoiceList.getMstChoice()) {
            directionCategoryMap.put(String.valueOf(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
        }

        /*Detail*/
        for (int i = 0; i < list.size(); i++) {
            lineList = new ArrayList();
            TblDirection tblDirection = (TblDirection) list.get(i);
            if (tblDirection.getDirectionCode() != null && !"".equals(tblDirection.getDirectionCode())) {
                lineList.add(tblDirection.getDirectionCode());
            } else {
                lineList.add("");
            }
            if (tblDirection.getDirectionCategory() != null) {
                if(directionCategoryMap.size() > 0){
                   lineList.add(directionCategoryMap.get(String.valueOf(tblDirection.getDirectionCategory())));
                }else{
                    lineList.add("");
                }
            } else {
                lineList.add("");
            }

            if (tblDirection.getMstComponent() != null) {
                lineList.add(tblDirection.getMstComponent().getComponentCode());
                lineList.add(tblDirection.getMstComponent().getComponentName());
            } else {
                lineList.add("");
                lineList.add("");
            }
            if (tblDirection.getQuantity() != null) {
                lineList.add(String.valueOf(tblDirection.getQuantity()));
            } else {
                lineList.add("");
            }
            if (tblDirection.getDueDate() != null) {
                lineList.add(sdf.format(tblDirection.getDueDate()));
            } else {
                lineList.add("");
            }

            if (tblDirection.getMstMold() != null) {
                lineList.add(tblDirection.getMstMold().getMoldId());
                lineList.add(tblDirection.getMstMold().getMoldName());
            } else {
                lineList.add("");
                lineList.add("");
            }
            if (tblDirection.getDepartment() != null) {
                if(departmentMap.size() > 0){
                    lineList.add(departmentMap.get(String.valueOf(tblDirection.getDepartment())));
                }else{
                    lineList.add("");
                }
            } else {
                lineList.add("");
            }

            if (tblDirection.getPoNumber() != null) {
                lineList.add(tblDirection.getPoNumber());
            } else {
                lineList.add("");
            }

            gLineList.add(lineList);
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
        tblCsvExport.setExportTable(CommonConstants.TBL_DIRECTION);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_MST_DIRECTION_MAINTENANCE);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, "direction_list");
        tblCsvExport.setClientFileName(fu.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }

    /**
     * CSVの中身に対してチェックを行う
     *
     * @param logMap
     * @param lineCsv
     * @param userLangId
     * @param logFile
     * @param index
     * @return
     */
    public boolean checkCsvFileData(Map<String, String> logMap, String lineCsv[], String userLangId, String logFile, int index) {

        //ログ出力内容を用意する
        FileUtil fu = new FileUtil();

        //分割した文字をObjectに格納する
        String strDirectionCode = lineCsv[0].trim();
        if (fu.isNullCheck(strDirectionCode)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(logMap.get("lineNo"), index, logMap.get("directionCode"), strDirectionCode, logMap.get("error"), 1, logMap.get("errorContents"), logMap.get("nullMsg")));
            return false;
        } else if (fu.maxLangthCheck(strDirectionCode, 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(logMap.get("lineNo"), index, logMap.get("directionCode"), strDirectionCode, logMap.get("error"), 1, logMap.get("errorContents"), logMap.get("maxLangth")));
            return false;
        }

        String strDirectionValue = lineCsv[1].trim();
        if (fu.maxLangthCheck(strDirectionValue, 11)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(logMap.get("lineNo"), index, logMap.get("directionValue"), strDirectionValue, logMap.get("error"), 1, logMap.get("errorContents"), logMap.get("maxLangth")));
            return false;
        }

        String strComponentCode = lineCsv[2].trim();
        if (fu.maxLangthCheck(strComponentCode, 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(logMap.get("lineNo"), index, logMap.get("componentCode"), strComponentCode, logMap.get("error"), 1, logMap.get("errorContents"), logMap.get("maxLangth")));
            return false;
        }

        String strComponentName = lineCsv[3].trim();
        if (fu.maxLangthCheck(strComponentName, 100)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(logMap.get("lineNo"), index, logMap.get("componentName"), strComponentName, logMap.get("error"), 1, logMap.get("errorContents"), logMap.get("maxLangth")));
            return false;
        }

//        String strQuantity = lineCsv[4].trim();
//        if (fu.isNullCheck(strQuantity)) {
//            //エラー情報をログファイルに記入
//            fu.writeInfoToFile(logFile, fu.outValue(logMap.get("lineNo"), index, logMap.get("quantity"), strQuantity, logMap.get("error"), 1, logMap.get("errorContents"), logMap.get("maxLangth")));
//            return false;
//        }else if(!fu.isDouble(strQuantity)){
//            //エラー情報をログファイルに記入
//            fu.writeInfoToFile(logFile, fu.outValue(logMap.get("lineNo"), index, logMap.get("quantity"), strQuantity, logMap.get("error"), 1, logMap.get("errorContents"), logMap.get("isNumber")));
//            return false;
//        }else if(!fu.validateDeciamlLen(strQuantity, 16, 4)){
//            //エラー情報をログファイルに記入
//            fu.writeInfoToFile(logFile, fu.outValue(logMap.get("lineNo"), index, logMap.get("quantity"), strQuantity, logMap.get("error"), 1, logMap.get("errorContents"), logMap.get("maxLangth")));
//            return false;
//        }

        String strDueDate = lineCsv[5].trim();
//        if (fu.isNullCheck(strDueDate)) {
//            //エラー情報をログファイルに記入
//            fu.writeInfoToFile(logFile, fu.outValue(logMap.get("lineNo"), index, logMap.get("dueDate"), strDueDate, logMap.get("error"), 1, logMap.get("errorContents"), logMap.get("nullMsg")));
//            return false;
//        }else if(!fu.isValidDate(strDueDate)){
//            //エラー情報をログファイルに記入
//            fu.writeInfoToFile(logFile, fu.outValue(logMap.get("lineNo"), index, logMap.get("dueDate"), strDueDate, logMap.get("error"), 1, logMap.get("errorContents"), logMap.get("isDate")));
//            return false;
//        }
        if (strDueDate != null && !"".equals(strDueDate)) {
            if (!fu.isValidDate(strDueDate)) {
                //エラー情報をログファイルに記入
                fu.writeInfoToFile(logFile, fu.outValue(logMap.get("lineNo"), index, logMap.get("dueDate"), strDueDate, logMap.get("error"), 1, logMap.get("errorContents"), logMap.get("isDate")));
                return false;
            }
        }

        String strmoldId = lineCsv[6].trim();
        if (fu.maxLangthCheck(strmoldId, 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(logMap.get("lineNo"), index, logMap.get("moldId"), strmoldId, logMap.get("error"), 1, logMap.get("errorContents"), logMap.get("maxLangth")));
            return false;
        }

        String strMoldName = lineCsv[7].trim();

        if (fu.maxLangthCheck(strMoldName, 100)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(logMap.get("lineNo"), index, logMap.get("moldName"), strMoldName, logMap.get("error"), 1, logMap.get("errorContents"), logMap.get("maxLangth")));
            return false;
        }
        String strDepartment = lineCsv[8].trim();
        if (fu.maxLangthCheck(strDepartment, 11)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(logMap.get("lineNo"), index, logMap.get("department"), strDepartment, logMap.get("error"), 1, logMap.get("errorContents"), logMap.get("maxLangth")));
            return false;
        }

        String strOrderedNumber = lineCsv[9].trim();

        if (fu.maxLangthCheck(strOrderedNumber, 50)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(logMap.get("lineNo"), index, logMap.get("orderedNumber"), strOrderedNumber, logMap.get("error"), 1, logMap.get("errorContents"), logMap.get("maxLangth")));
            return false;
        }
        return true;
    }

    public boolean isExsistByDirectionCode(String directionCode) {
        Query query = entityManager.createNamedQuery("TblDirection.findByDirectionCode");
        query.setParameter("directionCode", directionCode);
        try {
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (NoResultException noResultException) {
            return false;
        }
        return false;
    }

    @Transactional
    public int updateTblDirectionByQuery(TblDirection readCsvInfo) {
        Query query = entityManager.createNamedQuery("TblDirection.updateByComponentId");
        query.setParameter("directionCategory", readCsvInfo.getDirectionCategory());
        query.setParameter("quantity", readCsvInfo.getQuantity());
        query.setParameter("dueDate", readCsvInfo.getDueDate());
        query.setParameter("componentId", readCsvInfo.getComponentId());
        query.setParameter("moldUuid", readCsvInfo.getMoldUuid());
        query.setParameter("department", readCsvInfo.getDepartment());
        query.setParameter("poNumber", readCsvInfo.getPoNumber());
        query.setParameter("updateDate", readCsvInfo.getUpdateDate());
        query.setParameter("updateUserUuid", readCsvInfo.getUpdateUserUuid());
        query.setParameter("directionCode", readCsvInfo.getDirectionCode());

        int cnt = query.executeUpdate();
        return cnt;
    }
    
    /**
     * IDを取得
     * @param strDirectionCode
     * @param strComponentId
     * @return 
     */
    public String getDirectionId(String strDirectionCode,String strComponentId){
        
        String id = "";
        
        StringBuilder sql = new StringBuilder("SELECT t FROM TblDirection t WHERE 1=1 ");
        sql.append(" AND t.directionCode = :directionCode ");
        sql.append(" AND t.componentId = :componentId ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("directionCode", strDirectionCode);
        query.setParameter("componentId", strComponentId);
        try{
            TblDirection tblDirection = (TblDirection)query.getSingleResult();
            id = tblDirection.getId();
        }catch(NoResultException e){
            id = "";
        }
        return id;
    }

    public TblDirectionVo getTblDirectionByDirectionId(String id,LoginUser loginUser) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        
        Map<String, String> departmentMap = new HashMap<>();
        MstChoiceList mstChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");
        for (MstChoice mstChoice : mstChoiceList.getMstChoice()) {
            departmentMap.put(String.valueOf(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
        }
        Map<String, String> directionCategoryMap = new HashMap<>();
        MstChoiceList mstDirectionChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_direction.direction_caterogy");
        for (MstChoice mstChoice : mstDirectionChoiceList.getMstChoice()) {
            directionCategoryMap.put(String.valueOf(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
        }
        
        Query query = entityManager.createNamedQuery("TblDirection.findById");
        query.setParameter("id", id);
        TblDirectionVo tblDirectionVo = new TblDirectionVo();
        TblDirection tblDirection = (TblDirection) query.getSingleResult();
        
        tblDirectionVo.setMoldUuid(tblDirection.getMoldUuid() == null ? "" : tblDirection.getMoldUuid());
        if(tblDirection.getMstMold() != null){
            tblDirectionVo.setMoldId(tblDirection.getMstMold().getMoldId());
            tblDirectionVo.setMoldName(tblDirection.getMstMold().getMoldName());
        }else{
            tblDirectionVo.setMoldId("");
            tblDirectionVo.setMoldName("");
        }
        
        
        tblDirectionVo.setDirectionCode(tblDirection.getDirectionCode() == null ? "" : tblDirection.getDirectionCode());
        
        if(tblDirection.getDirectionCategory() != null){
            tblDirectionVo.setDirectionCategory(String.valueOf(tblDirection.getDirectionCategory()));
            if(directionCategoryMap != null && directionCategoryMap.size() > 0){
                tblDirectionVo.setDirectionCategoryText(directionCategoryMap.get(String.valueOf(tblDirection.getDirectionCategory())));
            }else{
                tblDirectionVo.setDirectionCategoryText("");
            }
        }else{
            tblDirectionVo.setDirectionCategory("");
            tblDirectionVo.setDirectionCategoryText("");
        }
        
        if(tblDirection.getDueDate() != null){
            tblDirectionVo.setDueDate(sdf.format(tblDirection.getDueDate()));
        }else{
            tblDirectionVo.setDueDate("");
        }
        
        tblDirectionVo.setPoNumber(tblDirection.getPoNumber() == null ? "" : tblDirection.getPoNumber());
        if(tblDirection.getDepartment() != null){
            tblDirectionVo.setDepartment(String.valueOf(tblDirection.getDepartment()));
            if(departmentMap != null && departmentMap.size() > 0){
                tblDirectionVo.setDepartmentText(departmentMap.get(String.valueOf(tblDirection.getDepartment())));
            }else{
                tblDirectionVo.setDepartmentText("");
            }
        }else{
            tblDirectionVo.setDepartment("");
            tblDirectionVo.setDepartmentText("");
        }
        
        if(tblDirection.getQuantity() != null){
            tblDirectionVo.setQuantity(String.valueOf(tblDirection.getQuantity()));
        }else{
            tblDirectionVo.setQuantity("");
        }
        
        tblDirectionVo.setComponentId(tblDirection.getComponentId() == null ? "" : tblDirection.getComponentId());
        
        if (tblDirection.getMstComponent() != null) {
            tblDirectionVo.setComponentName(tblDirection.getMstComponent().getComponentName());
            tblDirectionVo.setComponentCode(tblDirection.getMstComponent().getComponentCode());
        } else {
            tblDirectionVo.setComponentName("");
            tblDirectionVo.setComponentCode("");
        }

        return tblDirectionVo;

    }
    /*
    *T0012 手配詳細_手配テーブル更新 画面の各項目の値で手配テーブルへ追加・更新する入力の判断
    * @param tblDirection
    * @param loginUser
    */
    public BasicResponse checkTblDirection(TblDirectionVo tblDirection,LoginUser loginUser) {
         BasicResponse response = new BasicResponse();
         FileUtil fileUtil = new FileUtil();
         //手配・工事番号check
        if(tblDirection.getDirectionCode()==null||"".equals(tblDirection.getDirectionCode())){
             response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        }
        //手配・工事内容check
        if(tblDirection.getDirectionCategory()==null){
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        }else if(!fileUtil.isNumber(String.valueOf(tblDirection.getDirectionCategory()))){
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_isnumber"));
            return response;
        }
       
        
//        //数量check
//        if(tblDirection.getQuantity()==null || !fileUtil.isDouble(String.valueOf(tblDirection.getQuantity()))){
//             response.setError(true);
//            response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_isnumber"));
//            return response;
//        }
        
        if (tblDirection.getDueDate() != null && !"".equals(tblDirection.getDueDate())) {
            //时间格式check
            if (!FileUtil.isValidDate(tblDirection.getDueDate())) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid"));
                return response;
            }
        }
        
        return response;
    }

     /**
     * 手配テーブル取得
     *
     * @return
     */
    public TblDirectionList getDirections() {
        Query query = entityManager.createNamedQuery("TblDirection.findAll");
        TblDirectionList tblDirectionList =(TblDirectionList) query.getResultList();
        return tblDirectionList;
    }
    
    /**
     * 手配・工事テーブル、生産実績テーブル、生産実績明細テーブルより条件にあてはまるレコードを検索し、
     * システム設定の一覧表示最大件数を超えるときは警告。
     * @param directionCode
     * @param department
     * @param startDueDate
     * @param endDueDate
     * @param poNumber
     * @param loginUser
     * @return 
     */
    public CountResponse getProductionRefCompareWithDirectionCount(String directionCode,String department,String startDueDate,String endDueDate,String poNumber,LoginUser loginUser){
        List list = getProductionRefDirection(directionCode,department,startDueDate,endDueDate,poNumber,loginUser,"count");
        CountResponse count = new CountResponse();
        List object = (List)list.get(0);
        Long counts = ((Long) object.get(0));
        count.setCount(counts);
        return count;
    }
    
    /**
     * 手配・工事テーブル、生産実績テーブル、生産実績明細テーブルより条件にあてはまるレコードを検索し、
     * 手配番号の降順で表示する。
     * @param directionCode
     * @param department
     * @param startDueDate
     * @param endDueDate
     * @param poNumber
     * @param loginUser
     * @return 
     */
    public TblDirectionList getProductionRefCompareWithDirection(String directionCode,String department,String startDueDate,String endDueDate,String poNumber,LoginUser loginUser){
        TblDirectionList tblDirectionList = new TblDirectionList();
        List list = getProductionRefDirection(directionCode,department,startDueDate,endDueDate,poNumber,loginUser,"");
        tblDirectionList.setTblDirectionVoList(list);
        return tblDirectionList;
    }
    
    
    /**
     * 手配・工事テーブル、生産実績テーブル、生産実績明細テーブルより条件にあてはまるレコードを検索し、
     * 手配番号の降順で表示する。
     * @param directionCode
     * @param department
     * @param startDueDate
     * @param endDueDate
     * @param poNumber
     * @param loginUser
     * @param action
     * @return 
     */
    public List getProductionRefDirection(String directionCode, String department, String startDueDate, String endDueDate, String poNumber, LoginUser loginUser, String action) {
        List tblDirectionVoList = new ArrayList<>();
        String strDirectionCode = "";
        String strDepartment = "";
        String strPoNumber = "";
//        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);

        StringBuffer sql;
        if ("count".equals(action)) {
            sql = new StringBuffer("SELECT count(a.ID) FROM tbl_direction a WHERE 1=1 ");
        } else {
            sql = new StringBuffer("SELECT");
            sql.append(" a.ID, ");
            sql.append(" a.DIRECTION_CODE, ");
            sql.append(" a.DIRECTION_CATEGORY, ");
            sql.append(" a.COMPONENT_ID, ");
            sql.append(" e.COMPONENT_CODE, ");
            sql.append(" e.COMPONENT_NAME, ");
            sql.append(" a.QUANTITY, ");
            sql.append(" a.DUE_DATE, ");
            //未着手数量
            sql.append(" a.QUANTITY - SUM(CASE WHEN f.FINAL_FLG = 0 THEN CASE WHEN f.COMPLETE_COUNT IS NULL THEN 0 ELSE (CASE WHEN f.Balance IS NOT NULL THEN f.balance ELSE f.COMPLETE_COUNT END) END ELSE 0 END) AS NOSTARTAMOUNT, ");
//            sql.append(" - SUM(CASE WHEN f.PROCEDURE_ID IS NULL OR f.FINAL_FLG = 1 THEN CASE WHEN f.COMPLETE_COUNT IS NULL THEN 0 ELSE (CASE WHEN f.Balance IS NOT NULL THEN f.balance ELSE f.COMPLETE_COUNT END) END ELSE 0 END) AS NOSTARTAMOUNT,  ");
            //仕掛数量
            sql.append(" SUM(CASE WHEN f.FINAL_FLG = 0 THEN CASE WHEN f.COMPLETE_COUNT IS NULL THEN 0 ELSE (CASE WHEN f.Balance IS NOT NULL THEN f.balance ELSE f.COMPLETE_COUNT END) END ELSE 0 END) AS PRODUCTIONVOLUME,  ");
            //完成数量
            sql.append(" SUM(CASE WHEN f.PROCEDURE_ID IS NULL OR f.FINAL_FLG = 1 THEN CASE WHEN f.COMPLETE_COUNT IS NULL THEN 0 ELSE f.COMPLETE_COUNT END ELSE 0 END) AS COMPLETEAMOUNT, ");
            sql.append(" a.DEPARTMENT, ");
            sql.append(" a.PO_NUMBER ");
            sql.append(" FROM tbl_direction a LEFT OUTER JOIN mst_component e ON a.COMPONENT_ID = e.ID LEFT OUTER JOIN  ");
            sql.append(" ( SELECT d.id,c.COMPONENT_ID,b.DIRECTION_ID,c.PROCEDURE_ID,d.FINAL_FLG,c.COMPLETE_COUNT , g.BALANCE ");
            sql.append(" FROM tbl_production b INNER JOIN tbl_production_detail c ON b.ID = c.PRODUCTION_ID INNER JOIN mst_procedure d ON c.PROCEDURE_ID = d.ID AND (d.FINAL_FLG = 1 or ");
            sql.append(" d.ID IN (SELECT h.id FROM mst_procedure h WHERE 1 > (SELECT COUNT(id) FROM mst_procedure WHERE COMPONENT_ID = h.COMPONENT_ID AND PROCEDURE_CODE < h.PROCEDURE_CODE) AND h.FINAL_FLG = 0)) ");
            sql.append(" LEFT OUTER JOIN tbl_production_lot_balance g ON c.id = g.PRODUCTION_DETAIL_ID) f ON a.ID = f.DIRECTION_ID AND a.COMPONENT_ID = f.COMPONENT_ID  WHERE 1=1 ");
        }
        if (directionCode != null && !"".equals(directionCode)) {
            strDirectionCode = directionCode.trim();
            sql.append(" And a.DIRECTION_CODE LIKE '").append("%" + strDirectionCode + "%").append("'");
        }
        if (department != null && !"".equals(department)) {
            strDepartment = department.trim();
            sql.append(" And a.DEPARTMENT = '").append(Integer.parseInt(strDepartment)).append("'");
        }

        if (startDueDate != null && !"".equals(startDueDate)) {
            sql.append(" And a.DUE_DATE >= '").append(startDueDate).append("'");
        }
        if (endDueDate != null && !"".equals(endDueDate)) {
            sql.append(" And a.DUE_DATE <= '").append(endDueDate).append("'");
        }

        if (poNumber != null && !"".equals(poNumber)) {
            strPoNumber = poNumber.trim();
            sql.append(" And a.PO_NUMBER LIKE '").append("%" + strPoNumber + "%").append("'");
        }
        if ("count".equals(action)) {

        } else {
            sql.append(" GROUP BY a.DIRECTION_CODE,a.COMPONENT_ID ");
            sql.append(" ORDER BY a.DIRECTION_CODE DESC,e.COMPONENT_CODE ASC ");
        }

        Query query = entityManager.createNativeQuery(sql.toString());
//        if (directionCode != null && !"".equals(directionCode)) {
//            query.setParameter(1, strDirectionCode);
//        }
//        if (department != null && !"".equals(department)) {
//            query.setParameter(2, Integer.parseInt(strDepartment));
//        }
//        
//
//            if (startDueDate != null && !"".equals(startDueDate)) {
//                query.setParameter(3, sdf.parse(startDueDate));
//            }
//            if (endDueDate != null && !"".equals(endDueDate)) {
//                query.setParameter(4, sdf.parse(endDueDate));
//            }
//
//        
//        if (poNumber != null && !"".equals(poNumber)) {
//            query.setParameter(5, strPoNumber);
//        }
        List list = query.getResultList();
        

        TblDirectionVo tblDirectionVo;
        if ("".equals(action)) {
            
            Map<String, String> departmentMap = new HashMap<>();
            MstChoiceList mstChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");
            for (MstChoice mstChoice : mstChoiceList.getMstChoice()) {
                departmentMap.put(String.valueOf(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
            }
            Map<String, String> directionCategoryMap = new HashMap<>();
            MstChoiceList mstDirectionChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_direction.direction_caterogy");
            for (MstChoice mstChoice : mstDirectionChoiceList.getMstChoice()) {
                directionCategoryMap.put(String.valueOf(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
            }

            for (int i = 0; i < list.size(); i++) {
                Object[] objes = (Object[]) list.get(i);
                tblDirectionVo = new TblDirectionVo();

                String directionId = String.valueOf(objes[0]);
                tblDirectionVo.setId(directionId);

                String directionCodes = "";
                if (objes[1] != null && !"".equals(objes[1])) {
                    directionCodes = String.valueOf(objes[1]).trim();
                }
                tblDirectionVo.setDirectionCode(directionCodes);

                String directionCategorys = "";
                if (objes[2] != null && !"".equals(objes[2])) {
                    directionCategorys = String.valueOf(objes[2]).trim();
                }
                tblDirectionVo.setDirectionCategory(directionCategorys);
                if (directionCategorys != null && !"".equals(directionCategorys)) {
                    if (directionCategoryMap.size() > 0) {
                        tblDirectionVo.setDirectionCategoryText(directionCategoryMap.get(String.valueOf(directionCategorys)));
                    } else {
                        tblDirectionVo.setDirectionCategoryText("");
                    }
                } else {
                    tblDirectionVo.setDirectionCategoryText("");
                }

                String componentId = "";
                String componentCode = "";
                String componentName = "";
                if (objes[3] != null && !"".equals(objes[3])) {
                    componentId = String.valueOf(objes[3]).trim();
                    tblDirectionVo.setComponentId(componentId);
                } else {
                    tblDirectionVo.setComponentId(componentId);
                }
                if (objes[4] != null && !"".equals(objes[4])) {
                    componentCode = String.valueOf(objes[4]).trim();
                    tblDirectionVo.setComponentCode(componentCode);
                } else {
                    tblDirectionVo.setComponentCode(componentCode);
                }
                if (objes[5] != null && !"".equals(objes[5])) {
                    componentName = String.valueOf(objes[5]);
                    tblDirectionVo.setComponentName(componentName);
                } else {
                    tblDirectionVo.setComponentName(componentName);
                }

                String quantity = "";
                if (objes[6] != null && !"".equals(objes[6])) {
                    quantity = String.valueOf(objes[6]).trim();
                    tblDirectionVo.setQuantity(quantity);
                } else {
                    tblDirectionVo.setQuantity("");
                }

                String dueDate = "";
                if (objes[7] != null && !"".equals(objes[7])) {
                    dueDate = String.valueOf(objes[7]).trim();
                    tblDirectionVo.setDueDate(dueDate);
                } else {
                    tblDirectionVo.setDueDate(dueDate);
                }

                String noStartAmount = "";
                String productionVolume = "";
                String completeAmount = "";
                if (objes[8] != null && !"".equals(objes[8])) {
                    noStartAmount = String.valueOf(objes[8]).trim();
                    tblDirectionVo.setNoStartAmount(noStartAmount);
                } else {
                    tblDirectionVo.setNoStartAmount(noStartAmount);
                }
                if (objes[9] != null && !"".equals(objes[9])) {
                    productionVolume = String.valueOf(objes[9]).trim();
                    tblDirectionVo.setProductionVolume(productionVolume);
                } else {
                    tblDirectionVo.setProductionVolume(productionVolume);
                }
                if (objes[10] != null && !"".equals(objes[10])) {
                    completeAmount = String.valueOf(objes[10]).trim();
                    tblDirectionVo.setCompleteAmount(completeAmount);
                } else {
                    tblDirectionVo.setCompleteAmount(completeAmount);
                }

                String departments = "";
                if (objes[11] != null && !"".equals(objes[11])) {
                    departments = String.valueOf(objes[11]).trim();
                    tblDirectionVo.setDepartment(departments);
                } else {
                    tblDirectionVo.setDepartment(departments);
                }

                if (departments != null && !"".equals(departments)) {
                    if (departmentMap != null && departmentMap.size() > 0) {
                        tblDirectionVo.setDepartmentText(departmentMap.get(String.valueOf(departments)));
                    } else {
                        tblDirectionVo.setDepartmentText("");
                    }
                } else {
                    tblDirectionVo.setDepartmentText("");
                }

                String poNumbers = "";
                if (objes[12] != null && !"".equals(objes[12])) {
                    poNumbers = String.valueOf(objes[12]).trim();
                    tblDirectionVo.setPoNumber(poNumbers);
                } else {
                    tblDirectionVo.setPoNumber(poNumbers);
                }

                tblDirectionVoList.add(tblDirectionVo);
            }
        } else {
            tblDirectionVoList.add(list);
        }

        return tblDirectionVoList;
    }
    
    /**
     *  生産実績データ取得
     * @param directionId
     * @return 
     */
    public List<TblProduction> getTblProduction(String directionId){
        List<TblProduction> response = new ArrayList<>();
        Query query = entityManager.createNamedQuery("TblProduction.findByDirectionId");
        query.setParameter("directionId", directionId);
        response = query.getResultList();
        return response;
    }
    
    
    /**
     * 詳細判定
     * 生産計画対比照会または生産実績照会に遷移する。
     * @param id
     * @param loginUser
     * @return 
     */
    public TblDirectionVo getProductionRefCompareWithDirectionFlag(String id, LoginUser loginUser) {
        TblDirection tblDirection = entityManager.find(TblDirection.class, id);

        TblDirectionVo tblDirectionVo = new TblDirectionVo();
        tblDirectionVo.setId(id);
        tblDirectionVo.setDirectionCode(tblDirection.getDirectionCode() == null ? "" : tblDirection.getDirectionCode());
        int flag = 0;

        //生産計画照会対比
        String sqlPlan = "SELECT t FROM TblProductionPlan t WHERE t.directionId = :directionId ";
        Query queryPlan = entityManager.createQuery(sqlPlan);
        queryPlan.setParameter("directionId", id);

        List listPlan = queryPlan.getResultList();
        if (listPlan != null && listPlan.size() > 0) {
            flag = 1;
            tblDirectionVo.setActionFlag(String.valueOf(flag));
            return tblDirectionVo;
        }

        //生産実績照会対比
        String sqlProduction = "SELECT t FROM TblProduction t WHERE t.directionId = :directionId ";
        Query queryProduction = entityManager.createQuery(sqlProduction);
        queryProduction.setParameter("directionId", id);

        List listProduction = queryProduction.getResultList();
        if (listProduction != null && listProduction.size() > 0) {
            flag = 2;
            tblDirectionVo.setActionFlag(String.valueOf(flag));
            return tblDirectionVo;
        }

        if (flag == 0) {
            tblDirectionVo.setError(true);
            tblDirectionVo.setErrorCode(ErrorMessages.E201_APPLICATION);
            tblDirectionVo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
        }

        return tblDirectionVo;
    }
    
    
    /**
     * T0015 手配別生産実績照会 CSV出力
     * @param directionCode
     * @param department
     * @param startDueDate
     * @param endDueDate
     * @param poNumber
     * @param loginUser
     * @return 
     */
    public FileReponse getProductionRefCompareWithDirectionCSV(String directionCode,String department,String startDueDate,String endDueDate,String poNumber,LoginUser loginUser){
        
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList HeadList = new ArrayList();
        ArrayList lineList;
        String langId = loginUser.getLangId();
        FileReponse fr = new FileReponse();
        FileUtil fu = new FileUtil();
        
        Map<String, String> departmentMap = new HashMap<>();
        MstChoiceList mstChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");
        for (MstChoice mstChoice : mstChoiceList.getMstChoice()) {
            departmentMap.put(String.valueOf(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
        }
        Map<String, String> directionCategoryMap = new HashMap<>();
        MstChoiceList mstDirectionChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_direction.direction_caterogy");
        for (MstChoice mstChoice : mstDirectionChoiceList.getMstChoice()) {
            directionCategoryMap.put(String.valueOf(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
        }
        
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        //TODO
        String outDirectionCode = mstDictionaryService.getDictionaryValue(langId, "direction_code");
        String outDirectionCategory = mstDictionaryService.getDictionaryValue(langId, "direction_value");
        String outComponentCode = mstDictionaryService.getDictionaryValue(langId, "component_code");
        String outComponentName = mstDictionaryService.getDictionaryValue(langId, "component_name");
        String outQuantity = mstDictionaryService.getDictionaryValue(langId, "quantity");
        String outDuDate = mstDictionaryService.getDictionaryValue(langId, "due_date");
        String outNoStartAmount = mstDictionaryService.getDictionaryValue(langId, "no_start_amount");
        String outProductionVolume = mstDictionaryService.getDictionaryValue(langId, "producing_amount");
        String outCompleteAmount = mstDictionaryService.getDictionaryValue(langId, "complete_amount");
        String outDepartment = mstDictionaryService.getDictionaryValue(langId, "work_user_department");
        String outPoNumber = mstDictionaryService.getDictionaryValue(langId, "po_number");
        
        /*Head*/
        HeadList.add(outDirectionCode);
        HeadList.add(outDirectionCategory);
        HeadList.add(outComponentCode);
        HeadList.add(outComponentName);
        HeadList.add(outQuantity);
        HeadList.add(outDuDate);
        HeadList.add(outNoStartAmount);
        HeadList.add(outProductionVolume);
        HeadList.add(outCompleteAmount);
        HeadList.add(outDepartment);
        HeadList.add(outPoNumber);
        gLineList.add(HeadList);
        
        //明細データを取得
        List list = getProductionRefDirection(directionCode,department,startDueDate,endDueDate,poNumber,loginUser,"");
        
        TblDirectionList response = new TblDirectionList();
        response.setTblDirectionVoList(list);
        
        for(TblDirectionVo tblDirectionVo : response.getTblDirectionVoList()){
            
            lineList = new ArrayList();
            lineList.add(tblDirectionVo.getDirectionCode() == null ? "" : tblDirectionVo.getDirectionCode());
            
            if(tblDirectionVo.getDirectionCategory() != null && !"".equals(tblDirectionVo.getDirectionCategory())){
                if(directionCategoryMap != null && directionCategoryMap.size() > 0){
                    lineList.add(directionCategoryMap.get(tblDirectionVo.getDirectionCategory()));
                }else{
                    lineList.add("");
                }
            }else{
                lineList.add("");
            }
            
            lineList.add(tblDirectionVo.getComponentCode() == null ? "" : tblDirectionVo.getComponentCode());
            lineList.add(tblDirectionVo.getComponentName() == null ? "" : tblDirectionVo.getComponentName());
            lineList.add(tblDirectionVo.getQuantity() == null ? "0.0000" : tblDirectionVo.getQuantity());
            lineList.add(tblDirectionVo.getDueDate() == null ? "" : tblDirectionVo.getDueDate());
            lineList.add(tblDirectionVo.getNoStartAmount() == null ? "0" : tblDirectionVo.getNoStartAmount());
            lineList.add(tblDirectionVo.getProductionVolume() == null ? "0" : tblDirectionVo.getProductionVolume());
            lineList.add(tblDirectionVo.getCompleteAmount() == null ? "0" : tblDirectionVo.getCompleteAmount());
            
            if(tblDirectionVo.getDepartment() != null && !"".equals(tblDirectionVo.getDepartment())){
                if(departmentMap != null && departmentMap.size() > 0){
                    lineList.add(departmentMap.get(tblDirectionVo.getDepartment()));
                }else{
                    lineList.add("");
                }
            }else{
                lineList.add("");
            }
            
            lineList.add(tblDirectionVo.getPoNumber() == null ? "" : tblDirectionVo.getPoNumber());
            
            gLineList.add(lineList);
        }
        
        CSVFileUtil csvFileUtil = null;
        try {
            csvFileUtil = new CSVFileUtil(outCsvPath, "csvOutput");
            Iterator<ArrayList> iter = gLineList.iterator();
            while (iter.hasNext()) {
                csvFileUtil.toCSVLine(iter.next());
            }
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            // CSVファイルwriterのクローズ処理
            if (csvFileUtil != null) {
                csvFileUtil.close();
            }
        }
        
        //TODO
        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_DIRECTION);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_PRODUCTION_REF_COMPARE_WITH_DIRECTION);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, CommonConstants.TBL_PRODUCTION_REF_COMPARE_WITH_DIRECTION);
        tblCsvExport.setClientFileName(fu.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        
        return fr;
    }
    
    /**
     * 手配テーブルから条件にあてはまるデータを取得し、手配・工事番号の昇順で表示する。
     *
     * @param directionCode
     * @param dueDateStart
     * @param dueDateEnd
     * @param department
     * @param poNumber
     * @param loginUser
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isPage
     * @return
     */
    public TblDirectionList getTblDirectionsByPage(String directionCode, String dueDateStart, String dueDateEnd,
            String department, String poNumber, LoginUser loginUser, String sidx, String sord, int pageNumber,
            int pageSize, boolean isPage) {
        TblDirectionList response = new TblDirectionList();
        
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        FileUtil fu = new FileUtil();
        if (dueDateStart != null && !"".equals(dueDateStart)) {
            if (!fu.dateCheck(dueDateStart)) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(),
                        "msg_error_date_format_invalid"));
                return response;
            }
        }

        if (dueDateEnd != null && !"".equals(dueDateEnd)) {
            if (!fu.dateCheck(dueDateEnd)) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(),
                        "msg_error_date_format_invalid"));
                return response;
            }
        }
        
        if (isPage) {

            List count = getDirectionSqlByPage(directionCode, dueDateStart, dueDateEnd, department, poNumber, sidx,
                    sord, pageNumber, pageSize, true);

            // ページをめぐる
            Pager pager = new Pager();
            response.setPageNumber(pageNumber);
            long counts = (long) count.get(0);
            response.setCount(counts);
            response.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));

        }

        List list = getDirectionSqlByPage(directionCode, dueDateStart, dueDateEnd, department, poNumber, sidx,
                sord, pageNumber, pageSize, false);
        Map<String, String> departmentMap = new HashMap<>();
        Map<String, String> directionvalueMap = new HashMap<>();
        MstChoiceList mstChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");
        for (MstChoice mstChoice : mstChoiceList.getMstChoice()) {
            departmentMap.put(String.valueOf(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
        }
        TblDirectionVo tblDirectionVo = null;
        List<TblDirectionVo> tblDirectionVoList = new ArrayList<>();
        MstChoiceList mstChoiceList1 = mstChoiceService.getChoice(loginUser.getLangId(),
                "tbl_direction.direction_caterogy");
        for (MstChoice mstChoice : mstChoiceList1.getMstChoice()) {
            directionvalueMap.put(String.valueOf(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
        }

        for (int i = 0; i < list.size(); i++) {
            TblDirection tblDirection = (TblDirection) list.get(i);
            tblDirectionVo = new TblDirectionVo();
            if (tblDirection.getDepartment() != null) {
                tblDirectionVo.setDepartment(String.valueOf(tblDirection.getDepartment()));
                if (departmentMap != null && departmentMap.size() > 0) {
                    tblDirectionVo.setDepartmentText(departmentMap.get(String.valueOf(tblDirection.getDepartment())));
                } else {
                    tblDirectionVo.setDepartmentText("");
                }
            } else {
                tblDirectionVo.setDepartment("");
                tblDirectionVo.setDepartmentText("");
            }
            tblDirectionVo.setId(tblDirection.getId() == null ? "" : tblDirection.getId());

            tblDirectionVo
                    .setDirectionCode(tblDirection.getDirectionCode() == null ? "" : tblDirection.getDirectionCode());
            tblDirectionVo.setComponentId(tblDirection.getComponentId() == null ? "" : tblDirection.getComponentId());
            if (tblDirection.getMstComponent() != null) {
                tblDirectionVo.setComponentCode(tblDirection.getMstComponent().getComponentCode());
                tblDirectionVo.setComponentName(tblDirection.getMstComponent().getComponentName());
            } else {
                tblDirectionVo.setComponentCode("");
                tblDirectionVo.setComponentName("");
            }

            tblDirectionVo.setQuantity(tblDirection.getQuantity().toString());

            if (tblDirection.getDirectionCategory() != null) {
                tblDirectionVo.setDirectionCategory(String.valueOf(tblDirection.getDirectionCategory()));
                if (directionvalueMap != null && directionvalueMap.size() > 0) {
                    tblDirectionVo.setCategorytext(
                            directionvalueMap.get(String.valueOf(tblDirection.getDirectionCategory())));
                } else {
                    tblDirectionVo.setCategorytext("");
                }
            } else {
                tblDirectionVo.setDirectionCategory("");
            }

            if (tblDirection.getDueDate() != null) {
                tblDirectionVo.setDueDate(sdf.format(tblDirection.getDueDate()));
            } else {
                tblDirectionVo.setDueDate("");
            }

            if (tblDirection.getMstMold() != null) {
                tblDirectionVo.setMoldId(tblDirection.getMstMold().getMoldId());
                tblDirectionVo.setMoldName(tblDirection.getMstMold().getMoldName());
            } else {
                tblDirectionVo.setMoldId("");
                tblDirectionVo.setMoldName("");
            }

            tblDirectionVo.setPoNumber(tblDirection.getPoNumber() == null ? "" : tblDirection.getPoNumber());

            tblDirectionVoList.add(tblDirectionVo);
        }

        response.setTblDirectionVoList(tblDirectionVoList);
        return response;
    }
    
    /**
     * sql文の用
     *
     * @param directionCode
     * @param dueDateStart
     * @param dueDateEnd
     * @param department
     * @param poNumber
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isCount
     * @return
     */
    public List getDirectionSqlByPage(String directionCode, String dueDateStart, String dueDateEnd, String department,
            String poNumber, String sidx, String sord, int pageNumber, int pageSize, boolean isCount) {
        StringBuilder sql = null;
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        if (isCount) {
            sql = new StringBuilder("SELECT count(t) FROM TblDirection t WHERE 1=1 ");
        } else {
            
            sql = new StringBuilder(
                    "SELECT t FROM TblDirection t LEFT JOIN FETCH t.mstComponent mc LEFT JOIN FETCH t.mstMold mold WHERE 1=1 ");
        }
//        sql.append(" and exists (SELECT p FROM t.mstComponent.mstProcedureCollection p WHERE p.finalFlg = 1) ");

        String sqlDirectionCode = "";
        int sqlDepartment = 0;
        String sqlPoNumber = "";
        if (directionCode != null && !"".equals(directionCode)) {
            sqlDirectionCode = directionCode.trim();
            sql.append(" and t.directionCode LIKE :directionCode ");
        }
        if (dueDateStart != null && !"".equals(dueDateStart)) {
            sql.append(" and t.dueDate >= :dueDateStart ");
        }
        if (dueDateEnd != null && !"".equals(dueDateEnd)) {
            sql.append(" and t.dueDate <= :dueDateEnd ");
        }
        if (department != null && !"".equals(department)) {
            sql.append(" and t.department = :department ");
        }
        if (poNumber != null && !"".equals(poNumber)) {
            sqlPoNumber = poNumber.trim();
            sql.append(" and t.poNumber LIKE :poNumber ");
        }

        if (!isCount) {

            if (StringUtils.isNotEmpty(sidx)) {

                String sortStr = orderKey.get(sidx) + " " + sord;

                sql.append(sortStr);

            } else {

                sql.append(" Order by t.directionCode "); // directionCodeの昇順

            }

        }
        
        Query query = entityManager.createQuery(sql.toString());
        if (directionCode != null && !"".equals(directionCode)) {
            query.setParameter("directionCode", "%" + sqlDirectionCode + "%");
        }
        try {
            if (dueDateStart != null && !"".equals(dueDateStart)) {

                query.setParameter("dueDateStart", sdf.parse(dueDateStart));

            }
            if (dueDateEnd != null && !"".equals(dueDateEnd)) {
                query.setParameter("dueDateEnd", sdf.parse(dueDateEnd));
            }
        } catch (ParseException ex) {
            Logger.getLogger(TblDirectionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (department != null && !"".equals(department)) {
            query.setParameter("department", Integer.parseInt(department));
        }

        if (poNumber != null && !"".equals(poNumber)) {
            query.setParameter("poNumber", "%" + sqlPoNumber + "%");
        }
        
        // 画面改ページを設定する
        if (!isCount) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }

        return query.getResultList();
    }
    
}
