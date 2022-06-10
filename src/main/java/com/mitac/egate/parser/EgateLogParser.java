package com.mitac.egate.parser;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mitac.egate.parser.utils.CommonUtils;


@SpringBootApplication
public class EgateLogParser implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("ApplicationRunner的run方法");
	}

	public EgateLogParser() {
		// TODO Auto-generated constructor stub
	}
	

	private static void showUsage() {
		System.out.println("Usage:");
		showExcelUsage();
		showDBUsage();
	}
	
	private static void showExcelUsage() {
		System.out.println("Convert to excel File: java -jar EgateLogParser.jar --excel --log=<logfile>");
		System.out.println("Convert to excel File: java -jar EgateLogParser.jar --excel --v1 --log=<logfile>");
		System.out.println("Convert to excel File: java -jar EgateLogParser.jar --excel --v2 --log=<logfile>");
		System.out.println("Convert to excel File: java -jar EgateLogParser.jar -excel --log=<logfilesPath>");
		
	}
	
	private static void showDBUsage() {
		System.out.println("Convert to database: java -jar EgateLogParser.jar --db --url=<ip:port> --dbName=<databaseName> --username=<db user> --password=<db password> --type=<in|out> --log=<logfilesPath>");
		System.out.println("Convert to database example: java -jar EgateLogParser.jar --db --url=10.11.12.13:1433 --dbName=MNR --username=mitac --password=mitac123 --type=in --log=E:\\KSin-20200301\\");
	}
	
	public static void main(String[] args) throws IOException {
//		System.out.println(CommonUtils.getVersion());
		if (args.length < 1) {
			showUsage();
			return;
		}

//		List<String> argsList = Arrays.asList(args);

		//加速多檔執行
//		for(int i =1 ;i<31;i++){
//			if(10 <= i && i <32){
//				argsList.set(1,"--log=jegate-2021-04-"+i+"-204.log");
//			}else {
//				argsList.set(1, "--log=jegate-2021-04-0" + i + "-204.log");
//			}
//		if (argsList.contains("--excel")) {
//			try {
//				new ConvertToExcel().start(argsList);
//			}catch (InvalidParameterException ie) {
//				System.out.println("參數錯誤!!");
//				showExcelUsage();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} else if (argsList.contains("--db")) {
//			try {
//				new ConvertToDB().start(argsList);
//			} catch (InvalidParameterException ie) {
//				System.out.println("參數錯誤!!" + ie.toString());
//				showDBUsage();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} else {
//			showUsage();
//		}
//		//判斷式的括號
////		}
//		  System.out.println("  >> finish !!!程式執行完畢!!!!");
//		  System.out.println("  >> system exit.");
	}

}
