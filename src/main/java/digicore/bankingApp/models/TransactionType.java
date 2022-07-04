package digicore.bankingApp.models;

public enum TransactionType {
    DEPOSIT, WITHDRAW;


    @Override
    public String toString() {
        switch (this) {
            case WITHDRAW: return "Withdrawn";
            case DEPOSIT: return "Deposited";
        }
        return null;
    }
}
