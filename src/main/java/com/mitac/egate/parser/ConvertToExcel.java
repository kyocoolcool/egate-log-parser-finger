package com.mitac.egate.parser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mitac.egate.parser.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.mitac.egate.parser.bean.EmrpLog;
import com.mitac.egate.parser.bean.FingerLog;
import com.mitac.egate.parser.bean.PassengerAnalysis;
import com.mitac.egate.parser.bean.PassengerLog;
import com.mitac.egate.parser.bean.ReturnLogs;
import com.mitac.egate.parser.utils.CommonUtils;
import com.mitac.egate.parser.utils.ParserUtils;

public class ConvertToExcel {

	public ConvertToExcel() {
		// TODO Auto-generated constructor stub
	}

	// 原始版本
	private void generatorExcel_V1(String logFile, List<PassengerLog> logList) {
		System.out.println("  >> Generate report: " + logFile + "_V1.xlsx");
		try (LogToExcel_V1 excel = new LogToExcel_V1(); OutputStream out = new FileOutputStream(logFile + "_V1.xlsx")) {
			excel.setReportTitle("eGate Log Parser");
			excel.setReportCondition(logFile);
			excel.setSheetName("LogParser");
			excel.createExcel(logList);
			excel.write(out);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 副總要的版本, 明細表和分析表放一起
	private void generatorExcel(String logFile, List<PassengerLog> logList, PassengerAnalysis pa) {
		System.out.println("  >> Generate report: " + logFile + "_V2.xlsx");

		try (SXSSFWorkbook wb = new SXSSFWorkbook(); OutputStream out = new FileOutputStream(logFile + "_V2.xlsx");) {
			LogToExcel_V2 excel = new LogToExcel_V2(wb);
			excel.setReportTitle("每日通關狀況明細表");
			excel.setReportCondition("");
			excel.setSheetName("每日通關狀況明細表");
			excel.createExcel(logList);
			LogToExcel_V3 excel1 = new LogToExcel_V3(wb);
			excel1.setReportTitle("每日通關統計分析表");
			excel1.setReportCondition("");
			excel1.setSheetName("每日通關統計分析表");
			excel1.createExcel(pa);
			LogToExcel_V4 excel2 = new LogToExcel_V4(wb);
			excel2.setReportTitle("每日通關EGATE使用時間大於10秒的明細表");
			excel2.setReportCondition("");
			excel2.setSheetName("每日通關EGATE使用時間大於10秒的明細表");
			excel2.createExcel(logList);

			wb.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 產每日通關明細表與分析表，分國人與外籍人士兩種
	private void generatorExcel_R1(String logFile, List<PassengerLog> logList) {
		System.out.println("  >> Generate report: " + logFile + "_R1.xlsx");

		try (SXSSFWorkbook wb = new SXSSFWorkbook(); OutputStream out = new FileOutputStream(logFile + "_R1.xlsx");) {

			List<PassengerLog> twnLogList = logList.stream().filter(p -> "TWN".equals(p.getNationality()))
					.collect(Collectors.toList());
//			List<PassengerLog> twnLogList = logList.stream().collect(Collectors.toList());  //OneID不需要過濾國籍,所以把過濾器拿掉

			List<PassengerLog> forLogList = logList.stream().filter(p -> !"TWN".equals(p.getNationality()))
					.collect(Collectors.toList());  //屬於oneid美日通關狀況明細表的,搬上來

			List<PassengerLog> b_or_c = logList.stream().filter(p -> p.getBoardingorchecking() !=null)
					.collect(Collectors.toList());

			PassengerAnalysis twnPa = ParserUtils.analysisLog(twnLogList);

			// 2020-01-02國人格式改與外籍旅客相同
			// LogToExcel_V2 excel = new LogToExcel_V2(wb);
			if(!b_or_c.isEmpty()) {   //關掉了讓程式判斷是不是外國人(讓程式可以判端是屬於boarding還是checking)

				LogToExcel_V21 excel = new LogToExcel_V21(wb);
				excel.setReportTitle("國人每日通關狀況明細表");
				excel.setReportCondition("");
				excel.setSheetName("國人每日通關狀況明細表");
				excel.createExcel(twnLogList);
				// LogToExcel_V3 excel1 = new LogToExcel_V3(wb);
				LogToExcel_V31 excel1 = new LogToExcel_V31(wb);
				excel1.setReportTitle("國人每日通關統計分析表");
				excel1.setReportCondition("");
				excel1.setSheetName("國人每日通關統計分析表");
				excel1.createExcel(twnPa);
			}


//			List<PassengerLog> forLogList = logList.stream().filter(p -> !"TWN".equals(p.getNationality()))
//					.collect(Collectors.toList());  //搬到上面讓判斷式方便運行

			PassengerAnalysis forPa = ParserUtils.analysisLog(forLogList);
			if(!b_or_c.isEmpty()){   //關掉了讓程式判斷是不是外國人(讓程式可以判端是屬於boarding還是checking)
				LogToExcel_V21 excel21 = new LogToExcel_V21(wb); //原本是LogToExcel_V21,用LogToExcel_V211取代
				excel21.setReportTitle("外籍人士通關狀況明細表");
				excel21.setReportCondition("");
				excel21.setSheetName("外籍人士通關狀況明細表");
				excel21.createExcel(forLogList);

				LogToExcel_V31 excel31 = new LogToExcel_V31(wb);
				excel31.setReportTitle("OneID每日通關統計分析表");
				excel31.setReportCondition("");
				excel31.setSheetName("OneID每日通關統計分析表");
				excel31.createExcel(forPa);

			}

			//此為用來判斷是不是checking or boarding的判斷式
			if(b_or_c.isEmpty()){
				LogToExcel_V211 excel21 = new LogToExcel_V211(wb); //原本是LogToExcel_V21,用LogToExcel_V211取代
				excel21.setReportTitle("203204通關狀況明細表");
				excel21.setReportCondition("");
				excel21.setSheetName("203204通關狀況明細表");
				excel21.createExcel(forLogList);

				LogToExcel_V31 excel31 = new LogToExcel_V31(wb);
				excel31.setReportTitle("203204通關統計分析表");
				excel31.setReportCondition("");
				excel31.setSheetName("203204通關統計分析表");
				excel31.createExcel(forPa);

			}


			wb.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 產每日通關EGATE使用時間大於10秒的明細表，分國人與外籍人士兩種
	private void generatorExcel_R2(String logFile, List<PassengerLog> logList) {
		System.out.println("  >> Generate report: " + logFile + "_R2.xlsx");
		try (SXSSFWorkbook wb = new SXSSFWorkbook(); OutputStream out = new FileOutputStream(logFile + "_R2.xlsx");) {

			List<PassengerLog> twnLogList = logList.stream().filter(p -> "TWN".equals(p.getNationality()))
					.collect(Collectors.toList());

			// 2020-01-02國人格式改與外籍旅客相同
			// LogToExcel_V4 excel2 = new LogToExcel_V4(wb);
			LogToExcel_V41 excel2 = new LogToExcel_V41(wb);
			excel2.setReportTitle("國人每日通關EGATE使用時間大於10秒的明細表");
			excel2.setReportCondition("");
			excel2.setSheetName("國人每日通關EGATE使用時間大於10秒的明細表");
			excel2.createExcel(twnLogList);

			List<PassengerLog> forLogList = logList.stream().filter(p -> !"TWN".equals(p.getNationality()))
					.collect(Collectors.toList());

			LogToExcel_V41 excel41 = new LogToExcel_V41(wb);
			excel41.setReportTitle("外籍人士每日通關EGATE使用時間大於10秒的明細表");
			excel41.setReportCondition("");
			excel41.setSheetName("外籍人士每日通關EGATE使用時間大於10秒的明細表");
			excel41.createExcel(forLogList);

			wb.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 產生外籍旅客歷次指紋擷取比對資料
	private void generatorExcel_R3(String logFile, List<PassengerLog> logList) {
		System.out.println("  >> Generate report: " + logFile + "_R3.xlsx");

		try (SXSSFWorkbook wb = new SXSSFWorkbook(); OutputStream out = new FileOutputStream(logFile + "_R3.xlsx");) {

			List<PassengerLog> forLogList = logList.stream().filter(p -> !"TWN".equals(p.getNationality()))
					.collect(Collectors.toList());

			List<FingerLog> fingerList = ParserUtils.parseToFingerLog(forLogList);

			LogToExcel_V22 v22 = new LogToExcel_V22(wb);
			v22.setReportTitle("外籍人士指紋擷取比對明細表");
			v22.setReportCondition("");
			v22.setSheetName("外籍人士指紋擷取比對明細表");
			v22.createExcel(fingerList);

			wb.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 產生外籍旅客歷次指紋擷取比對資料
	private void generatorExcel_R4(String logFile, List<EmrpLog> logList) {
		System.out.println("  >> Generate report: " + logFile + "_R4.xlsx");

		try (SXSSFWorkbook wb = new SXSSFWorkbook(); OutputStream out = new FileOutputStream(logFile + "_R4.xlsx");) {

			LogToExcel_V23 v23 = new LogToExcel_V23(wb);
			v23.setReportTitle("護照讀取錯誤明細表");
			v23.setReportCondition("");
			v23.setSheetName("護照讀取錯誤明細表");
			v23.createExcel(logList);

			wb.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void generatorExcel_V2(String logFile, List<PassengerLog> logList) {
		System.out.println("  >> Generate report: " + logFile + "_V2.xlsx");
		try (LogToExcel_V2 excel = new LogToExcel_V2(); OutputStream out = new FileOutputStream(logFile + "_V2.xlsx")) {
			excel.setReportTitle("每日通關狀況明細表");
			excel.setReportCondition("");
			excel.setSheetName("每日通關狀況明細表");
			excel.createExcel(logList);
			excel.write(out);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generatorExcel_Analysis(String logFile, PassengerAnalysis pa) {
		System.out.println("  >> Generate report: " + logFile + "_Analysis.xlsx");
		try (LogToExcel_V3 excel = new LogToExcel_V3();
				OutputStream out = new FileOutputStream(logFile + "_Analysis.xlsx")) {
			excel.setReportTitle("每日通關統計分析表");
			excel.setReportCondition("");
			excel.setSheetName("每日通關統計分析表");
			excel.createExcel(pa);
			excel.write(out);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 產每日通關明細表與分析表，分國人與外籍人士兩種
//	private void generatorExcel_R1_5(String logFile, List<PassengerLog> logList) {
//		System.out.println("  >> Generate report: " + logFile + "_R1.xlsx");
//
//		try (SXSSFWorkbook wb = new SXSSFWorkbook(); OutputStream out = new FileOutputStream(logFile + "_R1.xlsx");) {
//
//			List<PassengerLog> twnLogList = logList.stream().filter(p -> "TWN".equals(p.getNationality()))
//					.collect(Collectors.toList());
//
//			PassengerAnalysis twnPa = ParserUtils.analysisLog(twnLogList);
//
//			// 2020-01-02國人格式改與外籍旅客相同
//			// LogToExcel_R1T5 r1t5 = new LogToExcel_R1T5(wb);
//			LogToExcel_R1F5 r1t5 = new LogToExcel_R1F5(wb);
//			r1t5.setReportTitle("國人每日通關狀況明細表");
//			r1t5.setReportCondition("");
//			r1t5.setSheetName("國人每日通關狀況明細表");
//			r1t5.createExcel(twnLogList);
//			// LogToExcel_R1T51 r1t51 = new LogToExcel_R1T51(wb);
//			LogToExcel_R1F51 r1t51 = new LogToExcel_R1F51(wb);
//			r1t51.setReportTitle("國人每日通關統計分析表");
//			r1t51.setReportCondition("");
//			r1t51.setSheetName("國人每日通關統計分析表");
//			r1t51.createExcel(twnPa);
//
//			List<PassengerLog> forLogList = logList.stream().filter(p -> !"TWN".equals(p.getNationality()))
//					.collect(Collectors.toList());
//
//			PassengerAnalysis forPa = ParserUtils.analysisLog(forLogList);
//
//			LogToExcel_R1F5 r1f5 = new LogToExcel_R1F5(wb);
//			r1f5.setReportTitle("外籍人士每日通關狀況明細表");
//			r1f5.setReportCondition("");
//			r1f5.setSheetName("外籍人士每日通關狀況明細表");
//			r1f5.createExcel(forLogList);
//
//			LogToExcel_R1F51 r1f51 = new LogToExcel_R1F51(wb);
//			r1f51.setReportTitle("外籍人士每日通關統計分析表");
//			r1f51.setReportCondition("");
//			r1f51.setSheetName("外籍人士每日通關統計分析表");
//			r1f51.createExcel(forPa);
//
//			wb.write(out);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

	// 產每日通關EGATE使用時間大於10秒的明細表，分國人與外籍人士兩種
	private void generatorExcel_R2_5(String logFile, List<PassengerLog> logList) {
		System.out.println("  >> Generate report: " + logFile + "_R2.xlsx");
		try (SXSSFWorkbook wb = new SXSSFWorkbook(); OutputStream out = new FileOutputStream(logFile + "_R2.xlsx");) {

			List<PassengerLog> twnLogList = logList.stream().filter(p -> "TWN".equals(p.getNationality()))
					.collect(Collectors.toList());

			// 2020-01-02國人格式改與外籍旅客相同
			// LogToExcel_R2T5 r2t5 = new LogToExcel_R2T5(wb);
			LogToExcel_R2F5 r2t5 = new LogToExcel_R2F5(wb);
			r2t5.setReportTitle("國人每日通關EGATE使用時間大於10秒的明細表");
			r2t5.setReportCondition("");
			r2t5.setSheetName("國人每日通關EGATE使用時間大於10秒的明細表");
			r2t5.createExcel(twnLogList);

			List<PassengerLog> forLogList = logList.stream().filter(p -> !"TWN".equals(p.getNationality()))
					.collect(Collectors.toList());

			LogToExcel_R2F5 r2f5 = new LogToExcel_R2F5(wb);
			r2f5.setReportTitle("外籍人士每日通關EGATE使用時間大於10秒的明細表");
			r2f5.setReportCondition("");
			r2f5.setSheetName("外籍人士每日通關EGATE使用時間大於10秒的明細表");
			r2f5.createExcel(forLogList);
			wb.write(out);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 產生外籍旅客歷次指紋擷取比對資料
	private void generatorExcel_R3_5(String logFile, List<PassengerLog> logList) {
		System.out.println("  >> Generate report: " + logFile + "_R3.xlsx");

		try (SXSSFWorkbook wb = new SXSSFWorkbook(); OutputStream out = new FileOutputStream(logFile + "_R3.xlsx");) {

			List<PassengerLog> forLogList = logList.stream().filter(p -> !"TWN".equals(p.getNationality()))
					.collect(Collectors.toList());

			List<FingerLog> fingerList = ParserUtils.parseToFingerLog(forLogList);

			LogToExcel_R3F5 r3f5 = new LogToExcel_R3F5(wb);
			r3f5.setReportTitle("外籍人士指紋擷取比對明細表");
			r3f5.setReportCondition("");
			r3f5.setSheetName("外籍人士指紋擷取比對明細表");
			r3f5.createExcel(fingerList);

			wb.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 產生護照讀取錯誤明細表
	private void generatorExcel_R4_5(String logFile, List<EmrpLog> logList) {
		System.out.println("  >> Generate report: " + logFile + "_R4.xlsx");

		try (SXSSFWorkbook wb = new SXSSFWorkbook(); OutputStream out = new FileOutputStream(logFile + "_R4.xlsx");) {

			LogToExcel_R4_5 r45 = new LogToExcel_R4_5(wb);
			r45.setReportTitle("護照讀取錯誤明細表");
			r45.setReportCondition("");
			r45.setSheetName("護照讀取錯誤明細表");
			r45.createExcel(logList);

			wb.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void processFiles(Path path) throws IOException {
		String excelFile = path.toString();
		List<Path> filesList = CommonUtils.getFiles(path);

		if (filesList.size() == 0) {
			System.out.println("  >> No log files found!");
			return;
		}

		System.out.println("  >> found " + filesList.size() + " log files");

		Map<String, List<PassengerLog>> logsMap = new LinkedHashMap<>();
		Map<String, List<EmrpLog>> emrpErrMap = new LinkedHashMap<>();
		Map<String, PassengerAnalysis> paMap = new HashMap<>();

		filesList.forEach(p -> { // 解析各log檔資料

			String fileName = p.getFileName().toString();
			System.out.println("     >> Parsing " + fileName);
			String[] subStr = fileName.split("\\.");
//			if (subStr.length == 4) {
//				String portType = subStr[2];
//				String eGateId = subStr[3];
				String eGateId = p.toString().replaceAll("^.*[.](.*)$", "$1");
				// String portType = p.toString().replaceAll("^.*[.](.*)$", "$2");
				ReturnLogs rLogs = ParserUtils.parser(p.toString(), "", eGateId);
				List<PassengerLog> logList = rLogs.getPassengerLog();
				List<EmrpLog> emrpErrList = rLogs.getEmrpErrorLog();
				if (logList.size() <= 0) {
					System.out.println("        >> No passenger clearance record!!!(1)");
					return;
				}
				if (logsMap.containsKey(eGateId)) {
					List<PassengerLog> logExist = logsMap.get(eGateId);
					logList.addAll(logExist);
				}
				   logsMap.put(eGateId, logList);
				
				if (emrpErrList.size() > 0) {
					if (emrpErrMap.containsKey(eGateId)) {
						List<EmrpLog> emrpErrListExist =emrpErrMap.get(eGateId);
						emrpErrList.addAll(emrpErrListExist);
					}
					emrpErrMap.put(eGateId, emrpErrList);
				}

				PassengerAnalysis pa = ParserUtils.analysisLog(logList);
				paMap.put(eGateId, pa);
//			} else {
//				System.out.println("檔名格式有誤!!檔名:" + fileName);
//			}
		});
		System.out.println("  >> Parsing completed !");
		// 合併logMap裡所有的值
		List<PassengerLog> allLogsList = logsMap.values().stream().flatMap(list -> list.stream())
				.collect(Collectors.toList());
		List<EmrpLog> allEmrpErrList = emrpErrMap.values().stream().flatMap(list -> list.stream())
				.collect(Collectors.toList());
		if (allLogsList.size() == 0) {
			return;
		}
//		generatorExcel_R1_5(excelFile, allLogsList);
		generatorExcel_R2_5(excelFile, allLogsList);
		generatorExcel_R3_5(excelFile, allLogsList);
		generatorExcel_R4_5(excelFile, allEmrpErrList);
	}

	public void processFile(String verLog, String logFile) {

		ReturnLogs rLogs = ParserUtils.parser(logFile, "", "");
		List<PassengerLog> logList = rLogs.getPassengerLog();
		List<EmrpLog> emrpErrList = rLogs.getEmrpErrorLog();

		if (logList.size() <= 0) {
			System.out.println("\n  >> No passenger clearance record!!!(2)");
		} else {
			PassengerAnalysis pa = ParserUtils.analysisLog(logList);
			System.out.println("  >> Parsing completed !");
			if (StringUtils.isNotBlank(verLog)) {
				if ("--v1".equalsIgnoreCase(verLog)) {
					generatorExcel_V1(logFile, logList);
				} else if ("--v2".equals(verLog)) {
					generatorExcel(logFile, logList, pa);
				}
			} else {
				generatorExcel_R1(logFile, logList);
				generatorExcel_R2(logFile, logList);
				generatorExcel_R3(logFile, logList);
				generatorExcel_R4(logFile, emrpErrList);
			}
		}
	}
	public static String logname;
	public void start(List<String> argsList) throws InvalidParameterException, Exception {


		Optional<String> logArg = CommonUtils.getArgs(argsList, "(?i)--log=.*");
		Optional<String> versionArg = CommonUtils.getArgs(argsList, "(?i)--v[12]");

		if (!logArg.isPresent()) {
			System.out.println("請指定要解析的Log資料!");
			throw new InvalidParameterException("請指定要解析的Log資料!");
		}

		String logFile = logArg.get().split("=", -1)[1];
		Path path = Paths.get(logFile);
		//擷取log上的檔案名字看是哪個閘道的
		String lognametmp = logFile.substring(logFile.indexOf(".")-3, logFile.indexOf("."));
		logname = lognametmp;

		String ver = "";

		if (versionArg.isPresent()) {
			if (Files.isDirectory(path)) {
				System.out.println("參數錯誤!  " + versionArg.get() + " 參數後面只能接log檔案，不可為目錄!");
				throw new InvalidParameterException("參數錯誤!  " + versionArg.get() + " 參數後面只能接log檔案，不可為目錄!");
			}
			ver = versionArg.get();
		}

		System.out.println("  >> EgateLogParser " + CommonUtils.getVersion());

		if (Files.isDirectory(path)) {
			processFiles(path);
		} else { // 舊版報表
			processFile(ver, logFile);
		}
	}
}
