package concurrency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * compare the synchronized function and block
 * User: ${wa7chen}
 * Time: PM10:01
 */

class Pair{
	private int x,y;
	public Pair(int x,int y){
		this.x = x;
		this.y = y;
	}
	public Pair(){
		this(0,0);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	//Pair is no-thread safe,it need to keep
	//two param the same at the same time,
	//but we can do sth to make it safe
	public void incrementX(){
		x++;
	}

	public void incrementY(){
		y++;
	}

	public String toString(){
		return "x:"+ x + ",y:" + y;
	}

	public class PairValuesNotEqualException extends RuntimeException{
		public PairValuesNotEqualException(){
			super("pair values not equal" + Pair.this);
		}
	}

	public void checkStatue(){
		if(x != y){
			throw new PairValuesNotEqualException();
		}
	}
}

abstract class PairManager{
	AtomicInteger checkCounter = new AtomicInteger(0);
	protected Pair pair = new Pair();
	//storge is thread-safe
	private List<Pair> storage = Collections.synchronizedList(new ArrayList<Pair>());

	public synchronized Pair getPair(){
		//make a copy to keep the original safe
		return new Pair(pair.getX(),pair.getY());
	}

	protected void store(Pair p ){
		storage.add(p);
		try {
			TimeUnit.MILLISECONDS.sleep(50);
		} catch (InterruptedException e) {}
	}

	public abstract void increment();

}

class PairManager1 extends PairManager {
	@Override
	public synchronized void increment() {
		pair.incrementX();
		pair.incrementY();
		store(pair);
	}
}

class PairManager2 extends PairManager {
	@Override
	public void increment() {
		Pair temp;
		synchronized (this){
			pair.incrementX();
			pair.incrementY();
			temp = getPair();
		}
		store(temp);
	}
}

class PairManipulator implements Runnable{
	private PairManager pm;

	public PairManipulator(PairManager pm){
		this.pm = pm;
	}
	public void run() {
		while (true){
			pm.increment();
		}
	}

	@Override
	public String toString() {
		return "Pair:" + pm.getPair() +
				"checkCounter :" + pm.checkCounter.get();
	}
}

class PairChecker implements Runnable{
	private PairManager pm;

	PairChecker(PairManager pm) {
		this.pm = pm;
	}

	public void run() {
		while (true){
			pm.checkCounter.getAndIncrement();
			pm.getPair().checkStatue();
		}
	}
}

public class CriticalSection {

	static void testApproaches(PairManager pman1,PairManager pman2){
		ExecutorService service = Executors.newCachedThreadPool();
		PairManipulator pm1 = new PairManipulator(pman1),
				pm2 = new PairManipulator(pman2);
		PairChecker pcheck1 = new PairChecker(pman1),
				pcheck2 = new PairChecker(pman2);

		service.execute(pm1);
		service.execute(pm2);
		service.execute(pcheck1);
		service.execute(pcheck2);
		try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e) {
			System.out.println("sleep interrupted");
		}

		System.out.println("pm1:" + pm1 +"\n pm2:" + pm2);
		System.exit(0);
	}

	public static void main(String[] args) {
		PairManager
				pman1 = new PairManager1(),
				pman2 = new PairManager2();
		testApproaches(pman1,pman2);
	}
	/*
	output:
	pm1:Pair:x:436,y:436checkCounter :2
    pm2:Pair:x:437,y:437checkCounter :785904921
	 */
}
