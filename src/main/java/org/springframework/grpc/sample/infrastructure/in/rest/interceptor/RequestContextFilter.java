package org.springframework.grpc.sample.infrastructure.in.rest.interceptor;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component("customRequestContextFilter")
public class RequestContextFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            System.out.println("---->>>>>>>> VAO FILTER: " + request.getRequestURI());
            // Tạo request ID và gán vào ThreadLocal
            RequestContextHolder.RequestContext context = new RequestContextHolder.RequestContext();
            context.setRequestPath(request.getRequestURI());
            RequestContextHolder.setContext(context);

            // Tiếp tục chuỗi filter
            filterChain.doFilter(request, response);
        } finally {
            System.out.println("---->>>>>>>>XÓA VAO FILTER: " + request.getRequestURI());
            // Xóa ThreadLocal sau khi xử lý request
            RequestContextHolder.clearContext();
        }
    }
}