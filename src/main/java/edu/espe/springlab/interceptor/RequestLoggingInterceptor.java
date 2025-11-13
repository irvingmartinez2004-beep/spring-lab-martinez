package edu.espe.springlab.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {

        long start = (long) request.getAttribute("startTime");
        long elapsed = System.currentTimeMillis() - start;

        // 🔹 Header requerido por el taller
        response.addHeader("X-Elapsed-Time", elapsed + "ms");

        // 🔹 Log requerido por el taller
        System.out.println(
                request.getMethod() + " " +
                        request.getRequestURI() + " -> " +
                        response.getStatus() + " (" + elapsed + " ms)");
    }
}
