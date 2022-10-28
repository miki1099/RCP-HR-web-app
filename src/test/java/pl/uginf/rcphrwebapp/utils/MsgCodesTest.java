package pl.uginf.rcphrwebapp.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class MsgCodesTest {

    @Test
    void getMsgTest() {
        //given
        String param = "Field";

        //when
        String msg = MsgCodes.ERROR_EMPTY.getMsg(param);

        //then
        assertThat(msg).isEqualTo("Field can not be empty");
    }

    @Test
    void getMsgTooMuchArgumentsTest() {
        //given
        String param = "Field";
        String uselessParam = "x";

        //when
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class, () ->
                MsgCodes.ERROR_EMPTY.getMsg(param, uselessParam));

        //then
        assertThat(error.getMessage()).isEqualTo("Given too much params, not found index: {1}");
    }
}