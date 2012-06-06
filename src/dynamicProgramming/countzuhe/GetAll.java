package dynamicProgramming.countzuhe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-5-17
 * Time: 下午10:53
 */
/*
第一个输入的参数是总共缺少的金额money
第二个参数是所有的交易量records
接下来的records个参数是每笔的金额

要求输出所有可能的组合
 */
public class GetAll {
	public static ArrayList<List<Integer>> allPossible = new ArrayList<List<Integer>>();
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int money = input.nextInt();
		int records = input.nextInt();
		List<Integer> nums = new ArrayList<Integer>();
		for(int i = 0;i < records;i++){
			nums.add(input.nextInt());
		}
		Collections.sort(nums);
		//开始找出所有可能情况
		getAll(money,0,nums,0,new ArrayList<Integer>());
		System.out.println(allPossible.size());
		System.out.println(allPossible);
	}

	/**
	 * 由递归实现在给定情况下查找组合该金额的所有可能
	 * @param money 目的钱数
	 * @param index 此时探测到链表元素的下标
	 * @param nums 每笔交易的钱数，由用户输入，不会改变，
	 * @param currentMoney 此时已经算出的钱数
	 * @param currentNums 现阶段对钱的组合
	 */
	public static void getAll(int money,int index,List<Integer> nums,int currentMoney,List<Integer> currentNums){

		for(int i = index;i < nums.size();i++){
			int iMoney = nums.get(i);
			//构造一个暂时链表将i的数放进去,同时将currentMoney加上此时的值
			List<Integer> temp = new ArrayList<Integer>(currentNums);
			temp.add(iMoney);

			int now = currentMoney + iMoney;
			//如果超过，那么直接舍去
			if(now > money) return;
			//如果等于，将temp添加到allPossible中
			else if(now == money){
				allPossible.add(temp);
				return;
			}
			//如果小于，继续递归查找

			for(int j = i + 1;j < nums.size();j++){
				//构造一个新的链表，那么才能不同组合间排除干扰，使每种组合情况都拥有一个独立的链表存储
				List<Integer> newList = new ArrayList<Integer>(temp);
				int jMoney = nums.get(j);
				newList.add(jMoney);
				/*
				这里的边界情况是，如果加上最后一个元素就达到额定金额，如果在这里不判断，在下一层判断，那么数组会越界
				因为这里传过去的是j+1
				 */
				if(now + jMoney == money)
					allPossible.add(newList);
				getAll(money,j + 1,nums,now + jMoney,newList);
			}
		}
	}
}
/*
测试用例
5
5
1 2 3 4 5

5
4
1 2 4 5

5
6
1 2 2 3 3 4
 */
