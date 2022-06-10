package com.mitac.egate.db.service;

import static com.mitac.egate.parser.utils.CommonUtils.DateTimeToTime;
import static com.mitac.egate.parser.utils.CommonUtils.decimalRoundD;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.mitac.egate.db.model.PassLog;
import com.mitac.egate.db.repository.PassLogRepository;
import com.mitac.egate.parser.bean.PassengerLog;

@Service
@Component
public class PassLogService {

	@Autowired
	PassLogRepository passLogRepository;

	public void saveLog(List<PassengerLog> passengerList) {
		System.out.println("  >> mapping data...");
		List<PassLog> passLogList = mapToPassLog(passengerList);
		System.out.println("  >> total records = " + passLogList.size());
		System.out.println("  >> Writing to database...");
		try {
			passLogRepository.saveAll(passLogList);
		} catch (Exception ex) {
			ex.printStackTrace();
			for (PassLog pl : passLogList) {
				try {
					passLogRepository.save(pl);
				} catch (Exception ex1) {
					System.out.println("  >> save exception = " + pl.toString());
					ex1.printStackTrace();
				}
			}
		}
		// persistedPassLogIn.forEach(passLogIn -> {
		// System.out.println(passLogIn);
		//
		// });
	}

	/**
	 * Convert PassengerLog to PassLog
	 * 
	 * @param c
	 * @param passengerList
	 * @return
	 */
	private List<PassLog> mapToPassLog(List<PassengerLog> passengerList) {

		AtomicInteger cnt = new AtomicInteger(0);
		return passengerList.stream().map(pLog -> {
			PassLog passLog = new PassLog();
			passLog.setPortType(pLog.getPortType());
			passLog.seteGateId(pLog.geteGateId());
			passLog.setPassDate(pLog.getLogDate());
			passLog.setSerialNo(cnt.incrementAndGet());
			passLog.setPassportNo(pLog.getMainDocument());

			passLog.setNation(pLog.getNationality());
			passLog.setPassportExpiryDate(pLog.getEmrpExpireDate());
			passLog.setPassSuccessOrFail("成功".equals(pLog.getIdentifyResult()) ? "Y" : "N");

			passLog.setPassportNoCheckTimePassmachineStarts(DateTimeToTime(pLog.getEmrpStartTime()));
			passLog.setPassportNoCheckTimeToInspect(DateTimeToTime(pLog.getQryHywebStartTime()));
			passLog.setPassprotNoCheckTimeSeconds(decimalRoundD(pLog.getReportASeconds(), 3));

			passLog.setIdCheckTimeToInspect_eGate(DateTimeToTime(pLog.getQryHywebStartTime()));
			passLog.setIdCheckTimeRespondFromInspect(DateTimeToTime(pLog.getQryHywebEndTime()));
			passLog.setIdCheckTimeSeconds(decimalRoundD(pLog.getReportBSeconds(), 3));

			passLog.setIdCheckTimeRcode(pLog.getQryHywebRcode());
			passLog.setQueryBioBeginTime(DateTimeToTime(pLog.getQryMnrStartTime()));
			passLog.setQueryBioEndTime(DateTimeToTime(pLog.getQryMnrEndTime()));
			passLog.setQueryBioSeconds(decimalRoundD(pLog.getQryMnrSeconds(), 3));

			passLog.setFromInspectReceivedTime(DateTimeToTime(pLog.getQryHywebEndTime()));
			passLog.setOpenDoor1Time(DateTimeToTime(pLog.getDoor1aOpenTime()));
			passLog.setOpeneGateDoor1Seconds(decimalRoundD(pLog.getReportCSeconds(), 3));

			passLog.seteGateDoor1OpenTime(DateTimeToTime(pLog.getDoor1aOpenTime()));
			passLog.seteGateDoor1CloseTime(DateTimeToTime(pLog.getDoor1aCloseTime()));
			passLog.seteGateDoorOCseconds(decimalRoundD(pLog.getReportDSeconds(), 3));

			passLog.setFaceBioFetchBegTime(DateTimeToTime(pLog.getFaceNoResultSTime()));
			passLog.setFaceBioFetchEndTime(DateTimeToTime(pLog.getFaceNoResultETime()));
			passLog.setFaceBioFetchTimeSeconds(decimalRoundD(pLog.getFaceNoResultSecs(), 3));

			passLog.setFaceBioIdentifyBegTime(DateTimeToTime(pLog.getFaceHasResultSTime()));
			passLog.setFaceBioIdentifyEndTime(DateTimeToTime(pLog.getFaceHasResultETime()));
			passLog.setFaceBioIdentifySeconds(decimalRoundD(pLog.getFaceHasResultSecs(), 3));

			passLog.setFaceBioIdentifyQualityScore(
					StringUtils.isBlank(pLog.getFaceScore()) ? null : Integer.parseInt(pLog.getFaceScore()));
			passLog.setFingerPrintFetchBegTime(DateTimeToTime(pLog.getFingerPressSTimeNoResult()));
			passLog.setFingerPrintFetchEndTime(DateTimeToTime(pLog.getFingerPressETimeNoResult()));
			passLog.setFingerPrintFetchSeconds(decimalRoundD(pLog.getFingerPressNoResultSecs(), 3));

			passLog.setFingerBioIdentifyBegTime(DateTimeToTime(pLog.getFingerPressSTimeHasResult()));
			passLog.setFingerBioIdentifyEndTime(DateTimeToTime(pLog.getFingerPressETimeHasResult()));
			passLog.setFingerBioIdentifySeconds(decimalRoundD(pLog.getFingerPressHasResultSecs(), 3));

			if (!StringUtils.isBlank(pLog.getFingerScore())) {
				String[] fingerArr = pLog.getFingerScore().split(",", -1);
				passLog.setFinger1BioIdentifyQualityScore(
						StringUtils.isBlank(fingerArr[0]) ? null : Integer.parseInt(fingerArr[0].trim()));
				passLog.setFinger2BioIdentifyQualityScore(
						StringUtils.isBlank(fingerArr[1]) ? null : Integer.parseInt(fingerArr[1].trim()));
			}

			passLog.setFinger1BioIdentifySeconds(decimalRoundD(pLog.getLastFinger01IdentifySeconds(), 3));
			passLog.setFinger2BioIdentifySeconds(decimalRoundD(pLog.getLastFinger02IdentifySeconds(), 3));

			passLog.setIdentifyEndTime(DateTimeToTime(pLog.getIdentifyEndTime()));

			passLog.setOpenDoor2Time(DateTimeToTime(pLog.getDoor2aOpenTime()));
			passLog.setOpenDoor2Seconds(decimalRoundD(pLog.getReportFSeconds(), 3));
			passLog.seteGateUseTime(decimalRoundD(pLog.getReportACE1E2FSeconds(), 3));
			passLog.setTotalPassTime(decimalRoundD(pLog.getReportABCDE1E2FSeconds(), 3));
			return passLog;
		}).collect(Collectors.toList());

	}
}
