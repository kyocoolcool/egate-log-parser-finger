package com.mitac.egate.parser.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.stream.Collectors;

import com.mitac.egate.parser.ConvertToExcel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import com.innovatrics.mrz.MrzParser;
import com.innovatrics.mrz.MrzRecord;
import com.mitac.egate.parser.LogPatterns;
import com.mitac.egate.parser.bean.EmrpLog;
import com.mitac.egate.parser.bean.FingerLog;
import com.mitac.egate.parser.bean.PassengerAnalysis;
import com.mitac.egate.parser.bean.PassengerLog;
import com.mitac.egate.parser.bean.ReturnLogs;

public class ParserUtils {
	//private List<PassengerLog> logList = new LinkedList<>();	
	private static final String success = "成功";
	private static final String fail = "失敗";
	private static final String dateTime = "yy-MM-dd HH:mm:ss.SSS";
	private static boolean  boardingA = false;  //給予最後的時間計算做區分,判斷是不是boarding還是checking的LOG
	private static List<String> preBrush = Arrays.asList(new String[] {"N07", "N08", "N09", "N10", "N11", "N12", "S02"}); //允許預刷的狀態
	//private boolean isPreBrush = false;

	public static ReturnLogs parser(String logFile, String portType,String eGateId) {

		List<PassengerLog> logList = new LinkedList<>();
		List<EmrpLog> emrpErrList = new LinkedList<>();

		ReturnLogs rtnLogs = new ReturnLogs();
		rtnLogs.setPassengerLog(logList);
		rtnLogs.setEmrpErrorLog(emrpErrList);

		boolean isPreBrush = false;
		String statusNow = "";

		try(LineIterator it = FileUtils.lineIterator(new File(logFile), "UTF8");) {

			PassengerLog[] passengerLog = new PassengerLog[2];
			passengerLog[0] = new PassengerLog(portType,eGateId);
			EmrpLog errLog = new EmrpLog(portType,eGateId);
			boolean startMatches = false;
			AtomicInteger lineCnt = new AtomicInteger(0);
			 while (it.hasNext()) {
				 String line = it.nextLine();
				 System.out.print("        >> Process lines: " + lineCnt.incrementAndGet() + "\r");

				 /** log error passport **/
				 if (line.matches(LogPatterns.EMRP_START)) {
					 String time = line.replaceAll(LogPatterns.EMRP_START, "$1");
					 errLog = new EmrpLog(portType,eGateId);
					 errLog.setStartTime(time);
					 errLog.setLogDateTime(time);
					 errLog.setLogDate(CommonUtils.DateTimeToDate(time));
					 //塞一下東西看看有沒有護照機,判斷是checking or boarding
					 passengerLog[0].setBoardingorchecking("yes");
				 }

				 //解析Mrz
				 if (line.matches(LogPatterns.EMRP_PATTERN)) {
					 String mrzStr = line.replaceAll(LogPatterns.EMRP_PATTERN, "$2");
					 if (StringUtils.isNotBlank(mrzStr)) {
						 try {
							 String mrz = mrzStr.replace(" ", "\n");
							 errLog.setPattern(mrz);
							 final MrzRecord record = MrzParser.parse(mrz);
							 errLog.setMainDocument(record.documentNumber);
						 } catch (Exception e) {
							 //
						 }
					 }
				 }
				 
				 if (line.matches(LogPatterns.EMRP_ERR1)) {
					 String reason = line.replaceAll(LogPatterns.EMRP_ERR1, "$1");
					 errLog.setErrorReason(reason);
				 }
				 
				 if (line.matches(LogPatterns.EMRP_ERR2)) {
					 String reason = line.replaceAll(LogPatterns.EMRP_ERR2, "$1");
					 errLog.setErrorReason(reason);
				 }

				 if (line.matches(LogPatterns.EMRP_STOP)) {
					 if (StringUtils.isNotBlank(errLog.getErrorReason())) {
						 emrpErrList.add(errLog);
					 }
				 }

				 //從後台返回結果若有錯誤也算進護照讀取錯誤中, 該處應該和EMRP_STOP互斥, 有那個就沒有這個, 所以理論上不會重複add
				 if (line.matches(LogPatterns.EMRP_STOP1)) {
					 String reason =  line.replaceAll(LogPatterns.EMRP_STOP1, "$3");
					 if (StringUtils.isNotBlank(reason) && !reason.equals("null")) {
						 String passPort = line.replaceAll(LogPatterns.EMRP_STOP1, "$2");
						 errLog.setMainDocument(passPort);
						 errLog.setErrorReason(reason);
						 emrpErrList.add(errLog);
					 }
				 }

				 /***************************/
				 
				 if (line.matches(LogPatterns.STATUS_NOW)) {
					 statusNow = line.replaceAll(LogPatterns.STATUS_NOW, "$1");
				 }

				 /**
				  * 從其他狀態切回N00時, 表示該名旅客已經通關完成, 要計算時間了
				  */
				 if (line.matches(LogPatterns.STATUS_TO_N00)) {
					 if (StringUtils.isEmpty(passengerLog[0].getEndTime())) {
						 String time = line.replaceAll(LogPatterns.STATUS_TO_N00, "$1");
						 passengerLog[0].setLastTime(time);
					 }

					 /**
					  * 計算旅客的通關紀錄
					  */
					 //進入點, get~ 若要測試 !StringUtils.isNotEmpty(passengerLog[0].getMainDocument() 之後放一個時間在setLogDate即可
					 if (StringUtils.isNotEmpty(passengerLog[0].getMainDocument())) {
						 processDetail(passengerLog[0]);

						 calSeconds(passengerLog[0]);
						 passengerLog[0].setLogDate(CommonUtils.DateTimeToDate(passengerLog[0].getEmrpStartTime()));  //放報表日期時間的
						 logList.add(passengerLog[0]);
					 }

					 /**
					  * 計算完後, 如果passengerLog[1]有資料, 表示有預刷, 將passengerLog往前調
					  */
					 if (passengerLog[1] != null) {
						 passengerLog[0] = passengerLog[1];
						 passengerLog[1] = null;
					 } else {
						 passengerLog[0] = new PassengerLog(portType,eGateId);
					 }
					 //如果預刷, startMatches應該是在啟用狀況, 不應reset
					 if (isPreBrush) {
						 isPreBrush = false;
					 } else {
						 startMatches = false;
					 }


				 }

				 //跳過Exception printstack訊息
				 if (line.matches(LogPatterns.EXCEPTION_LOG)) {
					 continue;
				 }
				 //護照開始讀取時間            備註:時間日期這邊改,"2021/2/8"
				 if (line.matches(LogPatterns.EMRP_START_TIME)) {
					 String time = line.replaceAll(LogPatterns.EMRP_START_TIME, "$1");
					 PassengerLog regP = passengerLog[0];

					 /**
					  *   若護照機讀取失敗, 狀態不會改變, 資料再讀一次要先清除已產生的資料
					  */
					 if ("N00".equals(statusNow) && StringUtils.isNotBlank(regP.getEmrpStartTime())) {
						 regP.setEmrpStartTime(time);
						 regP.setEmrpEndTime(null);
						 regP.setMainDocument(null);
						 regP.setNationality(null);
						 regP.setEmrpExpireDate(null);
					 }

					 /**
					  * 當時狀態若是預刷允許的狀態, 表示有預刷
					  */
					 if (preBrush.contains(statusNow)) {
						 isPreBrush = true;
						 passengerLog[1] = new PassengerLog(portType,eGateId);
						 regP = passengerLog[1];

					 } else {
						 startMatches = true;
					 }

					 if (StringUtils.isBlank(regP.getEmrpStartTime())) {
						 regP.setEmrpStartTime(time);
					 }
					 continue;
				 }else if(line.matches(LogPatterns.STATUS_TO_N00) && StringUtils.isBlank(passengerLog[0].getEmrpStartTime())){
					 String time = line.replaceAll(LogPatterns.STATUS_TO_N00, "$1");
					 PassengerLog regP = passengerLog[0];
					 regP.setEmrpStartTime(time);    //else if 的判斷式為測試用時間的,目前測試到line.replaceAll要配合line.matches使用才能成功切割
				 }else if(line.matches(LogPatterns.Passport_reader)){
					 startMatches = true; //測試,要找原因,原本沒有這個
				 }


				 if (startMatches) {
				 	String tt ="";  //供下面使用
					 //證件號碼
					 if (line.matches(LogPatterns.MAIN_DOCUMENT)) {
						 PassengerLog regP = isPreBrush ? passengerLog[1] : passengerLog[0];
						 String id = line.replaceAll(LogPatterns.MAIN_DOCUMENT, "$1");
						 if (StringUtils.isEmpty(regP.getMainDocument()) || regP.getMainDocument()== tt) {
							 regP.setMainDocument(id);  //這邊也很重要

						 }
					 }else if(line.matches(LogPatterns.MAIN_DOCUMENT_BoardingPass)){
						 PassengerLog regP = isPreBrush ? passengerLog[1] : passengerLog[0];
						 if (StringUtils.isEmpty(regP.getMainDocument()) && (ConvertToExcel.logname.equals("203") || ConvertToExcel.logname.equals("204"))) {
							 String id = line.replaceAll(LogPatterns.MAIN_DOCUMENT_BoardingPass, "$1");
							 tt = id;
							 regP.setMainDocument(id);  //wang後來加上去的,可刪改
						 }
					 } //else 跟 || regP.getMainDocument()=="nopeople" 為測試中加上去的

					 if (line.matches(LogPatterns.EMRP_NATIONALITY)) {
						 PassengerLog regP = isPreBrush ? passengerLog[1] : passengerLog[0];
						 String nationality = line.replaceAll(LogPatterns.EMRP_NATIONALITY, "$1");
						 if (StringUtils.isEmpty(regP.getNationality())) {
							 regP.setNationality(nationality);
						 }
					 }					 

					 //護照機Timeout, 清除時間, 表示這次刷護照無效
					 if (line.matches(LogPatterns.EMRP_TIMEOUT)) {
						 PassengerLog regP = isPreBrush ? passengerLog[1] : passengerLog[0];
						 regP.setEmrpStartTime(null);
						 regP.setMainDocument(null);
						 regP.setNationality(null);
						 regP.setEmrpEndTime(null);
						 regP.setEmrpExpireDate(null);
					 }
					 
					 //護照讀取結束時間
					 if (line.matches(LogPatterns.EMRP_END_TIME)) {

						 PassengerLog regP = isPreBrush ? passengerLog[1] : passengerLog[0];
						 String time = line.replaceAll(LogPatterns.EMRP_END_TIME, "$1");

						 //有開始時間才有結束時間
						 if (StringUtils.isEmpty(regP.getEmrpEndTime()) && StringUtils.isNotEmpty(regP.getEmrpStartTime())) {
							 regP.setEmrpEndTime(time);							 
						 }
					 }

					 if (line.matches(LogPatterns.EMRP_EXPIREDATE)) {
						 PassengerLog regP = isPreBrush ? passengerLog[1] : passengerLog[0];
						 String expireDate = line.replaceAll(LogPatterns.EMRP_EXPIREDATE, "$1");
						 regP.setEmrpExpireDate(CommonUtils.formatDate(expireDate, "uuuu/M/d", "uuuu/MM/dd"));
					 }

					 //讀取查驗系統開始時間
					 if (line.matches(LogPatterns.QRY_HYWEB_START_TIME)) {
						 PassengerLog regP = isPreBrush ? passengerLog[1] : passengerLog[0];
						 String time = line.replaceAll(LogPatterns.QRY_HYWEB_START_TIME, "$1");
						 regP.setQryHywebStartTime(time);
					 }
					 
					//讀取查驗系統結束時間
					 if (line.matches(LogPatterns.QRY_HYWEB_END_TIME)) {
						 PassengerLog regP = isPreBrush ? passengerLog[1] : passengerLog[0];
						 String time = line.replaceAll(LogPatterns.QRY_HYWEB_END_TIME, "$1");
						 regP.setQryHywebEndTime(time);
						 if (line.matches(LogPatterns.QRY_HYWEB_RCODE)) { //排除字串中有Test字樣的Log, 裡面沒有Rcode=這串
							 String rCode = line.replaceAll(LogPatterns.QRY_HYWEB_RCODE, "$1");
							 regP.setQryHywebRcode(rCode);
						 }
					 }
					 
					//讀取MNR開始時間
					 if (line.matches(LogPatterns.QRY_MNR_START_TIME)) {
						 PassengerLog regP = isPreBrush ? passengerLog[1] : passengerLog[0];
						 String time = line.replaceAll(LogPatterns.QRY_MNR_START_TIME, "$1");
						 regP.setQryMnrStartTime(time);
					 }

					//讀取MNR結束時間
					 if (line.matches(LogPatterns.QRY_MNR_END_TIME)) {
						 PassengerLog regP = isPreBrush ? passengerLog[1] : passengerLog[0];
						 String time = line.replaceAll(LogPatterns.QRY_MNR_END_TIME, "$1");
						 regP.setQryMnrEndTime(time);
					 }

					 //臉辨開始時間
					 if (line.matches(LogPatterns.FACE_START_TIME)) {
						 String time = line.replaceAll(LogPatterns.FACE_START_TIME, "$1");
						 passengerLog[0].setFaceStartTime(time);
					 }
					 
					 //指辨開始時間
					 if (line.matches(LogPatterns.FINGER_START_TIME)) {
						 String time = line.replaceAll(LogPatterns.FINGER_START_TIME, "$1");
						 passengerLog[0].setFingerStartTime(time);
						 passengerLog[0].setStartFingerList(line);

						 if(StringUtils.isNotBlank(passengerLog[0].getFaceHasResultSTime()) && StringUtils.isBlank(passengerLog[0].getFaceHasResultETime())) { //若無臉辨結束時間, 表示只有一次辨識, 若有指辨就拿指辨開始當結束
							 passengerLog[0].setFaceHasResultETime(time);
							 System.out.println("passengerLog[0].getFaceHasResultETime() 1 :" + passengerLog[0].getFaceHasResultETime());
						 }
					 }
					 
					 /*
					  * 重複寫入資料，表示會記錄最後一次01指辨時間, 如果preTime沒資料, 拿FINGER_START_TIME來填充
					  *  如果preTime有資料, 將thisTime先放到preTime, 再把這次的結果放到ThisTime
					  */
					 if (line.matches(LogPatterns.FINGER01_IDENTIFY_THIS_TIME)) {
						 String time = line.replaceAll(LogPatterns.FINGER01_IDENTIFY_THIS_TIME, "$1");

						 if (StringUtils.isBlank(passengerLog[0].getFinger01IdentifyPreTime())) {
							 passengerLog[0].setFinger01IdentifyPreTime(passengerLog[0].getFingerStartTime());
						 } else {
							 passengerLog[0].setFinger01IdentifyPreTime(passengerLog[0].getFinger01IdentifyThisTime());
						 }
						 
						 passengerLog[0].setFinger01IdentifyThisTime(time);
						 passengerLog[0].setFinger01Log(line); //紀錄歷次指辨log
					 }
					 
					 if (line.matches(LogPatterns.FINGER02_IDENTIFY_THIS_TIME)) {
						 String time = line.replaceAll(LogPatterns.FINGER02_IDENTIFY_THIS_TIME, "$1");

						 if (StringUtils.isBlank(passengerLog[0].getFinger02IdentifyPreTime())) {
							 passengerLog[0].setFinger02IdentifyPreTime(passengerLog[0].getFingerStartTime());
						 } else {
							 passengerLog[0].setFinger02IdentifyPreTime(passengerLog[0].getFinger02IdentifyThisTime());
						 }
						 
						 passengerLog[0].setFinger02IdentifyThisTime(time);
						 passengerLog[0].setFinger02Log(line); //紀錄歷次指辨log
					 }

					 //紀錄臉部擷取時間(沒分數)
					 if (line.matches(LogPatterns.FACE_NO_RESULT)) {
						 String time = line.replaceAll(LogPatterns.FACE_NO_RESULT, "$1");
						 //第一次沒分數時間
						 if (StringUtils.isBlank(passengerLog[0].getFaceNoResultSTime())) {
							 passengerLog[0].setFaceNoResultSTime(time);
						 }
						 

						  // 若臉辨開始有分數了, 就停止紀錄無分數時間
						 if (StringUtils.isBlank(passengerLog[0].getFaceHasResultSTime())) {
							 passengerLog[0].setFaceNoResultETime(time);
						 }
						 
					 }
					 //記錄臉部擷取時間
					 if (line.matches(LogPatterns.FACE_NO_RESULT_END)) {
						 String time = line.replaceAll(LogPatterns.FACE_NO_RESULT_END, "$1");
						 if (StringUtils.isBlank(passengerLog[0].getFaceHasResultSTime())) {
							 passengerLog[0].setFaceNoResultETime(time);
						 }
						 
					 }

					 //紀錄臉部辨識時間(有分數)
					 if (line.matches(LogPatterns.FACE_HAS_RESULT)) {
						 String time = line.replaceAll(LogPatterns.FACE_HAS_RESULT, "$1");
						 if (StringUtils.isBlank(passengerLog[0].getFaceHasResultSTime())) {
							 passengerLog[0].setFaceHasResultSTime(time);
						 } else {
							 /** finger開始後就不再讀取,  避免讀到finger結束後同樣的一行Log **/
							 if (StringUtils.isBlank(passengerLog[0].getFingerStartTime())) {
								 passengerLog[0].setFaceHasResultETime(time);
							 }
							 
						 }
					 }
					 
					 //紀錄按壓指紋出現Result=-1的區間(E21, 指紋按壓時間
					 if (line.matches(LogPatterns.FINGER_PRESS_NO_RESULT)) {
						 String time = line.replaceAll(LogPatterns.FINGER_PRESS_NO_RESULT, "$1");
						 
						 //壓指紋第一次Result=-1
						 if (StringUtils.isBlank(passengerLog[0].getFingerPressSTimeNoResult())) {
							 passengerLog[0].setFingerPressSTimeNoResult(time);
						 }
						 
						 //按壓指紋最後一次Result=-1的時間點(這是想盡可能排除旅客轉身按壓指紋前的時間)
						 //若Result=1已經出現過, 表示旅客已開始按壓指紋, 後面若又出現Result=-1的就不再紀錄
						 if (StringUtils.isBlank(passengerLog[0].getFingerPressSTimeHasResult())) {
							 passengerLog[0].setFingerPressETimeNoResult(time);
						 }
					 }
					 
					 //紀錄按壓指紋出現Result=1的區間(E22, 指紋辨識時間)
					 if (line.matches(LogPatterns.FINGER_PRESS_HAS_RESULT)) {
						 String time = line.replaceAll(LogPatterns.FINGER_PRESS_HAS_RESULT, "$1");
						 //第一次Result=1的時間
						 if (StringUtils.isBlank(passengerLog[0].getFingerPressSTimeHasResult())) {
							 passengerLog[0].setFingerPressSTimeHasResult(time);
						 } else {
							 //最後一次Result=1的時間
							 
							 passengerLog[0].setFingerPressETimeHasResult(time);
						 }
					 }
					 
					 if (line.matches(LogPatterns.Finger01_SCORE_LINE)) {
						 passengerLog[0].setFinger01ScoreLog(line); //紀錄歷次指辨log分數
					 }
					 
					 if (line.matches(LogPatterns.Finger02_SCORE_LINE)) {
						 passengerLog[0].setFinger02ScoreLog(line);
					 }
					 
					 //指辨結束時間
					 if (line.matches(LogPatterns.FINGER_END_TIME)) {
						 String time = line.replaceAll(LogPatterns.FINGER_END_TIME, "$1");
						 passengerLog[0].setFingerEndTime(time);
						 passengerLog[0].setEndFingerList(line);						 
					 }

					 //辨識結束時間
					 if (line.matches(LogPatterns.IDENTIFY_END_TIME)) {
						 String time = line.replaceAll(LogPatterns.IDENTIFY_END_TIME, "$1");
						 passengerLog[0].setIdentifyEndTime(time);
						 passengerLog[0].setFinishVerifyList(line);
						 
						 if(StringUtils.isNotBlank(passengerLog[0].getFaceHasResultSTime()) && StringUtils.isBlank(passengerLog[0].getFaceHasResultETime())) { //若無臉辨結束時間, 表示只有一次辨識, 若有指辨就拿指辨開始當結束
							 passengerLog[0].setFaceHasResultETime(time); //若到辨識結束都無臉辨結束時間, 就用辨識結束當臉辨結束時間
						 }
					 }
					 
					 
					 //辨識失敗分數
					 if (line.matches(LogPatterns.IDENTIFY_FAIL_SCORE)) {
						 String scores = line.replaceAll(LogPatterns.IDENTIFY_FAIL_SCORE, "$1:$2");
						 String[] scoreArr = scores.split(":", -1);
						 passengerLog[0].setIdentifyResult(fail);
						 passengerLog[0].setFaceScore(scoreArr[0]);
						 passengerLog[0].setFingerScore(scoreArr[1]);
					 }
					 
					 //辨識成功分數
					 if (line.matches(LogPatterns.IDENTIFY_PASS_SCORE)) {
						 String scores = line.replaceAll(LogPatterns.IDENTIFY_PASS_SCORE, "$1:$2");
						 String[] scoreArr = scores.split(":", -1);
						 passengerLog[0].setIdentifyResult(success);
						 passengerLog[0].setFaceScore(scoreArr[0]);
						 passengerLog[0].setFingerScore(scoreArr[1]);
					 }
					 
					 //上傳查驗系統開始時間
					 if (line.matches(LogPatterns.UPD_HYWEB_START_TIME)) {
						 String time = line.replaceAll(LogPatterns.UPD_HYWEB_START_TIME, "$1");
						 passengerLog[0].setUpdHywebStartTime(time);
					 }
					 
					//上傳查驗系統結束時間
					 if (line.matches(LogPatterns.UPD_HYWEB_END_TIME)) {
						 String time = line.replaceAll(LogPatterns.UPD_HYWEB_END_TIME, "$1");
						 passengerLog[0].setUpdHywebEndTime(time);
					 }
					 
					//上傳MNR開始時間
					 if (line.matches(LogPatterns.UPD_MNR_START_TIME)) {
						 String time = line.replaceAll(LogPatterns.UPD_MNR_START_TIME, "$1");
						 passengerLog[0].setUpdMnrStartTime(time);
					 }
					 
					//上傳MNR結束時間
					 if (line.matches(LogPatterns.UPD_MNR_END_TIME)) {
						 String time = line.replaceAll(LogPatterns.UPD_MNR_END_TIME, "$1");
						 passengerLog[0].setUpdMnrEndTime(time);
					 }
					 
					 //第二道閘門開啟時間
					 if (line.matches(LogPatterns.DOOR2A_OPEN_TIME)) {
						 String time = line.replaceAll(LogPatterns.DOOR2A_OPEN_TIME, "$1");
						 passengerLog[0].setDoor2aOpenTime(time);
					 }
					 
					 if (line.matches(LogPatterns.DOOR2A_CLOSE_TIME)) {
						 if (StringUtils.isNotBlank(passengerLog[0].getDoor2aOpenTime())) {
							 String time = line.replaceAll(LogPatterns.DOOR2A_CLOSE_TIME, "$1");
							 passengerLog[0].setDoor2aCloseTime(time);
						 }
					 }
					 
					 //系統重置開始時間
					 if (line.matches(LogPatterns.RESET_SYSTEM_START)) {
						 String time = line.replaceAll(LogPatterns.RESET_SYSTEM_START, "$1");
						 passengerLog[0].setResetSystemTime(time);
					 }
					 
					 //疑似兩人判斷
					 if (line.matches(LogPatterns.TWO_PERSONAL)) {
						 passengerLog[0].setTwoPersonal("V");
					 }
					 
					 //第一道門開啟時間(只抓第一次)
					 if (line.matches(LogPatterns.DOOR1A_OPEN_TIME)) {
						 if (statusNow.contains("N") || statusNow.contains("A")) {
							 if (StringUtils.isBlank(passengerLog[0].getDoor1aOpenTime())) {
								 String time = line.replaceAll(LogPatterns.DOOR1A_OPEN_TIME, "$1");
								 passengerLog[0].setDoor1aOpenTime(time);
							 }
						 }
					 }
					 
					 //第一道門關閉時間(只抓第一次), 有開門才有關門
					 /**
					      *   開門的方向有分1A(往內), 1B(往外), 但是關門的LOG是相同的, 所以需要判斷
					  */
					 if (line.matches(LogPatterns.DOOR1A_CLOSE_TIME)) {
						 if (StringUtils.isNotBlank(passengerLog[0].getDoor1aOpenTime())) {
							 if (StringUtils.isBlank(passengerLog[0].getDoor1aCloseTime())) {
								 String time = line.replaceAll(LogPatterns.DOOR1A_CLOSE_TIME, "$1");
								 passengerLog[0].setDoor1aCloseTime(time);
							 }
						 }
					 }

					 if(line.matches(LogPatterns.BoardingPass_Start)){
						 String time = line.replaceAll(LogPatterns.BoardingPass_Start, "$1");
						 if (StringUtils.isBlank(passengerLog[0].getBoardingpassSTime()) || LogPatterns.BoardingPass_Start2 !=null && StringUtils.isBlank(passengerLog[0].getBoardingpassETime())) {
							 passengerLog[0].setBoardingpassSTime(time);
						 }
					 }

					 if(line.matches(LogPatterns.BoardingPass_End)){
						 boardingA = true;
						 String time = line.replaceAll(LogPatterns.BoardingPass_End, "$1");
						 if (StringUtils.isBlank(passengerLog[0].getBoardingpassETime())){
							 passengerLog[0].setBoardingpassETime(time);
						 }
					 }

					 if(line.matches(LogPatterns.BoardingPass_Start2)){
						 String time = line.replaceAll(LogPatterns.BoardingPass_Start2, "$1");
						 if (StringUtils.isBlank(passengerLog[0].getBoardingpassSTime())) {
							 passengerLog[0].setBoardingpassSTime(time);
						 }
					 }

					 if(line.matches(LogPatterns.BoardingPass_End2)){
						 String time = line.replaceAll(LogPatterns.BoardingPass_End2, "$1");
						 if (StringUtils.isBlank(passengerLog[0].getBoardingpassETime())) {
							 passengerLog[0].setBoardingpassETime(time);
						 }
					 }

					 //體溫檢測時間
					 if(line.matches(LogPatterns.Body_Temperature_Start) && statusNow.contains("N01")){

						 String time = line.replaceAll(LogPatterns.Body_Temperature_Start, "$1");

						 if(StringUtils.isBlank(passengerLog[0].getBodyTemperatureTime())){
							 passengerLog[0].setBodyTemperatureTime(time);
						 }

					 }
				 }
			 }

			 System.out.print("\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rtnLogs;
	}
	
	/**
	 * 以現有的欄位資料判斷缺少的欄位資料
	 * @param passengerLog
	 */
	private static void processDetail(PassengerLog passengerLog) {
		
		/*處理旅客通關起訖時間*/
		passengerLog.setStartTime(passengerLog.getEmrpStartTime()); //開始讀取護照為通關開始
		
		if (StringUtils.isNotEmpty(passengerLog.getDoor2aOpenTime())) {
			passengerLog.setEndTime(passengerLog.getDoor2aOpenTime()); //第二道閘門開啟為通關結束			
		} else {
			passengerLog.setEndTime(passengerLog.getLastTime());
		}

		
		/*處理臉指辨起訖時間*/
		if (StringUtils.isNotEmpty(passengerLog.getFingerStartTime())) {
			//指辨開始時間即是臉辨結束時間, 辨識完成時間即指辨結束時間
			passengerLog.setFaceEndTime(passengerLog.getFingerStartTime());
			//passengerLog.setFingerEndTime(passengerLog.getFingerEndTime());
		} else {
			//沒指辨時間,辨識完成時間即臉辨結束時間
			if (StringUtils.isNotEmpty(passengerLog.getFaceStartTime())) { //有出現沒臉指辨開始時間就結束檢查, 所以加這個判斷
				passengerLog.setFaceEndTime(passengerLog.getIdentifyEndTime());				
			}

		}
		
		/*處理辨識結果*/
		if (StringUtils.isNotEmpty(passengerLog.getIdentifyResult())) {
			if(success.equals(passengerLog.getIdentifyResult())) {
				//有指紋辨識表示臉辨失敗了, 所以是指辨成功
				if (StringUtils.isNotEmpty(passengerLog.getFingerStartTime())) {
					passengerLog.setFingerIdentifyResult(success);
					passengerLog.setFaceIdentifyResult(fail);
				} else {
					passengerLog.setFaceIdentifyResult(success);
				}
			} else {
				passengerLog.setFaceIdentifyResult(fail);
				passengerLog.setFingerIdentifyResult(fail);
			}
		}
	}
	
	private static void calSeconds(PassengerLog passengerLog) {
		/*計算通關時間*/
		passengerLog.setTotalSeconds(betweenSeconds(passengerLog.getStartTime(), passengerLog.getEndTime()));

		/*計算護照讀取時間*/
		passengerLog.setEmrpSeconds(betweenSeconds(passengerLog.getEmrpStartTime(), passengerLog.getEmrpEndTime()));
		
		/*計算讀取查驗時間*/
		passengerLog.setQryHywebSeconds(betweenSeconds(passengerLog.getQryHywebStartTime(), passengerLog.getQryHywebEndTime()));
		
		/*計算MNR讀取時間*/
		passengerLog.setQryMnrSeconds(betweenSeconds(passengerLog.getQryMnrStartTime(), passengerLog.getQryMnrEndTime()));
		
		/*計算臉辨讀取時間*/
		passengerLog.setFaceScends(betweenSeconds(passengerLog.getFaceStartTime(), passengerLog.getFaceEndTime()));
		
		/*計算指辨時間*/
		passengerLog.setFingerSeconds(betweenSeconds(passengerLog.getFingerStartTime(), passengerLog.getFingerEndTime()));
		
		/*計算上傳查驗系統時間*/
		passengerLog.setUpdHywebSeconds(betweenSeconds(passengerLog.getUpdHywebStartTime(), passengerLog.getUpdHywebEndTime()));
		
		/*計算上傳MNR時間*/
		passengerLog.setUpdMnrSeconds(betweenSeconds(passengerLog.getUpdMnrStartTime(), passengerLog.getUpdMnrEndTime()));
		
		//計算第一道門開啟到關閉的時間
		passengerLog.setDoor1aSeconds(betweenSeconds(passengerLog.getDoor1aOpenTime(), passengerLog.getDoor1aCloseTime()));
		
		//計算第二道門開啟到關閉的時間
		passengerLog.setDoor2aSeconds(betweenSeconds(passengerLog.getDoor2aOpenTime(), passengerLog.getDoor2aCloseTime()));
		
		//計算證件檢核時間(A), 護照機啟動 ~ eGate送查驗系統
		passengerLog.setReportASeconds(betweenSeconds(passengerLog.getEmrpStartTime(), passengerLog.getQryHywebStartTime()));
		
		//計算身份查驗時間 (B), eGate送查驗系統 ~ eGate收到查驗系統回覆
		passengerLog.setReportBSeconds(betweenSeconds(passengerLog.getQryHywebStartTime(), passengerLog.getQryHywebEndTime()));
		
		//計算閘門開啟時間(C) , 臉部辨識結束時間~第一道門開啟
		passengerLog.setReportCSeconds(betweenSeconds(passengerLog.getFaceHasResultETime(), passengerLog.getDoor1aOpenTime()));
		
		//計算旅客使用時間(D), 第一道門開啟 ~ 第一道門關閉
		passengerLog.setReportDSeconds(betweenSeconds(passengerLog.getDoor1aOpenTime(), passengerLog.getDoor1aCloseTime()));
		
		//計算身份辨識時間(E), 第一道門關閉, 第二道門開啟
		passengerLog.setReportESeconds(betweenSeconds(passengerLog.getDoor1aCloseTime(), passengerLog.getDoor2aOpenTime()));
		
		//計算臉辨時間(E1), 第一道門關閉~臉辨結束
		passengerLog.setReportE1Seconds(betweenSeconds(passengerLog.getDoor1aCloseTime(), passengerLog.getFaceEndTime()));
		
		//計算臉部擷取時間(E11),分數為0
		passengerLog.setFaceNoResultSecs(betweenSeconds(passengerLog.getFaceNoResultSTime(), passengerLog.getFaceNoResultETime()));
		
		//計算臉部辨識時間(E12),有分數
		passengerLog.setFaceHasResultSecs(betweenSeconds(passengerLog.getFaceHasResultSTime(), passengerLog.getFaceHasResultETime()));
		
		//計算指辨時間(E2), 指辨開始~指辨結束
		passengerLog.setReportE2Seconds(betweenSeconds(passengerLog.getFingerStartTime(), passengerLog.getFingerEndTime()));
		
		//計算第二道門開啟時間(F), 辨識結束!~第二道門開啟
		passengerLog.setReportFSeconds(betweenSeconds(passengerLog.getIdentifyEndTime(), passengerLog.getDoor2aOpenTime()));
		
		//計算最後一次右手指紋辨識的時間
		passengerLog.setLastFinger01IdentifySeconds(betweenSeconds(passengerLog.getFinger01IdentifyPreTime(), passengerLog.getFinger01IdentifyThisTime()));
		
		//計算最後一次左手指紋辨識的時間
		passengerLog.setLastFinger02IdentifySeconds(betweenSeconds(passengerLog.getFinger02IdentifyPreTime(), passengerLog.getFinger02IdentifyThisTime()));
		
		//計算壓指紋(E21)Result=-1的秒數
		passengerLog.setFingerPressNoResultSecs(betweenSeconds(passengerLog.getFingerPressSTimeNoResult(), passengerLog.getFingerPressETimeNoResult()));

		//計算辨識指紋(E22)Result=1的秒數
		//passengerLog.setFingerPressHasResultSecs(betweenSeconds(passengerLog.getFingerPressSTimeHasResult(), passengerLog.getFingerPressETimeHasResult()));
		//2020-01-06 改以指辨分數最高的那個時間點當作辨識指紋時間
		passengerLog.setFingerPressHasResultSecs(E22(passengerLog));

		//計算boardingpass秒數
		passengerLog.setBoardingpassSecs(betweenSeconds(passengerLog.getBoardingpassSTime(), passengerLog.getBoardingpassETime()));

		//計算BodyTemperature秒數
		passengerLog.setBodyTemperatureSecs(betweenSeconds(passengerLog.getStartTime(),passengerLog.getBodyTemperatureTime()));

		if (StringUtils.isNotBlank(passengerLog.getIdentifyResult()) && success.equals(passengerLog.getIdentifyResult())) {
			//計算A+C+E
			Double dd = CommonUtils.calculateSecond(
					passengerLog.getReportASeconds(),
					passengerLog.getReportCSeconds(),
					passengerLog.getReportESeconds());
			passengerLog.setReportACESeconds(dd);
			
			//計算A+C+E12+E22+F
			Double dd1 = CommonUtils.calculateSecond(
					passengerLog.getEmrpSeconds(),
					passengerLog.getBoardingpassSecs(),
					passengerLog.getFaceHasResultSecs(), //E12
					passengerLog.getQryMnrSeconds(),
					passengerLog.getReportCSeconds());
			passengerLog.setReportACE1E2FSeconds(dd1);			
		}
		
		if (StringUtils.isNotBlank(passengerLog.getIdentifyResult()) && success.equals(passengerLog.getIdentifyResult())) {
			//計算A+B+C+D+E
			Double dd = CommonUtils.calculateSecond(
					passengerLog.getReportASeconds(),
					passengerLog.getReportBSeconds(),
					passengerLog.getReportCSeconds(),
					passengerLog.getReportDSeconds(),
					passengerLog.getReportESeconds());
			passengerLog.setReportABCDESeconds(dd);
			
			//計算A+B+C+D+E11+E12+E21+E22+F
//			Double dd1 = CommonUtils.calculateSecond(
//					passengerLog.getReportASeconds(),
//					passengerLog.getReportBSeconds(),
//					passengerLog.getReportCSeconds(),
//					passengerLog.getReportDSeconds(),
//					passengerLog.getFaceNoResultSecs(), //E11
//					passengerLog.getFaceHasResultSecs(), //E12
//					passengerLog.getFingerPressNoResultSecs(), //E21
//					passengerLog.getFingerPressHasResultSecs(), //E22
//					passengerLog.getReportFSeconds());
			Double dd1;
			if(boardingA){
				dd1 = betweenSeconds(passengerLog.getEmrpStartTime(),passengerLog.getDoor1aCloseTime());
				passengerLog.setReportABCDE1E2FSeconds(dd1);
			}else{
				dd1 = betweenSeconds(passengerLog.getBoardingpassSTime(),passengerLog.getDoor1aCloseTime());
				passengerLog.setReportABCDE1E2FSeconds(dd1);

			}
		}
	}
	
	/**
	 * 取指辨有分數且是最高分數的
	 * @param passengerLog
	 * @return
	 */
	private static Double E22(PassengerLog passengerLog) {
		
		DoubleAdder rightDa = new DoubleAdder();
		DoubleAdder leftDa = new DoubleAdder();
		//1.找出右手有結果的LOG(finger01LogList
		//passengerLog.getFinger01LogList()
		//2.找出右手這個比這個結果晚的第一個比對結果
		//passengerLog.getFinger01ScoreLogList()
		//3計算時間差, 累計到暫存變數
		//同樣方式改用左手
		List<String> hasResultList01 = passengerLog.getFinger01LogList().stream()
				.filter(l -> l.matches(LogPatterns.FINGER_PRESS_HAS_RESULT))
				.collect(Collectors.toList());
		
		//用Result=1的Log時間區間來尋找 分數, 若有分數表示有比對時間, 沒有就是沒有比對時間
		for(int i=0; i<hasResultList01.size(); i++) { //右手
			String logTime = hasResultList01.get(i).replaceAll(LogPatterns.FINGER_PRESS_HAS_RESULT, "$1");
			LocalDateTime logDt = LocalDateTime.parse(logTime, DateTimeFormatter.ofPattern(dateTime));
			LocalDateTime logDtNext = null;
			if ((i+1) < hasResultList01.size()) {
				String logTimeNext = hasResultList01.get(i+1).replaceAll(LogPatterns.FINGER_PRESS_HAS_RESULT, "$1");
				logDtNext = LocalDateTime.parse(logTimeNext, DateTimeFormatter.ofPattern(dateTime));
			}
			LocalDateTime finalLogDtNext = logDtNext;
			
			//尋找有比對分數的log
			String scoreLine = passengerLog.getFinger01ScoreLogList().stream().filter(l -> {
				String scoreTime = l.replaceAll(LogPatterns.Finger01_SCORE_LINE, "$1");
				LocalDateTime scoreDt = LocalDateTime.parse(scoreTime, DateTimeFormatter.ofPattern(dateTime));
				if (finalLogDtNext != null) {
					return scoreDt.isAfter(logDt) && scoreDt.isBefore(finalLogDtNext);					
				}
				return scoreDt.isAfter(logDt);
			}).findFirst()
			.orElse(null);		
			if (StringUtils.isNotBlank(scoreLine)) {
				String scoreTime = scoreLine.replaceAll(LogPatterns.Finger01_SCORE_LINE, "$1");
				rightDa.add(betweenSeconds(logTime, scoreTime));
			}			
			
		}
		
		List<String> hasResultList02 = passengerLog.getFinger02LogList().stream()
				.filter(l -> l.matches(LogPatterns.FINGER_PRESS_HAS_RESULT))
				.collect(Collectors.toList());		
		
		for(int i=0; i<hasResultList02.size(); i++) { //左手
			String logTime = hasResultList02.get(i).replaceAll(LogPatterns.FINGER_PRESS_HAS_RESULT, "$1");
			LocalDateTime logDt = LocalDateTime.parse(logTime, DateTimeFormatter.ofPattern(dateTime));
			LocalDateTime logDtNext = null;
			if ((i+1) < hasResultList02.size()) {
				String logTimeNext = hasResultList02.get(i+1).replaceAll(LogPatterns.FINGER_PRESS_HAS_RESULT, "$1");
				logDtNext = LocalDateTime.parse(logTimeNext, DateTimeFormatter.ofPattern(dateTime));
			}
			LocalDateTime finalLogDtNext = logDtNext;
			
			//尋找有比對分數的log
			String scoreLine = passengerLog.getFinger02ScoreLogList().stream().filter(l -> {
				String scoreTime = l.replaceAll(LogPatterns.Finger02_SCORE_LINE, "$1");
				LocalDateTime scoreDt = LocalDateTime.parse(scoreTime, DateTimeFormatter.ofPattern(dateTime));
				if (finalLogDtNext != null) {
					return scoreDt.isAfter(logDt) && scoreDt.isBefore(finalLogDtNext);					
				}
				return scoreDt.isAfter(logDt);
			}).findFirst()
			.orElse(null);		
			if (StringUtils.isNotBlank(scoreLine)) {
				String scoreTime = scoreLine.replaceAll(LogPatterns.Finger02_SCORE_LINE, "$1");
				leftDa.add(betweenSeconds(logTime, scoreTime));
			}			
		}		

		if (rightDa.sum() == 0.0 && leftDa.sum() == 0.0) {
			return null;
		}
		//return Math.max(rightDa.sum(), leftDa.sum());
		return rightDa.sum() + leftDa.sum();
		
//		if (StringUtils.isBlank(passengerLog.getFingerScore())) {
//			return null;
//		}
//		
//		if (passengerLog.getFingerScore().equals("-1,-1")) {
//			return null;
//		}
//		
//		String[] score = passengerLog.getFingerScore().split(",", -1);
//		int score01 = Integer.parseInt(score[0].trim());
//		int score02 = Integer.parseInt(score[1].trim());
//		if (score01 >= score02) { //取右手該次分數的比對間隔時間, 就是回傳分數前的那次FingerResult Event, 以及那次FingerResult Event的前一次的間隔時間
//			return parserFingerHighestScoreGap(passengerLog.getFinger01ScoreLogList(), 
//					passengerLog.getFinger01LogList(), 
//					LogPatterns.Finger01_SCORE_LINE, 
//					LogPatterns.FINGER01_IDENTIFY_THIS_TIME, score01, passengerLog.getFingerStartTime(), passengerLog);
//		} else { //取左右
//			return parserFingerHighestScoreGap(passengerLog.getFinger02ScoreLogList(), 
//					passengerLog.getFinger02LogList(), 
//					LogPatterns.Finger02_SCORE_LINE, 
//					LogPatterns.FINGER02_IDENTIFY_THIS_TIME, score02, passengerLog.getFingerStartTime(), passengerLog);
//			
//		}
	}
	
	/**
	 * 用來尋找旅客指辨最高分的那一次的時間, 想法:由結果找出左右手指辨分數, 用最高分的那一指, 往回推上一次指紋機回傳訊息的時間點(FingerResult Event)
	 * ,再往前找上上一次的FingerResult Event, 就是此次指紋辨識的時間間隔, 有個例外是中間遇到疑似兩人, 此時上上一次的FingerResult Event 要改用比較接近上一次時間點的指紋機重啟時間(Type=S:A)
	 * @param scoreList
	 * @param logList
	 * @param scorePattern
	 * @param logPattern
	 * @param score
	 * @param startTime
	 * @param passengerLog
	 * @return
	 */
	private static Double parserFingerHighestScoreGap(List<String> scoreList, List<String> logList, String scorePattern, String logPattern, int score, String startTime, PassengerLog passengerLog) {
		
		String scoreLine = scoreList.stream().filter(str -> {
			String scoreStr = str.replaceAll(scorePattern, "$2");
			int iScore = Integer.parseInt(scoreStr.trim());
			return iScore == score;
		}).findFirst()
		.orElse(null); //理論上不會有null
		
		String scoreTimeStr = scoreLine.replaceAll(scorePattern, "$1");
		LocalTime scoreTime = CommonUtils.DateTimeToLocalTime(scoreTimeStr); 
		
		List<String> xList = logList.stream().filter(str -> { //找出分數時間前的所有log
			LocalTime lineTime = CommonUtils.DateTimeToLocalTime(str.replaceAll(logPattern, "$1")); 
			return scoreTime.isAfter(lineTime);
		}).collect(Collectors.toList());
		
		List<String> saTimeList = passengerLog.getStartFingerList().stream().filter(str -> {
			LocalTime lineTime = CommonUtils.DateTimeToLocalTime(str.replaceAll(LogPatterns.FINGER_START_TIME, "$1"));
			return scoreTime.isAfter(lineTime);
		}).collect(Collectors.toList());
		
		
		if (xList.size() == 1) {
			String preTime = startTime;
			String thisTime = xList.get(0).replaceAll(logPattern, "$1");
			passengerLog.setFingerPressSTimeHasResult(preTime);
			passengerLog.setFingerPressETimeHasResult(thisTime);
			return betweenSeconds(preTime, thisTime);
		} else if (xList.size() > 1){
			
			String preTime = xList.get(xList.size()-2).replaceAll(logPattern, "$1");
			
			//若中間遇到疑似兩人, 會在排除後重新啟動finger, 若有S:A time 比preTime更近, 則用S:A的這個時間
			if (saTimeList.size() > 0) {
				String preTime1 = saTimeList.get(saTimeList.size()-1).replaceAll(LogPatterns.FINGER_START_TIME, "$1");
				LocalTime lPreTime = CommonUtils.DateTimeToLocalTime(preTime);
				LocalTime lPreTime1 = CommonUtils.DateTimeToLocalTime(preTime1);
				if (lPreTime1.isAfter(lPreTime)) {
					preTime = preTime1;
				}
			}
			
			String thisTime = xList.get(xList.size()-1).replaceAll(logPattern, "$1");
			passengerLog.setFingerPressSTimeHasResult(preTime);
			passengerLog.setFingerPressETimeHasResult(thisTime);
			
			return betweenSeconds(preTime, thisTime);
		}
		return 0.0;
	}
	
	private static Double betweenSeconds(String startTime, String endTime) {
		if (StringUtils.isAnyEmpty(startTime, endTime)) {
			return null;
		}
		return CommonUtils.getSeconds(startTime,endTime);
	}
	
	public static PassengerAnalysis analysisLog(List<PassengerLog> logList) {
		PassengerAnalysis pa = new PassengerAnalysis();
		if (logList.size() == 0) {
			return pa;
		}
		
		try {
			String logDate = logList.get(0).getLogDate();
			pa.setLogDate(logDate);
			
			Integer totalCnt = logList.size();
			pa.setTotalCnt(totalCnt);
			
			//失敗筆數
			Long failCnt = logList
					.stream()
					.filter(pl -> !success.equals(pl.getIdentifyResult()))
					.count();
			pa.setFailCnt(failCnt.intValue());
			
			//失敗比例
			Double failPercentage = CommonUtils.calculatePercentage(totalCnt.doubleValue(), failCnt.doubleValue());
			pa.setFailPercentage(failPercentage);
			
			//成功筆數
			Long successCnt = logList
					.stream()
					.filter(pl -> success.equals(pl.getIdentifyResult()))
					.count();
			pa.setSuccessCnt(successCnt.intValue());
			
			//成功比例
			Double successPercentage = CommonUtils.calculatePercentage(totalCnt.doubleValue(), successCnt.doubleValue());
			pa.setSuccessPercentage(successPercentage);
			
			//A+C+E 小於等於10
			Long ace_N_le_10 = logList
					.stream()
					.filter(pl -> success.equals(pl.getIdentifyResult()))
					.filter(pl -> pl.getReportACESeconds() <= 10.0).count();
			pa.setAce_N_le_10(ace_N_le_10.intValue());
			
			//A+C+E12+E22+F 小於等於10
			Long ace1e2f_N_le_10 = logList
					.stream()
					.filter(pl -> success.equals(pl.getIdentifyResult()))
					.filter(pl -> pl.getReportACE1E2FSeconds() <= 10.0).count();
			pa.setAce1e2f_N_le_10(ace1e2f_N_le_10.intValue());			
			
			//A+C+E 10<N<=12
			Long ace_10_lt_N_le_12 = logList
					.stream()
					.filter(pl -> success.equals(pl.getIdentifyResult()))
					.filter(pl -> 10.0 < pl.getReportACESeconds() && pl.getReportACESeconds() <= 12.0).count();
			pa.setAce_10_lt_N_le_12(ace_10_lt_N_le_12.intValue());
			
			//A+C+E12+E22+F 10<N<=12
			Long ace1e2f_10_lt_N_le_12 = logList
					.stream()
					.filter(pl -> success.equals(pl.getIdentifyResult()))
					.filter(pl -> 10.0 < pl.getReportACE1E2FSeconds() && pl.getReportACE1E2FSeconds() <= 12.0).count();
			pa.setAce1e2f_10_lt_N_le_12(ace1e2f_10_lt_N_le_12.intValue());			
			
			//A+C+E 12<N<=15
			Long ace_12_lt_N_le_15 = logList
					.stream()
					.filter(pl -> success.equals(pl.getIdentifyResult()))
					.filter(pl -> 12.0 < pl.getReportACESeconds() && pl.getReportACESeconds() <= 15.0).count();
			pa.setAce_12_lt_N_le_15(ace_12_lt_N_le_15.intValue());
			
			//A+C+E12+E22+F 12<N<=15
			Long ace1e2f_12_lt_N_le_15 = logList
					.stream()
					.filter(pl -> success.equals(pl.getIdentifyResult()))
					.filter(pl -> 12.0 < pl.getReportACE1E2FSeconds() && pl.getReportACE1E2FSeconds() <= 15.0).count();
			pa.setAce1e2f_12_lt_N_le_15(ace1e2f_12_lt_N_le_15.intValue());			
			
			//A+C+E N>15
			Long ace_N_gt_15 = logList
					.stream()
					.filter(pl -> success.equals(pl.getIdentifyResult()))
					.filter(pl -> pl.getReportACESeconds() > 15.0).count();
			pa.setAce_N_gt_15(ace_N_gt_15.intValue());
			
			//A+C+E12+E22+F N>15
			Long ace1e2f_N_gt_15 = logList
					.stream()
					.filter(pl -> success.equals(pl.getIdentifyResult()))
					.filter(pl -> pl.getReportACE1E2FSeconds() > 15.0).count();
			pa.setAce1e2f_N_gt_15(ace1e2f_N_gt_15.intValue());			
			
			//A+B+C+D+E N <= 10
			Long abcde_N_le_10 = logList
					.stream()
					.filter(pl -> success.equals(pl.getIdentifyResult()))
					.filter(pl -> pl.getReportABCDESeconds() <= 10.0).count();
			pa.setAbcde_N_le_10(abcde_N_le_10.intValue());
			
			//A+B+C+D+E11+E12+E21+E22+F N <= 10
			Long abcde1e2f_N_le_10 = logList
					.stream()
					.filter(pl -> success.equals(pl.getIdentifyResult()))
					.filter(pl -> pl.getReportABCDE1E2FSeconds() <= 10.0).count();
			pa.setAbcde1e2f_N_le_10(abcde1e2f_N_le_10.intValue());
			
			//A+B+C+D+E 10<N<=12
			Long abcde_10_lt_N_le_12 = logList
					.stream()
					.filter(pl -> success.equals(pl.getIdentifyResult()))
					.filter(pl -> 10.0 < pl.getReportABCDESeconds() && pl.getReportABCDESeconds() <= 12.0).count();
			pa.setAbcde_10_lt_N_le_12(abcde_10_lt_N_le_12.intValue());
			
			//A+B+C+D+E11+E12+E21+E22+F 10<N<=12
			Long abcde1e2f_10_lt_N_le_12 = logList
					.stream()
					.filter(pl -> success.equals(pl.getIdentifyResult()))
					.filter(pl -> 10.0 < pl.getReportABCDE1E2FSeconds() && pl.getReportABCDE1E2FSeconds() <= 12.0).count();
			pa.setAbcde1e2f_10_lt_N_le_12(abcde1e2f_10_lt_N_le_12.intValue());
			
			//A+B+C+D+E 12<N<=15
			Long abcde_12_lt_N_le_15 = logList
					.stream()
					.filter(pl -> success.equals(pl.getIdentifyResult()))
					.filter(pl -> 12.0 < pl.getReportABCDESeconds() && pl.getReportABCDESeconds() <= 15.0).count();
			pa.setAbcde_12_lt_N_le_15(abcde_12_lt_N_le_15.intValue());
			
			//A+B+C+D+E11+E12+E21+E22+F 12<N<=15
			Long abcde1e2f_12_lt_N_le_15 = logList
					.stream()
					.filter(pl -> success.equals(pl.getIdentifyResult()))
					.filter(pl -> 12.0 < pl.getReportABCDE1E2FSeconds() && pl.getReportABCDE1E2FSeconds() <= 15.0).count();
			pa.setAbcde1e2f_12_lt_N_le_15(abcde1e2f_12_lt_N_le_15.intValue());
			
			//A+B+C+D+E N>15	
			Long abcde_N_gt_15 = logList
					.stream()
					.filter(pl -> success.equals(pl.getIdentifyResult()))
					.filter(pl -> pl.getReportABCDESeconds() > 15.0).count();
			pa.setAbcde_N_gt_15(abcde_N_gt_15.intValue());

			//A+B+C+D+E11+E12+E21+E22+F N>15
			Long abcde1e2f_N_gt_15 = logList
					.stream()
					.filter(pl -> success.equals(pl.getIdentifyResult()))
					.filter(pl -> pl.getReportABCDE1E2FSeconds() > 15.0).count();
			pa.setAbcde1e2f_N_gt_15(abcde1e2f_N_gt_15.intValue());
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return pa;
		
	}
	
	public static List<FingerLog> parseToFingerLog(List<PassengerLog> list) {

		list = list.stream()
				.filter(pl -> pl.getFinger01LogList().size() > 0)
				.collect(Collectors.toList());
		
		if (list.size() == 0) {
			return new ArrayList<>();
		}
		
		List<FingerLog> rtnList = new ArrayList<>();
		for(int x=0; x<list.size(); x++) {
		//list.forEach(pLog -> {
			try {
				PassengerLog pLog = list.get(x);
				String logDate = pLog.getLogDate();
				String eGateId = pLog.geteGateId();
				String md = pLog.getMainDocument();
				List<String> finger01List = pLog.getFinger01LogList();
				List<String> finger02List = pLog.getFinger02LogList();

				List<String> fingerScore01List = pLog.getFinger01ScoreLogList();
				List<String> fingerScore02List = pLog.getFinger02ScoreLogList();

				List<String> startFingerList = pLog.getStartFingerList();
				AtomicInteger cnt01 = new AtomicInteger(0);
				startFingerList = startFingerList.stream().filter(str -> { //用S:A判斷會同時抓到兩台指紋機啟動, 拿掉其中一台的log(比較早啟動那台), 避免資料重複
					return (cnt01.getAndIncrement() % 2 != 0);
				}).collect(Collectors.toList());
				
				List<String> endFingerList = pLog.getEndFingerList();
				AtomicInteger cnt02 = new AtomicInteger(0);
				endFingerList = endFingerList.stream().filter(str -> { //用S:B判斷會同時抓到兩台指紋機停止, 拿掉其中一台的log(比較早停止那台), 避免資料重複
					return (cnt02.getAndIncrement() % 2 != 0);
				}).collect(Collectors.toList());
				
				List<String> fihishVerifyList = pLog.getFinishVerifyList();

				/*
				 * 一次通關可能出現多次啟動/停止指紋辨識的情況, 先假設start 和 end 長度相同
				 */
				for(int i=0; i<startFingerList.size(); i++) {
					
					String startLine = startFingerList.get(i);
					String endLine = endFingerList.get(i);
					String finishVerifyLine = fihishVerifyList.get(i); 
					
					String startLogTime = startLine.replaceAll(LogPatterns.FINGER_START_TIME, "$1");
					String endLogTime = endLine.replaceAll(LogPatterns.FINGER_END_TIME, "$1");
					String finishVerifyTime = finishVerifyLine.replaceAll(LogPatterns.IDENTIFY_END_TIME, "$1");
					List<String> subFinger01List = findRangeLog(finger01List, LogPatterns.FINGER01_IDENTIFY_THIS_TIME, startLogTime, finishVerifyTime); //找出這次啟動/停止中間的擷取比對log
					List<String> subFinger02List = findRangeLog(finger02List, LogPatterns.FINGER02_IDENTIFY_THIS_TIME, startLogTime, finishVerifyTime); //找出這次啟動/停止中間的擷取比對log
					List<String> subFinger01ScoreList = findRangeLog(fingerScore01List, LogPatterns.Finger01_SCORE_LINE, startLogTime, finishVerifyTime); //找出這次啟動/停止中間的擷取比對log
					List<String> subFinger02ScoreList = findRangeLog(fingerScore02List, LogPatterns.Finger02_SCORE_LINE, startLogTime, finishVerifyTime); //找出這次啟動/停止中間的擷取比對log

					int idx = Math.max(subFinger01List.size(), subFinger02List.size());
					AtomicReference<String> preTime01 = new AtomicReference<>(startLogTime);
					AtomicReference<String> preTime02 = new AtomicReference<>(startLogTime);
					
					for(int j = 0; j < idx; j++) {
						FingerLog fingerLog = new FingerLog();
						fingerLog.seteGateId(eGateId);
						fingerLog.setLogDate(logDate);
						fingerLog.setMainDocument(md);
						if (subFinger01List.size()-1 >= j) {
							
							String log = subFinger01List.get(j).replaceAll(LogPatterns.FINGER01_IDENTIFY_THIS_TIME, "$1,$2");
							String[] arr = log.split(",", -1);
							fingerLog.setLogDateTime(arr[0]);
							fingerLog.setFinger01Quality(Integer.parseInt(arr[1]));
							fingerLog.setFinger01Seconds(CommonUtils.getSeconds(preTime01.get(), arr[0]));

							/** 由此次時間點與下次的時間點尋找落在中間的finger score log, 找不到表示沒比對分數 **/
							String startTime = arr[0];
							String endTime;
							if (j >= subFinger01List.size()-1) {
								//endTime = endLogTime; //分數可能出現在S:B之後, 改用辨識結束時間搜尋
								endTime = finishVerifyTime;
							} else {
								endTime = subFinger01List.get(j + 1).replaceAll(LogPatterns.FINGER01_IDENTIFY_THIS_TIME, "$1");
							}
							String score = findFingerScore(subFinger01ScoreList, LogPatterns.Finger01_SCORE_LINE, startTime, endTime);
							fingerLog.setFinger01Score(Integer.parseInt(score.trim()));
							preTime01.set(arr[0]);						
						}
						
						if (subFinger02List.size()-1 >= j) {
							String log = subFinger02List.get(j).replaceAll(LogPatterns.FINGER02_IDENTIFY_THIS_TIME, "$1,$2");
							String[] arr = log.split(",", -1);
							fingerLog.setLogDateTime(arr[0]);
							fingerLog.setFinger02Quality(Integer.parseInt(arr[1]));
							fingerLog.setFinger02Seconds(CommonUtils.getSeconds(preTime02.get(), arr[0]));
							
							String startTime = arr[0];
							String endTime;
							if (j >= subFinger02List.size()-1) {
								//endTime = endLogTime; //分數可能出現在S:B之後, 改用辨識結束時間搜尋
								endTime = finishVerifyTime;

							} else {
								endTime = subFinger02List.get(j + 1).replaceAll(LogPatterns.FINGER02_IDENTIFY_THIS_TIME, "$1");
							}
							String score = findFingerScore(subFinger02ScoreList, LogPatterns.Finger02_SCORE_LINE, startTime, endTime);
							fingerLog.setFinger02Score(Integer.parseInt(score.trim()));
							preTime02.set(arr[0]);						
							
						}			
						rtnList.add(fingerLog);						
					}
				}				
			} catch (Exception e) {
				//e.printStackTrace();
			}

		//});
		}
		return rtnList;
	}
	
	private static List<String> findRangeLog(List<String> list, String logPattern, String startTime, String endTime) {
		LocalDateTime start = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern(dateTime));
		LocalDateTime end = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern(dateTime));

		return list.stream().filter(str -> {
			String time = str.replaceAll(logPattern, "$1");
			LocalDateTime aTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(dateTime));
			return start.isBefore(aTime) && end.isAfter(aTime);
		}).collect(Collectors.toList());
	}
	
	/**
	 * finger如果有分數會出現在start(本次)與end(下一次) log的中間，以此方式來判斷finger有沒有回傳分數
	 * @param list
	 * @param logPattern
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	private static String findFingerScore(List<String> list, String logPattern, String startTime, String endTime) {
		LocalDateTime start = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern(dateTime));
		LocalDateTime end = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern(dateTime));
		
		return list.stream().filter(str -> {
			String time = str.replaceAll(logPattern, "$1");
			LocalDateTime aTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(dateTime));
			return start.isBefore(aTime) && end.isAfter(aTime);
		}).map(str -> {
			String score = str.replaceAll(logPattern, "$2");
			return score;
		}).findFirst()
		.orElse("-1");
	}
}
