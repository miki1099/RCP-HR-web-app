package pl.uginf.rcphrwebapp.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public enum MsgCodes {

    ERROR_EMPTY("{0} can not be empty"),
    INVALID("Invalid {0}"),
    NOT_UNIQUE("{0} is not unique"),
    BIRTH_DATE_NOT_MATCH_PESEL("Date {0} does not match PESEL beginning {1}"),
    USER_NOT_FOUND("User with username {0} is not found."),
    INCORRECT_DATE_VALUE("Incorrect date.");

    private final String message;

    MsgCodes(String message) {
        this.message = message;
    }

    public String getMsg(String... params) {
        String filledMsg = this.message;
        for (int i = 0; i < params.length; i++) {
            String indexToReplace = "{" + i + "}";
            if (StringUtils.contains(filledMsg, indexToReplace)) {
                filledMsg = filledMsg.replace(indexToReplace, params[i]);
            } else {
                String errorMsg = "Given too much params, not found index: " + indexToReplace;
                log.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
        }
        return filledMsg;
    }
}
