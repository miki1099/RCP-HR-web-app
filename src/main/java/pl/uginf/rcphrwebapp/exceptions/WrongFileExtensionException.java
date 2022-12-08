package pl.uginf.rcphrwebapp.exceptions;

import pl.uginf.rcphrwebapp.utils.MsgCodes;

public class WrongFileExtensionException extends RuntimeException {
    public WrongFileExtensionException(String filename) {
        super(MsgCodes.WRONG_FILE_EXTENSION.getMsg(filename));
    }
}
