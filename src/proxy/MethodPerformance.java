package proxy;

/**
 * @author <a href="mailto:chenxiong_ch.pt@taobao.com">chenxiong</a>
 * @since 12-8-31 AM10:29
 */
public class MethodPerformance {
	private long begin;
	private long end;
	private String serviceMethod;

	public MethodPerformance(String serviceMethod) {
		this.serviceMethod = serviceMethod;
		this.begin = System.currentTimeMillis();
	}

	public void printPerformance() {
		end = System.currentTimeMillis();
		long elapse = end - begin;

		System.out.println(serviceMethod + " costs" + elapse + "ms");

	}

}
