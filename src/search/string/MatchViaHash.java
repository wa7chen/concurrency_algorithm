package search.string;

/**
 * @author <a href="mailto:chenxiong_ch.pt@taobao.com">chenxiong</a>
 * @since 12-6-7 PM4:50
 */

/**
 * 利用Hash查找字符串，Rabin-Karp 算法
 * 给定长度为M的目标字符串，长度为N的父字符串
 * 计算需要查找子串的hash，然后分别计算个父字符串中sub的hash，
 * 只需要进行一次O(M)的父字符串hash1,下一位字串的hash 可以根据hash1进行2次乘法，1次加法，1一次减法
 *
 * 算法复杂度 O(n + m)
 *
 */
public class MatchViaHash {
	public static long findString(String str ,String subStr){
		if(str == null || subStr == null ||  str.length() == 0 || subStr.length() == 0){
			return  -1;
		}
		long subHash = subStr.hashCode();
		long parentHash = str.substring(0,subStr.length()).hashCode();

		for(int i = 0 ;i < str.length() - subStr.length();i++){
			if(subHash == parentHash){
				return i;
			}
			else {
				parentHash = parentHash * 31 - (long)(str.charAt(i) * Math.pow(31,subStr.length())) + str.charAt(i + subStr.length());
			}
		}
		return -1;
	}
	public static void main(String[] args) {
		String parent = "hello world!";
		String sub = "world";
		System.out.println(findString(parent,sub));
}

}
