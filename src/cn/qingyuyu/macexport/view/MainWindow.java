package cn.qingyuyu.macexport.view;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.qingyuyu.macexport.util.SerialTool;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

public class MainWindow {
	JFrame frame=null;
	JComboBox portList;
	JTextField baudrateField;
	JButton connectButton;
	JButton exportButton;
	JTextArea dataarea;
	JTextField title;
	public MainWindow() {
		
		frame=new JFrame("防盗MAC读取");
		
		
		frame.setLayout(new FlowLayout());
		
		JPanel settingPanel = new JPanel(new FlowLayout() );
		
		settingPanel.add(new JLabel("选择端口："));
		
		 portList=new JComboBox();   
		
		settingPanel.add(portList);
		
		settingPanel.add(new JLabel("输入波特率："));
		
		 baudrateField = new JTextField(8);
		
		settingPanel.add(baudrateField);
		
		connectButton=new JButton("连接");
		
		connectButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					SerialTool.getInstance().openPort((String)portList.getSelectedItem()
							, Integer.parseInt(baudrateField.getText()));
					
					new Thread(new Runnable() {
						@Override
						public void run() {
							
							while(true)
							{
								try {
									Thread.sleep(100);
									
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								     char[] b=SerialTool.getInstance().readFromPort(14);
								     if(b.length!=0)
								     {
								    	 String s=new String(b);
								    	 System.out.println(s+" L="+s.length());
								    	 if(s.indexOf('\n')==s.length()-1&&s.length()==14)
								    	{
									   if(dataarea.getText().toString().lastIndexOf(s)==-1)
									   {
										  String tmp=dataarea.getText().toString();
										dataarea.append(s);
										dataarea.requestFocus();
										dataarea.select(tmp.length(), dataarea.getText().length());
									   }
									   else
										   System.out.println(s+" 已存在");
								    	}
								     }
								
							}
							
						}
						
					}).start();
					
				} catch (Exception e)
				{
					e.printStackTrace();
					JOptionPane.showMessageDialog(
	                        frame,
	                        "错误:"+e,
	                        "消息标题",
	                        JOptionPane.INFORMATION_MESSAGE
	                );
				}
				finally
				{
					connectButton.setText("连接");
				}
				super.mouseClicked(arg0);
			}
			
		});
		
		settingPanel.add(connectButton );
		
		  exportButton=new JButton("导出excel");
			
			exportButton.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent arg0) {
					try {
						exportExcel();
						java.awt.Desktop.getDesktop().open(new File("./"));
					} catch (Exception e)
					{
						e.printStackTrace();
						JOptionPane.showMessageDialog(
		                        frame,
		                        "错误:"+e,
		                        "消息标题",
		                        JOptionPane.INFORMATION_MESSAGE
		                );
					}
					finally
					{
						
					}
					super.mouseClicked(arg0);
				}
				
			});
			
			settingPanel.add(exportButton );
		
		
		
		frame.add(settingPanel);
		
		 title=new JTextField(40);
		 title.setText("防盗MAC标题");
		
		 frame.add(title);

		JScrollPane sp=new JScrollPane();
		 dataarea = new JTextArea(30,40);
		sp.setViewportView(dataarea);
		sp.setSize(640, 480);
		
		
		frame.add(sp);
		
     
		
		// 设置尺寸
        frame.setSize(640, 640);
        // JFrame在屏幕居中
        frame.setLocationRelativeTo(null);
        
        frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
			SerialTool.getInstance().closePort();
			}
		});
        
        // JFrame关闭时的操作
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}
	
	
	
	
	public void show()
	{
		if(!frame.isVisible())
		{
			ArrayList<String >list=SerialTool.getInstance().getAvailablePort();
			
			for(String s:list)
			{
				portList.addItem(s);
			}
			if(list.size()==0)
			{
				JOptionPane.showMessageDialog(null, "没有可用的端口 ", "警告",JOptionPane.WARNING_MESSAGE);
			}
			
			
        // 显示JFrame
        frame.setVisible(true);
		
		}
	}
	public void exportExcel() throws Exception
	{
		//创建HSSFWorkbook对象(excel的文档对象)
	      HSSFWorkbook wb = new HSSFWorkbook();
	//建立新的sheet对象（excel的表单）
	HSSFSheet sheet=wb.createSheet(title.getText());
	//在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
	HSSFRow row1=sheet.createRow(0);
	//创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
	row1.createCell(0).setCellValue("Number");

	      //设置单元格内容
	row1.createCell(1).setCellValue("Mac   Address");
	
	
	sheet.setColumnWidth(1, 20 * 256);
	
	
	String[] macs=dataarea.getText().toString().split("\n");
	
	
	
	for(int i=1;i<=macs.length;i++)
	{
		//在sheet里创建行
		HSSFRow row2=sheet.createRow(i);    
		      //创建单元格并设置单元格内容
		      row2.createCell(0).setCellValue("NO."+i);
		      row2.createCell(1).setCellValue(macs[i-1]);    
	}
	 
	
	
	
	
	    //输出Excel文件
	      FileOutputStream output=new FileOutputStream(title.getText()+".xls");
	      wb.write(output);
	      output.flush();
	      output.close();
		
	}
	
}
