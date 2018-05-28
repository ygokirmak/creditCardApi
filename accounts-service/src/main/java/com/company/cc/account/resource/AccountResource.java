package com.company.cc.account.resource;


import com.company.cc.account.exceptions.EntityAlreadyExistsException;
import com.company.cc.account.exceptions.EntityNotFoundException;
import com.company.cc.account.exceptions.ServiceCommunicationException;
import com.company.cc.account.service.AccountService;
import com.company.cc.shared.AccountDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final AccountService accountService;

    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * GET  /accounts : get all the accounts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of deals in body
     */
    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDTO>> getAccounts(@RequestParam(required = true) Long customerId) throws ServiceCommunicationException {
        log.debug("REST request to get a page of Accounts");
        List<AccountDTO> result = accountService.getAccounts(customerId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * POST  /accounts : Create a new customer.
     *
     * @param accountDTO the accountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountDTO, or with status 400 (Bad Request) if the stage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/accounts")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO) throws URISyntaxException, EntityAlreadyExistsException, ServiceCommunicationException {
        log.debug("REST request to create account : {}", accountDTO);

        AccountDTO result = accountService.create(accountDTO);
        return ResponseEntity.created(new URI("/api/accounts/" + result.getId()))
            .body(result);
    }

    /**
     * GET  /accounts/:id : get the "id" customer.
     *
     * @param id the id of the accountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountDTO, or with status 404 (Not Found)
     */
    @GetMapping("/accounts/{id}")
    public ResponseEntity getAccount(@PathVariable Long id) throws EntityNotFoundException, ServiceCommunicationException {
        log.debug("REST request to get Account : {}", id);
        AccountDTO accountDTO = accountService.getOne(id);

        if( accountDTO == null ){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }else{
            return ResponseEntity.ok().body(accountDTO);
        }
    }

}