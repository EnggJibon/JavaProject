/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.daily.report;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationService;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationVoList;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.MstMachineService;
import com.kmcj.karte.resources.machine.daily.report.detail.TblMachineDailyReportDetail;
import com.kmcj.karte.resources.machine.daily.report.detail.TblMachineDailyReportDetailList;
import com.kmcj.karte.resources.material.stock.TblMaterialStockService;
import com.kmcj.karte.resources.mold.MstMoldService;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import com.kmcj.karte.resources.production.TblProduction;
import com.kmcj.karte.resources.production.TblProductionResource;
import com.kmcj.karte.resources.production.TblProductionService;
import com.kmcj.karte.resources.production.detail.TblProductionDetail;
import com.kmcj.karte.resources.stock.TblStockService;
import com.kmcj.karte.util.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author
 */
@RequestScoped
@Path("machine/daily/report")
public class TblMachineDailyReportResource {

    private Logger logger = Logger.getLogger(TblMachineDailyReportResource.class.getName());

    @Inject
    MstChoiceService mstChoiceService;

    @Context
    ContainerRequestContext requestContext;

    @Inject
    private CnfSystemService cnfSystemService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private TblMachineDailyReportService tblMachineDailyReportService;
    
    @Inject
    private TblProductionService tblProductionService;
    
    @Inject
    private MstMoldService mstMoldService;
    
    @Inject
    private MstMachineService mstMachineService;
    
    @Inject
    private TblStockService tblStockService;
    
    @Inject
    private MstProcedureService mstProcedureService;
    
    @Inject
    private TblMaterialStockService tblMaterialStockService;
    
    @Inject
    private TblComponentLotRelationService tblComponentLotRelationService;

    /**
     *
     * @param productionId
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineDailyReportVo getDailyReports(@QueryParam("productionId") String productionId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineDailyReportService.getDailyReports(productionId, loginUser);
    }

    /**
     *
     * @param machineDailyReportId
     * @param productionId
     * @return
     */
    @GET
    @Path("details")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineDailyReportVo getDailyReportDetail(@QueryParam("machineDailyReportId") String machineDailyReportId, @QueryParam("productionId") String productionId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineDailyReportService.getDailyReportDetails(machineDailyReportId, productionId, loginUser);
    }

    /**
     * 機械日報テーブル条件検索(機械日報一覧データ取得)
     *
     * @param department
     * @param productionDateFrom
     * @param productionDateTo
     * @param machineId
     * @param componentCode
     * @param reporterUser
     * @return
     */
    @GET
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineDailyReportDetailList getDailyReportListByCondition(
            @QueryParam("componentCode") String componentCode // 部品コード
            , @QueryParam("productionDateFrom") String productionDateFrom // 生産日From yyyy/MM/dd
            , @QueryParam("productionDateTo") String productionDateTo // 生産日To yyyy/MM/dd
            , @QueryParam("reporterUser") String reporterUser // 生産者
            , @QueryParam("machineId") String machineId // 設備ID
            , @QueryParam("department") int department // 部署
    ) {

        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        // クエリパラムログ出力
        logger.log(Level.FINE, "QueryParams'{'"
                + ", componentCode:{0}, productionDateFrom:{1}, productionDateTo:{2}, reporterUser:{3}, machineId:{4}, department:{5}}",
                new Object[]{componentCode, productionDateFrom, productionDateTo, reporterUser, machineId, department}
        );

        // 日付項目をDate型(yyyy/MM/dd)に変換
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        java.util.Date formatProductionDateFrom = null;
        java.util.Date formatProductionDateTo = null;

        try {
            if (productionDateFrom != null && !"".equals(productionDateFrom)) {
                formatProductionDateFrom = sdf.parse(productionDateFrom);
            }
        } catch (ParseException ex) {
            Logger.getLogger(TblProductionResource.class.getName()).log(Level.WARNING, null, "日付形式不正 productioDateFrom[" + productionDateFrom + "], productioDateTo[" + productionDateTo + "]");
        }

        try {
            if (productionDateTo != null && !"".equals(productionDateTo)) {
                formatProductionDateTo = sdf.parse(productionDateTo);
            }
        } catch (ParseException ex) {
            Logger.getLogger(TblProductionResource.class.getName()).log(Level.WARNING, null, "日付形式不正 productioDateFrom[" + productionDateFrom + "], productioDateTo[" + productionDateTo + "]");
        }
        
        // データ取得
        TblMachineDailyReportDetailList tblMachineDailyReportDetailList = tblMachineDailyReportService.getMachineDailyReportDetailsByCondition(
                componentCode, formatProductionDateFrom, formatProductionDateTo, reporterUser, machineId, department);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return tblMachineDailyReportDetailList;
    }

