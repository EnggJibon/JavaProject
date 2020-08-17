package com.kmcj.karte.resources.machine.inventory;

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
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.MstMachineList;
import com.kmcj.karte.resources.machine.MstMachineService;
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
@Path("machine/inventory")
public class TblMachineInventoryResource {

    public TblMachineInventoryResource() {
    }

    @Inject
    private TblMachineInventoryService tblMachineInventoryService;
    @Inject
    private MstMachineService mstMachineService;

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
     * @param machineId
     * @param machineName
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
    @Path("getmachines")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineInventoryList getMachinesByPage(@QueryParam("machineId") String machineId,
            @QueryParam("machineName") String machineName,
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
        TblMachineInventoryList tblMachineInvList = tblMachineInventoryService.getMachinesByPage(machineId, machineName, ownerCompanyName, companyName, locationName, instllationSiteName, 
                latestInventoryDateStart, latestInventoryDateEnd, iStatus, depart, loginUser, sidx, sord, pageNumber, pageSize, stocktakeIncomplete, true);
 
        return tblMachineInvList;
    }
            
    /**
     * 設備棚卸照会 検索 設備棚卸テーブル件数取得
     *
     * @param machineId
     * @param machineName
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
    public CountResponse getRecordCount(@QueryParam("machineId") String machineId,
            @QueryParam("machineName") String machineName,
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
        TblMachineInventoryVo paraMachineInventorys = new TblMachineInventoryVo(machineId, machineName, ownerCompanyName, companyName, locationName, instllationSiteName, latestInventoryDateStart, latestInventoryDateEnd, status);
        CountResponse count = tblMachineInventoryService.getTblMachineInventoryCount(paraMachineInventorys, loginUser, stocktakeIncomplete);

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
     * 設備棚卸照会 検索 設備棚卸テーブル複数取得
     *
     * @param machineId
     * @param machineName
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
    public TblMachineInventoryVo getMachineInventories(@QueryParam("machineId") String machineId,
            @QueryParam("machineName") String machineName,
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
        TblMachineInventoryVo resMachineInventorys = new TblMachineInventoryVo();
        
        TblMachineInventoryVo paraMachineInventorys = new TblMachineInventoryVo(machineId, machineName, ownerCompanyName, companyName, locationName, instllationSiteName, latestInventoryDateStart, latestInventoryDateEnd, status);
        resMachineInventorys.setTblMachineInventoryVos(tblMachineInventoryService.getMachineInventories(paraMachineInventorys, loginUser, stocktakeIncomplete));
        return resMachineInventorys;
    }

    /**
     * 設備棚卸履歴 画面レイアウト 設備マスタ、設備棚卸テーブルよりデータを取得し
     *
     * @param machineId
     * @return
     */
    @GET
    @Path("history")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Interceptors({UrlDecodeInterceptor.class})
    public TblMachineInventoryVo getMachineInventoryHistories(@QueryParam("machineId") String machineId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblMachineInventoryVo resVo = tblMachineInventoryService.getMachineInventoriesHistories(machineId, loginUser);
        if (null == resVo.getMachineId() || resVo.getMachineId().isEmpty()) {
            resVo.setError(true);
            resVo.setErrorCode(ErrorMessages.E201_APPLICATION);
            resVo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
        }
        return resVo;
    }

