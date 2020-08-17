package com.kmcj.karte.resources.component.inspection.result;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.component.inspection.result.model.ComponentInspectionItemResultDetail;
import com.kmcj.karte.resources.component.inspection.result.model.ComponentInspectionItemResultDetail.SamplingInspectionResult;
import com.kmcj.karte.resources.component.inspection.result.model.ComponentInspectionResultInfo;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.ZipCompressor;
import org.apache.poi.ss.usermodel.Cell;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Apeng
 */
public class TblComponentInspectionResultTemplate {
    private final static String ITEM_RESULT = "tbl_component_inspection_result_detail.item_result";
    private final static String INSP_RESULT = "tbl_component_inspection_result.result_type";
    private final static String COMPONENT_TYPE = "mst_component.component_type";

    public final static String INSPECT_RESULT = "inspect_result_";

    public final static String REPLACE_ZERO = "0+?$";

    public final static String REPLACE_POINT = "[.]$";
    
    /**
     * ファイル種類によりインステンスを取得
     *
     * @param filepath
     * @return
     * @throws IOException
     */
    private Workbook getWorkbookTypeByFile(String filepath) {
        Workbook workbook = null;
        File file = new File(filepath);
        BufferedInputStream in = null;

        try {
            if (file.exists()) {
                in = new BufferedInputStream(new FileInputStream(file));
            }

            if (filepath.endsWith("xls")) {
                workbook = new HSSFWorkbook(in);
            } else if (filepath.endsWith("xlsx")) {
                workbook = new XSSFWorkbook(in);
            }
        } catch (Exception e) {
            Logger.getLogger(TblComponentInspectionResultTemplate.class.getName()).log(Level.SEVERE, null, e);
        }
        return workbook;
    }

    /**
     * 検査結果ごとにテンプレート書き込み
     *
     * @param componentInspectionResultInfo
     * @param outputPath
     * @param inspectionType
     * @param choiceMap
     * @param docDirPath
     * @return
     * @throws IOException
     */
    public Response write(
            ComponentInspectionResultInfo componentInspectionResultInfo,
            String outputPath,
            Integer inspectionType,
            Map<String, String> choiceMap, 
            String docDirPath) throws IOException {
        int cavCnt = componentInspectionResultInfo.getCavityCnt();
        Response.ResponseBuilder response;
        if(cavCnt == 1) {
            Workbook workbook = getWorkbookTypeByFile(outputPath);
            if (workbook == null) {
                return Response.status(500).build();
            }
            saveInspectionResultListToExcelBook(workbook, componentInspectionResultInfo, inspectionType, choiceMap, componentInspectionResultInfo.getCavityStartNum());
            StreamingOutput stream = outputStream -> workbook.write(outputStream);
            response = Response.ok(stream);
        } else {
            String fileUuid = IDGenerator.generate();
            String zipDirPath = getInspRepDir(docDirPath) + File.separator + fileUuid;
            new File(zipDirPath).mkdirs();
            for(int i = 0; i < cavCnt; i ++) {
                // 検査結果ごとにループし依頼票ファイルを作成
                Workbook workbook = getWorkbookTypeByFile(outputPath);
                if (workbook == null) {
                    return Response.status(500).build();
                }
                int cavNo = componentInspectionResultInfo.getCavityStartNum() + i;
                saveInspectionResultListToExcelBook(workbook, componentInspectionResultInfo, inspectionType, choiceMap, cavNo);
                String fileNm = new StringBuilder(INSPECT_RESULT).append(componentInspectionResultInfo.getComponentCode()).append("_")
                    .append(componentInspectionResultInfo.getCavityPrefix()).append(String.valueOf(cavNo)).append(".xlsx").toString();
                workbook.write(new FileOutputStream(zipDirPath + File.separator + fileNm));
            }
            String zipFilePath = zipDirPath + File.separator + fileUuid + ".zip";
            ZipCompressor.zipDirectory(zipDirPath + File.separator, fileUuid, false);
            response = Response.ok(new File(zipFilePath));
        }
        try {
            //inspect_result_%部品コード%.xlsx
            StringBuilder fileName = new StringBuilder();
            fileName.append(INSPECT_RESULT);
            fileName.append(componentInspectionResultInfo.getComponentCode());
            String encodeStr = FileUtil.getEncod(fileName.toString());
            if (encodeStr != null) {
                encodeStr = encodeStr.replace("+", "%20");
            }
            String ext = cavCnt == 1 ? ".xlsx" : ".zip";
            response.header(CommonConstants.CONTENT_DISPOSITION, "attachment; filename=\"" + encodeStr + ext + "\"");
            return response.build();
        } catch (Exception e) {
            Logger.getLogger(TblComponentInspectionResultTemplate.class.getName()).log(Level.SEVERE, null, e);
            return Response.status(500).build();
        }
    }
    
