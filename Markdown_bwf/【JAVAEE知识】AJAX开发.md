description: "AJAX就是用JS执行异步网络请求。实现过程差不多就是用户触发了某个事件，向服务器发送了一次请求，这次请求可能带有数据，然后服务器做相应的处理后也可以给客户端及时的返回一些数据。这就应该广泛了，让你的网站非常流弊！"

---

>如果您是抱着学习的态度过来的，那么本人拙劣的文笔看起来就没有那么让人愉快了-。-；所以这里推荐一下大牛@廖雪峰的[AJAX的教程](https://www.liaoxuefeng.com/wiki/001434446689867b27157e896e74d51a89c25cc8b43bdb3000/001434499861493e7c35be5e0864769a2c06afb4754acc6000)



**AJAX** [Asynchronous JavaScript and XML]（异步的 JavaScript 和 XML）网上大部分就是这么来解释的。专业的名词解释另一个专业的名称，让人看起来就迷茫。

反而又会冒出更多的问题：*什么是异步？异步和同步是什么概念？*

这里解释一下：一般同步现象，就好比客户端发送请求到服务器端, 在服务器返回响应之前, 客户端都处于等待卡死状态。而异步现象呢，就是在客户端发送请求到服务器端, 无论服务器是否返回响应, 客户端都可以随时的做其他事情, 不会被卡死。

对异步有了一个概念，那么就知道AJAX其实就是一段JS代码，只不过是执行异步网络请求。


**这里介绍一下两种情况下怎么去使用AJAX**

- ***原生JS写AJAX***
- ***引入Jquery写AJAX***


## 回忆一下JSON
 
JSON是一种轻量级的数据交换格式，语法很简单：

- `{ }` 表示一个对象
- `[ ]` 表示一个数组

数据的存放方式是键值对的样式。举个例子：
```json
{"people":[
	{"firstName": "Brett","lastName":"McLaughlin"},
	{"firstName":"Jason","lastName":"Hunter"}
	]}
```

## 原生JS写AJAX

```html

<!-- js原生Ajax  -->
<input type="button" value="get发送异步请求" onclick="fn1()">
<input type="button" value="get发送同步请求" onclick="fn2()">
<input type="button" value="post发送异步请求" onclick="fn3()">

<script type="text/javascript">
	function fn1(){
		// 1. 创建异步传输对象
		var xmlhttp = new XMLHttpRequest();
		// 2. 设置好响应回来的时候要触发的函数
		xmlhttp.onreadystatechange = function(){
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200){
				// 4. 接收服务器的响应
				var res = xmlhttp.responseText;
				alert(res);
			}
		}
		// 3. 发送Ajax请求
		xmlhttp.open("GET","/Ajax/s2?name=李四",true);
		xmlhttp.send();	
	}
		
	function fn2(){
		// 1. 创建异步传输对象
		var xmlhttp = new XMLHttpRequest();
		// 2. 设置好响应回来的时候要触发的函数
		xmlhttp.onreadystatechange = function(){
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200){
				// 4. 接收服务器的响应
				var res = xmlhttp.responseText;
				alert(res);
			}
		}
		// 3. 发送Ajax请求
		xmlhttp.open("GET","/Ajax/s2", false);
		xmlhttp.send();
			
	}
		
	function fn3(){
		// 1. 创建异步传输对象
		var xmlhttp = new XMLHttpRequest();
		// 2. 设置好响应回来的时候要触发的函数
		xmlhttp.onreadystatechange = function(){
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200){
				// 4. 接收服务器的响应
				var res = xmlhttp.responseText;
				alert(res);
			}
		}
		// 3. 发送Ajax请求
		xmlhttp.open("POST","/Ajax/s2",true);
		xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
		xmlhttp.send("name=李四");	
	}
```

上面的代码中有三个`button`，代表GET方式发送异步请求、GET方式发送同步请求和POST方式发送异步请求。前两个以GET方式，代码上可以看出在`xmlhttp.open`的传入参数上有不同，这是第三个布尔类型的参数作用是指定此请求是否为异步方式，默认为true。

这里，GET和POST又多了一些差别，他们在AJAX发送请求的时候传数据的方式不同，GET方式是在地址的后面拼接上数据，而POST方式是在`xmlhttp.send()`方法中以参数的形式传送数据。

## 引入Jquery写AJAX

Jquery中也封装了AJAX的方法，而且更加强大。建议先去看看文档，再稍微练习一下，这样就算忘了具体的写法也关系，后头看看文档也就会了。

下面的代码作为示例可以常常看看：

```html
<input id="btn1" type="button" value="get请求">
<input id="btn2" type="button" value="post请求">
<input id="btn3" type="button" value="ajax请求">

<script type="text/javascript" src="/Ajax/js/jquery-1.7.2.js"></script>
<script type="text/javascript">
	$(function() {
		$("#btn1").click(function() {
			$.get("/Ajax/s4", {
				name : "John",
				time : "2pm"
			},function(data) {
				alert(data.name);
			}, "json");})

		$("#btn2").click(function() {
			$.post("/Ajax/s4", {
				name : "John",
				time : "2pm"
			}, function(data) {
				alert(data.name);
			}, "json");})

		$("#btn3").click(function() {
			$.ajax({
				type : "GET",
				url : "/Ajax/s4",
				data : "name=John&time=Boston",
				success : function(msg) {
					alert(msg.height);
				},
				statusCode : {
					404 : function() {
						alert('欢迎来到火星!');
					},
					500 : function(){
						alert('服务器出错!')
					}
				},
				dataType : "json"});})
	})
```