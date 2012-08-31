package concurrency;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 用CountDownLatch来改进公园人口计数问题。
 * * User: ${wa7chen}
 * Time: PM3:10
 */
class Count{
	private int count = 0;
	private Random random = new Random(47);

	public synchronized int increment(){
		//temp and yield cause the possible of failure.
		int temp = count;
		if(random.nextBoolean()){
			Thread.yield();
		}
		return (count = ++temp );
	}

	public synchronized int value(){
		return count;
	}
}

class SumEntracne implements Runnable{
	private final CountDownLatch latch;

	SumEntracne(CountDownLatch latch) {
		this.latch = latch;
	}

	public void run() {
		try {
			//当COUNTDOWN为0的时候，马上执行。应该时优先级最高。
			latch.await();
			closeThenSum();
		} catch (InterruptedException e) {
			System.out.println("SumEntrance interrupted");
		}
	}

	public void closeThenSum(){
		Entrance.cancel();
		System.out.println("Total: " + Entrance.getTotalCount());
		System.out.println("Sum of the Entrances:" + Entrance.sumEntrances());
	}

}

class Entrance implements Runnable{
	private static Count count = new Count();
	private static List<Entrance> entrances = new ArrayList<Entrance>();
	private int number = 0;
	private final int id ;
	private static volatile boolean canceled = false;
	private final CountDownLatch latch ;
	public static void cancel(){
		canceled = true;
	}
	public Entrance(int id,CountDownLatch latch) {
		this.latch = latch;
		this.id = id;
		entrances.add(this);
	}

	public void run() {
		while (!canceled){
			synchronized (this){
				++number;
			}
			System.out.println(this + "Total:" + count.increment());
			try {
				TimeUnit.MILLISECONDS.sleep(100);
				latch.countDown();
			} catch (InterruptedException e) {
				System.out.println("sleep interrupted");
			}
		}
	}

	public synchronized int getValue(){
		return number;
	}

	@Override
	public String toString() {
		return "Entrance:" + id + ":" + getValue();
	}

	public static int getTotalCount(){
		return count.value();
	}

	public static int sumEntrances(){
		int sum = 0;
		for (Entrance entrance : entrances) {
			sum += entrance.getValue();
		}
		return sum;
	}
}

public class OrnamentalGarden {
	public static void main(String[] args) throws Exception{
		final int SIZE = 150;
		CountDownLatch latch = new CountDownLatch(SIZE);
		ExecutorService service = Executors.newCachedThreadPool();
		for(int i = 0;i < 5;i++){
			service.execute(new Entrance(i,latch));
		}
		service.execute(new SumEntracne(latch));

//		TimeUnit.SECONDS.sleep(2);
//		Entrance.cancel();
		service.shutdown();
//		service.shutdownNow(); use shutdownNow() would send a interrupt()
//      to the thread that started by this service
//		if(!service.awaitTermination(250,TimeUnit.MILLISECONDS))
//			System.out.println("some task not terminated");

	}

}
