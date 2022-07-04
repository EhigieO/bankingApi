package digicore.bankingApp.services;

import digicore.bankingApp.dtos.AccountDto;
import digicore.bankingApp.dtos.LoginDto;
import digicore.bankingApp.exceptions.CoreBankException;
import digicore.bankingApp.models.AccessToken;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class UserServiceImplTest {

    @Autowired
    UserService userService;
    @Autowired
    BankService bankService;

    AccountDto accountDto;
    LoginDto loginDto;

    @BeforeEach
    void setUp() {
        accountDto = new AccountDto();
        accountDto.setAccountName("Ikpea Ehigie");
        accountDto.setAccountPassword("through");
        accountDto.setInitialDeposit(20000.0);
        loginDto = new LoginDto();
    }

    @Test
    void testThatLoginGeneratesToken(){
        bankService.reset();
        bankService.createAccount(accountDto);
        String accountNumber = bankService.findAccountByAccountName("Ikpea Ehigie").getAccountNumber();

        loginDto.setAccountNumber(accountNumber);
        loginDto.setPassword("through");

        AccessToken token = userService.login(loginDto);
        log.info("Token ---->{}",token);

        assertNotNull(token.getAccessToken());
    }

    @Test
    void testInvalidPasswordThrowsCoreException(){
        bankService.createAccount(accountDto);
        String accountNumber = bankService.findAccountByAccountName("Ikpea Ehigie").getAccountNumber();
        loginDto.setAccountNumber(accountNumber);
        loginDto.setPassword("sdeewsas");
        assertThrows(CoreBankException.class, ()-> userService.login(loginDto));
    }
}