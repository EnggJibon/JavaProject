package com.kmcj.karte.batch.externalmold;

/**
 * このバッチが外部金型関連データだけを取得する
 *
 * @author zds
 */
import com.kmcj.karte.batch.externalmold.delete.TblDeletedKeyService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.batch.externalmold.delete.TblDeletedKeyVo;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.Credential;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.choice.MstChoiceVo;
import com.kmcj.karte.resources.company.MstCompanyList;
import com.kmcj.karte.resources.company.MstCompanyService;
import com.kmcj.karte.resources.component.company.MstComponentCompanyVoList;
import com.kmcj.karte.resources.external.MstExternalDataGetSetting;
import com.kmcj.karte.resources.external.MstExternalDataGetSettingService;
import com.kmcj.karte.resources.files.TblUploadFileList;
import com.kmcj.karte.resources.files.TblUploadFileService;
import com.kmcj.karte.resources.installationsite.MstInstallationSiteList;
import com.kmcj.karte.resources.installationsite.MstInstallationSiteService;
import com.kmcj.karte.resources.location.MstLocationService;
import com.kmcj.karte.resources.location.MstLocationVo;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.MstMoldDetail;
import com.kmcj.karte.resources.mold.MstMoldDetailList;
import com.kmcj.karte.resources.mold.MstMoldService;
import com.kmcj.karte.resources.mold.attribute.MstMoldAttributeList;
import com.kmcj.karte.resources.mold.attribute.MstMoldAttributeService;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelationList;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelationService;
import com.kmcj.karte.resources.mold.inspection.choice.MstMoldInspectionChoiceService;
import com.kmcj.karte.resources.mold.inspection.choice.MstMoldInspectionChoiceVo;
import com.kmcj.karte.resources.mold.inspection.item.MstMoldInspectionItemList;
import com.kmcj.karte.resources.mold.inspection.item.MstMoldInspectionItemService;
import com.kmcj.karte.resources.mold.inspection.result.TblMoldInspectionResultService;
import com.kmcj.karte.resources.mold.inspection.result.TblMoldInspectionResultVo;
import com.kmcj.karte.resources.mold.inventory.TblMoldInventoryList;
import com.kmcj.karte.resources.mold.inventory.TblMoldInventoryService;
import com.kmcj.karte.resources.mold.inventory.TblMoldInventorys;
import com.kmcj.karte.resources.mold.issue.TblIssueImageFileVo;
import com.kmcj.karte.resources.mold.issue.TblIssueList;
import com.kmcj.karte.resources.mold.issue.TblIssueService;
import com.kmcj.karte.resources.mold.issue.TblIssueVo;
import com.kmcj.karte.resources.mold.location.history.TblMoldLocationHistoryList;
import com.kmcj.karte.resources.mold.location.history.TblMoldLocationHistoryService;
import com.kmcj.karte.resources.mold.maintenance.detail.TblMoldMaintenanceDetailImageFileVo;
import com.kmcj.karte.resources.mold.maintenance.detail.TblMoldMaintenanceDetailService;
import com.kmcj.karte.resources.mold.maintenance.detail.TblMoldMaintenanceDetailVo;
import com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodelingList;
import com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodelingService;
import com.kmcj.karte.resources.mold.proccond.MstMoldProcCondList;
import com.kmcj.karte.resources.mold.proccond.MstMoldProcCondService;
import com.kmcj.karte.resources.mold.proccond.attribute.MstMoldProcCondAttributeList;
import com.kmcj.karte.resources.mold.proccond.attribute.MstMoldProcCondAttributeService;
import com.kmcj.karte.resources.mold.proccond.spec.MstMoldProcCondSpecList;
import com.kmcj.karte.resources.mold.proccond.spec.MstMoldProcCondSpecService;
import com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetailImageFileVo;
import com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetailService;
import com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetailVo;
import com.kmcj.karte.resources.mold.remodeling.inspection.TblMoldRemodelingInspectionResultService;
import com.kmcj.karte.resources.mold.remodeling.inspection.TblMoldRemodelingInspectionResultVo;
import com.kmcj.karte.resources.mold.spec.MstMoldSpecList;
import com.kmcj.karte.resources.mold.spec.MstMoldSpecService;
import com.kmcj.karte.resources.mold.spec.history.MstMoldSpecHistoryList;
import com.kmcj.karte.resources.mold.spec.history.MstMoldSpecHistoryService;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import com.kmcj.karte.resources.procedure.MstProcedureVo;
import com.kmcj.karte.resources.production.TblProductionList;
import com.kmcj.karte.resources.production.TblProductionService;
import com.kmcj.karte.resources.production.detail.TblProductionDetailService;
import com.kmcj.karte.resources.production.detail.TblProductionDetailVo;
import com.kmcj.karte.util.FileUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@Dependent
public class ExternalMoldBatchlet extends AbstractBatchlet {

