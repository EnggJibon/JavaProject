/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.mgmt.company;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.batch.inventory.InventoryInfoCreateTemplate;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.asset.inventory.TblInventory;
import com.kmcj.karte.resources.asset.inventory.detail.TblInventoryDetail;
import com.kmcj.karte.resources.asset.inventory.excelvo.R0001InventoryRequest;
import com.kmcj.karte.resources.asset.inventory.request.detail.TblInventoryRequestDetailVoList;
import com.kmcj.karte.resources.asset.relation.TblMoldMachineAssetRelation;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author
 */
@RequestScoped
@Path("inventory/mgmt/company")
public class TblInventoryMgmtCompanyResource {
    
    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private TblInventoryMgmtCompanyService tblInventoryMgmtCompanyService;
    
    @Inject
    private KartePropertyService kartePropertyService;
    
    @Inject
    private MstChoiceService mstChoiceService;
    
    /**
     * T0022_資産棚卸依頼先_件数検索
     *
     * @param inventoryId
     * @return
     */
    @GET
    @Path("search/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getInventoryMgmtCompanyCount(@QueryParam("inventoryId") String inventoryId) {
        return tblInventoryMgmtCompanyService.getInventoryMgmtCompanyCount(inventoryId);
    }
    
    /**
     * T0022_資産棚卸依頼先_検索
     *
     * @param inventoryId
     * @return
     */
    @GET
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblInventoryMgmtCompanyVoList getInventoryMgmtCompanyList(@QueryParam("inventoryId") String inventoryId) {
        TblInventoryMgmtCompanyVoList tblInventoryMgmtCompanyVoList = new TblInventoryMgmtCompanyVoList();
        List<TblInventoryMgmtCompany> list = tblInventoryMgmtCompanyService.getInventoryMgmtCompanyList(inventoryId);
        tblInventoryMgmtCompanyVoList.setTblInventoryMgmtCompanys(list);
        return tblInventoryMgmtCompanyVoList;
    }
    
    /**
     * T0022_資産棚卸依頼先_保存
     *
     * @param tblInventoryMgmtCompanyVoList
     * @return
     */
    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblInventoryMgmtCompanyVoList postInventoryMgmtCompany(TblInventoryMgmtCompanyVoList tblInventoryMgmtCompanyVoList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblInventoryMgmtCompanyVoList response = tblInventoryMgmtCompanyService.postInventoryMgmtCompany(tblInventoryMgmtCompanyVoList, loginUser.getUserUuid());
        return response;
    }
    
    /**
     * T0022_資産棚卸依頼先_棚卸依頼票ダウンロード
     *
     * @param tblInventoryMgmtCompanyVoList
     * @return
     */
    @POST
    @Path("download")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse downloadExcel(TblInventoryMgmtCompanyVoList tblInventoryMgmtCompanyVoList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        FileReponse response = tblInventoryMgmtCompanyService.downloadExcel(tblInventoryMgmtCompanyVoList, loginUser.getLangId());
        return response;
    }
    
    /**
     * T0022_資産棚卸依頼先_棚卸実施送信履歴取得
     *
     * @param inventoryId
     * @return
     */
    @GET
    @Path("sending/history")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblInventoryMgmtCompanyVoList getInventorySendingHistory(@QueryParam("inventoryId") String inventoryId) {
        TblInventoryMgmtCompanyVoList response = tblInventoryMgmtCompanyService.getInventorySendingHistory(inventoryId);
        return response;
    }
    
