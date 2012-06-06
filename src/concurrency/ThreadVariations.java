package concurrency;

/**
 * User: ${wa7chen}
 * Time: PM11:59
 */
class InnerThread1{
	private int countDown = 5;
	private Inner inner;
	private class Inner extends Thread{
		Inner(String name){
			super(name);
			start();
		}

		public void run(){
			while (true){
				try {
					System.out.println(this);
					if(--countDown == 0 )
						return;
					sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public String toString() {
			return getName() + ":" + countDown;
		}
		
	}

	public InnerThread1(String name){
		inner = new Inner(name);
	}
}


class InnerThread2{
	private int countDown = 5;
	private Thread t ;

	public InnerThread2(String name){
		t = new Thread(name){
			@Override
			public void run() {
				while (true){
					try {
						System.out.println(this);
						if(--countDown == 0 )
							return;
						sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			@Override
			public String toString() {
				return getName() + ":" +countDown ;
			}
		};

		t.start();
	}
}

class ThreadMethod{
	private int countDown = 5;
	private Thread t;
	private String name;

	public ThreadMethod(String name){
		this.name = name;
	}

	public void runTask(){
		if(t == null){
			t = new Thread(name){
				@Override
				public void run() {
					while (true){
						try {
							System.out.println(this);
							if(--countDown == 0 )
								return;
							sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

				@Override
				public String toString() {
					return getName() + ":" +countDown ;
				}
			};
			t.start();
		}
	}
}

public class ThreadVariations {
	public static void main(String[] args) {
		InnerThread1 innerThread1 = new InnerThread1("InnerThread1");
		InnerThread2 innerThread2 = new InnerThread2("InnerThread2");
		ThreadMethod threadMethod = new ThreadMethod("ThreadMethod");
		threadMethod.runTask();
	}
}
