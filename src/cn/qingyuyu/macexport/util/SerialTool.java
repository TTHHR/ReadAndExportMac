package cn.qingyuyu.macexport.util;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Enumeration;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
public class SerialTool {

	private static SerialTool st=null;
	
	private SerialPort serialPort=null;
	
	private SerialTool() {
		
	}
	
	public static SerialTool getInstance()
	{
		if(st==null)
			st=new SerialTool();
		
		return st;
	}
	
	public ArrayList<String>getAvailablePort(){
		
		Enumeration<CommPortIdentifier> portList=CommPortIdentifier.getPortIdentifiers();
		
		ArrayList<String>portNameList=new ArrayList<>();
		
		while(portList.hasMoreElements())
		{
			portNameList.add(portList.nextElement().getName());
		}
		
		return portNameList;
	}
	
	public void openPort(String portName,int baudrate) throws NoSuchPortException
	, PortInUseException, UnsupportedCommOperationException {
		
		
			//use name to find port
			CommPortIdentifier cpi=CommPortIdentifier.getPortIdentifier(portName);
			
			//open port by name and 3000ms timeout
			CommPort commPort=cpi.open(portName, 3000);
			
			if(commPort instanceof SerialPort)
			{
				serialPort=(SerialPort) commPort;
				serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8
						, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				
			}
			
	}
	
	
	public boolean sendBByPort(byte[] data)
	{
		boolean b=false;
		OutputStream os=null;
		
		try {
			if(serialPort!=null)
			{
			os=serialPort.getOutputStream();
			os.write(data);
			os.flush();
			b=true;
			}
			else
			{
				System.out.println("serial port not open!");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return b;
	}
	
	public byte[] readFromPort()
	{
		InputStream is=null;
		
		byte[] data=null;
		
		try {
			if(serialPort!=null)
			{
			is=serialPort.getInputStream();
			
			int buffLenth=is.available();
			
			data=new byte[buffLenth];
			
			is.read(data);
			
			}
			else
			{
				data="serial port not open !".getBytes();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return data;
	}
	public char[] readFromPort(int l)
	{
		InputStream is=null;
		
		char[] data=null;
		
		try {
			if(serialPort!=null)
			{
			is=serialPort.getInputStream();
			while(is.available()<14)
			{
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			InputStreamReader isr=new InputStreamReader(is);
			data=new char[l];
			
			isr.read(data);
			
			}
			else
			{
				data="serial port not open !".toCharArray();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return data;
	}
	public void closePort()
	{
		if(serialPort!=null)
			serialPort.close();
	}
	
	
}
