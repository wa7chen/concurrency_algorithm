/**
 * User: ${wa7chen}
 * Time: AM11:35
 */
final public class TernaryOperator {
	private int temp(){
		return (true?null:0);
	}

	private int same(){
		if(true){
			return new Integer(null);//NumberFormatException  runtime
			//return null will compile error
			//this will antobox the null to Interger. 
		}
		else {
			return 0;
		}
	}

	public static void main(String[] args) {
		TernaryOperator to = new TernaryOperator();
//		to.temp();
		to.same();
	}
	
}
