package com.company.cc.transaction.service.impl;

import com.company.cc.transaction.domain.Transaction;
import com.company.cc.transaction.exceptions.EntityAlreadyExistsException;
import com.company.cc.transaction.exceptions.EntityNotFoundException;
import com.company.cc.transaction.repository.TransactionRepository;
import com.company.cc.transaction.service.TransactionService;
import com.company.cc.transaction.service.dto.TransactionDTO;
import com.company.cc.transaction.service.mapper.TransactionMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public TransactionDTO findOne(long id) throws EntityNotFoundException {

        Optional<Transaction> transaction = transactionRepository.findById(id);

        if( transaction.isPresent() ){
            return transactionMapper.toDto(transaction.get());
        }else{
            throw new EntityNotFoundException(Transaction.class, "id", String.valueOf(id));
        }
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
    public TransactionDTO update(TransactionDTO transactionDTO) throws EntityNotFoundException {

        Optional<Transaction> transaction = transactionRepository.findById(transactionDTO.getId());

        if( transaction.isPresent() ){
            Transaction saved = transactionRepository.save(transactionMapper.toEntity(transactionDTO));
            return transactionMapper.toDto(saved);

        }else{
            throw new EntityNotFoundException(Transaction.class, "id", String.valueOf(transactionDTO.getId()));
        }
    }

    @Override
    public TransactionDTO delete(long id) throws EntityNotFoundException {

        Optional<Transaction> transaction = transactionRepository.findById(id);

        if( transaction.isPresent() ){
            transactionRepository.deleteById(id);

            return new TransactionDTO().setId(id);

        }else{
            throw new EntityNotFoundException(Transaction.class, "id", String.valueOf(id));
        }

    }

    @Override
    public Page<TransactionDTO> getByAccountId(String accountId, Pageable pageable) {
        return null;
    }
}
