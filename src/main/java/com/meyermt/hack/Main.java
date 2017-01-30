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
 * Created by michaelmeyer on 1/29/17.
 */
public class Main {

    public static void main(String[] args) {
        Path inputPath = Paths.get(args[0]);
        String fileName = inputPath.getFileName().toString();

        List<String> cleanFileLines = removeWSComments(readFile(inputPath, fileName));
        MachineCoder coder = new MachineCoder();
        HackParser parser = new HackParser(coder);
        List<String> machineOutput = parser.removeAndStoreSymbols(cleanFileLines).stream()
                .peek(line -> System.out.println("line going in is: " + line))
                .map(line -> parser.parseToBinaryString(line))
                .peek(line -> System.out.println("new line is: " + line))
                .collect(Collectors.toList());

        writeOutput(machineOutput, inputPath, fileName);
    }

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
