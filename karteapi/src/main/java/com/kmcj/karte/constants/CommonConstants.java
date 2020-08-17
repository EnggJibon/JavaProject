/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.constants;

import java.util.Date;

/**
 *
 * @author zds
 */
public class CommonConstants {
    
    public final static String PERSISTENCE_UNIT_NAME = "pu_karte";
    
    public final static String PERSISTENCE_UNIT_NAME_VIEWONLY = "pu_karte_viewonly";
    
    public final static String EXT_LOGIN_API_URL = "ws/karte/api/authentication/extlogin?lang=ja";
    public final static Date SYS_MAX_DATE = new Date("2400/12/31");// 2400/12/31
    public final static String SYS_MIN_TIME = " 00:00:00";
    public final static String SYS_MAX_TIME = " 23:59:59";
    /**
     * ImportORExportTableName
     */
    public final static String TBL_MST_COMPANY = "mst_company";
    public final static String TBL_MST_LOCATION = "mst_location";
    public final static String TBL_MST_INSTALLATION_SITE = "mst_installation_site";
    public final static String TBL_MST_COMPONENT = "mst_component";
    public final static String TBL_MST_COMPONENT_SPEC = "mst_component_spec";
    public final static String TBL_MST_COMPONENT_ATTRIBUTE = "mst_component_attribute";
    public final static String TBL_MST_MOLD = "mst_mold";
    public final static String TBL_MST_MOLD_SPEC = "mst_mold_spec";
    public final static String TBL_MST_MOLD_ATTRIBUTE = "mst_mold_attribute";
    public final static String TBL_MST_MOLD_PART_MAINTENANCE = "mst_mold_part_maintenance";
    public final static String TBL_TBL_MOLD_INVENTORY = "tbl_mold_inventory";
    public final static String TBL_MST_MOLD_PART = "mst_mold_part";
    public final static String TBL_MST_MOLD_PART_REL = "mst_mold_part_rel";
    public final static String TBL_MST_MOLD_PROCCOND_ATTRIBUTE = "mst_mold_proc_cond_attribute";
    public final static String TBL_MST_USER = "mst_user";
    public final static String TBL_MOLD_MAINTENANCE_REMODELING = "mold_maintenance_list";
    public final static String TBL_ISSUE = "tbl_issue";
    public final static String MST_PROCEDURE = "mst_procedure_maintenance";
    public final static String TBL_MST_MATERIAL = "mst_material_maintenance";
    public final static String TBL_MST_COMPONENT_MATERIAL = "mst_component_material";
    public final static String TBL_PRODUCTION = "tbl_production";
    public final static String TBL_PRODUCTION_PLAN = "tbl_production_plan";
    public final static String TBL_PRODUCTION_REF_COMPARE_WITH_DIRECTION = "production_ref_compare_with_direction";
    public final static String TBL_WORK = "tbl_work";
    public final static String TBL_DIRECTION = "tbl_direction";
    public final static String TBL_MST_MACHINE = "mst_machine";
    public final static String TBL_MST_MACHINE_SPEC = "mst_machine_spec";
    public final static String TBL_MST_MACHINE_ATTRIBUTE = "mst_machine_attribute";
    public final static String TBL_TBL_MACHINE_INVENTORY = "tbl_machine_inventory";
    public final static String TBL_MST_MACHINE_PROCCOND_ATTRIBUTE = "mst_machine_proc_cond_attribute";
    public final static String TBL_MACHINE_MAINTENANCE_REMODELING = "machine_maintenance_list";
    public final static String TBL_MACHINE_HISTORY = "tbl_machine_history";
    public final static String TBL_MACHINE_DAILY_REPORT = "tbl_machine_daily_report";
	public final static String MACHINE_DAILY_REPORT2_REFERENCE = "machine_daily_report2_reference";
    public final static String MST_MACHINE_FILE_DEF = "mst_log_item_setting";
    public final static String TBL_SIGMA_LOG = "tbl_sigma_log";
    public final static String MST_SIGMA_THRESHOLD_SETTING = "mst_sigma_threshold_setting";
    public final static String MST_CHOICE_CATEGORY = "mst_choice_category";
    public final static String MST_COMPONENT_INSPECTION_ITEM_DETAIL = "component_inspection_item";
    public final static String TBL_INVENTORY_MGMT_COMPANY = "tbl_inventory_mgmt_company";
    public final static String TBL_INVENTORY = "tbl_inventory_mgmt_company";
    public final static String TBL_ASSET_DISPOSAL_REQUEST = "tbl_asset_disposal_request";

