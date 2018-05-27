package com.company.cc.customer.service.mapper;

import com.company.cc.customer.domain.Customer;
import com.company.cc.customer.service.dto.CustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {})
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {

    @Mappings({
            @Mapping(target = "balance", ignore = true),
            @Mapping(target = "accounts", ignore = true)
    })
    CustomerDTO toDto(Customer customer);

    Customer toEntity(CustomerDTO customerDTO);

    default Customer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }
}

