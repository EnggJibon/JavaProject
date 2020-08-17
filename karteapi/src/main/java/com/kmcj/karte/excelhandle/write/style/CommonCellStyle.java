package com.kmcj.karte.excelhandle.write.style;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
/**
 * 
 * @author Apeng
 */
public class CommonCellStyle extends MyCellStyle {
//    private final static String FONT_NAME = "ＭＳ Ｐゴシック";
    public CommonCellStyle(Workbook workbook) {
        super(workbook);
    }
    
    //生成一般样式
    @Override
    public CellStyle createCommonCellStyle() {
        CellStyle cellStyle = workbook.createCellStyle();
//		cellStyle.setBorderBottom(BorderStyle.THIN);
//		cellStyle.setBorderLeft(BorderStyle.THIN);
//		cellStyle.setBorderTop(BorderStyle.THIN);
//		cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);
//        Font font = workbook.createFont();
//        font.setFontName(FONT_NAME);
//        font.setFontHeightInPoints((short) 11);
//        cellStyle.setFont(font);
        return cellStyle;
    }
    
    //生成一般数字样式
    @Override
    public CellStyle createCommonNumberCellStyle() {
        CellStyle cellStyle = createCommonCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        DataFormat  format = this.workbook.createDataFormat();
        cellStyle.setDataFormat(format.getFormat("#,##"));
        return cellStyle;
    }
    
    //生成一般文字样式
    @Override
    public CellStyle createCommonTextCellStyle() {
        CellStyle cellStyle = createCommonCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        DataFormat  format = this.workbook.createDataFormat();
        cellStyle.setDataFormat(format.getFormat("@"));
        return cellStyle;
    }
    
    //生成一般日期样式
    @Override
    public CellStyle createCommonDateCellStyle() {
        CellStyle cellStyle = createCommonCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        DataFormat  format = this.workbook.createDataFormat();
        cellStyle.setDataFormat(format.getFormat("yyyy/mm/dd"));
        return cellStyle;
    }
    
    //生成一般列头样式
    @Override
    public CellStyle createCommonHeaderCellStyler() {
        CellStyle cellStyle = createCommonCellStyle();
//        Font font = workbook.createFont();
//        font.setFontName(FONT_NAME);
//        font.setFontHeightInPoints((short) 12);
//        font.setBold(true);
//        cellStyle.setFont(font);
        return cellStyle;
    }

}
