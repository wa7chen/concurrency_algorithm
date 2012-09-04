package stack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;


/**
 *计算后缀表达式
 * 中缀转后缀
 */
/*
这里出现的主要问题就是从char数字中读数字 然后再取出时数字类型的问题
记住一点char类型的数字计算时 是拿他们对应的码值计算，而不是符号值，所以会有问题
我的做法是将char数字先-'0'字符0，这时得到的就是int，然后再存入stack
 */
public class UsesOfStack {
	private static char[] operators = {'*','+','-','/'};

	public static int countPostfix(char[] items){
		MyStack stack = new MyStack();
		for(int i = 0;i < items.length;i++){
			//如果是数字直接推入stack中
			if(Character.isDigit(items[i]) ){
				//这里数字还需要处理 从字符转化为整数
				int digit = items[i] - '0';
				stack.push(digit);
			}else if(Arrays.binarySearch(operators,items[i]) != -1){
				//如果是操作符那么从stack中取两个数进行计算，然后在push
				//这里要注意先后顺序
				int y = (Integer)stack.pop();
				int x = (Integer)stack.pop();
				int value =  0;
				switch(items[i]){
					case'*':value = x * y;break;
					case'+':value = x + y;break;
					case'-':value = x - y;break;
					case'/':value = x / y;break;
				}
				stack.push(value);
				//System.out.println(value);
			}
		}
		//循环截止时，pop出stack里面的值
		int total = (Integer)stack.pop();
		System.out.println(total);
		return total;
	}
	/*
	从流中读取字符串
	 */
	public static String getString() throws IOException{
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(input);
		String string  = br.readLine();
		return string;
	}

	public static String infix2Posfix(String infix){
		MyStack stack = new MyStack();
		String posfix = "";
		for(int i = 0 ;i < infix.length();i++){
			char item = infix.charAt(i);
			//如果是数字直接输出
			if(Character.isDigit(item)){
				posfix += item;
			}
			else {
				if(stack.size() == 0)
					stack.push(item);
				else {
					while(stack.size() >= 1){
						char last = (Character)stack.peek();
						//首先判断是否是')',那么将pop‘（’之前所有的
						if(item == ')'){
							while(true){
								char x = (Character)stack.pop();
								if(x != '(') {
									posfix += x;
								}else break;
							}  //循环的控制很重要，如果这里没有break，那么）也会输入到栈中
							break;
						}
						//如果stack首元素大于即将进stack的元素，那么将栈首元素pop，新元素push
						while(stack.size() >= 1 && priority(last,item)){
							posfix += stack.pop();
							if(stack.size() > 0)
								last = (Character)stack.peek();
							else break;
						}
						stack.push(item);
						break;
					}
				}
			}
		}
		//循环结束后，pop所有的元素
		while(stack.size() != 0){
			System.out.println("popall");
			posfix += stack.pop();
		}
		return posfix;
	}

	//判断读取的下个符号的优先级
	//若读取的符号的优先级不高于栈顶的符号的优先级则将栈顶输出
	//特例：如果栈顶是（那么所有的符号均push
	//flag表示是否能出栈
	public static boolean priority(char a,char b){
		boolean flag = false;
		switch (b){
			case '+':;
			case '-':flag = true;break;
			case '*':;
			case '/':if(a == '*' || a == '/') flag = true;break;
		}//'('默认为最高优先级
		if(a == '(') flag = false;
		return flag;
	}

	public static void main(String[] args) throws IOException{
//将字符串转化为字符数组
		String string = "9-8*(4+9*7)-2";
		String posfix =  infix2Posfix(string)  ;
		System.out.println(posfix);

		char[] items = posfix.toCharArray();

		int value = countPostfix(items);
		System.out.println(value);

	}

}
