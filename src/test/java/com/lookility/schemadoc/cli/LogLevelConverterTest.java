package com.lookility.schemadoc.cli;

import com.beust.jcommander.ParameterException;
import org.apache.logging.log4j.Level;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LogLevelConverterTest {

    @Test
    public void validLogLevels() {
        LogLevelConverter lc = new LogLevelConverter();

        assertEquals(Level.TRACE, lc.convert("trace"));
        assertEquals(Level.TRACE, lc.convert("TRACE"));
    }

    @Test
    public void exceptionOnInvalidLogLevel() {
        LogLevelConverter lc = new LogLevelConverter();

        try {
            lc.convert("invalidLogLevel");

            fail("ParameterException expected");
        } catch(ParameterException e) {
            // succeeded
        }
    }

}