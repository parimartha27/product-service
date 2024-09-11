package com.training.product.log;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private final ServletOutputStream servletOutputStream = new ServletOutputStream() {
        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
        }

        @Override
        public void write(int b) {
            byteArrayOutputStream.write(b);
        }
    };

    public HttpResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return servletOutputStream;
    }

    public String getBody() {
        return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
    }

    public void copyBodyToResponse() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        StreamUtils.copy(byteArrayInputStream, super.getOutputStream());
    }

    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        Collection<String> headerNames = this.getHeaderNames();
        for (String headerName : headerNames) {
            headers.put(headerName, this.getHeader(headerName));
        }
        return headers;
    }
}