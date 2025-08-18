package org.example.repository;

import org.example.exception.AccountWithInvestmentException;
import org.example.exception.InvestmentNotFoundException;
import org.example.exception.PixInUseException;
import org.example.exception.WalletNotFoundException;
import org.example.model.AccountWallet;
import org.example.model.Investment;
import org.example.model.InvestmentWallet;

import java.util.ArrayList;
import java.util.List;

import static org.example.repository.CommonsRepository.checkFoundsForTransaction;

public class InvestmentRepository {
    private long nextId=0;
    private final List<Investment> investments=new ArrayList<>();
    private final List<InvestmentWallet> wallets=new ArrayList<>();
    
    public List<Investment> list(){
        return this.investments;
    }
    public List<InvestmentWallet> listWallet(){
        return this.wallets;
    }
    
    
    public Investment create(final long tax,  final long initialFunds){
        this.nextId++;
        var investment = new Investment(this.nextId,tax,initialFunds);
        investments.add(investment);
        return investment;
    }
    
    public InvestmentWallet initInvestment(final AccountWallet account, final long id){
        if(!wallets.isEmpty()){
            var acountsInUse = wallets.stream().map(InvestmentWallet::getAccount).toList();
            
            if (acountsInUse.contains(account)) {
                throw new AccountWithInvestmentException("A conta '" + account + "'ja possui um investimento");
            }
        }
        
        
       
        
        
        var investment = findById(id);
        checkFoundsForTransaction(account,investment.initialFunds());
        var wallet = new InvestmentWallet(investment,account,investment.initialFunds());
        wallets.add(wallet);
        return wallet;
    }
    
    public InvestmentWallet deposit(final String pix, final long funds){
        var wallet=findWalletByAccountPix(pix);
        wallet.addMoney(wallet.getAccount().reduceMoney(funds),wallet.getService(), "investimentos");
        return wallet;
    }
    
    public InvestmentWallet withDraw(final String pix, final long funds){
        var wallet=findWalletByAccountPix(pix);
        checkFoundsForTransaction(wallet,funds);
        wallet.getAccount().addMoney(wallet.reduceMoney(funds),wallet.getService(),"saque de investimentos");
        if(wallet.getFunds()==0){
            wallets.remove(wallet);
        }
        return wallet;
    }
    
    public void updateAmount(){
        wallets.forEach(w->w.updateAmount(w.getInvestment().tax()));
    }
    
    public Investment findById(final long id){
        return investments.stream().filter(a->a.id()==id)
                .findFirst().orElseThrow(()->new InvestmentNotFoundException("o investimento '"+id+"' não foi encontrado"));
        
    }
    
    public InvestmentWallet findWalletByAccountPix(final String pix){
        return wallets.stream().filter(w->w.getAccount().getPix().contains(pix))
                .findFirst().orElseThrow(()->new WalletNotFoundException("A carteira não foi encontrada"));
    }
    
}
