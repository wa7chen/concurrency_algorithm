package backtrace;

/**
 * User: ${wa7chen}
 * Time: 下午8:41
 */
public class Loading {
	private static int n ;//集装箱数量
	private static int[] w;//集装箱重量数组
	private static int c;//第一艘船的载重量
	private static int cw;//当前载重量
	private static int bestw;//当前最优载重量
	private static int r;//剩余集装箱重量
	private static int[] x;//当前解
	private static int[] bestx;//当前最优解

	public static int maxLoading(int[] ww,int cc,int[] xx){
		n = ww.length-1;
		w = ww;
		c = cc;
		cw = 0;
		bestw  = 0;
		x = new int[n + 1];
		bestx = xx;

		for(int i = 1;i <= n;i++){
			r += w[i];
		}

		//计算最优装载
		backtrace(1);
		return bestw;
	}

	private static void backtrace(int i){
		//搜索第i层节点
		if(i > n ){
			if(cw > bestw){
				for(int j = 1;j <= n;j++)
					bestx[j] = x[j];
				bestw = cw;
			}
			return;
		}

		//搜索子树
		r -= w[i];
		if(cw + w[i] <= c){
			x[i] = 1;
			cw += w[i];
			backtrace(i + 1);
			cw -= w[i];
		}

		if(cw + r > bestw)//进行剪枝
		{
			x[i] = 0;
			backtrace(i+1);
		}

		r += w[i];
	}
}
