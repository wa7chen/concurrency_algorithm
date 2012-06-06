package concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: PM2:15
 */
public class Exercise21 {
	public static void main(String[] args) throws Exception {
		Task1 task1 = new Task1();
		ExecutorService service = Executors.newCachedThreadPool();
		//task1 and task2 operate the same instance,so
		service.execute(task1);
		service.execute(new Task2(task1));
		TimeUnit.SECONDS.sleep(5);
		service.shutdownNow();
	}
}


class Task1 implements Runnable {
	private volatile boolean lock = false;

	public synchronized void towait() throws InterruptedException {
		lock = true;
		wait();
	}

	public synchronized void toprint() throws InterruptedException {
		while (lock)
			wait();
		System.out.println("I'm back");
	}

	public void run() {
		try {
			System.out.println("task is leaving");
			towait();
			lock = false;
			toprint();
		} catch (InterruptedException e) {
			System.out.println("exiting via interrupt");
		}
		System.out.println("ending !");
	}

}

class Task2 implements Runnable {
	private Task1 pre;

	public Task2(Task1 pre) {
		this.pre = pre;
	}

	public void run() {
		try {
			while (!Thread.interrupted()) {

				TimeUnit.SECONDS.sleep(1);
				//notify ,wait must doing at
				//the same lock
				synchronized (pre) {
					pre.notifyAll();
				}
			}
		} catch (InterruptedException e) {
			System.out.println("exiting 2 via interrupt");
		}

		System.out.println("ending 2!!!");
	}
}

