package test.baixing;


import java.util.HashMap;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 浏览记录缓存服务
 *
 * @author <a href="mailto:chenxiong_ch.pt@taobao.com">chenxiong</a>
 * @since 12-9-18 PM6:21
 */
public class ViewCounterCache {

	//更新数据库间隔时间(ms)
	private static final int PEROID = 2000;
	final ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);
	final BlockingQueue<Integer> waitingRecord = new LinkedBlockingQueue<Integer>(200);
	final Executor service = Executors.newSingleThreadExecutor();
	ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	volatile HashMap<Integer, Integer> recordMap = new HashMap<Integer, Integer>(100000);

	public ViewCounterCache() {
		//每隔2s 更新数据库
		scheduled.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				//TODO 更新DB
			}
		}, 500, PEROID, TimeUnit.MILLISECONDS);

		service.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					//更新内存
					updateCache();
				}

			}
		});

	}

	/**
	 * 每个请求处理后增加缓存计数
	 * 异步操作，读取当前的计数返回
	 * 这次的更新放入队列中
	 * 读写分离
	 * @param uid
	 * @return
	 */
	public int increment(int uid) {
		int current = 1;
		try {
			lock.readLock().lock();
			if (recordMap.containsValue(uid)) {
				current = recordMap.get(uid);
			}
		} finally {
			lock.readLock().unlock();
		}

		try {
			waitingRecord.put(uid);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return current;
	}

	/**
	 * 更新操作
	 * 读取队列中需要更新的记录
	 */
	public void updateCache() {
		try {
			int uid = waitingRecord.take();

			if(uid == 0)
				return;

			lock.writeLock().lock();
			//更新浏览记录
			if (recordMap.containsValue(uid)) {
				recordMap.put(uid, recordMap.get(uid) + 1);
			} else {
				recordMap.put(uid, 1);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.writeLock().unlock();
		}

	}
}
