package chessBoard;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 棋盘覆盖问题
 * User: ${wa7chen}
 * Date: 11-3-14
 * Time: 下午3:53
 */
public class ChessBoard {
	/*
	tile为全局变量，表示L型骨牌的序号，
	 */
	static int tile ;
	/*
	board 表示棋盘,用二维数组表示
	 */
	static int[][] board = new int[4][4];
	/*
	棋盘覆盖的方法，用递归实现
	2^size表示该棋盘的边长
	dr , dc 表示特殊方格的序号
	tr,tc表示左上角方格的序号
	 */
	public static void chessBoard(int tr,int tc,int dr,int dc,int size){
		/*
		size为1时是临界条件。这是的棋盘就只剩下一个方块
		 */
		if(size == 1) return;
		else {
			/*
			s表示棋盘分割后的长度
			t表示此时的L型骨牌的序号
			 */
			int s = size / 2;
			int t = tile++;

			//覆盖左上焦的棋盘
			//如果特殊方格在左上角
			if(dr < tr + s && dc < tc + s ){
				//继续分割
				chessBoard(tr,tc,dr,dc,s);
			}
			//如果特殊方格不在，哪么在右下角放置骨牌
			else{
				board[tr + s - 1][tc + s - 1] = t;
				//覆盖其余方格,此时特殊方格是刚刚放置骨牌的位置
				chessBoard(tr,tc,tr + s - 1 ,tc + s - 1,s);
			}

			//覆盖右上焦的棋盘
			//如果特殊方格在右上角
			if(dr < tr +  s && dc >= tc + s ){
				//继续分割,此时需要改变左上角位置的坐标（右上角棋盘的左上角）
				chessBoard(tr,tc + s,dr,dc,s);
			}
			//如果特殊方格不在，哪么在左下角放置骨牌
			else{
				//注意此时左下角的坐标 不是相同的，很容易弄错
				board[tr + s - 1][tc + s ] = t;
				//覆盖其余方格
				chessBoard(tr,tc + s,tr + s -1,tc + s,s);
			}

			//覆盖左下角的棋盘
			//如果特殊方格在左下角
			if(dr >= tr + s  && dc < tc + s ){
				//继续分割,此时需要改变左上角位置的坐标
				chessBoard(tr + s,tc,dr,dc,s);
			}
			//如果特殊方格不在，哪么在右上角放置骨牌
			else{
				board[tr + s ][tc + s - 1 ] = t;
				//覆盖其余方格
				chessBoard(tr + s,tc,tr + s ,tc + s - 1,s);
			}

			//覆盖右下角角的棋盘
			//如果特殊方格在右下角
			if(dr >= tr + s  && dc >= tc + s ){
				//继续分割,此时需要改变左上角角位置的坐标
				chessBoard(tr + s,tc + s,dr,dc,s);
			}
			//如果特殊方格不在，哪么在左上角放置骨牌
			else{
				board[tr + s ][tc + s ] = t;
				//覆盖其余方格
				chessBoard(tr + s,tc + s,tr + s ,tc + s,s);
			}
		}


	}

	//生成棋盘的方法
	public static int[] getBoard(){
		int size = 4;
		int [] location = new int[2];
		Random random = new Random();
		//随即生成特殊方格的位置
		int row = random.nextInt(size);
		int col = random.nextInt(size);
		//将特殊位置的坐标储存，作为返回值返回
		location[0] = row;
		location[1] = col;

		for(int i = 0; i < size;i++){
			for(int j = 0 ;j < size;j++){
				if(i == row && j == col) board[i][j] = -1;
				else{
					board[i][j] = 0;
				}
			}
		}
		return location;
	}
	/*
	显示棋盘的方法
	 */
	public static void showBoard(int[][] boards){
		for(int i = 0 ; i < boards.length;i++){
			for(int j = 0 ;j < boards.length;j++){
				System.out.print(" " + boards[i][j]);
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		int[] location = getBoard();
		System.out.println("原棋盘");
		showBoard(board);

		chessBoard(0,0,location[0],location[1],board.length);
		System.out.println("放置后的棋盘");
		showBoard(board);
	}
}
