package digicore.bankingApp.controllers;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Response {
    private HttpStatus responseCode;
    private boolean success;
    private String token;

    public Response(HttpStatus responseCode, boolean success, String token) {
        this.responseCode = responseCode;
        this.success = success;
        this.token = token;
    }
}
