package com.warehouse.exceptioncatcher.domain.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExceptionInformation {

    private String message;

    private String stackTrace;

    private int code;

    private int lineNumber;

    private String methodName;

    private String className;

    private String moduleVersion;

    private String moduleName;

    private String classLoaderName;

    private String fileName;
}
