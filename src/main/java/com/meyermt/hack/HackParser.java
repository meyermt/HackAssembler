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
    private static final String symbolRegEx = "\\((?<varName>.+)\\)";
    private static final String destCompJumpRegEx = "(?<dest>)=(?<comp>);(?<jump>)";
    private static final String destJumpRegEx = "(?<dest>);(?<jump>)";
    private static final String destCompRegEx = "(?<dest>)=(?<comp>)";
    private static final String nullJmpDest = "000";
    private static final String nonComp = "111110";
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
            String varName = command.substring(1, command.length() - 1);
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

    public List<String> removeAndStoreSymbols(List<String> fileLines) {
        List<String> noSymbolsList = new ArrayList<>();
        int symbolMarker = 0;
        for (int i = 0; i < fileLines.size(); i++) {
            if (fileLines.get(i).matches(symbolRegEx)) {
                String entry = fileLines.get(i).replaceAll(symbolRegEx, "${varName}");
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
            // eg dest=comp;jump
            String dest = instruction.replaceAll(destCompJumpRegEx, "${dest}");
            String comp = instruction.replaceAll(destCompJumpRegEx, "${comp}");
            String jump = instruction.replaceAll(destCompJumpRegEx, "${jump}");
            return coder.getInstructionBin(comp) + coder.getCompBin(comp) + coder.getDestBin(dest) + coder.getJumpBin(jump);
        } else if (hasEquals) {
            // eg dest=comp
            String dest = instruction.replaceAll(destCompRegEx, "${dest}");
            String comp = instruction.replaceAll(destCompRegEx, "${comp}");
            return coder.getInstructionBin(comp) + coder.getCompBin(comp) + coder.getDestBin(dest) + nullJmpDest;
        } else if (hasJump) {
            // eg dest;jump
            String dest = instruction.replaceAll(destJumpRegEx, "${dest}");
            String jump = instruction.replaceAll(destJumpRegEx, "${jump}");
            return "1110" + nonComp + coder.getDestBin(dest) + coder.getJumpBin(jump);
        } else {
            // else it is just a computation
            return coder.getInstructionBin(instruction) + coder.getCompBin(instruction) + nullJmpDest + nullJmpDest;
        }
    }

    private String convertDecimalToBinary(int decimal) {
        String binaryString = Integer.toBinaryString(decimal);
        int bits = Integer.bitCount(decimal);
        int bitsToFill = 16 - bits;
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
