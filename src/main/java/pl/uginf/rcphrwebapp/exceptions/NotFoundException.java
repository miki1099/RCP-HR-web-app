package pl.uginf.rcphrwebapp.exceptions;

import pl.uginf.rcphrwebapp.utils.MsgCodes;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String value) {
        super(MsgCodes.NOT_FOUND.getMsg(value));
    }
}
