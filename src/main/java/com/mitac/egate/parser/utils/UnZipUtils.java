package com.mitac.egate.parser.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnZipUtils {
	public static List<String> fileList = new ArrayList<String>();
	public static void main(String[] args) {
		try {
			// 將temp下所有的壓縮文件解壓
			unzipFromLoc("E://Projects//3代閘門//20200311-20200326T071838Z-001//2020031123", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 從總目錄下解壓文件里所有的壓縮包至目的文件路徑
	public static void unzipFromLoc(String filePath, String directoryName) throws Exception {
		File file = new File(filePath);
		File[] list = file.listFiles();// 固定元素，不包含新產生的
		String from = "";
		for (File f : list) {
			if (f.isDirectory()) {
				unzipFromLoc(f.getAbsolutePath(), directoryName + "." + f.getName());
			} else {
				boolean bool = f.isFile();
				if (bool) {
					from = f.getAbsolutePath();
					if (from.indexOf("logs-") <= 0) {
						continue;
					}
					String gateIp = f.getName();
					gateIp = gateIp.substring(gateIp.indexOf("-", 13) + 1, gateIp.indexOf(".zip"));// 擷取@之前的字串
					System.out.println(gateIp);
					unZipFiles(new File(from), filePath, directoryName + "." + gateIp);
				}
			}
		}
	}

	/**
	 * 解壓文件到指定目錄 解壓后的文件名，和之前一致
	 * 
	 * @param zipFile
	 *            待解壓的zip文件
	 * @param descDir
	 *            指定目錄
	 */
	public static void unZipFiles(File zipFile, String descDir, String directoryName) throws IOException {
		ZipFile zip = null;
		System.out.println("");
		try {
			zip = new ZipFile(zipFile, Charset.forName("GBK"));// 解決中文文件夾亂碼
			File pathFile = new File(descDir);
			if (!pathFile.exists()) {
				pathFile.mkdirs();
			}
			descDir = descDir.replace("/", "\\");
			// 即使有內層目錄，目錄里的文件也是可以在這里遍歷出來的，只不過在內層目錄之后，而且是帶內層目錄的全路徑，所以解壓時需要判斷路徑是否存在，
			// 不存在則創建內層目錄dir;判斷一下內層目錄，continue,目錄里面的文件再寫
			for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String zipEntryName = entry.getName();
				if (!zipEntryName.startsWith("jegate-")) { // 僅解壓縮egate log 檔
					continue;
				}
				InputStream in = zip.getInputStream(entry);

				String outPath = "";
				if (zipEntryName.endsWith("zip")) {
					outPath = (descDir + File.separator + zipEntryName).replaceAll("\\\\", "/");
				} else {
					outPath = (descDir + File.separator + zipEntryName + directoryName).replaceAll("\\\\", "/");
					System.out.println("解壓縮檔:" + outPath);
					fileList.add(outPath);
				}
				File file = new File(outPath);
				if (entry.isDirectory()) {// 有內層文件夾，需要創建新的內層目錄后continue,后面內層文件夾里的壓縮文件才有合法新路徑，否則內層文件夾里的壓縮文件使用路徑創建流時報拒絕訪問異常，因為父目錄不存在
					file.mkdirs();// 是文件而不是文件夾路徑的不能寫這個，會創建到以文件名為最終目錄名的全目錄導致與文件路徑沖突，下面流關聯文件時因讀取了同名文件夾而發生異常
					continue;
				}
				// 輸出文件路徑信息
				System.out.println(outPath);
				FileOutputStream out = new FileOutputStream(outPath);
				byte[] buf1 = new byte[1024];
				int len;
				while ((len = in.read(buf1)) > 0) {
					out.write(buf1, 0, len);
				}
				in.close();
				out.close();

				if (zipEntryName.endsWith("zip")) {
					unZipFiles(file, outPath.substring(0, outPath.lastIndexOf('/')), directoryName);// 可遞歸解壓zip,jar文件，不能解壓rar文件
					System.out.println("******************解壓完畢********************");
				}
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			zip.close();
		}
	}
}
