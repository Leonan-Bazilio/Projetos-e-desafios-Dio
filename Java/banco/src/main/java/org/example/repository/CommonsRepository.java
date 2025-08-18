package org.example.repository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.enums.BankService;
import org.example.exception.NoFundsEnoughException;
import org.example.model.AccountWallet;
import org.example.model.Money;
import org.example.model.MoneyAudit;
import org.example.model.Wallet;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final  class CommonsRepository {
    
    public static void checkFoundsForTransaction(final Wallet source, final long amount){
        if(source.getFunds()<amount){
            throw new NoFundsEnoughException("Sua conta não tem dinheiro suficiente para realizar essa transação");
            
        }
    }
    public static List<Money> generateMoney(final UUID transactionId,final long funds, final String description){
        var history = new MoneyAudit(transactionId, BankService.ACCOUNT,description, OffsetDateTime.now());
        return Stream.generate(()->new Money(history)).limit(funds).toList();
    }
}
