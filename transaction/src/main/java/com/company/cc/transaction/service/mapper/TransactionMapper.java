package com.company.cc.transaction.service.mapper;

import com.company.cc.transaction.domain.Transaction;
import com.company.cc.transaction.service.dto.TransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface TransactionMapper extends EntityMapper<TransactionDTO, Transaction> {

    @Mapping(target = "transactions", ignore = true)
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

