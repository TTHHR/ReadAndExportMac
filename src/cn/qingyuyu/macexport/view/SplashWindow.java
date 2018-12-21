package cn.qingyuyu.macexport.view;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SplashWindow {
	 JFrame frame=null;
	public void show()
	{
		
		// 创建JFrame
	     frame = new JFrame("java 串口");
	    
	    
	    ImageIcon imageIcon = new ImageIcon("src/cn/qingyuyu/macexport/view/images/background.png"); 
	    JLabel backImage = new JLabel(); 
	    backImage.setIcon(imageIcon);
	    
	    frame.add(backImage);
	    
	    // 设置尺寸
	    frame.setSize(320,320);
	    // JFrame在屏幕居中
	    frame.setLocationRelativeTo(null);
	    
	    
	    // 显示JFrame
	    frame.setVisible(true);
		
	}
	
	public void close()
	{
		if(frame!=null)
			frame.dispose();
		
	}
	
	
}
