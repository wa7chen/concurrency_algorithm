package list.linkedlist;

import java.util.ListIterator;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-3-25
 * Time: 下午7:50
 */
public class LinkedList<E> {

    int size = 0;
    Node<E> head = new Node(null, null, null);

    public LinkedList() {
        head.next = head.previous = head;
    }

    public void add(E node) {
        //核心 循环双向链表
        Node<E> newNode = new Node(head.previous, node, head);   //新节点的prev指向头结点的prev 新节点的next指向头结点
        newNode.previous.next = newNode;    //调整，新节点的前一个的后一个
        newNode.next.previous = newNode;    //调整，新节点的后一个的前一个
        size++;
    }

    public Node get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index:" + index + ",size:" + size);
        }
        Node node = head;
        if (index < (size >> 1)) {          //index转为二进制带符号向右移动一个bit，相当于index/2
            for (int i = 0; i <= index; i++) {    //head是哑元，i<=index当index=0时，返回head.next
                node = node.next;           //对头结点进行迭代
            }
        }else{
            for(int i=size;i>index;i--){
                node=node.previous;
            }
        }
        return node;
    }

    public int size() {
        return size;
    }

	private class LikedListIterator implements ListIterator{

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

    public static void main(String[] args) {
        LinkedList list = new LinkedList();
        list.add(1);
        list.add(2);
        for (int i = 0; i < list.size; i++) {
            System.out.println(list.get(i).node);
        }
    }
}

class Node<E> {

    E node;
    list.linkedlist.Node<E> next;
    list.linkedlist.Node<E> previous;

    public Node(list.linkedlist.Node<E> previous, E node, list.linkedlist.Node<E> next) {
        this.node = node;
        this.next = next;
        this.previous = previous;
    }
}