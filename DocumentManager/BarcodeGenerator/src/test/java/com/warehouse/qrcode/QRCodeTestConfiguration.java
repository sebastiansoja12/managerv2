package com.warehouse.qrcode;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = { "com.warehouse.qrcode" })
@EntityScan(basePackages = { "com.warehouse.qrcode" })
@EnableJpaRepositories(basePackages = { "com.warehouse.qrcode" })
public class QRCodeTestConfiguration {
}
