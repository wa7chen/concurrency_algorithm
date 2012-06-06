package massip;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: PM2:10
 */

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;


/**
 * 提取出某日访问百度次数最多的那K个IP。<br>
 * 注意，此处简化起见，没有处理重复的情况，即有多个ip出现次数一样多
 *
 * @author Administrator
 *
 */
public class MassIP {
	public static int K10 = 1024 * 10;
	public static int M1 = 1024 * 1024;
	public static int M4 = 4 * M1;
	private static int partationCount = 100;

	/**
	 * @param args
	 * @throws java.io.IOException
	 */
	public static void main(String[] args) throws IOException {
//		generateMassIp("ip", "ips.txt", 100000000);
//		generatePartitionFile("ip", "ips.txt", 100);
		searchTopN(10);
		// searchTopN2("ip", "ips.txt", 10);
	}

	/**
	 * 如果是1亿条记录，直接放到内存排序会如何呢？
	 *
	 * @param srcDirName
	 * @param srcFileName
	 * @param count
	 * @throws java.io.IOException
	 */
	public static void searchTopN2(String srcDirName, String srcFileName,
	                               int count) throws IOException {
		Map<Integer, Integer> ipCountMap = new HashMap<Integer, Integer>();
		File file = FileUtils.getFile(srcDirName, srcFileName);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file), M4);
			String s;
			int i = 0, j = 0;
			while ((s = br.readLine()) != null) {
				int ip = parseIp2Int(s);
				Integer cnt = ipCountMap.get(ip);
				if (cnt == null) {
					i++;
					if (i % 10000000 == 0) {
						System.out.println(i);
					}
				}
				j++;
				if (j % 10000000 == 0) {
					System.out.println(j);
				}
				ipCountMap.put(ip, cnt == null ? 1 : cnt + 1);
			}
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		TopNHeap<IPInfo> heap = new TopNHeap<IPInfo>(count);
		searchMostCountIps(ipCountMap, heap);
		printResult(heap);
	}

	/**
	 * 查找出现次数最多的K个ip地址
	 *
	 * @param count
	 * @throws java.io.IOException
	 */
	public static void searchTopN(int count) throws IOException {
		File[] smallFiles = getPartitionFile("ip", partationCount);
		DataInputStream dis = null;
		Map<Integer, Integer> ipCountMap = new HashMap<Integer, Integer>();
		TopNHeap<IPInfo> heap = new TopNHeap<IPInfo>(count);
		for (int i = 0; i < partationCount; i++) {
			ipCountMap.clear();
			try {
				dis = new DataInputStream(new BufferedInputStream(
						new FileInputStream(smallFiles[i]), K10));
				while (dis.available() > 0) {
					int ip = dis.readInt();
					Integer cnt = ipCountMap.get(ip);
					ipCountMap.put(ip, cnt == null ? 1 : cnt + 1);
				}
				searchMostCountIps(ipCountMap, heap);
			} finally {
				if (dis != null) {
					try {
						dis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		printResult(heap);
	}

	/**
	 * 打印结果集
	 *
	 * @param heap
	 */
	private static void printResult(TopNHeap<IPInfo> heap) {
		while (heap.hasNext()) {
			System.out.println(heap.removeTop().toString());
		}
	}

	/**
	 * 查找出现次数最多的ip
	 *
	 * @param map
	 * @param heap
	 */
	private static void searchMostCountIps(Map<Integer, Integer> map,
	                                       TopNHeap<IPInfo> heap) {
		Iterator<Integer> iter = map.keySet().iterator();
		Integer key = null;
		while (iter.hasNext()) {
			key = iter.next();
			int count = map.get(key);
			if (!heap.isFull() || count > heap.getHeapTop().getCount()) {
				heap.addToHeap(new IPInfo(count, key));
			}
		}
	}

	/**
	 * 把32位int值转换成ip字符串
	 *
	 * @param value
	 * @return
	 */
	public static String parseInt2Ip(int value) {
		StringBuilder sb = new StringBuilder(15);
		String[] segs = new String[4];
		for (int i = 0; i < 4; i++) {
			segs[3 - i] = String.valueOf((0xFF & value));
			value >>= 8;
		}
		for (int i = 0; i < 4; i++) {
			sb.append(segs[i]).append(".");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	/**
	 * 把ip字符串转换成int值
	 *
	 * @param ip
	 * @return
	 */
	public static int parseIp2Int(String ip) {
		String[] segs = ip.split("\\.");
		int rst = 0;
		for (int i = 0; i < segs.length; i++) {
			rst = (rst << 8) | Integer.valueOf(segs[i]);
		}
		return rst;
	}

	/**
	 * 随机生成ip
	 *
	 * @param srcDirName
	 * @param srcFileName
	 * @param count
	 * @throws java.io.IOException
	 */
	public static void generateMassIp(String srcDirName, String srcFileName,
	                                  int count) throws IOException {
		File file = FileUtils.createFile(srcDirName, srcFileName);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
			count = count / 100;
			Random rd = new Random(255);
			StringBuilder sb = new StringBuilder(1500);
			for (int i = 0; i < count; i++) {
				sb.delete(0, sb.length());
				for (int j = 0; j < 100; j++) {
					sb.append(rd.nextInt(100)).append(".");
					sb.append(rd.nextInt(150)).append(".");
					sb.append(rd.nextInt(256)).append(".");
					sb.append(rd.nextInt(256)).append("\n");
				}
				bw.write(sb.toString());
				bw.flush();
			}
		} finally {
			if (bw != null) {
				bw.close();
			}
		}
	}

	public static File[] getPartitionFile(String srcDirName, int count)
			throws IOException {
		File[] files = new File[count];
		for (int i = 0; i < count; i++) {
			files[i] = FileUtils.getFile(srcDirName, i + ".data");
		}
		return files;
	}

	/**
	 * 分割大文件到小文件中
	 *
	 * @param srcDirName
	 * @param srcFileName
	 * @param count
	 * @return
	 * @throws java.io.IOException
	 */
	public static File[] generatePartitionFile(String srcDirName,
	                                           String srcFileName, int count) throws IOException {
		File[] files = new File[count];
		DataOutputStream[] dops = new DataOutputStream[count];
		for (int i = 0; i < count; i++) {
			files[i] = FileUtils.createFile(srcDirName, i + ".data");
		}
		File file = FileUtils.getFile(srcDirName, srcFileName);
		BufferedReader br = null;
		// Buffered
		try {
			for (int i = 0; i < count; i++) {
				dops[i] = new DataOutputStream(new BufferedOutputStream(
						new FileOutputStream(files[i]), K10));
			}
			br = new BufferedReader(new FileReader(file), M4);
			String s;
			while ((s = br.readLine()) != null) {
				int ip = parseIp2Int(s);
				dops[Math.abs(ip % count)].writeInt(ip);
			}
		} finally {
			for (int i = 0; i < count; i++) {
				if (dops[i] != null) {
					try {
						dops[i].close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return files;
	}

	/**
	 * 表示IP信息，存有次数和ip值。在放入堆中时候会用作存储
	 *
	 * @author Administrator
	 *
	 */
	static class IPInfo implements Comparable<IPInfo> {
		private int count;
		private int ipValue;

		public IPInfo(int count, int ipValue) {
			super();
			this.count = count;
			this.ipValue = ipValue;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public int getIpValue() {
			return ipValue;
		}

		public void setIpValue(int ipValue) {
			this.ipValue = ipValue;
		}

		public String getIp() {
			return parseInt2Ip(ipValue);
		}

		@Override
		public int compareTo(IPInfo o) {
			if (this.count > o.getCount()) {
				return 1;
			} else if (this.count < o.getCount()) {
				return -1;
			} else {
				return 0;
			}
		}

		public String toString() {
			return this.getIp() + " -- " + this.count;
		}

	}
}
