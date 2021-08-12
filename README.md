# 스프링 부트의 기본적인 작동원리

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

