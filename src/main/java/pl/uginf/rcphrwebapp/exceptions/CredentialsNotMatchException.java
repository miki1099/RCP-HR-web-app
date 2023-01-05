package pl.uginf.rcphrwebapp.exceptions;

public class CredentialsNotMatchException extends RuntimeException{

    public CredentialsNotMatchException() {
        super("Credentials does not match!");
    }
}
