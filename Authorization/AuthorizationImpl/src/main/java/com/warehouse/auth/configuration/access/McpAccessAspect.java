package com.warehouse.auth.configuration.access;

import com.warehouse.auth.Mcp;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class McpAccessAspect {

    private final McpAccessEvaluator mcpAccessEvaluator;

    public McpAccessAspect(final McpAccessEvaluator mcpAccessEvaluator) {
        this.mcpAccessEvaluator = mcpAccessEvaluator;
    }

    @Around("@annotation(mcp)")
    public Object authorize(final ProceedingJoinPoint joinPoint, final Mcp mcp) throws Throwable {
        this.mcpAccessEvaluator.validate(SecurityContextHolder.getContext().getAuthentication(), mcp);
        return joinPoint.proceed();
    }
}
