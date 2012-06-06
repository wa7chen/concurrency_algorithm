package massip;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: PM2:12
 */

import java.util.Random;

public class TopNHeap<T extends Comparable<? super T>> {

	private int size;
	private int maxSize;
	private Object[] data = new Object[256];

	public TopNHeap(T[] data) {
		super();
		this.initHeap(data);
	}

	public TopNHeap(int fixedSize) {
		super();
		assert fixedSize >= 1;
		this.maxSize = fixedSize;
	}


	@SuppressWarnings("unchecked")
	public void initHeap(T[] data) {
		assert data.length >= 1;
		if (this.data.length <= this.size) {
			this.data = new Object[(int) (data.length * 1.5)];
		}
		this.maxSize = this.size = data.length;
		System.arraycopy(data, 0, this.data, 0, this.size);
		int startPos = this.getParentIndex(this.size - 1);
		for (int i = startPos; i >= 0; i--) {
			this.shiftdown(i);
		}
	}

	@SuppressWarnings("unchecked")
	public T getHeapTop() {
		return (T) this.data[0];
	}

	/**
	 * 加元素到堆中，但是保持堆 的大小
	 *
	 * @param value
	 */
	public void addToHeap(T value) {
		if (this.maxSize > this.size) {
			this.data[this.size] = value;
			this.shiftup(this.size++);
		} else {
			if (value.compareTo(this.getHeapTop()) > 0) {
				this.data[0] = value;
				this.shiftdown(0);
			}
		}
	}

	private void shiftup(int pos) {
		int parentIdx = this.getParentIndex(pos);
		while (pos != 0
				&& this.getValue(pos).compareTo(this.getValue(parentIdx)) < 0) {
			this.swap(pos, parentIdx);
			pos = parentIdx;
			parentIdx = this.getParentIndex(pos);
		}
	}

	public T removeTop() {
		T rst = this.getHeapTop();
		this.data[0] = this.data[--this.size];
		this.shiftdown(0);
		return rst;
	}

	public boolean hasNext() {
		return this.size > 0;
	}

	@SuppressWarnings("unchecked")
	public T[] getData() {
		return (T[]) this.data;
	}

	@SuppressWarnings("unchecked")
	public T getValue(int index) {
		return (T) this.data[index];
	}

	private int getParentIndex(int pos) {
		return (pos - 1) / 2;
	}

	private int getLeftChildIdx(int pos) {
		return pos * 2 + 1;
	}

	private int getRightChildIdx(int pos) {
		return pos * 2 + 2;
	}

	private void swap(int idx1, int idx2) {
		T tmp = this.getValue(idx1);
		this.data[idx1] = this.getValue(idx2);
		this.data[idx2] = tmp;
	}

	/**
	 * 节点值向下级交换，构造堆
	 *
	 * @param pos
	 */
	private void shiftdown(int pos) {
		int leftChildIdx = this.getLeftChildIdx(pos);
		// 没有子节点了
		if (leftChildIdx >= this.size) {
			return;
		}
		int rightChildIdx = getRightChildIdx(pos);
		int toBeSwapIdx = leftChildIdx;
		if (rightChildIdx < this.size
				&& this.getValue(leftChildIdx).compareTo(
				this.getValue(rightChildIdx)) > 0) {
			toBeSwapIdx = rightChildIdx;
		}
		// swap
		if (this.getValue(pos).compareTo(this.getValue(toBeSwapIdx)) > 0) {
			this.swap(pos, toBeSwapIdx);
			this.shiftdown(toBeSwapIdx);
		}
	}

	public boolean isFull(){
		return this.maxSize == this.size;
	}


	public int getMaxSize() {
		return maxSize;
	}
//
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Integer[] data = { 7, 12, 13, 24, 8, 6, 4, 27, 14, 8, 12, 56, 22 };
		TopNHeap<Integer> heap = new TopNHeap<Integer>(data);
		while (heap.hasNext()) {
			System.out.print(heap.removeTop());
			System.out.print("  ");
		}

		System.out.println("  ");
		heap.initHeap(data);
		for (int i = 0; i < 10; i++) {
			heap.addToHeap(i);
		}
		while (heap.hasNext()) {
			System.out.print(heap.removeTop());
			System.out.print("  ");
		}

		System.out.println("  ");
		heap = new TopNHeap<Integer>(10);
		Random rd = new Random();
		for (int i = 0; i < 20; i++) {
			int value = rd.nextInt(100);
//			System.out.print(value);
//			System.out.print("  ");
			heap.addToHeap(value);
		}
		System.out.println("  ");
		while (heap.hasNext()) {
			System.out.print(heap.removeTop());
			System.out.print("  ");
		}

	}

}
