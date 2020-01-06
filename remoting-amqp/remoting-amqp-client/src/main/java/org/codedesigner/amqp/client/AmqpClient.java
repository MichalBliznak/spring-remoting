package org.codedesigner.amqp.client;

import org.codedesigner.remoting.api.BookingException;
import org.codedesigner.remoting.api.CabBookingService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.remoting.client.AmqpProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static java.lang.System.out;

@SpringBootApplication
@EnableScheduling
public class AmqpClient {

    @Autowired
    private ApplicationContext appContext;

    @Bean
    Queue queue() {
        return new Queue(CabBookingService.class.getSimpleName());
    }

    @Bean
    AmqpProxyFactoryBean amqpFactoryBean(AmqpTemplate amqpTemplate) {
        AmqpProxyFactoryBean factoryBean = new AmqpProxyFactoryBean();
        factoryBean.setServiceInterface(CabBookingService.class);
        factoryBean.setAmqpTemplate(amqpTemplate);
        return factoryBean;
    }

    @Bean
    Exchange directExchange(Queue queue) {
        DirectExchange exchange = new DirectExchange("remoting.exchange");
        BindingBuilder.bind(queue).to(exchange).with(CabBookingService.class.getSimpleName());
        return exchange;
    }

    @Bean
    RabbitTemplate amqpTemplate(ConnectionFactory factory) {
        RabbitTemplate template = new RabbitTemplate(factory);
        template.setRoutingKey(CabBookingService.class.getSimpleName());
        template.setExchange("remoting.exchange");
        return template;
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
        SpringApplication.run(AmqpClient.class, args);
    }

}
