package concurrency;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * demo notifyAll just work on the special threads that
 * share a common lock
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: PM10:10
 */
class Blocker {
	synchronized void waitingCall() {
		try {
			while (!Thread.interrupted()) {
				wait();
				System.out.println(Thread.currentThread() + " ");
			}
		} catch (InterruptedException e) {

		}
	}
	synchronized void prod(){notify();}
	synchronized void prodAll(){notifyAll();}
}

class Task implements Runnable{
	static Blocker blocker = new Blocker();

	public void run() {
		blocker.waitingCall();
	}
}

class Tasko implements Runnable{
	static Blocker blocker = new Blocker();

	public void run() {
		blocker.waitingCall();
	}
}

public class NotifyVsNotifyAll {
	public static void main(String[] args) throws Exception{
		ExecutorService service = Executors.newCachedThreadPool();
		for(int i = 0;i < 5;i++){
			service.execute(new Task());
		}
		service.execute(new Tasko());//Tasko's wait cant notify by Task'notifyAll()
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			boolean prod = true;
			@Override
			public void run() {
				if(prod){
					System.out.println("\nnotify() ");
					Task.blocker.prod();
					prod = false;
				} else {
					System.out.println("\nnotifyAll() ");
					Task.blocker.prodAll();
				}
			}
		},400,400);//dely 400,period 400
		TimeUnit.SECONDS.sleep(5);
		timer.cancel();
		System.out.println("\nTimer canceled");
		TimeUnit.MILLISECONDS.sleep(500);
		System.out.println("Tasko.blocker.prodAll()");
		Tasko.blocker.prodAll();
		TimeUnit.MILLISECONDS.sleep(500);
		System.out.println("\nshut down now!");
		service.shutdownNow();

	}
}
