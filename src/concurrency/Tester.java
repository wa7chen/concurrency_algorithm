package concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: PM3:30
 */


public abstract class Tester<C> {
	static int testReps = 10;
	static int testCycles = 1000;
	static int containerSize = 1000;
	abstract C containerInitializer();
	abstract void startReadersAndWriters();
	C testContainer;
	String testId;
	int nReaders ;
	int nWriters;
	volatile long readResult = 0;
	volatile long readTime = 0;
	volatile long writerTime = 0;
	CountDownLatch endLatch;
	static ExecutorService service =
			Executors.newCachedThreadPool();
	Integer[] writeData;

	Tester(String testId,int nReaders,int nWriters){
		this.testId = testId + " " +
				nReaders + "r " + nWriters + "w";
		this.nReaders = nReaders;
		this.nWriters = nWriters;
//		writeData =
	}
	
}
