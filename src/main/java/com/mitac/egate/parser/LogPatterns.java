package com.mitac.egate.parser;

public class LogPatterns {

	public static final String STATUS_NOW = "^.*System Status Change:\\s*.*\\s*to\\s*(\\S*).*$"; //讀出當下閘道狀態代碼
	public static final String STATUS_TO_N00 = "^\\[([^\\]]*)\\].*System Status Change:\\s*.*\\s*to\\s*N00"; //表示旅客通關完全結束(取代RESET_SYSTEM_END)
	public static final String RESET_SYSTEM_START = "^\\[([^\\]]*)\\].*RESET SYSTEM Start.*$"; //重置閘道開始, 通關流程結束時間
	public static final String RESET_SYSTEM_END = "^.*RESET SYSTEM End.*$"; //重置閘道結束, 等待新旅客
	public static final String EMRP_START_TIME = "^\\[([^\\]]*)\\].*emrpType=A;txPattern=.*$"; //護照機讀取開始時間
	public static final String EMRP_END_TIME = "^\\[([^\\]]*)\\].*emrpType=B;txPattern=.*$"; //護照機讀取結束時間
	public static final String QRY_HYWEB_START_TIME = "^\\[([^\\]]*)\\].*CheckPassenger.*BEGIN.*$"; //查驗主機讀取開始時間
	public static final String QRY_HYWEB_END_TIME = "^\\[([^\\]]*)\\].*CheckPassenger END.*$"; //查驗主機讀取結束時間
	public static final String QRY_HYWEB_RCODE = "^.*Rcode=\\[(.*)\\].*$"; //查驗主機回傳RCODE
	public static final String QRY_MNR_START_TIME = "^\\[([^\\]]*)\\].*GetPassengerBiometricData BEGIN.*$"; //閘道管理系統讀取開始時間
	public static final String QRY_MNR_END_TIME = "^\\[([^\\]]*)\\].*GetPassengerBiometricData END.*$"; //閘道管理系統讀取結束時間
	public static final String FACE_START_TIME = "^\\[([^\\]]*)\\].*=== Event => StartFaceVerifyEventHandler ===.*$"; //臉辨開始時間
	
	//public static final String FINGER_START_TIME = "^\\[([^\\]]*)\\].*=== Event => StartFingerVerifyEventHandler ===.*$"; //指辨開始時間
	public static final String FINGER_START_TIME = "^\\[([^\\]]*)\\].*FingerCtrlSender.fingerAction.*.Type=S:A.*$"; //指辨開始時間
	public static final String FINGER_END_TIME = "^\\[([^\\]]*)\\].*FingerCtrlSender.fingerAction.*.Type=S:B.*$"; //指辨結束時間
	public static final String IDENTIFY_END_TIME = "^\\[([^\\]]*)\\].*=== Event => .*FinishVerifyEventHandler ===.*$"; //辨識結束時間, 這邊的 === Event => .* 是用在oneid裡的
	public static final String IDENTIFY_FAIL_SCORE = "^.*===Identify Fail===.*faceScore=([-0-9]*).*fingerScore=([-0-9]*,[-0-9]*).*$"; //辨識失敗臉指辨分數
	public static final String IDENTIFY_PASS_SCORE = "^.*===Identify Pass===.*faceScore=([-0-9]*).*fingerScore=([-0-9]*,[-0-9]*).*$"; //辨識成功臉指辨分數
	public static final String UPD_HYWEB_START_TIME = "^\\[([^\\]]*)\\].*uploadToHYWEB BEGIN.*$"; //上傳查驗系統開始時間
	public static final String UPD_HYWEB_END_TIME = "^\\[([^\\]]*)\\].*uploadToHYWEB END.*$"; //上傳查驗系統結束時間
	public static final String UPD_MNR_START_TIME = "^\\[([^\\]]*)\\].*uploadToMNR BEGIN.*$"; //上傳閘道管理系統開始時間
	public static final String UPD_MNR_END_TIME = "^\\[([^\\]]*)\\].*uploadToMNR END.*$"; //上傳閘道管理系統結束時間
	public static final String MAIN_DOCUMENT = "^.*ValidatePassportEventHandler.validateReadPassport.*mrpdata.*\\[.*passportNum:([^;]*).*$"; //護照號碼
	public static final String DOOR2A_OPEN_TIME = "^\\[([^\\]]*)\\].*Open Flap-2A Success.*$"; //第二道閘門開啟, 通關結束時間
	public static final String DOOR2A_CLOSE_TIME = "^\\[([^\\]]*)\\].*Close Flap-2 Success.*$";
	public static final String EXCEPTION_LOG = "^[^\\[]"; //排除開頭不是[字元的log line, 例如Exception, 
	public static final String TWO_PERSONAL = "^.*\\[TWO_PERSON\\].*$"; //兩人同行
	public static final String EMRP_TIMEOUT = "^.*Reading document timeout.*$"; //護照機Timeout
	public static final String EMRP_EXPIREDATE = "^.*validateReadPassport.*passportNum:[^;]*.*expireDate:([^;]*).*$"; //護照到期日期
	public static final String EMRP_NATIONALITY = "^.*mrpdata\\[.*nationality:([^;]*).*$"; //國籍
	
