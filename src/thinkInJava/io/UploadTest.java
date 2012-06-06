package thinkInJava.io;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-5-1
 * Time: 上午10:23
 */
public class UploadTest  {
	public static void main(String[] args) {
		BufferedReader in ;
		BufferedWriter out;
		try {
			in = new BufferedReader(new FileReader("foo.in"));
			out = new BufferedWriter(new FileWriter("bar.txt"));
			while(in.readLine() != null){
				out.write(in.readLine());
			}
			in.close();
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

}
