/**
 * 外籍人士每日通關狀況明細表
 */
package com.mitac.egate.parser.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.mitac.egate.parser.ConvertToExcel;
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

public class LogToExcel_V21 implements AutoCloseable {
//	private static final int COLUMNS = 34;
	
	private static final String[] columnName0 = new String[] {// 合併ColumnName欄位相同區塊的title
			"Gate",
			"日期",
			"序號", 
			"證件號碼/英文姓名",
			"國籍",
//			"證件到期時間",
			"通關狀況","",
			"證件檢核時間(A)", "", "",
			"登機證", "", "",
//			"身分查驗時間(B)", "", "", "",
//			"體溫檢測時間", "", "",
			"查詢生物特徵", "", "",
			"臉部擷取時間(E11)", "", "",
			"臉部辨識時間(E12)", "", "", "",
//			"按壓指紋時間(E21)", "", "",
//			"指紋比對時間(E22)", "", "", "", "", "",
//			"第二道門開啟時間(F)","", "",
			"閘門開啟時間(C)", "", "",
			"閘道重置時間", "", "",
			"Gate使用時間",
			"整體使用時間"
			};

	private List<Integer> styleFlagStart = new ArrayList<>(); //由columnName0是否有中文紀錄每個區塊的開始
	private List<Integer> styleFlagEnd = new ArrayList<>(); //紀錄每個區塊的結束, 與StyleFlagStart的長度必須相同
//	private static final int[] styleAColumn = {0, 2, 3, 4, 6, 9, 10, 12, 14, 15, 16, 17, 21, 22, 23, 27, 28, 29, 33};
//	private static final int[] styleBColumn = {1, 5, 7, 8, 11, 12, 13, 18, 19, 20, 24, 25, 26, 30, 31, 32};

	private static final String[] columnName = new String[] { //與ColumnName0 欄位互相對應, ColumnName0 是合併ColumnName的區塊
			"", //Gate
			"", //日期
			"", //序號
			"", //證件號碼
			"", //國籍
//			"", //證件到期時間
			"不成功", "成功", //通關狀況
			"護照機讀取開始時間", "護照機讀取結束時間", "秒數", //證件檢核時間A
			"登機證讀取開始時間", "登機證讀取結束時間", "秒數", //登機證檢核時間
//			"eGate送查驗系統", "eGate收到查驗系統回覆", "秒數", "RCODE",//身分查驗時間B
//			"開始時間", "結束時間", "秒數", //體溫擷取時間
			"開始時間", "結束時間", "秒數",
			"開始時間", "結束時間", "秒數", //臉部擷取時間E11
			"開始時間", "結束時間", "秒數", "臉辨分數",//臉部擷取時間E12
//			"開始時間", "結束時間", "秒數", //E21
//			"開始時間", "結束時間", "秒數", "指辨分數", "右手\n指紋擷取\n辨識時間", "左手\n指紋擷取\n辨識時間", //E22
//			"辨識結束時間", "第二道門開啟", "秒數",
			"臉部辨識結束時間", "閘門開啟", "秒數", //第一道門開啟時間C
			"閘門開啟","閘門關閉","秒數", //旅客使用時間D
			"", //eGate使用時間\nA+C+E
			"" //整體通關時間\nA+B+C+D+E
			};
	private static Short[] columnWidth = new Short[] {
			2000,
			3000,
			3000,
			3000,
			2000,
//			4000,
			2000, 2000,
			4000, 4000,	3000,
			4000, 4000,	3000,
//			4000, 4000, 3000, 4000,
//			4000, 4000, 3000,//體溫
			4000, 4000, 3000,
			4000, 4000, 3000,
			4000, 4000, 3000, 3000,
//			4000, 4000, 3000,
//			4000, 4000, 3000, 3000, 3000, 3000,
//			4000, 4000, 3000,
			4000, 4000, 3000,
			4000, 4000, 3000,
			4000, 
			5000
			};
	private SXSSFWorkbook wb;
	private SXSSFSheet sheet;
	private int rowIndex = 0;
	//private int cells = 34;
	private List<PassengerLog> list = null;
	private String title = "";
	private String condition = "";
	private String sheetName = "sheet1";
	private String logname = ConvertToExcel.logname;

	public LogToExcel_V21() {
		wb = new SXSSFWorkbook();
	}
	
