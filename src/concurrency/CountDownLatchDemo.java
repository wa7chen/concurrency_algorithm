package concurrency;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * demo how to use CountDownLatch
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: PM2:49
 */
class TaskPortion implements Runnable{
	private static int counter = 0;
	private final int id = counter++;
	private Random random = new Random(47);
	private final CountDownLatch latch;

	TaskPortion(CountDownLatch latch) {
		this.latch = latch;
	}

	public void run() {
		try {
			doWorks();
			//COUNTDOWN 直到为0时，那些任务就会被激活。
			latch.countDown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void doWorks() throws InterruptedException{
		TimeUnit.MILLISECONDS.sleep(random.nextInt(2000));
		System.out.println(this + "completed!");
	}

	@Override
	public String toString() {
		return String.format("%1$-3d",id);
	}
}

class WaitingTask implements Runnable{
	private static int counter = 0;
	private final int id = counter++;
	private final CountDownLatch latch;

	WaitingTask(CountDownLatch latch) {
		this.latch = latch;
	}

	public void run() {
		try {
			latch.await();
			System.out.println("Latch barrier passed for " + this);
		} catch (InterruptedException e) {
			System.out.println(this+ " interrupted");
		}
	}

	@Override
	public String toString() {
		return String.format("WaitingTask %1$-3d",id);
	}
}

public class CountDownLatchDemo {
	//设置计数
	static final int SIZE = 100;
	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		CountDownLatch latch = new CountDownLatch(SIZE);

		//这些任务都会被阻塞
		for (int i = 0; i < 10; i++) {
			service.execute(new WaitingTask(latch));
		}
		//在这些任务完成后，上面的任务才能进行。这就是CountDownLatch的一般用法。
		for (int i = 0; i < SIZE; i++) {
			service.execute(new TaskPortion(latch));
		}
		System.out.println("launched all tasks");
		service.shutdown();
	}
}