    private String getInspRepDir(String docDirPath) {
        return new StringBuilder(docDirPath).append(File.separator)
            .append(CommonConstants.WORK).append(File.separator)
            .append(CommonConstants.INSPECTION_REPORT).toString();
    }

    private void saveInspectionResultListToExcelBook(
            Workbook workbook,
            ComponentInspectionResultInfo componentInspectionResultInfo,
            Integer inspectionType,
            Map<String, String> choiceMap,
            int cavNo
    ) {
        try {
            Name name;
            Sheet sheet = null;
            Row row;
            Cell cell;
            CellReference ref;
            //出荷検査した会社名
            name = workbook.getName("OUTGOING_COMPANY_NAME");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getOutgoingCompanyName()));
            }

            //出荷サプライヤコード
            name = workbook.getName("OUTGOING_COMPANY_CODE");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getOutgoingCompanyCode()));
            }

            //出荷承認者
            name = workbook.getName("OUTGOING_APPROVER");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getOutgoingInspectionApprovePersonName()));
            }

            //出荷測定担当者
            name = workbook.getName("OUTGOING_INSPECTION_PERSON");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getOutgoingInspectionPersonName()));
            }

            //出荷測定日
            name = workbook.getName("OUTGOING_INSPECTION_DATE");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getOutgoingInspectionDate()));
            }

            //出荷検査項結果
            name = workbook.getName("OUTGOING_INSPECTION_RESULT");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                if ("0".equals(getStringValue(componentInspectionResultInfo.getOutgoingInspectionResult()))) {
                    cell.setCellValue("");
                } else {
                    String itemResult = choiceMap.get(INSP_RESULT + getStringValue(componentInspectionResultInfo.getOutgoingInspectionResult()));
                    cell.setCellValue(itemResult);
                }
            }

            //出荷検査確認者
            name = workbook.getName("OUTGOING_CONFIRMER");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getOutgoingConfirmerName()));
            }

            //出荷検査確認日
            name = workbook.getName("OUTGOING_CONFIRM_DATE");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getOutgoingConfirmDate()));
            }

            //出荷検査承認日
            name = workbook.getName("OUTGOING_APPROVE_DATE");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getOutgoingInspectionApproveDate()));
            }

            //出荷検査コメント(公開)
            name = workbook.getName("OUTGOING_INSPECTION_COMMENT");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getOutgoingInspectionComment()));
            }
            //受入検査した会社名
            name = workbook.getName("INCOMING_COMPANY_NAME");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getIncomingCompanyName()));
            }

            //入荷サプライヤコード
            name = workbook.getName("INCOMING_COMPANY_CODE");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getIncomingCompanyCode()));
            }

            //入荷承認者
            name = workbook.getName("INCOMING_APPROVER");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getIncomingInspectionApprovePersonName()));
            }

            //入荷測定担当者
            name = workbook.getName("INCOMING_INSPECTION_PERSON");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getIncomingInspectionPersonName()));
            }

            //入荷測定日
            name = workbook.getName("INCOMING_INSPECTION_DATE");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getIncomingInspectionDate()));
            }

            //入荷検査項結果
            name = workbook.getName("INCOMING_INSPECTION_RESULT");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                String itemResult;
                if ("0".equals(getStringValue(componentInspectionResultInfo.getIncomingInspectionResult()))) {
                    cell.setCellValue("");
                } else {
                    itemResult = choiceMap.get(INSP_RESULT + getStringValue(componentInspectionResultInfo.getIncomingInspectionResult()));
                    cell.setCellValue(itemResult);
                }
            }

            //入荷検査確認者
            name = workbook.getName("INCOMING_CONFIRMER");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getIncomingConfirmerName()));
            }

            //入荷検査確認日
            name = workbook.getName("INCOMING_CONFIRM_DATE");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getIncomingConfirmDate()));
            }

            //入荷検査承認日
            name = workbook.getName("INCOMING_APPROVE_DATE");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getIncomingInspectionApproveDate()));
            }

            //入荷検査コメント(公開)
            name = workbook.getName("INCOMING_INSPECTION_COMMENT");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getIncomingInspectionComment()));
            }

            //部品番号
            name = workbook.getName("PART_CODE");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getComponentCode()));
            }

            //部品名称
            name = workbook.getName("PART_NAME");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getComponentName()));
            }

            //製造ロット
            name = workbook.getName("LOT_NUM");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getProductionLotNum()));
            }

            //INSPECTION_ID
            name = workbook.getName("INSPECTION_ID");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getComponentInspectionResultId()));
            }

            //MATERIAL_01
            name = workbook.getName("MATERIAL_01");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getMaterial01()));
            }

            //MATERIAL_02
            name = workbook.getName("MATERIAL_02");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getMaterial02()));
            }

            //MATERIAL_03
            name = workbook.getName("MATERIAL_03");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getMaterial03()));
            }

            //PO_NUMBER
            name = workbook.getName("PO_NUMBER");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                if (StringUtils.isNotEmpty(componentInspectionResultInfo.getPoNumber())) {
                    String[] poNumberArray = componentInspectionResultInfo.getPoNumber().split(",");
                    for (int i = 0; i < poNumberArray.length; i++) {
                        if (i == 0) {
                            row = sheet.getRow(ref.getRow());
                        } else {
                            row = sheet.getRow(ref.getRow() + i);
                        }
                        cell = row.getCell(ref.getCol());
                        if (cell == null) {
                            cell = row.createCell(ref.getCol());
                        }
                        cell.setCellValue(getStringValue(poNumberArray[i]));
                    }
                }
            }

            //検査区分
            name = workbook.getName("INSPECT_CLASS");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(componentInspectionResultInfo.getFirstFlagName());
            }

            //部品種類
            name = workbook.getName("PART_TYPE");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                if ("0".equals(getStringValue(componentInspectionResultInfo.getComponentType()))) {
                    cell.setCellValue("");
                } else {
                    String itemResult = choiceMap.get(COMPONENT_TYPE + getStringValue(componentInspectionResultInfo.getComponentType()));
                    cell.setCellValue(itemResult);
                }
            }

            //数量
            name = workbook.getName("QUANTITY");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getQuantity()));
            }

            //CAV数
            name = workbook.getName("CAVITY_CNT");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getCavityCnt()));
            }

            //開始CAV位置
            name = workbook.getName("CAVITY_START_NUM");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getCavityStartNum()));
            }

            //CAV番号接頭字
            name = workbook.getName("CAVITY_PREFIX");
            if (name != null) {
                ref = new CellReference(name.getRefersToFormula());
                sheet = workbook.getSheet(ref.getSheetName());
                row = sheet.getRow(ref.getRow());
                cell = row.getCell(ref.getCol());
                if (cell == null) {
                    cell = row.createCell(ref.getCol());
                }
                cell.setCellValue(getStringValue(componentInspectionResultInfo.getCavityPrefix()));
            }
            
            getNamedCell("CAVITY_NUM", workbook).ifPresent(cavNmCell -> {
              cavNmCell.setCellValue(cavNo);
            });

            List<ComponentInspectionItemResultDetail> measureResultDetails;
            List<ComponentInspectionItemResultDetail> visualResultDetails;
            List<ComponentInspectionItemResultDetail> componentInspectionItemResultDetails = new ArrayList<>();
            int measSamplingQuantity = 0;
            int visualSamplingQuantity = 0;
            int samplingQuantity = 0;
            if (CommonConstants.INSPECTION_TYPE_OUTGOING == inspectionType) {
                measureResultDetails = componentInspectionResultInfo.getOutgoingMeasureResultDetails();
                visualResultDetails = componentInspectionResultInfo.getOutgoingVisualResultDetails();
                measSamplingQuantity = componentInspectionResultInfo.getOutgoingMeasSamplingQuantity();
                visualSamplingQuantity = componentInspectionResultInfo.getOutgoingVisualSamplingQuantity();
            } else {
                measureResultDetails = componentInspectionResultInfo.getIncomingMeasureResultDetails();
                visualResultDetails = componentInspectionResultInfo.getIncomingVisualResultDetails();
                measSamplingQuantity = componentInspectionResultInfo.getIncomingMeasSamplingQuantity();
                visualSamplingQuantity = componentInspectionResultInfo.getIncomingVisualSamplingQuantity();
            }
            if(visualSamplingQuantity <= measSamplingQuantity){
                samplingQuantity = measSamplingQuantity;
            }else{
                samplingQuantity = visualSamplingQuantity;
            }
            if (measureResultDetails != null) {
                componentInspectionItemResultDetails.addAll(measureResultDetails);
            }
            if (visualResultDetails != null) {
                componentInspectionItemResultDetails.addAll(visualResultDetails);
            }
            if (componentInspectionItemResultDetails.size() > 0) {
                setInspectionMeasureResultDetail(workbook, choiceMap, componentInspectionItemResultDetails, samplingQuantity, cavNo);
            }

            //method to force the change Sheet to execute the formula
            sheet.setForceFormulaRecalculation(true);
        } catch (Exception e) {
            Logger.getLogger(TblComponentInspectionResultTemplate.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     *
     * @param obj
     * @return
     */
    private String getStringValue(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return String.valueOf(obj);
        }
    }

    private void setInspectionMeasureResultDetail(
            Workbook workbook,
            Map<String, String> choiceMap,
            List<ComponentInspectionItemResultDetail> componentInspectionItemResultDetails,
            int samplingQuantity,
            int cavNo) {
        ComponentInspectionItemResultDetail componentInspectionItemResultDetail;
        int index;
        Sheet sheet;
        Name name;
        Name seqName;
        Row row;
        Cell cell;
        for (int i = 0; i < componentInspectionItemResultDetails.size(); i++) {
            try {
                index = i + 1;
                componentInspectionItemResultDetail = componentInspectionItemResultDetails.get(i);
                //図面記載No
                name = workbook.getName("DRAWING_NO_" + index);
                if (name != null) {
                    CellReference refDRAWING_NO = new CellReference(name.getRefersToFormula());
                    sheet = workbook.getSheet(refDRAWING_NO.getSheetName());
                    row = sheet.getRow(refDRAWING_NO.getRow());
                    cell = row.getCell(refDRAWING_NO.getCol());
                    if (cell == null) {
                        cell = row.createCell(refDRAWING_NO.getCol());
                    }
                    cell.setCellValue(getStringValue(componentInspectionItemResultDetail.getDrawingMentionNo()));
                }

                //同項目複数個所
                name = workbook.getName("SIMILAR_MULTIITEM_" + index);
                if (name != null) {
                    CellReference refSIMILAR_MULTIITEM = new CellReference(name.getRefersToFormula());
                    sheet = workbook.getSheet(refSIMILAR_MULTIITEM.getSheetName());
                    row = sheet.getRow(refSIMILAR_MULTIITEM.getRow());
                    cell = row.getCell(refSIMILAR_MULTIITEM.getCol());
                    if (cell == null) {
                        cell = row.createCell(refSIMILAR_MULTIITEM.getCol());
                    }
                    cell.setCellValue(getStringValue(componentInspectionItemResultDetail.getSimilarMultiitem()));
                }

                //検査項目
                name = workbook.getName("ITEM_NAME_" + index);
                if (name != null) {
                    CellReference refITEM_NAME = new CellReference(name.getRefersToFormula());
                    sheet = workbook.getSheet(refITEM_NAME.getSheetName());
                    row = sheet.getRow(refITEM_NAME.getRow());
                    cell = row.getCell(refITEM_NAME.getCol());
                    if (cell == null) {
                        cell = row.createCell(refITEM_NAME.getCol());
                    }
                    cell.setCellValue(getStringValue(componentInspectionItemResultDetail.getInspectionItemName()));
                }

                BigDecimal seqMeasurementResult;
                int seqVisualResult;
                double sumResult = 0;
                List<String> listData = new ArrayList();

                if (CommonConstants.MEASUREMENT_TYPE_MEASURE == componentInspectionItemResultDetail.getMeasurementType()) {// 測定
                    Double averageResult = null;
                    String maxNum = null;
                    String minNum = null;
                    //規格値
                    name = workbook.getName("DIMENSION_VALUE_" + index);
                    if (name != null) {
                        CellReference refDIMENSION_VALUE = new CellReference(name.getRefersToFormula());
                        sheet = workbook.getSheet(refDIMENSION_VALUE.getSheetName());
                        row = sheet.getRow(refDIMENSION_VALUE.getRow());
                        cell = row.getCell(refDIMENSION_VALUE.getCol());
                        if (cell == null) {
                            cell = row.createCell(refDIMENSION_VALUE.getCol());
                        }
                        cell.setCellValue(componentInspectionItemResultDetail.getDimensionValue().doubleValue());
                    }

                    //公差正
                    name = workbook.getName("TOLERANCE_PLUS_" + index);
                    if (name != null) {
                        CellReference refTOLERANCE_PLUS = new CellReference(name.getRefersToFormula());
                        sheet = workbook.getSheet(refTOLERANCE_PLUS.getSheetName());
                        row = sheet.getRow(refTOLERANCE_PLUS.getRow());
                        cell = row.getCell(refTOLERANCE_PLUS.getCol());
                        if (cell == null) {
                            cell = row.createCell(refTOLERANCE_PLUS.getCol());
                        }
                        cell.setCellValue(componentInspectionItemResultDetail.getTolerancePlus().doubleValue());
                    }

                    //公差負
                    name = workbook.getName("TOLERANCE_MINUS_" + index);
                    if (name != null) {
                        CellReference refTOLERANCE_MINUS = new CellReference(name.getRefersToFormula());
                        sheet = workbook.getSheet(refTOLERANCE_MINUS.getSheetName());
                        row = sheet.getRow(refTOLERANCE_MINUS.getRow());
                        cell = row.getCell(refTOLERANCE_MINUS.getCol());
                        if (cell == null) {
                            cell = row.createCell(refTOLERANCE_MINUS.getCol());
                        }
                        cell.setCellValue(componentInspectionItemResultDetail.getToleranceMinus().doubleValue());
                    }

                    // SEQを挿入する
                    seqName = workbook.getName("DETAIL_SEQ");
                    if (null != seqName) {
                        CellReference refDETAIL_SEQ = new CellReference(seqName.getRefersToFormula());
                        int startSeqRowNum = refDETAIL_SEQ.getRow();
                        sheet = workbook.getSheet(refDETAIL_SEQ.getSheetName());
                        row = sheet.getRow(startSeqRowNum);
                        cell = row.getCell((int) refDETAIL_SEQ.getCol());
                        if (null == cell) {
                            int num = 0;
                            for (int s = 1; s <= samplingQuantity; s++) {
                                //SEQ
                                row = sheet.getRow(startSeqRowNum + num);
                                row = row == null ? sheet.createRow(startSeqRowNum + num) : row;
                                cell = row.getCell((int) refDETAIL_SEQ.getCol());
                                if (cell == null) {
                                    cell = row.createCell(refDETAIL_SEQ.getCol());
                                }
                                cell.setCellValue(s);
                                num++;
                            }
                        }
                    }

                    //測定值
                    name = workbook.getName("SAMPLE_RESULT_" + index);
                    if (name != null) {
                        CellReference refSAMPLE_RESULT = new CellReference(name.getRefersToFormula());
                        sheet = workbook.getSheet(refSAMPLE_RESULT.getSheetName());
                        int startRowNum = refSAMPLE_RESULT.getRow();

                        // 一行を挿入する
                        List<SamplingInspectionResult> list = componentInspectionItemResultDetail.getSamplingInspectionResults()
                            .stream().filter(detail -> detail.getCavityNum() == cavNo).collect(Collectors.toList());
                        if (list != null && list.size() > 0) {
                            for (int rowNum = 0; rowNum < list.size(); rowNum++) {
                                SamplingInspectionResult result = list.get(rowNum);
                                if(result.getSeqMeasurementResult() != null) {
                                    seqMeasurementResult = result.getSeqMeasurementResult();
                                    sumResult += seqMeasurementResult.doubleValue();
                                    //测定值
                                    row = sheet.getRow(startRowNum + rowNum);
                                    row = row == null ? sheet.createRow(startRowNum + rowNum) : row;
                                    cell = row.getCell((int) refSAMPLE_RESULT.getCol());
                                    if (cell == null) {
                                        cell = row.createCell(refSAMPLE_RESULT.getCol());
                                    }
                                    cell.setCellValue(seqMeasurementResult.doubleValue());
                                }
                            }
                            long inputedCnt = list.stream().filter(result->result.getSeqMeasurementResult() != null).count();
                            averageResult = inputedCnt != 0 ? sumResult / inputedCnt : null;
                        }
                    }
                    if (listData.size() > 0) {
                        listData.sort(Comparator.comparing(BigDecimal::new));
                        minNum = listData.get(0);
                        maxNum = listData.get(listData.size() - 1);
                    }
                    //最大
                    name = workbook.getName("MAX_" + index);
                    if (name != null) {
                        CellReference refMAX = new CellReference(name.getRefersToFormula());
                        sheet = workbook.getSheet(refMAX.getSheetName());
                        row = sheet.getRow(refMAX.getRow());
                        cell = row.getCell(refMAX.getCol());
                        if (cell == null) {
                            cell = row.createCell(refMAX.getCol());
                        }
                        cell.setCellValue(getStringValue(maxNum));
                    }

                    //最小
                    name = workbook.getName("MIN_" + index);
                    if (name != null) {
                        CellReference refMIN = new CellReference(name.getRefersToFormula());
                        sheet = workbook.getSheet(refMIN.getSheetName());
                        row = sheet.getRow(refMIN.getRow());
                        cell = row.getCell(refMIN.getCol());
                        if (cell == null) {
                            cell = row.createCell(refMIN.getCol());
                        }
                        cell.setCellValue(getStringValue(minNum));
                    }
                    
                    //平均値
                    name = workbook.getName("AVERAGE_" + index);
                    if (name != null) {
                        CellReference refAVERAGE = new CellReference(name.getRefersToFormula());
                        sheet = workbook.getSheet(refAVERAGE.getSheetName());
                        row = sheet.getRow(refAVERAGE.getRow());
                        cell = row.getCell(refAVERAGE.getCol());
                        if (cell == null) {
                            cell = row.createCell(refAVERAGE.getCol());
                        }
                        if(averageResult != null) {
                            cell.setCellValue(averageResult);
                        }
                    }

                } else if(CommonConstants.MEASUREMENT_TYPE_VISUAL == componentInspectionItemResultDetail.getMeasurementType()) {// 目視
                    // SEQを挿入する
                    seqName = workbook.getName("DETAIL_SEQ");
                    if (null != seqName) {
                        CellReference refDETAIL_SEQ = new CellReference(seqName.getRefersToFormula());
                        int startSeqRowNum = refDETAIL_SEQ.getRow();
                        sheet = workbook.getSheet(refDETAIL_SEQ.getSheetName());
                        row = sheet.getRow(startSeqRowNum);
                        cell = row.getCell((int) refDETAIL_SEQ.getCol());
                        if (null == cell) {
                            int num = 0;
                            for (int s = 1; s <= samplingQuantity; s++) {
                                //SEQ
                                row = sheet.getRow(startSeqRowNum + num);
                                cell = row.getCell((int) refDETAIL_SEQ.getCol());
                                if (cell == null) {
                                    cell = row.createCell(refDETAIL_SEQ.getCol());
                                }
                                cell.setCellValue(s);
                                num++;
                            }
                        }
                    }

                    name = workbook.getName("SAMPLE_RESULT_" + index);
                    if (name != null) {
                        CellReference refSAMPLE_RESULT = new CellReference(name.getRefersToFormula());
                        sheet = workbook.getSheet(refSAMPLE_RESULT.getSheetName());
                        int startRowNum = refSAMPLE_RESULT.getRow();

                        // 一行を挿入する
                        List<SamplingInspectionResult> list = componentInspectionItemResultDetail.getSamplingInspectionResults()
                            .stream().filter(detail -> detail.getCavityNum() == cavNo).collect(Collectors.toList());
                        if (list != null && list.size() > 0) {
                            for (int rowNum = 0; rowNum < list.size(); rowNum++) {
                                SamplingInspectionResult result = list.get(rowNum);
                                //测定值
                                seqVisualResult = result.getSeqVisualResult();
                                row = sheet.getRow(startRowNum + rowNum);
                                row = row == null ? sheet.createRow(startRowNum + rowNum) : row;
                                cell = row.getCell((int) refSAMPLE_RESULT.getCol());
                                if (cell == null) {
                                    cell = row.createCell(refSAMPLE_RESULT.getCol());
                                }
                                if ("0".equals(getStringValue(seqVisualResult))) {
                                    cell.setCellValue("");
                                } else {
                                    String itemResult = choiceMap.get(ITEM_RESULT + getStringValue(seqVisualResult));
                                    cell.setCellValue(itemResult);
                                }
                            }
                        }
                    }
                }

                //測定器
                name = workbook.getName("MEASUREMENT_METHOD_" + index);
                if (name != null) {
                    CellReference refMEASUREMENT_METHOD = new CellReference(name.getRefersToFormula());
                    sheet = workbook.getSheet(refMEASUREMENT_METHOD.getSheetName());
                    row = sheet.getRow(refMEASUREMENT_METHOD.getRow());
                    cell = row.getCell(refMEASUREMENT_METHOD.getCol());
                    if (cell == null) {
                        cell = row.createCell(refMEASUREMENT_METHOD.getCol());
                    }
                    cell.setCellValue(getStringValue(componentInspectionItemResultDetail.getMeasurementMethod()));
                }

                //判定
                name = workbook.getName("RESULT_" + index);
                if (name != null) {
                    CellReference refITEM_RESULT = new CellReference(name.getRefersToFormula());
                    sheet = workbook.getSheet(refITEM_RESULT.getSheetName());
                    row = sheet.getRow(refITEM_RESULT.getRow());
                    cell = row.getCell(refITEM_RESULT.getCol());
                    if (cell == null) {
                        cell = row.createCell(refITEM_RESULT.getCol());
                    }
                    if ("0".equals(getStringValue(componentInspectionItemResultDetail.getItemResult()))) {
                        cell.setCellValue("");
                    } else {
                        String itemResult = choiceMap.get(ITEM_RESULT + getStringValue(componentInspectionItemResultDetail.getItemResult()));
                        cell.setCellValue(itemResult);
                    }
                }

                //改訂記号
                name = workbook.getName("REVISION_" + index);
                if (name != null) {
                    CellReference refREVISION = new CellReference(name.getRefersToFormula());
                    sheet = workbook.getSheet(refREVISION.getSheetName());
                    row = sheet.getRow(refREVISION.getRow());
                    cell = row.getCell(refREVISION.getCol());
                    if (cell == null) {
                        cell = row.createCell(refREVISION.getCol());
                    }
                    cell.setCellValue(getStringValue(componentInspectionItemResultDetail.getRevisionSymbol()));
                }

                //図面頁
                name = workbook.getName("DRAWING_PAGE_" + index);
                if (name != null) {
                    CellReference refDRAWING_PAGE = new CellReference(name.getRefersToFormula());
                    sheet = workbook.getSheet(refDRAWING_PAGE.getSheetName());
                    row = sheet.getRow(refDRAWING_PAGE.getRow());
                    cell = row.getCell(refDRAWING_PAGE.getCol());
                    if (cell == null) {
                        cell = row.createCell(refDRAWING_PAGE.getCol());
                    }
                    cell.setCellValue(getStringValue(componentInspectionItemResultDetail.getDrawingPage()));
                }

                //図面注記(N)
                name = workbook.getName("DRAWING_ANNOTATION_" + index);
                if (name != null) {
                    CellReference refDRAWING_ANNOTATION = new CellReference(name.getRefersToFormula());
                    sheet = workbook.getSheet(refDRAWING_ANNOTATION.getSheetName());
                    row = sheet.getRow(refDRAWING_ANNOTATION.getRow());
                    cell = row.getCell(refDRAWING_ANNOTATION.getCol());
                    if (cell == null) {
                        cell = row.createCell(refDRAWING_ANNOTATION.getCol());
                    }
                    cell.setCellValue(getStringValue(componentInspectionItemResultDetail.getDrawingAnnotation()));
                }

                //図面エリア
                name = workbook.getName("DRAWING_AREA_" + index);
                if (name != null) {
                    CellReference refDRAWING_AREA = new CellReference(name.getRefersToFormula());
                    sheet = workbook.getSheet(refDRAWING_AREA.getSheetName());
                    row = sheet.getRow(refDRAWING_AREA.getRow());
                    cell = row.getCell(refDRAWING_AREA.getCol());
                    if (cell == null) {
                        cell = row.createCell(refDRAWING_AREA.getCol());
                    }
                    cell.setCellValue(getStringValue(componentInspectionItemResultDetail.getDrawingArea()));
                }

                //PQS
                name = workbook.getName("PQS_" + index);
                if (name != null) {
                    CellReference refPQS = new CellReference(name.getRefersToFormula());
                    sheet = workbook.getSheet(refPQS.getSheetName());
                    row = sheet.getRow(refPQS.getRow());
                    cell = row.getCell(refPQS.getCol());
                    if (cell == null) {
                        cell = row.createCell(refPQS.getCol());
                    }
                    cell.setCellValue(getStringValue(componentInspectionItemResultDetail.getPqs()));
                }

                //注記
                name = workbook.getName("NOTE_" + index);
                if (name != null) {
                    CellReference refNOTE = new CellReference(name.getRefersToFormula());
                    sheet = workbook.getSheet(refNOTE.getSheetName());
                    row = sheet.getRow(refNOTE.getRow());
                    cell = row.getCell(refNOTE.getCol());
                    if (cell == null) {
                        cell = row.createCell(refNOTE.getCol());
                    }
                    cell.setCellValue(getStringValue(componentInspectionItemResultDetail.getNote()));
                }

                //手動判定
                name = workbook.getName("MANUAL_RESULT_" + index);
                if (name != null) {
                    CellReference refMANUAL_RESULT = new CellReference(name.getRefersToFormula());
                    sheet = workbook.getSheet(refMANUAL_RESULT.getSheetName());
                    row = sheet.getRow(refMANUAL_RESULT.getRow());
                    cell = row.getCell(refMANUAL_RESULT.getCol());
                    if (cell == null) {
                        cell = row.createCell(refMANUAL_RESULT.getCol());
                    }
                    if ("0".equals(getStringValue(componentInspectionItemResultDetail.getItemResultManual()))) {
                        cell.setCellValue("");
                    } else {
                        String itemResult = choiceMap.get(ITEM_RESULT + getStringValue(componentInspectionItemResultDetail.getItemResultManual()));
                        cell.setCellValue(itemResult);
                    }
                }

                //手動判定コメント
                name = workbook.getName("MANUAL_COMMENT_" + index);
                if (name != null) {
                    CellReference refMANUAL_COMMENT = new CellReference(name.getRefersToFormula());
                    sheet = workbook.getSheet(refMANUAL_COMMENT.getSheetName());
                    row = sheet.getRow(refMANUAL_COMMENT.getRow());
                    cell = row.getCell(refMANUAL_COMMENT.getCol());
                    if (cell == null) {
                        cell = row.createCell(refMANUAL_COMMENT.getCol());
                    }
                    cell.setCellValue(getStringValue(componentInspectionItemResultDetail.getManJudgeComment()));
                }

            } catch (Exception e) {
                Logger.getLogger(TblComponentInspectionResultTemplate.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    /**
     * zip write
     *
     * @param componentInspectionResultInfo
     * @param outputPath
     * @param inspectionType
     * @param choiceMap
     * @throws IOException
     */
    public void writeExcel(
            ComponentInspectionResultInfo componentInspectionResultInfo,
            String outputPath,
            Integer inspectionType,
            Map<String, String> choiceMap) throws IOException {

        int cavCnt = componentInspectionResultInfo.getCavityCnt();
        for(int i = 0; i < cavCnt; i ++) {
            Workbook workbook = getWorkbookTypeByFile(outputPath);
            if (workbook == null) {
                return;
            }
            int cavNo = componentInspectionResultInfo.getCavityStartNum() + i;
            saveInspectionResultListToExcelBook(workbook, componentInspectionResultInfo, inspectionType, choiceMap, cavNo);
            if(cavCnt == 1) {
                workbook.write(new FileOutputStream(outputPath));
            } else {
                workbook.write(new FileOutputStream(outputPath.replace(".xlsx", "_" + componentInspectionResultInfo.getCavityPrefix() + String.valueOf(cavNo) + ".xlsx")));
            }
        }
        if(cavCnt != 1) {
            new File(outputPath).delete();
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
}
