/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.plan;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.direction.TblDirection;
import com.kmcj.karte.resources.direction.TblDirectionService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import com.kmcj.karte.resources.work.TblWorkResource;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;

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
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;

/**
 * 工程マスタ（部品ごとの製造手順）サービス
 *
 * @author t.ariki
 */
@Dependent
public class TblProductionPlanService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private MstComponentService mstComponentService;

    @Inject
    MstProcedureService mstProcedureService;

    @Inject
    TblDirectionService tblDirectionService;
    
    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("componentCode", " ORDER BY mc.componentCode ");// 部品コード
        orderKey.put("componentName", " ORDER BY mc.componentName ");// 部品名称
        orderKey.put("procedureCode", " ORDER BY mp.procedureCode ");// 部品工程番号
        orderKey.put("procedureName", " ORDER BY mp.procedureName ");// 部品工程名称
        orderKey.put("quantity", " ORDER BY m.quantity ");// 数量
        orderKey.put("procedureDueDate", " ORDER BY m.procedureDueDate ");// 工程納期
        orderKey.put("directionCode", " ORDER BY td.directionCode ");// 手配番号

    }

    /**
     * システム設定の一覧表示最大件数を超える場合は警告。
     *
     * @param componentCode
     * @param procedureDueDateFrom
     * @param procedureDueDateTo
     * @param directionNumber
     * @param completeFlg
     * @return
     */
    public CountResponse getRecordCount(String componentCode, String procedureDueDateFrom, String procedureDueDateTo,
            String directionNumber, String completeFlg) {
        List list = getSql(componentCode, procedureDueDateFrom, procedureDueDateTo, 0, directionNumber,"", completeFlg, "count");
        CountResponse count = new CountResponse();
        count.setCount(((Long) list.get(0)));
        return count;
    }

    /**
     * 生産計画テーブルから条件にあてはまるデータを取得し
     *
     * @param componentCode
     * @param procedureDueDateFrom
     * @param procedureDueDateTo
     * @param finalProcedureOnly
     * @param directionNumber
     * @param directionId
     * @param completeFlg
     * @param loginUser
     * @return
     */
    public TblProductionPlanList getTblProductionPlans(
            String componentCode,
            String procedureDueDateFrom,
            String procedureDueDateTo,
            int finalProcedureOnly,
            String directionNumber,
            String directionId,
            String completeFlg,
            LoginUser loginUser) {
        TblProductionPlanList tblProductionPlanList = new TblProductionPlanList();
        List list = getSql(componentCode, procedureDueDateFrom, procedureDueDateTo, finalProcedureOnly, directionNumber, directionId,completeFlg, "");

        List<TblProductionPlanVo> tblProductionPlanVoList = new ArrayList<>();
        //取值
        for (int i = 0; i < list.size(); i++) {
            TblProductionPlan tblProductionPlan = (TblProductionPlan) list.get(i);
            TblProductionPlanVo tblProductionPlanVo = new TblProductionPlanVo();
            tblProductionPlanVo.setId(tblProductionPlan.getId());
            // 部品
            MstComponent mstComponent = tblProductionPlan.getMstComponent();
            if (mstComponent != null) {
                //  部品ID
                tblProductionPlanVo.setComponentId(mstComponent.getId());
                // 部品コード
                tblProductionPlanVo.setComponentCode(mstComponent.getComponentCode());
                // 部品名称
                tblProductionPlanVo.setComponentName(mstComponent.getComponentName());
            } else {
                //  部品ID
                tblProductionPlanVo.setComponentId(tblProductionPlan.getComponentId());
                // 部品コード
                tblProductionPlanVo.setComponentCode("");
                // 部品名称
                tblProductionPlanVo.setComponentName("");
            }

            //工番
            MstProcedure mstProcedure = tblProductionPlan.getMstProcedure();
            if (mstProcedure != null) {
                tblProductionPlanVo.setProcedureId(mstProcedure.getId());
                tblProductionPlanVo.setProcedureCode(mstProcedure.getProcedureCode());
                //工程名称	
                tblProductionPlanVo.setProcedureName(mstProcedure.getProcedureName());
            } else {
                tblProductionPlanVo.setProcedureId(tblProductionPlan.getProcedureId());
                tblProductionPlanVo.setProcedureCode("");
                //工程名称	
                tblProductionPlanVo.setProcedureName("");
            }

            //数量
            tblProductionPlanVo.setQuantity(String.valueOf(tblProductionPlan.getQuantity()));
            //工程納期	
            FileUtil fu = new FileUtil();
            tblProductionPlanVo.setProcedureDueDate(fu.getDateFormatForStr(tblProductionPlan.getProcedureDueDate()));
            //手配番号
            TblDirection tblDirection = tblProductionPlan.getTblDirection();
            if (tblDirection != null) {
                tblProductionPlanVo.setDirectionId(tblDirection.getId());
                tblProductionPlanVo.setDirectionCode(tblDirection.getDirectionCode());
            } else {
                tblProductionPlanVo.setDirectionId(tblProductionPlan.getDirectionId());
                tblProductionPlanVo.setDirectionCode("");
            }
            //完成数量
            if (tblProductionPlan.getCompletedCount() != null) {
                tblProductionPlanVo.setCompletedCount(String.valueOf(tblProductionPlan.getCompletedCount()));
            } else {
                tblProductionPlanVo.setCompletedCount("");
            }
            //未完成数量
            if (tblProductionPlan.getUncompletedCount() != null) {
                tblProductionPlanVo.setUncompletedCount(String.valueOf(tblProductionPlan.getUncompletedCount()));
            } else {
                tblProductionPlanVo.setUncompletedCount("");
            }
            //赋值リスト
            tblProductionPlanVoList.add(tblProductionPlanVo);
        }

        tblProductionPlanList.setTblProductionPlanVo(tblProductionPlanVoList);
        return tblProductionPlanList;
    }

    /**
     *
     * システム設定の一覧表示最大件数を超える場合は警告。
     * 生産計画テーブルから条件にあてはまるデータを取得し、工程納期の降順、部品コード、工番の昇順で表示する。
     *
     * @param componentCode
     * @param procedureDueDateFrom
     * @param procedureDueDateTo
     * @param directionNumber
     * @param action
     * @return
     */
    private List getSql(String componentCode,
            String procedureDueDateFrom, String procedureDueDateTo,
            int finalProcedureOnly,
            String directionNumber,
            String directionId,
            String completeFlg,
            String action) {

        StringBuilder sql;
        //
        if ("count".equals(action)) {
            sql = new StringBuilder("SELECT COUNT(m) FROM TblProductionPlan m WHERE 1=1 ");
        } else {
            sql = new StringBuilder(" SELECT m FROM TblProductionPlan m LEFT JOIN FETCH m.mstComponent LEFT JOIN FETCH m.tblDirection LEFT JOIN FETCH  m.mstProcedure WHERE 1=1 ");
        }

        // 日付項目をDate型(yyyy-MM-dd)に変換
        SimpleDateFormat sdf = new SimpleDateFormat(com.kmcj.karte.util.DateFormat.DATE_FORMAT);
        java.util.Date formatProcedureDueDateFrom = null;
        java.util.Date formatProcedureDueDateTo = null;
        try {
            if (procedureDueDateFrom != null && !"".equals(procedureDueDateFrom)) {
                formatProcedureDueDateFrom = sdf.parse(procedureDueDateFrom);
            }
            if (procedureDueDateTo != null && !"".equals(procedureDueDateTo)) {
                formatProcedureDueDateTo = sdf.parse(procedureDueDateTo);
            }
        } catch (ParseException ex) {
            Logger.getLogger(TblWorkResource.class.getName()).log(Level.WARNING, null, "日付形式不正 workingDateFrom[" + procedureDueDateFrom + "], workingDateTo[" + procedureDueDateTo + "]");
        }

        if (componentCode != null && !"".equals(componentCode)) {
            sql = sql.append(" and m.mstComponent.componentCode LIKE :componentCode ");
        }

        if (formatProcedureDueDateFrom != null) {
            sql = sql.append(" and m.procedureDueDate >= :formatProcedureDueDateFrom ");
        }

        if (formatProcedureDueDateTo != null) {
            sql = sql.append(" and m.procedureDueDate <= :formatProcedureDueDateTo ");
        }

        if (finalProcedureOnly == 1) {
            sql = sql.append(" and m.mstProcedure.finalFlg = 1 ");
        }

        if (directionNumber != null && !"".equals(directionNumber)) {
            sql.append(" and m.tblDirection.directionCode LIKE :directionNumber ");
        }

        if (directionId != null && !"".equals(directionId)) {
            sql.append(" and m.directionId = :directionId ");
        }
        if (completeFlg != null && "1".equals(completeFlg)) {
            sql.append(" and m.uncompletedCount > :completeFlg ");
        }

        //工程納期の降順、部品コード、工番の昇順で表示する。
        sql = sql.append(" ORDER BY m.procedureDueDate DESC , m.mstComponent.componentCode ASC ,m.mstProcedure.procedureCode ASC");

        Query query = entityManager.createQuery(sql.toString());

        if (componentCode != null && !"".equals(componentCode)) {
            query.setParameter("componentCode", "%" + componentCode.trim() + "%");
        }
        if (formatProcedureDueDateFrom != null) {
            query.setParameter("formatProcedureDueDateFrom", formatProcedureDueDateFrom);
        }
        if (formatProcedureDueDateTo != null) {
            query.setParameter("formatProcedureDueDateTo", formatProcedureDueDateTo);
        }
        
        if (directionNumber != null && !"".equals(directionNumber)) {
            query.setParameter("directionNumber", "%" + directionNumber.trim() + "%");
        }

        if (directionId!= null && !"".equals(directionId)) {
            query.setParameter("directionId", directionId.trim());
        }
        
        if (completeFlg != null && "1".equals(completeFlg)) {
            query.setParameter("completeFlg", 0);
        }
        List list = query.getResultList();
        return list;
    }

    /**
     * id存在check
     *
     * @param id
     * @return
     */
    public boolean getTblProductionPlanExistCheck(String id) {
        Query query = entityManager.createNamedQuery("TblProductionPlan.findById");
        query.setParameter("id", id);
        return query.getResultList().size() > 0;
    }

    /**
     * CSV入力データ存在チェック
     *
     * @param componentId
     * @param procedureId
     * @param directionId
     * @return
     */
    public TblProductionPlan getTblProductionPlanExistChecks(String componentId, String procedureId, String directionId) {
        Query query = entityManager.createNamedQuery("TblProductionPlan.check");
        query.setParameter("componentId", componentId);
        query.setParameter("procedureId", procedureId);
        query.setParameter("directionId", directionId);
        List list = query.getResultList();
        if (list.size() > 0) {
            return (TblProductionPlan) list.get(0);
        }
        return null;
    }
    
    
    public boolean ProductionPlanExistChecks(String componentId, String procedureId, String directionId) {
        Query query = entityManager.createNamedQuery("TblProductionPlan.check");
        query.setParameter("componentId", componentId);
        query.setParameter("procedureId", procedureId);
        query.setParameter("directionId", directionId);
        List list = query.getResultList();
        return list.size() > 0;
    }

    /**
     * 画面で入力された値を用いて生産計画テーブルへ追加.更新を行う。
     *
     * @param blProductionPlan
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse createTblProductionPlan(TblProductionPlan blProductionPlan, LoginUser loginUser) {
        blProductionPlan.setId(IDGenerator.generate());
        blProductionPlan.setCreateDate(new java.util.Date());
        blProductionPlan.setUpdateDate(new java.util.Date());
        blProductionPlan.setCreateUserUuid(loginUser.getUserUuid());
        blProductionPlan.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.persist(blProductionPlan);

        BasicResponse response = new BasicResponse();
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_added"));
        return response;
    }

    /**
     * 選択されている生産計画情報の詳細画面に読み取り専用で遷移する。
     *
     * @param id
     * @param displayTimeZone
     * @return
     */
    public TblProductionPlanVo getProductionPlanDetails(String id, String displayTimeZone) {
        TblProductionPlanVo planvo = new TblProductionPlanVo();
        Query query = entityManager.createNamedQuery("TblProductionPlan.findById");
        query.setParameter("id", id);
        List<TblProductionPlan> list = query.getResultList();
        if (null != list && !list.isEmpty()) {
            TblProductionPlan proplan = list.get(0);
            if (proplan.getMstComponent() != null) {
                planvo.setComponentId(proplan.getMstComponent().getId());
                planvo.setComponentCode(proplan.getMstComponent().getComponentCode());
                planvo.setComponentName(proplan.getMstComponent().getComponentName());
            } else {
                planvo.setComponentId("");
                planvo.setComponentCode("");
                planvo.setComponentName("");
            }
            if (proplan.getMstProcedure() != null) {
                planvo.setProcedureId(proplan.getMstProcedure().getId());
                planvo.setProcedureCode(proplan.getMstProcedure().getProcedureCode());
                planvo.setProcedureName(proplan.getMstProcedure().getProcedureName());
            } else {
                planvo.setProcedureId("");
                planvo.setProcedureCode("");
                planvo.setProcedureName("");
            }

            planvo.setQuantity(String.valueOf(proplan.getQuantity()));
            //工程納期	
            FileUtil fu = new FileUtil();
            planvo.setProcedureDueDate(fu.getDateFormatForStr(proplan.getProcedureDueDate()));
            if (proplan.getTblDirection() != null) {
                planvo.setDirectionId(proplan.getTblDirection().getId());
                planvo.setDirectionCode(proplan.getTblDirection().getDirectionCode());
                planvo.setCompletedCount(String.valueOf(proplan.getCompletedCount()));
            } else {
                planvo.setDirectionId("");
                planvo.setDirectionCode("");
                planvo.setCompletedCount("");
            }

            planvo.setUncompletedCount(String.valueOf(proplan.getUncompletedCount()));
        }
        return planvo;
    }

    /**
     * 生産計画テーブル更新
     *
     * @param tblProductionPlan
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse updateTblProductionPlanByQuery(TblProductionPlan tblProductionPlan, LoginUser loginUser) {
        tblProductionPlan.setUpdateDate(new java.util.Date());
        tblProductionPlan.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.merge(tblProductionPlan);

        BasicResponse response = new BasicResponse();
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }

    /**
     * 生産計画テーブル更新
     *
     * @param tblProductionPlanVo
     * @param loginUser
     * @return
     */
    @Transactional
    public int updateTblProductionPlanByVoQuery(TblProductionPlanVo tblProductionPlanVo, LoginUser loginUser) {
        //更新
        StringBuilder sql = new StringBuilder(" UPDATE TblProductionPlan t SET   ");

        if (tblProductionPlanVo.getComponentId() != null && !"".equals(tblProductionPlanVo.getComponentId())) {
            sql.append(" t.componentId = :componentId, ");
        }
        if (tblProductionPlanVo.getProcedureId() != null && !"".equals(tblProductionPlanVo.getProcedureId())) {
            sql.append(" t.procedureId = :procedureId, ");
        }

        if (tblProductionPlanVo.getDirectionId() != null ) {
            sql.append(" t.directionId = :directionId, ");
        }

        if (tblProductionPlanVo.getQuantity() != null && !"".equals(tblProductionPlanVo.getQuantity())) {
            sql.append(" t.quantity = :quantity, ");
        }

        if (tblProductionPlanVo.getProcedureDueDate() != null && !"".equals(tblProductionPlanVo.getProcedureDueDate())) {
            sql.append(" t.procedureDueDate = :procedureDueDate, ");
        }

        sql.append(" t.updateDate = :updateDate, t.updateUserUuid = :updateUserUuid ");

        sql.append(" WHERE t.id = :id ");
        Query query = entityManager.createQuery(sql.toString());

        if (tblProductionPlanVo.getComponentId() != null && !"".equals(tblProductionPlanVo.getComponentId())) {
            query.setParameter("componentId", tblProductionPlanVo.getComponentId());
        }

        if (tblProductionPlanVo.getProcedureId() != null && !"".equals(tblProductionPlanVo.getProcedureId())) {
            query.setParameter("procedureId", tblProductionPlanVo.getProcedureId());
        }

        if (tblProductionPlanVo.getDirectionId() != null && !"".equals(tblProductionPlanVo.getDirectionId())) {
            query.setParameter("directionId", tblProductionPlanVo.getDirectionId());
        }else{
            query.setParameter("directionId", null);
        }

        if (tblProductionPlanVo.getQuantity() != null && !"".equals(tblProductionPlanVo.getQuantity())) {
            query.setParameter("quantity", Integer.valueOf(tblProductionPlanVo.getQuantity().trim()));
        }

        if (tblProductionPlanVo.getProcedureDueDate() != null && !"".equals(tblProductionPlanVo.getProcedureDueDate())) {
            query.setParameter("procedureDueDate", new Date(tblProductionPlanVo.getProcedureDueDate()));
        }

        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", loginUser.getUserUuid());
        query.setParameter("id", tblProductionPlanVo.getId());
        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     * 確認メッセージを出力の上、選択されている生産計画情報を削除する。
     *
     * @param id
     * @return
     */
    @Transactional
    public int deleteTblProductionPlan(String id) {
        Query query = entityManager.createNamedQuery("TblProductionPlan.delete");
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    /**
     * 画面で入力された値をcheck
     *
     * @param blProductionPlan
     * @param loginUser
     * @return
     */
    public BasicResponse checkTblProductionPlan(TblProductionPlan blProductionPlan, LoginUser loginUser) {
        BasicResponse checkResponse = new BasicResponse();

        // 部品IDcheck
        if (null == blProductionPlan.getComponentId() || "".equals(blProductionPlan.getComponentId().trim())) {
            checkResponse.setError(true);
            checkResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            checkResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return checkResponse;
        }

        // 工番IDcheck
        if (null == blProductionPlan.getProcedureId() || "".equals(blProductionPlan.getProcedureId().trim())) {
            checkResponse.setError(true);
            checkResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            checkResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return checkResponse;
        }

        // 工程納期IDcheck
        if (null == blProductionPlan.getProcedureDueDate()) {
            checkResponse.setError(true);
            checkResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            checkResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return checkResponse;
        }

        //时间格式check
        FileUtil fu = new FileUtil();
        if (!FileUtil.isValidDate(fu.getDateFormatForStr(blProductionPlan.getProcedureDueDate()))) {
            checkResponse.setError(true);
            checkResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            checkResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid"));
            return checkResponse;
        }

        // 手配IDcheck
//        if (null == blProductionPlan.getDirectionId() || "".equals(blProductionPlan.getDirectionId())) {
//            checkResponse.setError(true);
//            checkResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
//            checkResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
//            return checkResponse;
//        }
        return checkResponse;
    }

    /**
     * 生産計画テーブルから条件にあてはまるデータを取得し、工程納期の降順、部品コード、工順の昇順でCSVファイルに出力する。
     *
     * @param componentCode
     * @param procedureDueDateFrom
     * @param procedureDueDateTo
     * @param finalProcedureOnly
     * @param directionNumber
     * @param completeFlg
     * @param loginUser
     * @return
     */
    public FileReponse getTblProductionPlansOutputCsv(String componentCode,
            String procedureDueDateFrom,
            String procedureDueDateTo,
            int finalProcedureOnly,
            String directionNumber,
            String completeFlg,
            LoginUser loginUser) {
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList HeadList = new ArrayList();
        ArrayList lineList;
        String langId = loginUser.getLangId();
        FileReponse fr = new FileReponse();
        FileUtil fu = new FileUtil();
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        //String outId = mstDictionaryService.getDictionaryValue(langId, "id");
        String outComponentCode = mstDictionaryService.getDictionaryValue(langId, "component_code");
        String outComponentName = mstDictionaryService.getDictionaryValue(langId, "component_name");
        String outProcedureCode = mstDictionaryService.getDictionaryValue(langId, "procedure_code");
        String outProcedureName = mstDictionaryService.getDictionaryValue(langId, "procedure_name");
        String outQuantity = mstDictionaryService.getDictionaryValue(langId, "quantity");
        String outProcedureDueDate = mstDictionaryService.getDictionaryValue(langId, "procedure_due_date");
        String outDirectionNumber = mstDictionaryService.getDictionaryValue(langId, "direction_number");
        //String outValidFlg = mstDictionaryService.getDictionaryValue(langId, "user_valid_flg");
        //未完成数量
        String outUncompletedCount = mstDictionaryService.getDictionaryValue(langId, "no_start_amount");
        //完成数量
        String outCompletedCount = mstDictionaryService.getDictionaryValue(langId, "complete_amount");
        //String delete = mstDictionaryService.getDictionaryValue(langId, "delete_record");

        /*Head*/
        //HeadList.add(outId);
        HeadList.add(outComponentCode);
        HeadList.add(outComponentName);
        HeadList.add(outProcedureCode);
        HeadList.add(outProcedureName);
        HeadList.add(outQuantity);
        HeadList.add(outProcedureDueDate);
        HeadList.add(outDirectionNumber);
        //HeadList.add(outValidFlg);
        if (completeFlg != null) {
            HeadList.add(outUncompletedCount);
            HeadList.add(outCompletedCount);
        }
        //HeadList.add(delete);

        gLineList.add(HeadList);

        //明細データを取得
        TblProductionPlanList response = getTblProductionPlans(componentCode, procedureDueDateFrom, procedureDueDateTo, finalProcedureOnly, directionNumber, "",completeFlg, loginUser);

        /*Detail*/
        for (int i = 0; i < response.getTblProductionPlanVo().size(); i++) {
            lineList = new ArrayList();
            TblProductionPlanVo tblProductionPlanVo = response.getTblProductionPlanVo().get(i);
            //lineList.add(tblProductionPlanVo.getId());
            lineList.add(tblProductionPlanVo.getComponentCode());
            lineList.add(tblProductionPlanVo.getComponentName());
            lineList.add(tblProductionPlanVo.getProcedureCode());
            lineList.add(tblProductionPlanVo.getProcedureName());
            lineList.add(tblProductionPlanVo.getQuantity());
            lineList.add(tblProductionPlanVo.getProcedureDueDate());
            lineList.add(tblProductionPlanVo.getDirectionCode());
            if (completeFlg != null) {
                lineList.add(tblProductionPlanVo.getUncompletedCount());
                lineList.add(tblProductionPlanVo.getCompletedCount());
            }
            //lineList.add("");
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
        tblCsvExport.setExportTable(CommonConstants.TBL_PRODUCTION_PLAN);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        if (completeFlg != null) {
            mstFunction.setId(CommonConstants.FUN_PRODUCTION_PLAN_COMPARE);
        } else {
            mstFunction.setId(CommonConstants.FUN_PRODUCTION_PLAN);
        }
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName;
        if (completeFlg != null) {
            fileName = mstDictionaryService.getDictionaryValue(langId, "production_ref_compare_with_plan");
        } else {
            fileName = mstDictionaryService.getDictionaryValue(langId, "tbl_production_plan_list");
        }
        tblCsvExport.setClientFileName(fu.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }

    /**
     * CSVの中身に対してチェックを行う
     *
     * @param logParm
     * @param lineCsv
     * @param LangId
     * @param logFile
     * @param index
     * @return
     */
    public boolean checkCsvFileData(Map<String, String> logParm, String lineCsv[], String LangId, String logFile, int index) {

        //ログ出力内容を用意する
        String lineNo = logParm.get("lineNo");
        // String id = logParm.get("id");
        String componentCode = logParm.get("componentCode");
        String componentName = logParm.get("componentName");
        String procedureCode = logParm.get("procedureCode");
        String procedureName = logParm.get("procedureName");
        String quantity = logParm.get("quantity");
        String procedureDueDate = logParm.get("procedureDueDate");
        String directionCode = logParm.get("directionCode");

        String error = logParm.get("error");
        String errorContents = logParm.get("errorContents");
        String nullMsg = logParm.get("nullMsg");
        String notFound = logParm.get("notFound");
        String invalidDate = logParm.get("invalidDate");
        String notNumber = logParm.get("notNumber");
        String maxLangth = logParm.get("maxLangth");
        String layout = logParm.get("layout");

        int arrayLength = lineCsv.length;
        FileUtil fu = new FileUtil();
        if (arrayLength != 7) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, componentCode, "", error, 1, errorContents, layout));
            return false;
        }
        //分割した文字をObjectに格納する
        String strComponentCode = lineCsv[0].trim();
        MstComponent mstComponent = mstComponentService.getMstComponent(strComponentCode);
        if (fu.isNullCheck(strComponentCode)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, componentCode, strComponentCode, error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(strComponentCode, 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, componentCode, strComponentCode, error, 1, errorContents, maxLangth));
            return false;
        } else if (mstComponent == null) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, componentCode, strComponentCode, error, 1, errorContents, notFound));
            return false;
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
        } else {
            MstProcedure mstProcedure = mstProcedureService.getMstProcedureByComponentIdAndProcedureCode(mstComponent.getId(), strProcedureCode);
            if (mstProcedure == null) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, procedureCode, strProcedureCode, error, 1, errorContents, notFound));
                return false;
            }
        }

        String strprocedureName = lineCsv[3].trim();
        if (fu.maxLangthCheck(strprocedureName, 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, procedureName, strprocedureName, error, 1, errorContents, maxLangth));
            return false;
        }

        String strQuantity = lineCsv[4].trim();
        if (fu.maxLangthCheck(strQuantity, 11)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, quantity, strQuantity, error, 1, errorContents, maxLangth));
            return false;
        } else if (!fu.isNumber(strQuantity)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, quantity, strQuantity, error, 1, errorContents, notNumber));
            return false;
        }
        //分割した文字をObjectに格納する
        String strProcedureDueDate = lineCsv[5].trim();
        if (fu.isNullCheck(strProcedureDueDate)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, procedureDueDate, strProcedureDueDate, error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(strProcedureDueDate, 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, procedureDueDate, strProcedureDueDate, error, 1, errorContents, maxLangth));
            return false;
        } else if (!fu.isValidDate(strProcedureDueDate)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, procedureDueDate, strProcedureDueDate, error, 1, errorContents, invalidDate));
            return false;
        }

        String strDirectionCode = lineCsv[6].trim();
