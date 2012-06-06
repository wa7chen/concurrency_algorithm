package thinkInJava;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-5-2
 * Time: 下午8:53
 */

//class A{
//     private int a;
//	A(){
//
//	}
//
//}
//
//public class MyTest{
//    public static void main(String[] args){
//        A x = new A();
//        System.out.println(x.a);
//   }
//}
public class MyTest  {
  public static void main(String args[])  {
    MyTest nc = new MyTest();
    nc.t = nc.t++;
  }
	public void tosh(){
		MyTest mc = new MyTest();
		mc.t = mc.t++;
	}

  private int t;
  MyTest()  {
  }
}