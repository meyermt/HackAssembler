package com.meyermt.hack;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

/**
 * Writes machine code to output file for provided outputPath.
 * Created by michaelmeyer on 2/3/17.
 */
public class HackFileWriter {

    private final Path outputPath;

    /**
     * Instantiates a new Hack file writer. The provided outputPath must be of .asm extension. The writer will write to
     *  the same directory and change this to be a .hack extension.
     *
     * @param outputPath the output path
     */
    public HackFileWriter(Path outputPath) {
        this.outputPath = outputPath;
    }

    /**
     * Write hack file to output file.
     *
     * @param machineCode the machine code to write out
     */
    /*
        Writes output to a file with .hack extension. Will truncate/write over existing .hack files if there
    */
    public void writeHackFile(List<String> machineCode) {
        // create the output file
        String fileName = outputPath.getFileName().toString();
        String outputFileName = fileName.replace("asm", "hack");
        try {
            String outputDir = outputPath.toRealPath(NOFOLLOW_LINKS).getParent().toString();
            Path outputPath = Paths.get(outputDir, outputFileName);
            Files.write(outputPath, machineCode, Charset.defaultCharset());
        } catch (IOException e) {
            System.out.println("Issue encountered writing output file for: " + outputFileName);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
