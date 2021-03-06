package com.company.cc.transaction.service.mapper;

import com.company.cc.shared.TransactionDTO;
import com.company.cc.transaction.domain.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface TransactionMapper extends EntityMapper<TransactionDTO, Transaction> {

    TransactionDTO toDto(Transaction transaction);

    Transaction toEntity(TransactionDTO transactionDTO);

    default Transaction fromId(Long id) {
        if (id == null) {
            return null;
        }
        Transaction user = new Transaction();
        user.setId(id);
        return user;
    }
}

