package digicore.bankingApp.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Transaction {
    private LocalDate transactionDate;
    private TransactionType transactionType;
    private Double amount;
    private String description;
    private Double accountBalance;

}
