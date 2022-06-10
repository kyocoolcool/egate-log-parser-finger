package com.mitac.egate.parser.bean;

public class PassengerAnalysis {

	private String logDate;
	private Integer totalCnt;
	private Integer failCnt;
	private Double failPercentage;
	private Integer successCnt;
	private Double successPercentage;
	private Integer ace_N_le_10; //A+C+E 小於等於10
	private Integer ace_10_lt_N_le_12; //A+C+E 10<N<=12
	private Integer ace_12_lt_N_le_15; //A+C+E 12<N<=15
	private Integer ace_N_gt_15; //A+C+E N>15
	
	private Integer ace1e2f_N_le_10; //A+C+E 小於等於10
	private Integer ace1e2f_10_lt_N_le_12; //A+C+E1+E2 10<N<=12
	private Integer ace1e2f_12_lt_N_le_15; //A+C+E1+E2 12<N<=15
	private Integer ace1e2f_N_gt_15; //A+C+E1+E2 N>15	
	
	private Integer abcde_N_le_10; //A+B+C+D+E 小於等於10
	private Integer abcde_10_lt_N_le_12; //A+B+C+D+E 10<N<=12
	private Integer abcde_12_lt_N_le_15; //A+B+C+D+E 12<N<=15
	private Integer abcde_N_gt_15; //A+B+C+D+E N>15
	
	private Integer abcde1e2f_N_le_10; //A+B+C+D+E1+E2 小於等於10
	private Integer abcde1e2f_10_lt_N_le_12; //A+B+C+D+E1+E2 10<N<=12
	private Integer abcde1e2f_12_lt_N_le_15; //A+B+C+D+E1+E2 12<N<=15
	private Integer abcde1e2f_N_gt_15; //A+B+C+D+E1+E2 N>15	
	
	public PassengerAnalysis() {
		// TODO Auto-generated constructor stub
	}

