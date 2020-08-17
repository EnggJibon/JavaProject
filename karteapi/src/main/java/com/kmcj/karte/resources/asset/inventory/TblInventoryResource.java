/*
 * To chnge this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.asset.MstAssetList;
import com.kmcj.karte.resources.asset.inventory.detail.TblInventoryDetail;
import com.kmcj.karte.resources.asset.inventory.detail.TblInventoryDetailVo;
import com.kmcj.karte.resources.asset.inventory.detail.TblInventoryDetailVoList;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.batch.operations.JobOperator;
import javax.batch.operations.NoSuchJobException;
import javax.batch.runtime.BatchRuntime;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("inventory")
public class TblInventoryResource {

    /**
     * Creates a new instance of TblInventoryResource
     */
    public TblInventoryResource() {
    }

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblInventoryService tblInventoryService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     * T0021_資産棚卸実施登録_検索ボタン
     *
     * @param status
     * @return an instance of getTblInventoryList
     */
    @GET
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblInventoryVoList getTblInventoryList(@QueryParam("status") int status) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblInventoryService.getTblInventoryList(status, loginUser.getLangId());
    }

    /**
     * T0021_資産棚卸実施登録_追加ダイアログで[OK]ボタン
     *
     * @param inventoryName　必須
     * @param mstAssetList
     * @return
     */
    @POST
    @Path("add/{inventoryName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postTblInventory(@PathParam("inventoryName") String inventoryName, MstAssetList mstAssetList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();

        if (StringUtils.isEmpty(inventoryName)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String itemName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "inventory_name");
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null_with_item");
            msg = String.format(msg, itemName);
            response.setErrorMessage(msg);
            return response;
        }

        // バッチを起動する。
        JobOperator job = BatchRuntime.getJobOperator();
        List<Long> karteBatchlets = null;

        try {
            karteBatchlets = job.getRunningExecutions("inventoryInfoCreateBatchlet");
        } catch (NoSuchJobException ne) {
            //do nothing
        }
        if (karteBatchlets != null && karteBatchlets.size() > 0) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            // 棚卸実行中です、少々お待ちください。
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_inventory_execution"));
        } else {
            String inventoryId = tblInventoryService.postTblInventory(inventoryName, loginUser.getUserUuid());
            Properties props = new Properties();
            //棚卸実施ID
            props.setProperty("inventoryId", inventoryId);

            tblInventoryService.postWkAssetInventory(inventoryId,mstAssetList.getMstAssetList());

            job.start("inventoryInfoCreateBatchlet", props);
        }
        return response;
    }

    /**
     * T0021_資産棚卸実施登録_削除ボタン
     *
     * @param inventoryId
     * @return
     */
    @DELETE
    @Path("delete/{inventoryId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteTblInventory(@PathParam("inventoryId") String inventoryId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblInventoryService.deleteTblInventory(inventoryId, loginUser.getLangId());
    }

    /**
     * T0021_資産棚卸実施登録_変更データ出力ボタン
     *
     * @param inventoryId
     * @return
     */
    @GET
    @Path("change")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getChangeTblInventoryData(@QueryParam("inventoryId") String inventoryId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblInventoryService.getChangeTblInventoryData(inventoryId, loginUser);
    }
    
    /**
     * T0021_資産棚卸実施登録_新規所在先一覧ボタン
     *
     * @param inventoryId
     * @return
     */
    @GET
    @Path("mgmtloctionlist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblInventoryDetailVoList getAllInventoryAssetCsv(@QueryParam("inventoryId") String inventoryId) {
        
        TblInventoryDetailVoList response = new TblInventoryDetailVoList();

        // データ取得
        List<TblInventoryDetailVo> list = tblInventoryService.getMgmtLoctionList(inventoryId);
        response.setTblInventoryDetailVos(list);

        return response;
    }
    
    /**
     * T0021_資産棚卸実施登録_棚卸ステータス変更
     *
     * @param inventoryId
     * @param completeFlag
     * @return
     */
    @GET
    @Path("changestatus")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse chkChangeTblInventoryData(@QueryParam("inventoryId") String inventoryId,
            @QueryParam("completeFlag") Boolean completeFlag) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return tblInventoryService.updTblInventoryStatus(inventoryId, completeFlag, loginUser);
    }

}
