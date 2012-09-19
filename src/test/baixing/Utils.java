package test.baixing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author <a href="mailto:chenxiong_ch.pt@taobao.com">chenxiong</a>
 * @since 12-9-18 PM9:18
 */
public class Utils {
	private static char[] HEX_DIGITS = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

	/**
	 * 发送http请求获取图片数据
	 * @param url
	 * @return
	 */
	public static byte[] fetch(String url) {


		return new byte[100];
	}

	/**
	 * 将计算过后16位长的byte数组转成字符串表示的数据
	 * @param md5Bytes 16位长的表示MD5的byte数组
	 * @return 32位长度的字符串
	 */
	public static String MD5Bytes2String(byte[] md5Bytes) {
		if(md5Bytes != null && md5Bytes.length == 16 ) {
			StringBuilder s = new StringBuilder();
			for (int i = 0; i < md5Bytes.length; i++) {
				byte b = md5Bytes[i];
				s.append(HEX_DIGITS[b >>> 4 & 0x0f]);
				s.append(HEX_DIGITS[b & 0x0f]);
			}
			return s.toString();
		}

		return null;
	}

	/**
	 * 计算MD5值
	 * @param data
	 * @return
	 */
	public static byte[] getMD5FromBytes(byte[] data) {
		if( data != null && data.length > 0) {
			try {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				return md5.digest(data);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
