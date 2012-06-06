package concurrency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: PM1:32
 */
class LiftOffRunner implements Runnable {
	private BlockingQueue<LiftOff> rockets;

	LiftOffRunner(BlockingQueue<LiftOff> rockets) {
		this.rockets = rockets;
	}

	public void add(LiftOff lo) {
		try {
			rockets.put(lo);
		} catch (InterruptedException e) {
			System.out.println("Interrupted during put()");
		}
	}

	public void run() {
		try {
			while (!Thread.interrupted()) {
				//Retrieves and removes the head of this queue, waiting if necessary until an element becomes available.
				LiftOff rocket = rockets.take();
				rocket.run();
			}
		} catch (InterruptedException e) {
			System.out.println("Waking from take()");
		}

		System.out.println("Exiting LiftOffRunner");

	}
}

class LiftOffAdder implements Runnable {
	private LiftOffRunner runner;

	LiftOffAdder(LiftOffRunner runner) {
		this.runner = runner;
	}

	public void run() {
		for (int i = 0; i < 5; i++) {
			runner.add(new LiftOff(5));
		}
	}
}

public class TestBlockingQueues {

	static void getKey() {
		try {
			new BufferedReader(
					new InputStreamReader(System.in)
			).readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	static void getKey(String message) {
		System.out.println(message);
		getKey();
	}

	static void test(String msg, BlockingQueue<LiftOff> queue) {
		System.out.println(msg);
		LiftOffRunner runner = new LiftOffRunner(queue);
		LiftOffAdder adder = new LiftOffAdder(runner);
		ExecutorService service = Executors.newCachedThreadPool();
		service.execute(runner);
		service.execute(adder);

		//main thread will block at IO operation,but
		//other thread will be not affected
		getKey("Press 'Enter' (" + msg + ")");
		service.shutdownNow();
		System.out.println("Finished " + msg + " test");
	}

	public static void main(String[] args) {
		test("LinkedBlockingQueue", new LinkedBlockingQueue<LiftOff>());
		test("ArrayBlockingQueue", new ArrayBlockingQueue<LiftOff>(3));
		test("SynchronizedBlockingQueue", new SynchronousQueue<LiftOff>());
	}
}
