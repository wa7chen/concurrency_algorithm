package concurrency;

import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: AM10:10
 *
 *
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
			int j = 0;
			while(!Thread.interrupted()){
				System.out.println(j++);
				//point 1
				NeedsCleanup n1 = new NeedsCleanup(1);
				try{
					System.out.println("sleeping");
					TimeUnit.SECONDS.sleep(5);
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
			//因为在while退出前已经判断过一次
			//所以这两个都是false，因为检查interrupted会对状态进行清理
			System.out.println(Thread.interrupted());
			System.out.println(Thread.interrupted());
			System.out.println("Exiting via InterruptedException.");
		}
	}
}

public class InterruptingIdiom {
	public static void main(String[] args) throws Exception{
		Thread thread = new Thread(new Block3());
		thread.start();
		TimeUnit.MILLISECONDS.sleep(2000);
		thread.interrupt();
	}
}

