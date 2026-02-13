package com.warehouse.logistics.configuration;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class LogisticsSoapWebServiceConfiguration {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> logisticsMessageDispatcherServlet(
            final ApplicationContext applicationContext) {
        final MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "deliveries")
    public DefaultWsdl11Definition deliveriesWsdlDefinition(final XsdSchema terminalSchema) {
        final DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("LogisticsDispatchPort");
        definition.setLocationUri("/ws");
        definition.setRequestSuffix("Request");
        definition.setResponseSuffix("Response");
        definition.setSchema(terminalSchema);
        return definition;
    }

    @Bean
    public XsdSchema terminalSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/terminal-request.xsd"));
    }
}
