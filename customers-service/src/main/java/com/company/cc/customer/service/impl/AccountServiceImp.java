package com.company.cc.customer.service.impl;

import com.company.cc.customer.exceptions.ServiceCommunicationException;
import com.company.cc.customer.service.AccountService;
import com.company.cc.customer.service.dto.AccountDTO;
import com.company.cc.customer.service.dto.TransactionDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AccountServiceImp implements AccountService {

    RestTemplate restTemplate;

    public AccountServiceImp(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<AccountDTO> getAccounts(long customerId) throws ServiceCommunicationException{

        List<AccountDTO> accounts = null;

        try {
            ParameterizedTypeReference<List<AccountDTO>> typeReference =
                    new ParameterizedTypeReference<List<AccountDTO>>() {};

            ResponseEntity<List<AccountDTO>> response =
                    restTemplate.exchange("http://accounts-service/api/accounts?customerId="+customerId, HttpMethod.GET, null, typeReference);

            accounts = response.getBody();
        }catch (RestClientException ex){
            throw new ServiceCommunicationException(ex.getMessage());
        }

        return accounts;
    }
}
