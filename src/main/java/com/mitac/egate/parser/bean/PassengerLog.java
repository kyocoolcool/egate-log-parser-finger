package com.mitac.egate.parser.bean;

import java.util.ArrayList;
import java.util.List;

public class PassengerLog {

	private String mainDocument; //證件號碼
	private String startTime; //使用閘道開始時間
	private String endTime; //使用閘道結束時間
	private Double totalSeconds;
	
	private String emrpStartTime; //讀取護照開始時間
	private String emrpEndTime; //讀取護照結束時間
	private Double emrpSeconds;
	
	private String qryHywebStartTime; //讀取查驗資料開始時間
	private String qryHywebEndTime; //讀取查驗資料結束時間
	private Double qryHywebSeconds;
	
	private String qryHywebRcode; //查驗系統回傳Rcode
	
	private String qryMnrStartTime; //讀取智慧閘道管理系統開始時間
	private String qryMnrEndTime; //讀取智慧閘道管理系統結束時間
	private Double qryMnrSeconds;
		
	private String faceStartTime; //臉辨開始時間
	private String faceEndTime; //臉辨結束時間
	private Double faceScends;
	
	private String fingerStartTime; //指辨開始時間
	private String fingerEndTime; //指辨結束時間
	private Double fingerSeconds;
	
	private String updHywebStartTime; //上傳查驗資料開始時間
	private String updHywebEndTime; //上傳查驗資料開始時間
	private Double updHywebSeconds;
	
	private String updMnrStartTime; //上傳智慧閘道管理系統開始時間
	private String updMnrEndTime; //上傳智慧閘道管理系統結束時間
	private Double updMnrSeconds;
	
	private String door2aOpenTime; //第二道閘門開門時間
	private String door2aCloseTime; //第二道閘門關門時間
	private Double door2aSeconds; //開關門秒數
	
	private String resetSystemTime; //系統重置時間
	private String identifyEndTime; //辨識結束時間
	private String lastTime; //Log最後狀態歸為N00的時間
	
	private String identifyResult; //通關是否成功
	private String faceIdentifyResult; //臉辨是否成功
	private String fingerIdentifyResult; //指辨是否成功
	private String faceScore; //臉辨分數
	private String fingerScore; //只辨分數
	private String twoPersonal; //是否兩人同行
	
	private String door1aOpenTime; //第一道門開啟時間
	private String door1aCloseTime; //第一道門開啟時間
	private Double door1aSeconds; //開關門秒數
	
	private Double reportASeconds; //證件檢核時間, 護照機啟動 ~ eGate送查驗系統
	private Double reportBSeconds; //身分查驗時間, eGate送查驗系統 ~ eGate收到查驗系統回覆
	private Double reportCSeconds; //第一道門開啟時間, eGate收到查驗系統回覆 ~ 第一道門開啟
	private Double reportDSeconds; //旅客使用時間, 第一道門開啟 ~ 第一道門關閉
	private Double reportESeconds; //身份辨識時間, 第一道門關閉 ~ 第二道門開啟
	private Double reportE1Seconds; //臉辨時間, 第一道門關閉 ~ 臉辨結束
	private Double reportE2Seconds; //指辨時間, 指辨開始 ~ 指辨結束
	private Double reportFSeconds; //"第二道門開啟時間(F), 指辨結束到第二道閘門開啟
	private Double reportACESeconds;
	private Double reportABCDESeconds;
	private Double reportACE1E2FSeconds;
	private Double reportABCDE1E2FSeconds;
	private String nationality; //國籍
	private String logDate;
	private String emrpExpireDate; //護照有效日期
	private String portType; //機場出入境別
	private String eGateId; //閘道ID
	
	private String finger01IdentifyThisTime;
	private String finger02IdentifyThisTime;
	
	private String finger01IdentifyPreTime;
	private String finger02IdentifyPreTime;
	
	private Double lastFinger01IdentifySeconds;
	private Double lastFinger02IdentifySeconds;
	
