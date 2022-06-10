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

import com.mitac.egate.parser.bean.PassengerLog;
import com.mitac.egate.parser.utils.CommonUtils;
import com.mitac.egate.parser.utils.ScPoiUtil;

public class LogToExcel_V1 implements AutoCloseable {
//	private static final int COLUMNS = 34;
	
	private static final String[] columnName0 = new String[] {// 合併ColumnName欄位相同區塊的title
			"序號", "證件號碼","護照有效日期", 
			"通關", "", "", 
			"通關結果", "疑似兩人" , "臉辨", "", "指辨", "", 
			"讀取證件", "", "", 	
			"查詢查驗系統", "", "", "", 
			"查詢生物特徵", "", "",
			"第一道門", "", "",
			"臉辨", "", "", 
			"指辨", "", "", 
			"上傳查驗系統", "", "", 
			"上傳智慧閘道管理系統", "", "",
			"第二道門\r\n開啟時間"
			};
	
	private static final List<Integer> styleFlagStart = new ArrayList<>(); //由columnName0是否有中文紀錄每個區塊的開始
	private static final List<Integer> styleFlagEnd = new ArrayList<>(); //紀錄每個區塊的結束, 與StyleFlagStart的長度必須相同
//	private static final int[] styleAColumn = {0, 2, 3, 4, 6, 9, 10, 12, 14, 15, 16, 17, 21, 22, 23, 27, 28, 29, 33};
//	private static final int[] styleBColumn = {1, 5, 7, 8, 11, 12, 13, 18, 19, 20, 24, 25, 26, 30, 31, 32};
	
	private static final String[] columnName = new String[] { //與ColumnName0 欄位互相對應, ColumnName0 是合併ColumnName的區塊
			"", "","", 
			"開始時間", "結束時間", "秒數", 
			"", "", "辨識結果", "分數", "辨識結果", "分數",
			"開始時間", "結束時間", "秒數",
			"開始時間", "結束時間", "秒數", "RCODE",
			"開始時間", "結束時間", "秒數",
			"開啟時間", "關閉時間", "秒數", 
			"開始時間", "結束時間", "秒數",
			"開始時間", "結束時間", "秒數",
			"開始時間", "結束時間", "秒數",
			"開始時間", "結束時間", "秒數",
			""
			};
	private static Short[] columnWidth = new Short[] {  
			2000, 3000, 5000, 
			7000, 7000, 3000, 
			3000, 3000, 3000, 3000, 3000, 3000, 
			7000, 7000, 3000, 
			7000, 7000, 3000, 3000, 
			7000, 7000, 3000, 
			7000, 7000, 3000,
			7000, 7000, 3000,
			7000, 7000, 3000,
			7000, 7000, 3000, 
			7000, 7000,	3000, 
			7000
			};
	private SXSSFWorkbook wb;
	private SXSSFSheet sheet;
	private int rowIndex = 0;
	//private int cells = 34;
	private List<PassengerLog> list = null;
	private String title = "";
	private String condition = "";
	private String sheetName = "sheet1";

	public LogToExcel_V1() {
		wb = new SXSSFWorkbook();
	}
	
	public LogToExcel_V1(SXSSFWorkbook wb) {
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

	public void createExcel(List<PassengerLog> list) throws Exception {
		this.list = list;
		sheet = wb.createSheet(sheetName);
		writeTitle();
		writeCondition();
		writeColumnHead0();
		writeColumnHead();
		setColumnWidth();
		writeData();
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
				styleFlagStart.add(i);
				styleFlagEnd.add(k);
				k = 0;
			} else {
				k = i;
			}
			cell.setCellStyle(flag ?  styleA : styleB);
			
			
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
		styleFlagEnd.add(k);
		styleFlagEnd.remove(0);
		
		
		
		for(int i=0; i<styleFlagStart.size(); i++) {
			if (styleFlagEnd.get(i) != 0) {
				sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, styleFlagStart.get(i), styleFlagEnd.get(i)));
			}
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
	
	private void writeColumnHead() {

		XSSFCellStyle styleA = getCellStyle(IndexedColors.PALE_BLUE.getIndex(), HorizontalAlignment.CENTER);
		XSSFCellStyle styleB = getCellStyle(IndexedColors.SEA_GREEN.getIndex(), HorizontalAlignment.CENTER);

		SXSSFRow row = sheet.createRow(rowIndex);

		boolean flag = false;
		for (int i = 0; i < columnName.length; i++) {
			SXSSFCell cell = row.createCell(i);
			cell.setCellValue(columnName[i]);
			
			if (styleFlagStart.contains(i)) {
				flag = !flag;
			}
			
			cell.setCellStyle(flag ?  styleA : styleB);
			
			
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
		
		for(int i = 0; i < styleFlagStart.size(); i++) {
			if (styleFlagEnd.get(i) == 0) {
				sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex, styleFlagStart.get(i), styleFlagStart.get(i)));
			}
		}
		
//		sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex, 0, 0));
//		sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex, 1, 1));
//		sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex, 5, 5));
//		sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex, 6, 6));
//		sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex, 33, 33));
		//sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 1));
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
	
