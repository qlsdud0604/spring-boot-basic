# 스프링 부트의 기본적인 작동원리
---
## :mag_right: 목차
* [스프링 웹 개발 기초](#mag_right-스프링-웹-개발-기초)
 
* [의존성 주입(Dependency Injection)](#mag_right-의존성-주입dependency-injection))
 
* [사용자 입력 데이터의 처리](#mag_right-사용자-입력-데이터의-처리)

* [AOP](#mag_right-aop)

* [최종 결과물](#mag_right-최종-결과물)
</br>

---
## :mag_right: 스프링 웹 개발 기초
**✔️ 정적 콘텐츠의 동작 과정**   
<img src="https://user-images.githubusercontent.com/61148914/129156622-900d0af3-62ff-4142-970b-2c2012aac849.png" width="50%">
1) 웹 브라우저 요청 ex) localhost:8080/hello-static.html

2) 내장 톰켓 서버에서 콘텐츠 탐색
    1. 우선, 스프링 컨테이너에 접근 → hello-static 관련 컨트롤러가 존재하지 않음
    2. static 폴더에 접근 → hello-static.html 파일이 존재
    
3) 브라우저에 hello-static.html 파일을 렌더링
</br>

**✔️ MVC와 템플릿 엔진의 동작 과정**   
<img src="https://blog.kakaocdn.net/dn/bfBysA/btqXTmxDRZO/01wenKdMkef6YwkKvdtDB1/img.png" width="50%">
1) 웹 브라우저 요청 ex) localhost:8080/hello-mvc?name=spring

2) 내장 톰켓 서버에서 콘텐츠 탐색
    1. 스프링 컨테이너에 접근 → 해당 url과 매핑된 컨트롤러가 존재
    2. 모델이 전달받은 데이터를 처리한 후, 템플릿 이름을 반환
    3. viewResolver는 해당 템플릿을 찾아 템플릿 엔진인 타임리프에게 전달
    
3) hello-template.html 파일로 변환 후, 브라우저로 전송
</br>

**✔️ API의 동작 과정**   
<img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbizk8p%2FbtqXLfUfQ8q%2FgBHqJezHXHak8ji2Rgbnm1%2Fimg.png" width="50%">
1) 웹 브라우저 요청 ex) localhost:8080/hello-api

2) 내장 톰켓 서버에서 콘텐츠 탐색
    1. 스프링 컨테이너에 접근 → 해당 url과 매핑된 컨트롤러가 존재
    2. "@ResponseBody" 애너테이션이 있을 경우, viewResolver 대신에 HttpMessageConverter가 동작
    
3) 문자열 처리의 경우 StringHttpMessageConverter가 동작, 객체 처리의 경우 MappingJackson2HttpMessageConverter가 동작
</br>

---
## :mag_right: 의존성 주입(Dependency Injection)
**✔️ 의존성 주입이란?**   
```java
@Controller
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
}
```
ㆍ 생성자에 "@Autowired" 애너테이션이 있으면 스프링이 연관된 객체를 스프링 컨테이너에서 찾아서 넣어준다.   
ㆍ 이렇게 객체 의존관계를 외부에서 넣어주는 것을 의존성 주입이하고 한다.   
</br>

**✔️ 컴포넌트 스캔을 통한 자동 의존관계 설정**
```java
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}
```
```java
@Repository
public class MemoryMemberRepository implements MemberRepository {}
```
ㆍ "@Component" 애너테이션이 있으면 스프링 빈으로 자동 등록된다.   
ㆍ "@Controller", "@Service", "@Repository" 애너테이션은 "@Component" 애너테이션을 포함하기 때문에 스프링 빈으로 자동 등록된다.   
</br>

**✔️ 자바 코드로 직접 스프링 빈 등록**
```java
@Configuration
public class SpringConfig {
    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
}
```
ㆍ "@Bean" 애너테이션을 사용하여 개발자 직접 해당 클래스들을 스프링 빈으로 등록한다.   
</br>

---
## :mag_right: 사용자 입력 데이터의 처리
**✔️ 회원 등록 폼 HTML**
```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>
<div class="container">
    <form action="/members/new" method="post">
        <div class="form-group">
            <label for="name">이름</label>
            <input type="text" id="name" name="name" placeholder="이름을 입력하세요.">
        </div>
        <button type="submit">등록</button>
    </form>
</div> <!-- /container -->
</body>
</html>
```
ㆍ 사용자가 이름을 입력할 수 있는 폼 형식의 HTML 파일이다.   
ㆍ input 태그에 이름을 입력하고 등록 버튼을 눌렀을 때, "/members/new" 주소에 post 방식으로 전달된다.   
</br>

**✔️ 웹 화면에서 데이터를 전달 받을 폼 객체**
```java
public class MemberForm {
    private String name;

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
```
ㆍ 사용자가 입력한 이름 데이터와 매핑할 객체이다.   
</br>