    /**
     * T0022_資産棚卸依頼先_棚卸依頼送信OKボタン
     *
     * @param tblInventoryMgmtCompanyVoList
     * @return
     */
    @POST
    @Path("sending")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse sendInventoryMgmtCompany(TblInventoryMgmtCompanyVoList tblInventoryMgmtCompanyVoList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        
        TblInventoryMgmtCompanyVoList postInventoryMgmtCompanyVoList = new TblInventoryMgmtCompanyVoList();
        TblInventoryMgmtCompanyVoList threadPostInventoryMgmtCompanyVoList = new TblInventoryMgmtCompanyVoList();
        // 送信履歴登録
        BasicResponse response = tblInventoryMgmtCompanyService.postInventorySendHistory(tblInventoryMgmtCompanyVoList, loginUser.getUserUuid());
        if (!StringUtils.isEmpty(response.getErrorCode())) {
            return response;
        }
        
        // サプライヤーへデータ連携
        // 登録用データ用意
        // 棚卸実施情報取得
        TblInventory tblInventory = tblInventoryMgmtCompanyService.getTblInventoryById(tblInventoryMgmtCompanyVoList);
        postInventoryMgmtCompanyVoList.setTblInventory(tblInventory);
        threadPostInventoryMgmtCompanyVoList.setTblInventory(tblInventory);
        
        // 各管理先コード毎サプライヤーへデータ連携行う
        if (tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys() != null && !tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys().isEmpty()) {
            // 管理先情報取得
            List<TblInventoryMgmtCompany> tblInventoryMgmtCompanyList = 
                    tblInventoryMgmtCompanyService.getTblInventoryMgmtCompanyById(tblInventoryMgmtCompanyVoList);
            // 棚卸実施明細情報取得
            List<TblInventoryDetail> tblInventoryDetails = tblInventoryMgmtCompanyService.getInventoryDetailById(tblInventoryMgmtCompanyVoList);
            // 棚卸実施名称取得
            String inventoryName = tblInventoryMgmtCompanyService.getInventoryNameById(tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys().get(0).getTblInventoryMgmtCompanyPK().getInventoryId());
            
            MstChoiceList assetTypeChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_asset.asset_type");
            
            ExecutorService executor = Executors.newFixedThreadPool(1);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    BasicResponse threadResponse = new BasicResponse();
                    // 帳票ファイルパス：document/report/inventory_request/棚卸実施ID/
                    StringBuilder srcFileDir = new StringBuilder(kartePropertyService.getDocumentDirectory());
                    srcFileDir.append(FileUtil.SEPARATOR).append(CommonConstants.REPORT)
                        .append(FileUtil.SEPARATOR).append(CommonConstants.INVENTORY_REQUEST)
                        .append(FileUtil.SEPARATOR).append(tblInventory.getUuid()).append(FileUtil.SEPARATOR);
                    for (TblInventoryMgmtCompany mgmtCompany : tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys()) {
                        // 帳票の実施依頼日更新
                        if (DateFormat.strToDate(DateFormat.getCurrentDate()).compareTo(tblInventory.getRegistrationDate()) != 0) {
                            Map<String, R0001InventoryRequest> param = new HashMap();
                            // 帳票オブジェクトリスト(更新値設定)
                            R0001InventoryRequest request = new R0001InventoryRequest();
                            
                            // パス設定
                            request.setOutputPath(srcFileDir.toString() + mgmtCompany.getFileUuid() + CommonConstants.EXT_EXCEL);
                            param.put(mgmtCompany.getTblInventoryMgmtCompanyPK().getMgmtCompanyCode(), request);
                            
                            InventoryInfoCreateTemplate inventoryInfoCreateTemplate = new InventoryInfoCreateTemplate();
                            try {
                                inventoryInfoCreateTemplate.updateRequestDate(param);
                            } catch (IOException ex) {
                                Logger.getLogger(TblInventoryMgmtCompanyResource.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        
                        if (tblInventoryMgmtCompanyList != null && !tblInventoryMgmtCompanyList.isEmpty()) {
                            for (TblInventoryMgmtCompany tblInventoryMgmtCompany : tblInventoryMgmtCompanyList) {
                                if (mgmtCompany.getTblInventoryMgmtCompanyPK().getMgmtCompanyCode().equals(tblInventoryMgmtCompany.getTblInventoryMgmtCompanyPK().getMgmtCompanyCode())) {
                                    tblInventoryMgmtCompany.setDueDateStr(DateFormat.dateToStr(tblInventoryMgmtCompany.getDueDate(), DateFormat.DATE_FORMAT));
                                    threadPostInventoryMgmtCompanyVoList.setTblInventoryMgmtCompany(tblInventoryMgmtCompany);

                                    List<TblInventoryDetail> companyCodeInventoryDetails = new ArrayList();
                                    if (tblInventoryDetails != null && !tblInventoryDetails.isEmpty()) {
                                        for (TblInventoryDetail tblInventoryDetail : tblInventoryDetails) {
                                            if (mgmtCompany.getTblInventoryMgmtCompanyPK().getMgmtCompanyCode().equals(tblInventoryDetail.getMstAsset().getMgmtCompanyCode())) {
                                                if (tblInventoryDetail.getMstAsset().getTblMoldMachineAssetRelationVos() != null && !tblInventoryDetail.getMstAsset().getTblMoldMachineAssetRelationVos().isEmpty()) {
                                                    for (TblMoldMachineAssetRelation tblMoldMachineAssetRelation : tblInventoryDetail.getMstAsset().getTblMoldMachineAssetRelationVos()) {
                                                        tblMoldMachineAssetRelation.setMoldId(tblMoldMachineAssetRelation.getMstMold() == null ? null : tblMoldMachineAssetRelation.getMstMold().getMoldId());
                                                        tblMoldMachineAssetRelation.setMachineId(tblMoldMachineAssetRelation.getMstMachine() == null ? null : tblMoldMachineAssetRelation.getMstMachine().getMachineId());
                                                        tblMoldMachineAssetRelation.setMstMold(null);
                                                        tblMoldMachineAssetRelation.setMstMachine(null);
                                                    }
                                                }
                                                // 資産種類設定しておく
                                                if (assetTypeChoiceList.getMstChoice() != null && !assetTypeChoiceList.getMstChoice().isEmpty()) {
                                                    for (MstChoice mstChoice : assetTypeChoiceList.getMstChoice()) {
                                                        if (String.valueOf(tblInventoryDetail.getMstAsset().getAssetType()).equals(mstChoice.getMstChoicePK().getSeq())) {
                                                            tblInventoryDetail.setAssetTypeStr(mstChoice.getChoice());
                                                            break;
                                                        }
                                                    }
                                                }
                                                // 設置場所設定
                                                tblInventoryDetail.setMgmtLocationNameStr(tblInventoryDetail.getMstAsset().getMstMgmtLocation() == null ? "" : tblInventoryDetail.getMstAsset().getMstMgmtLocation().getMgmtLocationName());
                                                // 品目名称設定
                                                tblInventoryDetail.setItemNameStr(tblInventoryDetail.getMstAsset().getMstItem() == null ? "" : tblInventoryDetail.getMstAsset().getMstItem().getItemName());
                                                companyCodeInventoryDetails.add(tblInventoryDetail);
                                            }
                                        }
                                    }
                                    threadPostInventoryMgmtCompanyVoList.setTblInventoryDetails(companyCodeInventoryDetails);
                                    // データPOST
                                    threadResponse = tblInventoryMgmtCompanyService.pushGetExternalInventoryRequest(threadPostInventoryMgmtCompanyVoList, loginUser.getLangId());
                                    // TODO:失敗の場合、画面にメッセージを返す
        //                            if ("1".equals(response.getErrorCode())) {
        //                                // 連携失敗
        //                                response.setError(true);
        //                                response.setErrorMessage("");
        //                            } else if ("2".equals(response.getErrorCode())) {
        //                                
        //                            } else {
        //                                
        //                            }
                                    if (!StringUtils.isEmpty(threadResponse.getErrorCode())) {
                                        break;
                                    }

                                    threadPostInventoryMgmtCompanyVoList.setMaxSeqInventorySendHistory(tblInventoryMgmtCompanyVoList.getMaxSeqInventorySendHistory());
                                    threadPostInventoryMgmtCompanyVoList.setTblInventorySendHistoryAttachments(tblInventoryMgmtCompanyVoList.getTblInventorySendHistoryAttachments());
                                    // 管理先送信ファイル作成
                                    FileReponse fileResponse = tblInventoryMgmtCompanyService.createMgmtCompanyZipFile(threadPostInventoryMgmtCompanyVoList, inventoryName, loginUser.getLangId());

                                    // サプライヤーへ送信行う
                                    tblInventoryMgmtCompanyService.sendMailMgmtCompany(threadPostInventoryMgmtCompanyVoList, fileResponse.getFileUuid(), inventoryName, loginUser.getLangId());

                                    break;
                                }
                            }
                        }
                    }
                }
            });
            executor.shutdown();
                            
            for (TblInventoryMgmtCompany mgmtCompany : tblInventoryMgmtCompanyVoList.getTblInventoryMgmtCompanys()) {
                if (tblInventoryMgmtCompanyList != null && !tblInventoryMgmtCompanyList.isEmpty()) {
                    for (TblInventoryMgmtCompany tblInventoryMgmtCompany : tblInventoryMgmtCompanyList) {
                        if (mgmtCompany.getTblInventoryMgmtCompanyPK().getMgmtCompanyCode().equals(tblInventoryMgmtCompany.getTblInventoryMgmtCompanyPK().getMgmtCompanyCode())) {
                            tblInventoryMgmtCompany.setDueDateStr(DateFormat.dateToStr(tblInventoryMgmtCompany.getDueDate(), DateFormat.DATE_FORMAT));
                            postInventoryMgmtCompanyVoList.setTblInventoryMgmtCompany(tblInventoryMgmtCompany);
                            
                            // 棚卸実施テーブルに実施依頼日、実施ステータス更新/棚卸管理先テーブルに対象管理先の依頼日更新
                            response = tblInventoryMgmtCompanyService.updateInventory(postInventoryMgmtCompanyVoList, loginUser.getUserUuid(), loginUser.getLangId());
                            
                            break;
                        }
                    }
                }
            }
        }
        
        return response;
    }
    
