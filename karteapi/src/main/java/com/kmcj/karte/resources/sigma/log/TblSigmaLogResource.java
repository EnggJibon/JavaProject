package com.kmcj.karte.resources.sigma.log;

import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ObjectResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.machine.filedef.MstMachineFileDef;
import com.kmcj.karte.resources.machine.filedef.MstMachineFileDefList;
import com.kmcj.karte.resources.machine.filedef.MstMachineFileDefService;
import com.kmcj.karte.resources.machine.filedef.MstMachineFileDefVo;
import com.kmcj.karte.util.BeanCopyUtil;
import com.kmcj.karte.util.DateFormat;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
 * @author penggd
 */
@RequestScoped
@Path("sigmalog")
public class TblSigmaLogResource {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblSigmaLogService tblSigmaLogService;

    @Inject
    private MstMachineFileDefService mstMachineFileDefService;

    private Logger logger = Logger.getLogger(TblSigmaLogResource.class.getName());

    public TblSigmaLogResource() {

    }

    /**
     * Σ軍師データにより、設備ログ取込API insert設備ログデータ登録
     *
     * @param tblSigmaLogBatchVo Σ軍師ログ情報
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse postSigmaLog(TblSigmaLogBatchVo tblSigmaLogBatchVo) {

        CountResponse response = new CountResponse();

        try {

            List<TblSigmaLogVo> tblSigmaLogList = getInsertSigmaLogs(tblSigmaLogBatchVo);

            int count = 0;

            List<TblSigmaLog> tblSigmaLogInsertList = new ArrayList();
            List<String> keyDupDetail = new ArrayList<>();

            if (tblSigmaLogList.size() > 0) {

                for (TblSigmaLogVo tblSigmaLogVo : tblSigmaLogList) {

                    // 設備ログデータの存在チェック
                    if (!tblSigmaLogService.existsSigmaLog(tblSigmaLogVo)) {

                        TblSigmaLog tblSigmaLog = new TblSigmaLog();

                        BeanCopyUtil.copyFields(tblSigmaLogVo, tblSigmaLog);

                        tblSigmaLog.setTblSigmaLogPK(new TblSigmaLogPK());

                        // PKを設定
                        tblSigmaLog.getTblSigmaLogPK().setMachineUuid(tblSigmaLogVo.getMachineUuid());
                        tblSigmaLog.getTblSigmaLogPK().setEventNo(Long.valueOf(tblSigmaLogVo.getEventNo()));
                        tblSigmaLog.getTblSigmaLogPK().setCreateDate(DateFormat.strToDateMill(tblSigmaLogVo.getCreateDate()));

                        tblSigmaLogInsertList.add(tblSigmaLog);
                    } else {
                        // キー重複を検知したらAPIはエラーをΣインポートに返す
                        response.setKeyDupFlag(true);
                        String keyDupMsg = "Duplicated record is included. Date time is " + tblSigmaLogVo.getCreateDate();
                        keyDupDetail.add(keyDupMsg);
                    }
                }

                if (tblSigmaLogInsertList.size() > 0) {
                    count = tblSigmaLogService.batchInsert(tblSigmaLogInsertList);
                }

                response.setFirstEventNo(Long.valueOf(tblSigmaLogList.get(0).getEventNo()));
                response.setLastEventNo(Long.valueOf(tblSigmaLogList.get(tblSigmaLogList.size() - 1).getEventNo()));
            }

            response.setKeyDupDetail(keyDupDetail);
            response.setCount(count);

        } catch (Exception e) {// 異常処理
            response.setError(true);
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String stackTrace = sw.toString();
            response.setErrorMessage(e.toString());
            response.setErrorMessage(stackTrace);
            response.setCount(0);
            return response;

        }

        return response;
    }

//    /**
//     * getMachineHis設備稼働ログを取得
//     *
//     * @param machineUuid
//     * @param eventNo
//     * @param createDate
//     * @return
//     */
//    @GET
//    @Path("getmachinehis/{machineUuid}/{eventNo}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public TblSigmaLogList getMachineHis(@PathParam("machineUuid") String machineUuid, @PathParam("eventNo") String eventNo, @QueryParam("createDate") String createDate) {
//
//        TblSigmaLogList response = new TblSigmaLogList();
//
//        try {
//            response = tblSigmaLogService.getMachineHis(machineUuid, eventNo, createDate);
//
//        } catch (Exception e) {
//
//            response.setError(true);
//
//            response.setErrorMessage(e.toString());
//
//            return response;
//        }
//
//        return response;
//    }
    /**
     * バッチでΣ軍師ログテーブルデータを取得
     *
     * @param latestCreateDate
     * @param machineUuid
     * @return
     */
    @GET
    @Path("extsigmalog")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblSigmaLogList getExtSigmaLogsByBatch(@QueryParam("latestCreateDate") String latestCreateDate, @QueryParam("machineUuid") String machineUuid, @QueryParam("eventNo") String eventNo) {
        return tblSigmaLogService.getExtSigmaLogsByBatch(latestCreateDate, machineUuid, eventNo);
    }

