package com.company.cc.customer.service.mapper;

import com.company.cc.customer.domain.Account;
import com.company.cc.customer.service.dto.AccountDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface AccountMapper extends EntityMapper<AccountDTO, Account> {

    @Mapping(target = "balance", ignore = true)
    AccountDTO toDto(Account account);

    Account toEntity(AccountDTO accountDTO);

    default Account fromId(Long id) {
        if (id == null) {
            return null;
        }
        Account user = new Account();
        user.setId(id);
        return user;
    }
}

