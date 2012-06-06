package concurrency;

import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: AM10:43
 */

/*
Thread.interrupt() only interrupts this thread,
unless the current thread is interrupting

so,the statement that cant cause interruption,
cant be interrupted!!
 */
class Block4 implements Runnable{
	public void run() {
		int i = 0;
		//return a boolean if the current thread has been interrupted
		while(!Thread.interrupted()){

			System.out.println(i);
			i++;
		}
		
	}
}

public class Interrupting3 {
	public static void main(String[] args) throws Exception{
		Thread t = new Thread(new Block4());
		t.start();
		TimeUnit.SECONDS.sleep(3);
		t.interrupt();//cant work
	}
}
