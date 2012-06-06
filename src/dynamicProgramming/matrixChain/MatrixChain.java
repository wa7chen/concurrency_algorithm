package dynamicProgramming.matrixChain;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-3-30
 * Time: 下午10:52
 */
/*
用动态规划处理矩阵连乘问题 时间上界是O（n^3)的，空间是O（n^2)的
 */
public class MatrixChain {
	/*
	p代表矩阵，m数组的每一个元素代表i到j的最小乘次数，s数组的元素表示对i到j划分的下标
	 */
	public static  void matrixChain(int[] p,int[][] m,int[][] s ){
		int n = p.length;
		for(int x = 1;x <= n;x++){
			m[x][x] = 0 ;
		}
		//r表示链长，从底层开始往上进行
		for(int r = 2;r <=  n;r++){
			for(int i = 1;i <= n - r + 1;i++ ){
				int j = i + r - 1;
				//在第一个元素处划分，少一项 m[i][i] ，因为它==0
				m[i][j] = m[i + 1][j] + p[i - 1] * p[i] * p[j];
				s[i][j] = i;
				//进行划分
				for(int k = i + 1;k < j;k++){
					/*p[i - 1] * p[k] * p[j]表示：A[i][k]*A{k+1][j] 在k处划分
					*然后再求子模块的划分，将三者的计算次数相加
					*
					*/
					int t = m[i][k] + m[k + 1][j] + p[i - 1] * p[k] * p[j];
					if(t < m[i][j]){
				 		m[i][j] = t ;
						s[i][j] = k ;
					}
				}
			}
		}
	}

	//找出最优计算次序
	public static void traceBack(int[][] s ,int i ,int j){
		if(i == j ) return;
		traceBack(s,i,s[i][j]);
		traceBack(s,s[i][j]+1,j);
		System.out.println("矩阵" + i + "," + s[i][j] + "and " + (s[i][j] + 1) + "," + "j");
	}

	public static void main(String[] args) {
		int[] a = {30,35,15,5,10,20,25};
		int[][] m = new int[a.length][a.length];
		int[][] s = new int[a.length][a.length];
		matrixChain(a,m,s);
		traceBack(s,1,5);
	}
}
