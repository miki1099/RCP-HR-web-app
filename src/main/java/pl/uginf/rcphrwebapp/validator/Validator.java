package pl.uginf.rcphrwebapp.validator;

public interface Validator<T> {

    void validate(T objToValidate);
}
