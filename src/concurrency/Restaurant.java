package concurrency;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * customer/service model
 *
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: PM10:54
 */
class Meal {
	private final int orderNum;

	Meal(int orderNum) {
		this.orderNum = orderNum;
	}

	@Override
	public String toString() {
		return "Meal " + orderNum;
	}
}

class WaitPerson implements Runnable {
	private Restaurant restaurant;
	private static final int limit = 3;
	WaitPerson(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public void run() {
		try {
			while (!Thread.interrupted()) {
				synchronized (this){
					while (restaurant.meals.isEmpty()){
						wait();
					}
					System.out.println("WaitPerson get " + restaurant.getMeal());
					synchronized (restaurant.chef){
//						restaurant.meals.remove(restaurant.meals.size());
						//只有等待该锁的任务才会被唤醒。
						restaurant.chef.notifyAll();//ready for another
					}
				}

			}

		} catch (InterruptedException e ){
			System.out.println("waitperson interrupted!");
		}

	}
}

class Chef implements Runnable{
	private Restaurant restaurant;
	private int count = 0;

	Chef(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public void run() {
		try{
			while(!Thread.interrupted()){
				synchronized (this){
					while (restaurant.meals.size() >= 3)
						wait();//for the meal to be taken
				}
				if(++count == 10){
					System.out.println("out of food ,closing");
					restaurant.exec.shutdownNow();//没用立即结束嘛，因为接受到的interrupted，必须在进入可中断阻塞状态中
				}
				System.out.println("Order up");

				synchronized (restaurant.waitPerson){
					restaurant.putMeal(new Meal(count));
					restaurant.waitPerson.notifyAll();
				}

				TimeUnit.MILLISECONDS.sleep(100);//shutdownNow()之后，在这里结束,如果没有，则会在while判断thread状态那里退出
			}
		} catch (InterruptedException e ){
			System.out.println("chef interrupted");
		}
	}
}

//centre control
public class Restaurant {
	Queue<Meal> meals = new ArrayDeque<Meal> ();
	ExecutorService exec = Executors.newCachedThreadPool();
	WaitPerson waitPerson = new WaitPerson(this);
	Chef chef = new Chef(this);

	public Restaurant(){
		exec.execute(chef);
		exec.execute(waitPerson);

	}

	public static void main(String[] args) {
		new Restaurant();
	}

	public synchronized Meal getMeal(){
		return meals.poll();
	}
	
	public synchronized void putMeal(Meal meal){
		meals.add(meal);
	}
}
