package com.kmcj.karte.resources.circuitboard.product;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.IndexsResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.circuitboard.product.component.MstProductComponent;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponentList;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.*;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xiaozhou.wang on 2017/08/10.
 * Updated by MinhDTB
 */
@Dependent
public class MstProductService {

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private TblCsvImportService tblCsvImportService;

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    private long addedCount = 0;    //追加件数
    private long updatedCount = 0;  //更新件数
    private long failedCount = 0;   //失敗件数
    private long deletedCount = 0;  //削除件数

    private boolean checkProductExistByProductId(String productId) {
        return getMstProductByProductId(productId) != null;
    }

    private boolean checkProductExistByProductCode(String productCode) {
        return getMstProductByProductCode(productCode, null) != null;
    }

    private boolean checkProductExistByProductName(String productName) {
        return getMstProductByProductName(productName, null) != null;
    }

    private MstProduct getMstProductByProductId(String productId) {
        TypedQuery<MstProduct> query = entityManager.createNamedQuery("MstProduct.findByProductId", MstProduct.class);
        query.setParameter("productId", productId);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    private MstProduct getMstProductByProductCode(String productCode, String productId) {
        TypedQuery<MstProduct> query;
        if (productId != null) {
            query = entityManager.createNamedQuery("MstProduct.findByProductCodeEx", MstProduct.class);
        } else {
            query = entityManager.createNamedQuery("MstProduct.findByProductCode", MstProduct.class);
        }

        query.setParameter("productCode", productCode);

        if (productId != null) {
            query.setParameter("productId", productId);
        }

        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    private MstProduct getMstProductByProductName(String productName, String productId) {
        TypedQuery<MstProduct> query;
        if (productId != null) {
            query = entityManager.createNamedQuery("MstProduct.findByProductNameEx", MstProduct.class);
        } else {
            query = entityManager.createNamedQuery("MstProduct.findByProductName", MstProduct.class);
        }

        query.setParameter("productName", productName);

        if (productId != null) {
            query.setParameter("productId", productId);
        }

        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    private List<MstProduct> getMstProducts(String productName, String productCode) {
        StringBuilder sql = new StringBuilder("SELECT mstProduct");

        sql = sql.append(" FROM MstProduct mstProduct WHERE 1=1 ");

        if (StringUtils.isNotEmpty(productName)) {
            sql = sql.append(" and mstProduct.productName LIKE :productName ");
        }

        if (StringUtils.isNotEmpty(productCode)) {
            sql = sql.append(" and mstProduct.productCode LIKE :productCode ");
        }

        sql = sql.append(" ORDER BY mstProduct.productCode ");

        TypedQuery<MstProduct> query = entityManager.createQuery(sql.toString(), MstProduct.class);

        if (StringUtils.isNotEmpty(productName)) {
            query.setParameter("productName", "%" + SpecialSqlLikeOperator.verify(productName) + "%");
        }

        if (StringUtils.isNotEmpty(productCode)) {
            query.setParameter("productCode", "%" + SpecialSqlLikeOperator.verify(productCode) + "%");
        }

        return query.getResultList();
    }

    MstProductList getMstProductList(String productName, String productCode) {
        MstProductList response = new MstProductList();

        List<MstProduct> mstProducts = getMstProducts(productName, productCode);
        for (int i = 0; i < mstProducts.size(); i++) {
            MstProduct mstProduct = mstProducts.get(i);
            List<MstComponent> components = getMstComponentsByProductId(mstProduct.getProductId());
            mstProduct.setMstComponentList(components);
            mstProducts.set(i, mstProduct);
        }

        response.setMstProducts(mstProducts);

        return response;
    }

    private List<MstComponent> getMstComponents(String componentCode) {
        StringBuilder sql = new StringBuilder("SELECT mstComponent");

        sql = sql.append(" FROM MstComponent mstComponent WHERE 1=1 ");

        if (StringUtils.isNotEmpty(componentCode)) {
            sql = sql.append(" and mstComponent.componentCode LIKE :componentCode ");
        }

        TypedQuery<MstComponent> query = entityManager.createQuery(sql.toString(), MstComponent.class);

        if (StringUtils.isNotEmpty(componentCode)) {
            query.setParameter("componentCode", "%" + SpecialSqlLikeOperator.verify(componentCode) + "%");
        }

        return query.getResultList();
    }

    private MstComponent getMstComponent(String componentId) {
        TypedQuery<MstComponent> query = entityManager.createNamedQuery("MstComponent.findById", MstComponent.class);
        query.setParameter("id", componentId);
        return query.getSingleResult();
    }

    private List<MstComponent> getMstComponentsByProductId(String productId) {
        List<MstComponent> components = new ArrayList<>();
        TypedQuery<String> query = entityManager.createNamedQuery("MstProductComponent.findByProductId", String.class);
        query.setParameter("productId", productId);
        List<String> mstProductComponents = query.getResultList();

        for (String mstProductComponent : mstProductComponents) {
            components.add(getMstComponent(mstProductComponent));
        }

        return components;
    }

    MstComponentList getMstComponentList(String componentCode) {
        MstComponentList response = new MstComponentList();
        response.setMstComponents(getMstComponents(componentCode));
        return response;
    }

    FileReponse exportCsv(String productName, String productCode, LoginUser loginUser) {
        String langId = loginUser.getLangId();

        ArrayList<ArrayList<String>> gLineList = new ArrayList<>();
        ArrayList<String> headList = new ArrayList<>();

        headList.add(mstDictionaryService.getDictionaryValue(langId, "product_code"));
        headList.add(mstDictionaryService.getDictionaryValue(langId, "product_name"));
        headList.add(mstDictionaryService.getDictionaryValue(langId, "production_start_date"));
        headList.add(mstDictionaryService.getDictionaryValue(langId, "production_end_date"));
        headList.add(mstDictionaryService.getDictionaryValue(langId, "production_flag"));
        headList.add(mstDictionaryService.getDictionaryValue(langId, "product_update_date"));
        headList.add(mstDictionaryService.getDictionaryValue(langId, "delete_record"));

        gLineList.add(headList);

        DateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat df2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        List<MstProduct> mstProducts = getMstProducts(productName, productCode);
        for (MstProduct mstProduct : mstProducts) {
            ArrayList<String> valueList = new ArrayList<>();
            valueList.add(mstProduct.getProductCode());
            valueList.add(mstProduct.getProductName());
            valueList.add(mstProduct.getProductionStartDate() != null ? df1.format(mstProduct.getProductionStartDate()) : "");
            valueList.add(mstProduct.getProductionEndDate() != null ? df1.format(mstProduct.getProductionEndDate()) : "");
            valueList.add(mstProduct.getProductionEndFlag() ? "1" : "0");
            valueList.add(mstProduct.getUpdateDate() != null ? df2.format(mstProduct.getUpdateDate()) : "");
            valueList.add("");
            gLineList.add(valueList);
        }

        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_PRODUCT);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MST_PRODUCT);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_PRODUCT);

        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);

