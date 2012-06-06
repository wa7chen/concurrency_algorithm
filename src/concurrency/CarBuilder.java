package concurrency;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: PM3:00
 */
class Car {
	private final int id ;
	private boolean
		engine = false,driveTrain = false,wheels = false;

	Car(int id) {
		this.id = id;
	}

	Car() {
		id = -1;
	}

	public synchronized int getId() {
		return id;
	}

	public synchronized void addEngine(){
		engine = true;
	}

	public synchronized void addDriveTrain(){
		driveTrain = true;
	}

	public synchronized void addWheels(){
		wheels = true;
	}

	@Override
	public String toString() {
		return "[Car -- id: " + id + " engine :" + engine + " driveTrain :" + driveTrain +
				" wheels :" + wheels + "]";
	}
}

class CarQueue extends LinkedBlockingQueue<Car>{}

class ChassisBuilder implements Runnable{
	private int counter = 0;
	private CarQueue cars;

	ChassisBuilder(CarQueue cars) {
		this.cars = cars;
	}

	public void run() {
		try{
			while (!Thread.interrupted()){
				TimeUnit.MILLISECONDS.sleep(300);
				Car car = new Car(counter++);
				System.out.println("ChassisBuilder create car " + car);
				cars.put(car);
			}
		} catch (InterruptedException e){
			System.out.println("ChassisBuilder interrupted!");
		}

		System.out.println("ChassisBuilder off");

	}
}

class Assembler implements Runnable{
	private CarQueue chassisQueue,finishingQueue;
	private Car car;
	private CyclicBarrier barrier = new CyclicBarrier(4);
	private RobotPool robotPool;

	Assembler(CarQueue finishingQueue, CarQueue chassisQueue, RobotPool robotPool) {
		this.finishingQueue = finishingQueue;
		this.chassisQueue = chassisQueue;
		this.robotPool = robotPool;
	}

	public Car car(){return car;}

	public CyclicBarrier barrier(){
			return barrier;
	}

	public void run() {
		try{
			while (!Thread.interrupted()){
				//只有chassis可用时才不会阻塞
				car = chassisQueue.take();
				robotPool.hire(EngineRobot.class,this);
				robotPool.hire(DriveTrainRobot.class,this);
				robotPool.hire(WheelRobot.class,this);
				barrier.await();//等待robot工作的完成
				finishingQueue.add(car);
			}
		} catch (InterruptedException e){
			System.out.println("Exiting Assembler via interrupted");
		} catch (BrokenBarrierException e){
			throw new RuntimeException(e);
		}
		System.out.println("Assembler off");
	}
}

class Reporter implements Runnable{
	private CarQueue cars;

	Reporter(CarQueue cars) {
		this.cars = cars;
	}

	public void run() {
		try{
			while (!Thread.interrupted()){
				System.out.println(cars.take());
			}
		}catch (InterruptedException e){
			System.out.println("Reporter interrupted!");
		}
		System.out.println("Reported off");
	}
}

abstract class Robot implements Runnable{
	private RobotPool robotPool;

	protected Robot(RobotPool robotPool) {
		this.robotPool = robotPool;
	}

	protected Assembler assembler;

	public Robot assignAssembler(Assembler assembler1){
		assembler = assembler1;
		return this;
	}

	private boolean engage = false;

	public synchronized void engage(){
		engage = true;
		//the lock object is the robot
		notifyAll();//通知在powerdown的robot，开始准备工作
	}

	abstract protected void performService();

	public void run(){
		try{
			powerDown();
			while (!Thread.interrupted()){
				performService();
				//同步操作
				assembler.barrier().await();
				//工作结束
				powerDown();
			}
		} catch (InterruptedException e){
			System.out.println("Exiting " + this + " via interrupted!");
		}catch (BrokenBarrierException e){
			throw new RuntimeException(e);
		}

		System.out.println(this + " off");
	}

	protected synchronized void
	powerDown() throws InterruptedException{
		engage = false;
		assembler = null;
		//把他们重新放回可用的池中
		robotPool.release(this);

		while (engage == false){
			//
			wait();
		}
	}

	@Override
	public String toString() {
		return getClass().getName();
	}
}

class EngineRobot extends Robot {
	EngineRobot(RobotPool robotPool) {
		super(robotPool);
	}

	@Override
	protected void performService() {
		System.out.println(this + " installing engine");
		assembler.car().addEngine();

	}
}

class DriveTrainRobot extends Robot {
	DriveTrainRobot(RobotPool robotPool) {
		super(robotPool);
	}

	@Override
	protected void performService() {
		System.out.println(this + " installing DriveTrain");
		assembler.car().addDriveTrain();
	}
}

class WheelRobot extends Robot {
	WheelRobot(RobotPool robotPool) {
		super(robotPool);
	}

	@Override
	protected void performService() {
		System.out.println(this + " installing Wheel");
		assembler.car().addWheels();
	}
}

class RobotPool {
	private Set<Robot> pool = new HashSet<Robot>();

	public synchronized void add(Robot r){
		pool.add(r);
		notifyAll();
	}

	public synchronized void hire(Class<? extends Robot> robotType,Assembler d)
	throws InterruptedException{
		for (Robot robot : pool) {
			if(robot.getClass().equals(robotType)){
				pool.remove(robot);
				robot.assignAssembler(d);
				robot.engage();
				return;
			}
		}
		wait();
		hire(robotType,d);

	}

	public synchronized void release(Robot robot){
		pool.add(robot);
	}
}

public class CarBuilder {
	public static void main(String[] args) throws Exception{
		CarQueue chassisQueue = new CarQueue(),
				finishingQueue = new CarQueue();

		ExecutorService service = Executors.newCachedThreadPool();
		RobotPool pool = new RobotPool();
		service.execute(new EngineRobot(pool));
		service.execute(new DriveTrainRobot(pool));
		service.execute(new WheelRobot(pool));

		service.execute(new Assembler(finishingQueue,chassisQueue,pool));
		service.execute(new Reporter(finishingQueue));
		service.execute(new ChassisBuilder(chassisQueue));

		TimeUnit.SECONDS.sleep(7);
		service.shutdownNow();
	}
}
