package list.linkedlist;

import java.util.NoSuchElementException;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-3-26
 * Time: 上午8:49
 */
//带头结点双循环链表
public class MyLinkedList<T> {
	private Node<T> head = new Node(null,null,null);
	private int size = 0 ;
	private int modCount = 0;

	public MyLinkedList(){
		head.next = head.previous = head;
	}

//清空链表
	public void clear(){
		for(Node<T> temp = head.next;temp != head ;){
			//获取下一个结点
			Node<T> next = temp.next;
			//将指针清空
			temp.next = temp.previous = null;
			//将元素清空
			temp.element = null;
			temp = next;
		}
		//进行初始化操作
		head.next = head.previous = head;
		size = 0;
		modCount++;
	}

	public int size(){
		return size;
	}
	/*
	添加元素 在链表尾部添加，并将生成的结点返回
	 */
	public Node<T> addBefore(T newEle){
		Node<T> newNode = new Node(head.previous,newEle,head);
		//改变插入后元素的指针
		head.previous.next = newNode.next.previous = newNode;
		size++;
		modCount++;
		return newNode;
	}
	//在指定位置添加元素
	public Node<T> add(int index,T newEle){
		Node<T> nodeBefore = entry(index);
		Node<T> newNode = new Node(nodeBefore.previous,newEle,nodeBefore);
		newNode.previous.next = newNode.next.previous = newNode;
		size++;
		modCount++;
		return newNode;
	}

	//通过下标返回指定结点元素
	public Node<T> entry(int index){
		if(index < 0 || index > size ){
			throw new IndexOutOfBoundsException("Index: "+index+", Size: "+size);
		}
		Node<T> temp = head;
		//向后移一位，即除以2
		if(index < (size >> 1)){
			for(int i = 0;i <= index;i++){
				temp = temp.next;
			}
		}else{
			for(int i = size;i > index;i--){
				temp = temp.previous;
			}
		}
		return temp;
	}
	public boolean contain(T element){
		return indexOf(element) != -1;
	}

	/*
	查找元素在链表中的位置，如果不存在返回-1
	 */
	public int indexOf(T element){
		int index = 0;
		if(element == null){
			for(Node<T> temp = head.next;temp != head;temp = temp.next){
				if(temp.element == null)
					return index;
				index++;
			}
		}else{
			for(Node<T> temp = head.next;temp != head;temp = temp.next){
				//注意这里是比较内容，不能是==
				if(element.equals(temp.element))
					return index;
				index++;
			}
		}
		return -1;
	}
	/*
	过结点元素删除
	如果存在那么调用删除结点的remove返回true 否则返回false
	 */

	public boolean remove(T element){
		if(element == null){
			for(Node<T> temp = head.next;temp != head;temp = temp.next ){
				if(temp.element == null){
					remove(temp);
					return true;
				}
			}
		}else{
			for(Node<T> temp = head.next;temp != head;temp = temp.next ){
				if(element.equals(temp.element)){
					remove(temp);
					return true;
				}
			}
		}
		return false;
	}

	/*
	通过结点删除，并返回删除结点的元素
	 */
	 public T remove(Node<T> oldNode){
		 //这里还有一个特殊情况需要考虑
		 if(oldNode == head){
			 throw new NoSuchElementException();
		 }
		 T result = oldNode.element;
		 oldNode.previous.next = oldNode.next;
		 oldNode.next.previous = oldNode.previous;
		 oldNode.previous = oldNode.next = null;
		 oldNode.element = null;
		 size--;
		 modCount++;

		 return result;
	 }

	public T set(int index,T element){
		Node<T> node = entry(index);
		T oldElement = node.element;
		node.element = element;
		return oldElement;
	}
	public ListIterator iterator(){
		return  new ListIterator();
	}

	private class ListIterator implements java.util.ListIterator{

		public boolean hasNext() {
			return false;
		}

		public Object next() {
			return null;
		}

		public boolean hasPrevious() {
			return false;
		}

		public Object previous() {
			return null;
		}

		public int nextIndex() {
			return 0;
		}

		public int previousIndex() {
			return 0;
		}

		public void remove() {

		}

		public void set(Object o) {

		}

		public void add(Object o) {

		}
	}

	private static class Node<T>{
		private Node<T> previous;
		private Node<T> next;
		private T element;
		public Node(Node<T> p,T e,Node<T> next){
			this.element = e;
			this.next = next;
			this.previous = p;
		}
		public void get(){
		}

	}

	public static void main(String[] args) {
		MyLinkedList list = new MyLinkedList();
		list.addBefore(1);
		list.addBefore(2);
		list.addBefore(5);
		list.addBefore(7);
		System.out.println(list.remove(2) + "---");
		System.out.println(list.contain(5));
		System.out.println(list.contain(2));

		for(int i = 0 ;i < list.size;i++){
			System.out.println(list.entry(i).element);
		}
	}
}
