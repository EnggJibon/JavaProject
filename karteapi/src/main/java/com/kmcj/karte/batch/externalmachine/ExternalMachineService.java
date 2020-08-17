package com.kmcj.karte.batch.externalmachine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.component.company.MstComponentCompanyVoList;
import com.kmcj.karte.resources.external.MstExternalDataGetSettingService;
import com.kmcj.karte.resources.files.TblUploadFileList;
import com.kmcj.karte.resources.files.TblUploadFileService;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.MstMachineList;
import com.kmcj.karte.resources.machine.MstMachineService;
import com.kmcj.karte.resources.machine.MstMachineVo;
import com.kmcj.karte.resources.machine.history.TblMachineHistory;
import com.kmcj.karte.resources.machine.history.TblMachineHistoryList;
import com.kmcj.karte.resources.machine.history.TblMachineHistoryService;
import com.kmcj.karte.resources.machine.inspection.result.TblMachineInspectionResultService;
import com.kmcj.karte.resources.machine.inspection.result.TblMachineInspectionResultVo;
import com.kmcj.karte.resources.machine.inventory.TblMachineInventoryList;
import com.kmcj.karte.resources.machine.inventory.TblMachineInventoryService;
import com.kmcj.karte.resources.machine.inventory.TblMachineInventoryVo;
import com.kmcj.karte.resources.machine.location.history.TblMachineLocationHistoryList;
import com.kmcj.karte.resources.machine.location.history.TblMachineLocationHistoryService;
import com.kmcj.karte.resources.machine.maintenance.detail.TblMachineMaintenanceDetailImageFileVo;
import com.kmcj.karte.resources.machine.maintenance.detail.TblMachineMaintenanceDetailService;
import com.kmcj.karte.resources.machine.maintenance.detail.TblMachineMaintenanceDetailVo;
import com.kmcj.karte.resources.machine.maintenance.remodeling.TblMachineMaintenanceRemodelingList;
import com.kmcj.karte.resources.machine.maintenance.remodeling.TblMachineMaintenanceRemodelingService;
import com.kmcj.karte.resources.machine.proccond.MstMachineProcCondList;
import com.kmcj.karte.resources.machine.proccond.MstMachineProcCondService;
import com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpecList;
import com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpecService;
import com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetailImageFileVo;
import com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetailService;
import com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetailVo;
import com.kmcj.karte.resources.machine.remodeling.inspection.TblMachineRemodelingInspectionResultService;
import com.kmcj.karte.resources.machine.remodeling.inspection.TblMachineRemodelingInspectionResultVo;
import com.kmcj.karte.resources.machine.spec.MstMachineSpecList;
import com.kmcj.karte.resources.machine.spec.MstMachineSpecService;
import com.kmcj.karte.resources.machine.spec.history.MstMachineSpecHistoryList;
import com.kmcj.karte.resources.machine.spec.history.MstMachineSpecHistoryService;
import com.kmcj.karte.resources.mold.issue.TblIssueImageFileVo;
import com.kmcj.karte.resources.mold.issue.TblIssueList;
import com.kmcj.karte.resources.mold.issue.TblIssueService;
import com.kmcj.karte.resources.mold.issue.TblIssueVo;
import com.kmcj.karte.resources.production.TblProductionList;
import com.kmcj.karte.resources.production.TblProductionService;
import com.kmcj.karte.resources.production.detail.TblProductionDetailService;
import com.kmcj.karte.resources.production.detail.TblProductionDetailVo;
import com.kmcj.karte.resources.sigma.log.TblSigmaLog;
import com.kmcj.karte.resources.sigma.log.TblSigmaLogList;
import com.kmcj.karte.resources.sigma.log.TblSigmaLogService;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 *
 * @author admin
 */
@Dependent
public class ExternalMachineService {

    //設備棚卸	
    private static final String EXTLOGIN_MACHINEINVENTORY_API_URL = "ws/karte/api/machine/inventory/extmachineinventory";
    //設備マスタ	
    private static final String EXTLOGIN_MACHINE_API_URL = "ws/karte/api/machine/extmachine";
    //設備履歴テーブル	
    private static final String EXTLOGIN_MACHINEHISTORY_API_URL = "ws/karte/api/machine/history/extmachinehistory";
    //Σ軍師ログテーブル
    private static final String EXTLOGIN_SIGMALOG_API_URL = "ws/karte/api/sigmalog/extsigmalog";
    //設備仕様履歴マスタ
    private static final String EXTLOGIN_MACHINESPECHISTORY_API_URL = "ws/karte/api/machine/spec/history/extmachinespechistory";
    //設備仕様	
    private static final String EXTLOGIN_MACHINESPEC_API_URL = "ws/karte/api/machine/spec/extmachinespec";
    //設備加工条件	
    private static final String EXTLOGIN_MACHINEPROCCOND_API_URL = "ws/karte/api/machine/proccond/extmachineproccond";
    //設備加工条件仕様	
    private static final String EXTLOGIN_MACHINEPROCCONDSPEC_API_URL = "ws/karte/api/machine/proccond/spec/extmachineproccondspec";
    //設備所在履歴	
    private static final String EXTLOGIN_MACHINELOCATIONHISTORY_API_URL = "ws/karte/api/machine/location/history/extmachinelocationhistory";
    //異常テーブル
    private static final String EXTLOGIN_MACHINEISSUE_API_URL = "ws/karte/api/mold/issue/extmachineissue";
    //設備メンテナンス改造テーブル
    private static final String EXTLOGIN_MACHINEMAINTENANCEREMODELING_API_URL = "ws/karte/api/machine/maintenance/remodeling/extmachinemaintenanceremodeling";
    //設備メンテナンス詳細
    private static final String EXTLOGIN_MACHINEMAINTENANCEDETAIL_API_URL = "ws/karte/api/machine/maintenance/extmachinemaintenancedetail";
    //設備メンテナンス詳細ImageFileマスタ
    private static final String EXTLOGIN_MACHINEMAINTENANCEDETAILMAGEFILE_API_URL = "ws/karte/api/machine/maintenance/extmachinemaintenancedetailimagefile";
    //設備改造詳細
    private static final String EXTLOGIN_MACHINEREMODELINGDETAIL_API_URL = "ws/karte/api/machine/remodeling/extmachineremodelingdetail";
    //設備改造詳細ImageFileマスタ
    private static final String EXTLOGIN_MACHINEREMODELINGDETAILIMAGEFILE_API_URL = "ws/karte/api/machine/remodeling/extmachineremodelingdetailimagefile";
    //設備点検结果マスタ
    private static final String EXTLOGIN_MACHINEINSPECTIONRESULT_API_URL = "ws/karte/api/machine/inspection/result/extmachineinspectionresult";
    //設備改造点検结果マスタ
    private static final String EXTLOGIN_MACHINEREMODELINGINSPECTIONRESULT_API_URL = "ws/karte/api/machine/remodeling/inspection/result/extmachineremodelinginspectionresult";
    //生産実績テーブル
    private static final String EXTLOGIN_MACHINEPRODUCTION_API_URL = "ws/karte/api/production/extmachineproduction";
    //生産実績明細テーブル
    private static final String EXTLOGIN_MACHINEPRODUCTIONDETAIL_API_URL = "ws/karte/api/productiondetail/extmachineproductiondetail";