    /**
     * 金型期間別生産実績照会
     */
    public final static String TBL_MOLD_PRODUCTION_PER_DAY = "tbl_mold_production_per_day"; // 日別
    public final static String TBL_MOLD_PRODUCTION_PER_WEEK = "tbl_mold_production_per_week";// 周別
    public final static String TBL_MOLD_PRODUCTION_PER_MONTH = "tbl_mold_production_per_month";// 月別
    /**
     * 設備期間別生産実績照会
     */
    public final static String TBL_MACHINE_PRODUCTION_PER_DAY = "tbl_machine_production_per_day"; // 日別
    public final static String TBL_MACHINE_PRODUCTION_PER_WEEK = "tbl_machine_production_per_week";// 周別
    public final static String TBL_MACHINE_PRODUCTION_PER_MONTH = "tbl_machine_production_per_month";// 月別
    /**
     * 設備期間別稼働率照会
     */
    public final static String TBL_MACHINE_OPERATING_RATE_PER_DAY = "tbl_machine_operating_rate_per_day"; // 日別
    public final static String TBL_MACHINE_OPERATING_RATE_PER_WEEK = "tbl_machine_operating_rate_per_week";// 周別
    public final static String TBL_MACHINE_OPERATING_RATE_PER_MONTH = "tbl_machine_operating_rate_per_month";// 月別
    
    public final static String TBL_CUSTOM_REPORT_QUERY = "tbl_custom_report_query";// カスタムレポートクエリテーブル

    public final static String TBL_MST_PRODUCT = "mst_product";

    /**
     * FunctionId
     */
    public final static String FUN_ID_MST_COMPANY = "10700";
    public final static String FUN_ID_MST_LOCATION = "10800";
    public final static String FUN_ID_INSTALLATION_SITE = "10900";
    public final static String FUN_ID_COMPONENT = "11100";
    public final static String FUN_ID_COMPONENT_CATTRIBUTE = "11101";
    public final static String FUN_ID_INVENTORY = "11001";
    public final static String FUN_ID_MOLD = "11000";
    public final static String FUN_ID_MOLD_ATTRIBUTE = "11002";
    public final static String FUN_ID_MOLD_PART = "12400";
    public final static String FUN_ID_MOLD_PROCCOND_ATTRIBUTE = "11003";
    public final static String FUN_ID_MACHINE_PROCCOND_ATTRIBUTE = "21008";
    public final static String FUN_ID_MST_USER = "10503";
    public final static String FUN_ID_MOLD_MAINTENANCE_REMODELING = "11328";
    public final static String FUN_ID_MOLD_ISSUE = "11213";
    public final static String FUN_ID_PROCEDURE = "11703";
    public final static String FUN_ID_MATERIAL = "11704";
    public final static String FUN_ID_PRODUCTION_LIST = "11710"; // "11733";
    public final static String FUN_ID_PRODUCTION_REF_COMPARE_WITH_DIRECTION = "11708";
    public final static String FUN_ID_WORK_LIST = "11625";
    public final static String FUN_PRODUCTION_PLAN = "11707";
    public final static String FUN_PRODUCTION_PLAN_COMPARE = "11709";
    public final static String FUN_MST_COMPONENT_MATERIAL = "11705";
    public final static String FUN_MST_DIRECTION_MAINTENANCE = "11706";
    public final static String FUN_ID_MACHINE_MAINTENANCE_REMODELING = "21012";
    public final static String FUN_ID_BL_MACHINE_HISTORY = "21400";
    public final static String FUN_TBL_SIGMA_LOG = "21405";
    public final static String FUN_ID_MST_SIGMA_THRESHOLD_SETTING = "21016";
    public final static String FUN_ID_MACHINE_DAILY_REPORT = "21500";
    public final static String FUN_ID_MACHINE_DAILY_REPORT2_REFERENCE = "21529";
    public final static String FUN_ID_MACHINE_FILE_DEF = "21015";
    public final static String FUN_ID_MST_CHOICE_CATEGORY = "50150";
    public final static String FUN_ID_INSPECTION_ITEM = "30200";
    public final static String FUN_ID_INVENTORY_EXECUTION = "12000";
    public final static String FUN_ID_STOCK_QUANTITY = "40000";
    public final static String FUN_ID_STOCK_HISTORY = "40001";
    public final static String FUN_ID_COMPONENT_STRUCTURE = "40002";
    public final static String FUN_ID_CUSTOM_REPORT_QUERY = "51200";
    public final static String FUN_ID_CUSTOM_REPORT_QUERY_EDIT = "51201";
    public final static String FUN_ID_MST_PRODUCT = "30190";
    public final static String FUN_ID_MATERIAL_STOCK_QUANTITY = "40004";
    public final static String FUN_ID_COMPONENT_LOT_RELATION = "40005";
    public final static String FUN_ID_MACHINE = "21006";
    public final static String FUN_ID_MACHINE_INVENTORY = "21000";
    public final static String FUN_ID_MACHINE_ATTRIBUTE = "21007";

