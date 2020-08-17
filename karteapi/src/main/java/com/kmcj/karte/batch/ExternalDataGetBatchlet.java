package com.kmcj.karte.batch;

/**
 *
 * @author lyd
 */
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.batch.externalmachine.ExternalMachineService;
import com.kmcj.karte.batch.externalmold.ExternalMoldService;
import com.kmcj.karte.batch.externalmold.delete.TblDeletedKeyService;
import com.kmcj.karte.batch.externalmold.delete.TblDeletedKeyVo;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.Credential;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.choice.MstChoiceVo;
import com.kmcj.karte.resources.company.MstCompanyList;
import com.kmcj.karte.resources.company.MstCompanyService;
import com.kmcj.karte.resources.component.company.MstComponentCompanyVoList;
import com.kmcj.karte.resources.external.MstExternalDataGetSetting;
import com.kmcj.karte.resources.external.MstExternalDataGetSettingService;
import com.kmcj.karte.resources.installationsite.MstInstallationSiteList;
import com.kmcj.karte.resources.installationsite.MstInstallationSiteService;
import com.kmcj.karte.resources.location.MstLocationService;
import com.kmcj.karte.resources.location.MstLocationVo;
import com.kmcj.karte.resources.machine.attribute.MstMachineAttributeList;
import com.kmcj.karte.resources.machine.attribute.MstMachineAttributeService;
import com.kmcj.karte.resources.machine.inspection.choice.MstMachineInspectionChoiceService;
import com.kmcj.karte.resources.machine.inspection.choice.MstMachineInspectionChoiceVo;
import com.kmcj.karte.resources.machine.inspection.item.MstMachineInspectionItemList;
import com.kmcj.karte.resources.machine.inspection.item.MstMachineInspectionItemService;
import com.kmcj.karte.resources.machine.proccond.attribute.MstMachineProcCondAttributeList;
import com.kmcj.karte.resources.machine.proccond.attribute.MstMachineProcCondAttributeService;
import com.kmcj.karte.resources.mold.attribute.MstMoldAttributeList;
import com.kmcj.karte.resources.mold.attribute.MstMoldAttributeService;
import com.kmcj.karte.resources.mold.inspection.choice.MstMoldInspectionChoiceService;
import com.kmcj.karte.resources.mold.inspection.choice.MstMoldInspectionChoiceVo;
import com.kmcj.karte.resources.mold.inspection.item.MstMoldInspectionItemList;
import com.kmcj.karte.resources.mold.inspection.item.MstMoldInspectionItemService;
import com.kmcj.karte.resources.mold.proccond.attribute.MstMoldProcCondAttributeList;
import com.kmcj.karte.resources.mold.proccond.attribute.MstMoldProcCondAttributeService;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import com.kmcj.karte.resources.procedure.MstProcedureVo;
import com.kmcj.karte.util.FileUtil;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.AbstractBatchlet;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * このバッチが外部金型と設備を取得するバッチである
 *
 * @author zds
 */
@Named
@Dependent
public class ExternalDataGetBatchlet extends AbstractBatchlet {
    // ジョブ名称を取得するためにJobContextをインジェクション
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
    public MstMachineAttributeService mstMachineAttributeService;
    @Inject
    public MstMachineInspectionItemService mstMachineInspectionItemService;
    @Inject
    private MstMachineProcCondAttributeService mstMachineProcCondAttributeService;
    @Inject
    private MstMachineInspectionChoiceService mstMachineInspectionChoiceService;
    @Inject
    private MstChoiceService mstChoiceService;
    @Inject
    private TblDeletedKeyService tblDeletedKeyService;
    @Inject
    private MstProcedureService mstProcedureService;
    @Inject
    private ExternalMoldService externalMoldService;
    @Inject
    private ExternalMachineService externalMachineService;

