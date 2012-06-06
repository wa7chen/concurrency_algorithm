package String;

/**
 * User: ${wa7chen}
 * Time: PM3:23
 */

/*
use javap -c ,see the result.
compare and analysis them;
 */
public class UserJavapTestString {
	public String implicit(){
		String bar = new String();
		for(int i = 0;i<2;i++){
			bar += "abc";
		}
		return bar;
	}
	public String explict(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i<2;i++){
			sb.append("abc");
		}
		return sb.toString();
	}
}
