package com.mitac.egate.parser.bean;

public class FingerLog {
	
	private String logDateTime;
	private String eGateId; //閘道ID	
	private String logDate;	
	private String mainDocument; //證件號碼
	
	private Integer finger01Quality; 
	private Integer finger01Score;
	private Double finger01Seconds;
	private Integer finger02Quality;
	private Integer finger02Score;
	private Double finger02Seconds;
	
	public String getLogDateTime() {
		return logDateTime;
	}

	public void setLogDateTime(String logDateTime) {
		this.logDateTime = logDateTime;
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

	public Integer getFinger01Quality() {
		return finger01Quality;
	}

	public void setFinger01Quality(Integer finger01Quality) {
		this.finger01Quality = finger01Quality;
	}

	public Integer getFinger01Score() {
		return finger01Score;
	}

	public void setFinger01Score(Integer finger01Score) {
		this.finger01Score = finger01Score;
	}

	public Double getFinger01Seconds() {
		return finger01Seconds;
	}

	public void setFinger01Seconds(Double finger01Seconds) {
		this.finger01Seconds = finger01Seconds;
	}

	public Integer getFinger02Quality() {
		return finger02Quality;
	}

	public void setFinger02Quality(Integer finger02Quality) {
		this.finger02Quality = finger02Quality;
	}

	public Integer getFinger02Score() {
		return finger02Score;
	}

	public void setFinger02Score(Integer finger02Score) {
		this.finger02Score = finger02Score;
	}

	public Double getFinger02Seconds() {
		return finger02Seconds;
	}

	public void setFinger02Seconds(Double finger02Seconds) {
		this.finger02Seconds = finger02Seconds;
	}

	@Override
	public String toString() {
		return "FingerLog [logDateTime=" + logDateTime + ", eGateId=" + eGateId + ", logDate=" + logDate
				+ ", mainDocument=" + mainDocument + ", finger01Quality=" + finger01Quality + ", finger01Score="
				+ finger01Score + ", finger01Seconds=" + finger01Seconds + ", finger02Quality=" + finger02Quality
				+ ", finger02Score=" + finger02Score + ", finger02Seconds=" + finger02Seconds + "]";
	}
	
}
