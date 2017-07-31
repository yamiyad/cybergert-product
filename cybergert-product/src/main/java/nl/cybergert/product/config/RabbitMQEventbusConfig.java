package nl.cybergert.product.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;

@Configuration
public class RabbitMQEventbusConfig {
	
	@Value("${axon.amqp.exchange:ProductEvents}")  // : is default waarde 
    String exchangeName;

    //    Add @Bean annotated exchange() method to declare a spring amqp Exchange
    //    Return the exchange from ExchangeBuilder.fanoutExchange(â€œComplaintEventsâ€�).build();

    @Bean
    public Exchange exchange(){
        return ExchangeBuilder.fanoutExchange(exchangeName).build();
    }

    //    Add @Bean annotated queue() method to declate a Queue
    //    Return the queue from QueueBuilder.durable(â€œComplaintEventsâ€�).build()

    @Bean
    public Queue queue(){
        return QueueBuilder.durable(exchangeName).build();
    }

    //    Add @Bean annotated binding() method to declare a Binding
    //    Return the binding from BindingBuilder.bind(queue()).to(exchange()).with(â€œ*â€�).noargs()

    @Bean
    public Binding binding(Queue queue, Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("*").noargs();
    }

    //    Add @Autowired method to configure(AmqpAdmin admin)
    //    Make admin.declareExchange(exchange());
    //    Make admin.declareQueue(queue());
    //    Make admin.declareBinding(binding());

    @Autowired
    public void configure(AmqpAdmin amqpAdmin, Exchange exchange, Queue queue, Binding binding){
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(binding);
    }
}
