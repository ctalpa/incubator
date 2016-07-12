package com.ctalpa.prxmen.web.rest.mapper;

import com.ctalpa.prxmen.domain.*;
import com.ctalpa.prxmen.web.rest.dto.CustomerDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Customer and its DTO CustomerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomerMapper {

    CustomerDTO customerToCustomerDTO(Customer customer);

    List<CustomerDTO> customersToCustomerDTOs(List<Customer> customers);

    @Mapping(target = "contacts", ignore = true)
    @Mapping(target = "locations", ignore = true)
    @Mapping(target = "customerToJobHistories", ignore = true)
    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    List<Customer> customerDTOsToCustomers(List<CustomerDTO> customerDTOs);
}