	private String fingerPressSTimeNoResult; //旅客開始壓指紋,且Result=-1
	private String fingerPressETimeNoResult; //旅客結束壓指紋,且Result=-1
	private Double fingerPressNoResultSecs; //旅客壓指紋Result=-1的秒數
	
	private String fingerPressSTimeHasResult;//旅客開始壓指紋,且Result=1時間
	private String fingerPressETimeHasResult;//旅客結束壓指紋,且Result=1時間
	private Double fingerPressHasResultSecs; //旅客壓指紋Result=1的秒數
	
	private String faceNoResultSTime; //旅客擷取臉部且分數為0開始
	private String faceNoResultETime; //旅客擷取臉部且分數為0結束
	private Double faceNoResultSecs;
	private String faceHasResultSTime; //旅客擷取臉部且有分數開始
	private String faceHasResultETime; //旅客擷取臉部且有分數結束
	private Double faceHasResultSecs;

	private String boardingpassSTime; //登機證開始時間
	private String boardingpassETime; //登機證結束時間
	private Double boardingpassSecs;

	private String bodyTemperatureTime; //體溫時間
	private Double bodyTemperatureSecs;

	private String boardingorchecking;//看log有沒有護照機那一段

	public String getBoardingorchecking() {
		return boardingorchecking;
	}

	public void setBoardingorchecking(String boardingorchecking) {
		this.boardingorchecking = boardingorchecking;
	}

	/** 以下紀錄 外籍旅客 擷取指紋的次數與分數,用以計算每次擷取比對時間 **/
	/* 發現一次通關可能會出現一次以上啟動,停止指紋辨識的動作 */
	private List<String> startFingerList = new ArrayList<>();
	private List<String> endFingerList = new ArrayList<>();
	private List<String> finger01List = new ArrayList<>();
	private List<String> finger02List = new ArrayList<>();
	private List<String> finger01ScoreList = new ArrayList<>();
	private List<String> finger02ScoreList = new ArrayList<>();
	private List<String> finishVerifyList = new ArrayList<>();
	
//	private List<String> finger01MixList; //紀錄finger01的回傳訊息,與回傳分數兩種Log
//	private List<String> finger02MixList;
	
	public PassengerLog(String portType,String eGateId) {
		this.portType = portType;
		this.eGateId = eGateId;
	}
	
