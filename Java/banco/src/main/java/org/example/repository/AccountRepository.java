package org.example.repository;

import org.example.exception.AccountNotFoundException;
import org.example.exception.PixInUseException;
import org.example.model.AccountWallet;

import java.util.ArrayList;
import java.util.List;

import static org.example.repository.CommonsRepository.checkFoundsForTransaction;

public class AccountRepository {
    
    private final List<AccountWallet> accounts= new ArrayList<>();
    
    public List<AccountWallet> list(){
        return this.accounts;
    }
    
    public AccountWallet create(final List<String> pix, final long initialFunds){
       if(!accounts.isEmpty()){
           var pixInUse = accounts.stream().flatMap(a->a.getPix().stream()).toList();
           for (String p : pix) {
               if (pixInUse.contains(p)) {
                   throw new PixInUseException("O pix '" + p + "'ja esta em uso");
               }
           }
       }
        
        
        
        var newAccount = new AccountWallet(initialFunds,pix);
        accounts.add(newAccount);
        return newAccount;
    }
    
    public long withDraw(final String pix, final long amount){
        var source= findByPix(pix);
        checkFoundsForTransaction(source,amount);
        source.reduceMoney(amount);
        return amount;
    }
    
    
    public void transferMoney(final String sourcePix, final String targetPix, final long amount){
        var source= findByPix(sourcePix);
        checkFoundsForTransaction(source,amount);
        var target =findByPix(targetPix);
        var message = "pix enviado de '"+sourcePix+"' para '"+targetPix+"'";
        target.addMoney(source.reduceMoney(amount),source.getService(),message);
        
    }
    public void deposit(final String pix, final long fundsAmount){
        var target = findByPix(pix);
        target.addMoney(fundsAmount,"deposito");
    }
    
    public AccountWallet findByPix(final String pix){
        return accounts.stream().filter(a->a.getPix().contains(pix))
                .findFirst().orElseThrow(()->new AccountNotFoundException("A conta com a chave pix '"+ pix +"' não existe"));
    }
}
