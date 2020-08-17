/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.inventory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.UrlDecodeInterceptor;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.util.FileUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
/**
 *
 * @author admin
 */
@RequestScoped
@Path("mold/inventory")
public class TblMoldInventoryResource {

    public TblMoldInventoryResource() {

    }

    @Inject
    private TblMoldInventoryService tblMoldInventoryService;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private CnfSystemService cnfSystemService;
    
    @Inject
    private MstChoiceService mstChoiceService;
    
    /**
     * 金型棚卸照会 検索 金型棚卸テーブル件数取得
     *
     * @param moldId
     * @param moldName
     * @param ownerCompanyName
     * @param companyName
     * @param locationName
     * @param instllationSiteName
     * @param latestInventoryDateStart
     * @param latestInventoryDateEnd
     * @param status
     * @param depart
     * @param stocktakeIncomplete
     * @return
     */
    @GET
    @Path("getmolds")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldInventoryList getMoldsByPage(@QueryParam("moldId") String moldId,
            @QueryParam("moldName") String moldName,
            @QueryParam("ownerCompanyName") String ownerCompanyName,
            @QueryParam("companyName") String companyName,
            @QueryParam("locationName") String locationName,
            @QueryParam("instllationSiteName") String instllationSiteName,
            @QueryParam("latestInventoryDateStart") String latestInventoryDateStart,
            @QueryParam("latestInventoryDateEnd") String latestInventoryDateEnd,
            @QueryParam("status") String status,
            @QueryParam("department") String depart,
            @QueryParam("pageNumber") int pageNumber, // ページNo
            @QueryParam("pageSize") int pageSize,
            @QueryParam("sidx") String sidx, // ソートキー
            @QueryParam("sord") String sord,
            @QueryParam("stocktakeIncomplete") int stocktakeIncomplete
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        Gson gson = new Gson();
        List<Integer> iStatus = gson.fromJson(status, new TypeToken<List<Integer>>(){}.getType());
        TblMoldInventoryList tblMoldInvList = tblMoldInventoryService.getMoldsByPage(moldId, moldName, ownerCompanyName, companyName, locationName, instllationSiteName, 
                latestInventoryDateStart, latestInventoryDateEnd, iStatus, depart, loginUser, sidx, sord, pageNumber, pageSize, stocktakeIncomplete, true);

        return tblMoldInvList;
    }
    
    /**
     * 金型棚卸照会 検索 金型棚卸テーブル件数取得
     *
     * @param moldId
     * @param moldName
     * @param ownerCompanyName
     * @param companyName
     * @param locationName
     * @param instllationSiteName
     * @param latestInventoryDateStart
     * @param latestInventoryDateEnd
     * @param status
     * @param stocktakeIncomplete
     * @return
     */
    @GET
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getRecordCount(@QueryParam("moldId") String moldId,
            @QueryParam("moldName") String moldName,
            @QueryParam("ownerCompanyName") String ownerCompanyName,
            @QueryParam("companyName") String companyName,
            @QueryParam("locationName") String locationName,
            @QueryParam("instllationSiteName") String instllationSiteName,
            @QueryParam("latestInventoryDateStart") String latestInventoryDateStart,
            @QueryParam("latestInventoryDateEnd") String latestInventoryDateEnd,
            @QueryParam("status") String status,
            @QueryParam("stocktakeIncomplete") int stocktakeIncomplete
    ) {
        
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblMoldInventorys paraMoldInventorys = new TblMoldInventorys(moldId, moldName, ownerCompanyName, companyName, locationName, instllationSiteName, latestInventoryDateStart, latestInventoryDateEnd, status);
        CountResponse count = tblMoldInventoryService.getTblMoldInventoryCount(paraMoldInventorys, loginUser, stocktakeIncomplete);

        CnfSystem cnf = cnfSystemService.findByKey("system", "max_list_record_count");
        long sysCount = Long.parseLong(cnf.getConfigValue());
        if (count.getCount() > sysCount) {

            count.setError(true);
            count.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_confirm_max_record_count");
            msg = String.format(msg, sysCount);
            count.setErrorMessage(msg);
        }
        return count;
    }