    /**
     * API path
     */
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
    //設備属性マスタ
    private static final String EXTLOGIN_MACHINEATTRIBUTE_API_URL = "ws/karte/api/machine/attribute/extmachineattribute";
    //設備点検項目マスタ
    private static final String EXTLOGIN_MACHINEINSPECTIONITEM_API_URL = "ws/karte/api/machine/inspection/item/extmachineinspectionitem";
    //設備加工条件属性マスタ	
    private static final String EXTLOGIN_MACHINEPROCCONDATTRIBUTE_API_URL = "ws/karte/api/machine/proccond/attribute/extmachineproccondattribute";
    //設備点検項目選択肢マスタ	外部テーブル
    private static final String EXTLOGIN_MACHINEINSPECTIONCHOICE_API_URL = "ws/karte/api/machine/inspection/choice/extmachineinspectionchoice";
    //選択肢マスタ	外部テーブル	
    private static final String EXTLOGIN_CHOICE_API_URL = "ws/karte/api/choice/extchoice";
    //工程マスタ
    private static final String EXTLOGIN_PROCEDURE_API_URL = "ws/karte/api/procedure/extprocedure";
    // 会社別部品コード
    private static final String EXTLOGIN_COMPONENT_COMPANY_API_URL = "ws/karte/api/component/company/extdata/get";

    /**
     * ログ
     */
    private Logger logger = Logger.getLogger(ExternalDataGetBatchlet.class.getName());
    private Level info = Level.FINE;
    private Level severe = Level.SEVERE;
    private Level warning = Level.WARNING;

    @Override
    public String process() {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.INFO, "ExternalDataGetBatchlet Start.");

        // 処理開始時点の時刻で前回取得日時を更新する。
        Date executedDate = new Date();

        String path = kartePropertyService.getDocumentDirectory();

