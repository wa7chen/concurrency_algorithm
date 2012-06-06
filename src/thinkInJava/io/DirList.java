package thinkInJava.io;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-4-18
 * Time: 下午4:31
 *
 * io初步
 */
public class DirList {
	public static void main(String[] args) {
		File path = new File(".");
		//System.out.println("parent" + path.getParent()); 这里是null
		String[] list;
		if(args.length== 0){
			list = path.list();
		}else{
			list = path.list(new DirFilter(args[0]));
		}
		Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
		for(String dirItem : list){
			File file = new File(dirItem);
			if(file.isDirectory()){
				//file.
			}
			System.out.println(dirItem );
		}
		//这里输出的用户工作目录 D:\MyeclipseWorkSpace\algorithm
		System.out.println(System.getProperty("user.dir"));
	}

}

class DirFilter implements FilenameFilter {
	private Pattern pattern;

	public DirFilter(String regex){
		pattern = Pattern.compile(regex);
	}

	public boolean accept(File dir, String name) {
		return  pattern.matcher(name).matches();
	}
}