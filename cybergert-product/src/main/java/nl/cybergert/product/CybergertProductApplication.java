package nl.cybergert.product;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.model.GenericJpaRepository;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.common.jpa.ContainerManagedEntityManagerProvider;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.ExceptionHandler;
import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;


import org.axonframework.commandhandling.AsynchronousCommandBus;

import nl.cybergert.product.actions.CreateProductCommand;
import nl.cybergert.product.domain.Product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@EnableAutoConfiguration
@SpringBootApplication
public class CybergertProductApplication {
		
	
	private static final Logger LOG = LoggerFactory.getLogger(CybergertProductApplication.class);

	
	public static void main(String[] args) {
		ConfigurableApplicationContext config = SpringApplication.run(CybergertProductApplication.class, args);
//		CommandBus commandbus = config.getBean(CommandBus.class);
//		commandbus.dispatch(asCommandMessage(new CreateProductCommand("6000", "Red IPA")), new CommandCallback<Object, Object>() {
//
//			@Override
//			public void onFailure(CommandMessage<?> commandMessage, Throwable cause) {
//				// Watch Asynchronous 
//				LOG.info("failure " + cause.getMessage());
//			}
//
//			@Override
//			public void onSuccess(CommandMessage<?> commandMessage, Object result) {
//				// hoera
//				LOG.info("success");
//			}
//		});
	}

	// All the events generated are persisted into the event store, here a relational database for the event store. 
	@Bean
	public Repository<Product> jpaProductRepository(EventBus eventBus){
		return new GenericJpaRepository<>(entityManagerProvider(), Product.class, eventBus);
	}
	
	// entityManagerFactory (the bean that provides the database connections) 
	@Bean
	public EntityManagerProvider entityManagerProvider() {
		return new ContainerManagedEntityManagerProvider();
	}
	
	@Bean
	public EventStorageEngine eventStorageEngine(){
		return new InMemoryEventStorageEngine();
	}
	
	// Als je tx gebruikt, gebruik je geen CommandBus of je moet hem volledig configureren. 
	// You need a transaction manager (to handle ACID transactions) to ensure that every command is 
	// processed and that the events are generated and made persistent in the event store.

	@Bean
	public TransactionManager axonTransactionManager(PlatformTransactionManager tx) {
		return new SpringTransactionManager(tx);
	}
	
	
//	@Bean
//	public CommandBus commandbus() {
//		return new AsynchronousCommandBus();
//	}
	
//	The event bus is the mechanism that dispatches the events to their listeners. Each event can have one or more listeners associated with it
//	2 implementations of event bus The SimpleEventBus is applicable in most cases where dispatching is done synchronously and locally (such as in a single JVM).
//	The ClusteringEventBus is suitable when your application requires Events to be published across multiple JVMs (that is, in a cloud ecosystem). The dispatching is therefore asynchronous and distributed. 
//	ClusteringEventBus can define an EventBusTerminal as well. That way, you can distribute events to transmit events to an AMQP-compatible message broker, such as Rabbit MQ.
//	It is possible to specify the type of serialization that can be in XML format, such as that shown in the example, or JSON format (using org.axonframework.serializer.json.JacksonSerializer), Java object serialization.

	
	
	
	// afvangen van database errors zodra web app start
	@ExceptionHandler(Exception.class)
	public void anyException(Exception exc) {
	        System.out.println("FOUT exception:" + exc.getMessage());
	}
}
