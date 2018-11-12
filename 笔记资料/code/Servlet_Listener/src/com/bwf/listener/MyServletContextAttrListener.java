package com.bwf.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

public class MyServletContextAttrListener implements ServletContextAttributeListener {

	@Override
	public void attributeAdded(ServletContextAttributeEvent scae) {
		System.out.println("添加了一个属性");
		// 获得域对象
		ServletContext context = scae.getServletContext();
		// 添加的属性名
		String name = scae.getName();
		// 添加的值
		Object value = scae.getValue();

		System.out.println("name = " + name + " value = " + value);
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent scae) {
		System.out.println("删除了一个属性");

		// 添加的属性名
		String name = scae.getName();
		// 原来的值
		Object value = scae.getValue();

		System.out.println("name = " + name + " value = " + value);
	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent scae) {
		System.out.println("修改了一个属性");
		// 添加的属性名
		String name = scae.getName();
		// 原来的值
		Object value = scae.getValue();
		
		// 新的值
		Object value2 = scae.getServletContext().getAttribute(name);

		System.out.println("name = " + name + " value = " + value);
		System.out.println("value2 = " + value2);
	}

}
