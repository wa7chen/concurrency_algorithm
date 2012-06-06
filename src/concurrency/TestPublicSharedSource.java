package concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: ${wa7chen}
 * Time: PM2:48
 */
abstract class IntGenerator{
	private volatile boolean canceled = false;
	public abstract int next();
	public void cancel(){
		canceled = true;
	}
	public boolean isCanceled(){
		return canceled;
	}
}

class EvenGenerator extends IntGenerator {
	private int currentEvenValue = 0;
	@Override
	public synchronized int next() {
		++currentEvenValue;
		//user yield() there to cause
		//the error occur faster
		Thread.yield();
		++currentEvenValue;
		return currentEvenValue;
	}
}

class  MutexEvenGenerator extends IntGenerator {
	private int currentEvenValue = 0;
	private Lock lock = new ReentrantLock();

	@Override
	public int next() {
		try {
			lock.lock();
			++currentEvenValue;
			Thread.yield();
			++currentEvenValue;
			return currentEvenValue;
		} finally {
			lock.unlock();
		}
	}
}

class EvenChecker implements Runnable{
	private IntGenerator generator;
	private final int id;

	public EvenChecker(IntGenerator generator,int id){
		this.generator = generator;
		this.id = id;
	}

	public void run() {
		while (!generator.isCanceled()){
			int val = generator.next();
			while (val % 2 != 0 ){
				System.out.println(val + "is not even");
				generator.cancel();
				Thread.yield();
			}
		}
	}

	public static void test(IntGenerator generator,int id){
		System.out.println("begin!!!");
		ExecutorService service = Executors.newCachedThreadPool();
		for (int i = 0; i < id; i++) {
			service.execute(new EvenChecker(generator,i));
		}
		service.shutdown();
	}
}

public class TestPublicSharedSource {
	public static void main(String[] args) {
		IntGenerator generator = new MutexEvenGenerator();
		EvenChecker.test(generator, 1000);
	}
}
