/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.inventory;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.asset.inventory.TblInventory;
import com.kmcj.karte.resources.asset.inventory.TblInventoryAssetClassCond;
import com.kmcj.karte.resources.asset.inventory.mgmt.company.TblInventoryMgmtCompany;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.language.MstLanguage;
import com.kmcj.karte.resources.language.MstLanguageService;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author xiaozhou.wang
 */
@Dependent
public class InventoryInfoCreateService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private MstLanguageService mstLanguageService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    private final static String TEMPLATE_FOLDER = "template";

    private final static String TEMPLATE_FILE = "inventory_request.xlsx";

    private final static String REPORT_FOLDER = "report";

    private final static String INVENTORY_FOLDER = "inventory_request";

    private final static String XSSF_FILE = ".xlsx";

    private final static String BATCH_NAME = "InventoryInfoCreateBatchlet";

    private final static String ASSET_TYPE = "mst_asset.asset_type";

    private final static String ACQUISITION_TYPE = "mst_asset.acquisition_type";

    public static final String[] DICT_KEYS = {ASSET_TYPE, ACQUISITION_TYPE};

    private static final String GET_ASSET_DISPOSAL_MOLD_DATA_SQL = " SELECT"
            + " asset.ITEM_CODE, "//品目コード
            + " asset.ASSET_NO, " //資産番号
            + " asset.BRANCH_NO," //補助番号
            + " t4.ITEM_NAME, " //名称
            + " asset.ASSET_TYPE, " //資産種類
            + " asset.VENDOR_CODE, "//SupplierCode
            + " asset.ACQUISITION_TYPE, "//取得区分
            + " asset.ACQUISITION_AMOUNT," //取得金額
            + " asset.ACQUISITION_YYYYMM, " //取得年月
            + " asset.MOLD_COUNT, " //型数
            + " t6.MGMT_LOCATION_NAME, "// 所在先
            + " asset.COMMON_INFORMATION , "//共通情報
            + " t3.DECIMAL_PLACES," //保留少数位
            + " t5.MGMT_COMPANY_NAME, " // 管理先
            + " t5.MGMT_COMPANY_CODE " // 管理CODE
            + " FROM tbl_inventory_detail detail "
            + " LEFT OUTER JOIN mst_asset asset "
            + " ON detail.ASSET_ID = asset.uuid "
            + " LEFT OUTER JOIN mst_currency t3 "
            + " ON (t3.CURRENCY_CODE = asset.CURRENCY_CODE) "
            + " LEFT OUTER JOIN mst_item t4 "
            + " ON (t4.ITEM_CODE = asset.ITEM_CODE) "
            + " LEFT OUTER JOIN mst_mgmt_company t5 "
            + " ON (t5.MGMT_COMPANY_CODE = asset.MGMT_COMPANY_CODE) "
            + " LEFT OUTER JOIN mst_mgmt_location t6 "
            + " ON (t6.MGMT_LOCATION_CODE = asset.MGMT_LOCATION_CODE) "
            + " WHERE detail.INVENTORY_ID = ? "
            + " AND asset.MGMT_COMPANY_CODE = ? "
            + " AND asset.MOLD_MACHINE_TYPE <> 2 " //0：空白、１：金型 0を金型に含む
            + " ORDER BY asset.ITEM_CODE ";

    private static final String GET_ASSET_DISPOSAL_MACHINE_DATA_SQL = " SELECT"
            + " asset.ITEM_CODE, "//品目コード
            + " asset.ASSET_NO, " //資産番号
            + " asset.BRANCH_NO," //補助番号
            + " t4.ITEM_NAME, " //名称
            + " asset.ASSET_TYPE, " //資産種類
            + " asset.ACQUISITION_TYPE, "//取得区分
            + " asset.ACQUISITION_AMOUNT," //取得金額
            + " asset.ACQUISITION_YYYYMM, " //取得年月
            + " t6.MGMT_LOCATION_NAME, "// 所在先
            + " asset.COMMON_INFORMATION , "//共通情報
            + " t3.DECIMAL_PLACES," //保留少数位
            + " t5.MGMT_COMPANY_NAME, " // 管理先
            + " t5.MGMT_COMPANY_CODE " // 管理CODE
            + " FROM tbl_inventory_detail detail "
            + " LEFT OUTER JOIN mst_asset asset "
            + " ON detail.ASSET_ID = asset.uuid "
            + " LEFT OUTER JOIN mst_currency t3 "
            + " ON (t3.CURRENCY_CODE = asset.CURRENCY_CODE) "
            + " LEFT OUTER JOIN mst_item t4 "
            + " ON (t4.ITEM_CODE = asset.ITEM_CODE) "
            + " LEFT OUTER JOIN mst_mgmt_company t5 "
            + " ON (t5.MGMT_COMPANY_CODE = asset.MGMT_COMPANY_CODE) "
            + " LEFT OUTER JOIN mst_mgmt_location t6 "
            + " ON (t6.MGMT_LOCATION_CODE = asset.MGMT_LOCATION_CODE) "
            + " WHERE detail.INVENTORY_ID = ? "
            + " AND asset.MGMT_COMPANY_CODE = ? "
            + " AND asset.MOLD_MACHINE_TYPE = 2 " //設備
            + " ORDER BY asset.ITEM_CODE ";

    /**
     * 棚卸明細データ作成
     */
    private static final String INSERT_TBLINVENTORY_DETAILGET_SQL = " INSERT  INTO tbl_inventory_detail ( "
            + " INVENTORY_ID, "
            + " ASSET_ID, "
            + " CREATE_DATE, "
            + " UPDATE_DATE, "
            + " CREATE_USER_UUID, "
            + " UPDATE_USER_UUID) "
            + " SELECT "
            + " ?, "// 棚卸実施ID
            + " t1.UUID, " //資産UUID
            + " ?, "// CREATE_DATE
            + " ?, "// UPDATE_DATE
            + " ?, "// CREATE_USER_UUID
            + " ? "// UPDATE_USER_UUID
            + " FROM mst_asset t1 "
            + " WHERE (t1.MGMT_COMPANY_CODE IS NOT NULL AND t1.MGMT_COMPANY_CODE <> '') "
            + " AND (t1.DISPOSAL_STATUS = 0) ";

    private static final String INSERT_TBLINVENTORY_MGMT_COMPANY1 = " INSERT INTO tbl_inventory_mgmt_company( "
            + " INVENTORY_ID, "
            + " MGMT_COMPANY_CODE, "
            + " COMPANY_NAME, "
            + " LOCATION_NAME, "
            + " DEPARTMENT, "
            + " TEL_NO, "
            + " POSITION, "
            + " CONTACT_NAME, "
            + " MAIL_ADDRESS, "
            + " INVENTORY_ASSET_COUNT, "
            + " INVENTORY_MOLD_ASSET_COUNT, "
            + " INVENTORY_MACHINE_ASSET_COUNT, "
            + " CREATE_DATE, "
            + " UPDATE_DATE, "
            + " CREATE_USER_UUID, "
            + " UPDATE_USER_UUID) "
            + " SELECT a.INVENTORY_ID , a.MGMT_COMPANY_CODE , a.COMPANY_NAME, a.LOCATION_NAME ,a.DEPARTMENT, a.TEL_NO, a.POSITION ,a.CONTACT_NAME, a.MAIL_ADDRESS , "
            //            + "a.INVENTORY_ASSET_COUNT  , "
            //            + "b.INVENTORY_MOLD_ASSET_COUNT ,"
            //            + "b.INVENTORY_MACHINE_ASSET_COUNT,"
            + " CASE WHEN a.INVENTORY_ASSET_COUNT IS NULL THEN 0 ELSE a.INVENTORY_ASSET_COUNT END INVENTORY_ASSET_COUNT, "
            + " CASE WHEN b.INVENTORY_MOLD_ASSET_COUNT IS NULL THEN 0 ELSE b.INVENTORY_MOLD_ASSET_COUNT END INVENTORY_MOLD_ASSET_COUNT,"
            + " CASE WHEN b.INVENTORY_MACHINE_ASSET_COUNT IS NULL THEN 0 ELSE b.INVENTORY_MACHINE_ASSET_COUNT END INVENTORY_MACHINE_ASSET_COUNT,"
            + " a.CREATE_DATE,  a.UPDATE_DATE,  a.CREATE_USER_UUID,a.UPDATE_USER_UUID "
            + " FROM ( SELECT detail.INVENTORY_ID,  asset.MGMT_COMPANY_CODE,  t7.COMPANY_NAME,  t8.LOCATION_NAME, t2.DEPARTMENT,  t2.TEL_NO,  t2.POSITION,    t2.CONTACT_NAME,  t2.MAIL_ADDRESS,  COUNT(detail.INVENTORY_ID) AS INVENTORY_ASSET_COUNT,    "
            + "   ? AS CREATE_DATE, ? AS UPDATE_DATE,  ? AS CREATE_USER_UUID,  ? AS UPDATE_USER_UUID  "
            + " FROM tbl_inventory_detail detail        LEFT OUTER JOIN mst_asset asset  ON detail.ASSET_ID = asset.uuid  "
            + " LEFT OUTER JOIN mst_contact t2  ON ((t2.MGMT_COMPANY_CODE = asset.MGMT_COMPANY_CODE) AND (t2.ASSET_MANAGEMENT_FLG = 1))  "
            + " LEFT OUTER JOIN mst_company t7  ON t2.COMPANY_ID = t7.ID        LEFT OUTER JOIN mst_location t8 ON t2.LOCATION_ID = t8.ID  "
            + " WHERE detail.INVENTORY_ID = ? GROUP BY asset.MGMT_COMPANY_CODE ) a "
            + " LEFT OUTER JOIN  ("
            // + " SELECT detail.INVENTORY_ID,  asset.MGMT_COMPANY_CODE,   CASE WHEN asset.MOLD_MACHINE_TYPE = 1 THEN COUNT(relation.MOLD_UUID) ELSE 0 END AS INVENTORY_MOLD_ASSET_COUNT,     CASE WHEN asset.MOLD_MACHINE_TYPE = 2 THEN COUNT(relation.MACHINE_UUID) ELSE 0 END AS INVENTORY_MACHINE_ASSET_COUNT"
            //+ " FROM tbl_inventory_detail detail  LEFT OUTER JOIN mst_asset asset  ON detail.ASSET_ID = asset.uuid        LEFT OUTER JOIN tbl_mold_machine_asset_relation relation  ON (relation.ASSET_ID = asset.UUID)    "
            //+ " WHERE detail.INVENTORY_ID = ?       GROUP BY asset.MGMT_COMPANY_CODE"
            + " SELECT detail.INVENTORY_ID, asset.MGMT_COMPANY_CODE,COUNT(relation.MOLD_UUID) INVENTORY_MOLD_ASSET_COUNT , COUNT(relation.MACHINE_UUID) INVENTORY_MACHINE_ASSET_COUNT FROM tbl_mold_machine_asset_relation relation  "
            + " INNER JOIN  mst_asset asset ON relation.ASSET_ID = asset.UUID "
            + " INNER JOIN tbl_inventory_detail detail   ON detail.ASSET_ID = asset.uuid WHERE detail.INVENTORY_ID = ?  GROUP BY asset.MGMT_COMPANY_CODE  "
            + " ) b ON a.INVENTORY_ID = b.INVENTORY_ID AND a.MGMT_COMPANY_CODE  = b.MGMT_COMPANY_CODE  ";

    /**
     * 資産データ抽出バッチ処理完了時 ステータスを「開始」にする。でも画面の状況が最新化していなくて、検索ボタンを押したら、最新の状況を表すこと。
     *
     * @param inventoryId
     * @param mgmtCompanyCount
     */
    @Transactional
    public void updateInventoryStatus(String inventoryId, int mgmtCompanyCount) {

        TblInventory tblInventory = entityManager.find(TblInventory.class, inventoryId);

        if (tblInventory != null) {

            tblInventory.setStatus(CommonConstants.INVENTORY_STATUS_START);

            tblInventory.setMgmtCompanyCount(mgmtCompanyCount);

            tblInventory.setUpdateDate(new Date());

            tblInventory.setUpdateUserUuid(BATCH_NAME);

            entityManager.merge(tblInventory);
        }

    }

    /**
     * 棚卸実施データの削除
     *
     * @param inventoryId
     */
    @Transactional
    public void deleteInventoryByException(String inventoryId) {

        Query query = entityManager.createNamedQuery("TblInventory.deleteByUuid");

        query.setParameter("uuid", inventoryId);

        query.executeUpdate();

    }

    /**
     * 棚卸管理先テーブルの更新
     *
     * @param inventoryId
     */
    @Transactional
    public int insertTblInventoryMgmtCompany(String inventoryId) {

        Query query1 = entityManager.createNativeQuery(INSERT_TBLINVENTORY_MGMT_COMPANY1);
        query1.setParameter(1, new Date());
        query1.setParameter(2, new Date());
        query1.setParameter(3, BATCH_NAME);
        query1.setParameter(4, BATCH_NAME);
        query1.setParameter(5, inventoryId);
        query1.setParameter(6, inventoryId);
        return query1.executeUpdate();

    }

    /**
     * 管理先ごとに依頼表を作成
     *
     * @param inventoryId
     */
    @Transactional
    public void createInventoryRequestFile(String inventoryId) {

        Query query1 = entityManager.createNamedQuery("TblInventoryMgmtCompany.findByInventoryId");
        query1.setParameter("inventoryId", inventoryId);
        List list1 = query1.getResultList();

        if (list1 != null && list1.size() > 0) {

            //ファイル名、ファイルの作成
            // フォルダ存在判定、存在してないの場合、新規作成する。
            String folderPath = kartePropertyService.getDocumentDirectory();
            StringBuilder sampleFilePath = new StringBuilder();
            sampleFilePath.append(folderPath);
            sampleFilePath.append(FileUtil.SEPARATOR);
            sampleFilePath.append(TEMPLATE_FOLDER);
            sampleFilePath.append(FileUtil.SEPARATOR);
            sampleFilePath.append(TEMPLATE_FILE);

            StringBuilder tempFilePath = new StringBuilder();
            tempFilePath.append(folderPath);
            tempFilePath.append(FileUtil.SEPARATOR);
            tempFilePath.append(REPORT_FOLDER);
            tempFilePath.append(FileUtil.SEPARATOR);
            tempFilePath.append(INVENTORY_FOLDER);
            tempFilePath.append(FileUtil.SEPARATOR);
            tempFilePath.append(inventoryId);
            tempFilePath.append(FileUtil.SEPARATOR);

            MstLanguage mstLanguage = mstLanguageService.getDefaultLanguage();

            // 選択肢Map
            Map<String, String> choiceMap = mstChoiceService.getChoiceMap(mstLanguage.getId(), DICT_KEYS);
            List<String> dictKey = new ArrayList();
            dictKey.add("in_operation");
            dictKey.add("low_operation");
            dictKey.add("non_operation");
            choiceMap.putAll(mstDictionaryService.getDictionaryHashMap(mstLanguage.getId(), dictKey));
            // 管理先ごとに明細データを取得し依頼票を作成する
            for (Object obj : list1) {
                TblInventoryMgmtCompany tblInventoryMgmtCompany = (TblInventoryMgmtCompany) obj;

                // 金型に属する資産情報を取得
                Query query2 = entityManager.createNativeQuery(GET_ASSET_DISPOSAL_MOLD_DATA_SQL);
                query2.setParameter(1, inventoryId);
                query2.setParameter(2, tblInventoryMgmtCompany.getTblInventoryMgmtCompanyPK().getMgmtCompanyCode());
                List moldList = query2.getResultList();

                // 設備に属する資産情報を取得
                Query query3 = entityManager.createNativeQuery(GET_ASSET_DISPOSAL_MACHINE_DATA_SQL);
                query3.setParameter(1, inventoryId);
                query3.setParameter(2, tblInventoryMgmtCompany.getTblInventoryMgmtCompanyPK().getMgmtCompanyCode());
                List machineList = query3.getResultList();

                String fileUuid = IDGenerator.generate();

                String path = tempFilePath.toString();
                StringBuilder tempFilePath1 = new StringBuilder(path);
                tempFilePath1.append(fileUuid);
                tempFilePath1.append(XSSF_FILE);
                // ファイルをコピーしてReNameする
                FileUtil.fileChannelCopy(sampleFilePath.toString(), tempFilePath1.toString(), false);

                try {
                    // 管理先ごとに依頼票を作成する
                    InventoryInfoCreateTemplate inventoryInfoCreateTemplate = new InventoryInfoCreateTemplate();
                    
                    inventoryInfoCreateTemplate.write(tempFilePath1.toString(), moldList, machineList, choiceMap);
                } catch (IOException e) {
                    // nothing
                }

                // 管理先ごとにファイルＵＵＩＤを更新
                TblInventoryMgmtCompany oldTblInventoryMgmtCompany = entityManager.find(TblInventoryMgmtCompany.class, tblInventoryMgmtCompany.getTblInventoryMgmtCompanyPK());
                oldTblInventoryMgmtCompany.setFileUuid(fileUuid);
                entityManager.merge(oldTblInventoryMgmtCompany);
            }
        }

    }

    /**
     * 棚卸明細テーブルの更新
     *
     * @param inventoryId
     */
    @Transactional
    public void insertTblInventoryDetail(String inventoryId) {

        Query query1 = entityManager.createNativeQuery(INSERT_TBLINVENTORY_DETAILGET_SQL + getWhereAssetClass(inventoryId));
        query1.setParameter(1, inventoryId);
        query1.setParameter(2, new Date());
        query1.setParameter(3, new Date());
        query1.setParameter(4, BATCH_NAME);
        query1.setParameter(5, BATCH_NAME);
        query1.executeUpdate();
    }

    /**
     *
     * @return
     */
    private String getWhereAssetClass(String inventoryId) {

        StringBuilder sqlWhere = new StringBuilder();

        Query query1 = entityManager.createNamedQuery("TblInventoryAssetClassCond.findByInventoryId");
        query1.setParameter("inventoryId", inventoryId);
        List list1 = query1.getResultList();

        if (list1 != null && list1.size() > 0) {
            sqlWhere.append(" AND t1.ASSET_CLASS IN (");
            for (int i = 0; i < list1.size(); i++) {
                sqlWhere.append(" '");
                TblInventoryAssetClassCond tblInventoryAssetClassCond = (TblInventoryAssetClassCond) list1.get(i);
                sqlWhere.append(tblInventoryAssetClassCond.getTblInventoryAssetClassCondPK().getAssetClass()).append("',");
            }
            String str = sqlWhere.toString();
            return str.substring(0, str.length() - 1) + ")";

        } else {

            return "";
        }

    }

}