    // ジョブ名称を取得するためにJobContextをインジェクション
    @Inject
    JobContext jobContext;
    @Inject
    private KartePropertyService kartePropertyService;
    @Inject
    public MstExternalDataGetSettingService externalDataGetSettingService;
    @Inject
    public MstCompanyService mstCompanyService;
    @Inject
    public MstInstallationSiteService mstInstallationSiteService;
    @Inject
    public MstLocationService mstLocationService;
    @Inject
    public MstMoldAttributeService mstMoldAttributeService;
    @Inject
    public MstMoldInspectionItemService mstMoldInspectionItemService;
    @Inject
    private MstMoldProcCondAttributeService mstMoldProcCondAttributeService;
    @Inject
    private MstMoldInspectionChoiceService mstMoldInspectionChoiceService;
    @Inject
    private MstChoiceService mstChoiceService;
    @Inject
    private MstMoldService mstMoldService;
    @Inject
    private MstMoldComponentRelationService mstMoldComponentRelationService;
    @Inject
    private MstMoldSpecHistoryService mstMoldSpecHistoryService;
    @Inject
    private MstMoldSpecService mstMoldSpecService;
    @Inject
    private MstMoldProcCondService mstMoldProcCondService;
    @Inject
    private MstMoldProcCondSpecService mstMoldProcCondSpecService;
    @Inject
    private TblMoldLocationHistoryService tblMoldLocationHistoryService;
    @Inject
    private TblMoldInventoryService tblMoldInventoryService;
    @Inject
    private TblIssueService tblIssueService;
    @Inject
    private TblMoldMaintenanceRemodelingService tblMoldMaintenanceRemodelingService;
    @Inject
    private TblMoldMaintenanceDetailService tblMoldMaintenanceDetailService;
    @Inject
    private TblMoldRemodelingDetailService tblMoldRemodelingDetailService;
    @Inject
    private TblDeletedKeyService tblDeletedKeyService;
    @Inject
    private TblMoldInspectionResultService tblMoldInspectionResultService;
    @Inject
    private TblMoldRemodelingInspectionResultService tblMoldRemodelingInspectionResultService;
    @Inject
    private MstProcedureService mstProcedureService;
    @Inject
    private TblProductionService tblProductionService;
    @Inject
    private TblProductionDetailService tblProductionDetailService;
    @Inject
    private TblUploadFileService tblUploadFileService;
    // 外部認証ＡＰＩ
    private static final String EXTLOGIN_LOGIN_API_URL = "ws/karte/api/authentication/extlogin?lang=ja";
    //DeletedKey
    private static final String EXTLOGIN_DELETEDKEY_API_URL = "ws/karte/api/deletedkey/extdeletedkey";
    //会社マスタ	
    private static final String EXTLOGIN_COMPANY_API_URL = "ws/karte/api/company/extcompany";
    //設置場所マスタ
    private static final String EXTLOGIN_INSTALLATIONSITE_API_URL = "ws/karte/api/installationsite/extinstallationsite";
    //所在地マスタ	
    private static final String EXTLOGIN_LOCATION_API_URL = "ws/karte/api/location/extlocation";
    //金型属性マスタ
    private static final String EXTLOGIN_MOLDATTRIBUTE_API_URL = "ws/karte/api/mold/attribute/extmoldattribute";
    //金型点検項目マスタ
    private static final String EXTLOGIN_MOLDINSPECTIONITEM_API_URL = "ws/karte/api/mold/inspection/item/extmoldinspectionitem";
    //金型加工条件属性マスタ	
    private static final String EXTLOGIN_MOLDPROCCONDATTRIBUTE_API_URL = "ws/karte/api/mold/proccond/attribute/extmoldproccondattribute";
    //金型点検項目選択肢マスタ	外部テーブル
    private static final String EXTLOGIN_MOLDINSPECTIONCHOICE_API_URL = "ws/karte/api/mold/inspection/choice/extmoldinspectionchoice";
    //選択肢マスタ	外部テーブル	
    private static final String EXTLOGIN_CHOICE_API_URL = "ws/karte/api/choice/extchoice";
    //金型棚卸	
    private static final String EXTLOGIN_MOLDINVENTORY_API_URL = "ws/karte/api/mold/inventory/extmoldinventory";
    //金型マスタ	
    private static final String EXTLOGIN_MOLD_API_URL = "ws/karte/api/mold/extmold";
    //金型部品関係	
    private static final String EXTLOGIN_MOLDCOMPONENTRELATION_API_URL = "ws/karte/api/mold/component/relation/extmoldcomponentrelation";
    //金型仕様履歴マスタ
    private static final String EXTLOGIN_MOLDSPECHISTORYRESOURCE_API_URL = "ws/karte/api/mold/spec/history/extmoldspechistory";
    //金型仕様	
    private static final String EXTLOGIN_MOLDSPEC_API_URL = "ws/karte/api/mold/spec/extmoldspec";
    //金型加工条件	
    private static final String EXTLOGIN_MOLDPROCCOND_API_URL = "ws/karte/api/mold/proccond/extmoldproccond";
    //金型加工条件仕様	
    private static final String EXTLOGIN_MOLDPROCCONDSPEC_API_URL = "ws/karte/api/mold/proccond/spec/extmoldproccondspec";
    //金型所在履歴	
    private static final String EXTLOGIN_MOLDLOCATIONHISTORY_API_URL = "ws/karte/api/mold/location/history/extmoldlocationhistory";
    //異常テーブル
    private static final String EXTLOGIN_ISSUE_API_URL = "ws/karte/api/mold/issue/extissue";
    //金型メンテナンス改造テーブル
    private static final String EXTLOGIN_MOLDMAINTENANCEREMODELING_API_URL = "ws/karte/api/mold/maintenance/remodeling/extmoldmaintenanceremodeling";
    //金型メンテナンス詳細
    private static final String EXTLOGIN_MOLDMAINTENANCEDETAIL_API_URL = "ws/karte/api/mold/maintenance/extmoldmaintenancedetail";
    //金型メンテナンス詳細ImageFileマスタ
    private static final String EXTLOGIN_MOLDMAINTENANCEDETAILMAGEFILE_API_URL = "ws/karte/api/mold/maintenance/extmoldmaintenancedetailimagefile";
    //金型改造詳細
    private static final String EXTLOGIN_MOLDREMODELINGDETAIL_API_URL = "ws/karte/api/mold/remodeling/extmoldremodelingdetail";
    //金型改造詳細ImageFileマスタ
    private static final String EXTLOGIN_MOLDREMODELINGDETAILIMAGEFILE_API_URL = "ws/karte/api/mold/remodeling/extmoldremodelingdetailimagefile";
    //金型点検结果マスタ
    private static final String EXTLOGIN_MOLDINSPECTIONRESULT_API_URL = "ws/karte/api/mold/inspection/result/extmoldinspectionresult";
    //金型改造点検结果マスタ
    private static final String EXTLOGIN_MOLDREMODELINGINSPECTIONRESULT_API_URL = "ws/karte/api/mold/remodeling/inspection/result/extmoldremodelinginspectionresult";
    //工程マスタ
    private static final String EXTLOGIN_PROCEDURE_API_URL = "ws/karte/api/procedure/extprocedure";
    //生産実績テーブル
    private static final String EXTLOGIN_PRODUCTION_API_URL = "ws/karte/api/production/extproduction";
    //生産実績明細テーブル
    private static final String EXTLOGIN_PRODUCTIONDETAIL_API_URL = "ws/karte/api/productiondetail/extproductiondetail";
    
    private static final String EXTLOGIN_COMPONENT_COMPANY_API_URL = "ws/karte/api/component/company/extdata/get";
    //FileDL
    private static final String EXTLOGIN_FILENAME_API_URL = "ws/karte/api/files/extfilename";
    private static final String EXTLOGIN_FILESDOWNLOAD_API_URL = "ws/karte/api/files/extfiledownload";
    private static final String EXTLOGIN_UPLOADFILE_API_URL = "ws/karte/api/files/extfileuploads";

    private Logger logger = Logger.getLogger(ExternalMoldBatchlet.class.getName());
    private Level info = Level.FINE;
    private Level fine = Level.FINE;
    private Level severe = Level.SEVERE;
    private Level warning = Level.WARNING;

