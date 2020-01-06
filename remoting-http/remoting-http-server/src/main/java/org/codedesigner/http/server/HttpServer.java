package org.codedesigner.http.server;

import org.codedesigner.remoting.api.CabBookingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

@SpringBootApplication
public class HttpServer {

    @Bean(name = "/booking")
    HttpInvokerServiceExporter accountService(CabBookingService service) {
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setService(service);
        exporter.setServiceInterface(CabBookingService.class);
        return exporter;
    }

    public static void main(String[] args) {
        SpringApplication.run(HttpServer.class, args);
    }

}