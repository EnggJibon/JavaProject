/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset;

import com.kmcj.karte.resources.asset.relation.TblMoldMachineAssetRelationVo;
import com.kmcj.karte.resources.asset.relation.TblMoldMachineAssetRelation;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.asset.inventory.TblInventory;
import com.kmcj.karte.resources.asset.inventory.TblInventoryAssetClassCond;
import com.kmcj.karte.resources.asset.relation.TblMoldMachineAssetRelationService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.item.MstItem;
import com.kmcj.karte.resources.item.MstItemService;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.mgmt.company.MstMgmtCompany;
import com.kmcj.karte.resources.mgmt.company.MstMgmtCompanyService;
import com.kmcj.karte.resources.mgmt.location.MstMgmtLocation;
import com.kmcj.karte.resources.mgmt.location.MstMgmtLocationService;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.util.BeanCopyUtil;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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
 * 資産金型・設備照合サービス
 *
 * @author xiaozhou.wang
 */
@Dependent
public class MstAssetService {

    @Inject
    private MstDictionaryService mstDictionaryService;

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private TblCsvImportService tblCsvImportService;

    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private TblMoldMachineAssetRelationService tblMoldMachineAssetRelationService;

    @Inject
    private MstMgmtCompanyService mstMgmtCompanyService;

    @Inject
    private MstMgmtLocationService mstMgmtLocationService;

