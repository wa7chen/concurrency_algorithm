package concurrency;


/**
 * User: ${wa7chen}
 * Time: PM5:37
 */
public class TestConcurrency implements Runnable{
	public void run() {
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestConcurrency tc = new TestConcurrency();
		tc.run();
		
//		int sshift = 1;
//		System.out.println(sshift <<= 2);
	}
}
