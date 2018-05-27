package com.company.cc.customer.service.impl;

import com.company.cc.customer.exceptions.ServiceCommunicationException;
import com.company.cc.customer.service.TransactionService;
import com.company.cc.customer.service.dto.TransactionSummaryDTO;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionServiceImp implements TransactionService {


    RestTemplate restTemplate;

    public TransactionServiceImp(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public TransactionSummaryDTO getTransactionSummary(long customerId) throws ServiceCommunicationException{

        TransactionSummaryDTO balance = null;

        try {
            ResponseEntity<TransactionSummaryDTO> response =
                    restTemplate.exchange("http://transactions-service/api/balances/"+customerId, HttpMethod.GET, null, TransactionSummaryDTO.class);

            if( response.getStatusCodeValue() == 404){
                // Create empty summary
                balance = new TransactionSummaryDTO(customerId);
            }else{
                balance = response.getBody();
            }
        }catch (RestClientException ex){
            throw new ServiceCommunicationException(ex.getMessage());
        }

        return balance;
    }
}
