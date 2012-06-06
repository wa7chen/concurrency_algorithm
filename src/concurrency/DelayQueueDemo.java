package concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * 实现了Delayed接口的对象，只能在到期后才能从队列中取走
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: PM11:21
 */
class DelayTask implements Runnable,Delayed{
	private static int counter = 0;
	private final int id = counter++;
	private final int delta;
	private final long trigger;
	protected static List<DelayTask> sequence = new ArrayList<DelayTask>();

	public DelayTask(int delayInMilliseconds) {
		delta = delayInMilliseconds;
		trigger = System.currentTimeMillis() + delta;
		sequence.add(this);
	}

	//delay的设置
	public long getDelay(TimeUnit unit) {
		return trigger - System.currentTimeMillis();
	}

	public int compareTo(Delayed o) {
		DelayTask that = (DelayTask)o;
		if(trigger < that.trigger) return -1;
		if(trigger > that.trigger) return 1;
		return 0;
	}

	public void run() {
		System.out.println(this + "");
	}

	@Override
	public String toString() {
		return String.format("[%1$-4d]",delta)
				+ "Task " + id;
	}

	public String summary(){
		return "(" + id + ":" + delta +")";
	}

	public static class EndSentinel extends DelayTask {
		private ExecutorService service;

		public EndSentinel(int delayInMilliseconds, ExecutorService service) {
			super(delayInMilliseconds);
			this.service = service;
		}

		public void run(){
			System.out.println("here wo back!");
			for(DelayTask task: sequence){
				System.out.println(task.summary() + " ");
			}
			System.out.println();
			System.out.println(this + " Calling shutdown");
			service.shutdownNow();
		}
	}
}

class DelayedTaskConsumer implements Runnable{
	private DelayQueue<DelayTask> q;

	DelayedTaskConsumer(DelayQueue<DelayTask> q) {
		this.q = q;
	}

	public void run() {
		try{
			while (!Thread.interrupted()){
				//按照delta的时间大小take task，而不是按照
				//进入queue的时间顺序
				q.take().run();
				//从启动顺序可以看出，delay最小的最先run
			}
		} catch (InterruptedException e){
			//
		}
		System.out.println("Finished Consumer!!!");
	}
}

public class DelayQueueDemo{
	public static void main(String[] args) {
		Random random = new Random(47);
		ExecutorService service = Executors.newCachedThreadPool();
		DelayQueue<DelayTask> queue = new DelayQueue<DelayTask>();

		for (int i = 0; i < 20; i++) {
			queue.put(new DelayTask(random.nextInt(5000)));
		}
		//这时是最后被取出来的。
		queue.add(new DelayTask.EndSentinel(5000,service));
		service.execute(new DelayedTaskConsumer(queue));
	}
}
