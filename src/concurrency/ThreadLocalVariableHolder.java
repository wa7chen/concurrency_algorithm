package concurrency;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * demonstrate the usage of the ThreadLocal variable
 * each thread hold a variable
 * User: ${wa7chen}
 * Time: PM3:50
 */

class Accessor implements Runnable{
	private final int id;

	public Accessor(int id) {
		this.id = id;
	}

	public void run() {
		while (!Thread.currentThread().isInterrupted()){
			ThreadLocalVariableHolder.increment();
			System.out.println(this);
			Thread.yield();
		}
	}

	@Override
	public String toString() {
		return "#" + id + ":" +
				ThreadLocalVariableHolder.get();
	}
}

public class ThreadLocalVariableHolder {
	private static ThreadLocal<Integer> value =
			new ThreadLocal<Integer>(){
				private Random  rand = new Random(47);

				@Override
				protected Integer initialValue() {
					return rand.nextInt(10000);
				}
			};

	public static void increment(){
		value.set(value.get() + 1);
	}

	public static int get(){
		return value.get();
	}

	public static void main(String[] args) throws Exception{
		ExecutorService service = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			service.execute(new Accessor(i));
		}
		TimeUnit.SECONDS.sleep(3);
		service.shutdownNow();
	}
}
