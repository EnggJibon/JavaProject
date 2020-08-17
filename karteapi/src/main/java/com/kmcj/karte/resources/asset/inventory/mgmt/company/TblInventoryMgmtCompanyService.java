/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.mgmt.company;

import com.google.gson.Gson;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.MailSender;
import com.kmcj.karte.batch.inventory.InventoryInfoCreateTemplate;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.asset.inventory.excelvo.R0001InventoryRequest;
import com.kmcj.karte.resources.asset.relation.TblMoldMachineAssetRelation;
import com.kmcj.karte.resources.authentication.Credential;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.contact.MstContact;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.external.MstExternalDataGetSetting;
import com.kmcj.karte.resources.external.MstExternalDataGetSettingService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.files.TblUploadFileService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.asset.inventory.TblInventory;
import com.kmcj.karte.resources.asset.inventory.TblInventoryService;
import com.kmcj.karte.resources.asset.inventory.detail.TblInventoryDetail;
import com.kmcj.karte.resources.asset.inventory.detail.TblInventoryDetailService;
import com.kmcj.karte.resources.asset.inventory.request.TblInventoryRequest;
import com.kmcj.karte.resources.asset.inventory.request.detail.TblInventoryRequestDetail;
import com.kmcj.karte.resources.asset.inventory.request.detail.TblInventoryRequestDetailVo;
import com.kmcj.karte.resources.asset.inventory.request.detail.TblInventoryRequestDetailVoList;
import com.kmcj.karte.resources.asset.inventory.request.detail.id.TblInventoryRequestDetailId;
import com.kmcj.karte.resources.asset.inventory.send.history.TblInventorySendHistory;
import com.kmcj.karte.resources.asset.inventory.send.history.TblInventorySendHistoryAttachment;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.ZipCompressor;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author
 */
@Dependent
public class TblInventoryMgmtCompanyService {
    
    private static final String COUNT_STR = "count";
    
    private static final String UNDER_YOKO = "_";
    
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
    private static final String EXT_INVENTORY_REQUEST_PUSH_API_URL = "ws/karte/api/inventory/mgmt/company/push/request";
    
    private static final String LANGID = "ja";
    
    private static final int ASSET_MANAGEMENT_FLG = 1;
    
    private static final String ASSET_TYPE = "mst_asset.asset_type";
    
    private static final String ACQUISITION_TYPE = "mst_asset.acquisition_type";
    
    private static final int POST_FAILURE = 1; // データ連携失敗しました。
    private static final int CONNECT_FAILURE = 2; // データ認証失敗しました。
    private static final int SETTING_FAILURE = 3; // 外部設定データ取得失敗しました。
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private KartePropertyService kartePropertyService;
    
    @Inject
    private TblUploadFileService tblUploadFileService;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private TblCsvExportService tblCsvExportService;
    
    @Inject
    private MstExternalDataGetSettingService externalDataGetSettingService;
    
    @Inject
    private TblInventoryDetailService tblInventoryDetailService;
    
    @Inject
    private MailSender mailSender;
    
    @Inject
    private MstChoiceService mstChoiceService;
    
    @Inject
    private TblInventoryService tblInventoryService;
    
    /**
     * 棚卸依頼先一覧件数取得
     * 
     * @param inventoryId
     * @return 
     */
    public CountResponse getInventoryMgmtCompanyCount(String inventoryId) {
        // 一覧件数取得
        List list = getInventoryMgmtCompanys(inventoryId, COUNT_STR);
        CountResponse count = new CountResponse();
        count.setCount(((Long) list.get(0)));
        return count;
    }
    
    /**
     * 棚卸依頼先一覧内容取得
     * 
     * @param inventoryId
     * @return 
     */
    public List<TblInventoryMgmtCompany> getInventoryMgmtCompanyList(String inventoryId) {
        // 一覧内容取得
        return getInventoryMgmtCompanys(inventoryId, null);
    }
    
