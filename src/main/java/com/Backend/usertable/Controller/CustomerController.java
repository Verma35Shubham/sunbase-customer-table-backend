package com.Backend.usertable.Controller;

import com.Backend.usertable.Exceptions.CustomerException;
import com.Backend.usertable.Models.Customer;
import com.Backend.usertable.RequestDTO.AddCustomerRequestDTO;
import com.Backend.usertable.ResponseDTO.CustomerResponseDTO;
import com.Backend.usertable.Service.ServiceImpl.AuthServiceImpl;
import com.Backend.usertable.Service.ServiceImpl.CustomerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private AuthServiceImpl authService;
    @Autowired
    private CustomerServiceImpl customerService;

    @PostMapping("/new/add")
    public String addCustomer(AddCustomerRequestDTO addCustomerRequestDTO) throws CustomerException {
        String response = customerService.addCustomer(addCustomerRequestDTO);
        return response;
    }
    @GetMapping("/info/list")
    public ResponseEntity<Page<Customer>> findAllCustomer(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size){
        Page<Customer> customers = customerService.findAllCustomer(PageRequest.of(page, size));
        return ResponseEntity.ok(customers);
    }
    @GetMapping("/find/customer/id/{customerId}")
    public ResponseEntity findCustomerById(@PathVariable int customerId) throws CustomerException{
        CustomerResponseDTO customerResponseDTO;
        try{
            customerResponseDTO = customerService.findCustomerById(customerId);
        }catch (CustomerException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(customerResponseDTO, HttpStatus.ACCEPTED);
    }
    @GetMapping("/search/customer/category")
    public List<Customer> searchByCategory(@RequestParam String category){
        return customerService.searchByCategory(category);
    }
    @DeleteMapping("/delete/customer/id/{customerId}")
    public ResponseEntity deleteCustomerInfo(@PathVariable int customerId) throws CustomerException{
        String response;
        try{
            response = customerService.deleteCustomerInfo(customerId);
        }catch (CustomerException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(response, HttpStatus.ACCEPTED);
    }
    @GetMapping("/find/customers/category")
    public ResponseEntity findCustomerByCategory(@RequestParam String category){
        List<CustomerResponseDTO> customerResponseDTOS = customerService.findCustomerByCategory(category);
        return ResponseEntity.ok(customerResponseDTOS);
    }
    @GetMapping("/add/from/api")
    public List<Customer> addfromApi(){
        return customerService.addCustomerFromApi();
    }
    @PutMapping("/update/customer/{customerId}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable int customerId, @RequestBody AddCustomerRequestDTO addCustomerRequestDTO) throws CustomerException{
        CustomerResponseDTO customerResponseDTO;

        try{
            customerResponseDTO = customerService.updateCustomer(customerId, addCustomerRequestDTO);
        }catch (CustomerException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(customerResponseDTO, HttpStatus.ACCEPTED);
    }



}
