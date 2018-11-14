---
title: 【JAVAEE知识】监听器Listener与过滤器Filter
date: 2018-11-14 21:42:45
tags: [JAVA,JavaEE,技术贴]
description: "这两种技术在后面的框架开发中用的并不多了，尤其是过滤器，基本被SpringMVC中的拦截器取代。但是这不代表它们不重要，热爱编程的伙伴更是需要这些知识打好基础。"

---
**JavaEE**已知的就有十几门规范（大概13种），而我现在掌握的主要就**Servlet技术**和**JSP技术**。

其中**Servlet规范**有三个技术点：

- Servlet  
- Listener  ---监听器
- Filter	---过滤器

Servlet之前已经详细介绍过了，使用起来也非常容易，接受客户端传上来的数据，调用业务逻辑层处理数据，最后回送响应给客户端或者转发给其它资源。而这里主要介绍后面两种技术。

## 监听器 Listener

监听器就是监听某个对象的的状态变化的组件，作为监听器，它的行为顾名思义就是监听某个目标的动作，专业一点的相关概念：

- 事件源：被监听的对象（主要是三个域对象`request`/`session`/`servletContext`）
- 监听器：监听事件源对象（事件源对象的状态的变化都会触发监听器）
- 注册监听器：将监听器与事件源进行绑定
- 响应行为：监听器监听到事件源的状态变化时**所涉及的功能代码**


### 监听器的种类

 ![监听器种类]()

 上面的图很清晰有六种监听器，对应着ServletContext域、HttpSession域和ServletRequest域三个域对象，而每个域对象都对应有两个监听接口，分别监听域对象的创建和销毁和域对象内的属性变化。

特别注意是这里只有对上面三种域对象有设置监听接口，比我们了解的四种域对象还少了一个作用范围最小的pageContext域对象。这从它的作用范围就很容易理解为什么它被排挤出去了。

***除了上面说的六个监听器接口，还有两个也很重要的监听器接口这里必须要介绍一下，它们也叫对象感知监听器。***

- `HttpSessionBindingListener`---绑定与解绑的监听器
- `HttpSessionActivationListener`---钝化与活化的监听器

### 监听器的编写（六个）

>这里以监听ServletContext域为例，其它监听器可以参考以下示例，实现接口的方法是一样的，配置也是一样的。

这里先说前面六个最重要的监听器接口，它们作为监听三大域对象的创建与销毁的监听器，编写步骤（重点）可以说能分为三步：

***第一步：编写一个监听器类去实现监听器接口；这里为了贴的代码统一，第二步:覆盖覆盖监听器的方法，也可以在下面的示例代码体现***

```java
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

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("ServletContext销毁了");		
	}
}
```

>上面代码中有一段是模拟银行系统计息的方法

上面的代码是为了监听ServletContext域对象的创建和销毁而写的一个类，现在这个类创建出来，可以说就是我们手上的一个拥有明确作用的监听器了。为了对比，我这里写放上一个对ServletContext域对象的属性变化进行监听的监听器。

```java
public class MyServletContextAttrListener implements ServletContextAttributeListener {
	@Override
	public void attributeAdded(ServletContextAttributeEvent scae) {
		System.out.println("添加了一个属性");
		// 获得域对象
		ServletContext context = scae.getServletContext();
		// 获得添加的属性名
		String name = scae.getName();
		// 获得添加的值
		Object value = scae.getValue();
		System.out.println("name = " + name + " value = " + value);
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent scae) {
		System.out.println("删除了一个属性");
		// 获得被删除的属性名
		String name = scae.getName();
		// 获得被删除前原来的值
		Object value = scae.getValue();
		System.out.println("name = " + name + " value = " + value);
	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent scae) {
		System.out.println("修改了一个属性");
		// 获得被修改的属性名
		String name = scae.getName();
		// 获得被修改前原来的值
		Object value = scae.getValue();
		// 获得修改后新的值
		Object value2 = scae.getServletContext().getAttribute(name);
		System.out.println("name = " + name + " value = " + value);
		System.out.println("value2 = " + value2);
	}
}
```

和前面一个监听创建和销毁的监听器不同的是，这里实现了三个方法，实现了对ServletContext域对象中属性的各种变化都能进行详细的监控，基本这个域对象对于我们来说没有了‘秘密’。

>监听器拿到手上，要给服务器安装上，也就是我们常说的注册这个监听器

***第三步：需要在*web.xml*中进行配置---注册监听器***