    @Override
    public String process() {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(info, "  ---> [[{0}]] Start", methodName);

        // 処理開始時点の時刻で前回取得日時を更新する。
        Date executedDate = new Date();

        String path = kartePropertyService.getDocumentDirectory();
        String imagePath = new StringBuffer(path).append(FileUtil.SEPARATOR).append(CommonConstants.IMAGE).toString();
        String videoPath = new StringBuffer(path).append(FileUtil.SEPARATOR).append(CommonConstants.VIDEO).toString();
        String docPath = new StringBuffer(path).append(FileUtil.SEPARATOR).append(CommonConstants.DOC).toString();
        List<String> imagesList = new ArrayList<>();
        List<String> videosList = new ArrayList<>();
        List<String> docsList = new ArrayList<>();
        try {
            //  外部データ取得設定から有効フラグ=1のレコードを取得し、レコード数分、以下の処理をループ。  
            List<MstExternalDataGetSetting> externalDatas = externalDataGetSettingService.getExternalDataMoldValids();
            if (externalDatas != null && externalDatas.size() > 0) {
                for (MstExternalDataGetSetting aExternalDataGetSetting : externalDatas) {
                    String apiBaseUrl = aExternalDataGetSetting.getApiBaseUrl();
                    if (null == apiBaseUrl || apiBaseUrl.equals("")) {
                        continue;
                    }

                    if (!apiBaseUrl.endsWith("/")) {
                        apiBaseUrl = apiBaseUrl + "/";
                    }

                    // 外部データ取得する前に認証確認
                    Credential credential = new Credential();
                    credential.setUserid(aExternalDataGetSetting.getUserId());
                    credential.setPassword(FileUtil.decrypt(aExternalDataGetSetting.getEncryptedPassword()));
                    String pathUrl = apiBaseUrl + EXTLOGIN_LOGIN_API_URL;
                    FileUtil.SSL();
                    Credential result;
                    try {
                        result = FileUtil.sendPost(pathUrl, credential);
                    } catch (Exception e) {
                        // 認証エラー発生場合、スキップ                    
                        logger.log(severe, e.getMessage());
                        continue;
                    }

                    // 認証失敗　スキップ
                    if (result.isValid() == false || result.isError() == true) {
                        logger.log(warning, result.getErrorMessage());
                        continue;
                    }

                    // 前回取得日時
                    Date latestExecutedDate = aExternalDataGetSetting.getLatestExecutedDate();
                    // 抽出条件は前回取得日時マイナス1分とする。
                    if (null != latestExecutedDate) {
                        Calendar beforeTime = Calendar.getInstance();
                        beforeTime.setTime(latestExecutedDate);
                        beforeTime.add(Calendar.MINUTE, -1);
                        latestExecutedDate = beforeTime.getTime();
                    }

                    // 抽出条件作成　#前回取得日時より更新日時があたらしい
                    String urlPara = "";
                    if (null != latestExecutedDate) {
                        urlPara = "?latestExecutedDate=" + new FileUtil().getDateTimeFormatForStr(latestExecutedDate).replace(" ", "-");
                    }

                    //　認証できたら、Tokenを取得
                    String token = result.getToken();
                    // データを取得できたら、その結果を対象に変換するため、Gsonを使う
                    Gson gson = new Gson();
                    // 削除済みデータ取得APIをコールし削除されたデータのテーブル名とIDを取得
                    BasicResponse response = new BasicResponse();

                    try {
                        String iDeletedKeys = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_DELETEDKEY_API_URL + urlPara, token);
                        TblDeletedKeyVo delKeys = gson.fromJson(iDeletedKeys, new TypeToken<TblDeletedKeyVo>() {
                        }.getType());
                        // 削除済みデータのテーブル名、IDをもとに、テーブルからデータを削除
                        // 金型マスタは削除しない
                        if (null != delKeys.getTblDeletedKeys() && delKeys.getTblDeletedKeys().size() > 0) {
                            response = tblDeletedKeyService.updateExtDeletedKeysByBatch(delKeys.getTblDeletedKeys());
                            logger.log(info, "削除キーテーブルのデータを取得・更新しました。", methodName);
                        } else {
                            logger.log(info, "削除キーテーブルのデータが見つかりません。", methodName);
                        }

                    } catch (Exception e) {
                        response.setError(true);
                        logger.log(severe, "削除キーテーブルのデータを取得・更新失敗しました。", e.getMessage());
                    }

                    // 外部・会社マスタのデータを取得するためのAPIをコール。
                    if (response.isError() == false) {
                        try {
                            String iComs = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_COMPANY_API_URL + urlPara, token);
                            MstCompanyList coms = gson.fromJson(iComs, new TypeToken<MstCompanyList>() {
                            }.getType());
                            if (null != coms.getMstCompanies() && coms.getMstCompanies().size() > 0) {
                                response = mstCompanyService.updateExtMstCompaniesByBatch(coms.getMstCompanies());
                                logger.log(info, "会社マスタのデータを取得・更新しました。", methodName);
                            } else {
                                logger.log(info, "会社マスタのデータが見つかりません。", methodName);
                            }

                        } catch (Exception e) {
                            response.setError(true);
                            logger.log(severe, "会社マスタのデータを取得・更新失敗しました。", e.getMessage());
                        }
                    }

                    // 外部・所在地マスタのデータを取得するためのAPIをコール。
                    if (response.isError() == false) {
                        try {
                            String iLocationVos = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_LOCATION_API_URL + urlPara, token);
                            MstLocationVo locationVos = gson.fromJson(iLocationVos, new TypeToken<MstLocationVo>() {
                            }.getType());
                            if (null != locationVos.getMstLocationVos() && locationVos.getMstLocationVos().size() > 0) {
                                response = mstLocationService.updateExtLocationsByBatch(locationVos.getMstLocationVos());
                                logger.log(info, "所在地マスタのデータを取得・更新しました。", methodName);
                            } else {
                                logger.log(info, "所在地マスタのデータが見つかりません。", methodName);
                            }

                        } catch (Exception e) {
                            response.setError(true);
                            logger.log(severe, "所在地マスタのデータを取得・更新失敗しました。", e.getMessage());
                        }
                    }

                    // 外部・設置場所マスタのデータを取得するためのAPIをコール。
                    if (response.isError() == false) {
                        try {
                            String iSties = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_INSTALLATIONSITE_API_URL + urlPara, token);
                            MstInstallationSiteList installationSites = gson.fromJson(iSties, new TypeToken<MstInstallationSiteList>() {
                            }.getType());
                            if (null != installationSites.getMstInstallationSites() && installationSites.getMstInstallationSites().size() > 0) {
                                response = mstInstallationSiteService.updateExtInstallationSitesByBatch(installationSites.getMstInstallationSites());
                                logger.log(info, "設置場所マスタのデータを取得・更新しました。", methodName);

                            } else {
                                logger.log(info, "設置場所マスタのデータが見つかりません。", methodName);
                            }
                        } catch (Exception e) {
                            response.setError(true);
                            logger.log(severe, "設置場所マスタのデータを取得・更新失敗しました。", e.getMessage());
                        }
                    }

