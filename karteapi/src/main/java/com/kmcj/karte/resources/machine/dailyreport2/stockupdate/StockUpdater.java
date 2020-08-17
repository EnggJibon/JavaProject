/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.dailyreport2.stockupdate;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.component.lot.TblComponentLot;
import com.kmcj.karte.resources.component.lot.TblComponentLotService;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelation;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationService;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationVo;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationVoList;
import com.kmcj.karte.resources.component.structure.MstComponentStructure;
import com.kmcj.karte.resources.component.structure.MstComponentStructureVoList;
import com.kmcj.karte.resources.material.MstMaterial;
import com.kmcj.karte.resources.material.MstMaterialService;
import com.kmcj.karte.resources.material.stock.TblMaterialStockService;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import com.kmcj.karte.resources.stock.TblStockService;
import com.kmcj.karte.util.DateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import com.kmcj.karte.resources.production2.Production;
import com.kmcj.karte.util.IDGenerator;
import java.math.BigDecimal;
import java.util.Date;
import javax.enterprise.context.Dependent;
import javax.persistence.Query;

/**
 *
 * @author f.kitaoji
 */
@Dependent
public class StockUpdater {
    private final List<ComponentStock> componentStockList = new ArrayList<>();
    private final List<MaterialStock> materialStockList = new ArrayList<>();
    private LoginUser loginUser;

    @Inject
    private MstComponentService mstComponentService;
    @Inject
    private TblStockService tblStockService;
    @Inject
    private TblMaterialStockService tblMaterialStockService;
    @Inject
    private TblComponentLotService tblComponentLotService;
    @Inject
    private TblComponentLotRelationService tblComponentLotRelationService;
    @Inject
    private MstProcedureService mstProcedureService;
    @Inject
    private MstMaterialService mstMaterialService;
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    /**
     * 部品在庫の追加。メモリに蓄積
     * @param productionId
     * @param productionDetailId
     * @param componentId
     * @param procedureId
     * @param quantity
     * @param lotRelations 
     */
    public void addComponentStock(String productionId, String productionDetailId, String componentId, String procedureId, int quantity, TblComponentLotRelationVoList lotRelations) {
        ComponentStock stock = new ComponentStock();
        stock.setComponentId(componentId);
        stock.setProductionId(productionId);
        stock.setProductionDetailId(productionDetailId);
        stock.setProcedureId(procedureId);
        stock.setQuantity(quantity);
        stock.setTblComponentLotRelationVoList(lotRelations);
        this.componentStockList.add(stock);
    }
    
    /**
     * 材料在庫の追加。メモリに蓄積
     * @param materialId
     * @param productionId
     * @param productionDetailId
     * @param materialLotNo
     * @param quantity 
     */
    public void addMaterialStock(String materialId, String productionId, String productionDetailId, String materialLotNo, BigDecimal quantity) {
        MaterialStock materialStock = new MaterialStock();
        materialStock.setMaterialId(materialId);
        materialStock.setProductionId(productionId);
        materialStock.setProductionDetailId(productionDetailId);
        materialStock.setMaterialLotNo(materialLotNo);
        materialStock.setQuantity(quantity);
        materialStockList.add(materialStock);
    }
    
    /**
     * 生産実績IDよりロット番号を取得
     * @param productionId
     * @return 
     */
    private String getLotNumber(String productionId) {
        Query query = entityManager.createQuery(" SELECT t FROM Production t WHERE t.id = :id ");
        query.setParameter("id", productionId);
        List<Production> list = query.getResultList();
        if (list != null && !list.isEmpty()) {
            return ((Production)list.get(0)).getLotNumber();
        }
        else {
            return null;
        }
    }
    
    /**
     * 部品IDから部品コード取得
     * @param componetId
     * @return 
     */
    private String getComponentCode(String componetId) {
        MstComponent component = mstComponentService.getMstComponentById(componetId);
        return component == null ? null : component.getComponentCode();
    }
    
    /**
     * 在庫DB更新処理
     * @throws Exception 
     */
    @Transactional
    public void updateStock() throws Exception {
        //部品在庫更新
        updateComponentStock();
        //材料在庫更新
        updateMaterialStock();
    }

    /**
     * 削除された日報に対する在庫の更新
     * @throws Exception 
     */
    @Transactional
    public void updateDeletedStock() throws Exception {
        //部品在庫更新
        updateDeletedComponentStock();
        
    }
    
