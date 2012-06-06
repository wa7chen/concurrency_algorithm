package sort
		;

/**
 *最困难的就是边界问题的处理，决定何时跳出循环很重要，0，1，2都有可能
 * getMidValue算法很有技巧
 */

/*
快速排序算法递归实现
 */
public class QuickSort {
	/*
	先找出一个中间值，partition方法。让划分更平均，那么时间复杂度约低
	然后对划分的子数组继续快排
	 */
	public static void qSort(int[] a ,int left,int right){
		//这里的边界是只剩下2个元素的时候的情况，因为划分的时候必须要存在3个值
		if (left + 1< right) {
			int part = partition(a,left,right);
			//这时中间值不用参与划分，
			qSort(a,left,part - 1 );
			qSort(a,part + 1,right);
		}
		else {
			if(a[right] < a[left]) swap(a,right,left);
		}
	}

	//划分数组的方法
	public static int partition(int[] a,int left,int right ){
		int midValue = getMidValue(a,left,right);
		//i 和 j 的开始位置也很重要，在getMidValue中已经将三个位置的值排好序
		int i = left + 1, j = right ;
		//不应该在这里判断i >= j，应该在i和j增减完后立即判断，否则会调换错误
		while(true){
			while(a[++i] < midValue){}
			while(a[--j] > midValue){}
			if(i > j) break;
			//没有过界就交换
			swap(a,i,j);
		}
		//将中间值调到正确位置，还原
		//这里和j对调也是有原因的，因为我把中值安排在左半部，那么j最后停留的位置是小于中值的，而i停留的位置是大于中值的
		swap(a,left + 1,j);
		return j;
	}
	//取出在第一，中间和最后一项中值是中间的
	//并且将它们三进行排序
	//排好后将最大的放在right 最小的放在left 中间值和left + 1交换
	//将中间值返回
	public static int getMidValue(int[] a ,int left ,int right){

		int mid = (left + right) / 2;
		//这个方法很简洁很高效好好揣摩,顺序也很重要，如果有重复元素的话，不是这个顺序会出现问题 
		//首先确定把两个排好序
		if(a[mid] < a[left]) swap(a,mid,left);
		//将第三个依次与前两个比较
		if(a[right] < a[left]) swap(a,right,left);
		if(a[right] < a[mid]) swap(a,right,mid);
		//将中值转到left+1
		swap(a,left + 1,mid );
		//返回中值
		return a[left + 1];
	}

	public static void swap(int[] a ,int i,int j ){
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	public static void main(String[] args) {
		int[] a = {8,1,3,9,7,2,5,6,1,5};
		qSort(a,0,a.length - 1);
		for(int i : a){
			System.out.println(i + " ");
		}

	}


}
