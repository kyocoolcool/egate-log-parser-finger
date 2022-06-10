package com.mitac.egate.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @ClassName PassLog 
 * @Description 旅客解析
 * @author Sunny Yeh
 * @Date 2020-03-02 15:54
 * @Version 1.0
 */

@Entity
@Table(name = "PassLog")
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@IdClass(PassLogKey.class)
public class PassLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id	
	@Column(name = "portType", nullable = false)
	private String portType;

	@Id	
	@Column(name = "eGateId", nullable = false)
	private String eGateId;
	
	@Id 
	@Column(name = "PassDate", nullable = false)
	private String passDate;
	
	@Id 
	@Column(name = "SerialNo", nullable = false)
	private Integer serialNo;
	
	@Id 
	@Column(name = "PassportNo", nullable = false)
	private String passportNo;
	
	@Column(name = "Nation")
	private String nation;
	
	@Column(name = "PassportExpiryDate")
	private String passportExpiryDate;
	
	@Column(name = "PassSuccessOrFail")
	private String passSuccessOrFail;
	
	@Column(name = "PassportNoCheckTimePassmachineStarts", nullable = false)
	private String passportNoCheckTimePassmachineStarts;
	
	@Column(name = "PassportNoCheckTimeToInspect")
	private String passportNoCheckTimeToInspect;
	
	@Column(name = "PassprotNoCheckTimeSeconds")
	private Double passprotNoCheckTimeSeconds;
	
	@Column(name = "IDCheckTimeToInspect_eGate")
	private String idCheckTimeToInspect_eGate;
	
	@Column(name = "IDCheckTimeRespondFromInspect")
	private String idCheckTimeRespondFromInspect;
	
	@Column(name = "IDCheckTimeSeconds")
	private Double idCheckTimeSeconds;
	
	@Column(name = "IDCheckTimeRcode")
	private String idCheckTimeRcode;	
	
	@Column(name = "QueryBioBeginTime")
	private String queryBioBeginTime;	 
	
	@Column(name = "QueryBioEndTime")
	private String queryBioEndTime;	
	
	@Column(name = "QueryBioSeconds")
	private Double queryBioSeconds;	
	
	@Column(name = "FromInspectReceivedTime")
	private String fromInspectReceivedTime;	

	@Column(name = "OpenDoor1Time")
	private String openDoor1Time;
	
	@Column(name = "OpeneGateDoor1Seconds")
	private Double openeGateDoor1Seconds;
	
	@Column(name = "eGateDoor1OpenTime")
	private String eGateDoor1OpenTime;	
	
	@Column(name = "eGateDoor1CloseTime")
	private String eGateDoor1CloseTime;	
	
	@Column(name = "eGateDoorOCseconds")
	private Double eGateDoorOCseconds;	
	
	@Column(name = "FaceBioFetchBegTime")
	private String faceBioFetchBegTime;	
	
	@Column(name = "FaceBioFetchEndTime")
	private String faceBioFetchEndTime;	
	
	@Column(name = "FaceBioFetchTimeSeconds")
	private Double faceBioFetchTimeSeconds;
	
	@Column(name = "FaceBioIdentifyBegTime")
	private String faceBioIdentifyBegTime;
	
	@Column(name = "FaceBioIdentifyEndTime")
	private String faceBioIdentifyEndTime;	
	
	@Column(name = "FaceBioIdentifySeconds")
	private Double faceBioIdentifySeconds;	
	
	@Column(name = "FaceBioIdentifyQualityScore")
	private Integer faceBioIdentifyQualityScore;	
	
	@Column(name = "FingerPrintFetchBegTime")
	private String fingerPrintFetchBegTime;	
	
	@Column(name = "FingerPrintFetchEndTime")
	private String fingerPrintFetchEndTime;
	
	@Column(name = "FingerPrintFetchSeconds")
	private Double fingerPrintFetchSeconds;	
	
	@Column(name = "FingerBioIdentifyBegTime")
	private String fingerBioIdentifyBegTime;	
	
	@Column(name = "FingerBioIdentifyEndTime")
	private String fingerBioIdentifyEndTime;
	
	@Column(name = "FingerBioIdentifySeconds")
	private Double fingerBioIdentifySeconds;	
	
	@Column(name = "Finger1BioIdentifyQualityScore")
	private Integer finger1BioIdentifyQualityScore;	
	
	@Column(name = "Finger2BioIdentifyQualityScore")
	private Integer finger2BioIdentifyQualityScore;	
	
	@Column(name = "Finger1BioIdentifySeconds")
	private Double finger1BioIdentifySeconds;
	
	@Column(name = "Finger2BioIdentifySeconds")
	private Double finger2BioIdentifySeconds;	
	
	@Column(name = "IdentifyEndTime")
	private String identifyEndTime;		
	
	@Column(name = "OpenDoor2Time")
	private String openDoor2Time;	
	
	@Column(name = "OpenDoor2Seconds")
	private Double openDoor2Seconds;
	
	@Column(name = "eGateUseTime")
	private Double eGateUseTime;	
	
	@Column(name = "TotalPassTime")
	private Double totalPassTime;
	
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

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getPassportExpiryDate() {
		return passportExpiryDate;
	}

	public void setPassportExpiryDate(String passportExpiryDate) {
		this.passportExpiryDate = passportExpiryDate;
	}

	public String getPassSuccessOrFail() {
		return passSuccessOrFail;
	}

	public void setPassSuccessOrFail(String passSuccessOrFail) {
		this.passSuccessOrFail = passSuccessOrFail;
	}

	public String getPassportNoCheckTimePassmachineStarts() {
		return passportNoCheckTimePassmachineStarts;
	}

	public void setPassportNoCheckTimePassmachineStarts(String passportNoCheckTimePassmachineStarts) {
		this.passportNoCheckTimePassmachineStarts = passportNoCheckTimePassmachineStarts;
	}

	public String getPassportNoCheckTimeToInspect() {
		return passportNoCheckTimeToInspect;
	}

	public void setPassportNoCheckTimeToInspect(String passportNoCheckTimeToInspect) {
		this.passportNoCheckTimeToInspect = passportNoCheckTimeToInspect;
	}

	public Double getPassprotNoCheckTimeSeconds() {
		return passprotNoCheckTimeSeconds;
	}

	public void setPassprotNoCheckTimeSeconds(Double passprotNoCheckTimeSeconds) {
		this.passprotNoCheckTimeSeconds = passprotNoCheckTimeSeconds;
	}

	public String getIdCheckTimeToInspect_eGate() {
		return idCheckTimeToInspect_eGate;
	}

	public void setIdCheckTimeToInspect_eGate(String idCheckTimeToInspect_eGate) {
		this.idCheckTimeToInspect_eGate = idCheckTimeToInspect_eGate;
	}

	public String getIdCheckTimeRespondFromInspect() {
		return idCheckTimeRespondFromInspect;
	}

	public void setIdCheckTimeRespondFromInspect(String idCheckTimeRespondFromInspect) {
		this.idCheckTimeRespondFromInspect = idCheckTimeRespondFromInspect;
	}

	public Double getIdCheckTimeSeconds() {
		return idCheckTimeSeconds;
	}

	public void setIdCheckTimeSeconds(Double idCheckTimeSeconds) {
		this.idCheckTimeSeconds = idCheckTimeSeconds;
	}

	public String getIdCheckTimeRcode() {
		return idCheckTimeRcode;
	}

	public void setIdCheckTimeRcode(String idCheckTimeRcode) {
		this.idCheckTimeRcode = idCheckTimeRcode;
	}

	public String getQueryBioBeginTime() {
		return queryBioBeginTime;
	}

	public void setQueryBioBeginTime(String queryBioBeginTime) {
		this.queryBioBeginTime = queryBioBeginTime;
	}

	public String getQueryBioEndTime() {
		return queryBioEndTime;
	}

	public void setQueryBioEndTime(String queryBioEndTime) {
		this.queryBioEndTime = queryBioEndTime;
	}

	public Double getQueryBioSeconds() {
		return queryBioSeconds;
	}

	public void setQueryBioSeconds(Double queryBioSeconds) {
		this.queryBioSeconds = queryBioSeconds;
	}

	public String getFromInspectReceivedTime() {
		return fromInspectReceivedTime;
	}

	public void setFromInspectReceivedTime(String fromInspectReceivedTime) {
		this.fromInspectReceivedTime = fromInspectReceivedTime;
	}

	public String getOpenDoor1Time() {
		return openDoor1Time;
	}

	public void setOpenDoor1Time(String openDoor1Time) {
		this.openDoor1Time = openDoor1Time;
	}

	public Double getOpeneGateDoor1Seconds() {
		return openeGateDoor1Seconds;
	}

	public void setOpeneGateDoor1Seconds(Double openeGateDoor1Seconds) {
		this.openeGateDoor1Seconds = openeGateDoor1Seconds;
	}

	public String geteGateDoor1OpenTime() {
		return eGateDoor1OpenTime;
	}

	public void seteGateDoor1OpenTime(String eGateDoor1OpenTime) {
		this.eGateDoor1OpenTime = eGateDoor1OpenTime;
	}

	public String geteGateDoor1CloseTime() {
		return eGateDoor1CloseTime;
	}

	public void seteGateDoor1CloseTime(String eGateDoor1CloseTime) {
		this.eGateDoor1CloseTime = eGateDoor1CloseTime;
	}

	public Double geteGateDoorOCseconds() {
		return eGateDoorOCseconds;
	}

	public void seteGateDoorOCseconds(Double eGateDoorOCseconds) {
		this.eGateDoorOCseconds = eGateDoorOCseconds;
	}

	public String getFaceBioFetchBegTime() {
		return faceBioFetchBegTime;
	}

	public void setFaceBioFetchBegTime(String faceBioFetchBegTime) {
		this.faceBioFetchBegTime = faceBioFetchBegTime;
	}

	public String getFaceBioFetchEndTime() {
		return faceBioFetchEndTime;
	}

	public void setFaceBioFetchEndTime(String faceBioFetchEndTime) {
		this.faceBioFetchEndTime = faceBioFetchEndTime;
	}

	public Double getFaceBioFetchTimeSeconds() {
		return faceBioFetchTimeSeconds;
	}

	public void setFaceBioFetchTimeSeconds(Double faceBioFetchTimeSeconds) {
		this.faceBioFetchTimeSeconds = faceBioFetchTimeSeconds;
	}

	public String getFaceBioIdentifyBegTime() {
		return faceBioIdentifyBegTime;
	}

	public void setFaceBioIdentifyBegTime(String faceBioIdentifyBegTime) {
		this.faceBioIdentifyBegTime = faceBioIdentifyBegTime;
	}

	public String getFaceBioIdentifyEndTime() {
		return faceBioIdentifyEndTime;
	}

	public void setFaceBioIdentifyEndTime(String faceBioIdentifyEndTime) {
		this.faceBioIdentifyEndTime = faceBioIdentifyEndTime;
	}

	public Double getFaceBioIdentifySeconds() {
		return faceBioIdentifySeconds;
	}

	public void setFaceBioIdentifySeconds(Double faceBioIdentifySeconds) {
		this.faceBioIdentifySeconds = faceBioIdentifySeconds;
	}

	public Integer getFaceBioIdentifyQualityScore() {
		return faceBioIdentifyQualityScore;
	}

	public void setFaceBioIdentifyQualityScore(Integer faceBioIdentifyQualityScore) {
		this.faceBioIdentifyQualityScore = faceBioIdentifyQualityScore;
	}

	public String getFingerPrintFetchBegTime() {
		return fingerPrintFetchBegTime;
	}

	public void setFingerPrintFetchBegTime(String fingerPrintFetchBegTime) {
		this.fingerPrintFetchBegTime = fingerPrintFetchBegTime;
	}

	public String getFingerPrintFetchEndTime() {
		return fingerPrintFetchEndTime;
	}

	public void setFingerPrintFetchEndTime(String fingerPrintFetchEndTime) {
		this.fingerPrintFetchEndTime = fingerPrintFetchEndTime;
	}

	public Double getFingerPrintFetchSeconds() {
		return fingerPrintFetchSeconds;
	}

	public void setFingerPrintFetchSeconds(Double fingerPrintFetchSeconds) {
		this.fingerPrintFetchSeconds = fingerPrintFetchSeconds;
	}

	public String getFingerBioIdentifyBegTime() {
		return fingerBioIdentifyBegTime;
	}

	public void setFingerBioIdentifyBegTime(String fingerBioIdentifyBegTime) {
		this.fingerBioIdentifyBegTime = fingerBioIdentifyBegTime;
	}

	public String getFingerBioIdentifyEndTime() {
		return fingerBioIdentifyEndTime;
	}

	public void setFingerBioIdentifyEndTime(String fingerBioIdentifyEndTime) {
		this.fingerBioIdentifyEndTime = fingerBioIdentifyEndTime;
	}

	public Double getFingerBioIdentifySeconds() {
		return fingerBioIdentifySeconds;
	}

	public void setFingerBioIdentifySeconds(Double fingerBioIdentifySeconds) {
		this.fingerBioIdentifySeconds = fingerBioIdentifySeconds;
	}

	public Integer getFinger1BioIdentifyQualityScore() {
		return finger1BioIdentifyQualityScore;
	}

	public void setFinger1BioIdentifyQualityScore(Integer finger1BioIdentifyQualityScore) {
		this.finger1BioIdentifyQualityScore = finger1BioIdentifyQualityScore;
	}

	public Integer getFinger2BioIdentifyQualityScore() {
		return finger2BioIdentifyQualityScore;
	}

	public void setFinger2BioIdentifyQualityScore(Integer finger2BioIdentifyQualityScore) {
		this.finger2BioIdentifyQualityScore = finger2BioIdentifyQualityScore;
	}

	public Double getFinger1BioIdentifySeconds() {
		return finger1BioIdentifySeconds;
	}

	public void setFinger1BioIdentifySeconds(Double finger1BioIdentifySeconds) {
		this.finger1BioIdentifySeconds = finger1BioIdentifySeconds;
	}

	public Double getFinger2BioIdentifySeconds() {
		return finger2BioIdentifySeconds;
	}

	public void setFinger2BioIdentifySeconds(Double finger2BioIdentifySeconds) {
		this.finger2BioIdentifySeconds = finger2BioIdentifySeconds;
	}

	public String getIdentifyEndTime() {
		return identifyEndTime;
	}

	public void setIdentifyEndTime(String identifyEndTime) {
		this.identifyEndTime = identifyEndTime;
	}

	public String getOpenDoor2Time() {
		return openDoor2Time;
	}

	public void setOpenDoor2Time(String openDoor2Time) {
		this.openDoor2Time = openDoor2Time;
	}

	public Double getOpenDoor2Seconds() {
		return openDoor2Seconds;
	}

	public void setOpenDoor2Seconds(Double openDoor2Seconds) {
		this.openDoor2Seconds = openDoor2Seconds;
	}

	public Double geteGateUseTime() {
		return eGateUseTime;
	}

	public void seteGateUseTime(Double eGateUseTime) {
		this.eGateUseTime = eGateUseTime;
	}

	public Double getTotalPassTime() {
		return totalPassTime;
	}

	public void setTotalPassTime(Double totalPassTime) {
		this.totalPassTime = totalPassTime;
	}
}