    /**
     * 金型棚卸照会 検索 金型棚卸テーブル複数取得
     *
     * @param moldId
     * @param moldName
     * @param ownerCompanyName
     * @param companyName
     * @param locationName
     * @param instllationSiteName
     * @param latestInventoryDateStart
     * @param latestInventoryDateEnd
     * @param status
     * @param stocktakeIncomplete
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldInventorys getMoldInventories(@QueryParam("moldId") String moldId,
            @QueryParam("moldName") String moldName,
            @QueryParam("ownerCompanyName") String ownerCompanyName,
            @QueryParam("companyName") String companyName,
            @QueryParam("locationName") String locationName,
            @QueryParam("instllationSiteName") String instllationSiteName,
            @QueryParam("latestInventoryDateStart") String latestInventoryDateStart,
            @QueryParam("latestInventoryDateEnd") String latestInventoryDateEnd,
            @QueryParam("status") String status,
            @QueryParam("stocktakeIncomplete") int stocktakeIncomplete
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblMoldInventorys resMoldInventorys = new TblMoldInventorys();
        TblMoldInventorys paraMoldInventorys = new TblMoldInventorys(moldId, moldName, ownerCompanyName, companyName, locationName, instllationSiteName, latestInventoryDateStart, latestInventoryDateEnd, status);
        resMoldInventorys.setMoldInventorys(tblMoldInventoryService.getMoldInventories(paraMoldInventorys, loginUser, stocktakeIncomplete));
        return resMoldInventorys;
    }

    /**
     * 金型棚卸履歴 画面レイアウト 金型マスタ、金型棚卸テーブルよりデータを取得し
     *
     * @param moldId
     * @return
     */
    @GET
    @Path("history")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Interceptors({UrlDecodeInterceptor.class})
    public TblMoldInventorys getMoldInventoryHistories(@QueryParam("moldId") String moldId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblMoldInventorys tblMoldInventorys = tblMoldInventoryService.getMoldInventoriesHistories(moldId, loginUser);
        if (null == tblMoldInventorys.getMoldId() || tblMoldInventorys.getMoldId().isEmpty()) {
            tblMoldInventorys.setError(true);
            tblMoldInventorys.setErrorCode(ErrorMessages.E201_APPLICATION);
            tblMoldInventorys.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
        }
        return tblMoldInventorys;
    }

