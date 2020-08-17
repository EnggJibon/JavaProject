/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.detail;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.asset.inventory.TblInventory;
import com.kmcj.karte.resources.asset.inventory.mgmt.company.TblInventoryMgmtCompany;
import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.resources.mgmt.company.MstMgmtCompany;
import com.kmcj.karte.resources.mgmt.company.MstMgmtCompanyService;
import com.kmcj.karte.resources.mgmt.location.MstMgmtLocation;
import com.kmcj.karte.resources.mgmt.location.MstMgmtLocationService;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.Pager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class TblInventoryDetailService {
    
    private static final int BLANK = 0;
    private static final int YES = 1;
    private static final int NO = 2;

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private MstChoiceService mstChoiceService;
    
    @Inject
    private MstMgmtCompanyService mstMgmtCompanyService;

    @Inject
    private MstMgmtLocationService mstMgmtLocationService;

    /**
     * 資産棚卸明細取得
     * 
     * @param inventoryId
     * @param mgmtCompanyCode
     * @param pageNumber
     * @param pageSize
     * @param loginUser
     * @return 
     */
    public TblInventoryDetailVoList getTblInventoryDetailList(String inventoryId, String mgmtCompanyCode,
            int pageNumber, int pageSize, LoginUser loginUser) {
        TblInventoryDetailVoList tblInventoryDetailVoList = new TblInventoryDetailVoList();
        // 件数取得
        List detailCount = getInventoryDetails(inventoryId, mgmtCompanyCode, true);
        Pager pager = new Pager();
        tblInventoryDetailVoList.setPageNumber(pageNumber);
        long counts = (long) detailCount.get(0);
        tblInventoryDetailVoList.setCount(counts);
        tblInventoryDetailVoList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));
        
        List<TblInventoryDetailVo> inventoryList = new ArrayList();
        if (counts != 0) {
            List<TblInventoryDetail> inventoryDetails = getInventoryDetails(inventoryId, mgmtCompanyCode, false);
            if (inventoryDetails != null && !inventoryDetails.isEmpty()) {
                MstChoiceList assetTypeChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_asset.asset_type");
                MstChoiceList acquisitionTypeChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_asset.acquisition_type");
                for (TblInventoryDetail tblInventoryDetail : inventoryDetails) {
                    TblInventoryDetailVo vo = new TblInventoryDetailVo();
                    
                    tblInventoryDetailVoList.setInventoryId(tblInventoryDetail.getTblInventoryDetailPK().getInventoryId()); // 棚卸実施ID
                    vo.setAssetNo(tblInventoryDetail.getMstAsset().getMstAssetPK().getAssetNo()); // 資産番号
                    vo.setBranchNo(tblInventoryDetail.getMstAsset().getMstAssetPK().getBranchNo()); // 補助番号
                    vo.setAssetName(tblInventoryDetail.getMstAsset().getAssetName()); // 資産名称
                    if (tblInventoryDetail.getMstAsset().getMstMgmtLocation() != null) {
                        vo.setMgmtLocationCode(tblInventoryDetail.getMstAsset().getMstMgmtLocation().getMgmtLocationCode()); // 所在先コード
                        vo.setMgmtLocationName(tblInventoryDetail.getMstAsset().getMstMgmtLocation().getMgmtLocationName()); // 設置場所
                    } else {
                        vo.setMgmtLocationCode(null);
                        vo.setMgmtLocationName(null);
                    }
                    if (tblInventoryDetail.getMstAsset().getMstItem()!= null) {
                        vo.setItemCode(tblInventoryDetail.getMstAsset().getMstItem().getItemCode()); // 品目コード
                        vo.setItemName(tblInventoryDetail.getMstAsset().getMstItem().getItemName()); // 品目名称
                    } else {
                        vo.setItemCode(null);
                        vo.setItemName(null);
                    }
                    vo.setAcquisitionYyyymm(tblInventoryDetail.getMstAsset().getAcquisitionYyyymm()); // 取得年月
                    vo.setExistence(tblInventoryDetail.getExistence()); // 金型有無
                    vo.setNoExistenceReason(tblInventoryDetail.getNoExistenceReason()); // 無理由
                    vo.setAssetType(tblInventoryDetail.getMstAsset().getAssetType()); // 資産種類
                    vo.setAcquisitionType(tblInventoryDetail.getMstAsset().getAcquisitionType()); // 取得区分
                    
                    vo.setAssetTypeStr("");
                    vo.setAcquisitionTypeStr("");
                    // 資産種類設定
                    if (assetTypeChoiceList.getMstChoice() != null && !assetTypeChoiceList.getMstChoice().isEmpty()) {
                        for (MstChoice mstChoice : assetTypeChoiceList.getMstChoice()) {
                            if (String.valueOf(vo.getAssetType()).equals(mstChoice.getMstChoicePK().getSeq())) {
                                vo.setAssetTypeStr(mstChoice.getChoice());
                                break;
                            }
                        }
                    }
                    
                    // 取得区分設定
                    if (acquisitionTypeChoiceList.getMstChoice() != null && !acquisitionTypeChoiceList.getMstChoice().isEmpty()) {
                        for (MstChoice mstChoice : acquisitionTypeChoiceList.getMstChoice()) {
                            if (String.valueOf(vo.getAcquisitionType()).equals(mstChoice.getMstChoicePK().getSeq())) {
                                vo.setAcquisitionTypeStr(mstChoice.getChoice());
                                break;
                            }
                        }
                    }
                    
                    if (tblInventoryDetail.getMstMgmtLocation() != null) {
                        vo.setNewMgmtLocationCode(tblInventoryDetail.getMstMgmtLocation().getMgmtLocationCode()); // 変更後所在先コード
                        vo.setNewMgmtLocationName(tblInventoryDetail.getMstMgmtLocation().getMgmtLocationName()); // 変更後設置場所
                    } else {
                        vo.setNewMgmtLocationCode(null);
                        vo.setNewMgmtLocationName(null);
                    }
                    if (tblInventoryDetail.getMstMgmtCompany() != null) {
                        vo.setNewMgmtCompanyCode(tblInventoryDetail.getMstMgmtCompany().getMgmtCompanyCode()); // 変更後管理先コード
                        vo.setNewMgmtCompanyName(tblInventoryDetail.getMstMgmtCompany().getMgmtCompanyName()); // 変更後管理先名称
                    } else {
                        vo.setNewMgmtCompanyCode(null);
                        vo.setNewMgmtCompanyName(null);
                    }
                    
                    // 所在地変更情報のカルテ間連携 LYD S
                    //・所在変更有無	CHANGE_LOCATION	INT(3)	0:未選択（ブランク）、1:有、2:無
                    vo.setChangeLocation(tblInventoryDetail.getChangeLocation());
                    //・変更後所在地	NEW_LOCATION	VARCHAR(100)
                    vo.setNewLocation(tblInventoryDetail.getNewLocation());
                    //・変更後所在地住所	NEW_LOCATION_ADDRESS	VARCHAR(100)
                    vo.setNewLocationAddress(tblInventoryDetail.getNewLocationAddress());
                    // 所在地変更情報のカルテ間連携 LYD E
                    
                    inventoryList.add(vo);
                }
            }
        }
        tblInventoryDetailVoList.setTblInventoryDetailVos(inventoryList);
        
        return tblInventoryDetailVoList;
    }
    
    public List<TblInventoryDetail> getInventoryDetails(String inventoryId, String mgmtCompanyCode, boolean isCount) {
        StringBuilder sql = new StringBuilder();
        if (isCount) {
            sql.append(" SELECT COUNT(1) ");
        } else {
            sql.append(" SELECT inventoryDetail ");
        }
        sql.append(" FROM TblInventoryDetail inventoryDetail ");
        
        sql.append(" JOIN FETCH inventoryDetail.mstAsset mstAsset ");
        sql.append(" LEFT JOIN FETCH mstAsset.mstMgmtLocation mstMgmtLocation ");
        sql.append(" LEFT JOIN FETCH mstAsset.mstMgmtCompany mstMgmtCompany ");
        sql.append(" LEFT JOIN FETCH mstAsset.mstItem mstItem ");
        sql.append(" LEFT JOIN FETCH mstAsset.tblMoldMachineAssetRelationVos assetRelation ");
        sql.append(" LEFT JOIN FETCH assetRelation.mstMold ");
        sql.append(" LEFT JOIN FETCH assetRelation.mstMachine ");
        sql.append(" LEFT JOIN FETCH inventoryDetail.mstMgmtLocation newMstMgmtLocation ");
        sql.append(" LEFT JOIN FETCH inventoryDetail.mstMgmtCompany newMstMgmtCompany ");
        sql.append(" WHERE 1=1 ");
        
        // 棚卸実施ID
        if (!StringUtils.isEmpty(inventoryId)) {
            sql.append("AND inventoryDetail.tblInventoryDetailPK.inventoryId = :inventoryId ");
        }
        // 管理先コード
        if (!StringUtils.isEmpty(mgmtCompanyCode)) {
            sql.append("AND mstAsset.mgmtCompanyCode = :mgmtCompanyCode ");
        }
        
        sql.append(" ORDER BY mstAsset.itemCode ");
        
        Query query = entityManager.createQuery(sql.toString());
        
        // パラーメタ設定
        if (!StringUtils.isEmpty(inventoryId)) {
            query.setParameter("inventoryId", inventoryId);
        }
        if (!StringUtils.isEmpty(mgmtCompanyCode)) {
            query.setParameter("mgmtCompanyCode", mgmtCompanyCode);
        }
        
        List list = query.getResultList();
        return list;
    }

    /**
     * 棚卸回収結果登録保存
     *
     * @param tblInventoryDetailVoList
     * @param loginUser
     * @return 
     */
    @Transactional
    public BasicResponse postTblInventoryDetailList(TblInventoryDetailVoList tblInventoryDetailVoList, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        // 棚卸明細テーブル更新
        if (tblInventoryDetailVoList != null && tblInventoryDetailVoList.getTblInventoryDetailVos() != null && !tblInventoryDetailVoList.getTblInventoryDetailVos().isEmpty()) {
            Date sysDate = DateFormat.strToDatetime(DateFormat.getCurrentDateTime());
            
            Query query = entityManager.createNamedQuery("TblInventoryDetail.findByInventoryId")
                .setParameter("inventoryId", tblInventoryDetailVoList.getInventoryId());
            List<TblInventoryDetail> tblInventoryDetailList = query.getResultList();
            
            int locationChgFlg = NO;
            int companyChgFlg = NO;
            int existenceFlg = NO;
            for (TblInventoryDetailVo vo : tblInventoryDetailVoList.getTblInventoryDetailVos()) {
                
                if (locationChgFlg != BLANK && vo.getExistence() == CommonConstants.EXISTENCE_YES) {
                    if (!StringUtils.isEmpty(vo.getNewMgmtLocationCode())) {
                        locationChgFlg = YES;
                    }
                    existenceFlg = YES;
                }
                if (companyChgFlg != BLANK && vo.getExistence() == CommonConstants.EXISTENCE_YES) {
                    if (!StringUtils.isEmpty(vo.getNewMgmtCompanyCode())) {
                        companyChgFlg = YES;
                    }
                    existenceFlg = YES;
                }
                if (vo.getExistence() == BLANK) {
                    existenceFlg = BLANK;
                    locationChgFlg = BLANK;
                    companyChgFlg = BLANK;
                }
                if (tblInventoryDetailList != null && !tblInventoryDetailList.isEmpty()) {
                    for (TblInventoryDetail detail: tblInventoryDetailList) {
                        // 資産番号、補助番号一致するレコードが更新対象
                        if (vo.getAssetNo().equals(detail.getMstAsset().getMstAssetPK().getAssetNo()) &&
                            vo.getBranchNo().equals(detail.getMstAsset().getMstAssetPK().getBranchNo())) {
                            detail.setExistence(vo.getExistence()); // 現品有無
                            if (StringUtils.isNotEmpty(vo.getNoExistenceReason())) {
                                detail.setNoExistenceReason(vo.getNoExistenceReason()); // 無理由
                            } else {
                                detail.setNoExistenceReason(null);
                            }
                            String oldNewMgmtLocationCode = "";
                            if (StringUtils.isNotEmpty(vo.getNewMgmtLocationCode())) {
                                oldNewMgmtLocationCode = detail.getNewMgmtLocationCode();
                                detail.setNewMgmtLocationCode(vo.getNewMgmtLocationCode()); // 変更後所在先コード
                            } else {
                                detail.setNewMgmtLocationCode(null);
                            }
                            if (StringUtils.isNotEmpty(vo.getNewMgmtCompanyCode())) {
                                detail.setNewMgmtCompanyCode(vo.getNewMgmtCompanyCode()); // 変更後管理先コード
                            } else {
                                detail.setNewMgmtCompanyCode(null);
                            }
                            
                            // 所在地変更情報のカルテ間連携 LYD S
                            //・所在変更有無	CHANGE_LOCATION	INT(3)	0:未選択（ブランク）、1:有、2:無
                            detail.setChangeLocation(vo.getChangeLocation());
                            //・変更後所在地	NEW_LOCATION	VARCHAR(100)
                            detail.setNewLocation(vo.getNewLocation());
                            //・変更後所在地住所	NEW_LOCATION_ADDRESS	VARCHAR(100)
                            detail.setNewLocationAddress(vo.getNewLocationAddress());
                            // 所在地変更情報のカルテ間連携 LYD E
                            
                            detail.setUpdateDate(sysDate);
                            detail.setUpdateUserUuid(loginUser.getUserUuid());
                            
                            // 管理先マスタ登録/更新
                            if (StringUtils.isNotEmpty(vo.getNewMgmtCompanyCode()) && StringUtils.isNotEmpty(vo.getNewMgmtCompanyName())) {
                                MstMgmtCompany mstMgmtCompany = new MstMgmtCompany();
                                mstMgmtCompany.setMgmtCompanyCode(vo.getNewMgmtCompanyCode());
                                mstMgmtCompany.setMgmtCompanyName(vo.getNewMgmtCompanyName());
                                if (mstMgmtCompanyService.getSingleMstMgmtCompany(vo.getNewMgmtCompanyCode())) {
                                    mstMgmtCompanyService.updateMstMgmtCompany(mstMgmtCompany, loginUser);
                                } else {
                                    mstMgmtCompanyService.createMstMgmtCompany(mstMgmtCompany, loginUser);
                                }
                            }
                            // 所在先マスタ登録/更新
                            if (StringUtils.isNotEmpty(vo.getNewMgmtLocationCode()) && StringUtils.isNotEmpty(vo.getNewMgmtLocationName())) {

                                MstMgmtLocation mstMgmtLocation = new MstMgmtLocation();
                                mstMgmtLocation.setMgmtLocationCode(vo.getNewMgmtLocationCode());
                                mstMgmtLocation.setMgmtLocationName(vo.getNewMgmtLocationName());
                                if (mstMgmtLocationService.getSingleMstMgmtLocation(vo.getNewMgmtLocationCode())) {
                                    mstMgmtLocationService.updateMstMgmtLocation(mstMgmtLocation, loginUser);
                                    
                                    if (StringUtils.isNotEmpty(oldNewMgmtLocationCode)) {
                                        if (!oldNewMgmtLocationCode.equals(vo.getNewMgmtLocationCode())) {
                                            detail.setNewAddedMgmtLocation(BLANK);
                                        }
                                    }                                   
                                    
                                } else {
                                    mstMgmtLocationService.createMstMgmtLocation(mstMgmtLocation, loginUser);
                                    detail.setNewAddedMgmtLocation(YES);
                                }
                            } else {
                                detail.setNewAddedMgmtLocation(BLANK);
                            }
                            
                            entityManager.merge(detail);
                            break;
                        }
                    }
                }
            }
            
            // 棚卸管理先テーブル更新
            List<TblInventoryMgmtCompany> mgmtCompanyList = entityManager.createNamedQuery("TblInventoryMgmtCompany.findByPK")
                .setParameter("inventoryId", tblInventoryDetailVoList.getInventoryId())
                .setParameter("mgmtCompanyCode", tblInventoryDetailVoList.getMgmtCompanyCode())
                .setMaxResults(1)
                .getResultList();
            if (mgmtCompanyList != null && !mgmtCompanyList.isEmpty()) {
                mgmtCompanyList.get(0).setReceivedDate(tblInventoryDetailVoList.getReceivedDate()); // 回収日
                mgmtCompanyList.get(0).setMgmtLocationChangedFlg(locationChgFlg); // 設置場所変更有無
                mgmtCompanyList.get(0).setMgmtCompanyChangedFlg(companyChgFlg); // 管理先変更有無
                mgmtCompanyList.get(0).setExistenceDiffFlg(existenceFlg); // 現品差異有無
                mgmtCompanyList.get(0).setUpdateDate(sysDate);
                mgmtCompanyList.get(0).setUpdateUserUuid(loginUser.getUserUuid());
                entityManager.merge(mgmtCompanyList.get(0));
            }
            // 棚卸実施テーブル更新
            List<TblInventory> inventoryList = entityManager.createNamedQuery("TblInventory.findByUuid")
                .setParameter("uuid", tblInventoryDetailVoList.getInventoryId())
                .setMaxResults(1)
                .getResultList();
            if (inventoryList != null && !inventoryList.isEmpty()) {
                query = entityManager.createNamedQuery("TblInventoryMgmtCompany.findAllReceived").setParameter("inventoryId", tblInventoryDetailVoList.getInventoryId());
                List count = query.getResultList();
                inventoryList.get(0).setReceivedMgmtCompanyCount(inventoryList.get(0).getMgmtCompanyCount() - Integer.parseInt(count.get(0).toString())); // 回収済み取引先数
                if (Integer.parseInt(count.get(0).toString()) > 0) {
                    inventoryList.get(0).setStatus(CommonConstants.INVENTORY_STATUS_PART_RECEIVE); // 実施ステータス5:一部回収済みに設定
                } else {
                    inventoryList.get(0).setStatus(CommonConstants.INVENTORY_STATUS_ALL_RECEIVE); // 実施ステータス6:回収済みに設定
                }
                inventoryList.get(0).setUpdateDate(sysDate);
                inventoryList.get(0).setUpdateUserUuid(loginUser.getUserUuid());
                entityManager.merge(inventoryList.get(0));
            }
        }
        
        return response;
    }
    
    /**
     * 棚卸明細取得
     *
     * @param moldUuid
     * @param machineUuid
     * @return
     */
    @Transactional
    public List getTblInventoryDetailMoldMachineUuidSql(String moldUuid, String machineUuid) {
        StringBuilder sql = new StringBuilder("SELECT tblInventoryDetail FROM TblInventoryDetail tblInventoryDetail"
                + " JOIN FETCH tblInventoryDetail.mstAsset mstAsset"
                + " JOIN FETCH mstAsset.tblMoldMachineAssetRelationVos assetRelation WHERE 1 = 1");
        if (StringUtils.isNotEmpty(moldUuid)) {
            sql.append(" AND assetRelation.moldUuid = :moldUuid");
        }
        if (StringUtils.isNotEmpty(machineUuid)) {
            sql.append(" AND assetRelation.machineUuid = :machineUuid");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (StringUtils.isNotEmpty(moldUuid)) {
            query.setParameter("moldUuid", moldUuid.trim());
        }
        if (StringUtils.isNotEmpty(machineUuid)) {
            query.setParameter("machineUuid", machineUuid.trim());
        }
        return query.getResultList();
    }

    /**
     * 棚卸明細テーブル現品有無更新
     *
     * @param moldUuid
     * @param machineUuid
     * @param inventoryResult
     * @param ischangeLocation
     * @param locationId
     */
    @Transactional
    public void updateTblInventoryDetailExistence(String moldUuid, String machineUuid, int inventoryResult,boolean ischangeLocation,String locationId) {
        List<TblInventoryDetail> list = getTblInventoryDetailMoldMachineUuidSql(moldUuid, machineUuid);
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
        if (list != null && list.size() > 0) {
            for (TblInventoryDetail tblInventoryDetail : list) {
                if (inventoryResult == CommonConstants.EXISTENCE_YES) {
                    tblInventoryDetail.setExistence(YES);
                    tblInventoryDetail.setNoExistenceReason(null);
                } else {
                    tblInventoryDetail.setExistence(NO);
                }
                tblInventoryDetail.setChangeLocation(changeLocation);//所在変更有無
                tblInventoryDetail.setNewLocation(newLocation);//変更後所在地
                tblInventoryDetail.setNewLocationAddress(newLocationAddress);//変更後所在地住所
                tblInventoryDetail.setUpdateUserUuid(tblInventoryDetail.getUpdateUserUuid());
                tblInventoryDetail.setUpdateDate(new Date());
                entityManager.merge(tblInventoryDetail);
            }
        }
    }

}
