package concurrency;

/**
 * User: ${wa7chen}
 * Time: PM2:41
 */

class Sleeper extends Thread{
	private int duration;

	public Sleeper(String name,int duration){
		super(name);
		this.duration = duration;
		start();
	}

	@Override
	public void run() {
		try{
			sleep(duration);
		}catch (InterruptedException e){
			System.out.println(getName() + " was interrupted ,"
			 + isInterrupted() );
			return;
		}
		System.out.println(getName() + " has awakened");
	}
}

class TheJoiner extends Thread{
	private Sleeper sleeper;
	public TheJoiner(String name ,Sleeper sleeper){
		super(name);
		this.sleeper = sleeper;
		start();
	}

	@Override
	public void run() {
		try {
			sleeper.join();
//			sleep(1500);
		} catch (InterruptedException e) {
			System.out.println("Interrupted");
		}
		System.out.println(getName() + " join completed");
	}
}

public class Joiner {
	public static void main(String[] args) {
		Sleeper
				sleeper = new Sleeper("Sleep",1500),
				grump = new Sleeper("Grump",1500);

		TheJoiner
				joiner = new TheJoiner("Joiner",sleeper),
				doc = new TheJoiner("Doc" ,grump);
		grump.interrupt();
	}
}
