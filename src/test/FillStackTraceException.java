package test;

/**
 * User: ${wa7chen}
 * Time: PM11:39
 */

public class FillStackTraceException {
	static void f() throws Exception{
		System.out.println("original in f()");
		throw new Exception();
	}

	static void g() throws Exception{
		try {
			f();
		} catch (Exception e) {
			System.out.println("inside in g(),e.printstackTrace");
			e.printStackTrace();
			throw e;
		}
	}

	static void h() throws Exception{
		try {
			f();
		} catch (Exception e) {
			System.out.println("inside in h()");
			e.printStackTrace();
			//fillInStackTrace() return a Throwale object
			//fillInstackTrace() change the orgin trace of the exception
			throw (Exception)e.fillInStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			g();
		} catch (Exception e) {
			System.out.println("main:printStackTrace1()");
			e.printStackTrace();
		}
		try {
			h();
		} catch (Exception e) {
			System.out.println("main:printStackTrace2()");
			e.printStackTrace();
		}

	}
}
