package concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: AM12:26
 */
class Fat{
	private volatile double d;
	private static int counter = 0;
	private final int id = counter++;

	public Fat(){
		for(int i = 0;i < 10000;i++){
			d += (Math.PI + Math.E) / (double)i;
		}
	}

	public void operation(){
		System.out.println(this);
	}

	@Override
	public String toString() {
		return "Fat id:" + id + " ";
	}
}

class CheckoutTask<T> implements Runnable{
	private static int counter = 0;
	private final int id = counter++;
	protected Pool<T> pool;

	public CheckoutTask(Pool<T> pool) {
		this.pool = pool;
	}

	public void run() {
		try{
			T item = pool.checkOut();
			System.out.println(this + " checkout item " + item);
			TimeUnit.SECONDS.sleep(1);
			pool.checkIn(item);
			System.out.println(this + " checkin item " + item);
		} catch (InterruptedException e){
			//
		}
	}

	@Override
	public String toString() {
		return "CheckoutTask :" + id ;
	}
}

public class SemaphoreDemo {
	static final int SIZE = 25;

	public static void main(String[] args) throws Exception{
		ExecutorService service = Executors.newCachedThreadPool();
		//decline the pool is final
		final Pool<Fat> pool = new Pool<Fat>(Fat.class,SIZE);
		for(int i = 0 ; i < SIZE;i++){
			service.execute(new CheckoutTask(pool));
		}
		System.out.println("ALL CHECKOUT TASK CREATED!");
		List<Fat> list = new ArrayList<Fat>();
		for (int i = 0; i < SIZE; i++) {
			//could wait here
			Fat fat = pool.checkOut();
			System.out.println(i + ": main() thread checked out !");
			fat.operation();
			list.add(fat);
		}

		Future<?> blocked = service.submit(new Runnable() {
			public void run() {
				try{
					//the call is blocked,bkz all permits are used
					pool.checkOut();
				} catch (InterruptedException e){
					System.out.println("checkout interrupted!");
				}

			}
		});

		TimeUnit.SECONDS.sleep(2);
		blocked.cancel(true);
		System.out.println("Check in object in " + list);
		for (Fat fat : list) {
			pool.checkIn(fat);
		}
		//second is ignored
		//ÈßÓàµÄcheckin»á±»ºöÂÔ
		for (Fat fat : list) {
			pool.checkIn(fat);
		}

		service.shutdown();

	}
}
