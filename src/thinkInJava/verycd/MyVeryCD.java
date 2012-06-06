package thinkInJava.verycd;

import sun.awt.windows.ThemeReader;

import java.io.*;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Date: 11-5-18
 * Time: 下午2:28
 */
/*
一个实现多线程上传下载的组件
用nio实现
 */

/**
 * downpath 是客户端得在客户端的下载目录,
 */
public class MyVeryCD implements Runnable{
	public static final  String DOWN_PATH = "D:\\ebook";
	public static final String UP_PATH = "D:\\WA7TEMP";
	//默认的缓冲大小
	public static final int BSIZE = 1024;
	private String[] files ;
	private String username ;
	private boolean died = false;
	public MyVeryCD(){
		//如果构造器有多行代码，记得调用其他构造器的方法要放在第一行
		this("null");
	}
	public MyVeryCD(String username){
		this.username = username;
		File localFile = new File(DOWN_PATH);
		files = localFile.list();
	}
//显示所有文件的名称及序号
	public void showAllFiles(){
		for(int i = 0;i < files.length;i++){
			System.out.println(i + ":" + files[i]);
		}   
	}
//这里必须要用到锁机制,
	public synchronized void download(){
		Scanner input = new Scanner(System.in);
		showAllFiles();
		System.out.println(username + "请输入你要下载的序号");
		int no = input.nextInt();
		File fromFile = new File(DOWN_PATH + File.separator + files[no]);
		System.out.println(fromFile);
		download(fromFile,files[no]);
	}
	public synchronized void download(File file,String fileName){
		try {
			//取得输入流的通道
			FileChannel downChannel = new FileInputStream(file).getChannel();
			//构造一个缓冲区
			ByteBuffer buffer = ByteBuffer.allocate(BSIZE) ;
			//取得输出流的通道
			FileChannel outChannel = new FileOutputStream(UP_PATH + File.separator + fileName).getChannel();

			while(downChannel.read(buffer) != -1){
				//准备开始写
				buffer.flip();
				outChannel.write(buffer);
				//准备读
				buffer.clear();
			}
			System.out.println(fileName + "下载完成");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("文件没有找到");
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public synchronized void upload(){
		
	}

	public void run() {
		//检查线程是否手动中断---用户退出
		try{
			//用变量来控制线程的结束
			while(!died){
				Scanner input = new Scanner(System.in);
				System.out.println( username +" 请输入：\n 1--下载 \n 2.--上传 \n 0--退出");
				int option = input.nextInt();
				switch(option){
					case 1:download();break;
					case 2:upload();break;
					case 0:died = true;
				}
				//线程休眠100毫秒，让操作系统重新分配优先级
				Thread.sleep(100);
			}
			System.out.println("用户:" + username + "退出");
		}catch (InterruptedException e){
			e.printStackTrace();
			//抛出一个自定义的异常，用来在上层应用的时候发现用户的退出

			throw new ExitException(username + "quit");
		}
	}

	public  int getActiveThread() {
		return 0;
	}

	public static void main(String[] args) {
		try{
			Thread verycd1 = new Thread( new MyVeryCD("A"));
			verycd1.start();
			Thread verycd2 = new Thread(new MyVeryCD("B"));
			verycd2.start();

		}catch(ExitException e){
			//这里没有截获到异常？？？
			System.out.println(e.getMessage());
		}
	}
}
