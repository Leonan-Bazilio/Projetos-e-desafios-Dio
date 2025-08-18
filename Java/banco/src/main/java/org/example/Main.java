package org.example;

import org.example.exception.AccountNotFoundException;
import org.example.exception.NoFundsEnoughException;
import org.example.model.AccountWallet;
import org.example.repository.AccountRepository;
import org.example.repository.InvestmentRepository;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    
    private final static AccountRepository accountRepository = new AccountRepository();
    private final static InvestmentRepository investmentRepository = new InvestmentRepository();
    
    
    static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("ola seja bem vindo ao bank");
        
        
        while(true){
            System.out.println("selecione a operação desejada");
            System.out.println("1 - criar uma conta");
            System.out.println("2 - criar um investimento");
            System.out.println("3 - fazer um investimento");
            System.out.println("4 - depositar na conta");
            System.out.println("5 - sacar da conta");
            System.out.println("6 - transferencia entre contas");
            System.out.println("7 - investir");
            System.out.println("8 - sacar investimentos");
            System.out.println("9 - listar contas");
            System.out.println("10 - listar investimentos");
            System.out.println("11 - listar carteiras de investimentos");
           
            System.out.println("12 - atualizar investimentos");
            System.out.println("13 - historico de contas ");
            System.out.println("14 - sair ");
            
            var option = scanner.nextInt();
            switch (option){
                case 1->createAccount();
                case 2->createInvestment();
                case 3->createWalletInvestment();
                case 4->deposit();
                case 5->withDraw();
                case 6->transferToAccount();
                case 7->incIInvestment();
                case 8->rescueInvestment();
                case 9-> accountRepository.list().forEach(System.out::println);
                case 10-> investmentRepository.list().forEach(System.out::println);
                
                case 11-> investmentRepository.listWallet().forEach(System.out::println);
              
                case 12->{investmentRepository.updateAmount();
                    System.out.println("investimentos reajustados");}
              
                case 14->System.exit(0);
                default->
                    System.out.println("Opção invalida");
            }
        }
    }
    
    private static void createAccount(){
        System.out.println("informe o pix (separado por ';')");
        var pix = Arrays.stream(scanner.next().split(";")).toList();
        System.out.println("informe o valor inicial do deposito");
        var amount = scanner.nextLong();
        var wallet = accountRepository.create(pix,amount);
        System.out.println("conta criada: " +wallet);
    }
    
    private static void createInvestment(){
        System.out.println("informe a taxa do investimento");
        var tax = scanner.nextInt();
        System.out.println("informe o valor inicial do deposito");
        var initialFunds = scanner.nextLong();
        var investment= investmentRepository.create(tax,initialFunds);
        System.out.println("investimento criada: " +investment);
    }
    
    
    private static void withDraw(){
        System.out.println("informe a chave pix para o saque");
        var pix = scanner.next();
        System.out.println("informe o valor que sera sacado");
        var amount = scanner.nextLong();
        try {
            accountRepository.withDraw(pix,amount);
        }catch (NoFundsEnoughException|AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
        
    }
    private static void deposit(){
        System.out.println("informe a chave pix para o deposito ");
        var pix = scanner.next();
        System.out.println("informe o valor que sera depositado");
        var amount = scanner.nextLong();
        
        try {
            accountRepository.deposit(pix,amount);
        }catch (AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }
    private static void transferToAccount(){
        System.out.println("informe a chave pix para a conta de origem ");
        var source = scanner.next();
        System.out.println("informe a chave pix para a conta de destino");
        var target = scanner.next();
        System.out.println("informe o valor que sera depositado");
        var amount = scanner.nextLong();
        
        try {
            accountRepository.transferMoney(source,target,amount);
        }catch (AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    
    private static void createWalletInvestment(){
        System.out.println("informe a chave pix para a conta");
        var pix = scanner.next();
        var account = accountRepository.findByPix(pix);
        System.out.println("Informe o identicador do investimento");
        var investmentId = scanner.nextInt();
        var investmentWallet = investmentRepository.initInvestment(account,investmentId);
        System.out.println("Conta de investimento criada"+investmentWallet);
    }
    
    
    private static void incIInvestment(){
        System.out.println("informe a chave pix para o investimento ");
        var pix = scanner.next();
        System.out.println("informe o valor que sera investimento");
        var amount = scanner.nextLong();
        
        try {
            investmentRepository.deposit(pix,amount);
        }catch (AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    private static void rescueInvestment(){
        System.out.println("informe a chave pix para o resgate do investimento");
        var pix = scanner.next();
        System.out.println("informe o valor que sera sacado");
        var amount = scanner.nextLong();
        try {
            investmentRepository.withDraw(pix,amount);
        }catch (NoFundsEnoughException|AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
        
    }
    
    /*
    private static void checkHistory(){
        System.out.println("Informe a chave pix para verificar extrato");
        var pix = scanner.next();
        AccountWallet wallet;
        try {
            var  sortedHistory=accountRepository.findByPix(pix);
            var audit=wallet.getFinancialTransactions();
            var group = audit.stream().collect(Collectors.groupingBy(
                    t->t.createdAt().truncatedTo(ChronoUnit.SECONDS)));
        }catch (AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }*/
    
    
}