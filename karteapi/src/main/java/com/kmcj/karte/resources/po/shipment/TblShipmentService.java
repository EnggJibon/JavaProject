/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.po.shipment;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.resources.po.*;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionResultService;
import com.kmcj.karte.resources.component.inspection.result.model.TblComponentInspectionResultPoList;
import com.kmcj.karte.resources.component.inspection.result.model.TblComponentInspectionResultPoVo;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import com.kmcj.karte.resources.production.TblProduction;
import com.kmcj.karte.resources.production.detail.TblProductionDetail;
import com.kmcj.karte.resources.stock.TblStock;
import com.kmcj.karte.resources.stock.TblStockService;
import com.kmcj.karte.resources.stock.TblStockVo;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
 * 出荷登録
 *
 * @author admin
 */
@Dependent
public class TblShipmentService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private TblPoService tblPoService;
    
    @Inject
    private TblStockService tblStockService;

    @Inject
    private MstProcedureService mstProcedureService;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private TblComponentInspectionResultService tblComponentInspectionResultService;
    
    @Inject
    private CnfSystemService cnfSysService;

    /**
     * 出荷登録
     *
     * @param tblShipmentVo
     * @param userUuid
     * @param langId
     * @return
     */
    @Transactional
    public BasicResponse save(TblShipmentVo tblShipmentVo, String userUuid, String langId) {
        BasicResponse basicResponse = new BasicResponse();
        //        ＜エラーチェック＞
        //※出荷数量は受注数量を超える値、生産実績明細の完成数を超える値は指定できない。
        if (tblShipmentVo.getShipQuantity() > tblShipmentVo.getTblPoVo().getQuantity()
                || (StringUtils.isNotEmpty(tblShipmentVo.getProductionDetailId()) && tblShipmentVo.getShipQuantity() > getCompleteQuantityByDetailId(tblShipmentVo.getProductionDetailId()))) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(langId, "msg_error_ship_quantity");
            basicResponse.setErrorMessage(msg);
            return basicResponse;
        }
        
        CnfSystem alertMode = cnfSysService.findByKey("system", "shipment_alert_on_inspection");
        
        if(!isShipable(tblShipmentVo.getComponentId(), tblShipmentVo.getProductionLotNumber(), alertMode)) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(langId, alertMode.getConfigValue().equals("0") ? "msg_inspection_denied" : "msg_not_inspect_passed");
            basicResponse.setErrorMessage(msg);
            return basicResponse;
        }

        // POテーブル
        basicResponse = tblPoService.saveTblPo(tblShipmentVo.getTblPoVo(), userUuid, langId);
        if (basicResponse.isError()) {
            return basicResponse;
        }
        entityManager.flush();
        entityManager.clear();
        insertShip(tblShipmentVo, userUuid);

        // 出荷登録画面で登録する際に在庫管理APIを呼び出して在庫数の減算を行う
        //	部品コード　：　画面で入力した製造ロット側の部品コード
        //	工程番号　：　部品コードの中で最大の工程番号
        //	分類　：　出庫（DELIVERY）
        //	数量　：　画面に入力した出荷数量
        //	在庫変更日　：　システム日付
        //	製造ロット番号　：　画面で入力した製造ロット番号
        MstProcedure mstProcedure = mstProcedureService.getFinalProcedureByComponentId(tblShipmentVo.getComponentId());
        if (mstProcedure != null) {
            tblStockService.doTblStock(mstProcedure.getMstComponent().getComponentCode(), mstProcedure, null,
                    CommonConstants.DELIVERY, tblShipmentVo.getShipQuantity(), DateFormat.getCurrentDateTime(), tblShipmentVo.getProductionLotNumber(), 0, null, CommonConstants.SHIPMENT_YES, null, userUuid, langId);
        }

        return basicResponse;
    }

    /**
     *
     * @param tblShipmentVo
     * @param userUuid
     */
    @Transactional
    public void insertShip(TblShipmentVo tblShipmentVo, String userUuid) {

        // 出荷テーブル
        TblShipment tblShipment = new TblShipment();
        tblShipment.setUuid(IDGenerator.generate());
        tblShipment.setPoId(tblShipmentVo.getTblPoVo().getUuid());
        FileUtil fileUtil = new FileUtil();
        if (StringUtils.isNotEmpty(tblShipmentVo.getShipDate())) {
            tblShipment.setShipDate(fileUtil.getDateParseForDate(tblShipmentVo.getShipDate()));
        } else {
            tblShipment.setProductionDetailId(null);
        }
        tblShipment.setQuantity(tblShipmentVo.getShipQuantity());
        // 生産実績にあるものとは限らない
        tblShipment.setProductionLotNumber(tblShipmentVo.getProductionLotNumber());
        // 生産実績とひもづかない場合はNULL
        if (StringUtils.isNotEmpty(tblShipmentVo.getProductionDetailId())) {
            tblShipment.setProductionDetailId(tblShipmentVo.getProductionDetailId());
        } else {
            tblShipment.setProductionDetailId(null);
        }
        if (StringUtils.isNotEmpty(tblShipmentVo.getComponentId())) {
            tblShipment.setComponentId(tblShipmentVo.getComponentId());
        } else {
            tblShipment.setComponentId(null);
        }
        tblShipment.setCreateDate(new Date());
        tblShipment.setUpdateDate(new Date());
        tblShipment.setCreateUserUuid(userUuid);
        tblShipment.setUpdateUserUuid(userUuid);
        tblShipment.setBatchUpdateStatus(CommonConstants.PO_BATCH_UPDATE_STATUS_UNDONE);//20171205 Apeng ADD
        entityManager.persist(tblShipment);

    }

    /**
     *
     * @param poUuid
     * @param formatShipDateFrom
     * @param formatShipDateTo
     * @return
     */
    public TblShipmentVoList getTblShipmentList(String poUuid, Date formatShipDateFrom, Date formatShipDateTo) {
        TblShipmentVoList tblShipmentVoList = new TblShipmentVoList();

        StringBuilder sql = new StringBuilder("SELECT ship FROM TblShipment ship LEFT JOIN FETCH ship.tblProductionDetail prod ");
        sql.append(" LEFT JOIN FETCH ship.mstComponent component  WHERE ship.poId = :poUuid ");

        if (formatShipDateFrom != null) {
            sql.append(" AND  ship.shipDate >= :formatShipDateFrom ");
        }

        if (formatShipDateTo != null) {
            sql.append(" AND  ship.shipDate <= :formatShipDateTo ");
        }

        sql.append(" ORDER BY ship.shipDate ");

        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("poUuid", poUuid);

        if (formatShipDateFrom != null) {
            query.setParameter("formatShipDateFrom", formatShipDateFrom);
        }

        if (formatShipDateTo != null) {
            query.setParameter("formatShipDateTo", formatShipDateTo);
        }

        List list = query.getResultList();
        TblShipmentVo tblShipmentVo;
        List<TblShipmentVo> tblShipmentVos = new ArrayList();
        FileUtil fileUtil = new FileUtil();
        for (int i = 0; i < list.size(); i++) {
            tblShipmentVo = new TblShipmentVo();
            TblShipment tblShipment = (TblShipment) list.get(i);

            tblShipmentVo.setUuid(tblShipment.getUuid());
            tblShipmentVo.setShipDate(fileUtil.getDateFormatForStr(tblShipment.getShipDate()));
            tblShipmentVo.setProductionLotNumber(FileUtil.getStringValue(tblShipment.getProductionLotNumber()));
            if (StringUtils.isNotEmpty(tblShipment.getProductionDetailId())) {
                // 1:show link
                tblShipmentVo.setLinkFlag(1);
            }

            MstComponent mstComponent = tblShipment.getMstComponent();
            if (mstComponent != null) {
                tblShipmentVo.setComponentCode(mstComponent.getComponentCode());
                TblStockVo tblStockVo = tblStockService.getTblStockQuantity(mstComponent.getId());
                tblShipmentVo.setStockQuantity(tblStockVo.getStockQuantity());
            } else {
                tblShipmentVo.setComponentCode("");
                tblShipmentVo.setStockQuantity(0);
            }
            tblShipmentVo.setShipQuantity(tblShipment.getQuantity());

            TblProductionDetail tblProductionDetail = tblShipment.getTblProductionDetail();
            if (tblProductionDetail != null) {
                tblShipmentVo.setProductionDetailId(tblProductionDetail.getId());
                tblShipmentVo.setCompleteQuantity(tblProductionDetail.getCompleteCount());// 完成数量
                TblProduction tblProduction = tblProductionDetail.getTblProduction();
                tblShipmentVo.setProductionId(tblProductionDetail.getTblProduction().getId());

                MstMachine mstMachine = tblProduction.getMstMachine();
                tblShipmentVo.setMachineName(mstMachine != null ? mstMachine.getMachineName() : "");

                MstMold mstMold = tblProduction.getMstMold();
                tblShipmentVo.setMoldId(mstMold != null ? mstMold.getMoldId() : "");

                tblShipmentVo.setProductionStartDatetime(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(tblProduction.getStartDatetime()));
                tblShipmentVo.setProductionEndDatetime(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(tblProduction.getEndDatetime()));

            } else {
                tblShipmentVo.setProductionId("");
                tblShipmentVo.setProductionDetailId("");
                tblShipmentVo.setMachineName("");
                tblShipmentVo.setMoldId("");
                tblShipmentVo.setProductionStartDatetime("");
                tblShipmentVo.setProductionEndDatetime("");
                tblShipmentVo.setCompleteQuantity(0);
            }

            tblShipmentVos.add(tblShipmentVo);
        }

        tblShipmentVoList.setTblShipmentVos(tblShipmentVos);
        return tblShipmentVoList;
    }

    /**
     *
     * @param uuid
     * @return
     */
    @Transactional
    public BasicResponse deleteTblShipment(String uuid) {
        // TblShipmentテーブル削除
        Query query = entityManager.createNamedQuery("TblShipment.delete");
        query.setParameter("uuid", uuid);
        query.executeUpdate();

        return new BasicResponse();
    }

    /**
     *
     * @param productionLotNumber
     * @param isLike
     * @return
     */
    public TblShipmentVoList getList(String productionLotNumber, boolean isLike) {

        StringBuilder sql = new StringBuilder("SELECT production FROM TblProduction production WHERE 1=1 ");

        if (StringUtils.isNotEmpty(productionLotNumber)) {
            if (isLike) {
                sql = sql.append(" and production.lotNumber LIKE :productionLotNumber ");
            } else {
                sql = sql.append(" and production.lotNumber = :productionLotNumber ");
            }
        }
        
        sql.append(" Order by production.startDatetime ");
        
        Query query = entityManager.createQuery(sql.toString());

        if (StringUtils.isNotEmpty(productionLotNumber)) {
            if (isLike) {
                query.setParameter("productionLotNumber", "%" + productionLotNumber + "%");
            } else {
                query.setParameter("productionLotNumber", productionLotNumber);
            }
        }
        query.setMaxResults(100);
        List list = query.getResultList();
        TblShipmentVo tblShipmentVo;
        List<TblShipmentVo> tblShipmentVos = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {

            TblProduction tblProduction = (TblProduction) list.get(i);

            tblShipmentVo = new TblShipmentVo();

            tblShipmentVo.setProductionId(tblProduction.getId());
            tblShipmentVo.setProductionLotNumber(tblProduction.getLotNumber());
            tblShipmentVos.add(tblShipmentVo);
        }

        TblShipmentVoList tblShipmentVoList = new TblShipmentVoList();
        tblShipmentVoList.setTblShipmentVos(tblShipmentVos);
        return tblShipmentVoList;

    }

    /**
     *
     * @param productionId
     * @return
     */
    public TblShipmentVoList getList(String productionId) {

        StringBuilder sql = new StringBuilder("SELECT detail FROM TblProductionDetail detail "
                + " JOIN FETCH detail.tblProduction m "
                + " LEFT JOIN FETCH detail.mstComponent component"
                + " WHERE 1=1 ");

        if (StringUtils.isNotEmpty(productionId)) {
            sql = sql.append(" AND m.id = :productionId ");
        }
        sql = sql.append(" ORDER BY component.componentCode ");

        Query query = entityManager.createQuery(sql.toString());

        if (StringUtils.isNotEmpty(productionId)) {
            query.setParameter("productionId", productionId);
        }

        List list = query.getResultList();
        TblShipmentVo tblShipmentVo;
        List<TblShipmentVo> tblShipmentVos = new ArrayList<>();
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < list.size(); i++) {

            TblProductionDetail tblProductionDetail = (TblProductionDetail) list.get(i);
            MstComponent mstComponent = tblProductionDetail.getMstComponent();

            tblShipmentVo = new TblShipmentVo();
            if (mstComponent != null && !map.containsKey(mstComponent.getId())) {
                tblShipmentVo.setProductionDetailId(tblProductionDetail.getId());
                tblShipmentVo.setCompleteQuantity(tblProductionDetail.getCompleteCount());
                tblShipmentVo.setComponentId(mstComponent.getId());
                tblShipmentVo.setComponentCode(mstComponent.getComponentCode());
                tblShipmentVo.setComponentName(mstComponent.getComponentName());
                tblShipmentVos.add(tblShipmentVo);
            }
        }
        TblShipmentVoList tblShipmentVoList = new TblShipmentVoList();
        tblShipmentVoList.setTblShipmentVos(tblShipmentVos);
        return tblShipmentVoList;

    }

    /**
     * 生産実績明細の完成数を取得
     *
     * @param productionDetailId
     * @return
     */
    private int getCompleteQuantityByDetailId(String productionDetailId) {
        int completeQuantity = 0;
        if (StringUtils.isNotEmpty(productionDetailId)) {
            TblProductionDetail tblProductionDetail = entityManager.find(TblProductionDetail.class, productionDetailId);
            if (tblProductionDetail != null) {
                completeQuantity = tblProductionDetail.getCompleteCount();
            }
        }
        return completeQuantity;
    }
    
    /**
     * 
     * 
     * @param productionLotNumber
     * @param componentId
     * @param companyId
     * @param componentInspectionResultId
     * @return 
     */
    public TblShipmentVoList getTblShipmentByComponentIdLike(String productionLotNumber, String componentId,String companyId,String componentInspectionResultId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT tblProduction.lotNumber FROM TblProductionDetail detail ");
        sql.append(" JOIN FETCH detail.productionId tblProduction ");
        sql.append(" WHERE detail.componentId = :componentId ");
        sql.append(" AND tblProduction.lotNumber LIKE :productionLotNumber order by tblProduction.lotNumber desc");


        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("componentId", componentId);
        query.setParameter("productionLotNumber", "%" + productionLotNumber + "%");
        
        List<String> list = query.getResultList();
        TblShipmentVo tblShipmentVo;
        String newProductionLotNumber;
        List<TblShipmentVo> tblShipmentVos = new ArrayList<>();
        
        for (int i = 0; i < list.size(); i++) {

            newProductionLotNumber = String.valueOf(list.get(i));
            tblShipmentVo = new TblShipmentVo();

            TblComponentInspectionResultPoList poList = tblComponentInspectionResultService.getTblPoOutbound(componentId, productionLotNumber, null,companyId);
            String poNumber = "";
            if (poList != null && poList.getTblComponentInspectionResultPoVos() != null && poList.getTblComponentInspectionResultPoVos().size() > 0) {
                for (TblComponentInspectionResultPoVo poVo : poList.getTblComponentInspectionResultPoVos()) {
                    poNumber += poVo.getOrderNumber().concat("-").concat(poVo.getItemNumber()) + "\n";
                }
            }
            tblShipmentVo.setPoNumber(poNumber);
            
            tblShipmentVo.setProductionLotNumber(newProductionLotNumber);
            tblShipmentVos.add(tblShipmentVo);
        }

        TblShipmentVoList tblShipmentVoList = new TblShipmentVoList();
        tblShipmentVoList.setTblShipmentVos(tblShipmentVos);
        return tblShipmentVoList;

    }
    
    /**
     * 
     * @param productionLotNumber
     * @param componentId
     * @param companyId
     * @param componentInspectionResultId
     * @return 
     */
    public TblShipmentVoList getTblShipmentByComponentIdEqual(String productionLotNumber, String componentId,String companyId,String componentInspectionResultId) {
        
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT tblProduction.lotNumber FROM TblProductionDetail detail ");
        sql.append(" JOIN FETCH detail.productionId tblProduction ");
        sql.append(" WHERE detail.componentId = :componentId ");
        sql.append(" AND tblProduction.lotNumber = :productionLotNumber ");

        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("componentId", componentId);
        query.setParameter("productionLotNumber", productionLotNumber);

        List list = query.getResultList();
        TblShipmentVo tblShipmentVo;
        String newProductionLotNumber;
        List<TblShipmentVo> tblShipmentVos = new ArrayList<>();
        
        for (int i = 0; i < list.size(); i++) {

            newProductionLotNumber = String.valueOf(list.get(i));
            tblShipmentVo = new TblShipmentVo();

            TblComponentInspectionResultPoList poList = tblComponentInspectionResultService.getTblPoOutbound(componentId, productionLotNumber, null, companyId);
            String poNumber = "";
            if (poList != null && poList.getTblComponentInspectionResultPoVos() != null && poList.getTblComponentInspectionResultPoVos().size() > 0) {
                for (TblComponentInspectionResultPoVo poVo : poList.getTblComponentInspectionResultPoVos()) {
                    poNumber += poVo.getOrderNumber().concat("-").concat(poVo.getItemNumber()) + "\n";
                }
            }
            tblShipmentVo.setPoNumber(poNumber);

            tblShipmentVo.setProductionLotNumber(newProductionLotNumber);
            tblShipmentVos.add(tblShipmentVo);
        }

        TblShipmentVoList tblShipmentVoList = new TblShipmentVoList();
        tblShipmentVoList.setTblShipmentVos(tblShipmentVos);
        return tblShipmentVoList;

    }
    
    /**
     * 製造ロット番号 オートコンプリート用 スマホ専用
     *
     * @param productionLotNumber
     * @param shipmentComponentId
     * @param isLike
     * @return
     */
    public TblShipmentVoList getListForSp(String productionLotNumber, String shipmentComponentId, boolean isLike) {

        StringBuilder sql = new StringBuilder("SELECT Distinct tp FROM TblProductionDetail tpd JOIN FETCH tpd.tblProduction tp JOIN FETCH tpd.mstComponent mc WHERE 1=1 ");

        if (StringUtils.isNotEmpty(productionLotNumber)) {
            if (isLike) {
                sql = sql.append(" and tp.lotNumber LIKE :productionLotNumber ");
            } else {
                sql = sql.append(" and tp.lotNumber = :productionLotNumber ");
            }
        }
        if (StringUtils.isNotEmpty(shipmentComponentId)) {
            sql = sql.append(" and mc.id = :shipmentComponentId ");
        }

        sql.append(" Order by tp.startDatetime ");

        Query query = entityManager.createQuery(sql.toString());

        if (StringUtils.isNotEmpty(productionLotNumber)) {
            if (isLike) {
                query.setParameter("productionLotNumber", "%" + productionLotNumber + "%");
            } else {
                query.setParameter("productionLotNumber", productionLotNumber);
            }
        }
        if (StringUtils.isNotEmpty(shipmentComponentId)) {
            query.setParameter("shipmentComponentId", shipmentComponentId);
        }
        query.setMaxResults(100);
        List list = query.getResultList();
        TblShipmentVo tblShipmentVo;
        List<TblShipmentVo> tblShipmentVos = new ArrayList<>();
        
        CnfSystem alertMode = cnfSysService.findByKey("system", "shipment_alert_on_inspection");

        for (int i = 0; i < list.size(); i++) {

            TblProduction tblProduction = (TblProduction) list.get(i);

            tblShipmentVo = new TblShipmentVo();
            tblShipmentVo.setProductionId(tblProduction.getId());
            tblShipmentVo.setProductionLotNumber(tblProduction.getLotNumber());
            if(isShipable(shipmentComponentId, tblProduction.getLotNumber(), alertMode)) {
                tblShipmentVos.add(tblShipmentVo);
            }
        }

        TblShipmentVoList tblShipmentVoList = new TblShipmentVoList();
        tblShipmentVoList.setTblShipmentVos(tblShipmentVos);
        return tblShipmentVoList;
    }
    
    private boolean isShipable(String componentID, String lotNum, CnfSystem alertMode) {
        if("0".equals(alertMode.getConfigValue())) {
            if(!tblComponentInspectionResultService.isInspectFailure(componentID, lotNum)) {
                return true;
            }
        } else {
            if(tblComponentInspectionResultService.isInspectPassed(componentID, lotNum)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 出荷登録部品在庫数量取得
     *
     * @param componentId
     * @return
     */
    public TblStock getSpStockQuality(String componentId) {
        TblStock response = new TblStock();
        // 部品コードの最終フラグが1の工程番号取得
        MstProcedure mstProcedure = mstProcedureService.getFinalProcedureByComponentId(componentId);
        if (mstProcedure != null) {            
            Query query = entityManager.createNamedQuery("TblStock.findByComponentAndProcedure");
            query.setParameter("componentId", componentId);
            query.setParameter("procedureCode", mstProcedure.getProcedureCode());
            
            List<TblStock> list = query.getResultList();
            if (list != null && !list.isEmpty()) {
                response = list.get(0);
            }
        }
        
        return response;
    }
}