	public String getMainDocument() {
		return mainDocument;
	}
	public void setMainDocument(String mainDocument) {
		this.mainDocument = mainDocument;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Double getTotalSeconds() {
		return totalSeconds;
	}
	public void setTotalSeconds(Double totalSeconds) {
		this.totalSeconds = totalSeconds;
	}
	public String getEmrpStartTime() {
		return emrpStartTime;
	}
	public void setEmrpStartTime(String emrpStartTime) {
		this.emrpStartTime = emrpStartTime;
	}
	public String getEmrpEndTime() {
		return emrpEndTime;
	}
	public void setEmrpEndTime(String emrpEndTime) {
		this.emrpEndTime = emrpEndTime;
	}
	public Double getEmrpSeconds() {
		return emrpSeconds;
	}
	public void setEmrpSeconds(Double emrpSeconds) {
		this.emrpSeconds = emrpSeconds;
	}
	public String getQryHywebStartTime() {
		return qryHywebStartTime;
	}
	public void setQryHywebStartTime(String qryHywebStartTime) {
		this.qryHywebStartTime = qryHywebStartTime;
	}
	public String getQryHywebEndTime() {
		return qryHywebEndTime;
	}
	public void setQryHywebEndTime(String qryHywebEndTime) {
		this.qryHywebEndTime = qryHywebEndTime;
	}
	public Double getQryHywebSeconds() {
		return qryHywebSeconds;
	}
	public void setQryHywebSeconds(Double qryHywebSeconds) {
		this.qryHywebSeconds = qryHywebSeconds;
	}
	public String getQryHywebRcode() {
		return qryHywebRcode;
	}
	public void setQryHywebRcode(String qryHywebRcode) {
		this.qryHywebRcode = qryHywebRcode;
	}
	public String getQryMnrStartTime() {
		return qryMnrStartTime;
	}
	public void setQryMnrStartTime(String qryMnrStartTime) {
		this.qryMnrStartTime = qryMnrStartTime;
	}
	public String getQryMnrEndTime() {
		return qryMnrEndTime;
	}
	public void setQryMnrEndTime(String qryMnrEndTime) {
		this.qryMnrEndTime = qryMnrEndTime;
	}
	public Double getQryMnrSeconds() {
		return qryMnrSeconds;
	}
	public void setQryMnrSeconds(Double qryMnrSeconds) {
		this.qryMnrSeconds = qryMnrSeconds;
	}
	public String getFaceStartTime() {
		return faceStartTime;
	}
	public void setFaceStartTime(String faceStartTime) {
		this.faceStartTime = faceStartTime;
	}
	public String getFaceEndTime() {
		return faceEndTime;
	}
	public void setFaceEndTime(String faceEndTime) {
		this.faceEndTime = faceEndTime;
	}
	public Double getFaceScends() {
		return faceScends;
	}
	public void setFaceScends(Double faceScends) {
		this.faceScends = faceScends;
	}
	public String getFingerStartTime() {
		return fingerStartTime;
	}
	public void setFingerStartTime(String fingerStartTime) {
		this.fingerStartTime = fingerStartTime;
	}
	public String getFingerEndTime() {
		return fingerEndTime;
	}
	public void setFingerEndTime(String fingerEndTime) {
		this.fingerEndTime = fingerEndTime;
	}
	public Double getFingerSeconds() {
		return fingerSeconds;
	}
	public void setFingerSeconds(Double fingerSeconds) {
		this.fingerSeconds = fingerSeconds;
	}
	public String getUpdHywebStartTime() {
		return updHywebStartTime;
	}
	public void setUpdHywebStartTime(String updHywebStartTime) {
		this.updHywebStartTime = updHywebStartTime;
	}
	public String getUpdHywebEndTime() {
		return updHywebEndTime;
	}
	public void setUpdHywebEndTime(String updHywebEndTime) {
		this.updHywebEndTime = updHywebEndTime;
	}
	public Double getUpdHywebSeconds() {
		return updHywebSeconds;
	}
	public void setUpdHywebSeconds(Double updHywebSeconds) {
		this.updHywebSeconds = updHywebSeconds;
	}
	public String getUpdMnrStartTime() {
		return updMnrStartTime;
	}
	public void setUpdMnrStartTime(String updMnrStartTime) {
		this.updMnrStartTime = updMnrStartTime;
	}
	public String getUpdMnrEndTime() {
		return updMnrEndTime;
	}
	public void setUpdMnrEndTime(String updMnrEndTime) {
		this.updMnrEndTime = updMnrEndTime;
	}
	public Double getUpdMnrSeconds() {
		return updMnrSeconds;
	}
	public void setUpdMnrSeconds(Double updMnrSeconds) {
		this.updMnrSeconds = updMnrSeconds;
	}
	public String getDoor2aOpenTime() {
		return door2aOpenTime;
	}
	public void setDoor2aOpenTime(String door2aOpenTime) {
		this.door2aOpenTime = door2aOpenTime;
	}
	public String getDoor2aCloseTime() {
		return door2aCloseTime;
	}
	public void setDoor2aCloseTime(String door2aCloseTime) {
		this.door2aCloseTime = door2aCloseTime;
	}
	public Double getDoor2aSeconds() {
		return door2aSeconds;
	}
	public void setDoor2aSeconds(Double door2aSeconds) {
		this.door2aSeconds = door2aSeconds;
	}
	public String getResetSystemTime() {
		return resetSystemTime;
	}
	public void setResetSystemTime(String resetSystemTime) {
		this.resetSystemTime = resetSystemTime;
	}
	public String getIdentifyEndTime() {
		return identifyEndTime;
	}
	public void setIdentifyEndTime(String identifyEndTime) {
		this.identifyEndTime = identifyEndTime;
	}
	public String getLastTime() {
		return lastTime;
	}
	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
	public String getIdentifyResult() {
		return identifyResult;
	}
	public void setIdentifyResult(String identifyResult) {
		this.identifyResult = identifyResult;
	}
	public String getFaceIdentifyResult() {
		return faceIdentifyResult;
	}
	public void setFaceIdentifyResult(String faceIdentifyResult) {
		this.faceIdentifyResult = faceIdentifyResult;
	}
	public String getFingerIdentifyResult() {
		return fingerIdentifyResult;
	}
	public void setFingerIdentifyResult(String fingerIdentifyResult) {
		this.fingerIdentifyResult = fingerIdentifyResult;
	}
	public String getFaceScore() {
		return faceScore;
	}
	public void setFaceScore(String faceScore) {
		this.faceScore = faceScore;
	}
	public String getFingerScore() {
		return fingerScore;
	}
	public void setFingerScore(String fingerScore) {
		this.fingerScore = fingerScore;
	}
	public String getTwoPersonal() {
		return twoPersonal;
	}
	public void setTwoPersonal(String twoPersonal) {
		this.twoPersonal = twoPersonal;
	}
	public String getDoor1aOpenTime() {
		return door1aOpenTime;
	}
	public void setDoor1aOpenTime(String door1aOpenTime) {
		this.door1aOpenTime = door1aOpenTime;
	}
	public String getDoor1aCloseTime() {
		return door1aCloseTime;
	}
	public void setDoor1aCloseTime(String door1aCloseTime) {
		this.door1aCloseTime = door1aCloseTime;
	}
	public Double getDoor1aSeconds() {
		return door1aSeconds;
	}
	public void setDoor1aSeconds(Double door1aSeconds) {
		this.door1aSeconds = door1aSeconds;
	}
	public String getEmrpExpireDate() {
		return emrpExpireDate;
	}
	public void setEmrpExpireDate(String emrpExpireDate) {
		this.emrpExpireDate = emrpExpireDate;
	}
	public String getLogDate() {
		return logDate;
	}
	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}
	
