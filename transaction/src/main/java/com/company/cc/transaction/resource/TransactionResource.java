package com.company.cc.transaction.resource;


import com.company.cc.transaction.exceptions.EntityAlreadyExistsException;
import com.company.cc.transaction.exceptions.EntityNotFoundException;
import com.company.cc.transaction.service.TransactionService;
import com.company.cc.transaction.service.dto.TransactionDTO;
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
public class TransactionResource {

    private final Logger log = LoggerFactory.getLogger(TransactionResource.class);

    private final TransactionService transactionService;

    public TransactionResource(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * POST  /transactions : Create a new transaction.
     *
     * @param transactionDTO the transactionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transactionDTO, or with status 400 (Bad Request) if the transaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transactions")
    public ResponseEntity<TransactionDTO> createAccount(@RequestBody TransactionDTO transactionDTO) throws URISyntaxException, EntityAlreadyExistsException {
        log.debug("REST request to create transaction : {}", transactionDTO);

        TransactionDTO result = transactionService.create(transactionDTO);
        return ResponseEntity.created(new URI("/api/transactions/" + result.getId()))
            .body(result);
    }

    /**
     * PUT  /accounts : Updates an existing account.
     *
     * @param transactionDTO the transactionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transactionDTO,
     * or with status 400 (Bad Request) if the transactionDTO is not valid,
     * or with status 500 (Internal Server Error) if the transactionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transactions")
    public ResponseEntity<TransactionDTO> updateAccount(@RequestBody TransactionDTO transactionDTO) throws URISyntaxException, EntityNotFoundException {
        log.debug("REST request to update Transaction : {}", transactionDTO);

        TransactionDTO result = transactionService.update(transactionDTO);
        return ResponseEntity.ok()
            .body(result);
    }

    /**
     * GET  /accounts/:id : get the "id" account.
     *
     * @param id the id of the accountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountDTO, or with status 404 (Not Found)
     */
    @GetMapping("/transactions/{id}")
    public ResponseEntity getTransaction(@PathVariable Long id) throws EntityNotFoundException {
        log.debug("REST request to get Account : {}", id);
        TransactionDTO transactionDTO = transactionService.findOne(id);

        if( transactionDTO == null ){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }else{
            return ResponseEntity.ok().body(transactionDTO);
        }
    }


    /**
     * GET  /deals : get all the deals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of deals in body
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactions(@RequestParam(required = false) String accountId, @RequestParam(required = false) Pageable pageable) {
        log.debug("REST request to get a page of Transactions");
        Page<TransactionDTO> page = transactionService.getByAccountId(accountId, pageable);
        return new ResponseEntity<>(page.getContent(), HttpStatus.OK);
    }


    /**
     * DELETE  /transactions/:id : delete the "id" transaction.
     *
     * @param id the id of the transactionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<TransactionDTO> deleteTransaction(@PathVariable Long id) throws EntityNotFoundException {
        log.debug("REST request to delete Stage : {}", id);
        transactionService.delete(id);

        TransactionDTO deleted = new TransactionDTO();
        deleted.setId(id);

        return ResponseEntity.ok()
            .body(deleted);

    }
}