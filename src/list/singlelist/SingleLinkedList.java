package list.singlelist;
public class SingleLinkedList<T> {

    int size = 0;
    Node<T> head, tail;

    public SingleLinkedList() {
        head = tail = null;
    }

    public void add(T node) {
        if (size == 0) {
            head = tail = new Node<T>(node, null);
        } else {
            tail.next = new Node<T>(node, null);
            tail = tail.next;
        }
        size++;
    }

    public Node<T> get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index:" + index + ",size:" + size);
        }
        Node<T> node = head;
        for (int i = 0; i < index; i++) { //当index=0时，i不小于index(零不小于零)，于是直接return头结点，与linkedList不同，head不是哑元
            node = node.next;           //对头结点进行迭代
        }
        return node;
    }

    public int size(){
        return size;
    }

    public static void main(String[] args){
        SingleLinkedList list=new SingleLinkedList();
        list.add(1);
        list.add(2);
        list.add(3);
        for(int i=0;i<list.size();i++){
            System.out.println(list.get(i).node);
        }
    }
}

class Node<T> {
    T node;
    Node<T> next;

    public Node(T node, Node next) {
        this.node = node;
        this.next = next;
    }
}