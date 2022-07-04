package digicore.bankingApp.exceptions;

public class CoreBankException extends RuntimeException {
    public CoreBankException(String message) {
        super(message);
    }

    public CoreBankException() {
        super();
    }

    public CoreBankException(Throwable cause) {
        super(cause);
    }

    public CoreBankException(String message, Throwable cause) {
        super(message, cause);
    }
}
