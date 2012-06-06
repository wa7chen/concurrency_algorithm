package thinkInJava;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-3-20
 * Time: 上午7:20
 */
class Game{
	Game(int i){
		System.out.println("Game constructor");
	}
	private final  void go(){
		System.out.println("Game go !");
	}
}

class BoardGame extends Game{
	BoardGame(int i ){
		//这里会默认调用超类的默认构造方法，如果没有就必须显示的调用超类的有参构造方法
		super(i);
		System.out.println("BoardGame constructor");
	}
	// 这里根本不是覆盖父类的go方法，private权限你根本看不到
	private final void go(){
		System.out.println("BoardGame go !");
	}

}

public class Chess extends BoardGame{
	Chess(){
		super(1);
		System.out.println("Chess constructor");
	}
	private void play(){
		System.out.println("let's play chess!");
	}

	public static void main(String[] args) {
		BoardGame chess = new Chess();

		//chess.play(); 这里虽然实例化的是chess对象，但是声明中是一个BoardGame对象，所有不能调用chess独有的play方法 
	}
}
