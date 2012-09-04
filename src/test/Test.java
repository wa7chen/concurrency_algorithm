package test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-3-25
 * Time: 下午12:15
 */
public class Test {
	//final int i;final修饰的成员变量必须初始化

	//int 最大能表示的数2147483647
	/*
1
2
4
8
16
32
64
128
256
512
1024
2048
4096
8192
16384
32768
65536
131072
262144
524288
1048576
2097152
4194304
8388608
16777216
33554432
67108864
134217728
268435456
536870912
1073741824
-2147483648
0
	 */


	/*static class Count extends HashMap<Class<? extends Chess>,Integer>{
		public Count(){
			super();
		}
		*//*
		遍历map的写法
		 *//*
		public void getTotal(Chess chess){
			for(Map.Entry<Class<?extends Chess>,Integer> pair:entrySet()){
			}
		}
	}*/
	private void test1() {

		int i = 1;
		int j = 1;
		while (true) {
			j = i;
			i *= 2;
			System.out.println(j);
			if (j == i) break;
		}
		//测试字符里面的数字表示
		char[] chars = new char[10];
		chars[0] = '1';
		chars[1] = 2;
		System.out.println(chars[0]);
	}

	private void test2() {
	}

	public static void main(String[] args) {

	}


}
