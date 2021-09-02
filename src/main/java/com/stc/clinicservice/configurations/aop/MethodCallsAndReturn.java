package com.stc.clinicservice.configurations.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Aspect
@Configuration
public class MethodCallsAndReturn {
    private static final Logger LOGGER = LoggerFactory.getLogger("default-logger");
    private static final String POINT_CUT = "within(com.stc.clinicservice.configurations.aop.Loggable+)";

    private final ObjectMapper objectMapper;

    public MethodCallsAndReturn(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private String convertObjectToString(Object obj) {
        try {
            return this.objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.toString());
        }

        return "";
    }

    @Before(POINT_CUT)
    public void printMethodArguments(JoinPoint joinPoint) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        final String[] parameterNames = signature.getParameterNames();
        final Object[] parameterValues = joinPoint.getArgs();
        final Map<String, Object> methodArguments = IntStream.range(0, parameterNames.length).boxed().collect(HashMap::new, (map, i) -> map.put(parameterNames[i], parameterValues[i]), HashMap::putAll);

        final String methodArgumentsString = this.convertObjectToString(methodArguments);
        if(LOGGER.isTraceEnabled())
            LOGGER.trace("METHOD_CALL, {}, {}", signature, methodArgumentsString);
    }

    @AfterReturning(value = POINT_CUT, returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        final String returnObjectString = this.convertObjectToString(result);
        if(LOGGER.isTraceEnabled())
            LOGGER.trace("METHOD_RETURN, {}, {}", signature, returnObjectString);
    }

    @AfterThrowing(value = POINT_CUT, throwing = "ex")
    public void handleThrownException(JoinPoint joinPoint, Throwable ex) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        final String exceptionName = ex.getClass().getName();
        final String exceptionMessage = this.convertObjectToString(ex.getMessage());
        LOGGER.error("METHOD_EXCEPTION, {}, {}, {}", signature, exceptionName, exceptionMessage);
    }
}
