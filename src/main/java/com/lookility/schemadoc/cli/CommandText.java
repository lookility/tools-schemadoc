package com.lookility.schemadoc.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Create schema documentation as text tree.")
public class CommandText {
    @Parameter(description = "schema-file", required = true)
    String schemaFile = null;
}
