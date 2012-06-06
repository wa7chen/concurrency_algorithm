import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

/**
 * User: ${wa7chen}
 * Time: PM10:12
 */

class SimpleException extends Exception{
	public SimpleException(){

	}
	public SimpleException(String msg){
		super(msg);
	}
}

class LoggingException extends Exception{
	private Logger logger = Logger.getLogger("LoggingException");

	public LoggingException(){
		StringWriter trace = new StringWriter();
		printStackTrace(new PrintWriter(trace));
		logger.severe(trace.toString());

	}
}

public class ExceptionTest {
	public void g() throws SimpleException {
		System.out.println("throws from g");
		throw new SimpleException("from g");
	}
	public void f() throws SimpleException {
		System.out.println("throws from f");
		throw new SimpleException("from f");
	}

	public static void main(String[] args) throws SimpleException {
		ExceptionTest et = new ExceptionTest();
		try {
			et.g();
		} catch (SimpleException e) {
			//if param is not System.out,then error info will be print by system.err
			//if use Sysout.out ,the output will be redirected
			e.printStackTrace(System.out);
			//
			throw new SimpleException("hello exception!");
		}

		try {
			et.f();
		} catch (SimpleException e) {
			e.printStackTrace();
		}

		try{
			throw new LoggingException();
		} catch (LoggingException e){
			System.err.println("Caugt " + e);
		}
	}

}
