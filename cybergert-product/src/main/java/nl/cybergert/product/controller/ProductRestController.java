package nl.cybergert.product.controller;

import org.springframework.web.bind.annotation.RestController;

import nl.cybergert.product.actions.CreateProductCommand;
import nl.cybergert.product.domain.Product;
import nl.cybergert.product.domain.ProductRepository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@EntityScan({"nl.cybergert.product.domain","org.axonframework.eventsourcing.eventstore.jpa","org.axonframework.eventhandling.saga.repository.jpa","org.axonframework.eventhandling.tokenstore.jpa"})
@RestController
public class ProductRestController {
	
	private static final org.slf4j.Logger LOG =  org.slf4j.LoggerFactory.getLogger(ProductRestController.class);

	// TODO This constructor initializes the commandGateway, Autowire like for repo is not enough? 
	public ProductRestController(CommandGateway commandGateway, ProductRepository repo) {
		this.commandGateway = commandGateway;
		this.repo = repo;
	}

	private final CommandGateway commandGateway;
	
    @RequestMapping("/")
    public String index() {
        return "product index";
    }
    
	@Autowired
	private ProductRepository repo;	
	
	@GetMapping("/api")
	public String showAPI(){
		return "/api/admin/createdummy create dummy product and //api/product/1 returns it";
	}
	
	@GetMapping("/api/product")
	public List<Product> products(){
		return repo.findAll();
	}
	
	@PostMapping("/api/product")
	public @ResponseBody String productSubmit(@RequestBody Product product) {
		
		if (product == null) {
			System.out.println("product is null");
		}
		System.out.println("POST to create invoked for beer " + product.getDescription());
		repo.save(product);
		return "saved product :" + product.getDescription();
    }
	
	@GetMapping("/admin/createdummy")
	public String createProduct() {
		Product product = new Product();
		product.setCode("C1");
		product.setDescription("C1 Description");
		repo.save(product);
		return "Dummy created";
	}
	
	@GetMapping("/admin/dummycommand/{code}")
	public CompletableFuture<String> createProductCommand(@PathVariable("code") String code) {
	// public CompletableFuture<String> createProductCommand(@RequestBody Map<String, String> request) {
    //  AddProductToCatalogCommand command = new AddProductToCatalogCommand(request.get("id"), request.get("name"));
		CreateProductCommand command = new CreateProductCommand(code, new String(code + "-description"));
        LOG.info("Executing command: {}", command);
        return commandGateway.send(command);
    }
	
	@GetMapping("api/product/{code}")
	public Product product(@PathVariable("code") String code){
		System.out.println("Product");
		return repo.findByCode(code);
	}
	
	@PostMapping("api/product/{code}")
	public @ResponseBody String updateSubmit(@RequestBody Product product, @PathVariable("code") String code ) {
		Product previous = repo.getOne(code); // later
		System.out.println("Product with " + code + " updated");
		repo.save(product);
		return "Done"; 
	}
}