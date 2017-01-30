# Hack Assembler

This program will take an input file of HACK assembly code and output a compiled .hack file that is ready to run in a
HACK CPUEmulator.

## How to Run the Program

A pre-requisite to running this program is having Java 8 installed on whichever system it runs on. After ensuring it is
installed, please follow these instructions for compiling and running the program.

1. Unzip the contents of the .zip file
2. Enter `javac -d bin src/main/java/com/meyermt/hack/*.java` to compile the program
3. Enter `java -cp bin com.meyermt.hack.Main <filename.asm>` to run the program. Please note that the file MUST have the
".asm" extension in order to be run through the program. You can use absolute or relative paths to specify the location
of the input file.
4. The program will produce a file with a ".hack" extension in the same directoy as the input file.
5. Load the output file into the CPUEmulator and enjoy.