description: "这里我只是整理一下MyBatis的用法，放到Java三层架构中要怎么使用。也是从这个框架我开始了配置文件的‘呆板’之路，但是稍微麻烦的配置完成，开发过程就非常清爽，这也是在框架专业作用之外的好处。"

---

## MyBatis介绍

**MyBatis**本是apache的一个开源项目iBatis，2010年这个项目由apache software foundation迁移到了google code，并且改名为MyBatis，所以它有个曾用名**iBatis**。

2013年11月迁移到Github。MyBatis是一个**优秀的持久层框架**，它对JDBC的操作数据库的过程进行封装，使开发者只需要关注SQL本身，而不需要花费精力去处理例如注册驱动、创建connection、创建statement、手动设置参数、结果集检索等jdbc繁杂的过程代码。所以，MyBatis可以代替三层架构中DAO层的作用，而且更加简便快捷（单独使用MyBatis，配置文件个人觉得并不简便，好在有**逆向工程**这个工具）。

MyBatis通过xml或注解的方式将要执行的各种`statement`（`statement`、`preparedStatemnt`、`CallableStatement`）配置起来，并通过Java对象和`statement`中的SQL进行映射生成最终执行的SQL语句，最后由MyBatis框架执行SQL并将结果映射成Java对象并返回。

原生Java中的JDBC编程存在很多的问题，而这些问题也是促使MyBatis的诞生。简单的来说，JDBC有多少缺陷，那么MyBatis就有多少优点。当然下面列举的缺点都是能解决的，只是使用了MyBatis这些问题就不用怎么关心了。

1. 数据库连接创建、释放频繁造成系统资源浪费，从而影响系统性能。如果使用数据库连接池可解决此问题。
2. SQL语句在代码中硬编码，造成代码不易维护，实际应用中SQL变化的可能较大，SQL变动需要改变java代码。
3. 使用preparedStatement向占有位符号传参数存在硬编码，因为SQL语句的where条件不一定，可能多也可能少，修改SQL还要修改代码, 系统不易维护。
4. 对结果集解析存在硬编码（查询列名），SQL变化导致解析代码变化，系统不易维护，如果能将数据库记录封装成pojo对象解析比较方便。

## MyBatis架构的使用

在开发中也许MyBatis不会单独被使用，总是配合着Spring框架开发。后面也会将MyBatis和Spring整合到一起使用。但是我这里还是要整理一下MyBatis的使用过程，逆向工程也许非常方便好用，可是不能只会使用工具，而不知其所以然。

### MyBatis准备工作

***第一步：*** 导包，后面接触的框架基本都会需要导入`jar`包。MyBatis的包：
`mybatis-3.2.7.jar`

除此之外，使用过程中，还会依赖很多的`jar`包，这里也罗列一下我开发过程中遇到的包(MyBatis默认使用log4j作为输出日志信息。)：

- `asm-3.3.1.jar`
- `cglib-2.2.2.jar`
- `commons-logging-1.1.1.jar`
- `javassist-3.17.1-GA.jar`
- `log4j-1.2.17.jar`
- `log4j-api-2.0-rc1.jar`
- `log4j-core-2.0-rc1.jar`
- `slf4j-api-1.7.5.jar`
- `slf4j-log4j12-1.7.5.jar`

>注意：既然要操作数据库，那么就必须导入数据库驱动包，别忘了

***第二步：*** 配置MyBatis核心文件`SQLMapConfig.xml`，此文件作为MyBatis的全局配置文件，配置了MyBatis的运行环境等信息。先看个示例：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<properties resource="dbconfig.properties"></properties>
	<typeAliases>
		<!-- 批量定义, 所有别名起为简单类名-->
		<package name="com.bwf.bean"/>
	</typeAliases>
	<environments default="development">
		<environment id="development">
			<transactionManager type="JDBC" />
			<dataSource type="POOLED">
				<property name="driver" value="${jdbc.driver}" />
				<property name="url" value="${url}" />
				<property name="username" value="${uname}" />
				<property name="password" value="${upwd}" />
			</dataSource>
		</environment>
	</environments>
	<mappers>
		<!-- <mapper resource="com/bwf/c_mapper/UserMapper.xml"/> -->
		<package name="com.bwf.c_mapper"/>
	</mappers>
