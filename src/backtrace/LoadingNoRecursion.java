package backtrace;

/**
 * User: ${wa7chen}
 * Time: 上午9:16
 */
//迭代回溯法
public class LoadingNoRecursion {


	public static int maxLoading(int[] w,int c,int[] bestx){
		int i = 1;
		int n = w.length - 1;
		int[] x = new int[n + 1];
		int bestw = 0;
		int cw = 0;
		int r = 0;

		for(int j = 1; j <= n;j++){
			r += w[j];
		}

		//搜索子树
		while(true){
			while(i <= n && cw + w[i] <= c){
				//进入左子树
				r -= w[i];
				cw += w[i];
				x[i] = 1;
				i++;
			}

			if(i > n){
				//到达叶子节点
				for(int j = 1;j <= n;j++){
					bestx[j] = x[j];
				}
				bestw = cw;
			}

			else{
				//进入右子树
				r -= w[i];
				x[i] = 0;
				i++;
			}

			while(cw + r <= bestw){
				//剪枝回溯
				i--;
				while(i > 0 && x[i] == 0){
					//从右子树返回
					r += w[i];
				}
				if(i ==0 ) return bestw;

				//进入右子树
				x[i] = 0;
				cw -= w[i];
				i++;
			}
		}
	}
}
