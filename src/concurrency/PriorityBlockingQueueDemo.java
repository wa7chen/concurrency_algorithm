package concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: AM11:30
 */
class PrioritizedTask implements Runnable,Comparable<PrioritizedTask>{
	private Random random = new Random(47);
	private static int counter = 0;
	private final int id = counter++;
	private int priority;
	protected static List<PrioritizedTask> sequence =
			new ArrayList<PrioritizedTask>();

	PrioritizedTask(int priority) {
		this.priority = priority;
		sequence.add(this);
	}

	public int compareTo(PrioritizedTask o) {
		return priority < o.priority ? 1:
				(priority > o.priority ? -1:0 );
	}

	public void run() {
		try {
			TimeUnit.MILLISECONDS.sleep(random.nextInt(200));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(this);
	}

	@Override
	public String toString() {
		return String.format("[%1$-3d",priority) + " Task " + id;
	}
	
	public String summary(){
		return "(" +id + " :" + priority + " )";
	}

	public static class EndSentinel extends PrioritizedTask {
		private ExecutorService service;

		public EndSentinel(ExecutorService service) {
			//lowest priority in this program
			super(-1);
			this.service = service;
		}

		public void run(){
			for (PrioritizedTask prioritizedTask : sequence) {
				System.out.println(prioritizedTask.summary());
			}
			System.out.println();
			System.out.println(this + " Calling shutdown!");
			service.shutdownNow();
		}
	}
}

class PrioritizedTaskProducer implements Runnable{
	private Random random = new Random(47);
	private Queue<Runnable> queue;
	private ExecutorService service;

	PrioritizedTaskProducer(Queue<Runnable> queue, ExecutorService service) {
		this.queue = queue;
		this.service = service;
	}

	public void run() {
		for(int i = 0;i < 20;i++){
			queue.add(new PrioritizedTask(random.nextInt(10)));
			Thread.yield();
		}
		try{
			//in highest-priority jobs
			for (int i = 0; i < 10; i++) {
				TimeUnit.MILLISECONDS.sleep(250);
				queue.add(new PrioritizedTask(10));
			}

			for(int i = 0;i < 10;i++){
				queue.add(new PrioritizedTask(i));
			}
			queue.add(new PrioritizedTask.EndSentinel(service));
		} catch(InterruptedException e){
			//
		}

		System.out.println("Finished PrioritizedTask Producer!!");
	}
}

class PrioritizedTaskConsumer implements Runnable{
	private PriorityBlockingQueue<Runnable> q;

	PrioritizedTaskConsumer(PriorityBlockingQueue<Runnable> q) {
		this.q = q;
	}

	public void run() {
		try{
			while (!Thread.interrupted())
				q.take().run();
		}catch (InterruptedException e){

		}

		System.out.println("Finished PrioritizedTaskConsumer!");
	}
}

public class PriorityBlockingQueueDemo {
	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		PriorityBlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>();
		service.execute(new PrioritizedTaskProducer(queue,service));
		service.execute(new PrioritizedTaskConsumer(queue));
	}
}
