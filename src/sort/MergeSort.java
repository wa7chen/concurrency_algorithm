package sort;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-3-22
 * Time: 下午6:07
 */
/*
合并排序
主要问题就是在合并那块的处理上，多看看merge这个方法
 */
public class MergeSort {
	static int[] b = new int[10];
	//这里是递归实现
	public static void meSort(int[] a ,int left , int right){
		//对子数组不断拆分
		if(left < right){
			int mid = ( left + right) / 2;
			//对数据进行拆分
			meSort(a,left,mid);
			meSort(a,mid + 1,right);
			//合并拆分的两半
			merge(a,b,left,right);
			//将数组拷贝到原数组中,长度是right-left + 1
			System.arraycopy(b,left,a,left,right - left + 1);
		}
	}

	//合并数组
	public static void merge(int[] a ,int[] b ,int left,int right){
		int mid = (left + right) / 2;
		int leftPos = left,rightPos = mid + 1;

		//index缓存数据的索引
		int index = left;
		/*
		分三种情况
		1，左右都没超界，那么一起比较，推进
		2，右边已经超界，那么循环右边的，直到结束
		3，左边超界
		 */
		//这里左右均在界限
		while(leftPos <= mid && rightPos <= right){
			if(a[leftPos] <= a[rightPos]){
				b[index++] = a[leftPos++];
			}
			else {
				b[index++] = a[rightPos++];
			}
		}
		while(leftPos <= mid){
			b[index++] = a[leftPos++];
		}
		while(rightPos <= right){
			b[index++] = a[rightPos++];
		}
	}

	public static void main(String[] args) {
		int[] a = {8,1,3,9,7,2,5,6};
		meSort(a,0,a.length - 1);
		for(int i : a){
			System.out.println(i + " ");
		}
	}
}
