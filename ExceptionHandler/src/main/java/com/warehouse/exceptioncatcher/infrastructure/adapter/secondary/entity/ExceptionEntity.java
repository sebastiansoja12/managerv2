package com.warehouse.exceptioncatcher.infrastructure.adapter.secondary.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@Table(name = "exception_information")
public class ExceptionEntity {

    @Id
    private Long id;

    @Column(name = "parcel_id")
    private Long parcelId;

    @Column(name = "message")
    private String message;

    @Column(name = "stack_trace")
    private String stackTrace;

    @Column(name = "code")
    private int code;

    @Column(name = "line_number")
    private int lineNumber;

    @Column(name = "method_name")
    private String methodName;

    @Column(name = "class_name")
    private String className;

    @Column(name = "module_version")
    private String moduleVersion;

    @Column(name = "module_name")
    private String moduleName;

    @Column(name = "class_loader_name")
    private String classLoaderName;

    @Column(name = "file_name")
    private String fileName;
}
