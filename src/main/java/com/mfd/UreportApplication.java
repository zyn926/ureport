package com.mfd;

import com.bstek.ureport.console.UReportServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication

@EnableAutoConfiguration
@ImportResource("classpath:ureport-context.xml")
public class UreportApplication {

    public static void main(String[] args) {
        SpringApplication.run(UreportApplication.class, args);
    }

    @Bean
    public ServletRegistrationBean buildUreportServlet() {
        return new ServletRegistrationBean(new UReportServlet(), "/ureport/*");

    }
}