    /**
     * 設備ログ照会　製造条件リスト取得
     *
     * @param machineId
     * @return
     */
    @GET
    @Path("initgraphparam")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineFileDefList getGraphParamList(@QueryParam("machineId") String machineId) {
        return tblSigmaLogService.getGraphParamList(machineId);
    }
    
    @GET
    @Path("columns")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineFileDefList geColumns(@QueryParam("machineId") String machineId) {
        return tblSigmaLogService.getSigmaLogColumns(machineId);
    }

    /**
     * 設備ログ照会　再表示
     *
     * @param machineId
     * @param startDate
     * @param endDate
     * @param columnNms1
     * @param columnNms2
     * @param tick
     * @return
     */
    @GET
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblSigmaLogList getSigmaLog(
            @QueryParam("machineId") String machineId,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @QueryParam("columnNms1") String columnNms1,
            @QueryParam("columnNms2") String columnNms2,
            @QueryParam("tick") String tick,
            @QueryParam("checkThresholds") boolean checkThresholds) {
        TblSigmaLogList response = new TblSigmaLogList();
        List<MachineGraphLogVo> returnLogList = tblSigmaLogService.getSigmaLog(machineId, startDate, endDate, columnNms1, columnNms2, tick);
        List<MstMachineFileDef> returnThresholdList = tblSigmaLogService.getSigmaThreshold(machineId, columnNms1, columnNms2);
        response.setMachineGraphLogVos(returnLogList);
        response.setMachineGraphThresholdVos(returnThresholdList);
        if (checkThresholds) {
            tblSigmaLogService.checkThresholds(response);
        }
        return response;
    }
    
    @GET
    @Path("avgline")
    public ObjectResponse<List<AvgLinePoint>> getAverageLine(
            @QueryParam("machineId") String machineId,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @QueryParam("columnNm") String columnNm) {
        List<AvgLinePoint> logList = tblSigmaLogService.getMovingAverage(machineId, startDate, endDate, columnNm);
        return new ObjectResponse<>(logList);
    }

