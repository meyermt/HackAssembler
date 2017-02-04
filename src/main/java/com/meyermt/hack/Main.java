package com.meyermt.hack;

import java.util.List;
import java.util.stream.Collectors;

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
        AsmFileReader fileReader = new AsmFileReader(args[0]);
        List<String> cleanFileLines = fileReader.readAndClean();

        MachineCoder coder = new MachineCoder();
        HackParser parser = new HackParser(coder);
        // Use the parser to remove and store symbols first, then stream over and parse to binary strings
        List<String> machineCode = parser.removeAndStoreSymbols(cleanFileLines).stream()
                .map(line -> parser.parseToBinaryString(line))
                .collect(Collectors.toList());

        // write out the binary strings
        HackFileWriter writer = new HackFileWriter(fileReader.getInputPath());
        writer.writeHackFile(machineCode);
    }
}
