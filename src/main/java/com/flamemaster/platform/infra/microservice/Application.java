package com.flamemaster.platform.infra.microservice;

import com.flamemaster.platform.infra.microservice.config.InvoiceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class);

        System.out.println(context.getBean(InvoiceConfig.class));
    }

}