    /**
     * 機械日報テーブル条件検索(機械日報一覧件数取得)
     *
     * @param componentCode
     * @param productionDateFrom
     * @param productionDateTo
     * @param reporterUser
     * @param machineId
     * @param department
     * @return
     */
    @GET
    @Path("search/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getDailyReportCountByCondition(
            @QueryParam("componentCode") String componentCode // 部品コード
            , @QueryParam("productionDateFrom") String productionDateFrom // 生産日From yyyy/MM/dd
            , @QueryParam("productionDateTo") String productionDateTo // 生産日To yyyy/MM/dd
            , @QueryParam("reporterUser") String reporterUser // 生産者
            , @QueryParam("machineId") String machineId // 設備ID
            , @QueryParam("department") int department // 部署
    ) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        // 日付項目をDate型(yyyy/MM/dd)に変換
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        java.util.Date formatProductionDateFrom = null;
        java.util.Date formatProductionDateTo = null;

        try {
            if (productionDateFrom != null && !"".equals(productionDateFrom)) {
                formatProductionDateFrom = sdf.parse(productionDateFrom);
            }
            
        } catch (ParseException ex) {
            Logger.getLogger(TblProductionResource.class.getName()).log(Level.WARNING, null, "日付形式不正 productioDateFrom[" + productionDateFrom + "], productioDateTo[" + productionDateTo + "]");
        }
        
        try {
            
            if (productionDateTo != null && !"".equals(productionDateTo)) {
                formatProductionDateTo = sdf.parse(productionDateTo);
            }
        } catch (ParseException ex) {
            Logger.getLogger(TblProductionResource.class.getName()).log(Level.WARNING, null, "日付形式不正 productioDateFrom[" + productionDateFrom + "], productioDateTo[" + productionDateTo + "]");
        }

        // 件数を取得
        CountResponse response = tblMachineDailyReportService.getMachineDailyReportCountByCondition(
                componentCode, formatProductionDateFrom, formatProductionDateTo, reporterUser, machineId, department);

        // システム上限を取得
        CnfSystem cnf = cnfSystemService.findByKey("system", "max_list_record_count");
        long sysCount = Long.parseLong(cnf.getConfigValue());

