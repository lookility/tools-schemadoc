package com.lookility.schemadoc.cli;

import com.beust.jcommander.Parameter;
import org.apache.logging.log4j.Level;

class CommandLineArgs {

    @Parameter(names = {"--help", "-h"}, description = "Show this help", help = true)
    boolean help = false;

    @Parameter(names = {"--language", "-l"}, description = "Language of descriptions", order = 2)
    String language = null;

    @Parameter(names = "--log-level", description = "Log level (TRACE, DEBUG, INFO, WARN, ERROR, FATAL)")
    Level level = Level.ERROR;

    @Parameter(names = "--lifecycle-namespace", description = "Namespace URI of life cycle extensions for XML Schema.")
    String lifeCycleNamespaceURI = null;
}
