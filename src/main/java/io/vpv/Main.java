package io.vpv;

import io.vpv.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.impl.util.io.InputStreamSource;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class Main {
    public static void main(final String[] args) throws Exception {

        final CliOptions cliOptions = parseCliInput(args);
        // Load the BPMN 2.0 XML file
        final String inputFileName = cliOptions.input();
        final String targetFileName = cliOptions.output();
        log.info("Converting {} to {}", inputFileName, targetFileName);
        final long start = System.currentTimeMillis();

        final InputStream imageStream = parseXMLBpmFileToImageStream(inputFileName);

        // Save the PNG image to a file
        saveImageFile(targetFileName, imageStream);

        final long end = System.currentTimeMillis();
        log.info("Conversion took {}", TimeUtil.formatElapsedTime((end-start)));

    }

    private static void saveImageFile(final String targetFileName, final InputStream inputStream) throws IOException {
        final FileOutputStream fos = new FileOutputStream(targetFileName);
        fos.write(inputStream.readAllBytes());
        fos.close();
    }

    private static InputStream parseXMLBpmFileToImageStream(final String inputFileName) throws FileNotFoundException {
        final BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(new InputStreamSource(new FileInputStream(inputFileName)), true, true);
        // Generate the PNG image
        final ProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
        final InputStream pngBytes = diagramGenerator.generatePngDiagram(bpmnModel, false);
        return pngBytes;
    }

    private static CliOptions parseCliInput(final String[] args) {
        final Options options = new Options();

        final Option input = new Option("i", "input", true, "input file path");
        input.setRequired(true);
        options.addOption(input);

        final Option output = new Option("o", "output", true, "output file");
        output.setRequired(true);
        options.addOption(output);

        final CommandLineParser parser = new DefaultParser();
        final HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;//not a good practice, it serves it purpose

        try {
            cmd = parser.parse(options, args);
        } catch (final ParseException e) {
            log.error("Problem while parsing arguments {}", e.getMessage());
            formatter.printHelp("bpmn-java-cli", options);

            System.exit(1);
        }

        return new CliOptions(cmd.getOptionValue("input"), cmd.getOptionValue("output"));
    }


}