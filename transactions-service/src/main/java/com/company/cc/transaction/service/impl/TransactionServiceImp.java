package com.company.cc.transaction.service.impl;

import com.company.cc.shared.TransactionDTO;
import com.company.cc.transaction.domain.Transaction;
import com.company.cc.transaction.exceptions.EntityAlreadyExistsException;
import com.company.cc.transaction.exceptions.EntityNotFoundException;
import com.company.cc.transaction.repository.TransactionRepository;
import com.company.cc.transaction.service.TransactionService;
import com.company.cc.transaction.service.dto.TransactionSummaryDTO;
import com.company.cc.transaction.service.mapper.TransactionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionServiceImp implements TransactionService{

    TransactionRepository transactionRepository;
    TransactionMapper transactionMapper;

    public TransactionServiceImp(TransactionRepository transactionRepository,
                                 TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public TransactionDTO create(TransactionDTO transactionDTO) throws EntityAlreadyExistsException {

        if( transactionDTO.getId() != null){
            throw new EntityAlreadyExistsException(Transaction.class, transactionDTO.getId());
        }

        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        transaction = transactionRepository.save(transaction);

        return transactionMapper.toDto(transaction);
    }


    @Override
    public List<TransactionDTO> getByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId).stream()
                .map(transactionMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public TransactionSummaryDTO getSummaryByCustomerId(Long customerId) {
        Optional<Double> balance = transactionRepository.getSummaryByCustomerId(customerId);
        TransactionSummaryDTO transactionSummaryDTO = null;

        if(!balance.isPresent()){
            transactionSummaryDTO = new TransactionSummaryDTO(customerId );
        }else{
            transactionSummaryDTO = new TransactionSummaryDTO(customerId, balance.get() );
        }

        return  transactionSummaryDTO;

    }
}