//        if (fu.isNullCheck(strDirectionCode)) {
//            //エラー情報をログファイルに記入
//            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, directionCode, strDirectionCode, error, 1, errorContents, nullMsg));
//            return false;
//        } else 
        if (fu.maxLangthCheck(strDirectionCode, 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, directionCode, strDirectionCode, error, 1, errorContents, maxLangth));
            return false;
        } else {
            TblDirection tblDirection = tblDirectionService.getTblDirectionByDirectionCode(strDirectionCode);
            if (tblDirection == null&&!"".equals(strDirectionCode)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, directionCode, strDirectionCode, error, 1, errorContents, notFound));
                return false;
            }
        }

        return true;
    }
    
    /**
     * 生産計画テーブルから条件にあてはまるデータを取得し
     *
     * @param componentCode
     * @param procedureDueDateFrom
     * @param procedureDueDateTo
     * @param finalProcedureOnly
     * @param directionNumber
     * @param directionId
     * @param completeFlg
     * @param loginUser
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isPage
     * @return
     */
    public TblProductionPlanList getTblProductionPlansByPage(
            String componentCode,
            String procedureDueDateFrom,
            String procedureDueDateTo,
            int finalProcedureOnly,
            String directionNumber,
            String directionId,
            String completeFlg,
            LoginUser loginUser, 
            String sidx, 
            String sord, 
            int pageNumber,
            int pageSize, 
            boolean isPage) {
        TblProductionPlanList tblProductionPlanList = new TblProductionPlanList();
        
        if (isPage) {

            List count = getSqlByPage(componentCode, procedureDueDateFrom, procedureDueDateTo, finalProcedureOnly, directionNumber,
                    directionId, completeFlg, sidx, sord, pageNumber, pageSize, true);

            // ページをめぐる
            Pager pager = new Pager();
            tblProductionPlanList.setPageNumber(pageNumber);
            long counts = (long) count.get(0);
            tblProductionPlanList.setCount(counts);
            tblProductionPlanList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));

        }

        List list = getSqlByPage(componentCode, procedureDueDateFrom, procedureDueDateTo, finalProcedureOnly, directionNumber, directionId,
                completeFlg, sidx, sord, pageNumber, pageSize, false);

        List<TblProductionPlanVo> tblProductionPlanVoList = new ArrayList<>();
        //取值
        for (int i = 0; i < list.size(); i++) {
            TblProductionPlan tblProductionPlan = (TblProductionPlan) list.get(i);
            TblProductionPlanVo tblProductionPlanVo = new TblProductionPlanVo();
            tblProductionPlanVo.setId(tblProductionPlan.getId());
            // 部品
            MstComponent mstComponent = tblProductionPlan.getMstComponent();
            if (mstComponent != null) {
                //  部品ID
                tblProductionPlanVo.setComponentId(mstComponent.getId());
                // 部品コード
                tblProductionPlanVo.setComponentCode(mstComponent.getComponentCode());
                // 部品名称
                tblProductionPlanVo.setComponentName(mstComponent.getComponentName());
            } else {
                //  部品ID
                tblProductionPlanVo.setComponentId(tblProductionPlan.getComponentId());
                // 部品コード
                tblProductionPlanVo.setComponentCode("");
                // 部品名称
                tblProductionPlanVo.setComponentName("");
            }

            //工番
            MstProcedure mstProcedure = tblProductionPlan.getMstProcedure();
            if (mstProcedure != null) {
                tblProductionPlanVo.setProcedureId(mstProcedure.getId());
                tblProductionPlanVo.setProcedureCode(mstProcedure.getProcedureCode());
                //工程名称  
                tblProductionPlanVo.setProcedureName(mstProcedure.getProcedureName());
            } else {
                tblProductionPlanVo.setProcedureId(tblProductionPlan.getProcedureId());
                tblProductionPlanVo.setProcedureCode("");
                //工程名称  
                tblProductionPlanVo.setProcedureName("");
            }

            //数量
            tblProductionPlanVo.setQuantity(String.valueOf(tblProductionPlan.getQuantity()));
            //工程納期  
            FileUtil fu = new FileUtil();
            tblProductionPlanVo.setProcedureDueDate(fu.getDateFormatForStr(tblProductionPlan.getProcedureDueDate()));
            //手配番号
            TblDirection tblDirection = tblProductionPlan.getTblDirection();
            if (tblDirection != null) {
                tblProductionPlanVo.setDirectionId(tblDirection.getId());
                tblProductionPlanVo.setDirectionCode(tblDirection.getDirectionCode());
            } else {
                tblProductionPlanVo.setDirectionId(tblProductionPlan.getDirectionId());
                tblProductionPlanVo.setDirectionCode("");
            }
            //完成数量
            if (tblProductionPlan.getCompletedCount() != null) {
                tblProductionPlanVo.setCompletedCount(String.valueOf(tblProductionPlan.getCompletedCount()));
            } else {
                tblProductionPlanVo.setCompletedCount("");
            }
            //未完成数量
            if (tblProductionPlan.getUncompletedCount() != null) {
                tblProductionPlanVo.setUncompletedCount(String.valueOf(tblProductionPlan.getUncompletedCount()));
            } else {
                tblProductionPlanVo.setUncompletedCount("");
            }
            //赋值リスト
            tblProductionPlanVoList.add(tblProductionPlanVo);
        }

        tblProductionPlanList.setTblProductionPlanVo(tblProductionPlanVoList);
        return tblProductionPlanList;
    }
    
    /**
     *
     * システム設定の一覧表示最大件数を超える場合は警告。
     * 生産計画テーブルから条件にあてはまるデータを取得し、工程納期の降順、部品コード、工番の昇順で表示する。
     *
     * @param componentCode
     * @param procedureDueDateFrom
     * @param procedureDueDateTo
     * @param directionNumber
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isCount
     * @return
     */
    private List getSqlByPage(String componentCode, String procedureDueDateFrom, String procedureDueDateTo, int finalProcedureOnly,
            String directionNumber, String directionId, String completeFlg, String sidx, String sord, int pageNumber,
            int pageSize, boolean isCount) {

        StringBuilder sql;
        
        if (isCount) {
            sql = new StringBuilder("SELECT COUNT(m) FROM TblProductionPlan m WHERE 1=1 ");
        } else {
            sql = new StringBuilder(
                    " SELECT m FROM TblProductionPlan m LEFT JOIN FETCH m.mstComponent mc LEFT JOIN FETCH m.tblDirection td LEFT JOIN FETCH  m.mstProcedure mp WHERE 1=1 ");
        }

        // 日付項目をDate型(yyyy-MM-dd)に変換
        SimpleDateFormat sdf = new SimpleDateFormat(com.kmcj.karte.util.DateFormat.DATE_FORMAT);
        java.util.Date formatProcedureDueDateFrom = null;
        java.util.Date formatProcedureDueDateTo = null;
        try {
            if (procedureDueDateFrom != null && !"".equals(procedureDueDateFrom)) {
                formatProcedureDueDateFrom = sdf.parse(procedureDueDateFrom);
            }
            if (procedureDueDateTo != null && !"".equals(procedureDueDateTo)) {
                formatProcedureDueDateTo = sdf.parse(procedureDueDateTo);
            }
        } catch (ParseException ex) {
            Logger.getLogger(TblWorkResource.class.getName()).log(Level.WARNING, null,
                    "日付形式不正 workingDateFrom[" + procedureDueDateFrom + "], workingDateTo[" + procedureDueDateTo + "]");
        }

        if (componentCode != null && !"".equals(componentCode)) {
            sql = sql.append(" and m.mstComponent.componentCode LIKE :componentCode ");
        }

        if (finalProcedureOnly == 1) {
            sql = sql.append(" and m.mstProcedure.finalFlg = 1 ");
        }

        if (formatProcedureDueDateFrom != null) {
            sql = sql.append(" and m.procedureDueDate >= :formatProcedureDueDateFrom ");
        }

        if (formatProcedureDueDateTo != null) {
            sql = sql.append(" and m.procedureDueDate <= :formatProcedureDueDateTo ");
        }

        if (directionNumber != null && !"".equals(directionNumber)) {
            sql.append(" and m.tblDirection.directionCode LIKE :directionNumber ");
        }

        if (directionId != null && !"".equals(directionId)) {
            sql.append(" and m.directionId = :directionId ");
        }
        if (completeFlg != null && "1".equals(completeFlg)) {
            sql.append(" and m.uncompletedCount > :completeFlg ");
        }

        if (!isCount) {

            if (StringUtils.isNotEmpty(sidx)) {

                String sortStr = orderKey.get(sidx) + " " + sord;

                sql.append(sortStr);

            } else {

                // 工程納期の降順、部品コード、工番の昇順で表示する。
                sql = sql.append(" ORDER BY m.procedureDueDate DESC , mc.componentCode ASC ,mp.procedureCode ASC");

            }

        }

        Query query = entityManager.createQuery(sql.toString());

        if (componentCode != null && !"".equals(componentCode)) {
            query.setParameter("componentCode", "%" + componentCode.trim() + "%");
        }
        if (formatProcedureDueDateFrom != null) {
            query.setParameter("formatProcedureDueDateFrom", formatProcedureDueDateFrom);
        }
        if (formatProcedureDueDateTo != null) {
            query.setParameter("formatProcedureDueDateTo", formatProcedureDueDateTo);
        }

        if (directionNumber != null && !"".equals(directionNumber)) {
            query.setParameter("directionNumber", "%" + directionNumber.trim() + "%");
        }

        if (directionId != null && !"".equals(directionId)) {
            query.setParameter("directionId", directionId.trim());
        }

        if (completeFlg != null && "1".equals(completeFlg)) {
            query.setParameter("completeFlg", 0);
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

}
