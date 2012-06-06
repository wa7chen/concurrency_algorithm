package concurrency;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: PM9:18
 */
class Customer{
	private final int serviceTime ;
	public Customer(int serviceTime) {
		this.serviceTime = serviceTime;
	}

	public int getServiceTime() {
		return serviceTime;
	}

	@Override
	public String toString() {
		return "[" + serviceTime +"]";
	}
}

class CustomerLine extends ArrayBlockingQueue<Customer>{
	public CustomerLine(int capacity) {
		super(capacity);
	}

	@Override
	public String toString() {
		if(this.size() == 0)
			return "[Empty]";
		StringBuilder sb = new StringBuilder();
		for (Customer customer : this) {
			sb.append(customer);
		}

		return sb.toString();
	}
}

class CustomerGenerator implements Runnable {
	private CustomerLine customers;
	private static Random random = new Random(47);

	CustomerGenerator(CustomerLine customers) {
		this.customers = customers;
	}

	public void run() {
		try{
			while (!Thread.interrupted()){
				TimeUnit.MILLISECONDS.sleep(random.nextInt(300));
				customers.put(new Customer(random.nextInt(1000)));
			}
		} catch (InterruptedException e ){
			System.out.println("Customer generator interrupted!!");
		}

		System.out.println("Customer generator terminating!!");
	}
}

class Teller implements Runnable,Comparable<Teller>{
	private static int counter = 0;
	private final int id = counter++;
	private int customerServed = 0;
	private CustomerLine customers;
	private boolean servingCustomerLine = true;

	Teller(CustomerLine customers) {
		this.customers = customers;
	}

	public void run() {
		try{
			while (!Thread.interrupted()){
				Customer customer = customers.take();
				TimeUnit.MILLISECONDS.sleep(customer.getServiceTime());
				synchronized (this){
					customerServed++;
					while (!servingCustomerLine)
						wait();
				}
			}
		} catch (InterruptedException e ){
			System.out.println(this + " interrupted!!");
		}

	}

	public synchronized void doSomethingElse(){
		servingCustomerLine = false;
		customerServed = 0;
	}

	public synchronized void serveCustomerLine(){
		assert !servingCustomerLine:"already serving : " + this;
		servingCustomerLine = true;
		notifyAll();
	}
	
	public String toString(){
		return "Teller " + id ;
	}
	
	public String shortString(){
		return "T " + id ;
	}

	//Used by priority queue
	//将任务量最小的TEller往前推
	public synchronized int compareTo(Teller o) {
		return customerServed < o.customerServed ? -1:
				(customerServed == o.customerServed ? 0:1);
	}
}

class TellerManager implements Runnable{
	private ExecutorService service;
	private CustomerLine customers;
	private PriorityQueue<Teller> workTells =
			new PriorityQueue<Teller>();
	private Queue<Teller> tellersDoingOther =
			new LinkedList<Teller>();
	private int adjustmentPeriod;

	TellerManager(int adjustmentPeriod, ExecutorService service, CustomerLine customers) {
		this.adjustmentPeriod = adjustmentPeriod;
		this.service = service;
		this.customers = customers;

		Teller teller = new Teller(customers);
		service.execute(teller);
		workTells.add(teller);
	}

	public void adjustTellerNumber(){
		if(customers.size() / workTells.size() > 2){
			if(tellersDoingOther.size() > 0){
				Teller teller = tellersDoingOther.remove();
				teller.serveCustomerLine();
				workTells.offer(teller);
				return;
			}

			Teller teller = new Teller(customers);
			service.execute(teller);
			workTells.add(teller);

			return;
		}

		if(workTells.size() > 1&&
				customers.size() / workTells.size() < 2){
			if(customers.size() == 0)
				while (workTells.size() > 1)
					reassignOtherTeller();
		}
	}

	public void reassignOtherTeller(){
		//我认为Teller里面需要同步的原因主要是在这里
		//当取出来之后，在可能这个teller继续在执行，
		Teller teller = workTells.poll();
		teller.doSomethingElse();
		tellersDoingOther.offer(teller);
	}

	public void run() {
		try{
			while (!Thread.interrupted()){
				TimeUnit.MILLISECONDS.sleep(adjustmentPeriod);
				adjustTellerNumber();
				System.out.println(customers + "{");
				for (Teller teller : workTells) {
					System.out.println(teller.shortString() + "  ");
				}
				System.out.println("}");
			}
		} catch (InterruptedException e){
			System.out.println("Teller Manager interrupted!!");
		}

		System.out.println(this + " terminating!!");
	}

	@Override
	public String toString() {
		return "Teller Manager";
	}
}

public class BankTellerSimulation {
	public static final int MAX_LINE = 50;
	public static final int ADJUSTMENT_PERIOD = 1000;

	public static void main(String[] args) throws Exception{
		ExecutorService service = Executors.newCachedThreadPool();
		CustomerLine customers = new CustomerLine(MAX_LINE);
		service.execute(new CustomerGenerator(customers));
		service.execute(new TellerManager(ADJUSTMENT_PERIOD,service,customers));
		System.out.println("Press Enter to quit");
		System.in.read();

		service.shutdownNow();

	}
}
