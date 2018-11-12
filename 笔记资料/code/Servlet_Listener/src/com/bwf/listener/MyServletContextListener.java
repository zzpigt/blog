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
		System.out.println("ServletContext������");
		// ��ô����¼��������
		// sce.getServletContext();
		
		// ����ϵͳ, ÿ��24Сʱ��Ϣһ��
		// ���������0���date
		Calendar time = Calendar.getInstance();
		time.add(Calendar.DATE, 1);
		time.set(Calendar.HOUR, 0);
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);
		
		
		new Timer().scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("���м�Ϣ��");
			}
		}, time.getTime(), 4000);
		
	}
	
	// ���24Сʱ�Ժ��ʱ��
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
		System.out.println("ServletContext������");
		
		
		
		
	}

}
