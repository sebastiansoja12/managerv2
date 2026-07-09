package com.warehouse.auth.configuration.access;

import java.lang.reflect.Method;

import com.warehouse.auth.AccessUserControl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AccessUserControlAspect {

    private final AccessUserControlEvaluator accessUserControlEvaluator;

    public AccessUserControlAspect(final AccessUserControlEvaluator accessUserControlEvaluator) {
        this.accessUserControlEvaluator = accessUserControlEvaluator;
    }

    @Around("@within(com.warehouse.auth.AccessUserControl) || @annotation(com.warehouse.auth.AccessUserControl)")
    public Object authorize(final ProceedingJoinPoint joinPoint) throws Throwable {
        final AccessUserControl accessUserControl = resolveAccessUserControl(joinPoint);
        accessUserControlEvaluator.validate(SecurityContextHolder.getContext().getAuthentication(), accessUserControl);
        return joinPoint.proceed();
    }

    private AccessUserControl resolveAccessUserControl(final ProceedingJoinPoint joinPoint) {
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Method method = resolveMethod(joinPoint, methodSignature);
        final AccessUserControl methodAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, AccessUserControl.class);

        if (methodAnnotation != null) {
            return methodAnnotation;
        }

        return AnnotatedElementUtils.findMergedAnnotation(joinPoint.getTarget().getClass(), AccessUserControl.class);
    }

    private Method resolveMethod(final ProceedingJoinPoint joinPoint,
                                 final MethodSignature methodSignature) {
        try {
            return joinPoint.getTarget()
                    .getClass()
                    .getMethod(methodSignature.getMethod().getName(), methodSignature.getMethod().getParameterTypes());
        } catch (NoSuchMethodException exception) {
            return methodSignature.getMethod();
        }
    }
}
