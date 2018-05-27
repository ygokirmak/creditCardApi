package com.company.cc.account.service.impl;

import com.company.cc.account.exceptions.ServiceCommunicationException;
import com.company.cc.account.service.TransactionService;
import com.company.cc.shared.TransactionDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TransactionServiceImp implements TransactionService {

    RestTemplate restTemplate;

    public TransactionServiceImp(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<TransactionDTO> getTransactions(long accountId) throws ServiceCommunicationException {
        List<TransactionDTO> transactions = null;

        try {
            ParameterizedTypeReference<List<TransactionDTO>> typeReference =
                    new ParameterizedTypeReference<List<TransactionDTO>>() {};

            ResponseEntity<List<TransactionDTO>> response =
                    restTemplate.exchange("http://transcations-service/api/transactions?accountId="+accountId, HttpMethod.GET, null, typeReference);

            transactions = response.getBody();
        }catch (RestClientException ex){
            throw new ServiceCommunicationException(ex.getMessage());
        }

        return transactions;
    }
}
