package hanoi;

public class Hanoi {
	public static void hanoi(int n ,char a,char b,char c){
		if(n == 1){
			move(1,a,c);
		}
		else{
			hanoi(n - 1,a,c,b);
			move(n,a,c);
			hanoi(n - 1,b,a,c);
		}
	}
	public static void move(int n ,char a,char b){
		System.out.println("°Ñ" + n + "ºÅ£¬´Ó" + a + "ÒÆµ½" + b);
	}

	public static void main(String[] args) {
		hanoi(3,'a','b','c');
	}

}
