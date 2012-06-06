package thinkInJava;

/**
 * User: ${wa7chen}
 * Time: 下午10:57
 */
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectTester {

    /**
     * 在这个类里面存在有copy（）方法，根据指定的方法的参数去 构造一个新的对象的拷贝
     * 并将他返回
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SecurityException
     * @throws IllegalArgumentException
     */
    public Object copy(Object obj) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{

        //获得对象的类型
        Class classType=obj.getClass();
        System.out.println("该对象的类型是："+classType.toString());

        //通过默认构造方法去创建一个新的对象，getConstructor的视其参数决定调用哪个构造方法
        Object objectCopy=classType.getConstructor(new Class[]{}).newInstance(new Object[]{});

        //获得对象的所有属性
        Field[] fields=classType.getDeclaredFields();

        for(int i=0;i < fields.length;i++){
            //获取数组中对应的属性
            Field field=fields[i];

            String fieldName=field.getName();
            String stringLetter=fieldName.substring(0, 1).toUpperCase();

            //获得相应属性的getXXX和setXXX方法名称
            String getName="get"+stringLetter+fieldName.substring(1);
            String setName="set"+stringLetter+fieldName.substring(1);

            //获取相应的方法
            Method getMethod=classType.getMethod(getName, new Class[]{});
            Method setMethod=classType.getMethod(setName, new Class[]{field.getType()});

            //调用源对象的getXXX（）方法
            Object value=getMethod.invoke(obj, new Object[]{});
            System.out.println(fieldName+" :"+value);

            //调用拷贝对象的setXXX（）方法
            setMethod.invoke(objectCopy,new Object[]{value});

        }

        return objectCopy;

    }


    public static void main(String[] args) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Customer customer=new Customer();
        customer.setName("hejianjie");
        customer.setId(new Long(1234));
        customer.setAge(19);

        Customer customer2=null;
        customer2=(Customer)new ReflectTester().copy(customer);
        System.out.println(customer.getName()+" "+customer2.getAge()+" "+customer2.getId());

        System.out.println(customer);
        System.out.println(customer2);

    }

}


class Customer{

    private Long id;

    private String name;

    private int age;


    public Customer(){

    }

    public int getAge() {
        return age;
    }


    public void setAge(int age) {
        this.age = age;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

}
