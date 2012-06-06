package concurrency;

/**
 * User: ${wa7chen}
 * Time: PM4:03
 */
class DaemonWa implements Runnable{
	private int num;
	private static int no = 1;
	private static int sleepTimes = 1;
	public void run() {
		num = no;
		System.out.println("Starting daemon" + no++);
		try {
			Thread.sleep(1000 * sleepTimes++);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// daemon thread wont get in finally where run() completed;
			System.out.println("daemon run in finally...." + num);
		}

	}
}

public class DaemonTest {
	public static void main(String[] args) {
		Thread daemon = new Thread(new DaemonWa());
		daemon.setDaemon(true);
		daemon.start();
	}
}
