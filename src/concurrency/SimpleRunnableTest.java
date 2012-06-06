package concurrency;

/**
 * User: ${wa7chen}
 * Time: PM4:48
 */
class SimpleRunnable implements Runnable{
	private int num;

	public SimpleRunnable(int num){
		super();
		this.num = num;
	}
	
	public void run() {

		for (int i = 0; i < 3; i++) {
			System.out.println("run" + num );
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Thread.yield();
		}
	}
}

public class SimpleRunnableTest{
	public static void main(String[] args) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(int i = 0;i < 5;i++){
			Thread thread = new Thread(new SimpleRunnable(i));
			thread.start();
		}
		System.out.println("started");
	}
}
