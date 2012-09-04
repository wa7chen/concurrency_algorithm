package search;

/**
 * 输入一个数组和一个数字，在数组中查找两个数，使得它们的和正好是输入的那个数字
 * @author <a href="mailto:chenxiong_ch.pt@taobao.com">chenxiong</a>
 * @since 12-9-3 下午4:17
 */

/**
 * 1.这里实现的是两端夹逼法,排好序是O(N)
 */
public class SearchCombinationNum {
	/**
	 * 如果数组无序，需先进行排序
	 *
	 * @param array 给定数组
	 * @param num 目标值
	 * @param offset 可以事先确定的左边界,减少查找次数
	 */
	public static void searchPress(int[] array, int num,int offset) {
		int left = 0;
		int right = array.length - 1;

		while (left < right) {
			int sum = array[left] + array[right];
			//如果sum大，那么尽量减少，让右端的数左移
			if (sum > num) {
				right--;
			}
			//如果sum小，那么尽量增大，让左端的数右移
			else if (sum < num) {
				left++;
			}
			else {
				System.out.println("got it:" + array[left] + " + " + array[right]);
				return;
			}
		}
	}

	/**
	 * 2.bit hash法也是O(N),但是耗费O(N)的空间复杂度
	 * @param array
	 */
	public static void searchHash(int[] array,int num,int offset) {

	}
	public static void main(String[] args) {

		int[] array = {1,3,5,7,8,13};
		searchPress(array,15,array.length);
	}
}
