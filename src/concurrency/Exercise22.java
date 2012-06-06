package concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: PM3:33
 */
public class Exercise22 {
	private volatile boolean flag = false;

	public synchronized void dosome() throws Exception{
		TimeUnit.MILLISECONDS.sleep(300);
		flag = true;
	}

	public synchronized void busyWait() throws InterruptedException{
		long start = System.nanoTime();
		while(!flag){
			wait();
		}
		long end = System.nanoTime();
		flag = false;
		System.out.println("flag changed!");
		System.out.println("waste time is " + (end - start));
	}

	public static void main(String[] args) throws Exception{
		Exercise22 exercise22 = new Exercise22();
		ExecutorService service = Executors.newCachedThreadPool();
		service.execute(new Task3(exercise22));
		service.execute(new Task4(exercise22));
		TimeUnit.SECONDS.sleep(3);
		service.shutdownNow();
	}
}

class Task3 implements Runnable{
	private Exercise22 obj;

	Task3(Exercise22 obj) {
		this.obj = obj;
	}

	public void run() {
		try {
			obj.dosome();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class Task4 implements Runnable{
	private Exercise22 obj;

	Task4(Exercise22 obj) {
		this.obj = obj;
	}

	public void run() {
		try {
			obj.busyWait();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}