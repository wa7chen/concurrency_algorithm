package tree;

import binarySearch.BinarySearch;
import com.sun.istack.internal.Nullable;

import java.util.NoSuchElementException;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-4-13
 * Time: 下午10:40
 * 二叉查找树的实现
 */
public class BinarySearchTree<T extends Comparable<? super T>> {
	private BinaryNode<T> root;

	public BinarySearchTree(){
		root = null;
	}
	public void makeEmpty(){
		root = null;
	}
	public boolean isEmpty(){
		return root == null;
	}

	public boolean contain(T element){
		return contain(element,root);
	}
	//从根节点开始比较
	private boolean contain(T element ,BinaryNode<T> tree){
		//此时树为空
		if(tree == null)
			return false;

		int compareResult = element.compareTo(tree.element);
		if(compareResult > 0)
			return contain(element,tree.right);
		else if(compareResult < 0)
			return contain(element,tree.left);
		else
			return true;
	}

	public void insert(T element){
		root = insert(element,root);
	}
	private BinaryNode<T> insert(T element ,BinaryNode<T> tree){
		if(tree == null)
			root = new BinaryNode<T>(null,null,element);

		int compareResult = element.compareTo(tree.element);
		if(compareResult > 0){
			tree.right = insert(element,tree.right);
		}
		else if(compareResult < 0)
			tree.left = insert(element,tree.left);
		//相同的情况暂时不考虑，可以添加附加域指示发生的频率来处理
		else;
		return tree;
	}

	public T findMin(){
		if(isEmpty())
			throw new NoSuchElementException();
		return findMin(root);
	}
	private T findMin(BinaryNode<T> tree){
		while(tree.left != null){
			tree = tree.left;
		}
		return tree.element;
	}

	public T findMax(){
		if(isEmpty())
			throw new NoSuchElementException();
		return findMax(root);
	}

	private T findMax(BinaryNode<T> tree) {
		if(tree.right == null)
			return tree.element;
		return findMax(tree.right);
	}

	//这里的返回值的意义：有点没搞懂

	public BinaryNode<T> remove(T element ,BinaryNode<T> tree){
		if(tree == null)
			return null;

		int compareResult = element.compareTo(tree.element);
		if(compareResult < 0){
			tree.left = remove(element,tree.left);
		}
		else if(compareResult > 0 )
			tree.right = remove(element,tree.right);
		//当该节点有两个孩子的时候
		else if(tree.left.element != null && tree.right.element != null){
			//讲该树的左子树的最小节点的值给他，然后再删除改那个左子树的最小节点
			tree.element = findMin(tree.right);
			tree.right = remove(tree.element ,tree.right);
		}
		else{
			//只有一个孩子节点，或是叶子节点的情况
			tree = (tree.left != null)? tree.left: tree.right;
		}
		return tree;
	}

	private static class BinaryNode<T>{
		private BinaryNode left;
		private BinaryNode right;
		private T element ;
		public BinaryNode(T element){
			this(null,null,element);
		}

		public BinaryNode(@Nullable BinaryNode left,@Nullable BinaryNode right,T element){
			this.element = element;
			this.left = left;
			this.right = right;
		}
	}

}