                    // 外部・金型属性マスタのデータを取得するためのAPIをコール。
                    if (response.isError() == false) {
                        try {
                            String iMstMoldAttributes = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MOLDATTRIBUTE_API_URL + urlPara, token);
                            MstMoldAttributeList moldAttribute = gson.fromJson(iMstMoldAttributes, new TypeToken<MstMoldAttributeList>() {
                            }.getType());
                            if (null != moldAttribute.getMstMoldAttribute() && moldAttribute.getMstMoldAttribute().size() > 0) {
                                response = mstMoldAttributeService.updateExtMoldAttributesByBatch(moldAttribute.getMstMoldAttribute());
                                logger.log(info, "金型属性マスタのデータを取得・更新しました。", methodName);
                            } else {
                                logger.log(info, "金型属性マスタのデータが見つかりません。", methodName);
                            }

                        } catch (Exception e) {
                            response.setError(true);
                            logger.log(severe, "金型属性マスタのデータを取得・更新失敗しました。", e.getMessage());
                        }
                    }

                    //外部・金型点検項目マスタのデータを取得するためのAPIをコール。
                    if (response.isError() == false) {
                        try {
                            String iMoldInspectionItems = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MOLDINSPECTIONITEM_API_URL + urlPara, token);
                            MstMoldInspectionItemList moldInspectionItems = gson.fromJson(iMoldInspectionItems, new TypeToken<MstMoldInspectionItemList>() {
                            }.getType());
                            if (null != moldInspectionItems.getMstMoldInspectionItemList() && moldInspectionItems.getMstMoldInspectionItemList().size() > 0) {
                                response = mstMoldInspectionItemService.updateExtMoldInspectionItemsByBatch(moldInspectionItems.getMstMoldInspectionItemList());
                                logger.log(info, "金型点検項目マスタのデータを取得・更新しました。", methodName);
                            } else {
                                logger.log(info, "金型点検項目マスタのデータが見つかりません。", methodName);
                            }

                        } catch (Exception e) {
                            response.setError(true);
                            logger.log(severe, "金型点検項目マスタのデータを取得・更新失敗しました。", e.getMessage());
                        }
                    }

                    // 外部・金型加工条件属性マスタのデータを取得するためのAPIをコール。
                    if (response.isError() == false) {
                        try {
                            String iMoldProcCondAttributes = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MOLDPROCCONDATTRIBUTE_API_URL + urlPara, token);
                            MstMoldProcCondAttributeList moldProcCondAttributes = gson.fromJson(iMoldProcCondAttributes, new TypeToken<MstMoldProcCondAttributeList>() {
                            }.getType());
                            if (null != moldProcCondAttributes.getMstMoldProcCondAttribute() && moldProcCondAttributes.getMstMoldProcCondAttribute().size() > 0) {
                                response = mstMoldProcCondAttributeService.updateExtMoldProcCondAttributesByBatch(moldProcCondAttributes.getMstMoldProcCondAttribute());
                                logger.log(info, "金型加工条件属性マスタのデータを取得・更新しました。", methodName);
                            } else {
                                logger.log(info, "金型加工条件属性マスタのデータが見つかりません。", methodName);
                            }

                        } catch (Exception e) {
                            response.setError(true);
                            logger.log(severe, "金型加工条件属性マスタのデータを取得・更新失敗しました。", e.getMessage());
                        }
                    }

                    // 外部・金型点検項目選択肢マスタのデータを取得するためのAPIをコール。
                    if (response.isError() == false) {
                        try {
                            String iMoldInspectionChoices = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MOLDINSPECTIONCHOICE_API_URL + urlPara, token);
                            MstMoldInspectionChoiceVo moldInspectionChoices = gson.fromJson(iMoldInspectionChoices, new TypeToken<MstMoldInspectionChoiceVo>() {
                            }.getType());

                            if (null != moldInspectionChoices.getMoldInspectionChoices() && moldInspectionChoices.getMoldInspectionChoices().size() > 0) {
                                response = mstMoldInspectionChoiceService.updateExtMoldInspectionChoicesByBatch(moldInspectionChoices.getMoldInspectionChoices());
                                logger.log(info, "金型点検項目選択肢マスタのデータを取得・更新しました。", methodName);
                            } else {
                                logger.log(info, "金型点検項目選択肢マスタのデータが見つかりません。", methodName);
                            }

                        } catch (Exception e) {
                            response.setError(true);
                            logger.log(severe, "金型点検項目選択肢マスタのデータを取得・更新失敗しました。", e.getMessage());
                        }
                    }

                    // 外部・選択肢マスタのデータを取得するためのAPIをコール。
                    if (response.isError() == false) {
                        try {
                            String iChoices = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_CHOICE_API_URL + urlPara, token);
                            MstChoiceVo choices = gson.fromJson(iChoices, new TypeToken<MstChoiceVo>() {
                            }.getType());
                            if (null != choices.getChoices() && choices.getChoices().size() > 0) {
                                mstChoiceService.deleteExtChoicesByBatch(aExternalDataGetSetting.getCompanyId());

                                response = mstChoiceService.updateExtChoicesByBatch(choices.getChoices(), aExternalDataGetSetting.getCompanyId());
                                logger.log(info, "選択肢マスタのデータを取得・更新しました。", methodName);
                            } else {
                                logger.log(info, "選択肢マスタのデータが見つかりません。", methodName);
                            }

                        } catch (Exception e) {
                            response.setError(true);
                            logger.log(severe, "選択肢マスタのデータを取得・更新失敗しました。", e.getMessage());
                        }
                    }
                    
                    
                    //会社別部品コードを取得                      
                    String mstComponentCompanyVos = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_COMPONENT_COMPANY_API_URL + urlPara, token);
                    MstComponentCompanyVoList mstComponentCompanyVoList = gson.fromJson(mstComponentCompanyVos, new TypeToken<MstComponentCompanyVoList>() {
                    }.getType());
                    
                    //工程マスタのデータを取得するためのAPIをコール。
                    if (response.isError() == false) {
                        try {
                            //外部データ取得設定テーブルの前回取得日時を処理開始時点の時刻で更新                        
                            String iProcedures = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_PROCEDURE_API_URL + urlPara, token);
                            MstProcedureVo procedures = gson.fromJson(iProcedures, new TypeToken<MstProcedureVo>() {
                            }.getType());
                            if (null != procedures.getMstProcedureVos() && procedures.getMstProcedureVos().size() > 0) {
                                
                                response = mstProcedureService.updateExtProceduresByBatch(procedures.getMstProcedureVos(), mstComponentCompanyVoList);
                                logger.log(info, "工程マスタのデータを取得・更新しました。", methodName);
                            } else {
                                logger.log(info, "工程マスタのデータが見つかりません。", methodName);
                            }

                        } catch (Exception e) {
                            response.setError(true);
                            logger.log(severe, "工程マスタのデータを取得・更新失敗しました。", e.getMessage());
                        }
                    }

                    MstMoldDetailList moldDetails;
                    MstMoldDetailList moldDetailNewDate;
                    if (response.isError() == false) {
                        List<MstMold> localMolds = externalDataGetSettingService.getMoldByExternalDataCompanyId(aExternalDataGetSetting.getCompanyId());
                        for (MstMold localMold : localMolds) {

                            // ローカルの金型ＩＤで相手サーバ一致な金型を取得
                            String iMolds = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MOLD_API_URL + "?moldId=" + localMold.getMoldId(), token);
                            moldDetails = gson.fromJson(iMolds, new TypeToken<MstMoldDetailList>() {
                            }.getType());
                            // 金型マスタが新しくなくても、関連テーブルが新しかったら取る
                            if (null != moldDetails.getMstMoldDetail() && moldDetails.getMstMoldDetail().size() > 0) {
                                urlPara = "?moldUuid=" + moldDetails.getMstMoldDetail().get(0).getMoldUuid() + "&latestExecutedDate=" + new FileUtil().getDateTimeFormatForStr(latestExecutedDate).replace(" ", "-");
                            } else {
                                continue;
                            }

                            // 金型マスタが新しかったら、全部取る                            
                            iMolds = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MOLD_API_URL + urlPara, token);
                            moldDetailNewDate = gson.fromJson(iMolds, new TypeToken<MstMoldDetailList>() {
                            }.getType());
                            if (null != moldDetailNewDate.getMstMoldDetail() && moldDetailNewDate.getMstMoldDetail().size() > 0) {
                                urlPara = "?moldUuid=" + moldDetails.getMstMoldDetail().get(0).getMoldUuid();
                            }

                            //外部・金型棚卸のデータを取得するためのAPIをコール。
                            try {
                                String iMoldInventorys = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MOLDINVENTORY_API_URL + urlPara, token);
                                TblMoldInventoryList moldInventorys = gson.fromJson(iMoldInventorys, new TypeToken<TblMoldInventoryList>() {
                                }.getType());
                                if (null != moldInventorys.getTblMoldInventorys() && moldInventorys.getTblMoldInventorys().size() > 0) {
                                    for (TblMoldInventorys aMoldInventorys : moldInventorys.getTblMoldInventorys()) {
                                        if (null != aMoldInventorys.getFileType() && !aMoldInventorys.getFileType().isEmpty()) {
                                            if (aMoldInventorys.getFileType().equals("" + CommonConstants.IMAGEFILE_TYPE_IMAGE)) {
                                                //pic
                                                imagesList.add(aMoldInventorys.getImgFilePath());
                                            } else {
                                                //video                                                
                                                videosList.add(aMoldInventorys.getImgFilePath());
                                            }
                                        }                                        
                                    }
                                    
                                    response = tblMoldInventoryService.updateExtMoldInventorysByBatch(moldInventorys.getTblMoldInventorys());
                                    logger.log(info, "金型棚卸のデータを取得・更新しました。", methodName);
                                } else {
                                    logger.log(info, "金型棚卸のデータが見つかりません。", methodName);
                                }
                            } catch (Exception e) {
                                response.setError(true);
                                logger.log(severe, "金型棚卸のデータを取得・更新失敗しました。", e.getMessage());
                            }

                            //外部・金型マスタのデータを取得するためのAPIをコール。
                            if (null != moldDetailNewDate.getMstMoldDetail() && response.isError() == false) {
                                try {
                                    if (null != moldDetailNewDate.getMstMoldDetail() && moldDetailNewDate.getMstMoldDetail().size() > 0) {
                                        for (MstMoldDetail aMoldDetail : moldDetailNewDate.getMstMoldDetail()) {
                                            if (null != aMoldDetail.getImgFilePath01() && !aMoldDetail.getImgFilePath01().trim().equals("")) {
                                                imagesList.add(aMoldDetail.getImgFilePath01());
                                            }
                                            if (null != aMoldDetail.getImgFilePath02() && !aMoldDetail.getImgFilePath02().trim().equals("")) {
                                                imagesList.add(aMoldDetail.getImgFilePath02());
                                            }
                                            if (null != aMoldDetail.getImgFilePath03() && !aMoldDetail.getImgFilePath03().trim().equals("")) {
                                                imagesList.add(aMoldDetail.getImgFilePath03());
                                            }
                                            if (null != aMoldDetail.getImgFilePath04() && !aMoldDetail.getImgFilePath04().trim().equals("")) {
                                                imagesList.add(aMoldDetail.getImgFilePath04());
                                            }
                                            if (null != aMoldDetail.getImgFilePath05() && !aMoldDetail.getImgFilePath05().trim().equals("")) {
                                                imagesList.add(aMoldDetail.getImgFilePath05());
                                            }
                                            if (null != aMoldDetail.getImgFilePath06() && !aMoldDetail.getImgFilePath06().trim().equals("")) {
                                                imagesList.add(aMoldDetail.getImgFilePath06());
                                            }
                                            if (null != aMoldDetail.getImgFilePath07() && !aMoldDetail.getImgFilePath07().trim().equals("")) {
                                                imagesList.add(aMoldDetail.getImgFilePath07());
                                            }
                                            if (null != aMoldDetail.getImgFilePath08() && !aMoldDetail.getImgFilePath08().trim().equals("")) {
                                                imagesList.add(aMoldDetail.getImgFilePath08());
                                            }
                                            if (null != aMoldDetail.getImgFilePath09() && !aMoldDetail.getImgFilePath09().trim().equals("")) {
                                                imagesList.add(aMoldDetail.getImgFilePath09());
                                            }
                                            if (null != aMoldDetail.getImgFilePath10() && !aMoldDetail.getImgFilePath10().trim().equals("")) {
                                                imagesList.add(aMoldDetail.getImgFilePath10());
                                            }

                                            //doc ReportFile
                                            if (null != aMoldDetail.getReportFilePath01() && !aMoldDetail.getReportFilePath01().trim().equals("")) {
                                                docsList.add(aMoldDetail.getReportFilePath01());
                                            }
                                            if (null != aMoldDetail.getReportFilePath02() && !aMoldDetail.getReportFilePath02().trim().equals("")) {
                                                docsList.add(aMoldDetail.getReportFilePath02());
                                            }
                                            if (null != aMoldDetail.getReportFilePath03() && !aMoldDetail.getReportFilePath03().trim().equals("")) {
                                                docsList.add(aMoldDetail.getReportFilePath03());
                                            }
                                            if (null != aMoldDetail.getReportFilePath04() && !aMoldDetail.getReportFilePath04().trim().equals("")) {
                                                docsList.add(aMoldDetail.getReportFilePath04());
                                            }
                                            if (null != aMoldDetail.getReportFilePath05() && !aMoldDetail.getReportFilePath05().trim().equals("")) {
                                                docsList.add(aMoldDetail.getReportFilePath05());
                                            }
                                            if (null != aMoldDetail.getReportFilePath06() && !aMoldDetail.getReportFilePath06().trim().equals("")) {
                                                docsList.add(aMoldDetail.getReportFilePath06());
                                            }
                                            if (null != aMoldDetail.getReportFilePath07() && !aMoldDetail.getReportFilePath07().trim().equals("")) {
                                                docsList.add(aMoldDetail.getReportFilePath07());
                                            }
                                            if (null != aMoldDetail.getReportFilePath08() && !aMoldDetail.getReportFilePath08().trim().equals("")) {
                                                docsList.add(aMoldDetail.getReportFilePath08());
                                            }
                                            if (null != aMoldDetail.getReportFilePath09() && !aMoldDetail.getReportFilePath09().trim().equals("")) {
                                                docsList.add(aMoldDetail.getReportFilePath09());
                                            }
                                            if (null != aMoldDetail.getReportFilePath10() && !aMoldDetail.getReportFilePath10().trim().equals("")) {
                                                docsList.add(aMoldDetail.getReportFilePath10());
                                            }
                                        }

                                        response = mstMoldService.updateExtMoldsByBatch(moldDetails.getMstMoldDetail());
                                        logger.log(info, "金型マスタのデータを取得・更新しました。", methodName);
                                    } else {
                                        logger.log(info, "金型マスタのデータが見つかりません。", methodName);
                                    }

                                } catch (Exception e) {
                                    response.setError(true);
                                    logger.log(severe, "金型マスタのデータを取得・更新失敗しました。", e.getMessage());
                                }
                            }

                            //外部・金型仕様履歴マスタのデータを取得するためのAPIをコール。
                            if (response.isError() == false) {
                                try {
                                    String iMoldSpecHistorys = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MOLDSPECHISTORYRESOURCE_API_URL + urlPara, token);
                                    MstMoldSpecHistoryList moldSpecHistorys = gson.fromJson(iMoldSpecHistorys, new TypeToken<MstMoldSpecHistoryList>() {
                                    }.getType());
                                    if (null != moldSpecHistorys.getMstMoldSpecHistorys() && moldSpecHistorys.getMstMoldSpecHistorys().size() > 0) {
                                        response = mstMoldSpecHistoryService.updateExtMoldSpecHistorysByBatch(moldSpecHistorys.getMstMoldSpecHistorys());
                                        logger.log(info, "金型仕様履歴マスタのデータを取得・更新しました。", methodName);
                                    } else {
                                        logger.log(info, "金型仕様履歴マスタのデータが見つかりません。", methodName);
                                    }

                                } catch (Exception e) {
                                    response.setError(true);
                                    logger.log(severe, "金型仕様履歴マスタのデータを取得・更新失敗しました。", e.getMessage());
                                }
                            }

                            //外部・金型仕様マスタのデータを取得するためのAPIをコール。
                            if (response.isError() == false) {
                                try {
                                    String iMoldSpecs = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MOLDSPEC_API_URL + urlPara, token);
                                    MstMoldSpecList moldSpecs = gson.fromJson(iMoldSpecs, new TypeToken<MstMoldSpecList>() {
                                    }.getType());
                                    if (null != moldSpecs.getMstMoldSpec() && moldSpecs.getMstMoldSpec().size() > 0) {
                                        response = mstMoldSpecService.updateExtMoldSpecsByBatch(moldSpecs.getMstMoldSpec());
                                        logger.log(info, "金型仕様マスタのデータを取得・更新しました。", methodName);
                                    } else {
                                        logger.log(info, "金型仕様マスタのデータが見つかりません。", methodName);
                                    }

                                } catch (Exception e) {
                                    response.setError(true);
                                    logger.log(severe, "金型仕様マスタのデータを取得・更新失敗しました。", e.getMessage());
                                }
                            }

                            //外部・金型部品関係のデータを取得するためのAPIをコール。
                            if (response.isError() == false) {
                                try {
                                    String iMoldComponentRelationVos = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MOLDCOMPONENTRELATION_API_URL + urlPara, token);
                                    MstMoldComponentRelationList moldComponentRelationVos = gson.fromJson(iMoldComponentRelationVos, new TypeToken<MstMoldComponentRelationList>() {
                                    }.getType());
                                    if (null != moldComponentRelationVos.getMstMoldComponentRelationVos() && moldComponentRelationVos.getMstMoldComponentRelationVos().size() > 0) {
                                        response = mstMoldComponentRelationService.updateMoldComponentRelationsByBatch(moldComponentRelationVos.getMstMoldComponentRelationVos(), mstComponentCompanyVoList);
                                        logger.log(info, "金型部品関係のデータを取得・更新しました。", methodName);
                                    } else {
                                        logger.log(info, "金型部品関係のデータのデータが見つかりません。", methodName);
                                    }

                                } catch (Exception e) {
                                    response.setError(true);
                                    logger.log(severe, "金型部品関係のデータを取得・更新失敗しました。", e.getMessage());
                                }
                            }

                            //外部・金型加工条件のデータを取得するためのAPIをコール。
                            if (response.isError() == false) {
                                try {
                                    String iProcConds = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MOLDPROCCOND_API_URL + urlPara, token);
                                    MstMoldProcCondList moldProcConds = gson.fromJson(iProcConds, new TypeToken<MstMoldProcCondList>() {
                                    }.getType());
                                    if (null != moldProcConds.getMstMoldProcConds() && moldProcConds.getMstMoldProcConds().size() > 0) {
                                        response = mstMoldProcCondService.updateExtMoldProcCondsByBatch(moldProcConds.getMstMoldProcConds());
                                        logger.log(info, "金型加工条件のデータを取得・更新しました。", methodName);
                                    } else {
                                        logger.log(info, "金型加工条件のデータが見つかりません。", methodName);
                                    }

                                } catch (Exception e) {
                                    response.setError(true);
                                    logger.log(severe, "金型加工条件のデータを取得・更新失敗しました。", e.getMessage());
                                }
                            }

                            //外部・金型加工条件仕様のデータを取得するためのAPIをコール。
                            if (response.isError() == false) {
                                try {
                                    String iProcCondSpecs = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MOLDPROCCONDSPEC_API_URL + urlPara, token);
                                    MstMoldProcCondSpecList moldProcCondSpecs = gson.fromJson(iProcCondSpecs, new TypeToken<MstMoldProcCondSpecList>() {
                                    }.getType());
                                    if (null != moldProcCondSpecs.getMstMoldProcCondSpecList() && moldProcCondSpecs.getMstMoldProcCondSpecList().size() > 0) {
                                        response = mstMoldProcCondSpecService.updateExtMoldProcCondSpecsByBatch(moldProcCondSpecs.getMstMoldProcCondSpecList());
                                        logger.log(info, "金型加工条件仕様のデータを取得・更新しました。", methodName);
                                    } else {
                                        logger.log(info, "金型加工条件仕様のデータが見つかりません。", methodName);
                                    }

                                } catch (Exception e) {
                                    response.setError(true);
                                    logger.log(severe, "金型加工条件仕様のデータを取得・更新失敗しました。", e.getMessage());
                                }
                            }

                            //外部・金型所在履歴のデータを取得するためのAPIをコール。
                            if (response.isError() == false) {
                                try {
                                    String iLocationHistorys = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MOLDLOCATIONHISTORY_API_URL + urlPara, token);
                                    TblMoldLocationHistoryList moldLocationHistorys = gson.fromJson(iLocationHistorys, new TypeToken<TblMoldLocationHistoryList>() {
                                    }.getType());
                                    if (null != moldLocationHistorys.getTblMoldLocationHistorys() && moldLocationHistorys.getTblMoldLocationHistorys().size() > 0) {
                                        response = tblMoldLocationHistoryService.updateExtMoldLocationHistorysByBatch(moldLocationHistorys.getTblMoldLocationHistorys());
                                        logger.log(info, "金型所在履歴のデータを取得・更新しました。", methodName);
                                    } else {
                                        logger.log(info, "金型所在履歴のデータが見つかりません。", methodName);
                                    }

                                } catch (Exception e) {
                                    response.setError(true);
                                    logger.log(severe, "金型所在履歴のデータを取得・更新失敗しました。", e.getMessage());
                                }
                            }

                            //外部・異常テーブルのデータを取得するためのAPIをコール。
                            if (response.isError() == false) {
                                try {
                                    String iIssueVos = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_ISSUE_API_URL + urlPara, token);
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
                                    logger.log(severe, "異常テーブルのデータを取得・更新失敗しました。", e.getMessage());
                                }
                            }

                            //外部・金型メンテナンス改造テーブルのデータを取得するためのAPIをコール。
                            if (response.isError() == false) {
                                try {
                                    String iMoldmaintenanceremodelingVos = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MOLDMAINTENANCEREMODELING_API_URL + urlPara, token);
                                    TblMoldMaintenanceRemodelingList moldMaintenanceRemodelingVos = gson.fromJson(iMoldmaintenanceremodelingVos, new TypeToken<TblMoldMaintenanceRemodelingList>() {
                                    }.getType());
                                    if (null != moldMaintenanceRemodelingVos.getTblMoldMaintenanceRemodelingVoList() && moldMaintenanceRemodelingVos.getTblMoldMaintenanceRemodelingVoList().size() > 0) {
                                        response = tblMoldMaintenanceRemodelingService.updateExtMoldMaintenanceRemodelingsByBatch(moldMaintenanceRemodelingVos.getTblMoldMaintenanceRemodelingVoList());
                                        logger.log(info, "金型メンテナンス改造テーブルのデータを取得・更新しました。", methodName);
                                    } else {
                                        logger.log(info, "金型メンテナンス改造テーブルのデータが見つかりません。", methodName);
                                    }
                                } catch (Exception e) {
                                    response.setError(true);
                                    logger.log(severe, "金型メンテナンス改造テーブルのデータを取得・更新失敗しました。", e.getMessage());
                                }
                            }

                            //外部・異常テーブルのデータを取得するためのAPIをコール Update。
                            if (response.isError() == false) {
                                try {
                                    String iIssueVos = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_ISSUE_API_URL + urlPara, token);
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
                                    logger.log(severe, "異常テーブルのデータを取得・更新失敗しました。", e.getMessage());
                                }
                            }

                            //外部・金型メンテナンス詳細のデータを取得するためのAPIをコール。
                            if (response.isError() == false) {
                                try {
                                    String iMoldMaintenanceDetails = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MOLDMAINTENANCEDETAIL_API_URL + urlPara, token);
                                    TblMoldMaintenanceDetailVo moldMaintenanceDetails = gson.fromJson(iMoldMaintenanceDetails, new TypeToken<TblMoldMaintenanceDetailVo>() {
                                    }.getType());

                                    if (null != moldMaintenanceDetails.getTblMoldMaintenanceDetails() && moldMaintenanceDetails.getTblMoldMaintenanceDetails().size() > 0) {
                                        response = tblMoldMaintenanceDetailService.updateExtMoldMaintenanceDetailsByBatch(moldMaintenanceDetails.getTblMoldMaintenanceDetails());
                                        logger.log(info, "金型メンテナンス詳細のデータを取得・更新しました。", methodName);
                                    } else {
                                        logger.log(info, "金型メンテナンス詳細のデータが見つかりません。", methodName);
                                    }

                                } catch (Exception e) {
                                    response.setError(true);
                                    logger.log(severe, "金型メンテナンス詳細のデータを取得・更新失敗しました。", e.getMessage());
                                }
                            }
                            
                            //金型メンテナンス詳細イメージファイルテーブルのデータを取得するためのAPIをコール。
                            if (response.isError() == false) {
                                try {
                                    String iMoldRemodelingDetailImageFiles = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MOLDMAINTENANCEDETAILMAGEFILE_API_URL + urlPara, token);
                                    TblMoldMaintenanceDetailVo moldMaintenanceDetails = gson.fromJson(iMoldRemodelingDetailImageFiles, new TypeToken<TblMoldMaintenanceDetailVo>() {
                                    }.getType());
                                    if (null != moldMaintenanceDetails.getMoldMaintenanceDetailImageFileVos() && moldMaintenanceDetails.getMoldMaintenanceDetailImageFileVos().size() > 0) {
                                        for (TblMoldMaintenanceDetailImageFileVo aImageFileVo : moldMaintenanceDetails.getMoldMaintenanceDetailImageFileVos()) {
                                            if (null != aImageFileVo.getFileUuid()) {
                                                if (aImageFileVo.getFileType().equals("" + CommonConstants.IMAGEFILE_TYPE_IMAGE)) {
                                                    imagesList.add(aImageFileVo.getFileUuid());
                                                } else if (aImageFileVo.getFileType().equals("" + CommonConstants.IMAGEFILE_TYPE_VIDEO)) {
                                                    docsList.add(aImageFileVo.getFileUuid());
                                                }
                                            }
                                        }

                                        response = tblMoldMaintenanceDetailService.updateExtMoldMaintenanceDetailImageFilesByBatch(moldMaintenanceDetails.getMoldMaintenanceDetailImageFileVos());
                                        logger.log(info, "金型メンテナンス詳細イメージファイルテーブルのデータを取得・更新しました。", methodName);
                                    } else {
                                        logger.log(info, "金型メンテナンス詳細イメージファイルテーブルのデータが見つかりません。", methodName);
                                    }

                                } catch (Exception e) {
                                    response.setError(true);
                                    logger.log(severe, "金型メンテナンス詳細イメージファイルテーブルのデータを取得・更新失敗しました。", e.getMessage());
                                }
                            }

                            //外部・金型改造詳細のデータを取得するためのAPIをコール。
                            if (response.isError() == false) {
                                try {
                                    String iMoldRemodelingDetails = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MOLDREMODELINGDETAIL_API_URL + urlPara, token);
                                    TblMoldRemodelingDetailVo moldRemodelingDetails = gson.fromJson(iMoldRemodelingDetails, new TypeToken<TblMoldRemodelingDetailVo>() {
                                    }.getType());

                                    if (null != moldRemodelingDetails.getTblMoldRemodelingDetails() && moldRemodelingDetails.getTblMoldRemodelingDetails().size() > 0) {
                                        response = tblMoldRemodelingDetailService.updateExtMoldRemodelingDetailsByBatch(moldRemodelingDetails.getTblMoldRemodelingDetails());
                                        logger.log(info, "金型改造詳細のデータを取得・更新しました。", methodName);
                                    } else {
                                        logger.log(info, "金型改造詳細のデータが見つかりません。", methodName);
                                    }

                                } catch (Exception e) {
                                    response.setError(true);
                                    logger.log(severe, "金型改造詳細のデータを取得・更新失敗しました。", e.getMessage());
                                }
                            }
                            
                            //金型改造詳細イメージファイルテーブルのデータを取得するためのAPIをコール。
                            if (response.isError() == false) {
                                try {
                                    String iMoldRemodelingDetailImageFiles = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MOLDREMODELINGDETAILIMAGEFILE_API_URL + urlPara, token);
                                    TblMoldRemodelingDetailVo moldRemodelingDetails = gson.fromJson(iMoldRemodelingDetailImageFiles, new TypeToken<TblMoldRemodelingDetailVo>() {
                                    }.getType());
                                    if (null != moldRemodelingDetails.getMoldRemodelingDetailImageFileVos() && moldRemodelingDetails.getMoldRemodelingDetailImageFileVos().size() > 0) {
                                        for (TblMoldRemodelingDetailImageFileVo aImageFileVo : moldRemodelingDetails.getMoldRemodelingDetailImageFileVos()) {
                                            if (null != aImageFileVo.getFileUuid()) {
                                                if (aImageFileVo.getFileType().equals("" + CommonConstants.IMAGEFILE_TYPE_IMAGE)) {
                                                    imagesList.add(aImageFileVo.getFileUuid());
                                                } else if (aImageFileVo.getFileType().equals("" + CommonConstants.IMAGEFILE_TYPE_VIDEO)) {
                                                    docsList.add(aImageFileVo.getFileUuid());
                                                }
                                            }
                                        }
                                        response = tblMoldRemodelingDetailService.updateExtMoldRemodelingDetailImageFilesByBatch(moldRemodelingDetails.getMoldRemodelingDetailImageFileVos());
                                        logger.log(info, "金型改造詳細イメージファイルテーブルのデータを取得・更新しました。", methodName);
                                    } else {
                                        logger.log(info, "金型改造詳細イメージファイルテーブルのデータが見つかりません。", methodName);
                                    }

                                } catch (Exception e) {
                                    response.setError(true);
                                    logger.log(severe, "金型改造詳細イメージファイルテーブルのデータを取得・更新失敗しました。", e.getMessage());
                                }
                            }

                            //金型点検结果のデータを取得するためのAPIをコール。
                            if (response.isError() == false) {
                                try {
                                    String iMoldInspectionResults = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MOLDINSPECTIONRESULT_API_URL + urlPara, token);
                                    TblMoldInspectionResultVo moldInspectionResults = gson.fromJson(iMoldInspectionResults, new TypeToken<TblMoldInspectionResultVo>() {
                                    }.getType());
                                    if (null != moldInspectionResults.getMoldInspectionResultVos() && moldInspectionResults.getMoldInspectionResultVos().size() > 0) {
                                        response = tblMoldInspectionResultService.updateExtMoldInspectionResultsByBatch(moldInspectionResults.getMoldInspectionResultVos());
                                        logger.log(info, "金型点検结果のデータを取得・更新しました。", methodName);
                                    } else {
                                        logger.log(info, "金型点検结果のデータが見つかりません。", methodName);
                                    }

                                } catch (Exception e) {
                                    response.setError(true);
                                    logger.log(severe, "金型点検结果のデータを取得・更新失敗しました。", e.getMessage());
                                }
                            }

                            //金型改造点検结果のデータを取得するためのAPIをコール。
                            if (response.isError() == false) {
                                try {
                                    String iMoldInspectionResults = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MOLDREMODELINGINSPECTIONRESULT_API_URL + urlPara, token);
                                    TblMoldRemodelingInspectionResultVo moldInspectionResults = gson.fromJson(iMoldInspectionResults, new TypeToken<TblMoldRemodelingInspectionResultVo>() {
                                    }.getType());
                                    if (null != moldInspectionResults.getMoldRemodelingInspectionResultVos() && moldInspectionResults.getMoldRemodelingInspectionResultVos().size() > 0) {
                                        response = tblMoldRemodelingInspectionResultService.updateExtMoldRemodelingInspectionResultsByBatch(moldInspectionResults.getMoldRemodelingInspectionResultVos());
                                        logger.log(info, "金型改造点検结果のデータを取得・更新しました。", methodName);
                                    } else {
                                        logger.log(info, "金型改造点検结果のデータが見つかりません。", methodName);
                                    }

                                } catch (Exception e) {
                                    response.setError(true);
                                    logger.log(severe, "金型改造点検结果のデータを取得・更新失敗しました。", e.getMessage());
                                }
                            }

                            //生産実績テーブル
                            if (response.isError() == false) {
                                try {
                                    String iProductions = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_PRODUCTION_API_URL + urlPara, token);
                                    TblProductionList productions = gson.fromJson(iProductions, new TypeToken<TblProductionList>() {
                                    }.getType());
                                    if (null != productions.getTblProductionVo() && productions.getTblProductionVo().size() > 0) {
                                        response = tblProductionService.updateExtProductionsByBatch(productions.getTblProductionVo());
                                        logger.log(info, "生産実績テーブルのデータを取得・更新しました。", methodName);
                                    } else {
                                        logger.log(info, "生産実績テーブルのデータが見つかりません。", methodName);
                                    }

                                } catch (Exception e) {
                                    response.setError(true);
                                    logger.log(severe, "生産実績テーブルのデータを取得・更新失敗しました。", e.getMessage());
                                }
                            }
                            //生産実績明細テーブル
                            if (response.isError() == false) {
                                try {
                                    String iProductionDetails = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_PRODUCTIONDETAIL_API_URL + urlPara, token);
                                    TblProductionDetailVo productionDetails = gson.fromJson(iProductionDetails, new TypeToken<TblProductionDetailVo>() {
                                    }.getType());
                                    if (null != productionDetails.getTblProductionDetailVos() && productionDetails.getTblProductionDetailVos().size() > 0) {
                                        response = tblProductionDetailService.updateExtProductionDetailsByBatch(productionDetails.getTblProductionDetailVos(), mstComponentCompanyVoList);
                                        logger.log(info, "生産実績明細テーブルのデータを取得・更新しました。", methodName);
                                    } else {
                                        logger.log(info, "生産実績明細テーブルのデータが見つかりません。", methodName);
                                    }

                                } catch (Exception e) {
                                    response.setError(true);
                                    logger.log(severe, "生産実績明細テーブルのデータを取得・更新失敗しました。", e.getMessage());
                                }
                            }
                        }
                        //File download
                        try {
                            logger.log(info, "ファイルダウンロード開始。", methodName);
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
                                    if (null != UploadFiles.getTblUploadFiles() && UploadFiles.getTblUploadFiles().size() > 0) {
                                        response = tblUploadFileService.updateExtIssuesByBatch(UploadFiles.getTblUploadFiles());
                                        logger.log(info, "UploadFileテーブルのデータを取得・更新しました。", methodName);
                                    } else {
                                        logger.log(info, "UploadFileテーブルのデータが見つかりません。", methodName);
                                    }
                                }
                            }

                            for (String videoUuid : videosList) {
                                fileName = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_FILENAME_API_URL + "?uuid=" + videoUuid + "&fileType=" + CommonConstants.VIDEO, token);
                                if (null != fileName && !fileName.trim().equals("")) {
                                    FileUtil.getFileGet(apiBaseUrl + EXTLOGIN_FILESDOWNLOAD_API_URL + "?uuid=" + videoUuid + "&fileType=" + CommonConstants.VIDEO, fileName, token, videoPath);
                                }
                            }
                            logger.log(info, "ファイルダウンロード終了。", methodName);
                        } catch (Exception e) {
                            response.setError(true);
                            logger.log(severe, "file download 失敗しました。", e.getMessage());
                        }

                        // 上記データ取得・更新でエラーなし場合、
                        if (!response.isError()) {
                            //外部データ取得設定テーブルの前回取得日時を処理開始時点の時刻で更新
                            externalDataGetSettingService.updateExtLatestExecutedDateByBatch(aExternalDataGetSetting, executedDate);
                            logger.log(info, "  データが取得・更新し、前回取得日時を更新しました。 ", methodName);
                        } else {
                            logger.log(info, "  データが取得・更新失敗で、前回取得日時を更新しません。 ", methodName);
                        }
                    }
                }
            }
            logger.log(info, "  <--- [[{0}]] End", methodName);
        } catch (Exception e) {
            logger.log(severe, "  < Error >", e.getMessage());
        }
        return "SUCCESS";
    }
}
