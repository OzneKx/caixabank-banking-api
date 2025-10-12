package com.hackathon.finservice.data.mapper;

import com.hackathon.finservice.data.entity.Transaction;
import com.hackathon.finservice.data.entity.TransactionType;
import com.hackathon.finservice.dto.TransactionResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "fee", expression = "java(BigDecimal.ZERO)"),
            @Mapping(target = "transactionStatus", expression = "java(TransactionStatus.PENDING)"),
            @Mapping(target = "transactionDate", expression = "java(Instant.now())"),
            @Mapping(target = "sourceAccount", ignore = true),
            @Mapping(target = "targetAccount", ignore = true)
    })
    Transaction toEntity(BigDecimal amount, TransactionType transactionType);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "amount", target = "amount"),
            @Mapping(source = "transactionType", target = "transactionType"),
            @Mapping(source = "transactionStatus", target = "transactionStatus"),
            @Mapping(source = "transactionDate", target = "transactionDate"),
            @Mapping(target = "sourceAccountNumber",
                    expression = "java(transaction.getSourceAccount() != null ? " +
                            "transaction.getSourceAccount().getAccountNumber() : null)"),
            @Mapping(target = "targetAccountNumber",
                    expression = "java(transaction.getTargetAccount() != null  " +
                            "transaction.getTargetAccount().getAccountNumber() : null)")
    })
    TransactionResponse toResponse(Transaction transaction);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTransaction(@MappingTarget Transaction transaction, Transaction updateData);
}
