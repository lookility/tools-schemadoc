package com.lookility.schemadoc.cli;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
import org.apache.logging.log4j.Level;

/**
 * Converter for log level parameters for command line parser.
 *
 * <p>Converts the log levels given in the command line to according log level objects.</p>
 *
 * @see com.beust.jcommander.JCommander
 * @see Level
 */
class LogLevelConverter implements IStringConverter<Level> {

    /**
     * Convert log level string into according log level object.
     *
     * @param value log level as given by the command line argument
     * @return parsed log level
     *
     * @throws ParameterException if given log level string is invalid
     */
    @Override
    public Level convert(String value) {
        Level level = Level.getLevel(value.toUpperCase());
        if (level == null) throw new ParameterException("invalid log level argument: " + value);
        return level;
    }
}
