package concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * User: ${wa7chen}
 * Time: PM4:37
 */
class DaemonThreadFactory implements ThreadFactory{
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);
		t.setDaemon(true);
		return t;
	}
}


public class ThreadFactoryTest {
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool(new DaemonThreadFactory());
		for(int i = 0 ;i < 5;i++){
			exec.execute(new DaemonWa());
		}

		System.out.println("all daemon started!!");
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		exec.shutdown();
	}
}
