/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.request;

import com.kmcj.karte.BasicResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.BeanCopyUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;

/**
 *
 * @author admin
 */
@Dependent
public class TblInventoryRequestService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    private static final String INVENTORY_REQUEST_STATUS_UNDO = "inventory_request_status_no_implementation";
    private static final String INVENTORY_REQUEST_STATUS_DOING = "inventory_request_status_implementing";
    private static final String INVENTORY_REQUEST_STATUS_DONE = "inventory_request_status_implemented";
    private static final String INVENTORY_REQUEST_STATUS_ANSWER_READY = "inventory_request_status_ready_send";
    private static final String INVENTORY_REQUEST_STATUS_ANSWER_DONE = "inventory_request_status_answer_sended";

    private static final String[] REQUEST_STATUS_ARRY = {INVENTORY_REQUEST_STATUS_UNDO, INVENTORY_REQUEST_STATUS_DOING,
        INVENTORY_REQUEST_STATUS_DONE, INVENTORY_REQUEST_STATUS_ANSWER_READY, INVENTORY_REQUEST_STATUS_ANSWER_DONE};

    /**
     * 資産棚卸実依頼情報取得
     *
     * @param answerFlg
     * @param loginUser
     * @return
     */
    TblInventoryRequestVoList getTblInventoryRequestList(int answerFlg, LoginUser loginUser) {

        TblInventoryRequestVoList response = new TblInventoryRequestVoList();

        List<TblInventoryRequestVo> tblInventoryRequestVos = new ArrayList();

        StringBuilder sql = new StringBuilder(
                "SELECT t FROM TblInventoryRequest t JOIN FETCH t.mstCompany mstCompany WHERE 1 = 1");

        // ステータスは回答送信済み以外の棚卸情報を検索する
        if (1 == answerFlg) {

            // 棚卸依頼ステータス＜＞4（回答送信済み）
            sql.append(" AND t.status <> 4");
        }
        // 依頼日の降順
        sql.append(" ORDER BY t.requestDate DESC ");
        
        Query query = entityManager.createQuery(sql.toString());

        List<TblInventoryRequest> tblInventoryRequestList = new ArrayList();

        tblInventoryRequestList = (List<TblInventoryRequest>) query.getResultList();

        List<String> dictKeyList = Arrays.asList(REQUEST_STATUS_ARRY);
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, loginUser.getLangId(),
                dictKeyList);

        // 検索結果を設定する
        for (TblInventoryRequest tblInventoryRequest : tblInventoryRequestList) {

            TblInventoryRequestVo tblInventoryRequestVo = new TblInventoryRequestVo();

            BeanCopyUtil.copyFields(tblInventoryRequest, tblInventoryRequestVo);

            tblInventoryRequestVo.setId(tblInventoryRequest.getUuid());

            tblInventoryRequestVo.setInventoryName(tblInventoryRequest.getName());

            if (null != tblInventoryRequest.getMstCompany()) {

                tblInventoryRequestVo.setInventoryCompanyName(tblInventoryRequest.getMstCompany().getCompanyName());

            } else {

                tblInventoryRequestVo.setInventoryCompanyName("");
            }

            if (null != tblInventoryRequestVo.getDueDate()) {
                tblInventoryRequestVo.setDueDateStr(DateFormat.dateToStr(tblInventoryRequestVo.getDueDate(), DateFormat.DATE_FORMAT));
            } else {
                tblInventoryRequestVo.setDueDateStr("");
            }

            if (null != tblInventoryRequestVo.getRequestDate()) {
                tblInventoryRequestVo.setRequestDateStr(DateFormat.dateToStr(tblInventoryRequestVo.getRequestDate(), DateFormat.DATE_FORMAT));
            } else {
                tblInventoryRequestVo.setRequestDateStr("");
            }

            if (null != tblInventoryRequestVo.getSendResponseDate()) {
                tblInventoryRequestVo.setSendResponseDateStr(DateFormat.dateToStr(tblInventoryRequestVo.getSendResponseDate(), DateFormat.DATE_FORMAT));
            } else {
                tblInventoryRequestVo.setSendResponseDateStr("");
            }

            switch (tblInventoryRequestVo.getStatus()) {
                case CommonConstants.INVENTORY_REQUEST_STATUS_UNDO:
                    tblInventoryRequestVo.setStatusText(headerMap.get(INVENTORY_REQUEST_STATUS_UNDO));
                    break;
                case CommonConstants.INVENTORY_REQUEST_STATUS_DOING:
                    tblInventoryRequestVo.setStatusText(headerMap.get(INVENTORY_REQUEST_STATUS_DOING));
                    break;
                case CommonConstants.INVENTORY_REQUEST_STATUS_DONE:
                    tblInventoryRequestVo.setStatusText(headerMap.get(INVENTORY_REQUEST_STATUS_DONE));
                    break;
                case CommonConstants.INVENTORY_REQUEST_STATUS_ANSWER_READY:
                    tblInventoryRequestVo.setStatusText(headerMap.get(INVENTORY_REQUEST_STATUS_ANSWER_READY));
                    break;
                case CommonConstants.INVENTORY_REQUEST_STATUS_ANSWER_DONE:
                    tblInventoryRequestVo.setStatusText(headerMap.get(INVENTORY_REQUEST_STATUS_ANSWER_DONE));
                    break;
                default:
                    tblInventoryRequestVo.setStatusText("");
                    break;
            }

            tblInventoryRequestVos.add(tblInventoryRequestVo);
        }

        response.setTblInventoryRequestVos(tblInventoryRequestVos);

        return response;

    }

    /**
     * 資産棚卸実依頼情報取得
     *
     * @param tblInventoryRequest
     * @param langId
     * @return
     */
    @Transactional
    public BasicResponse updTblInventoryRequestStatus(TblInventoryRequest tblInventoryRequest, String langId) {
        BasicResponse response = new BasicResponse();
        // 変更前のレコードを検索する
        Query query = entityManager.createNamedQuery("TblInventoryRequest.findByUuid");

        query.setParameter("uuid", tblInventoryRequest.getUuid());

        TblInventoryRequest tempTblInventoryRequest;

        try {

            tempTblInventoryRequest = (TblInventoryRequest) query.getSingleResult();
            if (tempTblInventoryRequest.getStatus() != CommonConstants.INVENTORY_REQUEST_STATUS_DONE) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_not_editable_status"));
                return response;
            }

        } catch (NoResultException e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "mst_error_record_not_found"));
            return response;
        }

        tempTblInventoryRequest.setStatus(tblInventoryRequest.getStatus());
//        tempTblInventoryRequest.setSendResponseDate(new Date());
        tempTblInventoryRequest.setUpdateUserUuid(tblInventoryRequest.getUpdateUserUuid());
        tempTblInventoryRequest.setUpdateDate(new Date());
        try {
            entityManager.merge(tempTblInventoryRequest);
        } catch (Exception e) {
            // ステータス更新失敗
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_data_deleted"));
            return response;
        }
        return response;
    }

}
