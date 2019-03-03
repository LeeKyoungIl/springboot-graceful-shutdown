# [Project illuminati](https://github.com/LeeKyoungIl/illuminati)
 - illuminati-springboot-graceful-shutdown

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
![image](https://github.com/LeeKyoungIl/springboot-graceful-shutdown/blob/master/image/sample_3.png)
![image](https://github.com/LeeKyoungIl/springboot-graceful-shutdown/blob/master/image/sample_4.png)

## 사용방법
- maven
```xml
<dependency>
  <groupId>me.phoboslabs.illuminati</groupId>
  <artifactId>illuminati-graceful-shutdown</artifactId>
  <version>0.1.7</version>
</dependency>
```
- gradle
```xml
compile 'me.phoboslabs.illuminati:illuminati-graceful-shutdown:0.1.7'
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

# License
illuminati-springboot-graceful-shutdown is distributed under the GNU GPL version 3 or later.