    /**
     * 担当者登録
     */
    public final static String FUN_ID_MST_CONTACT_MAINTENANCE = "11800";

    /**
     * 資産登録
     */
    public final static String FUN_ID_MST_ASSET_MAINTENANCE = "11900";

    /**
     * 金型期間別生産実績照会
     */
    public final static String FUN_ID_TBL_MOLD_PRODUCTION_REFERENCE = "11500";
    /**
     * 設備期間別生産実績照会
     */
    public final static String FUN_ID_TBL_MACHINE_PRODUCTION_REFERENCE = "11501";
    /**
     * 設備期間別稼働率照会
     */
    public final static String FUN_ID_TBL_MACHINE_OPERATING_RATE_REFERENCE = "11502";
    /**
     * 検査結果ダウンロード
     */
    public final static String FUN_ID_TBL_COMPONENT_INSPECTION_RESULT = "30160";

    /**
     * ImagesType
     */
    public final static String LOG = "log";
    public final static String CSV = "csv";
    public final static String DOC = "doc";
    public final static String EXCEL_FILE = "excel";
    public final static String EXCEL = "xlsx";
    public final static String IMAGE = "image";
    public final static String VIDEO = "video";
    public final static String MODULE = "module";
    public final static String GUIDE = "guide";
    public final static String OPELOG = "opelog";
    public final static String REPORT = "report";
    public final static String WORK = "work";
    public final static String INVENTORY_REQUEST = "inventory_request";
    public final static String INSPECTION_REPORT = "inspection_report";

    public final static String EXT_LOG = ".log";
    public final static String EXT_JSON = ".json";
    public final static String EXT_CSV = ".csv";
    public final static String EXT_TXT = ".txt";
    public final static String EXT_EXCEL = ".xlsx";
    public final static String EXT_ZIP = ".zip";

    public final static String CONTENT_DISPOSITION = "Content-Disposition";
    public final static String CONTENT_TYPE = "Content-Type";
    public final static String CONTENT_TYPE_IMAGE_PNG = "image/png";
    public final static String CONTENT_TYPE_IMAGE_JPG = "image/jpg";
    public final static String CONTENT_TYPE_VIDEO_X_MSVIDEO = "video/x-msvideo";
    public final static String CONTENT_TYPE_VIDEO_MP4 = "video/mp4";

    /**
     * mainteOrRemodel
     */
    public static final int MAINTEORREMODEL_MAINTE = 1; // メンテナンス
    public static final int MAINTEORREMODEL_REMODEL = 2; // 改造
    /**
     * mold's mainteStatus
     */
    public static final int MAINTE_STATUS_NORMAL = 0;
    public static final int MAINTE_STATUS_MAINTE = 1; // メンテナンス
    public static final int MAINTE_STATUS_REMODELING = 2; // 改造
    /**
     * externalFlg
     */
    public static final int MINEFLAG = 0;
    public static final int EXTERNALFLG = 1;
    /**
     * mente和改造的文件类别
     */
    public final static int IMAGEFILE_TYPE_IMAGE = 1;
    public final static int IMAGEFILE_TYPE_VIDEO = 2;
    /**
     * 金型和mente的attr的type
     */
    public final static int ATTRIBUTE_TYPE_NUMBER = 1;
    public final static int ATTRIBUTE_TYPE_TEXT = 2;
    public final static int ATTRIBUTE_TYPE_DATE = 3;
    public final static int ATTRIBUTE_TYPE_STATICLINK = 4;
    public final static int ATTRIBUTE_TYPE_DYNCLNK = 5;

