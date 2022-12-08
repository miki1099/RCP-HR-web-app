package pl.uginf.rcphrwebapp.utils;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum MsgCodes {

    ERROR_EMPTY("{0} can not be empty"),
    INVALID("Invalid {0}"),
    NOT_UNIQUE("{0} is not unique"),
    BIRTH_DATE_NOT_MATCH_PESEL("Date {0} does not match PESEL beginning {1}"),
    USER_NOT_FOUND("User with username {0} is not found."),
    NOT_FOUND("{0} not found."),
    CONFLICT_CONTRACT_DATE("There is already an agreement in the given timeframe for this user."),
    INCORRECT_DATE_VALUE("Incorrect {0} date."),
    WORK_LOG_STARTED("There is active work for user {0}, please stop current work to start new one."),
    WORK_LOG_NOT_STARTED("There is not any started work for user {0}"),
    BEFORE_ERROR("{0} can not be before {1}"),
    WRONG_FILE_EXTENSION("{0} is not a pdf file");

    private final String message;

    MsgCodes(String message) {
        this.message = message;
    }

    public String getMsg(String... params) {
        String filledMsg = this.message;
        for (int i = 0; i < params.length; i++) {
            String indexToReplace = "{" + i + "}";
            if ( StringUtils.contains(filledMsg, indexToReplace) ) {
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
