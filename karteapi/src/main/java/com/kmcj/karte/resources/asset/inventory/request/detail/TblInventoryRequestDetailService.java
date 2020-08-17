/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.request.detail;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.asset.inventory.request.TblInventoryRequest;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.asset.inventory.request.detail.id.TblInventoryRequestDetailId;
import com.kmcj.karte.resources.asset.inventory.request.detail.id.TblInventoryRequestDetailIdVo;
import com.kmcj.karte.resources.asset.inventory.request.detail.id.TblInventoryRequestDetailIdVoList;
import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.util.BeanCopyUtil;
import java.util.ArrayList;
import java.util.Date;
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
public class TblInventoryRequestDetailService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstChoiceService mstChoiceService;

    /**
     * T0025_資産棚卸依頼明細画面初期化表示
     *
     * @param inventoryRequestId
     * @param loginUser
     * @return
     */
    public TblInventoryRequestDetailVoList getTblInventoryRequestDetailList(String inventoryRequestId, LoginUser loginUser) {

        TblInventoryRequestDetailVoList response = new TblInventoryRequestDetailVoList();
        List<TblInventoryRequestDetail> list = getTblInventoryRequestDetailSql(inventoryRequestId);

        List<TblInventoryRequestDetailVo> tblInventoryRequestDetailVoList = new ArrayList<>();

        if (null != list && list.size() > 0) {
            TblInventoryRequestDetailVo tblInventoryRequestDetailVo;
            Map<String, String> existenceChoiceMap = mstChoiceService.getChoiceMap(loginUser.getLangId(), new String[]{"tbl_asset_disposal_request.existence"});
            for (TblInventoryRequestDetail tblInventoryRequestDetail : list) {

                tblInventoryRequestDetailVo = new TblInventoryRequestDetailVo();
                BeanCopyUtil.copyFields(tblInventoryRequestDetail, tblInventoryRequestDetailVo);
                //現品有無取得
                if (existenceChoiceMap != null) {
                    tblInventoryRequestDetailVo.setExistenceStr(existenceChoiceMap.get("tbl_asset_disposal_request.existence" + tblInventoryRequestDetail.getExistence()));
                } else {
                    tblInventoryRequestDetailVo.setExistenceStr("");
                }

                //代表金型・設備を取得
                Iterator<TblInventoryRequestDetailId> detailIds = tblInventoryRequestDetail.getTblInventoryRequestDetailIds().iterator();
                int count = 0;
                while (detailIds.hasNext()) {
                    count++;
                    TblInventoryRequestDetailId tblInventoryRequestDetailId = detailIds.next();
                    if (tblInventoryRequestDetailId.getMainFlg() == 1) {
                        if (tblInventoryRequestDetailId.getMoldId() != null) {
                            tblInventoryRequestDetailVo.setMainMoldId(tblInventoryRequestDetailId.getMoldId());
                        } else {
                            tblInventoryRequestDetailVo.setMainMoldId("");
                        }
                        if (tblInventoryRequestDetailId.getMachineId() != null) {
                            tblInventoryRequestDetailVo.setMainMachineId(tblInventoryRequestDetailId.getMachineId());
                        } else {
                            tblInventoryRequestDetailVo.setMainMachineId("");
                        }
                    }
                }
                if (count > 1) {
                    tblInventoryRequestDetailVo.setLinkflag(1);
                } else {
                    tblInventoryRequestDetailVo.setLinkflag(2);
                }

                tblInventoryRequestDetailVoList.add(tblInventoryRequestDetailVo);
            }
        }
        response.setTblInventoryRequestDetailVos(tblInventoryRequestDetailVoList);
        return response;

    }

    /**
     *
     * @param tblInventoryRequestDetailVoList
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse saveTblInventoryRequestDetail(TblInventoryRequestDetailVoList tblInventoryRequestDetailVoList, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        String inventoryRequestId = null;
        if (tblInventoryRequestDetailVoList != null && tblInventoryRequestDetailVoList.getTblInventoryRequestDetailVos().size() > 0) {

            for (TblInventoryRequestDetailVo tblInventoryRequestDetailVo : tblInventoryRequestDetailVoList.getTblInventoryRequestDetailVos()) {
                TblInventoryRequestDetail tblInventoryRequestDetail = entityManager.find(TblInventoryRequestDetail.class, tblInventoryRequestDetailVo.getUuid());
                if (StringUtils.isEmpty(inventoryRequestId)) {
                    inventoryRequestId = tblInventoryRequestDetail.getInventoryRequestId();
                }
                if (tblInventoryRequestDetail != null) {
                    tblInventoryRequestDetail.setExistence(tblInventoryRequestDetailVo.getExistence());
                    tblInventoryRequestDetail.setNoExistenceReason(tblInventoryRequestDetailVo.getNoExistenceReason());
                    
                    // 所在地変更情報のカルテ間連携 LYD S
                    //・所在変更有無	CHANGE_LOCATION	INT(3)	0:未選択（ブランク）、1:有、2:無
                    tblInventoryRequestDetail.setChangeLocation(tblInventoryRequestDetailVo.getChangeLocation());
                    //・変更後所在地	NEW_LOCATION	VARCHAR(100)
                    tblInventoryRequestDetail.setNewLocation(tblInventoryRequestDetailVo.getNewLocation());
                    //・変更後所在地住所	NEW_LOCATION_ADDRESS	VARCHAR(100)
                    tblInventoryRequestDetail.setNewLocationAddress(tblInventoryRequestDetailVo.getNewLocationAddress());
                    // 所在地変更情報のカルテ間連携 LYD E
                    
                    tblInventoryRequestDetail.setUpdateDate(new java.util.Date());
                    tblInventoryRequestDetail.setUpdateUserUuid(loginUser.getUserUuid());
                    entityManager.persist(tblInventoryRequestDetail);
                }
            }

            updateTblInventoryRequestStatus(inventoryRequestId, loginUser.getUserUuid());
        }

        return basicResponse;
    }

    /**
     *
     * @param inventoryRequestId
     * @param mainFlg
     * @return
     */
    private List getTblInventoryRequestDetailSql(String inventoryRequestId) {

        StringBuilder sql = new StringBuilder("SELECT tblInventoryRequestDetail ");

        sql = sql.append(" FROM TblInventoryRequestDetail tblInventoryRequestDetail "
                + " WHERE tblInventoryRequestDetail.inventoryRequestId = :inventoryRequestId "
                + " ORDER BY tblInventoryRequestDetail.assetNo, tblInventoryRequestDetail.branchNo ");

        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("inventoryRequestId", inventoryRequestId);
        List list = query.getResultList();
        return list;
    }

    /**
     * ダイアログで関連の金型、設備のID、名称、棚卸結果を一覧で表示。
     *
     * @param requestDetailId
     * @param moldMachineType
     * @param langId
     * @return
     */
    public TblInventoryRequestDetailIdVoList getTblInventoryRequestDetailList(String requestDetailId, String moldMachineType, String langId) {

        String orderBy = "";

        StringBuilder sql = new StringBuilder("SELECT tblInventoryRequestDetailId FROM TblInventoryRequestDetailId tblInventoryRequestDetailId ");
        if (moldMachineType != null && moldMachineType.equals(CommonConstants.MOLD_MACHINE_TYPE_MOLD)) {
            sql.append(" LEFT JOIN FETCH tblInventoryRequestDetailId.mstMold mstMold "
                    + " LEFT JOIN FETCH mstMold.tblMoldInventory tblMoldInventory");
            orderBy = " ORDER BY tblInventoryRequestDetailId.moldId";
        } else if (moldMachineType != null && moldMachineType.equals(CommonConstants.MOLD_MACHINE_TYPE_MACHINE)) {
            sql.append(" LEFT JOIN FETCH tblInventoryRequestDetailId.mstMachine mstMachine "
                    + " LEFT JOIN FETCH mstMachine.latestTblMachineInventory ");
            orderBy = " ORDER BY tblInventoryRequestDetailId.machineId";
        }
        sql.append(" JOIN FETCH tblInventoryRequestDetailId.tblInventoryRequestDetail ");
        sql.append(" WHERE tblInventoryRequestDetailId.tblInventoryRequestDetail.uuid = :requestDetailId ");
        sql.append(orderBy);

        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("requestDetailId", requestDetailId);

        List list = query.getResultList();

        TblInventoryRequestDetailIdVoList response = new TblInventoryRequestDetailIdVoList();
        List<TblInventoryRequestDetailIdVo> tblInventoryRequestDetailIdVos = new ArrayList();
        if (list != null && list.size() > 0) {
            Map<String, String> map = null;
            TblInventoryRequestDetailIdVo tblInventoryRequestDetailIdVo;
            for (int i = 0; i < list.size(); i++) {
                TblInventoryRequestDetailId tblInventoryRequestDetailId = (TblInventoryRequestDetailId) list.get(i);
                tblInventoryRequestDetailIdVo = new TblInventoryRequestDetailIdVo();
                //代表金型・設備を取得
                BeanCopyUtil.copyFields(tblInventoryRequestDetailId, tblInventoryRequestDetailIdVo);
                if (tblInventoryRequestDetailId.getMoldId() != null && tblInventoryRequestDetailId.getMstMold() != null) {
                    tblInventoryRequestDetailIdVo.setMoldName(tblInventoryRequestDetailId.getMstMold().getMoldName());
                    if (tblInventoryRequestDetailId.getMstMold().getTblMoldInventory() != null) {
                        if (map == null) {
                            map = mstChoiceService.getChoiceMap(langId, new String[]{"tbl_mold_inventory.inventory_result"});
                        }
                        tblInventoryRequestDetailIdVo.setInventoryResultValue(map.get("tbl_mold_inventory.inventory_result" + tblInventoryRequestDetailId.getMstMold().getTblMoldInventory().getInventoryResult()));
                    }
                } else {
                    tblInventoryRequestDetailIdVo.setMoldName("");
                }
                if (tblInventoryRequestDetailId.getMachineId() != null && tblInventoryRequestDetailId.getMstMachine() != null) {
                    tblInventoryRequestDetailIdVo.setMachineName(tblInventoryRequestDetailId.getMstMachine().getMachineName());
                    if (tblInventoryRequestDetailId.getMstMachine().getLatestTblMachineInventory() != null) {
                        if (map == null) {
                            map = mstChoiceService.getChoiceMap(langId, new String[]{"tbl_machine_inventory.inventory_result"});
                        }
                        tblInventoryRequestDetailIdVo.setInventoryResultValue(map.get("tbl_machine_inventory.inventory_result" + tblInventoryRequestDetailId.getMstMachine().getLatestTblMachineInventory().getInventoryResult()));
                    }
                } else {
                    tblInventoryRequestDetailIdVo.setMachineName("");
                }
                tblInventoryRequestDetailIdVos.add(tblInventoryRequestDetailIdVo);
            }
        }
        response.setTblInventoryRequestDetailIdVo(tblInventoryRequestDetailIdVos);
        return response;

    }

    /**
     * 棚卸依頼明細テーブル存在チェックを行う
     *
     * @param moldId
     * @param machineId
     * @return
     */
    public List getTblInventoryRequestMoldMachineIdExist(String moldId, String machineId) {
        StringBuilder sql = new StringBuilder("SELECT tblInventoryRequestDetail FROM TblInventoryRequestDetail tblInventoryRequestDetail"
                + " LEFT JOIN FETCH tblInventoryRequestDetail.tblInventoryRequest"
                + " JOIN FETCH tblInventoryRequestDetail.tblInventoryRequestDetailIds detailsId"
                + " WHERE 1=1");
        if (StringUtils.isNotEmpty(moldId)) {
            sql.append(" AND detailsId.moldId = :moldId");
        }
        if (StringUtils.isNotEmpty(machineId)) {
            sql.append(" AND detailsId.machineId = :machineId");
        }
        sql.append(" ORDER BY tblInventoryRequestDetail.tblInventoryRequest.requestDate DESC,tblInventoryRequestDetail.tblInventoryRequest.createDate DESC ");
        Query query = entityManager.createQuery(sql.toString());
        if (StringUtils.isNotEmpty(moldId)) {
            query.setParameter("moldId", moldId.trim());
        }
        if (StringUtils.isNotEmpty(machineId)) {
            query.setParameter("machineId", machineId.trim());
        }
        return query.getResultList();
    }

    /**
     * 棚卸依頼明細テーブル\棚卸依頼明細IDテーブル現品有無更新
     *
     * @param list
     * @param inventoryResult
     * @param loginUserUuid
     * @param ischangeLocation
     * @param locationId
     */
    @Transactional
    public void updateTblInventoryRequestDetailExistence(List<TblInventoryRequestDetail> list, int inventoryResult, String loginUserUuid,boolean ischangeLocation,String locationId) {
        MstLocation mstLocation;
        int changeLocation = CommonConstants.CHANGE_LOCATION_NOT_SELECT;
        String newLocation = "";
        String newLocationAddress = "";
        if (!ischangeLocation) {
            if (StringUtils.isNotEmpty(locationId)) {
                mstLocation = entityManager.find(MstLocation.class, locationId);
                if (mstLocation != null) {
                    changeLocation = CommonConstants.CHANGE_LOCATION_YES;//所在変更有無
                    newLocation = mstLocation.getLocationName();
                    newLocationAddress = mstLocation.getAddress();
                }
            } else {
                changeLocation = CommonConstants.CHANGE_LOCATION_NOT;//所在変更有無
            }
        }
        for (TblInventoryRequestDetail tblInventoryRequestDetail : list) {
            Iterator<TblInventoryRequestDetailId> detailIds = tblInventoryRequestDetail.getTblInventoryRequestDetailIds().iterator();
            TblInventoryRequestDetailId tblInventoryRequestDetailId;
            while (detailIds.hasNext()) {
                tblInventoryRequestDetailId = detailIds.next();
                if (inventoryResult == CommonConstants.EXISTENCE_YES) {
                    tblInventoryRequestDetailId.setExistence(CommonConstants.EXISTENCE_YES);
                } else {
                    tblInventoryRequestDetailId.setExistence(CommonConstants.EXISTENCE_NO);
                }
                tblInventoryRequestDetailId.setUpdateUserUuid(loginUserUuid);
                tblInventoryRequestDetailId.setUpdateDate(new Date());
                entityManager.merge(tblInventoryRequestDetailId);
            }

            if (!getTblInventoryRequestDetailIdExistenceExist(tblInventoryRequestDetail.getUuid())) {
                tblInventoryRequestDetail.setExistence(CommonConstants.EXISTENCE_YES);
                tblInventoryRequestDetail.setNoExistenceReason(null);
            } else {
                tblInventoryRequestDetail.setExistence(CommonConstants.EXISTENCE_NO);
            }
            tblInventoryRequestDetail.setChangeLocation(changeLocation);//所在変更有無
            tblInventoryRequestDetail.setNewLocation(newLocation);//変更後所在地
            tblInventoryRequestDetail.setNewLocationAddress(newLocationAddress);//変更後所在地住所
            tblInventoryRequestDetail.setUpdateUserUuid(loginUserUuid);
            tblInventoryRequestDetail.setUpdateDate(new Date());
            tblInventoryRequestDetail.setTblInventoryRequestDetailIds(null);
            entityManager.merge(tblInventoryRequestDetail);
            //棚卸依頼テーブル更新status
            updateTblInventoryRequestStatus(tblInventoryRequestDetail.getInventoryRequestId(), loginUserUuid);
        }
    }

    /**
     * 棚卸依頼明細IDテーブル存在チェックを行う
     *
     * @param detailUuid
     * @return
     */
    public boolean getTblInventoryRequestDetailIdExistenceExist(String detailUuid) {
        StringBuilder sql = new StringBuilder("SELECT tblInventoryRequestDetailId FROM TblInventoryRequestDetailId tblInventoryRequestDetailId"
                + " WHERE (tblInventoryRequestDetailId.existence = :existenceNo OR tblInventoryRequestDetailId.existence = :existenceNotSelect)");
        sql.append(" AND tblInventoryRequestDetailId.requestDetailId = :requestDetailId");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("existenceNo", CommonConstants.EXISTENCE_NO);
        query.setParameter("existenceNotSelect", CommonConstants.EXISTENCE_NOT_SELECT);
        query.setParameter("requestDetailId", detailUuid);
        List list = query.getResultList();
        return list != null && list.size() > 0;
    }

    /**
     * 棚卸依頼テーブル更新status
     *
     * @param inventoryRequestId
     * @param loginUserUuid
     * @return
     */
    @Transactional
    public boolean updateTblInventoryRequestStatus(String inventoryRequestId, String loginUserUuid) {
        TblInventoryRequest tblInventoryRequest;
        Query query = entityManager.createNamedQuery("TblInventoryRequest.findByUuid");
        query.setParameter("uuid", inventoryRequestId);

        try {

            tblInventoryRequest = (TblInventoryRequest) query.getSingleResult();

        } catch (NoResultException e) {

            return false;
        }
        int noSelectCount = 0;//未選択计数
        List<TblInventoryRequestDetail> list = getTblInventoryRequestDetailSql(inventoryRequestId);
        for (TblInventoryRequestDetail tblInventoryRequestDetail : list) {
            if (tblInventoryRequestDetail.getExistence() == CommonConstants.EXISTENCE_NOT_SELECT) {
                noSelectCount++;
            }
        }
        if (noSelectCount == 0) {
            tblInventoryRequest.setStatus(CommonConstants.INVENTORY_REQUEST_STATUS_DONE);
        } else if (noSelectCount > 0 && noSelectCount < list.size()) {
            tblInventoryRequest.setStatus(CommonConstants.INVENTORY_REQUEST_STATUS_DOING);
        } else if (noSelectCount == list.size()) {
            tblInventoryRequest.setStatus(CommonConstants.INVENTORY_REQUEST_STATUS_UNDO);
        }
        tblInventoryRequest.setUpdateUserUuid(loginUserUuid);
        tblInventoryRequest.setUpdateDate(new Date());
        entityManager.merge(tblInventoryRequest);

        return true;
    }

}
