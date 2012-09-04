package test;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ${wa7chen}
 * Time: PM3:38
 */
public abstract class TestAbstract {
	public static void foo(Object a){

		if(a instanceof Long){
			System.out.println("true");
		}
	}

	public static void main(String[] args) {
		long a = 1111l;
		Long b = 1111l;
		if( a == b ) {
			System.out.println("equals");
		}
		else {
			System.out.println("not equals");
		}
		foo(a);
	}
}
