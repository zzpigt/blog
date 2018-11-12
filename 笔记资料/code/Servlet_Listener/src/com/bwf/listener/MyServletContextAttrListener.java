package com.bwf.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

public class MyServletContextAttrListener implements ServletContextAttributeListener {

	@Override
	public void attributeAdded(ServletContextAttributeEvent scae) {
		System.out.println("�����һ������");
		// ��������
		ServletContext context = scae.getServletContext();
		// ��ӵ�������
		String name = scae.getName();
		// ��ӵ�ֵ
		Object value = scae.getValue();

		System.out.println("name = " + name + " value = " + value);
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent scae) {
		System.out.println("ɾ����һ������");

		// ��ӵ�������
		String name = scae.getName();
		// ԭ����ֵ
		Object value = scae.getValue();

		System.out.println("name = " + name + " value = " + value);
	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent scae) {
		System.out.println("�޸���һ������");
		// ��ӵ�������
		String name = scae.getName();
		// ԭ����ֵ
		Object value = scae.getValue();
		
		// �µ�ֵ
		Object value2 = scae.getServletContext().getAttribute(name);

		System.out.println("name = " + name + " value = " + value);
		System.out.println("value2 = " + value2);
	}

}
