package digicore.bankingApp.dtos;

import lombok.Data;

@Data
public class AccountDto {
    private String accountName;
    private Double initialDeposit;
    private String accountPassword;
}
