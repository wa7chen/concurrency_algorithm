package concurrency;

import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: AM10:10
 */
	class NeedsCleanup {
		private final int id;

		public NeedsCleanup(int id) {
			this.id = id;
			System.out.println("Needing clean up :" + id);
		}

		public void cleanup(){
			System.out.println("cleaning up :" + id);
		}

	}

class Block3 implements Runnable{
	private volatile double b = 0.0;
	public void run() {
		try{
			while(!Thread.interrupted()){
				//point 1
				NeedsCleanup n1 = new NeedsCleanup(1);
				try{
					System.out.println("sleeping");
					TimeUnit.SECONDS.sleep(1);
					//point 2
					NeedsCleanup n2 = new NeedsCleanup(2);

					try{
						System.out.println("calculating");
						for (int i = 0; i < 2500000; i++)
							 b = b + (Math.PI + Math.E) / b;
						System.out.println("finished time-consuming operation");

					} finally {
						n2.cleanup();
					}
				} finally {
					n1.cleanup();
				}

			}
		} catch (InterruptedException e){
			System.out.println("Exiting via InterruptedException.");
		}
	}
}

public class InterruptingIdiom {
	public static void main(String[] args) throws Exception{
		Thread thread = new Thread(new Block3());
		thread.start();
		TimeUnit.MILLISECONDS.sleep(4100);
		thread.interrupt();
	}
}

