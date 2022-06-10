/**
 * 外籍(FOREIGN)人士每日通關統計分析表(多閘道合併)
 */
package com.mitac.egate.parser.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import com.mitac.egate.parser.bean.PassengerAnalysis;
import com.mitac.egate.parser.bean.PassengerLog;
import com.mitac.egate.parser.utils.CommonUtils;
import com.mitac.egate.parser.utils.ScPoiUtil;

public class LogToExcel_R1F51 implements AutoCloseable {
//	private static final int COLUMNS = 34;
	
	private static final String[] columnName0 = new String[] {// 合併ColumnName欄位相同區塊的title
			"日期",
			"通關總筆數及比例", "", "", "", "", 
			"成功通關秒數分析(A+C+E12+E22+F)", "", "", "",
			"成功通關秒數分析(A+B+C+D+E11+E12+E21+E22+F)","", "", ""
	};
	
	private static final String[] columnName1 = new String[] { //與ColumnName0 欄位互相對應, ColumnName0 是合併ColumnName的區塊
			"", //日期
			"合計筆數", "不成功", "", "成功", "", //通關總筆數及比例
			"", "", "", "", //成功通關秒數分析(A+C+E)
			"", "", "", "" //成功通關秒數分析(A+B+C+D+E)
	};
	
	private static final String[] columnName2 = new String[] { //與ColumnName1, 欄位互相對應, ColumnName1 是合併ColumnName2的區塊
			"",
			"", "筆數", "比例%", "筆數", "比例%",
			"N<=10", "10<N<=12", "12<N<=15", "N>15",
			"N<=10", "10<N<=12", "12<N<=15", "N>15"
	};
	
	private static Short[] columnWidth = new Short[] {
			3000,
			3000, 3000, 3000, 3000, 3000,
			3000, 3000, 3000, 3000,
			3000, 3000, 3000, 3000 
	};

	private static final String[] color = new String[columnName0.length];
	private SXSSFWorkbook wb;
	private SXSSFSheet sheet;
	private int rowIndex = 0;
	//private int cells = 34;
	private PassengerAnalysis pa = null;
	private String title = "";
	private String condition = "";
	private String sheetName = "sheet1";

	public LogToExcel_R1F51() {
		wb = new SXSSFWorkbook();
	}
	