    @Transactional
    private void updateDeletedComponentStock() throws Exception {
        for (ComponentStock stock : componentStockList) {
            MstProcedure procedure = mstProcedureService.getMstProcedureById(stock.getProcedureId());
            if (procedure == null) continue;
            MstProcedure prevMstProcedure = mstProcedureService.getPrevProcedureCode(stock.getComponentId(), procedure.getProcedureCode());
            // 部品ロット関連テーブルから構成部品リスト取得
            TblComponentLotRelationVoList tblComponentLotRelationVoList = tblComponentLotRelationService.getTblComponentLotRelationList(null, stock.getProductionDetailId());
            BasicResponse basicResponse = tblStockService.doTblStock(
                getComponentCode(stock.getComponentId()),
                procedure,
                prevMstProcedure,
                CommonConstants.DELIVERY_DISCARD,
                stock.getQuantity(),
                DateFormat.getCurrentDateTime(),
                null,
                0,
                null,
                CommonConstants.SHIPMENT_NO,
                tblComponentLotRelationVoList,
                loginUser.getUserUuid(),
                loginUser.getLangId()
            );
            if (basicResponse.isError()) {
                throw new Exception(basicResponse.getErrorMessage());
            }
            
        }
    }
    
        
    
    /**
     * 材料在庫DB更新
     * @throws Exception 
     */
    @Transactional
    private void updateMaterialStock() throws Exception {
        for (MaterialStock materialStock: materialStockList) {
            MstMaterial material = mstMaterialService.getMstMaterialById(materialStock.getMaterialId());
            if (material == null) continue;
            BasicResponse basicResponse = tblMaterialStockService.doMaterialStock(
                material.getMaterialCode(),
                materialStock.getMaterialLotNo(),
                CommonConstants.DELIVERY,
                materialStock.getQuantity(),
                DateFormat.getCurrentDateTime(),
                materialStock.getProductionDetailId(),
                0,
                null,
                loginUser.getUserUuid(),
                loginUser.getLangId(),
                false
            );
            if (basicResponse.isError()) {
                throw new Exception(basicResponse.getErrorMessage());
            }
        }
    }
    
    /**
     * 部品在庫DB更新
     * @throws Exception 
     */
    @Transactional
    private void updateComponentStock() throws Exception {
        for (ComponentStock stock : componentStockList) {
            MstProcedure procedure = mstProcedureService.getMstProcedureById(stock.getProcedureId());
            if (procedure == null) {
                continue;
            }
            //最終工程のみ子部品在庫更新行う
            if (procedure.getFinalFlg() == 1) {
                // 生産実績明細IDから部品ロット関連情報取得する
                TblComponentLotRelationVoList tblComponentLotRelationVoList = tblComponentLotRelationService.getTblComponentLotRelationList(stock.getProductionDetailId(), null);
                if (tblComponentLotRelationVoList.getTblComponentLotRelationVos() == null || tblComponentLotRelationVoList.getTblComponentLotRelationVos().isEmpty()) {
                    // 下位階層部品リスト取得
                    List<MstComponentStructure> list = tblStockService.getStructureListByParentComponentId(stock.getComponentId());
                    if (list != null && !list.isEmpty()) {
                        List<TblComponentLotRelationVo> relationVos = new ArrayList<>();
                        for (MstComponentStructure structure : list) {
                            TblComponentLotRelationVo relationVo = new TblComponentLotRelationVo();
                            relationVo.setComponentId(structure.getParentComponentId());
                            relationVo.setSubComponentId(structure.getComponentId());
                            relationVo.setSubComponentLotNo("");
                            relationVos.add(relationVo);
                        }
                        tblComponentLotRelationVoList.setTblComponentLotRelationVos(relationVos);
                    }
                }
                stock.setTblComponentLotRelationVoList(tblComponentLotRelationVoList);
            }
            stock.setProcedureCode(procedure.getProcedureCode());
            MstProcedure prevMstProcedure = mstProcedureService.getPrevProcedureCode(stock.getComponentId(), procedure.getProcedureCode());
            //部品在庫テーブル更新(tbl_stock, tbl_stock_detail, tbl_component_lot)
            BasicResponse basicResponse = tblStockService.doTblStock(
                getComponentCode(stock.getComponentId()),
                procedure,
                prevMstProcedure,
                CommonConstants.STORE,
                stock.getQuantity(),
                DateFormat.getCurrentDateTime(),
                getLotNumber(stock.getProductionId()),
                0,
                null,
                CommonConstants.SHIPMENT_NO,
                stock.getTblComponentLotRelationVoList(),
                loginUser.getUserUuid(),
                loginUser.getLangId()
            );
            if (basicResponse.isError()) {
                throw new Exception(basicResponse.getErrorMessage());
            }
            //部品ロット関連テーブル更新
            makeComponentLotRelation(stock, stock.getTblComponentLotRelationVoList());
        }
    }

