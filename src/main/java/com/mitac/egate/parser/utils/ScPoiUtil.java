package com.mitac.egate.parser.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ScPoiUtil {
	
//	/**
//	 * 將Map<String, Object> 轉入 Excel 單一筆資料
//	 * @param sheet
//	 * @param list
//	 * @param rowIdx
//	 * @param includeColumnName //是否要以資料中Map的Key作為欄位名稱顯示為第一筆資料
//	 * @return
//	 */
//	public static int addDataToSheetByMap(SXSSFSheet sheet, Map<String, Object> map, int rowIdx, boolean includeColumnName) {
//		return addDataToSheetByMap(sheet, map, rowIdx, includeColumnName, null);
//	}
	
//	/**
//	 * 將Map<String, Object> 轉入 Excel 單一筆資料
//	 * @param sheet
//	 * @param list
//	 * @param rowIdx
//	 * @param includeColumnName //是否要以資料中Map的Key作為欄位名稱顯示為第一筆資料
//	 * @param dataStyle 資料的Style
//	 * @return
//	 */
//	public static int addDataToSheetByMap(SXSSFSheet sheet, Map<String, Object> map, int rowIdx, boolean includeColumnName, XSSFCellStyle dataStyle) {
//		
//		if (Objects.isNull(map) && map.size() == 0) {
//			return rowIdx;
//		}
//		
//		if (includeColumnName) {
//			String[] columnName = map.keySet().toArray(new String[map.size()]);			
//			XSSFCellStyle headStyle = getHeadStyle(sheet.getWorkbook());
//			SXSSFRow row = sheet.createRow(rowIdx);
//			setRowDataByArray(row, columnName, headStyle);
//			
//			for(int i=0; i<row.getLastCellNum(); i++) { //auto width head column
//				sheet.autoSizeColumn(i);
//			}
//			rowIdx++;
//		}
//		
//		SXSSFRow row = sheet.createRow(rowIdx);
//		Object[] values = map.values().toArray(new Object[map.size()]);
//		setRowDataByArray(row, values, dataStyle);
//		rowIdx++;		
//		return rowIdx;
//		
//	}
	
//	/**
//	 * 將List<Map<String, Object>> 轉入 Excel
//	 * @param sheet
//	 * @param list
//	 * @param rowIdx
//	 * @param includeColumnName //是否要以資料中Map的Key作為欄位名稱顯示為第一筆資料
//	 * @return
//	 */
//	public static int addDataToSheetByListMap(SXSSFSheet sheet, List<Map<String, Object>> list, int rowIdx, boolean includeColumnName) {
//
//		return addDataToSheetByListMap(sheet, list, rowIdx, includeColumnName, null);
//	}
	
//	/**
//	 * 將List<Map<String, Object>> 轉入 Excel
//	 * @param sheet
//	 * @param list
//	 * @param rowIdx
//	 * @param includeColumnName
//	 * @param dataStyle 資料的Style
//	 * @return
//	 */
//	public static int addDataToSheetByListMap(SXSSFSheet sheet, List<Map<String, Object>> list, int rowIdx, boolean includeColumnName, XSSFCellStyle dataStyle) {
//
//		if (list == null || list.size() == 0) {
//			return rowIdx;
//		}
//		
//		if (includeColumnName) {
//			Map<String, Object> nameMap = list.get(0);
//			String[] columnName = nameMap.keySet().toArray(new String[nameMap.size()]);			
//			XSSFCellStyle headStyle = getHeadStyle(sheet.getWorkbook());
//			SXSSFRow row = sheet.createRow(rowIdx);
//			setRowDataByArray(row, columnName, headStyle);
//			
//			for(int i=0; i<row.getLastCellNum(); i++) { //auto width head column
//				sheet.autoSizeColumn(i);
//			}
//			
//			rowIdx++;
//		}
//		
//		for(int i=0; i<list.size(); i++) {
//			SXSSFRow row = sheet.createRow(rowIdx);
//			Map<String, Object> map = list.get(i); 
//			Object[] values = map.values().toArray(new Object[map.size()]);
//			setRowDataByArray(row, values, dataStyle);
//			rowIdx++;
//		}
//		
//		return rowIdx;
//	}
	
	/**
	 * 取得cell使用的匡線style
	 * @param cellStyle
	 * @param borderLine
	 * @return
	 */
	public static XSSFCellStyle getBorderLine(Workbook wb, BorderStyle borderLine, boolean top, boolean bottom, boolean left, boolean right) {
		XSSFCellStyle cellStyle = (XSSFCellStyle)wb.createCellStyle();
		return getBorderLine(cellStyle, borderLine, top, bottom, left, right);
		
	}
	
	/**
	 * 取得cell使用的匡線style
	 * @param cellStyle
	 * @param borderLine
	 * @return
	 */
	public static XSSFCellStyle getBorderLine(XSSFCellStyle cellStyle, BorderStyle borderLine, boolean top, boolean bottom, boolean left, boolean right) {
		if (top) cellStyle.setBorderTop(borderLine); 
		if (bottom)	cellStyle.setBorderBottom(borderLine); 
		if (left) cellStyle.setBorderLeft(borderLine);
		if (right) cellStyle.setBorderRight(borderLine);
		return cellStyle;
	}	
	
//	/**
//	 * 取得Head sytle 固定的無法變更
//	 * @param wb //用來取得CellStyle instance
//	 * @return
//	 */
//	public static XSSFCellStyle getHeadStyle(SXSSFWorkbook wb) {
//		XSSFCellStyle headStyle = (XSSFCellStyle)wb.createCellStyle();
//		headStyle = getBorderLine(headStyle, BorderStyle.THIN);
//		headStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
//		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//		headStyle.setAlignment(HorizontalAlignment.CENTER);
//		Font font = wb.createFont();
//		font.setFontHeightInPoints((short)36);
//		font.setBold(true);
//		headStyle.setFont(font);
//		return headStyle;
//	}
	
//	/**
//	 * 寫入一筆row的資料
//	 * @param row
//	 * @param obj 資料陣列
//	 * @param style
//	 */
//	private static void setRowDataByArray(SXSSFRow row, Object[] obj, CellStyle style) {
//		
//		for(int i=0; i<obj.length; i++) {
//			SXSSFCell cell = row.createCell(i);
//			
//			if (style != null) {
//				cell.setCellStyle(style);				
//			}
//
//			if (obj[i] == null) {
//				cell.setCellValue("");
//				continue;
//			}
//			
//			if (obj[i] instanceof String) {
//				XSSFRichTextString ss = new XSSFRichTextString((String)obj[i]);
//				if (ss.length() > SpreadsheetVersion.EXCEL2007.getMaxTextLength()) {
//					XSSFCellStyle Errstyle = (XSSFCellStyle)row.getSheet().getWorkbook().createCellStyle();
//					Errstyle = getBorderLine(Errstyle, XSSFBorderFormatting.BORDER_THIN);
//					Errstyle.setFillForegroundColor(new XSSFColor(Color.PINK));
//					Errstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);					
//					cell.setCellStyle(Errstyle);
//					cell.setCellValue("The maximum length of cell contents (text) is 32,767 characters");
//				} else {
//					cell.setCellValue((String)obj[i]);					
//				}
//				continue;
//			}
//			
//			if (obj[i] instanceof Integer) {
//				cell.setCellValue((Integer)obj[i]);
//				continue;
//			}
//			
//			if (obj[i] instanceof Double) {
//				cell.setCellValue((Double)obj[i]);
//				continue;
//			}			
//			
//			if (obj[i] instanceof Boolean) {
//				cell.setCellValue((Boolean)obj[i]);
//				continue;
//			}
//			
//			cell.setCellValue(obj[i].toString());
//		}
//	}
	
	/**
	 * 取得第一筆資料，第一行應固定為column header, 第二行才是資料
	 * @param sheet
	 * @return
	 */
	public static Map<String, Object> getFirstMap(XSSFSheet sheet) {
		if (sheet.getLastRowNum() < 1) {
			return new HashMap<>();
		}
		XSSFRow headRow = sheet.getRow(0);
		XSSFRow dataRow = sheet.getRow(1);
		Map<String, Object> map = new LinkedHashMap<>();

		final DataFormatter df = new DataFormatter();
		for(int i=0; i<headRow.getLastCellNum(); i++) {
			map.put(df.formatCellValue(headRow.getCell(i)), df.formatCellValue(dataRow.getCell(i)));
		}
		return map;
	}
	
	/**
	 * 取得sheet所有資料，第一行應固定為column header, 第二行以後才是資料
	 * @param sheet
	 * @return
	 */
	public static List<Map<String, Object>> getList(XSSFSheet sheet) {
		System.out.println("LastRowNum=" + sheet.getLastRowNum());
		if (sheet.getLastRowNum()<1) {
			return new ArrayList<>();
		}
		XSSFRow headRow = sheet.getRow(0);
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = null;
		final DataFormatter df = new DataFormatter();
		for(int i=0; i<sheet.getLastRowNum(); i++) {
			map = new LinkedHashMap<>();
			XSSFRow dataRow = sheet.getRow(i+1);
			for(int j=0; j<dataRow.getLastCellNum(); j++) {
				map.put(df.formatCellValue(headRow.getCell(j)), df.formatCellValue(dataRow.getCell(j)));
			}
			list.add(map);
		}
		return list;
	}
	
	public static List<Map<String, Object>> getTest() {
		
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("AAAAAAAAAAAAAAAAAAAA", "中華民國萬歲");
		map.put("BBBB", 100);
		map.put("CCCC", 12.3);
		map.put("DDDD", "ASDF");
		map.put("EEEE", "");
		list.add(map);
		
		map = new LinkedHashMap<>();
		map.put("AAAA", "OOOV");
		map.put("BBBB", 55);
		map.put("CCCC", 77.03);
		map.put("DDDD", "ddss");
		map.put("EEEE", new Date());
		list.add(map);
		return list;
	}
	public static void main(String[] args) {
//		SXSSFWorkbook wb = new SXSSFWorkbook();
//		SXSSFSheet sheet = wb.createSheet("sql");
//		
//		ScPoiUtil.addDataToSheetByListMap(sheet, getTest(), 0, true);
//		try(FileOutputStream out = new FileOutputStream("c://test.xlsx")) {
//			wb.write(out);
//			wb.close();
//			//wb.dispose();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		try(InputStream is = new FileInputStream("E:\\22222\\2017-02-21\\WEZ_LTD_MAPPING.xlsx");
			XSSFWorkbook wb = new XSSFWorkbook(is)) {
			
			Map<String, Object> attrMap = ScPoiUtil.getFirstMap(wb.getSheet("ATTR"));
			List<Map<String, Object>> wezDataList = ScPoiUtil.getList(wb.getSheet("WEZ_DATA"));
			List<Map<String, Object>> wezLtdList = ScPoiUtil.getList(wb.getSheet("WEZ_LTD"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}

