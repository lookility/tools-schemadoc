package com.lookility.schemadoc;

import com.lookility.schemadoc.cli.SchemaDocCli;
import com.lookility.schemadoc.ui.SchemaDocApp;

/**
 * Single entry point for JavaFX and CLI program.
 */
public class SchemaDocMain {

    public static void main(String[] args) {
        if (args.length == 0) {
            SchemaDocApp.main(args);
        } else {
            SchemaDocCli.main(args);
        }
    }

    private SchemaDocMain() {};
}
