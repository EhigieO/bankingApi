package digicore.bankingApp.services;

import digicore.bankingApp.dtos.AccountDto;
import digicore.bankingApp.exceptions.CoreBankException;
import digicore.bankingApp.models.Account;
import digicore.bankingApp.models.AccountInfo;
import digicore.bankingApp.models.Transaction;
import digicore.bankingApp.models.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static digicore.bankingApp.models.TransactionType.DEPOSIT;
import static digicore.bankingApp.models.TransactionType.WITHDRAW;

@Service
@Slf4j
public class BankServiceImpl implements BankService{


    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    private final Map<String, Account> accountRepository = new HashMap<>();

    public void reset() {
        accountRepository.clear();
    }

    @Override
    public Double getAccountBalance(String accountNumber, String accountPassword) {
        Account account = findAccountByAccountNumber(accountNumber);
        if (!passwordEncoder.matches(accountPassword, account.getPassword())){
            throw new CoreBankException("Wrong password");
        }
        return account.getAccountBalance();
    }

    @Override
    public String createAccount(AccountDto accountDto) {
        validateAccountDto(accountDto);

        Account account = new Account();

        modelMapper.map(accountDto, account);
        account.setAccountBalance(accountDto.getInitialDeposit());
        account.setPassword(passwordEncoder.encode(accountDto.getAccountPassword()));
        String accountNumber = String.valueOf(UUID.randomUUID().getMostSignificantBits());
        accountNumber = accountNumber.substring(1, 11);
        account.setAccountNumber(accountNumber);

        String description = "account opening";
        saveTransaction(account, accountDto.getInitialDeposit(), description, DEPOSIT);
        accountRepository.put(account.getAccountNumber(),account);

        return accountCreated(account);
    }

    private String accountCreated(Account account) {
        return account.getAccountName() +" your account has been created successfully your account number is " +
                account.getAccountNumber() + " your account balance is " + account.getAccountBalance();
    }

    private void saveTransaction(Account account, Double depositAmount, String description, TransactionType transactionType) {
        Transaction transaction = new Transaction();

        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionType(transactionType);
        transaction.setDescription(description);
        transaction.setAmount(depositAmount);
        transaction.setAccountBalance(account.getAccountBalance());

        account.getTransactions().add(transaction);
    }

    private void validateAccountDto(AccountDto accountDto) {
        boolean invalidAccountName = accountDto.getAccountName() == null || accountDto.getAccountName().isEmpty() ;//|| accountDto.getAccountName().isBlank();
        if(invalidAccountName){
            throw new CoreBankException("Enter a valid account name");
        }

        boolean invalidPassword = accountDto.getAccountPassword() == null || accountDto.getAccountPassword().isEmpty();// || accountDto.getAccountPassword().isBlank();

        if(invalidPassword){
            throw new CoreBankException("Enter a valid password");
        }

        boolean accountExists = isExisting(accountDto.getAccountName());
        if(accountExists){
            throw new CoreBankException("Account with name: " +accountDto.getAccountName() + " already exists");
        }
        if(accountDto.getInitialDeposit() < 500.00){
            throw new CoreBankException("Initial deposit cannot be less than 500");
        }
    }

    private boolean isExisting(String accountName) {
        return  accountRepository
                .values()
                .stream()
                .anyMatch(account -> account.getAccountName().equalsIgnoreCase(accountName));
    }

    @Override
    public Account findAccountByAccountName(String accountName) {
        return accountRepository.values().stream()
                .filter(account -> account.getAccountName().equalsIgnoreCase(accountName))
                .findFirst().orElseThrow(() -> new CoreBankException("Account does not exist"));
    }

    @Override
    public Account findAccountByAccountNumber(String accountNumber) {
        Account account = accountRepository.getOrDefault(accountNumber, null);
        if (account == null){
            throw new CoreBankException("Account Number Invalid");
        }
        return account;
    }

    @Override
    public String deposit(String accountNumber, Double amount) {
        boolean amountIsValid = amount == null || amount < 1.00 || amount >1_000_000.00;
        if (amountIsValid){
            throw new CoreBankException("Amount to deposit must be between 1.00 and 1,000,000.00");
        }
        Account account = findAccountByAccountNumber(accountNumber);
        account.setAccountBalance(account.getAccountBalance() + amount);

        String description = "Deposit of "+ amount;
        saveTransaction(account, amount, description, DEPOSIT);

        return transactionMessage(account, amount, DEPOSIT);
    }

    @Override
    public String withdraw(String accountNumber, Double amount, String password) {
        Account account = validateWithdrawal(accountNumber, amount, password);
        account.setAccountBalance(account.getAccountBalance() - amount);
        String description = "Withdrawal of "+ amount;
        saveTransaction(account, amount, description, WITHDRAW);
        return transactionMessage(account, amount, WITHDRAW);
    }

    @Override
    public AccountInfo getAccountInfo(String accountNumber, String accountPassword) {
        Account account = findAccountByAccountNumber(accountNumber);
        if (!passwordEncoder.matches(accountPassword, account.getPassword())) {
            throw new CoreBankException("Wrong password");
        }
        AccountInfo accountInfo = new AccountInfo();
        modelMapper.map(account, accountInfo);

        return accountInfo;
    }

    @Override
    public List<Transaction> generateStatementOfAccount(String accountNumber, String password) {
        Account account = findAccountByAccountNumber(accountNumber);
        if (!passwordEncoder.matches(password, account.getPassword())){
            throw new CoreBankException("Wrong password");
        }
        return account.getTransactions();
    }

    private Account validateWithdrawal(String accountNumber, Double amount, String password) {
        if (amount == null || amount < 1.00){
            throw new CoreBankException("Amount to withdraw must be greater than 1.00");
        }
        Account account = findAccountByAccountNumber(accountNumber);
        if (!passwordEncoder.matches(password, account.getPassword())){
            throw new CoreBankException("wrong password");
        }
        if (account.getAccountBalance() - amount < 500){
            throw new CoreBankException("Insufficient account balance");
        }
        return account;
    }

    private String transactionMessage(Account account, Double amount, TransactionType transactionType) {
        return String.format("You have successfully %s %.2f. Your account balance is %.2f",
                transactionType.toString(), amount,account.getAccountBalance());
    }
}