    /**
     * 設備ログ照会　CSV出力
     *
     * @param startDate
     * @param endDate
     * @param columnNms
     * @param tick
     * @param headers
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getSigmaLogCSV(
            @QueryParam("machineId") String machineId,
            @QueryParam("startdate") String startDate,
            @QueryParam("enddate") String endDate,
            @QueryParam("columnNms") String columnNms,
            @QueryParam("tick") String tick,
            @QueryParam("headers") String headers) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblSigmaLogService.getSigmaLogCSV(machineId, startDate, endDate, columnNms, tick, headers, loginUser);
    }
    
    @GET
    @Path("rawdata")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getSigmaLogRawData(
            @QueryParam("machineId") String machineId,
            @QueryParam("startdate") String startDate,
            @QueryParam("enddate") String endDate,
            @QueryParam("columnNms") String columnNms,
            @QueryParam("headers") String headers) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblSigmaLogService.getSigmaLogRawData(machineId, startDate, endDate, columnNms, headers, loginUser);
    }
    

    /**
     * 登録用のΣ軍師ログ情報を取得
     *
     * @param tblSigmaLogBatchVo
     * @return
     */
    private List<TblSigmaLogVo> getInsertSigmaLogs(TblSigmaLogBatchVo tblSigmaLogBatchVo) throws Exception {

        List<TblSigmaLogVo> tblSigmaLogList = new ArrayList();

        List<MstMachineFileDefVo> mstMachineFileDefVos = mstMachineFileDefService.getMstMachinFileDefByMacKey(tblSigmaLogBatchVo.getMacKey());

        if (mstMachineFileDefVos.size() > 0) {

            try {

                for (SigmaLogDetailVo sigmaLogDetailVo : tblSigmaLogBatchVo.getLogs()) {

                    TblSigmaLogVo tblSigmaLogVo = new TblSigmaLogVo();

                    List<ColValueVo> ColValueVos = sigmaLogDetailVo.getValues();

                    if (ColValueVos.size() > 0) {

                        Class c = Class.forName("com.kmcj.karte.resources.sigma.log.TblSigmaLogVo");
                        Constructor con = c.getConstructor();

                        Object obj = con.newInstance();

                        for (ColValueVo colValueVo : ColValueVos) {

                            for (MstMachineFileDefVo mstMachineFileDefVo : mstMachineFileDefVos) {

                                // Σ軍師CSVファイルヘッダー項目名と定義ヘッダー名一致の場合
                                if (colValueVo.getHeaderName().equals(mstMachineFileDefVo.getHeaderLabel())) {

                                    String colName = StringUtils.lowerCase(mstMachineFileDefVo.getColumnName());

                                    Field col = c.getDeclaredField(colName);
                                    col.setAccessible(true);

                                    // ヘッダー名一致しても、対する値が空白である場合、無視
                                    if (colValueVo.getValue() != null && !"".equals(colValueVo.getValue().trim())) {
                                        col.set(obj, colValueVo.getValue());
                                    }

                                    // 稼働判定項目フラグ＝1の場合、自動(On/Off判定項目)を設定する
                                    if ("1".equals(mstMachineFileDefVo.getOnOffJudgeFlg())) {

                                        Field autoMode = c.getDeclaredField("autoMode");
                                        autoMode.setAccessible(true);
                                        if (colValueVo.getValue() != null && !"".equals(colValueVo.getValue().trim())) {
                                            autoMode.set(obj, colValueVo.getValue());
                                        }

                                    }

                                    // 停止判定項目フラグ＝1の場合、アラーム(停止回数カウント用)を設定する
                                    if ("1".equals(mstMachineFileDefVo.getStopJudgeFlg())) {
                                        Field alert = c.getDeclaredField("alert");
                                        alert.setAccessible(true);
                                        if (colValueVo.getValue() != null && !"".equals(colValueVo.getValue().trim())) {
                                            alert.set(obj, colValueVo.getValue());
                                        }
                                    }

                                    // ショット数カウントフラグ＝1の場合、ショット(ショット数カウント用)を設定する
                                    if ("1".equals(mstMachineFileDefVo.getShotCountFlg())) {
                                        Field cycleStart = c.getDeclaredField("cycleStart");
                                        cycleStart.setAccessible(true);
                                        if (colValueVo.getValue() != null && !"".equals(colValueVo.getValue().trim())) {
                                            cycleStart.set(obj, colValueVo.getValue());
                                        }
                                    }
                                }

                            }

                            BeanCopyUtil.copyFields((TblSigmaLogVo) obj, tblSigmaLogVo);
                        }

                        tblSigmaLogVo.setMachineUuid(mstMachineFileDefVos.get(0).getMachineUuid());

                        StringBuilder tempDate = new StringBuilder();

                        tempDate.append(DateFormat.formatDateYear(sigmaLogDetailVo.getCreateDate(), DateFormat.DATE_FORMAT)).append(" ").append(sigmaLogDetailVo.getCreateTime());

                        tblSigmaLogVo.setCreateDate(tempDate.toString());

                        // イベント番号を空白で受け取ったとき、日時(ミリ秒)から整数に変換した値をイベント番号としてデータベースに保存する。
                        if ("-1".equals(sigmaLogDetailVo.getEventNo())) {//＃VBのほうはイベント番号はLong型なので、ＣＳＶファイル該当項目空白であれば「-1」に設定
                            tblSigmaLogVo.setEventNo(String.valueOf(DateFormat.strToDateMill(tblSigmaLogVo.getCreateDate()).getTime()));
                        } else {
                            tblSigmaLogVo.setEventNo(sigmaLogDetailVo.getEventNo());
                        }
                    }

                    tblSigmaLogList.add(tblSigmaLogVo);

                }

            } catch (Exception e) {

                throw new Exception(e);
            }

        }

        return tblSigmaLogList;
    }

}
