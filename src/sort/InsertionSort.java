package sort;

import com.sun.rowset.internal.InsertRow;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-5-11
 * Time: 上午10:46
 */
/*
插入排序
 */
public class InsertionSort<T extends Comparable<? super T>> {
	private T[] elements;
	public InsertionSort(T[] elements){
		this.elements = elements;
	}

	public void sort(){
		if(elements.length < 2) return;

		for(int j = 1;j < elements.length;j++){
			T item = elements[j];
			int i = j -1;
			while(i >= 0 && elements[i].compareTo(item) > 0){
				elements[i + 1] = elements[i];
				i--;
			}
			elements[++i] = item;
		}
	}

	public String toString(){
		String result = "";
		for(int i = 0;i < elements.length;i++)
			result += (elements[i] + " ");

		return result;
	}

	public static void main(String[] args) {
		Integer[] integers = {new Integer(5),new Integer(2),new Integer(7),new Integer(1),new Integer(4)};
		InsertionSort<Integer> insertionSort = new InsertionSort<Integer>(integers);
		insertionSort.sort();
		System.out.println(insertionSort);
	}
}
