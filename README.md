<img src="https://i.imgur.com/Uh6137h.png" width="400"/>

----

`smart http logger` is auto http-logger for `SpringBoot` project.

![apache licensed](https://img.shields.io/badge/License-Apache_2.0-d94c32.svg)
![Java](https://img.shields.io/badge/Language-Java-f88909.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/jp.spring-boot-reference/smart-http-logger/badge.svg)](https://maven-badges.herokuapp.com/maven-central/jp.spring-boot-reference/smart-http-logger)



## introduction

you can logging automatically http req/res information that come to your application's api.

```javaScript

2019-04-19 20:20:00 ERROR 62593 --- [nio-8888-exec-2] j.s.s.logging.SmartHttpLogger            :
[ 'method' = 'GET', 'url' = '/user/100', 'request' = 'null', 'status' = '401', 'response' = '{"status":401,"message":"authorization exception"}', 'time' = '82ms' ]
2019-04-19 20:20:02  INFO 62593 --- [nio-8888-exec-3] j.s.s.logging.SmartHttpLogger            : 
[ 'method' = 'POST', 'url' = '/user', 'request' = '{"id":"1000","name":"kc","score":100}', 'status' = '200', 'response' = '{"id":"1000","name":"kc","score":100}', 'time' = '35ms' ]

```

## Usage

### 1. adding dependency on pom

```xml
<dependency>
	<groupId>jp.spring-boot-reference</groupId>
	<artifactId>smart-http-logger</artifactId>
	<version>1.0.0</version>
</dependency>

```

### 2. modify log output as you like. 

###### you can hide some http information by setting.

```yaml
smarthlog.output.method=false 
smarthlog.output.status=false
```

Then, output become 

```javaScript
2019-04-19 20:20:00 ERROR 62593 --- [nio-8888-exec-2] j.s.s.logging.SmartHttpLogger            : ['url' = '/user/100', 'request' = 'null', 'response' = '{"status":401,"message":"authorization exception"}', 'time' = '82ms' ]
```

###### you can mask header information.

you can hide header information by adding setting on `application.properties` like below.


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


