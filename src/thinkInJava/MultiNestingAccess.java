package thinkInJava;

/**
 *多次嵌套类中访问外部类的成员
 * 一个内部类被嵌套多少层平不重要，它能透明的访问所有它所嵌入的外围类得所有成员
 */
class MNA{
	private void f(){}
	class A{
		private void g(){}
		public class B{
			void h(){
				//嵌套三层 还是可以访问最外层类得成员
				g();
				f();
			}
		}
	}
}
interface Contents{
	int value();
	}


public class MultiNestingAccess {
	//构造的匿名内部类继承与Contents.通过new表达式返回的引用会自动向上转型为Contents的引用
	public Contents get(){
		return new Contents() {
			//普通的内部类不能有static数据和static字段，也不能能有嵌套类，
			//static int i = 1l;
			public int value() {
				return 1;
			}
		};
	}

	public static void main(String[] args) {
		MNA mna = new MNA();
		MNA.A mnaa = mna.new A();
		MNA.A.B mnaab = mnaa.new B();
		mnaab.h();
	}
}
