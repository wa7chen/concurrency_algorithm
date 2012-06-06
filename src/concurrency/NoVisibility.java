package concurrency;


/**
 * ÑÝÊ¾visibility
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: PM9:58
 */
public class NoVisibility {
	private static boolean ready;
	private static int number;

	private static class ReaderThread implements Runnable {
		public void run() {
			while (ready)
				Thread.yield();
			System.out.println(number);
		}
	}

	public static void main(String[] args) throws Exception{
		//could loop forever,bkz the value of ready might
		//never become visible to the reader thread
		new Thread(new ReaderThread()).start();
		ready = true;
		number = 52;
	}
}
