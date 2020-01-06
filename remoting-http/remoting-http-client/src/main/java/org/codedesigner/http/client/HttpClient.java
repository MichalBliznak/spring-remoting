package org.codedesigner.http.client;

import org.codedesigner.remoting.api.BookingException;
import org.codedesigner.remoting.api.CabBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static java.lang.System.out;

@SpringBootApplication
@EnableScheduling
public class HttpClient {

    @Autowired
    private ApplicationContext appContext;

    @Bean
    public HttpInvokerProxyFactoryBean invoker() {
        HttpInvokerProxyFactoryBean invoker = new HttpInvokerProxyFactoryBean();
        invoker.setServiceUrl("http://localhost:8080/booking");
        invoker.setServiceInterface(CabBookingService.class);
        return invoker;
    }

    @Scheduled(fixedRate = 5000)
    public void askBooking() {
        CabBookingService service = appContext.getBean(CabBookingService.class);
        try {
            out.println(service.bookRide("13 Seagate Blvd, Key Largo, FL 33037"));
        } catch (BookingException e) {
            out.println("Ooops, " + e.getMessage() + "...");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(HttpClient.class, args);
    }

}
