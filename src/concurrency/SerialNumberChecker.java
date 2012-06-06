package concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * volatile cant ensure thread safety,
 * ++operator is run by ser operation.
 *
 * another modify is use atomic class!!!false
 * return and add also need synchronized
 * User: ${wa7chen}
 * Time: PM11:07
 */
class CircularSet {
	private int[] array;
	private int len;
	private int index = 0;

	public CircularSet(int size){
		len = size;
		array = new int[size];

		for (int i = 0; i < array.length; i++) {
			array[i] = -1;
		}
		
	}

	public synchronized void add(int i ) {
		array[index] = i;
		index = ++index % len;
	}

	public synchronized boolean contains(int val){
		for (int i : array) {
			if(i == val ) return true;
		}
		return false;
	}
}

class SerialNumberGenerator{
	private static volatile int serialNumber = 0;
	private static AtomicInteger atomicInteger = new AtomicInteger(0);
	//if there is not synchronized ,this will not be thread safe.!
	public  synchronized static int nextSerialNumber(){
		//++is not atom operation,and
		//it contain read and write operator
		return serialNumber++;
	}
	//this is not safe neither,!!
	public static int nextSerialNumberAtom(){
		atomicInteger.addAndGet(1);
		return atomicInteger.get();
	}
}

public class SerialNumberChecker {

	private static final int SIZE = 10;
	private static CircularSet serials = new CircularSet(1000);
	private static ExecutorService service = Executors.newCachedThreadPool();

	static class SerialChecker implements Runnable{
		public void run() {
			while (true){
//				int serial = SerialNumberGenerator.nextSerialNumberAtom();
				int serial = SerialNumberGenerator.nextSerialNumber();
				if(serials.contains(serial)){
					System.out.println("Duplicate: " + serial);
					System.exit(0);
				}
				serials.add(serial);
			}

		}
	}

	public static void main(String[] args) throws Exception{
		for(int i = 0;i < SIZE; i++){
			service.execute(new SerialChecker());
			if(args.length > 0){
				TimeUnit.SECONDS.sleep(new Integer(args[0]));
				System.out.println("No Duplicate detected!");
				System.exit(0);
			}
		}
	}
}
