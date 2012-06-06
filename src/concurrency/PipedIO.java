package concurrency;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: PM1:43
 */

public class PipedIO {
	public static void main(String[] args) throws Exception{
		Sender sender = new Sender();
		Receiver receiver1 = new Receiver(sender);
		Receiver receiver2 = new Receiver(sender);
		ExecutorService service = Executors.newCachedThreadPool();
		service.execute(sender);
		service.execute(receiver1);
		service.execute(receiver2);
		TimeUnit.SECONDS.sleep(4);
		service.shutdownNow();
	}
}

class Sender implements Runnable{
	private Random rand = new Random(47);
	private PipedWriter out = new PipedWriter();

	public PipedWriter getOut() {
		return out;
	}

	public void run() {
		try{
			while (true){
				for(char c = 'A'; c <= 'Z';c++){
					out.write(c);
					TimeUnit.MILLISECONDS.sleep(rand.nextInt(500));
				}
			}
		} catch (IOException e){
			System.out.println(e + "send write exception!");
		} catch (InterruptedException e){
			System.out.println(e + "end interrupted!");
		}
		
	}
}

class Receiver implements Runnable{
	private PipedReader in;
	
	public Receiver(Sender sender) throws IOException{
		in = new PipedReader(sender.getOut());

	}

	public void run() {
		try {
			while (true){
				System.out.println("Read "+ (char)in.read() + "." );
			}
		} catch (IOException e){
			System.out.println(e + "receiver read exception!");
		}

	}
}