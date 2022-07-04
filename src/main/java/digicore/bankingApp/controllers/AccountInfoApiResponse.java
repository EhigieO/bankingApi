package digicore.bankingApp.controllers;

import digicore.bankingApp.models.AccountInfo;
import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
public class AccountInfoApiResponse {
    private HttpStatus responseCode;
    private boolean isSuccess;
    private String reply;
    private AccountInfo accountInfo;

    public AccountInfoApiResponse(HttpStatus responseCode, boolean isSuccess, String reply, AccountInfo accountInfo) {
        this.responseCode = responseCode;
        this.isSuccess = isSuccess;
        this.reply = reply;
        this.accountInfo = accountInfo;
    }
}
