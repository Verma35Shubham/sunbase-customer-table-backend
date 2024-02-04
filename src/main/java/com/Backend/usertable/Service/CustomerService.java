package com.Backend.usertable.Service;

import com.Backend.usertable.Exceptions.CustomerException;
import com.Backend.usertable.Models.Customer;
import com.Backend.usertable.RequestDTO.AddCustomerRequestDTO;
import com.Backend.usertable.ResponseDTO.CustomerResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CustomerService {

    public String addCustomer(AddCustomerRequestDTO addCustomerRequestDTO) throws CustomerException;

    public CustomerResponseDTO findCustomerById(int id) throws CustomerException;

    public Page<Customer> findAllCustomer(Pageable pageable);

    public String deleteCustomerInfo(int customerId) throws CustomerException;

    public List<CustomerResponseDTO> findCustomerByCategory(String category);

    public CustomerResponseDTO updateCustomer(int id, AddCustomerRequestDTO addCustomerRequestDTO) throws CustomerException;

    List<Customer> searchByCategory(String category);

    List<Customer> addCustomerFromApi();
}
