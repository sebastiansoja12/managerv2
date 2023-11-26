package com.warehouse.mail;


import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;

@ComponentScan(basePackages = { "com.warehouse.mail" })
public class MailTestConfiguration {

    @MockBean
    public JavaMailSender mailSender;
}
