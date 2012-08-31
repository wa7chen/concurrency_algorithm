package concurrency;

import java.util.concurrent.*;

/**
 * @author <a href="mailto:chenxiong_ch.pt@taobao.com">chenxiong</a>
 * @since 12-8-27 AM10:44
 */
public class SyncQueueTester {
	private static ExecutorService executor = new ThreadPoolExecutor(1, 1,
			1000, TimeUnit.SECONDS,
			new SynchronousQueue<Runnable>(),
			new ThreadPoolExecutor.DiscardPolicy());

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 20; i++) {
			kickOffEntry(i);

			Thread.sleep(200);
		}

		executor.shutdown();
	}

	private static void kickOffEntry(final int index) {
		executor.
				submit(
						new Callable<Void>() {
							public Void call() throws InterruptedException {
								System.out.println("start " + index);
								Thread.sleep(1000); // pretend to do work
								System.out.println("stop " + index);
								return null;
							}
						}
				);
	}
}
