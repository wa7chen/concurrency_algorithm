package concurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * user the ReentrantLock to deal the block that
 * cant be interrupted
 *
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: AM12:08
 */

class BlockedMutex {
	private Lock lock = new ReentrantLock();

	public BlockedMutex(){
		lock.lock();//lock the lock firstly
	}

	public void f(){
		try {
			//mission block on reentrantlock can be interrupted
			lock.lockInterruptibly();
			System.out.println("lock acquired in f()");
		} catch (InterruptedException e) {
			System.out.println("interrupted from lock acquisition in f()");
		}
	}
}

class Blokced2 implements Runnable{
	private BlockedMutex lock = new BlockedMutex();
	public void run() {
		System.out.println("waiting for f() in BlockedMutex");
		lock.f();
		System.out.println("Broken out of blocked call");
	}
}

public class Interrupting2 {

	public static void main(String[] args) throws Exception{
		Thread t = new Thread(new Blokced2());
		t.start();
		TimeUnit.SECONDS.sleep(1);
		System.out.println("Issuing t.interrupt()");
		t.interrupt();
	}
}
