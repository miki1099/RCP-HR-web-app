package pl.uginf.rcphrwebapp.exceptions;

import pl.uginf.rcphrwebapp.utils.MsgCodes;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super(MsgCodes.USER_NOT_FOUND.getMsg(username));
    }
}
