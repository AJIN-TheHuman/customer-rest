package com.example.customerrest.controller;

import com.example.customerrest.ds.Customer;
import com.example.customerrest.ds.CustomerDto;
import com.example.customerrest.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;




    @GetMapping("/creation")
    public String init(){

        List.of(
                new CustomerDto(null,"TH","Thomas","Hardy","thomas123@gmail.com"),
                new CustomerDto(null,"JD","John","Doe","john123@gmail.com"),
                new CustomerDto(null,"JW","John","William","william123@gmail.com")
        )
                .forEach(customerService::saveCustomer);

        return "success";

    }

    //curl -X POST -H "Content-Type: application/json" -d '{"id":null,"code":"AB","first_name":"Ashly","last_name":"Burn","email":"ashly123@gmail.com"}' localhost:8080/customers
    @PostMapping(value = "/customers",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> saveCustomer(@RequestBody CustomerDto customerDto){
      CustomerDto savedCustomer = customerService.saveCustomer(customerDto);

      return ResponseEntity.created(URI.create("http://localhost:8080/customers"))
              .body(customerDto);
    }


    @GetMapping(value = "/customers/all",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerDto> listAllCustomers(){
        return customerService.listCustomers();
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDto>> listCustomers(){

        return ResponseEntity
                .accepted()
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(customerService.listCustomers());
    }


    @GetMapping("/customers/customer")
    public ResponseEntity<CustomerDto> customerById(@RequestParam("id") int id){
        return ResponseEntity
                .ok()
                .body(customerService.findCustomerById(id));
    }

    @PatchMapping(value = "/customers/customer",consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<CustomerDto> changeCode(@RequestBody String code, @RequestParam("id") int id){
        CustomerDto customerDto= customerService.changeCode(id,code);
        return ResponseEntity
                .ok()
                .body(customerDto);
    }

    @PutMapping(value = "/customers/customer",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> updateCustomer(@RequestBody CustomerDto customerDto){
        CustomerDto customer = customerService
                .updateCustomer(customerDto);
        return ResponseEntity
                .ok()
                .body(customer);

    }

    //curl -X DELETE "http://localhost:8080/customers/customer?id=4"
    @DeleteMapping("/customers/customer")
    public ResponseEntity deleteCustomer(@RequestParam("id") int id){
        customerService.deleteCustomer(id);
        return ResponseEntity
                .notFound().build();
    }

}
