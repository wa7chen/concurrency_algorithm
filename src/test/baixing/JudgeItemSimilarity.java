package test.baixing;

import search.string.MatchViaHash;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

/**
 * @author <a href="mailto:chenxiong_ch.pt@taobao.com">chenxiong</a>
 * @since 12-9-18 PM8:39
 */
public class JudgeItemSimilarity {

	//新发布的信息
	private Item item;
	//已经发布的信息
	private Item[] reportedItems;

	public JudgeItemSimilarity(Item item) {
		this.item = item;

		this.reportedItems = selectItemByUserID(item.getUserID());
	}

	/**
	 * 和已发布的信息比较
	 *
	 * @return false 没有重复的
	 *         true  有重复
	 */
	public boolean check() {

		for (Item exist : reportedItems) {
			if (checkTags(item, exist)) {
				if (item.getDesc() == null || checkDesc(item, exist)) {
					if (item.getPicUrls() == null ||
							item.getPicUrls().length == 0 ||
							checkPics(item)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean checkTags(Item item, Item reported) {
		//TODO 顺序比较每个tag

		return true;
	}

	/**
	 * 取待发布描述信息内的10个随机子串，依次与已发布的描述信息匹配
	 * 采用Rabin-Karp 算法,复杂度算法复杂度 O(n + m)，n 为目标串长度，m为待查找串长度
	 *
	 * @param item
	 * @param reported
	 * @return
	 */
	public static boolean checkDesc(Item item, Item reported) {
		//随机获取的子串 必须满足的最短长度
		final int minLength = (int)(item.getDesc().length() * 0.1);
		int unmatch = 0;
		for (int i = 0; i < 10; i++) {
			long isMatch;
			while (true) {
				Random r = new Random();
				int index1 = r.nextInt(item.getDesc().length());
				int index2 = r.nextInt(item.getDesc().length());
				//保证子串长度
				if (index1 - index2 < minLength && index1 - index2 > -minLength)
					continue;
				if (index1 > index2)
					isMatch = MatchViaHash.findString(reported.getDesc(),
							item.getDesc().substring(index2, index1));
				else
					isMatch = MatchViaHash.findString(reported.getDesc(),
							item.getDesc().substring(index1, index2));

				break;
			}
			if (isMatch == -1) {
				unmatch++;
				if (unmatch >= 1) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean checkPics(Item item) {
		int unmatch = 0;
		String[] existPicMD5s = selectPicByUserID(item.getUserID());
		for (String url : item.getPicUrls()) {
			String stringMD5 = fetchPicMD5(url, item.getUserID());

			int result = Arrays.binarySearch(existPicMD5s, stringMD5);
			if (result == -1) {
				unmatch++;
				if (unmatch >= 1) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 根据user id 查询出一天发布的所有宝贝
	 *
	 * @param userID
	 * @return
	 */
	public static Item[] selectItemByUserID(long userID) {
		return new Item[5];
	}

	/**
	 * 根据用户id查出一天发布宝贝的所有图片MD5值
	 *
	 * @param userID
	 * @return
	 */
	public static String[] selectPicByUserID(long userID) {
		return new String[10];
	}

	/**
	 * 掉内部接口，获取对应url的图片数据
	 * 应该有吧。。。
	 * 如果没有直接发送网络请求获取数据
	 *
	 * @param url
	 * @return
	 */
	public static String fetchPicMD5(String url, long userID) {
		//调内部接口
		byte[] picData = queryPicData(url, userID);
		if (picData == null) {
			//没有数据走网络请求
			picData = Utils.fetch(url);
		}
		byte[] md5Bytes = Utils.getMD5FromBytes(picData);

		return Utils.MD5Bytes2String(md5Bytes);
	}

	public static byte[] queryPicData(String url, long userID) {
		//TODO
		return new byte[10000];
	}

	class Item implements Serializable {
		private static final long serialVersionUID = 606828672775212363L;
		private long userID;
		//属性标签
		private int tag1;
		private String tag2;
		private long tag3;

		//描述
		private String desc;
		//图片链接
		private String[] picUrls;

		Item(int tag1, String tag2, long tag3, String desc, String[] picUrls) {
			this.tag1 = tag1;
			this.tag2 = tag2;
			this.tag3 = tag3;
			this.desc = desc;
			this.picUrls = picUrls;

		}

		public long getUserID() {
			return userID;
		}

		public void setUserID(long userID) {
			this.userID = userID;
		}

		public int getTag1() {
			return tag1;
		}

		public void setTag1(int tag1) {
			this.tag1 = tag1;
		}

		public String getTag2() {
			return tag2;
		}

		public void setTag2(String tag2) {
			this.tag2 = tag2;
		}

		public long getTag3() {
			return tag3;
		}

		public void setTag3(long tag3) {
			this.tag3 = tag3;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public String[] getPicUrls() {
			return picUrls;
		}

		public void setPicUrls(String[] picUrls) {
			this.picUrls = picUrls;
		}
	}
}
