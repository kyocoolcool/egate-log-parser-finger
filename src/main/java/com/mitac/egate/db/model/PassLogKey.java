package com.mitac.egate.db.model;

import java.io.Serializable;

import javax.persistence.Column;

import org.springframework.data.annotation.Id;

public class PassLogKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "portType")
	private String portType;
	
	@Id
	@Column(name = "eGateId")
	private String eGateId;
	
	@Id
	@Column(name = "PassDate")
	private String passDate;
	
	@Id
	@Column(name = "SerialNo")
	private Integer serialNo;
	
	@Id
	@Column(name = "PassportNo")	
	private String passportNo;
	
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

	public String getPassDate() {
		return passDate;
	}

	public void setPassDate(String passDate) {
		this.passDate = passDate;
	}

	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public String getPassportNo() {
		return passportNo;
	}

	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eGateId == null) ? 0 : eGateId.hashCode());
		result = prime * result + ((passDate == null) ? 0 : passDate.hashCode());
		result = prime * result + ((passportNo == null) ? 0 : passportNo.hashCode());
		result = prime * result + ((serialNo == null) ? 0 : serialNo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PassLogKey other = (PassLogKey) obj;
		if (eGateId == null) {
			if (other.eGateId != null)
				return false;
		} else if (!eGateId.equals(other.eGateId))
			return false;
		if (passDate == null) {
			if (other.passDate != null)
				return false;
		} else if (!passDate.equals(other.passDate))
			return false;
		if (passportNo == null) {
			if (other.passportNo != null)
				return false;
		} else if (!passportNo.equals(other.passportNo))
			return false;
		if (serialNo == null) {
			if (other.serialNo != null)
				return false;
		} else if (!serialNo.equals(other.serialNo))
			return false;
		return true;
	}
	
	

}
