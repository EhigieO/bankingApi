package digicore.bankingApp.services;

import digicore.bankingApp.dtos.AccountDto;
import digicore.bankingApp.exceptions.CoreBankException;
import digicore.bankingApp.models.Account;
import digicore.bankingApp.models.AccountInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class BankServiceImplTest {

    @Autowired
    BankService bankService;
    AccountDto accountDto;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        accountDto = null;
        bankService.reset();
    }
    @Test
    void testToFindAccountNumber(){
        accountDto = new AccountDto();
        accountDto.setAccountName("Ikpea Ehigie");
        accountDto.setAccountPassword("through");
        accountDto.setInitialDeposit(1000.0);
        try {
            bankService.createAccount(accountDto);
        } catch (CoreBankException e) {
            e.printStackTrace();
        }
        Account account = bankService.findAccountByAccountName("Ikpea Ehigie");
        Account account1 = bankService.findAccountByAccountNumber(account.getAccountNumber());
        assertEquals(account, account1);
    }

    @Test
    void testCanCreateAccount(){
        accountDto = new AccountDto();
        accountDto.setAccountName("Ikpea Ehigie");
        accountDto.setAccountPassword("through");
        accountDto.setInitialDeposit(1000.0);
        String message = null;
        try {
            message = bankService.createAccount(accountDto);
        } catch (CoreBankException e) {
            e.printStackTrace();
        }
        log.info("{}",message);
        Account account = bankService.findAccountByAccountName("Ikpea Ehigie");
        assertEquals(account.getAccountName(), accountDto.getAccountName());
        assertTrue(passwordEncoder.matches(accountDto.getAccountPassword(), account.getPassword()));
        assertEquals(account.getAccountBalance(), accountDto.getInitialDeposit());
        assertEquals(10, account.getAccountNumber().length());
    }
    @Test
    void testCanDeposit(){
        accountDto = new AccountDto();
        accountDto.setAccountName("Ikpea Ehigie");
        accountDto.setAccountPassword("through");
        accountDto.setInitialDeposit(1000.0);
        assertEquals(1000.0, accountDto.getInitialDeposit());
        try {
            bankService.createAccount(accountDto);
        } catch (CoreBankException e){
            e.printStackTrace();
        }
        Account account = bankService.findAccountByAccountName("Ikpea Ehigie");
        assertEquals(1000, account.getAccountBalance());
        String message = bankService.deposit(account.getAccountNumber(), 3000.0);
        log.info(message);
        assertEquals(4000.0, account.getAccountBalance());
    }

    @Test
    void testToThrowExceptionForAmountsLessThanOne(){
        accountDto = new AccountDto();
        accountDto.setAccountName("Ikpea Ehigie");
        accountDto.setAccountPassword("through");
        accountDto.setInitialDeposit(1000.0);

        try{
            bankService.createAccount(accountDto);
        } catch (CoreBankException e){
            e.printStackTrace();
        }

        Account account = bankService.findAccountByAccountName("Ikpea Ehigie");
        assertThrows(CoreBankException.class, ()-> bankService.deposit(account.getAccountNumber(), -1000.0));
    }

    @Test
    void testWithdrawal(){
        accountDto = new AccountDto();
        accountDto.setAccountName("Ikpea Ehigie");
        accountDto.setAccountPassword("through");
        accountDto.setInitialDeposit(5000.0);
        assertEquals(5000.0, accountDto.getInitialDeposit());
        try {
            bankService.createAccount(accountDto);
        } catch (CoreBankException e){
            e.printStackTrace();
        }
        Account account = bankService.findAccountByAccountName("Ikpea Ehigie");
        assertEquals(5000, account.getAccountBalance());

        String message = bankService.withdraw(account.getAccountNumber(), 3000.0, "through");
        log.info(message);
        assertEquals(2000.0, account.getAccountBalance());
    }

    @Test
    void testToShowAccountInfo(){
        accountDto = new AccountDto();
        accountDto.setAccountName("Ikpea Ehigie");
        accountDto.setAccountPassword("through");
        accountDto.setInitialDeposit(5000.0);
        assertEquals(5000.0, accountDto.getInitialDeposit());
        try {
            bankService.createAccount(accountDto);
        } catch (CoreBankException e){
            e.printStackTrace();
        }
        Account account = bankService.findAccountByAccountName("Ikpea Ehigie");

        AccountInfo accountInfo = bankService.getAccountInfo(account
                        .getAccountNumber(), accountDto.getAccountPassword());

        assertEquals(accountInfo.getAccountNumber(), account.getAccountNumber());
        assertEquals(accountInfo.getAccountName(), account.getAccountName());
    }

    @Test
    void testThatInvalidAccountNumberThrowsException(){
        accountDto = new AccountDto();
        accountDto.setAccountName("Ikpea Ehigie");
        accountDto.setAccountPassword("through");
        accountDto.setInitialDeposit(5000.0);
        assertEquals(5000.0, accountDto.getInitialDeposit());
        try {
            bankService.createAccount(accountDto);
        } catch (CoreBankException e){
            e.printStackTrace();
        }
        assertThrows(CoreBankException.class, ()-> bankService.withdraw("258415",2000.00,"through"));
    }

    @Test
    void testThatInvalidPasswordThrowsException(){
        accountDto = new AccountDto();
        accountDto.setAccountName("Ikpea Ehigie");
        accountDto.setAccountPassword("through");
        accountDto.setInitialDeposit(5000.0);
        assertEquals(5000.0, accountDto.getInitialDeposit());
        try {
            bankService.createAccount(accountDto);
        } catch (CoreBankException e){
            e.printStackTrace();
        }
        Account account = bankService.findAccountByAccountName("Ikpea Ehigie");
        assertThrows(CoreBankException.class, ()-> bankService.withdraw(account.getAccountNumber(),2000.00,"thriugh"));
    }

    @Test
    void testThatInvalidWithdrawAmountThrowsException(){
        accountDto = new AccountDto();
        accountDto.setAccountName("Ikpea Ehigie");
        accountDto.setAccountPassword("through");
        accountDto.setInitialDeposit(5000.0);
        assertEquals(5000.0, accountDto.getInitialDeposit());
        try {
            bankService.createAccount(accountDto);
        } catch (CoreBankException e){
            e.printStackTrace();
        }
        Account account = bankService.findAccountByAccountName("Ikpea Ehigie");

        assertThrows(CoreBankException.class, ()-> bankService.withdraw(account.getAccountNumber(),4600.00,"through"));
    }

    @Test
    void testThatAccountCanGenerateStatement(){
        accountDto = new AccountDto();
        accountDto.setAccountName("Ikpea Ehigie");
        accountDto.setAccountPassword("through");
        accountDto.setInitialDeposit(5000.0);
        assertEquals(5000.0, accountDto.getInitialDeposit());
        try {
            bankService.createAccount(accountDto);
        } catch (CoreBankException e){
            e.printStackTrace();
        }
        Account account = bankService.findAccountByAccountName("Ikpea Ehigie");

        assertEquals(1, account.getTransactions().size());

        bankService.deposit(account.getAccountNumber(), 2000.0);
        bankService.deposit(account.getAccountNumber(), 2000.0);
        bankService.withdraw(account.getAccountNumber(), 500.0,"through");
        bankService.withdraw(account.getAccountNumber(), 200.0, "through");
        log.info("Account Statement ---->{}", account.getTransactions());
        assertEquals(5, account.getTransactions().size());
        assertEquals(8300.00, account.getAccountBalance());
    }
}