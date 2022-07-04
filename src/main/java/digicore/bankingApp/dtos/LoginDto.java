package digicore.bankingApp.dtos;

import lombok.Data;

@Data
public class LoginDto {
    private String accountNumber;
    private String password;
}
