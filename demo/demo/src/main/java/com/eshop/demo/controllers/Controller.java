package com.eshop.demo.controllers;

import com.eshop.demo.entities.Customer;
import com.eshop.demo.entities.Order;
import com.eshop.demo.entities.Product;
import com.eshop.demo.repositories.CustomerRepository;
import com.eshop.demo.repositories.OrderRepository;
import com.eshop.demo.repositories.ProductRepository;
import com.eshop.demo.services.CustomerService;
import com.eshop.demo.services.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/")
    public String prova(){
        return new Customer("mario","rossi","m@mail.it").toString();
    }

    @PostMapping("/postCustomer")
    public void postCustomer(@RequestBody String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Customer customer = objectMapper.readValue(json, Customer.class);
            customerRepository.save(customer);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/getCustomers")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @PostMapping("/postProduct")
    public void postProduct(@RequestBody String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Product product = objectMapper.readValue(json, Product.class);
            productRepository.save(product);
            System.out.println("Insert: "+product.toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/getProducts")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @PatchMapping("/patchCustomer/{customer_id}")
    public void patchCustomer(@PathVariable(value = "customer_id") Long customer_id, @RequestBody String json){
        Customer oldCustomer = customerRepository.findById(customer_id).orElseThrow(()-> new RuntimeException("Customer not found for id: "+customer_id));
        System.out.println(oldCustomer.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Customer updatedCustomer = objectMapper.readValue(json, Customer.class);
            if(updatedCustomer.getFirstName()!=null)
                oldCustomer.setFirstName(updatedCustomer.getFirstName());
            if(updatedCustomer.getLastName()!=null)
                oldCustomer.setLastName(updatedCustomer.getLastName());
            if(updatedCustomer.getEmail() != null)
                oldCustomer.setEmail(updatedCustomer.getEmail());
            customerRepository.save(oldCustomer);
            System.out.println("Updated customer "+customer_id+": "+oldCustomer.toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/postOrder")
    public void postOrder(@RequestBody String json){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            long customer_id= jsonNode.get("customer_id").asLong();
            long product_id= jsonNode.get("product_id").asLong();
            Customer customer=customerRepository.findById(customer_id).orElseThrow(() -> new RuntimeException("Customer not found for id:" + customer_id));
            Product product=productRepository.findById(product_id).orElseThrow(()-> new RuntimeException("Product not found for id:" + product_id));
            System.out.println(customer.toString());
            System.out.println(product.toString());
            Order order= new Order(customer,product);
            orderRepository.save(order);
            System.out.println("Inserted: "+order.toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getOrders")
    public List<Order> getOrders(){
        return orderRepository.findAll();
    }


}
