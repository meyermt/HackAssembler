package com.meyermt.hack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by michaelmeyer on 1/29/17.
 */
public class MachineCoder {

    private Map<String, String> destMap = new HashMap<>();
    private Map<String, String> compMap = new HashMap<>();
    private Map<String, String> jumpMap = new HashMap<>();

    // code values of destinations and a few computations
    public static final String M = "M";
    public static final String D = "D";
    public static final String MD = "MD";
    public static final String A = "A";
    public static final String AM = "AM";
    public static final String AD = "AD";
    public static final String AMD = "AMD";

    // code values of jumps
    public static final String JGT = "JGT";
    public static final String JEQ = "JEQ";
    public static final String JGE = "JGE";
    public static final String JLT = "JLT";
    public static final String JNE = "JNE";
    public static final String JLE = "JLE";
    public static final String JMP = "JMP";

    // binary mappings for destinations and jumps
    public static final String M_JGT_BIN = "001";
    public static final String D_JEQ_BIN = "010";
    public static final String MD_JGE_BIN = "011";
    public static final String A_JLT_BIN = "100";
    public static final String AM_JNE_BIN = "101";
    public static final String AD_JLE_BIN = "110";
    public static final String AMD_JMP_BIN = "111";
    public static final String NULL_BIN = "000";

    // code values of computations
    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String NEG_ONE = "-1";
    public static final String NOT_D = "!D";
    public static final String NOT_A = "!A";
    public static final String NOT_M = "!M";
    public static final String NEG_D = "-D";
    public static final String NEG_A = "-A";
    public static final String NEG_M = "-M";
    public static final String D_PLUS_1 = "D+1";
    public static final String A_PLUS_1 = "A+1";
    public static final String M_PLUS_1 = "M+1";
    public static final String D_MINUS_1 = "D-1";
    public static final String A_MINUS_1 = "A-1";
    public static final String M_MINUS_1 = "M-1";
    public static final String D_PLUS_A = "D+A";
    public static final String D_PLUS_M = "D+M";
    public static final String D_MINUS_A = "D-A";
    public static final String D_MINUS_M = "D-M";
    public static final String A_MINUS_D = "A-D";
    public static final String M_MINUS_D = "M-D";
    public static final String D_AND_A = "D&A";
    public static final String D_AND_M = "D&M";
    public static final String D_OR_A = "D|A";
    public static final String D_OR_M = "D|M";

    // binary mappings for computations
    public static final String ZERO_BIN = "101010";
    public static final String ONE_BIN = "111111";
    public static final String NEG_ONE_BIN = "111010";
    public static final String D_BIN = "001100";
    public static final String A_M_BIN = "110000";
    public static final String NOT_D_BIN = "001101";
    public static final String NOT_A_M_BIN = "110001";
    public static final String NEG_D_BIN = "001111";
    public static final String NEG_A_M_BIN = "110011";
    public static final String D_PLUS_ONE_BIN = "011111";
    public static final String A_M_PLUS_ONE_BIN = "110111";
    public static final String D_MINUS_ONE_BIN = "001110";
    public static final String A_M_MINUS_ONE_BIN = "110010";
    public static final String D_PLUS_A_M_BIN = "000010";
    public static final String D_MINUS_A_M_BIN = "010011";
    public static final String A_M_MINUS_D_BIN = "000111";
    public static final String D_AND_A_M_BIN = "000000";
    public static final String D_OR_A_M_BIN = "010101";

    public static final String M_COMP_BIN = "1111";
    public static final String A_COMP_BIN = "1110";

    public MachineCoder() {
        loadDestMap();
        loadCompmap();
        loadJumpMap();
    }

    public String getCompBin(String computation) {
        return compMap.get(computation);
    }

    public String getDestBin(String destination) {
        return destMap.get(destination);
    }

    public String getJumpBin(String jump) {
        return jumpMap.get(jump);
    }

    public String getInstructionBin(String computation) {
        if (computation.equals(M) || computation.equals(NOT_M) || computation.equals(NEG_M)
                || computation.equals(M_PLUS_1) || computation.equals(M_MINUS_1) || computation.equals(D_PLUS_M)
                || computation.equals(D_MINUS_M) || computation.equals(M_MINUS_D) || computation.equals(D_AND_M)
                || computation.equals(D_OR_M)) {
            return M_COMP_BIN;
        } else {
            return A_COMP_BIN;
        }
    }

    private void loadDestMap() {
        destMap.put(M, M_JGT_BIN);
        destMap.put(D, D_JEQ_BIN);
        destMap.put(MD, MD_JGE_BIN);
        destMap.put(A, A_JLT_BIN);
        destMap.put(AM, AM_JNE_BIN);
        destMap.put(AD, AD_JLE_BIN);
        destMap.put(AMD, AMD_JMP_BIN);
        destMap.put(ZERO, NULL_BIN);
    }

    private void loadJumpMap() {
        jumpMap.put(JGT, M_JGT_BIN);
        jumpMap.put(JEQ, D_JEQ_BIN);
        jumpMap.put(JGE, MD_JGE_BIN);
        jumpMap.put(JLT, A_JLT_BIN);
        jumpMap.put(JNE, AM_JNE_BIN);
        jumpMap.put(JLE, AD_JLE_BIN);
        jumpMap.put(JMP, AMD_JMP_BIN);
    }

    private void loadCompmap() {
        compMap.put(ZERO, ZERO_BIN);
        compMap.put(ONE, ONE_BIN);
        compMap.put(NEG_ONE, NEG_ONE_BIN);
        compMap.put(D, D_BIN);
        compMap.put(A, A_M_BIN);
        compMap.put(M, A_M_BIN);
        compMap.put(NOT_D, NOT_D_BIN);
        compMap.put(NOT_A, NOT_A_M_BIN);
        compMap.put(NOT_M, NOT_A_M_BIN);
        compMap.put(NEG_D, NEG_D_BIN);
        compMap.put(NEG_A, NEG_A_M_BIN);
        compMap.put(NEG_M, NEG_A_M_BIN);
        compMap.put(D_PLUS_1, D_PLUS_ONE_BIN);
        compMap.put(A_PLUS_1, A_M_PLUS_ONE_BIN);
        compMap.put(M_PLUS_1, A_M_PLUS_ONE_BIN);
        compMap.put(D_MINUS_1, D_MINUS_ONE_BIN);
        compMap.put(A_MINUS_1, A_M_MINUS_ONE_BIN);
        compMap.put(M_MINUS_1, A_M_MINUS_ONE_BIN);
        compMap.put(D_PLUS_A, D_PLUS_A_M_BIN);
        compMap.put(D_PLUS_M, D_PLUS_A_M_BIN);
        compMap.put(D_MINUS_A, D_MINUS_A_M_BIN);
        compMap.put(D_MINUS_M, D_MINUS_A_M_BIN);
        compMap.put(A_MINUS_D, A_M_MINUS_D_BIN);
        compMap.put(M_MINUS_D, A_M_MINUS_D_BIN);
        compMap.put(D_AND_A, D_AND_A_M_BIN);
        compMap.put(D_AND_M, D_AND_A_M_BIN);
        compMap.put(D_OR_A, D_OR_A_M_BIN);
        compMap.put(D_OR_M, D_OR_A_M_BIN);
    }
}
