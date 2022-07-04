package digicore.bankingApp.services;

import digicore.bankingApp.dtos.AccountDto;
import digicore.bankingApp.models.Account;
import digicore.bankingApp.models.AccountInfo;
import digicore.bankingApp.models.Transaction;

import java.util.List;

public interface BankService {
    String createAccount(AccountDto accountDto);

    Account findAccountByAccountName(String accountName);

    Account findAccountByAccountNumber(String accountNumber);

    String deposit(String accountNumber, Double amount);

    String withdraw(String accountNumber, Double amount, String password);

    AccountInfo getAccountInfo(String accountNumber, String accountPassword);

    List<Transaction> generateStatementOfAccount(String accountNumber, String accountPassword);

    void reset();

    Double getAccountBalance(String accountNumber, String accountPassword);
}
