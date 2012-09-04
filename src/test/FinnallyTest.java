package test;

/**
 * User: ${wa7chen}
 * Time: AM12:30
 */
//finally make sure that every time the codes in try statement run,
//the codes in finally block will be run 
public class FinnallyTest {
	private static int count = 0;
	public static void main(String[] args) {
		while (true){
			try{
				if(count++ == 0){
					throw new Exception();
				}
				System.out.println("no exception!"+ " " + count);
			}catch (Exception e){
				System.out.println("caugt exception!"+ " " + count);
			}finally{
				System.out.println("in finnally case"+ " " + count);
				if(count == 2 )break;
			}

		}
	}
}