    // *** component inspection ***
    /** inspectionType */
    public static final int INSPECTION_TYPE_OUTGOING = 1;
    public static final int INSPECTION_TYPE_INCOMING = 2;

    /** inspection target */
    public static final String INSPECTION_TARGET_NO = "0";
    public static final String INSPECTION_TARGET_YES = "1";

    /** measurementType */
    public static final int MEASUREMENT_TYPE_MEASURE = 1; // 測定
    public static final int MEASUREMENT_TYPE_VISUAL = 2; // 目視
    
    /** mst_component_inspection_items_table_detail additionalFlg */
    public static final String ITEMS_TABLE_ADDITIONAL_FLG_YES = "0"; // 連携
    public static final String ITEMS_TABLE_ADDITIONAL_FLG_NO = "1"; // 非連携
    
    /** additionalFlg */
    public static final String ADDITIONAL_FLG_YES = "1"; // 連携
    public static final String ADDITIONAL_FLG_NO = "0"; // 非連携

    /** firstFlag */
    public static final int INSPECTION_FIRST_FLAG = 1;
    /**
     * massFlag
     */
    public static final int INSPECTION_INT_MASS_FLAG = 0;//初物
    public static final int INSPECTION_PRODUCTION_MASS_FLAG = 1;//量産
    public static final int INSPECTION_PROCESS_MASS_FLAG = 2;//工程内
    public static final int INSPECTION_FIRST_MASS_PRODUCTION_FLAG = 3;//量産第一ロット
    /**
     * massFlag
     */
    public static final Character INSPECTION_CHAR_MASS_FLAG = '1';
    
    /** inspectionResult */
    public static final int INSPECTION_RESULT= 1;
    public static final int INSPECTION_RESULT_UNDEFINED = 0;
    public static final int INSPECTION_RESULT_NG = 2;
    public static final int INSPECTION_RESULT_SPECIAL = 3;

    /** fileConfirmStatus */
    public static final String FILE_CONFIRM_STATUS_DEFAULT = "01_DEFAULT";
    public static final String INVALID_FIELD_TYPE = "INVALID_TYPE";
    
    /** functionType */
    public static final String FUNCTION_TYPE_OUTGOING = "1";
    public static final String FUNCTION_TYPE_INCOMING = "3";
    /** act */
    public static final String ACT_CHECK = "2";
    public static final String ACT_CONFIRM = "3";
    public static final String ACT_APPROVE = "4";
    public static final String ACT_DETAIL = "5";
    
    public static final String PRODUCTION_LOT_NUMBER = "NO_PRODUCTION_LOT_NUM";
    public static final String PO_NUMBER = "NO_PO_NUMBER";

    /** inspectionStatus */
    public static final int INSPECTION_STATUS_O_INSPECTING = 11; // 出荷検査中
    public static final int INSPECTION_STATUS_O_CONFIRM = 12; // 出荷検査確認待ち
    public static final int INSPECTION_STATUS_O_UNAPPROVED = 13; // 出荷検査承認待ち
    /** 出荷検査完了(20) ver1.7.03以前、数値は14でした。*/
    public static final int INSPECTION_STATUS_O_APPROVED = 20;
    public static final int INSPECTION_STATUS_O_REJECTED = 15; // 再度出荷検査
    public static final int INSPECTION_STATUS_O_MATCH = 16; // 出荷待ち
    /** @deprecated
     *  旧受入完了。フローの変更とともに、このステータスは廃止となりました。
     */
    public static final int INSPECTION_STATUS_I_ACCEPTED = 20; // 受入完了
    public static final int INSPECTION_STATUS_I_INSPECTING = 21; // 入荷検査中
    public static final int INSPECTION_STATUS_I_CONFIRM = 22; // 入荷検査確認待ち
    public static final int INSPECTION_STATUS_I_UNAPPROVED = 23; // 入荷検査承認待ち
    public static final int INSPECTION_STATUS_I_APPROVED = 24; // 入荷検査完了
    public static final int INSPECTION_STATUS_I_REJECTED = 25; // 受入否認
    public static final int INSPECTION_STATUS_I_EXEMPTED = 26; // 免検
    public static final int INSPECTION_STATUS_I_AGAIN_REJECTED = 27; // 再度入荷検査
    /** 検査ステータス　検査中止*/
    public static final int INSPECTION_STATUS_ABORTED = 99;

