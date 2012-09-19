package crawler;

import java.util.concurrent.BlockingQueue;

/**
 * @author <a href="mailto:chenxiong_ch.pt@taobao.com">chenxiong</a>
 * @since 12-9-14 AM12:09
 */
public class CommitModule {
	private BlockingQueue inQueue ;
	public CommitModule(BlockingQueue queue ) {
		this.inQueue = queue;
	}
}
