package digicore.bankingApp.services;

import digicore.bankingApp.dtos.LoginDto;
import digicore.bankingApp.exceptions.CoreBankException;
import digicore.bankingApp.models.AccessToken;
import digicore.bankingApp.models.Account;
import digicore.bankingApp.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BankService bankService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private BankUserDetailsService bankUserDetailsService;


    @Override
    public AccessToken login(LoginDto loginDto) {
        Account account = bankService.findAccountByAccountNumber(loginDto.getAccountNumber());
        if (!passwordEncoder.matches(loginDto.getPassword(), account.getPassword())) {
            throw new CoreBankException("Wrong password");
        }

        UserDetails userDetails = bankUserDetailsService.loadUserByUsername(loginDto.getAccountNumber());
        String token = tokenService.generateToken(userDetails);
        return new AccessToken(token);
    }
}
