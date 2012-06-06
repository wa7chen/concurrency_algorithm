package concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: ${wa7chen}
 * Time: PM2:47
 */
public class ExecutorTest {
	public static void main(String[] args) {
//		ExecutorService exe = Executors.newCachedThreadPool();
		ExecutorService exe = Executors.newSingleThreadExecutor();
		for(int i = 0;i < 5;i++){
			exe.execute(new LiftOff());
		}
		exe.shutdown();
	}
}
