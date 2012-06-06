package concurrency;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: PM1:32
 */
class CarHo {
	private boolean waxOn = false;

	public synchronized void waxed(){
		waxOn = true;
		notifyAll();
	}

	public synchronized void buffed(){
		waxOn = false;
		notifyAll();
	}

	public synchronized void waitForWaxing() throws InterruptedException{
		while (waxOn == false){
			wait();
		}
	}

	public synchronized void waitForBuffing() throws InterruptedException{
		while (waxOn == true){
			wait();
		}
	}
}

class Waxon implements Runnable{
	private CarHo carHo;

	public Waxon(CarHo carHo) {
		this.carHo = carHo;
	}

	public void run() {
		try{
			while (!Thread.interrupted()){
				System.out.println("wax on!");
				TimeUnit.MILLISECONDS.sleep(200);//not release the lock
				carHo.waxed();
				carHo.waitForBuffing();
			}
		} catch (InterruptedException e){
			System.out.println("exiting via interrupt");
		}

		System.out.println("Ending Wax on Task");
	}
}

class WaxOff implements Runnable{
	private CarHo carHo;

	public WaxOff(CarHo carHo) {
		this.carHo = carHo;
	}

	public void run() {
		try{
			while (!Thread.interrupted()){
				carHo.waitForWaxing();
				System.out.println("waxed off!");
				TimeUnit.MILLISECONDS.sleep(200);
				carHo.buffed();
			}
		} catch (InterruptedException e){
			System.out.println("exiting via interrupt");
		}

		System.out.println("Ending wax off task");
	}
}

public class WaxOMatic {
	public static void main(String[] args) throws Exception{
		CarHo carHo = new CarHo();
		ExecutorService service = Executors.newCachedThreadPool();
		service.execute(new Waxon(carHo));
		service.execute(new WaxOff(carHo));
		TimeUnit.SECONDS.sleep(5);
		service.shutdownNow();//interrupt all tasks

	}
	
}
