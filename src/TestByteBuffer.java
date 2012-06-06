import java.nio.ByteBuffer;

/**
 * User: ${wa7chen}
 * Time: PM11:45
 */
public class TestByteBuffer {
	public static void main(String[] args) {
		byte[] data = new byte[16];
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.putShort(0, (short)0x1234);
		buf.putInt(2,0x12345678);
		buf.putLong(8,0x1122334455667788L);

		for(int i = 0;i < data.length;i++){
			System.console().printf("index %2d = %02x\n", i, data[i]);
		}
	}
}
