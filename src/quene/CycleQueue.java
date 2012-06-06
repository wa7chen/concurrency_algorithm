package quene;

/**
 * 要点就是数组的添加和删除位置的表示
 * 然后就是队列已满的情况
 * 经过一系列操作队列为空时 rear == font - 1;初始rear == font
 */
public class CycleQueue<T extends Comparable<? super T>> {
	/*
	font表示队列第一个元素的下标值，rear表示最后一个元素的下标值
	 */
	public static final int MAXSIZE = 10;
	private Object[] queue ;
	private int font,rear;
	public int currentSize;
	public CycleQueue(){
		this(MAXSIZE);
	}
	/*
	默认队列从数组中间开始
	 */
	public CycleQueue(int size){
		queue = new Object[size];
		font = rear = queue.length / 2;
		currentSize = 0;
	}
	/*
	计算此时队列里元素的个数
	当rear和font指向相同时，有两种情况
	 */
	public int size(){
		if(rear == font && queue[rear] == null){
			return 0;
		}
		return (rear - font + queue.length) % queue.length + 1;
	}

	public boolean enqueue(T item){
		//这里判断如果是空的队列
		if(queue[font] == null){
			queue[rear] = item;
			currentSize++;
			return true;
		}

		int next = (rear + 1) % queue.length;
		//如果队列已经满了：尾指针+1再模数组长度后和首指针重叠
		if(next == font){
			return false;
		}
		queue[next] = item;
		rear = next;
		currentSize++;
		return true;
	}

	public Object dequeue(){
		//队列为空有两种情况，
		//1，初始化时的状态没改变，首尾指针相同
		//2，经过一系列操作后，font跑到back前面并且queue[font] == null
		if(queue[font] == null|| (rear == font - 1 && queue[rear] == null) ){
			return null;
		}
		Object item = queue[font];
		int next = (font + 1) % queue.length;
		queue[font] = null;
		font = next;
		currentSize--;
		return item;
	}

	public Object peek(){
		if((rear == font - 1 && queue[font] == null) ||(rear == font && queue[rear] == null) ){
			return null;
		}
		return queue[font];
	}

	public static void main(String[] args) {
		CycleQueue<Integer> queue = new CycleQueue();
		for(int i = 0;;i++){
			if(!queue.enqueue(i)) {
				System.out.println("current size is "+queue.currentSize);;break;}
			System.out.println(i);
		}
		System.out.println();
		while(queue.peek() != null){
			System.out.println(queue.dequeue());
		}
	}
/*
大功告成,haha
 */

}
