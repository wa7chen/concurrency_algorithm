package sort;

import com.sun.xml.internal.ws.api.message.Header;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-4-5
 * Time: 下午2:31
 */
public class HeapSort<T extends Comparable<? super T>> {
	private int heapSize;
	private T[] items;

	public HeapSort(T[] items){
		this.items = items;
		heapSize = items.length;
	}

	/*
	将数组构造成二叉堆
	由下至上依次构建最大堆
	 */
	public void bulidMaxHeapify(){
		//这里从倒数第二层开层的最后一个不是叶子节点的元素开始
		//最后一个元素 n 除 2 就是它父节点的下标
		for(int i = heapSize / 2 ;i >= 0;i--){
			 maxHeapify(i);
		}
	}
	/*
	使数组满足二叉堆的性质，
	将坐标为i的元素与左右节点比较，取最大的放在i处
	注意左节点是 2*i；右节点是2*i + 1
	 */
	public void maxHeapify(int i){
		if( i > heapSize / 2)
			return;
		int left = i * 2;
		int right = i * 2 + 1 ;
		if(i == 0){
			left = 1;
			right = 2;
		}
		int largest = i ;
		if(left < heapSize && items[left].compareTo(items[i]) > 0  ){
			largest = left;
		}
		//注意这里是和largest比较
		if(right < heapSize && items[right].compareTo(items[largest]) > 0){
			largest = right;
		}
		if(i != largest){
			swap(largest,i);
			//调整后继续调整改变后的子树
			maxHeapify(largest);
		}
	}
	/*
	进行排序
	 */
	public T[] heapSort(){
		//先构建二叉堆
		bulidMaxHeapify();

		for(int i = items.length - 1;i > 0;i--){
			//将最大根和最后一个元素交换，然后使堆的大小-1；
			swap(i,0);
			heapSize--;
			//对改变后的二叉堆重新进行最大堆化
			maxHeapify(0);
		}
		return items;
	}

	public void swap(int a,int b ){
		T temp = items[a];
		items[a] = items[b];
		items[b] = temp;
	}

	public static void main(String[] args) {
		Integer[] items = {6,2,7,17,15,3,8,14,10};
		HeapSort<Integer> heapSort = new HeapSort<Integer>(items);
		items = heapSort.heapSort();
		for(int i:items){
			System.out.println(i);
		}
	}
}
