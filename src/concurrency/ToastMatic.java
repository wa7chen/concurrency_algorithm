package concurrency;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: PM3:51
 */
class Toast{
	public enum Status {DRY,BUFFERED,JAMMED}
	private Status status = Status.DRY;
	private final int id;

	Toast(int id) {
		this.id = id;
	}

	public void buffer(){
		status = Status.BUFFERED;
	}

	public void jam(){
		status = Status.JAMMED;
	}

	public int getId() {
		return id;
	}

	public Status getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "Toast{" +
				"status=" + status +
				", id=" + id +
				'}';
	}
}

class ToastQueue extends LinkedBlockingQueue<Toast>{}

class Toaster implements Runnable{
	private ToastQueue toastQueue;
	private int count = 0;
	private Random random = new Random(47);

	Toaster(ToastQueue toastQueue) {
		this.toastQueue = toastQueue;
	}

	public void run() {
		try{
			while(!Thread.interrupted()){
				TimeUnit.MILLISECONDS.sleep(100 + random.nextInt(500));
				Toast toast = new Toast(count++);
				System.out.println(toast);
				toastQueue.put(toast);
			}
		} catch (InterruptedException e){
			System.out.println("Toaster interrupted");
		}

		System.out.println("Toaster off");
	}
}

class Bufferer implements Runnable{
	private ToastQueue dryQueue,bufferedQueue;

	Bufferer(ToastQueue dryQueue, ToastQueue bufferedQueue) {
		this.dryQueue = dryQueue;
		this.bufferedQueue = bufferedQueue;
	}

	public void run() {
		try{
			while (!Thread.interrupted()){
				Toast t = dryQueue.take();
				t.buffer();
				System.out.println(t);
				bufferedQueue.put(t);
			}
		} catch (InterruptedException e){
			System.out.println("Buffered interrupted");
		}
		System.out.println("Bufferer off!");

	}
}

class Jammer implements Runnable{
	private ToastQueue bufferedQueeu,finishedQueue;

	Jammer(ToastQueue bufferedQueeu, ToastQueue finishedQueue) {
		this.bufferedQueeu = bufferedQueeu;
		this.finishedQueue = finishedQueue;
	}

	public void run() {
		try{
			while (!Thread.interrupted()){
				Toast t = bufferedQueeu.take();
				t.jam();
				System.out.println(t);
				finishedQueue.put(t);
			}
		} catch (InterruptedException e ){
			System.out.println("Jammer interrupted");
		}
		System.out.println("Jammer off");
	}
}

class Eater implements Runnable{
	private ToastQueue finishedQueue;
	private int counter = 0;

	Eater(ToastQueue finishedQueue) {
		this.finishedQueue = finishedQueue;
	}

	public void run() {
		try{
			while (!Thread.interrupted()){
				Toast t = finishedQueue.take();
				if(t.getId() != counter++ ||
				t.getStatus() != Toast.Status.JAMMED){
					System.err.println(">>>>Error:" + t);
					System.exit(1);
				} else{
					System.out.println("Chomp !" + t);
				}
			}
		} catch (InterruptedException e){
			System.out.println("Eater interrupted");
		}
		System.out.println("Eater off!");
	}
}

public class ToastMatic {
	public static void main(String[] args) throws Exception{
		ToastQueue dryQueue = new ToastQueue(),
				bufferedQueue = new ToastQueue(),
				finishedQueue = new ToastQueue();

		ExecutorService service = Executors.newCachedThreadPool();
		service.execute(new Toaster(dryQueue));
		service.execute(new Bufferer(dryQueue,bufferedQueue));
		service.execute(new Jammer(bufferedQueue,finishedQueue));
		service.execute(new Eater(finishedQueue));

		TimeUnit.SECONDS.sleep(5);
		service.shutdownNow();
	}
}
