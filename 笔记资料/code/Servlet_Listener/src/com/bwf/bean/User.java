package com.bwf.bean;

import java.io.Serializable;

import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;

public class User implements HttpSessionBindingListener, HttpSessionActivationListener, Serializable{

	private String name;
	private int age;
	
	public User() {
		super();
	}
	
	public User(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "User [name=" + name + ", age=" + age + "]";
	}

	// �� - ���ŵ�sessionȥ��
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		
		System.out.println("�ҽ�" + name + ", �ұ� " + event.getSession().getId() + " session����");
		System.out.println(hashCode());
	}

	// ��� - ��session�б��Ƴ�
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		System.out.println("�ҽ�" + name + ", �ұ� " + event.getSession().getId() + " session�����");
	}

	// �ۻ�
	@Override
	public void sessionWillPassivate(HttpSessionEvent se) {
		System.out.println("�ҽ�" + name + ", �һ������!");
	}

	// �
	@Override
	public void sessionDidActivate(HttpSessionEvent se) {
		System.out.println("�ҽ�" + name + ", ���ֻ���!");
	}
	
	
	
}
