package com.mitac.egate.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mitac.egate.db.service.PassLogService;
import com.mitac.egate.parser.bean.PassengerLog;
import com.mitac.egate.parser.bean.ReturnLogs;
import com.mitac.egate.parser.utils.CommonUtils;
import com.mitac.egate.parser.utils.ParserUtils;
import com.mitac.egate.parser.utils.UnZipUtils;

//@ComponentScan("com.mitac.egate")
//@EntityScan("com.mitac.egate.db.model")
//@EnableTransactionManagement
//@EnableJpaRepositories(basePackages = { "com.mitac.egate.db.repository" })
//@EnableAutoConfiguration
// @EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class ConvertToDB implements CommandLineRunner {

	@Autowired
	private PassLogService passLogService;

	public ConvertToDB() {
		// TODO Auto-generated constructor stub
	}

	public void processFiles(Path path) throws IOException {
		List<Path> filesList = null;
		if (Files.isDirectory(path)) {
			filesList = CommonUtils.getFiles(path);
		} else {
			filesList = new ArrayList<>();
			filesList.add(path);
		}
		if (filesList.size() == 0) {
			System.out.println("  >> No log files found!");
			return;
		}

		System.out.println("  >> found " + filesList.size() + " log files");

		Map<String, List<PassengerLog>> logsMap = new LinkedHashMap<>();
		filesList.forEach(p -> { // 解析各log檔資料
			String fileName = p.getFileName().toString();
			System.out.println("     >> Parsing >> " + fileName);
			String[] subStr = fileName.split("\\.");
			if (subStr.length == 4) {
				// String eGateId = p.toString().replaceAll("^.*[.](.*)$", "$1");
				String portType = subStr[2];
				String eGateId = subStr[3];
				ReturnLogs rLogs = ParserUtils.parser(p.toString(), portType, eGateId);
				List<PassengerLog> logList = rLogs.getPassengerLog();
				if (logList.size() <= 0) {
					System.out.println("        >> No passenger clearance record!!!");
					return;
				}
				logsMap.put(eGateId, logList);
			} else {
				System.out.println("檔名格式有誤!!檔名:" + fileName);
			}
		});
		System.out.println("  >> Parsing completed !");
		// 合併logMap裡所有的值
		List<PassengerLog> allLogsList = logsMap.values().stream().flatMap(list -> list.stream())
				.collect(Collectors.toList());

		if (allLogsList.size() == 0) {
			return;
		}

		/*
		 * if ("in".equalsIgnoreCase(type)) { passLogService.saveLog(PassLogIn.class,
		 * allLogsList); } else { passLogService.saveLog(PassLogOut.class, allLogsList);
		 * }
		 */
		passLogService.saveLog(allLogsList);
	}

	public void start(List<String> argsList) {
		Optional<String> urlArg = CommonUtils.getArgs(argsList, "(?i)--url=.*");
		Optional<String> userArg = CommonUtils.getArgs(argsList, "(?i)--username=.*");
		Optional<String> pwdArg = CommonUtils.getArgs(argsList, "(?i)--password=.*");
		Optional<String> logArg = CommonUtils.getArgs(argsList, "(?i)--log=.*");
		if (!urlArg.isPresent() || !userArg.isPresent() || !pwdArg.isPresent() || !logArg.isPresent()) {
			throw new InvalidParameterException("參數錯誤!  ");
		}
		SpringApplication.run(ConvertToDB.class, argsList.toArray(new String[0]));
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("args=" + Arrays.toString(args));
		List<String> argsList = Arrays.asList(args);
		List<String> unzipList = new ArrayList<String>();
		Optional<String> logArg = CommonUtils.getArgs(argsList, "--log=.*");
		String logFile = logArg.get().split("=", -1)[1];
		Path logpath = Paths.get(logFile.trim());
		if (Files.isDirectory(logpath)) {
			UnZipUtils.unzipFromLoc(logFile, "");
			unzipList = UnZipUtils.fileList;
		} else {
			unzipList.add(logFile);
		}
		AtomicInteger count = new AtomicInteger(0);
		for (String unzipFile : unzipList) {
			System.out.println("目前進度...." + count.incrementAndGet() + "/" + unzipList.size());
			Path path = Paths.get(unzipFile.trim());
			processFiles(path);
		}

	}

	public static void main(String[] args) {
		System.out.println("sss");
	}
}
