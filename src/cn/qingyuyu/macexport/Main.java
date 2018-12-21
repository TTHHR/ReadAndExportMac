package cn.qingyuyu.macexport;


import cn.qingyuyu.macexport.view.MainWindow;
import cn.qingyuyu.macexport.view.SplashWindow;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SplashWindow sw=new SplashWindow();
		
		sw.show();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sw.close();
		
		MainWindow mw=new MainWindow();
		
		mw.show();
		
	}

}