	public Double getReportASeconds() {
		return reportASeconds;
	}
	public void setReportASeconds(Double reportASeconds) {
		this.reportASeconds = reportASeconds;
	}
	public Double getReportBSeconds() {
		return reportBSeconds;
	}
	public void setReportBSeconds(Double reportBSeconds) {
		this.reportBSeconds = reportBSeconds;
	}
	public Double getReportCSeconds() {
		return reportCSeconds;
	}
	public void setReportCSeconds(Double reportCSeconds) {
		this.reportCSeconds = reportCSeconds;
	}
	public Double getReportDSeconds() {
		return reportDSeconds;
	}
	public void setReportDSeconds(Double reportDSeconds) {
		this.reportDSeconds = reportDSeconds;
	}
	public Double getReportESeconds() {
		return reportESeconds;
	}
	public void setReportESeconds(Double reportESeconds) {
		this.reportESeconds = reportESeconds;
	}
	public Double getReportACESeconds() {
		return reportACESeconds;
	}
	public void setReportACESeconds(Double reportACESeconds) {
		this.reportACESeconds = reportACESeconds;
	}
	public Double getReportABCDESeconds() {
		return reportABCDESeconds;
	}
	public void setReportABCDESeconds(Double reportABCDESeconds) {
		this.reportABCDESeconds = reportABCDESeconds;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	
	public Double getReportE1Seconds() {
		return reportE1Seconds;
	}
	public void setReportE1Seconds(Double reportE1Seconds) {
		this.reportE1Seconds = reportE1Seconds;
	}
	public Double getReportE2Seconds() {
		return reportE2Seconds;
	}
	public Double getReportFSeconds() {
		return reportFSeconds;
	}
	public void setReportFSeconds(Double reportFSeconds) {
		this.reportFSeconds = reportFSeconds;
	}

	public void setReportE2Seconds(Double reportE2Seconds) {
		this.reportE2Seconds = reportE2Seconds;
	}
	public Double getReportACE1E2FSeconds() {
		return reportACE1E2FSeconds;
	}
	public void setReportACE1E2FSeconds(Double reportACE1E2FSeconds) {
		this.reportACE1E2FSeconds = reportACE1E2FSeconds;
	}
	public Double getReportABCDE1E2FSeconds() {
		return reportABCDE1E2FSeconds;
	}
	public void setReportABCDE1E2FSeconds(Double reportABCDE1E2FSeconds) {
		this.reportABCDE1E2FSeconds = reportABCDE1E2FSeconds;
	}
	
	public String getPortType() {
		return portType;
	}
	public void setportType(String portType) {
		this.portType = portType;
	}
	
	public String geteGateId() {
		return eGateId;
	}
	public void seteGateId(String eGateId) {
		this.eGateId = eGateId;
	}
	
	public String getFinger01IdentifyThisTime() {
		return finger01IdentifyThisTime;
	}

	public void setFinger01IdentifyThisTime(String finger01IdentifyThisTime) {
		this.finger01IdentifyThisTime = finger01IdentifyThisTime;
	}

	public String getFinger02IdentifyThisTime() {
		return finger02IdentifyThisTime;
	}

	public void setFinger02IdentifyThisTime(String finger02IdentifyThisTime) {
		this.finger02IdentifyThisTime = finger02IdentifyThisTime;
	}

	public String getFinger01IdentifyPreTime() {
		return finger01IdentifyPreTime;
	}

	public void setFinger01IdentifyPreTime(String finger01IdentifyPreTime) {
		this.finger01IdentifyPreTime = finger01IdentifyPreTime;
	}

	public String getFinger02IdentifyPreTime() {
		return finger02IdentifyPreTime;
	}

	public void setFinger02IdentifyPreTime(String finger02IdentifyPreTime) {
		this.finger02IdentifyPreTime = finger02IdentifyPreTime;
	}

	public Double getLastFinger01IdentifySeconds() {
		return lastFinger01IdentifySeconds;
	}

	public void setLastFinger01IdentifySeconds(Double lastFinger01IdentifySeconds) {
		this.lastFinger01IdentifySeconds = lastFinger01IdentifySeconds;
	}

	public Double getLastFinger02IdentifySeconds() {
		return lastFinger02IdentifySeconds;
	}

	public void setLastFinger02IdentifySeconds(Double lastFinger02IdentifySeconds) {
		this.lastFinger02IdentifySeconds = lastFinger02IdentifySeconds;
	}

	public List<String> getFinger01LogList() {
		return finger01List;
	}

	public void setFinger01Log(String context) {
		this.finger01List.add(context);
	}

	public List<String> getFinger02LogList() {
		return finger02List;
	}

	public void setFinger02Log(String context) {
		this.finger02List.add(context);
	}

	public List<String> getFinger01ScoreLogList() {
		return finger01ScoreList;
	}

	public void setFinger01ScoreLog(String context) {
		this.finger01ScoreList.add(context);
	}

	public List<String> getFinger02ScoreLogList() {
		return finger02ScoreList;
	}

	public void setFinger02ScoreLog(String context) {
		this.finger02ScoreList.add(context);
	}

	public List<String> getStartFingerList() {
		return startFingerList;
	}

	public void setStartFingerList(String context) {
		this.startFingerList.add(context);
	}

	public List<String> getEndFingerList() {
		return endFingerList;
	}

	public void setEndFingerList(String context) {
		this.endFingerList.add(context);
	}

	public String getFingerPressSTimeNoResult() {
		return fingerPressSTimeNoResult;
	}

	public void setFingerPressSTimeNoResult(String fingerPressSTimeNoResult) {
		this.fingerPressSTimeNoResult = fingerPressSTimeNoResult;
	}

	public String getFingerPressETimeNoResult() {
		return fingerPressETimeNoResult;
	}

	public void setFingerPressETimeNoResult(String fingerPressETimeNoResult) {
		this.fingerPressETimeNoResult = fingerPressETimeNoResult;
	}

	public Double getFingerPressNoResultSecs() {
		return fingerPressNoResultSecs;
	}

	public void setFingerPressNoResultSecs(Double fingerPressNoResultSecs) {
		this.fingerPressNoResultSecs = fingerPressNoResultSecs;
	}

	public String getFingerPressSTimeHasResult() {
		return fingerPressSTimeHasResult;
	}

	public void setFingerPressSTimeHasResult(String fingerPressSTimeHasResult) {
		this.fingerPressSTimeHasResult = fingerPressSTimeHasResult;
	}

	public String getFingerPressETimeHasResult() {
		return fingerPressETimeHasResult;
	}

	public void setFingerPressETimeHasResult(String fingerPressETimeHasResult) {
		this.fingerPressETimeHasResult = fingerPressETimeHasResult;
	}

	public Double getFingerPressHasResultSecs() {
		return fingerPressHasResultSecs;
	}

	public void setFingerPressHasResultSecs(Double fingerPressHasResultSecs) {
		this.fingerPressHasResultSecs = fingerPressHasResultSecs;
	}

	public List<String> getFinishVerifyList() {
		return finishVerifyList;
	}

	public void setFinishVerifyList(String context) {
		this.finishVerifyList.add(context);
	}

	public String getFaceNoResultSTime() {
		return faceNoResultSTime;
	}

	public void setFaceNoResultSTime(String faceNoResultSTime) {
		this.faceNoResultSTime = faceNoResultSTime;
	}

	public String getFaceNoResultETime() {
		return faceNoResultETime;
	}

	public void setFaceNoResultETime(String faceNoResultETime) {
		this.faceNoResultETime = faceNoResultETime;
	}

	public Double getFaceNoResultSecs() {
		return faceNoResultSecs;
	}

	public void setFaceNoResultSecs(Double faceNoResultSecs) {
		this.faceNoResultSecs = faceNoResultSecs;
	}

	public String getFaceHasResultSTime() {
		return faceHasResultSTime;
	}

	public void setFaceHasResultSTime(String faceHasResultSTime) {
		this.faceHasResultSTime = faceHasResultSTime;
	}

	public String getFaceHasResultETime() {
		return faceHasResultETime;
	}

	public void setFaceHasResultETime(String faceHasResultETime) {
		this.faceHasResultETime = faceHasResultETime;
	}

	public Double getFaceHasResultSecs() {
		return faceHasResultSecs;
	}

	public void setFaceHasResultSecs(Double faceHasResultSecs) {
		this.faceHasResultSecs = faceHasResultSecs;
	}

	public String getBoardingpassSTime() {
		return boardingpassSTime;
	}

	public void setBoardingpassSTime(String boardingpassSTime) {
		this.boardingpassSTime = boardingpassSTime;
	}

	public String getBoardingpassETime() {
		return boardingpassETime;
	}

	public void setBoardingpassETime(String boardingpassETime) {
		this.boardingpassETime = boardingpassETime;
	}

	public Double getBoardingpassSecs() {
		return boardingpassSecs;
	}

	public void setBoardingpassSecs(Double boardingpassSecs) {
		this.boardingpassSecs = boardingpassSecs;
	}

	public String getBodyTemperatureTime() {
		return bodyTemperatureTime;
	}

	public void setBodyTemperatureTime(String bodyTemperatureTime) {
		this.bodyTemperatureTime = bodyTemperatureTime;
	}

	public Double getBodyTemperatureSecs() {
		return bodyTemperatureSecs;
	}

	public void setBodyTemperatureSecs(Double bodyTemperatureSecs) {
		this.bodyTemperatureSecs = bodyTemperatureSecs;
	}
}
