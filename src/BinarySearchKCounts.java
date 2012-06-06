/**
 * @author <a href="mailto:chenxiong_ch.pt@taobao.com">chenxiong</a>
 * @since 12-6-4 PM11:04
 */
//找出给定值K的出现次数
//利用二分查找法，分别确定 K的左右边界
public class BinarySearchKCounts {
	public static int searchRightBoundary(int[] s, int k, int low, int high){
		int mid;
		if(low > high) return low;
		mid = (low + high) / 2;
		if(s[mid] > k) return searchRightBoundary(s, k, low, mid - 1);
		else  return searchRightBoundary(s, k, mid + 1, high);
	}

	public static int searchLeftBoundary(int[] s,int k ,int low ,int high){
		int mid ;
		if(low > high) return high;
		mid = (low + high) / 2;
		if(s[mid] < k) return searchLeftBoundary(s, k, mid + 1, high);
		else return searchLeftBoundary(s, k, low, mid - 1);
	}

	public static int searchCounts(int[] s,int k){
		return searchRightBoundary(s,k,0,s.length - 1) - searchLeftBoundary(s,k,0,s.length - 1) - 1;
	}
	public static void main(String[] args) {
		int[] s = {1,2,3,4,4,4,9,11,13};
//		System.out.println(searchRightBoundary(s, 6, 0, s.length - 1));
//		System.out.println(searchLeftBoundary(s,6,0,s.length - 1));
		System.out.println(searchCounts(s,10));
	}
}