```xml
<listener>
    <listener-class>com.bwf.listener.MyServletContextListener</listener-class>
</listener>
<listener>
  	<listener-class>com.bwf.listener.MyServletContextAttrListener</listener-class>
</listener>
```

这最后一步就是把这个监听器注册上，在`web.xml`文件中配置，很简单，一个监听器基本就三行代码就能配置好。但是就是这一步很容易忘记，所以注意记住监听器的编写步骤。

### 监听器的测试

>为了能以后回头好复习，这里也贴上监听器的测试方法。当然这是为了我这样的菜鸟准备的。

对于域对象我们要记住的是它们基本上都有以下三个方法，通过这三个方法就能监听这个域对象的属性变化。

- setAttribute(name,value) ---触发添加属性的监听器的方法
- getAttribute(name)---触发修改属性的监听器的方法
- removeAttribute(name)---触发删除属性的监听器的方法



### 对象感知监听（两个）

这里我把这种对象感知类型的监听器放在单独一小节介绍，也说明了它们的特殊。对我来说它的特殊更多的是，我担心很长一段时间的用不到它们，需要它们的时候想不到它们。

所以这里我需要单独提出来放在这，时刻提醒自己它们的存在。它们不是压箱底，而是就在我们手边。

好了，下面说说它们到底有多特殊，多神奇。这种神奇也让它们容易被选择性遗忘。

第一点，这两个监听器只跟`session`域对象有关。

第二点，它们不是监听域对象的变化的，而是监听被放到域对象中的那个对象的状态的。


既然是监听对象状态的，那么我们想想，别绑定到域对象的对象有几种状态：

- **绑定状态**：就一个对象被放到session域中
- **解绑状态**：就是这个对象从session域中移除了
- **钝化状态**：是将session内存中的对象持久化（序列化）到磁盘
- **活化状态**：就是将磁盘上的对象再次恢复到session内存中

一共4中状态，我们只有两个监听器接口，它们各自监听的状态也很显而易见：

- 绑定与解绑的监听器---`HttpSessionBindingListener`
- 钝化与活化的监听器---`HttpSessionActivationListener`


绑定和解绑的概念很容易理解，钝化和活化又是什么情况呢？它们意思上面有提到，重要的是它们有什么作用。

说到了这里，这个监听对象的钝化和活化的监听器就能成为菜鸟显摆的一个知识点了。我们常常听说服务器优化，都知道这是高手大牛的‘大招’，功力不够的菜鸟是耍不出来的。那么现在我们算是会了半招，因为钝化与活化的监听器可以说是对服务器的一种优化。

当用户量多的时候, 多数的用户并没有操作session, 但是它是占用内存的，对服务器是存在压力的。所以可以对这些用户的session进行钝化(持久化到磁盘) 减少内存的占用，稍稍减轻服务器的压力。
  
了解上面说的这些，怎么用这两个监听器呢？与上面六个监听器不同的是，这里监听对象不再是域对象，所以编写步骤不同（以监听User类为例，是在User类上添加接口）：


***仅需一步***：对需要监听的对象添加监听器接口，还有别忘了要序列化。 

```java
public class User implements HttpSessionBindingListener, HttpSessionActivationListener, Serializable{
	private String name;
	private int age;
	public User() {super();}
	public User(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public int getAge() {return age;}
	public void setAge(int age) {this.age = age;}
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
		System.out.println("我叫" + name + ", 我被钝化了!");
	}
	// 活化
	@Override
	public void sessionDidActivate(HttpSessionEvent se) {
		System.out.println("我叫" + name + ", 我又活化了!");
	}	
}
```

>可以通过配置文件指定对象钝化时间或者对象多长时间不用被钝化：
     在META-INF下创建一个context.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Context>
	<Manager className="org.apache.catalina.session.PersistentManager" maxIdleSwap="1">
		<Store className="org.apache.catalina.session.FileStore" directory="bwf" />
	</Manager>
