package thinkInJava.io;



import java.io.*;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-5-13
 * Time: 下午1:46
 */
/*
测试对象序列化
实现写入和恢复
 */

class Data implements Serializable{
	private int n;
	public Data(int n ){this.n = n;}
	public String toString(){return Integer.toString(n);}
}
public class Worm implements Serializable{
	private static Random random = new Random(47);
	private Data[] d = {
			new Data(random.nextInt(10)),
			new Data(random.nextInt(10)),
			new Data(random.nextInt(10))
	};
	private Worm next;
	private char c ;

	/*
	递归的构造
	 */
	public Worm(int i ,char x){
		System.out.println("worm constructor :" + i);
		c = x;
		if(--i > 0){
			next = new Worm(i,(char)(x + 1));
		}
	}
	public Worm(){
		System.out.println("Default contructor");
	}

	public String toString(){
		StringBuilder result = new StringBuilder(":");
		result.append(c);
		result.append("(");
		for(Data data :d ){
			result.append(data);
		}
		result.append(")");
		if(next != null)
			result.append(next);
		return result.toString();
	}

	public static void main(String[] args) throws ClassNotFoundException,IOException {
		Worm w = new Worm(6,'a');
		System.out.println("w=" +w);
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("worm.out"));
		out.writeObject("WORM STORAGE \n");
		out.writeObject(w);
		out.close();

		ObjectInputStream in = new ObjectInputStream(new FileInputStream("worm.out"));
		//注意这里读到的对象都是object的引用，必须向下转型
		String s = (String)in.readObject();
		Worm w2 = (Worm)in.readObject();
		System.out.println(s + "w2" + w2);

		//这里是也写到字节数组缓冲区
		ByteArrayOutputStream bout  = new ByteArrayOutputStream();
		ObjectOutputStream out2 = new ObjectOutputStream(bout);
		out2.writeObject("worm storage \n");
		out2.writeObject(w);
		out.flush();

		ObjectInputStream in2 = new ObjectInputStream(new ByteArrayInputStream(bout.toByteArray()));
		s = (String)in2.readObject();
		Worm w3 = (Worm)in2.readObject();
		System.out.println(s + "w3=" + w3);
	}
}

