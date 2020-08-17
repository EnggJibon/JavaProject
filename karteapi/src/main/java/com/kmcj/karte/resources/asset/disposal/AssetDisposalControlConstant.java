/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.disposal;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zds
 */
public class AssetDisposalControlConstant {

    public final static Map<String, String> assetDisposalControlConstantMap = new HashMap<String, String>();

    static {

        // 画面表示のタイトル制御 Start
        // 資産破棄申請登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_TITLE12200",
                "asset_disposal_request_registration,1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_TITLE12200add",
                "asset_disposal_request_registration,1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_TITLE12200detail",
                "asset_disposal_request_registration,1");

        // 資産廃棄申請受付登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_TITLE12201", "asset_disposal_request_reception,1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_TITLE12201add",
                "asset_disposal_request_reception,1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_TITLE12201confirm",
                "asset_disposal_request_reception,1");
        // 資産廃棄申請確認登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_TITLE12202",
                "asset_disposal_request_confirmation,1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_TITLE12202confirm",
                "asset_disposal_request_confirmation,1");

        // 資産廃棄申請AP確認登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_TITLE12203",
                "asset_disposal_request_ap_confirmation,1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_TITLE12203confirm",
                "asset_disposal_request_ap_confirmation,1");

        // 資産廃棄処理登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_TITLE12204", "asset_disposal_processing,1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_TITLE12204confirm", "asset_disposal_processing,1");
        // 画面表示のタイトル制御 End

        // 画面表示のボタン制御 Start
        // 資産破棄申請登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_BUTTON12200",
                "search,1;add_record,1;record_detail,1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_BUTTON12200add", "save,1;cancel,1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_BUTTON12200detail", "cancel,1");

        // 資産廃棄申請受付登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_BUTTON12201",
                "search,1;confirm,1;add_record,1;form_export,1;form_import,1;asset_disposal_application_export,1;asset_disposal_application_import,1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_BUTTON12201add", "save,1;cancel,1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_BUTTON12201confirm", "save,1;cancel,1");
        // 資産廃棄申請確認登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_BUTTON12202",
                "search,1;confirm,1;asset_disposal_application_export,1;asset_disposal_application_import,1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_BUTTON12202confirm", "save,1;cancel,1");

        // 資産廃棄申請AP確認登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_BUTTON12203",
                "search,1;confirm,1;asset_disposal_application_export,1;asset_disposal_application_import,1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_BUTTON12203confirm", "save,1;cancel,1");

        // 資産廃棄処理登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_BUTTON12204",
                "search,1;confirm,1;asset_disposal_application_export,1;asset_disposal_application_import,1;disposal_record_export,1;disposal_record_export_again,1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_BUTTON12204confirm", "save,1;cancel,1");
        // 画面表示のボタン制御 End

        // 画面表示の検索条件制御 Start
        // 資産破棄申請登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_CONDITION12200",
                "asset_disposal_request_no,request_no,1,text;asset_disposal_request_date,request_date,1,fromto;asset_disposal_request_completion_before,disposal_completion_before,1,checkbox");

        // 資産廃棄申請受付登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_CONDITION12201",
                "asset_disposal_request_no,request_no,1,text;asset_disposal_request_date,request_date,1,fromto;disposal_request_status,status,1,select");

        // 資産廃棄申請確認登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_CONDITION12202",
                "asset_disposal_request_no,request_no,1,text;asset_disposal_request_date,request_date,1,fromto;disposal_request_status,status,1,select;asset_no,0,1,text;item_code,0,1,text");

        // 資産廃棄申請AP確認登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_CONDITION12203",
                "asset_disposal_request_no,request_no,1,text;asset_disposal_request_date,request_date,1,fromto;disposal_request_status,status,1,select;asset_no,0,1,text;item_code,0,1,text");

        // 資産廃棄処理登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_CONDITION12204",
                "asset_disposal_request_no,request_no,1,text;asset_disposal_request_date,request_date,1,fromto;disposal_request_status,status,1,select;asset_no,0,1,text;item_code,0,1,text");
        // 画面表示の検索条件制御 End

        // 画面表示の一覧（詳細画面）表示の項目制御 Start
        // 資産破棄申請登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_DETAIL_ITEM12200",
                "asset_disposal_request_no,request_no,1;asset_disposal_request_date,request_date_str,1;asset_disposal_request_to_company,to_company_name,1;mold_machine_type,mold_machine_type_text,1;item_code,0,1;asset_item_name,item_name,1;item_code2,0,1;item_name2,0,1;disposal_request_existence,existence_text,1;disposal_reason,disposal_request_reason_text,1;disposal_request_status,external_status_text,1;reject_reason,reject_reason_text,1;disposal_request_reason_other,0,1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_DETAIL_ITEM12200add",
                "asset_disposal_request_no,request_no,1,1,lable,0,0;asset_disposal_request_to_company,to_company_id,1,1,select,0,0;mold_machine_type,0,1,1,select,0,0;item_code,0,1,1,text,100,0;asset_item_name,item_name,1,1,lable,0,0;item_code2,0,1,1,text,100,0;item_name2,0,1,1,lable,0,0;disposal_request_existence,existence,1,1,select,0,0;disposal_reason,disposal_request_reason,1,1,select,0,0;disposal_request_status,external_status,1,1,lable,0,0;reject_reason,reject_reason_text,1,1,lable,0,0;disposal_request_reason_other,0,1,1,text,256,350");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_DETAIL_ITEM12200detail",
                "asset_disposal_request_no,request_no,1,0,lable,0,0;asset_disposal_request_to_company,to_company_name,1,0,lable,0,0;mold_machine_type,mold_machine_type_text,1,0,lable,0,0;item_code,0,1,0,lable,100,0;asset_item_name,item_name,1,0,lable,0,0;item_code2,0,1,0,lable,100,0;item_name2,0,1,0,lable,0,0;disposal_request_existence,existence_text,1,0,lable,0,0;disposal_reason,disposal_request_reason_text,1,0,lable,0,0;disposal_request_status,external_status_text,1,0,lable,0,0;reject_reason,reject_reason_text,1,0,lable,0,0;disposal_request_reason_other,0,1,0,lable,256,350");

        // 資産廃棄申請受付登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_DETAIL_ITEM12201",
                "asset_disposal_request_no,request_no,1;asset_disposal_request_date,request_date_str,1;disposal_request_status,internal_status_text,1;company_name,from_company_name,1;request_user_name,0,1;mold_machine_type,mold_machine_type_text,1;item_code,0,1;asset_item_name,item_name,1;item_code2,0,1;item_name2,0,1;disposal_request_existence,existence_text,1;disposal_reason,disposal_request_reason_text,1;asset_no,0,1;branch_no,0,1;mgmt_region,mgmt_region_text,1;mgmt_company_code,0,1;mgmt_company_name,0,1;installation_site_name,mgmt_location_name,1;vendor_code,0,1;acquisition_type,acquisition_type_text,1;eol_confirmation,eol_confirmation_text,1;disposal_judgment,disposal_judgment_text,1;disposal_judgment_reason,judgment_reason,1;receive_reject_reason,receive_reject_reason_text,1;receive_date,receive_date_str,1;receive_user_name,0,1;asset_class,0,1;item_ver_confirmation,item_ver_confirmation_text,1;oem_destination,oem_destination_text,1;oem_asset_no,0,1;using_section,using_section,1;acquisition_date,acquisition_date_str,1;period_book_value,0,1;cost_center,0,1;asset_mgmt_confirm,asset_mgmt_confirm_text,1;asset_mgmt_confirm_date,asset_mgmt_confirm_date_str,1;asset_mgmt_confirm_user_name,0,1;ap_disposal_judgment,ap_disposal_judgment_text,1;ap_reject_reason,ap_reject_reason_text,1;ap_confirm_date,ap_confirm_date_str,1;ap_confirm_user_name,0,1;ap_supply_remaining_month,0,1;ap_final_bulk_order,ap_final_bulk_order_text,1;final_reply,final_reply_text,1;final_reject_reason,final_reject_reason_text,1;final_reply_date,final_reply_date_str,1;final_reply_user_name,0,1;doc_request_date,doc_request_date_str,1;doc_approval_date,doc_approval_date_str,1;disposal_report_sent_date,disposal_report_sent_date_str,1;disposal_report_receipt_date,disposal_report_receipt_date_str,1;disposal_processing_completion_date,disposal_processing_completion_date_str,1;remarks,0,1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_DETAIL_ITEM12201add",
                "asset_disposal_request_date,request_date_str,1,1,lable,0,0;company_name,from_company_id,1,1,select,0,0;request_user_name,0,1,1,text,100,0;request_mail_address,0,1,1,text,100,0;mold_machine_type,0,1,1,select,0,0;item_code,0,1,1,text,100,0;asset_item_name,item_name,1,1,lable,0,0;item_code2,0,1,1,text,100,0;item_name2,0,1,1,lable,0,0;disposal_request_existence,existence,1,1,select,0,0;disposal_reason,disposal_request_reason,1,1,select,0,0;disposal_request_reason_other,0,1,1,text,256,350;asset_no,0,1,1,text,0,0;branch_no,0,1,1,text,0,0;mgmt_region,mgmt_region_text,1,0,lable,0,0;mgmt_company_code,0,1,0,lable,0,0;mgmt_company_name,0,1,0,lable,0,0;installation_site_name,mgmt_location_name,1,0,lable,0,0;vendor_code,0,1,0,lable,0,0;acquisition_type,acquisition_type_text,1,0,lable,0,0;eol_confirmation,0,1,1,select,0,0;disposal_judgment,0,1,1,select,0,0;disposal_judgment_reason,judgment_reason,1,1,text,256,350;receive_reject_reason,0,1,1,select,0,0;receive_date,receive_date_str,1,0,lable,0,0;receive_user_name,0,1,0,lable,0,0;asset_class,0,1,0,lable,0,0;item_ver_confirmation,item_ver_confirmation_text,1,0,lable,0,0;oem_destination,oem_destination_text,1,0,lable,0,0;oem_asset_no,0,1,0,lable,0,0;using_section,using_section,1,0,lable,0,0;acquisition_date,acquisition_date_str,1,0,lable,0,0;period_book_value,0,1,0,lable,0,0;cost_center,0,1,0,lable,0,0;asset_mgmt_confirm,asset_mgmt_confirm_text,1,0,lable,0,0;asset_mgmt_confirm_date,asset_mgmt_confirm_date_str,1,0,lable,0,0;asset_mgmt_confirm_user_name,0,1,0,lable,0,0;ap_disposal_judgment,ap_disposal_judgment_text,1,0,lable,0,0;ap_reject_reason,ap_reject_reason_text,1,0,lable,0,0;ap_confirm_date,ap_confirm_date_str,1,0,lable,0,0;ap_confirm_user_name,0,1,0,lable,0,0;ap_supply_remaining_month,0,1,0,lable,0,0;ap_final_bulk_order,ap_final_bulk_order_text,1,0,lable,0,0;final_reply,final_reply_text,1,0,lable,0,0;final_reject_reason,final_reject_reason_text,1,0,lable,0,0;final_reply_date,final_reply_date_str,1,0,lable,0,0;final_reply_user_name,0,1,0,lable,0,0;doc_request_date,doc_request_date_str,1,0,lable,0,0;doc_approval_date,doc_approval_date_str,1,0,lable,0,0;disposal_report_sent_date,disposal_report_sent_date_str,1,0,lable,0,0;disposal_report_receipt_date,disposal_report_receipt_date_str,1,0,lable,0,0;disposal_processing_completion_date,disposal_processing_completion_date_str,1,0,lable,0,0;remarks,0,1,0,lable,0,0");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_DETAIL_ITEM12201confirm",
                "asset_disposal_request_no,request_no,1,1,lable,0,0;asset_disposal_request_date,request_date_str,1,0,lable,0,0;disposal_request_status,internal_status_text,1,0,lable,0,0;company_name,from_company_name,1,0,lable,0,0;request_user_name,0,1,0,lable,0,0;request_mail_address,0,1,0,lable,0,0;mold_machine_type,mold_machine_type_text,1,0,lable,0,0;item_code,0,1,1,lable,100,0;asset_item_name,item_name,1,0,lable,0,0;item_code2,0,1,0,lable,100,0;item_name2,0,1,0,lable,0,0;disposal_request_existence,existence_text,1,0,lable,0,0;disposal_reason,disposal_request_reason_text,1,0,lable,0,0;disposal_request_reason_other,0,1,0,lable,256,350;asset_no,0,1,1,text,0,0;branch_no,0,1,1,text,0,0;mgmt_region,mgmt_region_text,1,0,lable,0,0;mgmt_company_code,0,1,0,lable,0,0;mgmt_company_name,0,1,0,lable,0,0;installation_site_name,mgmt_location_name,1,0,lable,0,0;vendor_code,0,1,0,lable,0,0;acquisition_type,acquisition_type_text,1,0,lable,0,0;eol_confirmation,0,1,1,select,0,0;disposal_judgment,0,1,1,select,0,0;disposal_judgment_reason,judgment_reason,1,1,text,256,350;receive_reject_reason,0,1,1,select,0,0;receive_date,receive_date_str,1,0,lable,0,0;receive_user_name,0,1,0,lable,0,0;asset_class,0,1,0,lable,0,0;item_ver_confirmation,item_ver_confirmation_text,1,0,lable,0,0;oem_destination,oem_destination_text,1,0,lable,0,0;oem_asset_no,0,1,0,lable,0,0;using_section,using_section,1,0,lable,0,0;acquisition_date,acquisition_date_str,1,0,lable,0,0;period_book_value,0,1,0,lable,0,0;cost_center,0,1,0,lable,0,0;asset_mgmt_confirm,asset_mgmt_confirm_text,1,0,lable,0,0;asset_mgmt_confirm_date,asset_mgmt_confirm_date_str,1,0,lable,0,0;asset_mgmt_confirm_user_name,0,1,0,lable,0,0;ap_disposal_judgment,ap_disposal_judgment_text,1,0,lable,0,0;ap_reject_reason,ap_reject_reason_text,1,0,lable,0,0;ap_confirm_date,ap_confirm_date_str,1,0,lable,0,0;ap_confirm_user_name,0,1,0,lable,0,0;ap_supply_remaining_month,0,1,0,lable,0,0;ap_final_bulk_order,ap_final_bulk_order_text,1,0,lable,0,0;final_reply,final_reply_text,1,0,lable,0,0;final_reject_reason,final_reject_reason_text,1,0,lable,0,0;final_reply_date,final_reply_date_str,1,0,lable,0,0;final_reply_user_name,0,1,0,lable,0,0;doc_request_date,doc_request_date_str,1,0,lable,0,0;doc_approval_date,doc_approval_date_str,1,0,lable,0,0;disposal_report_sent_date,disposal_report_sent_date_str,1,0,lable,0,0;disposal_report_receipt_date,disposal_report_receipt_date_str,1,0,lable,0,0;disposal_processing_completion_date,disposal_processing_completion_date_str,1,0,lable,0,0;remarks,0,1,0,lable,0,0");

        // 資産廃棄申請確認登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_DETAIL_ITEM12202",
                "asset_disposal_request_no,request_no,1;asset_disposal_request_date,request_date_str,1;disposal_request_status,internal_status_text,1;company_name,from_company_name,1;request_user_name,0,1;mold_machine_type,mold_machine_type_text,1;item_code,0,1;asset_item_name,item_name,1;item_code2,0,1;item_name2,0,1;disposal_request_existence,existence_text,1;disposal_reason,disposal_request_reason_text,1;asset_no,0,1;branch_no,0,1;mgmt_region,mgmt_region_text,1;mgmt_company_code,0,1;mgmt_company_name,0,1;installation_site_name,mgmt_location_name,1;vendor_code,0,1;acquisition_type,acquisition_type_text,1;eol_confirmation,eol_confirmation_text,1;disposal_judgment,disposal_judgment_text,1;disposal_judgment_reason,judgment_reason,1;receive_reject_reason,receive_reject_reason_text,1;receive_date,receive_date_str,1;receive_user_name,0,1;asset_class,0,1;item_ver_confirmation,item_ver_confirmation_text,1;oem_destination,oem_destination_text,1;oem_asset_no,0,1;using_section,using_section,1;acquisition_date,acquisition_date_str,1;period_book_value,0,1;cost_center,0,1;asset_mgmt_confirm,asset_mgmt_confirm_text,1;asset_mgmt_confirm_date,asset_mgmt_confirm_date_str,1;asset_mgmt_confirm_user_name,0,1;ap_disposal_judgment,ap_disposal_judgment_text,1;ap_reject_reason,ap_reject_reason_text,1;ap_confirm_date,ap_confirm_date_str,1;ap_confirm_user_name,0,1;ap_supply_remaining_month,0,1;ap_final_bulk_order,ap_final_bulk_order_text,1;final_reply,final_reply_text,1;final_reject_reason,final_reject_reason_text,1;final_reply_date,final_reply_date_str,1;final_reply_user_name,0,1;doc_request_date,doc_request_date_str,1;doc_approval_date,doc_approval_date_str,1;disposal_report_sent_date,disposal_report_sent_date_str,1;disposal_report_receipt_date,disposal_report_receipt_date_str,1;disposal_processing_completion_date,disposal_processing_completion_date_str,1;remarks,0,1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_DETAIL_ITEM12202confirm",
                "asset_disposal_request_no,request_no,1,1,lable,0,0;asset_disposal_request_date,request_date_str,1,0,lable,0,0;disposal_request_status,internal_status_text,1,0,lable,0,0;company_name,from_company_name,1,0,lable,0,0;request_user_name,0,1,0,lable,0,0;request_mail_address,0,1,0,lable,0,0;mold_machine_type,mold_machine_type_text,1,0,lable,0,0;item_code,0,1,0,lable,100,0;asset_item_name,item_name,1,0,lable,0,0;item_code2,0,1,0,lable,100,0;item_name2,0,1,0,lable,0,0;disposal_request_existence,existence_text,1,0,lable,0,0;disposal_reason,disposal_request_reason_text,1,0,lable,0,0;disposal_request_reason_other,0,1,0,lable,256,350;asset_no,0,1,0,lable,0,0;branch_no,0,1,0,lable,0,0;mgmt_region,mgmt_region_text,1,0,lable,0,0;mgmt_company_code,0,1,0,lable,0,0;mgmt_company_name,0,1,0,lable,0,0;installation_site_name,mgmt_location_name,1,0,lable,0,0;vendor_code,0,1,0,lable,0,0;acquisition_type,acquisition_type_text,1,0,lable,0,0;eol_confirmation,eol_confirmation_text,1,0,lable,0,0;disposal_judgment,disposal_judgment_text,1,0,lable,0,0;disposal_judgment_reason,judgment_reason,1,0,lable,256,350;receive_reject_reason,receive_reject_reason_text,1,0,lable,0,0;receive_date,receive_date_str,1,0,lable,0,0;receive_user_name,0,1,0,lable,0,0;asset_class,0,1,0,lable,0,0;item_ver_confirmation,0,1,1,checkbox,0,0;oem_destination,0,1,1,select,0,0;oem_asset_no,0,1,1,text,45,0;using_section,using_section,1,0,lable,0,0;acquisition_date,acquisition_date_str,1,0,lable,0,0;period_book_value,0,1,0,lable,0,0;cost_center,0,1,0,lable,0,0;asset_mgmt_confirm,0,1,1,checkbox,0,0;asset_mgmt_confirm_date,asset_mgmt_confirm_date_str,1,0,lable,0,0;asset_mgmt_confirm_user_name,0,1,0,lable,0,0;ap_disposal_judgment,ap_disposal_judgment_text,1,0,lable,0,0;ap_reject_reason,ap_reject_reason_text,1,0,lable,0,0;ap_confirm_date,ap_confirm_date_str,1,0,lable,0,0;ap_confirm_user_name,0,1,0,lable,0,0;ap_supply_remaining_month,0,1,0,lable,0,0;ap_final_bulk_order,ap_final_bulk_order_text,1,0,lable,0,0;final_reply,final_reply_text,1,0,lable,0,0;final_reject_reason,final_reject_reason_text,1,0,lable,0,0;final_reply_date,final_reply_date_str,1,0,lable,0,0;final_reply_user_name,0,1,0,lable,0,0;doc_request_date,doc_request_date_str,1,0,lable,0,0;doc_approval_date,doc_approval_date_str,1,0,lable,0,0;disposal_report_sent_date,disposal_report_sent_date_str,1,0,lable,0,0;disposal_report_receipt_date,disposal_report_receipt_date_str,1,0,lable,0,0;disposal_processing_completion_date,disposal_processing_completion_date_str,1,0,lable,0,0;remarks,0,1,0,lable,0,0");

        // 資産廃棄申請AP確認登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_DETAIL_ITEM12203",
                "asset_disposal_request_no,request_no,1;asset_disposal_request_date,request_date_str,1;disposal_request_status,internal_status_text,1;company_name,from_company_name,1;request_user_name,0,1;mold_machine_type,mold_machine_type_text,1;item_code,0,1;asset_item_name,item_name,1;item_code2,0,1;item_name2,0,1;disposal_request_existence,existence_text,1;disposal_reason,disposal_request_reason_text,1;asset_no,0,1;branch_no,0,1;mgmt_region,mgmt_region_text,1;mgmt_company_code,0,1;mgmt_company_name,0,1;installation_site_name,mgmt_location_name,1;vendor_code,0,1;acquisition_type,acquisition_type_text,1;eol_confirmation,eol_confirmation_text,1;disposal_judgment,disposal_judgment_text,1;disposal_judgment_reason,judgment_reason,1;receive_reject_reason,receive_reject_reason_text,1;receive_date,receive_date_str,1;receive_user_name,0,1;asset_class,0,1;item_ver_confirmation,item_ver_confirmation_text,1;oem_destination,oem_destination_text,1;oem_asset_no,0,1;using_section,using_section,1;acquisition_date,acquisition_date_str,1;period_book_value,0,1;cost_center,0,1;asset_mgmt_confirm,asset_mgmt_confirm_text,1;asset_mgmt_confirm_date,asset_mgmt_confirm_date_str,1;asset_mgmt_confirm_user_name,0,1;ap_disposal_judgment,ap_disposal_judgment_text,1;ap_reject_reason,ap_reject_reason_text,1;ap_confirm_date,ap_confirm_date_str,1;ap_confirm_user_name,0,1;ap_supply_remaining_month,0,1;ap_final_bulk_order,ap_final_bulk_order_text,1;final_reply,final_reply_text,1;final_reject_reason,final_reject_reason_text,1;final_reply_date,final_reply_date_str,1;final_reply_user_name,0,1;doc_request_date,doc_request_date_str,1;doc_approval_date,doc_approval_date_str,1;disposal_report_sent_date,disposal_report_sent_date_str,1;disposal_report_receipt_date,disposal_report_receipt_date_str,1;disposal_processing_completion_date,disposal_processing_completion_date_str,1;remarks,0,1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_DETAIL_ITEM12203confirm",
                "asset_disposal_request_no,request_no,1,1,lable,0,0;asset_disposal_request_date,request_date_str,1,0,lable,0,0;disposal_request_status,internal_status_text,1,0,lable,0,0;company_name,from_company_name,1,0,lable,0,0;request_user_name,0,1,0,lable,0,0;request_mail_address,0,1,0,lable,0,0;mold_machine_type,mold_machine_type_text,1,0,lable,0,0;item_code,0,1,0,lable,100,0;asset_item_name,item_name,1,0,lable,0,0;item_code2,0,1,0,lable,100,0;item_name2,0,1,0,lable,0,0;disposal_request_existence,existence_text,1,0,lable,0,0;disposal_reason,disposal_request_reason_text,1,0,lable,0,0;disposal_request_reason_other,0,1,0,lable,256,350;asset_no,0,1,0,lable,0,0;branch_no,0,1,0,lable,0,0;mgmt_region,mgmt_region_text,1,0,lable,0,0;mgmt_company_code,0,1,0,lable,0,0;mgmt_company_name,0,1,0,lable,0,0;installation_site_name,mgmt_location_name,1,0,lable,0,0;vendor_code,0,1,0,lable,0,0;acquisition_type,acquisition_type_text,1,0,lable,0,0;eol_confirmation,eol_confirmation_text,1,0,lable,0,0;disposal_judgment,disposal_judgment_text,1,0,lable,0,0;disposal_judgment_reason,judgment_reason,1,0,lable,256,350;receive_reject_reason,receive_reject_reason_text,1,0,lable,0,0;receive_date,receive_date_str,1,0,lable,0,0;receive_user_name,0,1,0,lable,0,0;asset_class,0,1,0,lable,0,0;item_ver_confirmation,item_ver_confirmation_text,1,0,lable,0,0;oem_destination,oem_destination_text,1,0,lable,0,0;oem_asset_no,0,1,0,lable,45,0;using_section,using_section,1,0,lable,0,0;acquisition_date,acquisition_date_str,1,0,lable,0,0;period_book_value,0,1,0,lable,0,0;cost_center,0,1,0,lable,0,0;asset_mgmt_confirm,asset_mgmt_confirm_text,1,0,lable,0,0;asset_mgmt_confirm_date,asset_mgmt_confirm_date_str,1,0,lable,0,0;asset_mgmt_confirm_user_name,0,1,0,lable,0,0;ap_disposal_judgment,0,1,1,select,0,0;ap_reject_reason,0,1,1,select,0,0;ap_confirm_date,ap_confirm_date_str,1,0,lable,0,0;ap_confirm_user_name,0,1,0,lable,0,0;ap_supply_remaining_month,0,1,1,text,9,0;ap_final_bulk_order,0,1,1,select,0,0;final_reply,final_reply_text,1,0,lable,0,0;final_reject_reason,final_reject_reason_text,1,0,lable,0,0;final_reply_date,final_reply_date_str,1,0,lable,0,0;final_reply_user_name,0,1,0,lable,0,0;doc_request_date,doc_request_date_str,1,0,lable,0,0;doc_approval_date,doc_approval_date_str,1,0,lable,0,0;disposal_report_sent_date,disposal_report_sent_date_str,1,0,lable,0,0;disposal_report_receipt_date,disposal_report_receipt_date_str,1,0,lable,0,0;disposal_processing_completion_date,disposal_processing_completion_date_str,1,0,lable,0,0;remarks,0,1,0,lable,0,0");

        // 資産廃棄処理登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_DETAIL_ITEM12204",
                "asset_disposal_request_no,request_no,1;asset_disposal_request_date,request_date_str,1;disposal_request_status,internal_status_text,1;company_name,from_company_name,1;request_user_name,0,1;mold_machine_type,mold_machine_type_text,1;item_code,0,1;asset_item_name,item_name,1;item_code2,0,1;item_name2,0,1;disposal_request_existence,existence_text,1;disposal_reason,disposal_request_reason_text,1;asset_no,0,1;branch_no,0,1;mgmt_region,mgmt_region_text,1;mgmt_company_code,0,1;mgmt_company_name,0,1;installation_site_name,mgmt_location_name,1;vendor_code,0,1;acquisition_type,acquisition_type_text,1;eol_confirmation,eol_confirmation_text,1;disposal_judgment,disposal_judgment_text,1;disposal_judgment_reason,judgment_reason,1;receive_reject_reason,receive_reject_reason_text,1;receive_date,receive_date_str,1;receive_user_name,0,1;asset_class,0,1;item_ver_confirmation,item_ver_confirmation_text,1;oem_destination,oem_destination_text,1;oem_asset_no,0,1;using_section,using_section,1;acquisition_date,acquisition_date_str,1;period_book_value,0,1;cost_center,0,1;asset_mgmt_confirm,asset_mgmt_confirm_text,1;asset_mgmt_confirm_date,asset_mgmt_confirm_date_str,1;asset_mgmt_confirm_user_name,0,1;ap_disposal_judgment,ap_disposal_judgment_text,1;ap_reject_reason,ap_reject_reason_text,1;ap_confirm_date,ap_confirm_date_str,1;ap_confirm_user_name,0,1;ap_supply_remaining_month,0,1;ap_final_bulk_order,ap_final_bulk_order_text,1;final_reply,final_reply_text,1;final_reject_reason,final_reject_reason_text,1;final_reply_date,final_reply_date_str,1;final_reply_user_name,0,1;doc_request_date,doc_request_date_str,1;doc_approval_date,doc_approval_date_str,1;disposal_report_sent_date,disposal_report_sent_date_str,1;disposal_report_receipt_date,disposal_report_receipt_date_str,1;disposal_processing_completion_date,disposal_processing_completion_date_str,1;remarks,0,1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_DETAIL_ITEM12204confirm",
                "asset_disposal_request_no,request_no,1,1,lable,0,0;asset_disposal_request_date,request_date_str,1,0,lable,0,0;disposal_request_status,internal_status_text,1,0,lable,0,0;company_name,from_company_name,1,0,lable,0,0;request_user_name,0,1,0,lable,0,0;request_mail_address,0,1,0,lable,0,0;mold_machine_type,mold_machine_type_text,1,0,lable,0,0;item_code,0,1,0,lable,100,0;asset_item_name,item_name,1,0,lable,0,0;item_code2,0,1,0,lable,100,0;item_name2,0,1,0,lable,0,0;disposal_request_existence,existence_text,1,0,lable,0,0;disposal_reason,disposal_request_reason_text,1,0,lable,0,0;disposal_request_reason_other,0,1,0,lable,256,350;asset_no,0,1,0,lable,0,0;branch_no,0,1,0,lable,0,0;mgmt_region,mgmt_region_text,1,0,lable,0,0;mgmt_company_code,0,1,0,lable,0,0;mgmt_company_name,0,1,0,lable,0,0;installation_site_name,mgmt_location_name,1,0,lable,0,0;vendor_code,0,1,0,lable,0,0;acquisition_type,acquisition_type_text,1,0,lable,0,0;eol_confirmation,eol_confirmation_text,1,0,lable,0,0;disposal_judgment,disposal_judgment_text,1,0,lable,0,0;disposal_judgment_reason,judgment_reason,1,0,lable,256,350;receive_reject_reason,receive_reject_reason_text,1,0,lable,0,0;receive_date,receive_date_str,1,0,lable,0,0;receive_user_name,0,1,0,lable,0,0;asset_class,0,1,0,lable,0,0;item_ver_confirmation,item_ver_confirmation_text,1,0,lable,0,0;oem_destination,oem_destination_text,1,0,lable,0,0;oem_asset_no,0,1,0,lable,45,0;using_section,using_section,1,0,lable,0,0;acquisition_date,acquisition_date_str,1,0,lable,0,0;period_book_value,0,1,0,lable,0,0;cost_center,0,1,0,lable,0,0;asset_mgmt_confirm,asset_mgmt_confirm_text,1,0,lable,0,0;asset_mgmt_confirm_date,asset_mgmt_confirm_date_str,1,0,lable,0,0;asset_mgmt_confirm_user_name,0,1,0,lable,0,0;ap_disposal_judgment,ap_disposal_judgment_text,1,0,lable,0,0;ap_reject_reason,ap_reject_reason_text,1,0,lable,0,0;ap_confirm_date,ap_confirm_date_str,1,0,lable,0,0;ap_confirm_user_name,0,1,0,lable,0,0;ap_supply_remaining_month,0,1,0,lable,0,0;ap_final_bulk_order,ap_final_bulk_order_text,1,0,lable,0,0;final_reply,0,1,1,select,0,0;final_reject_reason,0,1,1,select,0,0;final_reply_date,final_reply_date_str,1,1,lable,0,0;final_reply_user_name,0,1,0,lable,0,0;doc_request_date,doc_request_date_str,1,1,date,0,0;doc_approval_date,doc_approval_date_str,1,1,date,0,0;disposal_report_sent_date,disposal_report_sent_date_str,1,1,date,0,0;disposal_report_receipt_date,disposal_report_receipt_date_str,1,1,date,0,0;disposal_processing_completion_date,disposal_processing_completion_date_str,1,1,date,0,0;remarks,0,1,1,textarea,256,0");
        // 画面表示の一覧（詳細画面）表示の項目制御 End

        // 画面の採番可否制御 Start
        // 資産破棄申請登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_GET_REQUEST_NO12200add", "false,0");

        // 資産廃棄申請受付登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_GET_REQUEST_NO12201add",
                "true,AD_REQUEST_NUMBER_INTERNAL");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_GET_REQUEST_NO12201confirm",
                "true,AD_REQUEST_NUMBER_INTERNAL");

        // 資産廃棄申請確認登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_GET_REQUEST_NO12202confirm", "false,0");

        // 資産廃棄申請AP確認登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_GET_REQUEST_NO12203confirm", "false,0");

        // 資産廃棄処理登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_REQUEST_GET_REQUEST_NO12204confirm", "false,0");
        // 画面の採番可否制御 End

        // 画面のステータス変換用の属性制御 Start
        // 資産破棄申請登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_PROPERTY_CONTROL12200add", "externalStatus");

        // 資産廃棄申請受付登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_PROPERTY_CONTROL12201add", "disposalJudgment");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_PROPERTY_CONTROL12201confirm", "disposalJudgment");

        // 資産廃棄申請確認登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_PROPERTY_CONTROL12202confirm", "assetMgmtConfirm");

        // 資産廃棄申請AP確認登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_PROPERTY_CONTROL12203confirm", "apDisposalJudgment");

        // 資産廃棄処理登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_PROPERTY_CONTROL12204confirm",
                "finalReply,disposalProcessingCompletionDateStr");
        // 画面のステータス変換用の属性制御 End

        // 画面ステータス変換制御 Start
        // 資産破棄申請登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_STATUS_CONTROL12200add0", "0,0");

        // 資産廃棄申請受付登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_STATUS_CONTROL12201add0", "1,1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_STATUS_CONTROL12201add1", "2,2");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_STATUS_CONTROL12201add2", "9,9");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_STATUS_CONTROL12201add3", "8,8");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_STATUS_CONTROL12201confirm1", "2,2");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_STATUS_CONTROL12201confirm2", "9,9");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_STATUS_CONTROL12201confirm3", "8,8");

        // 資産廃棄申請確認登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_STATUS_CONTROL12202confirm0", "2,2");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_STATUS_CONTROL12202confirm1", "3,2");

        // 資産廃棄申請AP確認登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_STATUS_CONTROL12203confirm0", "3,2");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_STATUS_CONTROL12203confirm1", "4,2");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_STATUS_CONTROL12203confirm2", "9,9");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_STATUS_CONTROL12203confirm3", "4,2");

        // 資産廃棄処理登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_STATUS_CONTROL12204confirm0", "4,2");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_STATUS_CONTROL12204confirm1", "5,3");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_STATUS_CONTROL12204confirm2", "9,9");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_STATUS_CONTROL12204confirm11", "6,4");
        // 画面ステータス変換制御 End

        // 画面遷移可否判断用の属性制御 Start
        // 資産廃棄申請受付登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_JUMP_PROPERTY_CONTROL12201confirm",
                "internalStatus,disposalJudgment");

        // 資産廃棄申請確認登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_JUMP_PROPERTY_CONTROL12202confirm",
                "internalStatus,disposalJudgment");

        // 資産廃棄申請AP確認登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_JUMP_PROPERTY_CONTROL12203confirm",
                "internalStatus,apDisposalJudgment");

        // 資産廃棄処理登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_JUMP_PROPERTY_CONTROL12204confirm",
                "internalStatus,finalReply");
        // 画面遷移可否判断用の属性制御 End

        // 画面遷移可否制御 Start
        // 資産廃棄申請受付登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_JUMP_CONTROL12201confirm", "10;21;83;92");

        // 資産廃棄申請確認登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_JUMP_CONTROL12202confirm", "21;31");

        // 資産廃棄申請AP確認登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_JUMP_CONTROL12203confirm", "30;41;43;92");

        // 資産廃棄処理登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_JUMP_CONTROL12204confirm", "40;51;61;92");
        // 画面遷移可否制御 End

        // 申請フォーム出力タイトル項目制御 Start
        // 資産廃棄申請受付登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_EXCELFORM_TITLE_CONTROL12201",
                "tbl_asset_disposal_request;tbl_asset_disposal_request,company_code,request_user_name,request_mail_address,mold_machine_type,item_code,item_code2,disposal_request_existence,disposal_reason,disposal_request_reason_other;com.kmcj.karte.resources.asset.disposal.excelvo.TblAssetDisposalRequestExcelFormOutPutVo");
        // 申請フォーム出力タイトル項目制御 End

        // 申請フォーム出力項目制御 Start
        // 資産廃棄申請受付登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_EXCELFORM_CONTROL12201",
                "fromCompanyId,requestUserName,requestMailAddress,moldMachineType,itemCode,itemCode2,existenceText,disposalRequestReason,disposalRequestReasonOther");
        // 申請フォーム出力項目制御 End

        // EXCEL出力タイトル項目制御 Start
        // 資産廃棄申請受付登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_EXCEL_TITLE_CONTROLexcel_export_title",
                "tbl_asset_disposal_request;tbl_asset_disposal_request,asset_disposal_request_no,asset_disposal_request_date,disposal_request_status,company_name,request_user_name,mold_machine_type,item_code,asset_item_name,item_code2,item_name2,disposal_request_existence,disposal_reason,asset_no,branch_no,mgmt_region,mgmt_company_code,mgmt_company_name,installation_site_name,vendor_code,acquisition_type,eol_confirmation,disposal_judgment,disposal_judgment_reason,receive_reject_reason,receive_date,receive_user_name,asset_class,item_ver_confirmation,oem_destination,oem_asset_no,using_section,acquisition_date,period_book_value,cost_center,asset_mgmt_confirm,asset_mgmt_confirm_date,asset_mgmt_confirm_user_name,ap_disposal_judgment,ap_reject_reason,ap_confirm_date,ap_confirm_user_name,ap_supply_remaining_month,ap_final_bulk_order,final_reply,final_reject_reason,final_reply_date,final_reply_user_name,doc_request_date,doc_approval_date,disposal_report_sent_date,disposal_report_receipt_date,disposal_processing_completion_date,remarks;com.kmcj.karte.resources.asset.disposal.excelvo.TblAssetDisposalRequestExcelOutPutVo");
        // EXCEL出力タイトル項目制御 End

        // EXCEL出力項目制御 Start
        // 資産廃棄申請受付登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_EXCEL_ITEM_CONTROLexcel_export_title_item",
                "requestNo,requestDate,internalStatusText,fromCompanyName,requestUserName,moldMachineTypeText,itemCode,itemName,itemCode2,itemName2,existenceText,disposalRequestReasonText,assetNo,branchNo,mgmtRegion,mgmtCompanyCode,mgmtCompanyName,mgmtLocationName,vendorCode,acquisitionTypeText,eolConfirmationText,disposalJudgmentText,judgmentReason,receiveRejectReasonText,receiveDateStr,receiveUserName,assetClass,itemVerConfirmationText,oemDestinationText,oemAssetNo,usingSection,acquisitionDateStr,periodBookValue,costCenter,assetMgmtConfirmText,assetMgmtConfirmDateStr,assetMgmtConfirmUserName,apDisposalJudgmentText,apRejectReasonText,apConfirmDateStr,apConfirmUserName,apSupplyRemainingMonth,apFinalBulkOrderText,finalReplyText,finalRejectReasonText,finalReplyDateStr,finalReplyUserName,docRequestDateStr,docApprovalDateStr,disposalReportSentDateStr,disposalReportReceiptDateStr,disposalProcessingCompletionDateStr,remarks");
        // EXCEL出力項目制御 End

        // 廃棄メール送信制御 Start
        // 資産廃棄申請受付登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_MAIL_SEND_CONTROL12201form", "1");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_MAIL_SEND_CONTROL12201excel", "2");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_MAIL_SEND_CONTROL12201add", "2");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_MAIL_SEND_CONTROL12201confirm", "2");

        // 資産廃棄申請確認登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_MAIL_SEND_CONTROL12202excel", "3");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_MAIL_SEND_CONTROL12202confirm", "3");

        // 資産廃棄申請AP確認登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_MAIL_SEND_CONTROL12203excel", "4");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_MAIL_SEND_CONTROL12203confirm", "4");

        // 資産廃棄処理登録画面
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_MAIL_SEND_CONTROL12204excel", "5");
        assetDisposalControlConstantMap.put("ASSET_DISPOSAL_MAIL_SEND_CONTROL12204confirm", "5");
        // 廃棄メール送信制御 End

        // 廃棄管理一覧ソート Start
        assetDisposalControlConstantMap.put("requestNo", "tblAssetDisposalRequest.requestNo");
        assetDisposalControlConstantMap.put("requestDateStr", "tblAssetDisposalRequest.requestDate");
        assetDisposalControlConstantMap.put("toCompanyName", "tc.companyName");
        assetDisposalControlConstantMap.put("moldMachineTypeText", "tblAssetDisposalRequest.moldMachineType");
        assetDisposalControlConstantMap.put("itemCode", "tblAssetDisposalRequest.itemCode");
        assetDisposalControlConstantMap.put("itemName", "tblAssetDisposalRequest.itemName");
        assetDisposalControlConstantMap.put("itemCode2", "tblAssetDisposalRequest.itemCode2");
        assetDisposalControlConstantMap.put("itemName2", "tblAssetDisposalRequest.itemName2");
        assetDisposalControlConstantMap.put("existenceText", "tblAssetDisposalRequest.existence");
        assetDisposalControlConstantMap.put("disposalRequestReasonText",
                "tblAssetDisposalRequest.disposalRequestReason");
        assetDisposalControlConstantMap.put("externalStatusText", "tblAssetDisposalRequest.externalStatus");
        assetDisposalControlConstantMap.put("rejectReasonText", "tblAssetDisposalRequest.rejectReasonText");
        assetDisposalControlConstantMap.put("disposalRequestReasonOther",
                "tblAssetDisposalRequest.disposalRequestReasonOther");
        assetDisposalControlConstantMap.put("internalStatusText", "tblAssetDisposalRequest.internalStatus");
        assetDisposalControlConstantMap.put("fromCompanyName", "fc.companyName");
        assetDisposalControlConstantMap.put("requestUserName", "tblAssetDisposalRequest.requestUserName");
        assetDisposalControlConstantMap.put("assetNo", "tblAssetDisposalRequest.assetNo");
        assetDisposalControlConstantMap.put("branchNo", "tblAssetDisposalRequest.branchNo");
        assetDisposalControlConstantMap.put("mgmtRegionText", "m.mgmtRegion");
        assetDisposalControlConstantMap.put("mgmtCompanyCode", "m.mgmtCompanyCode");
        assetDisposalControlConstantMap.put("mgmtCompanyName", "m.mstMgmtCompany.mgmtCompanyName");
        assetDisposalControlConstantMap.put("mgmtLocationName", "m.mstMgmtLocation.mgmtLocationName");
        assetDisposalControlConstantMap.put("vendorCode", "m.vendorCode");
        assetDisposalControlConstantMap.put("acquisitionTypeText", "m.acquisitionType");
        assetDisposalControlConstantMap.put("eolConfirmationText", "tblAssetDisposalRequest.eolConfirmation");
        assetDisposalControlConstantMap.put("disposalJudgmentText", "tblAssetDisposalRequest.disposalJudgment");
        assetDisposalControlConstantMap.put("judgmentReason", "tblAssetDisposalRequest.judgmentReason");
        assetDisposalControlConstantMap.put("receiveRejectReasonText",
                "tblAssetDisposalRequest.receiveRejectReason");
        assetDisposalControlConstantMap.put("receiveDateStr", "tblAssetDisposalRequest.receiveDate");
        assetDisposalControlConstantMap.put("receiveUserName", "rm.userName");
        assetDisposalControlConstantMap.put("assetClass", "m.assetClass");
        assetDisposalControlConstantMap.put("itemVerConfirmationText",
                "tblAssetDisposalRequest.itemVerConfirmation");
        assetDisposalControlConstantMap.put("oemDestinationText", "tblAssetDisposalRequest.oemDestination");
        assetDisposalControlConstantMap.put("oemAssetNo", "tblAssetDisposalRequest.oemAssetNo");
        assetDisposalControlConstantMap.put("usingSection", "m.usingSection");
        assetDisposalControlConstantMap.put("acquisitionDateStr", "m.acquisitionDate");
        assetDisposalControlConstantMap.put("periodBookValue", "m.periodBookValue");
        assetDisposalControlConstantMap.put("costCenter", "m.costCenter");
        assetDisposalControlConstantMap.put("assetMgmtConfirmText", "tblAssetDisposalRequest.assetMgmtConfirm");
        assetDisposalControlConstantMap.put("assetMgmtConfirmDateStr",
                "tblAssetDisposalRequest.assetMgmtConfirmDate");
        assetDisposalControlConstantMap.put("assetMgmtConfirmUserName", "am.userName");
        assetDisposalControlConstantMap.put("apDisposalJudgmentText", "tblAssetDisposalRequest.apDisposalJudgment");
        assetDisposalControlConstantMap.put("apRejectReasonText", "tblAssetDisposalRequest.apRejectReason");
        assetDisposalControlConstantMap.put("apConfirmDateStr", "tblAssetDisposalRequest.apConfirmDate");
        assetDisposalControlConstantMap.put("apConfirmUserName", "apm.userName");
        assetDisposalControlConstantMap.put("apSupplyRemainingMonth",
                "tblAssetDisposalRequest.apSupplyRemainingMonth");
        assetDisposalControlConstantMap.put("apFinalBulkOrderText", "tblAssetDisposalRequest.apFinalBulkOrder");
        assetDisposalControlConstantMap.put("finalReplyText", "tblAssetDisposalRequest.finalReply");
        assetDisposalControlConstantMap.put("finalRejectReasonText", "tblAssetDisposalRequest.finalRejectReason");
        assetDisposalControlConstantMap.put("finalReplyDateStr", "tblAssetDisposalRequest.finalReplyDate");
        assetDisposalControlConstantMap.put("finalReplyUserName", "fm.userName");
        assetDisposalControlConstantMap.put("docRequestDateStr", "tblAssetDisposalRequest.docRequestDate");
        assetDisposalControlConstantMap.put("docApprovalDateStr", "tblAssetDisposalRequest.docApprovalDate");
        assetDisposalControlConstantMap.put("disposalReportSentDateStr",
                "tblAssetDisposalRequest.disposalReportSentDate");
        assetDisposalControlConstantMap.put("disposalReportReceiptDateStr",
                "tblAssetDisposalRequest.disposalReportReceiptDate");
        assetDisposalControlConstantMap.put("disposalProcessingCompletionDateStr",
                "tblAssetDisposalRequest.disposalProcessingCompletionDate");
        assetDisposalControlConstantMap.put("remarks", "tblAssetDisposalRequest.remarks");
        // 廃棄管理一覧ソート End

    }
}
