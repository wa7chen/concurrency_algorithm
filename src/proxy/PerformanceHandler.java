package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK's proxy to do performance monitor
 * aim to interface
 *
 * @author <a href="mailto:chenxiong_ch.pt@taobao.com">chenxiong</a>
 * @since 12-8-31 AM10:33
 */
public class PerformanceHandler implements InvocationHandler {
	private Object target;

	public PerformanceHandler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		PerformanceMonitor.begin(target.getClass().getName() + "." + method.getName());
		Object o = method.invoke(target, args);
		PerformanceMonitor.end();

		return o;
	}

	public static void main(String[] args) {
		FileService target = new FileServiceIml();
		PerformanceHandler handler = new PerformanceHandler(target);

		FileService proxy = (FileService)Proxy.newProxyInstance(target.getClass().getClassLoader(),
				target.getClass().getInterfaces(),
				handler
		);

		proxy.rename("hello");
		proxy.delete(1);

	}
}
