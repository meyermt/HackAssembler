package com.meyermt.hack;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

/**
 * Main driver for the HACK Assembler program. Accepts one file with .asm extension as input and outputs a machine language
 * output file with a .hack extension.
 * Created by michaelmeyer on 1/29/17.
 */
public class Main {

    /**
     * The entry point of application. The main method will drive the program through to completion. It works with a
     * HackParser (which in turn will work with a MachineCoder) to translate human readable HACK assembly code to
     * machine language byte code.
     *
     * @param args the input arguments. Must be an assembly language file with .asm extension.
     */
    public static void main(String[] args) {
        // read in the file and remove whitespace
        Path inputPath = Paths.get(args[0]);
        String fileName = inputPath.getFileName().toString();
        List<String> cleanFileLines = removeWSComments(readFile(inputPath, fileName));

        MachineCoder coder = new MachineCoder();
        HackParser parser = new HackParser(coder);
        // Use the parser to remove and store symbols first, then stream over and parse to binary strings
        List<String> machineOutput = parser.removeAndStoreSymbols(cleanFileLines).stream()
                .map(line -> parser.parseToBinaryString(line))
                .collect(Collectors.toList());

        // write out the binary strings
        writeOutput(machineOutput, inputPath, fileName);
    }

    /*
        Reads the file. Will exit the program if IOException encountered or file is not of .asm extension
     */
    private static List<String> readFile(Path inputPath, String fileName) {
        // if the filename doesn't have the .in extension we will exit with helpful message
        if (!fileName.endsWith(".asm")) {
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

    /*
        Writes output to a file with .hack extension. Will truncate/write over existing .hack files if there
     */
    private static void writeOutput(List<String> machineOutput, Path inputPath, String fileName) {
        // create the output file
        try {
            String outputFileName = fileName.replace("asm", "hack");
            String outputDir = inputPath.toRealPath(NOFOLLOW_LINKS).getParent().toString();
            Path outputPath = Paths.get(outputDir, outputFileName);
            Files.write(outputPath, machineOutput, Charset.defaultCharset());
        } catch (IOException e) {

        }
    }

}
