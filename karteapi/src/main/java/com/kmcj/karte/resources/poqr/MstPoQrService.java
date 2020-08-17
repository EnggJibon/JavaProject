/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.poqr;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 * poqrサービス
 *
 * @author admin
 */
@Dependent
public class MstPoQrService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;
    /**
     * 納品先はPO QRコードマスタより選択する。
     *
     * @param isLoad
     * @return
     */
    public MstPoQrVoList getPoQrList(boolean isLoad) {

        MstPoQrVoList mstPoQrVoList = new MstPoQrVoList();

        StringBuilder sql = new StringBuilder();
        if (isLoad) {
            sql.append(" SELECT poqr FROM MstPoQr poqr JOIN FETCH poqr.mstCompany company WHERE poqr.deliveryDestId <> NULL ORDER BY poqr.seq");
        } else {
            sql.append(" SELECT poqr FROM MstPoQr poqr LEFT JOIN FETCH poqr.mstCompany company ORDER BY poqr.seq ");

        }

        Query query = entityManager.createQuery(sql.toString());
        List list = query.getResultList();

        MstPoQrVo mstPoQrVo;
        List<MstPoQrVo> mstPoQrVos = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            MstPoQr mstPoQr = (MstPoQr) list.get(i);
            mstPoQrVo = new MstPoQrVo();
            mstPoQrVo.setDeliveryDestId(mstPoQr.getDeliveryDestId());
            if (mstPoQr.getMstCompany() != null) {
                mstPoQrVo.setCompamyCode(mstPoQr.getMstCompany().getCompanyCode());
                mstPoQrVo.setCompamyName(mstPoQr.getMstCompany().getCompanyName());
            }
            mstPoQrVo.setSeq(mstPoQr.getSeq());
            mstPoQrVo.setDescription(mstPoQr.getDescription());
            mstPoQrVo.setUuid(mstPoQr.getUuid());

            mstPoQrVos.add(mstPoQrVo);
        }
        mstPoQrVoList.setMstPoQrVos(mstPoQrVos);
        return mstPoQrVoList;
    }

    /**
     *
     * @param mstPoQrVo
     * @param userUuid
     */
    @Transactional
    public void postPoQr(MstPoQrVo mstPoQrVo, String userUuid) {
        MstPoQr mstPoQr = entityManager.find(MstPoQr.class, mstPoQrVo.getUuid());
        if (mstPoQr != null) {
            //mstPoQr.setDescription(mstPoQrVo.getDescription());
            if (StringUtils.isEmpty(mstPoQrVo.getDeliveryDestId())) {
                mstPoQr.setDeliveryDestId(null);
            } else {
                mstPoQr.setDeliveryDestId(mstPoQrVo.getDeliveryDestId());
            }
            mstPoQr.setUpdateDate(new Date());
            mstPoQr.setUpdateUserUuid(userUuid);
            entityManager.merge(mstPoQr);
        }
    }

    /**
     *
     * @param mstPoQrVos
     * @param userUuid
     * @param langId
     * @return
     */
    @Transactional
    public BasicResponse postPoQr(MstPoQrVoList mstPoQrVos, String userUuid,String langId) {
        BasicResponse basicResponse = new BasicResponse();
        if (mstPoQrVos != null && mstPoQrVos.getMstPoQrVos() != null) {
            
            //納品先は一意を確認
            if (mstPoQrVos.getMstPoQrVos().size() > 1) {
                for (int i = 0; i < mstPoQrVos.getMstPoQrVos().size(); i++) {
                    MstPoQrVo mstPoQrVo = mstPoQrVos.getMstPoQrVos().get(i);
                    for (int k = i + 1; k < mstPoQrVos.getMstPoQrVos().size(); k++) {
                        MstPoQrVo mstPoQrVo2 = mstPoQrVos.getMstPoQrVos().get(k);
                        if (StringUtils.isNotEmpty(mstPoQrVo.getDeliveryDestId()) && StringUtils.isNotEmpty(mstPoQrVo2.getDeliveryDestId())) {
                            if (mstPoQrVo.getDeliveryDestId().equals(mstPoQrVo2.getDeliveryDestId())) {
                                basicResponse.setError(true);
                                basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                                String msg = mstDictionaryService.getDictionaryValue(langId, "msg_error_delivery_dest_company_exists");
                                basicResponse.setErrorMessage(msg);
                                return basicResponse;
                            }
                        }
                    }
                }
            }
            
            for (int i = 0; i < mstPoQrVos.getMstPoQrVos().size(); i++) {
                postPoQr(mstPoQrVos.getMstPoQrVos().get(i), userUuid);
            }
        }
        return basicResponse;
    }

}
