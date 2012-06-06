package thinkInJava.io;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-5-12
 * Time: 下午2:20
 */
public class BufferTest {
	public static void main(String[] args) {
		ByteBuffer bb = ByteBuffer.allocate(32);
		CharBuffer cb = bb.asCharBuffer();
		String content = cb.put("hello").put("hello").flip().toString();
		System.out.println(content);
	}
}
