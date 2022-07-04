package digicore.bankingApp.dtos;

import lombok.Data;

@Data
public class DepositDto {
    private String accountNumber;
    private Double amount;
}