    /**
     * 金型棚卸照会 CSV出力
     *
     * @param moldId
     * @param moldName
     * @param ownerCompanyName
     * @param companyName
     * @param locationName
     * @param instllationSiteName
     * @param latestInventoryDateStart
     * @param latestInventoryDateEnd
     * @param status
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getTblMoldInventorysCsv(@QueryParam("moldId") String moldId,
            @QueryParam("moldName") String moldName,
            @QueryParam("ownerCompanyName") String ownerCompanyName,
            @QueryParam("companyName") String companyName,
            @QueryParam("locationName") String locationName,
            @QueryParam("instllationSiteName") String instllationSiteName,
            @QueryParam("latestInventoryDateStart") String latestInventoryDateStart,
            @QueryParam("latestInventoryDateEnd") String latestInventoryDateEnd,
            @QueryParam("status") String status, 
            @QueryParam("stocktakeIncomplete") int stocktakeIncomplete
    ) {
        TblMoldInventorys paraMoldInventorys = new TblMoldInventorys(moldId, moldName, ownerCompanyName, companyName, locationName, instllationSiteName, latestInventoryDateStart, latestInventoryDateEnd, status);
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        FileReponse fr = null;
        fr = tblMoldInventoryService.getTblMoldInventoryOutputCsv(paraMoldInventorys, loginUser, stocktakeIncomplete);
        return fr;
    }

    /**
     * 金型棚卸履歴.追加 保存
     *
     * @param tblMoldInventorys
     * @return
     */
    @POST
    @Path("history")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMoldInventoryHistorie(TblMoldInventorys tblMoldInventorys) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return tblMoldInventoryService.postMoldInventoryHistorie(tblMoldInventorys, loginUser);
    }

    /**
     * 金型棚卸履歴.削除 削除は最新の履歴のみ可能。
     *
     * @param inventoryId
     * @return
     */
    @DELETE
    @Path("history/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteMoldInventoryHistorie(@PathParam("id") String inventoryId) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMoldInventoryService.deleteMoldInventoryHistorie(inventoryId, loginUser);
    }

    /**
     * 金型棚卸（タブレット）
     * 金型棚卸登録/検索 金型棚卸情報を取得
     * @param ownerCompanyName
     * @param locationId
     * @param companyId
     * @param moldId
     * @param installationSiteId
     * @param department
     * @return 
     */
    @GET
    @Path("detail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldInventoryList getMoldInventories(@QueryParam("ownerCompanyName") String ownerCompanyName,
            @QueryParam("locationId") String locationId,
            @QueryParam("companyId") String companyId,
            @QueryParam("moldId") String moldId,
            @QueryParam("installationSiteId") String installationSiteId,
            @QueryParam("department") int department
            ) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        TblMoldInventoryList tblMoldInventoryList = tblMoldInventoryService.getMoldInventories(moldId, companyId, ownerCompanyName, locationId, installationSiteId, department, loginUser);
        //2018.5.02
        Map<String, String> map = mstChoiceService.getChoiceMap(loginUser.getLangId(), new String[]{"mst_user.department"});
        TblMoldInventoryList response = new TblMoldInventoryList();
        List<TblMoldInventorys> inputlist = new ArrayList<>();

        TblMoldInventorys tblMoldInventorys;
        FileUtil fu = new FileUtil();
        for (int i = 0; i < tblMoldInventoryList.getTblMoldInventoryList().size(); i++) {
            tblMoldInventorys = new TblMoldInventorys();
            MstMold input = tblMoldInventoryList.getTblMoldInventoryList().get(i);
            if (input != null) {

                tblMoldInventorys.setInventoryId(input.getLatestInventoryId() == null ? "" : input.getLatestInventoryId());

                tblMoldInventorys.setMoldId(input.getMoldId() == null ? "" : input.getMoldId());
                tblMoldInventorys.setMoldName(input.getMoldName() == null ? "" : input.getMoldName());
                tblMoldInventorys.setMoldUuid(input.getUuid() == null ? "" : input.getUuid());

                if (null != input.getMstCompanyByOwnerCompanyId()) {
                    tblMoldInventorys.setOwnerCompanyId(input.getMstCompanyByOwnerCompanyId().getId() == null ? "" : input.getMstCompanyByOwnerCompanyId().getId());
                    tblMoldInventorys.setOwnerCompanyName(input.getMstCompanyByOwnerCompanyId().getCompanyName() == null ? "" : input.getMstCompanyByOwnerCompanyId().getCompanyName());
                }

                tblMoldInventorys.setCompanyId(input.getCompanyId() == null ? "" : input.getCompanyId());
                tblMoldInventorys.setCompanyName(input.getCompanyName() == null ? "" : input.getCompanyName());

                tblMoldInventorys.setLocationId(input.getLocationId() == null ? "" : input.getLocationId());
                tblMoldInventorys.setLocationName(input.getLocationName() == null ? "" : input.getLocationName());

                tblMoldInventorys.setInstllationSiteId(input.getInstllationSiteId() == null ? "" : input.getInstllationSiteId());
                tblMoldInventorys.setInstllationSiteName(input.getInstllationSiteName() == null ? "" : input.getInstllationSiteName());

                tblMoldInventorys.setDepartment(String.valueOf(input.getDepartment()) == null ? "0" : String.valueOf(input.getDepartment()));
                tblMoldInventorys.setDepartmentText(map.get("mst_user.department" + input.getDepartment()));  
                if (null != input.getTblMoldInventory()) {
                    tblMoldInventorys.setMoldConfirmMethod(String.valueOf(input.getTblMoldInventory().getMoldConfirmMethod()));
                    tblMoldInventorys.setSiteConfirmMethod(String.valueOf(input.getTblMoldInventory().getSiteConfirmMethod()));
                    tblMoldInventorys.setInputType(String.valueOf(input.getTblMoldInventory().getInputType()));
                    if (input.getTblMoldInventory().getInventoryDate() != null && !"".equals(String.valueOf(input.getTblMoldInventory().getInventoryDate()))) {
                        tblMoldInventorys.setLatestInventoryDate(fu.getDateTimeFormatForStr(input.getTblMoldInventory().getInventoryDate()));
                    } else {
                        tblMoldInventorys.setLatestInventoryDate("");
                    }
                    tblMoldInventorys.setInventoryResult(String.valueOf(input.getTblMoldInventory().getInventoryResult()));
                    tblMoldInventorys.setRemarks(input.getTblMoldInventory().getRemarks() == null ? "" : input.getTblMoldInventory().getRemarks());
                    tblMoldInventorys.setImgFilePath(input.getTblMoldInventory().getImgFilePath()== null ? "" : input.getTblMoldInventory().getImgFilePath());
                    if (input.getTblMoldInventory().getFileType() != null) {
                        tblMoldInventorys.setFileType(String.valueOf(input.getTblMoldInventory().getFileType()));
                    } else {
                        tblMoldInventorys.setFileType("0");
                    }
                    if(input.getTblMoldInventory().getTakenDate() != null){
                        tblMoldInventorys.setTakenDate(fu.getDateTimeFormatForStr(input.getTblMoldInventory().getTakenDate()));
                    }else{
                        tblMoldInventorys.setTakenDate("");
                    }
                    if(input.getTblMoldInventory().getTakenDateStz() != null){
                        tblMoldInventorys.setTakenDateStz(fu.getDateTimeFormatForStr(input.getTblMoldInventory().getTakenDateStz()));
                    }else{
                        tblMoldInventorys.setTakenDateStz("");
                    }
                    
                    //2018.4.26              
                     if(input.getTblMoldInventory().getDepartmentChange()!= null){
                        tblMoldInventorys.setDepartmentChange(String.valueOf(input.getTblMoldInventory().getDepartmentChange()));
                    }else{
                        tblMoldInventorys.setDepartmentChange("0");
                    } 
                      if(input.getTblMoldInventory().getAssetDamaged()!= null){
                        tblMoldInventorys.setAssetDamaged(String.valueOf(input.getTblMoldInventory().getAssetDamaged()));
                    }else{
                        tblMoldInventorys.setAssetDamaged("0");
                    } 
                       if(input.getTblMoldInventory().getBarcodeReprint()!= null){
                        tblMoldInventorys.setBarcodeReprint(String.valueOf(input.getTblMoldInventory().getBarcodeReprint()));
                    }else{
                        tblMoldInventorys.setBarcodeReprint("0");
                    } 
                        if(input.getTblMoldInventory().getNotInUse()!= null){
                        tblMoldInventorys.setNotInUse(String.valueOf(input.getTblMoldInventory().getNotInUse()));
                    }else{
                        tblMoldInventorys.setNotInUse("0");
                    } 
                    if (input.getInventoryStatus()== 0) {
                        tblMoldInventorys.setLatestInventoryDate("");
                        tblMoldInventorys.setInventoryResult("");
                        tblMoldInventorys.setDepartmentChange("0");
                        tblMoldInventorys.setAssetDamaged("0");
                        tblMoldInventorys.setBarcodeReprint("0");
                        tblMoldInventorys.setNotInUse("0");
                    }
         
                } else {
                    tblMoldInventorys.setLatestInventoryDate("");
                    tblMoldInventorys.setInventoryResult("");
                    tblMoldInventorys.setRemarks("");
                    tblMoldInventorys.setMoldConfirmMethod("");
                    tblMoldInventorys.setInputType("");
                    tblMoldInventorys.setSiteConfirmMethod("");
                    tblMoldInventorys.setImgFilePath("");
                    tblMoldInventorys.setFileType("");
                    tblMoldInventorys.setTakenDate("");
                    tblMoldInventorys.setTakenDateStz("");
                    tblMoldInventorys.setDepartmentChange("0");
                    tblMoldInventorys.setAssetDamaged("0");
                    tblMoldInventorys.setBarcodeReprint("0");
                    tblMoldInventorys.setNotInUse("0");
                }
                inputlist.add(tblMoldInventorys);
            }
        }
        response.setTblMoldInventorys(inputlist);
        return response;
    }

    /**
     * 金型棚卸登録/登録 棚卸結果を登録
     *
     * @param tblMoldInventorys
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMoldInventories(TblMoldInventoryList tblMoldInventorys) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMoldInventoryService.updateMoldInventories(tblMoldInventorys, loginUser);
    }
    
    /**
     * バッチで金型棚卸テーブルデータを取得
     *
     * @param latestExecutedDate
     * @param moldUuid
     * @return
     */
    @GET
    @Path("extmoldinventory")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldInventoryList getExtMoldInventorysByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("moldUuid") String moldUuid) {
        return tblMoldInventoryService.getExtMoldInventorysByBatch(latestExecutedDate, moldUuid);
    }
    
    @POST
    @Path("standarddate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putMachineInventories(TblMoldInventoryList tblMoldInventorys) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMoldInventoryService.updateMoldInventory(tblMoldInventorys, loginUser);
    }
    
}