	public String getLogDate() {
		return logDate;
	}

	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}

	public Integer getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(Integer totalCnt) {
		this.totalCnt = totalCnt;
	}

	public Integer getFailCnt() {
		return failCnt;
	}

	public void setFailCnt(Integer failCnt) {
		this.failCnt = failCnt;
	}

	public Double getFailPercentage() {
		return failPercentage;
	}

	public void setFailPercentage(Double failPercentage) {
		this.failPercentage = failPercentage;
	}

	public Integer getSuccessCnt() {
		return successCnt;
	}

	public void setSuccessCnt(Integer successCnt) {
		this.successCnt = successCnt;
	}

	public Double getSuccessPercentage() {
		return successPercentage;
	}
	
	public void setSuccessPercentage(Double successPercentage) {
		this.successPercentage = successPercentage;
	}

	public Integer getAce_N_le_10() {
		return ace_N_le_10;
	}

	public void setAce_N_le_10(Integer ace_N_le_10) {
		this.ace_N_le_10 = ace_N_le_10;
	}

	public Integer getAce_10_lt_N_le_12() {
		return ace_10_lt_N_le_12;
	}

	public void setAce_10_lt_N_le_12(Integer ace_10_lt_N_le_12) {
		this.ace_10_lt_N_le_12 = ace_10_lt_N_le_12;
	}

	public Integer getAce_12_lt_N_le_15() {
		return ace_12_lt_N_le_15;
	}

	public void setAce_12_lt_N_le_15(Integer ace_12_lt_N_le_15) {
		this.ace_12_lt_N_le_15 = ace_12_lt_N_le_15;
	}

	public Integer getAce_N_gt_15() {
		return ace_N_gt_15;
	}

	public void setAce_N_gt_15(Integer ace_N_gt_15) {
		this.ace_N_gt_15 = ace_N_gt_15;
	}

	public Integer getAbcde_N_le_10() {
		return abcde_N_le_10;
	}

	public void setAbcde_N_le_10(Integer abcde_N_le_10) {
		this.abcde_N_le_10 = abcde_N_le_10;
	}

	public Integer getAbcde_10_lt_N_le_12() {
		return abcde_10_lt_N_le_12;
	}

	public void setAbcde_10_lt_N_le_12(Integer abcde_10_lt_N_le_12) {
		this.abcde_10_lt_N_le_12 = abcde_10_lt_N_le_12;
	}

	public Integer getAbcde_12_lt_N_le_15() {
		return abcde_12_lt_N_le_15;
	}

	public void setAbcde_12_lt_N_le_15(Integer abcde_12_lt_N_le_15) {
		this.abcde_12_lt_N_le_15 = abcde_12_lt_N_le_15;
	}

	public Integer getAbcde_N_gt_15() {
		return abcde_N_gt_15;
	}

	public void setAbcde_N_gt_15(Integer abcde_N_gt_15) {
		this.abcde_N_gt_15 = abcde_N_gt_15;
	}
	
	public Integer getAce1e2f_N_le_10() {
		return ace1e2f_N_le_10;
	}

	public void setAce1e2f_N_le_10(Integer ace1e2f_N_le_10) {
		this.ace1e2f_N_le_10 = ace1e2f_N_le_10;
	}

	public Integer getAce1e2f_10_lt_N_le_12() {
		return ace1e2f_10_lt_N_le_12;
	}

	public void setAce1e2f_10_lt_N_le_12(Integer ace1e2f_10_lt_N_le_12) {
		this.ace1e2f_10_lt_N_le_12 = ace1e2f_10_lt_N_le_12;
	}

	public Integer getAce1e2f_12_lt_N_le_15() {
		return ace1e2f_12_lt_N_le_15;
	}

	public void setAce1e2f_12_lt_N_le_15(Integer ace1e2f_12_lt_N_le_15) {
		this.ace1e2f_12_lt_N_le_15 = ace1e2f_12_lt_N_le_15;
	}

	public Integer getAce1e2f_N_gt_15() {
		return ace1e2f_N_gt_15;
	}

	public void setAce1e2f_N_gt_15(Integer ace1e2f_N_gt_15) {
		this.ace1e2f_N_gt_15 = ace1e2f_N_gt_15;
	}

	public Integer getAbcde1e2f_N_le_10() {
		return abcde1e2f_N_le_10;
	}

	public void setAbcde1e2f_N_le_10(Integer abcde1e2f_N_le_10) {
		this.abcde1e2f_N_le_10 = abcde1e2f_N_le_10;
	}

	public Integer getAbcde1e2f_10_lt_N_le_12() {
		return abcde1e2f_10_lt_N_le_12;
	}

	public void setAbcde1e2f_10_lt_N_le_12(Integer abcde1e2f_10_lt_N_le_12) {
		this.abcde1e2f_10_lt_N_le_12 = abcde1e2f_10_lt_N_le_12;
	}

	public Integer getAbcde1e2f_12_lt_N_le_15() {
		return abcde1e2f_12_lt_N_le_15;
	}

	public void setAbcde1e2f_12_lt_N_le_15(Integer abcde1e2f_12_lt_N_le_15) {
		this.abcde1e2f_12_lt_N_le_15 = abcde1e2f_12_lt_N_le_15;
	}

	public Integer getAbcde1e2f_N_gt_15() {
		return abcde1e2f_N_gt_15;
	}

	public void setAbcde1e2f_N_gt_15(Integer abcde1e2f_N_gt_15) {
		this.abcde1e2f_N_gt_15 = abcde1e2f_N_gt_15;
	}

	@Override
	public String toString() {
		return "PassengerAnalysis [logDate=" + logDate + ", totalCnt=" + totalCnt + ", failCnt=" + failCnt
				+ ", failPercentage=" + failPercentage + ", successCnt=" + successCnt + ", successPercentage="
				+ successPercentage + ", ace_N_le_10=" + ace_N_le_10 + ", ace_10_lt_N_le_12=" + ace_10_lt_N_le_12
				+ ", ace_12_lt_N_le_15=" + ace_12_lt_N_le_15 + ", ace_N_gt_15=" + ace_N_gt_15 + ", ace1e2_N_le_10="
				+ ace1e2f_N_le_10 + ", ace1e2_10_lt_N_le_12=" + ace1e2f_10_lt_N_le_12 + ", ace1e2_12_lt_N_le_15="
				+ ace1e2f_12_lt_N_le_15 + ", ace1e2_N_gt_15=" + ace1e2f_N_gt_15 + ", abcde_N_le_10=" + abcde_N_le_10
				+ ", abcde_10_lt_N_le_12=" + abcde_10_lt_N_le_12 + ", abcde_12_lt_N_le_15=" + abcde_12_lt_N_le_15
				+ ", abcde_N_gt_15=" + abcde_N_gt_15 + ", abcde1e2_N_le_10=" + abcde1e2f_N_le_10
				+ ", abcde1e2_10_lt_N_le_12=" + abcde1e2f_10_lt_N_le_12 + ", abcde1e2_12_lt_N_le_15="
				+ abcde1e2f_12_lt_N_le_15 + ", abcde1e2_N_gt_15=" + abcde1e2f_N_gt_15 + "]";
	}

}
