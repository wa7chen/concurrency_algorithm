package concurrency;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: PM11:26
 */
abstract class Accumulator{
	public static long cycles = 50000l;
	private static final int N = 4;
	public static ExecutorService service = Executors.newFixedThreadPool(2 * N);
	private static CyclicBarrier barrier = new CyclicBarrier(N * 2 + 1);
	protected volatile int index = 0;
	protected volatile long value = 0;
	protected long duration = 0;
	protected String id = "error";
	protected final static int SIZE = 100000;
	protected static int[] preloaded = new int[SIZE];

	static {
		Random rand = new Random(47);
		for (int i = 0; i < SIZE; i++) {
			preloaded[i] = rand.nextInt();
		}
	}

	public abstract void accumulate();
	public abstract long read();


	private class Modifier implements Runnable{
		public void run() {
			for (int i = 0; i < cycles; i++) {
				accumulate();
			}
			try{
				barrier.await();
			} catch (Exception e){
				throw new RuntimeException(e);
			}
		}
	}

	private class Reader implements Runnable{
		private volatile long value;

		public void run() {
			for(long i = 0;i < cycles;i++)
				value = read();
			try{
				barrier.await();
			} catch (Exception e){
				throw new RuntimeException(e);
			}
		}
	}

	public void timedTest(){
		long start = System.nanoTime();
		for(int i = 0;i < N; i++){
			service.execute(new Modifier());
			service.execute(new Reader());
		}

		try{
			barrier.await();
		} catch (Exception e ){
			throw new RuntimeException(e);
		}

		duration = System.nanoTime() - start;
		System.out.printf("%-13s: %13d\n", id, duration);
	}

	public static void report(Accumulator acc1,Accumulator acc2){
		System.out.printf("%-22s: %.2f\n",acc1.id + "/" + acc2.id,
				(double)acc1.duration/(double)acc2.duration);
	}
}

class BaseLine extends Accumulator {
	{ id = "BaseLine"; }

	@Override
	public void accumulate() {
		value += preloaded[index++];
		if(index >= SIZE) index = 0;
	}

	@Override
	public long read() {
		return value;
	}
}


class SynchronizedTest extends Accumulator {
	{id = "synchronized";}

	@Override
	public synchronized long read() {
		return value;
	}

	@Override
	public synchronized void accumulate() {
		value += preloaded[index++];
		if(index >= SIZE) index = 0;
	}
}

class LockTest extends Accumulator {
	{id = "lock";}
	private Lock lock = new ReentrantLock();
	@Override
	public void accumulate() {
		lock.lock();
		try{
			value += preloaded[index++];
			if(index >= SIZE) index = 0;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public long read() {
		lock.lock();
		try{
			return value;
		} finally {
			lock.unlock();
		}
	}
}

class AtomicTest extends Accumulator {
	{id = "Atomic";}
	private AtomicInteger index = new AtomicInteger(0);
	private AtomicLong value = new AtomicLong(0);
	//同时依靠两个原子类不能达到同步的效果，但是给我们一个性能调优的方向
	@Override
	public void accumulate() {
		int i = index.getAndIncrement();
		value.getAndAdd(preloaded[i]);
		if(++i >= SIZE)
			index.set(0);

	}

	@Override
	public long read() {
		return value.get();
	}
}

public class SynchronizationComparisions {
	static BaseLine baseLine = new BaseLine();
	static SynchronizedTest synchronizedTest = new SynchronizedTest();
	static LockTest lockTest = new LockTest();
	static AtomicTest atomicTest = new AtomicTest();
	static void test(){
		System.out.println("====================");
		System.out.printf("%-12s : %13d\n", "Cycles", Accumulator.cycles);
//		baseLine.timedTest();
		synchronizedTest.timedTest();
		lockTest.timedTest();
//		atomicTest.timedTest();

		Accumulator.report(synchronizedTest, baseLine);
		Accumulator.report(lockTest, baseLine);
		Accumulator.report(atomicTest, baseLine);
		Accumulator.report(synchronizedTest, lockTest);
		Accumulator.report(synchronizedTest, atomicTest);
		Accumulator.report(lockTest, atomicTest);
	}

	public static void main(String[] args) {
		int iterations = 10;
		System.out.println("warmup!");
		baseLine.timedTest();
		//
		for (int i = 0; i < iterations; i++) {
			test();
			Accumulator.cycles *= 2;
		}
		Accumulator.service.shutdown();
	}

}
