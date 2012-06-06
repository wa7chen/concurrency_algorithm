package stack;

	public class ExpressionCal {

	    // 构建一个栈
	    class Stack {

	        private int top;

	        private char[] stackArray;

        // 栈构造函数
	        public Stack(int s) {
	            stackArray = new char[s];
	            top = -1;
	        }

	        // 入栈
	        public void push(char i) {
	            stackArray[++top] = i;
	        }

	        // 出栈
	        public char pop() {
	            return stackArray[top--];
	        }

        public int size() {
	            return top + 1;
	        }

	        public boolean isEmpty() {
	            return (top == -1);
	        }

	        public void displayStack(String s) {
	            System.out.print(s);
	            for (int i = 0; i < size(); i++) {
	                System.out.print(stackArray[i]);
	                System.out.print(' ');
	            }
	            System.out.println(" ");
	        }

	    }

	    // 将中缀表达式转换为后缀表达式类
	    class PostExpression {
	        private Stack postExpStack;

	        private String input;

	        private String output = "";

	        public PostExpression(String expression) {
	            input = expression;
	            postExpStack = new Stack(expression.length());
	        }

	        // 将中缀表达式变成后缀表达式
	        public String transPostExp() {
	            for (int i = 0; i < input.length(); i++) {
	                postExpStack.displayStack("no" + i + " stack is: ");
	                char ch = input.charAt(i);
	                switch (ch) {
	                case '+':
	                case '-':
	                    doOper1(ch, 1);
	                    break;
	                case '*':
	                case '/':
	                    doOper1(ch, 2);
	                    break;
	                case '(':
	                    postExpStack.push(ch);
	                    break;
	                case ')':
	                    doOper2();
	                    break;
	                default:
	                    output += ch;
	                    break;
	                }
	                System.out.println("no" + i + " output: " + output);
	            }
	            while (!postExpStack.isEmpty()) {
	                output += postExpStack.pop();
	            }
	            postExpStack.displayStack("final stack is: ");
	            return output;
	        }

	        // 根据读取的字符是+-*/的时候做处理
	        private void doOper1(char ch, int num) {
	            while (!postExpStack.isEmpty()) {
	                char chPop = postExpStack.pop();
	                if (chPop == '(') {
	                    postExpStack.push(chPop);
	                    break;
	                } else {
	                    int opernum;
	                    if (chPop == '+' || (chPop == '-')) {
	                        opernum = 1;
	                    } else
	                        opernum = 2;

	                    if (num > opernum) {
	                        postExpStack.push(chPop); // 读取的操作符为*/，而栈顶内容为+-，则将*/入栈
	                        break;
	                    } else {
	                        output += chPop;
	                    }
	                }
	            }
	            postExpStack.push(ch);
	        }

	        // 读取的字符为)时的处理方式
	        private void doOper2() {
	            while (!postExpStack.isEmpty()) {
	                char chPop = postExpStack.pop();
	                if (chPop == '(') {
	                    break;
	                } else {
	                    output += chPop;
	                }
	            }
	        }
	    }

	    public static void main(String[] args) {
	        PostExpression pe = new ExpressionCal().new PostExpression(
	                "(1+(4-6/2))*3");
	        System.out.println("after trans: " + pe.transPostExp());
	    }

	}

