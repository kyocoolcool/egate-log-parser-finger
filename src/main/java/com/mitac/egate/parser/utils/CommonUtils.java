package com.mitac.egate.parser.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class CommonUtils {


	private static final String TIME_FORMAT1 = "[uuuu-MM-dd HH:mm:ss,SSS]";
	private static final String TIME_FORMAT2 = "[uu-MM-dd HH:mm:ss.SSS]";
	private static final DateTimeFormatter formatter = 
			DateTimeFormatter
				.ofPattern(TIME_FORMAT1 + TIME_FORMAT2)
				.withResolverStyle(ResolverStyle.STRICT);
	public CommonUtils() {
		// TODO Auto-generated constructor stub
	}

/**
 * pom.xml 改以spring-boot-maven-plugin 後這個方式已經不管用了, MANIFEST.MF會指向BOOT-INF/下任一個library jar裡面的MANIFEST.MF
 * 而不是EgateLogParser.jar 的META-INF目錄
 */
//	public static String getVersion() {
//		URLClassLoader cl = (URLClassLoader)CommonUtils.class.getClassLoader();
//
//		try {
//			URL url = cl.findResource("META-INF/MANIFEST.MF");
//			Manifest manifest = new Manifest(url.openStream());
//			return manifest.getMainAttributes().getValue("Build-Time");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}		
//	}

	/**
	 * pom.xml 改用 spring-boot-maven-plugin 後改用此方式取得版本號
	 * @return
	 */
	public static String getVersion() {
	    try {
	    	/**
	    	 * 此處在開發環境與run jar時取得的Object type不同, 開發環境中轉型JarFile會失敗是正常的
	    	 */
	        final JarFile jarFile = (JarFile) CommonUtils.class.getProtectionDomain().getCodeSource().getLocation().getContent();
	        final Manifest manifest = jarFile.getManifest();
	       
	        return manifest.getMainAttributes().getValue("Build-Time");

	    } catch (final Exception e) {
	    	return null;
	    }		
	}
	
	public static double getSeconds(String startDateTime, String endDateTime) {
		LocalDateTime start = LocalDateTime.parse(startDateTime, formatter);
		LocalDateTime end = LocalDateTime.parse(endDateTime, formatter);
		long milli = ChronoUnit.MILLIS.between(start, end);
		return milli / 1000.0;
	}
	
	public static Double calculateSecond(Double... seconds) {
		return Arrays.stream(seconds)
				.filter(dou -> dou != null)
				.collect(Collectors.summingDouble(Double::doubleValue));

	}
	
	public static String formatDate(String fromDate, String fromPattern, String toPattern) {
		if (StringUtils.isBlank(fromDate)) {
			return fromDate;
		}
		DateTimeFormatter dtf = 
				DateTimeFormatter
					.ofPattern(fromPattern)
					.withResolverStyle(ResolverStyle.STRICT);
		LocalDate date = LocalDate.parse(fromDate, dtf);
		return date.format(DateTimeFormatter.ofPattern(toPattern));
	}
	
	public static String formatDouble(Double d) {
		return formatDouble(d, "#.000");
	}
	
	public static String formatDouble(Double d, String pattern) {
		if (d == null) {
			return "";
		}
		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(d);
	}
	
	/**
	 * 小數點位數限制(四捨五入)
	 * @param d 小數
	 * @param i 位數
	 * @return
	 */
	public static double decimalRound(double d, int i) {
		double k = Math.pow(10, i);
		return Math.round((d * k)) / k;
	}
	
	public static Double decimalRoundD(Double d, int i) {
		return d == null ? null : decimalRound(d, i);
	}
	
	public static String DateTimeToDate(String startDateTime) {
		if (StringUtils.isBlank(startDateTime)) {
			return "";
		}
		LocalDate ld = LocalDate.parse(startDateTime, formatter);
		return ld.format(DateTimeFormatter.ofPattern("uuuu-MM-dd"));
	}
	
	public static String DateTimeToTime(String startDateTime) {
		if (StringUtils.isBlank(startDateTime)) {
			return "";
		}
		LocalDateTime ldt = LocalDateTime.parse(startDateTime, formatter);
		return ldt.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
	}
	
	public static LocalTime DateTimeToLocalTime(String startDateTime) {
		if (StringUtils.isBlank(startDateTime)) {
			return null;
		}
		LocalDateTime ldt = LocalDateTime.parse(startDateTime, formatter);
		return ldt.toLocalTime();
		
	}
	
	/**
	 * 
	 * @param denominator 分母
	 * @param molecule 分子
	 * @return
	 */
	public static Double calculatePercentage(Double denominator, Double molecule) {
		if (denominator == null || molecule == null) {
			return null;
		}
		return (molecule/denominator) * 100.0;
	}
	
	public static Optional<String> getArgs(List<String> argsList, String pattern) {
		return argsList.stream()
				.filter(str -> str.matches(pattern))
				.findFirst();
	}
	
	public static List<Path> getFiles(Path path) throws IOException {
		return Files
				.list(path)
				.filter(p -> !Files.isDirectory(p)).collect(Collectors.toList());
	}	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("seconds=" + calculatePercentage(312.0, 33.0));
	}

}
