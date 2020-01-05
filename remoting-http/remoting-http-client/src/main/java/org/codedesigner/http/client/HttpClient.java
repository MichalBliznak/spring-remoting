package org.codedesigner.http.client;

import org.codedesigner.remoting.api.BookingException;
import org.codedesigner.remoting.api.CabBookingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import static java.lang.System.out;

@SpringBootApplication
public class HttpClient {

    @Bean
    public HttpInvokerProxyFactoryBean invoker() {
        HttpInvokerProxyFactoryBean invoker = new HttpInvokerProxyFactoryBean();
        invoker.setServiceUrl("http://localhost:8080/booking");
        invoker.setServiceInterface(CabBookingService.class);
        return invoker;
    }

    public static void main(String[] args) throws BookingException {
        CabBookingService service = SpringApplication.run(HttpClient.class, args).getBean(CabBookingService.class);
        out.println(service.bookRide("13 Seagate Blvd, Key Largo, FL 33037"));
    }

}
