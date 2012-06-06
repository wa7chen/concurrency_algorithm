/**
 * User: ${wa7chen}
 * Time: PM10:47
 */
public class TestStaticParam {
	public static final String WORLD = bar();
	public static final String[] HELLO = {"foo" , WORLD};
	public static int choice = 0;
	public static String bar(){
		if(choice == 1){
			return "chen";
		}else {
			return "xiong";
		}
	}
	public static void main(String[] args) {
		System.out.println(HELLO[1]);
	}
}

