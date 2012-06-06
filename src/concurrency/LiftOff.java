package concurrency;

/**
 * User: ${wa7chen}
 * Time: PM2:41
 */
public class LiftOff implements Runnable{
	protected int countDown = 10;
	private static int taskCount = 0;
	private final int id = taskCount++;
	public LiftOff(){
	
	}
	public String status(){
		return "#" + id + "(" +
				(countDown > 0 ?countDown:"Liftoff!") + "),";
				
	}

	public LiftOff(int countDown){
		this.countDown = countDown;
	}
	public void run() {
		while (countDown-- > 0){
			System.out.println(status());
			Thread.yield();
		}
	}
}