**✔️ 컨트롤러를 통한 데이터의 처리**
```java
@PostMapping(value = "/members/new")
public String create(MemberForm form) {
    Member member = new Member();
    member.setName(form.getName());
    memberService.join(member);

    return "redirect:/";
}
```
ㆍ 컨트롤러 영역에서 post 방식의 "/members/new" 주소에 해당하는 메서드가 호출된다.   
ㆍ 매개 변수 form의 멤버 변수명과 사용자 입력 필드의 name 속성 값이 동일하면, form의 각 멤버 변수에 전달된 값들이 자동으로 매핑된다.   
</br>

---
## :mag_right: AOP
**✔️ AOP의 필요성**   
<img src="https://media.vlpt.us/images/kms9887/post/054686f2-da5f-4253-b676-582a4818555f/image.png" width="50%">

ㆍ 모든 메서드의 호출 시간을 측정하고 싶은 경우를 생각해본다.   
ㆍ 해결 방법은 위 사진과 같이 각 메서드마다 시간을 측정하는 로직을 작성하는 방법이 있다.   
ㆍ 이때, 시간을 측정하는 로직과 핵심 비즈니스의 로직이 섞여서 코드가 복잡해지고 유지보수가 어려워진다는 문제점이 발생한다.   
ㆍ 핵심 관심 사항(핵심 비즈니스 로직)과 공통 관심 사항(시간을 측정하는 로직)을 분리함으로써 이러한 문제점을 해결할 수 있다.   
</br>

**✔️ AOP를 통한 해결**   
<img src="https://media.vlpt.us/images/kms9887/post/49ecefcd-246c-48e3-aff4-8331127fb9e3/image.png" width="50%">

ㆍ 위 그림과 같이 핵심 관심 사항(핵심 비즈니스 로직)과 공통 관심 사항(시간을 측정하는 로직)을 분리한다.   
ㆍ 시간을 측정하는 로직을 공통 로직으로 만든 후 원하는 적용 대상을 선택해 준다.   
ㆍ 이와 같이 공통으로 처리해야 하는 기능들을 별도로 분리함으로써, 핵심 관심 사항을 깔끔하게 유지할 수 있고 변경이 필요하다면 공통으로 사용되는 로직만 변경해주면 된다.   
</br>

**✔️ AOP의 동작 과정**   
<img src="https://user-images.githubusercontent.com/61148914/130013774-ee122e70-3e73-4c49-aadf-6bd11469a1d3.png" width="50%">

ㆍ 위 사진은 AOP를 적용하기 전 각 클래스들의 호출 방식이다.   
ㆍ 각 클래스들은 Controller, Service, Repository 순으로 차례대로 호출된다.   

<img src="https://media.vlpt.us/images/kms9887/post/212080aa-3615-4282-9ccf-9f1576ad86e1/image.png" width="50%">

ㆍ 위 사진은 AOP를 적용한 후의 각 클래스들의 호출 방식이다.   
ㆍ 프록시를 생성하여 프록시를 먼저 호출한 후 프록시가 끝나고 나서 비로소 타겟이 호출되는 방식이다.   
ㆍ 프록시란 타겟을 감싸서 호출을 대신 받는 Wrapping Object이다.   
</br>

**✔️ AOP 적용**   
```java
@Component
@Aspect
public class TimeTraceAop {

    @Around("execution(* com.example.springboot..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        System.out.println("START : " + joinPoint.toString());

        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();

            long timeMs = finish - start;

            System.out.println("END : " + joinPoint.toString() + " " + timeMs + "ms");
        }
    }
}
```
ㆍ "@Around" 애너테이션을 통해 타게팅할 범위를 설정해준다. 위 코드에서는 springboot 패키지 내부 전체를 대상으로 타게팅을 한 것이다.   
ㆍ joinPoint.proceed( ) 메서드를 이용하여, AOP가 적용된 다음 타겟을 호출함으로써 시간 측정을 한다.   
</br>

---
## :mag_right: 최종 결과물
**✔️ 메뉴 선택 화면**   
<img src="https://user-images.githubusercontent.com/61148914/130189361-c5dc7e2f-2af5-49a3-9632-8f448f801cb0.png" width="50%">

ㆍ 메뉴 선택 화면에서 회원 가입 또는 회원 목록 기능을 선택할 수 있다.   
</br>

**✔️ 회원 가입 화면**   

<img src="https://user-images.githubusercontent.com/61148914/130189498-15770732-2b5b-4505-9ef3-c0c4093f3100.png" width="50%">

ㆍ 회원 가입 화면에서 본인의 이름을 입력하여 회원가입을 진행할 수 있다.   
</br>

**✔️ 회원 목록 화면**   

<img src="https://user-images.githubusercontent.com/61148914/130189765-198f5633-7677-4121-af92-128f11cba1a9.png" width="50%">

ㆍ 회원 목록 화면에서 회원 가입이 된 회원 목록들을 확인할 수 있다.   
</br>
