package com.warehouse.documentation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.warehouse.documentation")
public class DocumentationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocumentationApplication.class, args);
    }
}
