package com.bwf.listener;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sun.beans.util.Cache;

public class MyServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("ServletContext创建了");
		// 获得触发事件的域对象
		// sce.getServletContext();
		
		// 银行系统, 每隔24小时计息一次
		// 想获得明天的0点的date
		Calendar time = Calendar.getInstance();
		time.add(Calendar.DATE, 1);
		time.set(Calendar.HOUR, 0);
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);
		
		
		new Timer().scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("银行计息拉");
			}
		}, time.getTime(), 4000);
		
	}
	
	// 获得24小时以后的时间
//	public static Date get24(){
//		try {
//			Thread.sleep(24 * 60 * 1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		return new Date();
//	}
	

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("ServletContext销毁了");
		
		
		
		
	}

}
