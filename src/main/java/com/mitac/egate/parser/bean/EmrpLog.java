package com.mitac.egate.parser.bean;

public class EmrpLog {
	
	private String logDateTime;
	private String portType; //機場入出境別
	private String eGateId; //閘道ID	
	private String logDate;	
	private String mainDocument; //證件號碼
	
	private String startTime; 
	private String errorReason;
	private String pattern;
	
	public EmrpLog() {
		
	}
	
	public EmrpLog(String portType,String eGateId) {
		this.portType = portType;
		this.eGateId = eGateId;
	}
	
	public String getLogDateTime() {
		return logDateTime;
	}
	public void setLogDateTime(String logDateTime) {
		this.logDateTime = logDateTime;
	}
	public String getPortType() {
		return portType;
	}
	public void setPortType(String portType) {
		this.portType = portType;
	}
	public String geteGateId() {
		return eGateId;
	}
	public void seteGateId(String eGateId) {
		this.eGateId = eGateId;
	}
	public String getLogDate() {
		return logDate;
	}
	public void setLogDate(String logDate) {
		this.logDate = logDate;
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
	public String getErrorReason() {
		return errorReason;
	}
	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}
	
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public String toString() {
		return "EmrpLog [logDateTime=" + logDateTime + ", eGateId=" + eGateId + ", logDate=" + logDate
				+ ", mainDocument=" + mainDocument + ", startTime=" + startTime + ", errorReason=" + errorReason
				+ ", pattern=" + pattern + "]";
	}
}
