package com.meyermt.hack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The HackParser handles parsing the assembly code and uses the MachineCoder to help figure out what the machine code
 * translation is. The parser stores a map of variables and their memory addresses as well as symbols and their jump to
 * addresses.
 * Created by michaelmeyer on 1/29/17.
 */
public class HackParser {

    private Map<String, Integer> memoryMap = new HashMap<>();
    private static final String SYMBOL_REGEX = "\\((?<varName>.+)\\)";
    private static final String DEST_COMP_JUMP_REGEX = "(?<dest>.+)=(?<comp>.+);(?<jump>.+)";
    private static final String COMP_JUMP_REGEX = "(?<comp>.+);(?<jump>.+)";
    private static final String DEST_COMP_REGEX = "(?<dest>.+)=(?<comp>.+)";
    private int storageCounter = 16;
    private MachineCoder coder;

    /**
     * Instantiates a new Hack parser. A MachineCoder must be provided to a parser at instantiation.
     *
     * @param coder the coder
     */
    public HackParser(MachineCoder coder) {
        initMemoryMap();
        this.coder = coder;
    }

    /**
     * Parse to binary string. After symbols have been accounted for, this method can be used to translate the assembly
     * code into a binary code.
     *
     * @param command the command
     * @return the binary string
     */
    public String parseToBinaryString(String command) {
        if (command.matches("@[0-9]+")) {
            // numeric command, so get the binary translation
            Integer number = Integer.parseInt(command.substring(1, command.length()));
            return convertDecimalToBinary(number);
        } else if (command.startsWith("@")) {
            // else we have a variable, either stored or needing storing
            String varName = command.substring(1, command.length());
            int memoryValue = memoryMap.getOrDefault(varName, storageCounter);
            if (memoryValue == storageCounter) {
                memoryMap.put(varName, storageCounter);
                storageCounter++;
            }
            return convertDecimalToBinary(memoryValue);
        } else {
            // else it is an instruction
            return parseInstruction(command);
        }
    }

    /**
     * Given a list of code that still has symbols, this method will remove and store symbols in a map in the parser.
     * It is important that this method is invoked before any calls to parseToBinaryString
     *
     * @param fileLines the file lines to have symbols removed from
     * @return the symbol-free list
     */
    public List<String> removeAndStoreSymbols(List<String> fileLines) {
        List<String> noSymbolsList = new ArrayList<>();
        // keep track of a marker that marks the "cleaned" list's index position
        int symbolMarker = 0;
        for (int i = 0; i < fileLines.size(); i++) {
            if (fileLines.get(i).matches(SYMBOL_REGEX)) {
                // add the symbol to our memory map with value of symbolMarker for using in code later
                String entry = fileLines.get(i).replaceAll(SYMBOL_REGEX, "${varName}");
                memoryMap.put(entry, symbolMarker);
            } else {
                // keeping this code, so add it and increment the symbol marker
                noSymbolsList.add(fileLines.get(i));
                symbolMarker++;
            }
        }
        return noSymbolsList;
    }

    /*
        Using regex's for the type of instruction, this will parse an instruction and ask the MachineCoder to give its
        binary translation
     */
    private String parseInstruction(String instruction) {
        boolean hasEquals = instruction.contains("=");
        boolean hasJump = instruction.contains(";");
        if (hasEquals && hasJump) {
            // i.e., dest=comp;jump
            String dest = instruction.replaceAll(DEST_COMP_JUMP_REGEX, "${dest}");
            String comp = instruction.replaceAll(DEST_COMP_JUMP_REGEX, "${comp}");
            String jump = instruction.replaceAll(DEST_COMP_JUMP_REGEX, "${jump}");
            return coder.getInstructionBin(comp) + coder.getCompBin(comp) + coder.getDestBin(dest) + coder.getJumpBin(jump);
        } else if (hasEquals) {
            // i.e., dest=comp
            String dest = instruction.replaceAll(DEST_COMP_REGEX, "${dest}");
            String comp = instruction.replaceAll(DEST_COMP_REGEX, "${comp}");
            System.out.println("looking up dest: " + dest);
            System.out.println("looking up comp: " + comp);
            return coder.getInstructionBin(comp) + coder.getCompBin(comp) + coder.getDestBin(dest) + MachineCoder.NULL_BIN;
        } else if (hasJump) {
            // i.e., comp;jump
            String comp = instruction.replaceAll(COMP_JUMP_REGEX, "${comp}");
            String jump = instruction.replaceAll(COMP_JUMP_REGEX, "${jump}");
            return MachineCoder.A_COMP_BIN + coder.getCompBin(comp) + MachineCoder.NULL_BIN + coder.getJumpBin(jump);
        } else {
            // else it is just a computation
            return coder.getInstructionBin(instruction) + coder.getCompBin(instruction) + MachineCoder.NULL_BIN + MachineCoder.NULL_BIN;
        }
    }

    /*
        Helper method to convert a decimal value to a 16 bit binary string
     */
    private String convertDecimalToBinary(int decimal) {
        String binaryString = Integer.toBinaryString(decimal);
        int bitsToFill = 16 - binaryString.length();
        String bitString = "";
        for (int i = 0; i < bitsToFill; i++) {
            bitString = bitString + "0";
        }
        return bitString.concat(binaryString);
    }

    /*
        Initial loading of the memory map with constant HACK memory values
     */
    private void initMemoryMap() {
        memoryMap.put("R0", 0);
        memoryMap.put("R1", 1);
        memoryMap.put("R2", 2);
        memoryMap.put("R3", 3);
        memoryMap.put("R4", 4);
        memoryMap.put("R5", 5);
        memoryMap.put("R6", 6);
        memoryMap.put("R7", 7);
        memoryMap.put("R8", 8);
        memoryMap.put("R9", 9);
        memoryMap.put("R10", 10);
        memoryMap.put("R11", 11);
        memoryMap.put("R12", 12);
        memoryMap.put("R13", 13);
        memoryMap.put("R14", 14);
        memoryMap.put("R15", 15);
        memoryMap.put("SP", 0);
        memoryMap.put("LCL", 1);
        memoryMap.put("ARG", 2);
        memoryMap.put("THIS", 3);
        memoryMap.put("THAT", 4);
        memoryMap.put("SCREEN", 16384);
        memoryMap.put("KBD", 24576);
    }
}