</configuration>
```
这里逐行解释一下，第一行标准的xml文件开头不用多说。第二行开始是看到的是一个网址，这个网址链接的是一个约束，所以，这个配置文件要想生效必须连着网。在IDE编辑器中，鼠标移上去还能看到一些标签提示信息。

>注意：这个约束规定的标签顺序是有影响的，最好按照约束提示的前后顺序编写。

`properties`标签可以引入properties文件，上面是引入配置数据库信息。`environments`标签使用这些信息来配置数据库，这样每次改数据库信息就只用在`dbconfig.properties`文件中操作了。注意的是：`<dataSource type="POOLED">`*配置了数据库连接池*

`typeAliases`标签中配置的是批量扫描某个包下所有类，对扫描到的类型进行别名控制。也就是为了后面Mpper.xml文件中类型不用写全名。看到后面就能理解了，请继续阅读下去。

`mappers`标签是用于我们加载映射文件Mpper.xml用的，上面的代码中演示了具体的某个xml文件的配置，或者扫描某个包下所有的Mpper.xml。这一步很关键，如果要详细配置，必须保证Mapper.xml和Mapper文件名完全一致，还要保证Mapper.xml和Mapper文件放在同一路径下。

上面提到的**Mpper.xml**没接触过MyBatis的话，就非常疑惑，我都不确定上面写的对新手有没有一点用处-。-

### MyBatis开发

MyBatis的开发在准备工作的时候就已经开始了，不过这一小节更多是关注操作数据库。

上面说道mapper.xml文件，它到底有什么作用呢？其实这个文件就是SQL映射文件，文件中配置了操作数据库的SQL语句。如上所示，此文件需要在SQLMapConfig.xml中加载。

在开发过程中，为了让MyBatis取代DAO层，我们要需要写一个接口，而这个接口的编写和相对应的Mpper文件的编写都需要遵循一些规则：

- Mapper.xml文件中的namespace与mapper接口的类路径(完整类名)相同。
- Mapper接口方法名和Mapper.xml中定义的每个statement的id相同 
- Mapper接口方法的输入参数类型和mapper.xml中定义的每个SQL 的parameterType的类型相同
- Mapper接口方法的输出参数类型和mapper.xml中定义的每个SQL的resultType的类型相同

为了后面更加清晰的介绍这个文件的作用，这里会用一个*User用户表*来写示例说明。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="user">
	<select id="findById" parameterType="int" resultType="User">
		SELECT * from user WHERE id = #{id}
	</select>
	<!-- 根据姓名模糊查询 -->
	<select id="findLikeName" parameterType="String" resultType="com.bwf.bean.User">
		SELECT * FROM user WHERE username LIKE '%${value}%';
	</select>
	<!-- 用#代替$, 防止SQL注入 -->
	<select id="findLikeName2" parameterType="String" resultType="com.bwf.bean.User">
		SELECT * FROM user WHERE username LIKE "%"#{haha}"%";
	</select>
	<insert id="insert" parameterType="com.bwf.bean.User">
		<selectKey keyProperty="id" resultType="Integer" order="AFTER">
			SELECT LAST_INSERT_ID();
		</selectKey>
		INSERT INTO user (username, birthday, sex, address) 
		VALUES 
		(#{username}, #{birthday}, #{sex}, #{address});
	</insert>
	<update id="updateById" parameterType="com.bwf.bean.User">
		UPDATE user SET username = #{username}, birthday = #{birthday}, 
		sex = #{sex}, address = #{address}
		WHERE id = #{id};
	</update>
	<delete id="deleteById" parameterType="Integer">
		DELETE FROM user WHERE id = #{id};
	</delete>
</mapper>
```

这里我们给`namespace`取名是`user`，这是原生的DAO开发方式，现在还是有许多公司再这么使用。这里还有一个小知识点：

- `#{id}`：表示一个占位符, 相当于jdbc中的 ？, 左右两边会自动加上一个'', 防止SQL注入
- `${value}`：表示替换, 就是个简单的字符串拼接, 不会加上'', 不防SQL注入

>注意：上面的`parameterType`和`resultType`属性中有的写的是基本类型，或者其包装类型，还有的是类全名的引用类型。这个类全名就是如果想要简单写个类名就行的话，前面讲到的`SQLMapConfig.xml`文件中就需要配置`typeAliases`标签了，并且有扫描到这个类。

到此，整个MyBatis开发过程也可以说是有一种DAO层的开发方式，并没有取代它。后面也可以使用MyBatis操作数据库了。也还是看示例：

