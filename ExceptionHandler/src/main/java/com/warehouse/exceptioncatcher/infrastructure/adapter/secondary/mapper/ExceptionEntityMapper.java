package com.warehouse.exceptioncatcher.infrastructure.adapter.secondary.mapper;

import java.util.Arrays;

import org.mapstruct.Mapper;

import com.warehouse.exceptioncatcher.infrastructure.adapter.secondary.entity.ExceptionEntity;
import com.warehouse.exceptionhandler.exception.RestException;

@Mapper
public interface ExceptionEntityMapper {
    default ExceptionEntity map(RestException ex) {
        final ExceptionEntity exceptionEntity = new ExceptionEntity();
        exceptionEntity.setCode(ex.getCode());
        exceptionEntity.setMessage(ex.getMessage());
        /*exceptionEntity.setClassLoaderName(Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::getClassLoaderName)
                .collect(Collectors.joining()));*/
        exceptionEntity.setLineNumber(
                Arrays.stream(ex.getStackTrace()).map(StackTraceElement::getLineNumber).findAny().orElse(0));
        exceptionEntity.setClassName(
                Arrays.stream(ex.getStackTrace()).map(StackTraceElement::getClassName).toString());
        exceptionEntity.setModuleName(
                Arrays.stream(ex.getStackTrace()).map(StackTraceElement::getModuleName).toString());
        exceptionEntity.setMethodName(Arrays.stream(ex.getStackTrace()).map(StackTraceElement::getMethodName).toString());
        return exceptionEntity;
    }
}
