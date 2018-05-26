package com.company.cc.account.resource;


import com.company.cc.account.exceptions.EntityAlreadyExistsException;
import com.company.cc.account.exceptions.EntityNotFoundException;
import com.company.cc.account.exceptions.TransactionCreationException;
import com.company.cc.account.exceptions.TransactionFetchException;
import com.company.cc.account.resource.utils.PaginationUtil;
import com.company.cc.account.service.AccountService;
import com.company.cc.account.service.dto.AccountDTO;
import com.company.cc.account.service.dto.NewAccountDTO;
import com.sun.net.httpserver.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<List<AccountDTO>> getAccounts(Pageable pageable) {
        log.debug("REST request to get a page of Accounts");
        Page<AccountDTO> page = accountService.getAccounts(pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,"/api/accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * POST  /accounts : Create a new account.
     *
     * @param newAccountDTO the accountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountDTO, or with status 400 (Bad Request) if the stage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/accounts")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody NewAccountDTO newAccountDTO) throws URISyntaxException, EntityAlreadyExistsException, TransactionCreationException {
        log.debug("REST request to create account : {}", newAccountDTO);

        AccountDTO result = accountService.create(newAccountDTO);
        return ResponseEntity.created(new URI("/api/accounts/" + result.getId()))
            .body(result);
    }

    /**
     * GET  /accounts/:id : get the "id" account.
     *
     * @param id the id of the accountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountDTO, or with status 404 (Not Found)
     */
    @GetMapping("/accounts/{id}")
    public ResponseEntity getAccount(@PathVariable Long id) throws EntityNotFoundException, TransactionFetchException {
        log.debug("REST request to get Account : {}", id);
        AccountDTO accountDTO = accountService.getOne(id);

        if( accountDTO == null ){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }else{
            return ResponseEntity.ok().body(accountDTO);
        }
    }

    /**
     * DELETE  /accounts/:id : delete the "id" account.
     *
     * @param id the id of the accountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<AccountDTO> deleteAccount(@PathVariable Long id) throws EntityNotFoundException {
        log.debug("REST request to delete Stage : {}", id);
        accountService.delete(id);

        AccountDTO deletedAccount = new AccountDTO();
        deletedAccount.setId(id);

        return ResponseEntity.ok()
            .body(deletedAccount);

    }
}