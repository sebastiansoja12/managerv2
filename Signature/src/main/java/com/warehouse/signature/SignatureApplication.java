package com.warehouse.signature;


@EnableConfigurationProperties
@EnableCaching
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.warehouse.signature")
@EntityScan(basePackages = {"com.warehouse.signature"})
@EnableJpaRepositories(basePackages = {"com.warehouse.signature"})
@ConfigurationPropertiesScan("com.warehouse.signature")
public class SignatureApplication {

    public static void main(String[] args) {
        SpringApplication.run(SignatureApplication.class, args);
    }

}