    /** inspection batch update result */
    public static final String INSP_BATCH_UPDATE_STATUS_ITEM_NOT_PUSH = "10"; // 10：inspection
                                                                              // items
                                                                              // not
                                                                              // yet
                                                                              // push
    public static final String INSP_BATCH_UPDATE_STATUS_ITEM_PUSHED = "11"; // 11：inspection
                                                                            // items
                                                                            // had
                                                                            // pushed
    public static final String INSP_BATCH_UPDATE_STATUS_O_RESULT_NOT_SEND = "20"; // 20：inspection
                                                                                  // outgoing
                                                                                  // result
                                                                                  // not
                                                                                  // yet
                                                                                  // send
    public static final String INSP_BATCH_UPDATE_STATUS_O_RESULT_SENT = "21"; // 21：inspection
                                                                              // outgoing
                                                                              // result
                                                                              // had
                                                                              // sent
    public static final String INSP_BATCH_UPDATE_STATUS_O_RESULT_GET_NOT_NOTIFY = "22"; // 22：inspection
                                                                                        // outgoing
                                                                                        // result
                                                                                        // had
                                                                                        // get
                                                                                        // but
                                                                                        // not
                                                                                        // not
                                                                                        // yet
                                                                                        // notify
                                                                                        // to
                                                                                        // supplier
    public static final String INSP_BATCH_UPDATE_STATUS_O_RESULT_GET_NOTIFIED = "23"; // 23：inspection
                                                                                      // outgoing
                                                                                      // result
                                                                                      // had
                                                                                      // get
                                                                                      // and
                                                                                      // notified
                                                                                      // to
                                                                                      // supplier
    public static final String INSP_BATCH_UPDATE_STATUS_I_RESULT_NOT_PUSH = "30"; // 30：inspection
                                                                                  // outgoing
                                                                                  // result
                                                                                  // not
                                                                                  // yet
                                                                                  // push
    public static final String INSP_BATCH_UPDATE_STATUS_I_RESULT_PUSHED = "31"; // 31：inspection
                                                                                // outgoing
                                                                                // result
                                                                                // had
                                                                                // pushed

    /**
     * tbl_issue.measure_status
     */
    public static final int ISSUE_MEASURE_STATUS_NOTYET = 0; // 0:未対応
    public static final int ISSUE_MEASURE_STATUS_RESOLVING = 10; // 10:対応中
    public static final int ISSUE_MEASURE_STATUS_TEMPORARILY_RESOLVED = 20; // 20:暫定対応済み
    public static final int ISSUE_MEASURE_STATUS_PERMANENTLY_RESOLVED = 30; // 30:恒久対応済み
    public static final int ISSUE_MEASURE_STATUS_NONEED = 40; // 40:対応不要
    public static final int ISSUE_MEASURE_STATUS_COMPLETED = 50; // 50:完了

    /**
     * operationFlag
     */
    public final static String OPERATION_FLAG_DELETE = "1";
    public final static String OPERATION_FLAG_DEFAULT = "2";
    public final static String OPERATION_FLAG_UPDATE = "3";
    public final static String OPERATION_FLAG_CREATE = "4";

