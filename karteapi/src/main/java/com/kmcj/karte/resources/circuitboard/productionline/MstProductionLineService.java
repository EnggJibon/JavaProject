package com.kmcj.karte.resources.circuitboard.productionline;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.IndexsResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.circuitboard.productionline.machine.MstProductionLineMachineService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.location.MstLocationList;
import com.kmcj.karte.resources.location.MstLocationService;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.MstMachineList;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.SpecialSqlLikeOperator;
import org.apache.commons.lang.StringUtils;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Created by xiaozhou.wang on 2017/08/09.
 * Updated by MinhDTB
 */
@Dependent
public class MstProductionLineService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private MstLocationService mstLocationService;

    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private MstProductionLineMachineService mstProductionLineMachineService;

    private List<MstProductionLine> getMstProductionLines(String productionLineName) {
        StringBuilder sql = new StringBuilder("SELECT mstProductionLine");

        sql = sql.append(" FROM MstProductionLine mstProductionLine WHERE 1=1 ");

        if (StringUtils.isNotEmpty(productionLineName)) {
            sql = sql.append(" and mstProductionLine.productionLineName LIKE :productionLineName ");
        }

        sql = sql.append(" ORDER BY mstProductionLine.productionLineName ");

        TypedQuery<MstProductionLine> query = entityManager.createQuery(sql.toString(), MstProductionLine.class);

        if (StringUtils.isNotEmpty(productionLineName)) {
            query.setParameter("productionLineName", "%" + SpecialSqlLikeOperator.verify(productionLineName) + "%");
        }

        return query.getResultList();
    }

    private BasicResponse getIndexsResponse(List<MstProductionLine> mstProductionLines, LoginUser loginUser) {
//    private IndexsResponse<MstProductionLine> getIndexsResponse(List<MstProductionLine> mstProductionLines, LoginUser loginUser) {
//        IndexsResponse<MstProductionLine> response = new IndexsResponse<>();
        BasicResponse response = new BasicResponse();

        response.setError(false);
        
        List<MstProductionLine> list = getEmptyProductionLineNameRecords(mstProductionLines);
        if (list.size() > 0) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_production_line_name_empty"));
//            response.setFieldName("productionLineName");
//            response.setDatas(list);
            return response;
        }

        list = getDuplicateProductionLineNameRecords(mstProductionLines);
        if (list.size() > 0) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_duplicate_value"));
