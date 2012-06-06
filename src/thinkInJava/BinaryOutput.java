package thinkInJava;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-4-19
 * Time: 上午11:09
 * 二进制的文件写入和读取
 */
public class BinaryOutput {
	public static void main(String[] args) {
		try {
			DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("data.dat")));
			DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream("data.dat")));
			out.writeInt(21);
			out.writeInt(-1);
			out.write("hello world".getBytes());

			out.flush();
			byte[] bytes = new byte[100];
			if(in.available() != -1){
				in.read(bytes);
				System.out.println("ok");
				System.out.println(in.readInt());
				System.out.println("bytes ---" + bytes);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}finally {
		}
	}
}
