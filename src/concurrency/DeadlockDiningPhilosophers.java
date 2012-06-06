package concurrency;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: PM2:28
 */
class Chopstick {
	private boolean taken = false;

	public synchronized void take() throws InterruptedException{
		while (taken){
			wait();
		}
		taken = true;
	}

	public synchronized void drop() throws InterruptedException{
		taken = false;
		notifyAll();
	}
}


class Philosophers implements Runnable{
	private Chopstick left;
	private Chopstick right;
	private final int id ;
	private final int ponderFactor;
	private Random random = new Random(47);

	Philosophers(Chopstick left,Chopstick right,int id, int ponderFactor) {
		this.left = left;
		this.right = right;
		this.id = id;
		this.ponderFactor = ponderFactor;
	}

	private void pause() throws InterruptedException{
		if(ponderFactor == 0) return;
		TimeUnit.MILLISECONDS.sleep(random.nextInt(ponderFactor * 250));
	}

	public void run() {
		try{
			while (!Thread.interrupted()){
				System.out.println(this + " " + "thinking");
				pause();
				System.out.println(this + " " + "grabbing right");
				right.take();
				System.out.println(this + " " + "grabbing left");
				left.take();
				System.out.println(this + " " + "eating");
				pause();
				right.drop();
				left.drop();
			}
		} catch (InterruptedException e){
			System.out.println(this + " exiting via interrupt");
		}
	}

	@Override
	public String toString() {
		return "Philosopher " + id;
	}
}

public class DeadlockDiningPhilosophers {
	public static void main(String[] args) throws Exception{
		int ponder = 5;
		if(args.length > 0){
			ponder = Integer.parseInt(args[0]);
		}
		int size = 5;
		if(args.length > 1 ){
			size = Integer.parseInt(args[1]);
		}

		ExecutorService service = Executors.newCachedThreadPool();
		Chopstick[] chopsticks = new Chopstick[5];
		
		for(int i = 0;i < size;i++){
			chopsticks[i] = new Chopstick();
		}

		for(int i = 0;i < size;i++){
			service.execute(new Philosophers(
					chopsticks[i],chopsticks[(i + 1) % size],i,ponder
			));
		}

		System.out.println("press Enter to quit");
		System.in.read();

		service.shutdownNow();
	}
}
