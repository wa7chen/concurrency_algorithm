package quene;

import sort.HeapSort;

import java.util.NoSuchElementException;

/*
由二叉堆构造优先队列
 */
public class PriorityQueue<T extends Comparable<? super T>> {
	private T[] queue;
	private int queueSize;
	private HeapSort heapSort;

	public PriorityQueue(T[] queue){
		this.queue = queue;
		queueSize = queue.length;
		heapSort = new HeapSort(queue);
		heapSort.bulidMaxHeapify();
	}
	/*
	查看队列里面优先级最大的元素
	 */
	public T maximum(){
		if(queueSize < 0)
			throw new NoSuchElementException("队列里面没有元素");
		return queue[0];
	}
	//查看并返回队列里面优先级最大的元素
	public T extractMax(){
		if(queueSize < 0)
			throw new NoSuchElementException("队列里面没有元素");
		T max = queue[0];
		heapSort.swap(0,queueSize - 1);
		heapSort.maxHeapify(0);
		queueSize--;
		return max;
	}
	//增加指定下边的元素值，用比原来大的元素替换
	//如果新的元素比以前的小，或者改变失败都将返回false
	public boolean increaseKey(int i ,T key){
		boolean flag = false;
		T item = queue[i];
		if(key.compareTo(item) < 0)
			return false;

		queue[i] = key;
		//如果此节点的父节点小于改变后的该节点,那么继续调整
		//这里应该用上溢的做法，减少了交换，提高性能
		while(i > 0 && queue[parent(i)].compareTo(queue[i]) < 0){
			int parent = parent(i);
			heapSort.swap(i,parent);
			i  = parent;
		}
		flag = true;
		return flag ;
	}

	public boolean maxHeapInsert(T key){
		if(queue.length == queueSize){
			enlarge();
		}
		//这里应该赋为T支持的最小的元素，但是这里使用的是泛型，我还不知道怎么弄
		queue[queueSize] = key;
		queueSize++;
		increaseKey(queueSize - 1,key);
		return true;
	}

	/*
	对队列数组进行扩容
	 */
	public void enlarge(){
		int largerSize = queue.length * 2;
		Object[] newQueue = new Object[largerSize];
		System.arraycopy(queue,0,newQueue,0,queue.length);
		queue = (T[])newQueue;
	}

	/*
	返回i对应的父节点下标
	 */
	public int parent( int i){
		int  parent = i / 2;
		if(i % 2 == 0)
			parent = i / 2 - 1;
		return  parent;
	}

	//还没进行测试
	public static void main(String[] args) {

	}
}