	public LogToExcel_V21(SXSSFWorkbook wb) {
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
		//writeCondition();
		writeColumnHead0();
		writeColumnHead();
		setColumnWidth();
		writeData();
//		writeVersion();
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

		list.stream()
			//.filter(u -> "成功".equals(u.getIdentifyResult()))
			.forEach(u -> {

				SXSSFRow row = sheet.createRow(rowIndex);
				Object[] beanValue = beanValueToArray(u);
				boolean flag = false;
				
				for (int i = 0; i < columnName.length; i++) {
					SXSSFCell cell = row.createCell(i);
					//排序號碼
					if (i == 2) {
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
				}
				cnt.getAndIncrement();
				rowIndex++;
		});
		//sheet.addMergedRegion(new CellRangeAddress(rowIndex - cnt.get(), rowIndex - 1, 0, 0));
	}

	private Object[] beanValueToArray(PassengerLog u) {
		Object[] arr = new Object[columnName0.length];
		arr[0] = logname;
		arr[1] = u.getLogDate();
		int idx = 3;
		arr[idx++] = u.getMainDocument(); //證件號碼
		arr[idx++] = u.getNationality();
//		arr[idx++] = u.getEmrpExpireDate(); //護照到期時間
		arr[idx++] = !"成功".equals(u.getIdentifyResult()) ? "V" : "";
		arr[idx++] = "成功".equals(u.getIdentifyResult()) ? "V" : "";
		arr[idx++] = CommonUtils.DateTimeToTime(u.getEmrpStartTime()); //護照機啟動
		arr[idx++] = CommonUtils.DateTimeToTime(u.getEmrpEndTime()); //eGate送查驗系統
		
		arr[idx++] = CommonUtils.decimalRoundD(u.getEmrpSeconds(), 3); //秒數

		arr[idx++] = CommonUtils.DateTimeToTime(u.getBoardingpassSTime());   //登機證開始
		arr[idx++] = CommonUtils.DateTimeToTime(u.getBoardingpassETime());   //登機證結束
		arr[idx++] = CommonUtils.decimalRoundD(u.getBoardingpassSecs(), 3);; //登記證驗證時間秒數

//		arr[idx++] = CommonUtils.DateTimeToTime(u.getQryHywebStartTime()); //eGate送查驗系統
//		arr[idx++] = CommonUtils.DateTimeToTime(u.getQryHywebEndTime()); //eGate收到查驗系統回覆
//		arr[idx++] = CommonUtils.decimalRoundD(u.getReportBSeconds(), 3); //秒數
//		arr[idx++] = u.getQryHywebRcode(); //RCODE

//		arr[idx++] = CommonUtils.DateTimeToTime(u.getStartTime()); //查詢體溫開始時間
//		arr[idx++] = CommonUtils.DateTimeToTime(u.getBodyTemperatureTime()); //查詢體溫結束時間
//		arr[idx++] = CommonUtils.decimalRoundD(u.getBodyTemperatureSecs(), 3); //查詢體溫秒數

		arr[idx++] = CommonUtils.DateTimeToTime(u.getQryMnrStartTime()); //查詢生物特徵開始時間
		arr[idx++] = CommonUtils.DateTimeToTime(u.getQryMnrEndTime()); //查詢生物特徵結束時間
		arr[idx++] = CommonUtils.decimalRoundD(u.getQryMnrSeconds(), 3); //查詢生物特徵秒數

		arr[idx++] = CommonUtils.DateTimeToTime(u.getFaceNoResultSTime()); //臉部擷取開始時間
		arr[idx++] = CommonUtils.DateTimeToTime(u.getFaceNoResultETime()); //臉部擷取結束時間
		arr[idx++] = CommonUtils.decimalRoundD(u.getFaceNoResultSecs(), 3); //秒數
		
		arr[idx++] = CommonUtils.DateTimeToTime(u.getFaceHasResultSTime()); //臉部擷取開始時間
		arr[idx++] = CommonUtils.DateTimeToTime(u.getFaceHasResultETime()); //臉部擷取結束時間
		arr[idx++] = CommonUtils.decimalRoundD(u.getFaceHasResultSecs(), 3); //秒數
		arr[idx++] = u.getFaceScore(); //臉辨分數
		
//		arr[idx++] = CommonUtils.DateTimeToTime(u.getFingerPressSTimeNoResult()); //指壓開始
//		arr[idx++] = CommonUtils.DateTimeToTime(u.getFingerPressETimeNoResult()); //指壓結束
//		arr[idx++] = CommonUtils.decimalRoundD(u.getFingerPressNoResultSecs(), 3); //秒數
//
//		arr[idx++] = CommonUtils.DateTimeToTime(u.getFingerPressSTimeHasResult()); //指辨開始
//		arr[idx++] = CommonUtils.DateTimeToTime(u.getFingerPressETimeHasResult()); //指辨結束
//		arr[idx++] = CommonUtils.decimalRoundD(u.getFingerPressHasResultSecs(), 3); //秒數
//		arr[idx++] = u.getFingerScore(); //指辨分數
//		arr[idx++] = CommonUtils.decimalRoundD(u.getLastFinger01IdentifySeconds(), 3); //右手最後指辨秒數
//		arr[idx++] = CommonUtils.decimalRoundD(u.getLastFinger02IdentifySeconds(), 3); //左手最後指辨秒數
		
//		arr[idx++] = CommonUtils.DateTimeToTime(u.getIdentifyEndTime()); //辨識結束
//		arr[idx++] = CommonUtils.DateTimeToTime(u.getDoor2aOpenTime()); //第二道門開啟
//		arr[idx++] = CommonUtils.decimalRoundD(u.getReportFSeconds(), 3); //第二道門開啟時間


//		arr[idx++] = CommonUtils.DateTimeToTime(u.getQryHywebEndTime()); //eGate收到查驗系統回覆,先關閉
		arr[idx++] = CommonUtils.DateTimeToTime(u.getFaceHasResultETime()); //臉部辨識結束時間
		arr[idx++] = CommonUtils.DateTimeToTime(u.getDoor1aOpenTime()); //閘門開啟
		arr[idx++] = CommonUtils.decimalRoundD(u.getReportCSeconds(), 3); //秒數
		arr[idx++] = CommonUtils.DateTimeToTime(u.getDoor1aOpenTime()); //閘門開啟
		arr[idx++] = CommonUtils.DateTimeToTime(u.getDoor1aCloseTime()); //閘門關閉
		arr[idx++] = CommonUtils.decimalRoundD(u.getReportDSeconds(), 3); //秒數

		arr[idx++] = CommonUtils.decimalRoundD(u.getReportACE1E2FSeconds(), 3); //第三代自動通關系統所需時間\nA+C+D
		arr[idx++] = CommonUtils.decimalRoundD(u.getReportABCDE1E2FSeconds(), 3); //整體通關時間\nA+B+C+D+E		
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
