package com.training.product.config;


import com.training.product.log.HttpRequestWrapper;
import com.training.product.log.HttpResponseWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class LoggerConfiguration extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggerConfiguration.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
        HttpResponseWrapper responseWrapper = new HttpResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            logger.info("Request: URL={}, Method={}, Headers={}, Body={}",
                    request.getRequestURI(),
                    request.getMethod(),
                    requestWrapper.getHeaders(),
                    requestWrapper.getBody());

            logger.info("Response: Status={}, Headers={}, Body={}, Duration={} ms",
                    responseWrapper.getStatus(),
                    responseWrapper.getHeaders(),
                    responseWrapper.getBody(),
                    duration);

            responseWrapper.copyBodyToResponse();
        }
    }
}
