package thinkInJava;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-4-3
 * Time: 下午8:52
 */
public class Field {
	public int i = 0;
	public void go(){
		i++;
	}

	public static void main(String[] args) {
		Field field = new Field();
		field.go();
		System.out.println(field.i);
		String str = "陈雄a";
		System.out.println(str.charAt(0));
		System.out.println(str.length());
		System.out.println("陈".getBytes().length);
		/*
		一个汉字三字节？字符串的长度是字符的长度，不计较是汉字还是英文符号
		输出结果
1
陈
3
3

		 */
	}
}
