package com.meyermt.hack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Reads .asm file and removes comments, whitespaces, and blank lines.
 * Created by michaelmeyer on 2/3/17.
 */
public class AsmFileReader {

    private final Path inputPath;

    /**
     * Instantiates a new .asm file reader.
     *
     * @param inputFile the input file
     */
    public AsmFileReader(String inputFile) {
        this.inputPath = Paths.get(inputFile);
    }

    /**
     * Read and clean list of assembly code lines
     *
     * @return the list of cleaned assembly code lines
     */
    public List<String> readAndClean() {
        return removeWSComments(readFile(inputPath));
    }

    /**
     * Gets input path.
     *
     * @return the input path
     */
    public Path getInputPath() {
        return this.inputPath;
    }

    /*
        Reads the file. Will exit the program if IOException encountered or file is not of .asm extension
    */
    private static List<String> readFile(Path inputPath) {
        // if the filename doesn't have the .asm extension we will exit with helpful message
        if (!inputPath.toString().endsWith(".asm")) {
            System.out.println("Only able to read files with .asm extension. Please rename file and try again.");
            System.exit(1);
        }
        try {
            return Files.readAllLines(inputPath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to read file: " + inputPath);
            System.exit(1);
        }
        // can't actually hit this but needed to compile
        return null;
    }

    /*
        Removes whitespace, blank lines, and comments from code
    */
    private static List<String> removeWSComments(List<String> fileLines) {
        return fileLines.stream()
                // remove all whitespace
                .map(whiteful -> whiteful.replaceAll(" ", ""))
                // if the no comment flag is set, remove comments
                .map(commentful -> commentful.replaceAll("(//.*)", ""))
                // remove tab characters
                .map(tabful -> tabful.replaceAll("\t", ""))
                // remove blank lines after comment removal in case comment was the whole line
                .filter(line -> !line.equals(""))
                .collect(Collectors.toList());
    }
}
