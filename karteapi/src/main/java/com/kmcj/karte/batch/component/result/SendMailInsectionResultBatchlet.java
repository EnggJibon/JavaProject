/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.component.result;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.component.inspection.result.TblComponentInsectionSendMailService;
import com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionResult;
import com.kmcj.karte.resources.component.inspection.result.model.TblComponentInspectionSendMail;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.resources.user.MstUserService;
import com.kmcj.karte.util.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.BatchStatus;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

/**
 * 通知batch
 *
 * @author Apeng
 */
@Named
@Dependent
public class SendMailInsectionResultBatchlet extends AbstractBatchlet {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private TblComponentInsectionSendMailService tblComponentInsectionSendMailService;

    @Inject
    private KartePropertyService kartePropertyService;
    
    @Inject
    private MstUserService mstUserService;

    private final String noticeDate = DateFormat.dateToStr(new Date(), DateFormat.DATE_FORMAT);
    //部品検査結果取得
    private final static String QUERY_TBL_COMPONENT_INSECTION_RESULT
            = " SELECT t FROM TblComponentInspectionResult t JOIN FETCH t.mstComponent ";

    @Override
    public String process() {
        StringBuilder sql = new StringBuilder();
        Date dueDate = DateFormat.strToDate(noticeDate);
        Date dueEndDate = DateFormat.strToDatetime(noticeDate + " 23:59:59");
        TblComponentInspectionSendMail sendMail = new TblComponentInspectionSendMail();
        sql.append(QUERY_TBL_COMPONENT_INSECTION_RESULT);
        sql.append(" WHERE t.outgoingInspectionResult = :outgoingInspectionResult");
        sql.append(" AND t.outgoingInspectionDate between :outgoingInspectionDate and :endTime");

        Query queryOutgoingCheckEnd = entityManager.createQuery(sql.toString(), TblComponentInspectionResult.class)
            .setParameter("outgoingInspectionResult", CommonConstants.INSPECTION_RESULT)
            .setParameter("outgoingInspectionDate", dueDate)
            .setParameter("endTime", dueEndDate);

        List<TblComponentInspectionResult> listOutgoingCheckEnd = queryOutgoingCheckEnd.getResultList();
        // KM-10000 検査系メール通知の所属指定を修正 20181207 start
        List<Integer> outgoingInspectiongDepartmentList = new ArrayList<>();
        // KM-10000 検査系メール通知の所属指定を修正 20181207 end

        if (listOutgoingCheckEnd != null && listOutgoingCheckEnd.size() > 0) {
            //出荷检查完了部品
            List<String> outgoingCheckEndComponet = new ArrayList();
            int outgoingCheckEndQuantity = 0;
            for (TblComponentInspectionResult result : listOutgoingCheckEnd) {
                outgoingCheckEndComponet.add(result.getMstComponent().getComponentCode());
                sendMail.setOutgoingCheckEndComponet(outgoingCheckEndComponet);
                outgoingCheckEndQuantity++;
                // KM-10000 検査系メール通知の所属指定を修正 20181207 start
                getNoticeDepartList(result.getOutgoingInspectionPersonUuid(), outgoingInspectiongDepartmentList);
                // KM-10000 検査系メール通知の所属指定を修正 20181207 end
            }
            sendMail.setOutgoingCheckEndQuantity(outgoingCheckEndQuantity);
        }
        
        List<TblComponentInspectionResult> listOutgoingConcession = entityManager.createQuery(sql.toString(), TblComponentInspectionResult.class)
            .setParameter("outgoingInspectionResult", CommonConstants.INSPECTION_RESULT_SPECIAL)
            .setParameter("outgoingInspectionDate", dueDate)
            .setParameter("endTime", dueEndDate).getResultList();
        
        List<String> outgoingConcessionComp = listOutgoingConcession.stream().map(result->{
            getNoticeDepartList(result.getOutgoingInspectionPersonUuid(), outgoingInspectiongDepartmentList);
            return result.getMstComponent().getComponentCode();
        }).collect(Collectors.toList());
        sendMail.setOutgoingConcessionComp(outgoingConcessionComp);

        sql = new StringBuilder();
        sql.append(QUERY_TBL_COMPONENT_INSECTION_RESULT);
        sql.append(" WHERE t.outgoingInspectionResult = :outgoingInspectionResult");
        sql.append(" AND t.inspectionStatus <> :inspectionStatus1");
        sql.append(" AND t.inspectionStatus <> :inspectionStatus2");
        sql.append(" AND t.outgoingConfirmDate between :outgoingConfirmDate and :endTime");

        Query queryOutgingCofirm = entityManager.createQuery(sql.toString(), TblComponentInspectionResult.class)
            .setParameter("outgoingInspectionResult", CommonConstants.INSPECTION_RESULT)
            .setParameter("inspectionStatus1", CommonConstants.INSPECTION_STATUS_O_REJECTED)
            .setParameter("inspectionStatus2", CommonConstants.INSPECTION_STATUS_O_CONFIRM)
            .setParameter("outgoingConfirmDate", dueDate)
            .setParameter("endTime", dueEndDate);

        List<TblComponentInspectionResult> listOutgingConfirm = queryOutgingCofirm.getResultList();

        if (listOutgingConfirm != null && listOutgingConfirm.size() > 0) {
            //出荷検査確認完了部品
            List<String> outgoingCheckConfirmEndComponet = new ArrayList();
            int outgoingCheckCofirmEndQuantity = 0;
            for (TblComponentInspectionResult result : listOutgingConfirm) {
                outgoingCheckConfirmEndComponet.add(result.getMstComponent().getComponentCode());
                sendMail.setOutgoingCheckConfirmEndComponet(outgoingCheckConfirmEndComponet);
                // KM-10000 検査系メール通知の所属指定を修正 20181207 start
                getNoticeDepartList(result.getOutgoingInspectionPersonUuid(), outgoingInspectiongDepartmentList);
                // KM-10000 検査系メール通知の所属指定を修正 20181207 end
                outgoingCheckCofirmEndQuantity++;
            }
            sendMail.setOutgoingCheckCofirmEndQuantity(outgoingCheckCofirmEndQuantity);
        }
        
        //出荷検査承認完了部品
        sql = new StringBuilder();
        sql.append(QUERY_TBL_COMPONENT_INSECTION_RESULT);
        sql.append(" WHERE t.outgoingInspectionResult = :outgoingInspectionResult");
        sql.append(" AND t.inspectionStatus <> :inspectionStatus1");
        sql.append(" AND t.inspectionStatus <> :inspectionStatus2");
        sql.append(" AND t.inspectionStatus <> :inspectionStatus3");
        sql.append(" AND t.outgoingInspectionApproveDate between :outgoingInspectionApproveDate and :endTime");

        Query queryOutgoingApprove = entityManager.createQuery(sql.toString(), TblComponentInspectionResult.class)
            .setParameter("outgoingInspectionResult", CommonConstants.INSPECTION_RESULT)
            .setParameter("inspectionStatus1", CommonConstants.INSPECTION_STATUS_O_REJECTED)
            .setParameter("inspectionStatus2", CommonConstants.INSPECTION_STATUS_O_CONFIRM)
            .setParameter("inspectionStatus3", CommonConstants.INSPECTION_STATUS_O_UNAPPROVED)
            .setParameter("outgoingInspectionApproveDate", dueDate)
            .setParameter("endTime", dueEndDate);

        List<TblComponentInspectionResult> listOutgoingApprove = queryOutgoingApprove.getResultList();

        if (listOutgoingApprove != null && listOutgoingApprove.size() > 0) {
            List<String> outgoingCheckApproveEndComponet = new ArrayList();
            int outgoingCheckApproveEndQuantity = 0;
            for (TblComponentInspectionResult result : listOutgoingApprove) {
                outgoingCheckApproveEndComponet.add(result.getMstComponent().getComponentCode());
                sendMail.setOutgoingCheckApproveEndComponet(outgoingCheckApproveEndComponet);
                // KM-10000 検査系メール通知の所属指定を修正 20181207 start
                getNoticeDepartList(result.getOutgoingInspectionPersonUuid(), outgoingInspectiongDepartmentList);
                // KM-10000 検査系メール通知の所属指定を修正 20181207 end
                outgoingCheckApproveEndQuantity++;
            }
            sendMail.setOutgoingCheckApproveEndQuantity(outgoingCheckApproveEndQuantity);
        }
        sendMail.setBaseUrl(kartePropertyService.getBaseUrl());
        // KM-10000 検査系メール通知の所属指定を修正 20181207 start
        sendMail.setUserDepartmentList(outgoingInspectiongDepartmentList);
        if (sendMail.getUserDepartmentList().size() > 0) {
            tblComponentInsectionSendMailService.sendNotice(CommonConstants.OUTGOING_CHECK_RESULT_OK, sendMail);//出荷検査OK
        }
        // KM-10000 検査系メール通知の所属指定を修正 20181207 end
        
        
        //入荷通知batch
        //入荷检查完了部品
        TblComponentInspectionSendMail sendMailIncoming = new TblComponentInspectionSendMail();
        sql = new StringBuilder();
        sql.append(QUERY_TBL_COMPONENT_INSECTION_RESULT);
        sql.append(" WHERE t.incomingInspectionResult = :incomingInspectionResult");
        sql.append(" AND t.incomingInspectionDate between :incomingInspectionDate and :endTime");

        Query queryIncomingCheckEnd = entityManager.createQuery(sql.toString(), TblComponentInspectionResult.class)
            .setParameter("incomingInspectionResult", CommonConstants.INSPECTION_RESULT)
            .setParameter("incomingInspectionDate", dueDate)
            .setParameter("endTime", dueEndDate);

        List<TblComponentInspectionResult> listIncomingCheckEnd = queryIncomingCheckEnd.getResultList();
        // KM-10000 検査系メール通知の所属指定を修正 20181207 start
        List<Integer> incomingInspectiongDepartmentList = new ArrayList<>();
        // KM-10000 検査系メール通知の所属指定を修正 20181207 end

        if (listIncomingCheckEnd != null && listIncomingCheckEnd.size() > 0) {
            List<String> incomingCheckEndComponet = new ArrayList();
            int incomingCheckEndQuantity = 0;
            for (TblComponentInspectionResult result : listIncomingCheckEnd) {
                incomingCheckEndComponet.add(result.getMstComponent().getComponentCode());
                sendMailIncoming.setIncomingCheckEndComponet(incomingCheckEndComponet);
                incomingCheckEndQuantity++;
                // KM-10000 検査系メール通知の所属指定を修正 20181207 start
                getNoticeDepartList(result.getIncomingInspectionPersonUuid(), incomingInspectiongDepartmentList);
                // KM-10000 検査系メール通知の所属指定を修正 20181207 end
            }
            sendMailIncoming.setIncomingCheckEndQuantity(incomingCheckEndQuantity);
        }
        
        List<TblComponentInspectionResult> listIncomingConcession =  entityManager.createQuery(sql.toString(), TblComponentInspectionResult.class)
            .setParameter("incomingInspectionResult", CommonConstants.INSPECTION_RESULT_SPECIAL)
            .setParameter("incomingInspectionDate", dueDate)
            .setParameter("endTime", dueEndDate).getResultList();
        List<String> incomingConcessionComp = listIncomingConcession.stream().map(result->{
            getNoticeDepartList(result.getIncomingInspectionPersonUuid(), incomingInspectiongDepartmentList);
            return result.getMstComponent().getComponentCode();
        }).collect(Collectors.toList());
        sendMailIncoming.setIncomingConcessionComp(incomingConcessionComp);
        
        //入荷検査確認完了部品
        sql = new StringBuilder();
        sql.append(QUERY_TBL_COMPONENT_INSECTION_RESULT);
        sql.append(" WHERE t.incomingInspectionResult = :incomingInspectionResult");
        sql.append(" AND t.inspectionStatus <> :inspectionStatus");
        sql.append(" AND t.incomingConfirmDate between :incomingConfirmDate and :endTime");

        Query queryIncomingCofirm = entityManager.createQuery(sql.toString(), TblComponentInspectionResult.class)
            .setParameter("incomingInspectionResult", CommonConstants.INSPECTION_RESULT)
            .setParameter("inspectionStatus", CommonConstants.INSPECTION_STATUS_I_REJECTED)
            .setParameter("incomingConfirmDate", dueDate)
            .setParameter("endTime", dueEndDate);

        List<TblComponentInspectionResult> listIncomingConfirm = queryIncomingCofirm.getResultList();

        if (listIncomingConfirm != null && listIncomingConfirm.size() > 0) {
            List<String> incomingCheckConfirmEndComponet = new ArrayList();
            int incomingCheckCofirmEndQuantity = 0;
            for (TblComponentInspectionResult result : listIncomingConfirm) {
                incomingCheckConfirmEndComponet.add(result.getMstComponent().getComponentCode());
                sendMailIncoming.setIncomingCheckConfirmEndComponet(incomingCheckConfirmEndComponet);
                incomingCheckCofirmEndQuantity++;
                // KM-10000 検査系メール通知の所属指定を修正 20181207 start
                getNoticeDepartList(result.getIncomingInspectionPersonUuid(), incomingInspectiongDepartmentList);
                // KM-10000 検査系メール通知の所属指定を修正 20181207 end
            }
            sendMailIncoming.setIncomingCheckCofirmEndQuantity(incomingCheckCofirmEndQuantity);
        }
        
        //入荷検査承認完了部品
        sql = new StringBuilder();
        sql.append(QUERY_TBL_COMPONENT_INSECTION_RESULT);
        sql.append(" WHERE t.incomingInspectionResult = :incomingInspectionResult");
        sql.append(" AND t.inspectionStatus <> :inspectionStatus");
        sql.append(" AND t.incomingInspectionApproveDate between :incomingInspectionApproveDate and :endTime");

        Query queryIncomingApprove = entityManager.createQuery(sql.toString(), TblComponentInspectionResult.class)
            .setParameter("incomingInspectionResult", CommonConstants.INSPECTION_RESULT)
            .setParameter("inspectionStatus", CommonConstants.INSPECTION_STATUS_I_REJECTED)
            .setParameter("incomingInspectionApproveDate", dueDate)
            .setParameter("endTime", dueEndDate);

        List<TblComponentInspectionResult> listIncomingApprove = queryIncomingApprove.getResultList();

        if (listIncomingApprove != null && listIncomingApprove.size() > 0) {
            List<String> incomingCheckApproveEndComponet = new ArrayList();
            int incomingCheckApproveEndQuantity = 0;
            for (TblComponentInspectionResult result : listIncomingApprove) {
                incomingCheckApproveEndComponet.add(result.getMstComponent().getComponentCode());
                sendMailIncoming.setIncomingCheckApproveEndComponet(incomingCheckApproveEndComponet);
                incomingCheckApproveEndQuantity++;
                // KM-10000 検査系メール通知の所属指定を修正 20181207 start
                getNoticeDepartList(result.getIncomingInspectionPersonUuid(), incomingInspectiongDepartmentList);
                // KM-10000 検査系メール通知の所属指定を修正 20181207 end
            }
            sendMailIncoming.setIncomingCheckApproveEndQuantity(incomingCheckApproveEndQuantity);
        }
        sendMailIncoming.setBaseUrl(kartePropertyService.getBaseUrl());
        // KM-10000 検査系メール通知の所属指定を修正 20181207 start
        sendMailIncoming.setUserDepartmentList(incomingInspectiongDepartmentList);
        if (sendMailIncoming.getUserDepartmentList().size() > 0) {
            tblComponentInsectionSendMailService.sendNotice(CommonConstants.INCOMING_CHECK_RESULT_OK, sendMailIncoming);//入荷検査OK
        }
        // KM-10000 検査系メール通知の所属指定を修正 20181207 end
        
        // KM-977 受入検査結果(合格)がサプライヤに反映されたことを通知します 2018/11/29 start
        sql = new StringBuilder();
        sql.append(QUERY_TBL_COMPONENT_INSECTION_RESULT);
        sql.append(" JOIN FETCH t.mstCompanyOutgoing");
        sql.append(" WHERE (t.incomingInspectionResult = :incomingInspectionResult OR t.incomingInspectionResult = :incomingInspectionResult1)");
        sql.append(" AND t.inspectionStatus = :inspectionStatus");
        sql.append(" AND t.inspBatchUpdateStatus = :inspBatchUpdateStatus");
        sql.append(" AND t.incomingInspectionApproveDate between :incomingInspectionApproveDate and :endTime");

        Query queryIncomingApproved = entityManager.createQuery(sql.toString(), TblComponentInspectionResult.class)
            .setParameter("incomingInspectionResult", CommonConstants.INSPECTION_RESULT)
            .setParameter("incomingInspectionResult1", CommonConstants.INSPECTION_RESULT_SPECIAL)
            .setParameter("inspectionStatus", CommonConstants.INSPECTION_STATUS_I_APPROVED)
            .setParameter("inspBatchUpdateStatus", CommonConstants.INSP_BATCH_UPDATE_STATUS_O_RESULT_SENT)
            .setParameter("incomingInspectionApproveDate", dueDate)
            .setParameter("endTime", dueEndDate);
        
        TblComponentInspectionSendMail sendMailNotice = new TblComponentInspectionSendMail();
        
        List<TblComponentInspectionResult> listIncomingApproved = queryIncomingApproved.getResultList();
        
        List<Integer> departmentList = new ArrayList<>();

        for (TblComponentInspectionResult inspectionResult : listIncomingApproved) {

            if (null != inspectionResult.getMstCompanyOutgoing()) {
                // 出荷会社名称
                sendMailNotice.setOutgoingCompanyName(inspectionResult.getMstCompanyOutgoing().getCompanyName());
            }
            sendMailNotice.setBaseUrl(kartePropertyService.getBaseUrl());

            getNoticeDepartList(inspectionResult.getOutgoingInspectionPersonUuid(), departmentList);
        }
        sendMailNotice.setUserDepartmentList(departmentList);
        if (sendMailNotice.getUserDepartmentList().size() > 0) {
            tblComponentInsectionSendMailService.sendNotice(CommonConstants.INCOMING_INSPECTION_OK, sendMailNotice);//受入検査結果（合格）
        }
        // KM-977 受入検査結果(合格)がサプライヤに反映されたことを通知します 2018/11/29 end
        
        //通知日
        return BatchStatus.COMPLETED.toString();
    }
    
    // KM-10000 検査系メール通知の所属指定を修正 20181207 start
    private void getNoticeDepartList(String userUuid, List<Integer> departList) {

        MstUser user = mstUserService.getMstUserByUuid(userUuid);
        if (null != user) {
            if (StringUtils.isNotEmpty(user.getDepartment())) {
                departList.add(Integer.valueOf(user.getDepartment()));
            }
        }
    }
    // KM-10000 検査系メール通知の所属指定を修正 20181207 end

}
