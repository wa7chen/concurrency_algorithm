package concurrency;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * demonstrate the interrupt of the block thread
 * three situation of the block ,one can be interrupted,sleep
 * the other two cant ,io,get lock cant use at this time.
 * User: ${wa7chen}
 * Time: PM1:35
 */

class SleepBlocked implements Runnable{
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(100);
		} catch (InterruptedException e) {
			System.out.println("interrupted exception!");
		}

		System.out.println("Exiting SleepBlock.run()");
	}
}

class IOBlocked implements Runnable{
	private InputStream in;

	public IOBlocked(InputStream in) {
		this.in = in;
	}

	public void run() {
		try {
			System.out.println("waiting for read()");
			in.read();
		} catch (IOException e) {
			if(Thread.currentThread().isInterrupted()){
				System.out.println("interrupted from block IO");
			}else {
				throw new RuntimeException(e);
			}
		}
		System.out.println("Exiting IOBlock.run()");
	}
}

class SynchronizedBlocked implements Runnable{
	public synchronized void f(){
		System.out.println(Thread.currentThread());
		while (true){
			Thread.yield();//the lock is never releases
		}
	}

	public SynchronizedBlocked() {
		new Thread(){
			public void run(){
				f();
			}
		}.start();
	}

	public void run() {
		System.out.println("try to call f()");
		f();//
		System.out.println("Exiting SynchronizedBlocked.run()");//this statement never run
	}
}

public class Interrupting {
	private static ExecutorService service = Executors.newCachedThreadPool();

	static void test(Runnable r ) throws InterruptedException{
		Future<?> f = service.submit(r);//you can never use Future to get some object
		TimeUnit.MILLISECONDS.sleep(100);
		System.out.println("Interrupting " + r.getClass().getName());
		f.cancel(true);//interrupt the thread
		System.out.println("Interrupt sent to " + r.getClass().getName());
	}

	public static void main(String[] args) throws Exception{
		test(new SleepBlocked());
		test(new IOBlocked(System.in));
		test(new SynchronizedBlocked());
		TimeUnit.SECONDS.sleep(3);
		System.out.println("Aborting with System.exit(0)");
		System.exit(0);
	}
}