    /**
     * 棚卸依頼先一覧保存
     *
     * @param tblInventoryMgmtCompanyVoList
     * @param userUuid
     * @return 
     */
    @Transactional
    public TblInventoryMgmtCompanyVoList postInventoryMgmtCompany(TblInventoryMgmtCompanyVoList tblInventoryMgmtCompanyVoList, String userUuid) {
        TblInventoryMgmtCompanyVoList response = new TblInventoryMgmtCompanyVoList();
        // 画面更新情報を取得
        List<TblInventoryMgmtCompany> saveDataList = tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys();
        if (saveDataList != null && !saveDataList.isEmpty()) {
            Date sysDate = DateFormat.strToDatetime(DateFormat.getCurrentDateTime());
            // 棚卸実施IDにより、依頼先情報リストを取得
            List<TblInventoryMgmtCompany> dbDataList = entityManager.createNamedQuery("TblInventoryMgmtCompany.findByInventoryId")
                .setParameter("inventoryId", saveDataList.get(0).getTblInventoryMgmtCompanyPK().getInventoryId())
                .getResultList();
            
            InventoryInfoCreateTemplate we = new InventoryInfoCreateTemplate();
            // 帳票ファイルパス：document/report/inventory_request/棚卸実施ID/
            StringBuilder srcFileDir = new StringBuilder(kartePropertyService.getDocumentDirectory());
            srcFileDir.append(FileUtil.SEPARATOR).append(CommonConstants.REPORT)
                .append(FileUtil.SEPARATOR).append(CommonConstants.INVENTORY_REQUEST)
                .append(FileUtil.SEPARATOR).append(saveDataList.get(0).getTblInventoryMgmtCompanyPK().getInventoryId()).append(FileUtil.SEPARATOR);
            
            // 帳票更新用マップ
            Map<String, R0001InventoryRequest> param = new HashMap();
            R0001InventoryRequest request = null;
            for (TblInventoryMgmtCompany saveData : saveDataList) {
                if (dbDataList != null && !dbDataList.isEmpty()) {
                    for (TblInventoryMgmtCompany dbData : dbDataList) {
                        // 帳票オブジェクトリスト(更新値設定)
                        request = new R0001InventoryRequest();
                        // 管理先コード一致する場合、回収期限を更新する
                        if (saveData.getTblInventoryMgmtCompanyPK().getMgmtCompanyCode().equals(dbData.getTblInventoryMgmtCompanyPK().getMgmtCompanyCode())) {
                            // パス設定
                            request.setOutputPath(srcFileDir.toString() + saveData.getFileUuid() + CommonConstants.EXT_EXCEL);
                            if (saveData.getDueDate() != null) {
                                // 回収期限変更あり場合、更新する。
                                if (dbData.getDueDate() == null || saveData.getDueDate().compareTo(dbData.getDueDate()) != 0) {
                                    dbData.setDueDate(saveData.getDueDate());
                                    dbData.setUpdateDate(sysDate);
                                    dbData.setUpdateUserUuid(userUuid);
                                    entityManager.merge(dbData);
                                    
                                    // エクセルファイル回収期限更新
                                    request.setDueDate(saveData.getDueDate());
                                    param.put(dbData.getTblInventoryMgmtCompanyPK().getMgmtCompanyCode(), request);
                                }
                            } else {
                                // 回収期限変更あり場合、更新する。
                                if (dbData.getDueDate() != null) {
                                    dbData.setDueDate(null);
                                    dbData.setUpdateDate(sysDate);
                                    dbData.setUpdateUserUuid(userUuid);
                                    entityManager.merge(dbData);

                                    // エクセルファイル回収期限更新
                                    request.setDueDate(null);
                                    param.put(dbData.getTblInventoryMgmtCompanyPK().getMgmtCompanyCode(), request);
                                }
                            }
                            break;
                        }                        
                    }
                }
            }
            
            try {
                we.update(param);
            } catch (IOException ex) {
                Logger.getLogger(TblInventoryMgmtCompanyService.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // 最大回収期限の管理先情報取得
            dbDataList = entityManager.createNamedQuery("TblInventoryMgmtCompany.findByInventoryIdOrderByDueDate")
                .setParameter("inventoryId", saveDataList.get(0).getTblInventoryMgmtCompanyPK().getInventoryId())
                .setMaxResults(1)
                .getResultList();
            // 棚卸実施情報取得
            List<TblInventory> tblInventory = entityManager.createNamedQuery("TblInventory.findByUuid")
                .setParameter("uuid", saveDataList.get(0).getTblInventoryMgmtCompanyPK().getInventoryId())
                .setMaxResults(1)
                .getResultList();
            // 棚卸実施テーブルに最終回収期限を全ての管理先の回収期限のうち最大の日付で更新する。
            if (tblInventory != null && !tblInventory.isEmpty() && dbDataList != null && !dbDataList.isEmpty()) {
                tblInventory.get(0).setFinalDueDate(dbDataList.get(0).getDueDate());
                tblInventory.get(0).setUpdateDate(sysDate);
                tblInventory.get(0).setUpdateUserUuid(userUuid);
                entityManager.merge(tblInventory.get(0));
            }
        }
        return response;
    }
    
    /**
     * 棚卸依頼票ダウンロード
     *
     * @param tblInventoryMgmtCompanyVoList
     * @param langId
     * @return 
     */
    public FileReponse downloadExcel(TblInventoryMgmtCompanyVoList tblInventoryMgmtCompanyVoList, String langId) {
        FileReponse fr = new FileReponse();
        if (tblInventoryMgmtCompanyVoList != null && tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys() != null && !tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys().isEmpty()) {
            String inventoryId = tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys().get(0).getTblInventoryMgmtCompanyPK().getInventoryId();
            String inventoryName;
            // 棚卸実施名称取得
            List<TblInventory> tblInventoryList = entityManager.createNamedQuery("TblInventory.findByUuid")
                .setParameter("uuid", inventoryId).getResultList();

            if (tblInventoryList != null && !tblInventoryList.isEmpty()) {
                inventoryName = tblInventoryList.get(0).getName();
            } else {
                fr.setError(true);
                fr.setErrorCode(ErrorMessages.E201_APPLICATION);
                fr.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_data_deleted"));
                return fr;
            }

            // ファイルコピー
            StringBuilder srcFileDir = new StringBuilder(kartePropertyService.getDocumentDirectory());
            srcFileDir.append(FileUtil.SEPARATOR).append(CommonConstants.REPORT)
                .append(FileUtil.SEPARATOR).append(CommonConstants.INVENTORY_REQUEST)
                .append(FileUtil.SEPARATOR).append(inventoryId).append(FileUtil.SEPARATOR);

            StringBuilder destFileDir = new StringBuilder(kartePropertyService.getDocumentDirectory());
            destFileDir.append(FileUtil.SEPARATOR).append(CommonConstants.WORK)
                .append(FileUtil.SEPARATOR).append(CommonConstants.INVENTORY_REQUEST)
                .append(FileUtil.SEPARATOR).append(inventoryId).append(FileUtil.SEPARATOR);
            // 事前に[work/inventory_request/棚卸実施ID/]を削除する
            FileUtil.deleteDir(new File(destFileDir.toString()));
            
            StringBuilder newFileName = new StringBuilder();
            String srcFileName, destFileName = "";
            for (TblInventoryMgmtCompany tblInventoryMgmtCompany : tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys()) {
                newFileName.delete(0, newFileName.length());
                // 棚卸実施名称 + '_' + 管理先コード + 会社名称 '.xlsx'
                newFileName.append(inventoryName).append(UNDER_YOKO).append(tblInventoryMgmtCompany.getTblInventoryMgmtCompanyPK().getMgmtCompanyCode())
                    .append(tblInventoryMgmtCompany.getCompanyName()).append(CommonConstants.EXT_EXCEL);
                srcFileName = srcFileDir.toString() + tblInventoryMgmtCompany.getFileUuid() + CommonConstants.EXT_EXCEL;
                destFileName = destFileDir.toString() + tblInventoryMgmtCompany.getFileUuid() + CommonConstants.EXT_EXCEL;
                // 管理先対象帳票/workに移動する
                boolean result = FileUtil.fileChannelCopy(srcFileName, destFileName, true);
//                if (!result) {
//                    fr.setError(true);
//                    fr.setErrorCode(ErrorMessages.E201_APPLICATION);
//                    fr.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_request_file_not_found"));
//                    return fr;
//                }
                // 帳票ファイルリネームする
                FileUtil.renameFile(destFileDir.toString(), tblInventoryMgmtCompany.getFileUuid() + CommonConstants.EXT_EXCEL, newFileName.toString());
            }
            
            // ダウンロード管理先対象は複数の場合、圧縮処理を行う
            if (tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys().size() > 1) {
                // ファイル圧縮
                // ZIPファイル名：棚卸実施名称 + '_' + yyyyMMddhhmiss.zipとする
                java.text.DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                String dateTime = format.format(new Date());
                StringBuilder outFileName = new StringBuilder();
                outFileName.append(inventoryName).append(UNDER_YOKO).append(dateTime);
                
                boolean charsetFlg = false;
                if (LANGID.equalsIgnoreCase(langId)) {
                    charsetFlg = true;
                }
                
                ZipCompressor.zip(destFileDir.toString(), outFileName.toString(), charsetFlg);
                String zipPath = new StringBuilder(kartePropertyService.getDocumentDirectory())
                    .append(FileUtil.SEPARATOR).append(CommonConstants.WORK)
                    .append(FileUtil.SEPARATOR).append(CommonConstants.INVENTORY_REQUEST)
                    .append(FileUtil.SEPARATOR).toString();
                FileUtil.renameFile(zipPath, inventoryId + CommonConstants.EXT_ZIP, outFileName.toString() + CommonConstants.EXT_ZIP);
                FileUtil.fileChannelCopy(zipPath + outFileName.toString() + CommonConstants.EXT_ZIP, destFileDir.toString() + outFileName.toString() + CommonConstants.EXT_ZIP, true);
                new File(zipPath + outFileName.toString() + CommonConstants.EXT_ZIP).delete();
                fr.setFileUuid(outFileName.append(CommonConstants.EXT_ZIP).toString());
            } else {
                fr.setFileUuid(newFileName.toString());
            }
        }
        return fr;
    }
    
    /**
     * 棚卸実施送信履歴取得
     *
     * @param inventoryId
     * @return 
     */
    public TblInventoryMgmtCompanyVoList getInventorySendingHistory(String inventoryId) {
        TblInventoryMgmtCompanyVoList response = new TblInventoryMgmtCompanyVoList();
        // 送信履歴テーブルに最大Seq番号履歴情報を取得
        Query query = entityManager.createNamedQuery("TblInventorySendHistory.findMaxSeqByInventoryId");
        query.setParameter("inventoryId", inventoryId);
        List<TblInventorySendHistory> maxSeqSendingHis = query.getResultList();
        if (maxSeqSendingHis != null && !maxSeqSendingHis.isEmpty()) {
            response.setMaxSeqInventorySendHistory(maxSeqSendingHis.get(0));
            
            // 棚卸実施IDと最大Seq番号履歴情報により送信履歴添付情報取得
            query = entityManager.createNamedQuery("TblInventorySendHistoryAttachment.findMaxSeqByInventoryIdAndSeq");
            query.setParameter("inventoryId", inventoryId);
            query.setParameter("seq", maxSeqSendingHis.get(0).getTblInventorySendHistoryPK().getSeq());
            List<TblInventorySendHistoryAttachment> maxSeqSendingHisAttachs = query.getResultList();
            if (maxSeqSendingHisAttachs != null && !maxSeqSendingHisAttachs.isEmpty()) {
                // ファイル名取得
                for (TblInventorySendHistoryAttachment attach : maxSeqSendingHisAttachs) {
                    attach.setUploadFileName("");
                    if (!StringUtils.isEmpty(attach.getFileName())) {
                        TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(attach.getFileName());
                        if (null != tblUploadFile) {
                            attach.setUploadFileName(tblUploadFile.getUploadFileName());
                        }
                    }
                }
                response.setTblInventorySendHistoryAttachments(maxSeqSendingHisAttachs);
            }
        }
        
        return response;
    }
    
    /**
     * 棚卸依頼先送信
     *
     * @param tblInventoryMgmtCompanyVoList
     * @param userUuid
     * @return 
     */
    @Transactional
    public BasicResponse postInventorySendHistory(TblInventoryMgmtCompanyVoList tblInventoryMgmtCompanyVoList, String userUuid) {
        BasicResponse response = new BasicResponse();
        // 送信履歴テーブル登録
        if (tblInventoryMgmtCompanyVoList != null && tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory() != null
                && tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys() != null && !tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys().isEmpty()) {
            Date sysDate = DateFormat.strToDatetime(DateFormat.getCurrentDateTime());
            // 連番最大値取得
            int seq = 0;
            Query query = entityManager.createNamedQuery("TblInventorySendHistory.findMaxSeqByInventoryId");
            query.setParameter("inventoryId", tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys().get(0).getTblInventoryMgmtCompanyPK().getInventoryId());
            List<TblInventorySendHistory> maxSeqSendingHis = query.getResultList();
            if (maxSeqSendingHis != null && !maxSeqSendingHis.isEmpty()) {
                seq = maxSeqSendingHis.get(0).getTblInventorySendHistoryPK().getSeq() + 1;
            }
            String uuid = IDGenerator.generate();
            tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory().setUuid(uuid);
            tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory().getTblInventorySendHistoryPK().setSeq(seq);
            tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory().setCreateDate(sysDate);
            tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory().setCreateUserUuid(userUuid);
            tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory().setUpdateDate(sysDate);
            tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory().setUpdateUserUuid(userUuid);
            entityManager.persist(tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory());
            
            // 送信履歴添付テーブル登録
            if (tblInventoryMgmtCompanyVoList.getTblInventorySendHistoryAttachments() != null && !tblInventoryMgmtCompanyVoList.getTblInventorySendHistoryAttachments().isEmpty()) {
                int seqAttach = 0;
                for (TblInventorySendHistoryAttachment attach : tblInventoryMgmtCompanyVoList.getTblInventorySendHistoryAttachments()) {
                    attach.setUuid(IDGenerator.generate());
                    attach.getTblInventorySendHistoryAttachmentPK().setHistoryId(uuid);
                    attach.getTblInventorySendHistoryAttachmentPK().setSeq(seqAttach);
                    attach.setCreateDate(sysDate);
                    attach.setCreateUserUuid(userUuid);
                    attach.setUpdateDate(sysDate);
                    attach.setUpdateUserUuid(userUuid);
                    entityManager.persist(attach);
                    seqAttach++;
                }
            }
        }
        return response;
    }
    
    /**
     * サプライヤーへ送信用棚卸依頼情報取得
     *
     * @param tblInventoryMgmtCompanyVoList
     * @return 
     */
    public TblInventory getTblInventoryById(TblInventoryMgmtCompanyVoList tblInventoryMgmtCompanyVoList) {
        TblInventory tblInventory = null;
        if (tblInventoryMgmtCompanyVoList != null && tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys() != null && !tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys().isEmpty()) {
            Query query = entityManager.createNamedQuery("TblInventory.findByUuid")
                .setParameter("uuid", tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys().get(0).getTblInventoryMgmtCompanyPK().getInventoryId())
                .setMaxResults(1);
            List<TblInventory> tblInventoryList = query.getResultList();
            if (tblInventoryList != null && !tblInventoryList.isEmpty()) {
                tblInventory = tblInventoryList.get(0);
            }
        }
        return tblInventory;
    }
    
    /**
     * サプライヤーへ送信用棚卸管理先情報取得
     *
     * @param tblInventoryMgmtCompanyVoList
     * @return 
     */
    public List<TblInventoryMgmtCompany> getTblInventoryMgmtCompanyById(TblInventoryMgmtCompanyVoList tblInventoryMgmtCompanyVoList) {
        List<TblInventoryMgmtCompany> tblInventoryMgmtCompanyList = null;
        if (tblInventoryMgmtCompanyVoList != null && tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys() != null && !tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys().isEmpty()) {
            tblInventoryMgmtCompanyList = entityManager.createNamedQuery("TblInventoryMgmtCompany.findByInventoryId")
                .setParameter("inventoryId", tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys().get(0).getTblInventoryMgmtCompanyPK().getInventoryId())
                .getResultList();
        }
        return tblInventoryMgmtCompanyList;
    }
    
    /**
     * サプライヤーへ送信用棚卸依頼明細情報リスト取得
     *
     * @param tblInventoryMgmtCompanyVoList
     * @return 
     */
    public List<TblInventoryDetail> getInventoryDetailById(TblInventoryMgmtCompanyVoList tblInventoryMgmtCompanyVoList) {
        List<TblInventoryDetail> tblInventoryDetailList = null;
        if (tblInventoryMgmtCompanyVoList != null && tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys() != null && !tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys().isEmpty()) {
            tblInventoryDetailList = tblInventoryDetailService.getInventoryDetails(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys().get(0).getTblInventoryMgmtCompanyPK().getInventoryId(),
                null, false);
        }
        
        return tblInventoryDetailList;
    }
    
    /**
     * サプライヤーへデータ連携認証してAPIを呼ぶ
     *
     * @param tblInventoryMgmtCompanyVoList
     * @param langId
     * @return
     */
    @Transactional
    public BasicResponse pushGetExternalInventoryRequest(TblInventoryMgmtCompanyVoList tblInventoryMgmtCompanyVoList, String langId) {
        BasicResponse basicResponse = new BasicResponse();

        Query query;
        String companyId = "";
        // 管理先コードにより会社マスタから会社ID取得
        if (tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany() != null) {
            query = entityManager.createNamedQuery("MstContact.findByMgmtCompanyCode")
                .setParameter("mgmtCompanyCode", tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getTblInventoryMgmtCompanyPK().getMgmtCompanyCode())
                .setParameter("assetManagementFlg", ASSET_MANAGEMENT_FLG);
            List<MstContact> list = query.getResultList();
            if (list != null && !list.isEmpty()) {
                companyId = list.get(0).getCompanyId();
            }
        }
        //  外部データ取得設定から有効フラグ=1のレコードを取得する
        List<MstExternalDataGetSetting> externalDataSettings = externalDataGetSettingService.getExternalDataGetSettringByCompanyId(companyId);
        if (externalDataSettings != null && !externalDataSettings.isEmpty()) {
            MstExternalDataGetSetting mstExternalDataGetSetting = externalDataSettings.get(0);

            String apiBaseUrl = mstExternalDataGetSetting.getApiBaseUrl();
            if (null == apiBaseUrl || apiBaseUrl.equals("")) {
                return basicResponse;
            }

            if (!apiBaseUrl.endsWith("/")) {
                apiBaseUrl = apiBaseUrl + "/";
                mstExternalDataGetSetting.setApiBaseUrl(apiBaseUrl);
            }

            Credential credential = externalCompanyLogin(mstExternalDataGetSetting);
            if (credential != null && StringUtils.isNotEmpty(credential.getToken())) {
                // call API
                String resultJson = FileUtil.sendPost(apiBaseUrl + EXT_INVENTORY_REQUEST_PUSH_API_URL, credential.getToken(), tblInventoryMgmtCompanyVoList);

                if (StringUtils.isNotEmpty(resultJson)) {
                    Gson gson = new Gson();
                    return gson.fromJson(resultJson, BasicResponse.class);
                } else {
                    basicResponse.setErrorCode(String.valueOf(POST_FAILURE));
                    Logger.getLogger(TblInventoryMgmtCompanyService.class.getName()).log(Level.SEVERE, "データ連携失敗しました。");
                }
            } else {
                basicResponse.setErrorCode(String.valueOf(CONNECT_FAILURE));
                Logger.getLogger(TblInventoryMgmtCompanyService.class.getName()).log(Level.SEVERE, "認証失敗しました。");
            }
        }

        return basicResponse;
    }
    
    /**
     *
     * @param externalDataSetting
     * @return
     */
    private Credential externalCompanyLogin(MstExternalDataGetSetting externalDataSetting) {
        Credential result = null;

        try {
            // 外部データ取得する前に認証確認
            Credential credential = new Credential();
            credential.setUserid(externalDataSetting.getUserId());
            credential.setPassword(FileUtil.decrypt(externalDataSetting.getEncryptedPassword()));
            String pathUrl = externalDataSetting.getApiBaseUrl() + CommonConstants.EXT_LOGIN_API_URL;
            FileUtil.SSL();
            try {
                result = FileUtil.sendPost(pathUrl, credential);
            } catch (Exception e) {
                // 認証エラー発生場合、スキップ
                return result;
            }
            // 認証失敗　スキップ
            if (result.isValid() == false || result.isError() == true) {
                return result;
            }
            //　認証できたら、Tokenを取得
            return result;
        } catch (Exception e) {
            return result;
        }
    }
    
    /**
     * 所有会社から棚卸依頼情報を使用会社へ登録する
     *
     * @param tblInventoryMgmtCompanyVoList
     * @param companyId
     * @param userUuid
     * @param langId
     * @return
     */
    @Transactional
    public BasicResponse postExternalInventoryRequest(TblInventoryMgmtCompanyVoList tblInventoryMgmtCompanyVoList, String companyId, String userUuid, String langId) {
        // 下記棚卸依頼、棚卸依頼明細、棚卸依頼明細IDのデータ連携する際に
        // 現状所有会社依頼してから再依頼時、新しい棚卸実施IDを立ち、暫く更新パターン無し
        
        BasicResponse basicResponse = new BasicResponse();
        Query query;
        if (tblInventoryMgmtCompanyVoList.getTblInventory() != null && tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany() != null) {
            // システム日時取得
            Date sysDate = DateFormat.strToDatetime(DateFormat.getCurrentDateTime());
            // 棚卸依頼ID
            String inventoryRequestUuid = IDGenerator.generate();
            // 棚卸依頼情報存在するか
            query = entityManager.createNamedQuery("TblInventoryRequest.findByInventoryId").setParameter("inventoryId", tblInventoryMgmtCompanyVoList.getTblInventory().getUuid());
            List<TblInventoryRequest> inventoryRequestList = query.getResultList();
            
            TblInventoryRequest tblInventoryRequest = null;
            // 棚卸依頼テーブル更新
            if (inventoryRequestList != null && !inventoryRequestList.isEmpty()) {
                tblInventoryRequest = inventoryRequestList.get(0);
                inventoryRequestUuid = tblInventoryRequest.getUuid();
            // 棚卸依頼テーブル登録
            } else {
                tblInventoryRequest = new TblInventoryRequest();
                tblInventoryRequest.setUuid(inventoryRequestUuid); // ID
                tblInventoryRequest.setRequestCompanyId(companyId); // 棚卸実施会社ID
                tblInventoryRequest.setStatus(CommonConstants.INVENTORY_REQUEST_STATUS_UNDO); // ステータス
                tblInventoryRequest.setInventoryId(tblInventoryMgmtCompanyVoList.getTblInventory().getUuid()); // 棚卸実施ID
                tblInventoryRequest.setName(tblInventoryMgmtCompanyVoList.getTblInventory().getName()); // 棚卸実施名称
                tblInventoryRequest.setCreateDate(sysDate);
                tblInventoryRequest.setCreateUserUuid(userUuid);
            }
            tblInventoryRequest.setRequestDate(sysDate); // 依頼日
            if (StringUtils.isNotEmpty(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getDueDateStr())) {
                tblInventoryRequest.setDueDate(DateFormat.strToDate(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getDueDateStr())); // 回答期限
            } else {
                tblInventoryRequest.setDueDate(null);
            }
            tblInventoryRequest.setAssetCount(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getInventoryAssetCount()); // 対象資産数
            tblInventoryRequest.setUpdateDate(sysDate);
            tblInventoryRequest.setUpdateUserUuid(userUuid);
            entityManager.merge(tblInventoryRequest);
            
            if (tblInventoryMgmtCompanyVoList.getTblInventoryDetails() != null && !tblInventoryMgmtCompanyVoList.getTblInventoryDetails().isEmpty()) {
                // 棚卸依頼明細情報存在するか
                query = entityManager.createNamedQuery("TblInventoryRequestDetail.findByInventoryRequestId").setParameter("inventoryRequestId", inventoryRequestUuid);
                List<TblInventoryRequestDetail> inventoryRequestDetailList = query.getResultList();
                // 棚卸依頼明細IDテーブル情報取得
                query = entityManager.createNamedQuery("TblInventoryRequestDetailId.findAll");
                List<TblInventoryRequestDetailId> inventoryRequestDetailIdList = query.getResultList();

                // 棚卸依頼明細ID
                String inventoryRequestDetailUuid = "";
                for (TblInventoryDetail tblInventoryDetail : tblInventoryMgmtCompanyVoList.getTblInventoryDetails()) {
                    TblInventoryRequestDetail tblInventoryRequestDetail = null;
                    if (inventoryRequestDetailList != null && !inventoryRequestDetailList.isEmpty()) {
                        for (TblInventoryRequestDetail requestDetail : inventoryRequestDetailList) {
                            // 同一資産番号と補助番号の依頼明細を更新対象とする
                            if (requestDetail.getAssetNo().equals(tblInventoryDetail.getMstAsset().getMstAssetPK().getAssetNo()) &&
                                requestDetail.getBranchNo().equals(tblInventoryDetail.getMstAsset().getMstAssetPK().getBranchNo())) {
                                tblInventoryRequestDetail = requestDetail;
                                inventoryRequestDetailUuid = requestDetail.getUuid();
                                break;
                            }
                        }
                        // 所有会社から依頼明細追加するパターンが実装はここ
                    } else {
                        inventoryRequestDetailUuid = IDGenerator.generate();
                        tblInventoryRequestDetail = new TblInventoryRequestDetail();
                        tblInventoryRequestDetail.setUuid(inventoryRequestDetailUuid); // ID
                        tblInventoryRequestDetail.setInventoryRequestId(inventoryRequestUuid); // 棚卸依頼ID
                        tblInventoryRequestDetail.setCreateDate(sysDate); // 作成日時
                        tblInventoryRequestDetail.setCreateUserUuid(userUuid); // 作成ユーザーUUID
                    }
                    if (tblInventoryRequestDetail == null) {
                        continue;
                    }
                    tblInventoryRequestDetail.setAssetNo(tblInventoryDetail.getMstAsset().getMstAssetPK().getAssetNo()); // 資産番号
                    tblInventoryRequestDetail.setBranchNo(tblInventoryDetail.getMstAsset().getMstAssetPK().getBranchNo()); // 補助番号
                    tblInventoryRequestDetail.setAssetName(tblInventoryDetail.getMstAsset().getAssetName()); // 資産名称
                    tblInventoryRequestDetail.setAssetType(tblInventoryDetail.getAssetTypeStr()); // 資産種類
                    tblInventoryRequestDetail.setInstallationSite(tblInventoryDetail.getMgmtLocationNameStr()); // 設置場所
                    tblInventoryRequestDetail.setItemCode(tblInventoryDetail.getMstAsset().getItemCode()); // 品目コード
                    tblInventoryRequestDetail.setItemName(tblInventoryDetail.getItemNameStr()); // 品目名称
                    tblInventoryRequestDetail.setMoldMachineType(tblInventoryDetail.getMstAsset().getMoldMachineType()); // 金型・設備区分
                    // 現品有無、無理由はサプライヤー側で更新する
                    tblInventoryRequestDetail.setUpdateDate(sysDate); // 更新日時
                    tblInventoryRequestDetail.setUpdateUserUuid(userUuid); // 更新ユーザーUUID
                    entityManager.merge(tblInventoryRequestDetail);
                    
                    if (tblInventoryDetail.getMstAsset().getTblMoldMachineAssetRelationVos() != null && !tblInventoryDetail.getMstAsset().getTblMoldMachineAssetRelationVos().isEmpty()) {
                        for (TblMoldMachineAssetRelation assetRelation : tblInventoryDetail.getMstAsset().getTblMoldMachineAssetRelationVos()) {
                            TblInventoryRequestDetailId tblInventoryRequestDetailId = null;
                            if (inventoryRequestDetailIdList != null && !inventoryRequestDetailIdList.isEmpty()) {
                                boolean isFind = false;
                                for (TblInventoryRequestDetailId requestDetailId : inventoryRequestDetailIdList) {
                                    if (requestDetailId.getRequestDetailId().equals(inventoryRequestDetailUuid)) {
                                        if ((requestDetailId.getMoldId() != null && requestDetailId.getMoldId().equals(assetRelation.getMoldId()))
                                            || (requestDetailId.getMachineId() != null && requestDetailId.getMachineId().equals(assetRelation.getMachineId()))) {
                                            isFind = true;
                                            tblInventoryRequestDetailId = requestDetailId;
                                            break;
                                        }
                                    }
                                }
                                if (!isFind) {
                                    tblInventoryRequestDetailId = new TblInventoryRequestDetailId();
                                    tblInventoryRequestDetailId.setUuid(IDGenerator.generate()); // ID
                                    tblInventoryRequestDetailId.setRequestDetailId(inventoryRequestDetailUuid); // 棚卸依頼明細ID
                                    tblInventoryRequestDetailId.setMoldId(assetRelation.getMoldId()); // 金型ID
                                    tblInventoryRequestDetailId.setMachineId(assetRelation.getMachineId()); // 設備ID
                                    tblInventoryRequestDetailId.setCreateDate(sysDate);
                                    tblInventoryRequestDetailId.setCreateUserUuid(userUuid);
                                }
                            } else {
                                tblInventoryRequestDetailId = new TblInventoryRequestDetailId();
                                tblInventoryRequestDetailId.setUuid(IDGenerator.generate()); // ID
                                tblInventoryRequestDetailId.setRequestDetailId(inventoryRequestDetailUuid); // 棚卸依頼明細ID
                                tblInventoryRequestDetailId.setMoldId(assetRelation.getMoldId()); // 金型ID
                                tblInventoryRequestDetailId.setMachineId(assetRelation.getMachineId()); // 設備ID
                                tblInventoryRequestDetailId.setCreateDate(sysDate);
                                tblInventoryRequestDetailId.setCreateUserUuid(userUuid);
                            }
                            if (tblInventoryRequestDetailId == null) {
                                continue;
                            }
                            tblInventoryRequestDetailId.setMainFlg(assetRelation.getMainFlg()); // 代表フラグ
                            tblInventoryRequestDetailId.setUpdateDate(sysDate);
                            tblInventoryRequestDetailId.setUpdateUserUuid(userUuid);
                            entityManager.merge(tblInventoryRequestDetailId);
                        }
                    }
                }
            } else {
                basicResponse.setError(true);
                basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_data_deleted"));
            }
        } else {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_data_deleted"));
        }
        
        return basicResponse;
    }
    
    /**
     * 実施IDにより棚卸実施名称取得
     *
     * @param inventoryId
     * @return 
     */
    public String getInventoryNameById(String inventoryId) {
        if (inventoryId != null && !"".equals(inventoryId)) {
            List<TblInventory> tblInventoryList = entityManager.createNamedQuery("TblInventory.findByUuid")
                .setParameter("uuid", inventoryId).getResultList();

            if (tblInventoryList != null && !tblInventoryList.isEmpty()) {
                return tblInventoryList.get(0).getName();
            }
        }
        return "";
    }
    
    /**
     * サプライヤーへ送信用添付ファイル作成
     *
     * @param tblInventoryMgmtCompanyVoList
     * @param inventoryName
     * @param langId
     * @return 
     */
    public FileReponse createMgmtCompanyZipFile(TblInventoryMgmtCompanyVoList tblInventoryMgmtCompanyVoList, String inventoryName, String langId) {
        FileReponse response = new FileReponse();
        if (tblInventoryMgmtCompanyVoList != null && tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany() != null) {
            String inventoryId = tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getTblInventoryMgmtCompanyPK().getInventoryId();
            if (inventoryName == null || "".equals(inventoryName)) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_data_deleted"));
                return response;
            }

            // ファイルコピー
            StringBuilder srcFileDir = new StringBuilder(kartePropertyService.getDocumentDirectory());
            srcFileDir.append(FileUtil.SEPARATOR).append(CommonConstants.REPORT)
                .append(FileUtil.SEPARATOR).append(CommonConstants.INVENTORY_REQUEST)
                .append(FileUtil.SEPARATOR).append(inventoryId).append(FileUtil.SEPARATOR);

            StringBuilder destFileDir = new StringBuilder(kartePropertyService.getDocumentDirectory());
            destFileDir.append(FileUtil.SEPARATOR).append(CommonConstants.WORK)
                .append(FileUtil.SEPARATOR).append(CommonConstants.INVENTORY_REQUEST)
                .append(FileUtil.SEPARATOR).append(inventoryId)
                .append(FileUtil.SEPARATOR).append(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getFileUuid()).append(FileUtil.SEPARATOR);

            FileUtil.deleteDir(new File(destFileDir.toString()));
            
            StringBuilder tempName = new StringBuilder();
            // 棚卸実施名称 + '_' + 管理先コード + 会社名称 '.xlsx'
            String newFileName = tempName.append(inventoryName).append(UNDER_YOKO).append(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getTblInventoryMgmtCompanyPK().getMgmtCompanyCode())
                .append(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getCompanyName()).toString();
            String srcFileName, destFileName = "";
            
            srcFileName = srcFileDir.toString() + tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getFileUuid() + CommonConstants.EXT_EXCEL;
            destFileName = destFileDir.toString() + tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getFileUuid() + CommonConstants.EXT_EXCEL;
            FileUtil.fileChannelCopy(srcFileName, destFileName, true);
            FileUtil.renameFile(destFileDir.toString(), tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getFileUuid() + CommonConstants.EXT_EXCEL, newFileName + CommonConstants.EXT_EXCEL);
            
            if (tblInventoryMgmtCompanyVoList.getTblInventorySendHistoryAttachments() != null && !tblInventoryMgmtCompanyVoList.getTblInventorySendHistoryAttachments().isEmpty()) {
                StringBuilder attachFileDir = new StringBuilder(kartePropertyService.getDocumentDirectory());
                attachFileDir.append(FileUtil.SEPARATOR).append(CommonConstants.DOC).append(FileUtil.SEPARATOR);
                String attachFileName = "";
                TblUploadFile tblUploadFile;
                FileUtil fu = new FileUtil();
                for (TblInventorySendHistoryAttachment attachFile : tblInventoryMgmtCompanyVoList.getTblInventorySendHistoryAttachments()) {
                    tblUploadFile = tblUploadFileService.getTblUploadFile(attachFile.getFileName());
                    if (tblUploadFile != null) {
                        attachFileName = tblUploadFile.getUploadFileName();
                    }
                    String filePath = fu.loadFileByFileName(new File(attachFileDir.toString()), attachFile.getFileName());
                    FileUtil.fileChannelCopy(filePath, destFileDir.toString() + attachFileName, true);
                }
            }
            
            boolean charsetFlg = false;
            if (LANGID.equalsIgnoreCase(langId)) {
                charsetFlg = true;
            }

            String password = tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory().getPassword();
            // 圧縮処理を行う
            if (StringUtils.isEmpty(tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory().getPassword())) {
                // ZIPファイル名：棚卸実施名称.zipとする
                password = ZipCompressor.zipWithPassword(destFileDir.toString(), newFileName, charsetFlg);
            } else {
                ZipCompressor.zipWithPassword(destFileDir.toString(), newFileName, tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory().getPassword(), charsetFlg);
            }
            String zipPath = new StringBuilder(kartePropertyService.getDocumentDirectory())
                .append(FileUtil.SEPARATOR).append(CommonConstants.WORK)
                .append(FileUtil.SEPARATOR).append(CommonConstants.INVENTORY_REQUEST)
                .append(FileUtil.SEPARATOR).append(inventoryId).append(FileUtil.SEPARATOR).toString();
            FileUtil.renameFile(zipPath, tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getFileUuid() + CommonConstants.EXT_ZIP, newFileName + CommonConstants.EXT_ZIP);
            FileUtil.fileChannelCopy(zipPath + newFileName + CommonConstants.EXT_ZIP, destFileDir.toString() + newFileName + CommonConstants.EXT_ZIP, true);
            new File(zipPath + newFileName + CommonConstants.EXT_ZIP).delete();
            response.setFileUuid(password);
        }
        return response;
    }
    
    /**
     * サプライヤーへ送信行う
     *
     * @param tblInventoryMgmtCompanyVoList
     * @param password
     * @param inventoryName
     * @param langId
     * @return 
     */
    public BasicResponse sendMailMgmtCompany(TblInventoryMgmtCompanyVoList tblInventoryMgmtCompanyVoList, String password, String inventoryName, String langId) {
        BasicResponse response = new BasicResponse();
        if (tblInventoryMgmtCompanyVoList != null && tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany() != null && tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory() != null) {
            // 資産管理窓口メールアドレス取得
            List<String> receiverList = Arrays.asList(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getMailAddress());
            // CCメールアドレス取得
            List<String> ccList = Arrays.asList(tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory().getCcAddress().split(","));
            // 件名取得
            String subject = tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory().getSubject();
            // 本文内容取得
            StringBuilder mailBody = new StringBuilder();
            String mailContent = tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory().getMailContent();
            // 会社名称チェックボックス判定
            if (tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory().getAddresseeCompany() == 1) {
                if (!StringUtils.isEmpty(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getCompanyName())) {
                    mailBody.append(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getCompanyName()).append(" ");
                }
            }
            // 資産管理窓口部署チェックボックス判定
            if (tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory().getAddresseeDepartment() == 1) {
                if (!StringUtils.isEmpty(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getDepartment())) {
                    mailBody.append(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getDepartment()).append(" ");
                }
            }
            // 資産管理窓口氏名チェックボックス判定
            if (tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory().getAddresseeName() == 1) {
                if (!StringUtils.isEmpty(tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory().getHonorific())) {
                    // 敬称は氏名の後の場合
                    if (tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory().getHonorificPlace() == 0) {
                        if (!StringUtils.isEmpty(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getContactName())) {
                            mailBody.append(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getContactName());
                            mailBody.append(tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory().getHonorific());
                        }
                    // 敬称は氏名の前の場合
                    } else {
                        if (!StringUtils.isEmpty(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getContactName())) {
                            mailBody.append(tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory().getHonorific());
                            mailBody.append(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getContactName());
                        }
                    }
                }
            }
            // 本文内容まで空白行追加
            if (mailBody.length() > 0) {
                mailBody.append(LINE_SEPARATOR);
                mailBody.append(LINE_SEPARATOR);
            }
            mailBody.append(mailContent);
            // 添付ファイルパス取得
            StringBuilder filePath = new StringBuilder(kartePropertyService.getDocumentDirectory());
            filePath.append(FileUtil.SEPARATOR).append(CommonConstants.WORK)
                .append(FileUtil.SEPARATOR).append(CommonConstants.INVENTORY_REQUEST)
                .append(FileUtil.SEPARATOR).append(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getTblInventoryMgmtCompanyPK().getInventoryId())
                .append(FileUtil.SEPARATOR).append(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getFileUuid())
                .append(FileUtil.SEPARATOR).append(inventoryName).append(UNDER_YOKO).append(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getTblInventoryMgmtCompanyPK().getMgmtCompanyCode())
                .append(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getCompanyName()).append(CommonConstants.EXT_ZIP);
            if (receiverList != null && !receiverList.isEmpty()) {
                try {
                    mailSender.setMakePlainTextBody(true);
                    mailSender.sendMailWithAttachment(receiverList, ccList, subject, mailBody.toString(), filePath.toString());
                    subject += ("(" + mstDictionaryService.getDictionaryValue(langId, "mail_password_notice_subject") + ")");
                    password = mstDictionaryService.getDictionaryValue(langId, "mail_password_notice_body") + LINE_SEPARATOR + LINE_SEPARATOR + password;
                    Thread.sleep(3000);
                    mailSender.sendMail(receiverList, ccList, subject, password);
                } catch (IOException | MessagingException | InterruptedException ex) {
                    response.setError(true);
                    response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    response.setErrorMessage(ex.getMessage());
                    Logger.getLogger(TblInventoryMgmtCompanyService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return response;
    }
    /**
     * 棚卸実施テーブルに実施依頼日、実施ステータス更新
     * <p>
     * 棚卸管理先テーブルに対象管理先の依頼日更新
     * </p>
     * 
     * @param tblInventoryMgmtCompanyVoList
     * @param userUuid
     * @param langId
     * @return 
     */
    @Transactional
    public BasicResponse updateInventory(TblInventoryMgmtCompanyVoList tblInventoryMgmtCompanyVoList, String userUuid, String langId) {
        BasicResponse response = new BasicResponse();
        if (tblInventoryMgmtCompanyVoList != null && tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany() != null) {
            Date sysDate = DateFormat.strToDatetime(DateFormat.getCurrentDateTime());
            // 棚卸管理先情報取得
            List<TblInventoryMgmtCompany> mgmtCompanyList = entityManager.createNamedQuery("TblInventoryMgmtCompany.findByInventoryId")
                .setParameter("inventoryId", tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getTblInventoryMgmtCompanyPK().getInventoryId())
                .getResultList();
            if (mgmtCompanyList != null && !mgmtCompanyList.isEmpty()) {
                for (TblInventoryMgmtCompany tblInventoryMgmtCompany : mgmtCompanyList) {
                    if (tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompany().getTblInventoryMgmtCompanyPK().getMgmtCompanyCode().equals(tblInventoryMgmtCompany.getTblInventoryMgmtCompanyPK().getMgmtCompanyCode())) {
                        // 依頼日設定し更新する
                        tblInventoryMgmtCompany.setRequestedDate(sysDate);
                        tblInventoryMgmtCompany.setUpdateDate(sysDate);
                        tblInventoryMgmtCompany.setUpdateUserUuid(userUuid);
                        entityManager.merge(tblInventoryMgmtCompany);
                        break;
                    }
                }
            } else {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_data_deleted"));
                return response;
            }
            // 棚卸実施情報取得
            TblInventory tblInventory = tblInventoryMgmtCompanyVoList.getTblInventory();
            if (tblInventory != null) {
                if (tblInventory.getRequestedDate() == null || sysDate.compareTo(tblInventory.getRequestedDate()) < 0) {
                    // 実施依頼日設定
                    tblInventory.setRequestedDate(sysDate);
                }
                Query query = entityManager.createNamedQuery("TblInventoryMgmtCompany.findAllRequested").setParameter("inventoryId", tblInventory.getUuid());
                List count = query.getResultList();
                if (Integer.parseInt(count.get(0).toString()) > 0) {
                    // 実施ステータス3:一部実施依頼済みに設定
                    tblInventory.setStatus(CommonConstants.INVENTORY_STATUS_PART_REQUEST);
                } else {
                    // 実施ステータス4:実施依頼済みに設定
                    tblInventory.setStatus(CommonConstants.INVENTORY_STATUS_ALL_REQUEST);
                }
                tblInventory.setUpdateDate(sysDate);
                tblInventory.setUpdateUserUuid(userUuid);
                entityManager.merge(tblInventory);
            } else {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_data_deleted"));
                return response;
            }
        }
        return response;
    }
    
    /**
     * 外部会社から棚卸依頼データを取得する
     *
     * @param userId
     * @return 
     */
    public TblInventoryRequestDetailVoList getExternalInventoryList(String userId) {
        TblInventoryRequestDetailVoList tblInventoryRequestDetailVoList = new TblInventoryRequestDetailVoList();
        List<TblInventoryRequestDetailVo> detailVoList = new ArrayList();
        Set<String> requestUuids = new HashSet();
        Query query = entityManager.createNamedQuery("TblInventoryRequestDetail.findExternalStatusIsUnsent");
        query.setParameter("status", CommonConstants.INVENTORY_REQUEST_STATUS_ANSWER_READY);
        query.setParameter("apiUserId", userId);
        
        List<TblInventoryRequestDetail> list = query.getResultList();
        for (TblInventoryRequestDetail tblInventoryRequestDetail : list) {
            TblInventoryRequestDetailVo requestVo = new TblInventoryRequestDetailVo();
            requestUuids.add(tblInventoryRequestDetail.getTblInventoryRequest().getUuid());
            requestVo.setInventoryId(tblInventoryRequestDetail.getTblInventoryRequest().getInventoryId());
            requestVo.setAssetNo(tblInventoryRequestDetail.getAssetNo());
            requestVo.setBranchNo(tblInventoryRequestDetail.getBranchNo());
            requestVo.setExistence(tblInventoryRequestDetail.getExistence());
            requestVo.setNoExistenceReason(tblInventoryRequestDetail.getNoExistenceReason());
            // 所在地変更情報のカルテ間連携 LYD S
            //・所在変更有無	CHANGE_LOCATION	INT(3)	0:未選択（ブランク）、1:有、2:無
            requestVo.setChangeLocation(tblInventoryRequestDetail.getChangeLocation());
            //・変更後所在地	NEW_LOCATION	VARCHAR(100)
            requestVo.setNewLocation(tblInventoryRequestDetail.getNewLocation());
            //・変更後所在地住所	NEW_LOCATION_ADDRESS	VARCHAR(100)
            requestVo.setNewLocationAddress(tblInventoryRequestDetail.getNewLocationAddress());
            // 所在地変更情報のカルテ間連携 LYD E
            detailVoList.add(requestVo);
        }
        
        tblInventoryRequestDetailVoList.setTblInventoryRequestDetailVos(detailVoList);
        tblInventoryRequestDetailVoList.setRequestUuids(requestUuids);
        return tblInventoryRequestDetailVoList;
    }
    
    /**
     * 外部会社から取得された棚卸依頼回答データを自社に格納する用
     *
     * @param tblInventoryRequestDetails
     * @param userUuid
     * @param companyId
     * @return 
     */
    @Transactional
    public boolean saveExternalInventoryList(List<TblInventoryRequestDetailVo> tblInventoryRequestDetails, String userUuid, String companyId) {

        Query query = entityManager.createNamedQuery("TblInventoryDetail.findByInventoryId");
        boolean isCommited = false;
        String inventoryId = "";
        List<TblInventoryDetail> details = null;
        Date sysDate = DateFormat.strToDatetime(DateFormat.getCurrentDateTime());
        List<MstContact> mgmtCompanyCodeList = entityManager.createQuery("SELECT contact FROM MstContact contact WHERE contact.companyId = :companyId AND contact.assetManagementFlg = 1")
                .setParameter("companyId", companyId).getResultList();
        String mgmtCompanyCode = mgmtCompanyCodeList == null || mgmtCompanyCodeList.isEmpty() ? "" : mgmtCompanyCodeList.get(0).getMgmtCompanyCode();

        for (TblInventoryRequestDetailVo requestDetail : tblInventoryRequestDetails) {
            if (!inventoryId.equals(requestDetail.getInventoryId())) {
                isCommited = false;
                query.setParameter("inventoryId", requestDetail.getInventoryId());
                details = query.getResultList();

                boolean receivedFlg = false;
                // 棚卸管理先テーブル更新
                List<TblInventoryMgmtCompany> mgmtCompanyList = entityManager.createNamedQuery("TblInventoryMgmtCompany.findByInventoryId")
                        .setParameter("inventoryId", requestDetail.getInventoryId())
                        .getResultList();
                if (mgmtCompanyList != null && !mgmtCompanyList.isEmpty()) {
                    for (TblInventoryMgmtCompany mgmtCompany : mgmtCompanyList) {
                        if (mgmtCompany.getTblInventoryMgmtCompanyPK().getMgmtCompanyCode().equals(mgmtCompanyCode)) {
                            if (mgmtCompany.getReceivedDate() == null) {
                                receivedFlg = true;
                            }
                            mgmtCompany.setReceivedDate(sysDate); // 回収日
                            mgmtCompany.setUpdateDate(sysDate);
                            mgmtCompany.setUpdateUserUuid(userUuid);
                            entityManager.merge(mgmtCompany);
                            entityManager.flush();
                            entityManager.clear();
                            isCommited = true;
                            break;
                        }
                    }
                }

                // 棚卸実施テーブル更新
                List<TblInventory> tblInventoryList = entityManager.createNamedQuery("TblInventory.findByUuid")
                        .setParameter("uuid", requestDetail.getInventoryId())
                        .setMaxResults(1).getResultList();
                if (isCommited && tblInventoryList != null && !tblInventoryList.isEmpty()) {
                    boolean isComplete = true;
                    tblInventoryList.get(0).setStatus(CommonConstants.INVENTORY_STATUS_ALL_RECEIVE); // 実施ステータス
                    if (mgmtCompanyList != null && !mgmtCompanyList.isEmpty()) {
                        for (TblInventoryMgmtCompany mgmtCompany : mgmtCompanyList) {
                            if (mgmtCompany.getReceivedDate() == null) {
                                isComplete = false;
                                break;
                            }
                        }
                    }
                    if (!isComplete) {
                        tblInventoryList.get(0).setStatus(CommonConstants.INVENTORY_STATUS_PART_RECEIVE); // 実施ステータス
                    }
                    if (receivedFlg) {
                        tblInventoryList.get(0).setReceivedMgmtCompanyCount(tblInventoryList.get(0).getReceivedMgmtCompanyCount() + 1); // 回収済み取引先数
                    }
                    tblInventoryList.get(0).setUpdateDate(sysDate);
                    tblInventoryList.get(0).setUpdateUserUuid(userUuid);
                    entityManager.merge(tblInventoryList.get(0));
                    entityManager.flush();
                    entityManager.clear();
                }

                inventoryId = requestDetail.getInventoryId();
            }

            // 棚卸明細テーブル更新
            if (isCommited && details != null) {
                for (TblInventoryDetail detail : details) {
                    if (detail.getMstAsset().getMstAssetPK().getAssetNo().equals(requestDetail.getAssetNo())
                            && detail.getMstAsset().getMstAssetPK().getBranchNo().equals(requestDetail.getBranchNo())) {
                        detail.setExistence(requestDetail.getExistence()); // 現品有無
                        detail.setNoExistenceReason(requestDetail.getNoExistenceReason()); // 無理由

                        // 所在地変更情報のカルテ間連携 LYD S
                        //・所在変更有無	CHANGE_LOCATION	INT(3)	0:未選択（ブランク）、1:有、2:無
                        detail.setChangeLocation(requestDetail.getChangeLocation());
                        //・変更後所在地	NEW_LOCATION	VARCHAR(100)
                        detail.setNewLocation(requestDetail.getNewLocation());
                        //・変更後所在地住所	NEW_LOCATION_ADDRESS	VARCHAR(100)
                        detail.setNewLocationAddress(requestDetail.getNewLocationAddress());
                        // 所在地変更情報のカルテ間連携 LYD E

                        detail.setUpdateDate(sysDate);
                        detail.setUpdateUserUuid(userUuid);
                        entityManager.merge(detail);
                        entityManager.flush();
                        entityManager.clear();
                        break;
                    }
                }
            }
        }

        return isCommited;
    }
    
    /**
     * 棚卸依頼ステータスをstatusに更新する
     *
     * @param requestUuids
     * @param userUuid
     * @return 
     */
    @Transactional
    public BasicResponse pushExternalInventoryRequest(Set<String> requestUuids, String userUuid) {        
        if (requestUuids != null && !requestUuids.isEmpty()) {
            for (String requestUuid : requestUuids) {
                Query query = entityManager.createNamedQuery("TblInventoryRequest.findByUuid");
                query.setParameter("uuid", requestUuid);
                try {
                    TblInventoryRequest request = (TblInventoryRequest) query.getSingleResult();
                    request.setStatus(CommonConstants.INVENTORY_REQUEST_STATUS_ANSWER_DONE);
                    request.setSendResponseDate(new Date());
                    request.setUpdateDate(new Date());
                    request.setUpdateUserUuid(userUuid);
                    entityManager.merge(request);
                } catch (NoResultException e) {
                    // Do Nothing
                }
            }
        }
        return new BasicResponse();
    }
    
    /**
     * 棚卸依頼先CSV出力
     *
     * @param tblInventoryMgmtCompanyList
     * @param userUuid
     * @param langId
     * @return 
     */
    @Transactional
    public FileReponse getInventoryMgmtCompanyCsv(List<TblInventoryMgmtCompany> tblInventoryMgmtCompanyList, String userUuid, String langId) {
        /*
         * Header用意
         */
        List<String> dictKeyList = Arrays.asList("asset_inventory_companylist", "mgmt_company_code", "company_name", "location_name", "asset_mgmt_department", "asset_mgmt_tel_no", "asset_mgmt_position",
                "asset_mgmt_contact_name", "asset_mgmt_mail_address", "inventory_asset_count", "inventory_mold_asset_count", "inventory_machine_asset_count", "asset_due_date", 
                "requested_date", "received_date","mgmt_location_changed_flg", "mgmt_company_changed_flg", "existence_diff_flg", "existence_yes", "existence_no");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        
        FileReponse fr = new FileReponse();
        
        /*
         * Header設定
         */
        ArrayList csvOutHeadList = new ArrayList();
        csvOutHeadList.add(headerMap.get("mgmt_company_code"));
        csvOutHeadList.add(headerMap.get("company_name"));
        csvOutHeadList.add(headerMap.get("location_name"));
        csvOutHeadList.add(headerMap.get("asset_mgmt_department"));
        csvOutHeadList.add(headerMap.get("asset_mgmt_tel_no"));
        csvOutHeadList.add(headerMap.get("asset_mgmt_position"));
        csvOutHeadList.add(headerMap.get("asset_mgmt_contact_name"));
        csvOutHeadList.add(headerMap.get("asset_mgmt_mail_address"));
        csvOutHeadList.add(headerMap.get("inventory_asset_count"));
        csvOutHeadList.add(headerMap.get("inventory_mold_asset_count"));
        csvOutHeadList.add(headerMap.get("inventory_machine_asset_count"));
        csvOutHeadList.add(headerMap.get("asset_due_date"));
        csvOutHeadList.add(headerMap.get("requested_date"));
        csvOutHeadList.add(headerMap.get("received_date"));
        csvOutHeadList.add(headerMap.get("mgmt_location_changed_flg"));
        csvOutHeadList.add(headerMap.get("mgmt_company_changed_flg"));
        csvOutHeadList.add(headerMap.get("existence_diff_flg"));

        // 出力データ準備
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        gLineList.add(csvOutHeadList);

        // データ行
        ArrayList<String> line;
        
        // 日付変換用
        SimpleDateFormat sdfDate = new SimpleDateFormat(DateFormat.DATE_FORMAT);

        if (tblInventoryMgmtCompanyList != null && !tblInventoryMgmtCompanyList.isEmpty()) {
            
            for (TblInventoryMgmtCompany tblInventoryMgmtCompany : tblInventoryMgmtCompanyList) {
                line = new ArrayList<>();
                // 管理先コード
                line.add(tblInventoryMgmtCompany.getTblInventoryMgmtCompanyPK().getMgmtCompanyCode());
                
                // 会社名称
                if (tblInventoryMgmtCompany.getCompanyName() != null) {
                    line.add(tblInventoryMgmtCompany.getCompanyName());
                } else {
                    line.add("");
                }
                // 所在地名称
                if (tblInventoryMgmtCompany.getLocationName() != null) {
                    line.add(tblInventoryMgmtCompany.getLocationName());
                } else {
                    line.add("");
                }
                // 資産管理窓口部署
                if (tblInventoryMgmtCompany.getDepartment() != null) {
                    line.add(tblInventoryMgmtCompany.getDepartment());
                } else {
                    line.add("");
                }
                // 資産管理窓口電話番号
                if (tblInventoryMgmtCompany.getTelNo() != null) {
                    line.add(tblInventoryMgmtCompany.getTelNo());
                } else {
                    line.add("");
                }
                // 資産管理窓口役職
                if (tblInventoryMgmtCompany.getPosition() != null) {
                    line.add(tblInventoryMgmtCompany.getPosition());
                } else {
                    line.add("");
                }
                // 資産管理窓口氏名
                if (tblInventoryMgmtCompany.getContactName() != null) {
                    line.add(tblInventoryMgmtCompany.getContactName());
                } else {
                    line.add("");
                }
                // 資産管理窓口メールアドレス
                if (tblInventoryMgmtCompany.getMailAddress() != null) {
                    line.add(tblInventoryMgmtCompany.getMailAddress());
                } else {
                    line.add("");
                }
                // 棚卸対象資産数
                line.add(String.valueOf(tblInventoryMgmtCompany.getInventoryAssetCount()));
                // 棚卸対象金型資産数
                line.add(String.valueOf(tblInventoryMgmtCompany.getInventoryMoldAssetCount()));
                // 棚卸対象設備資産数
                line.add(String.valueOf(tblInventoryMgmtCompany.getInventoryMachineAssetCount()));
                // 回収期限
                if (tblInventoryMgmtCompany.getDueDate() != null) {
                    line.add(sdfDate.format(tblInventoryMgmtCompany.getDueDate()));
                } else {
                    line.add("");
                }
                // 依頼日
                if (tblInventoryMgmtCompany.getRequestedDate() != null) {
                    line.add(sdfDate.format(tblInventoryMgmtCompany.getRequestedDate()));
                } else {
                    line.add("");
                }
                // 回収日
                if (tblInventoryMgmtCompany.getReceivedDate() != null) {
                    line.add(sdfDate.format(tblInventoryMgmtCompany.getReceivedDate()));
                } else {
                    line.add("");
                }
                // 設置場所変更有無
                switch (tblInventoryMgmtCompany.getMgmtLocationChangedFlg()) {
                    case 1:
                        line.add(headerMap.get("existence_yes"));
                        break;
                    case 2:
                        line.add(headerMap.get("existence_no"));
                        break;
                    default:
                        line.add("");
                        break;
                }
                // 管理先変更有無
                switch (tblInventoryMgmtCompany.getMgmtCompanyChangedFlg()) {
                    case 1:
                        line.add(headerMap.get("existence_yes"));
                        break;
                    case 2:
                        line.add(headerMap.get("existence_no"));
                        break;
                    default:
                        line.add("");
                        break;
                }
                // 現品差異有無
                switch (tblInventoryMgmtCompany.getExistenceDiffFlg()) {
                    case 1:
                        line.add(headerMap.get("existence_yes"));
                        break;
                    case 2:
                        line.add(headerMap.get("existence_no"));
                        break;
                    default:
                        line.add("");
                        break;
                }
                // 行を明細リストに追加
                gLineList.add(line);
            }
        }

        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_INVENTORY_MGMT_COMPANY);
        tblCsvExport.setExportDate(new java.util.Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_INVENTORY_EXECUTION);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(userUuid);
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(headerMap.get("asset_inventory_companylist")));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }
    
    /**
     * 棚卸依頼先一覧取得SQL
     * 
     * @param inventoryId
     * @param action
     * @return 
     */
    private List<TblInventoryMgmtCompany> getInventoryMgmtCompanys(String inventoryId, String action) {
        StringBuilder sql = new StringBuilder();
        
        if (COUNT_STR.equals(action)) {
            sql.append(" SELECT COUNT(1) ");
        } else {
            sql.append(" SELECT company ");
        }
        
        sql.append(" FROM TblInventoryMgmtCompany company ");
        sql.append(" WHERE 1=1 ");
        
        // 棚卸実施ID
        if (!StringUtils.isEmpty(inventoryId)) {
            sql.append(" AND company.tblInventoryMgmtCompanyPK.inventoryId = :inventoryId ");
        }
        
        Query query = entityManager.createQuery(sql.toString());
        
        // パラメータ設定
        if (!StringUtils.isEmpty(inventoryId)) {
            query.setParameter("inventoryId", inventoryId);
        }
        
        List list = query.getResultList();
        return list;
    }
    
    /**
     * 棚卸の資産情報を取得
     * 
     * @param inventoryId
     * 
     * @return
     */
    public List<TblInventoryDetail> getAllInventoryAssetList(String inventoryId) {

        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT inventoryDetail ");
        sql.append(" FROM TblInventoryDetail inventoryDetail ");
        sql.append(" JOIN FETCH inventoryDetail.mstAsset mstAsset ");
        sql.append(" LEFT JOIN FETCH mstAsset.mstMgmtLocation mstMgmtLocation ");
        sql.append(" LEFT JOIN FETCH mstAsset.mstItem mstItem ");
        sql.append(" LEFT JOIN FETCH inventoryDetail.mstMgmtLocation newMstMgmtLocation ");
        sql.append(" LEFT JOIN FETCH inventoryDetail.mstMgmtCompany newMstMgmtCompany ");
        sql.append(" WHERE 1=1 ");

        // 棚卸実施ID
        if (!StringUtils.isEmpty(inventoryId)) {
            sql.append("AND inventoryDetail.tblInventoryDetailPK.inventoryId = :inventoryId ");
        }

        sql.append(" ORDER BY mstAsset.mgmtCompanyCode,  mstAsset.itemCode");

        Query query = entityManager.createQuery(sql.toString());

        // パラーメタ設定
        if (!StringUtils.isEmpty(inventoryId)) {
            query.setParameter("inventoryId", inventoryId);
        }

        List list = query.getResultList();
        return list;
    }

    /**
     * 棚卸の全データCSV出力
     *
     * @param inventoryId
     * @param tblInventoryDetailList
     * @param userUuid
     * @param langId
     * @return
     */
    @Transactional
    public FileReponse getAllInventoryAssetCsv(String inventoryId, List<TblInventoryDetail> tblInventoryDetailList,
            String userUuid, String langId) {

        /*
         * Header用意
         */
        List<String> dictKeyList = Arrays.asList("mgmt_company_code", "company_name", "asset_no", "branch_no",
                "asset_type", "asset_name", "mgmt_location_code", "mgmt_location_name", "item_code", "asset_item_name",
                "acquisition_type", "acquisition_yyyymm", "mold_existence", "no_existence_reason", "change_location",
                "new_location", "new_location_address", "new_mgmt_location_code", "new_mgmt_location_name",
                "new_mgmt_company_code", "new_mgmt_company_name", "existence_yes", "existence_no");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);

        String[] choiceArray = { ASSET_TYPE, ACQUISITION_TYPE };

        Map<String, String> choiceMap = mstChoiceService.getChoiceMap(langId, choiceArray);

        Map<String, String> companyMap = getCompanyMap(inventoryId);

        FileReponse fr = new FileReponse();

        /*
         * Header設定
         */
        ArrayList csvOutHeadList = new ArrayList();
        csvOutHeadList.add(headerMap.get("mgmt_company_code"));
        csvOutHeadList.add(headerMap.get("company_name"));
        csvOutHeadList.add(headerMap.get("asset_no"));
        csvOutHeadList.add(headerMap.get("branch_no"));
        csvOutHeadList.add(headerMap.get("asset_type"));
        csvOutHeadList.add(headerMap.get("asset_name"));
        csvOutHeadList.add(headerMap.get("mgmt_location_code"));
        csvOutHeadList.add(headerMap.get("mgmt_location_name"));
        csvOutHeadList.add(headerMap.get("item_code"));
        csvOutHeadList.add(headerMap.get("asset_item_name"));
        csvOutHeadList.add(headerMap.get("acquisition_type"));
        csvOutHeadList.add(headerMap.get("acquisition_yyyymm"));
        csvOutHeadList.add(headerMap.get("mold_existence"));
        csvOutHeadList.add(headerMap.get("no_existence_reason"));
        csvOutHeadList.add(headerMap.get("change_location"));
        csvOutHeadList.add(headerMap.get("new_location"));
        csvOutHeadList.add(headerMap.get("new_location_address"));
        csvOutHeadList.add(headerMap.get("new_mgmt_location_code"));
        csvOutHeadList.add(headerMap.get("new_mgmt_location_name"));
        csvOutHeadList.add(headerMap.get("new_mgmt_company_code"));
        csvOutHeadList.add(headerMap.get("new_mgmt_company_name"));

        // 出力データ準備
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        gLineList.add(csvOutHeadList);

        // データ行
        ArrayList<String> line;

        // 日付変換用
        SimpleDateFormat sdfDate = new SimpleDateFormat(DateFormat.DATE_FORMAT);

        if (tblInventoryDetailList != null && !tblInventoryDetailList.isEmpty()) {

            for (TblInventoryDetail tblInventoryDetail : tblInventoryDetailList) {

                line = new ArrayList<String>();

                // 管理先コード
                line.add(tblInventoryDetail.getMstAsset().getMgmtCompanyCode());

                // 会社名称
                if (StringUtils.isNotEmpty(companyMap.get(tblInventoryDetail.getMstAsset().getMgmtCompanyCode()))) {
                    line.add(companyMap.get(tblInventoryDetail.getMstAsset().getMgmtCompanyCode()));
                } else {
                    line.add("");
                }

                // 資産番号
                line.add(tblInventoryDetail.getMstAsset().getMstAssetPK().getAssetNo());

                // 補助番号
                line.add(tblInventoryDetail.getMstAsset().getMstAssetPK().getBranchNo());

                // 資産種類
                line.add(choiceMap.get(ASSET_TYPE + String.valueOf(tblInventoryDetail.getMstAsset().getAssetType())));

                // 資産名称
                if (StringUtils.isNotEmpty(tblInventoryDetail.getMstAsset().getAssetName())) {
                    line.add(tblInventoryDetail.getMstAsset().getAssetName());
                } else {
                    line.add("");
                }

                // 所在先コード
                if (StringUtils.isNotEmpty(tblInventoryDetail.getMstAsset().getMgmtLocationCode())) {
                    line.add(tblInventoryDetail.getMstAsset().getMgmtLocationCode());
                } else {
                    line.add("");
                }

                // 設置場所
                if (tblInventoryDetail.getMstAsset().getMstMgmtLocation() != null) {

                    if (StringUtils
                            .isNotEmpty(tblInventoryDetail.getMstAsset().getMstMgmtLocation().getMgmtLocationName())) {

                        line.add(tblInventoryDetail.getMstAsset().getMstMgmtLocation().getMgmtLocationName());
                    } else {
                        line.add("");
                    }
                } else {
                    line.add("");
                }

                // 品目コード
                if (StringUtils.isNotEmpty(tblInventoryDetail.getMstAsset().getItemCode())) {
                    line.add(tblInventoryDetail.getMstAsset().getItemCode());
                } else {
                    line.add("");
                }

                // 品目名称
                if (tblInventoryDetail.getMstAsset().getMstItem() != null) {

                    if (StringUtils.isNotEmpty(tblInventoryDetail.getMstAsset().getMstItem().getItemName())) {

                        line.add(tblInventoryDetail.getMstAsset().getMstItem().getItemName());
                    } else {
                        line.add("");
                    }
                } else {
                    line.add("");
                }

                // 取得区分
                line.add(choiceMap
                        .get(ACQUISITION_TYPE + String.valueOf(tblInventoryDetail.getMstAsset().getAcquisitionType())));

                // 取得年月
                if (StringUtils.isNotEmpty(tblInventoryDetail.getMstAsset().getAcquisitionYyyymm())) {
                    line.add(tblInventoryDetail.getMstAsset().getAcquisitionYyyymm());
                } else {
                    line.add("");
                }

                // 金型有無
                if (1 == tblInventoryDetail.getExistence()) {
                    line.add(headerMap.get("existence_yes"));
                } else if (2 == tblInventoryDetail.getExistence()) {
                    line.add(headerMap.get("existence_no"));
                } else {
                    line.add("");
                }

                // 無理由
                if (StringUtils.isNotEmpty(tblInventoryDetail.getNoExistenceReason())) {
                    line.add(tblInventoryDetail.getNoExistenceReason());
                } else {
                    line.add("");
                }

                // 所在変更有無
                if (1 == tblInventoryDetail.getChangeLocation()) {
                    line.add(headerMap.get("existence_yes"));
                } else if (2 == tblInventoryDetail.getChangeLocation()) {
                    line.add(headerMap.get("existence_no"));
                } else {
                    line.add("");
                }

                // 変更後所在地
                if (StringUtils.isNotEmpty(tblInventoryDetail.getNewLocation())) {
                    line.add(tblInventoryDetail.getNewLocation());
                } else {
                    line.add("");
                }

                // 変更後所在地住所
                if (StringUtils.isNotEmpty(tblInventoryDetail.getNewLocationAddress())) {
                    line.add(tblInventoryDetail.getNewLocationAddress());
                } else {
                    line.add("");
                }

                // 変更後所在先コード
                if (StringUtils.isNotEmpty(tblInventoryDetail.getNewMgmtLocationCode())) {
                    line.add(tblInventoryDetail.getNewMgmtLocationCode());
                } else {
                    line.add("");
                }

                // 変更後設置場所
                if (null != tblInventoryDetail.getMstMgmtLocation()) {

                    if (StringUtils.isNotEmpty(tblInventoryDetail.getMstMgmtLocation().getMgmtLocationName())) {

                        line.add(tblInventoryDetail.getMstMgmtLocation().getMgmtLocationName());
                    } else {
                        line.add("");
                    }

                } else {
                    line.add("");
                }

                // 変更後管理先コード
                if (StringUtils.isNotEmpty(tblInventoryDetail.getNewMgmtCompanyCode())) {
                    line.add(tblInventoryDetail.getNewMgmtCompanyCode());
                } else {
                    line.add("");
                }

                // 変更後管理先名称
                if (null != tblInventoryDetail.getMstMgmtCompany()) {

                    if (StringUtils.isNotEmpty(tblInventoryDetail.getMstMgmtCompany().getMgmtCompanyName())) {

                        line.add(tblInventoryDetail.getMstMgmtCompany().getMgmtCompanyName());
                    } else {
                        line.add("");
                    }

                } else {
                    line.add("");
                }

                // 行を明細リストに追加
                gLineList.add(line);
            }
        }

        TblInventory ｔblInventory = tblInventoryService.getInventoryById(inventoryId);

        String csvName = "";

        if (ｔblInventory != null) {

            csvName = ｔblInventory.getName();
        }

        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_INVENTORY_MGMT_COMPANY);
        tblCsvExport.setExportDate(new java.util.Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_INVENTORY_EXECUTION);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(userUuid);
        tblCsvExport.setClientFileName(csvName + ".csv");
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        // csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }

    /**
     * 会社情報を取得する
     *
     * @param inventoryId
     * @return
     */
    private Map<String, String> getCompanyMap(String inventoryId) {

        Map<String, String> companyMap = new HashMap<String, String>();

        Query query = entityManager.createNamedQuery("TblInventoryMgmtCompany.findByInventoryId");
        query.setParameter("inventoryId", inventoryId);

        List<TblInventoryMgmtCompany> list = query.getResultList();

        if (list.size() > 0) {

            for (TblInventoryMgmtCompany tblInventoryMgmtCompany : list) {

                companyMap.put(tblInventoryMgmtCompany.getTblInventoryMgmtCompanyPK().getMgmtCompanyCode(),
                        tblInventoryMgmtCompany.getCompanyName());
            }
        }

        return companyMap;

    }
}
