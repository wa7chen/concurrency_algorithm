package concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * User: ${wa7chen}
 * Time: PM3:32
 */
/* 
Thread cant handle the exception throw out of run(),
so we user executor and a handleUncaughtThreadFactory
to solve
 */
class ExceptionThread2 implements Runnable{
	private static MyUncaughtExceptionHandler uncaughtExceptionHandler = new MyUncaughtExceptionHandler();

	public void run() {
		Thread t = Thread.currentThread();
		//another way to handle the thread uncaught exception
		//is to use a static param,i.e here is uncaughtExceptionHandler
		// t.setUncaughtExceptionHandler(uncaughtExceptionHandler);
		System.out.println("run by :" + t);
		System.out.println("eh" + t.getUncaughtExceptionHandler());
		throw new RuntimeException();
	}
}

class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{
	public void uncaughtException(Thread t, Throwable e) {
		System.out.println("caught :" + e);

	}
}

class HandlerThreadFactory implements ThreadFactory{
	public Thread newThread(Runnable r) {
		System.out.println(this + " creating new Thread");
		Thread t = new Thread(r);
		System.out.println("created it" + t);
		//set the handler invoked for thread when it teminates due to an
		//uncaught exception
		t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
		System.out.println("eh :" + t.getUncaughtExceptionHandler());
		return t;
	}
}

public class CaptureUncaughtException {
	public static void main(String[] args) {
		ExecutorService exe = Executors.newCachedThreadPool(new HandlerThreadFactory());
		exe.execute(new ExceptionThread2());

	}


}
