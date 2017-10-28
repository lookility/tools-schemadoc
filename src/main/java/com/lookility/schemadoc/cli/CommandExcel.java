package com.lookility.schemadoc.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Create schema documentation as Excel file.")
public class CommandExcel {

    @Parameter(names = "--file", description = "Output Excel file path", required = true)
    String file;

    @Parameter(description = "schema-file", required = true)
    String schemaFile = null;

}
