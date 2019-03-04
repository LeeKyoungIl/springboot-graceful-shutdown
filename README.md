# [Project illuminati](https://github.com/LeeKyoungIl/illuminati)
 - illuminati-springboot-graceful-shutdown
 
 The Illuminati-Graceful-shutdown library is available for Spring Boot project.
 
 ## Required environment
  - Spring Boot 2.x
  - Oracle JDK 8, OpenJDK 10.0.2 (9, 11)
  
 ## Why do we need to graceful shutdown library?
 
 The SpringBoot does not supported the graceful shutdown basically.
 When a kill signal occurred. in progress or incoming request are all 
 terminate immediately on application context.
 Therefore error occurs by user side.
 
 ## A function of illuminati-springboot-graceful-shutdown
 
 - A function of safely shut down
 To prevent problems above, use it. 
 if a kill signal occurred. it checks until all in progress requests are completed and safely shutdown.
 
 - Minimize errors in deploymene.
 Incoming requests after the kill signal return 503 http status.
 If you use more than one SpringBoot Application with the nginx proxy, you can deploy it nondisruptive.

## How to use in the project.

- maven & gradle (repository add to your configuration)
 - maven
 ```xml
 <repositories>
   <repository>
   <id>jcenter</id>
   <url>https://jcenter.bintray.com/</url>
   </repository>
</repositories>
 ```
 - gradle
 ```xml
repositories {
    jcenter()
}
 ```

- maven
```xml
<dependency>
  <groupId>me.phoboslabs.illuminati</groupId>
  <artifactId>illuminati-graceful-shutdown</artifactId>
  <version>0.2.4</version>
</dependency>
```
- gradle
```xml
compile 'me.phoboslabs.illuminati:illuminati-graceful-shutdown:0.2.4'
```

- Spring Boot Application
    - import ServerSignalFilterConfiguration class on your Spring Boot Application.
```java
@SpringBootApplication
@Import({IlluminatiGSFilterConfiguration.class})
public class SpringBootApplication {
    ...
}
```

============================================================================

Spring Boot 프로젝트에서 사용할 수 있는 Graceful shutdown 라이브러리 입니다.

## 필요환경
 - Spring Boot 2.x
 - Oracle JDK 8, OpenJDK 10.0.2 (9이상, 11지원)
 
## 왜 Graceful shutdown 기능이 필요할까요?

Spring Boot는 기본적으로 Graceful Shutdown을 지원하지 않습니다.
Spring Boot는 kill(종료) 시그널 발생 시 진행중인, 혹은 현재 들어오는 요청을 
모두 처리하지 않은 상태로 바로 애플리케이션 컨텍스트를 모두 제거해 버립니다.
따라서 유저 입장에서는 오류가 발생합니다.

## illuminati-springboot-graceful-shutdown 기능

- 안전한 종료 가능
위에 문제점을 방지 하고자 illuminati-springboot-graceful-shutdown 라이브러리를 사용하면
kill(종료) 시그널 발생 시 진행중인 요청이 모두 끝날때까지 체크하며 요청이 끝나는
것을 확인하고 안전하게 종료를 해줍니다. 

- 무중단 배포시 오류 최소화
또한 kill(종료) 시그널 발생 이후부터 들어오는 요청은 503 http status 로 돌려주며,
앞에 nginx 프록시로 Spring Boot Application을 2개 이상 사용한다면 무중단 배포가 가능합니다. 

![image](https://github.com/LeeKyoungIl/springboot-graceful-shutdown/blob/master/image/sample_1.png)
![image](https://github.com/LeeKyoungIl/springboot-graceful-shutdown/blob/master/image/sample_2.png)
![image](https://github.com/LeeKyoungIl/springboot-graceful-shutdown/blob/master/image/sample_3_1.png)
![image](https://github.com/LeeKyoungIl/springboot-graceful-shutdown/blob/master/image/sample_4.png)

# License
illuminati-springboot-graceful-shutdown is distributed under the GNU GPL version 3 or later.