   /**
     * 部品ロット関連情報更新
     * @param stock
     * @param lotRelations 
     */
    @Transactional
    private void makeComponentLotRelation(ComponentStock stock, TblComponentLotRelationVoList lotRelations) {
        if (lotRelations == null) return;
        if (lotRelations.getTblComponentLotRelationVos() == null) return;
        //すでに同じ生産明細IDで部品ロット関連情報が登録されていたら登録しない
        if (tblComponentLotRelationService.getComponentLotRelationByProductionDetailId(stock.getProductionDetailId()) != null) return;
        //機械日報2登録時にロット選択ダイアログで選択された子部品のロットのリストを登録する
        for (TblComponentLotRelationVo componentLotRelation : lotRelations.getTblComponentLotRelationVos()) {
            //今回登録する部品の部品ロットテーブルを取得
//            TblComponentLot componentLot = tblComponentLotService.getComponentLotByProductionDetailId(stock.getProductionDetailId());
            //部品ロット関連情報の新オブジェクト作成
            TblComponentLotRelation newComponentLotRelation = new TblComponentLotRelation();
            //今回登録する部品ID(親部品)
            newComponentLotRelation.setComponentId(stock.getComponentId());
            //今回登録する部品の部品ロットID
//            newComponentLotRelation.setComponentLotId(componentLot.getUuid());
            newComponentLotRelation.setComponentLotId(null);
            //今回登録する部品工程番号
            newComponentLotRelation.setProcedureCode(stock.getProcedureCode());
            //子部品の部品ID
            newComponentLotRelation.setSubComponentId(componentLotRelation.getSubComponentId());
            //子部品の部品ロットID 画面で選択されないこともあるので空白ならNULLをセット
            if (componentLotRelation.getSubComponentLotId() != null && componentLotRelation.getSubComponentLotId().equals("")) {
                componentLotRelation.setSubComponentId(null);
            }
            newComponentLotRelation.setSubComponentLotId(componentLotRelation.getSubComponentLotId());
            //子部品の最終部品工程番号
            MstProcedure subFinalProcedure = mstProcedureService.getFinalProcedureByComponentId(componentLotRelation.getSubComponentId());
            if (subFinalProcedure == null) continue; //見つからなかったら部品ロット関連情報は登録しない
            newComponentLotRelation.setSubProcedureCode(subFinalProcedure.getProcedureCode());
            //生産明細ID
            newComponentLotRelation.setProductionDetailId(stock.getProductionDetailId());
            //UUID
            newComponentLotRelation.setUuid(IDGenerator.generate());
            newComponentLotRelation.setCreateDate(new Date());
            newComponentLotRelation.setCreateUserUuid(loginUser.getUserUuid());
            newComponentLotRelation.setUpdateDate(newComponentLotRelation.getCreateDate());
            newComponentLotRelation.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.persist(newComponentLotRelation);
        }
        
    }
    
    /**
     * 部品構成マスタ取得
     * @param componentId
     * @param procedureId
     * @return 
     */
    public MstComponentStructureVoList getComponentStructures(String componentId, String procedureId) {
        MstComponentStructureVoList response = new MstComponentStructureVoList();
        //最終工程のみ在庫更新を行う
        MstProcedure procedure = mstProcedureService.getMstProcedureById(procedureId);
        if (procedure == null || procedure.getFinalFlg() == 0) {
            return response;
        }
        List<MstComponentStructure> list = tblStockService.getStructureListByParentComponentId(componentId);
        if (list != null && !list.isEmpty()) {
            for (MstComponentStructure cs : list) {
                // 下位階層部品のロット番号取得
                List<TblComponentLot> componentLots = entityManager.createNamedQuery(
                    "TblComponentLot.findByComponentIdForStructure").setParameter("componentId", cs.getComponentId()).getResultList();
                cs.setTblComponentLotList(componentLots);
            }
            response.setMstComponentStructures(list);
        }
        return response;
    }

    /**
     * @return the loginUser
     */
    public LoginUser getLoginUser() {
        return loginUser;
    }

    /**
     * @param loginUser the loginUser to set
     */
    public void setLoginUser(LoginUser loginUser) {
        this.loginUser = loginUser;
    }
    
}
