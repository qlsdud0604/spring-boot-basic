package com.example.springboot.filter;

import javax.servlet.*;
import java.io.IOException;

public class TestFilter01 implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("TestFilter01이 생성되었습니다.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("TestFilter01 필터 시작");
        chain.doFilter(request, response);
        System.out.println("TestFilter01 필터 종료");
    }

    @Override
    public void destroy() {
        System.out.println("TestFilter01이 소멸되었습니다.");
    }
}