```java
// 根据id查询用户信息
@Test
public void fn1() {
	String resource = "SqlMapConfig.xml";
	try {
		// 解析配置文件
		Reader reader = Resources.getResourceAsReader(resource);
		// 根据配置文件构造session工厂
		SqlSessionFactory ssf = new SqlSessionFactoryBuilder().build(reader);
		// session工厂创造session对象
		SqlSession session = ssf.openSession();
			
		User user = session.selectOne("user.findById", 10);
		System.out.println(user);
	} catch (IOException e) {
		e.printStackTrace();
	}
}
```

如果仔细看，这里没有用到接口，而是直接通过`session.selectOne`方法调用`user`包名的xml文件中映射的方法。而且开发到这里遵循的MyBatis

根据开发规则配置了上面的Mpper.xml文件，还要在同包下的同名接口中配置每个方法，这个方法的参数类型和返回值类型都必须和xml文件中一致。
	2、通过MyBatis环境等配置信息构造SQLSessionFactory即会话工厂

	3、由会话工厂创建SQLSession即会话，操作数据库需要通过SQLSession进行。

	4、MyBatis底层自定义了Executor执行器接口操作数据库，Executor接口有两个实现，一个是基本执行器、一个是缓存执行器。

	5、Mapped Statement也是MyBatis一个底层封装对象，它包装了MyBatis配置信息及SQL映射信息等。
	 mapper.xml文件中一个SQL对应一个MappedStatement对象，SQL的id即是Mapped statement的id。

	6、Mapped Statement对SQL执行输入参数进行定义，包括HashMap、基本类型、pojo，
	 Executor通过Mapped Statement在执行SQL前将输入的java对象映射至SQL中，
	 输入参数映射就是jdbc编程中对preparedStatement设置参数。

	7、Mapped Statement对SQL执行输出结果进行定义，包括HashMap、基本类型、pojo，
	 Executor通过Mapped Statement在执行SQL后将输出结果映射至java对象中，
	 输出结果映射过程相当于jdbc编程中对结果的解析处理过程。

【快速入门】
	1.要求使用MyBatis实现以下功能：
	 根据用户id查询一个用户
	 根据用户名称模糊查询用户列表
	 添加用户
	 更新用户
	 删除用户

	2. 步骤

	 a. 创建java工程，加入MyBatis核心包、依赖包、数据驱动包。

	 b. 加入配置文件
		(1)log4j.properties
			(MyBatis默认使用log4j作为输出日志信息。)
		(2)SQLMapConfig.xml
			(SQLMapConfig.xml是MyBatis核心配置文件，配置文件内容为数据源、事务管理。)

	 c. 创建pojo
		pojo类作为MyBatis进行SQL映射使用，po类通常与数据库表对应

	 d. SQL映射文件
		SQLmap目录下创建SQL映射文件User.xml
		<?xml version="1.0" encoding="UTF-8"?>
		<!DOCTYPEmapper
		PUBLIC"-//MyBatis.org//DTD Mapper 3.0//EN"
		"http://MyBatis.org/dtd/MyBatis-3-mapper.dtd">

	 e. 加载映射文件
		MyBatis框架需要加载Mapper.xml映射文件
		将users.xml添加在SQLMapConfig.xml

【MyBatis解决jdbc编程的问题】
	1、数据库连接创建、释放频繁造成系统资源浪费从而影响系统性能，如果使用数据库连接池可解决此问题。
	解决：在SQLMapConfig.xml中配置数据连接池，使用连接池管理数据库链接。

	2、SQL语句写在代码中造成代码不易维护，实际应用SQL变化的可能较大，SQL变动需要改变java代码。
	解决：将SQL语句配置在XXXXmapper.xml文件中与java代码分离。

	3、向SQL语句传参数麻烦，因为SQL语句的where条件不一定，可能多也可能少，占位符需要和参数一一对应。
	解决：MyBatis自动将java对象映射至SQL语句，通过statement中的parameterType定义输入参数的类型。

	4、对结果集解析麻烦，SQL变化导致解析代码变化，且解析前需要遍历，如果能将数据库记录封装成pojo对象解析比较方便。
	解决：MyBatis自动将SQL执行结果映射至java对象，通过statement中的resultType定义输出结果的类型。


