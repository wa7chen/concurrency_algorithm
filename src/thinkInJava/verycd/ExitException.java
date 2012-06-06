package thinkInJava.verycd;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-5-19
 * Time: 下午2:58
 */
public class ExitException extends RuntimeException{
	public ExitException() {
	}

	public ExitException(String message) {
		super(message);
	}

	public ExitException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExitException(Throwable cause) {
		super(cause);
	}
}
