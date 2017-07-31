package nl.cybergert.product.domain;

import javax.persistence.Basic;

// import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
// import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import nl.cybergert.product.actions.CreateProductCommand;
import nl.cybergert.product.actions.ProductCreatedEvent;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

// TODO welke repo te gebruiken, de jpaProductRepository of ProductRepositroy? 


@Entity
@Aggregate(repository = "jpaProductRepository")
public class Product {
	
	public Product() {
		super();
	}

	@CommandHandler
	public Product(CreateProductCommand command){
		apply(new ProductCreatedEvent(command.getCode(), command.getDescription()));
	}

	@EventSourcingHandler
	public void on(ProductCreatedEvent event){
		this.code = event.getCode();
	}
	
	@Id
	@AggregateIdentifier
	private String code;
	// private UUID uuid; // aggregate identifier that does not collide ref:AXON 
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@Basic
	private String description;
}