【原始Dao开发】
	SQLSession中封装了对数据库的操作，如：查询、插入、更新、删除等。
	SQLSession通过SQLSessionFactory创建。
	SQLSessionFactory是通过SQLSessionFactoryBuilder进行创建。

	SQLSessionFactoryBuilder
		SQLSessionFactoryBuilder用于创建SQLSessionFacoty，
		SQLSessionFacoty一旦创建完成就不需要SQLSessionFactoryBuilder了，
		因为SQLSession是通过SQLSessionFactory创建的。所以可以将SQLSessionFactoryBuilder当成一个工具类使用，
		最佳使用范围是方法范围即方法体内局部变量。

	SQLSessionFactory
		SQLSessionFactory是一个接口，接口中定义了openSession的不同重载方法，
		SQLSessionFactory的最佳使用范围是整个应用运行期间，一旦创建后可以重复使用，
		通常以单例模式管理SQLSessionFactory。

	SQLSession
		SQLSession是一个面向用户的接口，SQLSession中定义了数据库操作方法。
		每个线程都应该有它自己的SQLSession实例。SQLSession的实例不能共享使用，它也是线程不安全的。
		因此最佳的范围是请求或方法范围。绝对不能将SQLSession实例的引用放在一个类的静态字段或实例字段中。

		打开一个 SQLSession；使用完毕就要关闭它。通常把这个关闭操作放到 finally 块中以确保每次都能执行关闭


	* 原始Dao开发中存在以下问题：
	Dao方法体存在重复代码：通过SQLSessionFactory创建SQLSession，调用SQLSession的数据库操作方法
	调用SQLSession的数据库操作方法需要指定statement的id，这里存在硬编码，不得于开发维护。

【Mapper动态代理方式】

	Mapper接口开发方法只需要程序员编写Mapper接口（相当于Dao接口），
	由MyBatis框架根据接口定义创建接口的动态代理对象，代理对象的方法体同上边Dao接口实现类方法。

	Mapper接口开发需要遵循以下规范：
	1、Mapper.xml文件中的namespace与mapper接口的类路径(完整类名)相同。
	2、Mapper接口方法名和Mapper.xml中定义的每个statement的id相同 
	3、Mapper接口方法的输入参数类型和mapper.xml中定义的每个SQL 的parameterType的类型相同
	4、Mapper接口方法的输出参数类型和mapper.xml中定义的每个SQL的resultType的类型相同

	*小结	
		selectOne和selectList
		动态代理对象调用SQLSession.selectOne()和SQLSession.selectList()是根据mapper接口方法的返回值决定，
		如果返回list则调用selectList方法，如果返回单个对象则调用selectOne方法。

		namespace
		MyBatis官方推荐使用mapper代理方法开发mapper接口，程序员不用编写mapper接口实现类，
		使用mapper代理方法时，输入参数可以使用pojo包装对象或map对象，保证dao的通用性。


【SQLMapConfig.xml配置文件】

	1. properties
	2. typeAliases
		MyBatis支持别名：
		别名		映射的类型
		_byte 		byte 
		_long 		long 
		_short 		short 
		_int 		int 
		_integer 	int 
		_double 	double 
		_float 		float 
		_boolean 	boolean 
		string 		String 
		byte 		Byte 
		long 		Long 
		short 		Short 
		int 		Integer 
		integer 	Integer 
		double 		Double 
		float 		Float 
		boolean 	Boolean 
		date 		Date 
		decimal 	BigDecimal 
		bigdecimal 	BigDecimal 
		map		Map

【MyBatis与hibernate不同】
  MyBatis和hibernate不同，它不完全是一个ORM框架，因为MyBatis需要程序员自己编写SQL语句。
MyBatis可以通过XML或注解方式灵活配置要运行的SQL语句，并将java对象和SQL语句映射生成最终执行的SQL，
最后将SQL执行的结果再映射生成java对象。

  MyBatis学习门槛低，简单易学，程序员直接编写原生态SQL，可严格控制SQL执行性能，灵活度高，
 非常适合对关系数据模型要求不高的软件开发，
例如互联网软件、企业运营类软件等，因为这类软件需求变化频繁，一但需求变化要求成果输出迅速。
但是灵活的前提是MyBatis无法做到数据库无关性，如果需要实现支持多种数据库的软件则需要自定义多套SQL映射文件，工作量大。

  Hibernate对象/关系映射能力强，数据库无关性好，对于关系模型要求高的软件（例如需求固定的定制化软件）
如果用hibernate开发可以节省很多代码，提高效率。但是Hibernate的学习门槛高，要精通门槛更高，
而且怎么设计O/R映射，在性能和对象模型之间如何权衡，以及怎样用好Hibernate需要具有很强的经验和能力才行。

  总之，按照用户的需求在有限的资源环境下只要能做出维护性、扩展性良好的软件架构都是好架构，所以框架只有适合才是最好。








	
	
 








