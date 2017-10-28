package com.lookility.schemadoc.cli;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.IStringConverterFactory;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.lookility.schemadoc.xsd.ModelBuilder;
import com.lookility.schemadoc.xsd.ModelBuilderException;
import com.lookility.schemadoc.model.Tree;
import com.lookility.schemadoc.model.TreeContainer;
import com.lookility.schemadoc.viewer.ExcelViewer;
import com.lookility.schemadoc.viewer.TextViewer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.IOException;

public class SchemaDocCli {

    private SchemaDocCli() {
    }

    public static void main(String[] args) {


        try {
            // parse command line arguments
            CommandLineArgs cla = new CommandLineArgs();
            CommandExcel execl = new CommandExcel();
            CommandText text = new CommandText();

            JCommander c = JCommander.newBuilder().addObject(cla)
                    .addCommand("excel", execl)
                    .addCommand("text", text)
                    .build();
            c.setProgramName("schemadoc");
            c.addConverterFactory(new IStringConverterFactory() {
                @Override
                public Class<? extends IStringConverter> getConverter(Class forType) {
                    if (forType.equals(Level.class)) return LogLevelConverter.class;
                    return null;
                }
            });
            c.parse(args);

            if (cla.help) {
                c.usage();
                System.exit(1);
            }

            configLogging(cla.level);

            SchemaDocCli app = new SchemaDocCli();
            String cmd = c.getParsedCommand();
            if ("excel".equals(cmd)) {
                app.buildExcel(cla, execl);
            } else if ("text".equals(cmd)){
                app.buildText(cla, text);
            } else {
                throw new IllegalStateException("unsupported command: " + cmd);
            }
        } catch (ParameterException e) {
            System.err.println(e.getLocalizedMessage());
            System.err.println();
            System.err.println("Use -h or --help to display help screen.");
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void configLogging(Level level) {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        AppenderComponentBuilder appenderBuilder = builder.newAppender("Stdout", "CONSOLE").addAttribute("target",
                ConsoleAppender.Target.SYSTEM_OUT);
        appenderBuilder.add(builder.newLayout("PatternLayout")
                .addAttribute("pattern", "[%t] %-5level: %msg%n%throwable"));
        appenderBuilder.add(builder.newFilter("MarkerFilter", org.apache.logging.log4j.core.Filter.Result.DENY, org.apache.logging.log4j.core.Filter.Result.NEUTRAL)
                .addAttribute("marker", "FLOW"));
        builder.add(appenderBuilder);
        builder.add(builder.newRootLogger(level).add(builder.newAppenderRef("Stdout")));
        Configurator.initialize(builder.build());
    }

    private void buildText(CommandLineArgs cla, CommandText ct) throws Exception {
        ModelBuilder mb = createModelBuilder(ct.schemaFile, cla.lifeCycleNamespaceURI);

        TreeContainer tc = mb.buildAll(null);

        for(Tree tree : tc.trees()) {
            TextViewer viewer = new TextViewer(tree, cla.language);
            System.out.println(viewer.view());
        }
    }

    private void buildExcel(CommandLineArgs cla, CommandExcel ce) throws Exception {
        ModelBuilder mb = createModelBuilder(ce.schemaFile, cla.lifeCycleNamespaceURI);

        ExcelViewer excel = new ExcelViewer();
        excel.write(new File(ce.file), mb.buildAll(null), cla.language);

    }

    private ModelBuilder createModelBuilder(String schemaFile, String lifeCycleNamespaceURI) throws IOException {
        File xmlSchema = new File(schemaFile);
        return new ModelBuilder(xmlSchema, lifeCycleNamespaceURI);
    }

    private Tree buildTreeFromXmlSchema(ModelBuilder mb, String namespaceURI, String rootElementName) throws IOException, ModelBuilderException {
        QName rootElement = new QName(namespaceURI, rootElementName);

        return mb.buildTree(rootElement, null);
    }
}
