package stack;

import java.util.EmptyStackException;

/*
基于数组实现的栈
 */
public class MyStack {
	public static final int DEFAULT = 10;
	private Object[] elements;
	private int size ;
	public MyStack(){
		clear();
	}
	/*
	初始化
	 */
	public void clear(){
		elements = new Object[DEFAULT];
		size = 0;
	}

	public int size(){
		return size;
	}
	/*
	扩容数组
	 */
	public void enlarge(){
		int largeSize = elements.length * 2;
		Object[] newItems = new Object[largeSize];
		System.arraycopy(elements,0,newItems,0,elements.length);
		elements = newItems;
	}
	/*
	向stack中添加元素
	 */
	public Object push(Object item){
		if(size == elements.length){
			enlarge();
		}
		elements[size] = item;
		size++;

		return item;
	}
	/*
	Looks at the object at the top of this stack without removing it
	注意这里必须要是线程同步的synchronized
	  */
	public synchronized Object peek(){
		if(size == 0 ){
			throw new EmptyStackException();
		}
		return elements[size - 1];
	}

	public synchronized Object pop(){
		Object obj = peek();
		elements[size - 1 ] = 0;
		size--;
		return obj  ;
	}

	/*
	查找元素，不存在返回-1；
	 找到后返回找到的第一个元素的下标
	 */
	public synchronized int search(Object obj){
		int index = - 1;
		for(int i = 0;i < size;i++){
			if(elements[i].equals(obj)){
				index = i;
				break;
			}
		}
		return index;
	}

	public static void main(String[] args) {
//		MyStack stack = new MyStack();
//		for(int i = 1;i < 20 ;i++){
//			stack.push(i);
//		}
//
//		stack.pop();
//		System.out.println(stack.size() +" ===========");
//		for(int i = stack.size();i >0;i--){
//			System.out.println(stack.pop());
//		}
//		System.out.println(stack.size() +" ===========");
	}



}
