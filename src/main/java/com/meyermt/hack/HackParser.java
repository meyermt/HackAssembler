package com.meyermt.hack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
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

    public HackParser(MachineCoder coder) {
        initMemoryMap();
        this.coder = coder;
    }

    public String parseToBinaryString(String command) {
        if (command.matches("@[0-9]+")) {
            // numeric command, so get the binary
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
            System.out.println("Memory value of " + varName + " is " + memoryValue);
            return convertDecimalToBinary(memoryValue);
        } else {
            // else it is an instruction
            return parseInstruction(command);
        }
    }

    public List<String> removeAndStoreSymbols(List<String> fileLines) {
        List<String> noSymbolsList = new ArrayList<>();
        int symbolMarker = 0;
        for (int i = 0; i < fileLines.size(); i++) {
            if (fileLines.get(i).matches(SYMBOL_REGEX)) {
                String entry = fileLines.get(i).replaceAll(SYMBOL_REGEX, "${varName}");
                memoryMap.put(entry, symbolMarker);
            } else {
                noSymbolsList.add(fileLines.get(i));
                symbolMarker++;
            }
        }
        return noSymbolsList;
    }

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

    private String convertDecimalToBinary(int decimal) {
        String binaryString = Integer.toBinaryString(decimal);
        int bitsToFill = 16 - binaryString.length();
        String bitString = "";
        for (int i = 0; i < bitsToFill; i++) {
            bitString = bitString + "0";
        }
        return bitString.concat(binaryString);
    }

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