    /**
     * 資産廃棄管理に関して、制御テーブル(tbl_control)の制御コードasset_disposal_request_registration
     */
    public final static String ASSET_DISPOSAL_REQUEST_REGISTRATION_TITLE = "ASSET_DISPOSAL_REQUEST_TITLE";
    public final static String ASSET_DISPOSAL_REQUEST_REGISTRATION_BUTTON = "ASSET_DISPOSAL_REQUEST_BUTTON";
    public final static String ASSET_DISPOSAL_REQUEST_REGISTRATION_CONDITION = "ASSET_DISPOSAL_REQUEST_CONDITION";
    public final static String ASSET_DISPOSAL_REQUEST_REGISTRATION_DETAIL_ITEM = "ASSET_DISPOSAL_REQUEST_DETAIL_ITEM";
    public final static String ASSET_DISPOSAL_REQUEST_REGISTRATION_GET_REQUEST_NO = "ASSET_DISPOSAL_REQUEST_GET_REQUEST_NO";
    // 画面ステータス変換制御
    public static final String ASSET_DISPOSAL_STATUS_CONTROL = "ASSET_DISPOSAL_STATUS_CONTROL";
    // 画面ステータス変換用の属性制御
    public static final String ASSET_DISPOSAL_PROPERTY_CONTROL = "ASSET_DISPOSAL_PROPERTY_CONTROL";
    // 画面遷移可否判断用の属性制御
    public static final String ASSET_DISPOSAL_JUMP_PROPERTY_CONTROL = "ASSET_DISPOSAL_JUMP_PROPERTY_CONTROL";
    // 画面遷移可否制御
    public static final String ASSET_DISPOSAL_JUMP_CONTROL = "ASSET_DISPOSAL_JUMP_CONTROL";
    // 申請フォーム出力タイトル項目制御
    public static final String ASSET_DISPOSAL_EXCELFORM_TITLE_CONTROL = "ASSET_DISPOSAL_EXCELFORM_TITLE_CONTROL";
    // 申請フォーム出力項目制御
    public static final String ASSET_DISPOSAL_EXCELFORM_CONTROL = "ASSET_DISPOSAL_EXCELFORM_CONTROL";
    // EXCEL出力タイトル項目制御
    public static final String ASSET_DISPOSAL_EXCEL_TITLE_CONTROL = "ASSET_DISPOSAL_EXCEL_TITLE_CONTROL";
    // EXCEL出力タイトル項目制御
    public static final String ASSET_DISPOSAL_EXCEL_ITEM_CONTROL = "ASSET_DISPOSAL_EXCEL_ITEM_CONTROL";
    // 廃棄メール送信制御
    public static final String ASSET_DISPOSAL_MAIL_SEND_CONTROL = "ASSET_DISPOSAL_MAIL_SEND_CONTROL";
    // 権限ID（12200）：資産廃棄申請登録
    public static final String ASSET_DISPOSAL_REQUEST_REGISTRATION = "12200";
    // 権限ID（12201）：資産廃棄申請受付登録
    public static final String ASSET_DISPOSAL_REQUEST_RECEPTION = "12201";
    // 権限ID（12202）：資産廃棄申請確認登録
    public static final String ASSET_DISPOSAL_REQUEST_CONFIRMATION = "12202";
    // 権限ID（12203）：資産廃棄申請AP確認登録
    public static final String ASSET_DISPOSAL_REQUEST_AP_CONFIRMATION = "12203";
    // 権限ID（12204）：資産廃棄処理登録
    public static final String ASSET_DISPOSAL_PROCESSING = "12204";

    /**
     * サプライヤー向けステータス : 廃棄資産の社外ステータス
     */
    public final static int ASSET_EXTERNAL_STATUS_UNSENT = 0; // 未送信
    public final static int ASSET_EXTERNAL_STATUS_APPLYING = 1;// 申請中
    public final static int ASSET_EXTERNAL_STATUS_RECEIVED = 2;// 受付済
    public final static int ASSET_EXTERNAL_STATUS_CONFIRMED = 3;// 確認済
    public final static int ASSET_EXTERNAL_STATUS_DISPOSAL_PROCESSED = 4;// 廃棄処理済
    public final static int ASSET_EXTERNAL_STATUS_NO_TARGET = 8;// 対象無
    public final static int ASSET_EXTERNAL_STATUS_DISMISSAL = 9;// 却下

    /**
     * 所有会社向けステータス : 廃棄資産の社内ステータス
     */
    public final static int ASSET_INTERNAL_STATUS_REQUESTED = 1;// 申請済

    /**
     * 資産所有会社ステータス : 棚卸実施ステータス
     */
    public final static int INVENTORY_STATUS_LOADING = 1; // データ抽出中
    public final static int INVENTORY_STATUS_START = 2;// 開始
    public final static int INVENTORY_STATUS_PART_REQUEST = 3;// 一部実施依頼済
    public final static int INVENTORY_STATUS_ALL_REQUEST = 4;// 実施依頼済
    public final static int INVENTORY_STATUS_PART_RECEIVE = 5;// 一部回収済
    public final static int INVENTORY_STATUS_ALL_RECEIVE = 6;// 回収済
    public final static int INVENTORY_STATUS_COMPLETE = 7;// 完了

    /**
     * サプライヤー向けステータス : 棚卸依頼ステータス
     */
    public final static int INVENTORY_REQUEST_STATUS_UNDO = 0; // 未実施
    public final static int INVENTORY_REQUEST_STATUS_DOING = 1;// 実施中
    public final static int INVENTORY_REQUEST_STATUS_DONE = 2;// 実施済
    public final static int INVENTORY_REQUEST_STATUS_ANSWER_READY = 3;// 回答送信準備済
    public final static int INVENTORY_REQUEST_STATUS_ANSWER_DONE = 4;// 回答送信済

