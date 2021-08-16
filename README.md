# 스프링 부트의 기본적인 작동원리
---
## :mag_right: 목차
* [스프링 웹 개발 기초](#mag_right-스프링-웹-개발-기초)
* [의존성 주입(Dependency Injection)](#mag_right-의존성-주입dependency-injection))
* [사용자 입력 데이터의 처리](#mag_right-사용자-입력-데이터의-처리)
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
