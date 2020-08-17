/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.stock;

import com.kmcj.karte.resources.mold.part.stock.excel.MoldPartStockExcelRecord;
import com.kmcj.karte.resources.mold.part.stock.excel.MoldPartStockExcelService;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.MstMoldService;
import com.kmcj.karte.resources.mold.part.MstMoldPart;
import com.kmcj.karte.resources.mold.part.MstMoldPartService;
import com.kmcj.karte.resources.mold.part.stock.excel.OrderNeededExcelService;
import com.kmcj.karte.resources.mold.part.stock.inout.MoldPartStockInOut;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author t.takasaki
 */
@Dependent
public class MoldPartStockService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    @Inject
    private KartePropertyService kartePropertyService;
    @Inject
    private MstChoiceService choiceService;
    @Inject
    private MoldPartStockExcelService moldPartStockExcelService;
    @Inject
    private OrderNeededExcelService orderNeededExcelService;
    @Inject
    private MstDictionaryService mstDictionaryService;
    @Inject
    private MstMoldService moldService;
    @Inject
    private MstMoldPartService moldPartService;
    @Inject
    private TblCsvImportService tblCsvImportService;
    
    public MoldPartStockRes getMoldPartStockById(String id) {
        Query query = entityManager.createNamedQuery("MoldPartStock.findById");
        query.setParameter("id", id);
        MoldPartStockRes res = new MoldPartStockRes();
        try {
            res.setMoldPartStock((MoldPartStock) query.getSingleResult());
            return res;
        }
        catch (NoResultException ne) {
            res.setError(true);
            res.setErrorCode(ErrorMessages.E201_APPLICATION);
            res.setErrorMessage("No data was found: " + id);
            return res;
        }
    }

    public MoldPartStockRes getMoldPartStockByCompoundKey(String moldUuid,  String moldPartId) {
        Query query = entityManager.createNamedQuery("MoldPartStock.findByMoldPartId");
        query.setParameter("moldPartId", moldPartId);
        query.setParameter("moldUuid", moldUuid);
        MoldPartStockRes res = new MoldPartStockRes();
        try {
            res.setMoldPartStock((MoldPartStock) query.getSingleResult());
            return res;
        }
        catch (NoResultException ne) {
            res.setError(true);
            res.setErrorCode(ErrorMessages.E201_APPLICATION);
            res.setErrorMessage("No data was found: " + moldUuid + " - " + moldPartId);
            return res;
        }
    }
    
    @Transactional
    public BasicResponse logicalDeleteMoldtPartStock(String id, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        Date currentDate = new java.util.Date();
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append("UPDATE MoldPartStock m ");
        sbQuery.append(" SET m.deleteFlg = 1, m.deleteUserUuid = :deleteUserUuid, m.deleteDate = :deleteDate ");
        sbQuery.append(" ,m.updateDate = :updateDate, m.updateUserUuid = :updateUserUuid ");
        sbQuery.append(" WHERE m.id = :id");
        Query query = entityManager.createQuery(sbQuery.toString());
        query.setParameter("deleteUserUuid", loginUser.getUserUuid());
        query.setParameter("deleteDate", currentDate);
        query.setParameter("updateUserUuid", loginUser.getUserUuid());
        query.setParameter("updateDate", currentDate);
        query.setParameter("id", id);
        int updatedCnt = query.executeUpdate();
        if (updatedCnt < 1) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage("No data was found: " + id);
        }
        return response;
    }

    @Transactional
    public BasicResponse physicalDeleteMoldtPartStock(String id, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append("DELETE FROM MoldPartStock m ");
        sbQuery.append(" WHERE m.id = :id");
        Query query = entityManager.createQuery(sbQuery.toString());
        query.setParameter("id", id);
        int updatedCnt = query.executeUpdate();
        if (updatedCnt < 1) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage("No data was found: " + id);
        }
        return response;
    }
    
    @Transactional
    public BasicResponse restoreMoldtPartStock(String id, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        Date currentDate = new java.util.Date();
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append("UPDATE MoldPartStock m ");
        sbQuery.append(" SET m.deleteFlg = 0, m.deleteUserUuid = null, m.deleteDate = null ");
        sbQuery.append(" ,m.updateDate = :updateDate, m.updateUserUuid = :updateUserUuid ");
        sbQuery.append(" WHERE m.id = :id");
        Query query = entityManager.createQuery(sbQuery.toString());
        query.setParameter("updateUserUuid", loginUser.getUserUuid());
        query.setParameter("updateDate", currentDate);
        query.setParameter("id", id);
        int updatedCnt = query.executeUpdate();
        if (updatedCnt < 1) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage("No data was found: " + id);
        }
        return response;
    }

    @Transactional
    public BasicResponse upsertMoldtPartStock(MoldPartStock moldPartStock, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        if (moldPartStock.getId() != null && !moldPartStock.getId().equals("")) {
            //UPDATE
            MoldPartStock stock = getMoldPartStockWithLock(moldPartStock.getId());
            if (stock == null) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage("No data was found: " + moldPartStock.getId());
                return response;
            }
            updateMoldPartStock(stock, moldPartStock, loginUser);
        }
        else {
            //INSERT
            createMoldPartStock(moldPartStock, loginUser);
        }
        return response;
    }
    
    @Transactional
    private void updateMoldPartStock(MoldPartStock oldRec, MoldPartStock newRec, LoginUser loginUser) {
        boolean basicInfoUpdated = false;
        if ((oldRec.getStorageCode() == null && newRec.getStorageCode() != null) ||
                (oldRec.getStorageCode() != null && newRec.getStorageCode() == null) ||
                (oldRec.getStorageCode() != null && newRec.getStorageCode() != null && !oldRec.getStorageCode().equals(newRec.getStorageCode())) ||
                (oldRec.getOrderPoint() != newRec.getOrderPoint()) ||
                (oldRec.getOrderUnit() != newRec.getOrderUnit())) {
            basicInfoUpdated = true;
        }
        updateMoldPartStockStatus(newRec);
        Date currentDate = new Date();
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append(" UPDATE MoldPartStock m SET ");
        sbQuery.append(" m.storageCode = :storageCode ");
        sbQuery.append(" ,m.orderPoint = :orderPoint ");
        sbQuery.append(" ,m.orderUnit = :orderUnit ");
        sbQuery.append(" ,m.status = :status ");
        if (basicInfoUpdated) {
            sbQuery.append(" ,m.basicUpdateDate = :basicUpdateDate ");
            sbQuery.append(" ,m.basicUpdateUserUuid = :basicUpdateUserUuid ");
        }
        sbQuery.append(" ,m.updateDate = :updateDate ");
        sbQuery.append(" ,m.updateUserUuid = :updateUserUuid ");
        sbQuery.append(" WHERE m.id = :id ");
        Query query = entityManager.createQuery(sbQuery.toString());
        query.setParameter("storageCode", newRec.getStorageCode());
        query.setParameter("orderPoint", newRec.getOrderPoint());
        query.setParameter("orderUnit", newRec.getOrderUnit());
        query.setParameter("status", newRec.getStatus());
        if (basicInfoUpdated) {
            query.setParameter("basicUpdateDate", currentDate);
            query.setParameter("basicUpdateUserUuid", loginUser.getUserUuid());
        }
        query.setParameter("updateDate", currentDate);
        query.setParameter("updateUserUuid", loginUser.getUserUuid());
        query.setParameter("id", newRec.getId());
        query.executeUpdate();
    }

    @Transactional
    private void updateMoldPartStockAdjust(MoldPartStock oldRec, MoldPartStock newRec, String adjustReason, LoginUser loginUser) {
        boolean basicInfoUpdated = false;
        if ((oldRec.getStorageCode() == null && newRec.getStorageCode() != null) ||
                (oldRec.getStorageCode() != null && newRec.getStorageCode() == null) ||
                (oldRec.getStorageCode() != null && newRec.getStorageCode() != null && !oldRec.getStorageCode().equals(newRec.getStorageCode())) ||
                (oldRec.getOrderPoint() != newRec.getOrderPoint()) ||
                (oldRec.getOrderUnit() != newRec.getOrderUnit())) {
            basicInfoUpdated = true;
        }
        Date currentDate = new Date();
        updateMoldPartStockStatus(newRec);
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append(" UPDATE MoldPartStock m SET ");
        sbQuery.append(" m.storageCode = :storageCode ");
        sbQuery.append(" ,m.stock = :stock ");
        sbQuery.append(" ,m.usedStock = :usedStock ");
        sbQuery.append(" ,m.status = :status ");
        sbQuery.append(" ,m.orderPoint = :orderPoint ");
        sbQuery.append(" ,m.orderUnit = :orderUnit ");
        if (basicInfoUpdated) {
            sbQuery.append(" ,m.basicUpdateDate = :basicUpdateDate ");
            sbQuery.append(" ,m.basicUpdateUserUuid = :basicUpdateUserUuid ");
        }
        sbQuery.append(" ,m.updateDate = :updateDate ");
        sbQuery.append(" ,m.updateUserUuid = :updateUserUuid ");
        sbQuery.append(" WHERE m.id = :id ");
        Query query = entityManager.createQuery(sbQuery.toString());
        query.setParameter("storageCode", newRec.getStorageCode());
        query.setParameter("stock", newRec.getStock());
        query.setParameter("usedStock", newRec.getUsedStock());
        query.setParameter("status", newRec.getStatus());
        query.setParameter("orderPoint", newRec.getOrderPoint());
        query.setParameter("orderUnit", newRec.getOrderUnit());
        if (basicInfoUpdated) {
            query.setParameter("basicUpdateDate", currentDate);
            query.setParameter("basicUpdateUserUuid", loginUser.getUserUuid());
        }
        query.setParameter("updateDate", currentDate);
        query.setParameter("updateUserUuid", loginUser.getUserUuid());
        query.setParameter("id", newRec.getId());
        query.executeUpdate();
        // If stock / used stock is changed, make an in-out record.
        int stockDiff = newRec.getStock() - oldRec.getStock();
        int usedStockDiff = newRec.getUsedStock() - oldRec.getUsedStock();
        if (stockDiff == 0 && usedStockDiff == 0) return;
        MoldPartStockInOut inOut = new MoldPartStockInOut();
        inOut.setId(IDGenerator.generate());
        inOut.setMoldPartStockId(newRec.getId());
        inOut.setIoDate(currentDate);
        inOut.setIoEvent(MoldPartStockInOut.IoEvent.ADJUST);
        inOut.setAdjustReason(adjustReason);
        inOut.setNewStockIo(stockDiff);
        inOut.setUsedStockIo(usedStockDiff);
        inOut.setStock(newRec.getStock());
        inOut.setUsedStock(newRec.getUsedStock());
        inOut.setCreateUserUuid(loginUser.getUserUuid());
        inOut.setCreateDate(currentDate);
        inOut.setUpdateUserUuid(loginUser.getUserUuid());
        inOut.setUpdateDate(currentDate);
        entityManager.persist(inOut);
    }
    
    @Transactional
    private void createMoldPartStock(MoldPartStock moldPartStock, LoginUser loginUser) {
        Date currentDate = new java.util.Date();
        moldPartStock.setId(IDGenerator.generate());
        moldPartStock.setCreateUserUuid(loginUser.getUserUuid());
        moldPartStock.setCreateDate(currentDate);
        moldPartStock.setUpdateUserUuid(loginUser.getUserUuid());
        moldPartStock.setUpdateDate(currentDate);
        moldPartStock.setMold(null);
        moldPartStock.setMoldPart(null);
        updateMoldPartStockStatus(moldPartStock);
        entityManager.persist(moldPartStock);
        MoldPartStockInOut inOut = new MoldPartStockInOut();
        inOut.setId(IDGenerator.generate());
        inOut.setMoldPartStockId(moldPartStock.getId());
        inOut.setIoDate(currentDate);
        inOut.setIoEvent(MoldPartStockInOut.IoEvent.NEW);
        inOut.setNewStockIo(moldPartStock.getStock());
        inOut.setUsedStockIo(moldPartStock.getUsedStock());
        inOut.setStock(moldPartStock.getStock());
        inOut.setUsedStock(moldPartStock.getUsedStock());
        inOut.setCreateUserUuid(loginUser.getUserUuid());
        inOut.setCreateDate(currentDate);
        inOut.setUpdateUserUuid(loginUser.getUserUuid());
        inOut.setUpdateDate(currentDate);
        entityManager.persist(inOut);
    }
    
    public void updateMoldPartStockStatus(MoldPartStock moldPartStock) {
        if (moldPartStock.getStatus() != MoldPartStock.Status.DELI_WT) { //納品待ちのときは変えない
            if (moldPartStock.getStock() <= moldPartStock.getOrderPoint()) {
                moldPartStock.setStatus(MoldPartStock.Status.ORDER_REQ);
            }
            else {
                moldPartStock.setStatus(MoldPartStock.Status.NORMAL);
            }
        }
    }
    
    public MoldPartStockList getMoldPartStockListForPageView(
            int department, String moldId, String moldPartCode, String storageCode, List<Integer> statuses,  boolean deleted, int pageNumber, int pageSize)
    {
        MoldPartStockList moldPartStockList = new MoldPartStockList();
        if (statuses.size() <= 0 && !deleted) {
            return moldPartStockList;
        }
        Query countQuery = makeQuery(true, department, moldId, moldPartCode, storageCode, statuses, deleted, false, 0, 0, null);
        int recCount = ((Long)countQuery.getSingleResult()).intValue();
        Query recordQuery = makeQuery(false, department, moldId, moldPartCode, storageCode, statuses, deleted, false, pageNumber, pageSize, null);
        List<MoldPartStock> list = recordQuery.getResultList();
        moldPartStockList.setTotalCount(recCount);
        moldPartStockList.setMoldPartStocks(list);
        return moldPartStockList;
    }
    
    private Query makeQuery(
            boolean countQuery, int department, String moldId, String moldPartCode, String storageCode, List<Integer> statuses,  
            boolean deleted, boolean replacedByMe, int pageNumber, int pageSize, LoginUser loginUser
    ) {
        StringBuilder sbQuery = new StringBuilder();
        if (countQuery) {
            sbQuery.append("SELECT count(moldPartStock) FROM MoldPartStock moldPartStock WHERE 1 = 1 ");
        }
        else {
            sbQuery.append("SELECT moldPartStock FROM MoldPartStock moldPartStock WHERE 1 = 1 ");
        }
        if (department != 0) {
            sbQuery.append(" AND moldPartStock.mold.department = :department ");
        }
        if (moldId != null && !moldId.equals("")) {
            sbQuery.append(" AND moldPartStock.mold.moldId LIKE :moldId ");
        }
        if (moldPartCode != null && !moldPartCode.equals("")) {
            sbQuery.append(" AND moldPartStock.moldPart.moldPartCode LIKE :moldPartCode ");
        }
        if (storageCode != null && !storageCode.equals("")) {
            sbQuery.append(" AND moldPartStock.storageCode LIKE :storageCode ");
        }
        if (statuses != null && statuses.size() > 0) {
            sbQuery.append(" AND moldPartStock.status IN :statuses ");
        }
        if (deleted) {
            sbQuery.append(" AND moldPartStock.deleteFlg = 1 ");
        }
        else {
            sbQuery.append(" AND moldPartStock.deleteFlg = 0 ");
        }
        if (replacedByMe && loginUser != null) {
            sbQuery.append(" AND moldPartStock.moldMaintenance.updateUserUuid = :replaceUserUuid ");
        }
        sbQuery.append(" ORDER BY moldPartStock.mold.moldId, moldPartStock.storageCode, moldPartStock.moldPart.moldPartCode ");
        
        Query query = entityManager.createQuery(sbQuery.toString());
        if (department != 0) {
            query.setParameter("department", department);
        }
        if (moldId != null && !moldId.equals("")) {
            query.setParameter("moldId", "%" + moldId + "%");
        }
        if (moldPartCode != null && !moldPartCode.equals("")) {
            query.setParameter("moldPartCode", "%" + moldPartCode + "%");
        }
        if (storageCode != null && !storageCode.equals("")) {
            query.setParameter("storageCode", "%" + storageCode + "%");
        }
        if (statuses != null && statuses.size() > 0) {
            query.setParameter("statuses", statuses);
        }
        if (replacedByMe && loginUser != null) {
            query.setParameter("replaceUserUuid", loginUser.getUserUuid());
        }
        if (!countQuery && pageNumber > 0 && pageSize > 0) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }
        return query;
    }
    
    /**
     * 指定IDのMoldPartStockを取得する。<br>
     * その際、LockModeType.PESSIMISTIC_READの排他を取得する。
     * @param id
     * @return 
     */
    @Transactional
    public MoldPartStock getMoldPartStockWithLock(String id) {
        return entityManager.createNamedQuery("MoldPartStock.findById", MoldPartStock.class)
            .setParameter("id", id)
            .setLockMode(LockModeType.PESSIMISTIC_READ).setHint("Javax.persistence.lock.timeout", 1000).getResultList().stream().findFirst().orElse(null);
    }
    
    public MoldPartStock getMoldPartStockNoLock(String id) {
        return entityManager.createNamedQuery("MoldPartStock.findById", MoldPartStock.class)
            .setParameter("id", id)
            .getResultList().stream().findFirst().orElse(null);
    }

    /**
     * moldPartId, moldUuid指定のMoldPartStockを取得する。<br>
     * その際、LockModeType.PESSIMISTIC_READの排他を取得する。
     * @param moldPartId
     * @param moldUuid
     * @return 
     */
    public Optional<MoldPartStock> getMoldPartStockWithLock(String moldPartId, String moldUuid) {
        return entityManager.createNamedQuery("MoldPartStock.findByMoldPartId", MoldPartStock.class)
            .setParameter("moldPartId", moldPartId)
            .setParameter("moldUuid", moldUuid)
            .setLockMode(LockModeType.PESSIMISTIC_READ).setHint("Javax.persistence.lock.timeout", 1000).getResultList().stream().findFirst();
    }
    
    public Optional<MoldPartStock> getMoldPartStockNoLock(String moldPartId, String moldUuid) {
        return entityManager.createNamedQuery("MoldPartStock.findByMoldPartId", MoldPartStock.class)
            .setParameter("moldPartId", moldPartId)
            .setParameter("moldUuid", moldUuid)
            .getResultList().stream().findFirst();
    }

    public List<MoldPartStock> getOrderReqList(String maintid) {
        return entityManager.createNamedQuery("MoldPartStock.getOrderRequired", MoldPartStock.class)
            .setParameter("maintenanceId", maintid).getResultList();
    }
    
    public MoldPartStockList getMoldPartStockOrderNeeded(
        int department, String moldId, String moldPartCode, String storageCode, boolean replacedByMe, LoginUser loginUser
    ) {
        MoldPartStockList list = new MoldPartStockList();
        List<Integer> statuses = new ArrayList<>();
        statuses.add(MoldPartStock.Status.ORDER_REQ);
        Query query = makeQuery(false, department, moldId, moldPartCode, storageCode, statuses,  false, replacedByMe, 0, 0, loginUser);
        List<MoldPartStock> result = query.getResultList();
        list.setMoldPartStocks(result);
        list.setTotalCount(result.size());
        return list;
    }

    /**
     * 金型部品在庫のExcel帳票を作成します。<br>
     * ・テンプレートのStockListColumnの名称セルへの値の書き出し<br>
     * ・ページ毎の合計欄の追加<br>
     * を行います。<br>
     * StockListColumnは同一シート、同一行にある必要があります。<br>
     * @param department
     * @param isUsed
     * @return 
     */
    Workbook createStockListExcel(Integer department, boolean isUsed, String langId) {
        Workbook wb = getExcelTemplate("mold_part_stock.xlsx");
        int rowsInPage = 30, pageIdx = 0;
        Map<StockListColumn, Cell> nameCellMap = getNameCellMap(wb, Arrays.asList(StockListColumn.values()), StockListColumn.class);
        int rowIdx = nameCellMap.get(StockListColumn.STOCK_PLACE).getRowIndex();
        List<MoldPartStock> stocks = getStockListInDep(department);
        /** データ行数+ページ合計欄数(各ページ3行)*/
        int addedRowCnt = stocks.size() + ((stocks.size() / rowsInPage) * 3);
        Sheet sheet = nameCellMap.get(StockListColumn.STOCK_PLACE).getSheet();
        if (addedRowCnt > 0) {
            sheet.shiftRows(rowIdx + 1, sheet.getLastRowNum(), addedRowCnt, true, false);
        }
        
        for (List<MoldPartStock> listInPage : toPages(stocks, rowsInPage)) {
            for (MoldPartStock moldPartStock : listInPage) {
                for (Map.Entry<StockListColumn, Cell> entry : nameCellMap.entrySet()) {
                    StockListColumn column = entry.getKey();
                    /** 書き込み起点のNamedCell*/
                    Cell baseCell = entry.getValue();
                    Cell tgtCell = getTargetCell(baseCell, rowIdx);
                    writeCellValue(tgtCell, column.getValue(moldPartStock, isUsed));
                }
                rowIdx++;
            }
            rowIdx++;
            if(nameCellMap.containsKey(StockListColumn.TOTAL_PRICE)) {
                Cell baseCell = nameCellMap.get(StockListColumn.TOTAL_PRICE);
                writePageTotalCell(listInPage, baseCell, rowIdx, isUsed);
                rowIdx++;
            }
            if(pageIdx != stocks.size() / rowsInPage) {
                sheet.setRowBreak(rowIdx);
            }
            rowIdx++;
            pageIdx++;
        }
        
        getNamedCell("TOTAL_SUM", wb).ifPresent(cell->{
            cell.setCellValue(getTotalPrice(stocks, isUsed).doubleValue());
        });
        getNamedCell("DEPARTMENT", wb).ifPresent(cell->{
            String dep = choiceService.getChoiceVal("mst_user.department", department.toString(), langId);
            cell.setCellValue(dep);
        });
        return wb;
    }
    
    private Workbook getExcelTemplate(String fileName) {
        String templatePath = new StringBuilder(kartePropertyService.getDocumentDirectory())
            .append(File.separator).append("template").append(File.separator).append(fileName).toString();
        Workbook wb;
        try (FileInputStream fis = new FileInputStream(templatePath)) {
            wb = new XSSFWorkbook(fis);
        } catch (IOException ex) {throw new RuntimeException(ex);}
        return wb;
    }
    
    private List<MoldPartStock> getStockListInDep(Integer department) {
        return entityManager.createNamedQuery("MoldPartStock.findByDep", MoldPartStock.class)
            .setParameter("department", department).getResultList();
    }
    
    /**
     * 金型部品変更リストを作成する。<br>
     * データは"CREATE", "UPDATE", "DELETE"用のそれぞれのシートに出力される。<br>
     * それぞれのシートの書き込みセル名称にはChangeListColumnの名称にprefixが付く。
     * @param department
     * @param from
     * @param to
     * @param langId
     * @return 
     */
    Workbook createChangeListExcel(Integer department, Date from, Date to, String langId) {
        Workbook wb = getExcelTemplate("mold_part_change_list.xlsx");
        List<String> sheetTypes = Arrays.asList("CREATE", "UPDATE", "DELETE");
        
        for (String sheetType : sheetTypes) {
            Map<ChangeListColumn, Cell> nameCellMap = getNameCellMap(wb, Arrays.asList(ChangeListColumn.values()), ChangeListColumn.class, sheetType + "_");
            int rowIdx = nameCellMap.get(ChangeListColumn.STOCK_PLACE).getRowIndex();
            List<MoldPartStock> stocks = getChangeListInDep(department, from, to, sheetType);
            Sheet sheet = nameCellMap.get(ChangeListColumn.STOCK_PLACE).getSheet();
            if(stocks.size() > 0) {
                sheet.shiftRows(rowIdx + 1, sheet.getLastRowNum(), stocks.size(), true, false);
            }
            
            for (MoldPartStock moldPartStock : stocks) {
                for (Map.Entry<ChangeListColumn, Cell> entry : nameCellMap.entrySet()) {
                    ChangeListColumn column = entry.getKey();
                    Cell baseCell = entry.getValue();
                    Cell tgtCell = getTargetCell(baseCell, rowIdx);
                    writeCellValue(tgtCell, column.getValue(moldPartStock));
                }
                rowIdx++;
            }
            getNamedCell(sheetType + "_" +"DEPARTMENT", wb).ifPresent(cell->{
                String dep = choiceService.getChoiceVal("mst_user.department", department.toString(), langId);
                cell.setCellValue(dep);
            });
        }
        return wb;
    }
    
    private List<MoldPartStock> getChangeListInDep(Integer department, Date from, Date to, String sheetType) {
        return entityManager.createNamedQuery("MoldPartStock.getChangeList." + sheetType, MoldPartStock.class)
            .setParameter("department", department).setParameter("from", from).setParameter("to", to).getResultList();
    }
    
    /**
     * listをcountsInPage毎のリストに分割する。
     * @param list
     * @param countsInPage
     * @return 
     */
    private List<List<MoldPartStock>> toPages(List<MoldPartStock> list, int countsInPage) {
        List<List<MoldPartStock>> ret = new ArrayList<>();
        List<MoldPartStock> inPage = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            if(i % countsInPage == 0) {
                inPage = new ArrayList<>();
                ret.add(inPage);
            }
            inPage.add(list.get(i));
        }
        return ret;
    }
    
    private <T extends Enum> Map<T, Cell> getNameCellMap(Workbook wb, List<T> values, Class<T> cls) {
        return getNameCellMap(wb, values, cls, "");
    }
    
    /** wbからvaluesの名前のセルを取得し、Mapで返す。セル名称はEnum名_prefixとする。*/
    private <T extends Enum> Map<T, Cell> getNameCellMap(Workbook wb, List<T> values, Class<T> cls, String prefix) {
        Map<T, Cell> ret = new EnumMap<>(cls);
        values.forEach(col->{
            getNamedCell(prefix + col.name(), wb).ifPresent(cell->ret.put(col, cell));
        });
        return ret;
    }
    
    private void writeCellValue(Cell cell, Object val) {
        if(val instanceof String) {
            cell.setCellValue(val.toString());
        } else if(val instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal)val).doubleValue());
        } else if(val instanceof Integer) {
            cell.setCellValue(((Integer) val).doubleValue());
        }
    }
    
    private Optional<Cell> getNamedCell(String cellName, Workbook wb) {
        Name name = wb.getName(cellName);
        if(name == null) {
            return Optional.empty();
        }
        AreaReference aref = AreaReference.generateContiguous(name.getRefersToFormula())[0];
        CellReference cref = aref.getFirstCell();
        Sheet sheet = wb.getSheet(cref.getSheetName());
        Row row = sheet.getRow(cref.getRow());
        row = row == null ? sheet.createRow(cref.getRow()) : row;
        Cell cell = row.getCell(cref.getCol());
        return Optional.of(cell == null ? row.createCell(cref.getCol()) : cell);
    }
    
    private List<Cell> getNamedArea(String cellName, Workbook wb) {
        List<Cell> ret = new ArrayList<>();
        Name name = wb.getName(cellName);
        if(name == null) {
            return ret;
        }
        AreaReference aref = AreaReference.generateContiguous(name.getRefersToFormula())[0];
        ret = Arrays.asList(aref.getAllReferencedCells()).stream().map(cref->{
            Sheet sheet = wb.getSheet(cref.getSheetName());
            Row row = sheet.getRow(cref.getRow());
            row = row == null ? sheet.createRow(cref.getRow()) : row;
            Cell cell = row.getCell(cref.getCol());
            return cell == null ? row.createCell(cref.getCol()) : cell;
        }).collect(Collectors.toList());
        return ret;
    }
    
    private Cell getTargetCell(Cell baseCell, int rowidx) {
        Row row = baseCell.getSheet().getRow(rowidx);
        row = row == null ? baseCell.getSheet().createRow(rowidx) : row;
        Cell cell = row.getCell(baseCell.getColumnIndex());
        cell = cell == null ? row.createCell(baseCell.getColumnIndex()) : cell;
        cell.setCellStyle(baseCell.getCellStyle());
        return cell;
    }
    
    private void writePageTotalCell(List<MoldPartStock> listInPage, Cell baseCell, int rowIdx, boolean isUsed) {
        List<Cell> pageTotalArea = getNamedArea("PAGE_SUM", baseCell.getSheet().getWorkbook());
        Cell tgtCell = getTargetCell(baseCell, rowIdx);
        for (Cell cell : pageTotalArea) {
            Cell copyCell = tgtCell.getRow().getCell(cell.getColumnIndex());
            copyCell = copyCell == null ? tgtCell.getRow().createCell(cell.getColumnIndex()) : copyCell;
            copyCell.setCellValue(cell.getStringCellValue());
            copyCell.setCellStyle(cell.getCellStyle());
        }
        tgtCell.setCellValue(getTotalPrice(listInPage, isUsed).doubleValue());
    }
    
    private BigDecimal getTotalPrice(List<MoldPartStock> stocks, boolean isUsed) {
        return stocks.stream().map(stock->(BigDecimal)StockListColumn.TOTAL_PRICE.getValue(stock, isUsed))
            .reduce(BigDecimal.ZERO, (carried, elem)->carried.add(elem));
    }
    
    /**
     * return dictVal_depname_yyMMddHHmmss.xlsx in URL Encoded.
     * @param dictKey
     * @param department
     * @param langId
     * @return 
     */
    public String getDocName(String dictKey, Integer department, String langId) {
        String docName = mstDictionaryService.getDictionaryValue(langId, dictKey);
        String dep = choiceService.getChoiceVal("mst_user.department", department.toString(), langId);
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fname = docName + "_" + dep + "_" + date + ".xlsx";
        try {
            return URLEncoder.encode(fname, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException ex) {throw new RuntimeException(ex);}
    }
    
    private enum StockListColumn {
        STOCK_PLACE {
            @Override
            Object getValue(MoldPartStock mps, boolean isUsed) {
                return mps.getStorageCode();
            }
        },
        MOLD_NAME {
            @Override
            Object getValue(MoldPartStock mps, boolean isUsed) {
                return mps.getMold().getMoldName();
            }
        },
        DRAWING {
            @Override
            Object getValue(MoldPartStock mps, boolean isUsed) {
//                return 1 == mps.getMoldPart().getIntrExtProduct() ? mps.getMoldPart().getMoldPartCode() : "";
                return 0 == mps.getMoldPart().getIntrExtProduct() ? mps.getMoldPart().getModelNumber(): "";
            }
        },
        MOLD_PART_NAME {
            @Override
            Object getValue(MoldPartStock mps, boolean isUsed) {
                return mps.getMoldPart().getMoldPartName();
            }
        },
        TYPE_NUM {
            @Override
            Object getValue(MoldPartStock mps, boolean isUsed) {
//                return 1 == mps.getMoldPart().getIntrExtProduct() ? "" : mps.getMoldPart().getMoldPartCode();
                return 0 == mps.getMoldPart().getIntrExtProduct() ? "" : mps.getMoldPart().getModelNumber();
            }
        },
        UNIT_PRICE {
            @Override
            Object getValue(MoldPartStock mps, boolean isUsed) {
                return isUsed ? mps.getMoldPart().getUsedUnitPrice() : mps.getMoldPart().getUnitPrice();
            }
        },
        ORDER_POINT {
            @Override
            Object getValue(MoldPartStock mps, boolean isUsed) {
                return mps.getOrderPoint();
            }
        },
        ORDER_UNIT {
            @Override
            Object getValue(MoldPartStock mps, boolean isUsed) {
                return mps.getOrderUnit();
            }
        },
        STOCK {
            @Override
            Object getValue(MoldPartStock mps, boolean isUsed) {
                return isUsed ? mps.getUsedStock() : mps.getStock();
            }
        },
        TOTAL_PRICE {
            @Override
            Object getValue(MoldPartStock mps, boolean isUsed) {
                return isUsed ? mps.getMoldPart().getUsedUnitPrice().multiply(new BigDecimal(mps.getUsedStock())) : mps.getMoldPart().getUnitPrice().multiply(new BigDecimal(mps.getStock()));
            }
        };
        
        abstract Object getValue(MoldPartStock mps, boolean isUsed);
    }
    
    /** 
     * 変更リスト用列定義Enum。<br>
     * 対応するセル名称は登録用、変更用、削除用で、それぞれCREATE_、UPDATE_、DELETE_が接頭辞として付与される。
     */
    private enum ChangeListColumn {
        STOCK_PLACE {
            @Override
            Object getValue(MoldPartStock mps) {
                return mps.getStorageCode();
            }
        },
        MOLD_NAME {
            @Override
            Object getValue(MoldPartStock mps) {
                return mps.getMold().getMoldName();
            }
        },
        DRAWING {
            @Override
            Object getValue(MoldPartStock mps) {
//                return 1 == mps.getMoldPart().getIntrExtProduct() ? mps.getMoldPart().getMoldPartCode() : "";
                return 0 == mps.getMoldPart().getIntrExtProduct() ? mps.getMoldPart().getModelNumber(): "";
            }
        },
        MOLD_PART_NAME {
            @Override
            Object getValue(MoldPartStock mps) {
                return mps.getMoldPart().getMoldPartName();
            }
        },
        ORDER_POINT {
            @Override
            Object getValue(MoldPartStock mps) {
                return mps.getOrderPoint();
            }
        },
        ORDER_UNIT {
            @Override
            Object getValue(MoldPartStock mps) {
                return mps.getOrderUnit();
            }
        },
        TYPE_NUM {
            @Override
            Object getValue(MoldPartStock mps) {
//                return 1 == mps.getMoldPart().getIntrExtProduct() ? "" : mps.getMoldPart().getMoldPartCode();
                return 0 == mps.getMoldPart().getIntrExtProduct() ? "" : mps.getMoldPart().getModelNumber();
            }
        },
        CREATE_DATE {
            @Override
            Object getValue(MoldPartStock mps) {
                return FileUtil.dateFormat(mps.getCreateDate());
            }
        },
        CHANGE_DATE {
            @Override
            Object getValue(MoldPartStock mps) {
                return FileUtil.dateFormat(mps.getBasicUpdateDate());
            }
        },
        DELETE_DATE {
            @Override
            Object getValue(MoldPartStock mps) {
                return FileUtil.dateFormat(mps.getDeleteDate());
            }
        };
        abstract Object getValue(MoldPartStock mps);
    }
    
    public Workbook createMoldPartStockExcel(
        int department, String moldId, String moldPartCode, String storageCode, List<Integer> statuses,  boolean deleted, LoginUser loginUser
    ) {
        MoldPartStockList stockList = getMoldPartStockListForPageView(department, moldId, moldPartCode, storageCode, statuses, deleted, 0, 0);
        return moldPartStockExcelService.createMoldPartStockExcel(stockList, loginUser);
    }
    
    public String getMoldPartStockExcelName(LoginUser loginUser) {
        return moldPartStockExcelService.getFileName(loginUser);
    }
    
    public Workbook createExcelOrderNeeded(
        int department, String moldId, String moldPartCode, String storageCode, boolean replacedByMe, LoginUser loginUser
    ) {
        MoldPartStockList stockList = getMoldPartStockOrderNeeded(department, moldId, moldPartCode, storageCode, replacedByMe, loginUser);
        return orderNeededExcelService.createMoldPartStockExcel(stockList, loginUser);
    }
    
    public String getOrderNeededExcelName(LoginUser loginUser) {
        return orderNeededExcelService.getFileName(loginUser);
    }
    
    @Transactional
    public ImportResultResponse importMoldPartStockExcel(String fileUuid, String adjustReason, LoginUser loginUser) {
        ImportResultResponse response = new ImportResultResponse();
        String filePath = FileUtil.getExcelFilePath(kartePropertyService, fileUuid);
        try {
            String logFileUuid = IDGenerator.generate();//ログファイルUUID用意
            String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
            FileUtil fileUtil = new FileUtil();
            List<MoldPartStockExcelRecord> records = moldPartStockExcelService.getMoldPartStockAdjustFromExcel(filePath);
            int rowNum = 0;
            //List<String> logs = new ArrayList<>();
            response.setTotalCount(records.size());
            for (MoldPartStockExcelRecord rec: records) {
                rowNum++;
                MoldPartStockRes oldStockRes = new MoldPartStockRes();
                MoldPartStock newStock = new MoldPartStock();
                if (!checkExcelRecord(rowNum, rec, newStock, oldStockRes, logFile, loginUser, fileUtil)) {
                    response.setFailedCount(response.getFailedCount() + 1);
                    continue;
                }
                String dbProc;
                MoldPartStock oldStock = oldStockRes.getMoldPartStock();
                if (oldStock != null) {
                    //UPDATE
                    dbProc = moldPartStockExcelService.getMessage("msg_data_modified", loginUser);
                    newStock.setId(oldStock.getId());
                    newStock.setStatus(oldStock.getStatus());
                    updateMoldPartStockAdjust(oldStockRes.getMoldPartStock(), newStock, adjustReason, loginUser);
                    response.setUpdatedCount(response.getUpdatedCount() + 1);
                }
                else {
                    //INSERT
                    dbProc = moldPartStockExcelService.getMessage("msg_record_added", loginUser);
                    createMoldPartStock(newStock, loginUser);
                    response.setAddedCount(response.getAddedCount() + 1);
                }
                fileUtil.writeInfoToFile(logFile,
                    fileUtil.outValue(
                        moldPartStockExcelService.getMessage("row_number", loginUser),
                        rowNum, 
                        "[" + moldPartStockExcelService.getMessage("mold_id", loginUser) + "][" + 
                                moldPartStockExcelService.getMessage("mst_mold_part_mold_part_code", loginUser) + "]",
                        "[" + rec.getMoldId() + "][" + rec.getMoldPartCode() + "]",
                        moldPartStockExcelService.getMessage("error", loginUser),
                        0,
                        moldPartStockExcelService.getMessage("db_process", loginUser),
                        dbProc
                    ));
            }
            response.setLog(logFileUuid);
            //ログファイル情報をテーブルに保存
            TblCsvImport tblCsvImport = new TblCsvImport();
            tblCsvImport.setImportUuid(IDGenerator.generate());
            tblCsvImport.setImportUserUuid(loginUser.getUserUuid());
            tblCsvImport.setImportDate(new Date());
            tblCsvImport.setImportTable("tbl_mold_part_stock");
            TblUploadFile tblUploadFile = new TblUploadFile();
            tblUploadFile.setFileUuid(fileUuid);
            tblCsvImport.setUploadFileUuid(tblUploadFile);
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId("12400");
            tblCsvImport.setFunctionId(mstFunction);
            tblCsvImport.setRecordCount((int)response.getTotalCount());
            tblCsvImport.setSuceededCount((int)response.getSucceededCount());
            tblCsvImport.setAddedCount((int)response.getAddedCount());
            tblCsvImport.setUpdatedCount((int)response.getUpdatedCount());
            tblCsvImport.setDeletedCount((int)response.getDeletedCount());
            tblCsvImport.setFailedCount((int)response.getFailedCount());
            tblCsvImport.setLogFileUuid(logFileUuid);
            String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "tbl_mold_part_stock");
            tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));
            tblCsvImportService.createCsvImpor(tblCsvImport);
        }
        catch (IOException | InvalidFormatException | IllegalStateException io) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E901_OTHER);
            response.setErrorMessage(io.getMessage());
        }
        return response;
    }
    
    
    private boolean checkExcelRecord(int rowNum, MoldPartStockExcelRecord rec, MoldPartStock newStock, MoldPartStockRes oldStockRes, String logFile, LoginUser loginUser, FileUtil fileUtil) {
        String errorMsg = null, errorColumn = null, errorVal = null;
        if (rec.getMoldId() == null || rec.getMoldId().equals("")) {
            errorColumn = moldPartStockExcelService.getMessage("mold_id", loginUser);
            errorMsg =  moldPartStockExcelService.getMessage("msg_error_not_null_with_item", loginUser).replace("%s", errorColumn);
            errorVal = "";
        }
        else if (rec.getMoldPartCode() == null || rec.getMoldPartCode().equals("")) {
            errorColumn = moldPartStockExcelService.getMessage("mst_mold_part_mold_part_code", loginUser);
            errorMsg =  moldPartStockExcelService.getMessage("msg_error_not_null_with_item", loginUser).replace("%s", errorColumn);
            errorVal = "";
        }
        else if (rec.getStrStock() != null) {
            errorColumn = moldPartStockExcelService.getMessage("mold_part_stock_qty", loginUser);
            errorVal = rec.getStrStock();
            if (rec.getStrStock().equals("")) {
                errorMsg =  moldPartStockExcelService.getMessage("msg_error_not_null_with_item", loginUser).replace("%s", errorColumn);
            }
            else {
                errorMsg =  moldPartStockExcelService.getMessage("msg_error_not_isnumber", loginUser);
            }
        }
        else if (rec.getStrUsedStock() != null) {
            errorColumn = moldPartStockExcelService.getMessage("mold_part_used_stock_qty", loginUser);
            errorVal = rec.getStrUsedStock();
            if (rec.getStrUsedStock().equals("")) {
                errorMsg =  moldPartStockExcelService.getMessage("msg_error_not_null_with_item", loginUser).replace("%s", errorColumn);
            } else {
                errorMsg =  moldPartStockExcelService.getMessage("msg_error_not_isnumber", loginUser);
            }
        }
        else if (rec.getStrOrderPoint() != null) {
            errorColumn = moldPartStockExcelService.getMessage("mold_part_order_point", loginUser);
            errorVal = rec.getStrOrderPoint();
            if (rec.getStrOrderPoint().equals("")) {
                errorMsg =  moldPartStockExcelService.getMessage("msg_error_not_null_with_item", loginUser).replace("%s", errorColumn);
            } else {
                errorMsg =  moldPartStockExcelService.getMessage("msg_error_not_isnumber", loginUser);
            }
        }
        else if (rec.getStrOrderUnit() != null) {
            errorColumn = moldPartStockExcelService.getMessage("mold_part_order_unit", loginUser);
            errorVal = rec.getStrOrderUnit();
            if (rec.getStrOrderUnit().equals("")) {
                errorMsg =  moldPartStockExcelService.getMessage("msg_error_not_null_with_item", loginUser).replace("%s", errorColumn);
            } else {
                errorMsg =  moldPartStockExcelService.getMessage("msg_error_not_isnumber", loginUser);
            }
        }
        if (errorMsg == null) {
            MstMold mold = moldService.getMstMoldByMoldId(rec.getMoldId());
            if (mold == null) {
                errorColumn = moldPartStockExcelService.getMessage("mold_id", loginUser);
                errorMsg =  moldPartStockExcelService.getMessage("mst_error_record_not_found", loginUser);
                errorVal = rec.getMoldId();
            }
            else {
                newStock.setMoldUuid(mold.getUuid());
            }
        }
        if (errorMsg == null) {
            MstMoldPart moldPart = moldPartService.getMstMoldPartByCode(rec.getMoldPartCode());
            if (moldPart == null) {
                errorColumn = moldPartStockExcelService.getMessage("mst_mold_part_mold_part_code", loginUser);
                errorMsg =  moldPartStockExcelService.getMessage("mst_error_record_not_found", loginUser);
                errorVal = rec.getMoldPartCode();
            }
            else {
                newStock.setMoldPartId(moldPart.getId());
            }
        }
        if (errorMsg == null) {
            newStock.setStorageCode(rec.getStorageCode());
            newStock.setStock(rec.getStock());
            newStock.setUsedStock(rec.getUsedStock());
            newStock.setOrderPoint(rec.getOrderPoint());
            newStock.setOrderUnit(rec.getOrderUnit());
            MoldPartStock oldStock = getMoldPartStockWithLock(newStock.getMoldPartId(), newStock.getMoldUuid()).orElse(null);
            oldStockRes.setMoldPartStock(oldStock);
        }
        if (errorMsg != null) {
            fileUtil.writeInfoToFile(logFile,
                fileUtil.outValue(
                    moldPartStockExcelService.getMessage("row_number", loginUser),
                    rowNum, 
                    "[" + errorColumn + "]",
                    errorVal,
                    moldPartStockExcelService.getMessage("error", loginUser),
                    1,
                    moldPartStockExcelService.getMessage("error_detail", loginUser),
                    errorMsg
                ));
            return false;
        }
        return true;
    }
    
            
}
