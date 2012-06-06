package thinkInJava.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-4-24
 * Time: 上午10:40
 */
public class TestThread implements Runnable {
	public int countDown = 10;
	private static int taskCount = 0;
	//来区分多个任务的多个实例，因为是final所有一旦实例化就不能被修改,每个对象的都不相同
	private final int id = taskCount++;

	public String status(){
		return "#" + id + "(" +(countDown > 0 ? countDown:"over")+ ")";
	}
	public void run() {
		while(countDown-- > 0){
			System.out.println(status());
			// 暂停当前正在执行的线程对象，并执行其他线程。
			Thread.yield();
		}
	}

	public static void main(String[] args) {
//		for(int i = 0;i < 5;i++){
//			new Thread(new TestThread()).start();
//			System.out.println("waiting for---");
//		}
		//使用executor
		ExecutorService executorService = Executors.newCachedThreadPool();
		for(int i = 0;i < 5; i++){
			executorService.execute(new TestThread());
		}
		executorService.shutdown();
	}
}