    /**
     * 設備棚卸照会 CSV出力
     *
     * @param machineId
     * @param machineName
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
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getTblMachineInventorysCsv(@QueryParam("machineId") String machineId,
            @QueryParam("machineName") String machineName,
            @QueryParam("ownerCompanyName") String ownerCompanyName,
            @QueryParam("companyName") String companyName,
            @QueryParam("locationName") String locationName,
            @QueryParam("instllationSiteName") String instllationSiteName,
            @QueryParam("latestInventoryDateStart") String latestInventoryDateStart,
            @QueryParam("latestInventoryDateEnd") String latestInventoryDateEnd,
            @QueryParam("status") String status,
            @QueryParam("stocktakeIncomplete") int stocktakeIncomplete
    ) {
        TblMachineInventoryVo paraMachineInventorys = new TblMachineInventoryVo(machineId, machineName, ownerCompanyName, companyName, locationName, instllationSiteName, latestInventoryDateStart, latestInventoryDateEnd, status);
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        FileReponse fileReponse = tblMachineInventoryService.getTblMachineInventoryOutputCsv(paraMachineInventorys, loginUser, stocktakeIncomplete);
        return fileReponse;
    }

    /**
     * 設備棚卸履歴.追加 保存
     *
     * @param tblMachineInventorys
     * @return
     */
    @POST
    @Path("history")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMachineInventoryHistorie(TblMachineInventoryVo tblMachineInventorys) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineInventoryService.postMachineInventoryHistorie(tblMachineInventorys, loginUser);
    }

    /**
     * 設備棚卸履歴.削除 削除は最新の履歴のみ可能。
     *
     * @param inventoryId
     * @return
     */
    @DELETE
    @Path("history/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteMachineInventoryHistorie(@PathParam("id") String inventoryId) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineInventoryService.deleteMachineInventoryHistorie(inventoryId, loginUser);
    }

    /**
     * 設備棚卸（タブレット） 設備棚卸登録/検索 設備棚卸情報を取得
     *
     * @param ownerCompanyName
     * @param locationId
     * @param companyId
     * @param machineId
     * @param installationSiteId
     * @param department
     * @return
     */
    @GET
    @Path("detail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineInventoryList getMachineInventories(@QueryParam("ownerCompanyName") String ownerCompanyName,
            @QueryParam("locationId") String locationId,
            @QueryParam("companyId") String companyId,
            @QueryParam("machineId") String machineId,
            @QueryParam("installationSiteId") String installationSiteId,
            @QueryParam("department") int department
    ) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        MstMachineList mstMachineList = mstMachineService.getMachineInventories(machineId, companyId, ownerCompanyName, locationId, installationSiteId, department,loginUser);
        //2018.4.26
        Map<String, String> map = mstChoiceService.getChoiceMap(loginUser.getLangId(), new String[]{"mst_user.department"});
        TblMachineInventoryList response = new TblMachineInventoryList();
        List<TblMachineInventoryVo> inputlist = new ArrayList<>();

        TblMachineInventoryVo tblMachineInventoryVo;
        FileUtil fu = new FileUtil();
        for (int i = 0; i < mstMachineList.getMstMachines().size(); i++) {
            tblMachineInventoryVo = new TblMachineInventoryVo();
            MstMachine aMstMachine = mstMachineList.getMstMachines().get(i);
            if (aMstMachine != null) {

                tblMachineInventoryVo.setInventoryId(aMstMachine.getLatestInventoryId() == null ? "" : aMstMachine.getLatestInventoryId());

                tblMachineInventoryVo.setMachineId(aMstMachine.getMachineId() == null ? "" : aMstMachine.getMachineId());
                tblMachineInventoryVo.setMachineName(aMstMachine.getMachineName() == null ? "" : aMstMachine.getMachineName());
                tblMachineInventoryVo.setMachineUuid(aMstMachine.getUuid() == null ? "" : aMstMachine.getUuid());

                if (null != aMstMachine.getMstCompany()) {
                    tblMachineInventoryVo.setCompanyId(aMstMachine.getMstCompany().getId() == null ? "" : aMstMachine.getMstCompany().getId());
                    tblMachineInventoryVo.setCompanyName(aMstMachine.getMstCompany().getCompanyName() == null ? "" : aMstMachine.getMstCompany().getCompanyName());
                }
                if (null != aMstMachine.getOwnerMstCompany()) {
                    tblMachineInventoryVo.setOwnerCompanyId(aMstMachine.getOwnerMstCompany().getId() == null ? "" : aMstMachine.getOwnerMstCompany().getId());
                    tblMachineInventoryVo.setOwnerCompanyName(aMstMachine.getOwnerMstCompany().getCompanyName() == null ? "" : aMstMachine.getOwnerMstCompany().getCompanyName());
                }

                tblMachineInventoryVo.setLocationId(aMstMachine.getLocationId() == null ? "" : aMstMachine.getLocationId());
                tblMachineInventoryVo.setLocationName(aMstMachine.getLocationName() == null ? "" : aMstMachine.getLocationName());

                tblMachineInventoryVo.setInstllationSiteId(aMstMachine.getInstllationSiteId() == null ? "" : aMstMachine.getInstllationSiteId());
                tblMachineInventoryVo.setInstllationSiteName(aMstMachine.getInstllationSiteName() == null ? "" : aMstMachine.getInstllationSiteName());
                
                tblMachineInventoryVo.setDepartment(String.valueOf(aMstMachine.getDepartment()) == null ? "0" : String.valueOf(aMstMachine.getDepartment()));
                tblMachineInventoryVo.setDepartmentText(map.get("mst_user.department" + aMstMachine.getDepartment()));      
                if (null != aMstMachine.getLatestTblMachineInventory()) {
                    tblMachineInventoryVo.setMachineConfirmMethod(String.valueOf(aMstMachine.getLatestTblMachineInventory().getMachineConfirmMethod()));
                    tblMachineInventoryVo.setSiteConfirmMethod(String.valueOf(aMstMachine.getLatestTblMachineInventory().getSiteConfirmMethod()));
                    tblMachineInventoryVo.setInputType(String.valueOf(aMstMachine.getLatestTblMachineInventory().getInputType()));
                    if (aMstMachine.getLatestTblMachineInventory().getInventoryDate() != null && !"".equals(String.valueOf(aMstMachine.getLatestTblMachineInventory().getInventoryDate()))) {
                        tblMachineInventoryVo.setLatestInventoryDate(fu.getDateTimeFormatForStr(aMstMachine.getLatestTblMachineInventory().getInventoryDate()));
                    } else {
                        tblMachineInventoryVo.setLatestTblMachineInventory("");
                    }
                    tblMachineInventoryVo.setInventoryResult(String.valueOf(aMstMachine.getLatestTblMachineInventory().getInventoryResult()));
                    tblMachineInventoryVo.setRemarks(aMstMachine.getLatestTblMachineInventory().getRemarks() == null ? "" : aMstMachine.getLatestTblMachineInventory().getRemarks());
                    tblMachineInventoryVo.setImgFilePath(aMstMachine.getLatestTblMachineInventory().getImgFilePath() == null ? "" : aMstMachine.getLatestTblMachineInventory().getImgFilePath());
                    if(aMstMachine.getLatestTblMachineInventory().getFileType() != null){
                        tblMachineInventoryVo.setFileType(String.valueOf(aMstMachine.getLatestTblMachineInventory().getFileType()));
                    }else{
                        tblMachineInventoryVo.setFileType("0");
                    }    
                    if(aMstMachine.getLatestTblMachineInventory().getTakenDate() != null){
                        tblMachineInventoryVo.setTakenDate(fu.getDateTimeFormatForStr(aMstMachine.getLatestTblMachineInventory().getTakenDate()));
                    }else{
                        tblMachineInventoryVo.setTakenDate("");
                    }
                    if(aMstMachine.getLatestTblMachineInventory().getTakenDateStz() != null){
                        tblMachineInventoryVo.setTakenDateStz(fu.getDateTimeFormatForStr(aMstMachine.getLatestTblMachineInventory().getTakenDateStz()));
                    }else{
                        tblMachineInventoryVo.setTakenDateStz("");
                    }
                    //2018.4.26                   
                     if(aMstMachine.getLatestTblMachineInventory().getDepartmentChange()!= null){
                        tblMachineInventoryVo.setDepartmentChange(String.valueOf(aMstMachine.getLatestTblMachineInventory().getDepartmentChange()));
                    }else{
                        tblMachineInventoryVo.setDepartmentChange("0");
                    } 
                      if(aMstMachine.getLatestTblMachineInventory().getAssetDamaged()!= null){
                        tblMachineInventoryVo.setAssetDamaged(String.valueOf(aMstMachine.getLatestTblMachineInventory().getAssetDamaged()));
                    }else{
                        tblMachineInventoryVo.setAssetDamaged("0");
                    } 
                       if(aMstMachine.getLatestTblMachineInventory().getBarcodeReprint()!= null){
                        tblMachineInventoryVo.setBarcodeReprint(String.valueOf(aMstMachine.getLatestTblMachineInventory().getBarcodeReprint()));
                    }else{
                        tblMachineInventoryVo.setBarcodeReprint("0");
                    } 
                        if(aMstMachine.getLatestTblMachineInventory().getNotInUse()!= null){
                        tblMachineInventoryVo.setNotInUse(String.valueOf(aMstMachine.getLatestTblMachineInventory().getNotInUse()));
                    }else{
                        tblMachineInventoryVo.setNotInUse("0");
                    }
                    if (aMstMachine.getInventoryStatus() == 0) {
                        tblMachineInventoryVo.setLatestInventoryDate("");
                        tblMachineInventoryVo.setInventoryResult("");
                        tblMachineInventoryVo.setDepartmentChange("0");
                        tblMachineInventoryVo.setAssetDamaged("0");
                        tblMachineInventoryVo.setBarcodeReprint("0");
                        tblMachineInventoryVo.setNotInUse("0");
                    }
                } else {
                    tblMachineInventoryVo.setLatestInventoryDate("");
                    tblMachineInventoryVo.setInventoryResult("");
                    tblMachineInventoryVo.setRemarks("");
                    tblMachineInventoryVo.setMachineConfirmMethod("");
                    tblMachineInventoryVo.setInputType("");
                    tblMachineInventoryVo.setSiteConfirmMethod("");
                    tblMachineInventoryVo.setImgFilePath("");
                    tblMachineInventoryVo.setFileType("0");
                    tblMachineInventoryVo.setTakenDate("");
                    tblMachineInventoryVo.setTakenDateStz("");
                    //2018.4.26
//                    tblMachineInventoryVo.setDepartment("0");
//                    tblMachineInventoryVo.setDepartmentText("");
                    tblMachineInventoryVo.setDepartmentChange("0");
                    tblMachineInventoryVo.setAssetDamaged("0");
                    tblMachineInventoryVo.setBarcodeReprint("0");
                    tblMachineInventoryVo.setNotInUse("0");
                }
                inputlist.add(tblMachineInventoryVo);
            }
        }
        response.setTblMachineInventoryVos(inputlist);
        return response;
    }

    /**
     * 設備棚卸登録/登録 棚卸結果を登録
     *
     * @param tblMachineInventorys
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMachineInventories(TblMachineInventoryList tblMachineInventorys) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineInventoryService.updateMachineInventories(tblMachineInventorys, loginUser);
    }

    /**
     * バッチで設備棚卸テーブルデータを取得
     *
     * @param latestExecutedDate
     * @param machineUuid
     * @return
     */
    @GET
    @Path("extmachineinventory")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineInventoryList getExtMachineInventorysByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("machineUuid") String machineUuid) {
        return tblMachineInventoryService.getExtMachineInventorysByBatch(latestExecutedDate, machineUuid);
    }
    
    @POST
    @Path("standarddate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putMachineInventories(TblMachineInventoryList tblMachineInventorys ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineInventoryService.updateMachineInventory(tblMachineInventorys,loginUser);
    }
    
}
