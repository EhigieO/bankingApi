package digicore.bankingApp.controllers;

import digicore.bankingApp.dtos.AccountDto;
import digicore.bankingApp.dtos.DepositDto;
import digicore.bankingApp.dtos.LoginDto;
import digicore.bankingApp.dtos.WithdrawalDto;
import digicore.bankingApp.exceptions.CoreBankException;
import digicore.bankingApp.models.AccountInfo;
import digicore.bankingApp.models.Transaction;
import digicore.bankingApp.services.BankService;
import digicore.bankingApp.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/banking-app")
public class BankingAppController {
    @Autowired
    BankService bankService;
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public Response register(@RequestBody AccountDto accountDto){
        String reply;
        try{
            reply = bankService.createAccount(accountDto);
            return new Response(HttpStatus.OK, true, reply);
        } catch (CoreBankException e){
            return new Response(HttpStatus.BAD_REQUEST, false, e.getMessage());
        }
    }

    @PostMapping("/login")
    public Response login(@RequestBody LoginDto loginDto ){
        String token;
        try {
            bankService.findAccountByAccountNumber(loginDto.getAccountNumber());
            token = userService.login(loginDto).getAccessToken();
        } catch (CoreBankException e){
            return new Response(HttpStatus.BAD_REQUEST, false,e.getMessage());
        }
        return new Response(HttpStatus.OK, true, token);
    }

    @PostMapping("/deposit")
    public Response deposit(@RequestBody DepositDto depositDto ){
        String accountInfo;
        try{
            accountInfo = bankService.deposit(depositDto.getAccountNumber(), depositDto.getAmount());
        } catch (CoreBankException e){
            return new Response(HttpStatus.BAD_REQUEST, false, e.getMessage());
        }
        return new Response(HttpStatus.OK, true, accountInfo);
    }

    @PostMapping("/withdraw")
    public Response withdrawal(@RequestBody WithdrawalDto withdrawalDto){
        String accountInfo;
        try {
            accountInfo = bankService.withdraw(withdrawalDto.getAccountNumber(), withdrawalDto.getAmount(), withdrawalDto.getPassword());
        } catch (CoreBankException e){
            return new Response(HttpStatus.BAD_REQUEST, false, e.getMessage());
        }
        return new Response(HttpStatus.OK, true, accountInfo);
    }

    @GetMapping("/account_statement/{accountNumber}")
    public ResponseEntity<?> accountStatement(@RequestBody AccountDto accountDto, @PathVariable String accountNumber){
        List<Transaction> accountStatement;
        try{
            accountStatement = bankService.generateStatementOfAccount(accountNumber, accountDto.getAccountPassword());
        } catch (CoreBankException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(accountStatement, HttpStatus.OK);
    }

    @GetMapping("/account_balance/{accountNumber}")
    public ResponseEntity<?> accountBalance(@RequestBody AccountDto accountDto, @PathVariable String accountNumber){
        Double accountBalance;
        try{
            accountBalance = bankService.getAccountBalance(accountNumber, accountDto.getAccountPassword());
        } catch (CoreBankException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(accountBalance, HttpStatus.OK);
    }

    @GetMapping("/account_info/{accountNumber}")
    public ResponseEntity<?> accountInfo(@RequestBody AccountDto accountDto, @PathVariable String accountNumber){
        AccountInfo accountInfo;
        try{
            accountInfo = bankService.getAccountInfo(accountNumber, accountDto.getAccountPassword());
        } catch (CoreBankException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new AccountInfoApiResponse(HttpStatus.OK, true, "successful", accountInfo), HttpStatus.OK);
    }
}
