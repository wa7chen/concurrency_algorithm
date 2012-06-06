package concurrency;

/**
 * sync in different object 
 *the two syncs are isolate,neither harm the other
 * g() and f() runing at the same time,not g() ends then f() begins
 *
 * User: ${wa7chen}
 * Time: PM11:56
 */
public class DualSync {
	private Object otherSyncObject = new Object();

	public synchronized void f(){
		for (int i = 0; i < 5; i++) {
			System.out.println("f()");
			Thread.yield();
		}
	}

	/*

	public synchronized void g(){
		for(int i = 0;i < 5;i++){
			System.out.println("g()");
			Thread.yield();
		}
	}
	 */
	public void g(){
		synchronized (otherSyncObject){
			for(int i = 0;i < 5;i++){
				System.out.println("g()");
				Thread.yield();
			}
		}
	}

	public static void main(String[] args) {
		final DualSync dualSync = new DualSync();
		new Thread(){
			public void run(){
				dualSync.f();
			}
		}.start();
		//main thread run this g() function
		dualSync.g();
	}
}