    /**
     * T0022_資産棚卸依頼先_CSV出力ボタン
     *
     * @param inventoryId
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getInventoryMgmtCompanyCsv(@QueryParam("inventoryId") String inventoryId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        // データ取得
        List<TblInventoryMgmtCompany> list = tblInventoryMgmtCompanyService.getInventoryMgmtCompanyList(inventoryId);
        
        FileReponse response = tblInventoryMgmtCompanyService.getInventoryMgmtCompanyCsv(list, loginUser.getUserUuid(), loginUser.getLangId());
        return response;
    }
    
    /**
     * T0022_資産棚卸依頼先_サプライヤーへPOST
     *
     * @param tblInventoryMgmtCompanyVoList
     * @return
     */
    @POST
    @Path("push/request")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postExternalInventoryRequest(TblInventoryMgmtCompanyVoList tblInventoryMgmtCompanyVoList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        
        BasicResponse response = tblInventoryMgmtCompanyService.postExternalInventoryRequest(tblInventoryMgmtCompanyVoList, loginUser.getCompanyId(), loginUser.getUserUuid(), loginUser.getLangId());
        return response;
    }
    
    /**
     * サプライヤーから回答送信準備済の棚卸依頼データを取得する
     *
     * @return
     */
    @GET
    @Path("extdata/get")
    @Produces(MediaType.APPLICATION_JSON)
    public TblInventoryRequestDetailVoList getExternalInventoryList() {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblInventoryMgmtCompanyService.getExternalInventoryList(loginUser.getUserid());
    }
    
    /**
     * サプライヤーに回答送信済状態を棚卸依頼データ更新する
     *
     * @param requestUuids
     * @return 
     */
    @POST
    @Path("extdata/push")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse pushExternalInventoryRequest(Set<String> requestUuids) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblInventoryMgmtCompanyService.pushExternalInventoryRequest(requestUuids, loginUser.getUserUuid());
    }
    
    /**
     * T0022_資産棚卸依頼先_全データ出力ボタン
     *
     * @param inventoryId
     * @return
     */
    @GET
    @Path("exportallassetcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getAllInventoryAssetCsv(@QueryParam("inventoryId") String inventoryId) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        // データ取得
        List<TblInventoryDetail> list = tblInventoryMgmtCompanyService.getAllInventoryAssetList(inventoryId);

        FileReponse response = tblInventoryMgmtCompanyService.getAllInventoryAssetCsv(inventoryId, list,
                loginUser.getUserUuid(), loginUser.getLangId());
        return response;
    }
}
