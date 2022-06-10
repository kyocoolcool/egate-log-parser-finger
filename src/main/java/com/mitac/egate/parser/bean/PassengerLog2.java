package com.mitac.egate.parser.bean;

import java.util.Arrays;

public class PassengerLog2 {

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
	private Double reportE2Seconds; //指辨時間, 指辨開始 ~ 第二道門開啟
	private Double reportACESeconds;
	private Double reportABCDESeconds;
	private Double reportACE1E2Seconds;
	private Double reportABCDE1E2Seconds;
	private String nationality; //國籍
	private String logDate;
	private String emrpExpireDate; //護照有效日期
	private String eGateId; //閘道ID
	
	private String finger01IdentifyStartTime;
	private String finger01IdentifyStartResult;
	private String finger01IdentifyEndTime;
	
	private String finger02IdentifyStartTime;
	private String finger02IdentifyStartResult;
	private String finger02IdentifyEndTime;
	

	private Double lastFinger01IdentifySeconds;
	private Double lastFinger02IdentifySeconds;
	
	public PassengerLog2(String eGateId) {
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
	public void setReportE2Seconds(Double reportE2Seconds) {
		this.reportE2Seconds = reportE2Seconds;
	}
	public Double getReportACE1E2Seconds() {
		return reportACE1E2Seconds;
	}
	public void setReportACE1E2Seconds(Double reportACE1E2Seconds) {
		this.reportACE1E2Seconds = reportACE1E2Seconds;
	}
	public Double getReportABCDE1E2Seconds() {
		return reportABCDE1E2Seconds;
	}
	public void setReportABCDE1E2Seconds(Double reportABCDE1E2Seconds) {
		this.reportABCDE1E2Seconds = reportABCDE1E2Seconds;
	}
	
	public String geteGateId() {
		return eGateId;
	}
	public void seteGateId(String eGateId) {
		this.eGateId = eGateId;
	}
	
	public String getFinger01IdentifyStartTime() {
		return finger01IdentifyStartTime;
	}

	public void setFinger01IdentifyStartTime(String finger01IdentifyStartTime) {
		this.finger01IdentifyStartTime = finger01IdentifyStartTime;
	}

	public String getFinger01IdentifyStartResult() {
		return finger01IdentifyStartResult;
	}

	public void setFinger01IdentifyStartResult(String finger01IdentifyStartResult) {
		this.finger01IdentifyStartResult = finger01IdentifyStartResult;
	}

	public String getFinger01IdentifyEndTime() {
		return finger01IdentifyEndTime;
	}

	public void setFinger01IdentifyEndTime(String finger01IdentifyEndTime) {
		this.finger01IdentifyEndTime = finger01IdentifyEndTime;
	}

	public String getFinger02IdentifyStartTime() {
		return finger02IdentifyStartTime;
	}

	public void setFinger02IdentifyStartTime(String finger02IdentifyStartTime) {
		this.finger02IdentifyStartTime = finger02IdentifyStartTime;
	}

	public String getFinger02IdentifyStartResult() {
		return finger02IdentifyStartResult;
	}

	public void setFinger02IdentifyStartResult(String finger02IdentifyStartResult) {
		this.finger02IdentifyStartResult = finger02IdentifyStartResult;
	}

	public String getFinger02IdentifyEndTime() {
		return finger02IdentifyEndTime;
	}

	public void setFinger02IdentifyEndTime(String finger02IdentifyEndTime) {
		this.finger02IdentifyEndTime = finger02IdentifyEndTime;
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

	@Override
	public String toString() {
		return "PassengerLog [mainDocument=" + mainDocument + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", totalSeconds=" + totalSeconds + ", emrpStartTime=" + emrpStartTime + ", emrpEndTime=" + emrpEndTime
				+ ", emrpSeconds=" + emrpSeconds + ", qryHywebStartTime=" + qryHywebStartTime + ", qryHywebEndTime="
				+ qryHywebEndTime + ", qryHywebSeconds=" + qryHywebSeconds + ", qryHywebRcode=" + qryHywebRcode
				+ ", qryMnrStartTime=" + qryMnrStartTime + ", qryMnrEndTime=" + qryMnrEndTime + ", qryMnrSeconds="
				+ qryMnrSeconds + ", faceStartTime=" + faceStartTime + ", faceEndTime=" + faceEndTime + ", faceScends="
				+ faceScends + ", fingerStartTime=" + fingerStartTime + ", fingerEndTime=" + fingerEndTime
				+ ", fingerSeconds=" + fingerSeconds + ", updHywebStartTime=" + updHywebStartTime + ", updHywebEndTime="
				+ updHywebEndTime + ", updHywebSeconds=" + updHywebSeconds + ", updMnrStartTime=" + updMnrStartTime
				+ ", updMnrEndTime=" + updMnrEndTime + ", updMnrSeconds=" + updMnrSeconds + ", door2aOpenTime="
				+ door2aOpenTime + ", door2aCloseTime=" + door2aCloseTime + ", door2aSeconds=" + door2aSeconds
				+ ", resetSystemTime=" + resetSystemTime + ", identifyEndTime=" + identifyEndTime + ", lastTime="
				+ lastTime + ", identifyResult=" + identifyResult + ", faceIdentifyResult=" + faceIdentifyResult
				+ ", fingerIdentifyResult=" + fingerIdentifyResult + ", faceScore=" + faceScore + ", fingerScore="
				+ fingerScore + ", twoPersonal=" + twoPersonal + ", door1aOpenTime=" + door1aOpenTime
				+ ", door1aCloseTime=" + door1aCloseTime + ", door1aSeconds=" + door1aSeconds + ", reportASeconds="
				+ reportASeconds + ", reportBSeconds=" + reportBSeconds + ", reportCSeconds=" + reportCSeconds
				+ ", reportDSeconds=" + reportDSeconds + ", reportESeconds=" + reportESeconds + ", reportE1Seconds="
				+ reportE1Seconds + ", reportE2Seconds=" + reportE2Seconds + ", reportACESeconds=" + reportACESeconds
				+ ", reportABCDESeconds=" + reportABCDESeconds + ", reportACE1E2Seconds=" + reportACE1E2Seconds
				+ ", reportABCDE1E2Seconds=" + reportABCDE1E2Seconds + ", nationality=" + nationality + ", logDate="
				+ logDate + ", emrpExpireDate=" + emrpExpireDate + ", eGateId=" + eGateId
				+ ", finger01IdentifyStartTime=" + finger01IdentifyStartTime + ", finger01IdentifyStartResult="
				+ finger01IdentifyStartResult + ", finger01IdentifyEndTime=" + finger01IdentifyEndTime
				+ ", finger02IdentifyStartTime=" + finger02IdentifyStartTime + ", finger02IdentifyStartResult="
				+ finger02IdentifyStartResult + ", finger02IdentifyEndTime=" + finger02IdentifyEndTime
				+ ", lastFinger01IdentifySeconds=" + lastFinger01IdentifySeconds + ", lastFinger02IdentifySeconds="
				+ lastFinger02IdentifySeconds + "]";
	}
}
