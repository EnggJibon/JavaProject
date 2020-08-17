/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.lot.balance;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelation;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelationService;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.procedure.MstProcedureList;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * 生産実績ロット残高テーブルリソース
 * @author t.ariki
 */
@RequestScoped
@Path("production/lot/balance")
public class TblProductionLotBalanceResource {
    
    @Context
    private UriInfo context;
    
    @Context
    ContainerRequestContext requestContext;
    
    @Inject
    MstDictionaryService mstDictionaryService;
    
    @Inject
    TblProductionLotBalanceService tblProductionLotBalanceService;
    
    @Inject
    MstProcedureService mstProcedureService;
    
    @Inject
    MstMoldComponentRelationService mstMoldComponentRelationService;
    
    private Logger logger = Logger.getLogger(TblProductionLotBalanceResource.class.getName());

    public TblProductionLotBalanceResource() {}

    /**
     * 生産実績ロット残高テーブル取得
     *
     * @param componentIds
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionLotBalanceList getProductionLotBalances(@QueryParam("componentIds") String componentIds) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        // 検索条件なし
        TblProductionLotBalance tblProductionLotBalance = new TblProductionLotBalance();
        TblProductionLotBalanceList response = tblProductionLotBalanceService.getProductionLotBalancesDistinctByLotNumber(tblProductionLotBalance, componentIds);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }

    /**
     * 生産実績ロット残高テーブル取得
     *
     * @param processNumber
     * @param componentIds
     * @return
     */
    @GET
    @Path("{processNumber}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionLotBalanceList getProductionLotBalances(@PathParam("processNumber") Integer processNumber, @QueryParam("componentIds") String componentIds) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        logger.log(Level.FINE, "PathParam'{'processNumber:{0}'}'", processNumber);
        // 検索条件に工程番号を追加
        TblProductionLotBalance tblProductionLotBalance = new TblProductionLotBalance();
        tblProductionLotBalance.setProcessNumber(processNumber);
        TblProductionLotBalanceList response = tblProductionLotBalanceService.getProductionLotBalancesDistinctByLotNumber(tblProductionLotBalance, componentIds);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }
    
    /**
     * ロット番号指定部品リスト取得
     * @param lotNumber
     * @return 
     */
    @GET
    @Path("/list/procedure/{lotNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public MstProcedureList getProceduresByLotNumber(@PathParam("lotNumber") String lotNumber) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        logger.log(Level.FINE, "PathParam'{'lotNumber:{0}'}'", lotNumber);
        
        // ロット番号最大値+1を取得
        int maxProcessNumber = tblProductionLotBalanceService.getMaxProcessNumber(lotNumber);
        
        // 生産実績ロット残高リスト取得
        TblProductionLotBalanceList tblProductionLotBalances = tblProductionLotBalanceService.getByLotNumberAndMaxProcessNumber(lotNumber, maxProcessNumber-1);
        
        // 取得した生産実績の部品ID数分、次工程(maxProcessNumber)に合致する工程マスタのデータを取得
        MstProcedureList responce = new MstProcedureList();
        ArrayList<MstProcedure> mstProcedures = new ArrayList<>();
        for (TblProductionLotBalance tblProductionLotBalance : tblProductionLotBalances.getTblProductionLotBalances()) {
            MstProcedure mstProcedure = mstProcedureService.getMstProcedureByComponentIdAndSeq(tblProductionLotBalance.getComponentId(), maxProcessNumber);
            if (mstProcedure != null) {
                // 金型部品関係マスタを部品IDで検索しリストを設定
                List<MstMoldComponentRelation> mstMoldComponentRelations = mstMoldComponentRelationService.getMstComponent(mstProcedure.getComponentId());
                mstProcedure.setMstMoldComponentRelation(mstMoldComponentRelations);
                mstProcedures.add(mstProcedure);
            }
        }
        responce.setMstProcedures(mstProcedures);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return responce;
    }
    
    /**
     * 生産実績ロット残高テーブル取得
     * @param lotNumber
     * @param componentId
     * @return 
     */
    @GET
    @Path("single")
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionLotBalanceList getProductionLotBalanceByProcessNumberAndComponentId(
            @QueryParam("lotNumber") String lotNumber
           ,@QueryParam("componentId") String componentId
    ) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        // クエリパラムログ出力
        logger.log(Level.FINE, "QueryParams'{'lotNumber:{0}, componentId:{1}}" , new Object[]{lotNumber, componentId});
        
        // ロット番号最大値+1を取得
        int maxProcessNumber = tblProductionLotBalanceService.getMaxProcessNumber(lotNumber);
        
        // 生産実績ロット残高リスト取得
        TblProductionLotBalanceList tblProductionLotBalances = tblProductionLotBalanceService.getByLotNumberAndComponentIdAndMaxProcessNumber(lotNumber, componentId, maxProcessNumber-1);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return tblProductionLotBalances;
    }   
    
    private BasicResponse setApplicationError(BasicResponse response, LoginUser loginUser, String dictKey, String errorContentsName) {
        setBasicResponseError(
                response
               ,true
               ,ErrorMessages.E201_APPLICATION
               ,mstDictionaryService.getDictionaryValue(loginUser.getLangId(), dictKey)
        );
        String logMessage = response.getErrorMessage() + ":" + errorContentsName;
        logger.log(Level.FINE, logMessage);
        return response;
    }
    
    private BasicResponse setBasicResponseError(BasicResponse response, boolean error, String errorCode, String errorMessage) {
        response.setError(error);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        return response;
    }
}
