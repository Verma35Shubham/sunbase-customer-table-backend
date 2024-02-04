package com.Backend.usertable.Service.ServiceImpl;

import com.Backend.usertable.Exceptions.CustomerException;
import com.Backend.usertable.Models.Customer;
import com.Backend.usertable.Repository.CustomerRepository;
import com.Backend.usertable.RequestDTO.AddCustomerRequestDTO;
import com.Backend.usertable.ResponseDTO.CustomerResponseDTO;
import com.Backend.usertable.Service.CustomerService;
import com.Backend.usertable.config.JWTConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${sunbasedata.api.auth.url}")
    private String authUrl; // = "sunbasedata.api.data.url";

    @Value("${sunbasedata.api.data.url}")
    private String dataUrl; // = "sunbasedata.api.data.url";

    @Override
    public String addCustomer(AddCustomerRequestDTO addCustomerRequestDTO) throws CustomerException {
        Customer isExist = customerRepository.findByEmail(addCustomerRequestDTO.getEmail());

        if(isExist != null){
            throw new CustomerException("this customer email already exists!!");
        }
        Customer customer = Customer.builder()
                .firstname(addCustomerRequestDTO.getFirstName())
                .lastname(addCustomerRequestDTO.getLastName())
                .email(addCustomerRequestDTO.getEmail())
                .street(addCustomerRequestDTO.getStreet())
                .address(addCustomerRequestDTO.getAddress())
                .city(addCustomerRequestDTO.getCity())
                .state(addCustomerRequestDTO.getState())
                .phone(addCustomerRequestDTO.getPhone())
                .build();

        return  customer.getFirstname() + " added successfully";
    }

    @Override
    public CustomerResponseDTO findCustomerById(int id) throws CustomerException {
        Customer customer = customerRepository.findById(id).get();
        if(customer==null){
            throw new CustomerException("This id is not found!!");
        }
        CustomerResponseDTO customerResponseDTO = CustomerResponseDTO.builder()
                .firstName(customer.getFirstname())
                .lastName(customer.getLastname())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .phone(customer.getPhone())
                .street(customer.getStreet())
                .city(customer.getCity())
                .state(customer.getState()).build();

        return customerResponseDTO;
    }


    @Override
    public Page<Customer> findAllCustomer(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public String deleteCustomerInfo(int customerId) throws CustomerException {
        Customer customer;
        try{
            customer =customerRepository.findById(customerId).get();
        }catch (Exception e){
            throw new CustomerException("Customer not Exist !!");
        }

        String message = "Customer with" + customer.getEmail() + "has been deleted.";

        customerRepository.deleteById(customer.getId());

        return message;
    }

    @Override
    public List<CustomerResponseDTO> findCustomerByCategory(String category) {
        List<Customer> list = customerRepository.searchByCategory(category);
        List<CustomerResponseDTO> categoryByList = new ArrayList<>();

        for(Customer customer : list){
            CustomerResponseDTO customerResponseDTO = CustomerResponseDTO.builder()
                    .firstName(customer.getFirstname())
                    .lastName(customer.getLastname())
                    .email(customer.getEmail())
                    .address(customer.getAddress())
                    .street(customer.getStreet())
                    .phone(customer.getPhone())
                    .city(customer.getCity())
                    .state(customer.getState()).build();

            categoryByList.add(customerResponseDTO);
        }
        return categoryByList;
    }

    @Override
    public CustomerResponseDTO updateCustomer(int id, AddCustomerRequestDTO addCustomerRequestDTO) throws CustomerException {
        Customer updatedCustomer = null;
        try{
            Customer customer = customerRepository.findById(id).get();

            if(customer != null){
                updatedCustomer = Customer.builder()
                        .firstname(addCustomerRequestDTO.getFirstName())
                        .lastname(addCustomerRequestDTO.getLastName())
                        .email(addCustomerRequestDTO.getEmail())
                        .phone(addCustomerRequestDTO.getPhone())
                        .state(addCustomerRequestDTO.getState())
                        .street(addCustomerRequestDTO.getStreet())
                        .address(addCustomerRequestDTO.getCity())
                        .city(addCustomerRequestDTO.getCity())
                        .build();
            }
        }catch (Exception e){
            throw new CustomerException("Customer not found!!");
        }
        customerRepository.save(updatedCustomer);

        CustomerResponseDTO customerResponseDTO = CustomerResponseDTO.builder()
                .firstName(updatedCustomer.getFirstname())
                .lastName(updatedCustomer.getLastname())
                .email(updatedCustomer.getEmail())
                .state(updatedCustomer.getState())
                .street(updatedCustomer.getStreet())
                .city(updatedCustomer.getCity())
                .phone(updatedCustomer.getPhone())
                .address(updatedCustomer.getAddress())
                .build();

        return customerResponseDTO;
    }

    @Override
    public List<Customer> searchByCategory(String category) {
        return customerRepository.searchByCategory(category);
    }

    @Override
    public List<Customer> addCustomerFromApi() {
        String url = "https://qa.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list";
        HttpHeaders httpHeaders = new HttpHeaders();
        String jwtToken = "dGVzdEBzdW5iYXNlZGF0YS5jb206VGVzdEAxMjM=";
        httpHeaders.set(JWTConstant.JWT_HEADER, "Bearer "+jwtToken);
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        RestTemplate restTemplate1 = new RestTemplate();
        ResponseEntity<Customer[]> responseEntity = restTemplate1.exchange(url, HttpMethod.GET, httpEntity, Customer[].class);

        Customer[] customers = responseEntity.getBody();
        for(Customer customer : customers){
            customerRepository.save(customer);
        }
        return List.of(customers);
    }

    private List<Customer> extractingData(String jwtToken){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(JWTConstant.JWT_HEADER, "Bearer"+jwtToken);

        try{
            ResponseEntity<Customer[]> responseEntity = restTemplate.exchange(dataUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), Customer[].class);
            return Arrays.asList(responseEntity.getBody());
        }catch(HttpStatusCodeException httpStatusCodeException){
            if(httpStatusCodeException.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
                throw new RuntimeException("Unacuthorized Access!!");
            } else if (httpStatusCodeException.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new RuntimeException("Invalid call!!");
            }
            throw httpStatusCodeException;
        }
    }
}