        try {
            //  外部データ取得設定から有効フラグ=1のレコードを取得し、レコード数分、以下の処理をループ。  
            List<MstExternalDataGetSetting> externalDatas = externalDataGetSettingService.getExternalDataAllValids();
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
                        logger.log(severe, "認証エラー", e);
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
                        logger.log(severe, "削除キーテーブルのデータを取得・更新失敗しました。", e);
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
                            logger.log(severe, "会社マスタのデータを取得・更新失敗しました。", e);
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
                            logger.log(severe, "所在地マスタのデータを取得・更新失敗しました。", e);
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
                            logger.log(severe, "設置場所マスタのデータを取得・更新失敗しました。", e);
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
                            logger.log(severe, "金型属性マスタのデータを取得・更新失敗しました。", e);
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
                            logger.log(severe, "金型点検項目マスタのデータを取得・更新失敗しました。", e);
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
                            logger.log(severe, "金型加工条件属性マスタのデータを取得・更新失敗しました。", e);
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
                            logger.log(severe, "金型点検項目選択肢マスタのデータを取得・更新失敗しました。", e);
                        }
                    }

                    // 外部・設備属性マスタのデータを取得するためのAPIをコール。
                    if (response.isError() == false) {
                        try {
                            String iMstMachineAttributes = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINEATTRIBUTE_API_URL + urlPara, token);
                            MstMachineAttributeList machineAttribute = gson.fromJson(iMstMachineAttributes, new TypeToken<MstMachineAttributeList>() {
                            }.getType());
                            if (null != machineAttribute.getMstMachineAttributes() && machineAttribute.getMstMachineAttributes().size() > 0) {
                                response = mstMachineAttributeService.updateExtMachineAttributesByBatch(machineAttribute.getMstMachineAttributes());
                                logger.log(info, "設備属性マスタのデータを取得・更新しました。", methodName);
                            } else {
                                logger.log(info, "設備属性マスタのデータが見つかりません。", methodName);
                            }

                        } catch (Exception e) {
                            response.setError(true);
                            logger.log(severe, "設備属性マスタのデータを取得・更新失敗しました。", e);
                        }
                    }

                    //外部・設備点検項目マスタのデータを取得するためのAPIをコール。
                    if (response.isError() == false) {
                        try {
                            String iMachineInspectionItems = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINEINSPECTIONITEM_API_URL + urlPara, token);
                            MstMachineInspectionItemList machineInspectionItems = gson.fromJson(iMachineInspectionItems, new TypeToken<MstMachineInspectionItemList>() {
                            }.getType());
                            if (null != machineInspectionItems.getMstMachineInspectionItems() && machineInspectionItems.getMstMachineInspectionItems().size() > 0) {
                                response = mstMachineInspectionItemService.updateExtMachineInspectionItemsByBatch(machineInspectionItems.getMstMachineInspectionItems());
                                logger.log(info, "設備点検項目マスタのデータを取得・更新しました。", methodName);
                            } else {
                                logger.log(info, "設備点検項目マスタのデータが見つかりません。", methodName);
                            }

                        } catch (Exception e) {
                            response.setError(true);
                            logger.log(severe, "設備点検項目マスタのデータを取得・更新失敗しました。", e);
                        }
                    }

                    // 外部・設備加工条件属性マスタのデータを取得するためのAPIをコール。
                    if (response.isError() == false) {
                        try {
                            String iMachineProcCondAttributes = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINEPROCCONDATTRIBUTE_API_URL + urlPara, token);
                            MstMachineProcCondAttributeList machineProcCondAttributes = gson.fromJson(iMachineProcCondAttributes, new TypeToken<MstMachineProcCondAttributeList>() {
                            }.getType());
                            if (null != machineProcCondAttributes.getMstMachineProcCondAttributes() && machineProcCondAttributes.getMstMachineProcCondAttributes().size() > 0) {
                                response = mstMachineProcCondAttributeService.updateExtMachineProcCondAttributesByBatch(machineProcCondAttributes.getMstMachineProcCondAttributes());
                                logger.log(info, "設備加工条件属性マスタのデータを取得・更新しました。", methodName);
                            } else {
                                logger.log(info, "設備加工条件属性マスタのデータが見つかりません。", methodName);
                            }

                        } catch (Exception e) {
                            response.setError(true);
                            logger.log(severe, "設備加工条件属性マスタのデータを取得・更新失敗しました。", e);
                        }
                    }

                    // 外部・設備点検項目選択肢マスタのデータを取得するためのAPIをコール。
                    if (response.isError() == false) {
                        try {
                            String iMachineInspectionChoices = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_MACHINEINSPECTIONCHOICE_API_URL + urlPara, token);
                            MstMachineInspectionChoiceVo machineInspectionChoices = gson.fromJson(iMachineInspectionChoices, new TypeToken<MstMachineInspectionChoiceVo>() {
                            }.getType());

                            if (null != machineInspectionChoices.getMachineInspectionChoices() && machineInspectionChoices.getMachineInspectionChoices().size() > 0) {
                                response = mstMachineInspectionChoiceService.updateExtMachineInspectionChoicesByBatch(machineInspectionChoices.getMachineInspectionChoices());
                                logger.log(info, "設備点検項目選択肢マスタのデータを取得・更新しました。", methodName);
                            } else {
                                logger.log(info, "設備点検項目選択肢マスタのデータが見つかりません。", methodName);
                            }

                        } catch (Exception e) {
                            response.setError(true);
                            logger.log(severe, "設備点検項目選択肢マスタのデータを取得・更新失敗しました。", e);
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
                            logger.log(severe, "選択肢マスタのデータを取得・更新失敗しました。", e);
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
                            logger.log(severe, "工程マスタのデータを取得・更新失敗しました。", e);
                        }
                    }

                    // 金型service
                    if (response.isError() == false) {
                        response = externalMoldService.getExtMold(token, methodName, apiBaseUrl, urlPara, latestExecutedDate, path, aExternalDataGetSetting.getCompanyId(), mstComponentCompanyVoList);
                    }

                    // 設備service
                    if (response.isError() == false) {
                        response = externalMachineService.getExtMachine(token, methodName, apiBaseUrl, urlPara, latestExecutedDate, path, aExternalDataGetSetting.getCompanyId(), mstComponentCompanyVoList);
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
            logger.log(Level.INFO, "ExternalDataGetBatchlet");
        } catch (Exception e) {
            logger.log(severe, "  < Error >", e);
        }
        return "SUCCESS";
    }
}