	private void writeData() {

		XSSFCellStyle styleA = getCellStyle(IndexedColors.LIGHT_TURQUOISE.getIndex(), HorizontalAlignment.CENTER);
		XSSFCellStyle styleB = getCellStyle(IndexedColors.LIGHT_GREEN.getIndex(), HorizontalAlignment.CENTER);
	

		AtomicInteger cnt = new AtomicInteger(1);

		list.forEach(u -> {

			SXSSFRow row = sheet.createRow(rowIndex);
		
			Object[] beanValue = beanValueToArray(u);
			
			boolean flag = false;
			
			for (int i = 0; i < columnName.length; i++) {
				SXSSFCell cell = row.createCell(i);
				
				
				if (i == 0) {
					cell.setCellValue(String.valueOf(cnt));
				} else {
					if (beanValue[i] instanceof Double) {
						cell.setCellValue((Double)beanValue[i]);	
					} else {
						cell.setCellValue((String)beanValue[i]);
					}
				}
				if (styleFlagStart.contains(i)) {
					flag = !flag;
				}
				cell.setCellStyle(flag ?  styleA : styleB);
				
//				int x = i;
//				if (IntStream.of(styleAColumn).anyMatch( v -> v == x)) {
//					cell.setCellStyle(styleA);
//				}
//				
//				if (IntStream.of(styleBColumn).anyMatch( v -> v == x)) {
//					cell.setCellStyle(styleB);
//				}				
				
			}
			cnt.getAndIncrement();

			rowIndex++;
		});
		//sheet.addMergedRegion(new CellRangeAddress(rowIndex - cnt.get(), rowIndex - 1, 0, 0));
	}

	private Object[] beanValueToArray(PassengerLog u) {
	
		Object[] arr = new Object[columnName0.length];
		int idx = 1;
		arr[idx++] = u.getMainDocument();
		arr[idx++] = u.getEmrpExpireDate();
		arr[idx++] = u.getStartTime();
		arr[idx++] = u.getEndTime();
		arr[idx++] = CommonUtils.decimalRoundD(u.getTotalSeconds(), 3);
		arr[idx++] = u.getIdentifyResult();
		arr[idx++] = u.getTwoPersonal();
		arr[idx++] = u.getFaceIdentifyResult();
		arr[idx++] = u.getFaceScore();
		arr[idx++] = u.getFingerIdentifyResult();
		arr[idx++] = u.getFingerScore();
		arr[idx++] = u.getEmrpStartTime();
		arr[idx++] = u.getEmrpEndTime();
		arr[idx++] = CommonUtils.decimalRoundD(u.getEmrpSeconds(), 3);
		arr[idx++] = u.getQryHywebStartTime();
		arr[idx++] = u.getQryHywebEndTime();
		arr[idx++] = CommonUtils.decimalRoundD(u.getQryHywebSeconds(), 3);
		arr[idx++] = u.getQryHywebRcode();
		arr[idx++] = u.getQryMnrStartTime();
		arr[idx++] = u.getQryMnrEndTime();
		arr[idx++] = CommonUtils.decimalRoundD(u.getQryMnrSeconds(), 3);
		arr[idx++] = u.getDoor1aOpenTime();
		arr[idx++] = u.getDoor1aCloseTime();
		arr[idx++] = CommonUtils.decimalRoundD(u.getDoor1aSeconds(), 3);
		arr[idx++] = u.getFaceStartTime();
		arr[idx++] = u.getFaceEndTime();
		arr[idx++] = CommonUtils.decimalRoundD(u.getFaceScends(), 3);
		arr[idx++] = u.getFingerStartTime();
		arr[idx++] = u.getFingerEndTime();
		arr[idx++] = CommonUtils.decimalRoundD(u.getFingerSeconds(), 3);
		arr[idx++] = u.getUpdHywebStartTime();
		arr[idx++] = u.getUpdHywebEndTime();
		arr[idx++] = CommonUtils.decimalRoundD(u.getUpdHywebSeconds(), 3);
		arr[idx++] = u.getUpdMnrStartTime();
		arr[idx++] = u.getUpdMnrEndTime();
		arr[idx++] = CommonUtils.decimalRoundD(u.getUpdMnrSeconds(), 3);
		arr[idx++] = u.getDoor2aOpenTime();
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
