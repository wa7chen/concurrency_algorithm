package thinkInJava.thread;

import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-4-24
 * Time: 下午1:25
 */
public class DaemonThread implements Runnable {

	public void run() {
		while(true){
			try {
				TimeUnit.MILLISECONDS.sleep(100);
				System.out.println(Thread.currentThread() + " " + this);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception{
		for(int i = 0;i < 5;i++){
			Thread daemon = new Thread(new DaemonThread());
			daemon.setDaemon(true);
			daemon.start();
		}
		System.out.println("All daemon started");
		TimeUnit.MILLISECONDS.sleep(4000);
	}

}