	public LogToExcel_R1F51(SXSSFWorkbook wb) {
		this.wb = wb;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public void setReportTitle(String title) {
		this.title = title;
	}

	public void setReportCondition(String condition) {
		this.condition = condition;
	}

	public void createExcel(PassengerAnalysis pa) throws Exception {
		this.pa = pa;
		sheet = wb.createSheet(sheetName);
		writeTitle();
		//writeCondition();
		writeColumnHead0();
		writeColumnHead1();
		writeColumnHead2();
		mergedHead();
		setColumnWidth();
		writeData();
		writeMarkData();
		writeVersion();
		// writeAmountAndRate();
	}

	public void write(OutputStream arg0) throws IOException {
		wb.write(arg0);
	}

	public void close() throws Exception {
		if (wb != null) {
			wb.close();
		}
	}

	private void writeTitle() {
		XSSFCellStyle titleStyle = (XSSFCellStyle) wb.createCellStyle();
		titleStyle = ScPoiUtil.getBorderLine(titleStyle, BorderStyle.THIN, true, true, true, true);
		titleStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		titleStyle.setAlignment(HorizontalAlignment.LEFT);
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 32);
		// font.setBold(true);
		titleStyle.setFont(font);

		SXSSFRow row = sheet.createRow(rowIndex);
		for (int i = 0; i < columnName0.length; i++) {
			SXSSFCell cell = row.createCell(i);
			if (i == 0) {
				cell.setCellValue(title);
			}
			cell.setCellStyle(titleStyle);
		}
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, columnName0.length-1));
		rowIndex++;
	}

	public void writeCondition() {
		XSSFCellStyle condStyle = getCellStyle(IndexedColors.WHITE.getIndex(), HorizontalAlignment.LEFT);
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 16);
		condStyle.setFont(font);

		SXSSFRow row = sheet.createRow(rowIndex);
		for (int i = 0; i < columnName0.length; i++) {
			SXSSFCell cell = row.createCell(i);
			if (i == 0) {
				cell.setCellValue(condition);
			}
			cell.setCellStyle(condStyle);
		}
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, columnName0.length-1));
		rowIndex++;
	}

	private void setColumnWidth() {
		for (int i = 0; i < columnWidth.length; i++) {
			sheet.setColumnWidth(i, columnWidth[i]);
		}
	}

	private void writeColumnHead0() {

		XSSFCellStyle styleA = getCellStyle(IndexedColors.PALE_BLUE.getIndex(), HorizontalAlignment.CENTER);
		XSSFCellStyle styleB = getCellStyle(IndexedColors.SEA_GREEN.getIndex(), HorizontalAlignment.CENTER);

		SXSSFRow row = sheet.createRow(rowIndex);

		boolean flag = false;
	
		int k = 0;
		
		for (int i = 0; i < columnName0.length; i++) {
			SXSSFCell cell = row.createCell(i);
			cell.setCellValue(columnName0[i]);
			
			if (StringUtils.isNotBlank(columnName0[i])) {
				flag = !flag;
				//System.out.println(i);
				k = 0;
			} else {
				k = i;
			}
			cell.setCellStyle(flag ?  styleA : styleB);
			color[i] = flag ? "A" : "B";
			
			
//			int x = i;
//			
//			if (IntStream.of(styleAColumn).anyMatch( v -> v == x)) {
//				cell.setCellStyle(styleA);
//			}
//			
//			if (IntStream.of(styleBColumn).anyMatch( v -> v == x)) {
//				cell.setCellStyle(styleB);
//			}
		}
		
		
//		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 2, 4));
//		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 7, 8));
//		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 9, 10));
//		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 11, 13));
//		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 14, 17));
//		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 18, 20));
//		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 21, 23));
//		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 24, 26));
//		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 27, 29));
//		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 30, 32));
		//sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 1));
		rowIndex++;
	}	
	
	private void writeColumnHead1() {

		XSSFCellStyle styleA = getCellStyle(IndexedColors.PALE_BLUE.getIndex(), HorizontalAlignment.CENTER);
		XSSFCellStyle styleB = getCellStyle(IndexedColors.SEA_GREEN.getIndex(), HorizontalAlignment.CENTER);

		SXSSFRow row = sheet.createRow(rowIndex);

		for (int i = 0; i < columnName1.length; i++) {
			SXSSFCell cell = row.createCell(i);
			cell.setCellValue(columnName1[i]);

			cell.setCellStyle("A".equals(color[i]) ?  styleA : styleB);
		}
		rowIndex++;
	}
	
	private void writeColumnHead2() {

		XSSFCellStyle styleA = getCellStyle(IndexedColors.PALE_BLUE.getIndex(), HorizontalAlignment.CENTER);
		XSSFCellStyle styleB = getCellStyle(IndexedColors.SEA_GREEN.getIndex(), HorizontalAlignment.CENTER);

		SXSSFRow row = sheet.createRow(rowIndex);

		for (int i = 0; i < columnName2.length; i++) {
			SXSSFCell cell = row.createCell(i);
			cell.setCellValue(columnName2[i]);
			cell.setCellStyle("A".equals(color[i]) ?  styleA : styleB);
		}			
		rowIndex++;
	}
	
	private void writeVersion() {
		XSSFCellStyle titleStyle = (XSSFCellStyle) wb.createCellStyle();
		titleStyle = ScPoiUtil.getBorderLine(titleStyle, BorderStyle.THIN, true, true, true, true);
		titleStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		titleStyle.setAlignment(HorizontalAlignment.LEFT);
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 14);
		// font.setBold(true);
		titleStyle.setFont(font);

		SXSSFRow row = sheet.createRow(rowIndex);
		for (int i = 0; i < columnName0.length; i++) {
			SXSSFCell cell = row.createCell(i);
			if (i == 0) {
				cell.setCellValue("EgateLogParser " + CommonUtils.getVersion());
			}
			cell.setCellStyle(titleStyle);
		}
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, columnName0.length-1));
		rowIndex++;
	}	
	
	private void mergedHead() {
		sheet.addMergedRegion(new CellRangeAddress(rowIndex-3, rowIndex-1, 0, 0)); //日期
		sheet.addMergedRegion(new CellRangeAddress(rowIndex-2, rowIndex-1, 1, 1)); //合計筆數
		sheet.addMergedRegion(new CellRangeAddress(rowIndex-3, rowIndex-3, 1, 5)); //通關總筆數及比例
		sheet.addMergedRegion(new CellRangeAddress(rowIndex-2, rowIndex-2, 2, 3)); //不成功
		sheet.addMergedRegion(new CellRangeAddress(rowIndex-2, rowIndex-2, 4, 5)); //成功
		sheet.addMergedRegion(new CellRangeAddress(rowIndex-3, rowIndex-2, 6, 9)); //成功通關秒數分析(A+C+E)
		sheet.addMergedRegion(new CellRangeAddress(rowIndex-3, rowIndex-2, 10, 13)); //成功通關秒數分析(A+B+C+D+E)
	}

	private void writeData() {

		XSSFCellStyle styleA = getCellStyle(IndexedColors.LIGHT_TURQUOISE.getIndex(), HorizontalAlignment.CENTER);
		XSSFCellStyle styleB = getCellStyle(IndexedColors.LIGHT_GREEN.getIndex(), HorizontalAlignment.CENTER);
	
		AtomicInteger cnt = new AtomicInteger(1);
		SXSSFRow row = sheet.createRow(rowIndex);
		Object[] beanValue = beanValueToArray(pa);
		
		for (int i = 0; i < columnName0.length; i++) {
			SXSSFCell cell = row.createCell(i);
			if (beanValue[i] instanceof Double) {
				cell.setCellValue((Double)beanValue[i]);
			} else if (beanValue[i] instanceof Integer) {
				cell.setCellValue((Integer)beanValue[i]);
			} else {
				cell.setCellValue((String)beanValue[i]);
			}
			cell.setCellStyle("A".equals(color[i]) ?  styleA : styleB);
		}
		cnt.getAndIncrement();
		rowIndex++;
	}
	
	private void writeMarkData() {
		String markData = "證件檢核時間(A), 身分查驗時間(B), 第一道門開啟時間(C), 旅客使用時間(D), \r\n臉部擷取時間(E11), 臉部辨識時間(E12), 指紋按壓時間(E21), 指紋辨識時間(E22), 第二道門開啟時間(F)";
		
		XSSFCellStyle titleStyle = (XSSFCellStyle) wb.createCellStyle();
		titleStyle = ScPoiUtil.getBorderLine(titleStyle, BorderStyle.THIN, true, true, true, true);
		titleStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		titleStyle.setAlignment(HorizontalAlignment.LEFT);
		titleStyle.setWrapText(true);
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 14);
		// font.setBold(true);
		titleStyle.setFont(font);

		SXSSFRow row = sheet.createRow(rowIndex);
		row.setHeight((short)1000);
		for (int i = 0; i < columnName0.length; i++) {
			SXSSFCell cell = row.createCell(i);
			if (i == 0) {
				cell.setCellValue(markData);
			}
			cell.setCellStyle(titleStyle);
		}
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, columnName0.length-1));
		rowIndex++;		
	}

	private Object[] beanValueToArray(PassengerAnalysis u) {
		Object[] arr = new Object[columnName0.length];
		int idx = 0;
		arr[idx++] = u.getLogDate();		
		arr[idx++] = u.getTotalCnt();
		arr[idx++] = u.getFailCnt();
		arr[idx++] = CommonUtils.decimalRoundD(u.getFailPercentage(), 2);
		arr[idx++] = u.getSuccessCnt();
		arr[idx++] = CommonUtils.decimalRoundD(u.getSuccessPercentage(), 2);
		arr[idx++] = u.getAce1e2f_N_le_10();
		arr[idx++] = u.getAce1e2f_10_lt_N_le_12();
		arr[idx++] = u.getAce1e2f_12_lt_N_le_15();
		arr[idx++] = u.getAce1e2f_N_gt_15();
		arr[idx++] = u.getAbcde1e2f_N_le_10();
		arr[idx++] = u.getAbcde1e2f_10_lt_N_le_12();
		arr[idx++] = u.getAbcde1e2f_12_lt_N_le_15();
		arr[idx++] = u.getAbcde1e2f_N_gt_15();
		
		return arr;

	}
	
	private XSSFCellStyle getCellStyle(short color, HorizontalAlignment align) {
		XSSFCellStyle colNameStyle = (XSSFCellStyle) wb.createCellStyle();
		colNameStyle = ScPoiUtil.getBorderLine(colNameStyle, BorderStyle.THIN, true, true, true, true);
		colNameStyle.setFillForegroundColor(color);
		colNameStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		colNameStyle.setAlignment(align);
		colNameStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		colNameStyle.setWrapText(true);
		return colNameStyle;
	}

}
