package com.company.cc.customer.resource;


import com.company.cc.customer.exceptions.EntityNotFoundException;
import com.company.cc.customer.exceptions.ServiceCommunicationException;
import com.company.cc.customer.service.CustomerService;
import com.company.cc.customer.service.dto.CustomerDTO;
import com.company.cc.customer.service.enumeration.CustomerView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerResource {

    private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    private final CustomerService customerService;

    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * GET  /customers : get all the customers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of customers in body
     */
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> getCustomers(@RequestParam(required = false, defaultValue = "BASIC") CustomerView view) {
        log.debug("REST request to get a page of Customers");
        List<CustomerDTO> result = customerService.getCustomers(view);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * GET  /customers/:id : get the "id" customer.
     *
     * @param id the id of the customerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/customers/{id}")
    public ResponseEntity getCustomer(@PathVariable Long id, @RequestParam(required = false, defaultValue = "BASIC") CustomerView view) throws EntityNotFoundException, ServiceCommunicationException {
        log.debug("REST request to get Customer : {}", id);
        CustomerDTO customerDTO = customerService.getOne(view, id);

        if( customerDTO == null ){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }else{
            return ResponseEntity.ok().body(customerDTO);
        }
    }

}