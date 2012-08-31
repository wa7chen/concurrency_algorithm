package proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * use Cglib to proxy ,
 * can aim to class not interface
 *
 * @author <a href="mailto:chenxiong_ch.pt@taobao.com">chenxiong</a>
 * @since 12-8-31 AM10:56
 */
public class CglibProxy implements MethodInterceptor {
	private Enhancer enhancer = new Enhancer();

	public Object getProxy(Class clazz) {
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(this);

		return enhancer.create();

	}

	@Override
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
		PerformanceMonitor.begin(o.getClass().getName() + "." + method.getName());
		Object result = methodProxy.invokeSuper(o,objects);
		PerformanceMonitor.end();
		return result;
	}


	public static void main(String[] args) {
		CglibProxy proxy = new CglibProxy();
		FileServiceIml fileService = (FileServiceIml)proxy.getProxy(FileServiceIml.class);
		fileService.rename("file");
		fileService.delete(1);

	}
}
