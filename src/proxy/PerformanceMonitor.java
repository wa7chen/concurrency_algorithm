package proxy;

/**
 * @author <a href="mailto:chenxiong_ch.pt@taobao.com">chenxiong</a>
 * @since 12-8-31 AM10:35
 */
public class PerformanceMonitor {
	private static ThreadLocal<MethodPerformance> performanceRecode =
			new ThreadLocal<MethodPerformance>();

	public static void begin(String method) {
		System.out.println("begin monitor ...");
		MethodPerformance mp = new MethodPerformance(method);
		performanceRecode.set(mp);
	}

	public static void end() {
		System.out.println("end monitor ...");
		MethodPerformance mp = performanceRecode.get();
		mp.printPerformance();

	}
}