    //FileDL
    private static final String EXTLOGIN_FILENAME_API_URL = "ws/karte/api/files/extfilename";
    private static final String EXTLOGIN_FILESDOWNLOAD_API_URL = "ws/karte/api/files/extfiledownload";
    private static final String EXTLOGIN_UPLOADFILE_API_URL = "ws/karte/api/files/extfileuploads";

    private Logger logger = Logger.getLogger(ExternalMachineService.class.getName());
    private Level info = Level.FINE;
    private Level severe = Level.SEVERE;

    @Inject
    private MstExternalDataGetSettingService externalDataGetSettingService;
    @Inject
    private TblMachineInventoryService tblMachineInventoryService;
    @Inject
    private MstMachineService mstMachineService;
    @Inject
    private MstMachineSpecHistoryService mstMachineSpecHistoryService;
    @Inject
    private MstMachineSpecService mstMachineSpecService;
    @Inject
    private MstMachineProcCondService mstMachineProcCondService;
    @Inject
    private MstMachineProcCondSpecService mstMachineProcCondSpecService;
    @Inject
    private TblMachineLocationHistoryService tblMachineLocationHistoryService;
    @Inject
    private TblMachineHistoryService tblMachineHistoryService;
    @Inject
    private TblSigmaLogService tblSigmaLogService;
    @Inject
    private TblIssueService tblIssueService;
    @Inject
    private TblMachineMaintenanceRemodelingService tblMachineMaintenanceRemodelingService;
    @Inject
    private TblMachineMaintenanceDetailService tblMachineMaintenanceDetailService;
    @Inject
    private TblMachineRemodelingDetailService tblMachineRemodelingDetailService;
    @Inject
    private TblMachineInspectionResultService tblMachineInspectionResultService;
    @Inject
    private TblMachineRemodelingInspectionResultService tblMachineRemodelingInspectionResultService;
    @Inject
    private TblProductionService tblProductionService;
    @Inject
    private TblProductionDetailService tblProductionDetailService;
    @Inject
    private TblUploadFileService tblUploadFileService;

    /**
     * 
     * @param token
     * @param methodName
     * @param apiBaseUrl
     * @param urlPara
     * @param latestExecutedDate
     * @param path
     * @param companyId
     * @param mstComponentCompanyVoList
     * @return 
     */
    public BasicResponse getExtMachine(String token, String methodName, String apiBaseUrl, String urlPara, Date latestExecutedDate, String path, String companyId, MstComponentCompanyVoList mstComponentCompanyVoList) {
        BasicResponse response = new BasicResponse();
        Gson gson = new Gson();
        MstMachineList machineDetails = null;
        MstMachineList machineDetailNewDate = null;

        String imagePath = new StringBuffer(path).append(FileUtil.SEPARATOR).append(CommonConstants.IMAGE).toString();
        String videoPath = new StringBuffer(path).append(FileUtil.SEPARATOR).append(CommonConstants.VIDEO).toString();
        String docPath = new StringBuffer(path).append(FileUtil.SEPARATOR).append(CommonConstants.DOC).toString();

        List<String> imagesList = new ArrayList<>();
        List<String> videosList = new ArrayList<>();
        List<String> docsList = new ArrayList<>();

        List<MstMachine> localMachines = externalDataGetSettingService.getMachineByExternalDataCompanyId(companyId);
        for (MstMachine localMachine : localMachines) {

            // ローカルの設備ＩＤで相手サーバ一致な設備を取得
            String iMachines = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINE_API_URL + "?machineId=" + localMachine.getMachineId(), token);
            machineDetails = gson.fromJson(iMachines, new TypeToken<MstMachineList>() {
            }.getType());
            // 設備マスタが新しくなくても、関連テーブルが新しかったら取る
            if (null != machineDetails.getMstMachineVos() && machineDetails.getMstMachineVos().size() > 0) {
                urlPara = "?machineUuid=" + machineDetails.getMstMachineVos().get(0).getMachineUuid() + "&latestExecutedDate=" + new FileUtil().getDateTimeFormatForStr(latestExecutedDate).replace(" ", "-");
            } else {
                continue;
            }

            // 設備マスタが新しかったら、全部取る                            
            iMachines = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINE_API_URL + urlPara, token);
            machineDetailNewDate = gson.fromJson(iMachines, new TypeToken<MstMachineList>() {
            }.getType());
            if (null != machineDetailNewDate.getMstMachineVos() && machineDetailNewDate.getMstMachineVos().size() > 0) {
                urlPara = "?machineUuid=" + machineDetails.getMstMachineVos().get(0).getMachineUuid();
            }