        if (response.getCount() > sysCount) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_confirm_max_record_count");
            msg = String.format(msg, sysCount);
            response.setErrorMessage(msg);
        }
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }

    /**
     * 機械日報テーブルCSV出力
     *
     * @param componentCode
     * @param productionDateFrom
     * @param productionDateTo
     * @param reporterUser
     * @param department
     * @param machineId
     * @return
     */
    @GET
    @Path("exportcsv")
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getDailyReportCSV(
            @QueryParam("componentCode") String componentCode // 部品コード
            , @QueryParam("productionDateFrom") String productionDateFrom // 生産日From yyyy/MM/dd
            , @QueryParam("productionDateTo") String productionDateTo // 生産日To yyyy/MM/dd
            , @QueryParam("reporterUser") String reporterUser // 生産者
            , @QueryParam("machineId") String machineId // 設備ID
            , @QueryParam("department") int department // 部署
    ) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        // データ取得
        TblMachineDailyReportDetailList tblMachineDailyReportDetailList = getDailyReportListByCondition(
                componentCode, productionDateFrom, productionDateTo, reporterUser, machineId, department);

        /*
         * 作成したCSV出力テーブルのIDを設定したファイルレスポンスを返却
         */
        FileReponse response = tblMachineDailyReportService.getDailyReportCSV(tblMachineDailyReportDetailList, loginUser);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }

    /**
     * 機械日報ル1件削除(ID指定)
     *
     * @param id
     * @return
     */
    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteDailyReportById(@PathParam("id") String id) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        // 削除データチェック
        tblMachineDailyReportService.checkDeleteData(response, id, loginUser);
        if (response.isError()) {
            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
            return response;
        }

        TblMachineDailyReport deleteDailyReport = tblMachineDailyReportService.getDailyReportSingleById(id);
        
        List<TblMachineDailyReportDetail> deleteDailyReportDetailList = tblMachineDailyReportService.getDailyReportDetailsByMacReportId(id);
        
        // 日/週/月別完成数集計テーブルに完成数計算
        tblMachineDailyReportService.calculateProductionPeriodForDelete(id, loginUser);
        
        TblComponentLotRelationVoList tblComponentLotRelationVoList = null;
        if (deleteDailyReportDetailList != null && !deleteDailyReportDetailList.isEmpty()) {
            // 部品ロット関連テーブルから構成部品リスト取得
            tblComponentLotRelationVoList = tblComponentLotRelationService.getTblComponentLotRelationList(null, id);
        }
        
        // 削除
        tblMachineDailyReportService.deleteMachineDailyReport(id, loginUser);
        
        // 生産実績ショット数、捨てショット数、実績明細取り数、不良数、完成数更新
        // 一回終了してない生産実績と終了して終了取消行う生産実績の判断できないため、合わせて機械日報削除する時、残った機械日報でショット数など加算する
        // 削除後すべての機械日報情報取得
        TblMachineDailyReportDetailList tblMachineDailyReportDetailList = tblMachineDailyReportService.getAllDailyReportByProductionId(deleteDailyReport.getTblMachineDailyReportPK().getProductionId());
        tblMachineDailyReportService.calculateProductionForDelete(tblMachineDailyReportDetailList, deleteDailyReport.getTblMachineDailyReportPK().getProductionId(), response, loginUser);
        if (response.isError()) {
            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
            return response;
        }
        
        // 金型/設備マスタ更新(最終生産日、累計生産時間、累計ショット数、メンテナンス後生産時間、メンテナンス後ショット数)
        TblProduction tblProduction = tblProductionService.getProductionSingleById(deleteDailyReport.getTblMachineDailyReportPK().getProductionId());
        TblMachineDailyReportVo machineDailyReportVo = new TblMachineDailyReportVo();
        machineDailyReportVo.setShotCount(null);
        machineDailyReportVo.setShotCountBeforeUpd(deleteDailyReport.getShotCount());
        machineDailyReportVo.setDisposedShotCount(null);
        machineDailyReportVo.setDisposedShotCountBeforeUpd(deleteDailyReport.getDisposedShotCount());
        machineDailyReportVo.setNetProducintTimeMinutes(null);
        machineDailyReportVo.setNetProducintTimeMinutesBeforeUpd(deleteDailyReport.getNetProducintTimeMinutes());
        // 該当日報削除後他の日報が存在する場合、生産日最大の日報取得し、終了時間を最終生産日として設定する
        if (tblMachineDailyReportDetailList.getTblMachineDailyReportDetails() != null && !tblMachineDailyReportDetailList.getTblMachineDailyReportDetails().isEmpty()) {
            machineDailyReportVo.setProductionDate(tblMachineDailyReportDetailList.getTblMachineDailyReportDetails().get(0).getTblMachineDailyReport().getTblMachineDailyReportPK().getProductionDate());
        }
        if (tblProduction != null && tblProduction.getMoldUuid() != null && !"".equals(tblProduction.getMoldUuid())) {
            // 該当日報削除後他の日報が存在しない場合、該当金型の生産実績中に、終了した生産実績の終了時間と終了していない生産実績の最大終了時間である日報を比較し、大きいほうで最終生産日として設定する
            if (machineDailyReportVo.getProductionDate() == null) {
                machineDailyReportVo.setProductionDate(tblProductionService.getLastProductionDateAfterDelete(tblProduction, 0));
            }
            // 金型詳細更新
            mstMoldService.updateMstMoldForDailyReport(tblProduction, machineDailyReportVo, loginUser);
        }
        if (machineDailyReportVo.getProductionDate() == null) {
            // 上記金型の場合と同じロジック
            machineDailyReportVo.setProductionDate(tblProductionService.getLastProductionDateAfterDelete(tblProduction, 1));
        }
        // 設備詳細更新
        mstMachineService.updateMstMachineForDailyReport(tblProduction, machineDailyReportVo, loginUser);
        
        // 在庫管理更新 2017/11 Add S
        if (deleteDailyReportDetailList != null && !deleteDailyReportDetailList.isEmpty()) {
            String lotNumber = tblProduction == null ? "" : tblProduction.getLotNumber();
                    
            for (TblMachineDailyReportDetail deleteDetail : deleteDailyReportDetailList) {
                if (deleteDetail.getMstProcedure() == null) continue;
                MstProcedure prevMstProcedure = mstProcedureService.getPrevProcedureCode(deleteDetail.getComponentId(), deleteDetail.getMstProcedure().getProcedureCode());
                // 休日以外且つ工程番号（工番）が部品コードの中で最大の時だけ実施する
                if (deleteDailyReport.getNoRegistrationFlag() != 0) {
                    
                    response = tblStockService.doTblStock(
                        deleteDetail.getMstComponent().getComponentCode(),
                        deleteDetail.getMstProcedure(),
                        prevMstProcedure,
                        CommonConstants.DELIVERY_DISCARD,
                        deleteDetail.getCompleteCount(),
                        DateFormat.getCurrentDateTime(),
                        lotNumber,
                        0,
                        null,
                        CommonConstants.SHIPMENT_NO,
                        tblComponentLotRelationVoList,
                        loginUser.getUserUuid(),
                        loginUser.getLangId());

                    if (response.isError()) {
                        logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                        return response;
                    }
                }
                
                // 材料在庫更新 2017/12 Add S
                if (StringUtils.isNotEmpty(deleteDetail.getMaterial01Id())) {
                    response = tblMaterialStockService.doMaterialStock(deleteDetail.getMstMaterial01() == null? "" : deleteDetail.getMstMaterial01().getMaterialCode(),
                        deleteDetail.getMaterial01LotNo(),
                        CommonConstants.STORE_DISCARD,
                        deleteDetail.getMaterial01Amount().add(deleteDetail.getMaterial01PurgedAmount()),
                        DateFormat.getCurrentDateTime(),
                        deleteDetail.getTblMachineDailyReportDetailPK().getProductionDetailId(),
                        0,
                        null,
                        loginUser.getUserUuid(),
                        loginUser.getLangId(),
                        false);
                    
                    if (response.isError()) {
                        logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                        return response;
                    }
                }
                if (StringUtils.isNotEmpty(deleteDetail.getMaterial02Id())) {
                    response = tblMaterialStockService.doMaterialStock(deleteDetail.getMstMaterial02() == null ? "" : deleteDetail.getMstMaterial02().getMaterialCode(),
                        deleteDetail.getMaterial02LotNo(),
                        CommonConstants.STORE_DISCARD,
                        deleteDetail.getMaterial02Amount().add(deleteDetail.getMaterial02PurgedAmount()),
                        DateFormat.getCurrentDateTime(),
                        deleteDetail.getTblMachineDailyReportDetailPK().getProductionDetailId(),
                        0,
                        null,
                        loginUser.getUserUuid(),
                        loginUser.getLangId(),
                        false);
                    
                    if (response.isError()) {
                        logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                        return response;
                    }
                }
                if (StringUtils.isNotEmpty(deleteDetail.getMaterial03Id())) {
                    response = tblMaterialStockService.doMaterialStock(deleteDetail.getMstMaterial03() == null ? "" : deleteDetail.getMstMaterial03().getMaterialCode(),
                        deleteDetail.getMaterial03LotNo(),
                        CommonConstants.STORE_DISCARD,
                        deleteDetail.getMaterial03Amount().add(deleteDetail.getMaterial03PurgedAmount()),
                        DateFormat.getCurrentDateTime(),
                        deleteDetail.getTblMachineDailyReportDetailPK().getProductionDetailId(),
                        0,
                        null,
                        loginUser.getUserUuid(),
                        loginUser.getLangId(),
                        false);
                    
                    if (response.isError()) {
                        logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                        return response;
                    }
                }
                // 材料在庫更新 2017/12 Add E
            }
        }
        // 在庫管理更新 2017/11 Add E

        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }

    /**
     * TT1017 機械日報登録 機械日報、機械日報明細テーブルにデータを登録する 存在している場合更新、　存在していない場合追加
     *
     * @param machineDailyReportVo
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineDailyReportVo postDailyReportDetail(TblMachineDailyReportVo machineDailyReportVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        TblMachineDailyReportVo response = tblMachineDailyReportService.postDailyReportDetail(machineDailyReportVo, loginUser);
        return response;
    }

    /**
     * 実稼動時間を計算する
     *
     * @param tblMachineDailyReportVo
     * @return
     */
    @POST
    @Path("calculate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineDailyReportVo calculateMin(TblMachineDailyReportVo tblMachineDailyReportVo) {

        tblMachineDailyReportService.calculateMin(tblMachineDailyReportVo);

        return tblMachineDailyReportVo;
    }

    /**
     * 材料使用量の計算
     *
     * @param tblMachineDailyReportVo
     * @return
     */
    @POST
    @Path("calculateusage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineDailyReportVo calculateUsage(TblMachineDailyReportVo tblMachineDailyReportVo) {

        tblMachineDailyReportService.calculateUsage(tblMachineDailyReportVo);

        return tblMachineDailyReportVo;
    }

    /**
     * 中断時間を計算する
     *
     * @param tblMachineDailyReportVo
     * @return
     */
    @POST
    @Path("calculatesuspendedmin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineDailyReportVo calculateSuspendedMin(TblMachineDailyReportVo tblMachineDailyReportVo) {
        
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        tblMachineDailyReportService.calculateSuspendedMin(tblMachineDailyReportVo, loginUser);

        return tblMachineDailyReportVo;
    }
    
    /**
     * 機械日報テーブル条件検索(機械日報一覧データ取得)
     *
     * @param department
     * @param productionDateFrom
     * @param productionDateTo
     * @param machineId
     * @param componentCode
     * @param reporterUser
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GET
    @Path("searchbypage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineDailyReportDetailList getDailyReportListByConditionPage(
            @QueryParam("componentCode") String componentCode // 部品コード
            , @QueryParam("productionDateFrom") String productionDateFrom // 生産日From yyyy/MM/dd
            , @QueryParam("productionDateTo") String productionDateTo // 生産日To yyyy/MM/dd
            , @QueryParam("reporterUser") String reporterUser // 生産者
            , @QueryParam("machineId") String machineId // 設備ID
            , @QueryParam("department") int department // 部署
            , @QueryParam("sidx") String sidx // ソートキー
            , @QueryParam("sord") String sord // ソート順
            , @QueryParam("pageNumber") int pageNumber // ページNo
            , @QueryParam("pageSize") int pageSize // ページSize
    ) {

        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        // クエリパラムログ出力
        logger.log(Level.FINE, "QueryParams'{'"
                + ", componentCode:{0}, productionDateFrom:{1}, productionDateTo:{2}, reporterUser:{3}, machineId:{4}, department:{5}}",
                new Object[]{componentCode, productionDateFrom, productionDateTo, reporterUser, machineId, department}
        );

        // 日付項目をDate型(yyyy/MM/dd)に変換
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        java.util.Date formatProductionDateFrom = null;
        java.util.Date formatProductionDateTo = null;

        try {
            if (productionDateFrom != null && !"".equals(productionDateFrom)) {
                formatProductionDateFrom = sdf.parse(productionDateFrom);
            }
        } catch (ParseException ex) {
            Logger.getLogger(TblProductionResource.class.getName()).log(Level.WARNING, null, "日付形式不正 productioDateFrom[" + productionDateFrom + "], productioDateTo[" + productionDateTo + "]");
        }

        try {
            if (productionDateTo != null && !"".equals(productionDateTo)) {
                formatProductionDateTo = sdf.parse(productionDateTo);
            }
        } catch (ParseException ex) {
            Logger.getLogger(TblProductionResource.class.getName()).log(Level.WARNING, null, "日付形式不正 productioDateFrom[" + productionDateFrom + "], productioDateTo[" + productionDateTo + "]");
        }
        
        // データ取得
        TblMachineDailyReportDetailList tblMachineDailyReportDetailList = tblMachineDailyReportService
                .getMachineDailyReportDetailsByConditionPage(componentCode, formatProductionDateFrom,
                        formatProductionDateTo, reporterUser, machineId, department, sidx, sord, pageNumber, pageSize,
                        true);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return tblMachineDailyReportDetailList;
    }

}
