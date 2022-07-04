package digicore.bankingApp.dtos;

import lombok.Data;

@Data
public class WithdrawalDto {
    private String accountNumber;
    private Double amount;
    private String password;

}
