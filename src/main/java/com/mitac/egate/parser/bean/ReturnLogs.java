package com.mitac.egate.parser.bean;

import java.util.List;

public class ReturnLogs {
	private List<PassengerLog> passengerLog;
	private List<EmrpLog> emrpErrorLog;
	public ReturnLogs() {
		// TODO Auto-generated constructor stub
	}
	public List<PassengerLog> getPassengerLog() {
		return passengerLog;
	}
	public void setPassengerLog(List<PassengerLog> passengerLog) {
		this.passengerLog = passengerLog;
	}
	public List<EmrpLog> getEmrpErrorLog() {
		return emrpErrorLog;
	}
	public void setEmrpErrorLog(List<EmrpLog> emrpErrorLog) {
		this.emrpErrorLog = emrpErrorLog;
	}
}
