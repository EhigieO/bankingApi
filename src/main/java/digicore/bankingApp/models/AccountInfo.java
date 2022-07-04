package digicore.bankingApp.models;

import lombok.Data;

@Data
public class AccountInfo {
    private String accountName;
    private String accountNumber;
    private Double balance;

}
