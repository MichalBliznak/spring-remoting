package org.codedesigner.amqp.server;

import org.codedesigner.remoting.api.CabBookingService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.remoting.service.AmqpInvokerServiceExporter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AmqpServer {

    /*
    Please note that
    - CachingConnectionFactory
    - RabbitAdmin
    - AmqpTemplate
    are automatically declared by SpringBoot.
     */

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("remoting.exchange");
    }

    @Bean
    public Queue queue() {
        return new Queue(CabBookingService.class.getSimpleName());
    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(CabBookingService.class.getSimpleName()).noargs();
    }

    @Bean
    AmqpInvokerServiceExporter exporter(CabBookingService implementation, AmqpTemplate template) {
        AmqpInvokerServiceExporter exporter = new AmqpInvokerServiceExporter();
        exporter.setServiceInterface(CabBookingService.class);
        exporter.setService(implementation);
        exporter.setAmqpTemplate(template);
        return exporter;
    }

    @Bean
    SimpleMessageListenerContainer listener(ConnectionFactory factory, AmqpInvokerServiceExporter exporter, Queue queue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(factory);
        container.setMessageListener(exporter);
        container.setQueueNames(queue.getName());
        return container;
    }

    public static void main(String[] args) {
        SpringApplication.run(AmqpServer.class, args);
    }

}