    /** 現品有無 */
    public final static int EXISTENCE_NOT_SELECT = 0; //未選択
    public final static int EXISTENCE_YES = 1; // 有
    public final static int EXISTENCE_NO = 2; // 無

    /** 資産タイプ */
    public final static int ASSET_TYPE_DEFAULT = 0;
    public final static int ASSET_TYPE_MOLD = 1;
    public final static int ASSET_TYPE_MACHINE = 2;

    /** 資産金型・設備タイプ */
    public final static int ASSET_MOLD_MACHINE_TYPE_1 = 1;
    public final static int ASSET_MOLD_MACHINE_TYPE_2 = 2;

    /**
     * 廃棄内部ステータス
     */
    public final static int ASSET_DISPOSAL_INTERNAL_STATUS_REQUESTED = 1; // 申請済
    public final static int ASSET_DISPOSAL_INTERNAL_STATUS_RECEIVED = 2;// 受付済
    public final static int ASSET_DISPOSAL_INTERNAL_STATUS_MGMT_CONFIRMED = 3;// 資産管理部門確認済
    public final static int ASSET_DISPOSAL_INTERNAL_STATUS_AP_CONFIRMED = 4;// AP確認済
    public final static int ASSET_DISPOSAL_INTERNAL_STATUS_FINAL_CONFIRMED = 5;// 最終確認済
    public final static int ASSET_DISPOSAL_INTERNAL_STATUS_COMPLETED = 6;// 廃棄処理済
    public final static int ASSET_DISPOSAL_INTERNAL_STATUS_NO_TARGET = 8;// 最終確認済
    public final static int ASSET_DISPOSAL_INTERNAL_STATUS_REJECT = 9;// 却下
    
    /**
     * 金型、設備类型
     */
    public static final String MOLD_MACHINE_TYPE_MOLD = "1";
    public static final String MOLD_MACHINE_TYPE_MACHINE = "2";
    
    /**
     * 廃棄ステータス
     */
    public static final int DISPOSAL_STATUS_NORMAL = 0;//未廃棄
    public static final int DISPOSAL_STATUS_PROCESSED = 1;//廃棄済み
    
    /**
     * excel export cell type
     */
    public static final int EXCEL_EXPORT_CELL_TYPE_STRING = 0;//string
    public static final int EXCEL_EXPORT_CELL_TYPE_NUMBER = 1;//number
    public static final int EXCEL_EXPORT_CELL_TYPE_DATE = 2;//date
    /**
     * 自動機タイプ一覧
     */
    public final static String TBL_AUTOMATIC_MACHINE_LOG_TYPE_SMT = "SMT";
    public final static String TBL_AUTOMATIC_MACHINE_LOG_TYPE_AOI = "AOI";
    public final static String TBL_AUTOMATIC_MACHINE_LOG_TYPE_AOI_NG = "AOI_NG";
    public final static String TBL_AUTOMATIC_MACHINE_LOG_TYPE_ICT = "ICT";
    public final static String TBL_AUTOMATIC_MACHINE_LOG_TYPE_FP_ICT = "FP-ICT";
    public final static String TBL_AUTOMATIC_MACHINE_LOG_TYPE_FP_ICT_NG = "FP-ICT_NG";
    public final static String TBL_AUTOMATIC_MACHINE_LOG_TYPE_FCT = "FCT";
    /**
     * LOG TYPE
     */
    public final static String TBL_AUTOMATIC_MACHINE_LOG_TYPE_SMT_C = "C";
    public final static String TBL_AUTOMATIC_MACHINE_LOG_TYPE_SMT_N = "N";
    public final static String TBL_AUTOMATIC_MACHINE_LOG_TYPE_SMT_P = "P";
    
    public final static String TBL_AUTOMATIC_MACHINE_CHART_CHOICE_ERROR = "ERROR_SUM";

