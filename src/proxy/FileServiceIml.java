package proxy;

/**
 * @author <a href="mailto:chenxiong_ch.pt@taobao.com">chenxiong</a>
 * @since 12-8-31 AM10:13
 */
public class FileServiceIml implements FileService {
	@Override
	public void rename(String name) {
		System.out.println("rename begin");
		try {
			Thread.currentThread().sleep(40);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void delete(int id) {
		System.out.println("delete begin");
		try {
			Thread.currentThread().sleep(40);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
