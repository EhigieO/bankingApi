package digicore.bankingApp.services;

import digicore.bankingApp.dtos.LoginDto;
import digicore.bankingApp.models.AccessToken;


public interface UserService {
    AccessToken login(LoginDto loginDto);
}