    /**
     * 自動機稼動項目名
     */
    public final static String SMT_OPERATION_TIME = "operation_time";
    public final static String SMT_OPERATION_STANDARD_TIME = "operation_standard_time";
    public final static String SMT_CIRCUIT_BOARD_WAITING_TIME_LOADER = "circuit_board_waiting_time_loader";
    public final static String SMT_CIRCUIT_BOARD_WAITING_TIME_UNLOADER = "circuit_board_waiting_time_unloader";
    public final static String SMT_MAINTENANCE_TIME = "maintenance_time";
    public final static String SMT_TROUBLE_DOWNTIME = "trouble_downtime";
    public final static String SMT_COMPONENT_CUTTING_TIME = "component_cutting_time";

    public final static String SMT_TITLE_OPERATION_ANALYSIS = "稼動分析";
    public final static String SMT_TITLE_LINE_BALANCE = "ラインバランス";
    public final static String SMT_TITLE_LINE_OPERATION_ANALYSIS = "ライン稼動分析";
    public final static String SMT_TITLE_COMPONENT_CUTTING_TIME_TAKT = "部品切れ時間/回、実装タクト";
    public final static String SMT_TITLE_COMPONENT_CUTTING_TIME = "部品切れ時間/回";
    public final static String SMT_TITLE_POWER_TAKT = "タクト（電源/台数）";
    public final static String SMT_TITLE_EQUIPMENT = "実装タクト（装置）";
    
    /**
     * 所在変更有無
     */
    public static final int CHANGE_LOCATION_NOT_SELECT = 0;//未選択（ブランク）
    public static final int CHANGE_LOCATION_YES = 1;//有
    public static final int CHANGE_LOCATION_NOT = 2;//無

    /**
     * 在庫種類 1>入庫：STORE, 2>出庫：DELIVERY, 3>入庫赤伝票：STORE-DISCARD,
     * 4>出庫赤伝票：DELIVERY-DISCARD
     */
    public static final int NONE = 0;//在庫管理更新
    public static final int STORE = 1;//入庫
    public static final int DELIVERY = 2;//出庫
    public static final int STORE_DISCARD = 3;//入庫赤伝票
    public static final int DELIVERY_DISCARD = 4;//出庫赤伝票
    
    /**
     * 出荷有無フラグ0：無し、1：有り
     */
    public static final int SHIPMENT_NO = 0;//無し
    public static final int SHIPMENT_YES = 1;//有り
    
    /**
     * 検査結果通知イベント
     */
    public static final int OUTGOING_CHECK_RESULT_OK = 1; //出荷検査OK
    public static final int CHECK_CONFIRM_DENIAL = 2; //確認者否認
    public static final int CHECK_APPROVE_DENIAL = 3; //承認者否認
    public static final int CHECK_NG_END = 4; //NG完了
    public static final int CHECK_NG_CONFIRM = 5; //NG確認
    public static final int CHECK_NG_APPROVE = 6; //NG承認
    public static final int INCOMING_CHECK_RESULT_OK = 7; //入荷検査OK
    public static final int INCOMING_FILE_DENIED = 8; //帳票否認
    public static final int INCOMING_INSPECTION_OK = 9; //受入検査結果通知(合格)
    public static final int INCOMING_INSPECTION_NG = 10; //受入検査結果通知(不合格)
    
    
    /**
     * POバッチ連携ステータス
     */
    public static final String PO_BATCH_UPDATE_STATUS_DONE = "DONE"; //連携済
    public static final String PO_BATCH_UPDATE_STATUS_UNDONE = "UNDONE"; //未連携
    
    /**ItemResultAuto*/
    public static final int INSPECTION_ITEM_RESULT_OK = 1;
    public static final int INSPECTION_ITEM_RESULT_NG = 2;

    public static final int ITEMS_TABLE_UPDATE_FLG_OPERATION = 1;
    public static final int ITEMS_TABLE_UPDATE_FLG_FILE = 2;

    public static final int ITEMS_FILE_FLG_UPLOAD = 1;
    public static final int ITEMS_FILE_FLG_REMOVE = 2;
    
    /**
     * 検査管理項目承認ステータス
     */
    public static final int ITEM_APPROVE_UNTREATED = 0;
    public static final int ITEM_APPROVE_YES = 1;
    public static final int ITEM_APPROVE_NO = 2;
    
    /**
     * 検索用の帳票ステータス
     */ 
    public static final String FILE_STATUS_DEFAULT = "01_DEFAULT";
    public static final String FILE_STATUS_WAIT = "WAIT";
    public static final String FILE_STATUS_CONFIRMED = "03_CONFIRMED";
    public static final String FILE_STATUS_DENIED = "02_DENIED";
}
