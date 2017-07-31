package nl.cybergert.product.test;

import org.junit.Before;
import org.junit.Test;

import nl.cybergert.product.actions.CreateProductCommand;
import nl.cybergert.product.actions.ProductCreatedEvent;
import nl.cybergert.product.domain.Product;

import org.axonframework.test.aggregate.*;

public class ProductTest {
	
	private FixtureConfiguration<Product> fixture;
	
	@Before
	public void setUp() throws Exception {
		fixture = new AggregateTestFixture(Product.class);
	}
	
	@Test
	public void testCreateProduct() throws Exception {
		fixture.givenNoPriorActivity()
		.when(new CreateProductCommand("4800", "Dwaes"))
		.expectEvents(new ProductCreatedEvent("4800","Dwaes"));
	}
}
