package concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: ${wa7chen}
 * Time: AM10:57
 */

class BlockDemo {
	private int x = 0;
	private Object object1 = new Object();
	private Lock lock = new ReentrantLock();
	private Lock lock1 = new ReentrantLock();
	private Lock lock2 = new ReentrantLock();
	public void f(){
		lock.lock();
		try{
				for(int i = 0 ;i < 1000;i++){
				System.out.println("f():  i=" + ++x );
				Thread.yield();
				}
		}finally {
			lock.unlock();
		}
	}

	public void g(){
		lock.lock();
		try{
			for(int i = 0 ;i < 1000;i++){
				System.out.println("g(): i=" + ++x);
				Thread.yield();
			}
		} finally{
			lock.unlock();
		}
	}

	public void m(){
		lock.lock();
		try{
			for(int i = 0 ;i < 1000;i++){
				System.out.println("m(): i=" + ++x);
				Thread.yield();
			}
		}finally{
			lock.unlock();
		}
	}

}

public class Exercise15 {
	public static void main(String[] args) {
		final BlockDemo blockDemo = new BlockDemo();
		new Thread(){
			@Override
			public void run() {
				blockDemo.f();
			}
		}.start();

		new Thread(){
			@Override
			public void run() {
				blockDemo.g();
			}
		}.start();

		blockDemo.m();
	}	
}