//            response.setFieldName("productionLineName");
//            response.setDatas(list);
            return response;
        }

        return response;
    }

    private MstProductionLine getMstProductionLineByProductionLineId(String productionLineId) {
        TypedQuery<MstProductionLine> query = entityManager.createNamedQuery("MstProductionLine.findByProductionLineId", MstProductionLine.class);
        query.setParameter("productionLineId", productionLineId);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    private MstProductionLine getMstProductionLineByProductionLineName(String productionLineName, String productionLineId) {
        TypedQuery<MstProductionLine> query;

        if (productionLineId != null) {
            query = entityManager.createNamedQuery("MstProductionLine.findByProductionLineNameEx", MstProductionLine.class);
        } else {
            query = entityManager.createNamedQuery("MstProductionLine.findByProductionLineName", MstProductionLine.class);
        }

        query.setParameter("productionLineName", productionLineName);

        if (productionLineId != null) {
            query.setParameter("productionLineId", productionLineId);
        }

        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    private boolean checkProductionLineExistByProductionLineId(String productionLineId) {
        return getMstProductionLineByProductionLineId(productionLineId) != null;
    }

    private List<MstProductionLine> getDuplicateProductionLineNameRecords(List<MstProductionLine> mstProductionLines) {
        List<MstProductionLine> ret = new ArrayList<>();
        for (MstProductionLine mstProductionLine : mstProductionLines) {
            MstProductionLine mstProductionLineX = getMstProductionLineByProductionLineName(mstProductionLine.getProductionLineName(),
                    mstProductionLine.getProductionLineId());

            if (mstProductionLineX != null) {
                Optional<MstProductionLine> optionalMstProductionLine = mstProductionLines.stream().filter(m ->
                        mstProductionLineX.getProductionLineId().equals(m.getProductionLineId())).findAny();
                if (!optionalMstProductionLine.isPresent()) {
                    ret.add(mstProductionLine);
                }
            }
            if (StringUtils.isEmpty(mstProductionLine.getProductionLineName())) {
                ret.add(mstProductionLine);
            }
        }

        return ret;
    }

    private List<MstProductionLine> getEmptyProductionLineNameRecords(List<MstProductionLine> mstProductionLines) {
        List<MstProductionLine> ret = new ArrayList<>();
        for (MstProductionLine mstProductionLine : mstProductionLines) {
            if (StringUtils.isEmpty(mstProductionLine.getProductionLineName())) {
                ret.add(mstProductionLine);
            }
        }

        return ret;
    }

    public MstProductionLineList getMstProductionLineList(String productionLineName) {
        MstProductionLineList response = new MstProductionLineList();

        List<MstProductionLine> mstProductionLines = getMstProductionLines(productionLineName);
        for (int i = 0; i < mstProductionLines.size(); i++) {
            MstProductionLine mstProductionLine = mstProductionLines.get(i);
            mstProductionLine.setMstProductionLineMachines(mstProductionLineMachineService
                    .getMstProductionLineMachines(mstProductionLine.getProductionLineId()));
            mstProductionLines.set(i, mstProductionLine);
        }

        response.setMstProductionLines(mstProductionLines);
        return response;
    }

    @Transactional
    BasicResponse updateMstProductionLines(List<MstProductionLine> mstProductionLines, LoginUser loginUser) {
//    IndexsResponse<MstProductionLine> updateMstProductionLines(List<MstProductionLine> mstProductionLines, LoginUser loginUser) {
//        IndexsResponse<MstProductionLine> response = getIndexsResponse(mstProductionLines, loginUser);
        BasicResponse response = new BasicResponse();

        if (response.isError())
            return response;

        for (MstProductionLine mstProductionLine : mstProductionLines) {
            if (!checkProductionLineExistByProductionLineId(mstProductionLine.getProductionLineId())) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_production_line_id_not_found"));
                return response;
            }

            updateMstProduct(mstProductionLine, loginUser);
        }

        return response;
    }

    @Transactional
    private void updateMstProduct(MstProductionLine mstProductionLine, LoginUser loginUser) {
        if (checkProductionLineChange(mstProductionLine)) {
            mstProductionLine.setUpdateDate(new java.util.Date());
            mstProductionLine.setUpdateUserUuid(loginUser.getUserUuid());
        }

        entityManager.merge(mstProductionLine);

        mstProductionLineMachineService.updateMstProductionLineMachines(mstProductionLine.getProductionLineId(),
                mstProductionLine.getMstProductionLineMachines(), loginUser);
    }

    private boolean checkProductionLineChange(MstProductionLine mstProductionLine) {
        TypedQuery<MstProductionLine> query = entityManager.createNamedQuery("MstProductionLine.findByProductionLineId", MstProductionLine.class);
        query.setParameter("productionLineId", mstProductionLine.getProductionLineId());
        MstProductionLine oldMstProductionLine = query.getSingleResult();

        return !StringUtils.equals(oldMstProductionLine.getProductionLineName(), mstProductionLine.getProductionLineName()) ||
                !Objects.equals(oldMstProductionLine.getLocation(), mstProductionLine.getLocation()) ||
                oldMstProductionLine.getDepartment() != mstProductionLine.getDepartment() ||
                oldMstProductionLine.getMstProductionLineMachines() != mstProductionLine.getMstProductionLineMachines();
    }

    @Transactional
    public BasicResponse addMstProductionLines(List<MstProductionLine> mstProductionLines, LoginUser loginUser) {
//    public IndexsResponse<MstProductionLine> addMstProductionLines(List<MstProductionLine> mstProductionLines, LoginUser loginUser) {
//        IndexsResponse<MstProductionLine> response = getIndexsResponse(mstProductionLines, loginUser);
        BasicResponse response = new BasicResponse();
        
        if (response.isError())
            return response;

        for (MstProductionLine mstProductionLine : mstProductionLines) {
            addMstProduct(mstProductionLine, loginUser);
        }

        return response;
    }

    @Transactional
    public void addMstProduct(MstProductionLine mstProductionLine, LoginUser loginUser) {
        mstProductionLine.setProductionLineId(IDGenerator.generate());
        mstProductionLine.setCreateDate(new java.util.Date());
        mstProductionLine.setCreateUserUuid(loginUser.getUserUuid());
        mstProductionLine.setUpdateDate(new java.util.Date());
        mstProductionLine.setUpdateUserUuid(loginUser.getUserUuid());

        entityManager.persist(mstProductionLine);

        mstProductionLineMachineService.updateMstProductionLineMachines(mstProductionLine.getProductionLineId(),
                mstProductionLine.getMstProductionLineMachines(), loginUser);
    }

    MstProductionLineData getData(LoginUser loginUser) {
        MstLocationList mstLocationList = mstLocationService.getLocations(null,
                null, null, null, false);
        MstChoiceList mstChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");

        List<MstProductionLineDepartment> mstProductionLineDepartments = new ArrayList<>();
        for (MstChoice mstChoice : mstChoiceList.getMstChoice()) {
            MstProductionLineDepartment mstProductionLineDepartment =
                    new MstProductionLineDepartment(mstChoice.getChoice(), Integer.parseInt(mstChoice.getMstChoicePK().getSeq()));
            mstProductionLineDepartments.add(mstProductionLineDepartment);
        }

        MstProductionLineData mstProductionLineData = new MstProductionLineData();
        mstProductionLineData.setMstLocations(mstLocationList.getMstLocations());
        mstProductionLineData.setMstProductionLineDepartments(mstProductionLineDepartments);

        return mstProductionLineData;
    }

    @Transactional
    BasicResponse deleteProductionLine(String productionLineId, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();

        if (checkProductionLineExistByProductionLineId(productionLineId)) {
            mstProductionLineMachineService.deleteAllByProductionLineId(productionLineId);

            Query productQuery = entityManager.createNamedQuery("MstProductionLine.deleteByProductionLineId");
            productQuery.setParameter("productionLineId", productionLineId);
            productQuery.executeUpdate();

        } else {
//            response.setError(true);
//            response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_production_line_id_not_found"));
        }

        return response;
    }

    MstMachineList getMstMachineList(String machineName) {
        MstMachineList response = new MstMachineList();
        response.setMstMachines(getMstMachines(machineName));
        return response;

    }

    private List<MstMachine> getMstMachines(String machineName) {
        StringBuilder sql = new StringBuilder("SELECT mstMachine");

        sql = sql.append(" FROM MstMachine mstMachine WHERE 1=1 ");

        if (StringUtils.isNotEmpty(machineName)) {
            sql = sql.append(" AND mstMachine.machineName LIKE :machineName ");
        }

        TypedQuery<MstMachine> query = entityManager.createQuery(sql.toString(), MstMachine.class);

        if (StringUtils.isNotEmpty(machineName)) {
            query.setParameter("machineName", "%" + SpecialSqlLikeOperator.verify(machineName) + "%");
        }

        return query.getResultList();
    }
}