    @Inject
    private MstItemService mstItemService;

    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("assetNo", " ORDER BY mstAsset.mstAssetPK.assetNo ");//資産番号
        orderKey.put("branchNo", " ORDER BY mstAsset.mstAssetPK.branchNo ");//補助番号
        orderKey.put("assetTypeText", " ORDER BY mstAsset.assetType ");//資産種類
        orderKey.put("assetName", " ORDER BY mstAsset.assetName ");//資産名称
        orderKey.put("mgmtCompanyCode", " ORDER BY mstAsset.mgmtCompanyCode ");//管理先コード
        orderKey.put("mgmtCompanyName", " ORDER BY mstMgmtCompany.mgmtCompanyName ");//管理先名称
        orderKey.put("mgmtLocationCode", " ORDER BY mstAsset.mgmtLocationCode ");//所在先コード
        orderKey.put("mgmtLocationName", " ORDER BY mstMgmtLocation.mgmtLocationName ");//所在先名称　＞設置場所
        orderKey.put("vendorCode", " ORDER BY mstAsset.vendorCode ");//　ベンダーコード
        orderKey.put("itemCode", " ORDER BY mstAsset.itemCode ");//　品目コード
        orderKey.put("itemName", " ORDER BY mstItem.itemName ");//　品目名称
        orderKey.put("acquisitionTypeText", " ORDER BY mstAsset.acquisitionType ");//取得区分
        orderKey.put("acquisitionDate", " ORDER BY mstAsset.acquisitionDate ");//取得日
        orderKey.put("acquisitionYyyymm", " ORDER BY mstAsset.acquisitionYyyymm ");//取得年月
        orderKey.put("acquisitionAmountStr", " ORDER BY mstAsset.acquisitionAmount ");//acquisitionAmount
        orderKey.put("monthBookValueStr", " ORDER BY mstAsset.monthBookValue ");//今月簿価
        orderKey.put("periodBookValueStr", " ORDER BY mstAsset.periodBookValue ");//期初簿価
        orderKey.put("moldCount", " ORDER BY mstAsset.moldCount ");///型数
        orderKey.put("purchasingGroup", "ORDER BY mstAsset.purchasingGroup ");//購買グループ
        orderKey.put("commonInformation", " ORDER BY mstAsset.commonInformation ");//共通情報
        orderKey.put("assetClass", " ORDER BY mstAsset.assetClass ");//資産クラス
        orderKey.put("usingSection", " ORDER BY mstAsset.usingSection ");//使用部門
        orderKey.put("costCenter", " ORDER BY mstAsset.costCenter ");//原価センタ
        orderKey.put("mgmtRegionStr", " ORDER BY mstAsset.mgmtRegion ");//管理地域
        orderKey.put("moldMachineTypeText", " ORDER BY mstAsset.moldMachineType ");//金型・設備区分
        orderKey.put("mainMoldId", " ORDER BY assetRelationMstMold.moldId ");//代表金型
        orderKey.put("mainMachineId", " ORDER BY assetRelationMstMachine.machineId ");//代表設備
        orderKey.put("disposalStatusText", " ORDER BY mstAsset.disposalStatus ");//廃棄ステータス

    }

    /**
     * 資産マスタ複数取得
     *
     * @param assetNo
     * @param assetName
     * @param moldId
     * @param machineId
     * @param mgmtCompanyCode
     * @param pageNumber
     * @param pageSize
     * @param assetId
     * @param langId
     * @param isPage
     * @param isList
     * @param itemCode
     * @param branchNo
     * @param sidx
     * @param sord
     * @return
     */
    public MstAssetList getMstAssetList(String assetNo, String assetName, String moldId, String machineId, String mgmtCompanyCode,
            int pageNumber, int pageSize, String assetId, String langId, boolean isPage,
            boolean isList, String itemCode, String branchNo,
            String sidx,//order Key
            String sord//order 順
    ) {
        MstAssetList mstAssetList = new MstAssetList();

        if (!isPage) {
            List count = getMstAssetSql(assetNo, assetName, moldId, machineId, mgmtCompanyCode, pageNumber, pageSize, assetId, true, itemCode, branchNo, null, null);
            // ページをめぐる
            Pager pager = new Pager();
            mstAssetList.setPageNumber(pageNumber);
            long counts = (long) count.get(0);
            mstAssetList.setCount(counts);
            mstAssetList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));
        }

        List list = getMstAssetSql(assetNo, assetName, moldId, machineId, mgmtCompanyCode, pageNumber, pageSize, assetId, false, itemCode, branchNo, sidx, sord);
        List<MstAssetVo> mstAssetVos = new ArrayList<>();

        if (list != null && list.size() > 0) {
            Map<String, String> map = mstChoiceService.getChoiceMap(langId, new String[]{"mst_asset.asset_type", "mst_asset.acquisition_type", "mst_asset.mgmt_region"});
            // ヘッダー種取得
            List<String> dictKeyList = Arrays.asList("mold", "machine", "disposal_status_processed", "disposal_status_normal");
            Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);

            MstAssetVo mstAssetVo;
            MstAsset mstAsset;
            int decimalPlaces = 0;
            for (int i = 0; i < list.size(); i++) {
                mstAsset = (MstAsset) list.get(i);
                mstAssetVo = new MstAssetVo();
                if (mstAsset.getMstCurrency() != null) {
                    decimalPlaces = mstAsset.getMstCurrency().getDecimalPlaces();
                }

                BeanCopyUtil.copyFields(mstAsset, mstAssetVo);
                mstAssetVo.setAssetNo(mstAsset.getMstAssetPK().getAssetNo());
                mstAssetVo.setBranchNo(mstAsset.getMstAssetPK().getBranchNo());
                if (mstAsset.getAcquisitionDate() != null) {
                    mstAssetVo.setAcquisitionYyyymm(FileUtil.dateFormatToMonthStr(mstAsset.getAcquisitionDate()));
                }
                if (mstAsset.getAcquisitionAmount() != null) {
                    mstAssetVo.setAcquisitionAmountStr(String.valueOf(mstAsset.getAcquisitionAmount().setScale(decimalPlaces, BigDecimal.ROUND_DOWN)));
                } else {
                    mstAssetVo.setAcquisitionAmountStr(String.valueOf(BigDecimal.ZERO));
                }
                if (mstAsset.getMonthBookValue() != null) {
                    mstAssetVo.setMonthBookValueStr(String.valueOf(mstAsset.getMonthBookValue().setScale(decimalPlaces, BigDecimal.ROUND_DOWN)));
                } else {
                    mstAssetVo.setMonthBookValueStr(String.valueOf(BigDecimal.ZERO));
                }
                if (mstAsset.getPeriodBookValue() != null) {
                    mstAssetVo.setPeriodBookValueStr(String.valueOf(mstAsset.getPeriodBookValue().setScale(decimalPlaces, BigDecimal.ROUND_DOWN)));
                } else {
                    mstAssetVo.setPeriodBookValueStr(String.valueOf(BigDecimal.ZERO));
                }
                mstAssetVo.setMoldCount(String.valueOf(mstAsset.getMoldCount()));

                if (map != null) {
                    mstAssetVo.setAssetTypeText(map.get("mst_asset.asset_type" + mstAsset.getAssetType()));// 資産種類値を取得
                    mstAssetVo.setAcquisitionTypeText(map.get("mst_asset.acquisition_type" + mstAsset.getAcquisitionType()));// 取得区分を取得
                    mstAssetVo.setMgmtRegionStr(map.get("mst_asset.mgmt_region" + mstAsset.getMgmtRegion()));// 管理地域を取得
                } else {
                    mstAssetVo.setAssetTypeText("");
                    mstAssetVo.setAcquisitionTypeText("");
                    mstAssetVo.setMgmtRegionStr("");
                }

                // 金型・設備区分を取得
                if (1 == mstAsset.getMoldMachineType()) {
                    mstAssetVo.setMoldMachineTypeText(headerMap.get("mold"));
                } else if (2 == mstAsset.getMoldMachineType()) {
                    mstAssetVo.setMoldMachineTypeText(headerMap.get("machine"));
                }
                if (mstAsset.getAcquisitionDate() != null) {
                    mstAssetVo.setAcquisitionDate(FileUtil.dateFormat(mstAsset.getAcquisitionDate()));
                } else {
                    mstAssetVo.setAcquisitionDate("");
                }
                if (mstAsset.getMstMgmtCompany() != null) {
                    mstAssetVo.setMgmtCompanyName(mstAsset.getMstMgmtCompany().getMgmtCompanyName());
                } else {
                    mstAssetVo.setMgmtCompanyName("");
                }
                if (mstAsset.getMstMgmtLocation() != null) {
                    mstAssetVo.setMgmtLocationName(mstAsset.getMstMgmtLocation().getMgmtLocationName());
                } else {
                    mstAssetVo.setMgmtLocationName("");
                }
                if (mstAsset.getMstItem() != null) {
                    mstAssetVo.setItemName(mstAsset.getMstItem().getItemName());
                } else {
                    mstAssetVo.setItemName("");
                }
                Iterator<TblMoldMachineAssetRelation> mstMoldComponentRelation = mstAsset.getTblMoldMachineAssetRelationVos().iterator();
                List<TblMoldMachineAssetRelationVo> tblMoldMachineAssetRelationVos = new ArrayList<>();
                while (mstMoldComponentRelation.hasNext()) {
                    TblMoldMachineAssetRelationVo tblMoldMachineAssetRelationVo = new TblMoldMachineAssetRelationVo();
                    TblMoldMachineAssetRelation tblMoldMachineAssetRelation = mstMoldComponentRelation.next();
                    tblMoldMachineAssetRelationVo.setUuid(tblMoldMachineAssetRelation.getUuid());
                    if (tblMoldMachineAssetRelation.getMoldUuid() != null) {
                        tblMoldMachineAssetRelationVo.setMoldUuid(tblMoldMachineAssetRelation.getMstMold().getUuid());
                        tblMoldMachineAssetRelationVo.setMoldId(tblMoldMachineAssetRelation.getMstMold().getMoldId());
                        tblMoldMachineAssetRelationVo.setMoldName(tblMoldMachineAssetRelation.getMstMold().getMoldName());
                    } else {
                        tblMoldMachineAssetRelationVo.setMoldUuid(tblMoldMachineAssetRelation.getMoldUuid());
                        tblMoldMachineAssetRelationVo.setMoldId("");
                        tblMoldMachineAssetRelationVo.setMoldName("");
                    }
                    if (tblMoldMachineAssetRelation.getMachineUuid() != null) {
                        tblMoldMachineAssetRelationVo.setMachineUuid(tblMoldMachineAssetRelation.getMstMachine().getUuid());
                        tblMoldMachineAssetRelationVo.setMachineId(tblMoldMachineAssetRelation.getMstMachine().getMachineId());
                        tblMoldMachineAssetRelationVo.setMachineName(tblMoldMachineAssetRelation.getMstMachine().getMachineName());
                    } else {
                        tblMoldMachineAssetRelationVo.setMachineUuid(tblMoldMachineAssetRelation.getMachineUuid());
                        tblMoldMachineAssetRelationVo.setMachineId("");
                        tblMoldMachineAssetRelationVo.setMachineName("");
                    }
                    tblMoldMachineAssetRelationVo.setMainFlg(tblMoldMachineAssetRelation.getMainFlg());

                    // 一覧画面で代表だけを取得する
                    if (!isList) {
                        tblMoldMachineAssetRelationVos.add(tblMoldMachineAssetRelationVo);
                    } else if (1 == tblMoldMachineAssetRelation.getMainFlg()) {
                        tblMoldMachineAssetRelationVos.add(tblMoldMachineAssetRelationVo);
                    }
                }
                if (tblMoldMachineAssetRelationVos.size() > 1) {
                    // 金型ID、設備ID昇順
                    Collections.sort(tblMoldMachineAssetRelationVos, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            TblMoldMachineAssetRelationVo vo1 = (TblMoldMachineAssetRelationVo) o1;
                            TblMoldMachineAssetRelationVo vo2 = (TblMoldMachineAssetRelationVo) o2;
                            if (StringUtils.isNotEmpty(vo1.getMoldId())) {
                                return vo1.getMoldId().compareTo(vo2.getMoldId());
                            } else {
                                return vo1.getMachineId().compareTo(vo2.getMachineId());
                            }
                        }
                    });
                }
                mstAssetVo.setTblMoldMachineAssetRelationVos(tblMoldMachineAssetRelationVos);
                if (mstAsset.getDisposalStatus() == CommonConstants.DISPOSAL_STATUS_NORMAL) {
                    mstAssetVo.setDisposalStatusText(headerMap.get("disposal_status_normal"));
                } else {
                    mstAssetVo.setDisposalStatusText(headerMap.get("disposal_status_processed"));
                }
                mstAssetVos.add(mstAssetVo);
            }
            mstAssetList.setMstAssetList(mstAssetVos);
        } else {

            mstAssetList.setError(true);
            mstAssetList.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(langId, "mst_error_record_not_found");
            mstAssetList.setErrorMessage(msg);
        }

        return mstAssetList;
    }

    /**
     * 資産情報を取得
     *
     * @param assetNo
     * @param branchNo
     * @param itemCode
     * @param langId
     * @return
     */
    public MstAssetList getMstAssetEqualsList(String assetNo, String branchNo, String itemCode, String langId) {
        MstAssetList mstAssetList = new MstAssetList();

        List list = getMstAssetEqualsSql(assetNo, branchNo, itemCode);
        List<MstAssetVo> mstAssetVos = new ArrayList<>();

        if (list != null && list.size() > 0) {
            MstChoiceList assetTypeChoiceList = mstChoiceService.getChoice(langId, "mst_asset.asset_type");
            MstChoiceList acquisitionTypeChoiceList = mstChoiceService.getChoice(langId, "mst_asset.acquisition_type");
            MstChoiceList mgmtRegionList = mstChoiceService.getChoice(langId, "mst_asset.mgmt_region");
            String moldText = mstDictionaryService.getDictionaryValue(langId, "mold");
            String machineText = mstDictionaryService.getDictionaryValue(langId, "machine");
            MstAssetVo mstAssetVo;
            MstAsset mstAsset;
            int decimalPlaces = 0;
            for (int i = 0; i < list.size(); i++) {
                mstAsset = (MstAsset) list.get(i);
                mstAssetVo = new MstAssetVo();
                if (mstAsset.getMstCurrency() != null) {
                    decimalPlaces = mstAsset.getMstCurrency().getDecimalPlaces();
                }

                BeanCopyUtil.copyFields(mstAsset, mstAssetVo);
                mstAssetVo.setAssetNo(mstAsset.getMstAssetPK().getAssetNo());
                mstAssetVo.setBranchNo(mstAsset.getMstAssetPK().getBranchNo());
                if (mstAsset.getAcquisitionDate() != null) {
                    mstAssetVo.setAcquisitionYyyymm(FileUtil.dateFormatToMonthStr(mstAsset.getAcquisitionDate()));
                }
                if (mstAsset.getAcquisitionAmount() != null) {
                    mstAssetVo.setAcquisitionAmountStr(String.valueOf(mstAsset.getAcquisitionAmount().setScale(decimalPlaces, BigDecimal.ROUND_DOWN)));
                } else {
                    mstAssetVo.setAcquisitionAmountStr(String.valueOf(BigDecimal.ZERO));
                }
                if (mstAsset.getMonthBookValue() != null) {
                    mstAssetVo.setMonthBookValueStr(String.valueOf(mstAsset.getMonthBookValue().setScale(decimalPlaces, BigDecimal.ROUND_DOWN)));
                } else {
                    mstAssetVo.setMonthBookValueStr(String.valueOf(BigDecimal.ZERO));
                }
                if (mstAsset.getPeriodBookValue() != null) {
                    mstAssetVo.setPeriodBookValueStr(String.valueOf(mstAsset.getPeriodBookValue().setScale(decimalPlaces, BigDecimal.ROUND_DOWN)));
                } else {
                    mstAssetVo.setPeriodBookValueStr(String.valueOf(BigDecimal.ZERO));
                }
                mstAssetVo.setMoldCount(String.valueOf(mstAsset.getMoldCount()));

                // 資産種類値を取得
                if (assetTypeChoiceList != null && assetTypeChoiceList.getMstChoice() != null && assetTypeChoiceList.getMstChoice().size() > 0) {
                    for (int momi = 0; momi < assetTypeChoiceList.getMstChoice().size(); momi++) {
                        MstChoice aMstChoice = assetTypeChoiceList.getMstChoice().get(momi);
                        if (aMstChoice.getMstChoicePK().getSeq() != null && mstAsset.getAssetType() > 0) {
                            if (aMstChoice.getMstChoicePK().getSeq().equals(String.valueOf(mstAsset.getAssetType()))) {
                                mstAssetVo.setAssetTypeText(aMstChoice.getChoice());
                                break;
                            }
                        } else {
                            mstAssetVo.setAssetTypeText("");
                        }
                    }
                } else {
                    mstAssetVo.setAssetTypeText("");
                }

                // 取得区分を取得
                if (acquisitionTypeChoiceList != null && acquisitionTypeChoiceList.getMstChoice() != null && acquisitionTypeChoiceList.getMstChoice().size() > 0) {
                    for (int momi = 0; momi < acquisitionTypeChoiceList.getMstChoice().size(); momi++) {
                        MstChoice aMstChoice = acquisitionTypeChoiceList.getMstChoice().get(momi);
                        if (aMstChoice.getMstChoicePK().getSeq() != null && mstAsset.getAcquisitionType() > 0) {
                            if (aMstChoice.getMstChoicePK().getSeq().equals(String.valueOf(mstAsset.getAcquisitionType()))) {
                                mstAssetVo.setAcquisitionTypeText(aMstChoice.getChoice());
                                break;
                            }
                        } else {
                            mstAssetVo.setAcquisitionTypeText("");
                        }
                    }
                } else {
                    mstAssetVo.setAcquisitionTypeText("");
                }

                // 管理地域を取得
                if (mgmtRegionList != null && mgmtRegionList.getMstChoice() != null && mgmtRegionList.getMstChoice().size() > 0) {
                    for (int momi = 0; momi < mgmtRegionList.getMstChoice().size(); momi++) {
                        MstChoice aMstChoice = mgmtRegionList.getMstChoice().get(momi);
                        if (aMstChoice.getMstChoicePK().getSeq() != null && mstAsset.getMgmtRegion() > 0) {
                            if (aMstChoice.getMstChoicePK().getSeq().equals(String.valueOf(mstAsset.getMgmtRegion()))) {
                                mstAssetVo.setMgmtRegionStr(aMstChoice.getChoice());
                                break;
                            }
                        } else {
                            mstAssetVo.setMgmtRegionStr("");
                        }
                    }
                } else {
                    mstAssetVo.setMgmtRegionStr("");
                }
                // 金型・設備区分を取得
                if (1 == mstAsset.getMoldMachineType()) {
                    mstAssetVo.setMoldMachineTypeText(moldText);
                } else if (2 == mstAsset.getMoldMachineType()) {
                    mstAssetVo.setMoldMachineTypeText(machineText);
                }
                if (mstAsset.getAcquisitionDate() != null) {
                    mstAssetVo.setAcquisitionDate(FileUtil.dateFormat(mstAsset.getAcquisitionDate()));
                } else {
                    mstAssetVo.setAcquisitionDate("");
                }
                if (mstAsset.getMstMgmtCompany() != null) {
                    mstAssetVo.setMgmtCompanyName(mstAsset.getMstMgmtCompany().getMgmtCompanyName());
                } else {
                    mstAssetVo.setMgmtCompanyName("");
                }
                if (mstAsset.getMstMgmtLocation() != null) {
                    mstAssetVo.setMgmtLocationName(mstAsset.getMstMgmtLocation().getMgmtLocationName());
                } else {
                    mstAssetVo.setMgmtLocationName("");
                }
                if (mstAsset.getMstItem() != null) {
                    mstAssetVo.setItemName(mstAsset.getMstItem().getItemName());
                } else {
                    mstAssetVo.setItemName("");
                }
                mstAssetVo.setTblMoldMachineAssetRelationVos(null);
                mstAssetVos.add(mstAssetVo);
            }
            mstAssetList.setMstAssetList(mstAssetVos);
        } else {
            mstAssetList.setError(true);
            mstAssetList.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(langId, "mst_error_record_not_found");
            mstAssetList.setErrorMessage(msg);
        }

        return mstAssetList;
    }

    /**
     * 設備マスタ更新
     *
     * @param mstAssetVo
     * @param msgValueMap
     * @param loginUser
     * @param csvFlag
     * @return
     */
    @Transactional
    public MstAssetVo updateMstAsset(MstAssetVo mstAssetVo, Map<String, String> msgValueMap, LoginUser loginUser, boolean csvFlag) {
        int oldMoldMachineType;
        int newMoldMachineType;
        if (msgValueMap == null) {
            msgValueMap = getDictionaryList(loginUser.getLangId());
        }
        if (!csvFlag) {
            mstAssetVo = checkMstAsset(mstAssetVo, msgValueMap);
            if (mstAssetVo.isError()) {
                mstAssetVo.setError(mstAssetVo.isError());
                mstAssetVo.setErrorCode(mstAssetVo.getErrorCode());
                mstAssetVo.setErrorMessage(mstAssetVo.getErrorMessage());
                return mstAssetVo;
            }
        }

        MstAssetPK mstAssetPK = new MstAssetPK();
        mstAssetPK.setAssetNo(mstAssetVo.getAssetNo());
        mstAssetPK.setBranchNo(mstAssetVo.getBranchNo());
        MstAsset updateMstAsset = entityManager.find(MstAsset.class, mstAssetPK);
        if (updateMstAsset == null) {
            mstAssetVo.setError(true);
            mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
            mstAssetVo.setErrorMessage(msgValueMap.get("mst_error_record_not_found"));
            return mstAssetVo;
        }

        oldMoldMachineType = updateMstAsset.getMoldMachineType();
        newMoldMachineType = mstAssetVo.getMoldMachineType();
        BeanCopyUtil.copyFields(mstAssetVo, updateMstAsset);
        updateMstAsset.setMstAssetPK(mstAssetPK);

        updateMstAsset.setUpdateDate(new Date());
        updateMstAsset.setUpdateUserUuid(loginUser.getUserUuid());
        updateMstAsset.setMoldCount(Integer.parseInt(mstAssetVo.getMoldCount()));
        FileUtil fu = new FileUtil();
        if (StringUtils.isNotEmpty(mstAssetVo.getAcquisitionDate())) {
            updateMstAsset.setAcquisitionDate(fu.getDateParseForDate(mstAssetVo.getAcquisitionDate()));
            updateMstAsset.setAcquisitionYyyymm(FileUtil.dateFormatToMonthStr(fu.getDateParseForDate(mstAssetVo.getAcquisitionDate())));
        } else {
            updateMstAsset.setAcquisitionDate(null);
            updateMstAsset.setAcquisitionYyyymm(null);
        }
        if (StringUtils.isNotEmpty(mstAssetVo.getAcquisitionAmountStr())) {
            updateMstAsset.setAcquisitionAmount(new BigDecimal(mstAssetVo.getAcquisitionAmountStr()));
        } else {
            updateMstAsset.setAcquisitionAmount(BigDecimal.ZERO);
        }
        if (StringUtils.isNotEmpty(mstAssetVo.getMonthBookValueStr())) {
            updateMstAsset.setMonthBookValue(new BigDecimal(mstAssetVo.getMonthBookValueStr()));
        } else {
            updateMstAsset.setMonthBookValue(BigDecimal.ZERO);
        }
        if (StringUtils.isNotEmpty(mstAssetVo.getPeriodBookValueStr())) {
            updateMstAsset.setPeriodBookValue(new BigDecimal(mstAssetVo.getPeriodBookValueStr()));
        } else {
            updateMstAsset.setPeriodBookValue(BigDecimal.ZERO);
        }
        updateMstAsset.setDisposalStatus(mstAssetVo.getDisposalStatus());
        //　資産マスタ更新
        updateMstAsset.setTblMoldMachineAssetRelationVos(null);
        entityManager.merge(updateMstAsset);
        if (StringUtils.isNotEmpty(mstAssetVo.getMgmtCompanyCode()) && StringUtils.isNotEmpty(mstAssetVo.getMgmtCompanyName())) {
            MstMgmtCompany mstMgmtCompany = new MstMgmtCompany();
            mstMgmtCompany.setMgmtCompanyCode(mstAssetVo.getMgmtCompanyCode());
            mstMgmtCompany.setMgmtCompanyName(mstAssetVo.getMgmtCompanyName());
            if (mstMgmtCompanyService.getSingleMstMgmtCompany(mstAssetVo.getMgmtCompanyCode())) {
                mstMgmtCompanyService.updateMstMgmtCompany(mstMgmtCompany, loginUser);
            } else {
                mstMgmtCompanyService.createMstMgmtCompany(mstMgmtCompany, loginUser);
            }
        }
        if (StringUtils.isNotEmpty(mstAssetVo.getMgmtLocationCode()) && StringUtils.isNotEmpty(mstAssetVo.getMgmtLocationName())) {
            MstMgmtLocation mstMgmtLocation = new MstMgmtLocation();
            mstMgmtLocation.setMgmtLocationCode(mstAssetVo.getMgmtLocationCode());
            mstMgmtLocation.setMgmtLocationName(mstAssetVo.getMgmtLocationName());
            if (mstMgmtLocationService.getSingleMstMgmtLocation(mstAssetVo.getMgmtLocationCode())) {
                mstMgmtLocationService.updateMstMgmtLocation(mstMgmtLocation, loginUser);
            } else {
                mstMgmtLocationService.createMstMgmtLocation(mstMgmtLocation, loginUser);
            }
        }
        if (StringUtils.isNotEmpty(mstAssetVo.getItemCode()) && StringUtils.isNotEmpty(mstAssetVo.getItemName())) {
            MstItem mstItem = new MstItem();
            mstItem.setItemCode(mstAssetVo.getItemCode());
            mstItem.setItemName(mstAssetVo.getItemName());
            if (mstItemService.getSingleMstItem(mstAssetVo.getItemCode())) {
                mstItemService.updateMstItem(mstItem, loginUser);
            } else {
                mstItemService.createMstItem(mstItem, loginUser);
            }
        }
        // 資産関係マスタを更新
        tblMoldMachineAssetRelationService.controlMoldMachineAssetRelation(updateMstAsset.getUuid(), mstAssetVo.getTblMoldMachineAssetRelationVos(), oldMoldMachineType, newMoldMachineType, loginUser, csvFlag);

        return mstAssetVo;
    }

    /**
     * 設備マスタ追加
     *
     * @param mstAssetVo
     * @param msgValueMap
     * @param loginUser
     * @return
     */
    @Transactional
    public MstAssetVo createMstAsset(MstAssetVo mstAssetVo, Map<String, String> msgValueMap, LoginUser loginUser, boolean csvFlag) {
        if (msgValueMap == null) {
            msgValueMap = getDictionaryList(loginUser.getLangId());
        }

        if (!csvFlag) {
            // 単体チェック
            mstAssetVo = checkMstAsset(mstAssetVo, msgValueMap);
            if (mstAssetVo.isError()) {
                mstAssetVo.setError(mstAssetVo.isError());
                mstAssetVo.setErrorCode(mstAssetVo.getErrorCode());
                mstAssetVo.setErrorMessage(mstAssetVo.getErrorMessage());
                return mstAssetVo;
            }
            // 複合チェック
            if (getMstAssetExistCheckByPK(mstAssetVo.getAssetNo(), mstAssetVo.getBranchNo())) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_record_exists"));
                return mstAssetVo;
            }
        }
        //設備マスタ追加
        MstAsset mstAsset = new MstAsset();
        BeanCopyUtil.copyFields(mstAssetVo, mstAsset);
        MstAssetPK mstAssetPK = new MstAssetPK();
        mstAssetPK.setAssetNo(mstAssetVo.getAssetNo());
        mstAssetPK.setBranchNo(mstAssetVo.getBranchNo());
        mstAsset.setMstAssetPK(mstAssetPK);
        mstAsset.setUuid(IDGenerator.generate());
        mstAssetVo.setUuid(mstAsset.getUuid());
        mstAsset.setCreateDate(new Date());
        mstAsset.setCreateUserUuid(loginUser.getUserUuid());
        mstAsset.setUpdateDate(new Date());
        mstAsset.setUpdateUserUuid(loginUser.getUserUuid());
        mstAsset.setCurrencyCode(mstAssetVo.getCurrencyCode());
        mstAsset.setMoldCount(Integer.parseInt(mstAssetVo.getMoldCount()));

        FileUtil fu = new FileUtil();
        if (StringUtils.isNotEmpty(mstAssetVo.getAcquisitionDate())) {
            mstAsset.setAcquisitionDate(fu.getDateParseForDate(mstAssetVo.getAcquisitionDate()));
            mstAsset.setAcquisitionYyyymm(FileUtil.dateFormatToMonthStr(fu.getDateParseForDate(mstAssetVo.getAcquisitionDate())));
        } else {
            mstAsset.setAcquisitionDate(null);
            mstAsset.setAcquisitionYyyymm(null);
        }
        if (StringUtils.isNotEmpty(mstAssetVo.getAcquisitionAmountStr())) {
            mstAsset.setAcquisitionAmount(new BigDecimal(mstAssetVo.getAcquisitionAmountStr()));
        } else {
            mstAsset.setAcquisitionAmount(BigDecimal.ZERO);
        }
        if (StringUtils.isNotEmpty(mstAssetVo.getMonthBookValueStr())) {
            mstAsset.setMonthBookValue(new BigDecimal(mstAssetVo.getMonthBookValueStr()));
        } else {
            mstAsset.setMonthBookValue(BigDecimal.ZERO);
        }
        if (StringUtils.isNotEmpty(mstAssetVo.getPeriodBookValueStr())) {
            mstAsset.setPeriodBookValue(new BigDecimal(mstAssetVo.getPeriodBookValueStr()));
        } else {
            mstAsset.setPeriodBookValue(BigDecimal.ZERO);
        }
        if (StringUtils.isNotEmpty(mstAssetVo.getMgmtCompanyCode()) && StringUtils.isNotEmpty(mstAssetVo.getMgmtCompanyName())) {
            MstMgmtCompany mstMgmtCompany = new MstMgmtCompany();
            mstMgmtCompany.setMgmtCompanyCode(mstAssetVo.getMgmtCompanyCode());
            mstMgmtCompany.setMgmtCompanyName(mstAssetVo.getMgmtCompanyName());
            if (mstMgmtCompanyService.getSingleMstMgmtCompany(mstAssetVo.getMgmtCompanyCode())) {
                mstMgmtCompanyService.updateMstMgmtCompany(mstMgmtCompany, loginUser);
            } else {
                mstMgmtCompanyService.createMstMgmtCompany(mstMgmtCompany, loginUser);
            }
        }
        if (StringUtils.isNotEmpty(mstAssetVo.getMgmtLocationCode()) && StringUtils.isNotEmpty(mstAssetVo.getMgmtLocationName())) {
            MstMgmtLocation mstMgmtLocation = new MstMgmtLocation();
            mstMgmtLocation.setMgmtLocationCode(mstAssetVo.getMgmtLocationCode());
            mstMgmtLocation.setMgmtLocationName(mstAssetVo.getMgmtLocationName());
            if (mstMgmtLocationService.getSingleMstMgmtLocation(mstAssetVo.getMgmtLocationCode())) {
                mstMgmtLocationService.updateMstMgmtLocation(mstMgmtLocation, loginUser);
            } else {
                mstMgmtLocationService.createMstMgmtLocation(mstMgmtLocation, loginUser);
            }
        }
        if (StringUtils.isNotEmpty(mstAssetVo.getItemCode()) && StringUtils.isNotEmpty(mstAssetVo.getItemName())) {
            MstItem mstItem = new MstItem();
            mstItem.setItemCode(mstAssetVo.getItemCode());
            mstItem.setItemName(mstAssetVo.getItemName());
            if (mstItemService.getSingleMstItem(mstAssetVo.getItemCode())) {
                mstItemService.updateMstItem(mstItem, loginUser);
            } else {
                mstItemService.createMstItem(mstItem, loginUser);
            }
        }
        mstAsset.setTblMoldMachineAssetRelationVos(null);
        // 資産マスタを追加
        entityManager.persist(mstAsset);

        // 関係マスタを更新
        tblMoldMachineAssetRelationService.controlMoldMachineAssetRelation(mstAsset.getUuid(), mstAssetVo.getTblMoldMachineAssetRelationVos(), 0, 0, loginUser, false);

        return mstAssetVo;
    }

    /**
     * 資産削除用
     *
     * @param assetId
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse deleteMstAsset(String assetId, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        if (!getMstAssetExistCheckId(assetId)) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found)"));
            return basicResponse;
        }

        Query query = entityManager.createNamedQuery("MstAsset.deleteByUuid");
        query.setParameter("uuid", assetId);
        query.executeUpdate();
        return basicResponse;
    }

    /**
     * 資産マスタに存在チェックを行う
     *
     * @param assetNo
     * @param branchNo
     * @return　boolean
     */
    public boolean getMstAssetExistCheckByPK(String assetNo, String branchNo) {
        Query query = entityManager.createNamedQuery("MstAsset.findByPK");
        query.setParameter("assetNo", assetNo);
        query.setParameter("branchNo", branchNo);
        try {
            query.getSingleResult();
        } catch (NoResultException e) {
            return false;
        }

        return true;
    }

    /**
     * PKより、資産マスタ情報を取得する
     *
     * @param assetNo
     * @param branchNo
     * @return boolean
     */
    public MstAsset getMstAssetByPK(String assetNo, String branchNo) {
        Query query = entityManager.createNamedQuery("MstAsset.findByPK");
        query.setParameter("assetNo", assetNo);
        query.setParameter("branchNo", branchNo);
        try {
            return (MstAsset) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    /**
     * 資産マスタに存在チェックを行う
     *
     * @param assetId
     * @return　boolean
     */
    public boolean getMstAssetExistCheckId(String assetId) {
        Query query = entityManager.createNamedQuery("MstAsset.findByUuid");
        query.setParameter("uuid", assetId);
        try {
            query.getSingleResult();
        } catch (NoResultException e) {
            return false;
        }

        return true;
    }

    /**
     *
     * @param assetNo
     * @param assetName
     * @param moldId
     * @param machineId
     * @param mgmtCompanyCode
     * @param pageNumber
     * @param pageSize
     * @param assetId
     * @param isCount
     * @param itemCode
     * @param branchNo
     * @return
     */
    private List getMstAssetSql(
            String assetNo,
            String assetName,
            String moldId,
            String machineId,
            String mgmtCompanyCode,
            int pageNumber,
            int pageSize,
            String assetId,
            boolean isCount,
            String itemCode,
            String branchNo,
            String sidx,//order Key
            String sord//order 順
    ) {

        StringBuilder sql = new StringBuilder(" SELECT ");
        if (isCount) {
            sql = sql.append(" COUNT(1) ");
        } else {
            sql = sql.append(" mstAsset ");
        }

        sql = sql.append(" FROM MstAsset mstAsset");
        if (StringUtils.isNotEmpty(assetId)) {
            sql = sql.append(" LEFT JOIN FETCH TblMoldMachineAssetRelation assetRelation ON mstAsset.uuid = assetRelation.assetId");
        } else {
            sql = sql.append(" LEFT JOIN FETCH mstAsset.tblMoldMachineAssetRelationVos assetRelation ON assetRelation.mainFlg = 1");
        }
        sql = sql.append(" LEFT JOIN FETCH assetRelation.mstMold assetRelationMstMold "
                + " LEFT JOIN FETCH assetRelation.mstMachine assetRelationMstMachine "
                + " LEFT JOIN FETCH mstAsset.mstMgmtCompany mstMgmtCompany"
                + " LEFT JOIN FETCH mstAsset.mstMgmtLocation mstMgmtLocation"
                + " LEFT JOIN FETCH mstAsset.mstItem mstItem"
                + " LEFT JOIN FETCH mstAsset.mstCurrency mstCurrency"
                + " WHERE 1=1 ");

        if (StringUtils.isNotEmpty(assetNo)) {
            sql = sql.append(" AND mstAsset.mstAssetPK.assetNo LIKE :assetNo ");
        }
        if (StringUtils.isNotEmpty(assetName)) {
            sql = sql.append(" AND mstAsset.assetName LIKE :assetName ");
        }
        if (StringUtils.isNotEmpty(moldId)) {
            sql = sql.append(" AND assetRelationMstMold.moldId LIKE :moldId ");
        }
        if (StringUtils.isNotEmpty(machineId)) {
            sql = sql.append(" AND assetRelationMstMachine.machineId LIKE :machineId ");
        }
        if (StringUtils.isNotEmpty(mgmtCompanyCode)) {
            sql = sql.append(" AND mstAsset.mgmtCompanyCode LIKE :mgmtCompanyCode ");
        }
        if (StringUtils.isNotEmpty(itemCode)) {
            sql = sql.append(" AND mstAsset.itemCode LIKE :itemCode ");
        }
        if (StringUtils.isNotEmpty(branchNo)) {
            sql = sql.append(" AND mstAsset.mstAssetPK.branchNo LIKE :branchNo ");
        }

        if (StringUtils.isNotEmpty(assetId)) {
            sql = sql.append(" AND mstAsset.uuid = :assetId ");
        }
        if (!isCount) {
            // 
            if (StringUtils.isNotEmpty(sidx)) {
                sql = sql.append(orderKey.get(sidx));

                if (StringUtils.isNotEmpty(sord)) {
                    sql = sql.append(sord);
                }

            } else {

                sql.append(" ORDER BY mstAsset.mstAssetPK.assetNo,mstAsset.mstAssetPK.branchNo ");//表示順は資産番号、補助番号の昇順。
            }
        }

        Query query = entityManager.createQuery(sql.toString());

        if (StringUtils.isNotEmpty(assetNo)) {
            query.setParameter("assetNo", "%" + assetNo + "%");
        }

        if (StringUtils.isNotEmpty(assetName)) {
            query.setParameter("assetName", "%" + assetName + "%");
        }

        if (StringUtils.isNotEmpty(moldId)) {
            query.setParameter("moldId", "%" + moldId + "%");
        }

        if (StringUtils.isNotEmpty(machineId)) {
            query.setParameter("machineId", "%" + machineId + "%");
        }

        if (StringUtils.isNotEmpty(mgmtCompanyCode)) {
            query.setParameter("mgmtCompanyCode", "%" + mgmtCompanyCode + "%");
        }

        if (StringUtils.isNotEmpty(itemCode)) {
            itemCode = itemCode.trim();
            query.setParameter("itemCode", "%" + itemCode + "%");
        }

        if (StringUtils.isNotEmpty(branchNo)) {
            branchNo = branchNo.trim();
            query.setParameter("branchNo", "%" + branchNo + "%");
        }

        if (StringUtils.isNotEmpty(assetId)) {
            query.setParameter("assetId", assetId);
        }

        if (!isCount) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }

        List list = query.getResultList();

        return list;
    }

    /**
     * 資産取得equals
     *
     * @param assetNo
     * @param branchNo
     * @param itemCode
     * @return
     */
    private List getMstAssetEqualsSql(
            String assetNo,
            String branchNo,
            String itemCode
    ) {

        StringBuilder sql = new StringBuilder(" SELECT mstAsset FROM MstAsset mstAsset"
                + " LEFT JOIN FETCH mstAsset.mstMgmtCompany mstMgmtCompany"
                + " LEFT JOIN FETCH mstAsset.mstMgmtLocation mstMgmtLocation"
                + " LEFT JOIN FETCH mstAsset.mstItem mstItem"
                + " LEFT JOIN FETCH mstAsset.mstCurrency mstCurrency"
                + " WHERE 1=1 ");

        if (StringUtils.isNotEmpty(assetNo)) {
            sql = sql.append(" AND mstAsset.mstAssetPK.assetNo = :assetNo ");
        }
        if (StringUtils.isNotEmpty(branchNo)) {
            sql = sql.append(" AND mstAsset.mstAssetPK.branchNo = :branchNo ");
        }
        if (StringUtils.isNotEmpty(itemCode)) {
            sql = sql.append(" AND mstAsset.itemCode = :itemCode ");
        }

        sql.append(" ORDER BY mstAsset.mstAssetPK.assetNo,mstAsset.mstAssetPK.branchNo ");

        Query query = entityManager.createQuery(sql.toString());

        if (StringUtils.isNotEmpty(assetNo)) {
            assetNo = assetNo.trim();
            query.setParameter("assetNo", assetNo);
        }

        if (StringUtils.isNotEmpty(branchNo)) {
            branchNo = branchNo.trim();
            query.setParameter("branchNo", branchNo);
        }

        if (StringUtils.isNotEmpty(itemCode)) {
            itemCode = itemCode.trim();
            query.setParameter("itemCode", itemCode);
        }

        List list = query.getResultList();

        return list;
    }

    /**
     * 資産に対するチェックを行う
     *
     * @param mstAsset
     * @param basicResponse
     * @return
     */
    private MstAssetVo checkMstAsset(MstAssetVo mstAssetVo, Map<String, String> msgValueMap) {
        FileUtil fu = new FileUtil();
        if (StringUtils.isEmpty(mstAssetVo.getAssetNo())) {// 資産番号
            mstAssetVo.setError(true);
            mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
            mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_not_null"));
            mstAssetVo.setField(msgValueMap.get("asset_no"));
            mstAssetVo.setValue(mstAssetVo.getAssetNo());
            return mstAssetVo;
        } else if (fu.maxLangthCheck(mstAssetVo.getAssetNo(), 45)) {
            mstAssetVo.setError(true);
            mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
            mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_over_length"));
            mstAssetVo.setField(msgValueMap.get("asset_no"));
            mstAssetVo.setValue(mstAssetVo.getAssetNo());
            return mstAssetVo;
        }
        if (StringUtils.isEmpty(mstAssetVo.getBranchNo())) {// 補助番号
            mstAssetVo.setError(true);
            mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
            mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_not_null"));
            mstAssetVo.setField(msgValueMap.get("branch_no"));
            mstAssetVo.setValue(mstAssetVo.getBranchNo());
            return mstAssetVo;
        } else if (fu.maxLangthCheck(mstAssetVo.getBranchNo(), 45)) {
            mstAssetVo.setError(true);
            mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
            mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_over_length"));
            mstAssetVo.setField(msgValueMap.get("branch_no"));
            mstAssetVo.setValue(mstAssetVo.getBranchNo());
            return mstAssetVo;
        }

        if (StringUtils.isNotEmpty(String.valueOf(mstAssetVo.getAssetType()))) {//資産種類
            if (!fu.isNumber(String.valueOf(mstAssetVo.getAssetType()))) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_not_null"));
                mstAssetVo.setField(msgValueMap.get("asset_type"));
                mstAssetVo.setValue(String.valueOf(mstAssetVo.getAssetType()));
                return mstAssetVo;
            }
        }

//        if (StringUtils.isEmpty(mstAssetVo.getAssetName())) {// 資産名称
//            mstAssetVo.setError(true);
//            mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
//            mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_not_null"));
//            mstAssetVo.setField(msgValueMap.get("branch_no"));
//            mstAssetVo.setValue(mstAssetVo.getBranchNo());
//            return mstAssetVo;
//        } else if (fu.maxLangthCheck(mstAssetVo.getAssetName(), 100)) {
//            mstAssetVo.setError(true);
//            mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
//            mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_over_length"));
//            mstAssetVo.setField(msgValueMap.get("asset_name"));
//            mstAssetVo.setValue(mstAssetVo.getAssetName());
//            return mstAssetVo;
//        }
        if (StringUtils.isNotEmpty(mstAssetVo.getAssetName())) {// 資産名称
            if (fu.maxLangthCheck(mstAssetVo.getAssetName(), 100)) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_over_length"));
                mstAssetVo.setField(msgValueMap.get("asset_name"));
                mstAssetVo.setValue(mstAssetVo.getAssetName());
                return mstAssetVo;
            }
        }

        if (StringUtils.isNotEmpty(mstAssetVo.getMgmtCompanyCode())) {// 管理先コード
            if (fu.maxLangthCheck(mstAssetVo.getMgmtCompanyCode(), 100)) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_over_length"));
                mstAssetVo.setField(msgValueMap.get("mgmt_company_code"));
                mstAssetVo.setValue(mstAssetVo.getMgmtCompanyCode());
                return mstAssetVo;
            }
        }
        if (StringUtils.isNotEmpty(mstAssetVo.getMgmtCompanyName())) {// 管理先名称
            if (fu.maxLangthCheck(mstAssetVo.getMgmtCompanyName(), 100)) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_over_length"));
                mstAssetVo.setField(msgValueMap.get("mgmt_company_name"));
                mstAssetVo.setValue(mstAssetVo.getMgmtCompanyName());
                return mstAssetVo;
            }
        }

        if (StringUtils.isNotEmpty(mstAssetVo.getMgmtLocationCode())) {// 所在先コード
            if (fu.maxLangthCheck(mstAssetVo.getMgmtLocationCode(), 100)) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_over_length"));
                mstAssetVo.setField(msgValueMap.get("mgmt_location_code"));
                mstAssetVo.setValue(mstAssetVo.getMgmtLocationCode());
                return mstAssetVo;
            }
        }
        if (StringUtils.isNotEmpty(mstAssetVo.getMgmtLocationName())) {// 設置場所
            if (fu.maxLangthCheck(mstAssetVo.getMgmtLocationName(), 100)) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_over_length"));
                mstAssetVo.setField(msgValueMap.get("mgmt_location_name"));
                mstAssetVo.setValue(mstAssetVo.getMgmtLocationName());
                return mstAssetVo;
            }
        }

        if (StringUtils.isNotEmpty(mstAssetVo.getVendorCode())) {// ベンダーコード
            if (fu.maxLangthCheck(mstAssetVo.getVendorCode(), 100)) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_over_length"));
                mstAssetVo.setField(msgValueMap.get("vendor_code"));
                mstAssetVo.setValue(mstAssetVo.getVendorCode());
                return mstAssetVo;
            }
        }

        if (StringUtils.isNotEmpty(mstAssetVo.getItemCode())) {// 品目コード
            if (fu.maxLangthCheck(mstAssetVo.getItemCode(), 100)) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_over_length"));
                mstAssetVo.setField(msgValueMap.get("item_code"));
                mstAssetVo.setValue(mstAssetVo.getItemCode());
                return mstAssetVo;
            }
        }
        if (StringUtils.isNotEmpty(mstAssetVo.getItemName())) {// 品目名称
            if (fu.maxLangthCheck(mstAssetVo.getItemName(), 100)) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_over_length"));
                mstAssetVo.setField(msgValueMap.get("asset_item_name"));
                mstAssetVo.setValue(mstAssetVo.getItemName());
                return mstAssetVo;
            }
        }

        if (StringUtils.isNotEmpty(String.valueOf(mstAssetVo.getAcquisitionType()))) {// 取得区分
            if (!fu.isNumber(String.valueOf(mstAssetVo.getAcquisitionType()))) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_not_isnumber"));
                mstAssetVo.setField(msgValueMap.get("acquisition_type"));
                mstAssetVo.setValue(String.valueOf(mstAssetVo.getAcquisitionType()));
                return mstAssetVo;
            }
        }

        if (StringUtils.isNotEmpty(mstAssetVo.getAcquisitionDate())) {//取得日
            if (!fu.dateCheck(mstAssetVo.getAcquisitionDate())) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_date_format_invalid"));
                mstAssetVo.setField(msgValueMap.get("acquisition_date"));
                mstAssetVo.setValue(mstAssetVo.getAcquisitionDate());
                return mstAssetVo;
            }
        }
        if (StringUtils.isNotEmpty(mstAssetVo.getAcquisitionYyyymm())) {//  取得年月
            if (fu.maxLangthCheck(mstAssetVo.getAcquisitionYyyymm(), 6)) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_over_length"));
                mstAssetVo.setField(msgValueMap.get("acquisition_yyyymm"));
                mstAssetVo.setValue(mstAssetVo.getAcquisitionYyyymm());
                return mstAssetVo;
            }
        }

        if (StringUtils.isNotEmpty(mstAssetVo.getAcquisitionAmountStr())) {//取得金額
            if (!fu.isDouble(mstAssetVo.getAcquisitionAmountStr())) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_not_isnumber"));
                mstAssetVo.setField(msgValueMap.get("acquisition_amout"));
                mstAssetVo.setValue(String.valueOf(mstAssetVo.getAcquisitionAmountStr()));
                return mstAssetVo;
            }
            if (!FileUtil.isStrByDecimal(mstAssetVo.getAcquisitionAmountStr())) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_over_length"));
                mstAssetVo.setField(msgValueMap.get("acquisition_amout"));
                mstAssetVo.setValue(String.valueOf(mstAssetVo.getAcquisitionAmountStr()));
                return mstAssetVo;
            }
        }
        if (StringUtils.isNotEmpty(mstAssetVo.getMonthBookValueStr())) {// 今月簿価
            if (!fu.isDouble(mstAssetVo.getMonthBookValueStr())) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_not_isnumber"));
                mstAssetVo.setField(msgValueMap.get("month_book_value"));
                mstAssetVo.setValue(String.valueOf(mstAssetVo.getMonthBookValueStr()));
                return mstAssetVo;
            }
            if (!FileUtil.isStrByDecimal(mstAssetVo.getMonthBookValueStr())) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_over_length"));
                mstAssetVo.setField(msgValueMap.get("month_book_value"));
                mstAssetVo.setValue(String.valueOf(mstAssetVo.getAcquisitionAmountStr()));
                return mstAssetVo;
            }
        }
        if (StringUtils.isNotEmpty(mstAssetVo.getPeriodBookValueStr())) {// 期初簿価
            if (!fu.isDouble(mstAssetVo.getPeriodBookValueStr())) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_not_isnumber"));
                mstAssetVo.setField(msgValueMap.get("month_book_value"));
                mstAssetVo.setValue(String.valueOf(mstAssetVo.getPeriodBookValueStr()));
                return mstAssetVo;
            }
            if (!FileUtil.isStrByDecimal(mstAssetVo.getPeriodBookValueStr())) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_over_length"));
                mstAssetVo.setField(msgValueMap.get("month_book_value"));
                mstAssetVo.setValue(String.valueOf(mstAssetVo.getAcquisitionAmountStr()));
                return mstAssetVo;
            }
        }

        if (StringUtils.isEmpty(mstAssetVo.getMoldCount())) {// 型数
            mstAssetVo.setError(true);
            mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
            mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_not_null"));
            mstAssetVo.setField(msgValueMap.get("mold_count"));
            mstAssetVo.setValue(String.valueOf(mstAssetVo.getMoldCount()));
            return mstAssetVo;
        } else if (!FileUtil.isInteger(mstAssetVo.getMoldCount())) {
            mstAssetVo.setError(true);
            mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
            mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_num_over_zero"));
            mstAssetVo.setField(msgValueMap.get("mold_count"));
            mstAssetVo.setValue(String.valueOf(mstAssetVo.getMoldCount()));
            return mstAssetVo;
        }

        if (StringUtils.isNotEmpty(mstAssetVo.getPurchasingGroup())) {// 購買グループ
            if (fu.maxLangthCheck(mstAssetVo.getPurchasingGroup(), 100)) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_over_length"));
                mstAssetVo.setField(msgValueMap.get("purchasing_group"));
                mstAssetVo.setValue(mstAssetVo.getPurchasingGroup());
                return mstAssetVo;
            }
        }

        if (StringUtils.isNotEmpty(mstAssetVo.getCommonInformation())) {// 共通情報
            if (fu.maxLangthCheck(mstAssetVo.getCommonInformation(), 100)) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_over_length"));
                mstAssetVo.setField(msgValueMap.get("common_information"));
                mstAssetVo.setValue(mstAssetVo.getCommonInformation());
                return mstAssetVo;
            }
        }

        if (StringUtils.isNotEmpty(String.valueOf(mstAssetVo.getMoldMachineType()))) {// 金型・設備区分
            if (!fu.isNumber(String.valueOf(mstAssetVo.getAssetType()))) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_not_isnumber"));
                mstAssetVo.setField(msgValueMap.get("mold_machine_type"));
                mstAssetVo.setValue(String.valueOf(mstAssetVo.getMoldMachineType()));
                return mstAssetVo;
            }
        }

        if (StringUtils.isNotEmpty(mstAssetVo.getAssetClass())) {// 資産クラス
            if (fu.maxLangthCheck(mstAssetVo.getAssetClass(), 100)) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_over_length"));
                mstAssetVo.setField(msgValueMap.get("asset_class"));
                mstAssetVo.setValue(mstAssetVo.getAssetClass());
                return mstAssetVo;
            }
        }
        if (StringUtils.isNotEmpty(mstAssetVo.getUsingSection())) {// 使用部門
            if (fu.maxLangthCheck(mstAssetVo.getUsingSection(), 100)) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_over_length"));
                mstAssetVo.setField(msgValueMap.get("using_section"));
                mstAssetVo.setValue(mstAssetVo.getUsingSection());
                return mstAssetVo;
            }
        }
        if (StringUtils.isNotEmpty(mstAssetVo.getCostCenter())) {// 原価センタ
            if (fu.maxLangthCheck(mstAssetVo.getCostCenter(), 100)) {
                mstAssetVo.setError(true);
                mstAssetVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstAssetVo.setErrorMessage(msgValueMap.get("msg_error_over_length"));
                mstAssetVo.setField(msgValueMap.get("cost_center"));
                mstAssetVo.setValue(mstAssetVo.getCostCenter());
                return mstAssetVo;
            }
        }

        return mstAssetVo;
    }

    /**
     * 資産マスタCSV出力
     *
     * @param assetNo
     * @param assetName
     * @param moldId
     * @param machineId
     * @param mgmtCompanyCode
     * @param loginUser
     * @return
     */
    public FileReponse getAssetCsvOutPut(String assetNo, String assetName, String moldId, String machineId, String mgmtCompanyCode, LoginUser loginUser) {
        MstAssetList mstAssetList = getMstAssetList(assetNo, assetName, moldId, machineId, mgmtCompanyCode, 0, 0, null, loginUser.getLangId(), false, true, null, null, null, null);
        FileReponse fileReponse = new FileReponse();
        ArrayList lineList;
        String uuid = IDGenerator.generate();
        String langId = loginUser.getLangId();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        // ヘッダー種取得
        Map<String, String> headerMap = getDictionaryList(langId);

        /*Head*/
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();
        headList.add(headerMap.get("asset_no"));  // 資産番号
        headList.add(headerMap.get("branch_no")); // 補助番号
        headList.add(headerMap.get("asset_type"));  // 資産種類
        headList.add(headerMap.get("asset_name")); // 資産名称
        headList.add(headerMap.get("mgmt_company_code")); // 管理先コード
        headList.add(headerMap.get("mgmt_company_name")); // 管理先名称
        headList.add(headerMap.get("mgmt_location_code")); // 所在先コード
        headList.add(headerMap.get("mgmt_location_name")); // 設置場所
        headList.add(headerMap.get("vendor_code"));  // ベンダーコード
        headList.add(headerMap.get("item_code")); // 品目コード
        headList.add(headerMap.get("asset_item_name")); // 品目名称
        headList.add(headerMap.get("acquisition_type")); // 取得区分
        headList.add(headerMap.get("acquisition_date")); // 取得日
        headList.add(headerMap.get("acquisition_yyyymm")); // 取得年月
        headList.add(headerMap.get("acquisition_amount"));  //取得金額
        headList.add(headerMap.get("month_book_value")); // 今月簿価
        headList.add(headerMap.get("period_book_value")); // 期初簿価
        headList.add(headerMap.get("mold_count"));  // 型数
        headList.add(headerMap.get("purchasing_group")); // 購買グループ
        headList.add(headerMap.get("common_information")); // 共通情報
        headList.add(headerMap.get("mold_machine_type")); // 金型・設備区分
        headList.add(headerMap.get("asset_class")); // 資産クラス
        headList.add(headerMap.get("using_section")); // 使用部門
        headList.add(headerMap.get("cost_center")); // 原価センタ
        headList.add(headerMap.get("mgmt_region")); // 管理地域
        headList.add(headerMap.get("main_mold_id"));  // 代表金型ID
        headList.add(headerMap.get("main_machine_id")); // 代表設備ID
        headList.add(headerMap.get("disposal_status")); // 廃棄ステータス
        headList.add(headerMap.get("delete_record")); // 削除

        gLineList.add(headList);
        if (mstAssetList != null && mstAssetList.getMstAssetList() != null && mstAssetList.getMstAssetList().size() > 0) {
            for (int i = 0; i < mstAssetList.getMstAssetList().size(); i++) {
                MstAssetVo mstAsset = mstAssetList.getMstAssetList().get(i);
                lineList = new ArrayList();
                lineList.add(mstAsset.getAssetNo());// 資産番号
                lineList.add(mstAsset.getBranchNo());// 補助番号
                lineList.add(mstAsset.getAssetTypeText());// 資産種類
                lineList.add(mstAsset.getAssetName());// 資産名称
                if (StringUtils.isNotEmpty(mstAsset.getMgmtCompanyCode())) {// 管理先コード
                    lineList.add(mstAsset.getMgmtCompanyCode());
                } else {
                    lineList.add("");
                }
                if (StringUtils.isNotEmpty(mstAsset.getMgmtCompanyName())) {// 管理先名称
                    lineList.add(mstAsset.getMgmtCompanyName());
                } else {
                    lineList.add("");
                }

                if (StringUtils.isNotEmpty(mstAsset.getMgmtLocationCode())) {// 所在先コード
                    lineList.add(mstAsset.getMgmtLocationCode());
                } else {
                    lineList.add("");
                }
                if (StringUtils.isNotEmpty(mstAsset.getMgmtLocationName())) {// 設置場所
                    lineList.add(mstAsset.getMgmtLocationName());
                } else {
                    lineList.add("");
                }

                if (StringUtils.isNotEmpty(mstAsset.getVendorCode())) {// ベンダーコード
                    lineList.add(mstAsset.getVendorCode());
                } else {
                    lineList.add("");
                }

                if (StringUtils.isNotEmpty(mstAsset.getItemCode())) {// 品目コード
                    lineList.add(mstAsset.getItemCode());
                } else {
                    lineList.add("");
                }
                if (StringUtils.isNotEmpty(mstAsset.getItemName())) {// 品目名称
                    lineList.add(mstAsset.getItemName());
                } else {
                    lineList.add("");
                }

                lineList.add(mstAsset.getAcquisitionTypeText());// 取得区分

                if (StringUtils.isNotEmpty(mstAsset.getAcquisitionDate())) {// 取得日
                    lineList.add(mstAsset.getAcquisitionDate());
                } else {
                    lineList.add("");
                }
                if (StringUtils.isNotEmpty(mstAsset.getAcquisitionYyyymm())) {// 取得年月
                    lineList.add(mstAsset.getAcquisitionYyyymm());
                } else {
                    lineList.add("");
                }

                lineList.add(mstAsset.getAcquisitionAmountStr());//取得金額
                lineList.add(mstAsset.getMonthBookValueStr());// 今月簿価
                lineList.add(mstAsset.getPeriodBookValueStr());// 期初簿価
                lineList.add(String.valueOf(mstAsset.getMoldCount()));// 型数

                if (StringUtils.isNotEmpty(mstAsset.getPurchasingGroup())) {// 購買グループ
                    lineList.add(mstAsset.getPurchasingGroup());
                } else {
                    lineList.add("");
                }

                if (StringUtils.isNotEmpty(mstAsset.getCommonInformation())) {// 共通情報
                    lineList.add(mstAsset.getCommonInformation());
                } else {
                    lineList.add("");
                }

                lineList.add(mstAsset.getMoldMachineTypeText());// 金型・設備区分

                if (StringUtils.isNotEmpty(mstAsset.getAssetClass())) {// 資産クラス
                    lineList.add(mstAsset.getAssetClass());
                } else {
                    lineList.add("");
                }

                if (StringUtils.isNotEmpty(mstAsset.getUsingSection())) {// 使用部門
                    lineList.add(mstAsset.getUsingSection());
                } else {
                    lineList.add("");
                }

                if (StringUtils.isNotEmpty(mstAsset.getCostCenter())) {// 原価センタ
                    lineList.add(mstAsset.getCostCenter());
                } else {
                    lineList.add("");
                }

                lineList.add(mstAsset.getMgmtRegionStr());// 管理地域

                if (mstAsset.getTblMoldMachineAssetRelationVos() != null && mstAsset.getTblMoldMachineAssetRelationVos().size() > 0) {
                    String mailMoldId = mstAsset.getTblMoldMachineAssetRelationVos().get(0).getMoldId();
                    if (StringUtils.isNotEmpty(mailMoldId)) {// 代表金型ID
                        lineList.add(mailMoldId);
                    } else {
                        lineList.add("");
                    }

                    String mailMachineId = mstAsset.getTblMoldMachineAssetRelationVos().get(0).getMachineId();
                    if (StringUtils.isNotEmpty(mailMachineId)) {// 代表設備ID
                        lineList.add(mailMachineId);
                    } else {
                        lineList.add("");
                    }

                } else {
                    lineList.add("");
                    lineList.add("");
                }

                lineList.add(mstAsset.getDisposalStatusText());//廃棄ステータス
                lineList.add("");
                gLineList.add(lineList);
            }
        }

        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        MstFunction functionId = new MstFunction();
        functionId.setId(CommonConstants.FUN_ID_MST_ASSET_MAINTENANCE);
        tblCsvExport.setFunctionId(functionId);
        tblCsvExport.setExportTable("mst_asset");
        tblCsvExport.setExportDate(new Date());
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, "mst_asset");
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fileReponse.setFileUuid(uuid);

        return fileReponse;
    }

    /**
     *
     * @param langId
     * @return
     */
    private Map<String, String> getDictionaryList(String langId) {
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList("row_number", "disposal_status_normal", "asset_no", "branch_no", "asset_type", "asset_name", "mgmt_company_code",
                "mgmt_company_name", "mgmt_location_code", "mgmt_location_name", "vendor_code", "item_code", "asset_item_name", "msg_record_deleted", "msg_cannot_delete_used_record",
                "acquisition_type", "acquisition_date", "acquisition_amount", "month_book_value", "acquisition_yyyymm", "period_book_value", "msg_error_num_over_zero",
                "mold_count", "purchasing_group", "common_information", "mold_machine_type", "main_mold_id", "main_machine_id", "error", "error_detail", "db_process", "delete_record",
                "msg_record_added", "msg_error_wrong_csv_layout", "mold", "machine", "asset_class", "using_section", "cost_center", "mgmt_region", "msg_error_mold_machine_asset_relation_not_null",
                "msg_error_over_length", "mst_error_record_not_found", "msg_cannot_delete_used_record", "msg_error_record_exists", "msg_error_mold_machine_asset_relation_main_flg",
                "msg_data_modified", "msg_error_not_isnumber", "msg_error_not_null", "msg_error_date_format_invalid", "msg_error_value_invalid", "disposal_status");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        return headerMap;
    }

    /**
     * 資産マスタCSV取込
     *
     * @param fileUuid
     * @param loginUser
     * @param currencyCode
     * @return
     */
    @Transactional
    public ImportResultResponse postAssetCsv(String fileUuid, LoginUser loginUser, String currencyCode) {
        ImportResultResponse importResultResponse = new ImportResultResponse();
        //①CSVファイルを取込み
        long succeededCount;
        long addedCount = 0;
        long updatedCount = 0;
        long deletedCount = 0;
        long failedCount = 0;

        String logFileUuid = IDGenerator.generate();
        String csvFile = FileUtil.getCsvFilePath(kartePropertyService, fileUuid);
        if (!csvFile.endsWith(CommonConstants.CSV)) {
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_wrong_csv_layout");
            importResultResponse.setErrorMessage(msg);
            return importResultResponse;
        }

        ArrayList readList = CSVFileUtil.readCsv(csvFile);
        String userLangId = loginUser.getLangId();
        Map<String, String> headerMap = getDictionaryList(userLangId);
        String lineNo = headerMap.get("row_number");
        String assetNo = headerMap.get("asset_no");// 資産番号
        String branchNo = headerMap.get("branch_no");// 補助番号
        String assentBranchNo = assetNo + "," + branchNo;// 資産\補助番号
        String assetType = headerMap.get("asset_type");// 資産種類
        String assetName = headerMap.get("asset_name");// 資産名称
        String mgmtCompanyCode = headerMap.get("mgmt_company_code");// 管理先コード
        String mgmtCompanyName = headerMap.get("mgmt_company_name");// 管理先名称
        String locationCode = headerMap.get("mgmt_location_code");// 所在先コード
        String locationName = headerMap.get("mgmt_location_name");// 所在先名称
        String vendorCode = headerMap.get("vendor_code");// ベンダーコード
        String itemCode = headerMap.get("item_code");// 品目コード
        String itemName = headerMap.get("item_name");// 品目名称
        String acquisitionType = headerMap.get("acquisition_type");// 取得区分
        String acquisitionYyyymm = headerMap.get("acquisition_yyyymm");// 取得年月
        String acquisitionAmount = headerMap.get("acquisition_amount");//取得金額
        String monthBookValue = headerMap.get("month_book_value");// 今月簿価
        String periodBookValue = headerMap.get("period_book_value");// 期初簿価
        String typeCount = headerMap.get("mold_count");// 型数
        String moldMachineType = headerMap.get("mold_machine_type");// 金型・設備区分
        String purchasingGroup = headerMap.get("purchasing_group");// 購買グループ
        String commonInformation = headerMap.get("common_information");// 共通情報
        String assetClass = headerMap.get("asset_class");//資産クラス
        String usingSection = headerMap.get("using_section");//使用部門
        String costCenter = headerMap.get("cost_center");//原価センタ
        String mgmtRegion = headerMap.get("mgmt_region");//管理地域
        String error = headerMap.get("error");
        String errorDetail = headerMap.get("error_detail");
        String result = headerMap.get("db_process");
        String addedMsg = headerMap.get("msg_record_added");
        String updatedMsg = headerMap.get("msg_data_modified");
        String deletedMsg = headerMap.get("msg_record_deleted");
        String cannotDeletedMsg = headerMap.get("msg_cannot_delete_used_record");
        String notFound = headerMap.get("mst_error_record_not_found");
        String maxLangth = headerMap.get("msg_error_over_length");
        String layout = headerMap.get("msg_error_wrong_csv_layout");
        String notnumber = headerMap.get("msg_error_not_isnumber");
        String nullMsg = headerMap.get("msg_error_not_null");
        String dataCheck = headerMap.get("msg_error_date_format_invalid");
        String numZero = headerMap.get("msg_error_num_over_zero");
        String moldMachineAssetNotNull = headerMap.get("msg_error_mold_machine_asset_relation_not_null");
        String disposalStatusNormal = headerMap.get("disposal_status_normal");

        if (readList.size() <= 1) {
            return importResultResponse;
        } else {
            String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
            Map<String, Integer> assetTypeChoice = getChoiceByKey(loginUser.getLangId(), "mst_asset.asset_type");
            Map<String, Integer> acquisitionTypeChioce = getChoiceByKey(loginUser.getLangId(), "mst_asset.acquisition_type");
            Map<String, Integer> mgmtRegionChioce = getChoiceByKey(loginUser.getLangId(), "mst_asset.mgmt_region");

            FileUtil fu = new FileUtil();
            for (int i = 1; i < readList.size(); i++) {
                ArrayList comList = (ArrayList) readList.get(i);
                if (comList.size() != 29) {
                    //エラー情報をログファイルに記入
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assentBranchNo, "", error, 1, errorDetail, layout));
                    failedCount = failedCount + 1;
                    continue;
                }

                String strAssetNo = String.valueOf(comList.get(0));//資産番号
                String strBranchNo = String.valueOf(comList.get(1));//補助番号
                String strAssentBranchNo = strAssetNo + "," + strBranchNo;
                String strAssetType = String.valueOf(comList.get(2));//資産種類
                String strAssetName = String.valueOf(comList.get(3));//補助名称
                String strMgmtCompanyCode = String.valueOf(comList.get(4));//管理先コード
                String strMgmtCompanyName = String.valueOf(comList.get(5));//管理先名称
                String strMgmtLocationCode = String.valueOf(comList.get(6));//所在先コード
                String strMgmtLocationName = String.valueOf(comList.get(7));//所在先名称
                String strVendorCode = String.valueOf(comList.get(8));//ベンダーコード
                String strItemCode = String.valueOf(comList.get(9));//品目コード
                String strItemName = String.valueOf(comList.get(10));//品目コ名称
                String strAcquisitionType = String.valueOf(comList.get(11));//取得区分
                String strAcquisitionDate = String.valueOf(comList.get(12));//取得日
                if (StringUtils.isNotEmpty(strAcquisitionDate)) {
                    strAcquisitionDate = DateFormat.formatDateYear(strAcquisitionDate, DateFormat.DATE_FORMAT);
                }
                String strAcquisitionDateYyyymm = String.valueOf(comList.get(13));//取得年月
                String strAcquisitionAmount = String.valueOf(comList.get(14));//取得金額
                String strMonthBookValue = String.valueOf(comList.get(15));//今月簿価
                String strPeriodBookValue = String.valueOf(comList.get(16));//期初簿価
                String strMoldCount = String.valueOf(comList.get(17));//型数
                String strPurchasingGroup = String.valueOf(comList.get(18));//購買グループ
                String strCommonInformation = String.valueOf(comList.get(19));//共通情報
                String strMoldMachineType = String.valueOf(comList.get(20));//金型・設備区分
                String strAssetClass = String.valueOf(comList.get(21));//資産クラス
                String strUsingSection = String.valueOf(comList.get(22));//使用部門
                String strCostCenter = String.valueOf(comList.get(23));//原価センタ
                String strMgmtRegion = String.valueOf(comList.get(24));//管理地域

                String disposalStatusText = String.valueOf(comList.get(27));//DISPOSAL_STATUS

                MstAssetVo newMstAssetVo = new MstAssetVo();
                newMstAssetVo.setCurrencyCode(currencyCode);
                if (StringUtils.isEmpty(strAssetNo)) {// 資産番号
                    failedCount = failedCount + 1;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assetNo, strAssetNo, error, 1, errorDetail, nullMsg));
                    continue;
                } else if (fu.maxLangthCheck(strAssetNo, 45)) {
                    failedCount = failedCount + 1;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assetNo, strAssetNo, error, 1, errorDetail, maxLangth));
                    continue;
                }
                if (StringUtils.isEmpty(strBranchNo)) {// 補助番号
                    failedCount = failedCount + 1;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, branchNo, strBranchNo, error, 1, errorDetail, nullMsg));
                    continue;
                } else if (fu.maxLangthCheck(strBranchNo, 45)) {
                    failedCount = failedCount + 1;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, branchNo, strBranchNo, error, 1, errorDetail, maxLangth));
                    continue;
                }
                newMstAssetVo.setAssetNo(strAssetNo);
                newMstAssetVo.setBranchNo(strBranchNo);

                BasicResponse basicResponse;
                MstAssetPK mstAssetPK = new MstAssetPK();
                mstAssetPK.setAssetNo(newMstAssetVo.getAssetNo());
                mstAssetPK.setBranchNo(newMstAssetVo.getBranchNo());
                MstAsset oldMstAsset = entityManager.find(MstAsset.class, mstAssetPK);

                String strDelFlag = String.valueOf(comList.get(28));
                if ("1".equals(strDelFlag)) {
                    if (oldMstAsset != null) {
                        //削除
                        basicResponse = deleteMstAsset(oldMstAsset.getUuid(), loginUser);
                        if (basicResponse.isError()) {
                            //エラー情報をログファイルに記入
                            failedCount = failedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assentBranchNo, strAssentBranchNo, error, 1, errorDetail, cannotDeletedMsg));
                            continue;
                        } else {
                            //エラー情報をログファイルに記入
                            deletedCount = deletedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assentBranchNo, strAssentBranchNo, error, 0, errorDetail, deletedMsg));
                            continue;
                        }
                    } else {
                        //エラー情報をログファイルに記入
                        failedCount = failedCount + 1;
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assentBranchNo, strAssentBranchNo, error, 1, errorDetail, cannotDeletedMsg));
                        continue;
                    }
                }

                if (StringUtils.isNotEmpty(strAssetType)) {//資産種類
                    Object strAssetTypeValue = "0";
                    if (null == (strAssetTypeValue = assetTypeChoice.get(strAssetType))) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assetType, strAssetType, error, 1, errorDetail, notFound));
                        continue;
                    } else if (fu.isNullCheck(strAssetTypeValue.toString())) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assetType, strAssetType, error, 1, errorDetail, notFound));
                        continue;
                    }
                    newMstAssetVo.setAssetType(assetTypeChoice.get(strAssetType));
                } else {
                    newMstAssetVo.setAssetType(0);
                }

                if (StringUtils.isNotEmpty(strAssetName)) {//資産名称
                    if (fu.maxLangthCheck(strAssetName, 100)) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assetName, strAssetName, error, 1, errorDetail, maxLangth));
                        continue;
                    }
                }
                newMstAssetVo.setAssetName(strAssetName);

                if (StringUtils.isEmpty(strMgmtCompanyCode)) {//管理先コード
                    failedCount = failedCount + 1;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assentBranchNo, strAssentBranchNo, error, 1, errorDetail, nullMsg));
                    continue;
                } else if (StringUtils.isNotEmpty(strMgmtCompanyCode)) {
                    if (fu.maxLangthCheck(strMgmtCompanyCode, 100)) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, mgmtCompanyCode, strMgmtCompanyCode, error, 1, errorDetail, maxLangth));
                        continue;
                    }
                }
                newMstAssetVo.setMgmtCompanyCode(strMgmtCompanyCode);
                if (StringUtils.isEmpty(strMgmtCompanyName)) {//管理先名称
                    failedCount = failedCount + 1;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assentBranchNo, strAssentBranchNo, error, 1, errorDetail, nullMsg));
                    continue;
                } else if (StringUtils.isNotEmpty(strMgmtCompanyName)) {
                    if (fu.maxLangthCheck(strMgmtCompanyName, 100)) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, mgmtCompanyName, strMgmtCompanyName, error, 1, errorDetail, maxLangth));
                        continue;
                    }
                }
                newMstAssetVo.setMgmtCompanyName(strMgmtCompanyName);

                if (fu.maxLangthCheck(strMgmtLocationCode, 100)) {//所在先コード
                    failedCount = failedCount + 1;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, locationCode, strMgmtLocationCode, error, 1, errorDetail, maxLangth));
                    continue;
                }
                newMstAssetVo.setMgmtLocationCode(strMgmtLocationCode);
                if (fu.maxLangthCheck(strMgmtLocationName, 100)) {//所在名称
                    failedCount = failedCount + 1;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, locationName, strMgmtLocationName, error, 1, errorDetail, maxLangth));
                    continue;
                }
                newMstAssetVo.setMgmtLocationName(strMgmtLocationName);

                if (StringUtils.isNotEmpty(strMgmtLocationCode) && StringUtils.isEmpty(strMgmtLocationName)) {
                    failedCount = failedCount + 1;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assentBranchNo, strAssentBranchNo, error, 1, errorDetail, nullMsg));
                    continue;
                }

                if (fu.maxLangthCheck(strVendorCode, 100)) {//ベンダーコード
                    failedCount = failedCount + 1;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, vendorCode, strVendorCode, error, 1, errorDetail, maxLangth));
                    continue;
                }
                newMstAssetVo.setVendorCode(strVendorCode);

                if (fu.maxLangthCheck(strItemCode, 100)) {//品目コード
                    failedCount = failedCount + 1;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, itemCode, strItemCode, error, 1, errorDetail, maxLangth));
                    continue;
                }
                newMstAssetVo.setItemCode(strItemCode);

                if (fu.maxLangthCheck(strItemName, 100)) {//品目名称
                    failedCount = failedCount + 1;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, itemName, strItemName, error, 1, errorDetail, maxLangth));
                    continue;
                }
                newMstAssetVo.setItemName(strItemName);

                if (StringUtils.isNotEmpty(strItemCode) && StringUtils.isEmpty(strItemName)) {
                    failedCount = failedCount + 1;
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assentBranchNo, strAssentBranchNo, error, 1, errorDetail, nullMsg));
                    continue;
                }

                if (StringUtils.isNotEmpty(strAcquisitionType)) {//取得区分
                    Object strAcquisitionTypeValue = acquisitionTypeChioce.get(strAcquisitionType);
                    if (null == strAcquisitionTypeValue) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, acquisitionType, strAcquisitionType, error, 1, errorDetail, notFound));
                        continue;
                    } else if (fu.isNullCheck(strAcquisitionTypeValue.toString())) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, acquisitionType, strAcquisitionType, error, 1, errorDetail, notFound));
                        continue;
                    }
                    newMstAssetVo.setAcquisitionType(acquisitionTypeChioce.get(strAcquisitionType));
                } else {
                    newMstAssetVo.setAcquisitionType(0);
                }

                if (StringUtils.isNotEmpty(strAcquisitionDate)) {//取得日
                    if (!fu.dateCheck(strAcquisitionDate)) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assentBranchNo, strAssentBranchNo, error, 1, errorDetail, dataCheck));
                        continue;
                    }
                    newMstAssetVo.setAcquisitionDate(strAcquisitionDate);
                } else {
                    newMstAssetVo.setAcquisitionDate(null);
                }
                if (StringUtils.isNotEmpty(strAcquisitionDateYyyymm)) {//取得年月
                    if (fu.maxLangthCheck(strAcquisitionDateYyyymm, 6)) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, acquisitionYyyymm, strAcquisitionDateYyyymm, error, 1, errorDetail, maxLangth));
                        continue;
                    }
                    newMstAssetVo.setAcquisitionYyyymm(strAcquisitionDateYyyymm);
                } else {
                    newMstAssetVo.setAcquisitionYyyymm(null);
                }

                if (StringUtils.isNotEmpty(strAcquisitionAmount)) {//取得金额
                    if (!fu.isDouble(strAcquisitionAmount)) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, acquisitionAmount, strAcquisitionAmount, error, 1, errorDetail, notnumber));
                        continue;
                    }
                    if (!FileUtil.isStrByDecimal(strAcquisitionAmount)) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, acquisitionAmount, strAcquisitionAmount, error, 1, errorDetail, maxLangth));
                        continue;
                    }
                }
                newMstAssetVo.setAcquisitionAmountStr(strAcquisitionAmount);

                if (StringUtils.isNotEmpty(strMonthBookValue)) {//今月簿価
                    if (!fu.isDouble(strMonthBookValue)) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, monthBookValue, strMonthBookValue, error, 1, errorDetail, notnumber));
                        continue;
                    }
                    if (!FileUtil.isStrByDecimal(strMonthBookValue)) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, monthBookValue, strMonthBookValue, error, 1, errorDetail, maxLangth));
                        continue;
                    }
                }
                newMstAssetVo.setMonthBookValueStr(strMonthBookValue);

                if (StringUtils.isNotEmpty(strPeriodBookValue)) {//期初簿価
                    if (!fu.isDouble(strPeriodBookValue)) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, periodBookValue, strPeriodBookValue, error, 1, errorDetail, notnumber));
                        continue;
                    }
                    if (!FileUtil.isStrByDecimal(strPeriodBookValue)) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, periodBookValue, strPeriodBookValue, error, 1, errorDetail, maxLangth));
                        continue;
                    }
                }
                newMstAssetVo.setPeriodBookValueStr(strPeriodBookValue);

                if (StringUtils.isNotEmpty(strMoldCount)) {//型数
                    if (!FileUtil.isInteger(strMoldCount)) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, typeCount, strMoldCount, error, 1, errorDetail, numZero));
                        continue;
                    }
                    newMstAssetVo.setMoldCount(strMoldCount);
                } else {
                    newMstAssetVo.setMoldCount("0");
                }

                if (StringUtils.isNotEmpty(strPurchasingGroup)) {// 購買グループ
                    if (fu.maxLangthCheck(strPurchasingGroup, 100)) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, purchasingGroup, strPurchasingGroup, error, 1, errorDetail, maxLangth));
                        continue;
                    }
                }
                newMstAssetVo.setPurchasingGroup(strPurchasingGroup);

                if (StringUtils.isNotEmpty(strCommonInformation)) {// 共通情報
                    if (fu.maxLangthCheck(strCommonInformation, 100)) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, commonInformation, strCommonInformation, error, 1, errorDetail, maxLangth));
                        continue;
                    }
                }
                newMstAssetVo.setCommonInformation(strCommonInformation);

                if (StringUtils.isNotEmpty(strAssetClass)) {//資産クラス
                    if (fu.maxLangthCheck(strAssetClass, 100)) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assetClass, strAssetClass, error, 1, errorDetail, maxLangth));
                        continue;
                    }
                }
                newMstAssetVo.setAssetClass(strAssetClass);

                if (StringUtils.isNotEmpty(strUsingSection)) {//使用部門
                    if (fu.maxLangthCheck(strUsingSection, 100)) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, usingSection, strUsingSection, error, 1, errorDetail, maxLangth));
                        continue;
                    }
                }
                newMstAssetVo.setUsingSection(strUsingSection);

                if (StringUtils.isNotEmpty(strCostCenter)) {//原価センタ
                    if (fu.maxLangthCheck(strCostCenter, 100)) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, costCenter, strCostCenter, error, 1, errorDetail, maxLangth));
                        continue;
                    }
                }
                newMstAssetVo.setCostCenter(strCostCenter);
                if (StringUtils.isNotEmpty(strMgmtRegion)) {//管理地域
                    Object strMgmtRegionValue = mgmtRegionChioce.get(strMgmtRegion);
                    if (null == strMgmtRegionValue) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, mgmtRegion, strMgmtRegion, error, 1, errorDetail, notFound));
                        continue;
                    } else if (fu.isNullCheck(strMgmtRegionValue.toString())) {
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, mgmtRegion, strMgmtRegion, error, 1, errorDetail, notFound));
                        continue;
                    }
                    newMstAssetVo.setMgmtRegion(mgmtRegionChioce.get(strMgmtRegion));
                } else {
                    newMstAssetVo.setMgmtRegion(0);
                }
                boolean existFlag;
                if (headerMap.get("mold").equals(strMoldMachineType)) {
                    newMstAssetVo.setMoldMachineType(1);
                } else if (headerMap.get("machine").equals(strMoldMachineType)) {
                    newMstAssetVo.setMoldMachineType(2);
                } else {
                    newMstAssetVo.setMoldMachineType(0);
                }

                if (oldMstAsset != null) {
                    TblMoldMachineAssetRelationVo tblMoldMachineAssetRelationVo = new TblMoldMachineAssetRelationVo();
                    List<TblMoldMachineAssetRelationVo> tblMoldMachineAssetRelationVos = new ArrayList();
                    Query query = entityManager.createNamedQuery("TblMoldMachineAssetRelation.findByAssetId");
                    query.setParameter("assetId", oldMstAsset.getUuid());
                    List<TblMoldMachineAssetRelation> oldTblMoldMachineAssetRelation = query.getResultList();
                    MstMold mstMold = entityManager.find(MstMold.class, String.valueOf(comList.get(25)));
                    MstMachine mstMachine = entityManager.find(MstMachine.class, String.valueOf(comList.get(26)));
//                    if ((newMstAssetVo.getMoldMachineType() == 1 && mstMold == null) || (newMstAssetVo.getMoldMachineType() == 2 && mstMachine == null)) {
//                        failedCount = failedCount + 1;
//                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, moldMachineType, strMoldMachineType, error, 1, errorDetail, moldMachineAssetNotNull));
//                        continue;
//                    }
                    if (oldTblMoldMachineAssetRelation != null && oldTblMoldMachineAssetRelation.size() > 0) {
                        tblMoldMachineAssetRelationVo.setMainFlg(0);
                        tblMoldMachineAssetRelationVo.setAssetId(oldMstAsset.getUuid());
                        tblMoldMachineAssetRelationVo.setUpdateDate(new Date());
                        tblMoldMachineAssetRelationVo.setUpdateUserUuid(loginUser.getUserUuid());
                        tblMoldMachineAssetRelationService.updateTblMoldMachineAssetRelationMainFlg(tblMoldMachineAssetRelationVo);
                        entityManager.flush();
                        entityManager.clear();

                        for (int q = 0; q < oldTblMoldMachineAssetRelation.size(); q++) {
                            TblMoldMachineAssetRelation tblMoldMachineAssetRelation = oldTblMoldMachineAssetRelation.get(q);
                            if ((mstMold != null && StringUtils.isNotEmpty(tblMoldMachineAssetRelation.getMoldUuid()) && tblMoldMachineAssetRelation.getMoldUuid().equals(mstMold.getUuid()))
                                    || (mstMachine != null && StringUtils.isNotEmpty(tblMoldMachineAssetRelation.getMachineUuid()) && tblMoldMachineAssetRelation.getMachineUuid().equals(mstMachine.getUuid()))) {
                                tblMoldMachineAssetRelationVo.setUuid(tblMoldMachineAssetRelation.getUuid());
                            }
                        }

                        if (mstMold != null && newMstAssetVo.getMoldMachineType() == 1) {
                            existFlag = tblMoldMachineAssetRelationService.getTblMoldMachineAssetRelationMoldUuidExist(oldMstAsset.getUuid(), mstMold.getUuid());
                            tblMoldMachineAssetRelationVo.setMoldUuid(mstMold.getUuid());
                            tblMoldMachineAssetRelationVo.setMoldId(String.valueOf(comList.get(25)));
                            tblMoldMachineAssetRelationVo.setMainFlg(1);
                            if (existFlag) {
                                tblMoldMachineAssetRelationVo.setOperationFlag(CommonConstants.OPERATION_FLAG_UPDATE);
                            } else {
                                tblMoldMachineAssetRelationVo.setOperationFlag(CommonConstants.OPERATION_FLAG_CREATE);
                            }
                            tblMoldMachineAssetRelationVos.add(tblMoldMachineAssetRelationVo);
                        } else if (mstMachine != null && newMstAssetVo.getMoldMachineType() == 2) {
                            existFlag = tblMoldMachineAssetRelationService.getTblMoldMachineAssetRelationMachineUuidExist(oldMstAsset.getUuid(), mstMachine.getUuid());
                            tblMoldMachineAssetRelationVo.setMachineUuid(mstMachine.getUuid());
                            tblMoldMachineAssetRelationVo.setMachineId(String.valueOf(comList.get(26)));
                            tblMoldMachineAssetRelationVo.setMainFlg(1);
                            if (existFlag) {
                                tblMoldMachineAssetRelationVo.setOperationFlag(CommonConstants.OPERATION_FLAG_UPDATE);
                            } else {
                                tblMoldMachineAssetRelationVo.setOperationFlag(CommonConstants.OPERATION_FLAG_CREATE);
                            }
                            tblMoldMachineAssetRelationVos.add(tblMoldMachineAssetRelationVo);
                        }

                    } else if (mstMold != null && newMstAssetVo.getMoldMachineType() == 1) {
                        tblMoldMachineAssetRelationVo.setMoldUuid(mstMold.getUuid());
                        tblMoldMachineAssetRelationVo.setMoldId(String.valueOf(comList.get(25)));
                        tblMoldMachineAssetRelationVo.setMainFlg(1);
                        tblMoldMachineAssetRelationVo.setOperationFlag(CommonConstants.OPERATION_FLAG_CREATE);
                        tblMoldMachineAssetRelationVos.add(tblMoldMachineAssetRelationVo);
                    } else if (mstMachine != null && newMstAssetVo.getMoldMachineType() == 2) {
                        tblMoldMachineAssetRelationVo.setMachineUuid(mstMachine.getUuid());
                        tblMoldMachineAssetRelationVo.setMachineId(String.valueOf(comList.get(26)));
                        tblMoldMachineAssetRelationVo.setMainFlg(1);
                        tblMoldMachineAssetRelationVo.setOperationFlag(CommonConstants.OPERATION_FLAG_CREATE);
                        tblMoldMachineAssetRelationVos.add(tblMoldMachineAssetRelationVo);
                    }
                    newMstAssetVo.setTblMoldMachineAssetRelationVos(tblMoldMachineAssetRelationVos);
                    newMstAssetVo.setUuid(oldMstAsset.getUuid());

                    if (StringUtils.isEmpty(disposalStatusText) || disposalStatusNormal.equals(disposalStatusText)) {
                        newMstAssetVo.setDisposalStatus(0);//未廃棄にする
                    } else {
                        newMstAssetVo.setDisposalStatus(oldMstAsset.getDisposalStatus());
                    }

                    //更新
                    newMstAssetVo = updateMstAsset(newMstAssetVo, headerMap, loginUser, true);

                    if (newMstAssetVo.isError()) {
                        //エラー情報をログファイルに記入
                        failedCount = failedCount + 1;
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, newMstAssetVo.getField(), newMstAssetVo.getValue(), error, 1, errorDetail, updatedMsg));

                    } else {
                        updatedCount = updatedCount + 1;
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assentBranchNo, strAssentBranchNo, error, 0, result, updatedMsg));
                    }
                } else {

                    if (StringUtils.isEmpty(disposalStatusText) || disposalStatusNormal.equals(disposalStatusText)) {
                        newMstAssetVo.setDisposalStatus(0);//未廃棄にする
                    } else {
                        newMstAssetVo.setDisposalStatus(0);
                    }
                    
                    List<TblMoldMachineAssetRelationVo> tblMoldMachineAssetRelationVos = new ArrayList();
                    TblMoldMachineAssetRelationVo tblMoldMachineAssetRelationVo = new TblMoldMachineAssetRelationVo();
                    if (1 == newMstAssetVo.getMoldMachineType() && StringUtils.isNotEmpty(String.valueOf(comList.get(25)))) {
                        MstMold mstMold = entityManager.find(MstMold.class, String.valueOf(comList.get(25)));
                        if (mstMold != null) {
                            tblMoldMachineAssetRelationVo.setMoldUuid(mstMold.getUuid());
                            tblMoldMachineAssetRelationVo.setMoldId(String.valueOf(comList.get(25)));
                            tblMoldMachineAssetRelationVo.setMainFlg(1);
                            tblMoldMachineAssetRelationVo.setOperationFlag(CommonConstants.OPERATION_FLAG_CREATE);
                            tblMoldMachineAssetRelationVos.add(tblMoldMachineAssetRelationVo);
                            newMstAssetVo.setTblMoldMachineAssetRelationVos(tblMoldMachineAssetRelationVos);
                        }
                    }

                    if (2 == newMstAssetVo.getMoldMachineType() && StringUtils.isNotEmpty(String.valueOf(comList.get(26)))) {
                        MstMachine mstMachine = entityManager.find(MstMachine.class, String.valueOf(comList.get(26)));
                        if (mstMachine != null) {
                            tblMoldMachineAssetRelationVo.setMachineUuid(mstMachine.getUuid());
                            tblMoldMachineAssetRelationVo.setMachineId(String.valueOf(comList.get(26)));
                            tblMoldMachineAssetRelationVo.setMainFlg(1);
                            tblMoldMachineAssetRelationVo.setOperationFlag(CommonConstants.OPERATION_FLAG_CREATE);
                            tblMoldMachineAssetRelationVos.add(tblMoldMachineAssetRelationVo);
                            newMstAssetVo.setTblMoldMachineAssetRelationVos(tblMoldMachineAssetRelationVos);
                        }
                    }
                    
                    //追加
                    newMstAssetVo = createMstAsset(newMstAssetVo, headerMap, loginUser, true);
                    if (newMstAssetVo.isError()) {
                        failedCount = failedCount + 1;
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, newMstAssetVo.getField(), newMstAssetVo.getValue(), error, 1, errorDetail, addedMsg));

                    } else {
                        addedCount = addedCount + 1;
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assentBranchNo, strAssentBranchNo, error, 0, result, addedMsg));
                    }
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
            tblCsvImport.setImportTable("mst_asset");
            TblUploadFile tblUploadFile = new TblUploadFile();
            tblUploadFile.setFileUuid(fileUuid);
            tblCsvImport.setUploadFileUuid(tblUploadFile);
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId(CommonConstants.FUN_ID_MST_ASSET_MAINTENANCE);
            tblCsvImport.setFunctionId(mstFunction);
            tblCsvImport.setRecordCount(readList.size() - 1);
            tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
            tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
            tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
            tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
            tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
            tblCsvImport.setLogFileUuid(logFileUuid);
            String langId = loginUser.getLangId();
            String fileName = mstDictionaryService.getDictionaryValue(langId, "mst_asset");
            tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

            tblCsvImportService.createCsvImpor(tblCsvImport);

            return importResultResponse;
        }
    }

    /**
     *
     * @param langId
     * @param key
     * @return
     */
    public Map<String, Integer> getChoiceByKey(String langId, String key) {

        Map<String, Integer> choiceMap = new HashMap<>();
        MstChoiceList mstChoiceList = mstChoiceService.getChoice(langId, key);
        for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
            MstChoice mstChoice = mstChoiceList.getMstChoice().get(i);
            choiceMap.put(mstChoice.getChoice(), Integer.parseInt(mstChoice.getMstChoicePK().getSeq()));
        }
        return choiceMap;
    }

    /**
     * 資産情報を取得
     *
     * @param assetNo
     * @param branchNo
     * @param itemCode
     * @return
     */
    public MstAsset getSingleAssetByPkAndItemCode(String assetNo, String branchNo, String itemCode) {
        Query query = entityManager.createNamedQuery("MstAsset.findByPKAndItemCode");
        query.setParameter("assetNo", assetNo);
        query.setParameter("branchNo", branchNo);
        query.setParameter("itemCode", itemCode);
        try {

            return (MstAsset) query.getSingleResult();

        } catch (NoResultException e) {

            return null;
        }
    }

    /**
     * 資産クラスを取得
     * <P>
     * KM-336 棚卸実施登録で抽出条件追加
     *
     * @return
     */
    public MstAssetList getMstAssetClassList() {
        
        Map<String, Integer> map = new HashMap();
        try {
            Query query1 = entityManager.createQuery("SELECT t FROM TblInventory t ORDER BY t.createDate DESC ");
            query1.setMaxResults(1);
            TblInventory tblInventory = (TblInventory) query1.getSingleResult();
            if (tblInventory != null) {
                StringBuilder sql = new StringBuilder(" SELECT assetCLassCond FROM TblInventoryAssetClassCond assetCLassCond ");
                sql.append(" WHERE assetCLassCond.tblInventoryAssetClassCondPK.inventoryId = :inventoryId ");
                Query query2 = entityManager.createQuery(sql.toString());
                query2.setParameter("inventoryId", tblInventory.getUuid());
                List list1 = query2.getResultList();
                for (Object obj : list1) {
                    TblInventoryAssetClassCond tblInventoryAssetClassCond = (TblInventoryAssetClassCond) obj;
                    map.put(tblInventoryAssetClassCond.getTblInventoryAssetClassCondPK().getAssetClass(), 1);
                }
            }
        } catch (NoResultException e) {
            //nothing
        }
        
        Query query = entityManager.createNamedQuery("MstAsset.findAssetClass");
        List list = query.getResultList();
        MstAssetVo mstAssetVo;

        for (Object obj : list) {
            if (map.get(String.valueOf(obj)) == null) {
                map.put(String.valueOf(obj), 0);
            }
        }
        
        List<MstAssetVo> mstAssetVos = new ArrayList();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            mstAssetVo = new MstAssetVo();
            mstAssetVo.setAssetClass(entry.getKey());
            mstAssetVo.setChecked(entry.getValue());
            mstAssetVos.add(mstAssetVo);
        }
        
        MstAssetList mstAssetList = new MstAssetList();
        mstAssetList.setMstAssetList(mstAssetVos);
        
        return mstAssetList;
    }

}
