package thinkInJava;


import java.lang.reflect.*;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-4-18
 * Time: 下午11:14
 */
public class MyReflect {
	public static String name = "chenxiong";
	public static void main(String[] args)  {
		try {
			//记得要加包名
			Class<?> c = Class.forName("thinkInJava.MyReflect");
			Method[] methods = c.getMethods();
			Constructor constructor = c.getConstructor();
			//只能获取公共成员变量 public static
			java.lang.reflect.Field field = c.getField("name");
			for(Method method : methods){
				System.out.println(method);
			}
			System.out.println();
			System.out.println(field.getType());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch(NoSuchMethodException e){
			e.printStackTrace();
		}catch(NoSuchFieldException e){
			e.printStackTrace();
		}
	}
}
