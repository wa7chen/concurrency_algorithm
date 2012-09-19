package test.baixing;

/**
 * 浏览记录客户端
 * @author <a href="mailto:chenxiong_ch.pt@taobao.com">chenxiong</a>
 * @since 12-9-18 PM6:20
 */
public class ViewCounterClient {
	private ViewCounterCache cache;

	/**
	 * 每次浏览一次页面
	 * @param id 页面编号
	 * @param old 上次的浏览量
	 * @return
	 */
	public int doRequest(int id,int old) {
		int count = cache.increment(id);
		//do some thing ...
		return count == old?++count:count;
	}
}
