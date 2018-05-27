package com.company.cc.transaction.resource;


import com.company.cc.transaction.exceptions.EntityAlreadyExistsException;
import com.company.cc.transaction.service.TransactionService;
import com.company.cc.transaction.service.dto.TransactionDTO;
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
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO) throws URISyntaxException, EntityAlreadyExistsException {
        log.debug("REST request to create transaction : {}", transactionDTO);

        TransactionDTO result = transactionService.create(transactionDTO);
        return ResponseEntity.created(new URI("/api/transactions/" + result.getId()))
            .body(result);
    }


    /**
     * GET  /transactions : get all the transactions.
     *
     * @param accountId the accountId information
     * @return the ResponseEntity with status 200 (OK) and the list of transactions in body
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactions(@RequestParam(required = true) Long accountId) {
        log.debug("REST request to get a page of Transactions");
        List<TransactionDTO> result = transactionService.getByAccountId(accountId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}