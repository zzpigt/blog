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

	// 绑定 - 被放到session去了
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		
		System.out.println("我叫" + name + ", 我被 " + event.getSession().getId() + " session绑定了");
		System.out.println(hashCode());
	}

	// 解绑 - 从session中被移除
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		System.out.println("我叫" + name + ", 我被 " + event.getSession().getId() + " session解绑了");
	}

	// 钝化
	@Override
	public void sessionWillPassivate(HttpSessionEvent se) {
		System.out.println("我叫" + name + ", 我会回来的!");
	}

	// 活化
	@Override
	public void sessionDidActivate(HttpSessionEvent se) {
		System.out.println("我叫" + name + ", 我又活了!");
	}
	
	
	
}