            //外部・設備棚卸のデータを取得するためのAPIをコール。
            try {
                String iMachineInventorys = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINEINVENTORY_API_URL + urlPara, token);
                TblMachineInventoryList machineInventorys = gson.fromJson(iMachineInventorys, new TypeToken<TblMachineInventoryList>() {
                }.getType());
                if (null != machineInventorys.getTblMachineInventoryVos() && machineInventorys.getTblMachineInventoryVos().size() > 0) {
                    for (TblMachineInventoryVo aInventoryVo : machineInventorys.getTblMachineInventoryVos()) {
                        if (aInventoryVo.getFileType() != null) {
                            if (aInventoryVo.getFileType().equals("" + CommonConstants.IMAGEFILE_TYPE_IMAGE)) {
                                //pic
                                imagesList.add(aInventoryVo.getImgFilePath());
                            } else {
                                //video                                                
                                videosList.add(aInventoryVo.getImgFilePath());
                            }
                        }
                    }

                    response = tblMachineInventoryService.updateExtMachineInventorysByBatch(machineInventorys.getTblMachineInventoryVos());
                    logger.log(info, "設備棚卸のデータを取得・更新しました。", methodName);
                } else {
                    logger.log(info, "設備棚卸のデータが見つかりません。", methodName);
                }
            } catch (Exception e) {
                response.setError(true);
                logger.log(severe, "設備棚卸のデータを取得・更新失敗しました。", e);
            }

            //外部・設備マスタのデータを取得するためのAPIをコール。
            if (null != machineDetailNewDate.getMstMachineVos() && response.isError() == false) {
                try {
                    if (machineDetailNewDate.getMstMachineVos().size() > 0) {
                        for (MstMachineVo aMachineVo : machineDetailNewDate.getMstMachineVos()) {
                            if (null != aMachineVo.getImgFilePath01() && !aMachineVo.getImgFilePath01().trim().equals("")) {
                                imagesList.add(aMachineVo.getImgFilePath01());
                            }
                            if (null != aMachineVo.getImgFilePath02() && !aMachineVo.getImgFilePath02().trim().equals("")) {
                                imagesList.add(aMachineVo.getImgFilePath02());
                            }
                            if (null != aMachineVo.getImgFilePath03() && !aMachineVo.getImgFilePath03().trim().equals("")) {
                                imagesList.add(aMachineVo.getImgFilePath03());
                            }
                            if (null != aMachineVo.getImgFilePath04() && !aMachineVo.getImgFilePath04().trim().equals("")) {
                                imagesList.add(aMachineVo.getImgFilePath04());
                            }
                            if (null != aMachineVo.getImgFilePath05() && !aMachineVo.getImgFilePath05().trim().equals("")) {
                                imagesList.add(aMachineVo.getImgFilePath05());
                            }
                            if (null != aMachineVo.getImgFilePath06() && !aMachineVo.getImgFilePath06().trim().equals("")) {
                                imagesList.add(aMachineVo.getImgFilePath06());
                            }
                            if (null != aMachineVo.getImgFilePath07() && !aMachineVo.getImgFilePath07().trim().equals("")) {
                                imagesList.add(aMachineVo.getImgFilePath07());
                            }
                            if (null != aMachineVo.getImgFilePath08() && !aMachineVo.getImgFilePath08().trim().equals("")) {
                                imagesList.add(aMachineVo.getImgFilePath08());
                            }
                            if (null != aMachineVo.getImgFilePath09() && !aMachineVo.getImgFilePath09().trim().equals("")) {
                                imagesList.add(aMachineVo.getImgFilePath09());
                            }
                            if (null != aMachineVo.getImgFilePath10() && !aMachineVo.getImgFilePath10().trim().equals("")) {
                                imagesList.add(aMachineVo.getImgFilePath10());
                            }

                            //doc ReportFile
                            if (null != aMachineVo.getReportFilePath01() && !aMachineVo.getReportFilePath01().trim().equals("")) {
                                docsList.add(aMachineVo.getReportFilePath01());
                            }
                            if (null != aMachineVo.getReportFilePath02() && !aMachineVo.getReportFilePath02().trim().equals("")) {
                                docsList.add(aMachineVo.getReportFilePath02());
                            }
                            if (null != aMachineVo.getReportFilePath03() && !aMachineVo.getReportFilePath03().trim().equals("")) {
                                docsList.add(aMachineVo.getReportFilePath03());
                            }
                            if (null != aMachineVo.getReportFilePath04() && !aMachineVo.getReportFilePath04().trim().equals("")) {
                                docsList.add(aMachineVo.getReportFilePath04());
                            }
                            if (null != aMachineVo.getReportFilePath05() && !aMachineVo.getReportFilePath05().trim().equals("")) {
                                docsList.add(aMachineVo.getReportFilePath05());
                            }
                            if (null != aMachineVo.getReportFilePath06() && !aMachineVo.getReportFilePath06().trim().equals("")) {
                                docsList.add(aMachineVo.getReportFilePath06());
                            }
                            if (null != aMachineVo.getReportFilePath07() && !aMachineVo.getReportFilePath07().trim().equals("")) {
                                docsList.add(aMachineVo.getReportFilePath07());
                            }
                            if (null != aMachineVo.getReportFilePath08() && !aMachineVo.getReportFilePath08().trim().equals("")) {
                                docsList.add(aMachineVo.getReportFilePath08());
                            }
                            if (null != aMachineVo.getReportFilePath09() && !aMachineVo.getReportFilePath09().trim().equals("")) {
                                docsList.add(aMachineVo.getReportFilePath09());
                            }
                            if (null != aMachineVo.getReportFilePath10() && !aMachineVo.getReportFilePath10().trim().equals("")) {
                                docsList.add(aMachineVo.getReportFilePath10());
                            }
                        }

                        response = mstMachineService.updateExtMachinesByBatch(machineDetails.getMstMachineVos());
                        logger.log(info, "設備マスタのデータを取得・更新しました。", methodName);
                    } else {
                        logger.log(info, "設備マスタのデータが見つかりません。", methodName);
                    }

                } catch (Exception e) {
                    response.setError(true);
                    logger.log(severe, "設備マスタのデータを取得・更新失敗しました。", e);
                }
            }

            //外部・設備稼働履歴テーブルのデータを取得するためのAPIをコール。
            if (response.isError() == false) {
                try {
                    
                    //最初の自社のＭａｘ日付を取得し、その後のデータを連携する
                    TblMachineHistory tblMachineHistory = tblMachineHistoryService.getLatestCreateDate(localMachine.getUuid());
                    String machineUuid = machineDetails.getMstMachineVos().get(0).getMachineUuid();
                    String histUrlPara = "?machineUuid=" + machineUuid;
                    
                    String histStartDate;
                    if (tblMachineHistory != null) {
                        histStartDate = DateFormat.dateToStrMill(tblMachineHistory.getTblMachineHistoryPK().getStartDate());
                        histUrlPara += "&histStartDate=" + histStartDate.replace(" ", "-");
                    }
                    
                    String iMachineHistorys = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINEHISTORY_API_URL + histUrlPara, token);
                    TblMachineHistoryList machineHistorys = gson.fromJson(iMachineHistorys, new TypeToken<TblMachineHistoryList>() {
                    }.getType());
                    if (null != machineHistorys && null != machineHistorys.getTblMachineHistoryVos() && machineHistorys.getTblMachineHistoryVos().size() > 0) {
                        response = tblMachineHistoryService.updateExtMachineHistorysByBatch(machineHistorys.getTblMachineHistoryVos());
                        logger.log(info, "設備稼働履歴テーブルのデータを取得・更新しました。", methodName);
                    } else {
                        logger.log(info, "設備稼働履歴テーブルのデータが見つかりません。", methodName);
                    }

                } catch (Exception e) {
                    response.setError(true);
                    logger.log(severe, "設備稼働履歴テーブルのデータを取得・更新失敗しました。", e);
                }
            }

            //外部・Σ軍師ログテーブルのデータを取得するためのAPIをコール。
            if (response.isError() == false) {
                //最初の自社のＭａｘ日付を取得
                TblSigmaLog tblSigmaLog = tblSigmaLogService.getLatestCreateDate(localMachine.getUuid());
 
                String machineUuid = machineDetails.getMstMachineVos().get(0).getMachineUuid();
                String sigmaUrlPara = "?machineUuid=" + machineUuid;
                String logLatestExecutedDate;
                if (tblSigmaLog != null) {
                    logLatestExecutedDate = DateFormat.dateToStrMill(tblSigmaLog.getTblSigmaLogPK().getCreateDate());
                    sigmaUrlPara += "&latestCreateDate=" + logLatestExecutedDate.replace(" ", "-") + "&eventNo=" + tblSigmaLog.getTblSigmaLogPK().getEventNo();
                }

                try {
                    TblSigmaLogList sigmaLogs;
                    do {
                        String iSigmaLogs = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_SIGMALOG_API_URL + sigmaUrlPara, token);
                        sigmaLogs = gson.fromJson(iSigmaLogs, new TypeToken<TblSigmaLogList>() {
                        }.getType());

                        if (sigmaLogs != null && null != sigmaLogs.getTblSigmaLogVos() && sigmaLogs.getTblSigmaLogVos().size() > 0) {
                            response = tblSigmaLogService.updateExtSigmaLogsByBatch(sigmaLogs.getTblSigmaLogVos());
                            logger.log(info, "Σ軍師ログテーブルのデータを取得・更新しました。", methodName);

                            TblSigmaLog nextTblSigmaLog = tblSigmaLogService.getLatestCreateDate(localMachine.getUuid());
                            sigmaUrlPara = "?machineUuid=" + machineUuid;
                            if (nextTblSigmaLog != null) {
                                logLatestExecutedDate = DateFormat.dateToStrMill(nextTblSigmaLog.getTblSigmaLogPK().getCreateDate());
                                sigmaUrlPara += "&latestCreateDate=" + logLatestExecutedDate.replace(" ", "-") + "&eventNo=" + nextTblSigmaLog.getTblSigmaLogPK().getEventNo();
                            }
                        } else {
                            logger.log(info, "Σ軍師ログテーブルのデータが見つかりません。", methodName);
                        }
                    } while (sigmaLogs != null && null != sigmaLogs.getTblSigmaLogVos() && !sigmaLogs.getTblSigmaLogVos().isEmpty());
                } catch (Exception e) {
                    response.setError(true);
                    logger.log(severe, "Σ軍師ログテーブルのデータを取得・更新失敗しました。", e);
                }
            }

            //外部・設備仕様履歴マスタのデータを取得するためのAPIをコール。
            if (response.isError() == false) {
                try {
                    String iMachineSpecHistorys = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINESPECHISTORY_API_URL + urlPara, token);
                    MstMachineSpecHistoryList machineSpecHistorys = gson.fromJson(iMachineSpecHistorys, new TypeToken<MstMachineSpecHistoryList>() {
                    }.getType());
                    if (null != machineSpecHistorys && null != machineSpecHistorys.getMstMachineSpecHistoryVos() && machineSpecHistorys.getMstMachineSpecHistoryVos().size() > 0) {
                        response = mstMachineSpecHistoryService.updateExtMachineSpecHistorysByBatch(machineSpecHistorys.getMstMachineSpecHistoryVos());
                        logger.log(info, "設備仕様履歴マスタのデータを取得・更新しました。", methodName);
                    } else {
                        logger.log(info, "設備仕様履歴マスタのデータが見つかりません。", methodName);
                    }

                } catch (Exception e) {
                    response.setError(true);
                    logger.log(severe, "設備仕様履歴マスタのデータを取得・更新失敗しました。", e);
                }
            }

            //外部・設備仕様マスタのデータを取得するためのAPIをコール。
            if (response.isError() == false) {
                try {
                    String iMachineSpecs = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINESPEC_API_URL + urlPara, token);
                    MstMachineSpecList machineSpecs = gson.fromJson(iMachineSpecs, new TypeToken<MstMachineSpecList>() {
                    }.getType());
                    if (null != machineSpecs && null != machineSpecs.getMstMachineSpecs() && machineSpecs.getMstMachineSpecs().size() > 0) {
                        response = mstMachineSpecService.updateExtMachineSpecsByBatch(machineSpecs.getMstMachineSpecs());
                        logger.log(info, "設備仕様マスタのデータを取得・更新しました。", methodName);
                    } else {
                        logger.log(info, "設備仕様マスタのデータが見つかりません。", methodName);
                    }

                } catch (Exception e) {
                    response.setError(true);
                    logger.log(severe, "設備仕様マスタのデータを取得・更新失敗しました。", e);
                }
            }

            //外部・設備成形条件のデータを取得するためのAPIをコール。
            if (response.isError() == false) {
                try {
                    String iProcConds = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINEPROCCOND_API_URL + urlPara, token);
                    MstMachineProcCondList machineProcConds = gson.fromJson(iProcConds, new TypeToken<MstMachineProcCondList>() {
                    }.getType());
                    if (null != machineProcConds && null != machineProcConds.getMstMachineProcCondVos() && machineProcConds.getMstMachineProcCondVos().size() > 0) {

                        response = mstMachineProcCondService.updateExtMachineProcCondsByBatch(machineProcConds.getMstMachineProcCondVos(), mstComponentCompanyVoList);
                        logger.log(info, "設備成形条件のデータを取得・更新しました。", methodName);
                    } else {
                        logger.log(info, "設備成形条件のデータが見つかりません。", methodName);
                    }

                } catch (Exception e) {
                    response.setError(true);
                    logger.log(severe, "設備成形条件のデータを取得・更新失敗しました。", e);
                }
            }

            //外部・設備加工条件仕様のデータを取得するためのAPIをコール。
            if (response.isError() == false) {
                try {
                    String iProcCondSpecs = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINEPROCCONDSPEC_API_URL + urlPara, token);
                    MstMachineProcCondSpecList machineProcCondSpecs = gson.fromJson(iProcCondSpecs, new TypeToken<MstMachineProcCondSpecList>() {
                    }.getType());
                    if (null != machineProcCondSpecs.getMstMachineProcCondSpecs() && machineProcCondSpecs.getMstMachineProcCondSpecs().size() > 0) {

                        response = mstMachineProcCondSpecService.updateExtMachineProcCondSpecsByBatch(machineProcCondSpecs.getMstMachineProcCondSpecs());
                        logger.log(info, "設備成形条件仕様のデータを取得・更新しました。", methodName);
                    } else {
                        logger.log(info, "設備成形条件仕様のデータが見つかりません。", methodName);
                    }

                } catch (Exception e) {
                    response.setError(true);
                    logger.log(severe, "設備成形条件仕様のデータを取得・更新失敗しました。", e);
                }
            }

            //外部・設備所在履歴のデータを取得するためのAPIをコール。
            if (response.isError() == false) {
                try {
                    String iLocationHistorys = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINELOCATIONHISTORY_API_URL + urlPara, token);
                    TblMachineLocationHistoryList machineLocationHistorys = gson.fromJson(iLocationHistorys, new TypeToken<TblMachineLocationHistoryList>() {
                    }.getType());
                    if (null != machineLocationHistorys.getTblMachineLocationHistoryVos() && machineLocationHistorys.getTblMachineLocationHistoryVos().size() > 0) {

                        response = tblMachineLocationHistoryService.updateExtMachineLocationHistorysByBatch(machineLocationHistorys.getTblMachineLocationHistoryVos());
                        logger.log(info, "設備所在履歴のデータを取得・更新しました。", methodName);
                    } else {
                        logger.log(info, "設備所在履歴のデータが見つかりません。", methodName);
                    }

                } catch (Exception e) {
                    response.setError(true);
                    logger.log(severe, "設備所在履歴のデータを取得・更新失敗しました。", e);
                }
            }

            //外部・異常テーブルのデータを取得するためのAPIをコール。
            if (response.isError() == false) {
                try {
                    String iIssueVos = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINEISSUE_API_URL + urlPara, token);
                    TblIssueList issueVos = gson.fromJson(iIssueVos, new TypeToken<TblIssueList>() {
                    }.getType());

                    if (null != issueVos.getTblIssueVoList() && issueVos.getTblIssueVoList().size() > 0) {
                        for (TblIssueVo aTblIssueVo : issueVos.getTblIssueVoList()) {
                            if (null != aTblIssueVo.getTblIssue().getReportFilePath01() && !aTblIssueVo.getTblIssue().getReportFilePath01().trim().equals("")) {
                                docsList.add(aTblIssueVo.getTblIssue().getReportFilePath01());
                            }
                            if (null != aTblIssueVo.getTblIssue().getReportFilePath02() && !aTblIssueVo.getTblIssue().getReportFilePath02().trim().equals("")) {
                                docsList.add(aTblIssueVo.getTblIssue().getReportFilePath02());
                            }
                            if (null != aTblIssueVo.getTblIssue().getReportFilePath03() && !aTblIssueVo.getTblIssue().getReportFilePath03().trim().equals("")) {
                                docsList.add(aTblIssueVo.getTblIssue().getReportFilePath03());
                            }
                            if (null != aTblIssueVo.getTblIssue().getReportFilePath04() && !aTblIssueVo.getTblIssue().getReportFilePath04().trim().equals("")) {
                                docsList.add(aTblIssueVo.getTblIssue().getReportFilePath04());
                            }
                            if (null != aTblIssueVo.getTblIssue().getReportFilePath05() && !aTblIssueVo.getTblIssue().getReportFilePath05().trim().equals("")) {
                                docsList.add(aTblIssueVo.getTblIssue().getReportFilePath05());
                            }
                            if (null != aTblIssueVo.getTblIssue().getReportFilePath06() && !aTblIssueVo.getTblIssue().getReportFilePath06().trim().equals("")) {
                                docsList.add(aTblIssueVo.getTblIssue().getReportFilePath06());
                            }
                            if (null != aTblIssueVo.getTblIssue().getReportFilePath07() && !aTblIssueVo.getTblIssue().getReportFilePath07().trim().equals("")) {
                                docsList.add(aTblIssueVo.getTblIssue().getReportFilePath07());
                            }
                            if (null != aTblIssueVo.getTblIssue().getReportFilePath08() && !aTblIssueVo.getTblIssue().getReportFilePath08().trim().equals("")) {
                                docsList.add(aTblIssueVo.getTblIssue().getReportFilePath08());
                            }
                            if (null != aTblIssueVo.getTblIssue().getReportFilePath09() && !aTblIssueVo.getTblIssue().getReportFilePath09().trim().equals("")) {
                                docsList.add(aTblIssueVo.getTblIssue().getReportFilePath09());
                            }
                            if (null != aTblIssueVo.getTblIssue().getReportFilePath10() && !aTblIssueVo.getTblIssue().getReportFilePath10().trim().equals("")) {
                                docsList.add(aTblIssueVo.getTblIssue().getReportFilePath10());
                            }
                            if (null != aTblIssueVo.getTblIssueImageFileVoList() && !aTblIssueVo.getTblIssueImageFileVoList().isEmpty()) {
                                for (TblIssueImageFileVo aTblIssueImageFileVo : aTblIssueVo.getTblIssueImageFileVoList()) {
                                    if (aTblIssueImageFileVo.getFileType().compareTo(1) == 0) {
                                        //pic
                                        imagesList.add(aTblIssueImageFileVo.getFileUuid());
                                    } else {
                                        //video                                                
                                        videosList.add(aTblIssueImageFileVo.getFileUuid());
                                    }
                                }
                            }
                        }

                        response = tblIssueService.updateExtIssuesByBatch(issueVos.getTblIssueVoList(), false, mstComponentCompanyVoList);
                        logger.log(info, "異常テーブルのデータを取得・更新しました。", methodName);
                    } else {
                        logger.log(info, "異常テーブルのデータが見つかりません。", methodName);
                    }
                } catch (Exception e) {
                    response.setError(true);
                    logger.log(severe, "異常テーブルのデータを取得・更新失敗しました。", e);
                }
            }

            //外部・設備メンテナンス改造テーブルのデータを取得するためのAPIをコール。
            if (response.isError() == false) {
                try {
                    String iMachinemaintenanceremodelingVos = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINEMAINTENANCEREMODELING_API_URL + urlPara, token);
                    TblMachineMaintenanceRemodelingList machineMaintenanceRemodelingVos = gson.fromJson(iMachinemaintenanceremodelingVos, new TypeToken<TblMachineMaintenanceRemodelingList>() {
                    }.getType());
                    if (null != machineMaintenanceRemodelingVos.getTblMachineMaintenanceRemodelingVos() && machineMaintenanceRemodelingVos.getTblMachineMaintenanceRemodelingVos().size() > 0) {

                        response = tblMachineMaintenanceRemodelingService.updateExtMachineMaintenanceRemodelingsByBatch(machineMaintenanceRemodelingVos.getTblMachineMaintenanceRemodelingVos());
                        logger.log(info, "設備メンテナンス改造テーブルのデータを取得・更新しました。", methodName);
                    } else {
                        logger.log(info, "設備メンテナンス改造テーブルのデータが見つかりません。", methodName);
                    }
                } catch (Exception e) {
                    response.setError(true);
                    logger.log(severe, "設備メンテナンス改造テーブルのデータを取得・更新失敗しました。", e);
                }
            }

            //外部・異常テーブルのデータを取得するためのAPIをコール Update。
            if (response.isError() == false) {
                try {
                    String iIssueVos = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINEISSUE_API_URL + urlPara, token);
                    TblIssueList issueVos = gson.fromJson(iIssueVos, new TypeToken<TblIssueList>() {
                    }.getType());

                    if (null != issueVos.getTblIssueVoList() && issueVos.getTblIssueVoList().size() > 0) {
                        response = tblIssueService.updateExtIssuesByBatch(issueVos.getTblIssueVoList(), true, mstComponentCompanyVoList);
                        logger.log(info, "異常テーブルのデータを取得・更新しました。", methodName);
                    } else {
                        logger.log(info, "異常テーブルのデータが見つかりません。", methodName);
                    }
                } catch (Exception e) {
                    response.setError(true);
                    logger.log(severe, "異常テーブルのデータを取得・更新失敗しました。", e);
                }
            }

            //外部・設備メンテナンス詳細のデータを取得するためのAPIをコール。
            if (response.isError() == false) {
                try {
                    String iMachineMaintenanceDetails = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINEMAINTENANCEDETAIL_API_URL + urlPara, token);
                    TblMachineMaintenanceDetailVo machineMaintenanceDetails = gson.fromJson(iMachineMaintenanceDetails, new TypeToken<TblMachineMaintenanceDetailVo>() {
                    }.getType());

                    if (null != machineMaintenanceDetails.getTblMachineMaintenanceDetails() && machineMaintenanceDetails.getTblMachineMaintenanceDetails().size() > 0) {

                        response = tblMachineMaintenanceDetailService.updateExtMachineMaintenanceDetailsByBatch(machineMaintenanceDetails.getTblMachineMaintenanceDetails());
                        logger.log(info, "設備メンテナンス詳細のデータを取得・更新しました。", methodName);
                    } else {
                        logger.log(info, "設備メンテナンス詳細のデータが見つかりません。", methodName);
                    }

                } catch (Exception e) {
                    response.setError(true);
                    logger.log(severe, "設備メンテナンス詳細のデータを取得・更新失敗しました。", e);
                }
            }
            
            //設備メンテナンス詳細イメージファイルテーブルのデータを取得するためのAPIをコール。
            if (response.isError() == false) {
                try {
                    String iMachineRemodelingDetailImageFiles = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINEMAINTENANCEDETAILMAGEFILE_API_URL + urlPara, token);
                    TblMachineMaintenanceDetailVo machineMaintenanceDetails = gson.fromJson(iMachineRemodelingDetailImageFiles, new TypeToken<TblMachineMaintenanceDetailVo>() {
                    }.getType());
                    if (null != machineMaintenanceDetails.getTblMachineMaintenanceDetailImageFileVos() && machineMaintenanceDetails.getTblMachineMaintenanceDetailImageFileVos().size() > 0) {
                        for (TblMachineMaintenanceDetailImageFileVo aImageFileVo : machineMaintenanceDetails.getTblMachineMaintenanceDetailImageFileVos()) {
                            if (null != aImageFileVo.getFileUuid()) {
                                if (aImageFileVo.getFileType().equals("" + CommonConstants.IMAGEFILE_TYPE_IMAGE)) {
                                    imagesList.add(aImageFileVo.getFileUuid());
                                } else if (aImageFileVo.getFileType().equals("" + CommonConstants.IMAGEFILE_TYPE_VIDEO)) {
                                    docsList.add(aImageFileVo.getFileUuid());
                                }
                            }
                        }

                        response = tblMachineMaintenanceDetailService.updateExtMoldMaintenanceDetailImageFilesByBatch(machineMaintenanceDetails.getTblMachineMaintenanceDetailImageFileVos());
                        logger.log(info, "設備メンテナンス詳細イメージファイルテーブルのデータを取得・更新しました。", methodName);
                    } else {
                        logger.log(info, "設備メンテナンス詳細イメージファイルテーブルのデータが見つかりません。", methodName);
                    }

                } catch (Exception e) {
                    response.setError(true);
                    logger.log(severe, "設備メンテナンス詳細イメージファイルテーブルのデータを取得・更新失敗しました。", e);
                }
            }

            //外部・設備改造詳細のデータを取得するためのAPIをコール。
            if (response.isError() == false) {
                try {
                    String iMachineRemodelingDetails = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINEREMODELINGDETAIL_API_URL + urlPara, token);
                    TblMachineRemodelingDetailVo machineRemodelingDetails = gson.fromJson(iMachineRemodelingDetails, new TypeToken<TblMachineRemodelingDetailVo>() {
                    }.getType());

                    if (null != machineRemodelingDetails.getTblMachineRemodelingDetails() && machineRemodelingDetails.getTblMachineRemodelingDetails().size() > 0) {

                        response = tblMachineRemodelingDetailService.updateExtMachineRemodelingDetailsByBatch(machineRemodelingDetails.getTblMachineRemodelingDetails());
                        logger.log(info, "設備改造詳細のデータを取得・更新しました。", methodName);
                    } else {
                        logger.log(info, "設備改造詳細のデータが見つかりません。", methodName);
                    }

                } catch (Exception e) {
                    response.setError(true);
                    logger.log(severe, "設備改造詳細のデータを取得・更新失敗しました。", e);
                }
            }
            
            //設備改造詳細イメージファイルテーブルのデータを取得するためのAPIをコール。
            if (response.isError() == false) {
                try {
                    String iMachineRemodelingDetailImageFiles = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINEREMODELINGDETAILIMAGEFILE_API_URL + urlPara, token);
                    TblMachineRemodelingDetailVo machineRemodelingDetails = gson.fromJson(iMachineRemodelingDetailImageFiles, new TypeToken<TblMachineRemodelingDetailVo>() {
                    }.getType());
                    if (null != machineRemodelingDetails.getMachineRemodelingDetailImageFileVos() && machineRemodelingDetails.getMachineRemodelingDetailImageFileVos().size() > 0) {
                        for (TblMachineRemodelingDetailImageFileVo aImageFileVo : machineRemodelingDetails.getMachineRemodelingDetailImageFileVos()) {
                            if (null != aImageFileVo.getFileUuid()) {
                                if (aImageFileVo.getFileType().equals("" + CommonConstants.IMAGEFILE_TYPE_IMAGE)) {
                                    imagesList.add(aImageFileVo.getFileUuid());
                                } else if (aImageFileVo.getFileType().equals("" + CommonConstants.IMAGEFILE_TYPE_VIDEO)) {
                                    docsList.add(aImageFileVo.getFileUuid());
                                }
                            }
                        }
                        response = tblMachineRemodelingDetailService.updateExtMoldRemodelingDetailImageFilesByBatch(machineRemodelingDetails.getMachineRemodelingDetailImageFileVos());
                        logger.log(info, "設備改造詳細イメージファイルテーブルのデータを取得・更新しました。", methodName);
                    } else {
                        logger.log(info, "設備改造詳細イメージファイルテーブルのデータが見つかりません。", methodName);
                    }

                } catch (Exception e) {
                    response.setError(true);
                    logger.log(severe, "設備改造詳細イメージファイルテーブルのデータを取得・更新失敗しました。", e);
                }
            }

            //設備点検结果のデータを取得するためのAPIをコール。
            if (response.isError() == false) {
                try {
                    String iMachineInspectionResults = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINEINSPECTIONRESULT_API_URL + urlPara, token);
                    TblMachineInspectionResultVo machineInspectionResults = gson.fromJson(iMachineInspectionResults, new TypeToken<TblMachineInspectionResultVo>() {
                    }.getType());
                    if (null != machineInspectionResults.getMachineInspectionResultVos() && machineInspectionResults.getMachineInspectionResultVos().size() > 0) {

                        response = tblMachineInspectionResultService.updateExtMachineInspectionResultsByBatch(machineInspectionResults.getMachineInspectionResultVos());
                        logger.log(info, "設備点検结果のデータを取得・更新しました。", methodName);
                    } else {
                        logger.log(info, "設備点検结果のデータが見つかりません。", methodName);
                    }

                } catch (Exception e) {
                    response.setError(true);
                    logger.log(severe, "設備点検结果のデータを取得・更新失敗しました。", e);
                }
            }

            //設備改造点検结果のデータを取得するためのAPIをコール。
            if (response.isError() == false) {
                try {
                    String iMachineRemodelingInspectionResults = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINEREMODELINGINSPECTIONRESULT_API_URL + urlPara, token);
                    TblMachineRemodelingInspectionResultVo machineRemodelingInspectionResults = gson.fromJson(iMachineRemodelingInspectionResults, new TypeToken<TblMachineRemodelingInspectionResultVo>() {
                    }.getType());
                    if (null != machineRemodelingInspectionResults.getMachineRemodelingInspectionResultVo() && machineRemodelingInspectionResults.getMachineRemodelingInspectionResultVo().size() > 0) {

                        response = tblMachineRemodelingInspectionResultService.updateExtMachineRemodelingInspectionResultsByBatch(machineRemodelingInspectionResults.getMachineRemodelingInspectionResultVo());
                        logger.log(info, "設備改造点検结果のデータを取得・更新しました。", methodName);
                    } else {
                        logger.log(info, "設備改造点検结果のデータが見つかりません。", methodName);
                    }

                } catch (Exception e) {
                    response.setError(true);
                    logger.log(severe, "設備改造点検结果のデータを取得・更新失敗しました。", e);
                }
            }

            //生産実績テーブル
            if (response.isError() == false) {
                try {
                    String iProductions = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINEPRODUCTION_API_URL + urlPara, token);
                    Gson gson2 = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
                    TblProductionList productions = gson2.fromJson(iProductions, new TypeToken<TblProductionList>() {
                    }.getType());
                    if (null != productions.getTblProductionVo() && productions.getTblProductionVo().size() > 0) {
                        response = tblProductionService.updateExtProductionsByBatch(productions.getTblProductionVo());
                        logger.log(info, "設備生産実績テーブルのデータを取得・更新しました。", methodName);
                    } else {
                        logger.log(info, "設備生産実績テーブルのデータが見つかりません。", methodName);
                    }

                } catch (Exception e) {
                    response.setError(true);
                    logger.log(severe, "設備生産実績テーブルのデータを取得・更新失敗しました。", e);
                }
            }
            //生産実績明細テーブル
            if (response.isError() == false) {
                try {
                    String iProductionDetails = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINEPRODUCTIONDETAIL_API_URL + urlPara, token);
                    TblProductionDetailVo productionDetails = gson.fromJson(iProductionDetails, new TypeToken<TblProductionDetailVo>() {
                    }.getType());
                    if (null != productionDetails.getTblProductionDetailVos() && productionDetails.getTblProductionDetailVos().size() > 0) {
                        response = tblProductionDetailService.updateExtProductionDetailsByBatch(productionDetails.getTblProductionDetailVos(), mstComponentCompanyVoList);
                        logger.log(info, "設備生産実績明細テーブルのデータを取得・更新しました。", methodName);
                    } else {
                        logger.log(info, "設備生産実績明細テーブルのデータが見つかりません。", methodName);
                    }

                } catch (Exception e) {
                    response.setError(true);
                    logger.log(severe, "設備生産実績明細テーブルのデータを取得・更新失敗しました。", e);
                }
            }
        }
        //File download
        try {
            logger.log(info, "設備ファイルダウンロード開始。", methodName);
            String fileName = "";
            for (String imageUuid : imagesList) {
                fileName = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_FILENAME_API_URL + "?uuid=" + imageUuid + "&fileType=" + CommonConstants.IMAGE, token);
                if (null != fileName && !fileName.trim().equals("")) {
                    FileUtil.getFileGet(apiBaseUrl + EXTLOGIN_FILESDOWNLOAD_API_URL + "?uuid=" + imageUuid + "&fileType=" + CommonConstants.IMAGE, fileName, token, imagePath);
                }
            }
            String docIds = "";
            for (String docUuid : docsList) {
                docIds += docUuid;
                docIds += ",";
                fileName = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_FILENAME_API_URL + "?uuid=" + docUuid + "&fileType=" + CommonConstants.DOC, token);
                if (null != fileName && !fileName.trim().equals("")) {
                    FileUtil.getFileGet(apiBaseUrl + EXTLOGIN_FILESDOWNLOAD_API_URL + "?uuid=" + docUuid + "&fileType=" + CommonConstants.DOC, fileName, token, docPath);
                }
            }

            if (!docIds.equals("")) {
                docIds = docIds.substring(0, docIds.length() - 1);
                String strUploadFiles = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_UPLOADFILE_API_URL + "?docIds=" + docIds, token);
                if (null != strUploadFiles && !strUploadFiles.trim().equals("")) {
                    TblUploadFileList UploadFiles = gson.fromJson(strUploadFiles, new TypeToken<TblUploadFileList>() {
                    }.getType());
                    if (null == tblUploadFileService) {
                        tblUploadFileService = new TblUploadFileService();
                    }
                    if (null != UploadFiles.getTblUploadFiles() && UploadFiles.getTblUploadFiles().size() > 0) {
                        response = tblUploadFileService.updateExtIssuesByBatch(UploadFiles.getTblUploadFiles());
                        logger.log(info, "設備UploadFileテーブルのデータを取得・更新しました。", methodName);
                    } else {
                        logger.log(info, "設備UploadFileテーブルのデータが見つかりません。", methodName);
                    }
                }
            }

            for (String videoUuid : videosList) {
                fileName = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_FILENAME_API_URL + "?uuid=" + videoUuid + "&fileType=" + CommonConstants.VIDEO, token);
                if (null != fileName && !fileName.trim().equals("")) {
                    FileUtil.getFileGet(apiBaseUrl + EXTLOGIN_FILESDOWNLOAD_API_URL + "?uuid=" + videoUuid + "&fileType=" + CommonConstants.VIDEO, fileName, token, videoPath);
                }
            }
            logger.log(info, "設備ファイルダウンロード終了。", methodName);
        } catch (Exception e) {
            response.setError(true);
            logger.log(severe, "設備file download 失敗しました。", e);
        }

        return response;
    }

}