</Context>
```

`Manager`标签中的一些属性可以进行设置：
- maxIdleSwap ：设置session中的对象多长时间不使用就钝化
- directory ：设置钝化后的对象的文件写到磁盘的哪个目录下

>默认：配置钝化的对象文件在Tomcat：work/catalina/localhost/钝化文件



## 过滤器 Filter

**Filter**是对客户端访问资源的过滤，设置一些条件。如果符合条件就放行，而不符合条件就不放行，并且可以对目标资源访问前后进行逻辑处理，这就涉及到AOP的思想，暂且不提，后面介绍框架的时候会着重说到。

好了，有了上面介绍过Listener的基础，这里我们可以快速入门。Filter的编写步骤也可以分为三步：

***第一步：编写一个过滤器的类实现Filter接口，紧接着就是第二步：实现接口中尚未实现的方法(着重实现doFilter方法)***
 
```java
 public class LoginFilter implements Filter {
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = ((HttpServletRequest)request);
		Object user = httpRequest.getSession().getAttribute("user");
		String url = httpRequest.getRequestURL().toString();
			
		// 如果登录了就放行, 或者访问的就是登录页面
		if(user != null || url.contains("login.jsp")){
			System.out.println("放行!");
			chain.doFilter(request, response);
		}else{
			System.out.println("没登录 跳转!");
			// 如果没登录, 就重定向到登录页面
			((HttpServletResponse)response).sendRedirect("/Servlet_Filter/user/login.jsp");
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}
	public void destroy() {
	}
}

```

>代码中有一段是演示没有登录的情况下，访问其他资源将会被过滤重定向到登入界面

上面的这段代码中，实现的Filter借口中三个方法：

- `init(Filterconfig)`：代表Filter对象初始化方法，Filter对象创建时执行
- `doFilter(ServletRequest,ServletResponse,FilterCha)`：代表Filter执行过滤的核心方法，如果某资源在已经被配置到这个Filter进行过滤的话，那么每次访问这个资源都会执行doFilter方法
- `destory()`：代表是Filter销毁方法，当Filter对象销毁时执行该方法

这三个方法也可以解释**Filter**的生命周期：

- Filter何时创建：服务器启动时就创建该Filter对象
- Filter何时销毁：服务器关闭时Filter销毁

如果需要对上面三个方法有更多的了解，那么就必须知道三个方法中参数的作用了：

1）***init(FilterConfig)***：其中参数config代表该Filter对象的配置信息的对象，内部封装是该Filter的配置信息。
2）***destory()***：Filter对象销毁时执行，不需要参数，就不用多说。
3）***doFilter(ServletRequest,ServletResponse,FilterChain)***：其中的参数`ServletRequest`/`ServletResponse`是每次在执行doFilter方法时web容器负责创建一个request和一个response对象作为doFilter的参数传递进来的。request个该response就是在访问目标资源的service方法时的request和response。`FilterChain`类型参数为过滤器链对象，通过该对象的doFilter方法可以放行该请求。


在用户发送请求到服务器，然后经过过滤器的条件判断是否放行。就是在没有AOP思想的情况下，我们也能运用这个过滤器解决请求中乱码的问题。 
 
 
***第三步：在web.xml中进行配置(主要是配置要对哪些资源进行过滤)***

```xml
<filter>
    <filter-name>LoginFilter</filter-name>
    <filter-class>cn.zzpigt.filter.LoginFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>LoginFilter</filter-name>
    <url-pattern>/s1</url-pattern>
</filter-mapping>
```

这里对web.xml文件的配置比监听器要复杂一点，但是也非常容易看懂。先配置`filter`标签，注册我们创建的这个过滤器，给这个过滤器取个名字；然后配置对应的映射`filter-mapping`标签，和`filter`名字必须要相同，再就是配置要过滤的资源了。上面代码中`/s1`代表的意思很难解释，需要在被过滤的资源标上注解，我这里附上这个资源的代码。

```java
@WebServlet("/s1")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("用户访问了LoginServlet");
		response.getWriter().write("欢迎光临");
	}
}

```


`url-pattern`子标签配置时，不止上面代码中一种配置方式。总结一下这个标签的所有配置方式：

- 完全匹配 --- `/s1`
- 目录匹配 --- `/aaa/bbb/*` (这种配置最多)
- 扩展名匹配 --- `*.abc` 或 `*.jsp`

目录匹配方式应用场景非常多，举例：
1）`/user/*`：访问前台的资源进入此过滤器
2）`/admin/*`：访问后台的资源时执行此过滤器

>注意：`url-pattern`可以使用`servlet-name`替代，也可以混用


	拓展：dispatcher标签指定哪一种访问web资源的方式会被拦截，可选值有四种：
   		- REQUEST：默认值，代表直接访问某个资源时执行filter
   		- FORWARD：转发时才执行filter
   		- INCLUDE: 包含资源时执行filter
   		- ERROR：发生错误时 进行跳转是执行filter






  












