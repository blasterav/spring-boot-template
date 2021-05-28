package com.volans.template.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

@Component
public class ControllerLogsInterceptor extends HandlerInterceptorAdapter {

    private final Logger LOG = LoggerFactory.getLogger(ControllerLogsInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Instant instant = Instant.now();
        long startTime = instant.toEpochMilli();
        LOG.info("Started {} {}", request.getMethod(), request.getServletPath());
        request.setAttribute("startTime", startTime);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long startTime = (Long) request.getAttribute("startTime");
        LOG.info("Finished {} {}, Status: {}, Duration: {}", request.getMethod(), request.getServletPath(), response.getStatus(), (Instant.now().toEpochMilli() - startTime));
    }

}