        FileReponse fileReponse = new FileReponse();
        fileReponse.setFileUuid(uuid);

        return fileReponse;
    }

    private ImportResultResponse getImportError(String msgKey, LoginUser loginUser) {
        ImportResultResponse importResultResponse = new ImportResultResponse();
        importResultResponse.setError(true);
        importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
        String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), msgKey);
        importResultResponse.setErrorMessage(msg);
        return importResultResponse;
    }

    private Date getDate(String dateString) {
        if (dateString == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        sdf.setLenient(false);

        try {
            return sdf.parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }

    private void writeErrorLog(FileUtil fileUtil, String logFile, String langId, int row, String field, String messageKey) {
        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(
                mstDictionaryService.getDictionaryValue(langId, "row_number"), row,
                mstDictionaryService.getDictionaryValue(langId, field), "",
                mstDictionaryService.getDictionaryValue(langId, "error"), 1,
                mstDictionaryService.getDictionaryValue(langId, "error_detail"),
                mstDictionaryService.getDictionaryValue(langId, messageKey)));
    }

    @Transactional
    private void doImportCsv(LoginUser loginUser, ArrayList list, String logFile) {
        FileUtil fileUtil = new FileUtil();
        String langId = loginUser.getLangId();

        for (int i = 1; i < list.size(); i++) {
            ArrayList lineCsv = (ArrayList) list.get(i);

            int arrayLength = lineCsv.size();
            if (arrayLength != 7) {
                writeErrorLog(fileUtil, logFile, langId, i, "product_code", "msg_error_wrong_csv_layout");
                failedCount = failedCount + 1;
                continue;
            }

            String productCode = (null == lineCsv.get(0) ? "" : String.valueOf(lineCsv.get(0)));
            if (fileUtil.isNullCheck(productCode)) {
                writeErrorLog(fileUtil, logFile, langId, i, "product_code", "msg_error_not_null");
                failedCount = failedCount + 1;
                continue;
            } else if (fileUtil.maxLangthCheck(productCode, 45)) {
                writeErrorLog(fileUtil, logFile, langId, i, "product_code", "msg_error_over_length");
                failedCount = failedCount + 1;
                continue;
            }

            String productName = (null == lineCsv.get(1) ? "" : String.valueOf(lineCsv.get(1)));
            if (fileUtil.isNullCheck(productName)) {
                writeErrorLog(fileUtil, logFile, langId, i, "product_name", "msg_error_not_null");
                failedCount = failedCount + 1;
                continue;
            } else if (fileUtil.maxLangthCheck(productName, 100)) {
                writeErrorLog(fileUtil, logFile, langId, i, "product_name", "msg_error_over_length");
                failedCount = failedCount + 1;
                continue;
            }

            String productionStartDate = (null == lineCsv.get(2) ? "" : String.valueOf(lineCsv.get(2)));
            if (!fileUtil.isNullCheck(productionStartDate) && getDate(productionStartDate) == null) {
                writeErrorLog(fileUtil, logFile, langId, i, "production_start_date", "msg_error_date_format_invalid");
                failedCount = failedCount + 1;
                continue;
            }

            String productionEndDate = (null == lineCsv.get(3) ? "" : String.valueOf(lineCsv.get(3)));
            if (!fileUtil.isNullCheck(productionEndDate) && getDate(productionEndDate) == null) {
                writeErrorLog(fileUtil, logFile, langId, i, "production_end_date", "msg_error_date_format_invalid");
                failedCount = failedCount + 1;
                continue;
            }

            Date startDate = getDate(productionStartDate);
            Date endDate = getDate(productionEndDate);
            if (startDate != null && endDate != null) {
                if (startDate.after(endDate)) {
                    writeErrorLog(fileUtil, logFile, langId, i, "production_end_date", "msg_error_endate_greater_than_start_date");
                    failedCount = failedCount + 1;
                    continue;
                }
            }

            /* check duplicate product name */
            if (!checkProductExistByProductCode(productCode)) {
                if (checkProductExistByProductName(productName)) {
                    writeErrorLog(fileUtil, logFile, langId, i, "product_name", "msg_error_duplicate_value");
                    failedCount = failedCount + 1;
                    continue;
                }
            } else {
                MstProduct product = getMstProductByProductCode(productCode, null);
                if (getMstProductByProductName(productName, product.getProductId()) != null) {
                    writeErrorLog(fileUtil, logFile, langId, i, "product_name", "msg_error_duplicate_value");
                    failedCount = failedCount + 1;
                    continue;
                }
            }

            String productionFlagString = (null == lineCsv.get(4) ? "" : String.valueOf(lineCsv.get(4)));
            boolean productionFlag = false;
            if (fileUtil.isNullCheck(productionFlagString)) {
                productionFlag = false;
            } else {
                if (!StringUtils.equals(productionFlagString, "0")) {
                    productionFlag = true;
                }
            }

            String deleteFlag = (null == lineCsv.get(6) ? "" : String.valueOf(lineCsv.get(6)));
            if (deleteFlag != null && deleteFlag.trim().equals("1")) {
                /* delete record */
                if (checkProductExistByProductCode(productCode)) {
                    Query productQuery = entityManager.createNamedQuery("MstProduct.deleteByProductCode");
                    productQuery.setParameter("productCode", productCode);
                    productQuery.executeUpdate();
                    deletedCount = deletedCount + 1;
                }
            } else {
                MstProduct mstProduct = new MstProduct();
                mstProduct.setProductCode(productCode);
                mstProduct.setProductName(productName);
                mstProduct.setProductionStartDate(getDate(productionStartDate));
                mstProduct.setProductionEndDate(getDate(productionEndDate));
                mstProduct.setProductionEndFlag(productionFlag);
                mstProduct.setProductTypeId("1");

                MstProduct oldMstProduct = getMstProductByProductCode(productCode, null);
                if (oldMstProduct != null) {
                    /* update record */
                    mstProduct.setProductId(oldMstProduct.getProductId());
                    mstProduct.setCreateDate(oldMstProduct.getCreateDate());
                    mstProduct.setCreateUserUuid(oldMstProduct.getCreateUserUuid());
                    updateMstProduct(mstProduct, loginUser);
                    updatedCount = updatedCount + 1;
                } else {
                    /* add record */
                    addMstProduct(mstProduct, loginUser);
                    addedCount = addedCount + 1;
                }
            }
        }
    }

    @Transactional
    ImportResultResponse importCsv(String fileUuid, LoginUser loginUser) {
        try {
            ImportResultResponse importResultResponse = new ImportResultResponse();

            String csvFile = FileUtil.getCsvFilePath(kartePropertyService, fileUuid);
            if (!csvFile.endsWith(CommonConstants.CSV)) {
                return getImportError("msg_error_wrong_csv_layout", loginUser);
            }

            ArrayList readList = CSVFileUtil.readCsv(csvFile);
            int csvInfoSize = readList.size();
            if (csvInfoSize > 1) {
                String logFileUuid = IDGenerator.generate();//ログファイルUUID用意
                String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);

                doImportCsv(loginUser, readList, logFile);

                long succeededCount = addedCount + updatedCount + deletedCount;

                TblCsvImport tblCsvImport = new TblCsvImport();
                tblCsvImport.setImportUuid(IDGenerator.generate());
                tblCsvImport.setImportUserUuid(loginUser.getUserUuid());
                tblCsvImport.setImportDate(new Date());
                tblCsvImport.setImportTable(CommonConstants.TBL_MST_PRODUCT);

                TblUploadFile tblUploadFile = new TblUploadFile();
                tblUploadFile.setFileUuid(fileUuid);

                tblCsvImport.setUploadFileUuid(tblUploadFile);
                MstFunction mstFunction = new MstFunction();
                mstFunction.setId(CommonConstants.FUN_ID_MST_PRODUCT);
                tblCsvImport.setFunctionId(mstFunction);
                tblCsvImport.setRecordCount(csvInfoSize - 1);
                tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
                tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
                tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
                tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
                tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
                tblCsvImport.setLogFileUuid(logFileUuid);

                String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_PRODUCT);
                tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

                tblCsvImportService.createCsvImpor(tblCsvImport);

                importResultResponse.setTotalCount(csvInfoSize - 1);
                importResultResponse.setSucceededCount(succeededCount);
                importResultResponse.setAddedCount(addedCount);
                importResultResponse.setUpdatedCount(updatedCount);
                importResultResponse.setDeletedCount(deletedCount);
                importResultResponse.setFailedCount(failedCount);
                importResultResponse.setLog(logFileUuid);
            }

            return importResultResponse;
        } catch (Exception ex) {
            return getImportError("msg_error_upload_file_type_invalid", loginUser);
        }
    }

    private boolean checkProductChange(MstProduct mstProduct) {
        TypedQuery<MstProduct> query = entityManager.createNamedQuery("MstProduct.findByProductId", MstProduct.class);
        query.setParameter("productId", mstProduct.getProductId());
        MstProduct oldMstProduct = query.getSingleResult();

        return !StringUtils.equals(oldMstProduct.getProductCode(), mstProduct.getProductCode()) ||
                !StringUtils.equals(oldMstProduct.getProductName(), mstProduct.getProductName()) ||
                !Objects.equals(oldMstProduct.getProductionStartDate(), mstProduct.getProductionStartDate()) ||
                !Objects.equals(oldMstProduct.getProductionEndDate(), mstProduct.getProductionEndDate()) ||
                !Objects.equals(oldMstProduct.getMstComponentList(), mstProduct.getMstComponentList()) ||
                oldMstProduct.getProductionEndFlag() != mstProduct.getProductionEndFlag();
    }

    @Transactional
    private void updateMstProductComponent(MstProduct mstProduct, LoginUser loginUser) {
        TypedQuery<MstProductComponent> query = entityManager.createNamedQuery("MstProductComponent.deleteByProductId",
                MstProductComponent.class);
        query.setParameter("productId", mstProduct.getProductId());
        query.executeUpdate();

        for (MstComponent mstComponent : mstProduct.getMstComponentList()) {
            MstProductComponent mstProductComponent = new MstProductComponent();
            mstProductComponent.setProductId(mstProduct.getProductId());
            mstProductComponent.setComponentId(mstComponent.getId());
            mstProductComponent.setCreateDate(new java.util.Date());
            mstProductComponent.setCreateUserUuid(loginUser.getUserUuid());
            mstProductComponent.setUpdateDate(new java.util.Date());
            mstProductComponent.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(mstComponent);
            entityManager.persist(mstProductComponent);
        }
    }

    @Transactional
    private void addMstProduct(MstProduct mstProduct, LoginUser loginUser) {
        mstProduct.setCreateDate(new java.util.Date());
        mstProduct.setCreateUserUuid(loginUser.getUserUuid());
        mstProduct.setUpdateDate(new java.util.Date());
        mstProduct.setUpdateUserUuid(loginUser.getUserUuid());
        mstProduct.setProductId(IDGenerator.generate());
        mstProduct.setProductTypeId("1");
        entityManager.persist(mstProduct);

        updateMstProductComponent(mstProduct, loginUser);
    }

    private List<MstProduct> getEmptyProductCodeRecords(List<MstProduct> mstProducts) {
        List<MstProduct> ret = new ArrayList<>();
        for (MstProduct mstProduct : mstProducts) {
            if (StringUtils.isEmpty(mstProduct.getProductCode())) {
                ret.add(mstProduct);
            }
        }

        return ret;
    }

    private List<MstProduct> getEmptyProductNameRecords(List<MstProduct> mstProducts) {
        List<MstProduct> ret = new ArrayList<>();
        for (MstProduct mstProduct : mstProducts) {
            if (StringUtils.isEmpty(mstProduct.getProductName())) {
                ret.add(mstProduct);
            }
        }

        return ret;
    }

    private List<MstProduct> getDuplicateProductCodeRecords(List<MstProduct> mstProducts) {
        List<MstProduct> ret = new ArrayList<>();
        for (MstProduct mstProduct : mstProducts) {
            MstProduct mstProductX = getMstProductByProductCode(mstProduct.getProductCode(), mstProduct.getProductId());
            if (mstProductX != null) {
                Optional<MstProduct> optionalMstProduct = mstProducts.stream().filter(m ->
                        mstProductX.getProductId().equals(m.getProductId())).findAny();
                if (!optionalMstProduct.isPresent()) {
                    ret.add(mstProduct);
                }
            }
        }

        return ret;
    }

    private List<MstProduct> getDuplicateProductNameRecords(List<MstProduct> mstProducts) {
        List<MstProduct> ret = new ArrayList<>();
        for (MstProduct mstProduct : mstProducts) {
            MstProduct mstProductX = getMstProductByProductName(mstProduct.getProductName(), mstProduct.getProductId());
            if (mstProductX != null) {
                Optional<MstProduct> optionalMstProduct = mstProducts.stream().filter(m ->
                        mstProductX.getProductId().equals(m.getProductId())).findAny();
                if (!optionalMstProduct.isPresent()) {
                    ret.add(mstProduct);
                }
            }
        }

        return ret;
    }

    private IndexsResponse<MstProduct> getIndexsResponse(List<MstProduct> mstProducts, LoginUser loginUser) {
        IndexsResponse<MstProduct> response = new IndexsResponse<>();

        List<MstProduct> list = getEmptyProductCodeRecords(mstProducts);
        if (list.size() > 0) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_product_code_empty"));
            response.setFieldName("productCode");
            response.setDatas(list);
            return response;
        }

        list = getEmptyProductNameRecords(mstProducts);
        if (list.size() > 0) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_product_name_empty"));
            response.setFieldName("productName");
            response.setDatas(list);
            return response;
        }

        list = getDuplicateProductCodeRecords(mstProducts);
        if (list.size() > 0) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_duplicate_value"));
            response.setFieldName("productCode");
            response.setDatas(list);
            return response;
        }

        list = getDuplicateProductNameRecords(mstProducts);
        if (list.size() > 0) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_duplicate_value"));
            response.setFieldName("productName");
            response.setDatas(list);
            return response;
        }

        return response;
    }

    @Transactional
    BasicResponse addMstProducts(List<MstProduct> mstProducts, LoginUser loginUser) {
//    IndexsResponse<MstProduct> addMstProducts(List<MstProduct> mstProducts, LoginUser loginUser) {
//        IndexsResponse<MstProduct> response = getIndexsResponse(mstProducts, loginUser);

        BasicResponse response = new BasicResponse();

        if (response.isError())
            return response;

        for (MstProduct mstProduct : mstProducts) {
            addMstProduct(mstProduct, loginUser);
        }

        return response;
    }

    @Transactional
    BasicResponse deleteMstProductByProductId(String productId, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();

        if (checkProductExistByProductId(productId)) {
            Query productQuery = entityManager.createNamedQuery("MstProduct.deleteByProductId");
            productQuery.setParameter("productId", productId);
            productQuery.executeUpdate();

        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_product_id_not_found"));
        }

        return response;
    }

    @Transactional
    private void updateMstProduct(MstProduct mstProduct, LoginUser loginUser) {
        if (checkProductChange(mstProduct)) {
            mstProduct.setUpdateDate(new java.util.Date());
            mstProduct.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(mstProduct);

            updateMstProductComponent(mstProduct, loginUser);
        }
    }

    @Transactional
    BasicResponse updateMstProducts(List<MstProduct> mstProducts, LoginUser loginUser) {
//    IndexsResponse<MstProduct> updateMstProducts(List<MstProduct> mstProducts, LoginUser loginUser) {
//        IndexsResponse<MstProduct> response = getIndexsResponse(mstProducts, loginUser);

        BasicResponse response = new BasicResponse();

        if (response.isError())
            return response;

        for (MstProduct mstProduct : mstProducts) {
            if (!checkProductExistByProductId(mstProduct.getProductId())) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_product_id_not_found"));
                return response;
            }

            updateMstProduct(mstProduct, loginUser);
        }

        return response;
    }
}
