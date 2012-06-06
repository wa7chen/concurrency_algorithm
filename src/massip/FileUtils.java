package massip;

import java.io.File;
import java.io.IOException;

public class FileUtils {

	public static String directory = "data";

	public static String getDirPath() {
		return System.getProperty("user.dir");
	}

	public static File createFile(String dirName, String fileName)
			throws IOException {
		String dataDir = getDirPath() + File.separator + directory;
		String dir = dirName != null ? dataDir + File.separator + dirName
				: dataDir;
		ensureDirectory(dir);
		String filePath = dir + File.separator + fileName;
		ensureFile(filePath);
		return new File(filePath);
	}

	public static File getFile(String dirName, String fileName)
			throws IOException {
		String dataDir = getDirPath() + File.separator + directory;
		String filePath = (dirName != null ? dataDir + File.separator + dirName
				: dataDir) + File.separator + fileName;
		return new File(filePath);
	}

	public static synchronized void ensureFile(String filePath)
			throws IOException {
		File dataFile = new File(filePath);
		if (!dataFile.exists()) {
			dataFile.createNewFile();
		}
	}

	public static synchronized void ensureDirectory(String dataDir) {
		File path = new File(dataDir);
		if (!path.exists()) {
			path.mkdirs();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(getDirPath());
	}

}