	public static final String DOOR1A_OPEN_TIME = "^\\[([^\\]]*)\\].* - Open Flap-1A Success.*$";
	public static final String DOOR1A_CLOSE_TIME = "^\\[([^\\]]*)\\].* - close Flap-1 Success.*$";
	
	public static final String FINGER_PRESS_NO_RESULT = "^\\[([^\\]]*)\\].*===FingerResult Event=== .*Result=-1.*$"; //按壓指紋沒結果
	public static final String FINGER_PRESS_HAS_RESULT = "^\\[([^\\]]*)\\].*===FingerResult Event=== .*Result=1.*$"; //按壓指紋有結果
	
	//public static final String FACE_NO_RESULT = "^\\[([^\\]]*)\\].* Face highest score: 0\\(.*$";
	//public static final String FACE_HAS_RESULT = "^\\[([^\\]]*)\\].* Face highest score: [0-9]*[1-9]\\d*.*$";
	//20200630 因為閘道程式改版,關鍵字變更
	public static final String FACE_NO_RESULT = "^\\[([^\\]]*)\\].* Face score: 0\\(.*$";
	public static final String FACE_HAS_RESULT = "^\\[([^\\]]*)\\].* Face score: [0-9]*[1-9]\\d*.*$";
	public static final String FACE_NO_RESULT_END = "^\\[([^\\]]*)\\].* Identity: \\(.*$";
	
	/** $1:日期時間, $2:品質分數 ****/
	public static final String FINGER01_IDENTIFY_THIS_TIME = "^\\[([^\\]]*)\\].*===FingerResult Event=== fingerResult:\\[Device=finger01;.*Quality=(\\d*)\\].*$";
	public static final String FINGER02_IDENTIFY_THIS_TIME = "^\\[([^\\]]*)\\].*===FingerResult Event=== fingerResult:\\[Device=finger02;.*Quality=(\\d*)\\].*$";
	
	/** $1:日期時間, $2:比對分數 ****/
	public static final String Finger01_SCORE_LINE = "^\\[([^\\]]*)\\].*Match Fingerprint\\(RIGHT\\):([^;]*).*$";
	public static final String Finger02_SCORE_LINE = "^\\[([^\\]]*)\\].*Match Fingerprint\\(LEFT\\):([^;]*).*$";
	
	public static final String EMRP_START = "^\\[([^\\]]*)\\].*=== PassportDevice === emrpIDPattern:\\[emrpType=A.*$";
	public static final String EMRP_ERR1 = "^.*(CHIP_NOT_READ).*$";
	public static final String EMRP_ERR2 = "^.*( MRP data is Empty or Invalid).*$";
	public static final String EMRP_PATTERN = "^\\[([^\\]]*)\\].*emrpType=V;txPattern=([^:]*).*$"; 
	public static final String EMRP_STOP = "^\\[([^\\]]*)\\].*=== PassportDevice === emrpIDPattern:\\[emrpType=B.*$";
	public static final String EMRP_STOP1 = "^\\[([^\\]]*)\\].*GetPassengerBiometricData END.*PassportNo=([^,]*).*Result=true, message=([^,:]*).*$"; //從後台返回結果

	public static final String BoardingPass_Start = "^\\[([^\\]]*)\\].*Read barcode data:.*$"; //登機證開始
	public static final String BoardingPass_End = "^\\[([^\\]]*)\\].*=== Event => .*ValidateBoardingPassEventHandler === STOP.*$"; //登機證結束,原本是取這個
	public static final String MAIN_DOCUMENT_BoardingPass = "^.*BarcodeControl.read.*Read barcode data: ([^. ]*).*$"; //BoardingPass用英文姓名
	public static final String BoardingPass_Start2 = "^\\[([^\\]]*)\\].*=== Event => .*ValidateBoardingPassEventHandler === START.*$";
	public static final String BoardingPass_End2 = "^\\[([^\\]]*)\\].*GetPassengerBiometricData BEGIN.*$";  //以上兩個為測試若沒用到可刪

	public static final String Body_Temperature_Start = "^\\[([^\\]]*)\\].*Body Temperature:.*([^. ]*).*$";  //紀錄最後一筆溫度
	public static final String Passport_reader= "^\\[([^\\]]*)\\].*from the passport reader.*$";  //boarding測試

	public LogPatterns() {
		// TODO Auto-generated constructor stub
	}

}
