package com.stc.clinicservice.configurations.httploggingfilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

@Component
public class RequestLogFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger("default-logger");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //nothing at init.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String path = ((HttpServletRequest) request).getServletPath();
         request = new CustomHttpServletRequestWrapper((HttpServletRequest) request);
            response = new CustomHttpServletResponseWrapper((HttpServletResponse) response);

            if (((HttpServletRequest) request).getHeader("host") != null) {
                final String host = ((HttpServletRequest) request).getHeader("host");
                MDC.put("host", host);
            }

            StringBuilder loggingMessage = new StringBuilder();
            /* request info */
            final String method = ((HttpServletRequest) request).getMethod();
            final String url = ((HttpServletRequest) request).getRequestURL().toString();
            loggingMessage.append(String.format("REST, request, %s, %s", method, url)).append("\n") ;

            /* request headers */
            final List<String> requestHeaders = new LinkedList<>();
            final Enumeration<String> headerNames = ((HttpServletRequest) request).getHeaderNames();
            while (headerNames.hasMoreElements()) {
                final String headerName = headerNames.nextElement();
                if (!headerName.equals("authorization")) {
                    final String headerValue = ((HttpServletRequest) request).getHeader(headerName);
                    requestHeaders.add(String.format("%s: %s", headerName, headerValue));
                }
            }
            String requestHeadersString = requestHeaders.toString();
            loggingMessage.append(String.format("REST, headers, %s", requestHeadersString)).append("\n") ;

            /* request data */
            final String requestData = ((CustomHttpServletRequestWrapper) request).getBody().replace("\n", "");
            final StringBuilder stringBuilder = new StringBuilder();
            if (request.getParameterMap().size() > 0) {
                stringBuilder.append("{");
                request.getParameterMap().forEach((key, value) -> stringBuilder.append(key).append(": ").append(value[0]).append(", "));
                stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                stringBuilder.append("}");
            }
            String stringBuilderString = stringBuilder.toString();
            loggingMessage.append(String.format("REST, data, %s, %s", requestData, stringBuilderString)).append("\n") ;

            //logging the request details
            String loggingMessageToString = loggingMessage.toString();
            logger.info(loggingMessageToString);

            /* response */
            try {
                chain.doFilter(request, response);
                response.flushBuffer();
            } finally {
                final String removingWhitespacesRegex = "\n";

                final Integer status = ((CustomHttpServletResponseWrapper) response).getStatus();
                final String contentType = response.getContentType();
                String responseData = ((CustomHttpServletResponseWrapper) response).getBody().replace(removingWhitespacesRegex, "");
                if (status < 400)
                    logger.info("REST, response, {}, {}, {}, {}", status, contentType, responseData, url);
                else
                    logger.error("REST, response, {}, {}, {}, {}", status, contentType, responseData, url);
            }
    }

    @Override
    public void destroy() {
        //do nothing.
    }
}
