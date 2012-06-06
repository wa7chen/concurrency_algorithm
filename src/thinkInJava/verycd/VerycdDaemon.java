package thinkInJava.verycd;


/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-5-21
 * Time: 下午1:01
 */
//该程序的后台线程，观察当前在线人数
public class VerycdDaemon implements Runnable{
	private int usersCount ;
	private MyVeryCD myVeryCD;

	public VerycdDaemon(MyVeryCD myVeryCD){
		this.myVeryCD = myVeryCD;
	}
//每隔10秒显示刷新一次当前在线人数
	public void run() {
		try{
			while(true){
				System.out.println("当前在线的用户: " + getUsersCount() );
				Thread.sleep(10000);
			}
		}catch(InterruptedException e){
			e.printStackTrace();

		}
	}

	public int getUsersCount(){
		Thread daemon = new Thread(myVeryCD);
		ThreadGroup threadGroup = daemon.getThreadGroup();
		return threadGroup.activeCount();
	}

	public static void main(String[] args) {
		Thread daemon = new Thread(new VerycdDaemon(new MyVeryCD()));
		daemon.start();
		try {
			daemon.join(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
