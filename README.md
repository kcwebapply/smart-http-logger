# smart-http-logger
auto http-logger for spring-boot

![apache licensed](https://img.shields.io/badge/License-Apache_2.0-d94c32.svg)
![Java](https://img.shields.io/badge/Language-Java-f88909.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/jp.spring-boot-reference/smart-http-logger/badge.svg)](https://maven-badges.herokuapp.com/maven-central/jp.spring-boot-reference/smart-http-logger)

https://search.maven.org/artifact/jp.spring-boot-reference/smart-http-logger/1.0.0-SNAPSHOT/jar
https://oss.sonatype.org/content/repositories/releases/jp/spring-boot-reference/smart-http-logger/

`smart-http-logger` is  http logging library for `SpringBoot` Project.





## introduction

```terminal
019-03-31 16:40:46.205  INFO 3508 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8888 (http) with context path ''
2019-03-31 16:40:46.210  INFO 3508 --- [           main] com.example.api.ApiApplication           : Started ApiApplication in 2.37 seconds (JVM running for 7.672)
2019-03-31 16:40:59.658  INFO 3508 --- [nio-8888-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2019-03-31 16:40:59.658  INFO 3508 --- [nio-8888-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2019-03-31 16:40:59.665  INFO 3508 --- [nio-8888-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 7 ms
2019-03-31 16:40:59.719  INFO 3508 --- [nio-8888-exec-1] j.s.smarthttplogger.LoggingPrinter       : 
{
 "url":"GET:/test/100 test=100",
 "requestHeaders":{"host":"localhost:8888","user-agent":"curl/7.54.0","accept":"*/*"},
 "requestBody":null,
 "responseHeaders":{},
 "httpStatus":200,
 "responseBody":100
}

// logging http req/response information
```

## Usage

- 1. adding dependency on pom

```xml
<dependency>
	<groupId>jp.spring-boot-reference</groupId>
	<artifactId>smart-http-logger</artifactId>
	<version>1.0.0</version>
</dependency>

```

- 2. scan this package to enable automatically logging.

```java
@SpringBootApplication
@ComponentScan(basePackages = {"com.example.api","jp.springbootreference.smarthttplogger"}) // here.
public class ApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
```

-3. to hide privacy , you can set what header's information you hide by adding setting on `application.properties` like below.

```yaml
smartlog.header.secrets=Authorization,host
```

then, `Authorization` and `host` header's information is hidden by `xxxxxxxxxxxxxx` expression.


## installation

adding dependency on pom.xml

```xml
<dependency>
	<groupId>jp.spring-boot-reference</groupId>
	<artifactId>smart-http-logger</artifactId>
	<version>1.0.0</version>
</dependency>
```


